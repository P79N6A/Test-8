package com.tydic.beijing.billing.account.type;

import com.tydic.beijing.billing.dao.BilActBalanceAddUp;

public class ItemWriteOff {
	private String user_Id;
	private String pay_Id;
	private int acct_month;
	private int unit_type_id;
	private int acct_item_code;
	private long org_fee;
	private long adjust_fee;
	private long discount_fee;
	private long fee;
	private long writeoff_fee;
	private long invoice_fee;

	public ItemWriteOff(WriteOffDetail wod) {
		user_Id = wod.getUser_Id();
		pay_Id = wod.getPay_Id();
		acct_month = wod.getAcct_month();
		unit_type_id = wod.getUnit_type_id();
		acct_item_code = wod.getAcct_item_code();
		org_fee = wod.getRaw_fee();
		adjust_fee = wod.getAdjust_fee();
		discount_fee = wod.getDiscount_fee();
		fee = wod.getFee();
		writeoff_fee = wod.getWriteoff_fee();
		invoice_fee = wod.getInvoice_fee();
	}

	public ItemWriteOff(BilActBalanceAddUp babau) {
		user_Id = babau.getUser_id();
		pay_Id = babau.getPay_id();
		acct_month = babau.getAcct_month();
		unit_type_id = babau.getUnit_type_id();
		acct_item_code = babau.getAcct_item_code();
		org_fee = babau.getDeduct_fee();
		adjust_fee = 0;
		discount_fee = 0;
		fee = babau.getDeduct_fee();
		writeoff_fee = babau.getDeduct_fee();
		invoice_fee = 0;
	}
	
	public String getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}

	public String getPay_Id() {
		return pay_Id;
	}

	public void setPay_Id(String pay_Id) {
		this.pay_Id = pay_Id;
	}

	public int getAcct_month() {
		return acct_month;
	}

	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
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

	public long getOrg_fee() {
		return org_fee;
	}

	public void setOrg_fee(long org_fee) {
		this.org_fee = org_fee;
	}

	public long getAdjust_fee() {
		return adjust_fee;
	}

	public void setAdjust_fee(long adjust_fee) {
		this.adjust_fee = adjust_fee;
	}

	public long getDiscount_fee() {
		return discount_fee;
	}

	public void setDiscount_fee(long discount_fee) {
		this.discount_fee = discount_fee;
	}

	public long getFee() {
		return fee;
	}

	public void setFee(long fee) {
		this.fee = fee;
	}

	public long getWriteoff_fee() {
		return writeoff_fee;
	}

	public void setWriteoff_fee(long writeoff_fee) {
		this.writeoff_fee = writeoff_fee;
	}

	public long getInvoice_fee() {
		return invoice_fee;
	}

	public void setInvoice_fee(long invoice_fee) {
		this.invoice_fee = invoice_fee;
	}

	public ItemWriteOff add(WriteOffDetail wod) {
		this.setWriteoff_fee(writeoff_fee + wod.getWriteoff_fee());
		this.setInvoice_fee(invoice_fee + wod.getInvoice_fee());
		return this;
	}
	
	public ItemWriteOff add(BilActBalanceAddUp babau) {
		this.setWriteoff_fee(writeoff_fee + babau.getDeduct_fee());
		this.setInvoice_fee(0);
		return this;
	}

	public Boolean equals(WriteOffDetail wod) {
		return this.getUser_Id().equals(wod.getUser_Id())
				&& this.getPay_Id().equals(wod.getPay_Id())
				&& this.getAcct_month() == wod.getAcct_month()
				&& this.getAcct_item_code() == wod.getAcct_item_code();
	}
	
	public Boolean equals(BilActBalanceAddUp babau) {
		return this.getUser_Id().equals(babau.getUser_id())
				&& this.getPay_Id().equals(babau.getPay_id())
				&& this.getAcct_month() == babau.getAcct_month()
				&& this.getAcct_item_code() == babau.getAcct_item_code();
	}

	@Override
	public String toString() {
		return "ItemWriteOff [user_Id=" + user_Id + ", pay_Id=" + pay_Id
				+ ", acct_month=" + acct_month + ", unit_type_id="
				+ unit_type_id + ", acct_item_code=" + acct_item_code
				+ ", fee=" + fee + ", writeoff_fee=" + writeoff_fee
				+ ", invoice_fee=" + invoice_fee + "]";
	}
}
