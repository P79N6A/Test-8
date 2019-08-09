package com.tydic.beijing.billing.rating.domain;

/**
 * 累计量定义表 code_ratable_resource
 * @author sung
 *
 */
public class CodeRatableResource {

	private String ratable_resource_code ;
	private String   ratable_resource_name ;
	private String  date_type;
	private String  ref_type;
	private int   ref_offset ;
	private int   ratable_resource_type ;
	private int   life_type ;
	private int   start_value ;
	private int   end_value ;
	private String   owner_type ;
	private String  remarks ;
	public String getRatable_resource_code() {
		return ratable_resource_code;
	}
	public void setRatable_resource_code(String ratable_resource_code) {
		this.ratable_resource_code = ratable_resource_code;
	}
	public String getRatable_resource_name() {
		return ratable_resource_name;
	}
	public void setRatable_resource_name(String ratable_resource_name) {
		this.ratable_resource_name = ratable_resource_name;
	}
	public String getDate_type() {
		return date_type;
	}
	public void setDate_type(String date_type) {
		this.date_type = date_type;
	}
	public String getRef_type() {
		return ref_type;
	}
	public void setRef_type(String ref_type) {
		this.ref_type = ref_type;
	}
	public int getRef_offset() {
		return ref_offset;
	}
	public void setRef_offset(int ref_offset) {
		this.ref_offset = ref_offset;
	}
	public int getRatable_resource_type() {
		return ratable_resource_type;
	}
	public void setRatable_resource_type(int ratable_resource_type) {
		this.ratable_resource_type = ratable_resource_type;
	}
	public int getLife_type() {
		return life_type;
	}
	public void setLife_type(int life_type) {
		this.life_type = life_type;
	}
	public int getStart_value() {
		return start_value;
	}
	public void setStart_value(int start_value) {
		this.start_value = start_value;
	}
	public int getEnd_value() {
		return end_value;
	}
	public void setEnd_value(int end_value) {
		this.end_value = end_value;
	}
	public String getOwner_type() {
		return owner_type;
	}
	public void setOwner_type(String owner_type) {
		this.owner_type = owner_type;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
