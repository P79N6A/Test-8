package com.tydic.beijing.billing.interfacex;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.impl.ReceiveCDRForJDNImpl;
import com.tydic.beijing.billing.interfacex.service.impl.ReceiveCDRImpl;


public class ReceiveCDRofJDNMain {
	private static Logger log = Logger.getLogger(ReceiveCDRofJDNMain.class);

	public static void main(String[] args) {
		log.debug("============进入容联话单实时接口============");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"receiveJDNCdr.xml" });
		
		context.start( );

		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
//		ReceiveCDRForJDNImpl receivecdr = (ReceiveCDRForJDNImpl) context.getBean("receiveCDRForJDNImpl");
//		
//		JSONObject inputJson = new JSONObject();
//		inputJson.put("CallId","1212121212re" );
//		inputJson.put("CdrType","1" );
//		inputJson.put("CdrSubType","2" );
//		inputJson.put("CallingNbr","891891891" );
//		inputJson.put("CalledNbr","00909" );
//		inputJson.put("SessionBeginTime","112323");
//		inputJson.put("SessionEndTime","323" );
//		inputJson.put("Duration", "1" );
//
//
//		receivecdr.receive(inputJson);
//		
		
		
//		log.debug(message, t);
	}
}	
	

