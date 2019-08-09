package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class JDPinRelationMain {

	private static Logger log = Logger.getLogger(JDPinRelationMain.class);
	
	public static void main(String args[]){
		
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"jdpinRelation.xml"});
		context.start();
		log.debug(">>>>>>>>>>>>>>>>>service JDPinRelation start successful....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
	}
	
}
