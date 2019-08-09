/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua.dao;

import java.io.Serializable;

/**
 * TODO(用几句话描述这个类型)<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class UaDstCdr implements Serializable {

	private static final long serialVersionUID = 1L;
	public String file_type;
	public String rule_condition;

	public final String getFile_type() {
		return file_type;
	}

	public final void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public final String getRule_condition() {
		return rule_condition;
	}

	public final void setRule_condition(String rule_condition) {
		this.rule_condition = rule_condition;
	}

	@Override
	public String toString() {
		return "UaDstCdr [file_type=" + file_type + ", rule_condition=" + rule_condition + "]";
	}

}
