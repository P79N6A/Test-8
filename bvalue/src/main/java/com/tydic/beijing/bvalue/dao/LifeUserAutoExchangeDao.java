package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class LifeUserAutoExchangeDao implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exchange_id;
	private String user_id;
	private String cycle_type;
	private String purchase_mode;
	private long top_b_value;
	private String exchange_mode;
	private String resource_list_id;
	private String eff_date;
	private String exp_date;

	public String getExchange_id() {
		return exchange_id;
	}

	public void setExchange_id(String exchange_id) {
		this.exchange_id = exchange_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCycle_type() {
		return cycle_type;
	}

	public void setCycle_type(String cycle_type) {
		this.cycle_type = cycle_type;
	}

	public String getPurchase_mode() {
		return purchase_mode;
	}

	public void setPurchase_mode(String purchase_mode) {
		this.purchase_mode = purchase_mode;
	}

	public long getTop_b_value() {
		return top_b_value;
	}

	public void setTop_b_value(long top_b_value) {
		this.top_b_value = top_b_value;
	}

	public String getExchange_mode() {
		return exchange_mode;
	}

	public void setExchange_mode(String exchange_mode) {
		this.exchange_mode = exchange_mode;
	}

	public String getResource_list_id() {
		return resource_list_id;
	}

	public void setResource_list_id(String resource_list_id) {
		this.resource_list_id = resource_list_id;
	}

	public String getEff_date() {
		return eff_date;
	}

	public void setEff_date(String eff_date) {
		this.eff_date = eff_date;
	}

	public String getExp_date() {
		return exp_date;
	}

	public void setExp_date(String exp_date) {
		this.exp_date = exp_date;
	}

	@Override
	public String toString() {
		return "LifeUserAutoExchange [exchange_id=" + exchange_id + ", user_id=" + user_id
				+ ", cycle_type=" + cycle_type + ", purchase_mode=" + purchase_mode
				+ ", top_b_value=" + top_b_value + ", exchange_mode=" + exchange_mode
				+ ", resource_list_id=" + resource_list_id + ", eff_date=" + eff_date
				+ ", exp_date=" + exp_date + "]";
	}
	
	
	
}
