package com.tydic.beijing.billing.account;

import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.service.impl.InstContractServiceImpl;
import com.tydic.beijing.billing.account.service.impl.DealContractService;

public class ContractDealMain {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(ContractDealMain.class);
	public static void main(String[] args) {
		log.debug("---PresentDeal start ---");
		log.debug("---parsing applicationContext.xml begin----");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"ContractDeal.xml"});
		InstContractServiceImpl instService = (InstContractServiceImpl) context.getBean("instContractService");
		DealContractService presentService = (DealContractService) context.getBean("present");

		try{
			
//			int mod=3;
//			DealContractService AryDealContractService[] = new DealContractService[mod];
//			Thread[] thread = new Thread[mod];
//			for(int i=0; i<mod; i++){
//				log.debug("----mod:"+mod+", partion:"+i);
//				AryDealContractService[i] = (DealContractService) context.getBean("present");
//				AryDealContractService[i].setMod(mod);
//				AryDealContractService[i].setPartion(i);
//				thread[i] = new Thread(AryDealContractService[i]);
//				thread[i].start();
//				Thread.sleep(3000);
//			}
			
			
			if(args[0].equals("-i")){
				log.debug("-i");
				instService.InstPresentRun();
			}else if(args[0].equals("-d")){
				int mod =1;
				if(args.length>1){
					mod = Integer.parseInt(args[1]);
				}
				
				log.debug("-d");
				DealContractService AryDealContractService[] = new DealContractService[mod];
				Thread[] thread = new Thread[mod];
				for(int i=0; i<mod; i++){
					log.debug("----mod:"+mod+", partion:"+i);
					AryDealContractService[i] = (DealContractService) context.getBean("present");
					AryDealContractService[i].setMod(mod);
					AryDealContractService[i].setPartion(i);
					thread[i] = new Thread(AryDealContractService[i]);
					thread[i].start();
					Thread.sleep(3000);
				}
				
				//presentService.run();
				for (int i = 0; i < mod; i++) {
					
					thread[i].join();
					log.debug("join " + thread[i].getId());
				}
			}
			
			
			/*
			if(args[0].equals("-i")){
				log.debug("-i");
				instService.InstPresentRun();
			}else if(args[0].equals("-d")){
				log.debug("-d");
				presentService.run();
			}
			*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
