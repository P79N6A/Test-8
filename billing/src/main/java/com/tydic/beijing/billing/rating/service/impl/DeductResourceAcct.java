
package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.account.core.WriteOff;
import com.tydic.beijing.billing.account.type.UserBillSummary;
import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.beijing.billing.rating.domain.DeductInfo;
import com.tydic.beijing.billing.rating.domain.OfrRateData;
import com.tydic.beijing.billing.rating.domain.RateMeasure;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingDeductLog;
import com.tydic.beijing.billing.rating.domain.RatingErrorCode;
import com.tydic.beijing.billing.rating.domain.RatingException;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;
///import org.apache.log4j.Logger;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;



public class DeductResourceAcct {
	
		

		@Autowired
		private DbUtil dbutil;
//		@Autowired
//		private UserInfoForMemCached userinfoForMemcached;
		@Autowired
	    private ApplicationContextHelper applicationContextHelper;

		//private static final Logger log = Logger.getLogger(DeductResourceAcct.class);
		private static final Logger log = LoggerFactory.getLogger(DeductResourceAcct.class);
		
	
	/**
	 * 
	 * @return 返回资源直扣结果 0资源账本够扣 且扣成功  1资源账本不够扣，扣完资源账本后还有剩余使用量 
	 * @throws Exception
	 * ,int specifybalanceType
	 */
    public int deductResourceAcctBalance(int acctItemCode,List<WriteOffDetail> lwriteOffDetail,RatingMsg ratingMsg,RatingData ratingData,OfrRateData ofrRateData,int specifybalanceType) throws Exception{
    	
//		List<WriteOffDetail> lwriteOffDetail = new ArrayList<WriteOffDetail>();
		//UserInfoForMemCached userinfoForMemcached;
    	
    	if(acctItemCode ==-1){ //如果acctitemcode =-1 说明是免费事件，不需要扣资源，也不需要批价
    		return 0;
    	}
    	
    	log.debug("开始抵扣资源账本  start....");
    	
    	//调用账务销账函数，确认是否有资源账本可用
		String strCurrTime = ratingMsg.getAllSessionStartTimes();  //ratingMsg.getBaseMsg().getM_strStartTime()
				// ratingMsg.getVarMsg().getM_strCurrTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = sdf.parse(strCurrTime);

		DbConfigDetail dbConfigDetail = (DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
//		Calendar ca=Calendar.getInstance();
//		ca.clear();
//		ca.setTime(curDate);
//		ca.set(Calendar.HOUR_OF_DAY, 0);
//		ca.set(Calendar.MINUTE, 0);
//		ca.set(Calendar.SECOND, 0);
//		ca.set(Calendar.MILLISECOND,0);
//		curDate=ca.getTime();
		//int acctMonth =dbConfigDetail.getAcctMonth(curDate); 改为使用ratingmsg里的账期
		int acctMonth = Integer.parseInt(ratingMsg.getAcctMonthId());
		if(acctMonth ==-1){
			//System.out.println("获取账期失败,日期["+curDate+"]");
			throw new RatingException(RatingErrorCode.ERR_GET_BILLING_CYCLE,"获取账期失败");
		}

//		//解析完先获取UserInfoForMemCached
//  	   SpyMemcachedClient smc =(SpyMemcachedClient) applicationContextHelper.getBean("SpyMemcachedClient");
//       
//  		MemcachedClient  mc = smc.getMClient();
//  		String phoneNumber = ratingMsg.getBaseMsg().getM_strCallingNbr();
//  		
//  		userinfoForMemcached =(UserInfoForMemCached) mc.get(phoneNumber);
//  		
//  		mc.shutdown();
//  		if(userinfoForMemcached == null){
//  			throw new RatingException(RatingErrorCode.ABM_ERR_NO_USER,"没有找到用户信息");
//  		}
//  		///获取UserInfoForMemCached结束
		

		List<UserBillSummary> usedInfo = new ArrayList<UserBillSummary>();
		    int unitTypeId = getUnitTypeId(ratingMsg);
		    long roundValue = getRoundValue(unitTypeId,ofrRateData);
		    //String userId =userinfoForMemcached.getInfoUser().getUser_id();
		    String userId =ratingMsg.getUserinfoForMemcached().getInfoUser().getUser_id();
		    
		    log.debug("DeductResourceAccount-->userid===="+userId);
		   
		    
		    String billDate = strCurrTime.substring(0,8);
		    log.debug("DeductResourceAccount-->billdate===="+billDate);

			UserBillSummary userBillSummary = new UserBillSummary();
			userBillSummary.setUser_id(userId);
			userBillSummary.setAcct_month(acctMonth);
			userBillSummary.setUnit_type_id(unitTypeId);
			userBillSummary.setFee(roundValue); 
			userBillSummary.setAcct_item_code(acctItemCode);
			userBillSummary.setBill_date(billDate);
			//userBillSummary.setRaw_fee();		
			usedInfo.add(userBillSummary);
		

		int caller = BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY;

		int resultCode =-1; ///调用销账函数结果  0可以销清 -1不能完全销清
		
		WriteOff writeOff = (WriteOff) applicationContextHelper.getBean("writeOff");
		
		//WriteOff writeOff = new WriteOff();
		
		UserInfoForMemCached userinfoForMemcached = ratingMsg.getUserinfoForMemcached();
		
		long starttime =System.currentTimeMillis();
		long endtime =1L;
		
		try {
			resultCode = writeOff.writeOff(userId, usedInfo, acctMonth, caller, lwriteOffDetail,userinfoForMemcached);
		} catch (BasicException be){
			be.printStackTrace();
			endtime = System.currentTimeMillis();
	    	log.debug("调用销账函数耗时："+(endtime-starttime));
			
			throw  new RatingException(be.getCode(),be.getMessage());
		} catch (Exception e) {
			
			e.printStackTrace();
			endtime = System.currentTimeMillis();
	    	log.debug("调用销账函数耗时："+(endtime-starttime));
				
			throw  new RatingException(RatingErrorCode.ERR_INVOKE_ACCT,"调用销账函数失败");
		}
		
    	endtime = System.currentTimeMillis();
    	log.debug("调用销账函数耗时："+(endtime-starttime));
    	
    	//如果是自动续订扣资源，这里只扣指定类型账本的资源
//    	List<WriteOffDetail> writeOffDetail2 = new ArrayList<WriteOffDetail>();
//    	if(specifybalanceType>0){
//    		for(WriteOffDetail detail:lwriteOffDetail){
//    			if(detail.getBalance_type_id()==specifybalanceType){
//    				writeOffDetail2.add(detail);
//    			}
//    		}
//    	//	lwriteOffDetail = writeOffDetail2;
//    	}
    	
    	if(lwriteOffDetail != null && lwriteOffDetail.size()>0){
    		deductResourceDirect(resultCode,lwriteOffDetail ,ratingData,ratingMsg);   
    		
    	}
		
    
		long deductendtime = System.currentTimeMillis();
		log.debug("资源账本扣减耗时："+(deductendtime-endtime));
		
		if((deductendtime-endtime)>0){
			log.debug("资源扣减耗时>0,本次请求消息："+ratingMsg.getStrRequestMsg());
		}
		
		
		//写入ofrRateData 具体扣了多少
		List<Object> balanceTypeList = new ArrayList<Object>(); 
		long chargedvalue =0L;
		String payId ="";
		for(WriteOffDetail writeOffDetail:lwriteOffDetail){
			chargedvalue = chargedvalue + writeOffDetail.getWriteoff_fee();
			payId = writeOffDetail.getPay_Id();
			
			int tmpbalancetype = writeOffDetail.getBalance_type_id();
			if(!balanceTypeList.contains(tmpbalancetype) && writeOffDetail.getWriteoff_fee() >0){ //如果该账本扣减大于0，且未包含在账本扣减类型list里，则add
				balanceTypeList.add(tmpbalancetype);
			}
			
		}
		
		log.debug("资源直扣共扣了使用量====>"+chargedvalue+",并且资源账目项acctItemCode==》"+acctItemCode);
		 ///调用销账函数结果  0可以销清 -1不能完全销清  resultCode
		
		String createTime = sdf.format(new Date());
		String allSessionStartTimes = ratingMsg.getAllSessionStartTimes().substring(0, 8);
		
		if(specifybalanceType <=0){//自动续订不发短信
			for(int i =0;i<balanceTypeList.size();i++){
				int balanceTypeId = (int) balanceTypeList.get(i);
				sendResourceOverSms(resultCode,chargedvalue,payId,acctItemCode,ratingMsg.getM_nMsgType(),ratingMsg.getBaseMsg().getM_strChargedNbr(),createTime,
						balanceTypeId,allSessionStartTimes);
				
			}
		}
		
			
	    String measuredomain = ofrRateData.getiRateMeasure().getM_iUsedMeasureDomain()+"";
	    if(measuredomain.equals(RatingMacro.SECTION_DOMAIN_DURATION)){
	    	chargedvalue =  chargedvalue*RatingMacro.UNIT_VALUE_VOICE;
	    }else if (measuredomain.equals(RatingMacro.SECTION_DOMAIN_TOTALVOLUME)){
	    	chargedvalue =  chargedvalue*RatingMacro.UNIT_VALUE_GGSN;
	    }else{
	    	
	    }
		
		ofrRateData.getiRateMeasure().addChargedDosage( chargedvalue);//计费账务两套编码，脑补很重要啊
	
    	return resultCode;
    }



private void sendResourceOverSms(int resultCode, long chargedvalue,
			String payId, int acctItemCode ,int msgType,String chargedPhone,String createTime, int balanceTypeId,String allSessionStartTimes) {

      //如果够扣，并且余额是0，或者不够扣，并且扣了资源量，则发资源用尽提醒短信
	//果  0可以销清 -1不能完全销清  resultCode
	
	if(payId == null || payId.length()==0){
		//如果payid是空，则认为没有扣资源账本
		return ;
	}
	
	//非 4,5,10,11,12 不发短信
	if (balanceTypeId!=4 && balanceTypeId!=5 && balanceTypeId!=10 && balanceTypeId!=11 && balanceTypeId!=12 ){
		return ;
	}
	
	log.debug("接收的acctItemCode=="+acctItemCode);
	   if(resultCode ==0 ){
		   //判断用户相关类型账本额是否为0
		   
		   List<InfoPayBalance> listInfoPayBalance = dbutil.getSumResourceBalance(payId,balanceTypeId,allSessionStartTimes);
		   long sumResourceBalance =0L;
		   for(InfoPayBalance tmpipb:listInfoPayBalance){
			   sumResourceBalance = sumResourceBalance + tmpipb.getReal_balance();
		   }
		   
		   if(sumResourceBalance ==0 && listInfoPayBalance.size()>0){
			   dbutil.sendResourceOverSms(chargedPhone,msgType,createTime,balanceTypeId);
		   }
		   
	   }else if (resultCode ==-1 && chargedvalue>0){
		   dbutil.sendResourceOverSms(chargedPhone,msgType,createTime,balanceTypeId);   
	   }
			
	}



/**
 * 更新ratemeasure
 * resultcode 调用账务销账函数的返回code
 */
	private void changeRateMeasureOfRatingData(int resultcode,List<WriteOffDetail> listofwriteOffDetail,RatingData ratingData) {
		
		if(resultcode ==0){
			//资源账本够扣 
			RateMeasure newRateMeasure = ratingData.getiRateMeasure();
//			newRateMeasure.setLnDuration(0L);
//			newRateMeasure.setLnMoney(0L);
//			newRateMeasure.setLnTotalVolume(0L);
//			newRateMeasure.setLnUpVolume(0L);
//			newRateMeasure.setLnDownVolume(0L);
//			newRateMeasure.setLnTimes(0L);
//			ratingData.setiRateMeasure(newRateMeasure);	
			
			newRateMeasure.setLnChargedDownVolume(newRateMeasure.getLnDownVolume());
			newRateMeasure.setLnChargedDuration(newRateMeasure.getLnDuration());
			newRateMeasure.setLnChargedTimes(newRateMeasure.getLnTimes());
			newRateMeasure.setLnChargedTotalVolume(newRateMeasure.getLnTotalVolume());
			newRateMeasure.setLnChargedMoney(newRateMeasure.getLnMoney());
			
			ratingData.setiRateMeasure(newRateMeasure);
	
			
		}else if (resultcode ==-1){
			//资源账本不够扣  在更新ratemeasure前需要恢复圆整
			long totalWriteOffFee =0L;
			int unitTypeId =0;
			for(WriteOffDetail tmpwod:listofwriteOffDetail){
				totalWriteOffFee = totalWriteOffFee + tmpwod.getWriteoff_fee();
				unitTypeId = tmpwod.getUnit_type_id();
			}
			
			RateMeasure oldRateMeasure = ratingData.getiRateMeasure();
			if(unitTypeId ==1){//语音
				oldRateMeasure.setLnDuration(oldRateMeasure.getLnDuration() - totalWriteOffFee*RatingMacro.UNIT_VALUE_VOICE);
			}else if (unitTypeId ==2){//数据
				oldRateMeasure.setLnTotalVolume(oldRateMeasure.getLnTotalVolume() - totalWriteOffFee*RatingMacro.UNIT_VALUE_GGSN);
			}else if (unitTypeId ==3){ //短信
				oldRateMeasure.setLnTimes(oldRateMeasure.getLnTimes() - totalWriteOffFee*RatingMacro.UNIT_VALUE_SMS);
			}else if (unitTypeId ==5){//vac
				oldRateMeasure.setLnTimes(oldRateMeasure.getLnTimes() - totalWriteOffFee*RatingMacro.UNIT_VALUE_SMS);
			}
			
			ratingData.setiRateMeasure(oldRateMeasure);
		}
		
	}



/**
 * 把扣除资源的详细信息写入ratingData.iDeductInfos，
 * @param listofwriteOffDetail
 */
	private void writeDeductInfoOfRatingData(List<WriteOffDetail> listofwriteOffDetail,RatingData ratingData) {
		
		List<DeductInfo> iDeductInfos = ratingData.getiDeductInfos();
		log.debug("写入资源账本扣除记录开始--------");
		for(WriteOffDetail tmpwod:listofwriteOffDetail){
			
			
			DeductInfo deductInfo = new DeductInfo();
			deductInfo.setLnAcctBalanceID(tmpwod.getBalance_id());
			deductInfo.setnBalanceUnitType(tmpwod.getUnit_type_id());    //basictype.UNIT_TYPE_xxx 相当于业务类型
			deductInfo.setLnAmount(tmpwod.getWriteoff_fee()); //扣除金额
			deductInfo.setLnBalance(tmpwod.getAfter_balance());//账本余额
			deductInfo.setLnBalanceTypeId(tmpwod.getBalance_type_id()); //账本类型
			deductInfo.setLnAcct_ItemType_id(tmpwod.getAcct_item_code());//账目项
			
			deductInfo.setPayId(tmpwod.getPay_Id());
			
			
			log.debug("写入资源账本扣除记录--------->"+deductInfo.getLnAcctBalanceID());
			log.debug("写入资源账本扣除记录--------->"+deductInfo.getLnAmount());
			
			iDeductInfos.add(deductInfo);
		}
		
	    ratingData.setiDeductInfos(iDeductInfos);
		
		}

	/**
	 * 实扣资源账本
	 * @param listofwriteOffDetail
	 * @return
	 * @throws Exception
	 */
	private void deductResourceDirect(int resultCodefromWriteOff,List<WriteOffDetail> listofwriteOffDetail,RatingData ratingData ,RatingMsg ratingMsg) throws Exception {
			
		for(WriteOffDetail writeOffDetail:listofwriteOffDetail){
			//String payId = writeOffDetail.getPay_Id();//账户
    		long balanceId = writeOffDetail.getBalance_id();//账本
    		long realBalance = writeOffDetail.getWriteoff_fee();
    		long usedBalance =0L; //后付费没有usedbalance

    	    dbutil.updateInfoPayBalance(balanceId, realBalance, usedBalance);
    	    
    	    log.debug("threadidis "+Thread.currentThread().getId()+"and after balance is "+writeOffDetail.getAfter_balance());
    	    //扣减后查询余量是否<0，如果<0，则返回异常,重新走批价
    	    InfoPayBalance infoPayBalance  = dbutil.getInfoPayBalanceAfterUpdate(balanceId);
    	    if(infoPayBalance.getReal_balance() <0){
    	    	log.debug("resultisminus!! resultrealbalance is "+infoPayBalance.getReal_balance() +",and deduct "+realBalance+" on balanceid"+balanceId);
    	    	throw new RatingException (RatingErrorCode.ERR_BALANCE_MINUS,"资源账本不能扣成负");
    	    }
    	    
    	    log.debug("result is not minus!!");
    	    
    	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	    String month = sdf.format(new Date()).substring(4,6);

    	    RatingDeductLog ratingDeductLog = new RatingDeductLog();
    	    ratingDeductLog.setBalanceid(writeOffDetail.getBalance_id()+"");
    	    ratingDeductLog.setDeducttime(new Date());
    	    ratingDeductLog.setDeductvalue(writeOffDetail.getWriteoff_fee()+"");
    	  //  ratingDeductLog.setMsisdn(writeOffDetail.getUser_Id());
    	    ratingDeductLog.setUserid(writeOffDetail.getUser_Id());
    	    ratingDeductLog.setNewbalance(writeOffDetail.getAfter_balance()+"");
    	    ratingDeductLog.setReqmsg(ratingMsg.getStrRequestMsg());
    	    ratingDeductLog.setPartitionId(month);
    	    dbutil.createDeductLog(ratingDeductLog);
		}
	

		//并且把扣除资源的详细信息写入ratingData.iDeductInfos，需要注意扣除顺序，统一接入最终需要正确的扣除轨迹
    	writeDeductInfoOfRatingData(listofwriteOffDetail,ratingData);
    	//更新ratingdata.ratemeasure
//    	changeRateMeasureOfRatingData(resultCodefromWriteOff,listofwriteOffDetail);
    	//更新累积量        	
        //updateRatableResource(); 已在外围调用，这里的取消
        
      
	}


	/**
	 * 圆整方法，用于调用账务销账函数前
	 * @return
	 */
	private long getRoundValue( int unitTypeId,OfrRateData ofrRateData) {
	 //根据unittype做圆整，unit对应着不同业务的单位类型
		int unitValue =1;
		long primaryValue =0L;
		if(unitTypeId ==1){//语音
			unitValue =RatingMacro.UNIT_VALUE_VOICE;
			primaryValue = ofrRateData.getiRateMeasure().getUnchargeDosage("1");
		}else if(unitTypeId ==2){//数据业务
			unitValue =RatingMacro.UNIT_VALUE_GGSN;
			primaryValue = ofrRateData.getiRateMeasure().getUnchargeDosage("2");
		}else if(unitTypeId ==3){//短信业务
			unitValue =RatingMacro.UNIT_VALUE_SMS;
			primaryValue = ofrRateData.getiRateMeasure().getUnchargeDosage("3");
		}else if(unitTypeId ==5){//vac
			unitValue =RatingMacro.UNIT_VALUE_VAC;
			primaryValue = ofrRateData.getiRateMeasure().getUnchargeDosage("3");
		}
		
		long roundValue =getLong(1.0*primaryValue/unitValue,0);
		
		return roundValue;
    }
	

	
	/**
	  * 取整
	  * @param dSpanValues
	  * @param tail_mod  0向上取整   1向下取整  2四舍五入
	  * @return
	  */
		private long getLong(double dSpanValues, int tail_mod) {
			
			  long nValue = (long)dSpanValues;
		        if( tail_mod == 0 ){
		            if( dSpanValues - nValue > 0.0000 )
		                nValue++;
		        }else if( tail_mod == 1 ){
		            ; //no op
		        }else{
		            if( dSpanValues - nValue >= 0.5 )
		                nValue++;
		        }
		        return nValue;
	}



	/**
	 * 	public static final int UNIT_TYPE_MONEY = 0;
	public static final int UNIT_TYPE_VOICE = 1;
	public static final int UNIT_TYPE_GGSN = 2;
	public static final int UNIT_TYPE_SMS = 3;
	public static final int UNIT_TYPE_MMS = 4;
	public static final int UNIT_TYPE_VAC = 5;
	public static final int UNIT_TYPE_WLAN = 6;
	public static final int UNIT_TYPE_RESERVE = 7;
	 * @return
	 * @throws Exception
	 */
	private int getUnitTypeId(RatingMsg ratingMsg) throws Exception {
		
		String msgType = ratingMsg.getBaseMsg().getM_strMsgType();
		int unitTypeId =-1;
		if(msgType ==null || msgType.length() ==0){
			throw new RatingException(RatingErrorCode.ERR_MSG_DEDUCT,"消息类型异常!");
		}
		if(msgType.equals("60")){
			unitTypeId =BasicType.UNIT_TYPE_VOICE;
		}else if (msgType.equals("70")){
			unitTypeId =BasicType.UNIT_TYPE_SMS;
		}else if (msgType.equals("80")){
			unitTypeId = BasicType.UNIT_TYPE_GGSN;
		}else if (msgType.equals("90")){
			unitTypeId = BasicType.UNIT_TYPE_VAC;
		}else{
			throw new RatingException(RatingErrorCode.ERR_GET_ACCTUNITTYPE,"没有消息类型对应的账务unittype");
		}
		
		return unitTypeId;
	}




	
	
	
	

}
