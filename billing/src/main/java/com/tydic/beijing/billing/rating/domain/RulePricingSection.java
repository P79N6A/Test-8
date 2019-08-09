package com.tydic.beijing.billing.rating.domain;

import java.io.Serializable;

/**
 * 段落 rule_pricing_section
 * @author sung
 *
 */

public class RulePricingSection implements Serializable{

	private static final long serialVersionUID = 1L;
	private long pricing_section;
	private String acct_item_id;
	private int acct_item_type;
	private String measure_domain;
	private int tail_mod;
	private int discount_id;
	private int tariff_id;
	private int tariff_type;//0	计量计费  1	时间计费
	private int section_type;  //0	免费段落  1	收费段落
	private String remarks;
	
	
	public long getPricing_section() {
		return pricing_section;
	}
	public void setPricing_section(long pricing_section) {
		this.pricing_section = pricing_section;
	}
	public String getAcct_item_id() {
		return acct_item_id;
	}
	public void setAcct_item_id(String acct_item_id) {
		this.acct_item_id = acct_item_id;
	}
	public int getAcct_item_type() {
		return acct_item_type;
	}
	public void setAcct_item_type(int acct_item_type) {
		this.acct_item_type = acct_item_type;
	}
	public String getMeasure_domain() {
		return measure_domain;
	}
	public void setMeasure_domain(String measure_domain) {
		this.measure_domain = measure_domain;
	}
	public int getTail_mod() {
		return tail_mod;
	}
	public void setTail_mod(int tail_mod) {
		this.tail_mod = tail_mod;
	}
	public int getDiscount_id() {
		return discount_id;
	}
	public void setDiscount_id(int discount_id) {
		this.discount_id = discount_id;
	}
	public int getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(int tariff_id) {
		this.tariff_id = tariff_id;
	}
	public int getTariff_type() {
		return tariff_type;
	}
	public void setTariff_type(int tariff_type) {
		this.tariff_type = tariff_type;
	}
	public int getSection_type() {
		return section_type;
	}
	public void setSection_type(int section_type) {
		this.section_type = section_type;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
