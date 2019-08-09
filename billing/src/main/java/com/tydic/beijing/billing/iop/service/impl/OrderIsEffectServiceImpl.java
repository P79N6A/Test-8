package com.tydic.beijing.billing.iop.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dto.OrderIsEffect;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

import net.sf.json.JSONObject;
import oracle.net.aso.e;

public class OrderIsEffectServiceImpl implements com.tydic.beijing.billing.iop.service.OrderIsEffectService {
	private static Logger log = Logger.getLogger(OrderIsEffectServiceImpl.class);

	@Override
	public JSONObject getIsEffect(JSONObject input) {
		JSONObject output = new JSONObject();
		output.put("errorCode", "1");
		try {
		// 入参校验
		if (!checkInput(input)) {
			log.error("入参数错误!");
			output.put("errorCode", "0");
			output.put("errorMsg", "入参数错误!");
			log.debug("最后结果是："+output);
			return output;
		}
		String orderid = input.getString("orderid");
		String imsi = input.getString("imsi");
		String productid = input.getString("productid");
		String startdate = input.getString("startdate");
		String enddate = input.getString("enddate");

		
			// 计算需要查询的月份
			String month = "";
			String nextmonth="";
			String startmonth = startdate.substring(4, 6);
			log.debug("生失效的月份分别为：" + startmonth);
			month=startmonth;
			int nextMonth=Integer.parseInt(month);
			if (nextMonth==12) {
				nextmonth="01";
			}else {
				nextmonth=(nextMonth+1)+"";
			}
			if (nextmonth.length()==1) {
				nextmonth="0"+nextmonth;
			}
			// 查询cdr_gprs表是否有话单
			List<OrderIsEffect> orderIsEffects = S.get(OrderIsEffect.class)
					.query(Condition.build("queryCdrGprs").filter("startdate", startdate).filter("enddate", enddate)
							.filter("imsi", imsi).filter("productid", productid).filter("month", month).filter("nextmonth",nextmonth));
			output.put("orderid", orderid);
			output.put("imsi", imsi);
			output.put("productid", productid);
			output.put("startdate", startdate);
			output.put("enddate", enddate);

			if (null == orderIsEffects || orderIsEffects.size() == 0) {
				output.put("result", "0");
			} else {
				output.put("result", "1");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			output.put("errorCode", "0");
			output.put("errorMsg", e.toString());
		}
		log.debug("最后结果是："+output);
		return output;
	}

	// 检验入参
	public boolean checkInput(JSONObject input) throws Exception{
		if (null == input.get("orderid")||input.get("orderid").equals("")) {
			log.debug("入参中没有orderid值");
			return false;
		}
		if (null == input.get("imsi")||input.get("imsi").equals("")) {
			log.debug("入参中没有imsi值");
			return false;
		}
		if (null == input.get("productid")||input.get("productid").equals("") ) {
			log.debug("入参中没有productid值");
			return false;
		}
		if (null == input.get("startdate")||input.get("startdate").equals("")) {
			log.debug("入参中没有startdate值");
			return false;
		}
		if (null == input.get("enddate")||input.get("enddate").equals("")) {
			log.debug("入参中没有enddate值");
			return false;
		}
		return true;
	}
}
