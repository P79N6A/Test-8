package com.tydic.beijing.billing.sms;

import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.sms.service.impl.DealSmsRecv;
import com.tydic.beijing.billing.sms.service.impl.SmsHisServiceImpl;
import com.tydic.beijing.billing.sms.service.impl.SmsServiceImpl;

public class SmsDeal {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(SmsDeal.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
		SmsServiceImpl SmsService = (SmsServiceImpl) context.getBean("sms");
		SmsHisServiceImpl SmsHisService = (SmsHisServiceImpl) context.getBean("smshis");
//		DealSmsRecv smsRecv = (DealSmsRecv) context.getBean("smsrecv");
		try {
			log.debug("--begin deal sms");
			//SmsService.start();
			//SmsHisService.start();
			if(args[0].equals("-s")){
				SmsService.run();
			}else if(args[0].equals("-h")){
				SmsHisService.run();
			}else if(args[0].equals("-r")){
				//smsRecv.run();
			}
			log.debug("sms deal stoped arg {}",args[0]);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
