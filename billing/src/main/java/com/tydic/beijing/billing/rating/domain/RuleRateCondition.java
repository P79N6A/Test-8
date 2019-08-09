package com.tydic.beijing.billing.rating.domain;

/**
 * 段落条件 rule_rate_condition
 * @author sung
 *
 */
public class RuleRateCondition {

	private long  cond_id;       
	private long  group_id;      
	private long  group_sn;      
	private String com_type;      
	private String  item_code;  
	private int  com_operators; 
	private String  item_value;
	
	public long getCond_id() {
		return cond_id;
	}
	public void setCond_id(long cond_id) {
		this.cond_id = cond_id;
	}
	public long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}
	public long getGroup_sn() {
		return group_sn;
	}
	public void setGroup_sn(long group_sn) {
		this.group_sn = group_sn;
	}
	public String getCom_type() {
		return com_type;
	}
	public void setCom_type(String com_type) {
		this.com_type = com_type;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}


	public int getCom_operators() {
		return com_operators;
	}
	public void setCom_operators(int com_operators) {
		this.com_operators = com_operators;
	}
	public String getItem_value() {
		return item_value;
	}
	public void setItem_value(String item_value) {
		this.item_value = item_value;
	}
	
	
	
}
