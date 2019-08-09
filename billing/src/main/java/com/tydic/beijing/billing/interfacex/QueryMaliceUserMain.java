package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public	class QueryMaliceUserMain{
	private final static Logger LOGGER = Logger
			.getLogger(QueryMaliceUserMain.class);
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context=new 
				ClassPathXmlApplicationContext(new String[] { "maliceuser.xml" });
		context.start();
		LOGGER.debug("QueryMaliceUser is start");
		
		while (true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO: handle exception
				LOGGER.warn(e);
			}
		}
	}	
}
