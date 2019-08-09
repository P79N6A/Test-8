package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class RuleGroupStateRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String state_group;

	private String user_state;
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState_group() {
		return state_group;
	}

	public void setState_group(String state_group) {
		this.state_group = state_group;
	}

	public String getUser_state() {
		return user_state;
	}

	public void setUser_state(String user_state) {
		this.user_state = user_state;
	}

	public String toString() {
		return "RuleGroupStateRelation [state_group=" + this.state_group
				+ ",user_state=" + this.user_state + " ]";
	}
}
