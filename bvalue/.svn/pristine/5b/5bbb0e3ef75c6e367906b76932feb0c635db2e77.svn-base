package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.UpdateBValueByShoppingFileThread;

public class UpdateBValueByShoppingFileMain {
	

	private static Logger log=Logger.getLogger(UpdateBValueByShoppingFileMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("UpdateBValueByShoppingFile.xml");
		context.start();
		log.debug("service UpateBValueByShoppingFileMain start ....");
		
		//获得bean
		//ShoppingFile shoppingFile = (ShoppingFile) context.getBean("shoppingFile");
		
		//shoppingFile.importShoppingFile();
		
//		DbConfigDetail dbConfigDetail = (DbConfigDetail) context.getBean("dbConfigDetail");
//		
//		int maxthread = dbConfigDetail.getMaxthread();
		
		//for(int i=0;i<maxthread;i++){
		String processNum = args[0];
		//String processNum ="00";
			
			UpdateBValueByShoppingFileThread updateBValueByShoppingFileThread = (UpdateBValueByShoppingFileThread) context.getBean("updateBValueByShoppingFileThread");
			
//			importShoppingFileThread.setFilePrefix(filePrefix);
//			importShoppingFileThread.setFileDir(fileDir);
			updateBValueByShoppingFileThread.setThreadId(processNum);

			//Thread importShoppingFileThread = new ImportShoppingFileThread(fileDir,filePrefix,i);
			updateBValueByShoppingFileThread.start();
			//log.debug("thread "+i+" is started!!!!!");
		//}
		
	}

}
