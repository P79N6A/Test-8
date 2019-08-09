package com.tydic.beijing.billing.interfacex.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.dao.BalanceAdjustLog;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.beijing.billing.dao.UserPayInfo;
import com.tydic.beijing.billing.dto.ResourceAdjustRequest;
import com.tydic.beijing.billing.dto.ResourceAdjustResponse;
import com.tydic.beijing.billing.dto.ResourceDto;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.ResourceAdjust;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;


public class ResourceAdjustImpl implements ResourceAdjust {
	
	private static Logger log = Logger.getLogger(ResourceAdjustImpl.class);
	

	
	@Autowired
	private ResourceAdjustProcess resourceAdjustProcess;
	
	@Override
	public   ResourceAdjustResponse doProcess(ResourceAdjustRequest rar)  {
		
		
		ResourceAdjustResponse resourceAdjustResponse = new ResourceAdjustResponse();
		
		try {
			//resourceAdjustResponse = resourceAdjustProcess.dealSftp(rar);
			resourceAdjustResponse = dealAdjust(rar);
		} catch (Exception e) {
			resourceAdjustResponse.setStatus("0");
			resourceAdjustResponse.setErrorCode("ZSMART-CC-10000");
			resourceAdjustResponse.setErrorMessage("系统异常");
		}
		
		
		return resourceAdjustResponse;
		
	}
	
	
	public ResourceAdjustResponse dealAdjust(ResourceAdjustRequest rar) throws Exception {
		ResourceAdjustResponse tmprar = new ResourceAdjustResponse();
		
		try{
			tmprar = resourceAdjustProcess.dealSftp(rar);
		} catch (BasicException be){
			if(be.getCode() ==1001){
				tmprar = dealAdjust(rar);
			} else {
				tmprar.setStatus("0");
				tmprar.setErrorCode("ZSMART-CC-10000");
				tmprar.setErrorMessage("系统异常");
			}
			
		} catch (Exception e){
			tmprar.setStatus("0");
			tmprar.setErrorCode("ZSMART-CC-10000");
			tmprar.setErrorMessage("系统异常");
		}
		
		return tmprar;
	}
	
	
	
	

	

}
