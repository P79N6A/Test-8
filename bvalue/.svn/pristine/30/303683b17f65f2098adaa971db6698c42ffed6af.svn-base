package com.tydic.beijing.bvalue;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.dto.ExchangeDtoAutoExchange;
import com.tydic.beijing.bvalue.dto.ResourceDto;
import com.tydic.beijing.bvalue.service.ISetAutoExchange;

public class SetAutoTradeExchangeTest {

	public void generateInsert() {
		JSONObject obj = new JSONObject();
		obj.put("jDPin", "xingzm@tydic.com");
		obj.put("mobileNumber", "18600578727");
		obj.put("channelType", "0");
		obj.put("orderNo", "93724932");

		// resource dto list 1
		List<ResourceDto> l1 = new ArrayList<ResourceDto>();
		ResourceDto res0 = new ResourceDto();
		res0.setResourceType("2");
		res0.setTotalResource("100");

		ResourceDto res1 = new ResourceDto();
		res1.setResourceType("1");
		res1.setTotalResource("200");

		l1.add(res0);
		l1.add(res1);
		JSONArray array2 = JSONArray.fromObject(l1);

		// resource dto list 1
		List<ResourceDto> l2 = new ArrayList<ResourceDto>();
		ResourceDto res3 = new ResourceDto();
		res3.setResourceType("2");
		res3.setTotalResource("300");

		ResourceDto res4 = new ResourceDto();
		res4.setResourceType("1");
		res4.setTotalResource("400");

		l2.add(res3);
		l2.add(res4);

		// ExchangeDtoAutoExchange
		List<ExchangeDtoAutoExchange> exchangeDtoAutoExchangeList = new ArrayList<ExchangeDtoAutoExchange>();
		ExchangeDtoAutoExchange exchangeDtoAutoExchange1 = new ExchangeDtoAutoExchange();
		exchangeDtoAutoExchange1.setOperationType("1");
		exchangeDtoAutoExchange1.setExchageID("382983");
		exchangeDtoAutoExchange1.setbValueExchangeMode("2");
		exchangeDtoAutoExchange1.setTopBValue("500");
		exchangeDtoAutoExchange1.setResourceExchangeMode("2");
		exchangeDtoAutoExchange1.setResourceDtoList(l1);
		exchangeDtoAutoExchange1.setEffDate(DateUtil.getSystemTime());
		exchangeDtoAutoExchange1.setExpDate(DateUtil.getSystemTime());

		ExchangeDtoAutoExchange exchangeDtoAutoExchange2 = new ExchangeDtoAutoExchange();
		exchangeDtoAutoExchange2.setOperationType("1");
		exchangeDtoAutoExchange2.setExchageID("382983");
		exchangeDtoAutoExchange2.setbValueExchangeMode("2");
		exchangeDtoAutoExchange2.setTopBValue("300");
		exchangeDtoAutoExchange2.setResourceExchangeMode("2");
		exchangeDtoAutoExchange2.setResourceDtoList(l2);
		exchangeDtoAutoExchange2.setEffDate(DateUtil.getSystemTime());
		exchangeDtoAutoExchange2.setExpDate(DateUtil.getSystemTime());
		// list
		exchangeDtoAutoExchangeList.add(exchangeDtoAutoExchange1);
		exchangeDtoAutoExchangeList.add(exchangeDtoAutoExchange2);

		JSONArray array = JSONArray.fromObject(exchangeDtoAutoExchangeList);
		obj.put("exchangtDtolist", array);

		System.err.println(obj);
	}

	public void generateUpdate() {
		JSONObject obj = new JSONObject();
		obj.put("jDPin", "xingzm@tydic.com");
		obj.put("mobileNumber", "18600578727");
		obj.put("channelType", "0");
		obj.put("orderNo", "93724932");
		List<ResourceDto> l1 = new ArrayList<ResourceDto>();
		ResourceDto res0 = new ResourceDto();
		res0.setResourceType("2");
		res0.setTotalResource("100");

		ResourceDto res1 = new ResourceDto();
		res1.setResourceType("1");
		res1.setTotalResource("200");

		l1.add(res0);
		l1.add(res1);

		// ExchangeDtoAutoExchange
		List<ExchangeDtoAutoExchange> exchangeDtoAutoExchangeList = new ArrayList<ExchangeDtoAutoExchange>();
		ExchangeDtoAutoExchange exchangeDtoAutoExchange1 = new ExchangeDtoAutoExchange();
		exchangeDtoAutoExchange1.setOperationType("2");
		exchangeDtoAutoExchange1.setExchageID("382982");
		exchangeDtoAutoExchange1.setbValueExchangeMode("2");
		exchangeDtoAutoExchange1.setTopBValue("500");
		exchangeDtoAutoExchange1.setResourceExchangeMode("2");
		exchangeDtoAutoExchange1.setResourceDtoList(l1);
		exchangeDtoAutoExchange1.setEffDate(DateUtil.getSystemTime());
		exchangeDtoAutoExchange1.setExpDate(DateUtil.getSystemTime());

		exchangeDtoAutoExchangeList.add(exchangeDtoAutoExchange1);

		JSONArray array = JSONArray.fromObject(exchangeDtoAutoExchangeList);
		obj.put("exchangtDtolist", array);

		System.err.println(obj);
	}

	public void generateAll() {
		JSONObject obj = new JSONObject();
		obj.put("MDPin", "xingzm@tydic.com");
		obj.put("MobileNumber", "18600578727");
		obj.put("ChannelType", "0");
		obj.put("OrderNo", "93724932");

		// resource dto list 1
		List<ResourceDto> l1 = new ArrayList<ResourceDto>();
		ResourceDto res0 = new ResourceDto();
		res0.setResourceType("2");
		res0.setTotalResource("100");

		ResourceDto res1 = new ResourceDto();
		res1.setResourceType("1");
		res1.setTotalResource("200");

		l1.add(res0);
		l1.add(res1);
		JSONArray array2 = JSONArray.fromObject(l1);

		// resource dto list 1
		List<ResourceDto> l2 = new ArrayList<ResourceDto>();
		ResourceDto res3 = new ResourceDto();
		res3.setResourceType("2");
		res3.setTotalResource("300");

		ResourceDto res4 = new ResourceDto();
		res4.setResourceType("1");
		res4.setTotalResource("400");

		l2.add(res3);
		l2.add(res4);

		// ExchangeDtoAutoExchange
		List<ExchangeDtoAutoExchange> exchangeDtoAutoExchangeList = new ArrayList<ExchangeDtoAutoExchange>();
		ExchangeDtoAutoExchange exchangeDtoAutoExchange1 = new ExchangeDtoAutoExchange();
		exchangeDtoAutoExchange1.setOperationType("1");
		exchangeDtoAutoExchange1.setExchageID("382983");
		exchangeDtoAutoExchange1.setbValueExchangeMode("2");
		exchangeDtoAutoExchange1.setTopBValue("500");
		exchangeDtoAutoExchange1.setResourceExchangeMode("2");
		exchangeDtoAutoExchange1.setResourceDtoList(l1);
		exchangeDtoAutoExchange1.setEffDate(DateUtil.getSystemTime());
		exchangeDtoAutoExchange1.setExpDate(DateUtil.getSystemTime());

		ExchangeDtoAutoExchange exchangeDtoAutoExchange2 = new ExchangeDtoAutoExchange();
		exchangeDtoAutoExchange2.setOperationType("2");
		exchangeDtoAutoExchange2.setExchageID("382982");
		exchangeDtoAutoExchange2.setbValueExchangeMode("2");
		exchangeDtoAutoExchange2.setTopBValue("300");
		exchangeDtoAutoExchange2.setResourceExchangeMode("2");
		exchangeDtoAutoExchange2.setResourceDtoList(l2);
		exchangeDtoAutoExchange2.setEffDate(DateUtil.getSystemTime());
		exchangeDtoAutoExchange2.setExpDate(DateUtil.getSystemTime());
		// list
		exchangeDtoAutoExchangeList.add(exchangeDtoAutoExchange1);
		exchangeDtoAutoExchangeList.add(exchangeDtoAutoExchange2);

		JSONArray array = JSONArray.fromObject(exchangeDtoAutoExchangeList);
		obj.put("exchangtDtolist", array);

		System.err.println(obj);
	}

	public void testInsert() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "setAutoTradeExchangeTest.xml" });
		context.start();

		ISetAutoExchange ie = (ISetAutoExchange) context.getBean("set");
		JSONObject obj = new JSONObject();
		obj.put("jDPin", "xingzm@tydic.com");
		obj.put("mobileNumber", "18600578727");
		obj.put("channelType", "0");
		obj.put("orderNo", "93724932");

		// resource dto list 1
		List<ResourceDto> l1 = new ArrayList<ResourceDto>();
		ResourceDto res0 = new ResourceDto();
		res0.setResourceType("2");
		res0.setTotalResource("100");

		ResourceDto res1 = new ResourceDto();
		res1.setResourceType("1");
		res1.setTotalResource("200");

		l1.add(res0);
		l1.add(res1);
		JSONArray array2 = JSONArray.fromObject(l1);

		// resource dto list 1
		List<ResourceDto> l2 = new ArrayList<ResourceDto>();
		ResourceDto res3 = new ResourceDto();
		res3.setResourceType("2");
		res3.setTotalResource("300");

		ResourceDto res4 = new ResourceDto();
		res4.setResourceType("1");
		res4.setTotalResource("400");

		l2.add(res3);
		l2.add(res4);

		// ExchangeDtoAutoExchange
		List<ExchangeDtoAutoExchange> exchangeDtoAutoExchangeList = new ArrayList<ExchangeDtoAutoExchange>();
		ExchangeDtoAutoExchange exchangeDtoAutoExchange1 = new ExchangeDtoAutoExchange();
		exchangeDtoAutoExchange1.setOperationType("1");
		exchangeDtoAutoExchange1.setExchageID("9999001");
		exchangeDtoAutoExchange1.setbValueExchangeMode("2");
		exchangeDtoAutoExchange1.setTopBValue("500");
		exchangeDtoAutoExchange1.setResourceExchangeMode("2");
		exchangeDtoAutoExchange1.setResourceDtoList(l1);
		exchangeDtoAutoExchange1.setEffDate(DateUtil.getSystemTime());
		exchangeDtoAutoExchange1.setExpDate(DateUtil.getSystemTime());

		ExchangeDtoAutoExchange exchangeDtoAutoExchange2 = new ExchangeDtoAutoExchange();
		exchangeDtoAutoExchange2.setOperationType("1");
		exchangeDtoAutoExchange2.setExchageID("9999001");
		exchangeDtoAutoExchange2.setbValueExchangeMode("2");
		exchangeDtoAutoExchange2.setTopBValue("300");
		exchangeDtoAutoExchange2.setResourceExchangeMode("2");
		exchangeDtoAutoExchange2.setResourceDtoList(l2);
		exchangeDtoAutoExchange2.setEffDate(DateUtil.getSystemTime());
		exchangeDtoAutoExchange2.setExpDate(DateUtil.getSystemTime());
		// list
		exchangeDtoAutoExchangeList.add(exchangeDtoAutoExchange1);
		exchangeDtoAutoExchangeList.add(exchangeDtoAutoExchange2);

		JSONArray array = JSONArray.fromObject(exchangeDtoAutoExchangeList);
		obj.put("exchangtDtolist", array);

		System.err.println(obj);
		JSONObject ret = ie.set(obj);

		System.out.println("status=" + ret.get("status"));
		System.out.println("ErrorCode=" + ret.get("errorCode"));
		System.out.println("ErrorMessage=" + ret.get("errorMessage"));
	}

	public void testUpdate() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "setAutoTradeExchangeTest.xml" });
		context.start();

		ISetAutoExchange ie = (ISetAutoExchange) context.getBean("set");
		JSONObject obj = new JSONObject();
		obj.put("jDPin", "xingzm@tydic.com");
		obj.put("mobileNumber", "18600578727");
		obj.put("channelType", "0");
		obj.put("orderNo", "93724932");
		List<ResourceDto> l1 = new ArrayList<ResourceDto>();
		ResourceDto res0 = new ResourceDto();
		res0.setResourceType("2");
		res0.setTotalResource("100");

		ResourceDto res1 = new ResourceDto();
		res1.setResourceType("1");
		res1.setTotalResource("200");

		l1.add(res0);
		l1.add(res1);

		// ExchangeDtoAutoExchange
		List<ExchangeDtoAutoExchange> exchangeDtoAutoExchangeList = new ArrayList<ExchangeDtoAutoExchange>();
		ExchangeDtoAutoExchange exchangeDtoAutoExchange1 = new ExchangeDtoAutoExchange();
		exchangeDtoAutoExchange1.setOperationType("2");
		exchangeDtoAutoExchange1.setExchageID("9999001");
		exchangeDtoAutoExchange1.setbValueExchangeMode("2");
		exchangeDtoAutoExchange1.setTopBValue("500");
		exchangeDtoAutoExchange1.setResourceExchangeMode("2");
		exchangeDtoAutoExchange1.setResourceDtoList(l1);
		exchangeDtoAutoExchange1.setEffDate(DateUtil.getSystemTime());
		exchangeDtoAutoExchange1.setExpDate(DateUtil.getSystemTime());

		exchangeDtoAutoExchangeList.add(exchangeDtoAutoExchange1);

		JSONArray array = JSONArray.fromObject(exchangeDtoAutoExchangeList);
		obj.put("exchangtDtolist", array);

		System.err.println(obj);
		JSONObject ret = ie.set(obj);

		System.out.println("status=" + ret.get("status"));
		System.out.println("ErrorCode=" + ret.get("errorCode"));
		System.out.println("ErrorMessage=" + ret.get("errorMessage"));
	}

	@Test
	public void testAll() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "setAutoTradeExchangeTest.xml" });
		context.start();

		ISetAutoExchange ie = (ISetAutoExchange) context.getBean("set");
		String orderNO = System.currentTimeMillis() + "";
		String str = "{'JDPin':'xingzm@tydic.com','MobileNumber':'18600578727',"
				+ "'ChannelType':'109','OrderNo':'" + orderNO + "','ExchangtDtolist':[{"
				+ "'BValueExchangeMode':'1'," + "'EffDate':'2015-02-12 19-07-56',"
				+ "'ExchageID':'08b420c3fab444f488251053589707af',"
				+ "'ExpDate':'2015-02-28 23-07-59'," + "'OperationType':'2',"
				+ "'ResourceDtoList':[{'" + "ResourceType':'2'," + "'TotalResource':'100'},"
				+ "{'ResourceType':'1',"
				+ "'TotalResource':'200'}],'ResourceExchangeMode':'1','TopBValue':'300'}]}";
		JSONObject obj = JSONObject.fromObject(str);

		System.err.println(obj);
		JSONObject ret = ie.set(obj);

		System.out.println("status=" + ret.get("Status"));
		System.out.println("ErrorCode=" + ret.get("ErrorCode"));
		System.out.println("ErrorMessage=" + ret.get("ErrorMessage"));
		context.close();
	}
}
