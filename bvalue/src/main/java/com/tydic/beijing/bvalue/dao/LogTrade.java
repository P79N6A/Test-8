package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class LogTrade implements Serializable {
	private static final long serialVersionUID = 1L;

	private String trade_id;
	private String trade_type_code;
	private String external_system_code;
	private String channel_type;
	private String user_id;
	private int partition_id;
	private String order_no;
	private String order_type;
	private long order_amount;
	private String order_completion_time;
	private long balance_type_id;
	private long unit_type_id;
	private long balance;
	private long process_tag;
	private long overtop_value; // 溢出B值
	private String trade_time;
	private String process_time;
	private long reserve_n1;
	private long reserve_n2;
	private long reserve_n3;
	private long reserve_n4;
	private String reserve_c1;
	private String reserve_c2;
	private String reserve_c3;
	private String reserve_c4;

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

	public int getPartition_id() {
		return partition_id;
	}

	public void setPartition_id(int partition_id) {
		this.partition_id = partition_id;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public long getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(long order_amount) {
		this.order_amount = order_amount;
	}

	public String getOrder_completion_time() {
		return order_completion_time;
	}

	public void setOrder_completion_time(String order_completion_time) {
		this.order_completion_time = order_completion_time;
	}

	public long getBalance_type_id() {
		return balance_type_id;
	}

	public void setBalance_type_id(long balance_type_id) {
		this.balance_type_id = balance_type_id;
	}

	public long getUnit_type_id() {
		return unit_type_id;
	}

	public void setUnit_type_id(long unit_type_id) {
		this.unit_type_id = unit_type_id;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public long getProcess_tag() {
		return process_tag;
	}

	public void setProcess_tag(long process_tag) {
		this.process_tag = process_tag;
	}

	public long getOvertop_value() {
		return overtop_value;
	}

	public void setOvertop_value(long overtop_value) {
		this.overtop_value = overtop_value;
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

	public long getReserve_n1() {
		return reserve_n1;
	}

	public void setReserve_n1(long reserve_n1) {
		this.reserve_n1 = reserve_n1;
	}

	public long getReserve_n2() {
		return reserve_n2;
	}

	public void setReserve_n2(long reserve_n2) {
		this.reserve_n2 = reserve_n2;
	}

	public long getReserve_n3() {
		return reserve_n3;
	}

	public void setReserve_n3(long reserve_n3) {
		this.reserve_n3 = reserve_n3;
	}

	public long getReserve_n4() {
		return reserve_n4;
	}

	public void setReserve_n4(long reserve_n4) {
		this.reserve_n4 = reserve_n4;
	}

	public String getReserve_c1() {
		return reserve_c1;
	}

	public void setReserve_c1(String reserve_c1) {
		this.reserve_c1 = reserve_c1;
	}

	public String getReserve_c2() {
		return reserve_c2;
	}

	public void setReserve_c2(String reserve_c2) {
		this.reserve_c2 = reserve_c2;
	}

	public String getReserve_c3() {
		return reserve_c3;
	}

	public void setReserve_c3(String reserve_c3) {
		this.reserve_c3 = reserve_c3;
	}

	public String getReserve_c4() {
		return reserve_c4;
	}

	public void setReserve_c4(String reserve_c4) {
		this.reserve_c4 = reserve_c4;
	}

	@Override
	public String toString() {
		return "LogTrade [trade_id=" + trade_id + ", trade_type_code=" + trade_type_code
				+ ", external_system_code=" + external_system_code + ", channel_type="
				+ channel_type + ", user_id=" + user_id + ", partition_id=" + partition_id
				+ ", order_no=" + order_no + ", order_type=" + order_type + ", order_amount="
				+ order_amount + ", order_completion_time=" + order_completion_time
				+ ", balance_type_id=" + balance_type_id + ", unit_type_id=" + unit_type_id
				+ ", balance=" + balance + ", process_tag=" + process_tag + ", overtop_value="
				+ overtop_value + ", trade_time=" + trade_time + ", process_time=" + process_time
				+ ", reserve_n1=" + reserve_n1 + ", reserve_n2=" + reserve_n2 + ", reserve_n3="
				+ reserve_n3 + ", reserve_n4=" + reserve_n4 + ", reserve_c1=" + reserve_c1
				+ ", reserve_c2=" + reserve_c2 + ", reserve_c3=" + reserve_c3 + ", reserve_c4="
				+ reserve_c4 + "]";
	}

}
