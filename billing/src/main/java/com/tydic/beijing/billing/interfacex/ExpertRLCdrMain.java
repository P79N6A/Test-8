package com.tydic.beijing.billing.interfacex;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.interfacex.service.ExpertRLCdrForCallTransferPartner;


public class ExpertRLCdrMain {

	//private static final Logger log = LoggerFactory.getLogger(ExpertCDRofAutoHomeMain.class);
	private final static Logger log = Logger.getLogger(ExpertRLCdrMain.class);
	
	
	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"expertCdrForCallTransferPartner.xml"});
   
		
		
		ExpertRLCdrForCallTransferPartner expertRLCdrForCallTransferPartner = (ExpertRLCdrForCallTransferPartner) context.getBean("expertRLCdrForCallTransferPartner");
		
		while (true){
			
			List<RuleParameters> allConfigInfo = expertRLCdrForCallTransferPartner.getAllCallTransferConfig();
		    
			for(RuleParameters tmppartner:allConfigInfo){
				try {
					long startTime = System.currentTimeMillis();
					expertRLCdrForCallTransferPartner.expert(tmppartner);
					
					log.debug(tmppartner.getPara_char1() +" cost time ==>"+(System.currentTimeMillis() - startTime) +" ms");
				} catch (Exception e) {
					log.error(tmppartner.getPara_char1() +"expert exception"+e.getMessage(),e);
				}
			}	
			
			
		    
			//每次全量导出一次后sleep5分钟
			try {
				
				Thread.sleep(5*60*1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
 
	}
	
}
