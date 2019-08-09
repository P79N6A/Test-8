package com.tydic.beijing.billing.credit.memcache.dao;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Id;

import com.tydic.uda.UdaAnnotationSetKey;

public class BilActBill4CreditMemcache implements Serializable {

	public static final String KEY_PREFIX = BilActBill4CreditMemcache.class.getSimpleName();
	private static final long serialVersionUID = 1L;
	public String mem_key;
	public long billing_id;
	public String user_id;
	public String pay_id;
	public int acct_month;
	public int acct_item_code;
	public int unit_type_id;
	public long fee;
	public long write_off_fee;
	public long owe_fee;
	public long late_fee;
	public Date insert_date;
	public Date update_time;
	public int invoice_tag;
	public double invoice_fee;
	public Date invoice_time;

	@Id
	public String getMem_key() {
		return mem_key;
	}

	@UdaAnnotationSetKey
	public void setMem_key(String mem_key) {
		this.mem_key = mem_key + user_id + pay_id;
	}

	public long getBilling_id() {
		return billing_id;
	}

	public void setBilling_id(long billing_id) {
		this.billing_id = billing_id;
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

	public long getWrite_off_fee() {
		return write_off_fee;
	}

	public void setWrite_off_fee(long write_off_fee) {
		this.write_off_fee = write_off_fee;
	}

	public long getOwe_fee() {
		return owe_fee;
	}

	public void setOwe_fee(long owe_fee) {
		this.owe_fee = owe_fee;
	}

	public long getLate_fee() {
		return late_fee;
	}

	public void setLate_fee(long late_fee) {
		this.late_fee = late_fee;
	}

	public Date getInsert_date() {
		return insert_date;
	}

	public void setInsert_date(Date insert_date) {
		this.insert_date = insert_date;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public int getInvoice_tag() {
		return invoice_tag;
	}

	public void setInvoice_tag(int invoice_tag) {
		this.invoice_tag = invoice_tag;
	}

	public double getInvoice_fee() {
		return invoice_fee;
	}

	public void setInvoice_fee(double invoice_fee) {
		this.invoice_fee = invoice_fee;
	}

	public Date getInvoice_time() {
		return invoice_time;
	}

	public void setInvoice_time(Date invoice_time) {
		this.invoice_time = invoice_time;
	}

	@Override
	public String toString() {
		return "BilActBill4Credit [billing_id=" + billing_id + ", user_id=" + user_id + ", pay_id="
				+ pay_id + ", acct_month=" + acct_month + ", acct_item_code=" + acct_item_code
				+ ", unit_type_id=" + unit_type_id + ", fee=" + fee + ", write_off_fee="
				+ write_off_fee + ", owe_fee=" + owe_fee + ", late_fee=" + late_fee
				+ ", insert_date=" + insert_date + ", update_time=" + update_time
				+ ", invoice_tag=" + invoice_tag + ", invoice_fee=" + invoice_fee
				+ ", invoice_time=" + invoice_time + "]";
	}

	public void clear() {
		mem_key = "";
		billing_id = -1;
		user_id = "";
		pay_id = "";
		acct_month = -1;
		acct_item_code = -1;
		unit_type_id = -1;
		fee = 0;
		write_off_fee = 0;
		owe_fee = 0;
		late_fee = 0;
		insert_date = null; // String instead of Date
		update_time = null; // String instead of Date
		invoice_tag = -1;
		invoice_fee = 0;
		invoice_time = null;
	}
}
