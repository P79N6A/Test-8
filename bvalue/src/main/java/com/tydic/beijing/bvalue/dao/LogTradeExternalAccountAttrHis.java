package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class LogTradeExternalAccountAttrHis implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  trade_id;
	private String  trade_type_code;
	private String  user_id;
	private long  partition_id;
	private String  external_account_id;
	private String  external_system_code;
	private String  operation_type;
	private String  attr_code;
	private String  attr_value;
	private String  eff_date;
	private String exp_date ;
	
	public LogTradeExternalAccountAttrHis(){}
	
	public LogTradeExternalAccountAttrHis(InfoUserExternalAccountAttr attr , LogTradeRelChg log){
		this.trade_id=log.getTrade_id();
		this.trade_type_code=log.getTrade_type_code();
		this.user_id=log.getUser_id();
		this.partition_id=log.getPartition_id();
		this.external_account_id=attr.getExternal_account_id();
		this.external_system_code=attr.getExternal_system_code();
		this.attr_code=attr.getAttr_code();
		this.attr_value=attr.getAttr_value();
		this.eff_date=attr.getEff_date();
		this.exp_date=attr.getExp_date();
		
	}
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	public String getTrade_type_code() {
		return trade_type_code;
	}
	public void setTrade_type_code(String trade_type_code) {
		this.trade_type_code = trade_type_code;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public long getPartition_id() {
		return partition_id;
	}
	public void setPartition_id(long partition_id) {
		this.partition_id = partition_id;
	}
	public String getExternal_account_id() {
		return external_account_id;
	}
	public void setExternal_account_id(String external_account_id) {
		this.external_account_id = external_account_id;
	}
	public String getExternal_system_code() {
		return external_system_code;
	}
	public void setExternal_system_code(String external_system_code) {
		this.external_system_code = external_system_code;
	}
	public String getOperation_type() {
		return operation_type;
	}
	public void setOperation_type(String operation_type) {
		this.operation_type = operation_type;
	}
	public String getAttr_code() {
		return attr_code;
	}
	public void setAttr_code(String attr_code) {
		this.attr_code = attr_code;
	}
	public String getAttr_value() {
		return attr_value;
	}
	public void setAttr_value(String attr_value) {
		this.attr_value = attr_value;
	}
	public String getEff_date() {
		return eff_date;
	}
	public void setEff_date(String eff_date) {
		this.eff_date = eff_date;
	}
	public String getExp_date() {
		return exp_date;
	}
	public void setExp_date(String exp_date) {
		this.exp_date = exp_date;
	}
	
	
	
	
}
