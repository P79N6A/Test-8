package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.BatchAdjustBValueThread;
import com.tydic.beijing.bvalue.service.impl.ShoppingRejectThread;

public class BatchAdjustBValueMain {
	

	private static Logger log=Logger.getLogger(BatchAdjustBValueMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("BatchAdjustBValue.xml");
		context.start();
		log.debug("service BatchAdjustBValueMain start ....");
 
		//String processNum = args[0];
		//String processNum ="00";
			
		BatchAdjustBValueThread batchAdjustBValueThread = (BatchAdjustBValueThread) context.getBean("batchAdjustBValueThread");

		batchAdjustBValueThread.start(); 
		
	}

}
