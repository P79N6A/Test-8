package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class BalanceAccessLog implements Serializable {
	private static final long serialVersionUID = 1L;

	private String trade_Id;
	private String trade_Type_Code;
	private String user_Id;
	private int partition_Id;
	private String balance_Id;
	private int balance_Type_Id;
	private String access_Tag; // 0存款 1取款
	private long money;
	private long old_Balance;
	private long new_Balance;
	private String operate_Time;

	public String getTrade_Id() {
		return trade_Id;
	}

	public void setTrade_Id(String trade_Id) {
		this.trade_Id = trade_Id;
	}

	public String getTrade_Type_Code() {
		return trade_Type_Code;
	}

	public void setTrade_Type_Code(String trade_Type_Code) {
		this.trade_Type_Code = trade_Type_Code;
	}

	public String getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}

	public int getPartition_Id() {
		return partition_Id;
	}

	public void setPartition_Id(int partition_Id) {
		this.partition_Id = partition_Id;
	}

	public String getBalance_Id() {
		return balance_Id;
	}

	public void setBalance_Id(String balance_Id) {
		this.balance_Id = balance_Id;
	}

	public int getBalance_Type_Id() {
		return balance_Type_Id;
	}

	public void setBalance_Type_Id(int balance_Type_Id) {
		this.balance_Type_Id = balance_Type_Id;
	}

	public String getAccess_Tag() {
		return access_Tag;
	}

	public void setAccess_Tag(String access_Tag) {
		this.access_Tag = access_Tag;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public long getOld_Balance() {
		return old_Balance;
	}

	public void setOld_Balance(long old_Balance) {
		this.old_Balance = old_Balance;
	}

	public long getNew_Balance() {
		return new_Balance;
	}

	public void setNew_Balance(long new_Balance) {
		this.new_Balance = new_Balance;
	}

	public String getOperate_Time() {
		return operate_Time;
	}

	public void setOperate_Time(String operate_Time) {
		this.operate_Time = operate_Time;
	}

	@Override
	public String toString() {
		return "BalanceAccessLog [trade_Id=" + trade_Id + ", trade_Type_Code=" + trade_Type_Code
				+ ", user_Id=" + user_Id + ", partition_Id=" + partition_Id + ", balance_Id="
				+ balance_Id + ", balance_Type_Id=" + balance_Type_Id + ", access_Tag="
				+ access_Tag + ", money=" + money + ", old_Balance=" + old_Balance
				+ ", new_Balance=" + new_Balance + ", operate_Time=" + operate_Time + "]";
	}

}
