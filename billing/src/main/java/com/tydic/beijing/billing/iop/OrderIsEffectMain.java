package com.tydic.beijing.billing.iop;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.tydic.beijing.billing.iop.service.impl.OrderIsEffectServiceImpl;

import net.sf.json.JSONObject;

public class OrderIsEffectMain {
	private final static Logger LOGGER = Logger.getLogger(OrderIsEffectMain.class);
	public static void main(String[] args) {
		LOGGER.debug("=================开始进行话单是否存在查询=============");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"order_is_effect.xml" });
//		context.start();
//		JSONObject input=new JSONObject();
//		input.put("orderid", "111");
//		input.put("imsi", "11111");
//		input.put("productid", "22222");
//		input.put("startdate", "20160406122300");
//		input.put("enddate", "20160408122346");
//		OrderIsEffectServiceImpl orderIsEffectServiceImpl=(OrderIsEffectServiceImpl)context.getBean("orderIsEffectService");
//		JSONObject output=orderIsEffectServiceImpl.getIsEffect(input);
//		LOGGER.debug("最后结果是："+output);
		try {
			while (true) {
				Thread.sleep(1000L);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
