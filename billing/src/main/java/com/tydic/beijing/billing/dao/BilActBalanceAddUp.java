package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class BilActBalanceAddUp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long balance_id;
	private int balance_type_id;
	private int unit_type_id;
	private int acct_month;
	private String pay_id;
	private String user_id;
	private int acct_item_code;
	private String partition_no;
	private long deduct_fee;
	private String update_time;

	public long getBalance_id() {
		return balance_id;
	}
	public void setBalance_id(long balance_id) {
		this.balance_id = balance_id;
	}
	public int getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(int balance_type_id) {
		this.balance_type_id = balance_type_id;
	}
	public int getUnit_type_id() {
		return unit_type_id;
	}
	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}
	public int getAcct_month() {
		return acct_month;
	}
	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}
	public String getPay_id() {
		return pay_id;
	}
	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getAcct_item_code() {
		return acct_item_code;
	}
	public void setAcct_item_code(int acct_item_code) {
		this.acct_item_code = acct_item_code;
	}
	public String getPartition_no() {
		return partition_no;
	}
	public void setPartition_no(String partition_no) {
		this.partition_no = partition_no;
	}
	public long getDeduct_fee() {
		return deduct_fee;
	}
	public void setDeduct_fee(long deduct_fee) {
		this.deduct_fee = deduct_fee;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "BilActBalanceAddUp [balance_id=" + balance_id
				+ ", balance_type_id=" + balance_type_id + ", unit_type_id="
				+ unit_type_id + ", acct_month=" + acct_month + ", pay_id="
				+ pay_id + ", user_id=" + user_id + ", acct_item_code="
				+ acct_item_code + ", partition_no=" + partition_no
				+ ", deduct_fee=" + deduct_fee + ", update_time=" + update_time
				+ "]";
	}
}
