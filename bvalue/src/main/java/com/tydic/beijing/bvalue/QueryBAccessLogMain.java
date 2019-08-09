package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class QueryBAccessLogMain {
	
	private static Logger log=Logger.getLogger(QueryBAccessLogMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"queryBAcctAccessLog.xml"});
		context.start();
		log.debug("service QueryBAccessLogMain start ....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
	}
}
