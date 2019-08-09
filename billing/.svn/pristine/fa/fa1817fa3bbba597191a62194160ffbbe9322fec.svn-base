package com.tydic.beijing.billing.rating;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.rating.service.impl.LiangHaoSms;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;

public class LiangHaoSmsMain {
	public static final Logger log=LoggerFactory.getLogger(LiangHaoSmsMain.class);
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		log.debug("==========新靓号发送短信=========");
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[] {"lianghaosms.xml"});
		context.start();
		LiangHaoSms liangHaoSms=(LiangHaoSms) context.getBean("lianghaoSms");
		liangHaoSms.LiangHaoSendSms();
		context.close();
	}

}
