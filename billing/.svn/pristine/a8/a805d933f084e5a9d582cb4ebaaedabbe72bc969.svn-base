package com.tydic.beijing.billing.memcache;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MemcachedUpdate {

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "memcachedupdate.xml" });
		context.start();
		while (true) {
			Thread.sleep(10000L);
		}
	}
}
