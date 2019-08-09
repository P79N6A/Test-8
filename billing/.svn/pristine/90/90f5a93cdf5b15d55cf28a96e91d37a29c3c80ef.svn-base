package com.tydic.beijing.billing.interfacex;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dao.CDR100TransferJDN;
import com.tydic.beijing.billing.interfacex.service.DealJDNCdrProcess;


public class DealJDNCdrMain {
	
	private static Logger log=Logger.getLogger(DealJDNCdrMain.class);

	public static void main(String[] args){
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"dealJDNCdr.xml" });
		
		DealJDNCdrProcess deal =(DealJDNCdrProcess) context.getBean("dealJDNCdrProcess");
		
		while(true){
			try {
				List<CDR100TransferJDN> jdnCdrs =  deal.getInitRLCdr();
				deal.dealJDN(jdnCdrs);
				
				if(jdnCdrs.size()<20){
					Thread.sleep(1000*60*5);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("处理荣联话单异常"+e.getMessage(),e);
				System.exit(0);
			}
			
		}
		
         
        
	    
	}
}
