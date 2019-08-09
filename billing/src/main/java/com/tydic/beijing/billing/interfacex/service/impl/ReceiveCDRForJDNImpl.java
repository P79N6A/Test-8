package com.tydic.beijing.billing.interfacex.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.CDR100Transfer;
import com.tydic.beijing.billing.dao.CDR100TransferJDN;
import com.tydic.beijing.billing.dao.LifeServiceAttrForMemcache;
import com.tydic.beijing.billing.dao.QTransferDownload;
import com.tydic.beijing.billing.dao.QTransferDownloadJDN;
import com.tydic.beijing.billing.dao.SynchronizeInfo;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.ReceiveCDR;
import com.tydic.beijing.billing.interfacex.service.ReceiveCdrForJDN;
import com.tydic.uda.service.S;

import net.sf.json.JSONObject;
/*
 * 1、时间转换格式前，应该先校验是否符合规则或者为空，或者超出当前时间， 
 */

@Transactional(rollbackFor = Exception.class)
public class ReceiveCDRForJDNImpl implements ReceiveCdrForJDN {

	private static Logger log = Logger.getLogger(ReceiveCDRForJDNImpl.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	public JSONObject receive(JSONObject input) {

		long time = System.currentTimeMillis();
		log.debug("荣联/云之讯报文===================》" + input.toString());
		JSONObject output = new JSONObject();

		if (!checkParam(input, output)) {
			return output;
		}

		// 从JSON中取值
		String callId = input.getString("CallId");
		String cdrType = input.getString("CdrType");
		String cdrSubType = input.getString("CdrSubType");
		String callingNbr = input.getString("CallingNbr");
		String calledNbr = input.getString("CalledNbr");

		String sessionBeginTime = (String) input.get("SessionBeginTime");
		String sessionEndTime = (String) input.get("SessionEndTime");

		// 非必传字段，加入传空值时的判断，若为空或者为0时则为null
		String duration_str = input.containsKey("Duration") ? input.getString("Duration") : null;

		if (cdrType.equals("1") && checkCdrSubTypeParam(cdrSubType)
				&& (duration_str == null || duration_str.length() == 0)) {
			output.put("Status", "0");
			output.put("ErrorCode", "1001");
			output.put("ErrorMessage", "语音业务时长字段必传");
			return output;
		}

		int duration = 1;
		if (duration_str != null && duration_str.length() > 0) {
			duration = Integer.parseInt(duration_str);
		}

		String CalledAnswerTime = (String) input.get("CalledAnswerTime");
		String CalledEndTime = (String) input.get("CalledEndTime");

		if (!checkCdrSubTypeParam(cdrSubType)) {
			if (CalledAnswerTime != null && CalledAnswerTime.length() > 0 && CalledEndTime != null
					&& CalledEndTime.length() > 0) {
				try {
					long durationl = (dateFormat.parse(CalledEndTime).getTime()
							- dateFormat.parse(CalledAnswerTime).getTime()) / 1000;
					duration = (int) durationl;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					output.put("Status", "0");
					output.put("ErrorCode", "1008");
					output.put("ErrorMessage", "时间格式异常");
					return output;
				}
			} else {
				// output.put("Status", "0");
				// output.put("ErrorCode", "1009");
				// output.put("ErrorMessage", "双向回呼没有被叫挂机时间或者应答时间");
				// return output;
				duration = 0;
			}
		}
		String SupplierCode = (String) input.get("SupplierCode");
		String IntoIVRcall = (String) input.get("IntoIVRcall");
		String TransferCall = (String) input.get("TransferCall");
		String IntoIVRStartTime = (String) input.get("IntoIVRStartTime");
		String CallerStartTime = (String) input.get("CallerStartTime");
		String CallerRingTime = (String) input.get("CallerRingTime");
		String CallerAnswerTime = (String) input.get("CallerAnswerTime");
		String CallerEndTime = (String) input.get("CallerEndTime");
		String CalledStartTime = (String) input.get("CalledStartTime");
		String CalledRingTime = (String) input.get("CalledRingTime");
		String CallerResult = (String) input.get("CallerResult");
		String CalledResult = (String) input.get("CalledResult");
		String EndPart = (String) input.get("EndPart");
		String RecordUrl = (String) input.get("RecordUrl");
		String OtherData = (String) input.get("OtherData");
		String CallingShowNbr = (String) input.get("CallingShowNbr");
		String CalledShowNbr = (String) input.get("CalledShowNbr");
		String CallErrCode = (String) input.get("CallErrCode");
		
		String receiveTime = dateFormat.format(new Date());
		String partitionId = receiveTime.substring(4, 6);

		// 将获得的值插入两张表中
		CDR100TransferJDN cdrJdn = new CDR100TransferJDN();
		cdrJdn.setCallId(callId);
		cdrJdn.setCdrType(cdrType);
		cdrJdn.setCdrSubType(cdrSubType);
		cdrJdn.setCallingNbr(callingNbr);
		cdrJdn.setCalledNbr(calledNbr);
		cdrJdn.setSessionBeginTime(sessionBeginTime);
		cdrJdn.setSessionEndTime(sessionEndTime);
		cdrJdn.setDuration(duration);
		cdrJdn.setReceiveTime(receiveTime);
		cdrJdn.setPartitionId(partitionId);
		cdrJdn.setStatus("0");

		cdrJdn.setCalledAnswerTime(CalledAnswerTime);
		cdrJdn.setCalledEndTime(CalledEndTime);
		cdrJdn.setCalledResult(CalledResult);
		cdrJdn.setCalledRingTime(CalledRingTime);
		cdrJdn.setCalledStartTime(CalledStartTime);

		cdrJdn.setCallerAnswerTime(CallerAnswerTime);
		cdrJdn.setCallerEndTime(CallerEndTime);
		cdrJdn.setCallerResult(CallerResult);
		cdrJdn.setCallerRingTime(CallerRingTime);
		cdrJdn.setCallerStartTime(CallerStartTime);

		cdrJdn.setEndPart(EndPart);
		cdrJdn.setIntoIVRcall(IntoIVRcall);
		cdrJdn.setTransferCall(TransferCall);
		cdrJdn.setOtherData(OtherData);
		cdrJdn.setRecordUrl(RecordUrl);
		cdrJdn.setIntoIVRStartTime(IntoIVRStartTime);
		cdrJdn.setSupplierCode(SupplierCode);
		cdrJdn.setCallingShowNbr(CallingShowNbr);
		cdrJdn.setCalledShowNbr(CalledShowNbr);
		cdrJdn.setCallErrCode(CallErrCode);
		// 调用方法将值插入表中
		S.get(CDR100TransferJDN.class).create(cdrJdn);
		if ((checkCdrSubType1004(cdrSubType) || checkCdrSubType1005(cdrSubType)) && RecordUrl != null
				&& !RecordUrl.equals("") && duration!=0) {
			QTransferDownloadJDN qJdn = new QTransferDownloadJDN();
			qJdn.setCallId(callId);
			qJdn.setCaller(callingNbr);
			qJdn.setCalled(calledNbr);
			qJdn.setOrigCalled(calledNbr);
			qJdn.setRecordUrl(RecordUrl);
			qJdn.setState(0);
			qJdn.setPartitionId(partitionId);
			qJdn.setCdr_source(SupplierCode);
			qJdn.setCdr_Sub_Type(cdrSubType);
			qJdn.setCalledAnswerTime(CalledAnswerTime);
			String jdnNumber = getBossCallingNbr(cdrJdn);
			qJdn.setJdnNumber(jdnNumber);
			log.debug("===京牛号码:"+jdnNumber);
			S.get(QTransferDownloadJDN.class).create(qJdn);
		}

		output.put("Status", "1");
		output.put("ErrorCode", "");
		output.put("ErrorMessage", "");
		log.debug("输出结果output是：" + output.toString());

		return output;

	}

	private boolean checkParam(JSONObject input, JSONObject output) {

		if (!input.containsKey("CallId") || input.getString("CallId") == null
				|| input.getString("CallId").length() == 0) {
			output.put("Status", "0");
			output.put("ErrorCode", "1001");
			output.put("ErrorMessage", "CallId字段未传或格式异常");
			return false;
		}

		if (!input.containsKey("CdrType") || input.getString("CdrType") == null
				|| input.getString("CdrType").length() == 0) {
			output.put("Status", "0");
			output.put("ErrorCode", "1002");
			output.put("ErrorMessage", "CdrType字段未传或格式异常");
			return false;
		}

		if (!input.containsKey("CdrSubType") || input.getString("CdrSubType") == null
				|| input.getString("CdrSubType").length() == 0) {
			output.put("Status", "0");
			output.put("ErrorCode", "1003");
			output.put("ErrorMessage", "CdrSubType字段未传或格式异常");
			return false;
		}

		if (!input.containsKey("CallingNbr") || input.getString("CallingNbr") == null
				|| input.getString("CallingNbr").length() == 0) {
			output.put("Status", "0");
			output.put("ErrorCode", "1004");
			output.put("ErrorMessage", "CallingNbr字段未传或格式异常");
			return false;
		}

		if (!input.containsKey("CalledNbr") || input.getString("CalledNbr") == null
				|| input.getString("CalledNbr").length() == 0) {
			output.put("Status", "0");
			output.put("ErrorCode", "1005");
			output.put("ErrorMessage", "CalledNbr字段未传或格式异常");
			return false;
		}

		if (checkCdrSubTypeParam(input.getString("CdrSubType")) && (!input.containsKey("SessionBeginTime")
				|| input.getString("SessionBeginTime") == null || input.getString("SessionBeginTime").length() == 0)) {
			output.put("Status", "0");
			output.put("ErrorCode", "1006");
			output.put("ErrorMessage", "SessionBeginTime字段未传或格式异常");
			return false;
		}

		if (checkCdrSubTypeParam(input.getString("CdrSubType")) && (!input.containsKey("SessionEndTime")
				|| input.getString("SessionEndTime") == null || input.getString("SessionEndTime").length() == 0)) {
			output.put("Status", "0");
			output.put("ErrorCode", "1007");
			output.put("ErrorMessage", "SessionEndTime字段未传或格式异常");
			return false;
		}

		if (!input.containsKey("SupplierCode") || input.getString("SupplierCode") == null
				|| input.getString("SupplierCode").length() == 0) {
			output.put("Status", "0");
			output.put("ErrorCode", "1010");
			output.put("ErrorMessage", "业务供应商未提供");
			return false;
		}

		try {
			if (checkCdrSubTypeParam(input.getString("CdrSubType"))){
				dateFormat.parse(input.getString("SessionBeginTime"));
				dateFormat.parse(input.getString("SessionEndTime"));
			}
		} catch (ParseException e) {
			output.put("Status", "0");
			output.put("ErrorCode", "1008");
			output.put("ErrorMessage", "时间格式异常");
			return false;
		}

		return true;

	}

	private boolean checkCdrSubTypeParam(String cdrSubType) {
		if (cdrSubType.equals("1001") || cdrSubType.equals("1002") || cdrSubType.equals("1003")
				|| cdrSubType.equals("3001")) {
			return true;
		}
		return false;
	}

	private boolean checkCdrSubType1004(String cdrSubType) {// 双向回呼，正向
		if (cdrSubType.equals("1004")) {
			return true;
		}
		return false;
	}

	private boolean checkCdrSubType1005(String cdrSubType) {// 双向回呼，反向
		if (cdrSubType.equals("1005")) {
			return true;
		}
		return false;
	}

	private String getBossCallingNbr(CDR100TransferJDN cdr) {
		String sessionTime = "";
		String callNbr = "";

		sessionTime = cdr.getCalledAnswerTime();
		if (null == sessionTime) {
			return null;
		}
		if (cdr.getCdrSubType().equals("1004")) {
			callNbr = cdr.getCallingNbr();
		}
		if (cdr.getCdrSubType().equals("1005")) {
			callNbr = cdr.getCalledNbr();
		}

		LifeServiceAttrForMemcache jdnMemcached = S.get(LifeServiceAttrForMemcache.class).get("JDNATTR001" + callNbr);

		log.debug("key is ==>" + ("JDNATTR001" + callNbr));

		String retNumber = null;

		if (jdnMemcached == null || jdnMemcached.getSynchronizeInfoList() == null) {
			return null;
		}

		for (SynchronizeInfo tmpinfo : jdnMemcached.getSynchronizeInfoList()) {
			if (tmpinfo.getEff_date().compareTo(sessionTime) <= 0
					&& tmpinfo.getExp_date().compareTo(sessionTime) >= 0) {
				retNumber = tmpinfo.getDevice_number();
			}
		}

		return retNumber;
	}
}
