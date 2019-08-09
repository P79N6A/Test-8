package com.tydic.beijing.billing.account;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.service.RechargeAsync2Sync;

public class RechargeAsync2SyncMain {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "rechargeasync2sync.xml" });
		context.start();
		RechargeAsync2Sync ras = (RechargeAsync2Sync) context
				.getBean("rechargeAsync2SyncImpl");
		ras.process();
		System.exit(0);
	}
}
