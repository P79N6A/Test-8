/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

/**
 * 查重日志表<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class LogAutoExchangeLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private String user_id;
	private String exchange_id;
	private String cycle_type;
	private long channel_no;
	private String cycle_id;
	private String process_tag;
	private String complete_time;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getExchange_id() {
		return exchange_id;
	}

	public void setExchange_id(String exchange_id) {
		this.exchange_id = exchange_id;
	}

	public String getCycle_type() {
		return cycle_type;
	}

	public void setCycle_type(String cycle_type) {
		this.cycle_type = cycle_type;
	}

	public long getChannel_no() {
		return channel_no;
	}

	public void setChannel_no(long channel_no) {
		this.channel_no = channel_no;
	}

	public String getCycle_id() {
		return cycle_id;
	}

	public void setCycle_id(String cycle_id) {
		this.cycle_id = cycle_id;
	}

	public String getProcess_tag() {
		return process_tag;
	}

	public void setProcess_tag(String process_tag) {
		this.process_tag = process_tag;
	}

	public String getComplete_time() {
		return complete_time;
	}

	public void setComplete_time(String complete_time) {
		this.complete_time = complete_time;
	}

	@Override
	public String toString() {
		return "LogAutoExchangeLog [user_id=" + user_id + ", exchange_id=" + exchange_id
				+ ", cycle_type=" + cycle_type + ", channel_no=" + channel_no + ", cycle_id="
				+ cycle_id + ", process_tag=" + process_tag + ", complete_time=" + complete_time
				+ "]";
	}

}
