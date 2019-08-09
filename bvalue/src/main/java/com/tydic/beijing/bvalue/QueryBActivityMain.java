package com.tydic.beijing.bvalue;

import java.text.ParseException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.QueryBActivityImpl;


public class QueryBActivityMain {
	
	private static Logger log=Logger.getLogger(QueryBActivityMain.class);

	public static void main(String args[]) throws ParseException{
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"QueryBActivity.xml"});
		context.start();
		log.debug("service QueryBActivity start ....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
// 	  JSONObject inputjson = new JSONObject();
//	  inputjson.put("ActivityId", "0bc827984265429799c4e4902d1dbfb5");
//	  inputjson.put("Pwd", "123");
//	  inputjson.put("ActivityType", "2");
//	  
//	  QueryBActivityImpl queryBActivityImpl = (QueryBActivityImpl) context.getBean("queryBActivityImpl");
//	  JSONObject outputJson = queryBActivityImpl.query(inputjson);
//	  log.debug("返回消息:"+outputJson.toString());
	
	  
	}
}
