/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.core.SKUProcess;

/**
 * SKU赠送，后台处理<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class SKUPresentApp {

	private static final Logger log = Logger.getLogger(SKUPresentApp.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "sku.xml" });
		final int sleepInterval = 5;
		String channelStr = System.getProperty("CHANNEL");
		String remainderStr = System.getProperty("REMAINDER");
		int channel = 0;
		int remainder = 0;
		try {
			channel = Integer.parseInt(channelStr);
		} catch (NumberFormatException e) {
			log.error("channel总通道数必须提供，mod(x, channel) = remainder，默认channel:1");
			channel = 1;
		}

		try {
			remainder = Integer.parseInt(remainderStr);
		} catch (NumberFormatException e) {
			log.error("remainder通道号必须提供,mod(x, channel) = remainder，默认remainder:0");
			remainder = 0;
		}

		log.info("channel[" + channel + "]");
		log.info("sleepInterval[" + sleepInterval + "]");

		SKUProcess sp = (SKUProcess) ctx.getBean("skuProcess");
		sp.setChannel(channel);
		sp.setRemainder(remainder);
		sp.setSleepInterval(sleepInterval);
		Thread t = new Thread(sp, "SKUProcess---");

		t.start();

		while (true) {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
			}
		}
	}
}
