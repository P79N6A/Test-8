package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class LifeProductResourceRel implements Serializable {


	private static final long serialVersionUID = 1L;
	private  String  user_id;
	private  String  user_product_id;
	private  String  product_id;
	private  int  acct_month;
	private  long  balance_id;
	private  int  balance_type_id;
	private  String product_flag;
	private  String  eff_date;
	private  String  exp_date;
	private  String  create_date;
	private  String  serial_num;
	private String ofr_c_id;
	public String getOfr_c_id() {
		return ofr_c_id;
	}
	public void setOfr_c_id(String ofr_c_id) {
		this.ofr_c_id = ofr_c_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_product_id() {
		return user_product_id;
	}
	public void setUser_product_id(String user_product_id) {
		this.user_product_id = user_product_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public int getAcct_month() {
		return acct_month;
	}
	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}
	public long getBalance_id() {
		return balance_id;
	}
	public void setBalance_id(long balance_id) {
		this.balance_id = balance_id;
	}
	public int getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(int balance_type_id) {
		this.balance_type_id = balance_type_id;
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
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getSerial_num() {
		return serial_num;
	}
	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
	}

	public String getProduct_flag() {
		return product_flag;
	}
	public void setProduct_flag(String product_flag) {
		this.product_flag = product_flag;
	}
	public String toString(){
		return "LifeProductResourceRel [user_id=" + user_id + ",user_product_id=" + user_product_id 
				+ ",product_id=" + product_id + ",acct_month=" + acct_month + ",balance_id=" + balance_id
				+ ",balance_type_id=" + balance_type_id + ",eff_date=" + eff_date + ",exp_date=" + exp_date
				+ ",create_date=" + create_date + ",serial_num=" + serial_num + ",product_flag=" + product_flag +",OFR_C_ID="+ofr_c_id+ "]";
	}
	
	
}
