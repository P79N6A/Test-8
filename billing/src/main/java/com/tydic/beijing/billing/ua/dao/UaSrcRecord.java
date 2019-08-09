/**  
 * Project Name:Develop
 * File Name:UaSrcRecord.java
 * Package Name:com.tydic.beijing.billing.ua.dao
 * Date:2014年7月11日下午4:24:55
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 *  
 */
package com.tydic.beijing.billing.ua.dao;

import java.io.Serializable;

/**
 * ClassName: UaSrcRecord <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年7月11日 下午4:24:55 <br/>
 * 
 * @author Bradish7Y
 * @version
 * @since JDK 1.6
 */
public class UaSrcRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	public String file_type;
	public int record_serial;
	public int field_serial;
	public int factor1;
	public int factor2;
	public String field_name;
	public String encode_type;

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public int getRecord_serial() {
		return record_serial;
	}

	public void setRecord_serial(int record_serial) {
		this.record_serial = record_serial;
	}

	public int getField_serial() {
		return field_serial;
	}

	public void setField_serial(int field_serial) {
		this.field_serial = field_serial;
	}

	public int getFactor1() {
		return factor1;
	}

	public void setFactor1(int factor1) {
		this.factor1 = factor1;
	}

	public int getFactor2() {
		return factor2;
	}

	public void setFactor2(int factor2) {
		this.factor2 = factor2;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getEncode_type() {
		return encode_type;
	}

	public void setEncode_type(String encode_type) {
		this.encode_type = encode_type;
	}

	@Override
	public String toString() {
		return "UaSrcRecord [file_type=" + file_type + ", record_serial=" + record_serial
				+ ", field_serial=" + field_serial + ", factor1=" + factor1 + ", factor2="
				+ factor2 + ", field_name=" + field_name + ", encode_type=" + encode_type + "]";
	}

}
