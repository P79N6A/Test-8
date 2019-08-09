package com.tydic.beijing.billing.rating.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.account.core.WriteOff;
import com.tydic.beijing.billing.account.type.UserBillSummary;
import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.rating.domain.BalanceReserve;
import com.tydic.beijing.billing.rating.domain.OfrRateData;
import com.tydic.beijing.billing.rating.domain.RateData;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.SectionRateData;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.service.BalanceDeduct;

public class BalanceDeductImpl implements BalanceDeduct {

	@Autowired
	RatingMsg ratingMsg;
	@Autowired
	DbUtil dbutil;
	
	
	
	private static final Logger log = Logger.getLogger(BalanceDeductImpl.class);
	
	
	
	public int deductBalance(RateData rateData,int operationType,List<WriteOffDetail> listDetail) throws Exception{
	
		//int billingFlag = ratingMsg.getVarMsg().getM_nBillingFlag();// 计费类型 1 I包 2 U包 3 T包 4 事件型计费 5 事件返还
		String payFlag = ratingMsg.getBaseMsg().getM_strPayFlag(); //付费标识  1 预付费  2 后付费
		List<WriteOffDetail> lwriteOffDetail = new ArrayList<WriteOffDetail>();
        int resultCode = getDeductBalanceInfo(rateData,lwriteOffDetail);
        String sessionId = ratingMsg.getBaseMsg().getM_strSessionId();
        
        //TODO 每次操作后要同时更新memcached
        
		try {
			if(payFlag.equals("2")){ //后付费
			    //不管是否能销清，都要做销账处理
			    for(WriteOffDetail writeOffDetail:lwriteOffDetail){
			    		String payId = writeOffDetail.getPay_Id();
			    		long balanceId = writeOffDetail.getBalance_id();
			    		long realBalance = writeOffDetail.getWriteoff_fee();
			    		long usedBalance = 0L;
			    		dbutil.updateInfoPayBalance(balanceId, realBalance, usedBalance);
			    	}
			    if (resultCode == -1){ //如果不能销清，还要记欠费
			    	//TODO 记欠费
			    	
			    }
			    
				 
				
			}else if (payFlag.equals("1") ){//预付费
				if(operationType ==1){
					//预占
					int reserveSn =1;
					String reserveId = UUID.randomUUID().toString();
					  for(WriteOffDetail writeOffDetail:lwriteOffDetail){
						  
						  if(writeOffDetail.getWriteoff_fee() >0){ //可销费用>0的才处理
			        		String payId = writeOffDetail.getPay_Id();
			        		long balanceId = writeOffDetail.getBalance_id();
			        		long realBalance = 0L;
			        		long usedBalance = writeOffDetail.getWriteoff_fee();
			        		dbutil.updateInfoPayBalance(balanceId, realBalance, usedBalance);
			     
			        		BalanceReserve balanceReserve = new BalanceReserve();
			        		balanceReserve.setReserve_id(reserveId);
			        		balanceReserve.setReserve_sn(reserveSn);
			        		balanceReserve.setAcct_item_code(writeOffDetail.getAcct_item_code());
			        		balanceReserve.setBalance_id(balanceId);
			        		balanceReserve.setBalance_type_id(writeOffDetail.getBalance_type_id());
			        		balanceReserve.setAmount(usedBalance);
			        		balanceReserve.setReserve_time(new Date());
			        		balanceReserve.setSession_id(sessionId);			        		
			        		//dbutil.createBalanceReserve(balanceReserve); TODO

			        		}
			        		
						  reserveSn++;
			        	}
					  
					  if(resultCode == -1 ){
						  return 1; //不能完全销清
					  }
					  
					
				}else if (operationType ==2){ //扣费
					//TODO U包的扣费和T包的扣费是不一样的啊！！！！！T包的实扣可能少于预占量,也可能大于预占量啊，我靠
					// 预付费扣费需要先查询预占信息
					//TODO List<BalanceReserve> iBalanceReserves = dbutil.getBalanceReservebySessionId(sessionId);
					//sessionid
			
      
					//batch update
					//dbutil.saveBalanceReserve(tmpbr);
					
					//TODO 如果不能完全销清，需要记录欠费
					//TODO 补充：预付费预占后扣费时余额不足的，直接扣real_balance到负
					
				} 
				
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return -1;
		}

		return 0;
	}

/**
 * 调用销账函数，校验是否可销账成功
 * @param writeOffDetail 模拟销账详情
 * @return  0可以销清 -1不能完全销清
 * @throws Exception
 */
	public int getDeductBalanceInfo(RateData rateData,List<WriteOffDetail> writeOffDetail) throws Exception {

		String userId = ratingMsg.getM_iUserMsg().getLnServId();
		int acctMonth = getAcctMonth();//获取账期
		List<UserBillSummary> usedInfo = new ArrayList<UserBillSummary>();
		Map<Integer,Double> feeMap = new HashMap<Integer,Double>();
		int unitTypeId = getUnitTypeId();
		List<OfrRateData> iOfrResults = rateData.getiOfrResults();
		if(iOfrResults.size() ==0){
			throw new Exception("批价结果为空，不需销账");
		}
		for(OfrRateData ofrRateData:iOfrResults){
			List<SectionRateData> iSectionResults = ofrRateData.getiSectionResults();
			for(SectionRateData sectionRateData:iSectionResults){
				int acctItemId = sectionRateData.getLnAcctItemTypeId();
				Double fee = sectionRateData.getdFee();
				if(feeMap.containsKey(acctItemId)){					
					feeMap.put(acctItemId, feeMap.get(acctItemId)+fee);
				}else{
					feeMap.put(acctItemId, fee);
				}
			}			
		}
		
		log.debug("待销账账目项数量："+feeMap.size());
		for(int key:feeMap.keySet()){
			UserBillSummary userBillSummary = new UserBillSummary();
			userBillSummary.setUser_id(userId);
			userBillSummary.setAcct_month(acctMonth);
			userBillSummary.setUnit_type_id(unitTypeId);
			userBillSummary.setFee(feeMap.get(key).longValue());
			userBillSummary.setAcct_item_code(key);
			userBillSummary.setRaw_fee(feeMap.get(key).longValue());		
			usedInfo.add(userBillSummary);
		}

		int caller = BasicType.WRITE_OFF_CALLER_RATING;

		int resultCode =-1; ///调用销账函数结果  0可以销清 -1不能完全销清
		WriteOff writeOff = new WriteOff();
		try {
			resultCode = writeOff.writeOff(userId, usedInfo, acctMonth, caller, writeOffDetail);
		} catch (Exception e) {
			log.debug("调用销账函数writeOff异常:"+e.getLocalizedMessage());
			throw e;
		}

		return resultCode;
	}

	/**
	 * 根据消息类型获取销账需要的unit_type_id
	 * @return
	 */
private int getUnitTypeId() throws Exception {
	
	String msgType = ratingMsg.getBaseMsg().getM_strMsgType();
	int unitTypeId =-1;
	if(msgType ==null || msgType.length() ==0){
		throw new Exception("消息类型异常!");
	}
	if(msgType.equals("60")){
		unitTypeId =1;
	}else if (msgType.equals("70")){
		unitTypeId =3;
	}else if (msgType.equals("80")){
		unitTypeId = 2;
	}else if (msgType.equals("90")){
		unitTypeId = 5;
	}else{
		//暂时没有映射poc\im\dsl三种业务类型
	}
	
	//消息类型	
	//	60-IN Request
	//	70-SMS Request
	//	80-CCG/GGSN Request或CDMA AAA Request
	//	90-ISMP/VAC Request
	//	100-POC Request
	//	110-IM Request
	//	120-DSL Request
		
	//销账侧需要的类型		
	//	public static final int UNIT_TYPE_MONEY = 0;
	//	public static final int UNIT_TYPE_VOICE = 1;
	//	public static final int UNIT_TYPE_GGSN = 2;
	//	public static final int UNIT_TYPE_SMS = 3;
	//	public static final int UNIT_TYPE_MMS = 4;
	//	public static final int UNIT_TYPE_VAC = 5;
	//	public static final int UNIT_TYPE_WLAN = 6;
	//	public static final int UNIT_TYPE_RESERVE = 7;

	return unitTypeId;
}

private int getAcctMonth() throws Exception {
	String strCurrTime = ratingMsg.getVarMsg().getM_strCurrTime();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	Date curDate;
	try {
		curDate = sdf.parse(strCurrTime);
	} catch (ParseException e) {
		throw new Exception("获取账期时转换日期格式异常!");
	}
	CodeAcctMonth cam = dbutil.getCodeAcctMonth(curDate);
	return cam.getAcct_month();
}

}
