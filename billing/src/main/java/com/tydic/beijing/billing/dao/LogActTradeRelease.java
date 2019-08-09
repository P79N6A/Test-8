package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.sql.Date;

public class LogActTradeRelease implements Serializable {
	private static final long serialVersionUID = 1L;

	private String release_operate_id;
	private String user_id;
	private String pay_id;
	private long balance_id;
	private String frozen_id;
	private long balance_type_id;
	private long release_balance;
	private Date process_time;
	public String getRelease_operate_id() {
		return release_operate_id;
	}
	public void setRelease_operate_id(String release_operate_id) {
		this.release_operate_id = release_operate_id;
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
	public long getBalance_id() {
		return balance_id;
	}
	public void setBalance_id(long balance_id) {
		this.balance_id = balance_id;
	}
	public String getFrozen_id() {
		return frozen_id;
	}
	public void setFrozen_id(String frozen_id) {
		this.frozen_id = frozen_id;
	}
	public long getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(long balance_type_id) {
		this.balance_type_id = balance_type_id;
	}
	public long getRelease_balance() {
		return release_balance;
	}
	public void setRelease_balance(long release_balance) {
		this.release_balance = release_balance;
	}
	public Date getProcess_time() {
		return process_time;
	}
	public void setProcess_time(Date process_time) {
		this.process_time = process_time;
	}
	@Override
	public String toString() {
		return "LogActTradeRelease [release_operate_id=" + release_operate_id
				+ ", user_id=" + user_id + ", pay_id=" + pay_id
				+ ", balance_id=" + balance_id + ", frozen_id=" + frozen_id
				+ ", balance_type_id=" + balance_type_id + ", release_balance="
				+ release_balance + ", process_time=" + process_time + "]";
	}
}
