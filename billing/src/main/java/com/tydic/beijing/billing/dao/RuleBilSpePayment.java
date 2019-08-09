package com.tydic.beijing.billing.dao;

import java.io.Serializable;

/**
 * RuleBilSpePayment 对应数据库中RULE_BIL_SPE_PAYMENT（扣费规则表）
 * 
 * @author Tian
 *
 */
public final class RuleBilSpePayment implements Serializable {
	private static final long serialVersionUID = 1L;
	private int spe_payment_id;
	private int include_tag;
	private int acct_item_code;
	private int priority; // 越大优先级越高

	public int getSpe_payment_id() {
		return spe_payment_id;
	}

	public void setSpe_payment_id(int spe_payment_id) {
		this.spe_payment_id = spe_payment_id;
	}

	public int getInclude_tag() {
		return include_tag;
	}

	public void setInclude_tag(int include_tag) {
		this.include_tag = include_tag;
	}

	public int getAcct_item_code() {
		return acct_item_code;
	}

	public void setAcct_item_code(int acct_item_code) {
		this.acct_item_code = acct_item_code;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
