package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QueryAutoExchangeMain {

	private static Logger log=Logger.getLogger(QueryAutoExchangeMain.class);
	
	
	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"queryAutoExchange.xml"});
		context.start();
		log.debug(">>>>>>>>>>>>>>>>>service QueryAutoExchange start successful....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
		
	}
}
