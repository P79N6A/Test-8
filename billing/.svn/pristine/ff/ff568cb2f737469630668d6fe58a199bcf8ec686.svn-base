package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.rating.domain.BalanceContent;
import com.tydic.beijing.billing.rating.domain.DeductInfo;
import com.tydic.beijing.billing.rating.domain.InfoRatableHistory;
import com.tydic.beijing.billing.rating.domain.OfrRateData;
import com.tydic.beijing.billing.rating.domain.ParamData;
import com.tydic.beijing.billing.rating.domain.PlanDisct;
import com.tydic.beijing.billing.rating.domain.PricingSection;
import com.tydic.beijing.billing.rating.domain.RatableResourceInfo;
import com.tydic.beijing.billing.rating.domain.RatableResourceValue;
import com.tydic.beijing.billing.rating.domain.RateData;
import com.tydic.beijing.billing.rating.domain.RateMeasure;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.TokenNode;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.dto.SpyMemcachedClient;
import com.tydic.beijing.billing.rating.dto.impl.DbUtilImpl;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;
import com.tydic.beijing.billing.rating.service.ErrorInfo;
import com.tydic.beijing.billing.rating.service.PricingSectionRate;
import com.tydic.beijing.billing.rating.service.RatingException;
import com.tydic.beijing.billing.rating.util.DateUtil;



public class GroupRatingSvc {

	private static  Logger log = Logger.getLogger(GroupRatingSvc.class);
	
	private TokenNode groupNode;

	private RatingData ratingData;

	private RatingMsg ratingMsg;
	
	private DinnerConversionImpl dinnerConversion;
	
	private RatableResourceExtractionImpl resource;
	
	private List<BalanceContent> iRealFees  =new ArrayList<BalanceContent>();
	
	private String currTime="";
	
	private RateData groupRateResult=null;
	
	PricingSectionRate sectionRate;
	@Autowired
    ApplicationContextHelper applicationContextHelper;
	
	private DeductResourceAcct dra;
	
	public GroupRatingSvc(RatingMsg ratingMsg,RatingData ratingData,DinnerConversionImpl dinnerConversion ,RatableResourceExtractionImpl resource){
		this.ratingMsg=ratingMsg;
		this.ratingData=ratingData;
		this.dinnerConversion=dinnerConversion;
		this.resource=resource;
	}
	
	
	
	
	/**
	 * @param groupNode the groupNode to set
	 */
	public void setGroupNode(TokenNode groupNode) {
		this.groupNode = groupNode;
	}
	
	public int rateGroup() throws RatingException{
//		dra=new DeductResourceAcct(ratingMsg,ratingData);
//		dra.setCurrTime(currTime);
////		dra.setCurrTime(ratingMsg.getVarMsg().getM_strCurrTime());
//		log.debug("rateGroup:根据消息获取时间为:"+ratingMsg.getBaseMsg().getM_strStartTime());
//		log.debug("this.currTime:::::"+this.currTime);
//		
//		groupRateResult=new RateData();
//		sectionRate=new PricingSectionImpl(ratingMsg,ratingData,resource);
//		RateMeasure iRateMeasure=groupNode.getM_iRateData().getiRateMeasure();
//		boolean directFlag=false;
//		Map<String,RatableResourceValue> iRatableValues=groupNode.getM_iRateData().getiRatableValues();
////		String m_nValue=groupNode.getM_nValue();
//		List<PlanDisct> dinners=dinnerConversion.getiOfrInGroups().get(groupNode.getM_nValue());
//		
//		Collections.sort(dinners,new Comparator<PlanDisct>(){
//			public int compare(PlanDisct o1, PlanDisct o2) {
//				return (int)(o2.getnCalcPriority()-o1.getnCalcPriority());  //降序
//			}
			
//		});
//		
//		int fav=dinnerConversion.getiGroupFavModes().get(groupNode.getM_nValue());
//		
//		boolean bSuccess = false;
//		OfrRateData ofrRateResult=null;
//		OfrRateData iOfrRateTemp=null;
//		OfrRateData iOfrRateCurr=null;
//		
//		switch(fav){
//		case RatingMacro.GROUP_FAVMODE_MUTEX:  //排它
//			
//			
//			for(PlanDisct plan:dinners){
//				iOfrRateTemp=new OfrRateData();
//				iOfrRateTemp.setiRateMeasure(iRateMeasure);
//				iOfrRateTemp.setiRatableValues(iRatableValues);   
//				
//				if(!ratingMsg.isNeedRating()){
//					log.debug("不需要批价");
//					log.debug("不需要批价");
//					String acctItemCode=plan.getAcct_item_type();
//					List<BalanceContent > a=ratingMsg.getM_iBalanceInMsg().getLtDeduct();
//					BalanceContent balanceOld=a.get(0);
//					BalanceContent iBalanceContent =new BalanceContent();
//					if(acctItemCode.isEmpty()){
//						acctItemCode="0";
//					}
//					
//					iBalanceContent.setLnAcctItemTypeId(Integer.parseInt(acctItemCode));
//				    iBalanceContent.setnUnitTypeId(balanceOld.getnUnitTypeId());
//				    iBalanceContent.setLnAmount(balanceOld.getLnAmount());
//				    iBalanceContent.setLnDosage(balanceOld.getLnDosage());
//				    iBalanceContent.setLnLeftMoney(0);
//				    iBalanceContent.setnMeasureDomain(balanceOld.getnMeasureDomain());
//				    
//				    
////				    ratingData.getiRealFees().add(iBalanceContent);
//				    groupRateResult.getiRealFees().add(iBalanceContent);
//				    
//				    return 0;
//				}
//				log.debug("strategyId:"+plan.getLnStrategyId());
//				if(plan.getLnStrategyId()==RatingMacro.RATE_NO_STRATEGY){
//					continue;
//				}
//				if(plan.getLnStrategyId() !=-1){
//					try {
//						ofrRateResult=sectionRate.rateOfr(plan,iOfrRateTemp);
//					} catch (RatingException e) {
//						log.debug(e.printError());
//						continue;
//					}
//					
//				}else{
//					directFlag=true;
//					try {
//						
//						log.debug("direct balance acct_item_type:"+plan.getAcct_item_type());
//						log.debug("套餐:"+plan.getnAtomOfrId());
//						List<WriteOffDetail> lwriteOffDetail=new ArrayList<WriteOffDetail>();
//						log.debug("acct_item_type:"+plan.getAcct_item_type());
//						if(!plan.getAcct_item_type().equals("-1")){//收费
////							String currTime=ratingMsg.getVarMsg().getM_strCurrTime();
//							int result=dra.deductResourceAcctBalance(Integer.parseInt(plan.getAcct_item_type()),lwriteOffDetail,currTime);
//							log.debug("writeOff result:"+result);
//							updateDirectRatableResource(plan,lwriteOffDetail);
//							continue;
//						}else{	//免费
//							
////							String currTime=ratingMsg.getVarMsg().getM_strCurrTime();
//							int result=dra.deductResourceAcctBalance(Integer.parseInt(plan.getAcct_item_type()),lwriteOffDetail,currTime);
//							log.debug("writeOff result:"+result);
//							updateDirectRatableResource(plan,lwriteOffDetail);
//							BalanceContent iBalanceContent =new BalanceContent();
//							iBalanceContent.setLnAcctItemTypeId(Integer.parseInt(plan.getAcct_item_type()));
//						    iBalanceContent.setnUnitTypeId(2);
//						    iBalanceContent.setLnAmount(0);
//						    iBalanceContent.setLnDosage(0);
//						    iBalanceContent.setLnLeftMoney(0);
//						    iBalanceContent.setnMeasureDomain(0);
//						    groupRateResult.getiRealFees().add(iBalanceContent);
//						    
////						    ratingData.getiRealFees().add(iBalanceContent);
//						    
//							return 0;
//						}
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//						log.error(e.getMessage());
//						throw new RatingException(ErrorInfo.ERR_WRITEOFF,"writeOff:"+e.getMessage());
//					}
//					
//				}
//				iOfrRateTemp.setDirect(directFlag);
//				groupRateResult.addOfrRateResult(iOfrRateTemp);
//				bSuccess = true;
//				break;
//				
//			}
//			break;
//		case RatingMacro.GROUP_FAVMODE_BEST://取优
//			boolean bFirstOfr = true;
//			int s=dinners.size();
//			PlanDisct plan=null;
//			int i=0;
//			for(;i<s;i++){
//				plan=dinners.get(i);
//				iOfrRateTemp=new OfrRateData();
//				iOfrRateTemp.setiRateMeasure(iRateMeasure);
//				iOfrRateTemp.setiRatableValues(iRatableValues);
//				log.debug("plan>>>>>"+plan.getnAtomOfrId()+","+plan.getLnStrategyId());
//				
//				log.debug("");
//				
//				
//				if(!ratingMsg.isNeedRating()){
//					String acctItemCode=plan.getAcct_item_type();
//					List<BalanceContent > a=ratingMsg.getM_iBalanceInMsg().getLtDeduct();
//					BalanceContent balanceOld=a.get(0);
//					BalanceContent iBalanceContent =new BalanceContent();
//					iBalanceContent.setLnAcctItemTypeId(Integer.parseInt(acctItemCode));
//				    iBalanceContent.setnUnitTypeId(balanceOld.getnUnitTypeId());
//				    iBalanceContent.setLnAmount(balanceOld.getLnAmount());
//				    iBalanceContent.setLnDosage(balanceOld.getLnDosage());
//				    iBalanceContent.setLnLeftMoney(0);
//				    iBalanceContent.setnMeasureDomain(balanceOld.getnMeasureDomain());
//				    ratingData.getiRealFees().add(iBalanceContent);
//				    return 0;
//				}
//				
//				if(plan.getLnStrategyId()==-1){
//					directFlag=true;
//					
//					try {
//						log.debug("direct balance acct_item_type:"+plan.getAcct_item_type());
//						
//						List<WriteOffDetail> lwriteOffDetail=new ArrayList<WriteOffDetail>();
//						if(!plan.getAcct_item_type().equals("-1")){	//收费
//							
//							String currTime=ratingMsg.getVarMsg().getM_strCurrTime();
//							int result=dra.deductResourceAcctBalance(Integer.parseInt(plan.getAcct_item_type()),lwriteOffDetail,currTime);
//						
//							updateDirectRatableResource(plan,lwriteOffDetail);
//							continue;
//						}else{	//免费
//							
//							String currTime=ratingMsg.getVarMsg().getM_strCurrTime();
//							int result=dra.deductResourceAcctBalance(Integer.parseInt(plan.getAcct_item_type()),lwriteOffDetail,currTime);
//							
//							updateDirectRatableResource(plan,lwriteOffDetail);
//							BalanceContent iBalanceContent =new BalanceContent();
//							iBalanceContent.setLnAcctItemTypeId(Integer.parseInt(plan.getAcct_item_type()));
//						    iBalanceContent.setnUnitTypeId(2);
//						    iBalanceContent.setLnAmount(0);
//						    iBalanceContent.setLnDosage(0);
//						    iBalanceContent.setLnLeftMoney(0);
//						    iBalanceContent.setnMeasureDomain(0);
//						    ratingData.getiRealFees().add(iBalanceContent);
//							
//							return 0;
//						}
//						
//					} catch (Exception e) {
//						log.error(e.getMessage());
//						throw new RatingException(ErrorInfo.ERR_WRITEOFF,"writeOff:"+e.getMessage());
//					}
//				}
//				OfrRateData bestRateResult=null;
//				try {
//					bestRateResult=sectionRate.rateOfr(plan,iOfrRateTemp);
//				} catch (RatingException e) {
//					log.debug(e.printError());
//					if(e.getErrorCode()==ErrorInfo.NOT_VALID_PRICING_PLAN){
//						continue;
//					}else{
//						break;
//					}
//					
//				}
//				if(bestRateResult == null )
//					break;
//				else{
//					if(bFirstOfr){
//						iOfrRateCurr=bestRateResult;
//						bFirstOfr=false;
//					}else{
//						if(new Comparator<OfrRateData>(){
//							public int compare(OfrRateData a,OfrRateData b){
//								return (int)(a.sum()-b.sum());
//							}
//						}.compare(bestRateResult ,iOfrRateCurr)<0){
//							iOfrRateCurr=bestRateResult;
//						}
//					}
//				}
//			}
////			if(iOfrRateCurr !=null){
//			if(i==s && iOfrRateCurr !=null){
//				groupRateResult.addOfrRateResult(iOfrRateCurr);
//				bSuccess=true;
//			}
//			
//			break;
//		case RatingMacro.GROUP_FAVMODE_ADD://叠加
//			int size=dinners.size();
//			int j=0;
//			PlanDisct p=new PlanDisct();
//			for(;j<size;j++){
//				p=dinners.get(j);
//				iOfrRateTemp=new OfrRateData();
//				iOfrRateTemp.setiRateMeasure(iRateMeasure);
//				iOfrRateTemp.setiRatableValues(iRatableValues);
//				if(!ratingMsg.isNeedRating()){
//					String acctItemCode=p.getAcct_item_type();
//					List<BalanceContent > a=ratingMsg.getM_iBalanceInMsg().getLtDeduct();
//					BalanceContent balanceOld=a.get(0);
//					BalanceContent iBalanceContent =new BalanceContent();
//					iBalanceContent.setLnAcctItemTypeId(Integer.parseInt(acctItemCode));
//				    iBalanceContent.setnUnitTypeId(balanceOld.getnUnitTypeId());
//				    iBalanceContent.setLnAmount(balanceOld.getLnAmount());
//				    iBalanceContent.setLnDosage(balanceOld.getLnDosage());
//				    iBalanceContent.setLnLeftMoney(0);
//				    iBalanceContent.setnMeasureDomain(balanceOld.getnMeasureDomain());
//				    ratingData.getiRealFees().add(iBalanceContent);
//				    return 0;
//				}
//				if(p.getLnStrategyId()==-1){	//资源直扣
//					directFlag=true;
//					try {
//						List<WriteOffDetail> lwriteOffDetail=new ArrayList<WriteOffDetail>();
//						if(!p.getAcct_item_type().equals("-1")){	//收费资源直扣
//							
//							String currTime=ratingMsg.getVarMsg().getM_strCurrTime();
//							
//							int result=dra.deductResourceAcctBalance(Integer.parseInt(p.getAcct_item_type()),lwriteOffDetail,currTime);
//							updateDirectRatableResource(p,lwriteOffDetail);
//						
//							continue;
//						}else{	//免费资源直扣
//							String currTime=ratingMsg.getVarMsg().getM_strCurrTime();
//							int result=dra.deductResourceAcctBalance(Integer.parseInt(p.getAcct_item_type()),lwriteOffDetail,currTime);
//							updateDirectRatableResource(p,lwriteOffDetail);
//							return 0;
//						}
//					} catch (Exception e) {
//						log.error(e.getMessage());
//						throw new RatingException(ErrorInfo.ERR_WRITEOFF,"writeOff:"+e.getMessage());
//					}
//				}
//				try {
//					iOfrRateTemp=sectionRate.rateOfr(p,iOfrRateTemp);
//				} catch (RatingException e) {
//					log.debug(e.printError());
//					if(e.getErrorCode()==ErrorInfo.NOT_VALID_PRICING_PLAN){
//						continue;
//					}else{
//						break;
//					}
//				}
//				if(iOfrRateTemp==null)
//					break;
//				else{
//
//					iRateMeasure=iOfrRateTemp.getiRateMeasure();
//
//					iRatableValues=iOfrRateTemp.getiRatableValues();
//					iOfrRateTemp.setDirect(directFlag);
//					groupRateResult.addOfrRateResult(iOfrRateTemp);
//					if(iRateMeasure.isNoLeftDosage())
//						break;
//				}
//			}
//			if(iRateMeasure.isNoLeftDosage() || j==size){
//				bSuccess=true;
//			}
//			break;
//		default:
//			break;
//			
//		}
//		if(bSuccess){
//			
//			return 0;
//		}
		return ErrorInfo.ERR_IN_RATING;
	}
	
	
	
	public boolean updateDirectRatableResource(PlanDisct plan,List<WriteOffDetail> writeOff){

		log.debug("infos  size :"+resource.getiPlanRatableResourceInfos().size());
				
		RatableResourceInfo info=resource.getiPlanRatableResourceInfos().get(""+plan.getnAtomOfrId());
//		log.debug("updateDirectRatableResource  查询累积量信息为:"+info);
		
		long value=0;
		
		if(writeOff.isEmpty())
			return false;
		
		for(WriteOffDetail   w  :writeOff){
			value+=w.getWriteoff_fee();
		}
		int unit_type=writeOff.get(0).getUnit_type_id();
		long directBalance=0;
		switch(unit_type){
		case 1:
			directBalance=value*RatingMacro.UNIT_VALUE_VAC;
			break;
		case 2:
			directBalance=value*RatingMacro.UNIT_VALUE_GGSN;
			break;
		case 3:
		case 5:
			directBalance=value*RatingMacro.UNIT_VALUE_SMS;
			break;
		
		}
		ratingData.getiTmpRatableResourceValues().get(info.getM_strRatableResourceCode()).add(directBalance);
		return true;
	}
	
	
	
	public RateData getGroupRateResult() {
		return groupRateResult;
	}


	public void setGroupRateResult(RateData groupRateResult) {
		this.groupRateResult = groupRateResult;
	}
	

public String getYearMonth(String planEffDate,String msgTime,int lifeType,int startValue,int endValue){
		

		
		String yearMonth="";
		//天:前闭后开  月:闭区间
		SimpleDateFormat df=new SimpleDateFormat("yyyymmddHH24mmss");
		Calendar cal=Calendar.getInstance();
//		String yearMonth=df.format(cal.getTime()).substring(0,10);//初始值为本次计费请求开始时间
		
		
		if(lifeType==RatingMacro.LifeType_Day){ //日
			if(startValue==-1){
				startValue=0;
			}
			if(endValue==-1){
				endValue=24;
			}
			int hour=Integer.parseInt(msgTime.substring(8,10));
			if(startValue<endValue){  //8:00-12:00 不含12:00
				if(hour>=endValue||hour<startValue ){
					return "";
				}
				yearMonth=msgTime.substring(0,8);
			}else if(startValue>endValue){  //22:00-8:00 
				if(hour>=endValue && hour<startValue) 
					return "";
				else{
					yearMonth=msgTime.substring(0,8);
					if(hour<startValue){
						yearMonth=DateUtil.addSeconds(yearMonth, -24*60*60, 8);
					}
				}
			}else{
				yearMonth=msgTime.substring(0,8);
				if(hour<startValue){
					yearMonth=addSeconds(yearMonth,-86400,8);
				}	
			}
		}else if(lifeType==RatingMacro.LifeType_Month){//月
			if(startValue==-1)
				startValue=1;
			if(endValue==-1)
				endValue=31;
			int date=Integer.parseInt(msgTime.substring(6,8));
			if(startValue<endValue){  //8-15日,包含15日
				if(date>endValue || date<startValue){
					return "";
				}
				yearMonth=msgTime.substring(0,6)+"00";   
//				yearMonth=msgTime.substring(0,6);
			}else if(startValue>endValue){			//22日-8日(含8日)
				if(date>endValue && date<startValue){
					return "";
				}else{
					yearMonth=msgTime.substring(0,6)+"00";	
//					yearMonth=msgTime.substring(0,6);
					yearMonth=DateUtil.getLastMonth(yearMonth, 8);
//					yearMonth=DateUtil.getLastMonth(yearMonth, 6);
				}
			}else{
				if(date !=startValue)
					return "";
				yearMonth=msgTime.substring(0,6)+"00";
//				yearMonth=msgTime.substring(0,6);
			}
		}else if(lifeType==RatingMacro.LifeType_HalfYear){
			if(planEffDate.equals("-1")){
				return "";
			}
			yearMonth=planEffDate;
		}
		return yearMonth;
	}
	
private String addSeconds(String strDate,int seconds,int format){
	String retDate="";
	Calendar cal=Calendar.getInstance();
	cal.set(Calendar.MILLISECOND, 0);
	switch(format){
		case 8:
			cal.set(Calendar.YEAR, Integer.parseInt(strDate.substring(0, 4)));
			cal.set(Calendar.MONTH, Integer.parseInt(strDate.substring(4,6))-1);
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDate.substring(6,8)));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.add(Calendar.SECOND, seconds);
			retDate=new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
			break;
		default:
			return null;
	}
	return retDate;
}


public List<BalanceContent> getiRealFees() {
	return iRealFees;
}

public void setiRealFees(List<BalanceContent> iRealFees) {
	this.iRealFees = iRealFees;
}

public void setCurrTime(String currTime) {
	this.currTime = currTime;
}
public String getCurrTime() {
	return currTime;
}


}
