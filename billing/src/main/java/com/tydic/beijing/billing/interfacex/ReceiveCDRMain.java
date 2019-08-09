package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ReceiveCDRMain {
	private static Logger log = Logger.getLogger(ReceiveCDRMain.class);

	public static void main(String[] args) {
		log.debug("============进入容联话单实时接口============");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"receive_cdr.xml" });
		
		context.start( );

		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
//		log.debug(message, t);
	}
}	
	

