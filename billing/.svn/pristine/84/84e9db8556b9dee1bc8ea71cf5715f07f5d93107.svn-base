package com.tydic.beijing.billing.cyclerent;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class CycleRentMain {
	private static final Logger LOGGER = Logger.getLogger(CycleRentMain.class);
	public static void main(String[] args) {
		  ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"CycleRent.xml"});
//		  CycleRentAndRes2AccountImpl cycle = (CycleRentAndRes2AccountImpl)context.getBean("RetryCycleRent");
	      context.start();
	      LOGGER.debug("---------interfaace CycleRent  start successful.....");
			while (true) {
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					LOGGER.warn(e.getMessage());
				}
			}
	}

}
