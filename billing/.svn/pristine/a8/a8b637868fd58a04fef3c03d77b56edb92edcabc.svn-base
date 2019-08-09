package com.tydic.beijing.billing.account;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Tian
 *
 */
public class RechargeMain {
	private final static Logger LOGGER = Logger.getLogger(RechargeMain.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "recharge.xml" });
		context.start();
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				LOGGER.warn(e.getMessage());
			}
		}
	}
}
