package com.tydic.beijing.bvalue.dao;

public class ExchangeRatableHistory {
	String user_id;
	String acct_month;
	String update_time;
	long ratable_balance;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAcct_month() {
		return acct_month;
	}
	public void setAcct_month(String acct_month) {
		this.acct_month = acct_month;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public long getRatable_balance() {
		return ratable_balance;
	}
	public void setRatable_balance(long ratable_balance) {
		this.ratable_balance = ratable_balance;
	}
	@Override
	public String toString() {
		return "ExchangeRatableHistory [user_id=" + user_id + ", acct_month="
				+ acct_month + ", update_time=" + update_time
				+ ", ratable_balance=" + ratable_balance + "]";
	}
	
	
}
