package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class RuleCycleHistory implements Serializable {

	private static final long serialVersionUID = 1L;
	private String user_id;

	private String ofr_id;

	private String user_state;

	private int tariff_id;

	private String user_product_id;
	private String product_id;

	private String present_last_time;

	private String event_type_id;

	private long tariff_value;

	private int acct_month;
	
	private int item_group;
	
	
	
	
	public int getItem_group() {
		return item_group;
	}

	public void setItem_group(int item_group) {
		this.item_group = item_group;
	}

	public int getAcct_month() {
		return acct_month;
	}

	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getOfr_id() {
		return ofr_id;
	}

	public void setOfr_id(String ofr_id) {
		this.ofr_id = ofr_id;
	}

	public String getUser_state() {
		return user_state;
	}

	public void setUser_state(String user_state) {
		this.user_state = user_state;
	}

	public int getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(int tariff_id) {
		this.tariff_id = tariff_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getPresent_last_time() {
		return present_last_time;
	}

	public void setPresent_last_time(String present_last_time) {
		this.present_last_time = present_last_time;
	}



	public String getEvent_type_id() {
		return event_type_id;
	}

	public void setEvent_type_id(String event_type_id) {
		this.event_type_id = event_type_id;
	}

	public long getTariff_value() {
		return tariff_value;
	}

	public void setTariff_value(long tariff_value) {
		this.tariff_value = tariff_value;
	}

	public String getUser_product_id() {
		return user_product_id;
	}

	public void setUser_product_id(String user_product_id) {
		this.user_product_id = user_product_id;
	}

	public String toString() {
		return "RuleCycleHistory [ + user_id=" + this.user_id + "ofr_id="
				+ this.ofr_id + ",user_state=" + this.user_state
				+ ",tariff_id=" + this.tariff_id + ",product_id="
				+ this.product_id +",user_product_id=" +user_product_id +",present_last_time="
				+ this.present_last_time + ",event_type_id="
				+ this.event_type_id + ",tariff_value=" + this.tariff_value
				+ " ]";
	}

}
