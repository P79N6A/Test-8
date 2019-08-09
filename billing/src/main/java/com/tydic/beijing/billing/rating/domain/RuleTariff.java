package com.tydic.beijing.billing.rating.domain;
/**
 * 费率表 rule_tariff
 * @author sung
 *
 */
public class RuleTariff {

	private long tariff_id;        
	private int   tariff_sn;       
	private long   lower;          
	private long   upper ;         
	private int  rate_unit ;       
	private long  fee_rate ; // zhanghb modify 20150820   int        
	private int  tail_mod ;        
	private int  disct_value;      
	private int  tariff_unit_id ;  
	private int  disct_value_base; 
	
//	NORMAL_TARIFF 0
//    HOLIDAY_TARIFF 1
//	NOTHOLIDAY_TARIFF 2
//	INVALID_TARIFF 3
	private int  date_type;        
	private int  holiday_id ;      
	private String  remarks ;
	
	
	public long getTariff_id() {
		return tariff_id;
	}
	
	public void setTariff_id(long tariff_id) {
		this.tariff_id = tariff_id;
	}
	public int getTariff_sn() {
		return tariff_sn;
	}
	public void setTariff_sn(int tariff_sn) {
		this.tariff_sn = tariff_sn;
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
	public int getRate_unit() {
		return rate_unit;
	}
	public void setRate_unit(int rate_unit) {
		this.rate_unit = rate_unit;
	}
	public long getFee_rate() {
		return fee_rate;
	}
	public void setFee_rate(long fee_rate) {
		this.fee_rate = fee_rate;
	}
	public int getTail_mod() {
		return tail_mod;
	}
	public void setTail_mod(int tail_mod) {
		this.tail_mod = tail_mod;
	}
	public int getDisct_value() {
		return disct_value;
	}
	public void setDisct_value(int disct_value) {
		this.disct_value = disct_value;
	}
	public int getTariff_unit_id() {
		return tariff_unit_id;
	}
	public void setTariff_unit_id(int tariff_unit_id) {
		this.tariff_unit_id = tariff_unit_id;
	}
	public int getDisct_value_base() {
		return disct_value_base;
	}
	public void setDisct_value_base(int disct_value_base) {
		this.disct_value_base = disct_value_base;
	}
	public int getDate_type() {
		return date_type;
	}
	public void setDate_type(int date_type) {
		this.date_type = date_type;
	}
	public int getHoliday_id() {
		return holiday_id;
	}
	public void setHoliday_id(int holiday_id) {
		this.holiday_id = holiday_id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
}
