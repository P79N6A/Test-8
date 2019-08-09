package com.tydic.beijing.billing.rating;

 
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;


public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class);

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"rating.xml"});
          
		context.start();
		
		//ApplicationContextHelper applicationContextHelper = (ApplicationContextHelper) context.getBean("applicationContextHelper");
		
		DbConfigDetail dbConfigDetail = (DbConfigDetail) context.getBean("dbConfigDetail");

		long start = System.currentTimeMillis();
        	while (true) {
        		Thread.sleep(10000L);
        	}

		//[100=80][101=20121020211803][000=c1-172-100-100-66-GGSNHI01.chinaunicom.com;1332783460;16788222;4262264831][001=2311263601924864364][R71=0][R85=1][R01=13006900012][R30=000,R301=220.206.147.178,R302=220.206.147.162,R305=cmnet,R306=0,R307=172.17.0.116,R309=3600,R3010=99-13921f739697eb74821040,R3012=460015862510310,R3013=1][R50=000,R504=0898,R505=0898,R5011=0,R5012=0898][R60=000,R601=0,R602=1,R603=0,R604=20121020211739,R605=0][B01=000,B010=4262264831,B016=3,B017=1024000][B06=1][B07=1][B08=1][B20=1][B21=1][vs=2]
//		Rating rating = (Rating) context.getBean("rating");
//		String inputstr="[100=80][101=20121020211803][000=c1-172-100-100-66-GGSNHI01.chinaunicom.com;1332783460;16788222;4262264831][001=2311263601924864364][R71=0][R85=1][R01=13006900012][R30=000,R301=220.206.147.178,R302=220.206.147.162,R305=cmnet,R306=0,R307=172.17.0.116,R309=3600,R3010=99-13921f739697eb74821040,R3012=460015862510310,R3013=1][R50=000,R504=0898,R505=0898,R5011=0,R5012=0898][R60=000,R601=0,R602=1,R603=0,R604=20121020211739,R605=0][B01=000,B010=4262264831,B016=3,B017=1024000][B06=1][B07=1][B08=1][B20=1][B21=1][vs=2]";
//		String resultstr =rating.deal(inputstr);
//		long end = System.currentTimeMillis();
        
	 	
//		

	
		
	}

	 
 

}
