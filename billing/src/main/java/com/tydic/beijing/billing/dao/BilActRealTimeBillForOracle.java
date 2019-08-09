package com.tydic.beijing.billing.dao;

import java.io.Serializable;

/**
 * 
 * @author Tian
 *
 */
public class BilActRealTimeBillForOracle implements Serializable {
	private static final long serialVersionUID = 1L;

	private String user_id;
	private String pay_id;
	private int acct_month;
	private String partition_num;
	private int acct_item_code;
	private int unit_type_id;
	private long org_fee;
	private long discount_fee;
	private long adjust_before;
	private long fee;
	private long non_deduct_fee;
	private String insert_date;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPay_id() {
		return pay_id;
	}

	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}

	public int getAcct_month() {
		return acct_month;
	}

	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}

	public String getPartition_num() {
		return partition_num;
	}

	public void setPartition_num(String partition_num) {
		this.partition_num = partition_num;
	}

	public int getAcct_item_code() {
		return acct_item_code;
	}

	public void setAcct_item_code(int acct_item_code) {
		this.acct_item_code = acct_item_code;
	}

	public int getUnit_type_id() {
		return unit_type_id;
	}

	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}

	public long getOrg_fee() {
		return org_fee;
	}

	public void setOrg_fee(long org_fee) {
		this.org_fee = org_fee;
	}

	public long getDiscount_fee() {
		return discount_fee;
	}

	public void setDiscount_fee(long discount_fee) {
		this.discount_fee = discount_fee;
	}

	public long getAdjust_before() {
		return adjust_before;
	}

	public void setAdjust_before(long adjust_before) {
		this.adjust_before = adjust_before;
	}

	public long getFee() {
		return fee;
	}

	public void setFee(long fee) {
		this.fee = fee;
	}

	public long getNon_deduct_fee() {
		return non_deduct_fee;
	}

	public void setNon_deduct_fee(long non_deduct_fee) {
		this.non_deduct_fee = non_deduct_fee;
	}

	public String getInsert_date() {
		return insert_date;
	}

	public void setInsert_date(String insert_date) {
		this.insert_date = insert_date;
	}

	@Override
	public String toString() {
		return "BilActRealTimeBill [user_id=" + user_id + ", pay_id=" + pay_id
				+ ", acct_month=" + acct_month + ", partition_num="
				+ partition_num + ", acct_item_code=" + acct_item_code
				+ ", unit_type_id=" + unit_type_id + ", org_fee=" + org_fee
				+ ", fee=" + fee + ", non_deduct_fee=" + non_deduct_fee
				+ ", insert_date=" + insert_date + "]";
	}
}
