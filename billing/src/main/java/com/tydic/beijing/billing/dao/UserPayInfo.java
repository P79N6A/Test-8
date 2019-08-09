package com.tydic.beijing.billing.dao;

import java.util.Date;

public class UserPayInfo {

	private String user_id;
	private String device_number;
	private String user_status;
	private Date create_date;
	private Date active_date;
	private String user_type;
	private String prepay_flag;
	private String local_net;
	private String develop_channel_id;
//	private String ofr_id;
	private String proto_flag;
	private String sub_user_status;
	private Date stop_date;
	
	private String pay_id;
	private String default_tag;
	private String eff_flag;
	
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
	public String getPay_id() {
		return pay_id;
	}
	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}
	public String getDefault_tag() {
		return default_tag;
	}
	public void setDefault_tag(String default_tag) {
		this.default_tag = default_tag;
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
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getPrepay_flag() {
		return prepay_flag;
	}
	public void setPrepay_flag(String prepay_flag) {
		this.prepay_flag = prepay_flag;
	}
	public String getLocal_net() {
		return local_net;
	}
	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}
	public String getDevelop_channle_id() {
		return develop_channel_id;
	}
	public void setDevelop_channle_id(String develop_channle_id) {
		this.develop_channel_id = develop_channle_id;
	}
//	public String getOfr_id() {
//		return ofr_id;
//	}
//	public void setOfr_id(String ofr_id) {
//		this.ofr_id = ofr_id;
//	}
	public String getProto_flag() {
		return proto_flag;
	}
	public void setProto_flag(String proto_flag) {
		this.proto_flag = proto_flag;
	}
	public String getSub_user_status() {
		return sub_user_status;
	}
	public void setSub_user_status(String sub_user_status) {
		this.sub_user_status = sub_user_status;
	}
	public Date getStop_date() {
		return stop_date;
	}
	public void setStop_date(Date stop_date) {
		this.stop_date = stop_date;
	}
	public String getEff_flag() {
		return eff_flag;
	}
	public void setEff_flag(String eff_flag) {
		this.eff_flag = eff_flag;
	}
	
}
