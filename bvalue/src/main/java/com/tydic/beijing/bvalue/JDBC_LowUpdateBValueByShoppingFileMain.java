package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.LowUpdateBValueByShoppingFileThreadJDBC;
import com.tydic.beijing.bvalue.service.impl.UpdateBValueByShoppingFileThreadJDBC;

public class JDBC_LowUpdateBValueByShoppingFileMain {
	

	private static Logger log=Logger.getLogger(JDBC_LowUpdateBValueByShoppingFileMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("LowUpdateBValueByShoppingFile_JDBC.xml");
		context.start();
		log.debug("service LowUpateBValueByShoppingFileMain start ....");
		
		//获得bean
		//ShoppingFile shoppingFile = (ShoppingFile) context.getBean("shoppingFile");
		
		//shoppingFile.importShoppingFile();
		
//		DbConfigDetail dbConfigDetail = (DbConfigDetail) context.getBean("dbConfigDetail");
//		
//		int maxthread = dbConfigDetail.getMaxthread();
		
		//for(int i=0;i<maxthread;i++){
		String processNum = args[0];
		//String processNum ="00001";
			
			LowUpdateBValueByShoppingFileThreadJDBC lowUpdateBValueByShoppingFileThreadjdbc = (LowUpdateBValueByShoppingFileThreadJDBC) context.getBean("lowUpdateBValueByShoppingFileThreadJDBC");
			
//			importShoppingFileThread.setFilePrefix(filePrefix);
//			importShoppingFileThread.setFileDir(fileDir);
			lowUpdateBValueByShoppingFileThreadjdbc.setThreadId(processNum);

			//Thread importShoppingFileThread = new ImportShoppingFileThread(fileDir,filePrefix,i);
			lowUpdateBValueByShoppingFileThreadjdbc.run();
			//log.debug("thread "+i+" is started!!!!!");
		//}
		
	}

}
