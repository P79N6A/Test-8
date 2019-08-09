package com.tydic.beijing.bvalue.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.bvalue.core.InfoUserDto;
import com.tydic.beijing.bvalue.dao.LogTrade;

public class BatchCreateUserProcess {
	
	@Autowired
	private InfoUserDto infoUserDto;
	
	private static Logger log=Logger.getLogger(BatchCreateUserProcess.class);
	
	@Transactional(rollbackFor=Exception.class)
	public void batchCreate(List<String> listjdpin) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String createTime = sdf.format(new Date());
		
		for(String tmp:listjdpin){
	        log.debug("本次创建用户["+tmp+"]");
	        
	        if(tmp.equals("user_log_acct")){
	        	continue;
	        }
	        
	        long starttime =System.currentTimeMillis();
			infoUserDto.createInfoUser(tmp, "110", createTime, new LogTrade());
			log.debug("开户耗时"+(System.currentTimeMillis()-starttime));
		}
 

	}
	
	 

}
