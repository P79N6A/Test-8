package com.tydic.beijing.bvalue.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import net.sf.json.JSONObject;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.CapitalizedPropertyNamingStrategy;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.LifeResourceList;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchange;
import com.tydic.beijing.bvalue.dto.ExchangeDto;
import com.tydic.beijing.bvalue.dto.ResourceDto;
import com.tydic.beijing.bvalue.service.QueryAutoExchange;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class QueryAutoExchangeImpl implements QueryAutoExchange{

	private static Logger log = Logger.getLogger(QueryAutoExchangeImpl.class);
	
	private DbTool db;
	
	@Override
	public JSONObject query(JSONObject request) {

		log.debug("请求参数>>>>>>>>>>>>:"+request.toString());
		JSONObject json=new JSONObject();
		json.put("Status", Constants.Result_Error);
		json.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
		json.put("ErrorMessage", "查询参数错误");
		String jdpin="";
		String mobileNumber="";
		try{
			jdpin=request.getString("JDPin");
			mobileNumber=request.getString("MobileNumber");
			if(jdpin==null||jdpin.isEmpty()||mobileNumber==null ||mobileNumber.isEmpty()){
			
				log.debug("ERR>>>>>>>>>>>>>"+json.toString());
				return json;
			}
		
		}catch(Exception e){
			log.debug("ERR>>>>>>>>>>>>>"+json.toString());
			
			return json;
		}
		
		
		JSONObject jerror=new JSONObject();
		jerror.put("Status", Constants.Result_Error);
		jerror.put("ErrorCode", BValueErrorCode.ERR_SYSTEM_ERR);
		jerror.put("ErrorMessage", "系统异常");
		
		String userId=Common.md5(jdpin);
		log.debug("查询JDPin["+jdpin+"],USERID["+userId+"]");
		
		if(!checkPhoneNumber(userId,mobileNumber)){
			jerror.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
			jerror.put("ErrorMessage", "查询参数错误,请核对手机号码及绑定关系");
			log.debug("ERR>>>>>>>>"+jerror.toString());
			return jerror ;
		}
		
		String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		List<LifeUserAutoExchange> exchanges=db.queryAutoExchangeSet(userId, currentTime);	//查询有效的
		
		if(exchanges == null || exchanges .isEmpty()){
			JSONObject js= new JSONObject();
			js.put("Status", Constants.Result_Succ);
			js.put("ErrorCode", "");
			js.put("ErrorMessage", "查询无自动兑换数据");
			log.debug("return  empty   >>>>"+js.toString());
			return js;
		}
		
//		List<LifeResourceList> resources=db.queryExchangeResourceList(userId);
		List<LifeResourceList> resources=new ArrayList<LifeResourceList>();
		
		for(LifeUserAutoExchange iter : exchanges){
			List<LifeResourceList> resource=db.queryExchangeResourceList(userId,iter.getResource_list_id());
			
			if(resource !=null && !resource.isEmpty()){
				for(LifeResourceList it : resource){
					resources.add(it);
				}
			}
			
		}
		
		
		
		List<ExchangeDto> exchangeDtos= new ArrayList<ExchangeDto>();
		
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		for(LifeUserAutoExchange iter : exchanges ){
			ExchangeDto dto=new ExchangeDto();
			dto.setExchageID(iter.getExchange_id());
			String exm=iter.getPurchase_mode();
			String exg="";
			if(exm.equals(Constants.PURCHASE_MODE_ALL)){
				exg="1";
			}else if(exm.equals(Constants.PURCHASE_MODE_TOP)){
				exg="2";
			}
			dto.setExchangeBMode(exg);
			dto.setTopBValue(iter.getTop_b_value());
			String srm=iter.getExchange_mode();
//			String erm="";
//			if(srm.equals(Constants.EXCHANGE_MODE_FIXED)){	//按固定值
//				erm="1";
//			}else if(srm.equals(Constants.EXCHANGE_MODE_PERCENT)){	//按比例
//				erm="2";
//			}
			dto.setExchangeResourceMode(srm); 	// 1 固定值     2 比例

			
			dto.setEffDate(iter.getEff_date());
			dto.setExpDate(iter.getExp_date());
			
			try {
				String reserve=getReserveTag(iter.getEff_date(),iter.getExp_date());
				if(reserve == null){
					log.error("自动兑换实例["+iter.getExchange_id()+"],生失效时间异常[eff:"+iter.getEff_date()+"],exp:["+iter.getExp_date()+"]");
					continue;
				}
				dto.setReserveTag(reserve);
				
			} catch (ParseException e) {
				log.error("日期转换异常");
				continue;
			}
			
			List<ResourceDto> list=new ArrayList<ResourceDto>();
			
			for(LifeResourceList it : resources){
				if(it.getResource_list_id().equals(iter.getResource_list_id())){
					String resourceType=it.getResource_type_code();
					String resTypeCode="";
					if(resourceType.equals(Constants.Resource_Type_Code_Money)){
						resTypeCode="0";
					}else if(resourceType.equals(Constants.Resource_Type_Code_Voice)){
						resTypeCode="1";
					}else if(resourceType.equals(Constants.Resource_Type_Code_Flow)){
						resTypeCode="2";
					}else if(resourceType.equals(Constants.Resource_Type_Code_Sms)){
						resTypeCode="3";
					}
					ResourceDto rd=new ResourceDto(resTypeCode,it.getResource_value(),"");
					list.add(rd);
				}
			}
			dto.setResourceDtoList(list);
			
			exchangeDtos.add(dto);
		}
		ObjectMapper mapper=new ObjectMapper();
		mapper.setPropertyNamingStrategy(new CapitalizedPropertyNamingStrategy());
		try {
			String dtos=mapper.writeValueAsString(exchangeDtos);
			JSONObject result= new JSONObject();
			result.put("Status", Constants.Result_Succ);
			result.put("ErrorCode", "");
			result.put("ErrorMessage", "");
			result.put("JDPin", jdpin);
			result.put("MobileNumber", mobileNumber);
			result.put("ExchangeDtolist", dtos);
			log.debug("exchangeDtolist:"+dtos);
			return result;
		} catch (JsonGenerationException e) {
			log.debug(e.getMessage());
			return jerror;
		} catch (JsonMappingException e) {
			log.debug(e.getMessage());
			return jerror;
		} catch (IOException e) {
			log.debug(e.getMessage());
			return jerror;
		}
		
		
		
	}

	
	public void setDb(DbTool db) {
		this.db = db;
	}
	
	String getReserveTag(String eff,String exp) throws ParseException{
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date effDate=sdf.parse(eff);
		Date expDate=sdf.parse(exp);
		Date now=Calendar.getInstance().getTime();
		if(effDate.after(expDate) || expDate.before(now)){
			return null;
		}
		if(effDate.before(now)){
			return Constants.RESERVETAG_N;
		}else{
			return Constants.RESERVETAG_Y;
		}
		
		
	}
	
	boolean checkPhoneNumber(String userId , String phone){
		if(!Common.isMobileNumber(phone)){
			return false;
		}
		
		String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		InfoUserExternalAccount external=db.queryPinRelation(userId ,currentTime);
		if(external==null){
			return false;
		}
		
		String number=external.getExternal_account_code();
		if(!phone.equals(number)){
			return false;
		}
		
		return true;
	}
	
	
}
