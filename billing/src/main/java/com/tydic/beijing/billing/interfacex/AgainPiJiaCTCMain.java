package com.tydic.beijing.billing.interfacex;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.impl.AgainPiJiaServiceImpl;

public class AgainPiJiaCTCMain {
	private final static Logger log=Logger.getLogger(AgainPiJiaCTCMain.class);
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "againPijiaCTC.xml" });
		AgainPiJiaServiceImpl againPiJia=(AgainPiJiaServiceImpl)context.getBean("againPiJiaImpl");
		try {
			againPiJia.pijia();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("生成原始电信话单文件时报错！！！");
		}
	}

}
