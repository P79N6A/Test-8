package com.tydic.beijing.billing.dao;

public class LifeImsiIOPLocal {
	
	private String imsi;
	private String user_id;
	private String start_time;
	private String end_time;
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	public LifeImsiIOPLocal(String imsi, String user_id, String start_time,
			String end_time) {
		super();
		this.imsi = imsi;
		this.user_id = user_id;
		this.start_time = start_time;
		this.end_time = end_time;
	}
	
	public LifeImsiIOPLocal(LifeImsiIOP lii) {
	
		this.imsi = lii.getImsi();
		this.user_id = lii.getUser_id();
		this.start_time = lii.getStart_time();
		this.end_time = lii.getEnd_time();
	}

	
}



 