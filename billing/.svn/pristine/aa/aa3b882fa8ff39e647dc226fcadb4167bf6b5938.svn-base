package com.tydic.beijing.billing.interfacex.service.impl;

import java.sql.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActRealTimeBillForOracle;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dto.QueryRealtimeBillResult;
import com.tydic.beijing.billing.interfacex.biz.QueryRealtimeBillOps;
import com.tydic.beijing.billing.interfacex.service.QueryRealtimeBill;

/**
 * 
 * @author Tian
 *
 */
public class QueryRealtimeBillImpl implements QueryRealtimeBill {
	private static Logger LOGGER = Logger.getLogger(QueryRealtimeBillImpl.class);

	private final static String RESULT_STATUS_FAIL = "0"; // 返回失败
	private final static String RESULT_STATUS_OK = "1"; // 返回成功
	
	private QueryRealtimeBillOps ops;
	
	@Override
	public QueryRealtimeBillResult query(String deviceNumber) {
		QueryRealtimeBillResult result = new QueryRealtimeBillResult();
		reset(result);
		if (deviceNumber == null) {
			LOGGER.error("QueryRealtimeBill In-Parameter DeviceNumber is NULL!");
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("QueryRealtimeBill In-Parameter DeviceNumber is NULL!");
			return result;
		}
		InfoUser iu = ops.getUserInfoByDeviceNumber(deviceNumber);
		if ((iu == null) || (iu.getUser_id() == null)) {
			LOGGER.error("QueryRealtimeBill DeviceNumber["+deviceNumber+"] Not Found!");
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("QueryRealtimeBill DeviceNumber["+deviceNumber+"] Not Found!");
			return result;
		}
		PayUserRel pur = ops.getPayInfoByUserId(iu.getUser_id().trim());
		if ((pur == null) || (pur.getPay_id() == null)) {
			LOGGER.error("QueryRealtimeBill DeviceNumber["+deviceNumber+"] PayRelation Abnormal!");
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("QueryRealtimeBill DeviceNumber["+deviceNumber+"] PayRelation Abnormal!");
			return result;
		}
		List<BilActRealTimeBillForOracle> bills = ops.getRealtimeBills(iu.getUser_id(), pur.getPay_id(), getCurrentMonth());
		if ((bills == null) || (bills.isEmpty())) {
			LOGGER.error("QueryRealtimeBill DeviceNumber["+deviceNumber+"] Realtime Bill NOT FOUND!");
			bills = null;
			// result.setStatus(RESULT_STATUS_FAIL);
			// result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			// result.setErrorMessage("QueryRealtimeBill DeviceNumber["+deviceNumber+"] Realtime Bill NOT FOUND!");
			// return result;
		}
		result.setRealtimeBills(bills);
		result.setStatus(RESULT_STATUS_OK);
		result.setErrorCode(null);
		result.setErrorMessage(null);
		return result;
	}
	
	private void reset(QueryRealtimeBillResult result) {
		if (result != null) {
			result.setRealtimeBills(null);
			result.setStatus(RESULT_STATUS_OK);
			result.setErrorCode(null);
			result.setErrorMessage(null);
		}
	}

	private String getCurrentMonth() {
		Date date = new Date(System.currentTimeMillis());
		return date.toString().substring(5, 7);		
	}
	
	public QueryRealtimeBillOps getOps() {
		return ops;
	}

	public void setOps(QueryRealtimeBillOps ops) {
		this.ops = ops;
	}
}
