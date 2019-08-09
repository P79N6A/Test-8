package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * 余额变动结果快照
 * 
 * @author Tian
 *
 */
public class BalanceChangeSnapshot implements Serializable {
	private static final long serialVersionUID = 1L;

	private long balanceId;
	private int balanceTypeId;
	private String changeType;
	private long changeValue;
	private long beforeChangeBalance;
	private long afterChangeBalance;
	private String effDate;
	private String expDate;

	public long getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(long balanceId) {
		this.balanceId = balanceId;
	}

	public int getBalanceTypeId() {
		return balanceTypeId;
	}

	public void setBalanceTypeId(int balanceTypeId) {
		this.balanceTypeId = balanceTypeId;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public long getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(long changeValue) {
		this.changeValue = changeValue;
	}

	public long getBeforeChangeBalance() {
		return beforeChangeBalance;
	}

	public void setBeforeChangeBalance(long beforeChangeBalance) {
		this.beforeChangeBalance = beforeChangeBalance;
	}

	public long getAfterChangeBalance() {
		return afterChangeBalance;
	}

	public void setAfterChangeBalance(long afterChangeBalance) {
		this.afterChangeBalance = afterChangeBalance;
	}

	public String getEffDate() {
		return effDate;
	}

	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	@Override
	public String toString() {
		return "BalanceChangeSnapshot [balanceId=" + balanceId
				+ ", balanceTypeId=" + balanceTypeId + ", changeType="
				+ changeType + ", changeValue=" + changeValue
				+ ", beforeChangeBalance=" + beforeChangeBalance
				+ ", afterChangeBalance=" + afterChangeBalance + ", effDate="
				+ effDate + ", expDate=" + expDate + "]";
	}
}
