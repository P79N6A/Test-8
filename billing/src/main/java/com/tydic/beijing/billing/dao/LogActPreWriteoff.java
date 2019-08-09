package com.tydic.beijing.billing.dao;

import java.io.Serializable;

import com.tydic.beijing.billing.account.core.Dao2File;

public class LogActPreWriteoff extends Dao2File implements Serializable {
	/**
	 * log_act_pre_writeoff_xxx
	 * 模拟销账日志表
	 */
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String pay_id;
	private int acct_month;
	private String partition_num;
	private int acct_item_code;
	private int unit_type_id;
	private long fee;
	private long balance_id;
	private int balance_type_id;
	private long writeoff_fee;
	private long old_fee;
	private long new_fee;
	private long old_real_balance;
	private long new_real_balance;
	private String update_time;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPay_id() {
		return pay_id;
	}
	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}
	public int getAcct_month() {
		return acct_month;
	}
	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}
	public String getPartition_num() {
		return partition_num;
	}
	public void setPartition_num(String partition_num) {
		this.partition_num = partition_num;
	}
	public int getAcct_item_code() {
		return acct_item_code;
	}
	public void setAcct_item_code(int acct_item_code) {
		this.acct_item_code = acct_item_code;
	}
	public int getUnit_type_id() {
		return unit_type_id;
	}
	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}
	public long getFee() {
		return fee;
	}
	public void setFee(long fee) {
		this.fee = fee;
	}
	public long getBalance_id() {
		return balance_id;
	}
	public void setBalance_id(long balance_id) {
		this.balance_id = balance_id;
	}
	public int getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(int balance_type_id) {
		this.balance_type_id = balance_type_id;
	}
	public long getWriteoff_fee() {
		return writeoff_fee;
	}
	public void setWriteoff_fee(long writeoff_fee) {
		this.writeoff_fee = writeoff_fee;
	}
	public long getOld_fee() {
		return old_fee;
	}
	public void setOld_fee(long old_fee) {
		this.old_fee = old_fee;
	}
	public long getNew_fee() {
		return new_fee;
	}
	public void setNew_fee(long new_fee) {
		this.new_fee = new_fee;
	}
	public long getOld_real_balance() {
		return old_real_balance;
	}
	public void setOld_real_balance(long old_real_balance) {
		this.old_real_balance = old_real_balance;
	}
	public long getNew_real_balance() {
		return new_real_balance;
	}
	public void setNew_real_balance(long new_real_balance) {
		this.new_real_balance = new_real_balance;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String dao2Line() {
		return "" + user_id + "|" + pay_id + "|" + acct_month + "|"
				+ partition_num + "|" + acct_item_code + "|" + unit_type_id
				+ "|" + fee + "|" + balance_id + "|" + balance_type_id + "|"
				+ writeoff_fee + "|" + old_fee + "|" + new_fee + "|"
				+ old_real_balance + "|" + new_real_balance + "|" + update_time + "\n";
	}
	@Override
	public String toString() {
		return "LogActPreWriteoff [user_id=" + user_id + ", pay_id=" + pay_id
				+ ", acct_month=" + acct_month + ", partition_num="
				+ partition_num + ", acct_item_code=" + acct_item_code
				+ ", unit_type_id=" + unit_type_id + ", fee=" + fee
				+ ", balance_id=" + balance_id + ", balance_type_id="
				+ balance_type_id + ", writeoff_fee=" + writeoff_fee
				+ ", old_fee=" + old_fee + ", new_fee=" + new_fee
				+ ", old_real_balance=" + old_real_balance
				+ ", new_real_balance=" + new_real_balance + ", update_time="
				+ update_time + "]";
	}
	
}
