package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuerySubsCDRMain {

	
	private final static Logger log = Logger.getLogger(QuerySubsCDRMain.class);
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "subsCdrQuery.xml" });
		context.start();
		log.debug("service QuerySubsCDR start successful......");
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
	}
}
