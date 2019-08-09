package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.dto.impl.DbUtilImpl;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;


public class RateCondCheck {

	private RateConditionParam param;
//	private static DbUtil db=new DbUtilImpl();
	@Autowired
	private ApplicationContextHelper applicationContextHelper;
	private DbConfigDetail dbConfig=null;
	private static Map<String,CodeRecordType> codeRecordMap=new HashMap<String,CodeRecordType>();
	
	private static Map<Integer , List<RuleRateCondition>> condMap=new HashMap<Integer ,List<RuleRateCondition>>();
	
	
	public boolean check(){
		if(dbConfig==null){
			dbConfig=(DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
			codeRecordMap=dbConfig.getCodeRecordMap();
			condMap=dbConfig.getCondMap();
		}
		if(param.getM_nCondId()==-1){
			return true;
		}
		List<RateCond> iConds=new ArrayList<RateCond>();
		List<RuleRateCondition> conds=condMap.get(param.getM_nCondId());
		CodeRecordType code=null;
		for(RuleRateCondition condition :conds){
			RateCond r=new RateCond(condition);
			code=codeRecordMap.get(condition.getItem_code());
			r.setM_strSourceType(code.getSource_type());
			r.setM_strValueType(code.getValue_type());
			iConds.add(r);
		}
		if(iConds.isEmpty()){
			return false;
		}
		
		long nLastGroupId = -1; //上一组的ID
		boolean nSatisfyFlag = true; //标识上一规则是否满足
//		int nRet = 0;
	    // char cSourceType = 0;
	    String cValueType ;
	    String strValueA;
	    String strValueB;
		
	    nLastGroupId = iConds.get(0).getM_lnGroupId();
	    for(RateCond iter :iConds){
	    	if(iter.getM_lnGroupId()!= nLastGroupId){
	    		if(nSatisfyFlag){
	    			return true;
	    			
	    		}
	    		nSatisfyFlag=true;
	    	}
	    	//第一字段
	    	strValueA=getValue(iter.getM_strSourceType().charAt(0),iter.getM_strItemCode());
	    	
	    	cValueType=iter.getM_strValueType();
	    	//第二字段
	    	if(iter.getM_strComType()==RatingMacro.SectionCond_Compare_Type){
	    		strValueB=iter.getM_strItemValue();
	    	}else{
	    		strValueB=getValue(iter.getM_strSourceType().charAt(0),iter.getM_strItemValue());
	    	}
	    	
	    	if(!compare(strValueA,strValueB,iter.getM_nComOperators(),cValueType)){
	    		nSatisfyFlag=false;
	    	}
	    	
	 }
	    
		
		return true;
	}
	
	
	
	private String getValue(char type,String code){
		String value="";
		RatingMsg ratingMsg=param.getM_pRatingMsg();
		String strCurrentTime=ratingMsg.getAllSessionStartTimes();
		Map<String,String> iServAttrMap=ratingMsg.getM_iRatingExtMsg().getM_iServAttrMap();
		switch(type){
		case '1': //话单
			value=ratingMsg.getValue(code, 1);
			break;
		case '2':	//客户的静态属性
		case '4':	//产品属性
			value=iServAttrMap.get(code);
			break;
		case '3':	//客户的动态属性(累积量)
			RatableResourceValue rv=param.getM_iRatables().get(code); 
			if(rv !=null){
				value=""+rv.getM_lnBalance();
			}
			break;
		case '6':
			RatableResourceValue trv=param.getM_iTmpRatables().get(code);
			if(trv !=null){
				value=""+trv.getM_lnBalance();
			}
			break;
		case '9':
			if(code.substring(0,3).equals("OCH")){
				value=""+param.getM_lnOriCharge();
				
			}else if(code.substring(0,3).equals("VPN")){
				value=""+param.getM_nGroupFlag();
			}else if(code.subSequence(0,5).equals("RYEAR")){
				value=strCurrentTime.substring(0,4);
			}else if(code.subSequence(0,4).equals("RMON")){
				value=strCurrentTime.substring(4,6);
			}else if(code.subSequence(0,4).equals("RDAY")){
				value=strCurrentTime.substring(6,8);
			}else if(code.subSequence(0,5).equals("RHOUR")){
				value=strCurrentTime.substring(8,10);
			}else if(code.subSequence(0,4).equals("RMIN")){
				value=strCurrentTime.substring(10,12);
			}else if(code.subSequence(0,5).equals("CTIME")){
				value=strCurrentTime;
			}else if(code.subSequence(0,5).equals("STIME")){
				value=strCurrentTime;
			}
			break;
		}
		return value;
	}
	
	
	
	
	private boolean compare(String valueA,String valueB,int oper ,String valueType){
		if(valueType.toLowerCase().equals("c")){
			return compareString(valueA,valueB,oper);
		}else{
			return compareValue(valueA,valueB,oper);
			
		}
		
	}
	
	
	
	private boolean compareString(String valueA,String valueB,int oper){
		switch(oper){
		case 0:
			return true;
		case 10:	// ==
			return valueA.equals(valueB);
		case 20:	// !=
			return !valueA.equals(valueB);
		case 30:	//包含
			return valueA.indexOf(valueB) !=-1;
		case 31:    //不包含
			return valueA.indexOf(valueB)==-1;
		}
		return false;
	}
	
	
	private boolean compareValue(String valueA,String valueB,int oper){
		long aValue=Long.parseLong(valueA);
		long bValue=Long.parseLong(valueB);
		switch(oper){
		case 10:	//==
			return aValue==bValue;
		case 20:	//!=
			return aValue !=bValue;
		case 40:	//>
			return aValue >bValue;
		case 41:	// >=
			return aValue >=bValue;
		case 50:	//<
			return aValue <bValue;
		case 51:	// <=
			return aValue <=bValue;
			
			
		}
		
		return false;
	}
	
	
	
	
	
	
	
	
	
//	static{
//		recordTypes=db.getAllCodeRecordType();
//		for(CodeRecordType code:recordTypes){
//			codeRecordMap.put(code.getRecord_code(), code);
//		}
//		conds=db.getAllRuleRateCondition();
//		for(RuleRateCondition iter :conds){
//			if(condMap.containsKey((int)iter.getCond_id())){
//				condMap.get(iter.getCond_id()).add(iter);
//			}else{
//				List<RuleRateCondition> l=new ArrayList<RuleRateCondition>();
//				l.add(iter);
//				condMap.put((int)iter.getCond_id(), l);
//			}
//		}
//	}
	
	
	public RateConditionParam getParam() {
		return param;
	}
	
	public void setParam(RateConditionParam param) {
		this.param = param;
	}
	
	
	
}
