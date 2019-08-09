package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.core.SKUMonthEndProcess;

public class SkuMonthEndProcessMain {
	private static final Logger log = Logger.getLogger(SkuMonthEndProcessMain.class);
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "skumonthendprocess.xml" });
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
		SKUMonthEndProcess skuMonthEndProcess=(SKUMonthEndProcess)ctx.getBean("skuMonthEndProcess");
		skuMonthEndProcess.setChannel(channel);
		skuMonthEndProcess.setRemainder(remainder);
		Thread t = new Thread(skuMonthEndProcess, "SKUMonthEndProcess---");
		t.start();
		ctx.close();
	}

}
