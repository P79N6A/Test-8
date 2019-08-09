package com.tydic.beijing.billing.account.type;

import java.io.Serializable;

/**
 * 销账结果便利类
 * 
 * @author Tian
 *
 */
public final class WriteOffDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user_Id;
	private String pay_Id;
	private int acct_month;
	private int unit_type_id;
	private int acct_item_code;
	private long fee;
	private long raw_fee;
	private long discount_fee;
	private long adjust_fee;
	private long writeoff_fee;
	private long before_fee;
	private long after_fee;

	private long balance_id;
	private int balance_type_id;
	private long balance;
	private long before_balance;
	private long after_balance;

	private long invoice_fee;

	private String billSource;
	private long billingId;

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

	public long getWriteoff_fee() {
		return writeoff_fee;
	}

	public void setWriteoff_fee(long writeoff_fee) {
		this.writeoff_fee = writeoff_fee;
	}

	public long getBefore_fee() {
		return before_fee;
	}

	public void setBefore_fee(long before_fee) {
		this.before_fee = before_fee;
	}

	public long getAfter_fee() {
		return after_fee;
	}

	public void setAfter_fee(long after_fee) {
		this.after_fee = after_fee;
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

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public long getBefore_balance() {
		return before_balance;
	}

	public void setBefore_balance(long before_balance) {
		this.before_balance = before_balance;
	}

	public long getAfter_balance() {
		return after_balance;
	}

	public void setAfter_balance(long after_balance) {
		this.after_balance = after_balance;
	}

	public int getUnit_type_id() {
		return unit_type_id;
	}

	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}

	public String getBillSource() {
		return billSource;
	}

	public void setBillSource(String billSource) {
		this.billSource = billSource;
	}

	public long getBillingId() {
		return billingId;
	}

	public void setBillingId(long billingId) {
		this.billingId = billingId;
	}

	public long getRaw_fee() {
		return raw_fee;
	}

	public void setRaw_fee(long raw_fee) {
		this.raw_fee = raw_fee;
	}

	public long getDiscount_fee() {
		return discount_fee;
	}

	public void setDiscount_fee(long discount_fee) {
		this.discount_fee = discount_fee;
	}

	public long getAdjust_fee() {
		return adjust_fee;
	}

	public void setAdjust_fee(long adjust_fee) {
		this.adjust_fee = adjust_fee;
	}

	public long getInvoice_fee() {
		return invoice_fee;
	}

	public void setInvoice_fee(long invoice_fee) {
		this.invoice_fee = invoice_fee;
	}

	@Override
	public String toString() {
		return "UserId[" + user_Id + "]PayId[" + pay_Id + "]AcctMonth["
				+ acct_month + "]AcctItemCode[" + acct_item_code + "]Fee["
				+ fee + "]RawFee[" + raw_fee + "]AdjustFee[" + adjust_fee
				+ "]DiscountFee[" + discount_fee + "]WriteOffFee["
				+ writeoff_fee + "]BeforeFee[" + before_fee + "]AfterFee["
				+ after_fee + "]BalanceId[" + balance_id + "]BalanceTypeId["
				+ balance_type_id + "]Balance[" + balance + "]BeforeBalance["
				+ before_balance + "]AfterBalance[" + after_balance
				+ "]InvoiceFee[" + invoice_fee + "]BillSource[" + billSource
				+ "]BillingId[" + billingId + "]";
	}
}
