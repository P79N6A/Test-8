package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.UserCorpInfo;
import com.tydic.beijing.billing.dao.RuleSystemSwitch;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.beijing.billing.rating.domain.BalanceContent;
import com.tydic.beijing.billing.rating.domain.BalanceInMsg;
import com.tydic.beijing.billing.rating.domain.BaseMsg;
import com.tydic.beijing.billing.rating.domain.CodeCountry;
import com.tydic.beijing.billing.rating.domain.CodeSpecialNbr;
import com.tydic.beijing.billing.rating.domain.CommandMsg;
import com.tydic.beijing.billing.rating.domain.DeductInfo;
import com.tydic.beijing.billing.rating.domain.ExtMsg;
import com.tydic.beijing.billing.rating.domain.Ratable;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingErrorCode;
import com.tydic.beijing.billing.rating.domain.RatingException;
import com.tydic.beijing.billing.rating.domain.RatingExtMsg;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.RuleEventTypeTree;
import com.tydic.beijing.billing.rating.domain.VarMsg;
import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;
import com.tydic.beijing.billing.rating.service.MsgParsing;
import com.tydic.uda.service.S;

public class MsgParsingImpl implements MsgParsing {
	
	@Autowired
	 private DbUtil dbUtil;
	
	//private RatingMsg ratingMsg;
	
	//private RatingData ratingData;
	
	@Autowired
	private ApplicationContextHelper applicationContextHelper;

	private DbConfigDetail dbConfigDetail;
	// @Autowired
	// private SpyMemcachedClient spyMemcachedClient;
	// private Map<Object,Object> msgMap;

	private UserCorpInfo caling;

	private UserCorpInfo caled;
	private static final String CORP_EFF_FLAG="0";
	private static final String EFF_FLAG="0";
	private static final String MEM_TYPE_MANAGE="01";
	private static final String MEM_TYPE_COMMON="00";
	private static final String COMM_TO_COMM="1";
	private static final String MANAG_TO_COMM="2";
	private static final String COMM_TO_MANAG="3";
	private static final String OTHER_TYPE="99";
	
	 private static  Logger log = Logger.getLogger(MsgParsingImpl.class);
 
	/**
	 * string消息解析，放到RatingMsg里
	 */
	@Override
	public RatingMsg getRatingMsgFromRequestMsg(RatingMsg ratingMsg,String strRequestMsg) throws Exception {
		
//	     String  m_szFieldElementName;       //临时存放  单位
//	     String  m_szFieldValueName;          //临时存放  量
	     String  m_szFieldProductID; 
//	     String  m_szFieldElementName2; 
//	     String  m_szFieldValueName2; 
//	     String  m_szFieldProductID2; 
//	     String  m_szFieldValueNameLast; 
//	     String  m_szFieldGroup;
	   //  boolean bExistFeeInfo = false;      //是否包含费用信息
	     boolean bExistLastValue =false ;     ///是否存在费率切换点
	     int creditUnit;
	     Long creditCount;
	     Long creditCountLast = 0L;
	     long eventTypeId = 0L;
	     int nBillingFlag ;
	     
	     Map<Object,Object> msgMap =generateMapFromMsg(strRequestMsg);
	  
	     
	     dbConfigDetail = (DbConfigDetail)  applicationContextHelper.getBean("dbConfigDetail");
	     if(dbConfigDetail.getAllRuleEventTypeTree().size() ==0){
	    	 throw new RatingException(RatingErrorCode.ERR_MSG_NULL,"事件树为空，请检查事件配置表");
	     }
	     
		// Vector<BalanceContent> ltFree;       //释放
		 List<BalanceContent> ltBack = new ArrayList<BalanceContent>();       //返回，解析B04组金钱类的保存位置
	//	 Vector<BalanceContent> ltQuery;      //查询
		 List<BalanceContent> ltDeduct = new ArrayList<BalanceContent>();     //实扣，解析B03组金钱类的保存位置
		 List<BalanceContent> ltReserve = new ArrayList<BalanceContent>();    //预占，解析B01组金钱类的保存位置
		 List<BalanceContent> ltRecharge = new ArrayList<BalanceContent>();   //充值
		 List<BalanceContent> ltUsed = new ArrayList<BalanceContent>();       //更新使用金额，解析B30金钱的保存位置
		 BalanceContent tmpBalanceContent =new BalanceContent();
		// RatingMsg ratingMsg =new RatingMsg() ;
	    
		try {	
			ratingMsg.setStrRequestMsg(strRequestMsg);
		   
		    
		    
		
	

		if(strRequestMsg==null || strRequestMsg.length()==0){////消息是空
			throw new RatingException(RatingErrorCode.ERR_SERVICE_TYPE_NOT_EXIST,"消息为空");
		}

		   BaseMsg baseMsg = getBaseMsg(msgMap);

			VarMsg varMsg = getVarMsg(msgMap);
			nBillingFlag =varMsg.getM_nBillingFlag();
		//baseMsg.print();
			ExtMsg extMsg = getExtMsg(msgMap);
			    
			RatingExtMsg ratingExtMsg =getRatingExtMsg(msgMap);
			
			////如果basemsg的starttime为空，则取varmsg的当前时间
			if(baseMsg.getM_strStartTime() ==null || baseMsg.getM_strStartTime().equals("0") || baseMsg.getM_strStartTime().length() ==0 ){
				baseMsg.setM_strStartTime(varMsg.getM_strCurrTime());
			}
			
			
			ratingMsg.setM_nBillingFlag(nBillingFlag);
			ratingMsg.setVarMsg(varMsg);
			ratingMsg.setBaseMsg(baseMsg);
			ratingMsg.setM_nMsgType(Integer.parseInt(baseMsg.getM_strMsgType()));
			
			//ratingMsg.setCommandMsg(commandMsg);
			
			//ratingMsg.setM_iBalanceInMsg(balanceInMsg);
			ratingMsg.setM_iRatingExtMsg(ratingExtMsg);
			
			////是否需要处理充值或释放？？balanceinmsg和balanceoutmsg？
			////msgtype=20 余额类操作需要解析balanceinfo，后续再增加
			
			////RatingMsg::replaceParameter 参数替换　需要确定是否需要转换

		if(baseMsg.getM_strRatingGroup()!= null && baseMsg.getM_strRatingGroup().length()>0){
			msgMap.put("RGP", baseMsg.getM_strRatingGroup());   //////RG放到map里，用于后面提取事件码
		}
		

		CommandMsg commandMsg = new CommandMsg();
		BalanceInMsg balanceInMsg = new BalanceInMsg();
		//commandmsg和balanceinmsg放到一起来解析  start
		/////B01
		if(msgMap.containsKey(RatingMacro.PARA_B_RESERVE)){
			if(msgMap.get(RatingMacro.PARA_B_RESERVE) instanceof String){////只有一组B01
				
				creditUnit = Integer.parseInt((String)msgMap.get(RatingMacro.PARA_B_RESERVE_CREDIT_UNIT));
				creditCount = Long.parseLong((String)msgMap.get(RatingMacro.PARA_B_RESERVE_CREDIT_COUNT));
		         m_szFieldProductID = RatingMacro.PARA_B_RESERVE_PRODUCT_ID ;
		         
		         if(isMoneyBalanceType(creditUnit)){//如果是金钱类请求
		        	 
		        	 BalanceContent tmpbal =new BalanceContent();
		        	 
		        	 tmpbal.setLnAcctItemTypeId(-1);
		        	 tmpbal.setnUnitTypeId(creditUnit);
		        	 tmpbal.setLnAmount(creditCount.intValue());
		        	 ltReserve = new ArrayList<BalanceContent>();
		        	 ltReserve.add(tmpbal);
 
		        	 balanceInMsg.setLtReserve(ltReserve);
		         }else{//非金钱类请求
		        	 
		        	 if( creditUnit == RatingMacro.CREDITUNIT_TIMELEN){//时长
		        		 commandMsg.setM_strReqDuration(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_TOTALVAL){//总流量
		        		 commandMsg.setM_strReqTotalVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_UPVAL){
		        		 commandMsg.setM_strReqUpVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_DOWNVAL){
		        		 commandMsg.setM_strReqDownVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_COUNT){
		        		 commandMsg.setM_strReqTimes(creditCount);
		        	 } else {
		        	 /////
		        	 }
		        	 
		         }         	
			} else if (msgMap.get(RatingMacro.PARA_B_RESERVE) instanceof List){
				
				List<Map> groupLists = (ArrayList)msgMap.get(RatingMacro.PARA_B_RESERVE);
				for(Map group :groupLists){
					
					creditUnit = Integer.parseInt((String)group.get(RatingMacro.PARA_B_RESERVE_CREDIT_UNIT));
					creditCount = Long.parseLong((String)group.get(RatingMacro.PARA_B_RESERVE_CREDIT_COUNT));
			         m_szFieldProductID = RatingMacro.PARA_B_RESERVE_PRODUCT_ID ;
			         
			         if(isMoneyBalanceType(creditUnit)){//如果是金钱类请求
			        	 
			        	 BalanceContent tmpbal =new BalanceContent();
			        	 
			        	 tmpbal.setLnAcctItemTypeId(-1);
			        	 tmpbal.setnUnitTypeId(creditUnit);
			        	 tmpbal.setLnAmount(creditCount.intValue());
			        	 ltReserve = new ArrayList<BalanceContent>();
			        	 ltReserve.add(tmpbal);
			        	 
			        	 balanceInMsg.setLtReserve(ltReserve);
			         }else{//非金钱类请求
			        	 
			        	 if( creditUnit == RatingMacro.CREDITUNIT_TIMELEN){//时长
			        		 commandMsg.setM_strReqDuration(creditCount);
			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_TOTALVAL){//总流量
			        		 commandMsg.setM_strReqTotalVolume(creditCount);
			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_UPVAL){
			        		 commandMsg.setM_strReqUpVolume(creditCount);
			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_DOWNVAL){
			        		 commandMsg.setM_strReqDownVolume(creditCount);
			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_COUNT){
			        		 commandMsg.setM_strReqTimes(creditCount);
			        	 } else {
			        	 /////
			        	 }
			        	 
			         }
		
				}
			}else {
				//////
			}
		}
		/////B02
        if(msgMap.containsKey(RatingMacro.PARA_B_FREE)){
	      
        	
		}
        ///B03  T包的ccg业务取B30
        if(msgMap.containsKey(RatingMacro.PARA_B_DEDUCT)){
        	
              if(msgMap.get(RatingMacro.PARA_B_DEDUCT) instanceof String){////只有一组B01
				
				creditUnit = Integer.parseInt((String)msgMap.get(RatingMacro.PARA_B_REAL_CREDIT_UNIT));
				creditCount = Long.parseLong((String)msgMap.get(RatingMacro.PARA_B_REAL_CREDIT_COUNT));
		         m_szFieldProductID = RatingMacro.PARA_B_REAL_PRODUCT_ID ;
		         
		         if(isMoneyBalanceType(creditUnit)){//如果是金钱类请求
		        	 BalanceContent tmpBal =new BalanceContent();
		        	 tmpBal.setLnAcctItemTypeId(-1);         //////账目类型id需要后续再增加
		        	 tmpBal.setnUnitTypeId(creditUnit);
		        	 tmpBal.setLnAmount(creditCount.intValue());
		        	 ltDeduct = new ArrayList<BalanceContent>();
		        	 ltDeduct.add(tmpBal);
		        	 
		        	 balanceInMsg.setLtDeduct(ltDeduct);
		         }else{//非金钱类请求
		        	 
		        	  	 if( creditUnit == RatingMacro.CREDITUNIT_TIMELEN){//时长
		        		 commandMsg.setM_strRealDuration(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_TOTALVAL){//总流量
		        			 //commandMsg.setM_strUsedTotalVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strRealTotalVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_UPVAL){
		        			// commandMsg.setM_strUsedUpVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strRealUpVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_DOWNVAL){
		        			 //commandMsg.setM_strUsedDownVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strRealDownVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_COUNT){
		        		 commandMsg.setM_strRealTimes(creditCount);
		        	 } else {
		        	 /////
		        	 }
		        	 
		         }         	
			} else if (msgMap.get(RatingMacro.PARA_B_DEDUCT) instanceof List){
				
				List<Map> groupLists = (ArrayList)msgMap.get(RatingMacro.PARA_B_DEDUCT);
				for(Map group :groupLists){
					
					creditUnit = Integer.parseInt((String)group.get(RatingMacro.PARA_B_REAL_CREDIT_UNIT));
					creditCount = Math.round(Double.parseDouble((String)group.get(RatingMacro.PARA_B_REAL_CREDIT_COUNT)));
			         m_szFieldProductID = RatingMacro.PARA_B_REAL_PRODUCT_ID ;
			         
			         if(isMoneyBalanceType(creditUnit)){//如果是金钱类请求
			        	 
			        	 BalanceContent tmpbal =new BalanceContent();
			        	 
			        	 tmpbal.setLnAcctItemTypeId(-1);
			        	 tmpbal.setnUnitTypeId(creditUnit);
			        	 tmpbal.setLnAmount(creditCount.intValue());
			        	 ltDeduct = new ArrayList<BalanceContent>();
			        	 ltDeduct.add(tmpbal);
			        	 
			        	 balanceInMsg.setLtDeduct(ltDeduct);
			         }else{//非金钱类请求
			        	 
			        	  	 if( creditUnit == RatingMacro.CREDITUNIT_TIMELEN){//时长
		        		 commandMsg.setM_strRealDuration(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_TOTALVAL){//总流量
		        			 //commandMsg.setM_strUsedTotalVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strRealTotalVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_UPVAL){
		        			// commandMsg.setM_strUsedUpVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strRealUpVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_DOWNVAL){
		        			 //commandMsg.setM_strUsedDownVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strRealDownVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_COUNT){
		        		 commandMsg.setM_strRealTimes(creditCount);
		        	 } else {
		        	 /////
		        	 }
			        	 
			         }
		
				}
			}else {
				//////
			}
			
		}
		///B04
        if(msgMap.containsKey(RatingMacro.PARA_B_BACK)){
			
        	   if(msgMap.get(RatingMacro.PARA_B_BACK) instanceof String){////只有一组B01
   				
   				creditUnit = Integer.parseInt((String)msgMap.get(RatingMacro.PARA_B_BACK_CREDIT_UNIT));
   				creditCount = Long.parseLong((String)msgMap.get(RatingMacro.PARA_B_BACK_CREDIT_COUNT));
   		         m_szFieldProductID = RatingMacro.PARA_B_BACK_PRODUCT_ID ;
   		         
   		         if(isMoneyBalanceType(creditUnit)){//如果是金钱类请求
   		        	BalanceContent tmpbal =new BalanceContent();
   		        	tmpbal.setLnAcctItemTypeId(-1);         //////账目类型id需要后续再增加
   		        	tmpbal.setnUnitTypeId(creditUnit);
   		        	tmpbal.setLnAmount(creditCount.intValue());
   		        	 ltBack = new ArrayList<BalanceContent>();
   		        	ltBack.add(tmpbal);
   		        	 
   		        	 balanceInMsg.setLtBack(ltBack);
   		         }else{//非金钱类请求
   		        	 
   		        	 if( creditUnit == RatingMacro.CREDITUNIT_TIMELEN){//时长
   		        		 commandMsg.setM_strReqDuration(creditCount);
   		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_TOTALVAL){//总流量
   		        		 commandMsg.setM_strReqTotalVolume(creditCount);
   		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_UPVAL){
   		        		 commandMsg.setM_strReqUpVolume(creditCount);
   		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_DOWNVAL){
   		        		 commandMsg.setM_strReqDownVolume(creditCount);
   		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_COUNT){
   		        		 commandMsg.setM_strReqTimes(creditCount);
   		        	 } else {
   		        	 /////
   		        	 }
   		        	 
   		         }         	
   			} else if (msgMap.get(RatingMacro.PARA_B_BACK) instanceof List){
   				
   				List<Map> groupLists = (ArrayList)msgMap.get(RatingMacro.PARA_B_BACK);
   				for(Map group :groupLists){
   					
   					creditUnit = Integer.parseInt((String)group.get(RatingMacro.PARA_B_BACK_CREDIT_UNIT));
   					creditCount = Long.parseLong((String)group.get(RatingMacro.PARA_B_BACK_CREDIT_COUNT));
   			         m_szFieldProductID = RatingMacro.PARA_B_BACK_PRODUCT_ID ;
   			         
   			         if(isMoneyBalanceType(creditUnit)){//如果是金钱类请求
   			        	 
   			        	BalanceContent tmpbal =new BalanceContent();
   			        	tmpbal.setLnAcctItemTypeId(-1);
   			        	tmpbal.setnUnitTypeId(creditUnit);
   			        	tmpbal.setLnAmount(creditCount.intValue());
   			        	ltBack = new ArrayList<BalanceContent>();
   			        	ltBack.add(tmpbal);
   			        	 
   			        	 balanceInMsg.setLtBack(ltBack);
   			         }else{//非金钱类请求
   			        	 
   			        	 if( creditUnit == RatingMacro.CREDITUNIT_TIMELEN){//时长
   			        		 commandMsg.setM_strReqDuration(creditCount);
   			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_TOTALVAL){//总流量
   			        		 commandMsg.setM_strReqTotalVolume(creditCount);
   			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_UPVAL){
   			        		 commandMsg.setM_strReqUpVolume(creditCount);
   			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_DOWNVAL){
   			        		 commandMsg.setM_strReqDownVolume(creditCount);
   			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_COUNT){
   			        		 commandMsg.setM_strReqTimes(creditCount);
   			        	 } else {
   			        	 /////
   			        	 }
   			        	 
   			         }
   		
   				}
   			}else {
   				//////
   			}
        	
        	
		}
        ///B05
        if(msgMap.containsKey(RatingMacro.PARA_B_VOUCHER)){
			
		}
		///B30
        if(msgMap.containsKey(RatingMacro.PARA_B_USED)){

     	   if(msgMap.get(RatingMacro.PARA_B_USED) instanceof String){////只有一组B01
				
				creditUnit = Integer.parseInt((String)msgMap.get(RatingMacro.PARA_B_USED_CREDIT_UNIT));
				creditCount = Long.parseLong((String)msgMap.get(RatingMacro.PARA_B_USED_CREDIT_COUNT));
				if(msgMap.containsKey(RatingMacro.PARA_B_USED_CREDIT_LAST_COUNT)){
					bExistLastValue = true;
					creditCountLast = Long.parseLong((String)msgMap.get(RatingMacro.PARA_B_USED_CREDIT_LAST_COUNT));/////费率切换点之后的流量
				}
				
		         m_szFieldProductID = RatingMacro.PARA_B_USED_PRODUCT_ID ;
		         
		         if(isMoneyBalanceType(creditUnit)){//如果是金钱类请求
		        	 BalanceContent tmpbal =new BalanceContent();
		        	 tmpbal.setLnAcctItemTypeId(-1);         //////账目类型id需要后续再增加
		        	 tmpbal.setnUnitTypeId(creditUnit);
		        	 tmpbal.setLnAmount(creditCount.intValue());
		        	 ltUsed = new ArrayList<BalanceContent>();
		        	 ltUsed.add(tmpbal);
		        	 
		        	 balanceInMsg.setLtUsed(ltUsed);
		         }else{//非金钱类请求
		        	 
		        	 if( creditUnit == RatingMacro.CREDITUNIT_TIMELEN){//时长
		        		 commandMsg.setM_strUsedDuration(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_TOTALVAL){//总流量
		        			 commandMsg.setM_strUsedTotalVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strUsedTotalVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_UPVAL){
		        			 commandMsg.setM_strUsedUpVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strUsedUpVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_DOWNVAL){
		        			 commandMsg.setM_strUsedDownVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strUsedDownVolume(creditCount);
		        	 } else if (creditUnit == RatingMacro.CREDITUNIT_COUNT){
		        		 commandMsg.setM_strUsedTimes(creditCount);
		        	 } else {
		        	 /////
		        	 }
		        	 
		         }         	
			} else if (msgMap.get(RatingMacro.PARA_B_USED) instanceof List){
				
				List<Map> groupLists = (ArrayList)msgMap.get(RatingMacro.PARA_B_USED);
				for(Map group :groupLists){
					
					creditUnit = Integer.parseInt((String)group.get(RatingMacro.PARA_B_USED_CREDIT_UNIT));
					creditCount = Long.parseLong((String)group.get(RatingMacro.PARA_B_USED_CREDIT_COUNT));
					creditCountLast = msgMap.containsKey(RatingMacro.PARA_B_USED_CREDIT_LAST_COUNT)?Long.parseLong((String)msgMap.get(RatingMacro.PARA_B_USED_CREDIT_LAST_COUNT)):0;/////费率切换点之后的流量
			         m_szFieldProductID = RatingMacro.PARA_B_BACK_PRODUCT_ID ;
			         
			         if(isMoneyBalanceType(creditUnit)){//如果是金钱类请求
			        	 BalanceContent tmpbal =new BalanceContent();
			        	 tmpbal.setLnAcctItemTypeId(-1);
			        	 tmpbal.setnUnitTypeId(creditUnit);
			        	 tmpbal.setLnAmount(creditCount.intValue());
			        	 ltUsed = new ArrayList<BalanceContent>();
			        	ltUsed.add(tmpbal);
			        	 
			        	 balanceInMsg.setLtUsed(ltUsed);
			         }else{//非金钱类请求
			        	 
			        	 if( creditUnit == RatingMacro.CREDITUNIT_TIMELEN){//时长
			        		 commandMsg.setM_strReqDuration(creditCount);
			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_TOTALVAL){//总流量
		        			 commandMsg.setM_strUsedTotalVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strUsedTotalVolume(creditCount);
		        	     } else if (creditUnit == RatingMacro.CREDITUNIT_UPVAL){
		        			 commandMsg.setM_strUsedUpVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strUsedUpVolume(creditCount);
		        	     } else if (creditUnit == RatingMacro.CREDITUNIT_DOWNVAL){
		        			 commandMsg.setM_strUsedDownVolumeFeeLast(creditCountLast);
		        			 commandMsg.setM_strUsedDownVolume(creditCount);
			        	 } else if (creditUnit == RatingMacro.CREDITUNIT_COUNT){
			        		 commandMsg.setM_strReqTimes(creditCount);
			        	 } else {
			        	 /////
			        	 }
			        	 
			         }
		
				}
			}else {
				//////
			}
		}
        
        
        
		//commandmsg和balanceinmsg放到一起来解析 end
        
		tmpBalanceContent.setLnAmount(msgMap.containsKey(RatingMacro.PARA_B_VOUCHER_CREDIT_COUNT)?Integer.parseInt((String)msgMap.get(RatingMacro.PARA_B_VOUCHER_CREDIT_COUNT)):-1);
		tmpBalanceContent.setnUnitTypeId(msgMap.containsKey(RatingMacro.PARA_B_VOUCHER_CREDIT_UNIT)?Integer.parseInt((String)msgMap.get(RatingMacro.PARA_B_VOUCHER_CREDIT_UNIT)):-1);
		tmpBalanceContent.setLnAcctItemTypeId(msgMap.containsKey(RatingMacro.PARA_B_VOUCHER_ACCOUNT_TYPE)?Integer.parseInt((String)msgMap.get(RatingMacro.PARA_B_VOUCHER_ACCOUNT_TYPE)):-1); //充值账目类型
		ltRecharge = new ArrayList<BalanceContent>();
		ltRecharge.add(tmpBalanceContent);		
		balanceInMsg.setLtRecharge(ltRecharge);

		 ratingMsg.setM_iBalanceInMsg(balanceInMsg);
		 ratingMsg.setCommandMsg(commandMsg);
		 
		 
		 split(msgMap,ratingMsg);
		 
			printmsgMap(msgMap);
		 

		 
		 
		//解析完先获取UserInfoForMemCached
	  	   //SpyMemcachedClient smc =(SpyMemcachedClient) applicationContextHelper.getBean("SpyMemcachedClient");
	       
	  		//MemcachedClient  mc = spyMemcachedClient.getMClient();
		   // mc = spyMemcachedClient.getMClient();
	  		String phoneNumber = ratingMsg.getBaseMsg().getM_strChargedNbr();
	  		
	  		
	  	    log.debug("获得用户号码：["+phoneNumber+"]");
	  		
	  		//UserInfoForMemCached userinfoForMemcached =(UserInfoForMemCached) mc.get(phoneNumber);
	  		
	  		UserInfoForMemCached userinfoForMemcached;
			try {
				userinfoForMemcached = S.get(UserInfoForMemCached.class).get(phoneNumber);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				throw new RatingException(RatingErrorCode.ERR_MEMCACHE_READ,"读取memcached异常");
			}
	  		
	  		String timeforcheck = ratingMsg.getAllSessionStartTimes();
	  		int monthforcheck = 12 * Integer.parseInt(timeforcheck.substring(0,4)) + Integer.parseInt(timeforcheck.substring(4,6));
	  		log.debug("话单时间"+timeforcheck);
	  		//log.debug("用户创建时间"+userinfoForMemcached.getInfoUser().getCreate_date());
	  		//如果话单时间早于当前时间两个月或以上，则认为是错单
	  		String timenow = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	  		int monthnow  =  12 * Integer.parseInt(timenow.substring(0,4)) + Integer.parseInt(timenow.substring(4,6));
	  		
	  		log.debug("monthforcheck is "+monthforcheck+",and monthnow is "+monthnow);
	  		if( (monthnow - monthforcheck )>1){
	  			throw new RatingException(RatingErrorCode.ERR_TOO_OLD,"话单时间比当前时间早两个月以上，不再处理!");
	  		}
	  		
	  		//mc.shutdown();
	  		if(userinfoForMemcached == null
	  				){
	  			//没有找到用户或者话单时间早于用户创建时间，则视为无主话单
	  			throw new RatingException(RatingErrorCode.ABM_ERR_NO_USER,"没有找到用户信息");
	  		}
	  		if( userinfoForMemcached.getInfoUser().getCreate_date().after(new SimpleDateFormat("yyyyMMddHHmmss").parse(ratingMsg.getAllSessionStartTimes()))){
	  			throw new RatingException(RatingErrorCode.ABM_ERR_NO_USER,"找到用户了，但用户开户时间晚于话单时间");
	  		}
	  		
	  		ratingMsg.setUserinfoForMemcached(userinfoForMemcached);
	  		///获取UserInfoForMemCached结束
	  		
	  		msgMap.put("TELE", userinfoForMemcached.getInfoUser().getTele_type());
	  		log.debug("该用户电信类型=["+msgMap.get("TELE")+"]");
	  		
	  		//如果是模组，并且是流量话单
	  		if(dbConfigDetail.isMode(userinfoForMemcached.getInfoUser().getMain_ofr_id())
	  				&& baseMsg.getM_strMsgType().equals("80")){
	  			msgMap.put("needRating", "Y");
	  		}
	  		
			 eventTypeId = getEventTypeId(msgMap);

			extMsg.setM_nEvtTypeId(eventTypeId);
			ratingMsg.setM_iExtMsg(extMsg);

// ********************************集团信息增强 by yanhx*************************
			String callTypeId;
			int sameFlag = 0;
			
			log.debug("calling=="+baseMsg.getM_strCallingNbr()+",called=="+baseMsg.getM_strCalledNbr());

			// 语音和短信 (msgMap.get(RatingMacro.PARA_R_CALL_TYPE_02).equals("1")) PARA_R_CALL_TYPE_70
			if ( (baseMsg.getM_strMsgType().equals(RatingMacro.DOMAIN_IN_BRR) &&  msgMap.get(RatingMacro.PARA_R_CALL_TYPE_02).equals("1")) 
					||  (baseMsg.getM_strMsgType().equals(RatingMacro.DOMAIN_SMS_BRR) && msgMap.get(RatingMacro.PARA_R_CALL_TYPE_70).equals("1"))
					) {
				// 主叫号码存在时
				if (baseMsg.getM_strCallingNbr() != null && baseMsg.getM_strCallingNbr().length() > 0) {// 主叫号码存在时
					String callingNbr = baseMsg.getM_strCallingNbr();
					// 从Memchache中得到主叫号码的信息
					UserInfoForMemCached callingUserInfoForMemcached=getUserInfoForMemCached(callingNbr);
//					if (callingUserInfoForMemcached == null) {
//						// 查不到主叫号码的信息异常
//						throw new RatingException(RatingErrorCode.ABM_ERR_NO_USER, "找不到主叫号码的用户信息");
//					}

					// 当被叫号码存在时
					if (baseMsg.getM_strCalledNbr() != null && baseMsg.getM_strCalledNbr().length() > 0) {
						String calledNbr = baseMsg.getM_strCalledNbr();
						// 从Memchache中得到被叫号码的信息
						UserInfoForMemCached calledUserInfoForMemcacheded=getUserInfoForMemCached(calledNbr) ;
						
						if (calledUserInfoForMemcacheded != null) {
                            //得到主被叫有效的集团信息
							List<UserCorpInfo> callingUserCorpInfo = getEFFUserCorpInfo(callingUserInfoForMemcached);
							List<UserCorpInfo> calledUserCorpInfo = getEFFUserCorpInfo(calledUserInfoForMemcacheded);
							// 主被叫信息中同时包含集团信息时
							if(callingUserCorpInfo !=null && calledUserCorpInfo != null &&
									callingUserCorpInfo.size() > 0 && calledUserCorpInfo.size() > 0) {
								for (UserCorpInfo caling : callingUserCorpInfo) {// 遍历主叫号码集团信息
									String callingCorpId = caling.getCorp_id();// 集团ID
									for (UserCorpInfo caled : calledUserCorpInfo) {// 遍历被叫号码集团信息
										String calledCorpId = caled.getCorp_id();
										// 判断集团成员关系有效时
										if (callingCorpId.equals(calledCorpId)) {
											String callingMemType = caling.getMem_type();
											String calledMemType = caled.getMem_type();
											// 根据主被叫号码的角色进行判断
											callTypeId=getCalTypeId(callingMemType, calledMemType);
											msgMap.put("CORP", callTypeId);
											sameFlag = 1;
											break;
										}
									}
									if (sameFlag == 1) {
										break;
									}
								}
							}
						}
					}

				}
			}

			ratingMsg.setMsgMap(msgMap);

			log.debug("通过memcached获取的用户id" + userinfoForMemcached.getInfoUser().getUser_id());

		} catch (RatingException e) {
			log.error(e.getMessage());
			throw e;		
		} catch (net.rubyeye.xmemcached.exception.MemcachedException ote){
		   throw new RatingException(RatingErrorCode.ERR_MEMCACHE_TIMEOUT,"读取memcached异常");
		}catch (Exception e) {
			e.printStackTrace();	
		    log.error(e.getLocalizedMessage());
		    throw e;
		}
		return ratingMsg;
		//ratingMsg.print();
	}
	
	
	
	
	/**
	 * 通过设备号码从Memchache中获得用户信息
	 * @param callNrb
	 * @return 可能为Null
	 * @throws Exception
	 */
	private UserInfoForMemCached getUserInfoForMemCached(String callNrb) throws Exception {
		UserInfoForMemCached callUserInfoForMemCached;
		try {
			callUserInfoForMemCached = S.get(UserInfoForMemCached.class).get(callNrb);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			throw new RatingException(RatingErrorCode.ERR_MEMCACHE_READ, "读取memcached异常");
		}
		return callUserInfoForMemCached;
	}
	
	
	/**
	 * 得到集团信息中成员关系有效并且集团已经生效的集团列表
	 * @param userInfoForMemCached
	 * @return 可能为null
	 */
	private List<UserCorpInfo> getEFFUserCorpInfo(UserInfoForMemCached userInfoForMemCached) {
		
		List<UserCorpInfo> userCorpInfo= new ArrayList<UserCorpInfo>();
		if(userInfoForMemCached == null){
			return userCorpInfo;
		}
				
		userCorpInfo=	userInfoForMemCached.getUserCorpInfos();
		List<UserCorpInfo> effUserCorpInfo=new ArrayList<UserCorpInfo>();
		if (null!=userCorpInfo) {
			for(UserCorpInfo effuserCorpInfo:userCorpInfo){
				if (effuserCorpInfo.getEff_flag().equals(EFF_FLAG)&&effuserCorpInfo.getCorp_eff_flag().equals(CORP_EFF_FLAG)) {
					effUserCorpInfo.add(effuserCorpInfo);
				}
			}
		}
		return effUserCorpInfo;
	}
	
	
	
	/**
	 * 通过主被叫角色类型得到通话类型Id
	 * @param callingMemType
	 * @param calledMemType
	 * @return
	 */
	private String getCalTypeId(String callingMemType,String calledMemType) {
		String callTypeId;
			// 根据主被叫号码的角色进行判断
			if (callingMemType.equals(MEM_TYPE_COMMON) && calledMemType.equals(MEM_TYPE_COMMON)) {
				callTypeId = COMM_TO_COMM;
			} else if (callingMemType.equals(MEM_TYPE_MANAGE) && calledMemType.equals(MEM_TYPE_COMMON)) {
				callTypeId = MANAG_TO_COMM;
			} else if (callingMemType.equals(MEM_TYPE_COMMON) && calledMemType.equals(MEM_TYPE_MANAGE)) {
				callTypeId = COMM_TO_MANAG;
			} else {
				callTypeId = OTHER_TYPE;
			}
		return callTypeId;
	}

	/**
	 * 针对业务类型做一些相应参数的解析
	 * @param msgMap2
	 */
	private void split(Map<Object, Object> msgMap,RatingMsg ratingMsg) throws Exception{
		
		int msgType = Integer.parseInt((String)msgMap.get(RatingMacro.PARA_MSGTYPE));
		
		if(msgType == RatingMacro.CODE_IN_BRR){
			splitSCP(ratingMsg,msgMap); 
		}else if (msgType == RatingMacro.CODE_SMS_BRR){
			splitSMS(ratingMsg,msgMap); 
		}else if (msgType == RatingMacro.CODE_CCG_BRR){
			splitPS(ratingMsg,msgMap); 
		}else if (msgType == RatingMacro.CODE_ISMP_BRR){
			splitISMP(ratingMsg,msgMap);
		}else if (msgType == RatingMacro.CODE_IM_BRR){
			splitIM(ratingMsg);
		}else if (msgType == RatingMacro.CODE_POC_BRR){
			splitPOC(ratingMsg);
		}else if (msgType == RatingMacro.CODE_NORATING_BRR){
			splitNoRating(ratingMsg,msgMap);
		}else if (msgType == RatingMacro.CODE_DSL_BRR){
			splitDSL(ratingMsg,msgMap);
		}else if (msgType == RatingMacro.CODE_MMS_BRR){ //彩信请求
			splitMMS(ratingMsg,msgMap);
		} else {
			throw new RatingException(RatingErrorCode.ERR_SERVICE_TYPE_NOT_EXIST,msgType+"业务类型不存在");
		}
		
		splitPublicInfo();
	}

	private void splitPublicInfo() {
		// TODO Auto-generated method stub
		
	}

	////geteventid2

	private void splitDSL(RatingMsg ratingMsg,Map msgMap) throws Exception{

		setCallingAreaCodeType(ratingMsg,msgMap);
		setProvCode();
		
	}

	private void splitNoRating(RatingMsg ratingMsg,Map msgMap) throws Exception{

		setCallingAreaCodeType(ratingMsg,msgMap);
		
	}

	private void splitPOC(RatingMsg ratingMsg) {
		BaseMsg baseMsg = ratingMsg.getBaseMsg();
		baseMsg.setM_strCallingType("1");
        ratingMsg.setBaseMsg(baseMsg);
		
	}

	private void splitIM(RatingMsg ratingMsg) {
		
		BaseMsg baseMsg = ratingMsg.getBaseMsg();
		baseMsg.setM_strCallingType("1");
        ratingMsg.setBaseMsg(baseMsg);
	}

	private void splitISMP(RatingMsg ratingMsg,Map msgMap) throws Exception{

		setCallingAreaCodeType(ratingMsg,msgMap);
		setProvCode();
		setZoneCode();
	}

	private void splitPS(RatingMsg ratingMsg,Map msgMap) throws Exception {

		setCallingAreaCodeType(ratingMsg,msgMap);
		
		setProvCode();
	}

	private void splitSMS(RatingMsg ratingMsg,Map msgMap) throws Exception{

		setSpecBillingState(ratingMsg,msgMap);
		setAreaCodeType(ratingMsg,msgMap);
		setProvCode();
		setZoneCode();
		
	}

	private void setSpecBillingState(RatingMsg ratingMsg,Map msgMap) {

		//有接入号的，不判断特服号码
       if(ratingMsg.getBaseMsg().getM_strAccessNbr() != null && ratingMsg.getBaseMsg().getM_strAccessNbr().length() >0){
    	   return ;
       }
       
       // sap sdp属性
       CodeSpecialNbr callingSpec = new CodeSpecialNbr() ;
       CodeSpecialNbr calledSpec = new CodeSpecialNbr();
       
       String callingNbr = ratingMsg.getBaseMsg().getM_strCallingHomeAreaCode();
       String callingAreaCode = ratingMsg.getBaseMsg().getM_strCallingNbr();
       String calledNbr = ratingMsg.getBaseMsg().getM_strCalledHomeAreaCode();
       String calledAreaCode = ratingMsg.getBaseMsg().getM_strCalledNbr();

       callingSpec = getSpecialNbr(callingNbr,callingAreaCode);
       calledSpec = getSpecialNbr(calledNbr,calledAreaCode);
       
       if(callingSpec.getSpecial_nbr() == null || callingSpec.getSpecial_nbr().length() ==0){
    	   callingSpec = getSpecialNbr(callingNbr,"888");
       }
       if(calledSpec.getSpecial_nbr() == null || calledSpec.getSpecial_nbr().length() ==0){
    	   calledSpec = getSpecialNbr(calledNbr,"888");
       }
    
       String callingNbrSpec = callingSpec.getSpecial_nbr();
       String callingSpecialStyle = callingSpec.getSpecial_style();
       String callingBillingState = callingSpec.getBilling_state();
       
       String calledNbrSpec = calledSpec.getSpecial_nbr();
       String calledSpecialStyle = calledSpec.getSpecial_style();
       String calledBillingState = calledSpec.getBilling_state();
       
       
       //SAP:优惠主叫号码
       //SDP:优惠被叫号码?
       //根据SAP和SDP的定义,其取值与计费类型无关
       //下面二个if语句分开判断,是避免主被叫都是特服的情况

       //主叫是特服号码&&  优惠主叫或主被叫&& 免费
       //或者
       //被叫是特服号码&& 优惠主叫或主被叫&& 免费
       //SAP=1
       if( ( callingNbrSpec!=null && callingNbrSpec.length()>0  && ( callingSpecialStyle.equals("1") ||callingSpecialStyle.equals("3") ) && callingBillingState.equals("0") )
           || (calledNbrSpec!=null && calledNbrSpec.length()>0 && ( calledSpecialStyle.equals("1") ||calledSpecialStyle.equals("3") ) && calledBillingState.equals("0") ) )
       {
           msgMap.put("SAP", "1");           
       }

       //主叫是特服号码&& 优惠被叫或主被叫 && 免费
       //或者
       //被叫是特服号码&&  优惠被叫或主被叫&& 免费
       //SDP=1
       if(( callingNbrSpec!=null && callingNbrSpec.length()>0  && ( callingSpecialStyle.equals("2") ||callingSpecialStyle.equals("3") ) && callingBillingState.equals("0") )
               || (calledNbrSpec!=null && calledNbrSpec.length()>0 && ( calledSpecialStyle.equals("2") ||calledSpecialStyle.equals("3") ) && calledBillingState.equals("0") ))
       {
    	   msgMap.put("SDP", "1");  
       }
		
	}

	private CodeSpecialNbr getSpecialNbr(String callingNbr,
			String callingAreaCode) {

		CodeSpecialNbr specialnbr = new CodeSpecialNbr();
		List<CodeSpecialNbr> allCodeSpecialNbr = dbConfigDetail.getAllCodeSpecialNbr();
		for(CodeSpecialNbr tmpcs:allCodeSpecialNbr){
	    	   if(tmpcs.getArea_code().equals(callingAreaCode) && tmpcs.getSpecial_nbr().equals(callingNbr)){
	    		   specialnbr =tmpcs;
	    	   }
	    	   
	    	   if(specialnbr.getSpecial_nbr() !=null &&specialnbr.getSpecial_nbr().length()>0){
	    		   break;
	    	   }
	       }
	       
		return specialnbr;
	       
	}

	private void splitSCP(RatingMsg ratingMsg,Map msgMap) throws Exception {

		setAreaCodeType(ratingMsg,msgMap);

	    // 查找主叫和被叫的省号，被叫省号可能增强不出来
	    setProvCode();

	    setPhoneType();

	    //查找区表代码
	    setZoneCode();

	}
	
	
	private void splitMMS(RatingMsg ratingMsg,Map msgMap) throws Exception {

		setAreaCodeType(ratingMsg,msgMap);

	    // 查找主叫和被叫的省号，被叫省号可能增强不出来
	    setProvCode();

	    setPhoneType();

	    //查找区表代码
	    setZoneCode();

	}

	private void setZoneCode() {
		// TODO Auto-generated method stub
		
	}

	private void setPhoneType() {
		// TODO Auto-generated method stub
		
	}

	private void setProvCode() throws Exception {
		
		setCallingProvCode();
		setCalledProvCode();
		setVisitingProvCode();
		setVisitedProvCode();
		
	}

	private void setVisitedProvCode() {
		// TODO Auto-generated method stub
		
	}

	private void setVisitingProvCode() {
		// TODO Auto-generated method stub
		
	}

	private void setCalledProvCode() {
		// TODO Auto-generated method stub
		
	}

	private void setCallingProvCode() {
		// TODO Auto-generated method stub
		
	}

	private void setAreaCodeType(RatingMsg ratingMsg,Map msgMap) throws Exception {
		
		setCallingAreaCodeType(ratingMsg,msgMap);
		
		setCalledAreaCodeType(ratingMsg,msgMap);
	}

	private void setCalledAreaCodeType(RatingMsg ratingMsg,Map msgMap) throws Exception {

		String calledAreaCode = ratingMsg.getBaseMsg().getM_strCalledHomeAreaCode();
		
		if(calledAreaCode != null && calledAreaCode.length()>0){
			CodeCountry codeCountry = dbConfigDetail.getAllCodeCountryMap().get(calledAreaCode);
			
//			if(codeCountry == null){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NOT_CDT,"没有找到被叫区号类型");
//			}
			
			if(codeCountry != null){
				msgMap.put("CDT", codeCountry.getArea_code_type());
			}
			
		}
		
	}

	private void setCallingAreaCodeType(RatingMsg ratingMsg,Map msgMap) throws Exception {
		
		String callingAreaCode = ratingMsg.getBaseMsg().getM_strCallingHomeAreaCode();
		CodeCountry codeCountry = dbConfigDetail.getAllCodeCountryMap().get(callingAreaCode);
		
		if(callingAreaCode!= null && callingAreaCode.length()>0){
//			if(codeCountry == null ){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NOT_CAT,"没有找到主叫区号类型");
//			}
			if(codeCountry != null){
				msgMap.put("CAT", codeCountry.getArea_code_type());
			}
			
			
		}
	}


	private long getEventTypeId(Map msgMap) throws Exception {
		try {
			
			for(RuleEventTypeTree tmpevent:dbConfigDetail.getAllRuleEventTypeTree()){
				if(tmpevent.getOper_list_id() == Long.parseLong((String)msgMap.get(RatingMacro.PARA_MSGTYPE)) && tmpevent.getUp_event_type_rule_tree_id() == -1){
					
					log.debug("根树节点=====>"+tmpevent.getEvent_type_rule_tree_id());
					long eventTypeId = getRuelEventTypeTreebyTreeId(tmpevent,msgMap); //根据父节点
					if(eventTypeId <0)
						continue;
					return eventTypeId;
				}
			}

			throw new  Exception("没有找到事件码");
		} catch (Exception e) {
			log.error("获取事件码错误！！"+e.getLocalizedMessage());
			throw new RatingException(RatingErrorCode.ERR_NOT_FIND_OPER_TYPE_ITEM,"获取事件码错误");
		}
	}

	private long getRuelEventTypeTreebyTreeId(RuleEventTypeTree eventtree,Map msgMap) {
		
		log.debug("开始判断事件treeid==="+eventtree.getEvent_type_rule_tree_id()+"and eventtype=="+eventtree.getEvent_type_id());

		if(eventtree.getItem_code().equals("") || eventtree.getItem_code().length()==0 )
			return RatingErrorCode.INVALID_CONDITION_NULL;
		
		   String comType  = eventtree.getCom_type();
		   String itemCode = eventtree.getItem_code();
		   String strItemCodeA = ""; ///比较左值，前端消息传过来的value
		   String strItemCodeB = "";  ///比较右值，从数据库查到的value 也有可能是把获得itemcode作为key从消息获取对应的value
		   int comOper = eventtree.getCom_operators();//10 20 30 40 50 60 61
		   
		   strItemCodeA = getValueFromMsgMapbyKey(itemCode,msgMap);

		   if(strItemCodeA ==null ||strItemCodeA.length() ==0){//如果消息中不包含该节点
			  
			   strItemCodeA="NULL";
		   }
		   
		   if (comType.equals("10")){
			 
			   strItemCodeB = eventtree.getItem_value();
		   } else if (comType.equals("20")) { ////20 元素和元素比较,
			   
			   //strItemCodeA =(String) map.get(itemCode);
			   strItemCodeB = getValueFromMsgMapbyKey(eventtree.getItem_value(),msgMap); 
		   } 
		   
		   //对于父节点，如果满足条件则进行子节点的判断，如果不满足则返回开始下一个兄弟节点的判断
		   //对于子节点，则走后面的代码返回子节点的事件id
		   if( !Compare(strItemCodeA,comOper,strItemCodeB)){
			   
			   return RatingErrorCode.INVALID_CONDITION_NULL;
		   }else{
			   for(RuleEventTypeTree tmpevent:dbConfigDetail.getAllRuleEventTypeTree()){
					if(tmpevent.getUp_event_type_rule_tree_id() == eventtree.getEvent_type_rule_tree_id()){
						long eventTypeId = getRuelEventTypeTreebyTreeId(tmpevent,msgMap); //根据父节点
						if(eventTypeId <0)
							continue;
						return eventTypeId;
					}
				}

		   }
		
		   if(eventtree.getEvent_type_id()<0){
			   return RatingErrorCode.INVALID_CONDITION_NULL;
		   }
		
		return eventtree.getEvent_type_id();
	}

	//因为map中可能含有list对象，所以单独写一个get
	private String getValueFromMsgMapbyKey(String itemCode,Map msgMap) {
		
		String retValue="";
		
		for(Object key:msgMap.keySet()){
		  if(msgMap.get(key) instanceof String){ //如果value是string
			  if(((String)key).equals(itemCode)){
				  retValue = (String)msgMap.get(key);
				  break;
			  }
		  }else if (msgMap.get(key) instanceof List){//如果value是list
			  for(Object tmp:(List)msgMap.get(key)){
				  Map innermap = (Map) tmp;
				  for(Object tmpo:innermap.keySet()){
					  if(((String)tmpo).equals(itemCode)){
						  retValue = (String)innermap.get(tmpo);
						  break;
					  }
				  }
				  
				  if(retValue.length()>0)
					  break;
				  
			  }
			  
		  }
		}
		
		return retValue;
	}

	/*
	 * 打印map
	 */
	public void printmsgMap(Map msgMap) {
		// TODO Auto-generated method stub
		try {
			for(Object key :msgMap.keySet()){
				if(key instanceof String){
					if(msgMap.get(key) instanceof String){
						log.info("not list:"+key.toString()+"="+msgMap.get(key));
					}else {
						List<Map> grouplist = (ArrayList)msgMap.get(key);
						for(Map group :grouplist){
							for(Object subkey:group.keySet()){
								log.info("this is a list mumber:"+subkey.toString()+"="+group.get(subkey));
							}
							
						}
					}	
				}else
				{
					log.error("key不是String类型，错误！！"+key.getClass().getName());
					
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}



	/*1-秒（时长）
	2-分（金额）
	3-Byte总流量
	4-Byte上行流量
	5-Byte下行流量
	6-次数
	7-联通3G-M
	8-联通3G-T
	*/
	private boolean isMoneyBalanceType(int creditUnit) {
		if(creditUnit == 2){
			return true;
		}
		return false;
	}

	private RatingExtMsg getRatingExtMsg(Map msgMap) {
		RatingExtMsg ratingExtMsg = new RatingExtMsg();

		ratingExtMsg.setM_nExtFeeType(getFeeType(msgMap));

		return ratingExtMsg;
	}
	
	private int getFeeType(Map msgMap){
		
		String billingFlag="";
		if(msgMap.containsKey(RatingMacro.PARA_R_REQ_TYPE_02)){   //会话类型 R602取值 1:Initial 2:Update 3:Term 4:Event 5:EvtBack -1:error
			 billingFlag = (String) msgMap.get(RatingMacro.PARA_R_REQ_TYPE_02);
			 if(billingFlag.equals("1")){//I包
				 billingFlag="0";
			 }else if(billingFlag.equals("2")){ //u包
				 billingFlag="2";
			 } else if(billingFlag.equals("3") || billingFlag.equals("4")){ //T包或者事件计费
				 billingFlag="1";
			 }  else if(billingFlag.equals("5")){ //T包
				 billingFlag="3";
			 } else{
				 
			 }
				 			 
		}else{
			billingFlag="-1";
		}
		
		return Integer.parseInt(billingFlag);
	}
	
	

	private ExtMsg getExtMsg(Map msgMap) throws Exception {
		
		ExtMsg extMsg = new ExtMsg();
		if(msgMap.containsKey(RatingMacro.PARA_B_FREE)){  ////释放
			extMsg.setM_bFreeFlag(true);
		}
		
		if(msgMap.containsKey(RatingMacro.PARA_B_VOUCHER)){      //充值
			extMsg.setM_bReChargeFlag(true);
		}
		
		if( ((String)msgMap.get(RatingMacro.PARA_MSGTYPE)).equals("90")){//如果是ismp消息
			if(!msgMap.containsKey(RatingMacro.PARA_R_SERVICE_ENABLE_TTYPE)){
				throw new RatingException(RatingErrorCode.ERR_MSG_NO_SERVICE_ENABLE_TYPE,"没有R402");
			}	
			extMsg.setM_strServiceEnableType((String)msgMap.get(RatingMacro.PARA_R_SERVICE_ENABLE_TTYPE));
		}

		//poc和IM业务需要提取serverRole
		if( ((String)msgMap.get(RatingMacro.PARA_MSGTYPE)).equals("100")){//如果是poc批价请求
			if(!msgMap.containsKey(RatingMacro.PARA_R_POC_SERVER_ROLE)){
				throw new RatingException(RatingErrorCode.ERR_MSG_NO_SERVER_ROLE,"没有PoC Server Role，R701");
			}	
			extMsg.setM_nPoCIMServerRole(Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_POC_SERVER_ROLE)));
		
			if(msgMap.containsKey(RatingMacro.PARA_R_POC_SESSION_TYPE)){
				extMsg.setM_nPoCIMSessionType(Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_POC_SESSION_TYPE)));	
			}	
			if(msgMap.containsKey(RatingMacro.PARA_R_POC_PARTICI_NUM)){
				extMsg.setM_nSessionParticipateNum(Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_POC_PARTICI_NUM)));	
			}	
			if(msgMap.containsKey(RatingMacro.PARA_R_POC_FEATURE_TYPE)){
				extMsg.setM_nIMFeatureType(Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_POC_FEATURE_TYPE)));	
			}	
		
		}
		if( ((String)msgMap.get(RatingMacro.PARA_MSGTYPE)).equals("110")){//如果是IM 批价请求
			if(msgMap.containsKey(RatingMacro.PARA_R_IM_SERVER_ROLE)){
				extMsg.setM_nPoCIMServerRole(Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_IM_SERVER_ROLE)));	
			}			
			
			if(msgMap.containsKey(RatingMacro.PARA_R_IM_SESSION_TYPE)){
				extMsg.setM_nPoCIMSessionType(Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_IM_SESSION_TYPE)));	
			}	
			if(msgMap.containsKey(RatingMacro.PARA_R_IM_PARTICI_NUM)){
				extMsg.setM_nSessionParticipateNum(Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_IM_PARTICI_NUM)));	
			}	
			if(msgMap.containsKey(RatingMacro.PARA_R_IM_FEATURE_TYPE)){
				extMsg.setM_nIMFeatureType(Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_IM_FEATURE_TYPE)));	
			}	
		}

		return extMsg;
		
		
	}


	private VarMsg getVarMsg(Map msgMap) throws RatingException {

//		if(!msgMap.containsKey(RatingMacro.PARA_R_SEND_FLAG)){
//			throw new RatingException(RatingErrorCode.ERR_MSG_NO_RESEND_FLAG,"没有重发标记");
//		}
		VarMsg varMsg = new VarMsg();

		varMsg.setM_strResendFlag(msgMap.containsKey(RatingMacro.PARA_R_SEND_FLAG)?(String)msgMap.get(RatingMacro.PARA_R_SEND_FLAG):"");
		int nbillingFlag = getBillingFlag(msgMap);
		String strPayFlag =  (String)msgMap.get(RatingMacro.PARA_R_PAY_FLAG_02);
		
		if(nbillingFlag<0 && strPayFlag.equals("1") ){//预付费 并且没有R602
			throw new RatingException(RatingErrorCode.ERR_MSG_NO_BILLING_FLAG,"业务请求消息中没有计费类型");
		}
		varMsg.setM_nBillingFlag(nbillingFlag);
		
		
		if(strPayFlag.equals("1")) {//如果是预付费
			if(!msgMap.containsKey(RatingMacro.PARA_R_REL_STIME_02)){
				throw new RatingException(RatingErrorCode.ERR_MSG_NO_LASTTIME,"业务请求消息中没有会话上次扣费开始时间");
			}
			if(!msgMap.containsKey(RatingMacro.PARA_R_REQ_STIME_02)){
				throw new RatingException(RatingErrorCode.ERR_MSG_NO_BEGINTIME,"业务请求消息中没有计费开始时间");
			}
		}

//		 if(msgMap.get(RatingMacro.PARA_MSGTYPE).equals("60")  ){
//				if( !msgMap.containsKey(RatingMacro.PARA_R_RATABLE_FLAG_02)){
//					throw new RatingException(RatingErrorCode.ERR_MSG_NO_RATABLE_FLAG,"没有使用量累计标识");
//				}
//				 
//			}
		 
//		 if(msgMap.get(RatingMacro.PARA_MSGTYPE).equals("80")  ){
//				if( !msgMap.containsKey(RatingMacro.PARA_R_RATABLE_FLAG_02)){
//					throw new RatingException(RatingErrorCode.ERR_MSG_NO_RATABLE_FLAG,"没有使用量累计标识");
//				}
//				 
//			}
	
		if(strPayFlag.equals("1")) {//如果是预付费
			varMsg.setM_strLastTime(((String)msgMap.get(RatingMacro.PARA_R_REL_STIME_02)).equals("0")?(String)msgMap.get(RatingMacro.PARA_R_REL_STIME_02):(String)msgMap.get(RatingMacro.PARA_R_REQ_STIME_02));//会话上次实际扣费开始时间（计算实际扣费）
			varMsg.setM_strCurrTime((String)msgMap.get(RatingMacro.PARA_R_REQ_STIME_02));//本次计费请求开始时间
		}
		//varMsg.setM_nRatableFlag(msgMap.containsKey(RatingMacro.PARA_R_RATABLE_FLAG_02)?Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_RATABLE_FLAG_02)):0);//是否进行使用量累计标识 默认不使用

		return varMsg;
	}
	
	private int getBillingFlag(Map msgMap){
		//预付费传的是iut包标识，后付费不传
		String billingFlag = msgMap.containsKey(RatingMacro.PARA_R_REQ_TYPE_02)?(String)msgMap.get(RatingMacro.PARA_R_REQ_TYPE_02):"-1";
		if((billingFlag.equals("2")||billingFlag.equals("1")) && msgMap.containsKey(RatingMacro.PARA_B_DEDUCT) ){ //如果是I包或者U包且包含B03组，则认为是T包
			billingFlag ="3";
		}		
		
		return Integer.parseInt(billingFlag);
	}

	private BaseMsg getBaseMsg(Map msgMap) throws Exception {

		if(!msgMap.containsKey(RatingMacro.PARA_SESSION_ID)){
			throw new RatingException(RatingErrorCode.ERR_MSG_NO_SESSIONID,"业务请求消息中没有会话ID");
		}
		if(!msgMap.containsKey(RatingMacro.PARA_REQ_ID)){
			throw new RatingException(RatingErrorCode.ERR_MSG_NO_REQ_ID,"业务请求消息中没有请求ID");
		}
	 
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setM_strMsgType((String)msgMap.get(RatingMacro.PARA_MSGTYPE));    //消息类型
		baseMsg.setM_strEvtTimeStamp((String)msgMap.get(RatingMacro.PARA_EVENTTIMESTAMP));//时间戳
		String startTime="";
		if(msgMap.containsKey(RatingMacro.PARA_R_START_TIME)){ //摘机时间
			startTime=(String)msgMap.get(RatingMacro.PARA_R_START_TIME);
		} else{
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
			startTime = df.format(new Date());
 		}
		baseMsg.setM_strStartTime(startTime);///会话开始时间，对于事件型计费，则取系统时间
		
		
		
		baseMsg.setM_strSessionId((String)msgMap.get(RatingMacro.PARA_SESSION_ID));//sessionid
		baseMsg.setM_strRequstId((String)msgMap.get(RatingMacro.PARA_REQ_ID));   //requestid
		
		baseMsg.setM_strAccessNbr(msgMap.containsKey(RatingMacro.PARA_R_CALLED_ACCESSNBR)?(String)msgMap.get(RatingMacro.PARA_R_CALLED_ACCESSNBR):"");//被叫接入码
		String thirdNbr = msgMap.containsKey(RatingMacro.PARA_R_CONNECT_NBR)?(String)msgMap.get(RatingMacro.PARA_R_CONNECT_NBR):""; //连接号码 用于呼转业务
		if(thirdNbr.length() == 0 || thirdNbr.equals("")){
			 thirdNbr = msgMap.containsKey(RatingMacro.PARA_R_CONNECT_NBR_02)?(String)msgMap.get(RatingMacro.PARA_R_CONNECT_NBR_02):"";//连接号码
		}

		if (!msgMap.containsKey(RatingMacro.PARA_R_CHARGED_NBR)) {
			if (msgMap.containsKey(RatingMacro.PARA_R_IMSI)) {// 没有计费号码，有imsi也行啊，

				// 保留原有的数据库查找（默认查找）加开关，在加载数据的时候就读取
				RuleSystemSwitch rule=dbConfigDetail.getBySwitchCode("IMSI_READMEMCACHE");
				//log.debug("查询的开关信息是=====》"+rule.toString());
				
				if(rule ==null){
					List<InfoUser> infoUserList = dbUtil
							.getInfoUserbyImsi((String) msgMap
									.get(RatingMacro.PARA_R_IMSI));
					if (infoUserList == null || infoUserList.size() != 1) {
						throw new RatingException(
								RatingErrorCode.ERR_NOUSER_BYIMSI,
								"根据Imsi查找用户异常");
					}
					msgMap.put(RatingMacro.PARA_R_CHARGED_NBR,
							(String) infoUserList.get(0).getDevice_number());
					
				}else{
					if(rule.getSwitch_value().equals("1")){
						UserInfoForMemCached userinfoForMemcached;
						try {
							userinfoForMemcached = S
									.get(UserInfoForMemCached.class)
									.get("IMSI"
											+ msgMap.get(RatingMacro.PARA_R_IMSI));
						} catch (Exception e) {
							e.printStackTrace();
							log.error(e.getLocalizedMessage());
							throw new RatingException(
									RatingErrorCode.ERR_MEMCACHE_READ,
									"读取memcached异常");
						}
						if (userinfoForMemcached == null) {
							
							//如果imsi从memcached没有找到，再到oracle查一次
							List<InfoUser> infoUserList = dbUtil
									.getInfoUserbyImsi((String) msgMap
											.get(RatingMacro.PARA_R_IMSI));
							if (infoUserList == null || infoUserList.size() != 1) {
								throw new RatingException(
										RatingErrorCode.ERR_NOUSER_BYIMSI,
										"根据Imsi查找用户异常");
							}
							msgMap.put(RatingMacro.PARA_R_CHARGED_NBR,
									(String) infoUserList.get(0).getDevice_number());
							
						}else{
							msgMap.put(RatingMacro.PARA_R_CHARGED_NBR,
									userinfoForMemcached.getDevice_number());
						}
						
						
					}else if (rule.getSwitch_value().equals("0")){
						List<InfoUser> infoUserList = dbUtil
								.getInfoUserbyImsi((String) msgMap
										.get(RatingMacro.PARA_R_IMSI));
						if (infoUserList == null || infoUserList.size() != 1) {
							throw new RatingException(
									RatingErrorCode.ERR_NOUSER_BYIMSI,
									"根据Imsi查找用户异常");
						}
						msgMap.put(RatingMacro.PARA_R_CHARGED_NBR,
								(String) infoUserList.get(0).getDevice_number());
						
					}else{
						//异常
						throw new RatingException(
								RatingErrorCode.ERR_RULE_SYSTEM_SWITCH,
								"读取IMSI_READMEMCAHE异常");
					}
					
				}
				
			} else {
				throw new RatingException(RatingErrorCode.ERR_NO_CHARGEDNBR,
						"没有计费号码同时也没有imsi");
			}
		}
		
		log.debug("从msgmap获取到了计费号码--->"+(String)msgMap.get(RatingMacro.PARA_R_CHARGED_NBR));
		
		baseMsg.setM_strChargedNbr((String)msgMap.get(RatingMacro.PARA_R_CHARGED_NBR));
		
		baseMsg.setM_strThirdNbr(thirdNbr);
		baseMsg.setM_strShortNbr(msgMap.containsKey(RatingMacro.PARA_R_CALLED_SHORTNBR)?(String)msgMap.get(RatingMacro.PARA_R_CALLED_SHORTNBR):"");//短号码
		baseMsg.setM_strCallingNbr(msgMap.containsKey(RatingMacro.PARA_R_CALLING_NBR)?(String)msgMap.get(RatingMacro.PARA_R_CALLING_NBR):"");//主叫号码
	
	    
		if(!msgMap.containsKey(RatingMacro.PARA_R_CLG_HOM_AC_02)){
			if(baseMsg.getM_strMsgType().equals("60") && 
					(msgMap.get(RatingMacro.PARA_R_CALL_TYPE_02).equals("4") || msgMap.get(RatingMacro.PARA_R_CALL_TYPE_02).equals("3"))){//目前只有呼转业务用到了主被叫归属区号
				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CLG_HM_AC,"业务请求消息中没有主叫归属区号");
			}			
		}
		if(!msgMap.containsKey(RatingMacro.PARA_R_PAY_FLAG_02)){
			throw new RatingException(RatingErrorCode.ERR_MSG_NO_PLAYFLAG,"没有用户属性标识");
		}		
		baseMsg.setM_strCallingHomeAreaCode((String)msgMap.get(RatingMacro.PARA_R_CLG_HOM_AC_02));//主叫号码归属区号
		baseMsg.setM_strPayFlag((String)msgMap.get(RatingMacro.PARA_R_PAY_FLAG_02)); //用户付费 属性标识
		
		//RG Rating-Group
        baseMsg.setM_strRatingGroup(getRatingGroupFromMsgMap(msgMap));
		
		if(baseMsg.getM_strMsgType().equals("60")  ){
			
//			if( !msgMap.containsKey(RatingMacro.PARA_R_CLG_VST_AC_02)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CLG_VST_AC,"业务请求消息中没有主叫拜访区号");
//			}
//			if( !msgMap.containsKey(RatingMacro.PARA_R_CLG_PARTNER_02)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CLG_PARTNER,"没有主叫运营商");
//			}
//			if( !msgMap.containsKey(RatingMacro.PARA_R_CALLED_NBR)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CALLED_NBR,"业务请求消息中没有被叫号码");
//			}
//			if( !msgMap.containsKey(RatingMacro.PARA_R_CLD_HOM_AC_02)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CLD_HM_AC,"没有被叫归属区号");
//			}
//			if( !msgMap.containsKey(RatingMacro.PARA_R_CLD_VST_AC_02)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CLD_VST_AC,"业务请求消息中没有被叫拜访区号");
//			}
//			if( !msgMap.containsKey(RatingMacro.PARA_R_CLD_PARTNER_02)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CLD_PARTNER,"没有被叫运营商");
//			}
			if( !msgMap.containsKey(RatingMacro.PARA_R_CALL_TYPE_02)){
				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CALLING_TYPE,"业务请求消息中没有话单类型");
			}
			
			baseMsg.setM_strCallingCell(msgMap.containsKey(RatingMacro.PARA_R_CALLING_CELL_02)?(String)msgMap.get(RatingMacro.PARA_R_CALLING_CELL_02):"");
			baseMsg.setM_strCallingMscId(msgMap.containsKey(RatingMacro.PARA_R_CALLING_MSC_02)?(String)msgMap.get(RatingMacro.PARA_R_CALLING_MSC_02):"");
            baseMsg.setM_strCalledCell(msgMap.containsKey(RatingMacro.PARA_R_CALLED_CELL_02)?(String)msgMap.get(RatingMacro.PARA_R_CALLED_CELL_02):"");
		    baseMsg.setM_nReqAttrCellFlag(msgMap.containsKey(RatingMacro.PARA_R_ATTR_CELL)?Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_ATTR_CELL)):0);
            
			
		}
	   if(baseMsg.getM_strMsgType().equals("70")  ){
//			if( !msgMap.containsKey(RatingMacro.PARA_R_CALLED_NBR)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CALLED_NBR,"业务请求消息中没有被叫号码");
//			}
//			if( !msgMap.containsKey(RatingMacro.PARA_R_CLD_PARTNER_02)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_CLD_PARTNER,"没有被叫运营商");
//			}
//			if( !msgMap.containsKey(RatingMacro.PARA_R_SM_ID)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_SM_ID,"没有SM_ID");
//			}
			
		}
	   
//	   if(baseMsg.getM_strMsgType().equals("80")  ){
//			if( !msgMap.containsKey(RatingMacro.PARA_R_VALIDITY_TIME_02)){
//				throw new RatingException(RatingErrorCode.ERR_MSG_NO_VALIDITY_TIME,"没有授权有效时间");
//			}
//			baseMsg.setM_nReqAttrCellFlag(msgMap.containsKey(RatingMacro.PARA_R_ATTR_CELL)?Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_ATTR_CELL)):0);
//            
//		}
	
	   
		baseMsg.setM_strCallingVisitAreaCode(msgMap.containsKey(RatingMacro.PARA_R_CLG_VST_AC_02)?(String)msgMap.get(RatingMacro.PARA_R_CLG_VST_AC_02):"");//主叫拜访区号
		baseMsg.setM_strCallingPartner(msgMap.containsKey(RatingMacro.PARA_R_CLG_PARTNER_02)?(String)msgMap.get(RatingMacro.PARA_R_CLG_PARTNER_02):"");//主叫运营商   默认应该是哪个运营商？？？
		baseMsg.setM_strCalledNbr(msgMap.containsKey(RatingMacro.PARA_R_CALLED_NBR)?(String)msgMap.get(RatingMacro.PARA_R_CALLED_NBR):"");//被叫号码
		baseMsg.setM_strCalledHomeAreaCode(msgMap.containsKey(RatingMacro.PARA_R_CLD_HOM_AC_02)?(String)msgMap.get(RatingMacro.PARA_R_CLD_HOM_AC_02):""); //被叫归属区号
		baseMsg.setM_strCalledVisitAreaCode(msgMap.containsKey(RatingMacro.PARA_R_CLD_VST_AC_02)?(String)msgMap.get(RatingMacro.PARA_R_CLD_VST_AC_02):"");//被叫拜访区号
		baseMsg.setM_strCalledPartner(msgMap.containsKey(RatingMacro.PARA_R_CLD_PARTNER_02)?(String)msgMap.get(RatingMacro.PARA_R_CLD_PARTNER_02):""); //被叫运营商
		baseMsg.setM_strCallingType(msgMap.containsKey(RatingMacro.PARA_R_CALL_TYPE_02)?(String)msgMap.get(RatingMacro.PARA_R_CALL_TYPE_02):"");  //话单类型 1-主叫 		2-被叫 		3-无条件呼转 		4-有条件呼转
    
		baseMsg.setM_strValidityTime(msgMap.containsKey(RatingMacro.PARA_R_VALIDITY_TIME_02)?(String)msgMap.get(RatingMacro.PARA_R_VALIDITY_TIME_02):"");//授权有效时间
		baseMsg.setM_bRatingQueryFlag(msgMap.containsKey(RatingMacro.PARA_B_RATE_QUERY)?true:false);
		baseMsg.setM_strSmId(msgMap.containsKey(RatingMacro.PARA_R_SM_ID)?(String)msgMap.get(RatingMacro.PARA_R_SM_ID):"");//短信消息ID
		baseMsg.setM_strISMPId(msgMap.containsKey(RatingMacro.PARA_R_ISMP_ID)?(String)msgMap.get(RatingMacro.PARA_R_ISMP_ID):""); //ismp短信ID
		
		if(baseMsg.getM_strMsgType().equals("90")){//ismp
			
			if( !msgMap.containsKey(RatingMacro.PARA_R_PRODUCT_ID_02)){
				throw new RatingException(RatingErrorCode.ERR_MSG_NO_ISMP_ID,"没有ISMP_id");
			}
			
			baseMsg.setM_strProductId(msgMap.containsKey(RatingMacro.PARA_R_PRODUCT_ID_02)?(String)msgMap.get(RatingMacro.PARA_R_PRODUCT_ID_02):""); //ismp产品
			baseMsg.setM_strSpcPrdId(msgMap.containsKey(RatingMacro.PARA_R_RATING_UPLINK)?(String)msgMap.get(RatingMacro.PARA_R_RATING_UPLINK):"");
			
		} else if (baseMsg.getM_strMsgType().equals("60")) { //语音
			if (msgMap.get(RatingMacro.PARA_R_CALL_TYPE_02).equals("1")){
				//主叫
				msgMap.put( RatingMacro.PARA_R_CALLING_NBR, (String) msgMap.get(RatingMacro.PARA_R_CHARGED_NBR)) ;
				baseMsg.setM_strCallingNbr((String) msgMap.get(RatingMacro.PARA_R_CHARGED_NBR));
			} else if (msgMap.get(RatingMacro.PARA_R_CALL_TYPE_02).equals("2")){
				//被叫
				msgMap.put( RatingMacro.PARA_R_CALLED_NBR, (String) msgMap.get(RatingMacro.PARA_R_CHARGED_NBR)) ;
				
			}
		}else if (baseMsg.getM_strMsgType().equals("70") || baseMsg.getM_strMsgType().equals("180")) { //短信 或彩信
			if (msgMap.get(RatingMacro.PARA_R_CALL_TYPE_70).equals("1")){
				//主叫
				msgMap.put( RatingMacro.PARA_R_CALLING_NBR, (String) msgMap.get(RatingMacro.PARA_R_CHARGED_NBR)) ;
				baseMsg.setM_strCallingNbr((String) msgMap.get(RatingMacro.PARA_R_CHARGED_NBR));
			} else if (msgMap.get(RatingMacro.PARA_R_CALL_TYPE_70).equals("2")){
				//被叫
				msgMap.put( RatingMacro.PARA_R_CALLED_NBR, (String) msgMap.get(RatingMacro.PARA_R_CHARGED_NBR)) ;
			}
		}

		   // 计费类型
		String payFlag = (String) msgMap.get(RatingMacro.PARA_R_PAY_FLAG_02);
		
		if(payFlag.equals("1")){//预付费
			int nBillingFlag = getBillingFlag(msgMap);
		    if( nBillingFlag == 5 || nBillingFlag == 4 ){ ///5返款  4事件型计费
		        // 事件计费信息按实扣传过来
		    	baseMsg.setM_nFeeType(( nBillingFlag == 5 )?RatingMacro.BACK_FEE_CMD:RatingMacro.REAL_FEE_CMD);	        
		    } else if( nBillingFlag == 1  ){ ///1  I包    2 U包
		        // 预占
		    	baseMsg.setM_nFeeType(RatingMacro.REQ_FEE_CMD); ////预占   zhanghb  U包应该也包含实扣的呀？	        
		    } else if ( nBillingFlag == 2){
		    	baseMsg.setM_nFeeType(RatingMacro.REAL_FEE_CMD | RatingMacro.REQ_FEE_CMD);
		    } else if( nBillingFlag == 3 ){ //3 T包
		        // 实扣
		    	baseMsg.setM_nFeeType(RatingMacro.REAL_FEE_CMD);
		    } else {
		      throw new RatingException(RatingErrorCode.ERR_SERTYPE_NOT_MATCH_SESSIONTYPE,"业务类型和会话类型不匹配");
		    }
		}else if (payFlag.equals("2")){//后付费
			baseMsg.setM_nFeeType(RatingMacro.POST_PAY_CMD); //zhanghb add
		}
	    


		baseMsg.setM_nActiveFlag(msgMap.containsKey(RatingMacro.PARA_R_ACTIVE_USER)?Integer.parseInt((String)msgMap.get(RatingMacro.PARA_R_ACTIVE_USER)):0);
	
		
	    //国际类话单通常没有号码，通过imsi获取号码后补充到主叫或被叫号码
		//TODO 
		
		
		return baseMsg;
	}

	private String getRatingGroupFromMsgMap(Map msgMap) {
	
		String ratingGroup ="";
		if(msgMap.containsKey(RatingMacro.PARA_B_RESERVE)){ ////B01
			if(msgMap.get(RatingMacro.PARA_B_RESERVE) instanceof String){
				ratingGroup = msgMap.containsKey(RatingMacro.PARA_B_RESERVE_RATING_GROUP)?(String)msgMap.get(RatingMacro.PARA_B_RESERVE_RATING_GROUP):"";
			}else{
				List<Map> groupList = (ArrayList)msgMap.get(RatingMacro.PARA_B_RESERVE);
				for(Map groupMap :groupList){
					ratingGroup = groupMap.containsKey(RatingMacro.PARA_B_RESERVE_RATING_GROUP)?(String)groupMap.get(RatingMacro.PARA_B_RESERVE_RATING_GROUP):"";
					if(ratingGroup.length()>0)
						return ratingGroup;
				}
			}			
		}else if (msgMap.containsKey(RatingMacro.PARA_B_FREE)){
			if(msgMap.get(RatingMacro.PARA_B_FREE) instanceof String){
				ratingGroup = msgMap.containsKey(RatingMacro.PARA_B_FREE_RATING_GROUP)?(String)msgMap.get(RatingMacro.PARA_B_FREE_RATING_GROUP):"";
			}else{
				List<Map> groupList = (ArrayList)msgMap.get(RatingMacro.PARA_B_FREE);
				for(Map groupMap :groupList){
					ratingGroup = groupMap.containsKey(RatingMacro.PARA_B_FREE_RATING_GROUP)?(String)groupMap.get(RatingMacro.PARA_B_FREE_RATING_GROUP):"";
					if(ratingGroup.length()>0)
						return ratingGroup;
				}
			}
			
		}else if (msgMap.containsKey(RatingMacro.PARA_B_DEDUCT)){
			if(msgMap.get(RatingMacro.PARA_B_DEDUCT) instanceof String){
				ratingGroup = msgMap.containsKey(RatingMacro.PARA_B_REAL_RATING_GROUP)?(String)msgMap.get(RatingMacro.PARA_B_REAL_RATING_GROUP):"";
			}else{
				List<Map> groupList = (ArrayList)msgMap.get(RatingMacro.PARA_B_DEDUCT);
				for(Map groupMap :groupList){
					ratingGroup = groupMap.containsKey(RatingMacro.PARA_B_REAL_RATING_GROUP)?(String)groupMap.get(RatingMacro.PARA_B_REAL_RATING_GROUP):"";
					if(ratingGroup.length()>0)
						return ratingGroup;
				}
			}
		}else if (msgMap.containsKey(RatingMacro.PARA_B_BACK)){
			if(msgMap.get(RatingMacro.PARA_B_BACK) instanceof String){
				ratingGroup = msgMap.containsKey(RatingMacro.PARA_B_BACK_RATING_GROUP)?(String)msgMap.get(RatingMacro.PARA_B_BACK_RATING_GROUP):"";
			}else{
				List<Map> groupList = (ArrayList)msgMap.get(RatingMacro.PARA_B_BACK);
				for(Map groupMap :groupList){
					ratingGroup = groupMap.containsKey(RatingMacro.PARA_B_BACK_RATING_GROUP)?(String)groupMap.get(RatingMacro.PARA_B_BACK_RATING_GROUP):"";
					if(ratingGroup.length()>0)
						return ratingGroup;
				}
			}
		}else if (msgMap.containsKey(RatingMacro.PARA_B_USED)){
			if(msgMap.get(RatingMacro.PARA_B_USED) instanceof String){
				ratingGroup = msgMap.containsKey(RatingMacro.PARA_B_USED_RATING_GROUP)?(String)msgMap.get(RatingMacro.PARA_B_USED_RATING_GROUP):"";
			}else{
				List<Map> groupList = (ArrayList)msgMap.get(RatingMacro.PARA_B_USED);
				for(Map groupMap :groupList){
					ratingGroup = groupMap.containsKey(RatingMacro.PARA_B_USED_RATING_GROUP)?(String)groupMap.get(RatingMacro.PARA_B_USED_RATING_GROUP):"";
					if(ratingGroup.length()>0)
						return ratingGroup;
				}
			}
		}else if (msgMap.containsKey(RatingMacro.PARA_B_VOUCHER)){
			if(msgMap.get(RatingMacro.PARA_B_VOUCHER) instanceof String){
				ratingGroup = msgMap.containsKey(RatingMacro.PARA_B_VOUCHER_RATING_GROUP)?(String)msgMap.get(RatingMacro.PARA_B_VOUCHER_RATING_GROUP):"";
			}else{
				List<Map> groupList = (ArrayList)msgMap.get(RatingMacro.PARA_B_VOUCHER);
				for(Map groupMap :groupList){
					ratingGroup = groupMap.containsKey(RatingMacro.PARA_B_VOUCHER_RATING_GROUP)?(String)groupMap.get(RatingMacro.PARA_B_VOUCHER_RATING_GROUP):"";
					if(ratingGroup.length()>0)
						return ratingGroup;
				}
			}
		} else if (msgMap.containsKey(RatingMacro.PARA_B_ATH_RATING)){
			if(msgMap.get(RatingMacro.PARA_B_ATH_RATING) instanceof String){
				ratingGroup = msgMap.containsKey(RatingMacro.PARA_B_ATH_RATING_GROUP)?(String)msgMap.get(RatingMacro.PARA_B_ATH_RATING_GROUP):"";
			}else{
				List<Map> groupList = (ArrayList)msgMap.get(RatingMacro.PARA_B_ATH_RATING);
				for(Map groupMap :groupList){
					ratingGroup = groupMap.containsKey(RatingMacro.PARA_B_ATH_RATING_GROUP)?(String)groupMap.get(RatingMacro.PARA_B_ATH_RATING_GROUP):"";
					if(ratingGroup.length()>0)
						return ratingGroup;
				}
			}
			
		}else {
			//
		}
		
		return ratingGroup;
	}




	private Map generateMapFromMsg(String strRequestMsg) throws RatingException {
	
		   Map<String,Object>  msgMap = new HashMap<String,Object>();
		  
		   List<Map> groupLists = new ArrayList<Map>();
			
		   if(!strRequestMsg.substring(0,1).equals("[") || !strRequestMsg.substring(strRequestMsg.length()-1).equals("]") ){
			   throw new RatingException(RatingErrorCode.ERR_MSG_FORMAT,"消息格式错误");
		   }
		
		   String[] params = strRequestMsg.substring(1,strRequestMsg.length()-1).split("]\\[");		
		   int containsMoneyParam =0;//如果一个组里同时包含B036=2和B037的value>0,则判断为该消息包含了批价结果，后续就不需要批价
		   
		   for(int i = 0; i<params.length; i++){
			   
			   groupLists.clear();
			  // log.info(params[i]);
			 
			   /*if(params[i].contains(";") && !params[i].subSequence(0,3).equals("B30") && !params[i].subSequence(0,3).equals("000")
					 &&  !params[i].subSequence(0,3).equals(RatingMacro.PARA_B_DEDUCT) && !params[i].subSequence(0,3).equals("B01") && !params[i].subSequence(0,3).equals(RatingMacro.PARA_B_FREE)
					   ){
				   return null;
			   } */
			   
			   //if(params[i].substring(0,3).equals("B30") || params[i].substring(0,3).equals("B01") || params[i].substring(0,3).equals(RatingMacro.PARA_B_FREE) || params[i].substring(0,3).equals(RatingMacro.PARA_B_DEDUCT)){
			   if(params[i].indexOf(";")>0 && !params[i].substring(0,3).equals("000")){ //除了sessionid，其他包含;节点的需要按组再次解析	   
				   String[] groups =params[i].split(";");
				   
				   
				   for(String group:groups){
					   Map<String, String> keyValue = new HashMap<String, String>();
					   containsMoneyParam =0;
					   
					   String[] kvs =group.split(",");
					   for(String kv:kvs){						 
						   String[] tmpkv = kv.split("=");		
						   if(tmpkv[1].length()>0 && !tmpkv[1].equals("")){//对于value非空的才需要
							   keyValue.put(tmpkv[0], tmpkv[1]);	
						   }   
					   
						   if((tmpkv[0].equals("B036")&& tmpkv[1].equals("2") ) 
								   || (tmpkv[0].equals("B037")&& !tmpkv[1].equals("") )){
							   containsMoneyParam++;
						   }
					   }
					   
					   groupLists.add( keyValue);
					   
					   if(containsMoneyParam ==2){
						   msgMap.put("needRating", "N");
					   }
					   
				   }
				   
				   msgMap.put(params[i].substring(0,params[i].indexOf("=")), groupLists);
			   } else {
				   
				   String[] kvs =params[i].split(",");
				    containsMoneyParam =0;//如果一个组里同时包含B036=2和B037的value>0,则判断为该消息包含了批价结果，后续就不需要批价
				   for(String kv:kvs){
					   
					   String[] tmpkv = kv.split("=");	
					   if(tmpkv.length ==2){
						   if(tmpkv[1].length()>0 &&  !tmpkv[1].equals("")){//对于value非空的才需要
							   msgMap.put(tmpkv[0], tmpkv[1]);  
						   }
						   
						   if((tmpkv[0].equals("B036")&& tmpkv[1].equals("2") ) 
								   || (tmpkv[0].equals("B037")&& !tmpkv[1].equals("") )){
							   containsMoneyParam++;
						   }
						   
						  
					   }
					  
					  
				   }
				   
				   if(containsMoneyParam ==2){
					   msgMap.put("needRating", "N");
				   }
				   
				 
			   }
 
		   }
		   
		  // log.info("msgMap.size==="+msgMap.size());
		   
		   
		
		return msgMap;
	}

	

	/**
	 * 比较左右值
	 * @param strItemCodeA
	 * @param comOperator 取值范围 10(=),20(!=),30(like) ,31(not like),40(>),41(>=),50(<),60(包含)
	 * @param strItemCodeB
	 * @return
	 */
	public boolean Compare(String strItemCodeA,int comOperator ,String strItemCodeB){
		
		String[] itemCodeBList ;
		//int i =0;
		
		if(comOperator == 10) // =
		{
			if( strItemCodeA.equals(strItemCodeB))
				   return true;
		} else if (comOperator ==20){//!=
			if( !strItemCodeA.equals(strItemCodeB))
				   return true;
		} else if (comOperator  ==30 ){// like 
			if( strItemCodeA.substring(0, strItemCodeB.length()).equals(strItemCodeB))
				   return true;
		} else if (comOperator ==31) {//not like
			if(! strItemCodeA.substring(0, strItemCodeB.length()).equals(strItemCodeB))
				   return true;
		} else if (comOperator == 40){// >
			
			if(strItemCodeA.compareTo(strItemCodeB) >0)//Long.parseLong(strItemCodeA) > Long.parseLong(strItemCodeB)
			return true;
		} else if (comOperator == 41){// >=
			
			if(strItemCodeA.compareTo(strItemCodeB) >=0)
			return true;
		} else if (comOperator == 50){// <
			
			if(strItemCodeA.compareTo(strItemCodeB) <0)
			return true;
		} else if (comOperator == 51){// <=
			
			if(strItemCodeA.compareTo(strItemCodeB) <=0)
			return true;
		} else if (comOperator ==60){//包含
			
			 itemCodeBList = strItemCodeB.split(",");

			   for(String codeb:itemCodeBList){
				   if( strItemCodeA.equals(codeb))
					   return true;
			   }
		} else if (comOperator ==61){///不包含
			itemCodeBList = strItemCodeB.split(",");
 
			   for(String codeb:itemCodeBList){
				   if( strItemCodeA.equals(codeb))
					 return false;
			   }
			   
			  return true;
			   
		} else if (comOperator ==62) {//包含相似
			itemCodeBList = strItemCodeB.split(",");

			   for(String codeb:itemCodeBList){
				   if(strItemCodeA.substring(0, Math.min(codeb.length(), strItemCodeA.length())).equals(codeb))
					   return true;
			   }
			   return false;
		} else if (comOperator ==63){//不包含相似
			itemCodeBList = strItemCodeB.split(",");

			   for(String codeb:itemCodeBList){
				   if(strItemCodeA.substring(0, Math.min(codeb.length(), strItemCodeA.length())).equals(codeb))
					  return false;
			   }
			   
			   return true;
			   
		} else {
			//如果有后续其他比较类型，再增加
		}

		return false;
	}

	/**
	 * 返回错误信息  根据错误代码和ratingmsg
	 */
@Override
	public  String createErrorOutMsg(int errorCode, String errormsg,  String strRequestMsg) {

	   StringBuffer errorOutMsg = new StringBuffer();
	  // SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMddHHmmss" );
	   String msgType = getValueFromRequestMsg("100",strRequestMsg);
	   
	   errorOutMsg.append("["+RatingMacro.PARA_MSGTYPE +"="+getRespMsgType(Integer.parseInt(msgType))+"]");
	   errorOutMsg.append("["+RatingMacro.PARA_EVENTTIMESTAMP+"="+getValueFromRequestMsg("101",strRequestMsg)+"]");
	   errorOutMsg.append("["+RatingMacro.PARA_SESSION_ID+"="+getValueFromRequestMsg("000",strRequestMsg)+"]");
	   errorOutMsg.append("["+RatingMacro.PARA_REQ_ID+"="+getValueFromRequestMsg("001",strRequestMsg)+"]");
	   errorOutMsg.append("["+RatingMacro.PARA_ERROR_CODE+"="+errorCode+"]");
	  // errorOutMsg.append("[vs="+getValue("[vs=",strRequestMsg)+"]");
	   
	   log.debug("批价失败返回消息===>"+errorOutMsg.toString());
	  
		return errorOutMsg.toString();
	}


private String getValue(String param,RatingMsg ratingMsg){
	String paramValue="";
	String strRequestMsg = ratingMsg.getStrRequestMsg();
	
	if(ratingMsg.getMsgMap().containsKey(param)){
		//如果map里有就直接返回
	    return (String) ratingMsg.getMsgMap().get(param);
	}else{
		//如果map里没有就到收到的统一接入消息去找
		param ="["+param+"=";
		if(strRequestMsg.indexOf(param) >=0){
			paramValue = strRequestMsg.substring(strRequestMsg.indexOf(param)+param.length(), strRequestMsg.indexOf("]", strRequestMsg.indexOf(param)+param.length()));
		}
	}

	return paramValue;
}


private String getValueFromRequestMsg(String param,String strRequestMsg){
	
	String paramvalue ="";
	
	param ="["+param+"=";
	if(strRequestMsg.indexOf(param) >=0){
		paramvalue = strRequestMsg.substring(strRequestMsg.indexOf(param)+param.length(), strRequestMsg.indexOf("]", strRequestMsg.indexOf(param)+param.length()));
	}
	
	return paramvalue;
}



/**
 * 获取返回消息类型
 * @param strRequestMsg
 * @return
 */
    private String getRespMsgType(int m_nMsgType) {

    	 int nRespMsgType =RatingMacro.CODE_KAA;

    	    if( m_nMsgType ==RatingMacro.CODE_IN_BRR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_IN_BRA;
    	    }
    	    else if( m_nMsgType ==RatingMacro.CODE_CCG_BRR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_CCG_BRA;
    	    }
    	    else if( m_nMsgType ==RatingMacro.CODE_ISMP_BRR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_ISMP_BRA;
    	    }
    	    else if( m_nMsgType ==RatingMacro.CODE_SMS_BRR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_SMS_BRA;
    	    }
    	    else if( m_nMsgType ==RatingMacro.CODE_NORATING_BRR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_NORATING_BRA;
    	    }
    	    else if( m_nMsgType ==RatingMacro.CODE_DSL_BRR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_DSL_BRA;
    	    }
    	    else if( m_nMsgType ==RatingMacro.CODE_BBR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_BBA;
    	    }
    	    else if( m_nMsgType ==RatingMacro.CODE_POC_BRR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_POC_BRA;
    	    }
    	    else if( m_nMsgType ==RatingMacro.CODE_IM_BRR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_IM_BRA;
    	    }
    	    else if( m_nMsgType ==RatingMacro.CODE_MMS_BRR )
    	    {
    	        nRespMsgType =RatingMacro.CODE_MMS_BRA;
    	    }
    	    else
    	    {
    	        nRespMsgType =RatingMacro.CODE_KAA;//异常情况，回keepalive应答消息(不会发生)
    	    }

    	
		return ""+nRespMsgType;
	}





/**
 * 拼装消息返回统一接入
 */
	@Override
	public String createMergeOutMsg(RatingMsg ratingMsg,RatingData ratingData) throws Exception {
		
		StringBuffer mergeOutMsg =new StringBuffer();
		int nMsgType = ratingMsg.getM_nMsgType();
		int nBillingFlag = ratingMsg.getM_nBillingFlag();
		
		   mergeOutMsg.append("["+RatingMacro.PARA_MSGTYPE +"="+getRespMsgType(nMsgType)+"]");
		   mergeOutMsg.append("["+RatingMacro.PARA_EVENTTIMESTAMP+"="+getValue("101",ratingMsg)+"]");
		   mergeOutMsg.append("["+RatingMacro.PARA_SESSION_ID+"="+getValue("000",ratingMsg)+"]");
		   mergeOutMsg.append("["+RatingMacro.PARA_REQ_ID+"="+getValue("001",ratingMsg)+"]");
		   mergeOutMsg.append("[003="+0+"]");
		   
		   String userId = ratingMsg.getUserinfoForMemcached().getInfoUser().getUser_id();
		   String chargedNumber = ratingMsg.getBaseMsg().getM_strChargedNbr();
		   
		   mergeOutMsg.append("[CHARGEDNUMBER="+chargedNumber+"]");
		   mergeOutMsg.append("[USERID="+userId+"]");
		   mergeOutMsg.append("[ACCTMONTH="+ratingMsg.getAcctMonthId()+"]");
		   mergeOutMsg.append("[PARTITIONNO="+ratingMsg.getPartitionNo()+"]");
		   
	  ///Balanceinfo
		   long totalAmount=0;
		   List<DeductInfo> listofDeductInfo = ratingData.getiDeductInfos();
		   if(listofDeductInfo.size() > 0){
			   //有资源扣取记录，需要组装B08消息
			   mergeOutMsg.append("[");
			   int count =0;
			   for(DeductInfo tmpdi:listofDeductInfo){
				   if(count>0){
					   mergeOutMsg.append(";");
				   }
					   //payid,balanceid,账本类型unittype,扣减量，账本余额,账本类型balancetypeid,账目项
				   mergeOutMsg.append(RatingMacro.PARA_B_ACCT_CHG_INFO+"=000,"); 
				   mergeOutMsg.append(RatingMacro.PARA_B_ACCT_CHG_PAY_ID+"="+tmpdi.getPayId()+",");//扣取的账户 20141224 add
				   mergeOutMsg.append(RatingMacro.PARA_B_ACCT_CHG_BAL_ID+"="+tmpdi.getLnAcctBalanceID()+",");//扣取的账本
				   mergeOutMsg.append(RatingMacro.PARA_B_ACCT_CHG_UNIT_TYPE+"="+tmpdi.getnBalanceUnitType()+",");
				   mergeOutMsg.append(RatingMacro.PARA_B_ACCT_CHG_AMOUNT+"="+tmpdi.getLnAmount()+",");
				   mergeOutMsg.append(RatingMacro.PARA_B_ACCT_CHG_BALANCE+"="+tmpdi.getLnBalance()+",");
				   mergeOutMsg.append(RatingMacro.PARA_B_ACCT_CHG_BALANCE_TYPE+"="+tmpdi.getLnBalanceTypeId()+",");
				   mergeOutMsg.append(RatingMacro.PARA_B_ACCT_CHG_ACCTITEM+"="+tmpdi.getLnAcct_ItemType_id());  
				   count++;
			   }
			   mergeOutMsg.append("]");
			   
			   //获得本次话单扣除资源账本额DeductResources 
			   List<DeductInfo> deductresources=ratingData.getiDeductInfos();
				   
				   for(DeductInfo tmpdi:listofDeductInfo){
					   totalAmount+=tmpdi.getLnAmount();
				   }
			   log.debug("本次话单扣除资源账本额="+totalAmount);
			    
			   
		   }
		  
		  
		   if(ratingMsg.getM_nMsgType()==60){
		  
			   //获取到花单的语音资源使用量vioceTotalAmount
			   long voiceTotalAmount=ratingMsg.getCommandMsg().getM_strRealDuration();
			   log.debug("从原始话单获取的总语音量="+voiceTotalAmount);
			   //话单的资源使用总量-  本次话单扣除资源账本额 =扣除金钱对应的资源使用量（voiceMoneytotalAmount）(语音)
			   long voiceMoneytotalAmount=voiceTotalAmount-totalAmount*60;
			   if(voiceMoneytotalAmount%60==0.0){
				   voiceMoneytotalAmount=(long)(voiceMoneytotalAmount/60); 
				  }else{
					  voiceMoneytotalAmount=(long)((voiceMoneytotalAmount/60)+1);
				  }
			   
			   if(voiceMoneytotalAmount <0){
				   voiceMoneytotalAmount = 0;
			   }
				  
			   log.debug("使用金钱的非呼转话单的金钱语音使用量"+voiceMoneytotalAmount);
			   if(voiceTotalAmount>totalAmount){//收费
				  if(ratingMsg.getBaseMsg().getM_strCallingType().equals("3")||ratingMsg.getBaseMsg().getM_strCallingType().equals("4")){//是呼转
					  mergeOutMsg.append("[");
					  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
					  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"1000"+",");
					  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+totalAmount+";");
					  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
					  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"1001"+",");
					  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+voiceTotalAmount); 
					  mergeOutMsg.append("]");
					  }	else{//非呼转
						  mergeOutMsg.append("[");
						  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
						  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"1000"+",");
						  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+totalAmount+";"); 
						  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
						  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"1001"+",");
						  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+voiceMoneytotalAmount); 
						  mergeOutMsg.append("]");
					  }
				  }else{//不收费的
					  mergeOutMsg.append("[");
					  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
					  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"1000"+",");
					  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+totalAmount+";"); 
					  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
					  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"1001"+",");
					  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+voiceMoneytotalAmount); 
					  mergeOutMsg.append("]");
				  } 
		   
		   }else if(ratingMsg.getM_nMsgType()==80){  
		   //获取到话单的流量资源使用总量 orgTotalAmount  
		   long orgTotalAmount = ratingMsg.getCommandMsg().getM_strRealTotalVolume();
		   log.debug("从原始话单获得的总流量="+orgTotalAmount);
		   
		   //话单的资源使用总量-  本次话单扣除资源账本额 =扣除金钱对应的资源使用量（orgMoneytotalAmount）(流量)
		  long orgMoneyTotalAmount=orgTotalAmount -totalAmount*1024;
		  if(orgMoneyTotalAmount%1024==0.0){
			  orgMoneyTotalAmount=(long)(orgMoneyTotalAmount/1024); 
		  }else{
			  orgMoneyTotalAmount=(long)((orgMoneyTotalAmount/1024)+1);
		  }
		  
		 
		  if(orgMoneyTotalAmount<=0){
			  orgMoneyTotalAmount=0;
			  log.debug("需要花费金钱的使用量="+orgMoneyTotalAmount);
		  }else{
		  log.debug("需要花费金钱的使用量="+orgMoneyTotalAmount);
		  }
		  mergeOutMsg.append("[");
		  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
		  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"3000"+",");
		  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+totalAmount+";");
		  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
		  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"3001"+",");
		  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+orgMoneyTotalAmount);
		  mergeOutMsg.append("]");
		  
		  

	     }else if(ratingMsg.getM_nMsgType()==70){
		  
		  //获取到话单的短信资源使用量mseeageTotalAmount(短信)
		  long messageTotalAmount=ratingMsg.getCommandMsg().getM_strRealTimes();
		   log.debug("从原始话单获取的总短信量="+messageTotalAmount);
		  
		//话单的资源使用总量-  本次话单扣除资源账本额 =扣除金钱对应的资源使用量（messageMoneytotalAmount）(短信)
		long messageMoneyTotalAmount=messageTotalAmount -totalAmount;
		  log.debug("需要花费金钱的使用量"+messageMoneyTotalAmount);
		  mergeOutMsg.append("[");
		  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
		  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"2000"+",");
		  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+totalAmount+";");
		  mergeOutMsg.append(RatingMacro.PARA_B_RATING_RES_MES_INFO+"=000,");
		  mergeOutMsg.append(RatingMacro.PARA_B_RES_TYPE+"="+"2001"+",");
		  mergeOutMsg.append(RatingMacro.PARA_B_RATING_TYPE+"="+messageTotalAmount);
		  mergeOutMsg.append("]");
		  
	}

////		  
		 // Accumlatorinfo 
		   List<Ratable> listofRatable = ratingData.getiRatableRefs();
		   if(listofRatable.size()>0){
			   mergeOutMsg.append("[");
			   int count =0;
			    for(Ratable tmpr:listofRatable){
			    	 if(count>0){
						   mergeOutMsg.append(";");
					   }
			    	mergeOutMsg.append(RatingMacro.PARA_B_RATABLE+"=000,");
			    	mergeOutMsg.append(RatingMacro.PARA_B_RATABLE_CODE+"="+tmpr.getStrRatableCode()+",");
			    	mergeOutMsg.append(RatingMacro.PARA_B_RATABLE_TYPE+"="+tmpr.getnRatableType()+",");
			    	mergeOutMsg.append(RatingMacro.PARA_B_RATABLE_VALUE+"="+tmpr.getLnValue()+",");
			    	mergeOutMsg.append(RatingMacro.PARA_B_RATABLE_TOTAL_VALUE+"="+tmpr.getLnRatableValue());
			    	count++;
			    }
			   mergeOutMsg.append("]");
			
		   }
		   //TariffInfo 需要扣钱的费用项
		   
		   List<BalanceContent> listofBalanceContent = ratingData.getiRealFees();
		   log.debug("扣费使用量账目项数量"+listofBalanceContent.size());
		   if(listofBalanceContent.size()>0 || listofDeductInfo.size()>0){
			   mergeOutMsg.append("[");
			  int count=0;
			      for(BalanceContent tmpbc:listofBalanceContent){
			    	  if(count>0){
						   mergeOutMsg.append(";");
					   }
			    	  mergeOutMsg.append(RatingMacro.PARA_R_REL_FEE+"=000,");
			    	  mergeOutMsg.append(RatingMacro.PARA_R_REL_FEE_A+"="+tmpbc.getLnAcctItemTypeId()+",");
			    	  mergeOutMsg.append(RatingMacro.PARA_R_REL_FEE_B+"="+tmpbc.getnUnitTypeId()+",");
			    	  mergeOutMsg.append(RatingMacro.PARA_R_REL_FEE_C+"="+tmpbc.getLnAmount());
			    	  count++;
			    	//  mergeOutMsg.append(";");
			      }
			      
			   //   count =0;
			      for(DeductInfo tmpdi:listofDeductInfo){
					   if(count>0){
						   mergeOutMsg.append(";");
					   }
					   
					   mergeOutMsg.append(RatingMacro.PARA_R_REL_FEE+"=000,");
				       mergeOutMsg.append(RatingMacro.PARA_R_REL_FEE_A+"="+tmpdi.getLnAcct_ItemType_id()+",");  // ratingMsg.getMsgMap().get("ACCTITEM")
				       mergeOutMsg.append(RatingMacro.PARA_R_REL_FEE_B+"="+tmpdi.getnBalanceUnitType()+",");
				       mergeOutMsg.append(RatingMacro.PARA_R_REL_FEE_C+"="+tmpdi.getLnAmount());
				       count++;
				    //   mergeOutMsg.append(";");
			      }
			      
			   mergeOutMsg.append("]");
		   }

		   
		   
        log.debug("通过createMergeOutMsg获得的返回信息=="+mergeOutMsg.toString());

		return mergeOutMsg.toString();
	}




	
	


}
