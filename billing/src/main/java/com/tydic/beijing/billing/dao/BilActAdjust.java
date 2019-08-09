package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class BilActAdjust implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String adjust_id;
	private String user_id;
	private long acct_item_code;
	private String adjust_mode;
	private long adjust_fee;
	private int acct_month;
	private int adjust_flag;
	private int eff_flag;
	private String operate_time;
	public String getAdjust_id() {
		return adjust_id;
	}
	public void setAdjust_id(String adjust_id) {
		this.adjust_id = adjust_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public long getAcct_item_code() {
		return acct_item_code;
	}
	public void setAcct_item_code(long acct_item_code) {
		this.acct_item_code = acct_item_code;
	}
	public String getAdjust_mode() {
		return adjust_mode;
	}
	public void setAdjust_mode(String adjust_mode) {
		this.adjust_mode = adjust_mode;
	}
	public long getAdjust_fee() {
		return adjust_fee;
	}
	public void setAdjust_fee(long adjust_fee) {
		this.adjust_fee = adjust_fee;
	}
	public int getAcct_month() {
		return acct_month;
	}
	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}
	public int getAdjust_flag() {
		return adjust_flag;
	}
	public void setAdjust_flag(int adjust_flag) {
		this.adjust_flag = adjust_flag;
	}
	public int getEff_flag() {
		return eff_flag;
	}
	public void setEff_flag(int eff_flag) {
		this.eff_flag = eff_flag;
	}
	public String getOperate_time() {
		return operate_time;
	}
	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}
	@Override
	public String toString() {
		return "BilActAdjust [adjust_id=" + adjust_id + ", user_id=" + user_id
				+ ", acct_item_code=" + acct_item_code + ", adjust_mode="
				+ adjust_mode + ", adjust_fee=" + adjust_fee + ", acct_month="
				+ acct_month + ", adjust_flag=" + adjust_flag + ", eff_flag="
				+ eff_flag + ", operate_time=" + operate_time + "]";
	}

}
