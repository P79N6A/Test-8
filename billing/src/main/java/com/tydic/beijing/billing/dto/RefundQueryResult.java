package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.List;

import com.tydic.beijing.billing.dao.InfoPayBalance;

/**
 * 
 * @author Tian
 *
 */
public class RefundQueryResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private long refundable; // 可退余额，单位，分
	private long nonRefundable; // 不可退余额，单位，分
	private String errorCode;
	private String errorMessage;
	private List<Long> refundableBalanceIds;
	private List<InfoPayBalance> allInfoPayBalance;
	
	
	public List<InfoPayBalance> getAllInfoPayBalance() {
		return allInfoPayBalance;
	}

	public void setAllInfoPayBalance(List<InfoPayBalance> allInfoPayBalance) {
		this.allInfoPayBalance = allInfoPayBalance;
	}

	public long getRefundable() {
		return refundable;
	}

	public void setRefundable(long refundable) {
		this.refundable = refundable;
	}

	public long getNonRefundable() {
		return nonRefundable;
	}

	public void setNonRefundable(long nonRefundable) {
		this.nonRefundable = nonRefundable;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<Long> getRefundableBalanceIds() {
		return refundableBalanceIds;
	}

	public void setRefundableBalanceIds(List<Long> refundableBalanceIds) {
		this.refundableBalanceIds = refundableBalanceIds;
	}

	@Override
	public String toString() {
		return "RefundQueryResult [refundable=" + refundable + ", nonRefundable=" + nonRefundable + ", errorCode="
				+ errorCode + ", errorMessage=" + errorMessage + ", refundableBalanceIds=" + refundableBalanceIds
				+ ", allInfoPayBalance=" + allInfoPayBalance + "]";
	}
	
	
	
}
