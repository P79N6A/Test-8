package com.tydic.beijing.bvalue;

import java.text.ParseException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.GiveOutBValueImpl;


public class GiveOutBValueMain {
	
	private static Logger log=Logger.getLogger(GiveOutBValueMain.class);

	public static void main(String args[]) throws ParseException{
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"giveOutBValue.xml"});
		context.start();
		log.debug("service GiveOutBValue start ....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
// 	  JSONObject inputjson = new JSONObject();
//	  inputjson.put("ActivityId", "20150711321166020445170897");
//	  inputjson.put("Pwd", "86039407");
//	  inputjson.put("JdPin", "testz");
//	  inputjson.put("BValue", "500");
//	  inputjson.put("OrderNo", "1312313212");
//	  inputjson.put("PlatName", "猜拳");
//	  inputjson.put("ChannelType", "110");
//	  
//	  GiveOutBValueImpl giveOutBValueImpl = (GiveOutBValueImpl) context.getBean("giveOutBValueImpl");
//	  JSONObject outputJson;
//	try {
//		outputJson = giveOutBValueImpl.giveOut(inputjson);
//		 log.debug("返回消息:"+outputJson.toString());
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	 
	
	  
	}
}
