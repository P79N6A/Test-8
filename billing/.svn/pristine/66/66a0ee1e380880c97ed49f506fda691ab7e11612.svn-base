package com.tydic.beijing.billing.account;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.service.impl.GetMonthEndUserImpl;

public class MonthEndMain {
	private final static Logger LOGGER = Logger.getLogger(MonthEndMain.class);
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
			new String[] { "monthend.xml"});
		context.start();
		int mod = Integer.parseInt(args[0]);
		
		GetMonthEndUserImpl array_obj[] = new GetMonthEndUserImpl[mod];
		Thread[] thread = new Thread[mod];
		for(int i=0; i<mod; i++) {
			array_obj[i] = (GetMonthEndUserImpl) context.getBean("GetMonthEndUser");
			array_obj[i].setMod(mod);
			array_obj[i].setPartition(i);
			thread[i] = new Thread(array_obj[i]);
		}
		for(int i=0; i<mod; i++) {
			thread[i].start();
		}
		try {
			for (int i = 0; i < mod; i++) {
				thread[i].join();
				LOGGER.debug("join " + thread[i].getId());
			}
		} catch (InterruptedException e) {
			LOGGER.debug(e.toString());
		}
		context.close();
	}
}
