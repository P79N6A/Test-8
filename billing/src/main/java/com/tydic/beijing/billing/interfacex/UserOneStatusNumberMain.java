package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.impl.UserOneStatusNumberImpl;

public class UserOneStatusNumberMain {
	private final static Logger LOGGER=Logger.getLogger(UserOneStatusNumberMain.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "userOneStatusNumber.xml" });
		context.start();
		LOGGER.debug("开始查找101状态用户");
		UserOneStatusNumberImpl userOneStatusNumberImpl=(UserOneStatusNumberImpl)context.getBean("userStatusNumber");
		userOneStatusNumberImpl.getStatusNumber();
		LOGGER.debug("=================工作结束！！！==================");
		System.exit(0);
//		context.close();
	}

}
