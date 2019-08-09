/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.core.AutoProcess;

/**
 * 自动兑换后台程序<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class AutoPresentApp {
	private static final Logger log = Logger.getLogger(AutoPresentApp.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "auto.xml" });
		ctx.start();
		AutoProcess ap = (AutoProcess) ctx.getBean("autoProcess");
		ap.run();

		ctx.stop();
	}

}
