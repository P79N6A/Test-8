package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

	public class QuerySubsBillPeriodCRMMain {

	private static Logger log=Logger.getLogger(QuerySubsBillPeriodCRMMain.class);
	public static void main(String args[]){
		
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"subsBillPeriodQueryCRM.xml"});
		context.start();
		log.debug("service QuerySubsBillPeriodCRM   start  successful ............");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
		
	}
	
}
