/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.credit.dao;

import java.io.Serializable;

/**
 * credit paramters<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class CreditPara implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CreditPara [name=" + name + ", value=" + value + "]";
	}

}
