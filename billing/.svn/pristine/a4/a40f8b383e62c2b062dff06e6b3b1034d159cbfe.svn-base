package com.tydic.beijing.billing.account;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.service.SumChargeService;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;

public class QAcctProcessTest extends MyApplicationContextUtil {

	//private static SumChargeService scs;
	//public static MyApplicationContextUtil myContext;

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml", "uda.xml" });
		//mycontext = MyApplicationContextUtil.
		mycontext = context;
		//((MyApplicationContextUtil) mycontext).setApplicationContext(context);
		
		//scs = (SumChargeImpl)MyApplicationContextUtil.getContext().getBean("SumChargeImpl");
		SumChargeService scs = (SumChargeService) mycontext.getBean("SumChargeImpl");
		scs.sumCharge();
	}

}
