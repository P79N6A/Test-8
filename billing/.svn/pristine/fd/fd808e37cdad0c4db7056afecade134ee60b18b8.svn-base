package com.tydic.beijing.billing.rating;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.service.impl.ChargeInitImpl;

public class RatingMain {

	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"rating.xml"});
        
		context.start();
		
		//ApplicationContextHelper applicationContextHelper = (ApplicationContextHelper) context.getBean("applicationContextHelper");
		
		
		
		
		
		DbConfigDetail dbConfigDetail = (DbConfigDetail) context.getBean("dbConfigDetail");

		try {
			long start = System.currentTimeMillis();
				while (true) {
					Thread.sleep(10000L);
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


//		ChargeInitImpl rating = (ChargeInitImpl)  context.getBean("rating");
//		String inputstr ="[100=60][101=201411031951560000][000=0][001=19748760][R01=17090134340][R02=17090134340][R03=18610052058][R71=20140801114137][R85=2][R65=0][R67=460010139808084][R10=000,R103=1,R105=18610052058][R50=000,R504=010,R505=010,R506=3,R507=010,R508=010,R509=3,R5010=0,R5011=0,R5012=010][B09=000,B090=1,B092=1,B096=1][B03=000,B036=1,B037=6]";
////
//				try {
//					//for(int i=0; i< 10000; ++i){
//						rating.deal(inputstr);
//					
////			String resultstr =rating.deal(inputstr);
//				//	rating.testupdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		long end = System.currentTimeMillis();
	}
}
