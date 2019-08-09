package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QueryCurrentConsumeMain {

	private final static Logger log = Logger.getLogger(QueryCurrentConsumeMain.class);
	
	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "currentConsumeQuery.xml" });
		context.start();
		log.debug("service QueryCurrentConsume start successful.........");
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
		
		
	}
	
	
	
	
}
