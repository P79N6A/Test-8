/**
 * 
 */
package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.rating.domain.ChargeUnit;
import com.tydic.beijing.billing.rating.domain.CodeRatableResource;
import com.tydic.beijing.billing.rating.domain.InfoRatableHistory;
import com.tydic.beijing.billing.rating.domain.ParamData;
import com.tydic.beijing.billing.rating.domain.PlanDisct;
import com.tydic.beijing.billing.rating.domain.Ratable;
import com.tydic.beijing.billing.rating.domain.RatableCondCheck;
import com.tydic.beijing.billing.rating.domain.RatableCondInParam;
import com.tydic.beijing.billing.rating.domain.RatableResourceInfo;
import com.tydic.beijing.billing.rating.domain.RatableResourceValue;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.RuleOfrResourceRel;
import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.dto.impl.DbUtilImpl;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;
import com.tydic.beijing.billing.rating.util.DateUtil;

/**
 * @author sung
 *
 */
public class RatableResourceUpdate {

	
	private RatingMsg ratingMsg;
	
	private RatingData ratingData;
	
	private DinnerConversionImpl dinnerConversion;
	private RatableResourceExtractionImpl resource;
	@Autowired
	private ApplicationContextHelper applicationContextHelper;
	private DbConfigDetail  dbConfig=null;
	private  List<RuleOfrResourceRel> allRuleOfrResourceRel = new ArrayList<RuleOfrResourceRel>(); 
	private Map<String,String> relsMap=new HashMap<String,String>();
	
	public RatableResourceUpdate(){}
	
	public void updateRatableResourceValue(){
		if(dbConfig==null){
			dbConfig=(DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
			allRuleOfrResourceRel=dbConfig.getAllRuleOfrResourceRel();
			relsMap=dbConfig.getRelsMap();
		}
		RatableCondInParam iCondInParam=new RatableCondInParam();
		RatableCondCheck rcc=new RatableCondCheck();
		boolean isFirstMonth=false;
		boolean isFirstMonth2=false;
		long lnSessionTotalCharge = 0;
		boolean bHave=false;
		int nBillingFlag=ratingMsg.getM_nBillingFlag();
		int moneyType;
		int lifeType=1;
		String strRefType;
		int refOffset=0;
		int startValue=0;
		int endValue=0;
		int ratableResourceType;
		long ratableValue=0;
		long ratableTotal=0;
		boolean isMoney=false;
		String ratableOwnerType;
		int ratableOwnerLandId;
		long ratableOwnerId;
		String strCurrTime = ratingMsg.getBaseMsg().getM_strStartTime();
		String yearMonth=strCurrTime.substring(0,10);
		String strAct;
		List<String> tmpRatables=new ArrayList<String>();
		List<InfoRatableHistory> his=new ArrayList<InfoRatableHistory>();
		
		List<String> iUsedRatable=new ArrayList<String>();
		int msgType=ratingMsg.getM_nMsgType();
		long lnRealDuration = ratingData.getLnReqDuration();
	    long lnRealTimes = ratingData.getLnReqTimes();
	    long lnRealUpVolume = ratingData.getLnReqUpVolume();
	    long lnRealDownVolume = ratingData.getLnReqDownVolume();
	    long lnRealTotalVolume = ratingData.getLnReqTotalVolume();
		
	    System.out.println("lnRealDuration:"+lnRealDuration);
	    
	    iCondInParam.setM_pRatingMsg(ratingMsg);
	    iCondInParam.setM_pRatingData(ratingData);
	    String time=ratingMsg.getBaseMsg().getM_strStartTime();
	    iCondInParam.setM_szYYYY(time.substring(0,4));
	    iCondInParam.setM_szMM(time.substring(4,6));
	    iCondInParam.setM_szDD(time.substring(6,8));
	    iCondInParam.setM_szHH(time.substring(8,10));
	    iCondInParam.setM_szMI(time.substring(10,12));
	    
	    ratingData.setiChargeUnitsForRatable(ratingData.getiRealChargeUnits());
	    
	    List<ChargeUnit> chargeUnitsForRatable=ratingData.getiChargeUnitsForRatable();
//	    System.out.println("length>>>"+chargeUnitsForRatable.size());
	    
	    //是否激活首月(与会话开始时间比较)
	    if(ratingMsg.getM_iServInfo().getStrAcceptDate().substring(0,6).equals(ratingMsg.getBaseMsg().getM_strStartTime().substring(0, 6))){
	    	isFirstMonth=true;
	    	
	    }else{
	    	
	    	isFirstMonth=false;
	    }
	    
//	    System.out.println("chargeUnitsForRatable.size:"+chargeUnitsForRatable.size());
//	    chargeUnitsForRatable.get(0).print();
	    
	    for(ChargeUnit cu : chargeUnitsForRatable){
	    	if(cu.getnRatableFlag()==1){
	    		lnSessionTotalCharge+=cu.getLnOriCharge();
	    	}else{
	    		lnSessionTotalCharge+=cu.getLnCharge();
	    	}
	    	
	    }
	    
//	    System.out.println("length>>>"+ratingData.getiPlanDiscts().size());
	    for(PlanDisct plan  :  dinnerConversion.getiAtomPlanDiscts()){
	    	iUsedRatable.clear();
//	    	iUsedRatable=getRatableResourceValue(msgType,plan.getnPricingPlanId()); ////基础套餐
	    	int atomId=plan.getnAtomOfrId();
	    	iUsedRatable=getRatableResourceCodes(msgType,plan.getnAtomOfrId());
//	    	System.out.println("pricingplan>>>"+plan.getnPricingPlanId());
	    	if(iUsedRatable.isEmpty())
	    		continue;
	    	if(plan.getStrEffDate().substring(0,6).compareTo(ratingMsg.getVarMsg().getM_strCurrTime())==0){
	    		isFirstMonth2=true;
	    	}else{
	    		isFirstMonth2=false;
	    	}
	    	if(ratingMsg.getM_iServInfo().getStrBasicState()=="F0A"){
	    		isFirstMonth2=true;
	    	}
	    	
	    	
	    	for(String code :iUsedRatable){
	    		RatableResourceInfo info=resource.getiRatableResourceInfos().get(code);
	    		if(info==null)
	    			continue;
	    		lifeType=Integer.parseInt(info.getM_strLifeType());
	    		strRefType=info.getM_strRefType();
	    		refOffset=info.getM_nRefOffset();
	    		startValue=info.getM_nStartValue();
	    		endValue=info.getM_nEndValue();
	    		ratableOwnerType=info.getM_strRatableOwnerType();
	    		ratableResourceType=Integer.parseInt(info.getM_strRatableResourceType());
	    		
	    		isMoney=isMoneyRatableType(ratableResourceType);
	    		
	    		yearMonth=getYearMonth(plan.getStrEffDate(),strCurrTime,lifeType,startValue,endValue);
	    		
	    		
	    		if(ratableOwnerType.equals("80C")){
	    			ratableOwnerId = plan.getLnOfrInstId();
	                ratableOwnerLandId = plan.getnOfrInstLatnId();
	    		}else{
	    			Iterator<String> itTmp=tmpRatables.iterator();
	    			boolean find=false;
	    			while(itTmp.hasNext()){
	    				String value=itTmp.next();
	    				if(value.equals(code)){
	    					find=true;
	    					break;
	    				}
	    			}
	    			if(!find){
	    				tmpRatables.add(code);
	    			}else{
	    				continue;
	    			}
	                

	    			String latnAndOwner=getLatnAndOwnerId( ratableOwnerType);
	    		    String strs[] =latnAndOwner.split(",");
	    		    ratableOwnerId=Long.parseLong(strs[1]);
	    		    ratableOwnerLandId=Integer.parseInt(strs[0]);
	    		}    
	    		    bHave=true;
//	    		    System.out.println("iRatableResourceValues.size:"+ratingData.getiRatableResourceValues().size());
	    		    RatableResourceValue rrv=resource.getiRatableResourceValues().get(code);
//	    		    Set<String> set=ratingData.getiRatableResourceValues().keySet();
//	    		    for(String str:set){
//	    		    	System.out.println("value's code:"+str);
//	    		    }
	    		    ratableTotal=rrv.getM_lnBalance();
	    		    System.out.println("code:"+code+",ratableTotal:"+ratableTotal);
	    		    
	    		    long lnTempRatableValue = 0;
	                lnTempRatableValue = getRatableValue( ratableResourceType, lnRealDuration, lnRealTimes, lnRealUpVolume, lnRealDownVolume, lnRealTotalVolume );
	                
	                moneyType=getMoneyTypeFromRatableType( ratableResourceType);//0
	    		    
	                for(ChargeUnit  unit :chargeUnitsForRatable){
	                	iCondInParam.setM_nPricingSectionId(unit.getLnPricingSectionId());
	                    iCondInParam.setM_nPlanIdA(unit.getnPlanId());
	                    iCondInParam.setM_nPlanIdB(unit.getnPlanId());
	                    iCondInParam.setM_lnOfrInstIdA(unit.getLnOfrInstId());
	                    iCondInParam.setM_lnOfrInstIdB(unit.getLnOfrInstId());
	                    if(rcc.check(code, iCondInParam)!=0){
	                    	continue;
	                    }
	                    
	                    if(!isMoney){
	                    	String ratableCode=unit.getStrRatableCode();
	                    	int beforDosage=unit.getLnBeforeDosage();
	                    	int nTmpRatableResourceType = getRatableResourceUnit( unit.getStrRatableCode() );
	                        if( nTmpRatableResourceType == -1 ){
	                            System.out.println("未查询到此累积量代码["+unit.getStrRatableCode()+"]");
	                        }
	                        if( nTmpRatableResourceType == ratableResourceType ){
	                            if( nBillingFlag == RatingMacro.EVENT_BACK )
	                                ratableValue -= calcRatableResourceValue( ratableResourceType, unit.getLnBeforeDosage() );
	                            else
	                                ratableValue += calcRatableResourceValue( ratableResourceType, unit.getLnBeforeDosage() );
	                        }else{
	                            ratableValue = lnTempRatableValue;//申请量
	                            break;
	                        }

	                        continue;
	                    }
	                    
	                    if( unit.getnRatableFlag() == 1 ){
	                        if( unit.getLnOriCharge() <= 0 )
	                            continue;
	                    }else{
	                        if( unit.getLnCharge() <= 0 )
	                            continue;
	                    }
	                    
	                    if( isMoney && unit.getnUnitType()== moneyType ){
	                        if( unit.getnRatableFlag() == 1 ){
	                            ratableValue += unit.getLnOriCharge();
	                        }else{
	                            if( unit.getLnLeftMoney() > 0 )
	                                ratableValue += unit.getLnOriCharge();
	                            else
	                                ratableValue += unit.getLnCharge();
	                        }
	                    }
	                    
	                }
	    		    if(isMoney){
	    		    	
	    		    }else{
	    		    	
	    		    }
	    		    
	    		    InfoRatableHistory  irh=new InfoRatableHistory();
	    		    Ratable ratable=new Ratable();
	    		    irh.setRes_his_id(UUID.randomUUID().toString());
	    		    irh.setOwner_type(ratableOwnerType);
	    		    irh.setOwner_id(ratableOwnerId);
	    		    irh.setLatn_id(ratableOwnerLandId);
	    		    irh.setResource_code(code);
	    		    irh.setAcct_month(Long.parseLong(yearMonth.substring(0,8)));
	    		    irh.setValue(""+ratableValue);
	    		    
	    		    his.add(irh);
	    		    
//	    		    ratableTotal += ratableValue;
//
//	    		    ratable.setStrRatableCode(code);
//	    		    ratable.setnRatableType(ratableResourceType);
//	    		    ratable.setLnValue(ratableValue);
//	    		    ratable.setLnRatableValue(ratableTotal);
//	    		    ratable.setStrAcctDate(yearMonth);
//	    		    ratable.setLnOwnerID(ratableOwnerId);
//	    		    ratable.setnOwnerLatnId(ratableOwnerLandId);
//	    		    ratable.setStrOwnerType(ratableOwnerType);
//	    		    ratingData.getiRatableRefs().add(ratable);
	    		    
	    		    System.out.println("ratableValue:"+ratableValue);
	    		    
	    		}
	    		
	    	}
	    	for(PlanDisct plan  :  dinnerConversion.getiAtomPlanDiscts()){
	    		
	    	}
	    	updateRatableHistory(his);
	    }
	    
	    
	    
		
		
	
	
	
	public void updateRatableHistory(List<InfoRatableHistory> his){
		DbUtil db=new DbUtilImpl();
		for(InfoRatableHistory irh :his){
			if(db.queryRatableHistory(irh)==null){
				db.addRatableHistory(irh);
			}else{
//				System.out.println("累积量更新:"+irh.getResource_code()+":"+irh.getValue());
				db.updateRatableHistory(irh);
			}
		}
		
	}
	
	
	public long calcRatableResourceValue( int nRatableResourceType, long dosage ){

	    switch( nRatableResourceType ){
	        case 1: //时长(秒)
	            return dosage;
	        case 2: //时长(分钟)
	            return ( dosage+59 )/60; //nRealDuration 单位是秒
	        case 3: //次数
	            return dosage;
	        case 4: //总流量
	            return dosage;
	        case 7: //上行流量按K
	            return dosage;
	        case 8: //下行流量
	            return dosage;
	        default:    //其他计算资源,暂不考虑
	            break;
	    }
	    return 0;
	}
	
	
	
	
	public int getRatableResourceUnit(String code){
		CodeRatableResource info=dbConfig.getSumInfoMap().get(code);
		if(info !=null){
			return info.getRatable_resource_type();
		}else
			return -1;
	}
	
	
	public int getMoneyTypeFromRatableType( int nRatableType ){
	    int nType=0;
	    switch ( nRatableType ){
	        case RatingMacro.RatableResourceType_Money://金钱
	            nType=2;
	            break;
	        case RatingMacro.RatableResourceType_M://M
	            nType=7;
	            break;
	        case RatingMacro.RatableResourceType_T://T
	            nType=8;
	            break;
	    }
	    return  nType;

	}
	
	
	
	
	
	public long  getRatableValue( int nRatableResourceType, long lnRealDuration, long lnRealTimes, long lnRealUpVolume, long lnRealDownVolume, long lnRealTotalVolume ){

	    switch( nRatableResourceType ){
	        case 1: //时长(秒)
	            return lnRealDuration;
	        case 2: //时长(分钟)
	            return ( lnRealDuration+59 )/60; //nRealDuration 单位是秒
	        case 3: //次数
	            return lnRealTimes;
	        case 4: //总流量
	            return lnRealTotalVolume;
	        case 7: //上行流量按K
	            return lnRealUpVolume;
	        case 8: //下行流量
	            return lnRealDownVolume;
	        default:    //其他计算资源,暂不考虑
	            break;
	    }
	    return 0;
	}

	
	
	
	
	public String getLatnAndOwnerId(String ownerType) {
		String ownerId="";
		String latn;
		if(ownerType.equals(ParamData.OWNERTYPE_CUSTOMER)){
			//从消息中获取客户ID
			ownerId=""+ratingMsg.getM_iUserMsg().getLnCustId();
			latn=""+ratingMsg.getM_iUserMsg().getnLatnId();
		}else if(ownerType.equals(ParamData.OWNERTYPE_ACCOUNT)){
			//从消息中获取帐户ID及latn
			ownerId=""+ratingMsg.getM_iUserMsg().getLnAcctId();
			latn=""+ratingMsg.getM_iUserMsg().getnLatnId();
		}else if(ownerType.equals(ParamData.OWNERTYPE_SELLINST)){
			//从消息中获取销售品实例ID
			ownerId=""+ratingMsg.getM_iUserMsg().getLnOfrInstId();
			latn=""+ratingMsg.getM_iUserMsg().getnOfrInstLatnId();
		}else{
			//消息中得到servId
			ownerId=ratingMsg.getM_iUserMsg().getLnServId();
			latn=""+ratingMsg.getM_iUserMsg().getnLatnId();
		}
		return latn+","+ownerId;
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



	public boolean isMoneyRatableType(int type){
		
		if(type==RatingMacro.RatableResourceType_Money || type==RatingMacro.RatableResourceType_M
				|| type==RatingMacro.RatableResourceType_T){
			
			return true;
		}else
			return false;
			
		
	}
	
	public List<String>  getRatableResourceCodes(int msgType,int pricingPlan){
		List<String> ratable=new ArrayList<String>();
		
		String key=""+pricingPlan+msgType;
		String value=relsMap.get(key);
		if(value !=null)
			ratable.add(value);
		
		return ratable;
	}
	
	
	
}
