package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.sql.Date;
public class RuleOfrSplit implements Serializable {

	private static final long serialVersionUID = 1L;
	private String product_id;

	private String ofr_a_id;

	private String ofr_b_id;

	private String ofr_c_id;

	private Date create_date;

	private Date update_date;

	private String remark;
	
	
	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getOfr_a_id() {
		return ofr_a_id;
	}

	public void setOfr_a_id(String ofr_a_id) {
		this.ofr_a_id = ofr_a_id;
	}

	public String getOfr_b_id() {
		return ofr_b_id;
	}

	public void setOfr_b_id(String ofr_b_id) {
		this.ofr_b_id = ofr_b_id;
	}

	public String getOfr_c_id() {
		return ofr_c_id;
	}

	public void setOfr_c_id(String ofr_c_id) {
		this.ofr_c_id = ofr_c_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String toString() {
		return "RuleOfrSplit [ product_id=" + this.product_id + ",ofr_a_id="
				+ this.ofr_a_id + ",ofr_b_id=" + this.ofr_b_id + ",ofr_c_id="
				+ this.ofr_c_id + ",create_date=" + this.create_date
				+ ",update_date=" + this.update_date + ",remark=" + this.remark
				+ " ]";
	}

}
