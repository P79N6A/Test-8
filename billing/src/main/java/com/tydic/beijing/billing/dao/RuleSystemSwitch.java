package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class RuleSystemSwitch implements Serializable {

	/**
	 * yuandao
	 */
	private static final long serialVersionUID = 1L;
	private int  domain_code;
	private String  program_name;
	private String  resale_code;
	private String  switch_code;
	private String  switch_desc;
	private String  switch_value;
	private String  para_char1;
	private String  para_char2;
	private String  para_char3;
	private String  para_char4;
	private int  para_num1;
	private int  para_num2;
	private long  para_num3;
	private long  para_num4;
	private String  remark;
	public int getDomain_code() {
		return domain_code;
	}
	public void setDomain_code(int domain_code) {
		this.domain_code = domain_code;
	}
	public String getProgram_name() {
		return program_name;
	}
	public void setProgram_name(String program_name) {
		this.program_name = program_name;
	}
	public String getResale_code() {
		return resale_code;
	}
	public void setResale_code(String resale_code) {
		this.resale_code = resale_code;
	}
	public String getSwitch_code() {
		return switch_code;
	}
	public void setSwitch_code(String switch_code) {
		this.switch_code = switch_code;
	}
	public String getSwitch_desc() {
		return switch_desc;
	}
	public void setSwitch_desc(String switch_desc) {
		this.switch_desc = switch_desc;
	}
	public String getSwitch_value() {
		return switch_value;
	}
	public void setSwitch_value(String switch_value) {
		this.switch_value = switch_value;
	}
	public String getPara_char1() {
		return para_char1;
	}
	public void setPara_char1(String para_char1) {
		this.para_char1 = para_char1;
	}
	public String getPara_char2() {
		return para_char2;
	}
	public void setPara_char2(String para_char2) {
		this.para_char2 = para_char2;
	}
	public String getPara_char3() {
		return para_char3;
	}
	public void setPara_char3(String para_char3) {
		this.para_char3 = para_char3;
	}
	public String getPara_char4() {
		return para_char4;
	}
	public void setPara_char4(String para_char4) {
		this.para_char4 = para_char4;
	}
	public int getPara_num1() {
		return para_num1;
	}
	public void setPara_num1(int para_num1) {
		this.para_num1 = para_num1;
	}
	public int getPara_num2() {
		return para_num2;
	}
	public void setPara_num2(int para_num2) {
		this.para_num2 = para_num2;
	}
	public long getPara_num3() {
		return para_num3;
	}
	public void setPara_num3(long para_num3) {
		this.para_num3 = para_num3;
	}
	public long getPara_num4() {
		return para_num4;
	}
	public void setPara_num4(long para_num4) {
		this.para_num4 = para_num4;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "RuleSystemSwitch [domain_code=" + domain_code
				+ ", program_name=" + program_name + ", resale_code="
				+ resale_code + ", switch_code=" + switch_code
				+ ", switch_desc=" + switch_desc + ", switch_value="
				+ switch_value + ", para_char1=" + para_char1 + ", para_char2="
				+ para_char2 + ", para_char3=" + para_char3 + ", para_char4="
				+ para_char4 + ", para_num1=" + para_num1 + ", para_num2="
				+ para_num2 + ", para_num3=" + para_num3 + ", para_num4="
				+ para_num4 + ", remark=" + remark + "]";
	}
}
