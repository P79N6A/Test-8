package com.tydic.beijing.billing.interfacex;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.BeforeAdjust;

public class BeforeAdjustMain {

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"BeforeAdjust.xml"});
		int mod = Integer.parseInt(args[0]);
		for(int i=0; i<mod; i++) {
			BeforeAdjust adjust = (BeforeAdjust) context.getBean("beforeAdjusct");
			adjust.init(mod, i);
			new Thread((Runnable) adjust).start();
		}
	}

}
