package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.Resource2Account;


public class Resource2AccountMain {
	private static Logger LOGGER = Logger.getLogger(Resource2Account.class);
	public static void main(String[] args) {
	      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"Resource2Account.xml"});
	      context.start();
	      
	      LOGGER.debug("interface Resource2Account  start successful.....");
			while (true) {
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					LOGGER.warn(e.getMessage());
				}
			}

		}
	
	

}
