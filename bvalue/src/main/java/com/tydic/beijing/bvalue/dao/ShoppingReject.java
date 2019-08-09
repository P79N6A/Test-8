package com.tydic.beijing.bvalue.dao;

public class ShoppingReject {

	//购物退
	private String jdpin;
	private String orderno;
	private long amount;
	private String completetime;
	private String orgorderno;
	public String getJdpin() {
		return jdpin;
	}
	public void setJdpin(String jdpin) {
		this.jdpin = jdpin;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getCompletetime() {
		return completetime;
	}
	public void setCompletetime(String completetime) {
		this.completetime = completetime;
	}
	public String getOrgorderno() {
		return orgorderno;
	}
	public void setOrgorderno(String orgorderno) {
		this.orgorderno = orgorderno;
	}

}
