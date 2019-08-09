package com.tydic.beijing.bvalue;

import java.text.ParseException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.QueryGiveOutBValueImpl;


public class QueryGiveOutBValueMain {
	
	private static Logger log=Logger.getLogger(QueryGiveOutBValueMain.class);

	public static void main(String args[]) throws ParseException{
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"queryGiveOutBValue.xml"});
		context.start();
		log.debug("service QueryGiveOutBValue start ....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
// 	  JSONObject inputjson = new JSONObject();
//	  inputjson.put("JDPin", "testz");
//	  inputjson.put("OrderNo", "1312313212");
//	  
//	  QueryGiveOutBValueImpl queryGiveOutBValueImpl = (QueryGiveOutBValueImpl) context.getBean("queryGiveOutBValueImpl");
//	  JSONObject outputJson = queryGiveOutBValueImpl.query(inputjson);
//	  log.debug("返回消息:"+outputJson.toString());
	
	  
	}
}
