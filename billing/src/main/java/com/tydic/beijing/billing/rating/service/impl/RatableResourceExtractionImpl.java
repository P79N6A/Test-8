package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.rating.domain.CodeRatableResource;
import com.tydic.beijing.billing.rating.domain.InfoRatableHistory;
import com.tydic.beijing.billing.rating.domain.ParamData;
import com.tydic.beijing.billing.rating.domain.PlanDisct;
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
import com.tydic.beijing.billing.rating.service.DinnerConversion;
import com.tydic.beijing.billing.rating.service.RatableResourceExtraction;
import com.tydic.beijing.billing.rating.service.RatingException;
import com.tydic.beijing.billing.rating.util.DateUtil;

public class RatableResourceExtractionImpl implements RatableResourceExtraction{

	private Logger log=Logger.getLogger(RatableResourceExtractionImpl.class);
	
	
	private DinnerConversionImpl dinnerConvertion;
	
	private RatingMsg ratingMsg;
	private DbConfigDetail  dbConfig=null;
	private RatingData ratingData;  //没用
	@Autowired
	private ApplicationContextHelper applicationContextHelper;
	
	//全部数据
	private  List<InfoRatableHistory> ratableHistory=new ArrayList<InfoRatableHistory>();
	private  List<RuleOfrResourceRel> rels=new ArrayList<RuleOfrResourceRel>();
	private  Map<String ,CodeRatableResource> sumInfoMap=new HashMap<String,CodeRatableResource>();
	
	
	//结果
	private Map<String, RatableResourceInfo> iPlanRatableResourceInfos = new HashMap<String,RatableResourceInfo>(); // key:
	private Map<String, RatableResourceValue> iRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:ratable_resource_code
	private Map<String, RatableResourceInfo> iRatableResourceInfos=new HashMap<String,RatableResourceInfo>(); // key:
	private Map<String, RatableResourceValue> iAllRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:ratable_resource_code
	
	
	public RatableResourceExtractionImpl(RatingMsg msg,RatingData data){
		this.ratingData=data;
		this.ratingMsg=msg;
	}
	
	
	public RatableResourceExtractionImpl(RatingMsg msg,DinnerConversionImpl dinner){
//		this.ratingData=data;
		this.ratingMsg=msg;
		this.dinnerConvertion=dinner;
	}

	
	@Override
	public boolean queryRatableResourceValue() throws RatingException{
	
		if(dbConfig==null){
			dbConfig=(DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
//			ratableHistory=dbConfig.getRatableHistory();
			rels=dbConfig.getRels();
			sumInfoMap=dbConfig.getSumInfoMap();
		}

		String msgType=""+ratingMsg.getM_nMsgType();
//		List<PlanDisct> atomsList=ratingData.getiAtomPlanDiscts();
		
		List<PlanDisct> atomsList=dinnerConvertion.getiAtomPlanDiscts();
		
		PlanDisct atom=null;
		String code="";
		RatableResourceInfo codeInfo=null;
		CodeRatableResource info=null;
		Iterator<PlanDisct> it=atomsList.iterator();
		boolean find;
		while(it.hasNext()){
			atom=it.next();
			find=false;
			for(RuleOfrResourceRel sum:rels){
				
				if(atom.getnAtomOfrId()==sum.getOfr_B_Id() && msgType.equals(sum.getMsg_Types())){
					code=sum.getResource_Code();
//					resourceCode.add(code);
					
					if(!code.equals("-1") && !code.isEmpty()){
						find=true;
					
					info = sumInfoMap.get(code);
					if (info != null) {
//						resourceInfo.add(info);
						codeInfo = new RatableResourceInfo(info);
//						ratingData.getiPlanRatableResourceInfos().put(""+atom.getnAtomOfrId(), codeInfo);
						iPlanRatableResourceInfos.put(""+atom.getnAtomOfrId(), codeInfo);
						
						
					} else {
						return false;
					}
				
//					String startTime=ratingMsg.getM_iRatingExtMsg().getM_strExtStartTime();
//					String currTime=ratingMsg.getM_iRatingExtMsg().getM_strExtCurrTime();
//					String ofrEffDate=atom.getStrEffDate();////销售品生效日期
//					getSumValue(info,ofrEffDate,currTime);
					break; 
					}
				}
			}
			if(!find){
//				ratingData.getiRatableResourceValues().put(""+atom.getnAtomOfrId(), new RatableResourceValue());
				iRatableResourceValues.put(""+atom.getnAtomOfrId(), new RatableResourceValue());
				continue;
			}
			ratingMsg.getM_iUserMsg().setLnOfrInstId(atom.getLnOfrInstId());
			ratingMsg.getM_iUserMsg().setnOfrInstLatnId(atom.getnOfrInstLatnId());
			if(!iRatableResourceInfos.containsKey(code)){
				iRatableResourceInfos.put(code, codeInfo);
				
			}
//			String currTime=ratingMsg.getM_iRatingExtMsg().getM_strExtCurrTime();
			String ofrEffDate=atom.getStrEffDate();
			RatableResourceValue value=queryRatableResource(codeInfo,ofrEffDate);
//			if(value != null)
			iRatableResourceValues.put(code, value);
//			else	
//				ratingData.getiRatableResourceValues().put(code, new RatableResourceValue());
			
		}

		return true;
		
		
		
	}
	
	
	private RatableResourceValue queryRatableResource(RatableResourceInfo  info ,String effDate){
//		info.print();
		
		RatableResourceValue value=new RatableResourceValue();
		int nRet = 0;
	    boolean bNeedSecondInterval = false;
	    int nSecondInterval = 0;
	    RatableResourceValue iValue;
//	    String strCurrTime = ratingMsg.getM_iRatingExtMsg().getM_strExtCurrTime();///本次计费请求开始时间   修改获取:
	    String strCurrTime = ratingMsg.getBaseMsg().getM_strStartTime();
	    System.out.println("计费请求开始时间:"+strCurrTime);
	    String strStartTime = ratingMsg.getM_iRatingExtMsg().getM_strExtStartTime();////会话开始时间
	    String strYearMonth = strCurrTime.substring( 0, 10 );
	    int nRatableResourceType = Integer.parseInt( info.getM_strRatableResourceType() );////累积量类型
	    int nLifeType = Integer.parseInt( info.getM_strLifeType() );////生存期类型
	    int nMsgType = ratingMsg.getM_nMsgType();
	    int nLatnId;
	    String lnOwnerId;
	    int nStartValue = info.getM_nStartValue();
	    int nEndValue = info.getM_nEndValue();
	    String strOwnerType = info.getM_strRatableOwnerType();////累积量属主类型
	    String strRatableResourceCode = info.getM_strRatableResourceCode();
	    if( !strStartTime.isEmpty() && nMsgType == RatingMacro.CODE_IN_BRR )
	    	nSecondInterval=(int)DateUtil.getIntervalSeconds(strStartTime, strCurrTime);
	    bNeedSecondInterval = isNeedSecondInterval( nRatableResourceType );
	    if( nRatableResourceType == RatingMacro.RatableResourceType_Minute )
	        nSecondInterval = ( nSecondInterval + 59 )/60;
	    
	    strYearMonth=getYearMonth(effDate, strCurrTime, nLifeType, nStartValue, nEndValue);
	    if(strYearMonth.isEmpty()){
	    	return value;
	    }
	    System.out.println("\t日期:"+strYearMonth);
	    String latnOwner=getLatnAndOwnerId(strOwnerType);
	    String strs[] =latnOwner.split(",");
	    lnOwnerId=strs[1];
	    
	    nLatnId=Integer.parseInt(strs[0]);
	    value.setM_strRatableResourceCode(strRatableResourceCode);
	    value.setM_strOwnerType(strOwnerType);
	    value.setM_lnOwnerId(lnOwnerId);
	    value.setM_nLatnId(nLatnId);
	    value.setM_lnBalance(0);
	    
	    iValue=value;
	    boolean bHasRecord = false;
	    long lnCycleId = 0;
	    String szCycleId="";
	    int nRefOffset = info.getM_nRefOffset();
	    InfoRatableHistory query=null;
	    if( info.getM_strRefType().equals(RatingMacro.REFTYPE_SINGLE )){ //取单一值
	        if( nLifeType == RatingMacro.LifeType_Month ) {//把当月的全部加起来
	            strYearMonth=changeToNthMonth( strYearMonth, nRefOffset ); ////加nRefOffset个月   yyyyMMdd
//	            strYearMonth = strYearMonth.substring( 0, 6 );

	            lnCycleId = Long.parseLong(strYearMonth);

	            query = queryRatableResourceHistory( strRatableResourceCode, strOwnerType, lnOwnerId, nLatnId, lnCycleId);
//	            if( query != null ){
	            	System.out.println("\t查询累积量值(单一值"+RatingMacro.REFTYPE_SINGLE+"):["+query.getValue()+"]\n");
	            	
	                bHasRecord = true;
	                value.setM_lnBalance(value.getM_lnBalance()+Long.parseLong(query.getValue()));
	                
	                iValue.setM_lnBalance(Long.parseLong(query.getValue()));
	                
	                iValue.setM_lnRatableCycleId((int)lnCycleId);
//	                ratingData.getiAllRatableResourceValues().put(iValue.getM_strRatableResourceCode(), iValue);
	                iAllRatableResourceValues.put(iValue.getM_strRatableResourceCode(), iValue);
	                
//	            }else{
//	            	System.out.println("未查询到累积量值");
//	            }
	        }else{
	            if( nLifeType != RatingMacro.LifeType_HalfYear ){
	                strYearMonth=changeToNthDay( strYearMonth, info.getM_nRefOffset() );////日
//	                strYearMonth = strYearMonth.substring( 0, 8 );
	            }

	            lnCycleId = Long.parseLong( strYearMonth );

	            query = queryRatableResourceHistory( strRatableResourceCode, strOwnerType, lnOwnerId, nLatnId, lnCycleId );
	            
//	            if( query != null ){
	            	System.out.println("\t查询累积量值(单一值"+RatingMacro.REFTYPE_SINGLE+"):["+query.getValue()+"]\n");
	                bHasRecord = true;
	                value.setM_lnBalance(value.getM_lnBalance()+Long.parseLong(query.getValue()));
	                iValue.setM_lnRatableCycleId((int)lnCycleId);
	                iValue.setM_lnBalance(Long.parseLong(query.getValue()));
//	                ratingData.getiAllRatableResourceValues().put(iValue.getM_strRatableResourceCode(), iValue);
	                iAllRatableResourceValues.put(iValue.getM_strRatableResourceCode(), iValue);
	                
//	            }else{
//	            	
//	            }
	        }

//	        if( bHasRecord ){
//	            if( ratingMsg.getOper() == RatingMacro.OPER_REQ && bNeedSecondInterval )
//	                value.setM_lnBalance(value.getM_lnBalance()+nSecondInterval);
//	        }else{
//	            if( ratingMsg.getOper() == RatingMacro.OPER_REQ && bNeedSecondInterval )
//	                value.setM_lnBalance(nSecondInterval);
//	        }
	    }else{	//取平均或者累积和
	    	if( nLifeType == RatingMacro.LifeType_Month ){
	            strYearMonth=changeToNthMonth( strYearMonth, nRefOffset );

	            String szMonth=strYearMonth.substring(4,6) ;
	            String szDay = strYearMonth.substring(6,8) ;
	            int nStartMonth = Integer.parseInt( szMonth );
	            int nMonthCount = 0;
	            int nStartDay = Integer.parseInt( szDay );

	            //不包含当前账期
	            if( info.getM_strRefType() .equals(RatingMacro.REFTYPE_AV)  || info.getM_strRefType() .equals(RatingMacro.REFTYPE_TOTAL) )
	                nMonthCount = -nRefOffset;
	            else
	                nMonthCount = -nRefOffset + 1;   //包含当前账期

	            strYearMonth = strYearMonth.substring( 0, 4 );
	            for( int i=0; i<nMonthCount; ++i ){
//	            	szCycleId=strYearMonth+DateUtil.lengthTo(i+nStartMonth)+DateUtil.lengthTo(nStartDay);
	            	szCycleId=strYearMonth+DateUtil.lengthTo(i+nStartMonth);
	                lnCycleId = Long.parseLong( szCycleId );

	                query = queryRatableResourceHistory( strRatableResourceCode, strOwnerType, lnOwnerId, nLatnId, lnCycleId );
//	                if( query != null ){
	                	
	                    bHasRecord = true;
	                    value.setM_lnBalance(value.getM_lnBalance()+Long.parseLong(query.getValue()));
	                    iValue.setM_lnRatableCycleId((int)lnCycleId);
	                    iValue.setM_lnBalance(Long.parseLong(query.getValue()));
//	                    ratingData.getiAllRatableResourceValues().put(iValue.getM_strRatableResourceCode(), iValue);
	                    iAllRatableResourceValues.put(iValue.getM_strRatableResourceCode(), iValue);
	                    
//	                }else{
//	                	
//	                }
	            }

	            if( bHasRecord ){
	                if( info.getM_strRefType().equals(RatingMacro.REFTYPE_AV) || info.getM_strRefType() .equals(RatingMacro.REFTYPE_TOTAL) ){
	                    value.setM_lnBalance(-( value.getM_lnBalance() / nRefOffset ));
	                    System.out.println("\t查询累积量值(平均值"+RatingMacro.REFTYPE_SINGLE+"):["+value.getM_lnBalance()+"]\n");
	                }
//	                if( ratingMsg.getOper() == RatingMacro.OPER_REQ )
//	                	value.setM_lnBalance(value.getM_lnBalance()+nSecondInterval);
	            }else{
//	                strYearMonth = strCurrTime.substring( 0, 8 );
//	            	strYearMonth = strCurrTime.substring( 0, 6 );
//	                lnCycleId =Long.parseLong( strYearMonth );
//	                if( updateRatableResource( strRatableResourceCode, strOwnerType, lnOwnerId, nLatnId, lnCycleId ) < 0 )
////	                    return -1;
//	                	return null;
	            }
	        }else{	//按天
	        	strYearMonth=changeToNthDay( strYearMonth, nRefOffset );
	            String szStart = strYearMonth.substring(6,8);
	            int nStart = Integer.parseInt( szStart );
	            int nCount = 0;
	            if( info.getM_strRefType() .equals(RatingMacro.REFTYPE_AV) || info.getM_strRefType() .equals(RatingMacro.REFTYPE_TOTAL) )
	                nCount = -nRefOffset;
	            else
	                nCount = -nRefOffset + 1;

	            strYearMonth = strYearMonth.substring( 0, 6 );
	            for( int i=0; i < nCount; ++i )
	            {
	                szCycleId=strYearMonth+DateUtil.lengthTo(i+nStart);
	                lnCycleId = Long.parseLong( szCycleId );

	                query = queryRatableResourceHistory( strRatableResourceCode, strOwnerType, lnOwnerId, nLatnId, lnCycleId );
	                
//	                if( query==null )
//	                    continue;

	                bHasRecord = true;
	                value.setM_lnBalance(value.getM_lnBalance()+Long.parseLong(query.getValue()));
	                
	                
	                iValue.setM_lnRatableCycleId((int)lnCycleId);
	                iValue.setM_lnBalance(Long.parseLong(query.getValue()));
//	                ratingData.getiAllRatableResourceValues().put(iValue.getM_strRatableResourceCode(), iValue);
	                iAllRatableResourceValues.put(iValue.getM_strRatableResourceCode(), iValue);
	                
	            }
	            if( bHasRecord ){
	                if( info.getM_strRefType() .equals(RatingMacro.REFTYPE_AV) || info.getM_strRefType() .equals(RatingMacro.REFTYPE_TOTAL) ){
	                    value.setM_lnBalance(value.getM_lnBalance()/(-nRefOffset));
	                    System.out.println("\t查询累积量值(平均值"+RatingMacro.REFTYPE_SINGLE+"):["+value.getM_lnBalance()+"]\n");
	                }
//	                if( ratingMsg.getOper() == RatingMacro.OPER_REQ && bNeedSecondInterval )
//	                	value.setM_lnBalance(value.getM_lnBalance()+nSecondInterval);
	            }else{
//	                strYearMonth = strCurrTime.substring( 0, 8 );
//	                lnCycleId = Long.parseLong( strYearMonth );
//	                if( updateRatableResource( strRatableResourceCode, strOwnerType, lnOwnerId, nLatnId, lnCycleId ) < 0 )
////	                    return -1;
//	                	return null;
	            }
	        }
	    	
	    }
	    value.setM_lnRatableCycleId((int)lnCycleId);
	    return value;
	}
	
	
	
	
	
	
	public int updateRatableResource(String ratableCode,String ownerType,String ownerId,int latn, long cycleId){
		InfoRatableHistory irh=new InfoRatableHistory();
		irh.setResource_code(ratableCode);
		irh.setOwner_type(ownerType);
		irh.setLatn_id(latn);
		irh.setAcct_month((int)cycleId);
		irh.setOwner_id(Long.parseLong(ownerId));
		irh.setValue("0");
//		String key=new SimpleDateFormat("yyyyMMddHH24mmssSSS").format(Calendar.getInstance().getTime());
//		irh.setRes_his_id(key);
		
		DbUtil db=new DbUtilImpl();
		
		db.addRatableHistory(irh);
		System.out.println("add ratable history...");
		return 0;
	}
	
	
	
	
	private String changeToNthDay(String date ,int offset){
		int len= date.length();
		if(len <8 || offset==0){
			return date;
		}
		
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(DateUtil.toDate(date));
		cal.add(Calendar.DAY_OF_MONTH, offset);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());
		
		
		
	}
	
	
	
	
	
	private InfoRatableHistory queryRatableResourceHistory(String code,String ownerType  ,String ownerId,int latn ,long cycle){
		System.out.println("\t查询累积量:code["+code+"],ownerType["+ownerType+
				"],ownerId["+ownerId+"],latn["+latn+"],cycle["+cycle+"]");
		
		ratableHistory=new DbUtilImpl().getAllRatableValue();
		InfoRatableHistory irh=new InfoRatableHistory();
		irh.setResource_code(code);
		irh.setOwner_type(ownerType);
		irh.setOwner_id(Long.parseLong(ownerId));
		irh.setLatn_id(latn);
		irh.setAcct_month(cycle);
		irh.setValue("0");
		irh.setRes_his_id(UUID.randomUUID().toString());
		for(InfoRatableHistory iter:ratableHistory){
			if(code.equals(iter.getResource_code()) && ownerType.equals(iter.getOwner_type()) && 
					ownerId.equals(""+iter.getOwner_id()) && latn==iter.getLatn_id() && cycle==iter.getAcct_month()){
				return iter;
			}else{
				
			}
		}
//		DbUtilImpl db=new DbUtilImpl();
//		db.addRatableHistory(irh);
		return irh;
	}
	
	
	private String changeToNthMonth(String date, int offset){
		int len=date.length();
		if(len< 6){
			return date;
		}
		if(offset==0 ){
			return date;
		}
		
		
		Calendar cal=Calendar.getInstance();
		cal.clear();
		cal.setTime(DateUtil.toDate(date.substring(0,6)+"01"));
		cal.add(Calendar.MONTH, offset);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		
	}
	
	
	
	public boolean isNeedSecondInterval(int resourceType){
		if(resourceType==RatingMacro.RatableResourceType_Second  || resourceType==RatingMacro.RatableResourceType_Minute){
			return true;
		}
		return false;
	}
	
	
	
	
//	public boolean getSumId() {  
//		String msgType=""+ratingMsg.getM_nMsgType();
//		List<PlanDisct> atomsList=ratingData.getiAtomPlanDiscts();
//		PlanDisct atom=null;
//		Iterator<PlanDisct> it=atomsList.iterator();
//		boolean find;
//		while(it.hasNext()){
//			atom=it.next();
//			find=false;
//			for(RuleOfrResourceRel sum:rels){
//				
//				if(atom.getnAtomOfrId()==sum.getOfr_B_Id() && msgType.equals(sum.getMsg_Types())){
//					String code=sum.getResource_Code();
////					resourceCode.add(code);
//					find=true;
//					
//					CodeRatableResource info = sumInfoMap.get(code);
//					if (info != null) {
////						resourceInfo.add(info);
//						RatableResourceInfo resourceInfo = new RatableResourceInfo(info);
//						ratingData.getiPlanRatableResourceInfos().put(""+atom.getnAtomOfrId(), resourceInfo);
////						if (!ratableResourceInfo.containsKey(code)) {
////							RatableResourceInfo resourceInfo = new RatableResourceInfo(info);
////							
////							ratableResourceInfo.put(code, resourceInfo);
////						}
//					} else {
//						return false;
//					}
//				
//					String startTime=ratingMsg.getM_iRatingExtMsg().getM_strExtStartTime();
//					String currTime=ratingMsg.getM_iRatingExtMsg().getM_strExtCurrTime();
//					String ofrEffDate=atom.getStrEffDate();////销售品生效日期
//					getSumValue(info,ofrEffDate,currTime);
//					break; 
//				}
//			}
//			
////			ratingMsg.getM_iUserMsg().setLnOfrInstId(atom.getLnOfrInstId());
////			ratingMsg.getM_iUserMsg().setnOfrInstLatnId(atom.getnOfrInstLatnId());
//			if(!find){
//				return false;
//			}
//			
//		}
////		if(resourceCode.isEmpty()){
////			System.out.println("未查询到累积量代码");
////			return false;
////		}
//		return true;
//	}

	
	

	
	
//	private boolean getSumValue(CodeRatableResource suminfo,String ofrEffDate,String currTime){
//		
////		String startTime=ratingMsg.getM_iRatingExtMsg().getM_strExtStartTime();
////		String currTime=ratingMsg.getM_iRatingExtMsg().getM_strExtCurrTime();
////		String ofrEffDate="20130205";//销售品生效日期
////		for(CodeRatableResource  suminfo:resourceInfo){
//			int resourceType=suminfo.getRatable_resource_type();
//			int lifeType=suminfo.getLife_type();
//			String ownerType=suminfo.getOwner_type();
//			int startValue=suminfo.getStart_value();
//			int endValue=suminfo.getEnd_value();
//			int latnId=ratingMsg.getM_iUserMsg().getnLatnId();
//			String yearMonth=getYearMonth(ofrEffDate,currTime,lifeType,startValue,endValue);
//			int cycle=Integer.parseInt(yearMonth);
////			String owner=getOwner(ownerType);
//			String owner=getLatnAndOwnerId(ownerType).split(",")[0];
//			String sumCode=suminfo.getRatable_resource_code();
//			RatableResourceValue sumValue=new RatableResourceValue(latnId,owner,cycle,0,ownerType,sumCode);
//			int value=getResourceValue(sumCode,ownerType,owner,latnId,cycle);//cycle暂未区分参考类型值
//			sumValue.setM_lnBalance(value);
//			
////			ratableResourceValues.put(sumCode, sumValue);
//			ratingData.getiRatableResourceValues().put(sumCode, sumValue);
//	
////		}
//		
//		return true;
//	}
	
	
	
//	private int getResourceValue(String resourceCode,String ownerType,String ownerId,int latnId,int cycleId){
//		int resourceValue=0;
//		for(InfoRatableHistory h:ratableHistory){
//			if(resourceCode==h.getResource_code() && ownerType==h.getOwner_type() && ownerId==""+h.getOwner_id()
//					&& latnId==h.getLatn_id()){
//				resourceValue=Integer.parseInt(h.getValue());
//			}
//		}
//		
//		return resourceValue;
//	}
//	
	
	
	
	
	
	
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

	public String getSumAcctDate() {
		
		return null;
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
	
	
	public boolean doSumOrNot(String now,String startValue,String endValue){
		
		return true;
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
	
	
	
//	public void setRatingData(RatingData ratingData) {
//		this.ratingData = ratingData;
//	}
//	
	
	public void setRatingMsg(RatingMsg ratingMsg) {
		this.ratingMsg = ratingMsg;
	}

	public Map<String, RatableResourceInfo> getiPlanRatableResourceInfos() {
		return iPlanRatableResourceInfos;
	}

	public void setiPlanRatableResourceInfos(
			Map<String, RatableResourceInfo> iPlanRatableResourceInfos) {
		this.iPlanRatableResourceInfos = iPlanRatableResourceInfos;
	}

	public Map<String, RatableResourceValue> getiRatableResourceValues() {
		return iRatableResourceValues;
	}

	public void setiRatableResourceValues(
			Map<String, RatableResourceValue> iRatableResourceValues) {
		this.iRatableResourceValues = iRatableResourceValues;
	}

	public Map<String, RatableResourceInfo> getiRatableResourceInfos() {
		return iRatableResourceInfos;
	}

	public void setiRatableResourceInfos(
			Map<String, RatableResourceInfo> iRatableResourceInfos) {
		this.iRatableResourceInfos = iRatableResourceInfos;
	}

	public Map<String, RatableResourceValue> getiAllRatableResourceValues() {
		return iAllRatableResourceValues;
	}

	public void setiAllRatableResourceValues(
			Map<String, RatableResourceValue> iAllRatableResourceValues) {
		this.iAllRatableResourceValues = iAllRatableResourceValues;
	}
	
	
	


	
}
