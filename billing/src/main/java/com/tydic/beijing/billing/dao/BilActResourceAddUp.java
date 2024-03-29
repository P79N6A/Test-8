package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class BilActResourceAddUp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String user_id;
	private int acct_month;
	private String partition_num;
	private long resource_id;
	private long resource_value;
	private String update_time;
	private String insert_timestamp;
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
	public String getPartition_num() {
		return partition_num;
	}
	public void setPartition_num(String partition_num) {
		this.partition_num = partition_num;
	}
	public long getResource_id() {
		return resource_id;
	}
	public void setResource_id(long resource_id) {
		this.resource_id = resource_id;
	}
	public long getResource_value() {
		return resource_value;
	}
	public void setResource_value(long resource_value) {
		this.resource_value = resource_value;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getInsert_timestamp() {
		return insert_timestamp;
	}
	public void setInsert_timestamp(String insert_timestamp) {
		this.insert_timestamp = insert_timestamp;
	}
}
