package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class QueryBAcctRemainBalanceMain {
	
	private static Logger log=Logger.getLogger(QueryBAcctRemainBalanceMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"queryBAcctRemainBalance.xml"});
		context.start();
		log.debug(">>>>>>>>>>>>>>>>>>>>service QueryBAcctRemainBalance start successful....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
	}
}
