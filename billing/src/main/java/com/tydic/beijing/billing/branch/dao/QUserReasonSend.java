package com.tydic.beijing.billing.branch.dao;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author Tian
 *
 */
public class QUserReasonSend implements Serializable {
	private static final long serialVersionUID = 1L;

	private long serial_num;
	private String user_no;
	private String tele_type;
	private String reason_code;
	private Date enqueue_date;
	private String local_net;
	private String active_type;
	private String charge_id;

	public long getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(long serial_num) {
		this.serial_num = serial_num;
	}

	public String getUser_no() {
		return user_no;
	}

	public void setUser_no(String user_no) {
		this.user_no = user_no;
	}

	public String getTele_type() {
		return tele_type;
	}

	public void setTele_type(String tele_type) {
		this.tele_type = tele_type;
	}

	public String getReason_code() {
		return reason_code;
	}

	public void setReason_code(String reason_code) {
		this.reason_code = reason_code;
	}

	public Date getEnqueue_date() {
		return enqueue_date;
	}

	public void setEnqueue_date(Date enqueue_date) {
		this.enqueue_date = enqueue_date;
	}

	public String getLocal_net() {
		return local_net;
	}

	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}

	public String getActive_type() {
		return active_type;
	}

	public void setActive_type(String active_type) {
		this.active_type = active_type;
	}

	public String getCharge_id() {
		return charge_id;
	}

	public void setCharge_id(String charge_id) {
		this.charge_id = charge_id;
	}
	
	public String toString(){
		return "userId:" + this.user_no + ",serial_num:" + this.serial_num + ",localNet:" + 
				this.local_net + ",activeType:" + this.active_type + ",chargeId:" + this.charge_id + 
				",reasonCode:" + this.reason_code + ",teleType" + this.tele_type;
	}
}
