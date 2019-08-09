package com.tydic.beijing.billing.dao;

import java.io.Serializable;

import javax.persistence.Id;

/**
 * LifeUserPayAddup对应数据库中LIFE_USER_PAY_ADDUP(支付关系累计表)
 * 
 * @author Tian
 *
 */
public final class LifeUserPayAddup implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user_pay_id;
	private String user_id;
	private String pay_id;
	private int acct_month;
	private long write_off_fee;

	@Id
	public String getUser_pay_id() {
		return user_pay_id;
	}

	public void setUser_pay_id(String user_pay_id) {
		this.user_pay_id = user_pay_id;
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

	public int getAcct_month() {
		return acct_month;
	}

	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}

	public long getWrite_off_fee() {
		return write_off_fee;
	}

	public void setWrite_off_fee(long write_off_fee) {
		this.write_off_fee = write_off_fee;
	}

}
