package com.tydic.beijing.billing.credit.dao;

import java.io.Serializable;

public class InfoAllList implements Serializable {
	/**
	 * 黑红名单客户信息表biansen
	 */
	private static final long serialVersionUID = 1L;
	private String alllist_id;
	private String customer_id;
	private String customer_name;
	private String id_type;
	private String id_number;
	private String alllist_type;
	private String cust_type;
	private String alllist_grade;
	private String create_date;
	private String eff_date;
	private String exp_date;
	private String remark;

	public String getAlllist_id() {
		return alllist_id;
	}

	public void setAlllist_id(String alllist_id) {
		this.alllist_id = alllist_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	
	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}
	
	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}
	
	public String getAlllist_type() {
		return alllist_type;
	}

	public void setAlllist_type(String alllist_type) {
		this.alllist_type = alllist_type;
	}
	
	public String getCust_type() {
		return cust_type;
	}

	public void setCust_type(String cust_type) {
		this.cust_type = cust_type;
	}
	
	public String getAlllist_grade() {
		return alllist_grade;
	}

	public void setAlllist_grade(String alllist_grade) {
		this.alllist_grade = alllist_grade;
	}

	public String getCreate_date() {
		return create_date;
	}
	
	public void setCreate_date(String create_date){
		this.create_date = create_date;
	}
	
	public String getEff_date() {
		return eff_date;
	}
	
	public void setEff_date(String eff_date){
		this.eff_date = eff_date;
	}
	
	public String getExp_date() {
		return exp_date;
	}
	
	public void setExp_date(String exp_date){
		this.exp_date = exp_date;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
