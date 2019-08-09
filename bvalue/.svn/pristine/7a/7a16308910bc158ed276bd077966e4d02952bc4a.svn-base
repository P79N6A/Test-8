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
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.LifeResourceList;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchange;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchangeComposite;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeSetHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.service.ISetAutoExchange;

/**
 * B值自动兑换设置<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class SetAutoExchangeImpl implements ISetAutoExchange {

	private static final Logger log = Logger.getLogger(SetAutoExchangeImpl.class);
	@Autowired
	private TradeExchangeOper tradeExchangeOper;

	private static final String TRADE_TYPE_CODE = "505";
	private static final String CYCLE_TYPE = "COM";

	@Override
	public JSONObject set(JSONObject in) {
		log.info("MESSAGE:" + in.toString());

		JSONObject ret = new JSONObject();

		// {// ************************************************************
		// if (true) { 
		// ret.put("Status", Constants.STATUS_FAILURE);
		// ret.put("ErrorCode", Constants.JDPIN_NOT_SUPPLY);
		// ret.put("ErrorMessage", "此功能正在内测中，未对外开放");
		// log.error("此功能正在内测中，未对外开放");
		// return ret;
		// }
		// }// ************************************************************

		try {
			String userId = null;
			String currDate = DateUtil.getSystemTime();
			int partitionId = DateUtil.getCurrMonthForTypeDecimal();
			String tradeId = Common.getUUID();
			String jDPin = (String) in.get("JDPin");// 京东帐号
			log.debug("trade_id[" + tradeId + "]");
			log.debug("京东帐号[" + jDPin + "]");
			//验证京东账号
			if (jDPin == null || jDPin.isEmpty()) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.JDPIN_NOT_SUPPLY);
				ret.put("ErrorMessage", "京东帐号未提供");
				log.error("京东帐号未提供");
				return ret;
			}
			//获取用户的userId
			userId = Common.md5(jDPin);
			log.debug("用户的userId为"+userId);
			// 验证手机号码
			String mobileNumber = (String) in.get("MobileNumber");
			log.debug("手机号码[" + mobileNumber + "]");
			if (mobileNumber == null || mobileNumber.isEmpty()
					|| !Common.isMobileNumber(mobileNumber)) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.MOBILE_NOT_SUPPLY);
				ret.put("ErrorMessage", "手机号码未提供或格式不正确，应为11位数字");
				log.error("手机号码未提供或格式不正确，应为11位数字");
				return ret;
			}
			/**
			 * 验证京东帐号和手机号关联性
			 */
			InfoUserExternalAccount infoUserExternalAccount = tradeExchangeOper
					.getInfoUserExternalAccount(userId, currDate);
			if (infoUserExternalAccount == null) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.JDPIN_AND_MOBILE_NUMBER_NOT_CONTACT);
				ret.put("ErrorMessage", "京东帐号和手机号码未关联");
				log.error("京东帐号和手机号码未关联");
				return ret;
			}
			// 京东帐号和手机号码不是关联的
			if (!mobileNumber.equals(infoUserExternalAccount.getExternal_account_code())) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.JDPIN_AND_MOBILE_NUMBER_NOT_CONTACT);
				ret.put("ErrorMessage", "京东帐号和手机号码不是关联的");
				log.error("京东帐号和手机号码不是关联的");
				return ret;
			}
			// 渠道类型
			String channelType = (String) in.get("ChannelType");
			log.debug("渠道类型[" + channelType + "]");
			// if (channelType == null || channelType.isEmpty() ||
			// !"0000".equals(channelType)) {// 不等于109
			// ret.put("Status", Constants.STATUS_FAILURE);
			// ret.put("ErrorCode", Constants.ORDER_NO_NOT_SUPPLY);
			// ret.put("ErrorMessage", "渠道编码不正确");
			// log.error("渠道编码不正确");
			// return ret;
			// }
			// 订单号
			String orderNo = (String) in.get("OrderNo");
			log.debug("订单号[" + orderNo + "]");
			// 验证订单号长度
			if (orderNo != null && orderNo.length() > Constants.ORDER_NO_MAX_LENGTH) {
				ret.put("Status", Constants.STATUS_FAILURE);
				ret.put("ErrorCode", Constants.ORDER_NO_MORE_THAN_20);
				ret.put("ErrorMessage", "订单号长度不能大于20");
				log.error("订单号长度不能大于20");
				return ret;
			}

			List<LifeUserAutoExchangeComposite> updateAutoExchange = new ArrayList<LifeUserAutoExchangeComposite>();
			List<LogTradeAutoExchangeSetHis> logTradeAutoExchangeSetHisList = new ArrayList<LogTradeAutoExchangeSetHis>();
			List<LifeUserAutoExchange> deleteAutoExchange = new ArrayList<LifeUserAutoExchange>();
			LifeUserAutoExchangeComposite lifeUserAutoExchangeComposite = new LifeUserAutoExchangeComposite();
			
				
				LifeUserAutoExchange lifeUserAutoExchange = new LifeUserAutoExchange();
				
				// 操作类型
				String operationType = (String) in.get("OperationType");
				log.debug("\t操作类型[" + operationType + "]");
				if (operationType == null || operationType.isEmpty()) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.OPERATION_TYPE_NOT_SUPPLY);
					ret.put("ErrorMessage", "操作类型未提供");
					log.error("操作类型未提供");
					return ret;
				}
				if ("123".indexOf(operationType) < 0) {
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.OPERATION_TYPE_NOT_12);
					ret.put("ErrorMessage", "操作类型取值不正确，operationType=" + operationType + ",范围[1,2,3]");
					log.error("操作类型取值不正确，operationType=" + operationType + ",范围[1,2,3]");
					return ret;
				}

				// 新增
				if ("1".equals(operationType)) {
					//判断是否存在预约设置
					LifeUserAutoExchange exchanges=tradeExchangeOper.newasExistedLifeUserAutoExchange(userId, currDate);
					if(exchanges==null){
					String exchageId = Common.getUUID();// 自动兑换实例ID
					log.debug("\t自动兑换实例ID[" + exchageId + "]");
					// B值兑换方式
					String bValueExchangeMode = (String) in.get("BValueExchangeMode");
					log.debug("\tB值兑换方式[" + bValueExchangeMode + "]");
					if (bValueExchangeMode == null || bValueExchangeMode.isEmpty()) {
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.B_VALUE_EXCHANGE_MODE_NOT_SUPPLY);
						ret.put("ErrorMessage", "B值兑换方式未提供");
						log.error("B值兑换方式未提供");
						return ret;
					}
					// B值封顶值
					String topBValue = (String) in.get("TopBValue");
					log.debug("\tB值封顶值[" + topBValue + "]");
					if ("2".equals(bValueExchangeMode)// 封顶
							&& (topBValue == null || topBValue.isEmpty())) {
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.TOP_VALUE_NOT_SUPPLY);
						ret.put("ErrorMessage", "新增操作并且为封顶值时，B值封顶值字段必填");
						log.error("新增操作并且为封顶值时，B值封顶值字段必填");
						return ret;
					} else {// 全部，bValueExchangeMode=1
						if (topBValue == null || topBValue.isEmpty()) {
							topBValue = "0";// 默认值
						}
					}
					long topValue = Long.parseLong(topBValue);
					
					// 兑换类型
					String resourceExchangeMode = (String) in.get("ResourceExchangeMode");
					log.debug("\t兑换类型[" + resourceExchangeMode + "]");
					if (resourceExchangeMode == null || resourceExchangeMode.isEmpty()) {
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.RESOURCE_EXCHANGE_MODE_NOT_SUPPLY);
						ret.put("ErrorMessage", "新增操作时，兑换类型字段必填");
						log.error("新增操作时，兑换类型字段必填");
						return ret;
					}
					if(!resourceExchangeMode.equals("2")){
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.RESOURCE_EXCHANGE_MODE_ERRO );
						ret.put("ErrorMessage", "兑换类型只支持按比例兑换");
						log.error("兑换类型只支持按比例兑换");
						return ret;
					}

					
					
					
					
					// 新增时兑换ID由系统生成
					String exchangeId = Common.getUUID();
					lifeUserAutoExchange.setExchange_id(exchangeId);
					lifeUserAutoExchange.setCycle_type(CYCLE_TYPE);
					String valueExchangeMode = null;
					if ("1".equals(bValueExchangeMode)) {
						valueExchangeMode = "001";
					} else if ("2".equals(bValueExchangeMode)) {
						valueExchangeMode = "002";
					} else {
						valueExchangeMode = "001";
					}

					lifeUserAutoExchange.setPurchase_mode(valueExchangeMode);
					lifeUserAutoExchange.setTop_b_value(topValue);
					lifeUserAutoExchange.setExchange_mode(resourceExchangeMode);
					
					//生失效时间
					String effDate=DateUtil.getBeginningOfNextMonth();
					String expDate="20501231235959";
					
					// 列表ID
					String exchangeListUUID = Common.getUUID();
					lifeUserAutoExchange.setResource_list_id(exchangeListUUID);

					List<LifeResourceList> resourceDtoList = new ArrayList<LifeResourceList>();
					// 兑换资源列表
					Object resourceDtoListObject = in.get("ResourceDtoList");
					if (resourceDtoListObject == null) {
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.RESOURCE_DTO_LIST_NOT_SUPPLY);
						ret.put("ErrorMessage", "新增操作时，兑换资源列表未提供");
						log.error("新增操作时，兑换资源列表未提供");
						return ret;
					}
					log.debug("\t兑换资源列表:"+resourceDtoListObject);

					
					JSONArray resourceDtoArray = JSONArray.fromObject(resourceDtoListObject);
					log.debug("resourceDtoArray:"+resourceDtoArray);
					int seq = 0;
					for (Object rdd : resourceDtoArray) {
						JSONObject oc = (JSONObject) rdd;
						 
						LifeResourceList lifeResourceList = new LifeResourceList();
						// 资源类型
						String resourceType = (String) oc.get("ResourceType");
						log.debug("\t\t资源类型[" + resourceType + "]");
						
						if (resourceType == null || resourceType.isEmpty()) {
							ret.put("Status", Constants.STATUS_FAILURE);
							ret.put("ErrorCode", Constants.RESOURCE_TYPE_NOT_SUPPLY);
							ret.put("ErrorMessage", "资源类型未提供");
							log.error("资源类型未提供");
							return ret;
						}
						if ("0123".indexOf(resourceType) < 0) {
							ret.put("Status", Constants.STATUS_FAILURE);
							ret.put("ErrorCode", Constants.RESOURCE_TYPE_NOT_0123);
							ret.put("ErrorMessage", "资源类型取值错误,ResourceType[" + resourceType
									+ "],范围[0,1,2,3]");
							log.error("资源类型取值错误，ResourceType[" + resourceType + "],范围[0,1,2,3]");
							return ret;
						}
						
						if(resourceType.equals("0") && "1".equals(resourceExchangeMode)){
							ret.put("Status", Constants.STATUS_FAILURE);
							ret.put("ErrorCode", Constants.EXCHANGE_BY_QUANTITY_NOT_ACCEPTED);
							ret.put("ErrorMessage", "按数量兑换和兑换为金钱不处理");
							log.error("按数量兑换和兑换为金钱不处理");
							return ret;
						}
						// 资源量/比例
						String totalResource = (String) oc.get("TotalResource");
						log.debug("\t\t资源量/比例[" + totalResource + "]");
						if (totalResource == null || totalResource.isEmpty()) {
							ret.put("Status", Constants.STATUS_FAILURE);
							ret.put("ErrorCode", Constants.TOTAL_RESOURCE_NOT_SUPPLY);
							ret.put("ErrorMessage", "资源量/比例未提供");
							log.error("资源量/比例未提供");
							return ret;
						}
						long total = Long.parseLong(totalResource);
						lifeResourceList.setResource_list_id(exchangeListUUID);
						lifeResourceList.setUser_id(userId);
						lifeResourceList.setOrder_number(seq++);// 顺序号
						String resourceTypeCode = null;
						if ("0".equals(resourceType)) {
							resourceTypeCode = "ROM";
						} else if ("1".equals(resourceType)) {
							resourceTypeCode = "ROV";
						} else if ("2".equals(resourceType)) {
							resourceTypeCode = "ROF";
						} else if ("3".equals(resourceType)) {
							resourceTypeCode = "ROS";
						} else {
							resourceTypeCode = "ROV";
						}
						lifeResourceList.setResource_type_code(resourceTypeCode);
						lifeResourceList.setResource_value(totalResource);

						// list
						resourceDtoList.add(lifeResourceList);
						
						// logTradeAutoExchangeSetHis自动兑换设置处理历史表
						LogTradeAutoExchangeSetHis logTradeAutoExchangeSetHis1 = new LogTradeAutoExchangeSetHis();
						logTradeAutoExchangeSetHis1.setTrade_id(tradeId);
						logTradeAutoExchangeSetHis1.setTrade_type_code(TRADE_TYPE_CODE);
						logTradeAutoExchangeSetHis1.setUser_id(userId);
						logTradeAutoExchangeSetHis1.setPartition_id(partitionId);
						logTradeAutoExchangeSetHis1.setExchange_id(exchangeId);
						logTradeAutoExchangeSetHis1.setOperation_type("1");
						logTradeAutoExchangeSetHis1.setCycle_type(CYCLE_TYPE);
						logTradeAutoExchangeSetHis1.setPurchase_mode(valueExchangeMode);
						logTradeAutoExchangeSetHis1.setTop_b_value(topValue);
						logTradeAutoExchangeSetHis1.setExchange_mode(resourceExchangeMode);
						logTradeAutoExchangeSetHis1.setEff_date(effDate);
						logTradeAutoExchangeSetHis1.setExp_date(expDate);
						logTradeAutoExchangeSetHis1.setResource_list_id(exchangeListUUID);
						logTradeAutoExchangeSetHis1.setOrder_number(0);
						logTradeAutoExchangeSetHis1.setResource_type_code(resourceTypeCode);
						logTradeAutoExchangeSetHis1.setResource_value(total);

						logTradeAutoExchangeSetHisList.add(logTradeAutoExchangeSetHis1);
					}
					

					lifeUserAutoExchange.setEff_date(effDate);
					lifeUserAutoExchange.setExp_date(expDate);
					lifeUserAutoExchange.setUser_id(userId);
					lifeUserAutoExchangeComposite.setLifeUserAutoExchange(lifeUserAutoExchange);
					lifeUserAutoExchangeComposite.setLifeResourceList(resourceDtoList);
					updateAutoExchange.add(lifeUserAutoExchangeComposite);
					log.debug("updateAutoExchange============>"+updateAutoExchange.toString());
					//获取原有的自动兑换设置信息
					LifeUserAutoExchange lifeUserAutoExchanges=tradeExchangeOper.hasOldLifeUserAutoExchange(userId, currDate);
					if (null!=lifeUserAutoExchanges) {
						
						String monthEndTime=DateUtil.getEndOfCurrMonth();
						lifeUserAutoExchanges.setExp_date(monthEndTime);
						tradeExchangeOper.updateExpdate(lifeUserAutoExchanges);
						
						
						LogTradeAutoExchangeSetHis logTradeAutoExchangeSetHis2 = new LogTradeAutoExchangeSetHis();
						logTradeAutoExchangeSetHis2.setTrade_id(lifeUserAutoExchanges.getExchange_id());
						logTradeAutoExchangeSetHis2.setTrade_type_code(TRADE_TYPE_CODE);
						logTradeAutoExchangeSetHis2.setUser_id(lifeUserAutoExchanges.getUser_id());
						logTradeAutoExchangeSetHis2.setPartition_id(partitionId);
						logTradeAutoExchangeSetHis2.setExchange_id(lifeUserAutoExchanges.getExchange_id());
						logTradeAutoExchangeSetHis2.setOperation_type("1");
						logTradeAutoExchangeSetHis2.setCycle_type(CYCLE_TYPE);
						logTradeAutoExchangeSetHis2.setPurchase_mode(lifeUserAutoExchanges.getPurchase_mode());
						logTradeAutoExchangeSetHis2.setTop_b_value(lifeUserAutoExchanges.getTop_b_value());
						logTradeAutoExchangeSetHis2.setExchange_mode(lifeUserAutoExchanges.getExchange_mode());
						logTradeAutoExchangeSetHis2.setEff_date(lifeUserAutoExchanges.getExp_date());
						logTradeAutoExchangeSetHis2.setExp_date(monthEndTime);
						logTradeAutoExchangeSetHis2.setResource_list_id(lifeUserAutoExchanges.getResource_list_id());
						logTradeAutoExchangeSetHis2.setOrder_number(0);
						logTradeAutoExchangeSetHis2.setResource_type_code("ROM");
						logTradeAutoExchangeSetHis2.setResource_value(0);

						logTradeAutoExchangeSetHisList.add(logTradeAutoExchangeSetHis2);
					}else{
						//如果没有原来的自动兑换设置将不做任何操作
					}
				}else{
					log.debug("自动兑换设置已经存在");
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.AUTO_EXCHANGE_HAS_EXISTED);
					ret.put("ErrorMessage", "当前在用或预约冲突");
					log.error("当前在用或预约冲突");
					return ret;
				}
				} else if ("2".equals(operationType)) {//删除预约自动兑换设置
					
					/*
					 * 删除预约操作时，把对应的exchangeId的失效时间改为当前
					 */
					String exchageId = in.getString("ExchangeID");// 自动兑换实例ID
					log.debug("\t自动兑换实例ID[" + exchageId + "]");
					if (exchageId == null || exchageId.isEmpty()) {
						ret.put("Status", Constants.STATUS_FAILURE);
						ret.put("ErrorCode", Constants.EXCHANGE_ID_NOT_SUPPLY);
						ret.put("ErrorMessage", "删除操作时， 自动兑换实例ID字段必填");
						log.error("自动兑换实例ID字段必填");
						return ret;
					}
					
					//判断当前是否存在有效的自动兑换设置
					LifeUserAutoExchange lifeUser=tradeExchangeOper.appointmentHasExistedLifeUserAutoExchange(userId,exchageId, currDate);
					if (lifeUser!=null  ) {
						LifeUserAutoExchange lifeUserAuto=new LifeUserAutoExchange();
							String effdate=lifeUser.getEff_date();
							String expdateString=currDate;
							log.debug("\t结束时间：[" + currDate + "]");
							lifeUserAuto.setExp_date(expdateString);
							//新增的改为当前时间
							lifeUserAuto.setEff_date(effdate);
							lifeUserAuto.setUser_id(userId);
							lifeUserAuto.setExchange_id(exchageId);
							deleteAutoExchange.add(lifeUserAuto);
							log.debug("deleteAutoExchange.toString=====>"+deleteAutoExchange.toString());
							String bValueExchangeMode = (String) in.get("BValueExchangeMode");// B值兑换方式
							log.debug("\tB值兑换方式[" + bValueExchangeMode + "]");

							String topBValue = (String) in.get("TopBValue");// B值封顶值
							log.debug("\tB值封顶值[" + topBValue + "]");

							String resourceExchangeMode = (String) in.get("ResourceExchangeMode");// 兑换类型
							log.debug("\t兑换类型[" + resourceExchangeMode + "]");
							// logTradeAutoExchangeSetHis自动兑换设置处理历史表
							LogTradeAutoExchangeSetHis logTradeAutoExchangeSetHis3 = new LogTradeAutoExchangeSetHis();
							logTradeAutoExchangeSetHis3.setTrade_id(tradeId);
							logTradeAutoExchangeSetHis3.setTrade_type_code(TRADE_TYPE_CODE);
							logTradeAutoExchangeSetHis3.setUser_id(userId);
							logTradeAutoExchangeSetHis3.setPartition_id(partitionId);
							logTradeAutoExchangeSetHis3.setExchange_id(exchageId);
							logTradeAutoExchangeSetHis3.setOperation_type("2");
							logTradeAutoExchangeSetHis3.setCycle_type(CYCLE_TYPE);
							logTradeAutoExchangeSetHis3.setPurchase_mode(lifeUser.getPurchase_mode());
							logTradeAutoExchangeSetHis3.setTop_b_value(lifeUser.getTop_b_value());
							logTradeAutoExchangeSetHis3.setExchange_mode(lifeUser.getExchange_mode());
							logTradeAutoExchangeSetHis3.setEff_date(effdate);
							logTradeAutoExchangeSetHis3.setExp_date(currDate);
							logTradeAutoExchangeSetHis3.setResource_list_id(lifeUser.getResource_list_id());
							logTradeAutoExchangeSetHis3.setOrder_number(0);
							logTradeAutoExchangeSetHis3.setResource_type_code("ROM");
							logTradeAutoExchangeSetHis3.setResource_value(0);

							logTradeAutoExchangeSetHisList.add(logTradeAutoExchangeSetHis3);

							// 原有设置结束时间变为20501231235959
							LifeUserAutoExchange oldlifeUserAutoExchangelist=tradeExchangeOper.hasOldLifeUserAutoExchange(userId,currDate);
//							log.debug("oldlifeUserAutoExchangelist=======================>"+oldlifeUserAutoExchangelist.toString());
							if(oldlifeUserAutoExchangelist==null){
								//此处没有原有的自动兑换设置不做任何处理
							}else{
							String expdate="20501231235959";
							oldlifeUserAutoExchangelist.setExp_date(expdate);
							
							deleteAutoExchange.add(oldlifeUserAutoExchangelist);
							
							
							// logTradeAutoExchangeSetHis自动兑换设置处理历史表
							LogTradeAutoExchangeSetHis logTradeAutoExchangeSetHis4 = new LogTradeAutoExchangeSetHis();
								logTradeAutoExchangeSetHis4.setTrade_id(tradeId);
								logTradeAutoExchangeSetHis4.setTrade_type_code(TRADE_TYPE_CODE);
								logTradeAutoExchangeSetHis4.setUser_id(userId);
								logTradeAutoExchangeSetHis4.setPartition_id(partitionId);
								logTradeAutoExchangeSetHis4.setExchange_id(oldlifeUserAutoExchangelist.getExchange_id());
								logTradeAutoExchangeSetHis4.setOperation_type("2");
								logTradeAutoExchangeSetHis4.setCycle_type(CYCLE_TYPE);
								logTradeAutoExchangeSetHis4.setPurchase_mode(oldlifeUserAutoExchangelist.getPurchase_mode());
								logTradeAutoExchangeSetHis4.setTop_b_value(oldlifeUserAutoExchangelist.getTop_b_value());
								logTradeAutoExchangeSetHis4.setExchange_mode(oldlifeUserAutoExchangelist.getExchange_mode());
								logTradeAutoExchangeSetHis4.setEff_date(oldlifeUserAutoExchangelist.getEff_date());
								logTradeAutoExchangeSetHis4.setExp_date(expdate);
								logTradeAutoExchangeSetHis4.setResource_list_id(oldlifeUserAutoExchangelist.getResource_list_id());
								logTradeAutoExchangeSetHis4.setOrder_number(0);
								logTradeAutoExchangeSetHis4.setResource_type_code("ROM");
								logTradeAutoExchangeSetHis4.setResource_value(0);

								logTradeAutoExchangeSetHisList.add(logTradeAutoExchangeSetHis4);
							}
							
					
						}else{
							ret.put("Status", Constants.STATUS_FAILURE);
							ret.put("ErrorCode", Constants.NO_ANTO_EXCHANGE);
							ret.put("ErrorMessage", "不存在预约自动兑换或已经关闭");
							return ret;
					}
		
				}else if("3".equals(operationType)){//已有的兑换规则停止


					List<LifeUserAutoExchange> comm=tradeExchangeOper.queryByUserId(userId,currDate);	
					if(!comm.equals(null)&&comm.size()!=0){
						
					for(LifeUserAutoExchange auto:comm){
						auto.setExp_date(currDate);
	
//						logTradeAutoExchangeSetHis自动兑换设置处理历史表
						LogTradeAutoExchangeSetHis logTradeAutoExchangeSetHis = new LogTradeAutoExchangeSetHis();
						logTradeAutoExchangeSetHis.setTrade_id(tradeId);
						logTradeAutoExchangeSetHis.setTrade_type_code(TRADE_TYPE_CODE);
						logTradeAutoExchangeSetHis.setUser_id(userId);
						logTradeAutoExchangeSetHis.setPartition_id(partitionId);
						logTradeAutoExchangeSetHis.setExchange_id(auto.getExchange_id());
						logTradeAutoExchangeSetHis.setOperation_type("3");
						logTradeAutoExchangeSetHis.setCycle_type(CYCLE_TYPE);
						logTradeAutoExchangeSetHis.setPurchase_mode(auto.getPurchase_mode());
						logTradeAutoExchangeSetHis.setTop_b_value(auto.getTop_b_value());
						logTradeAutoExchangeSetHis.setExchange_mode(auto.getExchange_mode());
						logTradeAutoExchangeSetHis.setEff_date(auto.getEff_date());
						logTradeAutoExchangeSetHis.setExp_date(currDate);
						logTradeAutoExchangeSetHis.setResource_list_id(auto.getResource_list_id());
						logTradeAutoExchangeSetHis.setOrder_number(0);
						logTradeAutoExchangeSetHis.setResource_type_code("ROM");
						logTradeAutoExchangeSetHis.setResource_value(0);

						logTradeAutoExchangeSetHisList.add(logTradeAutoExchangeSetHis);
						
						deleteAutoExchange.add(auto);
					}
				}else{
					ret.put("Status", Constants.STATUS_FAILURE);
					ret.put("ErrorCode", Constants.NO_ANTO_EXCHANGE);
					ret.put("ErrorMessage", "不存在自动兑换");
					return ret;
				}
//			
			}	
				
				// 交易登记处理历史表
			LogTradeHis logTradeHis = new LogTradeHis();

			logTradeHis.setTrade_id(tradeId);
			logTradeHis.setTrade_type_code(TRADE_TYPE_CODE);
			logTradeHis.setExternal_system_code("10000");
			logTradeHis.setChannel_type(channelType);
			logTradeHis.setUser_id(userId);
			logTradeHis.setPartition_id(partitionId);
			logTradeHis.setOrder_no(orderNo);
			logTradeHis.setOrder_type("");
			logTradeHis.setOrder_amount(0);
			logTradeHis.setOrder_completion_time(currDate);
			logTradeHis.setBalance_type_id(0);
			logTradeHis.setUnit_type_id(0);// 0 B值
			logTradeHis.setBalance(0);
			logTradeHis.setProcess_tag(2);
			logTradeHis.setTrade_time(currDate);
			logTradeHis.setProcess_time(currDate);
			logTradeHis.setRemark("");
			// 插入或删除自动兑换订购关系表
			tradeExchangeOper.updateSetAutoExchange(updateAutoExchange, deleteAutoExchange,
					logTradeHis, logTradeAutoExchangeSetHisList);

			ret.put("Status", Constants.STATUS_SUCCESS);
			log.info("RESULT:" + ret.toString());
		} catch (Exception e) {
			e.printStackTrace();
			
			ret.put("Status", Constants.STATUS_FAILURE);
			ret.put("ErrorCode", Constants.SYSTEM_EXCEPTION);
			ret.put("ErrorMessage", "接口异常");
			log.error(e);
			return ret;
		}
		return ret;
	}

	
}
