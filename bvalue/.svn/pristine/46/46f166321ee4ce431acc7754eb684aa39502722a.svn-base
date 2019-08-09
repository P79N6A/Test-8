package com.tydic.beijing.bvalue;

import java.util.concurrent.TimeUnit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TradeExchangeApp {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "tradeExchange.xml" });
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
