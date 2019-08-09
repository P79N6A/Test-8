/**
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue.core;

import com.tydic.beijing.bvalue.biz.TradeExchangeOper;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.dao.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SKU订单后台处理<br/>
 * 1.每个log_trade_sku都要单独提交，最后更改log_trade状态，移到log_trade_his，</br>
 * 保证log_trade_sku发生异常，下次可以继续处理</br>
 *
 * @author Bradish7Y
 * @since JDK 1.7
 */
public class SKUProcess implements Runnable {

    private static final Logger log = Logger.getLogger(SKUProcess.class);

    @Autowired
    private TradeExchangeOper oper;

    private static AtomicBoolean status = new AtomicBoolean(true);// 状态
    private static final String DEPOSIT = "0";// 存款
    private static final String REFUND = "1";// 取款
    private static final String TRADE_TYPE_CODE_101 = "101";// 购物
    private static final String TRADE_TYPE_CODE_102 = "201";// 退货

    private int channel;// mode(user_id, channel)
    private int remainder;// mode(user_id, channel)=remainder
    private int sleepInterval;


    
    
    
    @Override
    public void run() {
        log.info("SKU-run channel[" + channel + "]");
        log.info("SKU-run remainder[" + remainder + "]");
		while (status.get()) {
            try {
                Status ret = null;         
        		// 分通道取表数据
                List<LogTrade> logTrades = oper.getLogTradeByMod(channel, remainder);
                if (logTrades.isEmpty() == false) {

                    // 按订单完成时间排序
                    this.sortLogTrade(logTrades);

                    if (log.isDebugEnabled()) {
                        for (LogTrade l : logTrades) {
                            log.debug("SKU-run sorted by exp_date:" + l.toString());
                        }
                    }
                    SimpleDateFormat dateformat=new SimpleDateFormat("yyyyMMddHHmmss");
            		String processTime=dateformat.format(new Date());//
                    for (LogTrade logTrade : logTrades) {
                        String userId = logTrade.getUser_id();
                        String tradeId = logTrade.getTrade_id();

                        String orderTypeLogTrade = logTrade.getOrder_type();
                        String orderNO = logTrade.getOrder_no();

                        if ("1".equals(orderTypeLogTrade)) {// 购物
                            if (oper.isDuplicated(userId, orderNO, "101", logTrade)) {
                                log.error("该订单已经被处理过，为重复订单，订单号[" + orderNO + "]");

                                //更新logtrade表状态为4
                                logTrade.setProcess_tag(4);
                                oper.updateLogTradeProcessTag(logTrade);
                                continue;
                            }
                        } else {// 退货
                            if (oper.isDuplicated(userId, orderNO, "201", logTrade)) {
                                logTrade.setProcess_tag(5);
                                oper.updateLogTradeProcessTag(logTrade);
                                continue;
                            }
                        }


                        // 先判断是否开户
                        // 检查是否开户，没开就开，然后成功
                        if (!oper.ifAlreadyExisted(userId)) {
                            log.info("SKU-run jdpin[" + logTrade.getReserve_c1() + "]开户");
                            oper.updateOpenInfoUser(logTrade.getReserve_c1(), "109",
                                    DateUtil.getSystemTime());
                        }
                        // 查询log_trade_sku
                        List<LogTradeSku> logTradeSkus = oper.getLogTradeSkuByTradeId(userId,
                                tradeId);
                        // trade_id不存在表log_trade_sku
                        if (logTradeSkus.isEmpty()) {
                            log.info("SKU-run trade_id[" + tradeId + "] 不存在于log_trade_sku");
                            // 完成，可能的情况是，log_trade_sku处理成功了，但log_trade改状态失败了，不影响结果
                            log.info("SKU-run 删除log_trade, 插入log_trade_his");
                            LogTradeHis his = this.swapTradeAndHis(logTrade);
                            his.setRemark("SKU trade_id不存在");
                            // 搬数据到his
                            log.info("SKU-run 更新redis");
                            oper.updateSkuProcessForLogTrade(logTrade, his);
                            continue;
                        }

                        boolean notExists = false;
                        for (LogTradeSku tmplts : logTradeSkus) {
                            //退货还要校验用户的原始订单及原始sku是否存在
                            String orgOrderNO = tmplts.getOrg_order_no();
                            if ("2".equals(orderTypeLogTrade) && oper.isOrgNoNotFound(userId, orgOrderNO, "101", logTrade)) {
                                notExists = true;
                            }
                        }
                        
                        if (notExists) {
                            logTrade.setProcess_tag(5);
                            oper.updateLogTradeProcessTag(logTrade);
                            continue;
                        }



                        for (LogTradeSku logTradeSku : logTradeSkus) {
                            // 订单类型
                            String orderType = logTradeSku.getOrder_type();
                                if (orderType.equals("1")) {// 购物
                                    log.debug("SKU-run 购物order_type[" + orderType + "] "
                                            + logTradeSku.toString());
                                    ret = this.deposit(logTrade, logTradeSku);
                                } else if (orderType.equals("2")) {// 退货
                                    log.debug("SKU-run 退货order_type[" + orderType + "] "
                                            + logTradeSku.toString());
                                    ret = this.refund(logTrade, logTradeSku,processTime);
                                } else if (orderType.equals("3")) {// 换货
                                    log.debug("SKU-run order_type[" + orderType + "] "
                                            + logTradeSku.toString());
                                    // TODO 保留
                                } else {
                                    log.info("SKU-run trade_id[" + tradeId + "]，order_type["
                                            + orderType + "] 取值错误");
                                    continue;
                                }
//                            }

                        }
                        log.info("SKU-run 删除log_trade, 插入log_trade_his");
                        LogTradeHis his = this.swapTradeAndHis(logTrade);

                        his.setProcess_tag(2);
                        his.setRemark(ret.getRemark());
                        // 搬数据到his
                        log.info("SKU-run 更新redis");
                        oper.updateSkuProcessForLogTrade(logTrade, his);
                    }

                } else {
                    log.info("SKU-run ############log_trade暂无数据，sleep[" + sleepInterval
                            + "]#############");
                    try {
                        TimeUnit.SECONDS.sleep(sleepInterval);
                    } catch (InterruptedException e) {
                    }
                }
            } catch (Exception e) {
                log.error("SKU-run 处理过程中有异常，跳过，下次处理，" + e.toString());
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e_e) {
                }
            }
        }

    }

    /**
     * deposit:购物.<br/>
     *
     * @param logTrade
     * @param logTradeSku
     * @throws ParseException
     */
    public Status deposit(final LogTrade logTrade, final LogTradeSku logTradeSku)
            throws ParseException {
        String userId = logTrade.getUser_id();
        String org_order_number=logTrade.getOrder_no();
        List<InfoPayBalanceSku> infoPayBalanceSkus=new ArrayList<InfoPayBalanceSku>();
       
        //sku赠时，获取临时表中的该用户该订单的记录
        infoPayBalanceSkus=oper.getInfoPayBalanceSkuByUserId_0(userId,org_order_number);
        int infoPayBlanceSkuInsertFlag=0;
        InfoPayBalanceSku infoPayBalanceSku=new InfoPayBalanceSku();
        if (infoPayBalanceSkus.isEmpty() || infoPayBalanceSkus.size()==0) {
        	infoPayBalanceSku.setOrg_order_id(org_order_number);
        	infoPayBalanceSku.setProcess_tag(0);
        	infoPayBalanceSku.setUser_id(userId);
        	infoPayBalanceSku.setMonth_flag(0);
        	infoPayBlanceSkuInsertFlag=1;
		}else {
			infoPayBalanceSku=infoPayBalanceSkus.get(0);
		}
        
     // 赠送的总B值
        long depositValue = logTradeSku.getPurchase_quantity() * logTradeSku.getB_value();
        log.info("SKU-deposit 总赠送B值=" + depositValue);
        
        infoPayBalanceSku.setBalance(depositValue);
        LogTradeSkuHis logSkuHis = swapTradeSkuAndHis(logTradeSku);
        logSkuHis.setProcess_tag(2);
        logSkuHis.setBalance(depositValue);
        logTrade.setBalance(logTrade.getBalance() +depositValue);
        oper.updateSkuProcessDepositType(infoPayBalanceSku,infoPayBlanceSkuInsertFlag,logSkuHis,logTradeSku);
        return new Status(0, "购物OK");
    }

    
    /**
     * refund:退货.<br/>
     *
     * @param logTrade
     * @param logTradeSku
     * @throws ParseException
     */
    public Status refund(final LogTrade logTrade, final LogTradeSku logTradeSku,String processTime) throws ParseException {
        String userId = logTrade.getUser_id();
        String orgorderid=logTradeSku.getOrg_order_no();
        // 根据订单号查找订单完成时间
        String monthEnd10="20161031235959";
        log.debug("SKU-refund 查找订单完成时间、交易ID");
        List<LogTradeHis> lTradeHisList = oper.getLogTradeHis(userId,
                logTradeSku.getOrg_order_no(), TRADE_TYPE_CODE_101);
        String orgTradeId = null;
        String completeTime = null;// 购物时的完成时间
        // 上次购物所赠b值，logTrade.balance + logTrade.overtop
        long lastDeposit = 0;
        if (lTradeHisList.isEmpty()) {
            log.debug("SKU-refund log_trade_sku_his没有查到");
            List<LogTrade> lTradeList = oper.getLogTrade(userId, logTradeSku.getOrg_order_no(),
                    TRADE_TYPE_CODE_101);
            if (lTradeList.isEmpty()) {
                log.error("SKU-refund 原始订单号不存在，不退货");
                LogTradeSkuHis tmp = this.swapTradeSkuAndHis(logTradeSku);
                tmp.setReserve_1("原始订单号不存在，不退货");
                oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
                return new Status(-1, "原始订单号不存在，不退货");
            } else {
                LogTrade tmp = lTradeList.get(0);
                orgTradeId = tmp.getTrade_id();
                completeTime = tmp.getOrder_completion_time();
                lastDeposit += tmp.getBalance();
                lastDeposit += tmp.getOvertop_value();
            }
        } else {
            LogTradeHis tmp = lTradeHisList.get(0);
            orgTradeId = tmp.getTrade_id();
            completeTime = tmp.getOrder_completion_time();
            lastDeposit += tmp.getBalance();
            lastDeposit += tmp.getOvertop_value();

        }

        if (lastDeposit == 0) {
            log.info("SKU-refundNotRel 上次购物未赠B，本次不扣B");
            LogTradeSkuHis tmp = this.swapTradeSkuAndHis(logTradeSku);
            tmp.setReserve_1("退货SKU id， 不存在，不退货");
            oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
            return new Status(0, "上次购物未赠B，本次不扣B");
        }
        log.debug("SKU-refund orgTradeId[" + orgTradeId + "], completeTime[" + completeTime + "]");
        long unitBPrice = 0;// 单价，原始报文的
        long quantity = 0;
        String skuId = logTradeSku.getSku_id();
        log.debug("SKU-refund 查找退货单价、数量");
        // 查询退货单价、数量
        LogTradeSkuHis iTradeSkuHis = oper.getBValueOfLogTradeSkuHis(userId, orgTradeId, skuId);
        if (iTradeSkuHis == null) {
            LogTradeSku iTradeSku = oper.getBValueOfLogTradeSku(userId, orgTradeId, skuId);
            if (iTradeSku == null) {
                log.error("退货SKU id， 不存在，不退货");

                LogTradeSkuHis tmp = this.swapTradeSkuAndHis(logTradeSku);
                tmp.setReserve_1("退货SKU id， 不存在，不退货");
                oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
                return new Status(-1, "SKU id， 不存在，不退货");
            } else {
                unitBPrice = iTradeSku.getB_value();
                quantity = iTradeSku.getPurchase_quantity();
            }
        } else {
            unitBPrice = iTradeSkuHis.getB_value();
            quantity = iTradeSkuHis.getPurchase_quantity();
        }

        log.debug("SKU-refund 原始退货数量：[" + quantity + "],退货订单数量：["
                + logTradeSku.getPurchase_quantity() + "]");
        log.debug("SKU-refund 原始退货单价：[" + unitBPrice + "],退货订单单价：["
                + logTradeSku.getPurchase_quantity() + "]");

        // 校验
        if (logTradeSku.getPurchase_quantity() <= 0) {
            log.error("SKU-refund 退货订单数量不能等于0");

            LogTradeSkuHis tmp = this.swapTradeSkuAndHis(logTradeSku);
            tmp.setReserve_1("退货订单数量不能等于0");
            oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
            return new Status(-1, "退货订单数量不能等于0");
        }
        // 比数量
        if (logTradeSku.getPurchase_quantity() > quantity) {
            log.error("SKU-refund 退货订单数量大于原始订单数量，不退货");

            LogTradeSkuHis tmp = this.swapTradeSkuAndHis(logTradeSku);
            tmp.setReserve_1("退货订单数量大于原始订单数量，不退货");
            oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
            return new Status(-1, "退货订单数量大于原始订单数量，不退货");
        }
        // 退货的总B值
        long refundValue = logTradeSku.getPurchase_quantity() * unitBPrice;
        log.debug("SKU-refund 退货数量：[" + logTradeSku.getPurchase_quantity() + "]");
        try {
            int distance = DateUtil.getEndReduceStart(completeTime, DateUtil.getSystemTime());
            log.debug("SKU-refund 比较是当月还是往月退货distance[" + distance + "]");
            if (distance > 1) {
                log.info("SKU-refund userId[" + userId + "], 大于1个月不处理退货");
                LogTradeSkuHis tmp = this.swapTradeSkuAndHis(logTradeSku);
                tmp.setReserve_1("大于1个月不处理退货");
                oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
                return new Status(0, "大于1个月不处理退货");
            }
        } catch (ParseException e) {
            log.error("SKU-refund 订单完成时间格式不正确，complateTime[" + completeTime + "]");
            LogTradeSkuHis tmp = this.swapTradeSkuAndHis(logTradeSku);
            tmp.setReserve_1("订单完成时间格式不正确，complateTime[" + completeTime + "]");
            oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
            return new Status(-1, "SKU-refund 订单完成时间格式不正确");
        }

        
        List<InfoPayBalanceSku> infoPayBalanceSkus0=new ArrayList<InfoPayBalanceSku>();
        List<InfoPayBalanceSku> infoPayBalanceSkus1=new ArrayList<InfoPayBalanceSku>();
        //sku赠时，获取临时表中的该用户该订单的记录
        infoPayBalanceSkus0=oper.getInfoPayBalanceSkuByUserId_0(userId,orgorderid);
        infoPayBalanceSkus1=oper.getInfoPayBalanceSkuByUserId_1(userId,orgorderid);
        int infoPayBlanceSkuInsertFlag=0;
        InfoPayBalanceSku infoPayBalanceSku=new InfoPayBalanceSku();
        if (infoPayBalanceSkus0.isEmpty() || infoPayBalanceSkus0.size()==0) {
        	if (infoPayBalanceSkus1!=null && infoPayBalanceSkus1.size()==1) {
				infoPayBalanceSku.setOrg_order_id(orgorderid);
				infoPayBalanceSku.setUser_id(userId);
				infoPayBalanceSku.setProcess_tag(0);
				infoPayBalanceSku.setMonth_flag(1);
				infoPayBlanceSkuInsertFlag=1;
        	}else if (processTime.compareTo(monthEnd10)<0) {
        		log.debug("处理时间小于10月底"+processTime);
        		if (!lTradeHisList.isEmpty()) {
        			int distance = DateUtil.getEndReduceStart(completeTime, DateUtil.getSystemTime());
					if (distance==1) {
						infoPayBalanceSku.setOrg_order_id(orgorderid);
						infoPayBalanceSku.setUser_id(userId);
						infoPayBalanceSku.setProcess_tag(0);
						infoPayBalanceSku.setMonth_flag(1);
						infoPayBlanceSkuInsertFlag=1;
					}else if (distance==0) {
						infoPayBalanceSku.setOrg_order_id(orgorderid);
						infoPayBalanceSku.setUser_id(userId);
						infoPayBalanceSku.setProcess_tag(0);
						infoPayBalanceSku.setMonth_flag(0);
						infoPayBlanceSkuInsertFlag=1;
					}
				} 
			}else{
				log.error("SKU-refund 没有找到用户sku赠成功的记录，不退货");
	            LogTradeSkuHis tmp = this.swapTradeSkuAndHis(logTradeSku);
	            tmp.setReserve_1("没有找到用户sku赠成功的记录，不退货");
	            oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
	            return new Status(-1, "没有找到用户sku赠成功的记录，不退货");	
			} 
		}else {
			infoPayBalanceSku=infoPayBalanceSkus0.get(0);
		}
        
        infoPayBalanceSku.setBalance(-refundValue);
        LogTradeSkuHis logSkuHis = swapTradeSkuAndHis(logTradeSku);
        logSkuHis.setBalance(-refundValue);
        logSkuHis.setOvertop_value(0);
        logSkuHis.setProcess_tag(2);
        // 退货B值
        logTrade.setBalance(logTrade.getBalance() - refundValue);
        
        oper.updateSkuProcessDepositType(infoPayBalanceSku, infoPayBlanceSkuInsertFlag, logSkuHis, logTradeSku);
        log.debug("SKU-refund 退货完成！！！！！");
        return new Status(0, "退货OK");
    }


    /**
     * sort:按订单完成时间排序.<br/>
     *
     * @param logTrades
     */
    public void sortLogTrade(final List<LogTrade> logTrades) {
        Collections.sort(logTrades, new Comparator<LogTrade>() {

            @Override
            public int compare(LogTrade o1, LogTrade o2) {
                int ret = o1.getOrder_completion_time().compareTo(o2.getOrder_completion_time());
                if (0 == ret) {
                    return 0;
                } else if (ret > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

    }

    /**
     * sortLogTradeSku:logTradeSku排序.<br/>
     *
     * @param logTradeSkus
     */
    public void sortLogTradeSku(final List<LogTradeSku> logTradeSkus) {
        Collections.sort(logTradeSkus, new Comparator<LogTradeSku>() {

            @Override
            public int compare(LogTradeSku o1, LogTradeSku o2) {
                int ret = o1.getProcess_time().compareTo(o2.getProcess_time());
                if (0 == ret) {
                    return 0;
                } else if (ret > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

    }

   
   

    /**
     * swapTradeAndHis:交换his表.<br/>
     *
     * @param sku
     * @return
     */
    public LogTradeSkuHis swapTradeSkuAndHis(final LogTradeSku sku) {
        LogTradeSkuHis ret = new LogTradeSkuHis();
        ret.setTrade_id(sku.getTrade_id());
        ret.setTrade_type_code(sku.getTrade_type_code());
        ret.setUser_id(sku.getUser_id());
        ret.setPartition_id(sku.getPartition_id());
        ret.setOrder_no(sku.getOrder_no());
        ret.setOrder_type(sku.getOrder_type());
        ret.setSub_order_type(sku.getSub_order_type());
        ret.setOrder_number(sku.getOrder_number());
        ret.setSku_id(sku.getSku_id());
        ret.setPurchase_quantity(sku.getPurchase_quantity());
        ret.setB_value(sku.getB_value());
        ret.setOrg_order_no(sku.getOrg_order_no());
        ret.setBalance(sku.getBalance());
        ret.setUnit_type_id(sku.getUnit_type_id());
        ret.setOld_balance(sku.getOld_balance());
        ret.setNew_balance(sku.getNew_balance());
        ret.setOvertop_value(sku.getOvertop_value());
        ret.setProcess_tag(2);
        ret.setProcess_time(DateUtil.getSystemTime());
        ret.setReserve_1("");
        ret.setReserve_2("");
        ret.setReserve_3("");
        ret.setReserve_4("");

        return ret;
    }

    public LogTradeHis swapTradeAndHis(final LogTrade log) {
        LogTradeHis ret = new LogTradeHis();
        ret.setTrade_id(log.getTrade_id());
        ret.setTrade_type_code(log.getTrade_type_code());
        ret.setExternal_system_code(log.getExternal_system_code());
        ret.setChannel_type(log.getChannel_type());
        ret.setUser_id(log.getUser_id());
        ret.setPartition_id(log.getPartition_id());
        ret.setOrder_no(log.getOrder_no());
        // 购物和退货
        ret.setOrder_type(log.getTrade_type_code().equals("101") ? "1" : "2");
        ret.setOrder_amount(log.getOrder_amount());
        ret.setOrder_completion_time(log.getOrder_completion_time());
        ret.setBalance_type_id(log.getBalance_type_id());
        ret.setUnit_type_id(log.getUnit_type_id());
        ret.setBalance(log.getBalance());
        ret.setOvertop_value(log.getOvertop_value());
        ret.setProcess_tag(2);
        ret.setTrade_time(log.getTrade_time());
        ret.setProcess_time(DateUtil.getSystemTime());
        ret.setRemark("");
        return ret;
    }

    // 停程序
    public static void stop() {
        status.set(false);
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setRemainder(int remainder) {
        this.remainder = remainder;
    }

    public void setSleepInterval(int sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

}
