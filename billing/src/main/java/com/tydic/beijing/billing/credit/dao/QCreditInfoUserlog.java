package com.tydic.beijing.billing.credit.dao;

import java.io.Serializable;
import java.sql.Date;

public class QCreditInfoUserlog  implements Serializable {
	private static final long serialVersionUID = 1L;

	public String serial_num;
	public int action;
	public Date enqueue_date;
	public String user_id;
	public String device_number;
	public String process_time;
   
	public String getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public Date getEnqueue_date() {
		return enqueue_date;
	}

	public void setEnqueue_date(Date enqueue_date) {
		this.enqueue_date = enqueue_date;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getDevice_number() {
		return device_number;
	}

	public void setDevice_number(String device_number) {
		this.device_number = device_number;
	}

	public String getProcess_time() {
		return process_time;
	}

	public void setProcess_time(String process_time) {
		this.process_time = process_time;
	}

	@Override
	public String toString() {
		return "QCreditInfoUserlog [serial_num=" + serial_num + ", action=" + action + ", enqueue_date=" + enqueue_date
				+ ", user_id=" + user_id + ", device_number=" + device_number + ", process_time=" + process_time
				+ "]";
	}

	
}
