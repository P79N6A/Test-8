package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AdjustBValueMain {
	
	private static Logger log=Logger.getLogger(QueryBAcctRemainBalanceMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"adjustBValue.xml"});
		context.start();
		log.debug(">>>>>>>>>>>>>>>>>>>>service AdjustBValue start successful....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
	}

}
