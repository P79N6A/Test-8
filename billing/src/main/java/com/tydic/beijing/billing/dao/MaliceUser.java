package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.sql.Date;

public class MaliceUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String device_number;
	private String user_status;
	private Date create_date;
	private Date active_date;
	private String customer_name;
	private String local_net;
	
	private double real_balance;
	private String jd_acct;
	public String getDevice_number() {
		return device_number;
	}
	public void setDevice_number(String device_number) {
		this.device_number = device_number;
	}
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getActive_date() {
		return active_date;
	}
	public void setActive_date(Date active_date) {
		this.active_date = active_date;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getLocal_net() {
		return local_net;
	}
	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}
	public double getReal_balance() {
		return real_balance;
	}
	public void setReal_balance(double real_balance) {
		this.real_balance = real_balance;
	}
	public String getJd_acct() {
		return jd_acct;
	}
	public void setJd_acct(String jd_acct) {
		this.jd_acct = jd_acct;
	}
}
