package com.tydic.beijing.billing.interfacex.service.impl;

import java.util.Iterator;
import java.util.List;

import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.LogActPay;
import com.tydic.beijing.billing.dto.RechargeQueryInfo;
import com.tydic.beijing.billing.dto.RechargeQueryResult;
import com.tydic.beijing.billing.interfacex.biz.RechargeQueryOps;
import com.tydic.beijing.billing.interfacex.service.RechargeQuery;

/**
 * 充值查询接口
 * 
 * @author Tian
 *
 */
public class RechargeQueryImpl implements RechargeQuery {

	public static final String STATUS_OK = "1";
	public static final String STATUS_FAIL = "0";

	public static final long RECHARGE_STATUS_OK = 0L;
	public static final long RECHARGE_STATUS_FAIL = 1L;
	public static final long RECHARGE_STATUS_UNEXISTS = 9L;
	public static final int OUTER_TIME_LENGTH = 19;

	RechargeQueryOps ops;

	@Override
	public RechargeQueryResult doQuery(RechargeQueryInfo info) {
		RechargeQueryResult result = new RechargeQueryResult();
		resetResult(info, result);
		String errorMsg = checkInputParameter(info);
		if (errorMsg != null) {
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage(errorMsg);
			result.setRechargeStatus(RECHARGE_STATUS_FAIL);
			return result;
		}
		List<LogActPay> laps = ops.queryRechargeLogBySerialNo(info
				.getSn());
		Long amount = 0L;
		String payTime = null;
		if (laps != null) {
			for (Iterator<LogActPay> iter = laps.iterator(); iter.hasNext();) {
				LogActPay lap = iter.next();
				/* bug fixed@20141106 for: 无需校验接触渠道
				if (lap.getPay_channel() != null) {
					if (lap.getPay_channel().trim()
							.equalsIgnoreCase(info.getContactChannle().trim())) {
						amount += lap.getRece_fee();
						payTime = lap.getPay_time();
					}
				}
				*/
				amount += lap.getRece_fee();
				payTime = lap.getPay_time();
			}
		}
		if ((amount > 0L) && (payTime != null)) {
			result.setCharge(amount);
			result.setRechargeTime(alterTimeFormat(payTime));
		} else {
			result.setRechargeStatus(RECHARGE_STATUS_UNEXISTS);
		}
		return result;
	}

	private String alterTimeFormat(String time) {
		if (time != null) {
			String alterTime = time.replaceAll(":", "-");
			if (alterTime.length() > OUTER_TIME_LENGTH) {
				alterTime = alterTime.substring(0, OUTER_TIME_LENGTH);
			}
			return alterTime;
		}
		return null;
	}
	
	private void resetResult(RechargeQueryInfo info, RechargeQueryResult result) {
		result.setStatus(STATUS_OK);
		result.setSn(info.getSn());
		result.setCharge(0L);
		result.setRechargeTime(null);
		result.setErrorCode(null);
		result.setErrorMessage(null);
		result.setRechargeStatus(RECHARGE_STATUS_OK);
	}

	private String checkInputParameter(RechargeQueryInfo info) {
		if ((info.getSn() == null)
				|| (info.getSn().trim().equals(""))) {
			return "查询流水号为空";
		}
		if ((info.getContactChannle() == null)
				|| (info.getContactChannle().trim().equals(""))) {
			return "查询缴费渠道为空";
		}
		return null;
	}

	public RechargeQueryOps getOps() {
		return ops;
	}

	public void setOps(RechargeQueryOps ops) {
		this.ops = ops;
	}
}
