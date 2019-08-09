package com.tydic.beijing.bvalue.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.ExternalAccountAttrInfo;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LifeResourceList;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchange;
import com.tydic.beijing.bvalue.dao.LogTradeExternalAccountAttrHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.RuleParameters;
import com.tydic.beijing.bvalue.dto.SetAttrDto;
import com.tydic.beijing.bvalue.service.SetJDPinAttr;
import com.tydic.uda.service.S;

public class SetJDPinAttrImpl implements SetJDPinAttr {

	private static Logger log = Logger.getLogger(SetJDPinAttrImpl.class);

	private DbTool db;
	private String syncRedis;
	
//	@Autowired
//	private InfoPayBalanceSync paySync;

//	public InfoPayBalanceSync getPaySync() {
//		return paySync;
//	}
//
//	public void setPaySync(InfoPayBalanceSync paySync) {
//		this.paySync = paySync;
//	}

	public void setDb(DbTool db) {
		this.db = db;
	}
	public String getSyncRedis() {
		return syncRedis;
	}

	public void setSyncRedis(String syncRedis) {
		this.syncRedis = syncRedis;
	}
	private static final String CYCLE_TYPE = "COM";

	@Override
	public JSONObject perform(JSONObject request) {

		String jdpin = "";
		String mobileNumber = "";
		String dtos = "";
		String userId = "";
		List<SetAttrDto> attrDtos = new ArrayList<SetAttrDto>();
		JSONObject jerror = new JSONObject();
		jerror.put("Status", Constants.Result_Error);
		jerror.put("ErrorCode", Constants.SYSTEM_EXCEPTION);
		jerror.put("ErrorMessage", "服务异常");
		RuleParameters ruleParameters = new RuleParameters();
		try {
			jdpin = request.getString("JDPin");

			mobileNumber = request.getString("MobileNumber");
			dtos = request.getString("AttrDtoList");
			if (dtos.isEmpty() || jdpin.isEmpty() || mobileNumber.isEmpty() || !Common.isMobileNumber(mobileNumber)) {
				log.debug("请求参数错误[attrDtoList|jdpin|mobileNumber is null !]");
				JSONObject error = new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
				error.put("ErrorMessage", "请求参数错误");
				log.debug("ERR>>>>>>>>>>>>>>>>" + error.toString());
				return error;
			}

			userId = Common.md5(jdpin);

			ObjectMapper mapper = new ObjectMapper();

			List<LinkedHashMap<String, String>> dtoList = mapper.readValue(dtos, List.class);
			int size = dtoList.size();
			log.debug("dtoList.size>>>>>>>>>>>>>>>>>>>>" + size);
			for (int i = 0; i < size; ++i) {
				Map<String, String> iter = dtoList.get(i);
				String mode = iter.get("Mode");
				String attrCode = iter.get("AttrCode");
				String attrValue = iter.get("AttrValue");
				String effDate = iter.get("EffDate");
				String expDate = iter.get("ExpDate");
				if (mode.isEmpty() || attrCode.isEmpty() || attrValue.isEmpty() || effDate.isEmpty()
						|| expDate.isEmpty() || (!mode.equals("1") && !mode.equals("2"))) {
					log.debug("请求参数错误[attrdto]");
					JSONObject error = new JSONObject();
					error.put("Status", Constants.Result_Error);
					error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
					error.put("ErrorMessage", "请求参数错误");
					log.debug("ERR>>>>>>>>>>>>>>" + error.toString());
					return error;
				}

				SetAttrDto dto = new SetAttrDto(mode, attrCode, attrValue, effDate, expDate);
				attrDtos.add(dto);

			}

			if (attrDtos.isEmpty()) {
				log.debug("请求参数错误[attrdtos empty]");
				JSONObject error = new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
				error.put("ErrorMessage", "请求参数错误");
				log.debug("ERR>>>>>>>>>>>>>>" + error.toString());
				return error;

			}

		} catch (JsonParseException e) {
			log.debug(e.getMessage());
			return jerror;
		} catch (JsonMappingException e) {
			log.debug(e.getMessage());
			return jerror;
		} catch (IOException e) {
			log.debug(e.getMessage());
			return jerror;
		} catch (Exception e) {
			JSONObject error = new JSONObject();
			error.put("Status", Constants.Result_Error);
			error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
			error.put("ErrorMessage", "请求参数错误");
			return error;
		}

		List<SetAttrDto> adds = new ArrayList<SetAttrDto>();
		List<SetAttrDto> deles = new ArrayList<SetAttrDto>();
		for (SetAttrDto iter : attrDtos) {
			if (iter.getMode().equals("1")) {
				adds.add(iter);
			} else if (iter.getMode().equals("2")) {
				deles.add(iter);
			}

		}

		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

		InfoUserExternalAccount external = db.queryPinRelation(userId, currentTime);
		String tradeId = Common.getUUID();
		int partitionId = Calendar.getInstance().get(Calendar.MONTH) + 1;

		if (external == null || !external.getExternal_account_code().equals(mobileNumber)) {
			LogTradeHis trade = new LogTradeHis();

			String tradeTypeCode = "504";
			String externalSystemCode = "10000";
			String channelType = "107";

			long processTag = 3;
			String tradeTime = currentTime;
			String processTime = tradeTime;
			String remark = "不存在绑定关系,jdpin[" + jdpin + "],userId[" + userId + "],mobileNumber[" + mobileNumber + "]";
			trade.setTrade_id(tradeId);
			trade.setTrade_type_code(tradeTypeCode);
			trade.setExternal_system_code(externalSystemCode);
			trade.setPartition_id(partitionId);
			trade.setChannel_type(channelType);
			trade.setProcess_tag(processTag);
			trade.setProcess_time(processTime);
			trade.setTrade_time(tradeTime);
			trade.setUser_id(userId);
			trade.setRemark(remark);
			db.insertLogTradeHis(trade);
			JSONObject error = new JSONObject();
			error.put("Status", Constants.Result_Error);
			error.put("ErrorCode", Constants.Err_SetJDPinAttr_No_Rel);
			error.put("ErrorMessage", "jdpin[" + jdpin + "],mobileNumber[" + mobileNumber + "]不存在绑定关系");
			log.debug("ERR>>>>>>>>>>>>>>>>>>>>>" + error.toString());
			return error;

		}
		String externalId = external.getExternal_account_id();
		List<LifeUserAutoExchange> oldExchangelist = db.queryAutoExchangeSet(userId, currentTime); // 用户当前自动兑换设置
		List<LifeUserAutoExchange> addexchangelist = new ArrayList<LifeUserAutoExchange>();
		List<LifeUserAutoExchange> delexchangelist = new ArrayList<LifeUserAutoExchange>();
		List<LifeResourceList> addresourcelist = new ArrayList<LifeResourceList>();

		List<InfoUserExternalAccountAttr> attrs = new ArrayList<InfoUserExternalAccountAttr>();
		List<ExternalAccountAttrInfo> delAttrs = new ArrayList<ExternalAccountAttrInfo>();

		List<LogTradeExternalAccountAttrHis> attrhislist = new ArrayList<LogTradeExternalAccountAttrHis>();

		if (!adds.isEmpty()) { // 需要新增的属性

			for (SetAttrDto iter : adds) {
				InfoUserExternalAccountAttr attr = new InfoUserExternalAccountAttr();
				attr.setExternal_account_id(externalId);
				attr.setUser_id(userId);
				attr.setJd_pin(jdpin);
				attr.setExternal_system_code("10000");
				attr.setAttr_code(iter.getAttrCode());
				attr.setAttr_value(iter.getAttrValue());
				attr.setEff_date(iter.getEffDate().replace("-", "").replace(" ", "").replace(":", ""));
				attr.setExp_date(iter.getExpDate().replace("-", "").replace(" ", "").replace(":", ""));

				attrs.add(attr);

				LogTradeExternalAccountAttrHis tmpattrhis = new LogTradeExternalAccountAttrHis();
				tmpattrhis.setTrade_id(tradeId);
				tmpattrhis.setUser_id(userId);
				tmpattrhis.setTrade_type_code("504");
				tmpattrhis.setPartition_id(partitionId);
				tmpattrhis.setOperation_type("1");
				tmpattrhis.setExternal_system_code("10000");
				tmpattrhis.setExternal_account_id(mobileNumber);
				tmpattrhis.setAttr_code(iter.getAttrCode());
				tmpattrhis.setAttr_value(iter.getAttrValue());
				tmpattrhis.setEff_date(iter.getEffDate().replace("-", "").replace(" ", "").replace(":", ""));
				tmpattrhis.setExp_date(iter.getExpDate().replace("-", "").replace(" ", "").replace(":", ""));
				attrhislist.add(tmpattrhis);

				// 如果改变了品牌，需要根据新的品牌增加相应的自动兑换设置
				if (iter.getAttrCode().equals("1002")) { // 01 自由行 02乐购卡
					if (iter.getAttrValue().equals("01")) {// 自由行，默认50%语音50%流量
						// 新增时兑换ID由系统生成
						LifeUserAutoExchange lifeUserAutoExchange = new LifeUserAutoExchange();
						String exchangeId = Common.getUUID();
						lifeUserAutoExchange.setExchange_id(exchangeId);
						lifeUserAutoExchange.setUser_id(userId);
						lifeUserAutoExchange.setCycle_type(CYCLE_TYPE);
						lifeUserAutoExchange.setPurchase_mode("002"); // 001：余量全部兑换
																		// 002：最高封顶
						lifeUserAutoExchange.setTop_b_value(500);
						lifeUserAutoExchange.setExchange_mode("2"); // 百分比
						// 列表ID
						String exchangeListUUID = Common.getUUID();
						lifeUserAutoExchange.setResource_list_id(exchangeListUUID);
						lifeUserAutoExchange
								.setEff_date(iter.getEffDate().replace("-", "").replace(" ", "").replace(":", ""));
						lifeUserAutoExchange
								.setExp_date(iter.getExpDate().replace("-", "").replace(" ", "").replace(":", ""));
						addexchangelist.add(lifeUserAutoExchange);

						/* ROM:金钱 ROV:语音资源 ROF:流量资源 ROS:短信资源 */
						// 语音
						LifeResourceList lifeResourceList = new LifeResourceList();
						lifeResourceList.setResource_list_id(exchangeListUUID);
						lifeResourceList.setOrder_number(0);
						lifeResourceList.setResource_type_code("ROV");
						lifeResourceList.setResource_value("50");
						lifeResourceList.setUser_id(userId);
						addresourcelist.add(lifeResourceList);
						// 流量
						LifeResourceList lifeResourceList2 = new LifeResourceList();
						lifeResourceList2.setResource_list_id(exchangeListUUID);
						lifeResourceList2.setOrder_number(1);
						lifeResourceList2.setResource_type_code("ROF");
						lifeResourceList2.setResource_value("50");
						lifeResourceList2.setUser_id(userId);
						addresourcelist.add(lifeResourceList2);

					} else if (iter.getAttrValue().equals("02")) {// 乐购卡，默认所有B值都兑换为话费
						// 新增时兑换ID由系统生成
						LifeUserAutoExchange lifeUserAutoExchange = new LifeUserAutoExchange();
						String exchangeId = Common.getUUID();
						lifeUserAutoExchange.setExchange_id(exchangeId);
						lifeUserAutoExchange.setUser_id(userId);
						lifeUserAutoExchange.setCycle_type(CYCLE_TYPE);
						lifeUserAutoExchange.setPurchase_mode("001"); // 001：余量全部兑换
																		// 002：最高封顶
						lifeUserAutoExchange.setTop_b_value(500);
						lifeUserAutoExchange.setExchange_mode("2"); // 百分比
						// 列表ID
						String exchangeListUUID = Common.getUUID();
						lifeUserAutoExchange.setResource_list_id(exchangeListUUID);
						lifeUserAutoExchange
								.setEff_date(iter.getEffDate().replace("-", "").replace(" ", "").replace(":", ""));
						lifeUserAutoExchange
								.setExp_date(iter.getExpDate().replace("-", "").replace(" ", "").replace(":", ""));
						addexchangelist.add(lifeUserAutoExchange);

						/* ROM:金钱 ROV:语音资源 ROF:流量资源 ROS:短信资源 */
						// 话费
						LifeResourceList lifeResourceList = new LifeResourceList();
						lifeResourceList.setResource_list_id(exchangeListUUID);
						lifeResourceList.setOrder_number(0);
						lifeResourceList.setResource_type_code("ROM");
						lifeResourceList.setResource_value("100");
						lifeResourceList.setUser_id(userId);
						addresourcelist.add(lifeResourceList);

					} else {

					}
				}

			}

		}

		String now = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()); // 针对当前有效的属性进行删除

		if (!deles.isEmpty()) { // 需要删除的属性
			for (SetAttrDto iter : deles) {
				ExternalAccountAttrInfo attr = new ExternalAccountAttrInfo();
				attr.setExternal_account_id(externalId);
				attr.setUser_id(userId);
				attr.setJd_pin(jdpin);
				attr.setExternal_system_code("10000");
				attr.setAttr_code(iter.getAttrCode());
				attr.setAttr_value(iter.getAttrValue());
				// attr.setEff_date(iter.getEffDate().replace("-", "").replace("
				// ", "").replace(":", ""));
				attr.setEff_date(now); // 使用eff_date当做当前时间
				attr.setExp_date(iter.getExpDate().replace("-", "").replace(" ", "").replace(":", ""));

				delAttrs.add(attr);

				LogTradeExternalAccountAttrHis tmpattrhis = new LogTradeExternalAccountAttrHis();
				tmpattrhis.setTrade_id(tradeId);
				tmpattrhis.setUser_id(userId);
				tmpattrhis.setTrade_type_code("504");
				tmpattrhis.setPartition_id(partitionId);
				tmpattrhis.setOperation_type("2");
				tmpattrhis.setExternal_system_code("10000");
				tmpattrhis.setExternal_account_id(mobileNumber);
				tmpattrhis.setAttr_code(iter.getAttrCode());
				tmpattrhis.setAttr_value(iter.getAttrValue());
				tmpattrhis.setEff_date(iter.getEffDate().replace("-", "").replace(" ", "").replace(":", ""));
				tmpattrhis.setExp_date(iter.getExpDate().replace("-", "").replace(" ", "").replace(":", ""));
				attrhislist.add(tmpattrhis);

				// 如果改变了品牌，需要根据新的品牌增加相应的自动兑换设置
				if (iter.getAttrCode().equals("1002")) { // 01 自由行 02乐购卡
					// 如果删除了品牌，需要把自动兑换设置的结束时间更新
					for (LifeUserAutoExchange tmpautoexchange : oldExchangelist) {
						if (tmpautoexchange.getExp_date().compareTo(currentTime) > 0) {
							tmpautoexchange
									.setExp_date(iter.getExpDate().replace("-", "").replace(" ", "").replace(":", ""));
							delexchangelist.add(tmpautoexchange);
						}

					}
				}

			}

		}


		// 旧套餐变更新套餐，账本清0
		log.debug("开始新增的内容了!!!");
		List<InfoPayBalance> infobs = new ArrayList<InfoPayBalance>();
		ruleParameters = db.query504Attr();
		String[] oldTariff = ruleParameters.getPara_char3().split(",");
		String[] newTariff = ruleParameters.getPara_char4().split(",");
		log.debug("旧套餐："+oldTariff.toString());
		log.debug("新套餐："+newTariff.toString());
		int old_delete = 0;
		int new_add = 0;

		for (SetAttrDto setAttrDto : deles) {
			String attrCode = setAttrDto.getAttrCode();
			if (attrCode.equals("1003")) {
				for (int i = 0; i < oldTariff.length; i++) {
					String code = oldTariff[i];
					if (setAttrDto.getAttrValue().equals(code)) {
						old_delete = 1;
						break;
					}
				}
				if (old_delete == 1) {
					break;
				}
			}
		}

		for (SetAttrDto setAttrDto : adds) {
			String attrCode = setAttrDto.getAttrCode();
			if (attrCode.equals("1003")) {
				for (int i = 0; i < newTariff.length; i++) {
					String code = newTariff[i];
					if (setAttrDto.getAttrValue().equals(code)) {
						new_add = 1;
						break;
					}
				}
				if (new_add == 1) {
					break;
				}
			}
		}
		log.debug("old_delete=="+old_delete+"  new_add=="+new_add);
		long totalB=0;
		if (old_delete == 1 && new_add == 1) {
			if (this.syncRedis != null && this.syncRedis.equals("Y")) {
				log.debug("================UserId:"+userId);
				infobs = db.queryInfoPayBalancesByUserId(userId);
				log.debug("该用户的账本个数是："+infobs.size());
				for (InfoPayBalance infoPayBalance : infobs) {
					totalB=totalB+infoPayBalance.getBalance();
					infoPayBalance.setBalance(-infoPayBalance.getBalance());
					BalanceAccessLog insertBalanceAccessLog=new BalanceAccessLog();
					insertBalanceAccessLog.setTrade_Id(tradeId);
					insertBalanceAccessLog.setTrade_Type_Code("504");
					insertBalanceAccessLog.setUser_Id(userId);
					insertBalanceAccessLog.setPartition_Id(partitionId);
					insertBalanceAccessLog.setBalance_Id(infoPayBalance.getBalance_id());
					insertBalanceAccessLog.setBalance_Type_Id(infoPayBalance.getBalance_type_id());
					insertBalanceAccessLog.setAccess_Tag("1");//取款还是存款
					insertBalanceAccessLog.setMoney(infoPayBalance.getBalance());
					insertBalanceAccessLog.setNew_Balance(0);
					insertBalanceAccessLog.setOld_Balance(infoPayBalance.getBalance());				
					insertBalanceAccessLog.setOperate_Time(currentTime);
					db.updateInfoPayBalance(infoPayBalance);
					db.insertBalanceAccessLog(insertBalanceAccessLog);
//					log.debug("同步redis, " + infoPayBalance.toString());
//					int retCode = paySync.sync(infoPayBalance);
//					if (retCode != 0) {
//						// 0成功 -1失败
//						log.debug("同步Redis失败！");
//					}
//					log.debug("同步redis成功！");
				}
			}
		}

		LogTradeHis trade = new LogTradeHis();
		trade.setTrade_id(tradeId);
		trade.setTrade_type_code("504");
		trade.setExternal_system_code("10000");
		trade.setPartition_id(partitionId);
		trade.setChannel_type("107");
		trade.setProcess_tag(2);
		trade.setProcess_time(currentTime);
		trade.setTrade_time(currentTime);
		trade.setUser_id(userId);
		if (old_delete == 1 && new_add == 1) {
			trade.setBalance(-totalB);
			trade.setUnit_type_id(0);// 0 B值
			trade.setRemark("老套餐变更新套餐,账本清零");
		}else {
			trade.setRemark("");
		}
		
		db.updateAttrDtos(attrs, delAttrs, addexchangelist, delexchangelist, addresourcelist, trade, attrhislist);

		JSONObject ret = new JSONObject();
		ret.put("Status", Constants.Result_Succ);
		ret.put("ErrorCode", "");
		ret.put("ErrorMessage", "");

		log.debug(">>>>>>>>>>>>>>>>>>>>success return :>>>>>>" + ret.toString());
		return ret;
	}

}
