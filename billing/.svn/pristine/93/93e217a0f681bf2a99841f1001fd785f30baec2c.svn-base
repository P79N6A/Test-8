package com.tydic.beijing.billing.interfacex;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.interfacex.service.ExpertCdrForCallTransferPartner;


public class ExpertCdrForCallTransferPartnerMain {

	//private static final Logger log = LoggerFactory.getLogger(ExpertCDRofAutoHomeMain.class);
	private final static Logger log = Logger.getLogger(ExpertCdrForCallTransferPartnerMain.class);
	
	
	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"expertCdrForCallTransferPartner.xml"});
   
		
		
		ExpertCdrForCallTransferPartner expertCdrForCallTransferPartner = (ExpertCdrForCallTransferPartner) context.getBean("expertCdrForCallTransferPartner");
		//String expertTime = "20150610";
//		String startTime = args[0];
//		String endTime  =args[1];
		
 
//		String startTime = "20150701121211";
//		String endTime  ="20150801010101";
		
		
		while (true){
			
			List<RuleParameters> allConfigInfo = expertCdrForCallTransferPartner.getAllCallTransferConfig();
		    
			for(RuleParameters tmppartner:allConfigInfo){
				try {
					long startTime = System.currentTimeMillis();
					expertCdrForCallTransferPartner.expert(tmppartner);
					
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
