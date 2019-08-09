package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.sql.Date;

public class QPresentUserInfoHis implements Serializable {
	private static final long serialVersionUID = 1L;

	private String present_user_id;
	private String user_id;
	private long protocol_id;
	private String resource_type;
	private long resource_value;
	private Date eff_date;
	private Date exp_date;
	private Date create_time;
	private long state;
	private String serv_code;
	private String operate_type;
	public String getPresent_user_id() {
		return present_user_id;
	}
	public void setPresent_user_id(String present_user_id) {
		this.present_user_id = present_user_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public long getProtocol_id() {
		return protocol_id;
	}
	public void setProtocol_id(long protocol_id) {
		this.protocol_id = protocol_id;
	}
	public String getResource_type() {
		return resource_type;
	}
	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}
	public long getResource_value() {
		return resource_value;
	}
	public void setResource_value(long resource_value) {
		this.resource_value = resource_value;
	}
	public Date getEff_date() {
		return eff_date;
	}
	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}
	public Date getExp_date() {
		return exp_date;
	}
	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public long getState() {
		return state;
	}
	public void setState(long state) {
		this.state = state;
	}
	public String getServ_code() {
		return serv_code;
	}
	public void setServ_code(String serv_code) {
		this.serv_code = serv_code;
	}
	public String getOperate_type() {
		return operate_type;
	}
	public void setOperate_type(String operate_type) {
		this.operate_type = operate_type;
	}
	@Override
	public String toString() {
		return "QPresentUserInfoHis [present_user_id=" + present_user_id
				+ ", user_id=" + user_id + ", protocol_id=" + protocol_id
				+ ", resource_type=" + resource_type + ", resource_value="
				+ resource_value + ", eff_date=" + eff_date + ", exp_date="
				+ exp_date + ", create_time=" + create_time + ", state="
				+ state + ", serv_code=" + serv_code + ", operate_type="
				+ operate_type + "]";
	}
}
