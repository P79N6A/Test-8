/**
 * 
 */
package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author sung
 *
 */
public class QueryCurrentBillMain {

	
	private final static Logger log = Logger.getLogger(QueryCurrentBillMain.class);
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "currentBillQuery.xml" });
		context.start();
		log.debug("service QueryCurrentBill start successful......");
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
	}
	
	
	
}
