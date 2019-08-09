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
public class QueryRemainResourceMain {

	private final static Logger log = Logger.getLogger(QueryRemainResourceMain.class);
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "remainResourceQuery.xml" });
		context.start();
		log.debug("service QueryRemainResourceList start successful.....");
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
	}
	
	
	

	
}
