package com.tydic.beijing.billing.account.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.biz.RefundQueryOps;
import com.tydic.beijing.billing.account.service.RefundQuery;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.beijing.billing.dto.RefundQueryResult;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class RefundQueryImpl implements RefundQuery {
	private final static Logger LOGGER = Logger
			.getLogger(RefundQueryImpl.class);

	private RefundQueryOps ops;

	/**
	 * CRM调用此方法，传入设备号，查询有效用户的可退余额
	 */
	@Override
	public RefundQueryResult doQuery(String deviceNumber) {
		if (deviceNumber == null || deviceNumber.trim().equals("")) {
			LOGGER.error("RefundQuery DeviceNumber is Null or Empty!");
			RefundQueryResult result = new RefundQueryResult();
			reset(result);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("参数校验错，设备号空!");
			return result;
		}
		String userId = getUserIdByDeviceNumber(deviceNumber);
		return doQuery(deviceNumber, userId);
	}

	@Override
	public RefundQueryResult doQuery(String deviceNumber, String userId) {
		RefundQueryResult result = new RefundQueryResult();
		RefundQueryResult queryResult = null;
		reset(result);
		if (deviceNumber == null || deviceNumber.trim().equals("")) {
			LOGGER.error("RefundQuery DeviceNumber is Null or Empty!");
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("参数校验错，设备号空!");
			return result;
		}
		if (userId == null || userId.trim().equals("")) {
			LOGGER.error("RefundQuery DeviceNumber[" + deviceNumber
					+ "]NOT Exist!");
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("设备号[" + deviceNumber + "]用户信息不存在!");
			return result;
		}
		UserInfoForMemCached userInfo = getUserInfoByDeviceNumber(deviceNumber,
				userId);
		if (userInfo == null) {
			LOGGER.error("RefundQuery DeviceNumber[" + deviceNumber
					+ "] Info Not Found!");
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("退费查询设备号[" + deviceNumber + "]信息未找到!");
		} else {
			List<PayUserRel> lups = userInfo.getPayUserRels();
			if ((lups == null) || (lups.isEmpty())) {
				LOGGER.error("RefundQuery DeviceNumber[" + deviceNumber
						+ "] Pay Relation Not Found!");
				result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				result.setErrorMessage("退费查询设备号[" + deviceNumber + "]支付信息未找到!");
			} else {
				try {
					// SQL中按失效时间降序排列，取失效时间最近
					PayUserRel lup = lups.get(0);
					LOGGER.debug("RefundQuery DeviceNumber[" + deviceNumber
							+ "] Default PayId[" + lup.getPay_id() + "]");
					queryResult = ops.queryRefund(lup.getPay_id());
				} catch (BasicException ex) {
					LOGGER.error(ex.toString());
					result.setErrorCode(Integer.toString(ex.getCode()));
					result.setErrorMessage(ex.getMessage());
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage());
					result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
					result.setErrorMessage(ex.getMessage());
				}
			}

		}
		if (queryResult != null) {
			result.setRefundable(queryResult.getRefundable());
			result.setNonRefundable(queryResult.getNonRefundable());
			result.setRefundableBalanceIds(queryResult
					.getRefundableBalanceIds());
			result.setAllInfoPayBalance(queryResult.getAllInfoPayBalance());
		}
		return result;
	}

	private UserInfoForMemCached getUserInfoByDeviceNumber(String deviceNumber,
			String userId) {
		UserInfoForMemCached userInfo = null;
		List<PayUserRel> purs = S.get(PayUserRel.class).query(
				Condition.build("getByUserIdForRefund").filter("user_id",
						userId));
		if (purs != null) {
			userInfo = new UserInfoForMemCached();
			userInfo.setDevice_number(deviceNumber);
			userInfo.setInfoUser(null);
			userInfo.setPayUserRels(purs);
			userInfo.setUserInfoPays(null);
			userInfo.setUserProducts(null);
			return userInfo;
		}
		return null;
	}

	private String getUserIdByDeviceNumber(String deviceNumber) {
		InfoUser iu = S.get(InfoUser.class).queryFirst(
				Condition.build("queryByDeviceNo").filter("device_number",
						deviceNumber));
		if (iu != null) {
			return iu.getUser_id();
		}
		return null;
	}

	private void reset(RefundQueryResult result) {
		result.setRefundable(0L);
		result.setNonRefundable(0L);
		result.setErrorCode(null);
		result.setErrorMessage(null);
		result.setRefundableBalanceIds(null);
		result.setAllInfoPayBalance(null);
	}

	public RefundQueryOps getOps() {
		return ops;
	}

	public void setOps(RefundQueryOps ops) {
		this.ops = ops;
	}
}
