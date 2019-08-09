package com.tydic.beijing.billing.branch.dao;

import java.io.Serializable;
import java.sql.Date;

public class LogQBlock implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String pay_id;	
	private String user_id;
	private int block_flag;
	private Date enqueue_date;
	private int serial_num;
	private Date action_time;
	private String source;
	public String getPay_id() {
		return pay_id;
	}
	public void setPay_id(String payId) {
		pay_id = payId;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public int getBlock_flag() {
		return block_flag;
	}
	public void setBlock_flag(int blockFlag) {
		block_flag = blockFlag;
	}
	public Date getEnqueue_date() {
		return enqueue_date;
	}
	public void setEnqueue_date(Date enqueueDate) {
		enqueue_date = enqueueDate;
	}
	public int getSerial_num() {
		return serial_num;
	}
	public void setSerial_num(int serialNum) {
		serial_num = serialNum;
	}
	public Date getAction_time() {
		return action_time;
	}
	public void setAction_time(Date actionTime) {
		action_time = actionTime;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

	public String toString(){
		return "payId:" + this.pay_id + ",userId:" + this.user_id + ",blcokFalg:" + this.block_flag + "serialNum:" + this.serial_num + "source:" + this.source;
	}
	
	
}
