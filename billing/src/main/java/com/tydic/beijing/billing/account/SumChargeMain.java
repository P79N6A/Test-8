package com.tydic.beijing.billing.account;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.service.SumChargeService;

public class SumChargeMain {

	//private static SumChargeService scs;
	//public static MyApplicationContextUtil myContext;

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
			new String[] { "sum.xml"});
		//mycontext = MyApplicationContextUtil.
		//mycontext = context;
		//((MyApplicationContextUtil) mycontext).setApplicationContext(context);
		
		//scs = (SumChargeImpl)MyApplicationContextUtil.getContext().getBean("SumChargeImpl");
		SumChargeService scs = (SumChargeService) context.getBean("SumChargeImpl");
		scs.sumCharge();
	}

}
