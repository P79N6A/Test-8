package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.ExpertForAutoHome;


public class ExpertCDRofAutoHomeMain {

	//private static final Logger log = LoggerFactory.getLogger(ExpertCDRofAutoHomeMain.class);
	private final static Logger log = Logger.getLogger(ExpertCDRofAutoHomeMain.class);
	
	
	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"expertCDRofAutoHome.xml"});
   
		ExpertForAutoHome expertForAutoHome = (ExpertForAutoHome) context.getBean("expertForAutoHome");
		//String expertTime = "20150610";
		String startTime = args[0];
		String endTime  =args[1];
		
		log.debug("开始导出话单文件，起始时间"+startTime+"结束时间"+endTime);
		
//		String startTime = "20150701121211";
//		String endTime  ="20150801010101";
		
		expertForAutoHome.expert(startTime,endTime);
		
		log.debug("结束");
		System.exit(0);
		 
		
		
	}
	
}
