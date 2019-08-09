package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.UpdateBValueByShoppingFileThreadJDBC;

public class JDBC_UpdateBValueByShoppingFileMain {
	

	private static Logger log=Logger.getLogger(JDBC_UpdateBValueByShoppingFileMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("UpdateBValueByShoppingFile_JDBC.xml");
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
		//String processNum ="00001";
			
			UpdateBValueByShoppingFileThreadJDBC updateBValueByShoppingFileThreadjdbc = (UpdateBValueByShoppingFileThreadJDBC) context.getBean("updateBValueByShoppingFileThreadJDBC");
			
//			importShoppingFileThread.setFilePrefix(filePrefix);
//			importShoppingFileThread.setFileDir(fileDir);
			updateBValueByShoppingFileThreadjdbc.setThreadId(processNum);

			//Thread importShoppingFileThread = new ImportShoppingFileThread(fileDir,filePrefix,i);
			updateBValueByShoppingFileThreadjdbc.run();
			//log.debug("thread "+i+" is started!!!!!");
		//}
		
	}

}
