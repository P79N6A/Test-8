package com.tydic.beijing.billing.plugin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.plugin.service.SumBalanceInfo;

public class SumBalanceInfoMain {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"plugin.xml"});

		SumBalanceInfo sbi = (SumBalanceInfo) context.getBean("SumBalanceInfo");
		sbi.sumBalanceInfo();
		((AbstractApplicationContext) context).close();
	}

}
