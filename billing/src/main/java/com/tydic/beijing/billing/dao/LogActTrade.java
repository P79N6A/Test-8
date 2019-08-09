package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class LogActTrade implements Serializable {

	/**
	 * YUANDAO
	 */
	private static final long serialVersionUID = 1L;
	private String trade_id;
	private long trade_type_code;
	private String trader_type;
	private String trader_account;
	private String user_id;
	private String trade_time;
	private String staff_id;
	private String channel_id;
	private String external_system_id;
	private String external_trade_id;
	private String remark;
	private String reserve_1;
	private String reserve_2;
	private String reserve_3;
	private String reserve_4;
	private String reserve_5;
	private String reserve_6;
	private String reserve_7;
	private String reserve_8;

	public String getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}

	public long getTrade_type_code() {
		return trade_type_code;
	}

	public void setTrade_type_code(long trade_type_code) {
		this.trade_type_code = trade_type_code;
	}

	public String getTrader_type() {
		return trader_type;
	}

	public void setTrader_type(String trader_type) {
		this.trader_type = trader_type;
	}

	public String getTrader_account() {
		return trader_account;
	}

	public void setTrader_account(String trader_account) {
		this.trader_account = trader_account;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTrade_time() {
		return trade_time;
	}

	public void setTrade_time(String trade_time) {
		this.trade_time = trade_time;
	}

	public String getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getExternal_system_id() {
		return external_system_id;
	}

	public void setExternal_system_id(String external_system_id) {
		this.external_system_id = external_system_id;
	}

	public String getExternal_trade_id() {
		return external_trade_id;
	}

	public void setExternal_trade_id(String external_trade_id) {
		this.external_trade_id = external_trade_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getReserve_5() {
		return reserve_5;
	}

	public void setReserve_5(String reserve_5) {
		this.reserve_5 = reserve_5;
	}

	public String getReserve_6() {
		return reserve_6;
	}

	public void setReserve_6(String reserve_6) {
		this.reserve_6 = reserve_6;
	}

	public String getReserve_7() {
		return reserve_7;
	}

	public void setReserve_7(String reserve_7) {
		this.reserve_7 = reserve_7;
	}

	public String getReserve_8() {
		return reserve_8;
	}

	public void setReserve_8(String reserve_8) {
		this.reserve_8 = reserve_8;
	}

	public String toString() {
		return "LogActTrade [trade_id=" + trade_id + ",trade_type_code="
				+ trade_type_code + "trader_type=" + trader_type
				+ ",trader_account=" + trader_account + ",user_id=" + user_id
				+ ",trade_time=" + trade_time + ",staff_id" + staff_id
				+ ",channel_id=" + channel_id + ",external_system_id="
				+ external_system_id + ",external_trade_id="
				+ external_trade_id + ",remark=" + remark + ",reserve_1"
				+ reserve_1 + ",reserve_2" + reserve_2 + ",reserve_3"
				+ reserve_3 + ",reserve_4" + reserve_4 + ",reserve_5"
				+ reserve_5 + ",reserve_6" + reserve_6 + ",reserve_7"
				+ reserve_7 + ",reserve_8" + reserve_8 + "]";
	}

}
