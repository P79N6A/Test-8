package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.dao.DbConfigDetail;
import com.tydic.beijing.bvalue.service.impl.ImportShoppingFileThread;

public class ImportShoppingFileMain {
	

	private static Logger log=Logger.getLogger(ImportShoppingFileMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("ImportShoppingFile.xml");
		context.start();
		log.debug("service ImportShoppingFileMain start ....");
		
		//获得bean
		//ShoppingFile shoppingFile = (ShoppingFile) context.getBean("shoppingFile");
		
		//shoppingFile.importShoppingFile();
		
		if(args.length ==0){
			log.debug("没有输入进程号！");
			return ;
		}
		
		DbConfigDetail dbConfigDetail = (DbConfigDetail) context.getBean("dbConfigDetail");
		String processNum= args[0];
	 
		for(int i=0;i< dbConfigDetail.getMaxthread();i++){
			
			String stri ="";
			if(i<10){
				stri = "0"+i;
			}else{
				stri = ""+i;
			}
			
			ImportShoppingFileThread importShoppingFileThread = (ImportShoppingFileThread) context.getBean("importShoppingFileThread");
			importShoppingFileThread.setThreadId(processNum+stri);
			importShoppingFileThread.start();
			log.debug("thread "+i+" is started!!!!!");
		}
	 
	}

}
