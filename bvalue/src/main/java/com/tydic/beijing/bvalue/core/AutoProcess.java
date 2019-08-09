/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.midi.MidiDevice.Info;

import net.sf.json.JSONObject;

import org.apache.activemq.transport.tcp.ExceededMaximumConnectionsException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.account.service.Recharge;
import com.tydic.beijing.billing.dto.BValueExchangeRequest;
import com.tydic.beijing.billing.dto.BaseResponse;
import com.tydic.beijing.billing.dto.PackageDetailDto;
import com.tydic.beijing.billing.dto.RechargeInfo;
import com.tydic.beijing.billing.dto.RechargeInfoDto;
import com.tydic.beijing.billing.dto.RechargeResult;
import com.tydic.beijing.billing.interfacex.service.OrderExtraPackage;
import com.tydic.beijing.bvalue.biz.TradeExchangeOper;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.dao.AutoExchangeSmsSend;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.ExchangeRatableHistory;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoPayBalanceBilling;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LifeResourceList;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchange;
import com.tydic.beijing.bvalue.dao.LogAutoExchangeLog;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.RuleParameters;
import com.tydic.beijing.bvalue.service.impl.InfoPayBalanceManager;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;
import com.tydic.unicom.crm.busi.po.OrderStatus;
import com.tydic.unicom.crm.busi.po.PubInputDto;
import com.tydic.unicom.crm.busi.service.interfaces.QuerySubsStatusServ;

/**
 * 自动兑换后台程序<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class AutoProcess implements Runnable {
	private static final Logger log = Logger.getLogger(AutoProcess.class);

	@Autowired
	private TradeExchangeOper oper;
	/*
	@Autowired
	private InfoPayBalanceSync sync;
	*/
	@Autowired
	private Recharge reCharge;
	@Autowired
	private OrderExtraPackage doExchange;

	@Autowired
	private QuerySubsStatusServ querySubsStatus;
	private static final String TRADE_TYPE_CODE = "302";
	private static final String REFUND = "1";// 取款
	private static final String CYCLE_TYPE = "COM";
	private int sleepInterval;
	
	private static final String RECHARGE_TYPE ="BV"; //充值类型 充值接口给b值兑换话费分配的充值类型
	
	//定义List存放兑换信息
	public class ResourceExchangeInfo{
		String resourceType ="";
		long resourceValue =0L;
		
		public String getResourceType() {
			return resourceType;
		}
		public void setResourceType(String resourceType) {
			this.resourceType = resourceType;
		}
		public long getResourceValue() {
			return resourceValue;
		}
		public void setResourceValue(long resourceValue) {
			this.resourceValue = resourceValue;
		}
		public ResourceExchangeInfo( ){

		}
		public ResourceExchangeInfo( String a,long b){
			this.resourceType =a;
			this.resourceValue=b;
		}
		@Override
		public String toString() {
			return "ResourceExchangeInfo [resourceType=" + resourceType
					+ ", resourceValue=" + resourceValue + "]";
		}
		
		
	}
	

	@Override
	public void run() {
		// while (status.get()) {
		JSONObject ret = new JSONObject();
		try {
			int partitionId = DateUtil.getCurrMonthForTypeDecimal();

			String nextMonth = DateUtil.getBeginningOfCurrMonth();
			// String nextMonth = DateUtil.getBeginningOfNextMonth();
			// B值生效失效时间
			String endMonth = DateUtil.getBValueExpDate(DateUtil.getCalendarOfGivenDate(nextMonth));
			// String endMonth = DateUtil.getEndOfGivenMonth(nextMonth);
			String cycleIdDate = nextMonth.substring(0, 6);
			log.debug("AutoProcess- nextMonth[" + nextMonth + "]");
			log.debug("AutoProcess- endMonth[" + endMonth + "]");
			log.debug("AutoProcess- cycleIdDate[" + cycleIdDate + "]");

			RechargeInfo rechargeInfo = new RechargeInfo();
			
			// 查询有效的记录，时间按是下个月
			List<LifeUserAutoExchange> datas = oper.getLifeUserAutoExchanges(nextMonth);

			if (!datas.isEmpty()) {
				//定义异动表用于接口人参
//				List<BalanceAccessLog> insertBalanceAccessLog=new ArrayList<BalanceAccessLog>();
				
				
				List<InfoPayBalance> newInfoPayBalances = new ArrayList<InfoPayBalance>();
				List<InfoPayBalance> infoPayBalances = new ArrayList<InfoPayBalance>();//add zxm 给自动兑换使用
				List<InfoPayBalanceBilling> infoPayBalancesBilling = new ArrayList<InfoPayBalanceBilling>();//add zxm 给billing发送信息使用
				List<BalanceAccessLog> balanceAccessLogs = new ArrayList<BalanceAccessLog>();
				List<LogTradeAutoExchangeHis> logTradeAutoExchangeHises = new ArrayList<LogTradeAutoExchangeHis>();
				for (LifeUserAutoExchange auto : datas) {
					log.debug("AutoProcess- " + auto.toString());

					String currDate = DateUtil.getSystemTime();

					log.debug("AutoProcess- currDate[" + currDate + "]");
				List<InfoPayBalance> updateInfoPayBalance=new ArrayList<InfoPayBalance>();

					newInfoPayBalances.clear();
					infoPayBalances.clear();//add zxm
					infoPayBalancesBilling.clear();//add zxm
					balanceAccessLogs.clear();
					logTradeAutoExchangeHises.clear();
					LogTradeHis logTradeHis = new LogTradeHis();

					String tradeId = Common.getUUID();
					String exchangeId = auto.getExchange_id();
					String userId = auto.getUser_id();
					log.debug("AutoProcess- 检查该exchangeid是否已经被处理过，防止重复赠送");
					if (oper.notfoundLogAutoExchangeLog(userId, exchangeId, cycleIdDate)) {
						log.debug("AutoProcess- 已经处理过的不在处理!!!!!!!!!!!!!!" + userId);
						continue;
					}
					LogAutoExchangeLog logAutoExchangeLog = new LogAutoExchangeLog();
					logAutoExchangeLog.setUser_id(userId);
					logAutoExchangeLog.setExchange_id(auto.getExchange_id());
					logAutoExchangeLog.setCycle_type(auto.getCycle_type());
					logAutoExchangeLog.setChannel_no(0);
					logAutoExchangeLog.setCycle_id(cycleIdDate);
					logAutoExchangeLog.setProcess_tag("2");
					logAutoExchangeLog.setComplete_time(currDate);
					// 查找关联表
					InfoUserExternalAccount infoUserExternalAccount = oper
							.getInfoUserExternalAccount(userId, nextMonth);
					if (infoUserExternalAccount == null) {
						// 不处理
						log.debug("关联表没有信息");
						continue;
					}
					
					
					//12yue版本新增
					//调用crm接口判断用户的状态
					
					
					
					

				
					
					//判断是不是乐购卡用户 add zxm
					List<InfoUserExternalAccountAttr> listattrs = oper.getExternalAccountAttrbyUserIdAndExternal(userId,infoUserExternalAccount.getExternal_account_id());
//					此处的判断需要否待定
//					boolean isLEGOU = false;
//					for(InfoUserExternalAccountAttr tmpattr:listattrs){
//						if(tmpattr.getAttr_code().equals("1002") && tmpattr.getAttr_value()!= null && tmpattr.getAttr_value().equals("02") 
//								&& tmpattr.getEff_date().compareTo(currDate)<=0 && tmpattr.getExp_date().compareTo(currDate)>=0){
//							isLEGOU = true;
//							break;
//						}
//					}
//					if(isLEGOU == true){
//						log.debug(infoUserExternalAccount.getJd_pin()+"用户是乐购卡，不能兑换资源");
//						continue;
//					}
					String exchangeMode=auto.getExchange_mode();
					if (auto.getExchange_mode().equals("1")) {
						log.info("AutoProcess- 兑换类型:按数量=1，不处理");

						oper.updateLogAutoExchangeLog(logAutoExchangeLog);
						continue;
					}

					String jdPin = infoUserExternalAccount.getJd_pin();
					String mobileNumber = infoUserExternalAccount.getExternal_account_code();
					log.info("AutoProcess- jdPin[" + jdPin + "]");
					log.info("AutoProcess- mobileNumber[" + mobileNumber + "]");

					long topValue = auto.getTop_b_value();

					log.debug("AutoProcess- 查询life_resource_list");
					List<LifeResourceList> resList = oper.getLifeResourceLists(userId,
							auto.getResource_list_id());
					
					if (resList == null || resList.size() ==0) {
						// 不处理
						log.debug(userId+"的LifeResourceList为空");
//						oper.updateLogAutoExchangeLog(logAutoExchangeLog);
						continue;
					}
					
					//判断用户是否为乐购卡无需判断合约
					//查找累积量历史表
					String acctMonth=currDate.substring(0,6);
					ExchangeRatableHistory exchangeRatableHistory=oper.getExchangeRatableHistory(userId,acctMonth); 
					//用于最后的更新累积量表
					ExchangeRatableHistory exchangeRatable=new ExchangeRatableHistory();
					boolean isLEGOU=false;
					boolean isHEYUE=false;
					for(InfoUserExternalAccountAttr tmpattr:listattrs){
						if(tmpattr.getAttr_code().equals("1002") && tmpattr.getAttr_value()!= null && tmpattr.getAttr_value().equals("02") 
								&& tmpattr.getEff_date().compareTo(currDate)<=0 && tmpattr.getExp_date().compareTo(currDate)>=0){
							isLEGOU = true;
							break;
						}
						if(tmpattr.getAttr_code().equals("1002") && tmpattr.getAttr_value()!= null && tmpattr.getAttr_value().equals("01") 
								&& tmpattr.getEff_date().compareTo(currDate)<=0 && tmpattr.getExp_date().compareTo(currDate)>=0){
							isHEYUE = true;
							break;
						}
					}
					
					//判断用户类型和兑换资源类型是否匹配
					LifeResourceList lifeExchangeResource=null;
					LifeResourceList lifeExchangeMoney=null;
					for(LifeResourceList life:resList){
						if(life.getResource_type_code().equals("ROV")||life.getResource_type_code().equals("ROF")){
								lifeExchangeResource=life;
							}
						if(life.getResource_type_code().equals("ROM")){
								lifeExchangeMoney=life;
							}
						}
					if(isLEGOU){
						if(lifeExchangeResource!=null){
//							oper.updateLogAutoExchangeLog(logAutoExchangeLog);
							log.error("乐购卡不能兑换资源");
							continue;
						}
					}
					if(isHEYUE){
						if(lifeExchangeMoney!=null){
							log.error("非乐购卡不能兑换金钱");
//							oper.updateLogAutoExchangeLog(logAutoExchangeLog);
							continue;
						}
					}
					long userBalance=0;
					log.debug("用户为乐购卡==========="+isLEGOU);
					if(isLEGOU){
					//判断用户的装填
						String MobileNumber=infoUserExternalAccount.getExternal_account_code();
						String ChannelType="107";
						//***************调用crm的QuerySubsStatus接口用于判断用户的装态******************
						PubInputDto pubInputDto=new PubInputDto();
						pubInputDto.setContactChannle(ChannelType);
						pubInputDto.setMSISDN(MobileNumber);
						log.debug("调用QuerySubsStatus的接口入参为 ========="+pubInputDto.toString());
						Date startTime=new Date();
						long start=startTime.getTime();
						
						try{
							OrderStatus queryResponse=querySubsStatus.QuerySubsStatus(pubInputDto);
							log.debug("crm service return :"+queryResponse.getStatus()+","+queryResponse.getSubsStatus());
							if (queryResponse.getStatus().equals("1")&&queryResponse.getSubsStatus().equals("A")) {
								log.debug("Exchange-调用crm查询状态接口成功，message:" + queryResponse.toString());
								Date endTime=new Date();
								long end=endTime.getTime();
								long time=end-start;
								log.debug("Exchange-调用crm查询状态接口，成功，耗时"+time+"毫秒");
								
							} else {
								log.debug("Exchange-调用crm查询状态接口失败，message:" + queryResponse.toString());
								log.error("Exchange-调用crm查询状态接口失败，trade_id[" + tradeId + "]");
								log.error("用户不属于A状态，兑换失败");
								continue;
							}
						}catch(Exception e){
							log.debug("调用QuerySubsStatusServ失败"+e.toString());
//							oper.updateLogAutoExchangeLog(logAutoExchangeLog);
							continue;
//							log.debug("调用QuerySubsStatusServ失败"+e.toString());
//							throw new Exception("调用QuerySubsStatusServ失败"+e.toString());
						}
					
					String attrValue=null;
					long maxAutoBalance=0;
					long ratableBalance=0;
					for(InfoUserExternalAccountAttr tmpattr:listattrs){
						if(tmpattr.getAttr_code().equals("1003") && tmpattr.getAttr_value()!= null&& tmpattr.getEff_date().compareTo(currDate)<=0 
								&& tmpattr.getExp_date().compareTo(currDate)>=0){
							attrValue=tmpattr.getAttr_value();
							break;
					}
				}
					if(attrValue==null||attrValue.length()==0){
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.NOT_FIND_PRODUCT);
						ret.put("ErrorMessage", "乐购卡用户激活首月不允许兑换");
						log.error("乐购卡用户激活首月不允许兑换");
//						oper.updateLogAutoExchangeLog(logAutoExchangeLog);
						continue;
					}
					log.debug("用户的主套餐为========="+attrValue);
					String domainCode="2000";
					String tradeTypeCode="104";
					Map<String,Object> filter = new HashMap<String,Object>();
		    		filter.put("domain_code", domainCode);
		    		filter.put("trade_type_code", tradeTypeCode);
					List<RuleParameters> ruleParameters=S.get(RuleParameters.class).query(Condition.build("byattrValue").filter(filter));
					if(ruleParameters==null||ruleParameters.isEmpty()){
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.NOT_FIND_PRODUCT);
						ret.put("ErrorMessage", "乐购卡用户套餐未生效，不允许兑换");
						log.error("乐购卡用户套餐未生效，不允许兑换");
//						oper.updateLogAutoExchangeLog(logAutoExchangeLog);
						continue;
					}
					
					//获取套餐对话 封顶值
					for(RuleParameters rule:ruleParameters){
						if(rule.getPara_name()==null){
//							oper.updateLogAutoExchangeLog(logAutoExchangeLog);
							continue;
						}else{
						if(rule.getPara_name().equals(attrValue)){
							if(rule.getPara_char4()==null){
//								oper.updateLogAutoExchangeLog(logAutoExchangeLog);
								continue;	
							}
							maxAutoBalance=Long.parseLong(rule.getPara_char4());
							break;
						}
						}
					}
					if(maxAutoBalance==0){
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.NOT_FIND_PRODUCT);
						ret.put("ErrorMessage", "乐购卡用户套餐未生效，不允许兑换");
						log.error("乐购卡用户套餐未生效，不允许兑换");
//						oper.updateLogAutoExchangeLog(logAutoExchangeLog);
						continue;
					}
					log.debug("套餐允许兑换的最大值为==========="+maxAutoBalance);
					//获取累积量
					if(exchangeRatableHistory==null){
						ratableBalance=0;
					}else{
						exchangeRatable.setAcct_month(acctMonth);
						exchangeRatable.setUpdate_time(currDate);
						exchangeRatable.setUser_id(userId);
						ratableBalance=exchangeRatableHistory.getRatable_balance();
					}
					userBalance=maxAutoBalance-ratableBalance;
					log.debug("用户允许兑换的b值数为===="+userBalance);
					if(userBalance<=0){
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.EXCHANGE_B_NOT_ENOUGH);
						ret.put("ErrorMessage", "兑换失败，本月还可兑换0B");
						log.error("兑换失败，本月还可兑换0B");
//						oper.updateLogAutoExchangeLog(logAutoExchangeLog);
						continue;
					}
					
				}
				//乐购卡判断完毕	
					
					
					long voiceResourceValue = 0;
					long streamResourceValue = 0;
					long messageResourceValue = 0;
					long moneyResourceValue =0;
					
//					List<Long> resourceValue=new ArrayList<Long>();
//					Map<String, Long>resourceValue=new HashMap<String, Long>();
//					
//					for (LifeResourceList resource : resList) {
//						String resourceTypeCode = resource.getResource_type_code();
//						if ("ROV".equalsIgnoreCase(resourceTypeCode)) {// 语音
//							voiceResourceValue = Long.parseLong(resource.getResource_value());
//							resourceValue.put("ROV", voiceResourceValue);
//						} else if ("ROF".equalsIgnoreCase(resourceTypeCode)) {// 流量
//							streamResourceValue = Long.parseLong(resource.getResource_value());
//							resourceValue.put("ROF",streamResourceValue);
//						} else if ("ROS".equalsIgnoreCase(resourceTypeCode)) {// 短信
//							messageResourceValue = Long.parseLong(resource.getResource_value());
//							resourceValue.put("ROS",messageResourceValue);
//						}else if ("ROM".equalsIgnoreCase(resourceTypeCode)) {// 话费
//							//目前没有兑换话费
////							log.debug("目前不支持兑换话费");
////							continue;
//							moneyResourceValue = Long.parseLong(resource.getResource_value());
//							resourceValue.put("ROM",moneyResourceValue);
//						}
//						
//					}
					//将数据按兑换顺序放入新map
//					Map<String, Long>reValue=new HashMap<String, Long>();
//					reValue.put("ROM", moneyResourceValue);
//					reValue.put("ROS", messageResourceValue);
//					reValue.put("ROF", streamResourceValue);
//					reValue.put("ROV", voiceResourceValue);
					List<ResourceExchangeInfo> resourceExchangeInfo=new ArrayList<ResourceExchangeInfo>();
					
					for (LifeResourceList resource : resList) {
						String resourceTypeCode = resource.getResource_type_code();
						if ("ROV".equalsIgnoreCase(resourceTypeCode)) {// 语音
							voiceResourceValue = Long.parseLong(resource.getResource_value());
							ResourceExchangeInfo rei_voice=new ResourceExchangeInfo();
							rei_voice.setResourceType(resourceTypeCode);
							rei_voice.setResourceValue(voiceResourceValue);
							resourceExchangeInfo.add(rei_voice);
//							resourceValue.put("ROV", voiceResourceValue);
						} else if ("ROF".equalsIgnoreCase(resourceTypeCode)) {// 流量
							streamResourceValue = Long.parseLong(resource.getResource_value());
							ResourceExchangeInfo rei_stream=new ResourceExchangeInfo();
							rei_stream.setResourceType(resourceTypeCode);
							rei_stream.setResourceValue(streamResourceValue);
							resourceExchangeInfo.add(rei_stream);
//							resourceValue.put("ROF",streamResourceValue);
						} else if ("ROS".equalsIgnoreCase(resourceTypeCode)) {// 短信
							messageResourceValue = Long.parseLong(resource.getResource_value());
							ResourceExchangeInfo rei_message=new ResourceExchangeInfo();
							rei_message.setResourceType(resourceTypeCode);
							rei_message.setResourceValue(messageResourceValue);
							resourceExchangeInfo.add(rei_message);
//							resourceValue.put("ROS",messageResourceValue);
						}else if ("ROM".equalsIgnoreCase(resourceTypeCode)) {// 话费
							//目前没有兑换话费
//							log.debug("目前不支持兑换话费");
//							continue;
							moneyResourceValue = Long.parseLong(resource.getResource_value());
							ResourceExchangeInfo rei_money=new ResourceExchangeInfo();
							rei_money.setResourceType(resourceTypeCode);
							rei_money.setResourceValue(moneyResourceValue);
							resourceExchangeInfo.add(rei_money);
//							resourceValue.put("ROM",moneyResourceValue);
						}
						
					}
					
					List<ResourceExchangeInfo> resourceList = new ArrayList<ResourceExchangeInfo>(); //资源兑换列表，优先级顺序： 语音>流量>短信    话费只能乐购卡单独兑换，并且不能与其他资源同时兑换，和排序无关
					resourceList.add(new ResourceExchangeInfo("ROV",voiceResourceValue));
					resourceList.add(new ResourceExchangeInfo("ROF",streamResourceValue));
					resourceList.add(new ResourceExchangeInfo("ROS",messageResourceValue));
					resourceList.add(new ResourceExchangeInfo("ROM",moneyResourceValue));
					log.debug("=========resourceList=========="+resourceList.toString());
			
					log.debug("AutoProcess- 资源语音voiceResourceValue[" + voiceResourceValue + "]");
					log.debug("AutoProcess- 资源流量streamResourceValue[" + streamResourceValue + "]");
					log.debug("AutoProcess- 资源短信messageResourceValue[" + messageResourceValue + "]");
					log.debug("AutoProcess- 话费moneyResourceValue[" + moneyResourceValue + "]");

					//因为多了3账本，后面代码做部分修改 add zxm
					// 查询当前可用账本
					log.debug("AutoProcess- 查询所有当前可用余额");
					//修改账本查询
				  infoPayBalances=oper.getInfoPayBalanceByManager(userId,
							 currDate);
					
					log.debug("infoPayBalances=========="+infoPayBalances.toString());
//					List<InfoPayBalance> infoPayBalances_0 = oper.getInfoPayBalance(userId, "0",
//							currDate);
//					List<InfoPayBalance> infoPayBalances_3 = oper.getInfoPayBalance(userId, "3",
//							currDate);

//					long balance_0 = 0;
//					long balance_3 = 0;
//					long balance = 0;//0和3账本的全部余额
//					for (InfoPayBalance i : infoPayBalances_3) {
//						balance_3 += i.getBalance();
//						infoPayBalances.add(i);
//					}
//					for (InfoPayBalance i : infoPayBalances_0) {
//						balance_0 += i.getBalance();
//						infoPayBalances.add(i);
//					}
//					balance = balance_0 + balance_3;
//					log.info("AutoProcess- 总余额balance[" + balance + "]");
					long balance=0;
					for(InfoPayBalance info:infoPayBalances){
						balance=balance+info.getBalance();
					}
					log.debug("balance========"+balance);
					//
					if (infoPayBalances.isEmpty()) {
						log.info("AutoProcess- 账本不存在，不兑换");

						oper.updateLogAutoExchangeLog(logAutoExchangeLog);
						continue;
					}
					if (balance <= 0) {
						log.info("AutoProcess- 余额小于等于0，不兑换");

						oper.updateLogAutoExchangeLog(logAutoExchangeLog);

						continue;
					}
					
					
					// 排序
//					this.sortInfoPayBalanceByIdAndExpdate(infoPayBalances);//modify zxm
//					if (log.isDebugEnabled()) {
//						log.debug("AutoProcess- 排序后的账本");
//						for (InfoPayBalance i : infoPayBalances) {
//							log.debug("AutoProcess- Sorted:" + i.toString());
//						}
//					}

					long voice = 0;
					long stream = 0;
					long message = 0;
					long money =0;

					{// 赋值log_trade_his
						logTradeHis.setTrade_id(tradeId);
						logTradeHis.setTrade_type_code(TRADE_TYPE_CODE);
						logTradeHis.setExternal_system_code("10000");
						logTradeHis.setChannel_type("109");
						logTradeHis.setUser_id(userId);
						logTradeHis.setPartition_id(partitionId);
						logTradeHis.setOrder_no("0");
						logTradeHis.setOrder_type("");
						logTradeHis.setOrder_amount(0);
						logTradeHis.setOrder_completion_time(currDate);
						logTradeHis.setBalance_type_id(0);
						logTradeHis.setUnit_type_id(0);// 0 B值
					   // logTradeHis.setBalance();
						logTradeHis.setProcess_tag(2);
						logTradeHis.setTrade_time(currDate);
						logTradeHis.setProcess_time(currDate);
						logTradeHis.setRemark("自动兑换成功");

					}
					long min=0;
					if (auto.getPurchase_mode().equals("001")) {// 全部
						log.debug("AutoProcess- 兑换方式，全部兑换");
						//
					if(isLEGOU){
						min=balance-userBalance;
						if(min>0){
							balance=userBalance;
							log.debug("乐购卡本次兑换的B值为=="+balance);
							if(exchangeRatableHistory!=null){
								exchangeRatable.setRatable_balance(userBalance);	
							}
						}else{
							if(exchangeRatableHistory!=null){
								exchangeRatable.setRatable_balance(balance);	
							}
							log.debug("乐购卡本次兑换的B值为=="+balance);
							log.debug("AutoProcess- 兑换方式，封顶兑换，兑换允许兑换的余额");
						}
					}else{
						log.debug("非乐购卡用户本次兑换的B值为=="+balance);
						log.debug("AutoProcess- 兑换方式，全部兑换，兑换所有余额");
					}
						
						//调用方法进行兑换
						int type=0;
//						long bValue=tmpResource.getResourceValue();
						int changeType=2;
						InfoPayBalanceManager infoPayBalanceManage=new InfoPayBalanceManager();
//						List<InfoPayBalance> updateInfoPayBalance=new ArrayList<InfoPayBalance>();
						List<InfoPayBalance> insertInfoPayBalance=new ArrayList<InfoPayBalance>();
						List<BalanceAccessLog> insertBalanceAccessLog=new ArrayList<BalanceAccessLog>();
						int code=infoPayBalanceManage.manage(userId, type, changeType, balance, updateInfoPayBalance, insertInfoPayBalance, insertBalanceAccessLog);
						if(code==1){
							log.debug("========B值扣减成功========");
						}else{
							ret.put("Status", Constants.STATUS_FAILURE);
							ret.put("ErrorCode", Constants.TRADE_EXCHANGE_FAIL);
							ret.put("ErrorMessage", "B值扣减失败");
							log.error("B值扣减失败");
//							oper.updateLogAutoExchangeLog(logAutoExchangeLog);
							continue;
						}
						
						log.debug("manage返回更新账本数量:"+updateInfoPayBalance.size());
						
						for(InfoPayBalance infoPB:updateInfoPayBalance){
							if (infoPB.getBalance() == 0) {
								log.debug("AutoProcess- 余额为0， 跳过");
								//不记录
//								oper.updateLogAutoExchangeLog(logAutoExchangeLog);
								continue;
							}
							InfoPayBalance pay=new InfoPayBalance(infoPB);
							pay.setBalance(infoPB.getBalance());
							newInfoPayBalances.add(pay);
						}
						for(BalanceAccessLog acc:insertBalanceAccessLog){
							BalanceAccessLog access = new BalanceAccessLog();
							access.setTrade_Id(tradeId);
							access.setTrade_Type_Code(TRADE_TYPE_CODE);
							access.setUser_Id(acc.getUser_Id());
							access.setPartition_Id(acc.getPartition_Id());
							access.setBalance_Id(acc.getBalance_Id());
							access.setBalance_Type_Id(acc.getBalance_Type_Id());
							access.setAccess_Tag(REFUND);
							access.setMoney(acc.getMoney());
							access.setOperate_Time(acc.getOperate_Time());
							access.setOld_Balance(acc.getOld_Balance());
							access.setNew_Balance(acc.getNew_Balance());
							log.debug("======access========="+access.toString());
							balanceAccessLogs.add(access);
						}
//						
						
						
						
						
						long newBalance = 0;
						long oldBalance = 0;
						voice = (long) Math.ceil(balance * voiceResourceValue / 100.0);// 语音向上取整
						stream = (long) Math.floor(balance * streamResourceValue / 100.0);// 流量向上取整
						money = (long) Math.floor(balance * moneyResourceValue / 100.0) ;
						

						logTradeHis.setBalance(-balance);
						// 所有账本都设置为0
						for (InfoPayBalance pay : infoPayBalances) {
							if (pay.getBalance() == 0) {
								log.debug("AutoProcess- 余额为0， 跳过");
//								oper.updateLogAutoExchangeLog(logAutoExchangeLog);
								continue;
							}

//							BalanceAccessLog access = new BalanceAccessLog();
//							access.setTrade_Id(tradeId);
//							access.setTrade_Type_Code(TRADE_TYPE_CODE);
//							access.setUser_Id(userId);
//							access.setPartition_Id(partitionId);
//							access.setBalance_Id(pay.getBalance_id());
//							access.setBalance_Type_Id(pay.getBalance_type_id());
//							access.setAccess_Tag(REFUND);
//							access.setMoney(pay.getBalance());
//							access.setOperate_Time(currDate);

							oldBalance = pay.getBalance();
							pay.setBalance(-pay.getBalance());
							newBalance = 0;// 可以去掉

//							access.setOld_Balance(oldBalance);
//							access.setNew_Balance(newBalance);

							// list add
//							newInfoPayBalances.add(pay);
//							balanceAccessLogs.add(access);
						}

						logTradeAutoExchangeHises=getLogTradeAutoExchangeHis(updateInfoPayBalance, resourceList, tradeId, auto, balance,exchangeMode);
					} else if (auto.getPurchase_mode().equals("002")) {// 封顶
						log.debug("AutoProcess- 兑换方式，封顶兑换");
						if (topValue > balance) {
							// 兑换所有余额
							long deductValue;
							if(isLEGOU){
								min=balance-userBalance;
								if(min>0){
									deductValue=userBalance;
									log.debug("乐购卡本次兑换的B值为=="+deductValue);
									if(exchangeRatableHistory!=null){
									exchangeRatable.setRatable_balance(userBalance);
									}
									logTradeHis.setBalance(-userBalance);
								}else{
									if(exchangeRatableHistory!=null){
										exchangeRatable.setRatable_balance(balance);
										}
									log.debug("乐购卡本次兑换的B值为=="+balance);
									deductValue=balance;
									logTradeHis.setBalance(-balance);
									log.debug("AutoProcess- 兑换方式，封顶兑换，兑换允许兑换的余额");
								}
							}else{
								log.debug("非乐购卡用户本次兑换的B值为=="+balance);
								deductValue=balance;
								logTradeHis.setBalance(-balance);
								log.debug("AutoProcess- 兑换方式，封顶兑换，兑换所有余额");
							}
							
							
							
							
							//调用方法进行兑换
							int type=0;
//							long bValue=tmpResource.getResourceValue();
							int changeType=2;
							InfoPayBalanceManager infoPayBalanceManage=new InfoPayBalanceManager();
							
							List<InfoPayBalance> insertInfoPayBalance=new ArrayList<InfoPayBalance>();
							List<BalanceAccessLog> insertBalanceAccessLog=new ArrayList<BalanceAccessLog>();
							int code=infoPayBalanceManage.manage(userId, type, changeType, deductValue, updateInfoPayBalance, insertInfoPayBalance, insertBalanceAccessLog);
							if(code==1){
								log.debug("========B值扣减成功========");
							}else{
								ret.put("Status", Constants.STATUS_FAILURE);
								ret.put("ErrorCode", Constants.TRADE_EXCHANGE_FAIL);
								ret.put("ErrorMessage", "B值扣减失败");
								log.error("B值扣减失败");
//								oper.updateLogAutoExchangeLog(logAutoExchangeLog);
								continue;
							}
							
							
							log.debug("manage返回更新账本数量:"+updateInfoPayBalance.size());
							
							
							for(InfoPayBalance infoPB:updateInfoPayBalance){
								if (infoPB.getBalance() == 0) {
									log.debug("AutoProcess- 余额为0， 跳过");
//									oper.updateLogAutoExchangeLog(logAutoExchangeLog);
									continue;
								}
								InfoPayBalance pay=new InfoPayBalance(infoPB);
								pay.setBalance(infoPB.getBalance());
								newInfoPayBalances.add(pay);
							}
							for(BalanceAccessLog acc:insertBalanceAccessLog){
								BalanceAccessLog access = new BalanceAccessLog();
								access.setTrade_Id(tradeId);
								access.setTrade_Type_Code(TRADE_TYPE_CODE);
								access.setUser_Id(acc.getUser_Id());
								access.setPartition_Id(acc.getPartition_Id());
								access.setBalance_Id(acc.getBalance_Id());
								access.setBalance_Type_Id(acc.getBalance_Type_Id());
								access.setAccess_Tag(REFUND);
								access.setMoney(acc.getMoney());
								access.setOperate_Time(acc.getOperate_Time());
								access.setOld_Balance(acc.getOld_Balance());
								access.setNew_Balance(acc.getNew_Balance());
								balanceAccessLogs.add(access);
							}
							
							
							
							
							
							long newBalance = 0;
							long oldBalance = 0;
							voice = (long) Math.ceil(balance * voiceResourceValue / 100.0);// 语音向上取整
							stream = (long) Math.floor(balance * streamResourceValue / 100.0);// 流量向上取整

							// 所有账本都设置为0
							for (InfoPayBalance pay : infoPayBalances) {
								if (pay.getBalance() == 0) {
									log.debug("AutoProcess- 余额为0， 跳过");	
									continue;
								}

//								BalanceAccessLog access = new BalanceAccessLog();
//								access.setTrade_Id(tradeId);
//								access.setTrade_Type_Code(TRADE_TYPE_CODE);
//								access.setUser_Id(userId);
//								access.setPartition_Id(partitionId);
//								access.setBalance_Id(pay.getBalance_id());
//								access.setBalance_Type_Id(pay.getBalance_type_id());
//								access.setAccess_Tag(REFUND);
//								access.setMoney(pay.getBalance());
//								access.setOperate_Time(currDate);

								oldBalance = pay.getBalance();
								pay.setBalance(-pay.getBalance());
								newBalance = 0;// 可以去掉

//								access.setOld_Balance(oldBalance);
//								access.setNew_Balance(newBalance);

								// list add
//								newInfoPayBalances.add(pay);
//								balanceAccessLogs.add(access);
							}
							
							logTradeAutoExchangeHises=getLogTradeAutoExchangeHis(updateInfoPayBalance, resourceList, tradeId, auto, balance,exchangeMode);
							log.debug("========logTradeAutoExchangeHises==========="+logTradeAutoExchangeHises.toString());
						} else {// 只兑换封顶值
							
							
							if(isLEGOU){
								min=topValue-userBalance;
								if(min>0){
									topValue=userBalance;
									log.debug("乐购卡本次兑换的B值为=="+userBalance);
									if(exchangeRatableHistory!=null){
									exchangeRatable.setRatable_balance(userBalance);
									}
									logTradeHis.setBalance(-userBalance);
								}else{
									if(exchangeRatableHistory!=null){
									exchangeRatable.setRatable_balance(topValue);
									}
									log.debug("乐购卡本次兑换的B值为=="+topValue);
									log.debug("AutoProcess- 兑换方式，封顶兑换，只兑换允许兑换的值");
									logTradeHis.setBalance(-topValue);
								}
							}else{
								log.debug("AutoProcess- 兑换方式，封顶兑换，只兑换封顶值");
								log.debug("非乐购卡用户本次兑换的B值为=="+topValue);
								logTradeHis.setBalance(-topValue);
							}
						
							
							int type=0;
//							long bValue=tmpResource.getResourceValue();
							int changeType=2;
							InfoPayBalanceManager infoPayBalanceManage=new InfoPayBalanceManager();
//							List<InfoPayBalance> updateInfoPayBalance=new ArrayList<InfoPayBalance>();
							List<InfoPayBalance> insertInfoPayBalance=new ArrayList<InfoPayBalance>();
							List<BalanceAccessLog> insertBalanceAccessLog=new ArrayList<BalanceAccessLog>();
							int code=infoPayBalanceManage.manage(userId, type, changeType, topValue, updateInfoPayBalance, insertInfoPayBalance, insertBalanceAccessLog);
							if(code==1){
								log.debug("========B值扣减成功========");
							}else{
								ret.put("Status", Constants.STATUS_FAILURE);
								ret.put("ErrorCode", Constants.TRADE_EXCHANGE_FAIL);
								ret.put("ErrorMessage", "B值扣减失败");
								log.error("B值扣减失败");
//								oper.updateLogAutoExchangeLog(logAutoExchangeLog);
								continue;
							}
							
							log.debug("manage返回更新账本数量:"+updateInfoPayBalance.size());
							
							
							for(InfoPayBalance infoPB:updateInfoPayBalance){
								if (infoPB.getBalance() == 0) {
									log.debug("AutoProcess- 余额为0， 跳过");
									continue;
								}
								InfoPayBalance pay=new InfoPayBalance(infoPB);
								pay.setBalance(infoPB.getBalance());
								newInfoPayBalances.add(pay);
							}
							for(BalanceAccessLog acc:insertBalanceAccessLog){
								BalanceAccessLog access = new BalanceAccessLog();
								access.setTrade_Id(tradeId);
								access.setTrade_Type_Code(TRADE_TYPE_CODE);
								access.setUser_Id(acc.getUser_Id());
								access.setPartition_Id(acc.getPartition_Id());
								access.setBalance_Id(acc.getBalance_Id());
								access.setBalance_Type_Id(acc.getBalance_Type_Id());
								access.setAccess_Tag(REFUND);
								access.setMoney(acc.getMoney());
								access.setOperate_Time(acc.getOperate_Time());
								access.setOld_Balance(acc.getOld_Balance());
								access.setNew_Balance(acc.getNew_Balance());
								balanceAccessLogs.add(access);
							}
							
							
							
							
							
							logTradeHis.setBalance(-topValue);
							voice = (long) Math.ceil(topValue * voiceResourceValue / 100.0);// 语音向上取整
							stream = (long) Math.floor(topValue * streamResourceValue / 100.0);// 流量向上取整

							// 所有账本都设置为0
//							for (InfoPayBalance pay : infoPayBalances) {
//								long newBalance = 0;
//								long oldBalance = 0;
//								long currBalance = pay.getBalance();
//								if (currBalance == 0) {
//									continue;
//								}
////								BalanceAccessLog access = new BalanceAccessLog();
////								access.setTrade_Id(tradeId);
////								access.setTrade_Type_Code(TRADE_TYPE_CODE);
////								access.setUser_Id(userId);
////								access.setPartition_Id(partitionId);
////								access.setBalance_Id(pay.getBalance_id());
////								access.setBalance_Type_Id(pay.getBalance_type_id());
////								access.setAccess_Tag(REFUND);
////
////								access.setOperate_Time(currDate);
//
//								oldBalance = currBalance;
////								access.setOld_Balance(oldBalance);
//
//								if (currBalance >= topValue) {
//									pay.setBalance(-topValue);
//									newBalance = currBalance - topValue;
//									newInfoPayBalances.add(pay);
//									log.info("AutoProcess- 当前扣减账本:" + pay.toString());
////									access.setMoney(topValue);
////									access.setNew_Balance(newBalance);
//									topValue = 0;
//
////									balanceAccessLogs.add(access);
//									break;// TODO
//								}
//								topValue -= currBalance;
//								pay.setBalance(-currBalance);
//								newBalance = 0;
//								log.info("AutoProcess- 当前扣减账本:" + pay.toString());
////								access.setMoney(currBalance);
////								access.setNew_Balance(newBalance);
//
////								newInfoPayBalances.add(pay);
////								balanceAccessLogs.add(access);
//							}
							logTradeAutoExchangeHises=getLogTradeAutoExchangeHis(updateInfoPayBalance, resourceList, tradeId, auto, topValue,exchangeMode);
							log.debug("==============logTradeAutoExchangeHises=============="+logTradeAutoExchangeHises.toString());
						}// top value

					}// end if

					
					log.debug("SKU-refund- balance_access_log" + balanceAccessLogs.toString());
					log.info("SKU-refund- 更新表");
					log.debug("logTradeAutoExchangeHises============="+logTradeAutoExchangeHises.toString());
					oper.updateAutoProcess(logTradeHis, logTradeAutoExchangeHises, newInfoPayBalances, //infoPayBalances,
							balanceAccessLogs, logAutoExchangeLog,currDate, isLEGOU, exchangeRatable);
					// 更新redis
					log.info("SKU-refund 更新redis");
					for (LogTradeAutoExchangeHis l : logTradeAutoExchangeHises) {
						if (l.getResource_type_code().equals("ROV")) {
							l.setResource_value(voice + "");
						} else if (l.getResource_type_code().equals("ROF")) {
							l.setResource_value(stream + "");
						} else if (l.getResource_type_code().equals("ROS")) {
							l.setResource_value(message + "");
						}
						log.debug("++++++++++++++++"+l.getResource_type_code()+"==="+l.getResource_value());
					}
//					for (InfoPayBalance e : newInfoPayBalances) {
//						if (sync.sync(e) < 0) {
//							log.error("SKU-refund 更新失败");
//						}
//					}
					for(InfoPayBalance in:updateInfoPayBalance){
						log.debug("djksfhsk"+logTradeAutoExchangeHises.size());
						log.debug("++++++++++++++++"+in.toString());
					}
					for(ResourceExchangeInfo reInfo:resourceExchangeInfo){
						String typeCode=reInfo.getResourceType();
						log.debug("========="+typeCode+"======"+reInfo.resourceValue);
					}
					//发送短信
					AutoExchangeSmsSend autoSms=new AutoExchangeSmsSend();
					autoSms.setMsisdn(mobileNumber);
					autoSms.setAcctmonth(cycleIdDate);
					autoSms.setVoice(voice);
					autoSms.setStream(stream);
					autoSms.setMoney(money+"");
					autoSms.setResvalue(balance-voice-stream-money);
					autoSms.setNote("autoexchange");
					
					oper.insertAutoExchangeMessage(autoSms);
					
					// TODO 调用billing兑换
					{
						long tmp = 0;//记录兑换剩余的 
						int type = 1;//记录兑换的资源类型 1为语音，2为流量  ,3为话费
						long source[]={voice, stream,money};
						for (long value : source) {
							
							if(value ==0){
								type++;
								continue;
								
							}
							
							for(int i=0; i<infoPayBalances.size(); i++) {
								if( infoPayBalances.get(i).getBalance() < 0){
									infoPayBalances.get(i).setBalance(infoPayBalances.get(i).getBalance()*(-1));//前面为了更新余额表，写成了负值
								}
								if(infoPayBalances.get(i).getBalance() == 0){
									continue;
								}
								log.debug("balance="+ infoPayBalances.get(i).getBalance());
								log.debug("value="+ value);
								tmp = infoPayBalances.get(i).getBalance() - value;
								if(tmp >= 0){
									//兑换完，B值还有剩余
									infoPayBalances.get(i).setBalance(tmp);
									InfoPayBalanceBilling ipbb = new InfoPayBalanceBilling();
									ipbb.setBalance(value);
									ipbb.setType(type);
									ipbb.setEff_date(infoPayBalances.get(i).getEff_date());
									ipbb.setExp_date(infoPayBalances.get(i).getExp_date());
									log.debug("add > 0");
									infoPayBalancesBilling.add(ipbb);
									break;
								}
								if(tmp < 0){
									//不够兑换，资源还有剩余
									InfoPayBalanceBilling ipbb = new InfoPayBalanceBilling();
									ipbb.setBalance(infoPayBalances.get(i).getBalance());
									ipbb.setType(type);
									ipbb.setEff_date(infoPayBalances.get(i).getEff_date());
									ipbb.setExp_date(infoPayBalances.get(i).getExp_date());
									log.debug("add < 0");
									infoPayBalancesBilling.add(ipbb);
									value = value - infoPayBalances.get(i).getBalance();//剩余的资源
									infoPayBalances.get(i).setBalance(0);
								}
							}
							type++;
						}
						log.debug("infoPayBalancesBilling.size=" + infoPayBalancesBilling.size());
						
						for (InfoPayBalanceBilling l : infoPayBalancesBilling) {
							log.debug(l.toString());
						}
						
						
						//根据类型和失效期整合兑换的帐本
//						List<InfoPayBalanceBilling> infoPayBalancesBillingTmp = new ArrayList<InfoPayBalanceBilling>();//add zxm 给billing发送信息使用
//						infoPayBalancesBillingTmp.clear();
//						for(int i=0; i<infoPayBalancesBilling.size(); i++){
//							int tab = 0;
//							for(int j=0; j<infoPayBalancesBillingTmp.size(); j++){
//								if(infoPayBalancesBillingTmp.get(j).compare(infoPayBalancesBilling.get(i))){
//									log.debug("is equal");
//									long bal = infoPayBalancesBillingTmp.get(j).getBalance()+infoPayBalancesBilling.get(i).getBalance();
//									infoPayBalancesBillingTmp.get(j).setBalance(bal);
//									tab = 1;
//								}
//								log.debug("no equal");
//							}
//							if(tab == 0){
//								log.debug("no have");
//								infoPayBalancesBillingTmp.add(infoPayBalancesBilling.get(i));
//							}
//						}
						
						/****** billing interface *******/
						//兑换
						BValueExchangeRequest request = new BValueExchangeRequest();
						request.setSn(tradeId);
						request.setMSISDN(mobileNumber);
						request.setJDPin(jdPin);
						request.setContactChannel("109");

						//Calendar cal = DateUtil.getCalendarOfGivenDate(nextMonth);
						//String effDate = DateUtil.dateToString(cal).substring(0, 8);
						//String expDate = DateUtil.getBValueExpDate(cal).substring(0, 8);

						List<PackageDetailDto> packages = new ArrayList<PackageDetailDto>();
						List<RechargeInfoDto> rechargeInfoDtoList = new ArrayList<RechargeInfoDto>();
						
						for (InfoPayBalanceBilling ipbb : infoPayBalancesBilling) {
							log.debug("Exchange-兑换类型:" + ipbb.getType() + "。兑换数量:" + ipbb.getBalance() + "。兑换失效期"+ ipbb.getExp_date());
							PackageDetailDto dto = new PackageDetailDto();
							dto.setPackageType(ipbb.getType());
							if(ipbb.getType()==1){
								dto.setQuantity(ipbb.getBalance());
								dto.setDescription("语音");
								dto.setEffDate(ipbb.getEff_date());
								dto.setExpDate(ipbb.getExp_date());
								packages.add(dto);
							}
							if(ipbb.getType()==2){
								dto.setQuantity(ipbb.getBalance()*1024);
								dto.setDescription("流量");
								dto.setEffDate(ipbb.getEff_date());
								dto.setExpDate(ipbb.getExp_date());
								packages.add(dto);
							}
							if(ipbb.getType()==3){
								RechargeInfoDto rechargeInfoDto = new RechargeInfoDto ();
								rechargeInfoDto.setRechargeType(RECHARGE_TYPE);
								rechargeInfoDto.setAmount(ipbb.getBalance()*10);  // 话费汇率 1:10  zhanghb modify  money
								rechargeInfoDto.setEffDate(ipbb.getEff_date());
								rechargeInfoDto.setExpDate(ipbb.getExp_date());
								
								rechargeInfoDtoList.add(rechargeInfoDto);
								log.debug("rechargeInfoDtoList======"+rechargeInfoDtoList.toString());
							}
							
							
						}
						
						/*
						// 语音
						if (voice != 0) {
							log.debug("Exchange-兑换语音:" + voice);
							PackageDetailDto voiceDto = new PackageDetailDto();
							voiceDto.setPackageType(1);
							voiceDto.setQuantity(voice);// MB
							voiceDto.setDescription("语音");
							voiceDto.setEffDate(effDate);
							voiceDto.setExpDate(expDate);
							packages.add(voiceDto);
						}

						// 流量
						if (stream != 0) {
							log.debug("Exchange-兑换流量:" + stream * 1024);
							PackageDetailDto streamDto = new PackageDetailDto();
							streamDto.setPackageType(2);
							streamDto.setQuantity(stream * 1024);
							streamDto.setDescription("流量");
							streamDto.setEffDate(effDate);
							streamDto.setExpDate(expDate);
							packages.add(streamDto);
						}
						// 短信
						if (message != 0) {
							log.debug("Exchange-兑换短信:" + message);
							PackageDetailDto messageDto = new PackageDetailDto();
							messageDto.setPackageType(3);
							messageDto.setQuantity(message);
							messageDto.setDescription("短信");
							messageDto.setEffDate(effDate);
							messageDto.setExpDate(expDate);
							packages.add(messageDto);
						}*/
						log.debug("================packages================"+packages.toString());
						request.setPackageDetailDtoList(packages);
						
						if(packages.size()>0){ //兑换资源
							log.debug("Exchange-调用billng兑换接口"+request.toString());
							// 调用兑换接口
							BaseResponse response = doExchange.doExchange(request);
							log.debug("Exchange-调用billng兑换，result:" + response.toString());
							// 失败
							if (response.getStatus().equals("0")) {
								log.debug("Exchange-调用billng兑换接口失败，message:" + response.toString());
								log.error("Exchange-调用billng兑换接口失败，trade_id[" + tradeId + "]");
							} else {
								log.debug("Exchange-调用billng兑换接口，成功");
							}
						}
 
						if(rechargeInfoDtoList.size()>0){//兑换话费
							rechargeInfo.setMSISDN(mobileNumber);
							//rechargeInfo.setCallBackUrl("");
							rechargeInfo.setSn(tradeId);
							rechargeInfo.setRechargeInfoDtoList(rechargeInfoDtoList);
							rechargeInfo.setUserEventCode("88");
							rechargeInfo.setContactChannle("109");
							rechargeInfo.setJdAcctNbr(jdPin);
							
							
							log.debug("调计费充值接口入参："+rechargeInfo.toString());
							
							RechargeResult rechargeResult = reCharge.recharge(rechargeInfo);
							
							if(rechargeResult.getStatus().equals("1")){  //充值成功
								log.debug("Exchange-调用billng充值接口，成功");
							}else{
								log.debug("Exchange-调用billng充值接口失败，message:" + rechargeResult.getErrorMessage());
								log.error("Exchange-调用billng充值接口失败，trade_id[" + tradeId + "]"+rechargeInfo.toString());
							}

						}
						
						
					 
					}
				}
				
			} else {
				log.info("AutoProcess- ############life_user_auto_exchange无数据#############");
			}

			log.info("AutoProcess- 自动兑换OK...");

		} catch (Exception e) {
			log.error("AutoProcess- 处理过程中有异常，退出");
			log.error("AutoProcess- "+e.getMessage(),e);
			e.printStackTrace();
		}
		// }
	}

	/**
	 * 
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
	
	
	
	
	
	
	
	
	//将日志的结束设置为账本的结束时间
	private List<LogTradeAutoExchangeHis> getLogTradeAutoExchangeHis(List<InfoPayBalance> updateInfoPayBalance,List<ResourceExchangeInfo> resourceList,String tradeId,LifeUserAutoExchange auto,long balance,String exchangeMode){
		log.debug("==============reValue========="+resourceList.toString());
		List<LogTradeAutoExchangeHis> logTradeAutoExchangeHises=new ArrayList<LogTradeAutoExchangeHis>();
		String userId=auto.getUser_id();
		String nextMonth = DateUtil.getBeginningOfCurrMonth();
		int partitionId = DateUtil.getCurrMonthForTypeDecimal();
		String cycleIdDate = nextMonth.substring(0, 6);
		String currDate = DateUtil.getSystemTime();
		List<InfoPayBalance> newUpdateInfoPayBalance=new ArrayList<InfoPayBalance>();
		for(InfoPayBalance info:updateInfoPayBalance){
			InfoPayBalance infoPayBalance=new InfoPayBalance(info);
			infoPayBalance.setBalance(-info.getBalance());
			newUpdateInfoPayBalance.add(infoPayBalance);
		}
		for(ResourceExchangeInfo rExchange:resourceList){
			long resource=rExchange.getResourceValue();
			log.debug("==========resource=========="+resource);
			if (resource!=0) {
				String typeCode=rExchange.getResourceType();
				log.debug("=========typeCode==========");
				long deductValue=0;
				double deductValue_d=0;
				if(exchangeMode.equals("2")){
					 deductValue_d=Math.ceil((double)balance*resource/100);
					 deductValue=(long)deductValue_d;
				}else if(exchangeMode.equals("1")){
					deductValue=resource;
				}
				for(InfoPayBalance in:newUpdateInfoPayBalance){
					if (in.getBalance()<=0) {
						continue;
					}
					if (deductValue==0) {
						break;
					}
					if (in.getBalance()>=deductValue) {
						LogTradeAutoExchangeHis logTradeAutoExchangeHis = new LogTradeAutoExchangeHis();
						logTradeAutoExchangeHis.setTrade_id(tradeId);
						logTradeAutoExchangeHis.setTrade_type_code(TRADE_TYPE_CODE);
						logTradeAutoExchangeHis.setUser_id(userId);
						logTradeAutoExchangeHis.setPartition_id(partitionId);
						logTradeAutoExchangeHis.setExchange_id(auto.getExchange_id());
						logTradeAutoExchangeHis.setExchange_cycle_id(cycleIdDate);
						logTradeAutoExchangeHis.setResource_type_code(typeCode);
						logTradeAutoExchangeHis.setResource_value(""+deductValue);
						logTradeAutoExchangeHis.setEff_date(in.getEff_date());
						logTradeAutoExchangeHis.setExp_date(in.getExp_date());//TODO 需要改为B值账本的失效时间
						logTradeAutoExchangeHis.setExchange_time(currDate);
						// list add
						logTradeAutoExchangeHises.add(logTradeAutoExchangeHis);
						long newbalance=in.getBalance()-deductValue;
						in.setBalance(newbalance);
						log.debug("=====余额为："+in.getBalance());
						break;
					}else {
						deductValue-=in.getBalance();
						LogTradeAutoExchangeHis logTradeAutoExchangeHis = new LogTradeAutoExchangeHis();
						logTradeAutoExchangeHis.setTrade_id(tradeId);
						logTradeAutoExchangeHis.setTrade_type_code(TRADE_TYPE_CODE);
						logTradeAutoExchangeHis.setUser_id(userId);
						logTradeAutoExchangeHis.setPartition_id(partitionId);
						logTradeAutoExchangeHis.setExchange_id(auto.getExchange_id());
						logTradeAutoExchangeHis.setExchange_cycle_id(cycleIdDate);
						logTradeAutoExchangeHis.setResource_type_code(typeCode);
						logTradeAutoExchangeHis.setResource_value(""+in.getBalance());
						logTradeAutoExchangeHis.setEff_date(in.getEff_date());
						logTradeAutoExchangeHis.setExp_date(in.getExp_date());//TODO 需要改为B值账本的失效时间
						logTradeAutoExchangeHis.setExchange_time(currDate);
						// list add
						logTradeAutoExchangeHises.add(logTradeAutoExchangeHis);
						in.setBalance(0);
						log.debug("=====余额为："+in.getBalance());
					}
				}
			}
			
		}
		
		return logTradeAutoExchangeHises;
	}
	
	
	
	
	/**
	 * 
	 * sortInfoPayBalanceByIdAndExpdate 按账本类型和失效期排序.<br/>
	 * 
	 * @param infoPayBalances
	 */
	public void sortInfoPayBalanceByIdAndExpdate(final List<InfoPayBalance> infoPayBalances) {
		Collections.sort(infoPayBalances, new Comparator<InfoPayBalance>() {

			@Override
			public int compare(InfoPayBalance o1, InfoPayBalance o2) {
				
				String key1 = o1.getBalance_type_id() + o1.getExp_date();
				String key2 = o2.getBalance_type_id() + o2.getExp_date();
				
				int ret = key1.compareTo(key2);
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
}
