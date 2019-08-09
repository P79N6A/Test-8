package com.tydic.beijing.billing.dao;

import java.sql.Date;

public class LifeUserProto {
	private static final long serialVersionUID = 1L;
	private String user_proto_id;
	private String user_id;
	private String ofr_id;
	private String element_id;
	private Date eff_date;
	private Date exp_date;
	private String eff_flag;
	private Date create_date;
	private String activity_id;
	private String is_num_rule;
	public String getUser_proto_id() {
		return user_proto_id;
	}
	public void setUser_proto_id(String user_proto_id) {
		this.user_proto_id = user_proto_id;
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
	public String getElement_id() {
		return element_id;
	}
	public void setElement_id(String element_id) {
		this.element_id = element_id;
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
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getIs_num_rule() {
		return is_num_rule;
	}
	public void setIs_num_rule(String is_num_rule) {
		this.is_num_rule = is_num_rule;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "LifeUserProto [user_proto_id=" + user_proto_id + ", user_id="
				+ user_id + ", ofr_id=" + ofr_id + ", element_id=" + element_id
				+ ", eff_date=" + eff_date + ", exp_date=" + exp_date
				+ ", eff_flag=" + eff_flag + ", create_date=" + create_date
				+ ", activity_id=" + activity_id + ", is_num_rule="
				+ is_num_rule + "]";
	}
	
}
