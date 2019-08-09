package com.tydic.beijing.billing.account;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MonthEndWriteOffServiceMain {

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "monthendwriteoffservice.xml" });
		context.start();
		while (true) {
			Thread.sleep(10000L);
		}
	}

}
