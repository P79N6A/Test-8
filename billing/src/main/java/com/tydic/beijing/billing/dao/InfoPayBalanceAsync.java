package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author Tian
 *
 */
public class InfoPayBalanceAsync implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sn;
	private long balance_id;
	private String pay_id;
	private int balance_type_id;
	private long balance;
	private int async_state;
	private Date create_time;
	private Date update_time;
	private String uuid;
	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public long getBalance_id() {
		return balance_id;
	}

	public void setBalance_id(long balance_id) {
		this.balance_id = balance_id;
	}

	public String getPay_id() {
		return pay_id;
	}

	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}

	public int getBalance_type_id() {
		return balance_type_id;
	}

	public void setBalance_type_id(int balance_type_id) {
		this.balance_type_id = balance_type_id;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public int getAsync_state() {
		return async_state;
	}

	public void setAsync_state(int async_state) {
		this.async_state = async_state;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "InfoPayBalanceAsync [sn=" + sn + ", balance_id=" + balance_id
				+ ", pay_id=" + pay_id + ", balance_type_id=" + balance_type_id
				+ ", balance=" + balance + ", async_state=" + async_state
				+ ", create_time=" + create_time + ", update_time="
				+ update_time + ", uuid=" + uuid + "]";
	}
}
