package com.tydic.beijing.billing.credit.memcache.dao;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Id;

import com.tydic.uda.UdaAnnotationSetKey;

public class InfoUser4CreditMemcache implements Serializable {
	public static final String KEY_PREFIX = InfoUser4CreditMemcache.class.getSimpleName();
	private static final long serialVersionUID = 1L;
	public String mem_key;
	public String user_id;
	public String tele_type;
	public String device_number;
	public String user_pwd;
	public String user_status;
	public Date create_date;
	public Date active_date;
	public String user_type;
	public String prepay_flag;
	public String local_net;
	public String develop_channel_id;
	public String product_id;
	public String proto_flag;
	public String sub_user_status;
	public Date stop_date;

	@Id
	public String getMem_key() {
		return mem_key;
	}

	@UdaAnnotationSetKey
	public void setMem_key(String mem_key) {
		this.mem_key = mem_key + user_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTele_type() {
		return tele_type;
	}

	public void setTele_type(String tele_type) {
		this.tele_type = tele_type;
	}

	public String getDevice_number() {
		return device_number;
	}

	public void setDevice_number(String device_number) {
		this.device_number = device_number;
	}

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getActive_date() {
		return active_date;
	}

	public void setActive_date(Date active_date) {
		this.active_date = active_date;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getPrepay_flag() {
		return prepay_flag;
	}

	public void setPrepay_flag(String prepay_flag) {
		this.prepay_flag = prepay_flag;
	}

	public String getLocal_net() {
		return local_net;
	}

	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}

	public String getDevelop_channel_id() {
		return develop_channel_id;
	}

	public void setDevelop_channel_id(String develop_channel_id) {
		this.develop_channel_id = develop_channel_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProto_flag() {
		return proto_flag;
	}

	public void setProto_flag(String proto_flag) {
		this.proto_flag = proto_flag;
	}

	public String getSub_user_status() {
		return sub_user_status;
	}

	public void setSub_user_status(String sub_user_status) {
		this.sub_user_status = sub_user_status;
	}

	public Date getStop_date() {
		return stop_date;
	}

	public void setStop_date(Date stop_date) {
		this.stop_date = stop_date;
	}

	@Override
	public String toString() {
		return "InfoUser4CreditMemcache [user_id=" + user_id + ", tele_type=" + tele_type
				+ ", device_number=" + device_number + ", user_pwd=" + user_pwd + ", user_status="
				+ user_status + ", create_date=" + create_date + ", active_date=" + active_date
				+ ", user_type=" + user_type + ", prepay_flag=" + prepay_flag + ", local_net="
				+ local_net + ", develop_channel_id=" + develop_channel_id + ", product_id="
				+ product_id + ", proto_flag=" + proto_flag + ", sub_user_status="
				+ sub_user_status + ", stop_date=" + stop_date + "]";
	}

	public void clear() {
		mem_key = "";
		user_id = "";
		tele_type = "";
		device_number = "";
		user_pwd = "";
		user_status = "";
		create_date = null;
		active_date = null;
		user_type = "";
		prepay_flag = "";
		local_net = "";
		develop_channel_id = "";
		product_id = "";
		proto_flag = "";
		sub_user_status = "";
		stop_date = null;
	}

}
