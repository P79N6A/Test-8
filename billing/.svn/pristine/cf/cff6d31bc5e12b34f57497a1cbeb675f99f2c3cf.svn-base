package com.tydic.beijing.billing.rating.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.rating.domain.BalanceContent;
import com.tydic.beijing.billing.rating.domain.OfrRateData;
import com.tydic.beijing.billing.rating.domain.RateData;
import com.tydic.beijing.billing.rating.domain.RateMeasure;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingErrorCode;
import com.tydic.beijing.billing.rating.domain.RatingException;
import com.tydic.beijing.billing.rating.domain.RatingExtMsg;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.SectionRateData;
import com.tydic.beijing.billing.rating.domain.StartValue;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;
import com.tydic.beijing.billing.rating.service.MsgParsing;
import com.tydic.beijing.billing.rating.service.Rating;
//import org.apache.log4j.Logger;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;
 
public class ChargeInitImpl implements Rating { //implements Charging {
	       

	@Autowired
	private DbUtil dbUtil;
//	@Autowired
//	UserInfoForMemCached userinfoForMemcached;
	@Autowired
	private ApplicationContextHelper applicationContextHelper;
	
	@Autowired
	private  CalculateFee calculateFee;
	@Autowired
	MsgParsing msgParsing ;
	
	private String filename="";
	private int cnt =1;

	

	//public static final Logger log = Logger.getLogger(ChargeInitImpl.class);
	
	public static final  Logger log = LoggerFactory.getLogger(ChargeInitImpl.class);
	
	@Override
	public String deal(String strRequestMsg)  {

		RatingData ratingData = new RatingData();
		RatingMsg ratingMsg = new RatingMsg();
		//MsgParsing msgParsing = new MsgParsingImpl();
		
		long starttime = System.currentTimeMillis();
		long endtime=1L;
		
          try {	  
        	  
//        	  String xx="xx";
//        	  int nn= Integer.parseInt(xx);
        	  
        	  
        		ratingMsg = msgParsing.getRatingMsgFromRequestMsg(ratingMsg,strRequestMsg);
        		
        		getAcctMonth(ratingMsg);
        		
        		
        		//checkReqMonth(ratingMsg);
        		
        		//ratingMsg.print();
        		log.debug("请求消息："+ratingMsg.getStrRequestMsg());
        		//checkActive(ratingMsg.getUserinfoForMemcached().getInfoUser());
        		log.debug("get event_type_id is "+ratingMsg.getM_iExtMsg().getM_nEvtTypeId());
        		

        	  ratingData.setnOper(RatingMacro.OPER_REAL);//OPER_REQ   后付费都是实扣啊，，，，，

        	  getInitRateMeasure(ratingMsg,ratingData);//后续可能要大改啊。。。。靠~~~~~~~~~~~~~~~~~~~
        	  setReqRateMesasure(ratingMsg,ratingData);
        	  
        	  if(ratingData.getiReqRateMeasure().getM_iUsedMeasureDomain()==-1){
        		  throw new RatingException(RatingErrorCode.ERR_NO_MEASUREDOMAIN,"没有使用量类型信息");
        	  }
        	  
        	  ratingData.setiRateMeasure(ratingData.getiReqRateMeasure());
        	 // CalculateFee calculateFee =  new CalculateFee(); 
        	  
        	 // CalculateFee calculateFee = new CalculateFee(); 
        	  
            //  RateData rateData = calculateFee.calcFee(ratingMsg,ratingData);	  
        	  RateData rateData = calculate(ratingMsg,ratingData);
              
        	  log.debug("==批价结束，获得ofrresult=="+rateData.getiOfrResults().size()+",sectionresult=="+rateData.getiOfrResults().get(0).getiSectionResults().size());
              List<BalanceContent> realfees = new ArrayList<BalanceContent>();
              for(OfrRateData tmpord:rateData.getiOfrResults()){
            	  
            	  for(SectionRateData tmpsr:tmpord.getiSectionResults()){
            		  BalanceContent tmpbal = new BalanceContent();
            		  tmpbal.setLnAmount(tmpsr.getSectionfee());
            		  tmpbal.setLnAcctItemTypeId(tmpsr.getLnAcctItemTypeId());
            		  tmpbal.setnUnitTypeId(2);
          			
            		  realfees.add(tmpbal);
          			
          		}
            	  
            	  
            	  
            	  if(tmpord.isDirect() && tmpord.getLnAcctItemTypeId()>0 ){
            		  BalanceContent tmpbal = new BalanceContent();
            		  tmpbal.setLnAmount(tmpord.getAmount());
            		  tmpbal.setLnAcctItemTypeId(tmpord.getLnAcctItemTypeId());
            		  tmpbal.setnUnitTypeId(2);
          			
            		  realfees.add(tmpbal);
            	  }
      		}
              
              
              
              ratingData.setiRealFees(realfees);
              
              endtime = System.currentTimeMillis();
              log.debug("批价耗时"+(endtime-starttime));
        
              //writerightmsglog(ratingMsg.getStrRequestMsg());
              
              return msgParsing.createMergeOutMsg(ratingMsg,ratingData);
			
		}
          catch (RatingException re){
			 re.printStackTrace();
			 //log.debug(re.getCode());
		      endtime = System.currentTimeMillis();
              log.debug("批价耗时"+(endtime-starttime)+",异常"+re.getMessage());
              
              writeerrmsglog(re.getCode(),strRequestMsg);
        
			 return msgParsing.createErrorOutMsg(re.getCode(), re.getMessage(), strRequestMsg);
			 
		}
          catch (Exception e) {
			e.printStackTrace();
			
			log.debug("localmessage:"+e.getLocalizedMessage());
			log.debug("message:"+e.getMessage());
			log.debug("exceptionname:"+e.getClass().getName());
			
			String errdetail = e.getClass().getName() +"$"+ e.getLocalizedMessage()+"$"+strRequestMsg;
			writeerrdetaillog(errdetail);
		      endtime = System.currentTimeMillis();
              log.debug("批价耗时"+(endtime-starttime));
              
              writeerrmsglog(-10000,strRequestMsg);
        
			return msgParsing.createErrorOutMsg(-10000, e.getMessage(), strRequestMsg);
		}
          
 	}
	
	 



private void getAcctMonth(RatingMsg ratingMsg) throws Exception {
		List<CodeAcctMonth> listCodeAcctMonth =  dbUtil.getAllCodeAcctMonth();
		log.debug("listCodeAcctMonth's size is "+listCodeAcctMonth.size());
		log.debug("allstarttime===>"+ratingMsg.getAllSessionStartTimes());
		
		Date sessionTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(ratingMsg.getAllSessionStartTimes());
		int acctMonthId ;
		String partitionNo ;
		CodeAcctMonth codeAcctMonth = null;
		CodeAcctMonth codeAcctMonthFinal = null;
		
		for(CodeAcctMonth tmpcam : listCodeAcctMonth){ //获取话单时间对应的账期
			if(tmpcam.getBil_eff_date().getTime() <= sessionTime.getTime() 
					 && tmpcam.getBil_exp_date().getTime() >= sessionTime.getTime()){
				codeAcctMonth = tmpcam;
				break;
			}
		}
		
		//开始判断账期状态
		
		/*
use_tag	act_tag	状态说明	计费
0	0	未启用账期	发话时间在该状态账期直接判错单
1	0	在用账期，且处于实时账务处理状态	发话时间在该状态账期，按照acct_month打账期标识
1	1	在用账期，且处于出账准备状态，此状态下计费账务信控仍处于实时账务处理状态，主要用于CRM参考该状态进行业务办理限制（如月末不允许预约套餐变更办理）	发话时间在该状态账期，按照acct_month打账期标识
1	2	在用账期，且处于出账状态，此状态下账务开始进行出账工作	发话时间在该状态账期，按照最小的user_tag=1,act_tag=0的acct_month打账期标识
2	3	出账完成，且该账期关闭	发话时间在该状态账期，按照最小的user_tag=1,act_tag=0的acct_month打账期标识

双账期改造后
use_tag	act_tag	状态说明	计费
0	0	未启用账期	发话时间在该状态账期直接判错单
1	0	在用账期，且处于实时账务处理状态	发话时间在该状态账期，按照acct_month打账期标识
1	1	在用账期，且处于出账准备状态，此状态下计费账务信控仍处于实时账务处理状态，主要用于CRM参考该状态进行业务办理限制（如月末不允许预约套餐变更办理）	发话时间在该状态账期，按照acct_month打账期标识
1	2	在用账期，且处于出账状态，此状态下账务开始进行出账工作	发话时间在该状态账期，按照最小的use_tag=1,act_tag=0的acct_month打账期标识
1	3	在用账期，且处于销账状态，在此状态下账务开始销账处理	发话时间在该状态账期，按照最小的use_tag=1,act_tag=0的acct_month打账期标识
2	4	出账完成，且该账期关闭	发话时间在该状态账期，按照最小的use_tag=1,act_tag=0的acct_month打账期标识（主要：如果话单账期超过两个月或两个月已上判定为错单）
		 * */
		if(codeAcctMonth.getUse_tag().equals("0") && codeAcctMonth.getAct_tag().equals("0")){
			throw new RatingException(RatingErrorCode.ERR_INVALID_ACCTMONTH,"未启用账期");
		} else if (codeAcctMonth.getUse_tag().equals("1") && codeAcctMonth.getAct_tag().equals("0")){
			acctMonthId = codeAcctMonth.getAcct_month() ;
			partitionNo = codeAcctMonth.getPartition_no();
		} else if (codeAcctMonth.getUse_tag().equals("1") && codeAcctMonth.getAct_tag().equals("1")){
			acctMonthId = codeAcctMonth.getAcct_month() ;
			partitionNo = codeAcctMonth.getPartition_no();
		} else if (codeAcctMonth.getUse_tag().equals("1") && codeAcctMonth.getAct_tag().equals("2")){
			codeAcctMonthFinal =  getFinalAcctMonth(codeAcctMonth,listCodeAcctMonth);
			acctMonthId = codeAcctMonthFinal.getAcct_month() ;
			partitionNo = codeAcctMonthFinal.getPartition_no();
		} else if (codeAcctMonth.getUse_tag().equals("1") && codeAcctMonth.getAct_tag().equals("3")){
			codeAcctMonthFinal =  getFinalAcctMonth(codeAcctMonth,listCodeAcctMonth);
			acctMonthId = codeAcctMonthFinal.getAcct_month() ;
			partitionNo = codeAcctMonthFinal.getPartition_no();
		} 		
		
		//双账期上线前保留
		else if (codeAcctMonth.getUse_tag().equals("2") && codeAcctMonth.getAct_tag().equals("3")){
			codeAcctMonthFinal =  getFinalAcctMonth(codeAcctMonth,listCodeAcctMonth);
			acctMonthId = codeAcctMonthFinal.getAcct_month() ;
			partitionNo = codeAcctMonthFinal.getPartition_no();
		} 	
		
		else if (codeAcctMonth.getUse_tag().equals("2") && codeAcctMonth.getAct_tag().equals("4")){
			codeAcctMonthFinal =  getFinalAcctMonth(codeAcctMonth,listCodeAcctMonth);
			acctMonthId = codeAcctMonthFinal.getAcct_month() ;
			partitionNo = codeAcctMonthFinal.getPartition_no();
		} else {
			throw new  RatingException (RatingErrorCode.ERR_ACCTMONTH_CONFIG,"账期配置异常");
		}
		
		log.debug("result acctmonth is "+acctMonthId +" and result partitioinno is "+partitionNo);
		
		if( acctMonthId ==0 || partitionNo == null || partitionNo.equals("")){
			throw new RatingException (RatingErrorCode.ERR_ACCTMONTH_CONFIG,"账期配置异常");
		}
		
		ratingMsg.setAcctMonthId(acctMonthId+"");
		ratingMsg.setPartitionNo(partitionNo);
		
		
	}




/*按照最小的user_tag=1,act_tag=0的acct_month打账期标识
 * */
private CodeAcctMonth getFinalAcctMonth(CodeAcctMonth codeAcctMonth,
		List<CodeAcctMonth> listCodeAcctMonth) throws Exception {
	
	CodeAcctMonth finalAcctMonth = new CodeAcctMonth();
	List<CodeAcctMonth> listValidAcctMonth = new ArrayList<CodeAcctMonth>();
	
	for(CodeAcctMonth tmpcam : listCodeAcctMonth){
		if(tmpcam.getUse_tag().equals("1") &&
				(tmpcam.getAct_tag().equals("0") || tmpcam.getAct_tag().equals("1") )){
			listValidAcctMonth.add(tmpcam);
		}
	}
	
	log.debug("get listValidAcctMonth's size =>"+listValidAcctMonth.size());
	if(listValidAcctMonth  == null || listValidAcctMonth.size() ==0){
		throw new  RatingException (RatingErrorCode.ERR_ACCTMONTH_CONFIG,"账期配置异常");
	}
	
	finalAcctMonth = listValidAcctMonth.get(0);
	for(CodeAcctMonth tmpcam : listValidAcctMonth){
       	if(tmpcam.getAcct_month() < finalAcctMonth.getAcct_month()){
       		finalAcctMonth = tmpcam;
       	}
	}

	log.debug("the finalacctmonth is =>"+finalAcctMonth.getAcct_month());
	
	return finalAcctMonth;
}





private RateData calculate(RatingMsg ratingMsg, RatingData ratingData) throws Exception  {

	RateData ratedata = new RateData();
	try {
		ratedata= calculateFee.calcFee(ratingMsg,ratingData);	  
		
		
	} catch (RatingException re){
		if(re.getCode() == RatingErrorCode.ERR_BALANCE_MINUS ){
			log.debug("calculate once more!!");
			ratedata = calculate(ratingMsg,ratingData);
		}else {
			re.printStackTrace();
			throw re;
		}
	}
	catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw e;
	}
	
	return ratedata;
	
	}





//	@Transactional(rollbackFor=Exception.class)
//	public void testupdate(){
//		dbUtil.updateInfoPayBalance(2862, 50, 0);
//		
//		log.debug("update over");
//	}
	
	
	public void writeerrmsglog(int code,String msg){
		 FileWriter writer = null;  
	        try {     
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
	            writer = new FileWriter("errmsg.log", true);     
	            writer.write(code+"$"+msg+"\r\n");       
	        } catch (IOException e) {     
	            e.printStackTrace();     
	        } finally {     
	            try {     
	                if(writer != null){  
	                    writer.close();     
	                }  
	            } catch (IOException e) {     
	                e.printStackTrace();     
	            }     
	        }   
	}
	
	
	public void writeerrdetaillog(String msg){
		 FileWriter writer = null;  
	        try {     
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
	            writer = new FileWriter("errdetail.log", true);     
	            writer.write(msg+"\r\n");       
	        } catch (IOException e) {     
	            e.printStackTrace();     
	        } finally {     
	            try {     
	                if(writer != null){  
	                    writer.close();     
	                }  
	            } catch (IOException e) {     
	                e.printStackTrace();     
	            }     
	        }   
	}
	
	public void writerightmsglog(String msg){
		 FileWriter writer = null;  
	        try {     
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件    
	        	filename = "rightmsg.log."+cnt;
	        	File file =  new File(filename);
	        	//String newfilename="";
	        	if(file.length()/(1024*1024)>500){
	        		cnt++;
	        		filename =  "rightmsg.log."+cnt;
	        		
	        	}
	        	
	            writer = new FileWriter(filename, true);    
	            writer.write(msg+"\r\n");       
	        } catch (IOException e) {     
	            e.printStackTrace();     
	        } finally {     
	            try {     
	                if(writer != null){  
	                    writer.close();     
	                }  
	            } catch (IOException e) {     
	                e.printStackTrace();     
	            }     
	        }   
	}
	

	private void setReqRateMesasure(RatingMsg ratingMsg ,RatingData ratingData) {

		if(ratingMsg.getM_iRatingExtMsg().getM_strDuration() != null && ratingMsg.getM_iRatingExtMsg().getM_strDuration().length()>0){
			ratingData.setLnReqDuration(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strDuration()));
		}
		
		if(ratingMsg.getM_iRatingExtMsg().getM_strTimes() !=null && ratingMsg.getM_iRatingExtMsg().getM_strTimes().length() >0 ){
			ratingData.setLnReqTimes(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strTimes()));
		}

		if(ratingMsg.getM_iRatingExtMsg().getM_strUpVolume() !=null && ratingMsg.getM_iRatingExtMsg().getM_strUpVolume().length()>0){
			ratingData.setLnReqUpVolume(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strUpVolume())
					+Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strUpVolumeFeeLast()));
		}
		
		if(ratingMsg.getM_iRatingExtMsg().getM_strDownVolume() !=null && ratingMsg.getM_iRatingExtMsg().getM_strDownVolume().length()>0 ){
			ratingData.setLnReqDownVolume(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strDownVolume())
					+ Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strDownVolumeFeeLast()));
		}

	    if(ratingMsg.getM_iRatingExtMsg().getM_strTotalVolume() !=null && ratingMsg.getM_iRatingExtMsg().getM_strTotalVolume().length()>0){
	    	ratingData.setLnReqTotalVolume(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strTotalVolume())
	    			+ Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strTotalVolumeFeeLast()));
	    }
 		
	}

	///初始化使用量
	private void getInitRateMeasure(RatingMsg ratingMsg,RatingData ratingData) {
		////处理时间
		RatingExtMsg ratingExtMsg = ratingMsg.getM_iRatingExtMsg();
		StartValue startValue = ratingData.getiStartValue();
		RateMeasure rateMeasure = new RateMeasure();
		
		ratingExtMsg.setM_strExtStartTime(ratingMsg.getBaseMsg().getM_strStartTime());
		ratingExtMsg.setM_strExtCurrTime(ratingMsg.getVarMsg().getM_strCurrTime());//R604 会话本次实际扣费开始时间
		ratingExtMsg.setM_strExtLastTime(ratingMsg.getVarMsg().getM_strLastTime()); //R603会话上次实际扣费开始时间
		//ratingMsg.setM_iRatingExtMsg(ratingExtMsg);
		
	    //预占时计费开始点计算方法:累计扣费量+累计未扣费量
	    //处理实长
		if(ratingMsg.getCommandMsg().getM_strRealDuration() >0){
			
		    ratingExtMsg.setM_strDuration(""+ratingMsg.getCommandMsg().getM_strRealDuration());
		    //startValue.setLnDuration(ratingData.getiPrevSessionValue().getUsedDuration() + ratingData.getiCurrSessionValue().getUsedDuration());
		    
		    rateMeasure.setLnDuration(ratingMsg.getCommandMsg().getM_strRealDuration()); //.getM_strReqDuration()
		    //rateMeasure.setM_iUsedMeasureDomain(Integer.parseInt(RatingMacro.SECTION_DOMAIN_DURATION));
		}
		////次数
		if(ratingMsg.getCommandMsg().getM_strRealTimes()>0){
	        ratingExtMsg.setM_strTimes(""+ratingMsg.getCommandMsg().getM_strRealTimes());
	        //startValue.setLnTimes(ratingData.getiPrevSessionValue().getUsedTimes() + ratingData.getiCurrSessionValue().getUsedTimes());
	        
	        rateMeasure.setLnTimes(ratingMsg.getCommandMsg().getM_strRealTimes()); //.getM_strReqTimes())
	        //rateMeasure.setM_iUsedMeasureDomain(Integer.parseInt(RatingMacro.SECTION_DOMAIN_TIMES));
		}
		///上行流量
		if(ratingMsg.getCommandMsg().getM_strRealUpVolume() >0 ){
	        ratingExtMsg.setM_strUpVolume(""+ratingMsg.getCommandMsg().getM_strRealUpVolume());
	       // startValue.setLnUpVolume(ratingData.getiPrevSessionValue().getUsedUpVolume() + ratingData.getiCurrSessionValue().getUsedUpVolume() );
	        ratingExtMsg.setM_strUpVolumeFeeLast("0");
	        
	        rateMeasure.setLnUpVolume(ratingMsg.getCommandMsg().getM_strRealUpVolume()); //.getM_strReqUpVolume()
	        //rateMeasure.setM_iUsedMeasureDomain(Integer.parseInt(RatingMacro.SECTION_DOMAIN_UPVOLUME));
		}
		///下行流量
		if(ratingMsg.getCommandMsg().getM_strRealDownVolume() >0){
		     ratingExtMsg.setM_strDownVolume(""+ratingMsg.getCommandMsg().getM_strRealDownVolume());
		     ratingExtMsg.setM_strDownVolumeFeeLast("0");
		     //startValue.setLnDownVolume(ratingData.getiPrevSessionValue().getUsedDownVolume() + ratingData.getiCurrSessionValue().getUsedDownVolume());
		     
		     rateMeasure.setLnDownVolume(ratingMsg.getCommandMsg().getM_strRealDownVolume());//.getM_strReqDownVolume()
		}
         ////总流量
		if(ratingMsg.getCommandMsg().getM_strRealTotalVolume() >0){
		    ratingExtMsg.setM_strTotalVolume("" + ratingMsg.getCommandMsg().getM_strRealTotalVolume()); //getM_strReqTotalVolume()
		    ratingExtMsg.setM_strTotalVolumeFeeLast("0");
		    //startValue.setLnTotalVolume(ratingData.getiPrevSessionValue().getUsedTotalVolume() + ratingData.getiCurrSessionValue().getUsedTotalVolume());
		    
		    rateMeasure.setLnTotalVolume(ratingMsg.getCommandMsg().getM_strRealTotalVolume()); //.getM_strReqTotalVolume()
		    //rateMeasure.setM_iUsedMeasureDomain(Integer.parseInt(RatingMacro.SECTION_DOMAIN_TOTALVOLUME));
		}
		
		//rateMeasure.setLnTotalVolume(Long.parseLong(ratingExtMsg.getM_strTotalVolumeFeeLast())); //
		//rateMeasure.setLnLastDownVolume(Long.parseLong(ratingExtMsg.getM_strDownVolumeFeeLast()));
		//rateMeasure.setLnLastUpVolume(Long.parseLong(ratingExtMsg.getM_strUpVolumeFeeLast()));

		rateMeasure.setLnUnusedDuration(rateMeasure.getLnDuration());
		rateMeasure.setLnUnusedTotalVolume(rateMeasure.getLnTotalVolume());
		rateMeasure.setLnUnusedTimes(rateMeasure.getLnTimes());
		rateMeasure.setLnUnusedUpVolume(rateMeasure.getLnUpVolume());
		rateMeasure.setLnUnusedDownVolume(rateMeasure.getLnDownVolume());
		rateMeasure.setLnUnusedLastTotalVolume(rateMeasure.getLnLastTotalVolume());
		rateMeasure.setLnUnusedLastUpVolume(rateMeasure.getLnLastUpVolume());
		rateMeasure.setLnUnusedLastDownVolume(rateMeasure.getLnLastDownVolume());
		
		int msgType =ratingMsg.getM_nMsgType();
		if(msgType ==60){
			rateMeasure.setM_iUsedMeasureDomain(Integer.parseInt(RatingMacro.SECTION_DOMAIN_DURATION));
		}else if (msgType ==70){
			rateMeasure.setM_iUsedMeasureDomain(Integer.parseInt(RatingMacro.SECTION_DOMAIN_TIMES));
		}else if (msgType ==80){
			rateMeasure.setM_iUsedMeasureDomain(Integer.parseInt(RatingMacro.SECTION_DOMAIN_TOTALVOLUME));
		}else if (msgType ==180){
			rateMeasure.setM_iUsedMeasureDomain(Integer.parseInt(RatingMacro.SECTION_DOMAIN_TIMES));
		}
		
		
  
		/////
		    
		ratingMsg.setM_iRatingExtMsg(ratingExtMsg);
		ratingData.setiStartValue(startValue);
		ratingData.setiReqRateMeasure(rateMeasure);
		
	}









}
