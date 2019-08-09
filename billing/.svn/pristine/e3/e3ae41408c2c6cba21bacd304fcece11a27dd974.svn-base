/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.credit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO(用几句话描述这个类型)<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class SpringContextUtil {
	private volatile static ClassPathXmlApplicationContext ctx = null;

	public static ClassPathXmlApplicationContext getContext() {
		if (ctx == null) {
			synchronized (SpringContextUtil.class) {
				if (ctx == null) {
					ctx = new ClassPathXmlApplicationContext(
							new String[] { "applicationContext.xml" });
				}
			}
		}
		return ctx;
	}
}
