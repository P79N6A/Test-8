package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

import javax.persistence.Id;

//import com.tydic.uda.consistency.Consistency;

public class InfoPayBalanceSku implements Serializable {
	private static final long serialVersionUID = 1L;

	private String user_id;
	private long balance;
	private String org_order_id;
	private int process_tag;
	private String process_time;
	private int month_flag;//0是本月， 1是上月
	private String remark;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public String getOrg_order_id() {
		return org_order_id;
	}

	public void setOrg_order_id(String org_order_id) {
		this.org_order_id = org_order_id;
	}

	public int getProcess_tag() {
		return process_tag;
	}

	public void setProcess_tag(int process_tag) {
		this.process_tag = process_tag;
	}

	public String getProcess_time() {
		return process_time;
	}

	public void setProcess_time(String process_time) {
		this.process_time = process_time;
	}

	public int getMonth_flag() {
		return month_flag;
	}

	public void setMonth_flag(int month_flag) {
		this.month_flag = month_flag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "InfoPayBalanceSku [user_id=" + user_id + ", balance=" + balance + ", org_order_id=" + org_order_id
				+ ", process_tag=" + process_tag + ", process_time=" + process_time + ", month_flag=" + month_flag
				+ ", remark=" + remark + "]";
	}


}
