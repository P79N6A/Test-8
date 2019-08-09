package com.tydic.beijing.bvalue.service.impl;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class BatchAdjustBValueProcess {

	@Autowired
	private AdjustBValueImpl adjustBValueImpl;
	
	private static Logger log=Logger.getLogger(BatchAdjustBValueProcess.class);
	
	 @Transactional(rollbackFor=Exception.class)
	public void perform(JSONObject inputJson) throws Exception{
		 
		 long starttime = System.currentTimeMillis();
		 JSONObject retJson = adjustBValueImpl.perform(inputJson);
		 
		 long endtime = System.currentTimeMillis();
		 log.debug("用户["+inputJson.getString("JDPin")+"]耗时=="+(endtime-starttime));
		 
		
		if(!retJson.getString("Status").equals("1")){
			
			throw new Exception ("调整失败,错误编码=["+retJson.getString("ErrorCode")+"],错误描述=["+retJson.getString("ErrorMessage")+"]");
		}
	}

	public AdjustBValueImpl getAdjustBValueImpl() {
		return adjustBValueImpl;
	}

	public void setAdjustBValueImpl(AdjustBValueImpl adjustBValueImpl) {
		this.adjustBValueImpl = adjustBValueImpl;
	}
	
	 
	 
	 
}
