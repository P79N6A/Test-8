package com.tydic.beijing.bvalue;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.biz.TradeExchangeOper;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.LogTradeExchangeHis;

public class TableOperTest {
	// @Test
	public void LogTradeExchangeHis() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "tradeExchange.xml" });
		context.start();
		TradeExchangeOper oper = (TradeExchangeOper) context.getBean("tradeExchangeOper");
		LogTradeExchangeHis log = new LogTradeExchangeHis();
		log.setTrade_id("21321");
		log.setUser_id("mmmmmmdddd");
		log.setTrade_type_code("1");
		log.setResource_value(1);
		log.setResource_type_code("324");
		log.setEff_date("20150108145500");
		log.setExchange_time("20150108145501");
		log.setExp_date("20150108145502");
		log.setPartition_id(8);
		try {
			oper.iLogTradeExchangeHis(log);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testQuery() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "tradeExchange.xml" });
		context.start();
		TradeExchangeOper oper = (TradeExchangeOper) context.getBean("tradeExchangeOper");
		List<InfoPayBalance> l = oper.getInfoPayBalance("1", "1" , "1");
		if (l != null)
			for (InfoPayBalance i : l) {
				System.err.println(i);
			}
		else {
			System.err.println("null");
		}
	}

	public static void main(String[] args) throws Exception {
		testQuery();
		// ClassPathXmlApplicationContext context = new
		// ClassPathXmlApplicationContext(
		// new String[] { "tradeExchange.xml" });
		// context.start();
		// TradeExchangeOper oper = (TradeExchangeOper)
		// context.getBean("tradeExchangeOper");
		// InfoPayBalance ip = new InfoPayBalance();
		// ip.setBalance_id(200001);
		// ip.setBalance(800);
		// oper.updateInfoPayBalanceByBalanceId(ip);
	}
}
