package com.tydic.beijing.billing.branch;

import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.branch.service.BranchService;


public class BranchMain {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(BranchMain.class);
	
	public static void main(String[] args){
		log.debug("---branch start ---");
		log.debug("---parsing branch.xml begin----");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"branch.xml"});
		log.debug("---branch.xml parsing end---");
		log.debug("---get branchServiceImpl bean----");
		BranchService branchService = (BranchService) context.getBean("BranchServiceImpl");
		
		try {
			long start = System.currentTimeMillis();
				while (true) {
					log.debug("---branchService run ----");
					branchService.run();
					Thread.sleep(10*1000);
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
	}
}
