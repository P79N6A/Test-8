/**
 * 
 */
package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.dto.impl.DbUtilImpl;
import com.tydic.beijing.billing.rating.service.ErrorInfo;

/**
 * @author sung
 *
 */
public class RatableCondCheck {
	private RatableCondInParam m_pParam=new RatableCondInParam();
	private  static  Map<String ,List<RatableResourceCond>> ratableResourceConds=new HashMap<String ,List<RatableResourceCond>>();
	private  static Map<String,CodeRecordType> sourceMap=new HashMap<String,CodeRecordType>();
	
	
	
	
	
	public int check(String resourceCode,RatableCondInParam param){
	
		m_pParam=param;
		long lnLastGroupId = -1; //上一组的ID
		int nSatisfyFlag = 0; //标志上一规则是否满足
	    String cValueType = "";
	    String strValueA="";
	    String strValueB="";

	    List<RatableResourceCond> iConds=new ArrayList<RatableResourceCond>();
	    
	    iConds=getRatableResourceConds(resourceCode);
	    if( iConds==null  )
	        return 0;
		
	    for(RatableResourceCond c : iConds){
	    	 if( lnLastGroupId < 0 )
	             lnLastGroupId = c.getM_lnGroupId();

	         if( lnLastGroupId != c.getM_lnGroupId() ){
	             if( nSatisfyFlag == 0 )
	                 return 0; //上一规则满足

	             nSatisfyFlag = 0;
	             lnLastGroupId = c.getM_lnGroupId();
	         }else{ //如果相等,判断多seq的问题,并列关系,如果一个不为0,则需要判断是否还有别的或条件
	             if( nSatisfyFlag != 0 )
	                 continue;
	         }
	         //第一字段
	         if((strValueA=getValue(c.getM_strItemCode()))==null){
	        	 return -1;
	         }
	         //取出值类型(数字还是字母)
	         if( (cValueType=getRecordTypeValue(c.getM_strItemCode()))==null){
	        	 cValueType = "c";
	         }
	         //第二字段
	         if( c.getM_strComType().equals("10") )
	             strValueB = c.getM_strItemValue(); //字段与值比较
	         else{
	             if( (strValueB = getValue(c.getM_strItemValue())) ==null )
	                 return -1;
	         }
	         
	         if(compareAB(strValueA,strValueB,c.getM_nComOperators(),cValueType)==false){
	        	 nSatisfyFlag = ErrorInfo.ERR_FAIL_COM_SOURCE_CODE;
	         }
	         
	    }
		
	    if( nSatisfyFlag == 0 )
	        return 0;
	    
	    
	     return ErrorInfo.ERR_SQL_QUERY_RATABLE_COND;
	    
	    
	}
	
	
	private String getRecordTypeValue(String code){
		
		return sourceMap.get(code).getValue_type();
	}
	
	
	private boolean compareAB(String valueA,String valueB,int comp,String oper){
		//字符型比较,默认比较类型
        if ( oper.toLowerCase() .equals("c") ){
            return compareString( valueA, valueB, comp );
        }else{
            return compareValue( valueA, valueB, comp );
        }

     
		
	}
	
	
	
	private boolean compareValue(String valueA, String valueB  ,int comp){
		switch(comp){
		case 10:
			return Integer.parseInt(valueA)==Integer.parseInt(valueB);
			
		case 20:
			return Integer.parseInt(valueA)!=Integer.parseInt(valueB);
			
		case 40:	//>
			return Integer.parseInt(valueA) > Integer.parseInt(valueB);
		
		case 41:	//>=
			return Integer.parseInt(valueA) >= Integer.parseInt(valueB);
			
		case 50:	//<
			return Integer.parseInt(valueA) < Integer.parseInt(valueB);
		case 51:	//<=
			
			return Integer.parseInt(valueA) <= Integer.parseInt(valueB);
		}
		
		return false;
	}
	//简化版
	private boolean compareString(String valueA, String valueB , int comp){
		
		switch(comp){
		case 0:
				return true;
		case 10:		//=
			return valueA.equals(valueB);
		case 20:		// !=
			return !valueA.equals(valueB);
		case 30:
			return valueA.indexOf(valueB)>=0;
			
		}
		return false;
	}
	
	
	private String getValue(String code){
		
	    RatingMsg pRatingMsg = m_pParam.getM_pRatingMsg();
	    Map<String, String> iServAttrMap = pRatingMsg.getM_iRatingExtMsg().getM_iServAttrMap();
	    String cSourceType="";
	    String szTmp="";
	    String strCurrentTime = pRatingMsg.getAllSessionStartTimes();
	    cSourceType=sourceMap.get(code).getSource_type();
	    if(null==cSourceType){
	    	return null;
	    }
	    
	    char type=cSourceType.charAt(0);
	    switch(type){
	    case '1':    //话单
	    	szTmp=pRatingMsg.getValue(code, 1);
	    	break;
	    case '2':		//客户静态属性
	    case '4':		//产品属性
	    	String tmp=iServAttrMap.get(code) ;
	    	if(null !=tmp){
	    		szTmp=tmp;
	    	}
	    	break;
	    case '3':	//客户动态属性(累积量)
	    	RatableResourceValue rrv=m_pParam.getM_pRatingData().getiRatableResourceValues().get(code);
	    	if(rrv !=null){
	    		szTmp=""+rrv.getM_lnBalance();
	    		
	    	}
	    	break;
	    case '9':
	    	if(code.substring(0,3).equals("OCH")){
	    		szTmp=""+m_pParam.getM_lnTariff();
	    		
	    	}else if(code.substring(0,3).equals("VPN")){
	    		szTmp=""+m_pParam.getM_nGroupFlag();
	    	}else if(code.substring(0,5).equals("RYEAR")){
	    		szTmp=strCurrentTime.substring(0,4);
	    	}else if(code.substring(0,4).equals("RMON")){
	    		szTmp = strCurrentTime.substring(5, 7);
	    	}else if(code.substring(0,4).equals("RDAY")){
	    		szTmp = strCurrentTime.substring(7, 9);
	    	}else if(code.substring(0,5).equals("RHOUR")){
	    		szTmp = strCurrentTime.substring(9, 11);
	    	}else if(code.substring(0,4).equals("RMIN")){
	    		szTmp = strCurrentTime.substring(11, 13);
	    	}else if(code.substring(0,4).equals("PSID")){
	    		szTmp=""+m_pParam.getM_nPricingSectionId();
	    	}else if(code.substring(0,4).equals("PIDA")){
	    		szTmp=""+m_pParam.getM_nPlanIdA();
	    	}else if(code.substring(0,4).equals("PIDB")){
	    		szTmp=""+m_pParam.getM_nPlanIdB();
	    	}else if(code.substring(0,5).equals("OFINA")){
	    		szTmp=""+m_pParam.getM_lnOfrInstIdA();
	    	}else if(code.substring(0,4).equals("OFINB")){
	    		szTmp=""+m_pParam.getM_lnOfrInstIdB();
	    	}
	    	break;
	    }
	    return szTmp;
	}


	
	
	
	
	private  List<RatableResourceCond> getRatableResourceConds(String resourceCode){
		List<RatableResourceCond>   conds=ratableResourceConds.get(resourceCode);
		return conds;
	}
	
	
	static{
		List<RuleRatableCond> rules=new ArrayList<RuleRatableCond>();
		DbUtil db=new DbUtilImpl();
		rules=db.getRatableConds();
		for(RuleRatableCond   cond  :rules){
			String resourceCode=cond.getResource_code();
			RatableResourceCond r=new RatableResourceCond(cond);
			if(ratableResourceConds.get(resourceCode)==null){
				
				List<RatableResourceCond> list=new ArrayList<RatableResourceCond>();
				list.add(r);
				ratableResourceConds.put(resourceCode, list);
			}else{
				List<RatableResourceCond> value=ratableResourceConds.get(resourceCode);
				if(!value.contains(resourceCode)){
					value.add(r);
				}
					
			}
			
		}
		List<CodeRecordType> l=db.getAllCodeRecordType();
		for(CodeRecordType r : l){
			sourceMap.put(r.getRecord_code(), r);
		}
		
	}
	
	
	
	
	
}
