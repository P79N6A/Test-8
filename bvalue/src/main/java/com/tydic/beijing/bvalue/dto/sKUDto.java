package com.tydic.beijing.bvalue.dto;

public class sKUDto {
	private String sKUID;
	private String changeType;
	private long quantity;
	private String bValue;

	public String getsKUID() {
		return sKUID;
	}

	public void setsKUID(String sKUID) {
		this.sKUID = sKUID;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public String getbValue() {
		return bValue;
	}

	public void setbValue(String bValue) {
		this.bValue = bValue;
	}

	@Override
	public String toString() {
		return "sKUDto [sKUID=" + sKUID + ", changeType=" + changeType + ", quantity=" + quantity
				+ ", bValue=" + bValue + "]";
	}

}
