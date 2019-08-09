package com.tydic.beijing.billing.rating.domain;
/**
 * 段落规则 rule_section_rule
 * @author sung
 *
 */

public class RuleSectionRule {
	
	private long pricing_section;
	private long  upper_pricing_section ;
	private long  pricing_sn ;           
	private long  priority ;             
	private int  cond_id ;              
	private long  lower ;                
	private long  upper ;                
	private int  ref_flag ;             
	private String  ref_resource_code ;    
	private int  node_type ;
	
	
	public long getPricing_section() {
		return pricing_section;
	}
	
	public void setPricing_section(long pricing_section) {
		this.pricing_section = pricing_section;
	}
	public long getUpper_pricing_section() {
		return upper_pricing_section;
	}
	public void setUpper_pricing_section(long upper_pricing_section) {
		this.upper_pricing_section = upper_pricing_section;
	}
	public long getPricing_sn() {
		return pricing_sn;
	}
	public void setPricing_sn(long pricing_sn) {
		this.pricing_sn = pricing_sn;
	}
	public long getPriority() {
		return priority;
	}
	public void setPriority(long priority) {
		this.priority = priority;
	}
	public int getCond_id() {
		return cond_id;
	}
	public void setCond_id(int cond_id) {
		this.cond_id = cond_id;
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
	public int getRef_flag() {
		return ref_flag;
	}
	public void setRef_flag(int ref_flag) {
		this.ref_flag = ref_flag;
	}
	public String getRef_resource_code() {
		return ref_resource_code;
	}
	public void setRef_resource_code(String ref_resource_code) {
		this.ref_resource_code = ref_resource_code;
	}

	public int getNode_type() {
		return node_type;
	}

	public void setNode_type(int node_type) {
		this.node_type = node_type;
	}


	
	
		
}
