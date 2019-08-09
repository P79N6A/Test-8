package com.tydic.beijing.bvalue;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.ITradeSku;

public class TradeSkuTest {

	@Test
	public void testTradeExchange() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "tradeSkuTest.xml" });
		context.start();

		ITradeSku ie = (ITradeSku) context.getBean("sku");
		JSONObject obj = new JSONObject();
		obj.put("JDPin", "xingzm@tydic.com.cn");
		obj.put("UserLevel", "3");
		obj.put("OrderType", "1");
		obj.put("OrderNO", System.currentTimeMillis() + "");
		obj.put("OrderAmount", "900");
		obj.put("OrderCompletionTime", "2015-07-01 00-00-01");
		obj.put("OrgOrderNO", "1435307240708");

		JSONArray l = new JSONArray();
		JSONObject b1 = new JSONObject();
		b1.put("SKUID", "8000001");
		b1.put("ChangeType", "109");
		b1.put("Quantity", 2);
		b1.put("BValue", "100");
		l.add(b1);
		obj.put("SKUDtoList", l);

		System.out.println("obj:" + obj);
		JSONObject ret = ie.tradeSKU(obj);

		System.out.println("status=" + ret.get("Status"));
		System.out.println("ErrorCode=" + ret.get("ErrorCode"));
		System.out.println("ErrorMessage=" + ret.get("ErrorMessage"));

	}

	public void batch() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "tradeSkuTest.xml" });
		context.start();

		ITradeSku ie = (ITradeSku) context.getBean("sku");
		JSONObject obj = new JSONObject();
		for (int i = 0; i < 100; i++) {
			obj.put("JDPin", "xingzm@tydic.com" + i);
			obj.put("UserLevel", "3");
			obj.put("OrderType", "1");
			obj.put("OrderNO", System.currentTimeMillis() + "");
			obj.put("OrderAmount", "900");
			obj.put("OrderCompletionTime", "2015-01-21 17-36-00");
			obj.put("OrgOrderNO", "1421739427668");

			JSONArray l = new JSONArray();
			JSONObject b1 = new JSONObject();
			b1.put("SKUID", "8000001");
			b1.put("ChangeType", "109");
			b1.put("Quantity", 2);
			b1.put("BValue", "1.1");
			l.add(b1);
			obj.put("SKUDtoList", l);

			System.out.println("obj:" + obj);
			JSONObject ret = ie.tradeSKU(obj);

			System.out.println("status=" + ret.get("Status"));
			System.out.println("ErrorCode=" + ret.get("ErrorCode"));
			System.out.println("ErrorMessage=" + ret.get("ErrorMessage"));
			obj.clear();
		}
	}
}
