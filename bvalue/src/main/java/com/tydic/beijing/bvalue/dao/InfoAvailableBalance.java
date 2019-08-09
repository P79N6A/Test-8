package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;
import java.util.Date;

public class InfoAvailableBalance implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String balance_id;
	private String user_id;
	private String balance_type_id;
	private long balance;
	private Date eff_date ;
	private Date exp_date;
	private String resource_type_code;
	private String priority ;
	public String getBalance_id() {
		return balance_id;
	}
	public void setBalance_id(String balance_id) {
		this.balance_id = balance_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(String balance_type_id) {
		this.balance_type_id = balance_type_id;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
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
	public String getResource_type_code() {
		return resource_type_code;
	}
	public void setResource_type_code(String resource_type_code) {
		this.resource_type_code = resource_type_code;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	@Override
	public String toString() {
		return "InfoAvailableBalance [balance_id=" + balance_id + ", user_id="
				+ user_id + ", balance_type_id=" + balance_type_id
				+ ", balance=" + balance + ", eff_date=" + eff_date
				+ ", exp_date=" + exp_date + ", resource_type_code="
				+ resource_type_code + ", priority=" + priority + "]";
	}
	
	
	
	
}
