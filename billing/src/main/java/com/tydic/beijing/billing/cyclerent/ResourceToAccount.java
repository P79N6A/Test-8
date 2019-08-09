package com.tydic.beijing.billing.cyclerent;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class ResourceToAccount {
	private static final Logger LOGGER = Logger.getLogger(ResourceToAccount.class);
	public static void main(String[] args) {
		  ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"ResourceToAccount.xml"});
//		  CycleRentAndRes2AccountImpl cycle = (CycleRentAndRes2AccountImpl)context.getBean("RetryCycleRent");
	      context.start();
	      LOGGER.debug("---------interfaace resourceToAccount  start successful.....");
			while (true) {
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					LOGGER.warn(e.getMessage());
				}
			}
	}

}
