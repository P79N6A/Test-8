/**  
 * Project Name:Develop
 * File Name:UaParaInfo.java
 * Package Name:com.tydic.beijing.billing.ua.dao
 * Date:2014年7月11日下午4:22:25
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 *  
 */
package com.tydic.beijing.billing.ua.dao;

import java.io.Serializable;

/**
 * ClassName: UaParaInfo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年7月11日 下午4:22:25 <br/>
 * 
 * @author Bradish7Y
 * @version
 * @since JDK 1.6
 */
public class UaParaInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public String file_type;
	public String para_name;
	public String para_value;

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getPara_name() {
		return para_name;
	}

	public void setPara_name(String para_name) {
		this.para_name = para_name;
	}

	public String getPara_value() {
		return para_value;
	}

	public void setPara_value(String para_value) {
		this.para_value = para_value;
	}

	@Override
	public String toString() {
		return "UaParaInfo [file_type=" + file_type + ", para_name="
				+ para_name + ", para_value=" + para_value + "]";
	}

}
