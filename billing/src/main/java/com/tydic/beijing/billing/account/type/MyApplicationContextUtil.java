package com.tydic.beijing.billing.account.type;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MyApplicationContextUtil implements ApplicationContextAware {

	protected static ApplicationContext mycontext;
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		mycontext = applicationContext;
	}

	public static ApplicationContext getContext() {
		return mycontext;
	}
	
	public static Object getBean(String bean_name) {
		return mycontext.getBean(bean_name);
	}
}
