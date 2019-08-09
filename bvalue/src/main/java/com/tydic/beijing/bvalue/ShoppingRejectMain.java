package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.ShoppingRejectThread;

public class ShoppingRejectMain {
	

	private static Logger log=Logger.getLogger(ShoppingRejectMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("ShoppingReject.xml");
		context.start();
		log.debug("service ShoppingRejectMain start ....");
 
		String processNum = args[0];
		//String processNum ="00";
			
		ShoppingRejectThread shoppingRejectThread = (ShoppingRejectThread) context.getBean("shoppingRejectThread");
 
		shoppingRejectThread.setThreadId(processNum);
 
		shoppingRejectThread.start(); 
		
	}

}
