package com.tydic.beijing.billing.rating;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.rating.service.impl.MultiStopCallerDisplayProcess;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;

public class MultiStopCallerMain {
	public static final  Logger log = LoggerFactory.getLogger(MultiStopCallerMain.class);

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"MultiStopCallerDisplay.xml"});
		context.start();
		MultiStopCallerDisplayProcess mscdp=(MultiStopCallerDisplayProcess)context.getBean("multiStopCallerDisplay");
		log.debug("开始停来显啦！！！");
		mscdp.StopCallerDisplay();
		context.close();
	}

}
