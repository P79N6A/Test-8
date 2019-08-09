package com.tydic.beijing.bvalue;


import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.tydic.beijing.bvalue.service.impl.CreateBValuePoolAccessImpl;



public class CreateBValuePoolMain {
	
	private static Logger log=Logger.getLogger(CreateBValuePoolMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"createBValuePoolAccess.xml"});
		context.start();
		log.debug("CreateBValueAccessMain start ....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
//		CreateBValuePoolAccessImpl createimpl = (CreateBValuePoolAccessImpl)context.getBean("createBValuePoolAccessImpl");
//		org.apache.commons.dbcp.BasicDataSource g;
//		
//	    JSONObject inputJson = new JSONObject();
//	    
////	    inputJson.put("OperType", "1");
////	    inputJson.put("ActivityId", "111000111000");
////	    inputJson.put("ActivityName", "kaixinyizhan");
////	    inputJson.put("DepartMentType", "1");
////	    inputJson.put("ActivityType", "3");
////	    inputJson.put("StartTime", "2015-04-01 00-00-00");
////	    inputJson.put("EndTime", "2015-04-25 23-59-59");
////	    inputJson.put("Pwd", "123");
////	    inputJson.put("BPool", 100);
////	    inputJson.put("CostValue", 20);
////	    inputJson.put("used_balance", 0);
////	    inputJson.put("balance", 100);
////	    inputJson.put("Threshold", 5);
////	    inputJson.put("ThreStatus", 1);
////	    inputJson.put("Status", 1);
////	    inputJson.put("CreateTime", "20150425235959");
////	    inputJson.put("CreateStaff", "");
////	    inputJson.put("ContactPhone", "15655671235");
////	    inputJson.put("activity_id", "000023");
////	    inputJson.put("jdpin", "13809042972_p");
////	    inputJson.put("bvalue", 0);
////	    inputJson.put("old_bpool", 100);
////	    inputJson.put("new_bpool", 100);
////	    inputJson.put("ContactEmail", "1234567@ty.com");
////	    inputJson.put("Note", "无");
////
////	    
////	    inputJson.put("TRADE_TYPE_CODE", "601");
////	    inputJson.put("PARTITION_ID", 3);
////	    inputJson.put("ORDER_NO", "");
////	    inputJson.put("PROCESS_TIME", "20150101000000");
////	    inputJson.put("PROCESS_TAG", 1);
////	    inputJson.put("RESERVE_C1", "");
////	    inputJson.put("RESERVE_C2", "");
////	    inputJson.put("RESERVE_C3", "");
////	    inputJson.put("RESERVE_C4", "");
//	    
//	    
//	    inputJson.put("OperType", "2");
//	    inputJson.put("ActivityId", "0000000001");
//	    inputJson.put("ActivityName", "卡卡");
//	    inputJson.put("DepartMentType", "1");
//	    inputJson.put("ActivityType", "3");
//	    inputJson.put("StartTime", "2015-04-25 00-00-00");
//	    inputJson.put("EndTime", "2015-05-12 23-59-59");
//	    inputJson.put("BPool", 5000);
//	    inputJson.put("CostValue", 800);
//	    inputJson.put("Threshold", 70);
//	    inputJson.put("Status", 1);
//	    inputJson.put("ContactPhone", "13567800256");
//	    inputJson.put("ContactEmail", "1234567@ty.com");
//	    inputJson.put("Note", "无");
//	    inputJson.put("PROCESS_TAG", "1");
//
//	    JSONObject retJson =  createimpl.createAccess(inputJson);
//	    
//	    log.debug("创建获得返回值=====>"+retJson.toString());

		
	}
}
