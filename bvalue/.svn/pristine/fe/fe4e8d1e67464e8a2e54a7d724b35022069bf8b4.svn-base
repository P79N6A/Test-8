package com.tydic.beijing.bvalue;

import java.util.concurrent.TimeUnit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SetAutoExchangeApp {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "setAutoExchange.xml" });
		context.start();

		while (true) {
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
