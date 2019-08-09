package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class LogTradeAdjustHis implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String trade_id;
	private String user_id;
	private int partition_id;
	private String adjust_mode;
	private int balance_type_id;
	private int unit_type_id;
	private long adjust_fee;
	private long old_balance;
	private long new_balance;
	private String adjust_reason;
	private String adjust_time ;
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getPartition_id() {
		return partition_id;
	}
	public void setPartition_id(int partition_id) {
		this.partition_id = partition_id;
	}
	public String getAdjust_mode() {
		return adjust_mode;
	}
	public void setAdjust_mode(String adjust_mode) {
		this.adjust_mode = adjust_mode;
	}
	public int getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(int balance_type_id) {
		this.balance_type_id = balance_type_id;
	}
	public int getUnit_type_id() {
		return unit_type_id;
	}
	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}
	public long getAdjust_fee() {
		return adjust_fee;
	}
	public void setAdjust_fee(long adjust_fee) {
		this.adjust_fee = adjust_fee;
	}
	public long getOld_balance() {
		return old_balance;
	}
	public void setOld_balance(long old_balance) {
		this.old_balance = old_balance;
	}
	public long getNew_balance() {
		return new_balance;
	}
	public void setNew_balance(long new_balance) {
		this.new_balance = new_balance;
	}
	public String getAdjust_reason() {
		return adjust_reason;
	}
	public void setAdjust_reason(String adjust_reason) {
		this.adjust_reason = adjust_reason;
	}
	public String getAdjust_time() {
		return adjust_time;
	}
	public void setAdjust_time(String adjust_time) {
		this.adjust_time = adjust_time;
	}
	
	
	
}
