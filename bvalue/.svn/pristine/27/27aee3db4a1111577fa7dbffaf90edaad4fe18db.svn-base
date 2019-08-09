package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class InfoPayBalanceDao implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String balance_id;
	private String user_id;
	private int balance_type_id;
	private long balance;
	private String eff_date;
	private String exp_date;

	
	public InfoPayBalanceDao(){}
	
	public InfoPayBalanceDao(InfoPayBalance balance){
		this.balance_id=balance.getBalance_id();
		this.user_id=balance.getUser_id();
		this.balance_type_id=balance.getBalance_type_id();
		this.balance=balance.getBalance();
		this.eff_date=balance.getEff_date();
		this.exp_date=balance.getExp_date();
	}
	
	
	public void init(InfoPayBalance balance){
		this.balance_id=balance.getBalance_id();
		this.user_id=balance.getUser_id();
		this.balance_type_id=balance.getBalance_type_id();
		this.balance=balance.getBalance();
		this.eff_date=balance.getEff_date();
		this.exp_date=balance.getExp_date();
	}
	
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

	public int getBalance_type_id() {
		return balance_type_id;
	}

	public void setBalance_type_id(int balance_type_id) {
		this.balance_type_id = balance_type_id;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
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
	
	
	public void addBValue(long bvalue){
		this.balance = this.balance + bvalue;
	}

	@Override
	public String toString() {
		return "InfoPayBalance [balance_id=" + balance_id + ", user_id=" + user_id
				+ ", balance_type_id=" + balance_type_id + ", balance=" + balance + ", eff_date="
				+ eff_date + ", exp_date=" + exp_date + "]";
	}
	
	
	
}
