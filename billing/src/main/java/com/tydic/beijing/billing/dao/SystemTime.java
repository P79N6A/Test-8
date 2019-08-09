package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class SystemTime implements Serializable {
	/**
	 * 获取系统时间和时间戳 from dual
	 * lijianyu
	 */
	private static final long serialVersionUID = 1L;
	private String time;
	private String timestamp;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
