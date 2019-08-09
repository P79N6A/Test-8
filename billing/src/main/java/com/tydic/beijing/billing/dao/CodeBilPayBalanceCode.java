package com.tydic.beijing.billing.dao;

import java.io.Serializable;

/**
 * CodeBilPayBalanceCode对应数据库中CODE_BIL_PAY_BALANCE_CODE（付费账本编码表）
 * 
 * @author Tian
 *
 */
public final class CodeBilPayBalanceCode implements Serializable {
	private static final long serialVersionUID = 1L;
	private int paybalance_code;
	private int priority;
	private int balance_type_id;
	private String limit_type;
	private int limit_valueb;
	private long limit_valuea;

	public int getPaybalance_code() {
		return paybalance_code;
	}

	public void setPaybalance_code(int paybalance_code) {
		this.paybalance_code = paybalance_code;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getBalance_type_id() {
		return balance_type_id;
	}

	public void setBalance_type_id(int balance_type_id) {
		this.balance_type_id = balance_type_id;
	}

	public String getLimit_type() {
		return limit_type;
	}

	public void setLimit_type(String limit_type) {
		this.limit_type = limit_type;
	}

	public int getLimit_valueb() {
		return limit_valueb;
	}

	public void setLimit_valueb(int limit_valueb) {
		this.limit_valueb = limit_valueb;
	}

	public long getLimit_valuea() {
		return limit_valuea;
	}

	public void setLimit_valuea(long limit_valuea) {
		this.limit_valuea = limit_valuea;
	}

}
