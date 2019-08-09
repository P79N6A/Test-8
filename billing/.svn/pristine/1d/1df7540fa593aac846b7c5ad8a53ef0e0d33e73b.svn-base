package com.tydic.beijing.billing.interfacex;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.impl.AgainPiJiaServiceImpl;

public class AgainPiJiaGSMMain {
	private final static Logger log=Logger.getLogger(AgainPiJiaGSMMain.class);
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "againPijiaGSM.xml" });
		log.debug("========开始生成联通话单=============");
		AgainPiJiaServiceImpl againPiJia=(AgainPiJiaServiceImpl)context.getBean("againPiJiaImpl");
		try {
			againPiJia.pijia();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("生成原始联通话单文件时报错！！！");
		}
	}

}
