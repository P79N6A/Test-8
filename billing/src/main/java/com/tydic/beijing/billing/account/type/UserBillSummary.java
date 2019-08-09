package com.tydic.beijing.billing.account.type;

import java.io.Serializable;

/**
 * 用户账单便利类
 * 
 * @author Tian
 *
 */
public final class UserBillSummary implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private int acct_month;
	private int acct_item_code;
	private int unit_type_id;
	private long fee;
	private long active_fee;
	private long raw_fee;
	private long discount_fee;
	private long adjust_fee;
	private String source;
	private long billing_id;
	private String bill_date; // YYYYMMDD

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public long getRaw_fee() {
		return raw_fee;
	}

	public void setRaw_fee(long raw_fee) {
		this.raw_fee = raw_fee;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public long getBilling_id() {
		return billing_id;
	}

	public void setBilling_id(long billing_id) {
		this.billing_id = billing_id;
	}

	public String getBill_date() {
		return bill_date;
	}

	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
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

	public long getActive_fee() {
		return active_fee;
	}

	public void setActive_fee(long active_fee) {
		this.active_fee = active_fee;
	}

	@Override
	public String toString() {
		return "UserId[" + user_id + "]AcctMonth[" + acct_month
				+ "]AcctItemCode[" + acct_item_code + "]UnitTypeId["
				+ unit_type_id + "]ActiveFee[" + active_fee + "]Fee[" + fee
				+ "]RawFee[" + raw_fee + "]AdjustFee[" + adjust_fee
				+ "]DiscountFee[" + discount_fee + "]Source[" + source
				+ "]BillingId[" + billing_id + "]BillDate[" + bill_date + "]";
	}
}
