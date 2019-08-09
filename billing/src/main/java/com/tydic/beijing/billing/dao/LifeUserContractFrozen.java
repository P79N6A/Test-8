package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class LifeUserContractFrozen implements Serializable {
	private static final long serialVersionUID = 1L;

	private String frozen_id;
	private String contract_inst_id;
	private long balance_type_id;
	private long balance;
	private String User_id;
	public String getFrozen_id() {
		return frozen_id;
	}
	public void setFrozen_id(String forzen_id) {
		this.frozen_id = forzen_id;
	}
	public String getContract_inst_id() {
		return contract_inst_id;
	}
	public void setContract_inst_id(String contract_inst_id) {
		this.contract_inst_id = contract_inst_id;
	}
	public long getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(long balance_type_id) {
		this.balance_type_id = balance_type_id;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public String getUser_id() {
		return User_id;
	}
	public void setUser_id(String user_id) {
		User_id = user_id;
	}
	@Override
	public String toString() {
		return "LifeUserContractForzen [forzen_id=" + frozen_id
				+ ", contract_inst_id=" + contract_inst_id
				+ ", balance_type_id=" + balance_type_id + ", balance="
				+ balance + ", User_id=" + User_id + "]";
	}
	
}
