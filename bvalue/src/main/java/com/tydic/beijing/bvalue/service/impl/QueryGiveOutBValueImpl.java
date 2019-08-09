package com.tydic.beijing.bvalue.service.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LogTradeRewardHis;
import com.tydic.beijing.bvalue.service.QueryGiveOutBValue;

public class QueryGiveOutBValueImpl implements QueryGiveOutBValue {
	
	
	private static Logger log=Logger.getLogger(QueryGiveOutBValueImpl.class);
	@Autowired
	private DbTool dbTool;

	@Override
	public JSONObject query(JSONObject inputJson) {
		
		JSONObject outJson = new JSONObject();
		
		log.debug("QueryGiveOutBValue接口入参:"+inputJson.toString());
		
		try {
			if(!check(inputJson)){
				outJson.put("Status", "0");
				outJson.put("ErrorCode", "-10050");
				outJson.put("ErrorMessage", "参数异常");
			}
			
			String jdpin = inputJson.getString("JDPin");
			String userId = Common.md5(jdpin);
			String orderNo = inputJson.getString("OrderNo");
			
			List<LogTradeHis> listTradeHis = dbTool.getLogTradeHisByUserIdAndOrderNo(userId, orderNo);
			
			outJson.put("Status", "1");
			outJson.put("ErrorCode", "");
			outJson.put("ErrorMessage", "");
			
			JSONArray rewardArray= new JSONArray();
			for(LogTradeHis tmplth:listTradeHis){
				
				List<LogTradeRewardHis> logTradeRewardHisList = dbTool.queryLogTradeRewardHis(userId,tmplth.getTrade_id());
				for(LogTradeRewardHis tmpltrd:logTradeRewardHisList){
					JSONObject rewardInfo = new JSONObject();
					rewardInfo.put("JDPin",jdpin );
					rewardInfo.put("BValue", tmplth.getBalance());
					rewardInfo.put("OrderNo", orderNo);
					rewardInfo.put("PlatName", tmpltrd.getPlat_name());
					rewardInfo.put("ProcessTime", tmplth.getTrade_time());
					rewardInfo.put("ActivietyId", tmpltrd.getBpool_id());
					
					rewardArray.add(rewardInfo);
				}
				
				outJson.put("RewardInfoDtoList", rewardArray);
	
			}
	 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outJson.put("Status", "0");
			outJson.put("ErrorCode", "-10056");
			outJson.put("ErrorMessage", "接口异常");
		}
		
	 
		
		return outJson;
	}

	private boolean check(JSONObject inputJson) {
		
		if(!inputJson.containsKey("OrderNo")){
			log.error("OrderNo必填");
			return false;
		}
		if(!inputJson.containsKey("JDPin")){
			log.error("JDPin必填");
			return false;
		}
		return true;
	}

}
