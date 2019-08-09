package com.tydic.beijing.billing.credit.memcache.dao;

import java.io.Serializable;

import javax.persistence.Id;

import com.tydic.uda.UdaAnnotationSetKey;

public class BilActRealTimeBill4CreditMemcache implements Serializable {
	public static final String KEY_PREFIX = BilActRealTimeBill4CreditMemcache.class.getSimpleName();
	private static final long serialVersionUID = 1L;
	public String mem_key;
	public String user_id;
	public String pay_id;
	public int acct_month;
	public String partition_num;
	public int acct_item_code;
	public int unit_type_id;
	public long fee;
	public long non_deduct_fee;
	public String insert_date;

	@Id
	public String getMem_key() {
		return mem_key;
	}

	@UdaAnnotationSetKey
	public void setMem_key(String mem_key) {
		this.mem_key = mem_key + user_id + pay_id;
	}

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
		return "BilActRealTimeBill4Credit [user_id=" + user_id + ", pay_id=" + pay_id
				+ ", acct_month=" + acct_month + ", partition_num=" + partition_num
				+ ", acct_item_code=" + acct_item_code + ", unit_type_id=" + unit_type_id
				+ ", fee=" + fee + ", non_deduct_fee=" + non_deduct_fee + ", insert_date="
				+ insert_date + "]";
	}

	public void clear() {
		mem_key = "";
		user_id = "";
		pay_id = "";
		acct_month = -1;
		partition_num = "";
		acct_item_code = -1;
		unit_type_id = -1;
		fee = 0;
		non_deduct_fee = 0;
		insert_date = null;
	}

}
