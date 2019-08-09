package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QryPromDetailMain {
	private final static Logger log = Logger.getLogger(QryPromDetailMain.class);
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "qryPromDetail.xml" });
		context.start();
		log.debug("service QryPromDetail start successful......");
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
	}

}
