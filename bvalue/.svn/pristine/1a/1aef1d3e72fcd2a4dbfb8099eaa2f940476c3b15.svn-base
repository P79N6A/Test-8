package com.tydic.beijing.bvalue.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.dao.InfoBpool;
import com.tydic.beijing.bvalue.service.QueryInfoBPoolCRM;

public  class QueryInfoBPoolCRMImpl implements QueryInfoBPoolCRM {
	private static Logger log=Logger.getLogger(QueryInfoBPoolCRMImpl.class);
	/**
	 * BOSS系统查询B值
	 */
	
	public JSONObject queryInfoBPoolCRM(JSONObject inputJson){
		JSONObject retJson = new JSONObject();
		DbTool dbTool = new DbTool();
		int pagecount =0;
		
		log.debug("inputJson==>"+inputJson.toString());
		try {
			 if(!checkFormat(inputJson)){
				 retJson.put("Status", "0"); //失败
				 retJson.put("ErrorCode", -99999);
				 retJson.put("ErrorMessage", "格式异常");
				 return retJson;
			 }
			
			int pageIndex = inputJson.getInt("PageIndex");
			int rowPerPage =  inputJson.getInt("RowPerPage");
			int minNum = (pageIndex-1) * rowPerPage;
			int maxNum = pageIndex * rowPerPage;
			
//			String strEndTime = inputJson.getString("EndTime");
//			strEndTime = strEndTime + " 23-59-59";
			List<InfoBpool> resultBActivity = new ArrayList<InfoBpool>();
//			if(inputJson.containsKey("DepartMentType")&&inputJson.get("DepartMentType").equals("")&&
//				inputJson.containsKey("ActivityName")&&inputJson.get("ActivityName").equals("")&&
//				inputJson.containsKey("ActivityType")&&inputJson.get("ActivityType").equals("")&&
//				inputJson.containsKey("CreateStaff")&&inputJson.get("CreateStaff").equals("")&&
//				inputJson.containsKey("CreateTime")&&!inputJson.get("CreateTime").equals("")&&
//				inputJson.containsKey("StartTime")&&inputJson.get("StartTime").equals("")&&
//				inputJson.containsKey("EndTime")&&inputJson.get("EndTime").equals("")&&	
//				inputJson.containsKey("ThreStatus")&&inputJson.get("ThreStatus").equals("")&&	
//				inputJson.containsKey("Status")&&inputJson.get("Status").equals("")	
//					
//			)
//			{
//				List<InfoBpool> listBActivity=dbTool.getAllInfoBPoolCRM();
//				log.debug("get listBActivity ,size=>"+listBActivity.size());
//				
//				int index=0;
//				for(InfoBpool tamp:listBActivity){
//					index++;
//					if(index > minNum &&
//							index <=maxNum){
//						resultBActivity.add(tamp);
//						
//					}
//					//如果pageindex=1,需要查询页数
//					if(inputJson.getLong("PageIndex") == 1L){
//						pagecount = index / rowPerPage;
//						if(index > rowPerPage * pagecount){
//							pagecount++;
//						}
//						log.debug("一共有===>"+pagecount+"页");
//						retJson.put("PageCount", pagecount);
//					}		
//				}
//				
//			}
//			else
//			{
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat mysqlSdf = new SimpleDateFormat("yyyyMMdd");
			
				StringBuffer condition=new StringBuffer(" where ");
				if(inputJson.containsKey("DepartMentType")&&!inputJson.getString("DepartMentType").equals("")){
					condition.append("DepartMent_Type="+"'"+inputJson.getString("DepartMentType")+"'"+" and ");
				}
				if(inputJson.containsKey("ActivityName")&&!inputJson.getString("ActivityName").equals("")){
				condition.append("BPool_Name="+"'"+inputJson.getString("ActivityName")+"'"+" and ");
				}
				if(inputJson.containsKey("ActivityType")&&!inputJson.getString("ActivityType").equals("")){
					condition.append("Activity_Type="+"'"+inputJson.getString("ActivityType")+"'"+" and ");
				}
				if(inputJson.containsKey("CreateStaff")&&!inputJson.getString("CreateStaff").equals("")){
					condition.append("Create_Staff="+"'"+inputJson.getString("CreateStaff")+"'"+" and ");
				}
				if(inputJson.containsKey("CreateTime")&&!inputJson.getString("CreateTime").equals("")){
					String createTime = inputJson.getString("CreateTime");
					condition.append("Create_Time >= str_to_date('"+DateUtil.changeToInternalFormat(createTime).substring(0,8)+"','%Y%m%d%H%i%s')   and ");
					condition.append("Create_Time <= str_to_date('"+(DateUtil.changeToInternalFormat(createTime).substring(0,8)+"235959")+"','%Y%m%d%H%i%s')   and ");
				}
				if(inputJson.containsKey("StartTime")&&!inputJson.getString("StartTime").equals("")){
					String startTime = inputJson.getString("StartTime");
					startTime = mysqlSdf.format(sdf.parse(startTime))+"000000";
					condition.append("eff_date=str_to_date('"+startTime+"','%Y%m%d%H%i%s')   and ");
				}
				if(inputJson.containsKey("EndTime")&&!inputJson.getString("EndTime").equals("")){
					String endTime = inputJson.getString("EndTime");
					endTime = mysqlSdf.format(sdf.parse(endTime))+"235959";
					condition.append("exp_date= str_to_date('"+endTime+"','%Y%m%d%H%i%s') and ");
				}
				if(inputJson.containsKey("ThreStatus")&&!inputJson.getString("ThreStatus").equals("")){
					condition.append("Thre_Status="+"'"+inputJson.getString("ThreStatus")+"'"+" and ");
				}
				if(inputJson.containsKey("Status")&&!inputJson.getString("Status").equals("")){
					condition.append("Activity_Status="+"'"+inputJson.getString("Status")+"' and ");
				}
				if(inputJson.containsKey("ActivityId") && !inputJson.getString("ActivityId").equals("")){
					condition.append("bpool_id="+"'"+inputJson.getString("ActivityId")+"' and ");
				}
				condition.append(" 1=1 ");
				int startNum = (pageIndex-1)*rowPerPage+1;
                int endNum = pageIndex*rowPerPage;
			//	condition.append(" limit "+ startNum +","+endNum +";");
				
				log.debug("拼接sql为"+condition);
				 
				List<InfoBpool> listBActivity=dbTool.getqueryInfoBPoolCRM(condition);
				log.debug("get listBActivity ,size=>"+listBActivity.size());
				
//				int index=0;
//				for(InfoBpool tamp:listBActivity){
//					if(inputJson.containsKey("ActivityName")&&!inputJson.get("ActivityName").equals(tamp.getbpool_name())){
//						throw new BValueException(BValueErrorCode.ERR_REQUEST_PARAM,"查询参数错误");
//					}else if(inputJson.containsKey("DepartMentType")&&!inputJson.get("DepartMentType").equals(tamp.getdepartMent_type())){
//						throw new BValueException(BValueErrorCode.ERR_REQUEST_PARAM,"查询参数错误");
//					}else if(inputJson.containsKey("ActivityType")&&!inputJson.get("ActivityType").equals(tamp.getactivity_type())){
//						throw new BValueException(BValueErrorCode.ERR_REQUEST_PARAM,"查询参数错误");
//					}else if(inputJson.containsKey("Status")&&!inputJson.get("Status").equals(tamp.getactivity_status())){
//						throw new BValueException(BValueErrorCode.ERR_REQUEST_PARAM,"查询参数错误");
//					}
//					index++;
//					if(index > minNum &&
//							index <=maxNum){
//						resultBActivity.add(tamp);
//						
//					}
//					//如果pageindex=1,需要查询页数
//					if(inputJson.getLong("PageIndex") == 1L){
//						pagecount = index / rowPerPage;
//						if(index > rowPerPage * pagecount){
//							pagecount++;
//						}
//						log.debug("一共有===>"+pagecount+"页");
//						retJson.put("PageCount", pagecount);
//					}	
//				}
			//}
			    
				JSONArray jsonArray = new JSONArray (); 
			for(int i=1;i<=listBActivity.size();i++){
				
				if(i< startNum || i > endNum){
					continue;
				}
				
				InfoBpool tampl = listBActivity.get(i-1);
				
				JSONObject bactivityDto = new JSONObject();  //accessLogDto
				
				bactivityDto.put("ActivityId",tampl.getbpool_id());
				bactivityDto.put("ActivityName",tampl.getbpool_name());
				bactivityDto.put("DepartMentType",tampl.getdepartMent_type());
				bactivityDto.put("ActivityType",tampl.getactivity_type());
				bactivityDto.put("StartTime", tampl.geteff_date());
				bactivityDto.put("EndTime",tampl.getexp_date());
				bactivityDto.put("Pwd",tampl.getpwd());
				bactivityDto.put("BPool",tampl.getbpool());
				bactivityDto.put("CostValue",tampl.getcost_value());
				bactivityDto.put("UsedBalance", tampl.getused_balance());
				bactivityDto.put("Balance", tampl.getbalance());
				bactivityDto.put("Status", tampl.getactivity_status());
				bactivityDto.put("Threshold", tampl.getthreshold());
				bactivityDto.put("ThreStatus", tampl.getthre_status());
				bactivityDto.put("CreateTime", tampl.getcreate_time());
				bactivityDto.put("CreateStaff", tampl.getcreate_staff());
				bactivityDto.put("ContactPhone", tampl.getcontact_phone());
				bactivityDto.put("ContactEmail", tampl.getcontact_email());
				bactivityDto.put("Note", tampl.getnote());
				jsonArray.add(bactivityDto);
				
				
				}
			log.debug("jsonArray.size===>"+jsonArray.size());
			retJson.put("Status", "1");
			retJson.put("ErrorCode", "");
			retJson.put("ErrorMessage", "");
			retJson.put("ActivityDtoList", jsonArray);
			
			if(pageIndex==1){
				retJson.put("PageCount", Math.ceil(1.0*listBActivity.size()/rowPerPage) );
			}
			
	   	log.debug("retJson==>"+retJson.toString());
			
		}  catch (Exception e) {
			e.printStackTrace();
			retJson.put("Status", "0"); //失败
			retJson.put("ErrorCode", -99999);
			retJson.put("ErrorMessage", "查询异常");
		}
		
		
		return retJson;
	}
	private boolean checkFormat(JSONObject string)  {
		try {
			if(string.containsKey("DepartMentType")&&!string.get("DepartMentType").equals("1")&&
					!string.get("DepartMentType").equals("2")&&!string.get("DepartMentType").equals("")){
				throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"操作类型编码错误");
			}
			if(string.containsKey("ActivityType")&&!string.get("ActivityType").equals("1")&&
					!string.get("ActivityType").equals("2")&&!string.get("ActivityType").equals("3")&&!string.get("ActivityType").equals("")){
				throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"操作类型编码错误");
			}
			if(string.containsKey("ThreStatus")&&!string.get("ThreStatus").equals("1")&&
					!string.get("ThreStatus").equals("2")&&!string.get("ThreStatus").equals("3")&&!string.get("ThreStatus").equals("")){
				throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"操作类型编码错误");
			}
			if(string.containsKey("Status")&&!string.get("Status").equals("1")&&
					!string.get("Status").equals("2")&&!string.get("Status").equals("")){
				throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"操作类型编码错误");
			}
			
			if(!string.containsKey("PageIndex")){
				throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"页码不能为空");
			}
			
			if(!string.containsKey("RowPerPage")){
				throw new BValueException(BValueErrorCode.ERR_INPUT_FORMAT,"每页数量不能为空");
			}
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		
		
		
	}
	
}
