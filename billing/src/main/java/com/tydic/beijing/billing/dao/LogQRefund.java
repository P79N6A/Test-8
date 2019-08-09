package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author Tian
 *
 */
public class LogQRefund implements Serializable {
	private static final long serialVersionUID = 1L;

	private String log_serial_no;
	private String device_number;
	private String jd_acct;
	private String cust_name;
	private int state;
	private long balance;
	private long non_balance;
	private long audit_balance;
	private long refund;
	private String user_event_code;
	private String user_id;
	private Date insert_time;
	private Date update_time;

	public String getLog_serial_no() {
		return log_serial_no;
	}

	public void setLog_serial_no(String log_serial_no) {
		this.log_serial_no = log_serial_no;
	}

	public String getDevice_number() {
		return device_number;
	}

	public void setDevice_number(String device_number) {
		this.device_number = device_number;
	}

	public String getJd_acct() {
		return jd_acct;
	}

	public void setJd_acct(String jd_acct) {
		this.jd_acct = jd_acct;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public long getNon_balance() {
		return non_balance;
	}

	public void setNon_balance(long non_balance) {
		this.non_balance = non_balance;
	}

	public long getAudit_balance() {
		return audit_balance;
	}

	public void setAudit_balance(long audit_balance) {
		this.audit_balance = audit_balance;
	}

	public long getRefund() {
		return refund;
	}

	public void setRefund(long refund) {
		this.refund = refund;
	}

	public String getUser_event_code() {
		return user_event_code;
	}

	public void setUser_event_code(String user_event_code) {
		this.user_event_code = user_event_code;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Date getInsert_time() {
		return insert_time;
	}

	public void setInsert_time(Date insert_time) {
		this.insert_time = insert_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
}
