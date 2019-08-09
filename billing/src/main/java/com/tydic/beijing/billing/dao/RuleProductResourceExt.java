package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class RuleProductResourceExt implements Serializable {

	/**
	 * yuandao
	 */
	private static final long serialVersionUID = 1L;
	private String  ofr_c_id;
	private String  product_id;
	private String  group_id;
	private long  change_mode;
	private long  offset_ref_type;
	private long  offset_cycle;
	private long  offset_mode;
	private long  process_cycle;
	private long  unit_type_id;
	private long  resource_value;
	private long  balance_type_id;
	private long  cycle_type;
	private long  cycle_value;
	private String  eff_flag;
	public String getOfr_c_id() {
		return ofr_c_id;
	}
	public void setOfr_c_id(String ofr_c_id) {
		this.ofr_c_id = ofr_c_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public long getChange_mode() {
		return change_mode;
	}
	public void setChange_mode(long change_mode) {
		this.change_mode = change_mode;
	}
	public long getOffset_ref_type() {
		return offset_ref_type;
	}
	public void setOffset_ref_type(long offset_ref_type) {
		this.offset_ref_type = offset_ref_type;
	}
	public long getOffset_cycle() {
		return offset_cycle;
	}
	public void setOffset_cycle(long offset_cycle) {
		this.offset_cycle = offset_cycle;
	}
	public long getOffset_mode() {
		return offset_mode;
	}
	public void setOffset_mode(long offset_mode) {
		this.offset_mode = offset_mode;
	}
	public long getProcess_cycle() {
		return process_cycle;
	}
	public void setProcess_cycle(long process_cycle) {
		this.process_cycle = process_cycle;
	}
	public long getUnit_type_id() {
		return unit_type_id;
	}
	public void setUnit_type_id(long unit_type_id) {
		this.unit_type_id = unit_type_id;
	}
	public long getResource_value() {
		return resource_value;
	}
	public void setResource_value(long resource_value) {
		this.resource_value = resource_value;
	}
	public long getBalance_type_id() {
		return balance_type_id;
	}
	public void setBalance_type_id(long balance_type_id) {
		this.balance_type_id = balance_type_id;
	}
	public long getCycle_type() {
		return cycle_type;
	}
	public void setCycle_type(long cycle_type) {
		this.cycle_type = cycle_type;
	}
	public long getCycle_value() {
		return cycle_value;
	}
	public void setCycle_value(long cycle_value) {
		this.cycle_value = cycle_value;
	}
	public String getEff_flag() {
		return eff_flag;
	}
	public void setEff_flag(String eff_flag) {
		this.eff_flag = eff_flag;
	}
	@Override
	public String toString() {
		return "RuleProductResourceExt [ofr_c_id=" + ofr_c_id + ", product_id="
				+ product_id + ", group_id=" + group_id + ", change_mode="
				+ change_mode + ", offset_ref_type=" + offset_ref_type
				+ ", offset_cycle=" + offset_cycle + ", offset_mode="
				+ offset_mode + ", process_cycle=" + process_cycle
				+ ", unit_type_id=" + unit_type_id + ", resource_value="
				+ resource_value + ", balance_type_id=" + balance_type_id
				+ ", cycle_type=" + cycle_type + ", cycle_value=" + cycle_value
				+ ", eff_flag=" + eff_flag + "]";
	}
	
}
