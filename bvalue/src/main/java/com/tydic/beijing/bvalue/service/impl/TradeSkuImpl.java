/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.biz.TradeExchangeOper;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.dao.LogTrade;
import com.tydic.beijing.bvalue.dao.LogTradeSku;
import com.tydic.beijing.bvalue.service.ITradeSku;

/**
 * SKU订单接口<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class TradeSkuImpl implements ITradeSku {
	private static final Logger log = Logger.getLogger(TradeSkuImpl.class);

	@Autowired
	private TradeExchangeOper tradeExchangeOper;

	public TradeExchangeOper getTradeExchangeOper() {
		return tradeExchangeOper;
	}

	public void setTradeExchangeOper(TradeExchangeOper tradeExchangeOper) {
		this.tradeExchangeOper = tradeExchangeOper;
	}

	@Override
	public JSONObject tradeSKU(JSONObject in) {
		log.info("MESSAGE:" + in.toString());
		long time1 = System.currentTimeMillis();
		
		JSONObject ret = new JSONObject();
		try {
			
			

			String currDate = DateUtil.getSystemTime();
			int currMonth = DateUtil.getCurrMonthForTypeDecimal();
			String tradeId = Common.getUUID();
			String userId = null;
			String jDPin = (String) in.get("JDPin");// 京东帐号
			log.debug("京东帐号[" + jDPin + "]");
			if (jDPin == null || jDPin.isEmpty()) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.JDPIN_NOT_SUPPLY);
				ret.put("ErrorMessage", "京东帐号未提供");
				log.error("京东帐号未提供");
				return ret;
			}
			userId = Common.md5(jDPin);
			log.debug("user id[" + userId + "]");

			// 检查是否开户，没开就开，然后成功
			// if (!tradeExchangeOper.ifAlreadyExisted(userId)) {
			// tradeExchangeOper.updateOpenInfoUser(jDPin, "109",
			// DateUtil.getSystemTime());
			// }

			String userLevel = (String) in.get("UserLevel");// 京东账户等级
			log.debug("京东账户等级[" + userLevel + "]");
			if (userLevel == null || userLevel.isEmpty()) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.JD_USER_LEVEL_NOT_SUPPLY);
				ret.put("ErrorMessage", "京东账户用户等级未提供");
				log.error("京东账户用户等级未提供");
				return ret;
			}

			String orderType = (String) in.get("OrderType");// 订单类型
			log.debug("订单类型[" + orderType + "]");
			if (orderType == null || orderType.isEmpty()) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDRE_TYPE_NOT_SUPPLY);
				ret.put("ErrorMessage", "订单类型未提供");
				log.error("订单类型未提供");
				return ret;
			}

			if (orderType.length() > 1 || "123".indexOf(orderType) < 0) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDRE_TYPE_NOT_SUPPLY);
				ret.put("ErrorMessage", "订单类型取值错误，orderType[" + orderType + "]，范围[1,2,3]");
				log.error("订单类型取值错误，orderType[" + orderType + "]，范围[1,2,3]");
				return ret;
			}
			String tradeTypeCode = null;
			if ("1".equals(orderType)) {
				tradeTypeCode = "101";
			} else if ("2".equals(orderType)) {
				tradeTypeCode = "201";
			} else {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.CHANGE_GOODS_NOT_SUPPORT);
				ret.put("ErrorMessage", "订单类型[3]，暂不支持换货或取值错误，范围[1,2,3]");
				log.error("订单类型[3]，暂不支持换货或取值错误，范围[1,2,3]");
				return ret;
			}

			String orderNO = (String) in.get("OrderNO");// 京东订单号
			log.debug("京东订单号[" + orderNO + "]");
			if (orderNO == null || orderNO.isEmpty()) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_NO_NOT_SUPPLY);
				ret.put("ErrorMessage", "订单号未提供");
				log.error("订单号未提供");
				return ret;
			}
			// 验证订单号长度
			if (orderNO.length() > Constants.ORDER_NO_MAX_LENGTH) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_NO_MORE_THAN_20);
				ret.put("ErrorMessage", "订单号长度不能大于20");
				log.error("订单号长度不能大于20");
				return ret;
			}
			
			long time2 = System.currentTimeMillis();
		//	log.debug(userId+"校验耗时"+(time2-time1));
			
			/*
			 * 
			 * 检查订单号是否重复处理
			 */
			//TODO 重复校验改到后端执行
//			if ("1".equals(orderType)) {// 购物
//				if (tradeExchangeOper.isDuplicated(userId, orderNO, "101")) {
//					ret.put("Status", Constants.STATUS_FAILURE);
//					ret.put("ErrorCode", Constants.ORDER_NO_IS_DUPLICATED);
//					ret.put("ErrorMessage", "该订单已经被处理过，为重复订单，订单号[" + orderNO + "]");
//					log.error("该订单已经被处理过，为重复订单，订单号[" + orderNO + "]");
//					return ret;
//				}
//			} else {// 退货
//				if (tradeExchangeOper.isDuplicated(userId, orderNO, "201")) {
//					ret.put("Status", Constants.STATUS_FAILURE);
//					ret.put("ErrorCode", Constants.ORDER_NO_IS_DUPLICATED);
//					ret.put("ErrorMessage", "该订单已经被处理过，为重复订单，订单号[" + orderNO + "]");
//					log.error("该订单已经被处理过，为重复订单，订单号[" + orderNO + "]");
//					return ret;
//				}
//			}
			
//			long time3 = System.currentTimeMillis();
//			log.debug("主流程："+userId+"校验重复耗时"+(time3-time2));

			String orderAmount = (String) in.get("OrderAmount");// 订单金额
			log.debug("订单金额[" + orderAmount + "]");
			if (orderAmount == null || orderAmount.isEmpty()) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_AMOUNT_NOT_SUPPLY);
				ret.put("ErrorMessage", "订单金额未提供");
				log.error("订单金额未提供");
				return ret;
			}
			// 取整
			try {
				long s = (long) Double.parseDouble(orderAmount);
				orderAmount = String.valueOf(s);
			} catch (NumberFormatException e) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_AMOUNT_NOT_SUPPLY);
				ret.put("ErrorMessage", "订单金额格式不正确");
				log.error("订单金额格式不正确");
				return ret;
			}
			log.debug("\t订单金额，去点后[" + orderAmount + "]");

			String orderCompletionTime = (String) in.get("OrderCompletionTime");// 完成时间
			log.debug("完成时间[" + orderCompletionTime + "]");
			if (orderCompletionTime == null || orderCompletionTime.isEmpty()) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_COMPLETION_TIME_NOT_SUPPLY);
				ret.put("ErrorMessage", "订单完成时间未提供");
				log.error("订单完成时间未提供");
				return ret;
			}
			try {
				orderCompletionTime = DateUtil.changeToInternalFormat(orderCompletionTime);
			} catch (ParseException e) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.DATE_FORMAT_IS_INVALID);
				ret.put("ErrorMessage", "日期格式不正确");
				log.error("日期格式不正确");
				return ret;
			}

			long timex = System.currentTimeMillis();
			String orgOrderNO = (String) in.get("OrgOrderNO");// 原订单号
			log.debug("原订单号[" + orgOrderNO + "]");
			if ("2".equals(orderType)) {
				if (orgOrderNO == null || orgOrderNO.isEmpty()) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.ORG_ORDER_NO_NOT_SUPPLY);
					ret.put("ErrorMessage", "原始订单号未提供");
					log.error("原始订单号未提供");
					return ret;
				}

				// 验证原始订单号是否存在
				//TODO 
				//sku退校验改到后端执行
//				if (tradeExchangeOper.isOrgNoNotFound(userId, orgOrderNO, "101")) {
//					ret.put("Status", Constants.STATUS_FAILURE);
//					ret.put("ErrorCode", Constants.ORG_ORDER_NO_NOT_SUPPLY);
//					ret.put("ErrorMessage", "原始订单号不存在");
//					log.error("原始订单号不存在");
//					return ret;
//				}
			}
			
			long timey = System.currentTimeMillis();
            log.debug("主流程：购物退校验原订单耗时"+(timey-timex));
			
			Object sKUDtoList = in.get("SKUDtoList");// SKU列表
			if (sKUDtoList == null) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.SKU_LIST_NOT_SUPPLY);
				ret.put("ErrorMessage", "SKU列表未提供");
				log.error("SKU列表未提供");
				return ret;
			}
			log.debug("SKU列表:");

			JSONArray sKUArray = JSONArray.fromObject(sKUDtoList);
			if (sKUArray.size() == 0) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.SKU_LIST_IS_NULL);
				ret.put("ErrorMessage", "SKU列表不能为空");
				log.error("SKU列表不能为空");
				return ret;
			}
			List<LogTradeSku> logTradeSkus = new ArrayList<LogTradeSku>();
			int seq = 0;
			for (Object o : sKUArray) {
				JSONObject jso = (JSONObject) o;
				String sKUID = (String) jso.get("SKUID");// SKU ID
				log.debug("\tSKU ID[" + sKUID + "]");
				if (sKUID == null || sKUID.isEmpty()) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.SKU_ID_NOT_SUPPLY);
					ret.put("ErrorMessage", "SKU ID不能为空");
					log.error("SKU ID不能为空");
					return ret;
				}

				String changeType = (String) jso.get("ChangeType");// 换货类型
				log.debug("\t换货类型[" + changeType + "]");
				if ("3".equals(orderType)) {
					if (changeType == null || changeType.isEmpty()) {
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.CHANGE_TYPE_NOT_SUPPLY);
						ret.put("ErrorMessage", "OrderType为3时，换货类型不能为空");
						log.error("OrderType为3时，换货类型不能为空");
						return ret;
					}
				}

				long quantity = jso.getLong("Quantity");// 购买数量
				log.debug("\t购买数量[" + quantity + "]");

				if (quantity <= 0) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.QUANTITY_IS_ZERO);
					ret.put("ErrorMessage", "购买数量不能为0");
					log.error("购买数量不能为0");
					return ret;
				}

				String bValue = (String) jso.get("BValue");// SKU对应B值单价
				log.debug("\tSKU对应B值单价[" + bValue + "]");
				if (bValue == null || bValue.isEmpty()) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.BVALUE_NOT_SUPPLY);
					ret.put("ErrorMessage", "SKU对应B值单价不能为空");
					log.error("SKU对应B值单价不能为空");
					return ret;
				}
				try {
					long s = (long) Double.parseDouble(bValue);
					bValue = String.valueOf(s);
				} catch (NumberFormatException e) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.BVALUE_NOT_SUPPLY);
					ret.put("ErrorMessage", "SKU对应B值单价格式不正确");
					log.error("SKU对应B值单价格式不正确");
					return ret;
				}
				log.debug("\tSKU对应B值单价，去点后[" + bValue + "]");

				LogTradeSku logTradeSku = new LogTradeSku();
				logTradeSku.setTrade_id(tradeId);
				logTradeSku.setTrade_type_code(tradeTypeCode);
				logTradeSku.setUser_id(userId);
				logTradeSku.setPartition_id(currMonth);
				logTradeSku.setOrder_no(orderNO);
				logTradeSku.setOrder_type(orderType);
				logTradeSku.setSub_order_type("0");
				logTradeSku.setOrder_number(seq++);
				logTradeSku.setSku_id(sKUID);
				logTradeSku.setPurchase_quantity(quantity);
				logTradeSku.setB_value(Long.parseLong(bValue));
				logTradeSku.setOrg_order_no(orgOrderNO);
				logTradeSku.setBalance(0);
				logTradeSku.setUnit_type_id(0);
				logTradeSku.setOld_balance(0);
				logTradeSku.setNew_balance(0);
				logTradeSku.setOvertop_value(0);
				logTradeSku.setProcess_tag(0);
				logTradeSku.setProcess_time(DateUtil.getSystemTime());
				logTradeSku.setReserve_1("");
				logTradeSku.setReserve_2("");
				logTradeSku.setReserve_3("");
				logTradeSku.setReserve_4("");
				logTradeSkus.add(logTradeSku);
			}// for

			// 交易记录表

			LogTrade logTrade = new LogTrade();
			logTrade.setTrade_id(tradeId);
			logTrade.setTrade_type_code(tradeTypeCode);
			logTrade.setExternal_system_code("10000");
			logTrade.setUser_id(userId);
			logTrade.setPartition_id(currMonth);
			logTrade.setOrder_no(orderNO);
			logTrade.setOrder_type(orderType);
			logTrade.setOrder_amount(Long.parseLong(orderAmount));
			logTrade.setOrder_completion_time(orderCompletionTime);
			logTrade.setBalance_type_id(0);
			logTrade.setUnit_type_id(0);// 0 B值
			logTrade.setBalance(0);
			logTrade.setProcess_tag(0);
			logTrade.setTrade_time(currDate);
			logTrade.setProcess_time(currDate);
			logTrade.setReserve_c1(jDPin);// 京东帐号jdpin

			// 如表log_trade
			tradeExchangeOper.updateTradeSku(logTrade, logTradeSkus);

			ret.put("Status", Constants.STATUS_SUCCESS);
			log.info("RESULT:" + ret.toString());
		} catch (Exception e) {
			ret.put("Status", Constants.STATUS_FAILURE);
			ret.put("ErrorCode", Constants.SYSTEM_EXCEPTION);
			ret.put("ErrorMessage", "接口异常");
			log.error(e);
			return ret;
		}
		
		long timen = System.currentTimeMillis();
		log.debug("主流程：接口总耗时"+(timen-time1));
		return ret;
	}
}
