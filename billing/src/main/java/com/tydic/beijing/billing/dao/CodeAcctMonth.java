package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.Date;

public class CodeAcctMonth implements Serializable {
	/**
	 * 账期定义表 lijianyu
	 */
	private static final long serialVersionUID = 1L;
	private int acct_month;
	private String partition_no;
	private Date bil_eff_date;
	private Date bil_exp_date;
	private Date act_eff_date;
	private Date act_exp_date;
	private Date rev_eff_date;
	private Date rev_exp_date;
	private String use_tag;
	private String act_tag;

	public int getAcct_month() {
		return acct_month;
	}

	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}

	public String getPartition_no() {
		return partition_no;
	}

	public void setPartition_no(String partition_no) {
		this.partition_no = partition_no;
	}

	public Date getBil_eff_date() {
		return bil_eff_date;
	}

	public void setBil_eff_date(Date bil_eff_date) {
		this.bil_eff_date = bil_eff_date;
	}

	public Date getBil_exp_date() {
		return bil_exp_date;
	}

	public void setBil_exp_date(Date bil_exp_date) {
		this.bil_exp_date = bil_exp_date;
	}

	public Date getAct_eff_date() {
		return act_eff_date;
	}

	public void setAct_eff_date(Date act_eff_date) {
		this.act_eff_date = act_eff_date;
	}

	public Date getAct_exp_date() {
		return act_exp_date;
	}

	public void setAct_exp_date(Date act_exp_date) {
		this.act_exp_date = act_exp_date;
	}

	public Date getRev_eff_date() {
		return rev_eff_date;
	}

	public void setRev_eff_date(Date rev_eff_date) {
		this.rev_eff_date = rev_eff_date;
	}

	public Date getRev_exp_date() {
		return rev_exp_date;
	}

	public void setRev_exp_date(Date rev_exp_date) {
		this.rev_exp_date = rev_exp_date;
	}

	public String getUse_tag() {
		return use_tag;
	}

	public void setUse_tag(String use_tag) {
		this.use_tag = use_tag;
	}

	public String getAct_tag() {
		return act_tag;
	}

	public void setAct_tag(String act_tag) {
		this.act_tag = act_tag;
	}

}
