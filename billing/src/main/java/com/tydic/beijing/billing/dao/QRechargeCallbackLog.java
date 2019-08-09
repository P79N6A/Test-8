package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class QRechargeCallbackLog implements Serializable{
	private static final long serialVersionUID = 1L;
	private String serial_no;
	private String device_number;
	private String user_id;
	private String pay_id;
	private String recharge_detail;
	private String callbackurl;
	private String pay_time;
	private String process_time;
	private int state;
	public String getSerial_no() {
		return serial_no;
	}
	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}
	
	public String getDevice_number() {
		return device_number;
	}
	public void setDevice_number(String device_number) {
		this.device_number = device_number;
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
	public String getRecharge_detail() {
		return recharge_detail;
	}
	public void setRecharge_detail(String recharge_detail) {
		this.recharge_detail = recharge_detail;
	}
	public String getCallbackurl() {
		return callbackurl;
	}
	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}
	
	public String getProcess_time() {
		return process_time;
	}
	public void setProcess_time(String process_time) {
		this.process_time = process_time;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "QRechargeCallbackLog [serial_no=" + serial_no + ", device_number=" + device_number + ", user_id=" + user_id
				+ ", pay_id=" + pay_id + ", recharge_detail=" + recharge_detail + ", callbackurl=" + callbackurl
				+ ", pay_time=" + pay_time + ", process_time=" + process_time + ", state=" + state + "]";
	}
	
}
