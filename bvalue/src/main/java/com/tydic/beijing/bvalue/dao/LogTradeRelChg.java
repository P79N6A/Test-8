package com.tydic.beijing.bvalue.dao;

public class LogTradeRelChg {

	private String trade_id;
	private String trade_type_code;
	private String external_system_code;
	private String channel_type;
	private String user_id;
	private long partition_id ;
	private long process_tag ;
	private String trade_time ;
	private String process_time;
	private String remark;
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	public String getTrade_type_code() {
		return trade_type_code;
	}
	public void setTrade_type_code(String trade_type_code) {
		this.trade_type_code = trade_type_code;
	}
	public String getExternal_system_code() {
		return external_system_code;
	}
	public void setExternal_system_code(String external_system_code) {
		this.external_system_code = external_system_code;
	}
	public String getChannel_type() {
		return channel_type;
	}
	public void setChannel_type(String channel_type) {
		this.channel_type = channel_type;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public long getPartition_id() {
		return partition_id;
	}
	public void setPartition_id(long partition_id) {
		this.partition_id = partition_id;
	}
	public long getProcess_tag() {
		return process_tag;
	}
	public void setProcess_tag(long process_tag) {
		this.process_tag = process_tag;
	}
	public String getTrade_time() {
		return trade_time;
	}
	public void setTrade_time(String trade_time) {
		this.trade_time = trade_time;
	}
	public String getProcess_time() {
		return process_time;
	}
	public void setProcess_time(String process_time) {
		this.process_time = process_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
