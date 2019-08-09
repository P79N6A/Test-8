package com.tydic.beijing.billing.cyclerent;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dto.RetryCycleRentInfo;
import com.tydic.beijing.billing.interfacex.service.RetryCycleRentService;

public class TestCycleRent {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "TestCycleRent.xml" });
		String user_id = args[0];
		RetryCycleRentService app  = (RetryCycleRentService)context.getBean("RetryCycleRent");
		RetryCycleRentInfo info = new RetryCycleRentInfo();
		info.setUserId(user_id);
		app.doRetryCycleRent(info);
		
	}

}
