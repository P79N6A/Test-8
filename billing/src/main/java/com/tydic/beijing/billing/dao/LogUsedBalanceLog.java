package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class LogUsedBalanceLog implements Serializable {
	/**
	 * LOG_USED_BALANCE_LOG
	 * lijianyu
	 */
	private static final long serialVersionUID = 1L;
	private String user_id;
	private int acct_month;
	private String insert_timestamp; 
	private long balance_id; 
	private long balance_type_id;
	private long used_fee;
	private int process_tag;
	private String insert_date;
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
	public String getInsert_timestamp() {
		return insert_timestamp;
	}
	public void setInsert_timestamp(String insert_timestamp) {
		this.insert_timestamp = insert_timestamp;
	}
	public long getBalance_id() {
		return balance_id;
	}
	public void setBalance_id(long balance_id) {
		this.balance_id = balance_id;
	}
	public long getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(long balance_type_id) {
		this.balance_type_id = balance_type_id;
	}
	public long getUsed_fee() {
		return used_fee;
	}
	public void setUsed_fee(long used_fee) {
		this.used_fee = used_fee;
	}
	public int getProcess_tag() {
		return process_tag;
	}
	public void setProcess_tag(int process_tag) {
		this.process_tag = process_tag;
	}
	public String getInsert_date() {
		return insert_date;
	}
	public void setInsert_date(String insert_date) {
		this.insert_date = insert_date;
	}
}
