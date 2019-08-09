package com.tydic.beijing.billing.account;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.service.impl.SynchroniseExpdateImpl;



public class ContractSynchroniseExpdateMain {
	private final static Logger LOGGER = Logger.getLogger(ContractSynchroniseExpdateMain.class);
	public static void main(String arg[]){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "SynchroiseExpdate.xml" });
		SynchroniseExpdateImpl contractSynchroniseExpdateImpl=(SynchroniseExpdateImpl) context.getBean("synchroniseExpdateImpl");
			
			try {
				contractSynchroniseExpdateImpl.SynchroniseExpdate();
				context.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
			
			
		
		
	}
}
