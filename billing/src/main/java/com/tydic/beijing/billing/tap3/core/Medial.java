/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.tap3.core;

import java.util.List;

/**
 * tap3源记录到输出记录的中间记录<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class Medial {
	/**
	 * 0 normal record
	 * 1 list collection record
	 */
	private int type = 0;
	private String value;
	private List<String> multipValue;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getMultipValue() {
		return multipValue;
	}

	public void setMultipValue(List<String> multipValue) {
		this.multipValue = multipValue;
	}

}
