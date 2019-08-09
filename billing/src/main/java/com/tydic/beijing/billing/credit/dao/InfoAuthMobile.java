package com.tydic.beijing.billing.credit.dao;

import java.io.Serializable;
/**
 * InfoAuthMobile
 * 
 * @author bian
 *
 */
public class InfoAuthMobile implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private String auth_id;
	private String customer_id;
	private String user_id;
	private String device_number;
	private String auth_date;
	private String exp_date;
	private int eff_flag;
	private String customer_name;
	private String real_id_type;
	private String real_id_number;
	private String real_id_exp_date;
	private String real_id_address;
	

	public String getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getDevice_number() {
		return device_number;
	}

	public void setDevice_number(String device_number) {
		this.device_number = device_number;
	}

	public String getAuth_date() {
		return auth_date;
	}

	public void setAuth_date(String auth_date) {
		this.auth_date = auth_date;
	}

	public String getExp_date() {
		return exp_date;
	}

	public void setExp_date(String exp_date) {
		this.exp_date = exp_date;
	}

	public int getEff_flag() {
		return eff_flag;
	}

	public void setEff_flag(int eff_flag) {
		this.eff_flag = eff_flag;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getReal_id_type() {
		return real_id_type;
	}

	public void setReal_id_type(String real_id_type) {
		this.real_id_type = real_id_type;
	}

	public String getReal_id_number() {
		return real_id_number;
	}

	public void setReal_id_number(String real_id_number) {
		this.real_id_number = real_id_number;
	}

	public String getReal_id_exp_date() {
		return real_id_exp_date;
	}

	public void setReal_id_exp_date(String real_id_exp_date) {
		this.real_id_exp_date = real_id_exp_date;
	}

	public String getReal_id_address() {
		return real_id_address;
	}

	public void setReal_id_address(String real_id_address) {
		this.real_id_address = real_id_address;
	}
}
