package com.tydic.beijing.bvalue;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.ITradeExchange;

public class TradeExchangeTest {

	@Test
	public void testTradeExchange() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "tradeExchangeTest.xml" });
		context.start();

		ITradeExchange ie = (ITradeExchange) context.getBean("trade");
		JSONObject obj = new JSONObject();
		obj.put("JDPin", "xingzm@tydic.com");
		obj.put("MobileNumber", "18600578727");
		obj.put("ExchangeBValue", "800");
		obj.put("ChannelType", "109");
		obj.put("OrderNo", System.currentTimeMillis() + "");
		obj.put("OrderDesc", "我要兑换B值");

		JSONArray l = new JSONArray();
		JSONObject b1 = new JSONObject();
		b1.put("ResourceType", "2");
		b1.put("TotalResource", "300");
		l.add(b1);

		JSONArray array = JSONArray.fromObject(l);
		obj.put("ResourceDtoList", l);

		System.out.println("obj:" + obj);
		JSONObject ret = ie.trade(obj);

		System.out.println("status=" + ret.get("Status"));
		System.out.println("ErrorCode=" + ret.get("ErrorCode"));
		System.out.println("ErrorMessage=" + ret.get("ErrorMessage"));

	}

	public void generateJson() {
		JSONObject obj = new JSONObject();
		obj.put("JDPin", "xingzm@tydic.com");
		obj.put("MobileNumber", "18600578727");
		obj.put("ExchangeBValue", "100");
		obj.put("ChannelType", "0");
		obj.put("OrderNo", "93724932");
		obj.put("OrderDesc", "我要兑换B值");
		// List<ResourceDto> l = new ArrayList<ResourceDto>();
		// ResourceDto res = new ResourceDto();
		// res.setResourceType("2");
		// res.setTotalResource("100");
		//
		// ResourceDto res2 = new ResourceDto();
		// res2.setResourceType("1");
		// res2.setTotalResource("100");

		JSONArray l = new JSONArray();
		JSONObject b1 = new JSONObject();
		b1.put("ResourceType", "2");
		b1.put("TotalResource", "100");
		l.add(b1);

		JSONArray array = JSONArray.fromObject(l);
		obj.put("ResourceDtoList", l);

		System.out.println("obj:" + obj);
	}
}
