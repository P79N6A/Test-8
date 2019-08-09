package com.tydic.beijing.billing.account.type;

import com.tydic.beijing.billing.dao.BilActBalanceAddUp;

public class BalanceWriteOff {
	private long balance_id;
	private long writeoff_fee;
	// month end
	private int balance_type_id;

	public BalanceWriteOff(WriteOffDetail wod) {
		balance_id = wod.getBalance_id();
		writeoff_fee = wod.getWriteoff_fee();
		balance_type_id = wod.getBalance_type_id();
	}

	public BalanceWriteOff(BilActBalanceAddUp babau) {
		balance_id = babau.getBalance_id();
		writeoff_fee = babau.getDeduct_fee();
		balance_type_id = babau.getBalance_type_id();
	}

	public long getBalance_id() {
		return balance_id;
	}

	public void setBalance_id(long balance_id) {
		this.balance_id = balance_id;
	}

	public long getWriteoff_fee() {
		return writeoff_fee;
	}

	public void setWriteoff_fee(long writeoff_fee) {
		this.writeoff_fee = writeoff_fee;
	}

	public int getBalance_type_id() {
		return balance_type_id;
	}

	public void setBalance_type_id(int balance_type_id) {
		this.balance_type_id = balance_type_id;
	}

	public BalanceWriteOff add(WriteOffDetail wod) {
		this.setWriteoff_fee(writeoff_fee + wod.getWriteoff_fee());
		return this;
	}

	public BalanceWriteOff add(BilActBalanceAddUp babau) {
		this.setWriteoff_fee(writeoff_fee + babau.getDeduct_fee());
		return this;
	}

	public Boolean equals(WriteOffDetail wod) {
		return this.getBalance_id() == wod.getBalance_id();
	}
	
	public Boolean equals(BilActBalanceAddUp babau) {
		return this.getBalance_id() == babau.getBalance_id();
	}

	@Override
	public String toString() {
		return "BalanceWriteOff [balance_id=" + balance_id + ", writeoff_fee="
				+ writeoff_fee + ", unit_type_id=" + balance_type_id
				+ ", before_balance=" + "]";
	}
}
