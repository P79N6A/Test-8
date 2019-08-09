package com.tydic.beijing.billing.ua;

import org.springframework.context.support.ClassPathXmlApplicationContext;

class BB {
	public int a = 0;
}

public class Test {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = SpringContextUtil.getContext();
		ctx.start();
		String num = Search.getInstance().ruleStandardNumber("861709013513");
		System.err.println(num);
	}

}
