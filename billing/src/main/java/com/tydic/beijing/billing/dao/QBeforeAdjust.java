package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class QBeforeAdjust implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serial_no;
	private String adjust_id;
	private String user_id;
	private int acct_month;
	private int status;
	private String insert_date;
	private String update_date;
	public String getSerial_no() {
		return serial_no;
	}
	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}
	public String getAdjust_id() {
		return adjust_id;
	}
	public void setAdjust_id(String adjust_id) {
		this.adjust_id = adjust_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getInsert_date() {
		return insert_date;
	}
	public void setInsert_date(String insert_date) {
		this.insert_date = insert_date;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getAcct_month() {
		return acct_month;
	}
	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}
}
