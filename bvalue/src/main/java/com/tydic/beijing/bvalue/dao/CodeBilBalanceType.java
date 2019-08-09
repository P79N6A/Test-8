package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class CodeBilBalanceType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String balance_type_id ;
	private String resource_type_code;
	private String balance_type_name ;
	private  int priority ;
	public String getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(String balance_type_id) {
		this.balance_type_id = balance_type_id;
	}
	public String getResource_type_code() {
		return resource_type_code;
	}
	public void setResource_type_code(String resource_type_code) {
		this.resource_type_code = resource_type_code;
	}
	public String getBalance_type_name() {
		return balance_type_name;
	}
	public void setBalance_type_name(String balance_type_name) {
		this.balance_type_name = balance_type_name;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	
	
}
