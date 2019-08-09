package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class LifeResourceList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resource_list_id;
	private String user_id;
	private long order_number ;
	private String resource_type_code;
	private String resource_value;
	public String getResource_list_id() {
		return resource_list_id;
	}
	public void setResource_list_id(String resource_list_id) {
		this.resource_list_id = resource_list_id;
	}
	
	public String getResource_type_code() {
		return resource_type_code;
	}
	public void setResource_type_code(String resource_type_code) {
		this.resource_type_code = resource_type_code;
	}
	public String getResource_value() {
		return resource_value;
	}
	public void setResource_value(String resource_value) {
		this.resource_value = resource_value;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public long getOrder_number() {
		return order_number;
	}
	public void setOrder_number(long order_number) {
		this.order_number = order_number;
	}
	
	
}
