package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class RuleItemCodeRelation implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long item_group;

	private Long acct_item_code;

	private String group_name;
	private String remark;

	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getItem_group() {
		return item_group;
	}

	public void setItem_group(Long item_group) {
		this.item_group = item_group;
	}

	public Long getAcct_item_code() {
		return acct_item_code;
	}

	public void setAcct_item_code(Long acct_item_code) {
		this.acct_item_code = acct_item_code;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String toString() {
		return "RuleItemCodeRelation [ item_group=" + this.item_group
				+ ",acct_item_code=" + this.acct_item_code + ",group_name="
				+ this.group_name + ",remark="+remark+" ]";
	}
}
