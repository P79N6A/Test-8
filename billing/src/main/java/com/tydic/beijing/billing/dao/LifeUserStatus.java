package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.Date;

public class LifeUserStatus implements Serializable {

	/**
	 * YUANDAO
	 */
	private static final long serialVersionUID = 1L;
	private String user_status_id;
	private String user_id;
	private String user_status;
	private Date eff_date;
	private Date exp_date;
	private String eff_flag;
	private String order_id;
	private String chng_order_id;

	public String getUser_status_id() {
		return user_status_id;
	}

	public void setUser_status_id(String user_status_id) {
		this.user_status_id = user_status_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
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

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getChng_order_id() {
		return chng_order_id;
	}

	public void setChng_order_id(String chng_order_id) {
		this.chng_order_id = chng_order_id;
	}

	public String toString() {
		return "LifeUserStatus [user_status_id=" + user_status_id + ",user_id="
				+ user_id + ",user_status=" + user_status + ",eff_date="
				+ eff_date + ",exp_date=" + exp_date + ",eff_flag=" + eff_flag
				+ ",order_id=" + order_id + ",chng_order_id=" + chng_order_id
				+ "]";
	}
}
