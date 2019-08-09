package com.tydic.beijing.bvalue;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.BUserRegist;

public class RegistUserMain {

	private static Logger log= Logger.getLogger(RegistUserMain.class);
	
	public static void main(String args[]){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "regist.xml" });
		context.start();
		BUserRegist regist=(BUserRegist)context.getBean("regist");
		
		regist.registUser();
		log.debug("!!!!!!!!!!!!"+Calendar.getInstance().getTime()+":service RegistUserService  stopped.........");
		
		context.stop();
		context.close();
	}
}
