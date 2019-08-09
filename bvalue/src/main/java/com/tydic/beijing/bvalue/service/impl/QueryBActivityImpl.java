package com.tydic.beijing.bvalue.service.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.QueryBActivityMain;
import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.dao.InfoBpool;
import com.tydic.beijing.bvalue.service.QueryBActivity;
import com.tydic.beijing.bvalue.service.QueryTrade;

/**
 * @author zhanghengbo
 *
 */
public class QueryBActivityImpl implements QueryBActivity {

	@Autowired
	private DbTool dbTool;
	
	 
	private static Logger log=Logger.getLogger(QueryBActivityImpl.class);
	
	@Override
	public JSONObject query(JSONObject inputJson) {
		// 台账记录查询
		JSONObject outputJson = new JSONObject();

		try {			
			
			if(!inputJson.containsKey("ActivityId") 
					||!inputJson.containsKey("Pwd")
					|| !inputJson.containsKey("ActivityType")){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", BValueErrorCode.ERR_INPUT_FORMAT);
				outputJson.put("ErrorMessage", "参数必填项为空");
				return outputJson;
			}
			
			if(!inputJson.getString("ActivityType").equals("1") && !inputJson.getString("ActivityType").equals("2")){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", BValueErrorCode.ERR_INPUT_FORMAT);
				outputJson.put("ErrorMessage", "活动分类编码错误");
				return outputJson;
			}
			
			//根据活动ID查询活动明细
			String bPoolId=inputJson.getString("ActivityId");
			String Pwd=inputJson.getString("Pwd");
			
			List<InfoBpool> infoBPoolMessage=dbTool.getqueryInfoBPool(bPoolId);
			if(infoBPoolMessage.size()>0){
				log.debug("actionMessage,size="+infoBPoolMessage.size());
			}
			
			if(infoBPoolMessage.size()==0){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10020);
				outputJson.put("ErrorMessage", "没有该活动ID");
				return outputJson;
			}
			
			InfoBpool infobpool=infoBPoolMessage.get(0);
				if(inputJson.getString("Pwd")!=null &&inputJson.get("Pwd").equals(infobpool.getpwd())
						&&inputJson.getString("ActivityType")!=null && inputJson.getString("ActivityType").equals(infobpool.getactivity_type())
						){
					log.debug("活动密码匹配，密码为["+infobpool.getpwd()+"]");
					//获取b池余额
					for(InfoBpool info:infoBPoolMessage){
					long balance=info.getbalance();
					log.debug("balance==========================>"+balance);
					outputJson.put("Status", "1");
					outputJson.put("ErrorCode", "");
					outputJson.put("ErrorMessage", "");
					outputJson.put("Balance",balance);
					}
				}else{
					outputJson.put("Status", "0");
					outputJson.put("ErrorCode", -10021);
					outputJson.put("ErrorMessage", "密码错误");
					return outputJson;
				}

			
				
		} catch (Exception e) {
			e.printStackTrace();
			log.error("B值活动校验异常："+e.getMessage());
			outputJson.put("Status", "0");
			outputJson.put("ErrorCode", "-10000");
			outputJson.put("ErrorMessage", "接口异常");
		}
 
		log.debug("QueryBActivityImpl返回消息："+outputJson.toString());
		return outputJson;
	}

	 

}
