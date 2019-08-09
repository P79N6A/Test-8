package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.Date;

public class LifeUserProduct implements Serializable{

	private static final long serialVersionUID = 1L;
	private String user_product_id ;
	private String  user_id ;
	private String  ofr_id ;
	private String  product_id ;
	private Date  eff_date ;
	private Date  exp_date ;
	private String   eff_flag ;
	private Date  create_date;
	
	
	public String getUser_product_id() {
		return user_product_id;
	}
	public void setUser_product_id(String user_product_id) {
		this.user_product_id = user_product_id;
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
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
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
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	@Override
	public String toString() {
		return "LifeUserProduct [user_product_id=" + user_product_id
				+ ", user_id=" + user_id + ", ofr_id=" + ofr_id
				+ ", product_id=" + product_id + ", eff_date=" + eff_date
				+ ", exp_date=" + exp_date + ", eff_flag=" + eff_flag
				+ ", create_date=" + create_date + "]";
	}
	
}
