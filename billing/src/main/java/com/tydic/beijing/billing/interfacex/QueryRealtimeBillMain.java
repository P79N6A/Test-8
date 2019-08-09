package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QueryRealtimeBillMain {
	private final static Logger LOGGER = Logger
			.getLogger(QueryRealtimeBillMain.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "queryrealtimebill.xml" });
		context.start();
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				LOGGER.warn(e.getMessage());
			}
		}
	}

}
