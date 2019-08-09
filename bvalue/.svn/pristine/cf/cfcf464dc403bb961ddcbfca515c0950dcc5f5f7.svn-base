package com.tydic.beijing.bvalue;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.impl.BatchCreateUserThread;

public class BatchCreateUserMain {
	

	private static Logger log=Logger.getLogger(BatchCreateUserMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("BatchCreateUser.xml");
		context.start();
		log.debug("service BatchCreateUser start ....");

		if(args.length ==0){
			log.debug("没有输入进程号！");
			return ;
		}
		String processNum= args[0];
		//String threadNum = args[1];
		
//		String processNum ="";
//		String threadNum="3";
		
	 
//		for(int i=0;i< Integer.parseInt(threadNum);i++){
//			
//			String stri ="";
//			if(i<10){
//				stri = "0"+i;
//			}else{
//				stri = ""+i;
//			}
			
			BatchCreateUserThread batchCreateUserThread = (BatchCreateUserThread) context.getBean("batchCreateUserThread");
			batchCreateUserThread.setThreadId(processNum);
			batchCreateUserThread.start();
			//log.debug("thread "+i+" is started!!!!!");
		//}
	 
	}

}
