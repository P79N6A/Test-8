package com.tydic.beijing.billing.dao;

public class LifeProductIOPLocal {
	private String user_id;
	private String trans_idc;
	private String product_id;
	private String start_time;
	private String end_time;
	
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTrans_idc() {
		return trans_idc;
	}
	public void setTrans_idc(String trans_idc) {
		this.trans_idc = trans_idc;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
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
	
	public LifeProductIOPLocal( String user_id,String trans_idc,String product_id, String start_time,
			String end_time) {
		super();
		this.user_id = user_id;
		this.trans_idc = trans_idc;
		this.product_id = product_id;
		this.start_time = start_time;
		this.end_time = end_time;
	}
	
	public LifeProductIOPLocal(LifeProductIOP lpi) {
	
		this.user_id = lpi.getUser_id();
		this.trans_idc = lpi.getTrans_idc();
		this.product_id = lpi.getProduct_id();
		this.start_time = lpi.getStart_time();
		this.end_time = lpi.getEnd_time();
	}


}
