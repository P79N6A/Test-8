package com.tydic.beijing.billing.account.type;

import java.io.Serializable;

/*
 * For Dao2File
 * lijianyu
 */


import com.tydic.beijing.billing.account.core.Dao2File;

public final class BilActUserRealTimeBill extends Dao2File implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private int acct_month;
	private int acct_item_code;
	private int unit_type_id;
	private long org_fee;
	private long fee;
	private long discount_fee;
	private long adjust_before;
	private String insert_date; // String instead of Date
	private String partition_num;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getAcct_month() {
		return acct_month;
	}

	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
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
	public long getFee() {
		return fee;
	}

	public void setFee(long fee) {
		this.fee = fee;
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

	public String getInsert_date() {
		return insert_date;
	}

	public void setInsert_date(String insert_date) {
		this.insert_date = insert_date;
	}

	public String getPartition_num() {
		return partition_num;
	}

	public void setPartition_num(String partition_num) {
		this.partition_num = partition_num;
	}

	public String dao2Line() {
		return "" + user_id + "|" + acct_month + "|" + acct_item_code + "|"
				+ unit_type_id + "|" + org_fee + "|" + fee + "|" + discount_fee
				+ "|" + adjust_before + "|" + insert_date + "|" + partition_num
				+ "\n";
	}
	
	@Override
	public String toString() {
		return "BilActUserRealTimeBill [user_id=" + user_id + ", acct_month="
				+ acct_month + ", acct_item_code=" + acct_item_code
				+ ", unit_type_id=" + unit_type_id + ", org_fee=" + org_fee
				+ ", fee=" + fee + ", discount_fee=" + discount_fee
				+ ", adjust_before=" + adjust_before + ", insert_date="
				+ insert_date + ", partition_num=" + partition_num + "]";
	}	
}
