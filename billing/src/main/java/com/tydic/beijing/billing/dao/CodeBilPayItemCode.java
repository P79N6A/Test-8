package com.tydic.beijing.billing.dao;

import java.io.Serializable;

/**
 * CodeBilPayItemCode对应数据库中CODE_BIL_PAY_ITEM_CODE（付费账目编码）
 * 
 * @author Tian
 *
 */
public final class CodeBilPayItemCode implements Serializable {
	private static final long serialVersionUID = 1L;
	private int payitem_code;
	private int priority;
	private int acct_item_code;
	private String limit_type;
	private int limit_valueb;
	private long limit_valuea;

	public int getPayitem_code() {
		return payitem_code;
	}

	public void setPayitem_code(int payitem_code) {
		this.payitem_code = payitem_code;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getAcct_item_code() {
		return acct_item_code;
	}

	public void setAcct_item_code(int acct_item_code) {
		this.acct_item_code = acct_item_code;
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
