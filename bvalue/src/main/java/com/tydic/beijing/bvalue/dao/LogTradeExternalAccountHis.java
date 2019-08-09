package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class LogTradeExternalAccountHis implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String trade_id;
	private String trade_type_code;
	private String user_id;
	private long partition_id;
	private String external_account_id;
	private String external_system_code;
	private String operation_type;
	private String external_account_code;
	private String eff_date;
	private String exp_date;
	
	public LogTradeExternalAccountHis(){}
	
	public LogTradeExternalAccountHis(LogTradeRelChg his ,InfoUserExternalAccount external){
		this.trade_id=his.getTrade_id();
		this.trade_type_code=his.getTrade_type_code();
		this.user_id=his.getUser_id();
		this.partition_id=his.getPartition_id();
		this.external_account_id=external.getExternal_account_id();
		this.external_system_code=external.getExternal_system_code();
		this.external_account_code=external.getExternal_account_code();
		this.eff_date=external.getEff_date();
		this.exp_date=external.getExp_date();
		
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
	public String getExternal_account_code() {
		return external_account_code;
	}
	public void setExternal_account_code(String external_account_code) {
		this.external_account_code = external_account_code;
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
