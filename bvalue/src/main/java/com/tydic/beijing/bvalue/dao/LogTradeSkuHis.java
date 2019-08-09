package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class LogTradeSkuHis implements Serializable {

	private static final long serialVersionUID = 1L;
	private String trade_id;
	private String trade_type_code;
	private String user_id;
	private int partition_id;
	private String order_no;
	private String order_type;
	private String sub_order_type;
	private int order_number;
	private String sku_id;
	private long purchase_quantity;
	private long b_value;
	private String org_order_no;
	private long balance;
	private int unit_type_id;
	private long old_balance;
	private long new_balance;
	private long overtop_value;
	private int process_tag;
	private String process_time;
	private String reserve_1;
	private String reserve_2;
	private String reserve_3;
	private String reserve_4;

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

	public String getSub_order_type() {
		return sub_order_type;
	}

	public void setSub_order_type(String sub_order_type) {
		this.sub_order_type = sub_order_type;
	}

	public int getOrder_number() {
		return order_number;
	}

	public void setOrder_number(int order_number) {
		this.order_number = order_number;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public long getPurchase_quantity() {
		return purchase_quantity;
	}

	public void setPurchase_quantity(long purchase_quantity) {
		this.purchase_quantity = purchase_quantity;
	}

	public long getB_value() {
		return b_value;
	}

	public void setB_value(long b_value) {
		this.b_value = b_value;
	}

	public String getOrg_order_no() {
		return org_order_no;
	}

	public void setOrg_order_no(String org_order_no) {
		this.org_order_no = org_order_no;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public int getUnit_type_id() {
		return unit_type_id;
	}

	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}

	public long getOld_balance() {
		return old_balance;
	}

	public void setOld_balance(long old_balance) {
		this.old_balance = old_balance;
	}

	public long getNew_balance() {
		return new_balance;
	}

	public void setNew_balance(long new_balance) {
		this.new_balance = new_balance;
	}

	public long getOvertop_value() {
		return overtop_value;
	}

	public void setOvertop_value(long overtop_value) {
		this.overtop_value = overtop_value;
	}

	public int getProcess_tag() {
		return process_tag;
	}

	public void setProcess_tag(int process_tag) {
		this.process_tag = process_tag;
	}

	public String getProcess_time() {
		return process_time;
	}

	public void setProcess_time(String process_time) {
		this.process_time = process_time;
	}

	public String getReserve_1() {
		return reserve_1;
	}

	public void setReserve_1(String reserve_1) {
		this.reserve_1 = reserve_1;
	}

	public String getReserve_2() {
		return reserve_2;
	}

	public void setReserve_2(String reserve_2) {
		this.reserve_2 = reserve_2;
	}

	public String getReserve_3() {
		return reserve_3;
	}

	public void setReserve_3(String reserve_3) {
		this.reserve_3 = reserve_3;
	}

	public String getReserve_4() {
		return reserve_4;
	}

	public void setReserve_4(String reserve_4) {
		this.reserve_4 = reserve_4;
	}

	@Override
	public String toString() {
		return "LogTradeSku [trade_id=" + trade_id + ", trade_type_code=" + trade_type_code
				+ ", user_id=" + user_id + ", partition_id=" + partition_id + ", order_no="
				+ order_no + ", order_type=" + order_type + ", sub_order_type=" + sub_order_type
				+ ", order_number=" + order_number + ", sku_id=" + sku_id + ", purchase_quantity="
				+ purchase_quantity + ", b_value=" + b_value + ", org_order_no=" + org_order_no
				+ ", balance=" + balance + ", unit_type_id=" + unit_type_id + ", old_balance="
				+ old_balance + ", new_balance=" + new_balance + ", overtop_value=" + overtop_value
				+ ", process_tag=" + process_tag + ", process_time=" + process_time
				+ ", reserve_1=" + reserve_1 + ", reserve_2=" + reserve_2 + ", reserve_3="
				+ reserve_3 + ", reserve_4=" + reserve_4 + "]";
	}

}
