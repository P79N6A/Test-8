package com.tydic.beijing.billing.dao;

import java.util.Date;


public class BalanceAdjustLog {

	private String session_id      ;
	private String jdpin          ;
	private String msisdn         ;
	private String channel_no      ;
	private String request_time    ; //Date
	private String resource_type   ;
	private int resource_number ;
	private Date eff_date        ;
	private Date exp_date        ;
	private Date change_time;
	
	private String real_deduct_info;
	
	private String user_id ;
	private String staff_id;
	
	//20141222 jd需求，
	private String activity_type;
	private String reason;
	
	//private String file_name;
	
	
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getJdpin() {
		return jdpin;
	}
	public void setJdpin(String jdpin) {
		this.jdpin = jdpin;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getChannel_no() {
		return channel_no;
	}
	public void setChannel_no(String channel_no) {
		this.channel_no = channel_no;
	}
//	public Date getRequest_time() {
//		return request_time;
//	}
//	public void setRequest_time(Date request_time) {
//		this.request_time = request_time;
//	}
	public String getResource_type() {
		return resource_type;
	}
	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}
	public int getResource_number() {
		return resource_number;
	}
	public void setResource_number(int resource_number) {
		this.resource_number = resource_number;
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
	public Date getChange_time() {
		return change_time;
	}
	public void setChange_time(Date change_time) {
		this.change_time = change_time;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getRequest_time() {
		return request_time;
	}
	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}
	public String getReal_deduct_info() {
		return real_deduct_info;
	}
	public void setReal_deduct_info(String real_deduct_info) {
		this.real_deduct_info = real_deduct_info;
	}
	public String getActivity_type() {
		return activity_type;
	}
	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
 
	
	
	
}
