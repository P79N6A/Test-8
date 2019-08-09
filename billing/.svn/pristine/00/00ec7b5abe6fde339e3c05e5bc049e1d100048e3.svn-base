package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Id;

/**
 * PayUserRel 对应数据库中 PAY_USER_REL（原LIFE_USER_PAY）（支付关系表）
 * 
 * @author Tian
 *
 */
public class PayUserRel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pay_user_id;
	private String user_id;
	private String pay_id;
	private long latn_id;
	private String default_tag;
	private int payitem_code;
	private int paybalance_code;
	private int priority; // 越大优先级越高
	private Date eff_date;
	private Date exp_date;
	private String eff_flag;
	private String limit_type;
	private long limit_valuea;
	private long limit_valueb;

	@Id
	public String getPay_user_id() {
		return pay_user_id;
	}

	public void setPay_user_id(String pay_user_id) {
		this.pay_user_id = pay_user_id;
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

	public long getLatn_id() {
		return latn_id;
	}

	public void setLatn_id(long latn_id) {
		this.latn_id = latn_id;
	}

	public String getDefault_tag() {
		return default_tag;
	}

	public void setDefault_tag(String default_tag) {
		this.default_tag = default_tag;
	}

	public int getPayitem_code() {
		return payitem_code;
	}

	public void setPayitem_code(int payitem_code) {
		this.payitem_code = payitem_code;
	}

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

	public Date getEff_date() {
		return eff_date;
	}

	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}

	public String getEff_flag() {
		return eff_flag;
	}

	public void setEff_flag(String eff_flag) {
		this.eff_flag = eff_flag;
	}

	public String getLimit_type() {
		return limit_type;
	}

	public void setLimit_type(String limit_type) {
		this.limit_type = limit_type;
	}

	public long getLimit_valuea() {
		return limit_valuea;
	}

	public void setLimit_valuea(long limit_valuea) {
		this.limit_valuea = limit_valuea;
	}

	public long getLimit_valueb() {
		return limit_valueb;
	}

	public void setLimit_valueb(long limit_valueb) {
		this.limit_valueb = limit_valueb;
	}

	@Override
	public String toString() {
		return "PayUserId[" + pay_user_id + "]UserId[" + user_id + "]PayId["
				+ pay_id + "]LatnId[" + latn_id + "]DefaultTag[" + default_tag
				+ "]PayItemCode[" + payitem_code + "]PayBalanceCode["
				+ paybalance_code + "]Priority[" + priority + "]EffDate["
				+ eff_date + "]ExpDate[" + exp_date + "]EffFlag[" + eff_flag
				+ "]LimitType[" + limit_type + "]LimitVa[" + limit_valuea
				+ "]LimitVb[" + limit_valueb + "]";
	}
}
