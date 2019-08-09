package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class SKU implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sKUID;
	private String changeType;
	private String quantity;
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

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
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
		return "SKU [sKUID=" + sKUID + ", changeType=" + changeType + ", quantity=" + quantity
				+ ", bValue=" + bValue + "]";
	}

}
