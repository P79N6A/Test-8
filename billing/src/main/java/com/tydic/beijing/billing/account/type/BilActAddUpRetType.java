package com.tydic.beijing.billing.account.type;

public class BilActAddUpRetType {
	private String user_id;
	private String partition_no;
	private int acct_month;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPartition_no() {
		return partition_no;
	}
	public void setPartition_no(String partition_no) {
		this.partition_no = partition_no;
	}
	public int getAcct_month() {
		return acct_month;
	}
	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}
}
