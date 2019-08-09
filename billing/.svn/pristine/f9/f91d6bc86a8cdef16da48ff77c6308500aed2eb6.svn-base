package com.tydic.beijing.billing.account;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountProcessMain {
	private final static Logger LOGGER = Logger.getLogger(AccountProcessMain.class);
	public static void main(String[] args) throws InterruptedException {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "accountProcess.xml" });
		context.start();
		while (true) {
			Thread.sleep(60000L);
			/*
			LOGGER.info("================================= refresh");
			context.refresh();
			*/
		}
	}

}
