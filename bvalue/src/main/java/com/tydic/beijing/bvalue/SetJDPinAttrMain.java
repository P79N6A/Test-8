package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SetJDPinAttrMain {

	private static Logger log=Logger.getLogger(SetJDPinAttrMain.class);
	
	
	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"setJdpinAttr.xml"});
		context.start();
		log.debug(">>>>>>>>>>>>>>>>>service SetJDPinAttr start successful....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
	}
	
}
