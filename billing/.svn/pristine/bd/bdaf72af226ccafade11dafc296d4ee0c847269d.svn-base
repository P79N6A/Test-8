package com.tydic.beijing.billing.account;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.service.Refund;

/**
 * 
 * @author Tian
 *
 */
public class RefundMain {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "refund.xml" });
		context.start();
		Refund r = (Refund) context.getBean("refund");
		r.process();
	}

}
