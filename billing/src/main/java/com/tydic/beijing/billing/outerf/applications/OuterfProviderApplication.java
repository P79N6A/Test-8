/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.billing.outerf.applications;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author wangshida
 */
public class OuterfProviderApplication {
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger
				.getLogger(OuterfProviderApplication.class.getName());
		AbstractXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "OuterfProviderApplicationContext.xml" });
		context.start();
		synchronized (OuterfProviderApplication.class) {
			try {
				OuterfProviderApplication.class.wait();
			} catch (InterruptedException ex) {
				logger.error(ex);
			}
		}
	}
}
