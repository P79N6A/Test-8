package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * 余额变动信息
 * 
 * @author Tian
 *
 */
public class BalanceChangeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private long balanceId;
	private int balanceTypeId;
	private String changeType;
	private long changeValue;
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
		return "BalanceChangeInfo [balanceId=" + balanceId + ", balanceTypeId="
				+ balanceTypeId + ", changeType=" + changeType
				+ ", changeValue=" + changeValue + ", effDate=" + effDate
				+ ", expDate=" + expDate + "]";
	}
}
