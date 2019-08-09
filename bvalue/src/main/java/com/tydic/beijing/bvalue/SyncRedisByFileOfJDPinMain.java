package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.SyncRedisByFileOfJDPin;

public class SyncRedisByFileOfJDPinMain {
	

	private static Logger log=Logger.getLogger(SyncRedisByFileOfJDPinMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("syncRedisByFileOfJDPin.xml");
		context.start();
		log.debug("service syncRedisByFileOfJDPin start ....");
		
		//获得bean
		//ShoppingFile shoppingFile = (ShoppingFile) context.getBean("shoppingFile");
		
		//shoppingFile.importShoppingFile();
		
//		DbConfigDetail dbConfigDetail = (DbConfigDetail) context.getBean("dbConfigDetail");
//		
//		int maxthread = dbConfigDetail.getMaxthread();
		
		//for(int i=0;i<maxthread;i++){
		String processNum = args[0];
		//String processNum ="1111";
			
		SyncRedisByFileOfJDPin syncRedisByFileOfJDPin = (SyncRedisByFileOfJDPin) context.getBean("syncRedisByFileOfJDPin");
			
//			importShoppingFileThread.setFilePrefix(filePrefix);
//			importShoppingFileThread.setFileDir(fileDir);
		syncRedisByFileOfJDPin.setThreadId(processNum);

			//Thread importShoppingFileThread = new ImportShoppingFileThread(fileDir,filePrefix,i);
		syncRedisByFileOfJDPin.run();
			//log.debug("thread "+i+" is started!!!!!");
		//}
		
	}

}
