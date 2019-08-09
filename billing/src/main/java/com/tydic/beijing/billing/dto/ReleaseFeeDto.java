package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.sql.Date;

public class ReleaseFeeDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	private String BalanceTypeName;
	private String ReleaseFee;
	private String GiveStatus;
	private String NoGiveReason;
	private String ReturnType;
	private String EffDate;
	private String ExpDate;
	public String getBalanceTypeName() {
		return BalanceTypeName;
	}
	public void setBalanceTypeName(String balanceTypeName) {
		BalanceTypeName = balanceTypeName;
	}
	public String getReleaseFee() {
		return ReleaseFee;
	}
	public void setReleaseFee(String releaseFee) {
		ReleaseFee = releaseFee;
	}
	public String getGiveStatus() {
		return GiveStatus;
	}
	public void setGiveStatus(String giveStatus) {
		GiveStatus = giveStatus;
	}
	public String getNoGiveReason() {
		return NoGiveReason;
	}
	public void setNoGiveReason(String noGiveReason) {
		NoGiveReason = noGiveReason;
	}
	
	public String getReturnType() {
		return ReturnType;
	}
	public void setReturnType(String returnType) {
		ReturnType = returnType;
	}
	
	public String getEffDate() {
		return EffDate;
	}
	public void setEffDate(String effDate) {
		EffDate = effDate;
	}
	public String getExpDate() {
		return ExpDate;
	}
	public void setExpDate(String expDate) {
		ExpDate = expDate;
	}
	@Override
	public String toString() {
		return "ReleaseFeeDto [BalanceTypeName=" + BalanceTypeName + ", ReleaseFee=" + ReleaseFee + ", GiveStatus="
				+ GiveStatus + ", NoGiveReason=" + NoGiveReason + ", ReturnType=" + ReturnType + ", EffDate=" + EffDate
				+ ", ExpDate=" + ExpDate + "]";
	}
	
	
}
