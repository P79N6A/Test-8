package com.tydic.beijing.billing.interfacex.service.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.UserPayInfo;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.CheckSubsTerminate;

public class CheckSubsTerminateImpl implements CheckSubsTerminate{

	private  Logger log=Logger.getLogger(CheckSubsTerminateImpl.class);
	
	
	@Override
	public long check(String nbr) {
		String msisdn=nbr;
		
		log.debug("服务[CheckSubsTerminate],号码["+msisdn+"]");
		
		DbTool db=new DbTool();
  		
		List<UserPayInfo>  userInfos=db.queryUserInfoByNbr(msisdn);
	  	if(userInfos == null || userInfos.isEmpty()){
	  			
	  			return -1;
	  	}
	  		
	  		
	  	int findPay=0;
	  	String payId="";
	  	for(UserPayInfo iter : userInfos){
	  		if(iter==null)
	  			continue;
	  		payId=iter.getPay_id();
	  		if(payId !=null && !payId.isEmpty()){
	  			findPay=1;
	  		}
	  		String tag=iter.getDefault_tag();
	  		if(tag!=null && tag.equals("0")){
	  			findPay=2;
	  			break;
	  		}
	  	}
	  	if(findPay==0){
	  		log.debug("账户不存在");
  			return -1;
	  	}else if(findPay==1){
	  		log.debug("默认账户不存在");
  			return -1;
	  	}
	  	
	  	String userId=userInfos.get(0).getUser_id();
	  	
	  	int month=Calendar.getInstance().get(Calendar.MONTH)+1;
	  	String acctMonth=month>9?""+month:"0"+month; 
	  	long nonDeductFee=db.queryNonDeductFee(userId, payId, acctMonth);
	  	long oweFee=db.queryOweFee(userId, payId);
	  	
	  	return nonDeductFee+oweFee;
	  	
	}

	
}
