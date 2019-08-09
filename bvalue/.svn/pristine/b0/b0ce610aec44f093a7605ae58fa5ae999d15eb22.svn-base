package com.tydic.beijing.bvalue;

import java.text.MessageFormat;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.biz.TradeExchangeOper;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.dao.TbSmsSendHis;

public class QueryTest {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "sku.xml" });
		context.start();

		TradeExchangeOper t = (TradeExchangeOper) context.getBean("tradeExchangeOper");
		String msg = "您好，由于您上月购物产品出现退货情况，根据您的退货记录，我们已扣除您{0}B值，您目前剩余{1}B值。【京东通信】";
		String newMsg = MessageFormat.format(msg, 1, 2);
		TbSmsSendHis sendHis = new TbSmsSendHis();
		sendHis.setMsisdn_send("10023");
		sendHis.setMsisdn_receive("3897428932");
		sendHis.setMessage_text(newMsg);
		sendHis.setProcess_tag("0");
		sendHis.setPara_key("aoc.dic.bvaluecancel");
		sendHis.setSend_time(DateUtil.getSystemTime());

		// t.insertExchangeMessage(sendHis);
	}
}
