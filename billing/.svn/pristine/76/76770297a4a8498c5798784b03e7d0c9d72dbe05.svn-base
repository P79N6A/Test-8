package com.tydic.beijing.billing.rating.domain;

public class Discount {

	private long discountId;
	private int discountSn;
	private int discount;
	private long lower;
	private long upper;
	private int ratableFlag=0;
	private String resourceCode;
	
	
	
	public Discount(){}
	
	public Discount(RulePricingSectionDisct  disct){
		this.discountId=disct.getDiscount_id();
		this.discountSn=disct.getDiscount_sn();
		this.discount=disct.getDiscount();
		this.lower=disct.getLower();
		this.upper=disct.getUpper();
		this.resourceCode=disct.getResource_code();
		
	}
	public long getDiscountId() {
		return discountId;
	}
	public void setDiscountId(long discountId) {
		this.discountId = discountId;
	}
	public int getDiscountSn() {
		return discountSn;
	}
	public void setDiscountSn(int discountSn) {
		this.discountSn = discountSn;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public long getLower() {
		return lower;
	}
	public void setLower(long lower) {
		this.lower = lower;
	}
	public long getUpper() {
		return upper;
	}
	public void setUpper(long upper) {
		this.upper = upper;
	}
	public int getRatableFlag() {
		return ratableFlag;
	}
	public void setRatableFlag(int ratableFlag) {
		this.ratableFlag = ratableFlag;
	}
	public String getResourceCode() {
		return resourceCode;
	}
	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}
	
	
}
