package com.tydic.beijing.bvalue.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchange;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.RuleParameters;
import com.tydic.beijing.bvalue.dao.ExchangeRatableHistory;
import com.tydic.beijing.bvalue.dto.ResourceDto;
import com.tydic.beijing.bvalue.service.ITradeExchange;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class TradeExchangeImpl implements ITradeExchange {

	private static final Logger log = Logger.getLogger(TradeExchangeImpl.class);

	@Autowired
	private TradeExchangeOper tradeExchangeOper;

	//	private String startChannelNo;
	//	private String endChannelNo;
	//	201706去除redis
	//	@Autowired(request=false)
	//	private InfoPayBalanceSync paySync;
	//	
	//	public InfoPayBalanceSync getPaySync() {
	//		return paySync;
	//	}
	//
	//	public void setPaySync(InfoPayBalanceSync paySync) {
	//		this.paySync = paySync;
	//	}

	@Autowired
	private OrderExtraPackage doExchange;

	@Autowired
	private Recharge reCharge;

	//	private String syncRedis;


	private static final String TRADE_TYPE_CODE = "301";
	private String msg;
	private String sendNbr;
	private static final String RECHARGE_TYPE ="BV"; //充值类型 充值接口给b值兑换话费分配的充值类型

	//	public String getSyncRedis() {
	//		return syncRedis;
	//	}
	//
	//	public void setSyncRedis(String syncRedis) {
	//		this.syncRedis = syncRedis;
	//	}

	@Override
	public JSONObject trade(JSONObject in) {
		log.info("MESSAGE:" + in.toString());
		JSONObject ret = new JSONObject();

		String voiceMsg="";
		String volumeMsg="";
		String smsMsg="";
		String moneyMsg ="";
		String useB="";
		String remainB="";
		String receiveNbr="";
		try {
			int partitionId = DateUtil.getCurrMonthForTypeDecimal();
			String tradeId = Common.getUUID();
			String userId = null;
			String jDPin = (String) in.get("JDPin");// 京东帐号
			log.debug("trade_id[" + tradeId + "]");
			log.debug("京东帐号[" + jDPin + "]");
			if (jDPin == null || jDPin.isEmpty()) {// 校验为空
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.JDPIN_NOT_SUPPLY);
				ret.put("ErrorMessage", "京东帐号未提供");
				log.error("京东帐号未提供");
				return ret;
			}
			userId = Common.md5(jDPin);
			log.debug("user id[" + userId + "]");

			String mobileNumber = (String) in.get("MobileNumber");// 手机号码
			log.debug("手机号码[" + mobileNumber + "]");
			if (mobileNumber == null || mobileNumber.isEmpty()
					|| !Common.isMobileNumber(mobileNumber)) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.MOBILE_NOT_SUPPLY);
				ret.put("ErrorMessage", "手机号码未提供或格式不正确，应为11位数字");
				log.error("手机号码未提供或格式不正确，应为11位数");
				return ret;
			}

			receiveNbr=mobileNumber;
			/**
			 * 验证京东帐号和手机号关联性
			 */
			InfoUserExternalAccount infoUserExternalAccount = tradeExchangeOper
					.getInfoUserExternalAccount(userId, DateUtil.getSystemTime());
			if (infoUserExternalAccount == null) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.JDPIN_AND_MOBILE_NUMBER_NOT_CONTACT);
				ret.put("ErrorMessage", "京东帐号和手机号码未关联或关联失效");
				log.error("京东帐号和手机号码未关联或关联失效");
				return ret;
			}
			// 京东帐号和手机号码不是关联的
			if (!mobileNumber.equals(infoUserExternalAccount.getExternal_account_code())) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.JDPIN_AND_MOBILE_NUMBER_NOT_CONTACT);
				ret.put("ErrorMessage", "京东帐号和手机号码不是关联的");
				log.error("京东帐号和手机号码不是关联的");
				return ret;
			}


			String exchangeBValue = (String) in.get("ExchangeBValue");// 兑换B值数量
			log.debug("兑换B值数量[" + exchangeBValue + "]");

			String channelType = (String) in.get("ChannelType");// 渠道类型
			log.debug("渠道类型[" + channelType + "]");
			if (channelType == null || channelType.isEmpty()) {// 不等于109
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_NO_NOT_SUPPLY);
				ret.put("ErrorMessage", "渠道编码不正确");
				log.error("渠道编码不正确");
				return ret;
			}

			//if (channelType.compareTo("101") < 0 || channelType.compareTo("121") > 0) {
			//			if (channelType.compareTo(this.startChannelNo) < 0 || channelType.compareTo(this.endChannelNo) > 0) {
			//				ret.put("Status", Constants.STATUS_FAILURE);
			//				ret.put("ErrorCode", Constants.ORDER_NO_NOT_SUPPLY);
			//				ret.put("ErrorMessage", "渠道编码不正确");
			//				log.error("渠道编码不正确");
			//				return ret;
			//			}

			String orderNo = (String) in.get("OrderNo");// 订单号
			log.debug("订单号[" + orderNo + "]");
			if (orderNo == null || orderNo.isEmpty()) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_NO_NOT_SUPPLY);
				ret.put("ErrorMessage", "订单号未提供");
				log.error("订单号未提供");
				return ret;
			}
			// 验证订单号长度
			if (orderNo.length() > Constants.ORDER_NO_MAX_LENGTH) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_NO_MORE_THAN_20);
				ret.put("ErrorMessage", "订单号长度不能大于20");
				log.error("订单号长度不能大于20");
				return ret;
			}
			/*
			 * 
			 * 检查订单号是否重复处理
			 */
			if (tradeExchangeOper.isDuplicated(userId, orderNo, TRADE_TYPE_CODE)) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_NO_IS_DUPLICATED);
				ret.put("ErrorMessage", "该订单已经被处理过，为重复订单，订单号[" + orderNo + "]");
				log.error("该订单已经被处理过，为重复订单，订单号[" + orderNo + "]");
				return ret;
			}

			String orderDesc = (String) in.get("OrderDesc");// 备注
			log.debug("备注[" + orderDesc + "]");

			Object resourceObject = in.get("ResourceDtoList");// 兑换资源列表
			if (resourceObject == null) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.RESOURCE_DTO_LIST_NOT_SUPPLY);
				ret.put("ErrorMessage", "ResourceDtoList未提供");
				log.error("ResourceDtoList未提供");
				return ret;
			}
			log.debug("兑换资源列表："+resourceObject.toString());

			JSONArray resourceArray = JSONArray.fromObject(resourceObject);

			List<ResourceDto> resources = new ArrayList<ResourceDto>(5);

			if (resourceArray.size() == 0) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.RESOURCE_DTO_LIST_IS_EMPTY);
				ret.put("ErrorMessage", "资列表不能为空");
				log.error("资列表不能为空");
				return ret;
			}
			for (Object o : resourceArray) {
				JSONObject jso = (JSONObject) o;
				ResourceDto res = new ResourceDto();
				String resourceType = (String) jso.get("ResourceType");
				log.debug("\t资源类型[" + resourceType + "]");
				if (resourceType == null || resourceType.isEmpty()) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.RESOURCE_TYPE_NOT_SUPPLY);
					ret.put("ErrorMessage", "资源类型未提供");
					log.error("资源类型未提供");
					return ret;
				}

				if ("01234567".indexOf(resourceType) < 0) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.RESOURCE_TYPE_NOT_0123);
					ret.put("ErrorMessage", "资源类型取值错误,ResourceType[" + resourceType
							+ "],范围[0,1,2,3,4,5,6,7]");
					log.error("资源类型取值错误，ResourceType[" + resourceType + "],范围[0,1,2,3,4,5,6,7]");
					return ret;
				}

				String totalResource = (String) jso.get("TotalResource");
				log.debug("\t资源量[" + totalResource + "]");
				if (totalResource == null || totalResource.isEmpty()) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.TOTAL_RESOURCE_NOT_SUPPLY);
					ret.put("ErrorMessage", "资源量未提供");
					log.error("资源量未提供");
					return ret;
				}

				try {
					long s = Long.parseLong(totalResource);
					if (s < 0) {
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.TOTAL_RESOURCE_NOT_POSITIVE_INTEGER);
						ret.put("ErrorMessage", "资源量要求是正整数");
						log.error("资源量要求是正整数");
						return ret;
					}
				} catch (NumberFormatException e) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.TOTAL_RESOURCE_NOT_NUMBER);
					ret.put("ErrorMessage", "资源量是非数字的");
					log.error("资源量是非数字的");
					return ret;
				}
				//从接口获取ResourceRemark可先作为东券面额信息 modify by yangwen 20180201
				/*if(jso.has("ResourceRemark")){
					denomination = (String) jso.get("ResourceRemark");
					log.debug("\t面额["+denomination+"]");
				}*/
				String denomination = (String) jso.get("ResourceRemark");
				log.debug("\t面额["+denomination+"]");
				res.setResourceType(resourceType);
				res.setTotalResource(totalResource);

				//可先作为东券面额信息
				res.setDenomination(denomination);
				resources.add(res);
			}

			String currDate = DateUtil.getSystemTime();
			//判断产品属性和兑换资源是否匹配
			List<InfoUserExternalAccountAttr> listattrs = tradeExchangeOper.getExternalAccountAttrbyUserIdAndExternal(userId,infoUserExternalAccount.getExternal_account_id());
			log.debug("listattrs======"+listattrs.toString());
			boolean isLEGOU = false;
			if(isMoneyExchange(resources)){
				//如果是兑换话费
				for(InfoUserExternalAccountAttr tmpattr:listattrs){
					if(tmpattr.getAttr_code().equals("1002") && tmpattr.getAttr_value()!= null && tmpattr.getAttr_value().equals("02") 
							&& tmpattr.getEff_date().compareTo(currDate)<=0 && tmpattr.getExp_date().compareTo(currDate)>=0){
						isLEGOU = true;
						break;
					}
				}

				if(isLEGOU == false){
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.TOTAL_RESOURCE_NOT_NUMBER);
					ret.put("ErrorMessage", "用户不是乐购卡，不能兑换话费");
					log.error("用户不是乐购卡，不能兑换话费");
					return ret;
				}

			}else{
				//如果是兑换资源
				isLEGOU = false;
				for(InfoUserExternalAccountAttr tmpattr:listattrs){
					if(tmpattr.getAttr_code().equals("1002") && tmpattr.getAttr_value()!= null && tmpattr.getAttr_value().equals("02") 
							&& tmpattr.getEff_date().compareTo(currDate)<=0 && tmpattr.getExp_date().compareTo(currDate)>=0){
						isLEGOU = true;
						break;
					}
				}

				if(isLEGOU == true){
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.TOTAL_RESOURCE_NOT_NUMBER);
					ret.put("ErrorMessage", "用户是乐购卡，不能兑换资源");
					log.error("用户是乐购卡，不能兑换资源");
					return ret;
				}
			}
			//查找累积量历史表
			String acctMonth=currDate.substring(0,6);
			ExchangeRatableHistory exchangeRatableHistory=tradeExchangeOper.getExchangeRatableHistory(userId,acctMonth); 
			//用于最后的更新累积量表
			ExchangeRatableHistory exchangeRatable=new ExchangeRatableHistory();

			//判断是否为首月激活
			//			boolean fristActivation=false; 
			long userBalance=0;
			if(isLEGOU){
				String attrValue=null;
				long maxAutoBalance=0;
				long ratableBalance=0;
				InfoUserExternalAccountAttr attr_1003 =new InfoUserExternalAccountAttr();
				for(InfoUserExternalAccountAttr tmpattr:listattrs){
					if(tmpattr.getAttr_code().equals("1003")&&tmpattr.getEff_date().compareTo(currDate)<=0&&tmpattr.getExp_date().compareTo(currDate)>=0){
						attr_1003=tmpattr;
					}
				}
				log.debug("获取所有的1003========"+attr_1003.toString());
				if(attr_1003!=null&&attr_1003.getAttr_value()!=null){
					attrValue=attr_1003.getAttr_value();
				}else{
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.FRIST_MONTH_ACTIVITION);
					ret.put("ErrorMessage", "首月激活不能兑换");
				}
				log.debug("用户的主套餐为========="+attrValue);
				String domainCode="2000";
				String tradeTypeCode="104";
				Map<String,Object> filter = new HashMap<String,Object>();
				filter.put("domain_code", domainCode);
				filter.put("trade_type_code", tradeTypeCode);
				List<RuleParameters> ruleParameters=S.get(RuleParameters.class).query(Condition.build("byattrValue").filter(filter));;
				if(ruleParameters==null || ruleParameters.isEmpty()){
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.NOT_FIND_PRODUCT);
					ret.put("ErrorMessage", "乐购卡用户套餐未生效，不允许兑换");
					return ret;
				}
				//获取套餐对话 封顶值
				for(RuleParameters rule:ruleParameters){
					if(rule.getPara_name().equals(attrValue)&&rule.getPara_char4()!=null){
						maxAutoBalance=Long.parseLong(rule.getPara_char4());
					}
				}
				log.debug("套餐的封顶兑换值为========="+maxAutoBalance);
				if(maxAutoBalance==0){
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.NOT_FIND_TOP_VALUE);
					ret.put("ErrorMessage", "乐购卡用户套餐未生效，不允许兑换");
					return ret;
				}
				//获取累积量
				if(exchangeRatableHistory==null){
					ratableBalance=0;
				}else{
					exchangeRatable.setAcct_month(acctMonth);
					exchangeRatable.setUpdate_time(currDate);
					exchangeRatable.setUser_id(userId);
					ratableBalance=exchangeRatableHistory.getRatable_balance();
					log.debug("用户本月的兑换累积量为========="+ratableBalance);
				}
				userBalance=maxAutoBalance-ratableBalance;
				log.debug("用户本月可以用来兑换的b值为========="+userBalance);
				if(userBalance<=0){
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.EXCHANGE_B_NOT_ENOUGH);
					ret.put("ErrorMessage", "兑换失败，本月还可兑换0B");
					return ret;
				}

			}
			//乐购卡判断完毕	



			List<LogTradeExchangeHis> logTradeExchangeHisList = new ArrayList<LogTradeExchangeHis>();

			long totalBs = 0; //本次兑换需要消耗的总B值
			long voiceValue = 0;  //本次兑换语音需要消耗的B值
			long streamValue = 0; //本次兑换流量需要消耗的B值
			long messageValue = 0; //本次兑换短信需要消耗的B值
			long moneyValue =0L;  //兑换的话费，单位分
			String denomination="";//新增面额字段变量
			long dticketValue = 0;// 本次兑换东券需要消耗的B值
			long numberOf = 0; //东券张数
			List<PackageDetailDto> packages = new ArrayList<PackageDetailDto>(); //调billing接口入参，兑换的资源明细
			RechargeInfo rechargeInfo = new RechargeInfo();


			for (ResourceDto rdd : resources) {
				long resAmount = Long.parseLong(rdd.getTotalResource());
				if ("0".equals(rdd.getResourceType())) {// 金钱，单位：分
					totalBs += Math.ceil((resAmount*1.0) / 10);
					moneyValue = resAmount;
				} else if ("1".equals(rdd.getResourceType())) {
					totalBs += resAmount;// 1B值等于1分
					voiceValue = resAmount;
				} else if ("2".equals(rdd.getResourceType())) {
					totalBs += resAmount;// 1B值等于1M
					streamValue = resAmount;
				} else if ("3".equals(rdd.getResourceType())) {
					totalBs += resAmount;// 1B值等于一条短信
					messageValue = resAmount;
				} else if ("4".equals(rdd.getResourceType())){
					//新增循环判断 保证报文安全性
					if(resources.size() > 1) {
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.EXCHANGE_LIST_NOT_SUPPLY);
						ret.put("ErrorMessage", "兑换失败，兑换列表未提供");
						return ret;
					}
					totalBs += Integer.parseInt(exchangeBValue);
					denomination = rdd.getDenomination();
					log.debug("\t面额["+denomination+"]");
					dticketValue = Integer.parseInt(exchangeBValue);
					numberOf = resAmount;
				}


				/*
				 * ROM:金钱
				 * ROV:语音资源
				 * ROF:流量资源
				 * ROS:短信资源
				 * ROD:东券
				 */
				String resourceTypeCode = null;
				if ("0".equals(rdd.getResourceType())) {
					resourceTypeCode = "ROM";
				} else if ("1".equals(rdd.getResourceType())) {
					resourceTypeCode = "ROV";
				} else if ("2".equals(rdd.getResourceType())) {
					resourceTypeCode = "ROF";
				} else if ("3".equals(rdd.getResourceType())) {
					resourceTypeCode = "ROS";
				} else if ("4".equals(rdd.getResourceType())){
					resourceTypeCode = "ROD";
				}else {
					resourceTypeCode = "ROV";
				}

			}

			voiceMsg=voiceMsg+voiceValue;
			volumeMsg=volumeMsg+streamValue;
			smsMsg=smsMsg+messageValue;
			moneyMsg = moneyMsg + moneyValue;
			useB=useB+totalBs;
			List<RechargeInfoDto> rechargeInfoDtoList = new ArrayList<RechargeInfoDto>();

			log.debug("总B值数[" + totalBs + "]");
			// ############for database
			//0类型账本，可能需要补充账本类型
			List<InfoPayBalance> infoPayBalanceList = tradeExchangeOper.getInfoPayBalanceByManager(userId,
					currDate);


			// 有效的余额信息不存在
			if (infoPayBalanceList == null || infoPayBalanceList.isEmpty()) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.BALANCE_INFO_NOT_EXIST);
				ret.put("ErrorMessage", "无有效的B值，B值余额为0或全部失效");
				log.error("无有效的B值，B值余额为0或全部失效");
				return ret;
			}

			//做判断是否为乐购卡
			long min=0;
			if(isLEGOU){
				min=totalBs-userBalance;
				if(min>0){
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.EXCHANGE_B_NOT_ENOUGH);
					ret.put("ErrorMessage", "兑换失败，本月还可兑换"+userBalance+"B");
					return ret;
				}else{
					if(exchangeRatableHistory!=null){
						exchangeRatable.setRatable_balance(totalBs);
						log.debug("乐购卡本次兑换的B值为=="+totalBs);
					}
				}
			}else{
				log.debug("非乐购卡用户本次兑换的B值为=="+totalBs);
			}



			// 排序账本信息，只按失效时间排序
			//			Collections.sort(infoPayBalanceList, new Comparator<InfoPayBalance>() {
			//				@Override
			//				public int compare(InfoPayBalance o1, InfoPayBalance o2) {
			//					int ret = o1.getExp_date().compareTo(o2.getExp_date());
			//					if (0 == ret) {
			//						return 0;
			//					} else if (ret > 0) {
			//						return 1;
			//					} else {
			//						return -1;
			//					}
			//				}
			//			});

			if (log.isDebugEnabled()) {
				for (InfoPayBalance i : infoPayBalanceList) {
					log.debug("sorted by eff_date, exp_date :" + i);
				}
			}
			long oldBValue=0; //统计原所有B值

			for(InfoPayBalance iter : infoPayBalanceList){
				oldBValue=oldBValue+iter.getBalance();
			}
			log.debug("账本余额为==========="+oldBValue);
			long remain = 0L; //账本余额
			long totalBsTmp = totalBs;

			//如果余额不足以抵扣本次兑换，返回错误
			if ( oldBValue < totalBs) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.BALANCE_NOT_ENOUGH);
				ret.put("ErrorMessage", "B值余额不足");
				log.error("B值余额不足");
				return ret;
			}

			List<ResourceExchangeInfo> resourceList = new ArrayList<ResourceExchangeInfo>(); //资源兑换列表，优先级顺序： 语音>流量>短信    话费只能乐购卡单独兑换，并且不能与其他资源同时兑换，和排序无关
			resourceList.add(new ResourceExchangeInfo("ROV",voiceValue,denomination,0 ));
			resourceList.add(new ResourceExchangeInfo("ROF",streamValue,denomination,0 ));
			resourceList.add(new ResourceExchangeInfo("ROS",messageValue,denomination,0 ));
			resourceList.add(new ResourceExchangeInfo("ROM",moneyValue,denomination,0 ));
			resourceList.add(new ResourceExchangeInfo("ROD",dticketValue,denomination,numberOf ));
			List<BalanceAccessLog> remberAccessLog = new ArrayList<BalanceAccessLog>();
			List<InfoPayBalance> remberBalanceId = new ArrayList<InfoPayBalance>();


			//调用方法进行B值扣减
			int type=0;
			//			long bValue=tmpResource.getResourceValue();
			int changeType=2;
			InfoPayBalanceManager infoPayBalanceManage=new InfoPayBalanceManager();
			List<InfoPayBalance> updateInfoPayBalance=new ArrayList<InfoPayBalance>();
			List<InfoPayBalance> insertInfoPayBalance=new ArrayList<InfoPayBalance>();
			List<BalanceAccessLog> insertBalanceAccessLog=new ArrayList<BalanceAccessLog>();
			int code=infoPayBalanceManage.manage(userId, type, changeType, totalBs, updateInfoPayBalance, insertInfoPayBalance, insertBalanceAccessLog);
			if(code==1){
				log.debug("========B值扣减成功========");
			}else{
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.TRADE_EXCHANGE_FAIL);
				ret.put("ErrorMessage", "B值扣减失败");
				log.error("B值扣减失败");
				return ret;
			}
			for(InfoPayBalance infopay:updateInfoPayBalance){
				InfoPayBalance infoBalance=new InfoPayBalance(infopay);
				infoBalance.setBalance(infopay.getBalance());
				remberBalanceId.add(infoBalance);
			}

			for(BalanceAccessLog acc:insertBalanceAccessLog){
				BalanceAccessLog access = new BalanceAccessLog();
				access.setTrade_Id(tradeId);
				access.setTrade_Type_Code(TRADE_TYPE_CODE);
				access.setUser_Id(acc.getUser_Id());
				access.setPartition_Id(acc.getPartition_Id());
				access.setBalance_Id(acc.getBalance_Id());
				access.setBalance_Type_Id(acc.getBalance_Type_Id());
				access.setAccess_Tag(acc.getAccess_Tag());// 取款
				access.setOld_Balance(acc.getOld_Balance());
				access.setOperate_Time(acc.getOperate_Time());
				access.setMoney(acc.getMoney());// access扣减B值量
				access.setNew_Balance(acc.getNew_Balance());// access

				remberAccessLog.add(access);
			}
			log.debug("remberAccessLog=============="+remberAccessLog.toString());



			//			for (InfoPayBalance pay : infoPayBalanceList) {   
			//				BalanceAccessLog access = new BalanceAccessLog();
			//				access.setTrade_Id(tradeId);
			//				access.setTrade_Type_Code(TRADE_TYPE_CODE);
			//				access.setUser_Id(userId);
			//				access.setPartition_Id(partitionId);
			//				access.setBalance_Id(pay.getBalance_id());
			//				access.setBalance_Type_Id(pay.getBalance_type_id());
			//				access.setAccess_Tag("1");// 取款
			//				access.setOld_Balance(pay.getBalance());
			//				access.setOperate_Time(currDate);
			//				remain = pay.getBalance();
			//				
			//				if (remain <= 0) {
			//					continue;
			//				}
			//				// 当条在用余额大于剩余兑换
			//				if (remain >= totalBsTmp) {
			//					// 计算
			//					pay.setBalance(-totalBsTmp);
			//
			//					remberBalanceId.add(pay);
			//
			//					access.setMoney(totalBsTmp);// access扣减B值量
			//					access.setNew_Balance(remain - totalBsTmp);// access
			//					remberAccessLog.add(access);// access
			//
			//					totalBsTmp = 0L;
			//					break;
			//				}
			//				// 本条账本的余额不足，扣完后，到下一条
			//				totalBsTmp -= remain;
			//				pay.setBalance(-remain);
			//
			//				access.setMoney(remain);// access扣减B值量
			//				access.setNew_Balance(0);// access
			//
			//				remberAccessLog.add(access);
			//				remberBalanceId.add(pay);
			//			}
			
			for(ResourceExchangeInfo tmpResource :resourceList){
				//开始遍历需要兑换的资源列表
				String resourceType = tmpResource.getResourceType();
				long oldResourceValue = tmpResource.getResourceValue();
				if(resourceType.equals("ROM")){ //如果是现金  单位分  
					oldResourceValue = (long) Math.ceil((oldResourceValue*1.0) / 10) ;
				}
				//long newResourceValue = oldResourceValue; 剩余需兑换资源量
				if(oldResourceValue ==0){
					continue;
				}
				for (int i=0;i<infoPayBalanceList.size();i++) {  //InfoPayBalance pay : infoPayBalanceList
					InfoPayBalance pay = infoPayBalanceList.get(i);
					//遍历可用账本
					if(pay.getBalance() <=0){
						continue;
					}
					if(oldResourceValue ==0){
						break;
					}
					long oldBalance = pay.getBalance();
					long deductValue=0;
					deductValue =  Math.min(oldBalance, oldResourceValue); //当前账本需要扣减的B值,取当前账本余额和需扣减量的最小值
					long newBalance = oldBalance - deductValue;//兑换后账本余额
					oldResourceValue = oldResourceValue - deductValue; //兑换后剩余需兑换量
					infoPayBalanceList.get(i).setBalance(newBalance);
					InfoPayBalance infoPayBalance = new InfoPayBalance(pay);
					infoPayBalance.setBalance(-deductValue);
					//							remberAccessLog.add(access);
					//							remberBalanceId.add(infoPayBalance);
					if(moneyValue >0){
						//TODO
						RechargeInfoDto rechargeInfoDto = new RechargeInfoDto ();
						rechargeInfoDto.setRechargeType(RECHARGE_TYPE);
						rechargeInfoDto.setAmount(deductValue*10);
						rechargeInfoDto.setEffDate(pay.getEff_date());
						rechargeInfoDto.setExpDate(pay.getExp_date());
						rechargeInfoDtoList.add(rechargeInfoDto);

					}else{
						PackageDetailDto packageDetail = getPackageDetailDto(tmpResource.getResourceType(),deductValue,pay.getEff_date(),pay.getExp_date());
						packages.add(packageDetail);
						log.debug("===============packages================="+packages.toString());
					}




					//					BalanceAccessLog access = new BalanceAccessLog();
					//					access.setTrade_Id(tradeId);
					//					access.setTrade_Type_Code(TRADE_TYPE_CODE);
					//					access.setUser_Id(userId);
					//					access.setPartition_Id(partitionId);
					//					access.setBalance_Id(pay.getBalance_id());
					//					access.setBalance_Type_Id(pay.getBalance_type_id());
					//					access.setAccess_Tag("1");// 取款
					//					access.setOld_Balance(oldBalance);
					//					access.setOperate_Time(currDate);
					//					access.setMoney(deductValue);// access扣减B值量
					//					access.setNew_Balance(newBalance);// access

					//pay.setBalance(-deductValue);



					LogTradeExchangeHis exchangeTmp = new LogTradeExchangeHis();
					exchangeTmp.setTrade_id(tradeId);
					exchangeTmp.setTrade_type_code(TRADE_TYPE_CODE);
					exchangeTmp.setUser_id(userId);
					exchangeTmp.setPartition_id(partitionId);
					exchangeTmp.setResource_type_code(tmpResource.getResourceType());
					if("ROD".equals(tmpResource.getResourceType())&&tmpResource.getNumberOf()>0){
						exchangeTmp.setResource_value(tmpResource.getNumberOf()); //记录张数
					}else{
					    exchangeTmp.setResource_value(deductValue);    // 记录资源量
					}
					exchangeTmp.setEff_date(pay.getEff_date());
					exchangeTmp.setExp_date(pay.getExp_date());
					exchangeTmp.setReserve_1(tmpResource.getDenomination());  //添加面额   ""
					exchangeTmp.setExchange_time(currDate);
					logTradeExchangeHisList.add(exchangeTmp);
				}

				if(oldResourceValue >0){
					//需兑换量未完全兑换，B值不足，返回错误
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.BALANCE_NOT_ENOUGH);
					ret.put("ErrorMessage", "通信B余额不足");
					log.error("通信B余额不足");
					return ret;
				}

			}



			// 通信B余额不足
			//			if (totalBsTmp > 0) {
			//				ret.put("Status", Constants.STATUS_FAILURE);
			//				ret.put("ErrorCode", Constants.BALANCE_NOT_ENOUGH);
			//				ret.put("ErrorMessage", "通信B余额不足");
			//				log.error("通信B余额不足");
			//				return ret;
			//			}

			// 交易登记处理历史表
			LogTradeHis his = new LogTradeHis();
			his.setTrade_id(tradeId);
			his.setTrade_type_code(TRADE_TYPE_CODE);
			his.setExternal_system_code("10000");
			his.setChannel_type(channelType);
			his.setUser_id(userId);
			his.setPartition_id(partitionId);
			his.setOrder_no(orderNo);
			his.setOrder_type("");
			his.setOrder_amount(0);
			his.setOrder_completion_time(currDate);
			his.setBalance_type_id(0);
			his.setUnit_type_id(0);// 0 B值
			his.setBalance(-totalBs);// 兑换为负，赠送为正
			his.setProcess_tag(2);
			his.setTrade_time(currDate);
			his.setProcess_time(currDate);
			his.setRemark(orderDesc);
			// update info_pay_balance
			log.debug("remberBalanceId=========="+remberBalanceId.toString());
			log.debug("exchangeRatable=========="+exchangeRatable.toString());
			tradeExchangeOper.updateTradeExchange(remberBalanceId, his, logTradeExchangeHisList,
					remberAccessLog,currDate,exchangeRatable,isLEGOU);

			// 同步redis
			//			if(this.syncRedis!=null && this.syncRedis.equals("Y")){
			//				for (InfoPayBalance pay : remberBalanceId) {
			//					
			//					log.debug("同步redis, " + pay.toString());
			//					int retCode = paySync.sync(pay);
			//					if(retCode !=0){
			//						//0成功  -1失败
			//						log.debug("同步Redis失败！");
			//					}
			//				}
			//			}

			//如果兑换东券 不调用billing接口  gaobo
			remainB=remainB+(oldBValue-totalBs);
			for(ResourceExchangeInfo tmpResource1 :resourceList){
				String resourceType = tmpResource1.getResourceType();
				if(resourceType.equals("ROD")&&tmpResource1.getNumberOf()>0){ 
					ret.put("Status", Constants.STATUS_SUCCESS);
					ret.put("ExchangeBValue", totalBs);
					log.info("RESULT:" + ret.toString());
					return ret;
				}
			}

			/****** billing interface *******/
			BValueExchangeRequest request = new BValueExchangeRequest();
			request.setSn(tradeId);
			request.setMSISDN(mobileNumber);
			request.setJDPin(jDPin);
			request.setContactChannel(channelType);

			Calendar cal = Calendar.getInstance();
			String effDate = DateUtil.dateToString(cal).substring(0, 8);
			String expDate = DateUtil.getBValueExpDate(cal).substring(0, 8);


			// 语音
			//			if (voiceValue != 0) {
			//				log.debug("Exchange-兑换语音:" + voiceValue);
			//				PackageDetailDto voice = new PackageDetailDto();
			//				voice.setPackageType(1);
			//				voice.setQuantity(voiceValue);// MB
			//				voice.setDescription("语音");
			//				voice.setEffDate(effDate);
			//				voice.setExpDate(expDate);
			//				packages.add(voice);
			//			}
			//
			//			// 流量
			//			if (streamValue != 0) {
			//				log.debug("Exchange-兑换流量:" + streamValue * 1024);
			//				PackageDetailDto stream = new PackageDetailDto();
			//				stream.setPackageType(2);
			//				stream.setQuantity(streamValue * 1024);
			//				stream.setDescription("流量");
			//				stream.setEffDate(effDate);
			//				stream.setExpDate(expDate);
			//				packages.add(stream);
			//			}
			//			// 短信
			//			if (messageValue != 0) {
			//				log.debug("Exchange-兑换短信:" + messageValue);
			//				PackageDetailDto message = new PackageDetailDto();
			//				message.setPackageType(3);
			//				message.setQuantity(messageValue);
			//				message.setDescription("短信");
			//				message.setEffDate(effDate);
			//				message.setExpDate(expDate);
			//				packages.add(message);
			//			}

			request.setPackageDetailDtoList(packages);

			if(moneyValue >0 ){
				//兑换话费
				//				RechargeInfoDto rechargeInfoDto = new RechargeInfoDto();
				//				rechargeInfoDto.setAmount(moneyValue);
				//				rechargeInfoDto.setRechargeType(TradeExchangeImpl.RECHARGE_TYPE);
				//				rechargeInfoDto.setEffDate();
				//				rechargeInfoDto.setExpDate();

				//TODO 还需要增加话费有效期
				//rechargeInfo.getRechargeInfoDtoList().add(rechargeInfoDto);		
				rechargeInfo.setMSISDN(mobileNumber);
				//rechargeInfo.setCallBackUrl("");
				rechargeInfo.setSn(tradeId);
				rechargeInfo.setRechargeInfoDtoList(rechargeInfoDtoList);
				rechargeInfo.setUserEventCode("88");
				rechargeInfo.setContactChannle(channelType);
				rechargeInfo.setJdAcctNbr(jDPin);
				//				rechargeInfo.setOperId("");
				//				rechargeInfo.setGiftReason("B值兑换话费");



				log.debug("调计费充值接口入参："+rechargeInfo.toString());

				RechargeResult rechargeResult = reCharge.recharge(rechargeInfo);
				if(rechargeResult.getStatus().equals("1")){  //充值成功
					log.debug("Exchange-调用billng充值接口，成功");
				}else{
					log.debug("Exchange-调用billng充值接口失败，message:" + rechargeResult.getErrorMessage());
					log.error("Exchange-调用billng充值接口失败，trade_id[" + tradeId + "]");
				}

			}else{
				//TODO
				//兑换资源
				log.debug("Exchange-调用billng兑换接口");
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



			//发送短信 20150704 不再发短信
			//			String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
			////			String sms=msg.replace("$1", voiceMsg).replace("$2", volumeMsg).replace("$3", smsMsg).replace("$4", useB).replace("$5", remainB);
			//			String sms="|"+msg+"|"+voiceMsg+"|"+volumeMsg+"|"+smsMsg+"|"+useB+"|"+remainB;
			//			HlpSmsSend sendHis=new HlpSmsSend();
			//			sendHis.setMsisdn_send(sendNbr);
			//			sendHis.setMsisdn_receive(receiveNbr);
			//			sendHis.setMessage_text(sms);
			//			
			////			sendHis.setPara_key(paraKey);
			////			sendHis.setSend_time(currentTime);
			//			sendHis.setCreate_time(currentTime);
			//			
			//			tradeExchangeOper.insertExchangeMessage(sendHis);

			ret.put("Status", Constants.STATUS_SUCCESS);
			ret.put("ExchangeBValue", totalBs);
			log.info("RESULT:" + ret.toString());
		} catch (BValueException e) {

			ret.put("Status", Constants.STATUS_FAILURE);
			ret.put("ErrorCode", Constants.BALANCE_NOT_ENOUGH);
			ret.put("ErrorMessage", "通信B余额不足");
			log.error(e);
			return ret;
		} catch (Exception e) {
			ret.put("Status", Constants.STATUS_FAILURE);
			ret.put("ErrorCode", Constants.SYSTEM_EXCEPTION);
			ret.put("ErrorMessage", "接口异常");
			log.error(e);
			e.printStackTrace();
			return ret;
		}



		return ret;
	}

	private boolean isMoneyExchange(List<ResourceDto> resources) {
		for (ResourceDto rdd : resources) {

			if(rdd.getResourceType().equals("0")){//话费
				return true;
			}
		}
		return false;
	}

	private PackageDetailDto getPackageDetailDto(String resourceType,
			long deductValue, String effDate, String expDate) {
		PackageDetailDto packageDetail = new PackageDetailDto();

		long packageType = getPackageType(resourceType);
		packageDetail.setPackageType(packageType);
		packageDetail.setQuantity(getQuantity(packageType,deductValue));
		packageDetail.setDescription(getDescription(resourceType));
		packageDetail.setEffDate(effDate);
		packageDetail.setExpDate(expDate);
		return packageDetail;
	}


	private String getDescription(String resourceType) {
		// ROM:金钱 	 ROV:语音资源   ROF:流量资源   ROS:短信资源  ROD:面额
		String desc ="";
		if(resourceType.equals("ROM")){
			desc ="金钱";
		}else if(resourceType.equals("ROV")){
			desc ="语音";
		}else if(resourceType.equals("ROF")){
			desc ="流量";
		}else if(resourceType.equals("ROS")){
			desc ="短信";
		}else if(resourceType.equals("ROD")){
			desc ="面额";
		}

		return desc;
	}

	/**
	 * @param packageType  包类型，资源类型
	 * @param deductValue  扣减的B值
	 * @return
	 */
	private long getQuantity(long packageType, long deductValue) {
		//根据资源类型获取赠送资源量   1-语音   2-流量  3-短信 4-话费  单位分别是 分钟、K、条、分钱
		long quantity = 0L;
		if(packageType == 1){
			quantity = deductValue;
		}else if(packageType == 2){
			quantity = deductValue*1024;
		}else if(packageType == 3){
			quantity = deductValue;
		}else if(packageType == 4){
			quantity = deductValue*10; //1B相当于1毛钱
		}

		return quantity;
	}

	private long getPackageType(String resourceType) {
		// ROM:金钱 	 ROV:语音资源   ROF:流量资源   ROS:短信资源
		//1-语音   2-流量  3-短信 4-话费

		long packageType =0L;
		if(resourceType.equals("ROM")){
			packageType = 4L;
		}else if (resourceType.equals("ROV")){
			packageType = 1L;
		}else if (resourceType.equals("ROF")){
			packageType = 2L;
		}else if (resourceType.equals("ROS")){
			packageType = 3L;
		}else if(resourceType.equals("ROD")){
			packageType = 5L;
		}

		return packageType;
	}

	public TradeExchangeOper getTradeExchangeOper() {
		return tradeExchangeOper;
	}

	public void setTradeExchangeOper(TradeExchangeOper tradeExchangeOper) {
		this.tradeExchangeOper = tradeExchangeOper;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setSendNbr(String sendNbr) {
		this.sendNbr = sendNbr;
	}

	//	public String getEndChannelNo() {
	//		return endChannelNo;
	//	}
	//
	//	public void setEndChannelNo(String endChannelNo) {
	//		this.endChannelNo = endChannelNo;
	//	}
	//
	//	public String getStartChannelNo() {
	//		return startChannelNo;
	//	}
	//
	//	public void setStartChannelNo(String startChannelNo) {
	//		this.startChannelNo = startChannelNo;
	//	}

	public class ResourceExchangeInfo{
		String resourceType ="";
		long resourceValue =0L;
		String denomination="";
		long numberOf = 0L; //东券张数等
		//新增面额
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
		public String getDenomination(){
			return denomination;
		}
		public void setDenomination(String denomination){
			this.denomination=denomination;
		}
		public long getNumberOf(){
			return numberOf;
		}
		public void setNumberOf(long numberOf) {
			this.numberOf = numberOf;
		}
		
		public ResourceExchangeInfo( String a,long b,String c,long d){
			this.resourceType =a;
			this.resourceValue=b;
			this.denomination=c;
			this.numberOf = d;
		}
	}





}
