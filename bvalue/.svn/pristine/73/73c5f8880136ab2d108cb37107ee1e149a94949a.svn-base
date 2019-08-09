/**
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue.core;

import com.tydic.beijing.bvalue.biz.TradeExchangeOper;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.dao.*;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SKU订单后台处理<br/>
 * 1.每个log_trade_sku都要单独提交，最后更改log_trade状态，移到log_trade_his，</br>
 * 保证log_trade_sku发生异常，下次可以继续处理</br>
 *
 * @author Bradish7Y
 * @since JDK 1.7
 */
public class SKUMonthEndProcess implements Runnable {

	private static final Logger log = Logger.getLogger(SKUMonthEndProcess.class);

	@Autowired
	private TradeExchangeOper oper;
//	@Autowired
//	private InfoPayBalanceSync sync;

//	private String syncRedis;

	private long TOP_VALUE = 0;// SKU封顶值
	private static final long NOT_REL_TOP_VALUE = 500;// SKU未关联封顶值
	private static final String DEPOSIT = "0";// 存款
	private static final String REFUND = "1";// 取款
	private static final String TRADE_TYPE_CODE_101 = "101";// 购物
	private static final String TRADE_TYPE_CODE_102 = "201";// 退货

	private int channel;// mode(user_id, channel)
	private int remainder;// mode(user_id, channel)=remainder

	private List<RuleParameters> listrulePara = new ArrayList<RuleParameters>();

//	public String getSyncRedis() {
//		return syncRedis;
//	}
//
//	public void setSyncRedis(String syncRedis) {
//		this.syncRedis = syncRedis;
//	}


	@Override
	public void run() {
		log.info("SKU-run channel[" + channel + "]");
		log.info("SKU-run remainder[" + remainder + "]");
		// SKU赠封顶值
		String domaincode = "2000";
		String tradetypecode = "104";
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("domain_code", domaincode);
		filter.put("trade_type_code", tradetypecode);
		listrulePara = S.get(RuleParameters.class).query(Condition.build("byattrValue").filter(filter));
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		String processTime = dateformat.format(new Date());//
		while (true) {
			// 分通道取临时表数据
			List<InfoPayBalanceSku> userIds = oper.getUserIdInInfoPayBalanceSkubyMode(channel, remainder);
			if (userIds==null || userIds.size()==0) {
				return;
			}
			for (InfoPayBalanceSku infoPayBalanceSku : userIds) {// 开始单个处理用户
				String userId = infoPayBalanceSku.getUser_id();
				InfoUserExternalAccount relUser = oper.getInfoUserExternalAccount(userId, DateUtil.getSystemTime());
				if (relUser == null) {// 未关联不进行sku赠
					continue;
				}
				List<InfoPayBalanceSku> infoPayBalanceSkus = oper.getIfpSkuByUserId(userId);

				long realBvalue = 0;
				long lastBvalue = 0;
				for (InfoPayBalanceSku ipbSku : infoPayBalanceSkus) {
					if (ipbSku.getMonth_flag() == 0) {
						realBvalue += ipbSku.getBalance();
					}
					if (ipbSku.getMonth_flag() == 1) {
						lastBvalue += ipbSku.getBalance();
						;
					}
				}
				log.debug("====账本临时表===中本月sku赠退总和：" + realBvalue + ",次月sku退总和：" + lastBvalue);
				try {
					if (realBvalue == 0 && lastBvalue == 0) {
						continue;
					}
					if (realBvalue > 0 && lastBvalue <= 0) {
						if (lastBvalue < 0) {
							long lastRefundValue = Math.abs(lastBvalue);
							this.endRefund(userId, processTime, lastRefundValue, 1);
							log.debug("处理次月sku退，总和是：" + lastBvalue);
						}
						this.endDeposit(relUser, userId, processTime, realBvalue);
						log.debug("处理本月sku增退，总和是：" + realBvalue + ",大于0");
					} else if (realBvalue <= 0 && lastBvalue <= 0) {
						if (lastBvalue < 0) {
							long lastRefundValue = Math.abs(lastBvalue);
							this.endRefund(userId, processTime, lastRefundValue, 1);
							log.debug("处理次月sku退，总和是：" + lastBvalue);
						}
						if (realBvalue<0) {
							long refundValue = Math.abs(realBvalue);
							this.endRefund(userId, processTime, refundValue, 0);
							log.debug("处理本月sku增退，总和是：" + realBvalue + ",小于0");
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// 合并账本失败
					log.error("时间格式出错！！！");
					oper.updateInfoPayBalanceSkuTag(infoPayBalanceSkus, 2);
				}
				oper.updateInfoPayBalanceSkuTag(infoPayBalanceSkus, 1);
			}
		}
	}

	/**
	 * 获取关联属性
	 */
	public String getTopSkuBValue(InfoUserExternalAccount relUser, String userId) {
		// 获得关联的属性
		String param = "";
		int isMatch = 0;
		int isExit = 0;
		String externalId = relUser.getExternal_account_id();
		InfoUserExternalAccountAttr accountAttr = new InfoUserExternalAccountAttr();
		List<InfoUserExternalAccountAttr> attrslist = oper.getExternalAccountAttr(userId, externalId);
		if (null == attrslist || attrslist.size() == 0) {
			param = "-1";
		} else {
			for (int i = 0; i < attrslist.size(); i++) {
				if (attrslist.get(i).getAttr_code().equals("1003")) {
					accountAttr = attrslist.get(i);
					isExit = 1;
					break;
				}
			}
			if (isExit == 0) {
				param = "-1";
			} else {
				String attrValue = accountAttr.getAttr_value();
				for (RuleParameters ruleParameters : listrulePara) {
					if (ruleParameters.getPara_name().equals(attrValue)) {
						param = attrValue;
						isMatch = 1;
					}
				}
				if (isMatch == 0) {
					param = "-1";
				}
			}
		}
		return param;
	}

	public int getPartitionId(String currentTime) {
		String month = currentTime.substring(4, 6);
		int partition;
		if (month.startsWith("0")) {
			partition = Integer.parseInt(month.substring(1, 2));
		} else {
			partition = Integer.parseInt(month);
		}
		return partition;
	}

	/**
	 * endDeposit月底账本合并
	 */
	public void endDeposit(InfoUserExternalAccount relUser, String userId, String processTime, long depositValue)
			throws Exception {
		int partitionId = getPartitionId(processTime);
		// 获取关联属性
		TOP_VALUE = this.getSKUValue(getTopSkuBValue(relUser, userId));
		String tradeId = Common.getUUID();
		// 查询累积量表
		LogRatableHistory logRatableHistory = oper.getLogRatableHistory(userId, processTime);

		// 发生月封顶信息不存在
		boolean ratableNotFound = false;

		String beginningMonthEffDate = DateUtil.getBeginningOfGivenMonth(processTime);
		String endMonthExpDate = DateUtil.getEndOfGivenMonth(processTime);
		// 参考完成时间
		Calendar balanceCalendar = DateUtil.getCalendarOfGivenDate(processTime);
		String balanceEffDate = DateUtil.getBValueEffDate(balanceCalendar);// B值的生效时间
		String balanceExpDate = DateUtil.getBValueExpDate(balanceCalendar);// B值的失效时间
		if (logRatableHistory == null) {
			ratableNotFound = true;
			log.debug("SKU-月底合并账本 发生月封顶信息不存在，新建累积量表");
			logRatableHistory = new LogRatableHistory();
			logRatableHistory.setRatable_history_id(Common.getUUID());
			logRatableHistory.setUser_id(userId);
			logRatableHistory.setRatable_type_code("SKU");
			logRatableHistory.setValue(0);
			logRatableHistory.setStart_cycle(beginningMonthEffDate);// 月初
			logRatableHistory.setEnd_cycle(endMonthExpDate);// 月底
			logRatableHistory.setCreate_time(processTime);
		}
		log.debug("SKU-月底合并账本 累积量：ratableNotFound[" + ratableNotFound + "]，" + logRatableHistory.toString());
		long hisBValue = logRatableHistory.getValue();
		// 返回所有有效的账本
		List<InfoPayBalance> infoPayBalanceList = oper.getAllInfoPayBalance(userId, processTime);

		List<InfoPayBalance> infoPayBalanceList2 = new ArrayList<InfoPayBalance>();
		for (InfoPayBalance tmpipb : infoPayBalanceList) {
			if (tmpipb.getBalance_type_id() != 3) {
				infoPayBalanceList2.add(tmpipb);
			}
		}
		infoPayBalanceList = infoPayBalanceList2;
		this.sortInfoPayBalanceByExpdate(infoPayBalanceList);
		if (log.isDebugEnabled()) {
			if (!infoPayBalanceList.isEmpty()) {
				log.info("SKU-deposit 排序后的账本：");
				for (InfoPayBalance i : infoPayBalanceList) {
					log.debug("SKU-deposit sorted " + i.toString());
				}
			}
		}

		InfoPayBalance infoPayBalanceType_0 = null;// 在用账本
		InfoPayBalance infoPayBalanceType_2 = null;// 超出账本
		boolean hasPositivePay = false;
		// 0 插入，1 修改，2 修改（含时间）
		int balanceInsertOrUpdateType_0 = 1;
		if (infoPayBalanceList.isEmpty()) {// 新建一个账本，type=0
			log.debug("SKU-deposit 有效账本不存在");
			balanceInsertOrUpdateType_0 = 0;
			infoPayBalanceType_0 = new InfoPayBalance();
			infoPayBalanceType_0.setBalance_id(Common.getUUID());
			infoPayBalanceType_0.setBalance(0);
			infoPayBalanceType_0.setBalance_type_id(0);
			infoPayBalanceType_0.setUser_id(userId);

			// 参考完成时间
			infoPayBalanceType_0.setEff_date(balanceEffDate);// B值的生效时间
			infoPayBalanceType_0.setExp_date(balanceExpDate);// B值的失效时间
		} else {// 存在有效账本，与完成时间比较
			for (InfoPayBalance pay : infoPayBalanceList) {
				// 超出账本
				if (pay.getBalance_type_id() == 2) {
					infoPayBalanceType_2 = pay;
					continue;
				}

				if (balanceEffDate.equals(pay.getEff_date()) && balanceExpDate.equals(pay.getExp_date())
						&& pay.getBalance_type_id() == 0) { // zhanghb add
															// 增加判断账本类型
															// sku赠送应该是使用0账本
					infoPayBalanceType_0 = pay;
					if (pay.getBalance() < 0) {
						log.debug("SKU-deposit 有负账本存在");
						hasPositivePay = true;
					}
				} else {
					if (pay.getBalance() < 0) {
						log.debug("SKU-deposit 有负账本存在");
						hasPositivePay = true;
						infoPayBalanceType_0 = pay;
					}
				}
			}
		}

		if (infoPayBalanceType_0 == null) {// 没有找到合适的0账本，新建一个0账本
			balanceInsertOrUpdateType_0 = 0;
			log.debug("SKU-月底账本合并  当前账本和完成时间不在同一个失效时间，需要新建账本");
			infoPayBalanceType_0 = new InfoPayBalance();
			infoPayBalanceType_0.setBalance_id(Common.getUUID());
			infoPayBalanceType_0.setBalance(0);
			infoPayBalanceType_0.setBalance_type_id(0);
			infoPayBalanceType_0.setUser_id(userId);

			// 参考完成时间
			infoPayBalanceType_0.setEff_date(balanceEffDate);// B值的生效时间
			infoPayBalanceType_0.setExp_date(balanceExpDate);// B值的失效时间
		}
		log.debug("SKU-月末账本合并 infoPayBalanceType_0:" + infoPayBalanceType_0.toString());
		long toNegative = 0;// 冲正量，-100->100，正值
		// 负值
		if (hasPositivePay) {
			log.debug("SKU-deposit 存在负账本");
			long pop = infoPayBalanceType_0.getBalance() + depositValue;
			if (pop >= 0) {// 冲正
				// 冲正前的值都更新到累积量
				toNegative = Math.abs(infoPayBalanceType_0.getBalance());
				// 冲正后剩余的赠送的值，就是赠送的值
				depositValue = pop;

				balanceInsertOrUpdateType_0 = 2;// 修改时间
				Calendar calendar = Calendar.getInstance();
				infoPayBalanceType_0.setEff_date(DateUtil.getBValueEffDate(calendar));// B值的生效时间
				infoPayBalanceType_0.setExp_date(DateUtil.getBValueExpDate(calendar));// B值的失效时间
			} else {
				toNegative = depositValue;
				depositValue = 0;
			}
		}
		// 累积量值，有负余额时，要加上负的
		long ratableTopValue = logRatableHistory.getValue() + toNegative;
		if (hisBValue + toNegative + depositValue > TOP_VALUE) {// 超出封顶值
			// 加在在用部分的值
			long presentTypeValue_0 = 0;
			if (TOP_VALUE - ratableTopValue > 0) {
				presentTypeValue_0 = TOP_VALUE - ratableTopValue;
			}
			// 加在超出部分的值
			long exceedPartValue = depositValue - presentTypeValue_0;// 超出部分
			log.debug("SKU-deposit 超出部分exceedPartValue=" + exceedPartValue);
			log.debug("SKU-deposit 在用部分的值presentTypeValue_0=" + presentTypeValue_0);
			List<BalanceAccessLog> balanceAccessLogs = new ArrayList<BalanceAccessLog>();
			int exchangeFlag = 0;
			if (presentTypeValue_0 + toNegative != 0) {
				exchangeFlag = 1;
			}
			{// 处理在用部分*****************
				if (exchangeFlag == 1) {// 在用部分不为0
					long oldBalance = infoPayBalanceType_0.getBalance();
					long newBalance = oldBalance + presentTypeValue_0 + toNegative;
					infoPayBalanceType_0.setBalance(presentTypeValue_0 + toNegative);
					log.debug("SKU-月底账本合并 老的在用余额：" + oldBalance);
					log.debug("SKU-月底账本合并  新的在用余额：" + newBalance);

					BalanceAccessLog access = new BalanceAccessLog();
					access.setTrade_Id(tradeId);
					access.setTrade_Type_Code(TRADE_TYPE_CODE_101);
					access.setUser_Id(userId);
					access.setPartition_Id(partitionId);
					access.setBalance_Id(infoPayBalanceType_0.getBalance_id());
					access.setBalance_Type_Id(infoPayBalanceType_0.getBalance_type_id());
					access.setAccess_Tag(DEPOSIT);// 存款
					access.setOld_Balance(oldBalance);
					access.setNew_Balance(newBalance);
					access.setOperate_Time(processTime);
					access.setMoney(presentTypeValue_0 + toNegative);

					balanceAccessLogs.add(access);
					// 要把冲正的值也加上
					logRatableHistory.setValue(presentTypeValue_0 + toNegative);
				}

			}
			boolean payIsnewType_2 = false;
			{// 处理超出部分
				if (infoPayBalanceType_2 == null) {
					payIsnewType_2 = true;
					infoPayBalanceType_2 = new InfoPayBalance();
					infoPayBalanceType_2.setBalance_id(Common.getUUID());
					infoPayBalanceType_2.setBalance(0);
					infoPayBalanceType_2.setBalance_type_id(2);
					infoPayBalanceType_2.setUser_id(userId);

					// 参考完成时间
					infoPayBalanceType_2.setEff_date(beginningMonthEffDate);// B值的生效时间
					infoPayBalanceType_2.setExp_date(endMonthExpDate);// B值的失效时间
				}
				long oldBalance = infoPayBalanceType_2.getBalance();
				long newBalance = oldBalance + exceedPartValue;
				infoPayBalanceType_2.setBalance(exceedPartValue);

				BalanceAccessLog access = new BalanceAccessLog();
				access.setTrade_Id(tradeId);
				access.setTrade_Type_Code(TRADE_TYPE_CODE_101);
				access.setUser_Id(userId);
				access.setPartition_Id(partitionId);
				access.setBalance_Id(infoPayBalanceType_2.getBalance_id());
				access.setBalance_Type_Id(infoPayBalanceType_2.getBalance_type_id());
				access.setAccess_Tag(DEPOSIT);// 存款
				access.setOld_Balance(oldBalance);
				access.setNew_Balance(newBalance);
				access.setOperate_Time(processTime);
				access.setMoney(exceedPartValue);

				balanceAccessLogs.add(access);
			}
			oper.updateSkuProcessDepositType_2(balanceInsertOrUpdateType_0, payIsnewType_2, ratableNotFound,
					infoPayBalanceType_0, infoPayBalanceType_2, logRatableHistory, balanceAccessLogs);
			// 更新redis
			// syncRedis(userId);
		} else {
			// 没有超过封顶值
			log.debug("SKU-deposit 不存在封顶值，直接增加在用余额");
			long oldBalance = infoPayBalanceType_0.getBalance();
			long newBalance = oldBalance + depositValue + toNegative;
			infoPayBalanceType_0.setBalance(depositValue + toNegative);

			BalanceAccessLog access = new BalanceAccessLog();
			access.setTrade_Id(tradeId);
			access.setTrade_Type_Code(TRADE_TYPE_CODE_101);
			access.setUser_Id(userId);
			access.setPartition_Id(partitionId);
			access.setBalance_Id(infoPayBalanceType_0.getBalance_id());
			access.setBalance_Type_Id(infoPayBalanceType_0.getBalance_type_id());
			access.setAccess_Tag(DEPOSIT);// 存款
			access.setOld_Balance(oldBalance);
			access.setNew_Balance(newBalance);
			access.setOperate_Time(processTime);
			access.setMoney(depositValue + toNegative);
			// 操作type=0的账本更新到累积量
			logRatableHistory.setValue(depositValue + toNegative);
			oper.updateSkuProcessDepositType_0(balanceInsertOrUpdateType_0, ratableNotFound, infoPayBalanceType_0,
					logRatableHistory, access);
			// 更新redis
			// syncRedis(userId);

		}
	}

	/**
	 * refund:退货.<br/>
	 *
	 * @param logTrade
	 * @param logTradeSku
	 * @throws ParseException
	 */
	public void endRefund(String userId, String processTime, long refundValue, int monthFlag) throws Exception {
		int partitionId = getPartitionId(processTime);
		String tradeId = Common.getUUID();
		String currDate = processTime;
		String completeTime = "";
		if (monthFlag == 0) {
			completeTime = processTime;
		} else if (monthFlag == 1) {
			completeTime = getLastMonthProcessTime(processTime);// 购物时的完成时间,上个月月底
		}

		log.debug("SKU退对应的SKU赠的时间是：" + completeTime);
		// 上次购物所赠b值，logTrade.balance + logTrade.overtop
		long unitBPrice = 0;// 单价，原始报文的
		long quantity = 0;
		log.debug("SKU-上月退货  查找退货单价、数量");
		// 退货的总B值
		log.debug("SKU-上月退货 退货数量：[" + refundValue + "]");
		// 参考完成时间
		Calendar balanceCalendar = DateUtil.getCalendarOfGivenDate(completeTime);
		String balanceEffDate = DateUtil.getBValueEffDate(balanceCalendar);// B值的生效时间
		String balanceExpDate = DateUtil.getBValueExpDate(balanceCalendar);// B值的失效时间

		String beginningMonthEffDate = DateUtil.getBeginningOfGivenMonth(completeTime);
		String endMonthExpDate = DateUtil.getEndOfGivenMonth(completeTime);
		String expDate2050 = "20501231235959";

		// 返回所有有效的账本
		log.debug("SKU-refund 查找type=2的账本");
		List<InfoPayBalance> infoPayBalances = new ArrayList<InfoPayBalance>();
		List<InfoPayBalance> infoPayBalances_2 = oper.getInfoPayBalance(userId, "2", completeTime);
		if (!infoPayBalances_2.isEmpty()) {
			infoPayBalances.addAll(infoPayBalances_2);
		}
		log.debug("SKU-refund 查找type=0的账本");
		List<InfoPayBalance> infoPayBalances_0 = oper.getInfoPayBalance(userId, "0", currDate);
		if (!infoPayBalances_0.isEmpty()) {
			infoPayBalances.addAll(infoPayBalances_0);
		}

		List<InfoPayBalance> infoPayBalances_3 = oper.getInfoPayBalance(userId, "3", currDate);
		if (!infoPayBalances_3.isEmpty()) {
			infoPayBalances.addAll(infoPayBalances_3);
		}

		int balanceInsertOrUpdate = 1;
		boolean ratableNotFound = false;
		// 账本不存在
		if (infoPayBalances.isEmpty()) {
			balanceInsertOrUpdate = 0;
			// 新建
			log.debug("SKU-refund 在用和超出账本不存在，新建");
			InfoPayBalance infoPayBalance = new InfoPayBalance();
			infoPayBalance.setBalance_id(Common.getUUID());
			infoPayBalance.setBalance(0);
			infoPayBalance.setBalance_type_id(0);
			infoPayBalance.setUser_id(userId);

			// 参考完成时间
			infoPayBalance.setEff_date(balanceEffDate);// B值的生效时间
			infoPayBalance.setExp_date(balanceExpDate);// B值的失效时间

			infoPayBalances = new ArrayList<InfoPayBalance>();
			infoPayBalances.add(infoPayBalance);
		}

		// this.sortInfoPayBalanceByTypeIdByExpdate(infoPayBalances);

		if (log.isDebugEnabled()) {
			log.debug("SKU-refund 排序后的账本");
			for (InfoPayBalance i : infoPayBalances) {
				log.debug("SKU-refund InfoPayBalance:" + i.toString());
			}
		}

		long remainBvalue = 0;
		long refundValueTmp = refundValue;

		List<InfoPayBalance> uInfoPayBalanceList = new ArrayList<InfoPayBalance>();
		List<InfoPayBalance> insertInfoPayBalanceList = new ArrayList<InfoPayBalance>();
		List<BalanceAccessLog> uBalanceAccessLogList = new ArrayList<BalanceAccessLog>();
		// boolean onlyZeroPayBalance = true;
		// 扣在0账本上的B值
		long currValue = 0;
		long totalAvailableBalance = 0;
		for (InfoPayBalance pay : infoPayBalances) {
			if (pay.getBalance_type_id() == 0) {
				totalAvailableBalance += pay.getBalance();
			}
		}

		long newBalanceSum = totalAvailableBalance;// 总的
		log.debug("SKU-refund 开始扣减账本余额");
		for (InfoPayBalance pay : infoPayBalances) {
			BalanceAccessLog access = new BalanceAccessLog();
			access.setTrade_Id(tradeId);
			access.setTrade_Type_Code(TRADE_TYPE_CODE_102);// 退货
			access.setUser_Id(userId);
			access.setPartition_Id(partitionId);
			access.setBalance_Id(pay.getBalance_id());
			access.setBalance_Type_Id(pay.getBalance_type_id());
			access.setAccess_Tag(REFUND);// 取款
			access.setOld_Balance(pay.getBalance());
			access.setOperate_Time(currDate);

			long newBalance = 0;
			remainBvalue = pay.getBalance();// 余额，负的去绝对值

			// 此条余额 > 退货B值
			if (remainBvalue >= refundValueTmp) {
				log.debug("SKU-refund 当条账本大于退货B值，remainBvalue[" + remainBvalue + "], refundValueTmp[" + refundValueTmp
						+ "]");
				pay.setBalance(-refundValueTmp);
				newBalance = remainBvalue - refundValueTmp;// 变动后的余额

				// 超出部分
				if (pay.getBalance_type_id() == 0) {
					// 超出部分不计入累积量
					currValue += refundValueTmp;
					newBalanceSum -= refundValueTmp;
				}

				access.setMoney(refundValueTmp);// access扣减B值量
				access.setNew_Balance(newBalance);// access
				// add
				uInfoPayBalanceList.add(pay);
				uBalanceAccessLogList.add(access);
				refundValueTmp = 0;// 退货=0
				break;
			}
			log.debug("SKU-refund 当条账本不大于退货B值，remainBvalue[" + remainBvalue + "], refundValueTmp[" + refundValueTmp
					+ "]");
			// 退货B值 - 此条账本余额
			refundValueTmp -= remainBvalue;
			pay.setBalance(-remainBvalue);// 此账本扣到0
			newBalance = 0;// 变动后的余额
			// 超出部分
			if (pay.getBalance_type_id() == 0) {
				// 超出部分不计入累积量
				currValue += remainBvalue;
				newBalanceSum -= remainBvalue;
			}
			access.setMoney(remainBvalue);// access扣减B值量
			access.setNew_Balance(newBalance);// access

			// add
			uInfoPayBalanceList.add(pay);
			uBalanceAccessLogList.add(access);

		}
		if (log.isDebugEnabled()) {
			for (BalanceAccessLog b : uBalanceAccessLogList) {
				log.debug("SKU-refund BalanceAccessLog:" + b.toString());
			}
		}
		// 扣了所有账本，到这里，说明还有退货B值，扣最后一条到负，修改失效时间
		if (refundValueTmp > 0) {
			log.debug("SKU-refund 账本有扣负情况，退货B值大于余额");
			// 账本余额都是0
			// if (onlyZeroPayBalance) {
			// log.debug("SKU-refund 所有账本余额都是0");
			// 到了这里，最后一条肯定是0账本
			// InfoPayBalance infoPayIf =
			// uInfoPayBalanceList.get(uInfoPayBalanceList.size() - 1);
			InfoPayBalance infoPayIf = getLast0Balance(uInfoPayBalanceList);

			if (infoPayIf == null) {
				// 最后没有找到0账本，只能新增一个0账本，然后扣到负
				infoPayIf = new InfoPayBalance();
				infoPayIf.setBalance_id(Common.getUUID());
				infoPayIf.setUser_id(userId);
				infoPayIf.setBalance_type_id(0);
				infoPayIf.setEff_date(balanceEffDate);
				infoPayIf.setBalance(-refundValueTmp);
				infoPayIf.setExp_date(expDate2050);// 20500101235959
				insertInfoPayBalanceList.add(infoPayIf);
			} else {
				currValue += refundValueTmp;
				newBalanceSum = -refundValueTmp;

				infoPayIf.setBalance(infoPayIf.getBalance() - refundValueTmp);
				infoPayIf.setExp_date(expDate2050);// 20500101235959
				// uInfoPayBalanceList.add(infoPayIf);
			}

			BalanceAccessLog access = new BalanceAccessLog();
			access.setTrade_Id(tradeId);
			access.setTrade_Type_Code(TRADE_TYPE_CODE_102);// 退货
			access.setUser_Id(userId);
			access.setPartition_Id(partitionId);
			access.setBalance_Id(infoPayIf.getBalance_id());
			access.setBalance_Type_Id(infoPayIf.getBalance_type_id());
			access.setAccess_Tag(REFUND);// 取款
			access.setOld_Balance(0);
			access.setOperate_Time(currDate);
			access.setMoney(refundValueTmp);
			access.setNew_Balance(-refundValueTmp);
			uBalanceAccessLogList.add(access);

			refundValueTmp = 0;
			if (log.isDebugEnabled()) {
				for (BalanceAccessLog b : uBalanceAccessLogList) {
					log.debug("SKU-refund BalanceAccessLog:" + b.toString());
				}
			}
		}
		// 查询累积量表，扣减超出部分
		LogRatableHistory logRatableHistory = oper.getLogRatableHistory(userId, processTime);
		if (logRatableHistory == null) {
			log.debug("SKU-refund 新建累积量表");
			ratableNotFound = true;
			// 新建累积量表
			logRatableHistory = new LogRatableHistory();
			logRatableHistory.setRatable_history_id(Common.getUUID());
			logRatableHistory.setUser_id(userId);
			logRatableHistory.setRatable_type_code("SKU");
			logRatableHistory.setValue(0);
			logRatableHistory.setStart_cycle(beginningMonthEffDate);// 月初
			logRatableHistory.setEnd_cycle(endMonthExpDate);// 月底
			logRatableHistory.setCreate_time(currDate);
		}
		log.info("SKU-refund 操作type=0的账本部分currValue[" + currValue + "]");
		log.debug("SKU-refund 查询累积量表，当月退货操作type=0的账本，更新累积量；往月不更新");
		if (monthFlag == 0) {
			logRatableHistory.setValue(-currValue);
		} else if (monthFlag == 1) {
			logRatableHistory.setValue(0);
		}
		log.info("SKU-refund 更新表，提交");
		// 更新、新增
		oper.updateSkuProcessRefund(balanceInsertOrUpdate, ratableNotFound, uInfoPayBalanceList, logRatableHistory,
				uBalanceAccessLogList, insertInfoPayBalanceList);
		// 更新redis
		// log.info("SKU-refund 更新redis");
		// syncRedis(userId);
	}

	// 获取上一个月的时间
	public String getLastMonthProcessTime(String processTime) {
		int year = Integer.parseInt(processTime.substring(0, 4));
		int month = Integer.parseInt(processTime.substring(4, 6));
		int lastmont = 0;
		if (month == 1) {
			lastmont = 12;
			year = year - 1;
		} else {
			lastmont = month - 1;
		}
		String monthStr = String.valueOf(lastmont);
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}
		String completeTime = String.valueOf(year) + monthStr + processTime.substring(6, 14);
		return completeTime;
	}

	/**
	 * 获得SKU赠的封顶值
	 * 
	 */
	public int getSKUValue(final String parame) {
		int SkuValue = 0;
		for (RuleParameters ruleParameters : listrulePara) {
			if (ruleParameters.getPara_name().equals(parame)) {
				SkuValue = Integer.parseInt(ruleParameters.getPara_char2());
			}
		}
		log.debug("bvalue---------------------------" + SkuValue);
		return SkuValue;
	}

	/**
	 * depositNotRel:未关联购物.<br/>
	 *
	 * @param logTrade
	 * @param logTradeSku
	 * @throws ParseException
	 */
	public Status depositNotRel(final LogTrade logTrade, final LogTradeSku logTradeSku) throws ParseException {

		int partitionId = logTrade.getPartition_id();
		String userId = logTrade.getUser_id();
		String tradeId = logTrade.getTrade_id();
		String currDate = DateUtil.getSystemTime();
		String completeTime = logTrade.getOrder_completion_time();
		log.debug("SKU-depositNotRel completeTime[" + completeTime + "]");
		String beginningMonthEffDate = null;
		// 参考完成时间
		try {
			beginningMonthEffDate = DateUtil.getBeginningOfGivenMonth(completeTime);
		} catch (ParseException e) {
			log.error("SKU-depositNotRel  订单完成时间格式不正确，complateTime[" + completeTime + "]");

			LogTradeSkuHis tmp = this.swapTradeSkuAndHis(logTradeSku);
			tmp.setReserve_1("订单完成时间格式不正确，complateTime[" + completeTime + "]");
			oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
			return new Status(-1, "订单完成时间格式不正确");
		}

		// 新版本，未关联不赠送，开始时间是2015年7月1日0点0分0秒开始
		// 订单完成时间是之前的还是要赠送的

		if (completeTime.compareTo("20150701000000") >= 0) {// 不赠送
			LogTradeSkuHis tmp = swapTradeSkuAndHis(logTradeSku);
			tmp.setReserve_1("未关联用户不赠送");
			oper.updateSkuProcessForSkuToHis(logTradeSku, tmp);
			return new Status(-1, "未关联用户不赠送");
		}

		String endMonthExpDate = DateUtil.getEndOfGivenMonth(completeTime);
		// 参考完成时间
		Calendar balanceCalendar = DateUtil.getCalendarOfGivenDate(completeTime);
		String balanceEffDate = DateUtil.getBValueEffDate(balanceCalendar);// B值的生效时间
		String balanceExpDate = DateUtil.getBValueExpDate(balanceCalendar);// B值的失效时间

		// 赠送的总B值
		long depositValue = logTradeSku.getPurchase_quantity() * logTradeSku.getB_value();
		log.info("SKU-depositNotRel 总赠送B值=" + depositValue);

		log.info("SKU-depositNotRel 查询所有完成时间有效的账本");
		// 返回所有有效的账本
		List<InfoPayBalance> infoPayBalanceList = oper.getAllValidInfoPayBalance(userId, completeTime);

		List<InfoPayBalance> infoPayBalanceList2 = new ArrayList<InfoPayBalance>();
		for (InfoPayBalance tmpipb : infoPayBalanceList) {
			if (tmpipb.getBalance_type_id() != 3) {
				infoPayBalanceList2.add(tmpipb);
			}
		}

		infoPayBalanceList = infoPayBalanceList2;

		this.sortInfoPayBalanceByExpdate(infoPayBalanceList);

		if (log.isDebugEnabled()) {
			if (!infoPayBalanceList.isEmpty()) {
				log.info("SKU-depositNotRel 排序后的账本：");
				for (InfoPayBalance i : infoPayBalanceList) {
					log.debug("SKU-depositNotRel sorted " + i.toString());
				}
			}
		}
		InfoPayBalance infoPayBalanceType_0 = null;// 在用账本
		InfoPayBalance infoPayBalanceType_2 = null;// 超出账本

		boolean hasPositivePay = false;
		// 0 插入，1 修改，2 修改（含时间）
		int balanceInsertOrUpdateType_0 = 1;
		long totalBalance = 0;// 可用余额
		if (infoPayBalanceList.isEmpty()) {// 新建一个账本，type=0
			log.debug("SKU-depositNotRel 有效账本不存在");
			balanceInsertOrUpdateType_0 = 0;
			infoPayBalanceType_0 = new InfoPayBalance();
			infoPayBalanceType_0.setBalance_id(Common.getUUID());
			infoPayBalanceType_0.setBalance(0);
			infoPayBalanceType_0.setBalance_type_id(0);
			infoPayBalanceType_0.setUser_id(userId);

			// 参考完成时间
			infoPayBalanceType_0.setEff_date(balanceEffDate);// B值的生效时间
			infoPayBalanceType_0.setExp_date(balanceExpDate);// B值的失效时间
		} else {// 存在有效账本，与完成时间比较
			for (InfoPayBalance pay : infoPayBalanceList) {
				if (pay.getBalance_type_id() == 1) {
					continue;
				}
				// 超出账本
				if (pay.getBalance_type_id() == 2) {
					if (completeTime.compareTo(pay.getEff_date()) >= 0
							&& completeTime.compareTo(pay.getExp_date()) <= 0) {
						infoPayBalanceType_2 = pay;
					}
					continue;
				}

				if (pay.getBalance_type_id() == 0) {
					totalBalance += pay.getBalance();
				}

				if (balanceEffDate.equals(pay.getEff_date()) && balanceExpDate.equals(pay.getExp_date())
						&& pay.getBalance_type_id() == 0) { // zhanghb add
															// 增加匹配账本类型
					infoPayBalanceType_0 = pay;
					if (pay.getBalance() < 0) {
						log.debug("SKU-depositNotRel 有负账本存在");
						hasPositivePay = true;
					}
				} else {
					if (pay.getBalance() < 0) {
						log.debug("SKU-depositNotRel 有负账本存在");
						hasPositivePay = true;
						infoPayBalanceType_0 = pay;
					}
				}
			}
		}

		if (infoPayBalanceType_0 == null) {
			balanceInsertOrUpdateType_0 = 0;
			log.debug("SKU-depositNotRel 当前账本和完成时间不在同一个失效时间，需要新建账本");
			infoPayBalanceType_0 = new InfoPayBalance();
			infoPayBalanceType_0.setBalance_id(Common.getUUID());
			infoPayBalanceType_0.setBalance(0);
			infoPayBalanceType_0.setBalance_type_id(0);
			infoPayBalanceType_0.setUser_id(userId);

			// 参考完成时间
			infoPayBalanceType_0.setEff_date(balanceEffDate);// B值的生效时间
			infoPayBalanceType_0.setExp_date(balanceExpDate);// B值的失效时间
		}
		log.debug("SKU-depositNotRel infoPayBalanceType_0:" + infoPayBalanceType_0.toString());

		long toNegative = 0;// 冲正量，-100->100，正值
		// 负值
		if (hasPositivePay) {
			log.debug("SKU-depositNotRel 存在负账本");
			long pop = infoPayBalanceType_0.getBalance() + depositValue;
			if (pop >= 0) {// 冲正
				// 冲正前的值都更新到累积量
				toNegative = Math.abs(infoPayBalanceType_0.getBalance());
				// 冲正后剩余的赠送的值，就是赠送的值
				depositValue = pop;

				balanceInsertOrUpdateType_0 = 2;// 修改时间
				Calendar calendar = Calendar.getInstance();
				infoPayBalanceType_0.setEff_date(DateUtil.getBValueEffDate(calendar));// B值的生效时间
				infoPayBalanceType_0.setExp_date(DateUtil.getBValueExpDate(calendar));// B值的失效时间
			} else {
				toNegative = depositValue;
				depositValue = 0;
			}
		}

		log.debug("SKU-depositNotRel 新的赠送B值depositValue[" + depositValue + "]");
		// 累加后的总B值
		long usingParty = totalBalance;// infoPayBalanceType_0.getBalance();//
		// 在用余额

		// 在用部分大于500封顶，如果关联时是1000，解除关联后1000余额不变
		// 只更新超出账本
		if (usingParty >= NOT_REL_TOP_VALUE) {
			log.debug("SKU-depositNotRel 有超出部分计算，在用账本余额不变，只更改超出部分，未关联封顶值:" + NOT_REL_TOP_VALUE);

			// 0账本不变，加在超出部分的值
			long exceedPartValue = depositValue;
			log.debug("SKU-depositNotRel 超出部分exceedPartValue=" + exceedPartValue);

			List<BalanceAccessLog> balanceAccessLogs = new ArrayList<BalanceAccessLog>();

			boolean payIsnewType_2 = false;
			// 处理超出部分
			if (infoPayBalanceType_2 == null) {
				payIsnewType_2 = true;
				infoPayBalanceType_2 = new InfoPayBalance();
				infoPayBalanceType_2.setBalance_id(Common.getUUID());
				infoPayBalanceType_2.setBalance(0);
				infoPayBalanceType_2.setBalance_type_id(2);
				infoPayBalanceType_2.setUser_id(userId);

				// 参考完成时间
				infoPayBalanceType_2.setEff_date(beginningMonthEffDate);// B值的生效时间
				infoPayBalanceType_2.setExp_date(endMonthExpDate);// B值的失效时间
			}
			long oldBalance = infoPayBalanceType_2.getBalance();
			long newBalance = oldBalance + exceedPartValue;

			LogTradeSkuHis logSkuHis = swapTradeSkuAndHis(logTradeSku);
			logSkuHis.setBalance(0);// 不算封顶值部分，加在在用部分的值
			logSkuHis.setOvertop_value(exceedPartValue);
			logSkuHis.setProcess_tag(2);
			logSkuHis.setOld_balance(totalBalance);
			logSkuHis.setNew_balance(totalBalance);

			infoPayBalanceType_2.setBalance(exceedPartValue);

			BalanceAccessLog access = new BalanceAccessLog();
			access.setTrade_Id(tradeId);
			access.setTrade_Type_Code(TRADE_TYPE_CODE_101);
			access.setUser_Id(userId);
			access.setPartition_Id(partitionId);
			access.setBalance_Id(infoPayBalanceType_2.getBalance_id());
			access.setBalance_Type_Id(infoPayBalanceType_2.getBalance_type_id());
			access.setAccess_Tag(DEPOSIT);// 存款
			access.setOld_Balance(oldBalance);
			access.setNew_Balance(newBalance);
			access.setOperate_Time(currDate);
			access.setMoney(exceedPartValue);

			balanceAccessLogs.add(access);
			// 更新表
			oper.updateSkuProcessDepositTypeNotRel_2(payIsnewType_2, infoPayBalanceType_2, logSkuHis, logTradeSku,
					balanceAccessLogs);
			// 总赠送B值
			logTrade.setBalance(logTrade.getBalance() + 0 + toNegative);
			logTrade.setOvertop_value(logTrade.getOvertop_value() + exceedPartValue);
		} else {
			// *****************************************
			// 在用部分小于500封顶，
			log.debug("SKU-depositNotRel 不存在封顶值，直接增加在用余额");
			log.debug("SKU-depositNotRel usingParty总的在用余额=" + usingParty);
			// 加在在用部分的值
			long presentTypeValue_0 = 0;
			// 有负值冲正的情况
			if (depositValue + (usingParty + toNegative) <= NOT_REL_TOP_VALUE) {
				presentTypeValue_0 = depositValue;
			} else {
				presentTypeValue_0 = NOT_REL_TOP_VALUE - (usingParty + toNegative);
			}

			// 加在超出部分的值
			long exceedPartValue = depositValue - presentTypeValue_0;// 超出部分
			log.debug("SKU-depositNotRel 超出部分exceedPartValue=" + exceedPartValue);
			log.debug("SKU-depositNotRel 在用部分的值presentTypeValue_0=" + presentTypeValue_0);
			LogTradeSkuHis logSkuHis = swapTradeSkuAndHis(logTradeSku);
			logSkuHis.setBalance(presentTypeValue_0 + toNegative);// 不算封顶值部分
			logSkuHis.setOvertop_value(exceedPartValue);
			logSkuHis.setProcess_tag(2);
			List<BalanceAccessLog> balanceAccessLogs = new ArrayList<BalanceAccessLog>();
			{// 处理在用部分*****************
				long oldBalance = infoPayBalanceType_0.getBalance();
				long newBalance = oldBalance + presentTypeValue_0 + toNegative;
				infoPayBalanceType_0.setBalance(presentTypeValue_0 + toNegative);
				log.debug("SKU-depositNotRel 老的在用余额：" + oldBalance);
				log.debug("SKU-depositNotRel 新的在用余额：" + newBalance);

				BalanceAccessLog access = new BalanceAccessLog();
				access.setTrade_Id(tradeId);
				access.setTrade_Type_Code(TRADE_TYPE_CODE_101);
				access.setUser_Id(userId);
				access.setPartition_Id(partitionId);
				access.setBalance_Id(infoPayBalanceType_0.getBalance_id());
				access.setBalance_Type_Id(infoPayBalanceType_0.getBalance_type_id());
				access.setAccess_Tag(DEPOSIT);// 存款
				access.setOld_Balance(oldBalance);
				access.setNew_Balance(newBalance);
				access.setOperate_Time(currDate);
				access.setMoney(presentTypeValue_0 + toNegative);

				balanceAccessLogs.add(access);

				// 第一次，不需要累加
				logSkuHis.setOld_balance(totalBalance);
				logSkuHis.setNew_balance(totalBalance + presentTypeValue_0 + toNegative);
			}
			boolean payIsnewType_2 = false;
			{// 处理超出部分
				if (infoPayBalanceType_2 == null) {
					payIsnewType_2 = true;
					infoPayBalanceType_2 = new InfoPayBalance();
					infoPayBalanceType_2.setBalance_id(Common.getUUID());
					infoPayBalanceType_2.setBalance(0);
					infoPayBalanceType_2.setBalance_type_id(2);
					infoPayBalanceType_2.setUser_id(userId);

					// 参考完成时间
					infoPayBalanceType_2.setEff_date(beginningMonthEffDate);// B值的生效时间
					infoPayBalanceType_2.setExp_date(endMonthExpDate);// B值的失效时间
				}
				long oldBalance = infoPayBalanceType_2.getBalance();
				long newBalance = oldBalance + exceedPartValue;
				infoPayBalanceType_2.setBalance(exceedPartValue);

				BalanceAccessLog access = new BalanceAccessLog();
				access.setTrade_Id(tradeId);
				access.setTrade_Type_Code(TRADE_TYPE_CODE_101);
				access.setUser_Id(userId);
				access.setPartition_Id(partitionId);
				access.setBalance_Id(infoPayBalanceType_2.getBalance_id());
				access.setBalance_Type_Id(infoPayBalanceType_2.getBalance_type_id());
				access.setAccess_Tag(DEPOSIT);// 存款
				access.setOld_Balance(oldBalance);
				access.setNew_Balance(newBalance);
				access.setOperate_Time(currDate);
				access.setMoney(exceedPartValue);

				balanceAccessLogs.add(access);
			}

			oper.updateSkuProcessDepositTypeNotRel(balanceInsertOrUpdateType_0, payIsnewType_2, infoPayBalanceType_0,
					infoPayBalanceType_2, logSkuHis, logTradeSku, balanceAccessLogs);
					// 更新redis

			// syncRedis(userId);

			// sync.sync(infoPayBalanceType_2);超出部分不更新到redis
			// 总赠送B值
			logTrade.setBalance(logTrade.getBalance() + presentTypeValue_0 + toNegative);
			logTrade.setOvertop_value(logTrade.getOvertop_value() + exceedPartValue);

		}

		return new Status(0, "购物OK");
	}

/*	同步redis
 * private void syncRedis(String userId) {

		if (this.syncRedis != null && this.syncRedis.equals("Y")) {
			String currDate = DateUtil.getSystemTime();
			List<InfoPayBalance> infoPayBalanceList = oper.getAllInfoPayBalance(userId, currDate);
			for (InfoPayBalance ipb : infoPayBalanceList) {
				if (ipb.getBalance_type_id() == 0 || ipb.getBalance_type_id() == 3) {
					//去除redis同步方法
//					sync.sync(ipb);
				}
			}

		}

	}
	*/

	private InfoPayBalance getLast0Balance(List<InfoPayBalance> uInfoPayBalanceList) {
		for (int i = uInfoPayBalanceList.size() - 1; i >= 0; i--) {
			if (uInfoPayBalanceList.get(i).getBalance_type_id() == 0) {
				return uInfoPayBalanceList.get(i);
			}
		}

		return null;
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
	 * sortInfoPayBalanceByExpdate:账本按expdate排序.<br/>
	 *
	 * @param infoPayBalances
	 */
	public void sortInfoPayBalanceByExpdate(final List<InfoPayBalance> infoPayBalances) {
		Collections.sort(infoPayBalances, new Comparator<InfoPayBalance>() {

			@Override
			public int compare(InfoPayBalance o1, InfoPayBalance o2) {
				int ret = o1.getExp_date().compareTo(o2.getExp_date());
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
	 * sortInfoPayBalanceByTypeIdByExpdate:先排序balance_type_id，在排序exp_date.<br/>
	 *
	 * @param infoPayBalances
	 */
	public void sortInfoPayBalanceByTypeIdByExpdate(final List<InfoPayBalance> infoPayBalances) {
		Collections.sort(infoPayBalances, new Comparator<InfoPayBalance>() {

			@Override
			public int compare(InfoPayBalance o1, InfoPayBalance o2) {
				if (o1.getBalance_type_id() == o2.getBalance_type_id()) {
					int ret = o1.getExp_date().compareTo(o2.getExp_date());
					if (0 == ret) {
						return 0;
					} else if (ret > 0) {
						return 1;
					} else {
						return -1;
					}
				} else if (o1.getBalance_type_id() > o2.getBalance_type_id()) {
					return -1;
				} else {
					return 1;
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

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public void setRemainder(int remainder) {
		this.remainder = remainder;
	}

}
