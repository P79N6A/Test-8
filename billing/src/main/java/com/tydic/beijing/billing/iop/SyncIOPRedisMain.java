package com.tydic.beijing.billing.iop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.iop.service.SyncDbInfo;

public class SyncIOPRedisMain {
	
	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"SyncIOPRedis.xml"});
		   
		SyncDbInfo syncdbinfo = (SyncDbInfo) context.getBean("lifeImsisync");
		
		SyncDbInfo syncdbinfo2 = (SyncDbInfo) context.getBean("lifeNumbersync");
		
		SyncDbInfo syncdbinfo1 = (SyncDbInfo) context.getBean("lifeProductsync");

		syncdbinfo.start();
		
		syncdbinfo1.start();

		syncdbinfo2.start();


 
		
	}
	

}
