package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RetryCycleRentMain {
	private static final Logger LOGGER = Logger.getLogger(RetryCycleRentMain.class);
	public static void main(String[] args) {
      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
		new String[] {"retryCycleRent.xml"});
      	context.start();
      	LOGGER.debug("---RetryCycleRent start success !!!------");
      	while (true) {
			try {
				Thread.sleep(100000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}

	}

}
