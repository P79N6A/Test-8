package com.tydic.beijing.billing.account.type;

public class BalanceInfo {	
	private String pay_id;//账户ID
	private long balance_id;//账本代码
	private int balance_type_id;//账本类型
	private int unit_type_id;//资源类型
	private int acct_item_code;//费用代码 
	private long fee;//费用
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
	public String getPay_id() {
		return pay_id;
	}
	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}
	public int getUnit_type_id() {
		return unit_type_id;
	}
	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}
	public int getAcct_item_code() {
		return acct_item_code;
	}
	public void setAcct_item_code(int acct_item_code) {
		this.acct_item_code = acct_item_code;
	}
	public long getFee() {
		return fee;
	}
	public void setFee(long fee) {
		this.fee = fee;
	}
	@Override
	public String toString() {
		return "BalanceInfo [pay_id=" + pay_id + ", balance_id=" + balance_id
				+ ", balance_type_id=" + balance_type_id + ", unit_type_id="
				+ unit_type_id + ", code_acct_item=" + acct_item_code
				+ ", fee=" + fee + "]";
	}
}
