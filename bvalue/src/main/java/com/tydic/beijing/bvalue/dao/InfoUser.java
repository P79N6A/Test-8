package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;
import java.util.Date;

public class InfoUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String jd_pin;
	private String create_date;
	private int user_level ;
	private String create_channel; //000：SKU创建     001：购物赠送创建     002：登录查询创建     003：关联JDPin创建'	
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getJd_pin() {
		return jd_pin;
	}
	public void setJd_pin(String jd_pin) {
		this.jd_pin = jd_pin;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public int getUser_level() {
		return user_level;
	}
	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}
	public String getCreate_channel() {
		return create_channel;
	}
	public void setCreate_channel(String create_channel) {
		this.create_channel = create_channel;
	}
	
 

}
