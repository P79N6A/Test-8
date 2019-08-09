package com.tydic.beijing.billing.credit.memcache.dao;

import java.io.Serializable;

import javax.persistence.Id;

import com.tydic.uda.UdaAnnotationSetKey;

public class CreditActionMemcache implements Serializable {
	public static final String KEY_PREFIX = CreditActionMemcache.class.getSimpleName();
	private static final long serialVersionUID = 1L;
	public String mem_key;
	public long id;
	public String user_id;
	public String pay_id;
	public String reason;
	public String action_time;
	public String local_net;

	@Id
	public String getMem_key() {
		return mem_key;
	}

	@UdaAnnotationSetKey
	public void setMem_key(String mem_key) {
		this.mem_key = mem_key + user_id + reason;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPay_id() {
		return pay_id;
	}

	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAction_time() {
		return action_time;
	}

	public void setAction_time(String action_time) {
		this.action_time = action_time;
	}

	public String getLocal_net() {
		return local_net;
	}

	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}

	@Override
	public String toString() {
		return "CreditAction [id=" + id + ", user_id=" + user_id + ", pay_id=" + pay_id
				+ ", reason=" + reason + ", action_time=" + action_time + ", local_net="
				+ local_net + "]";
	}

	public void clear() {
		mem_key = "";
		id = -1;
		user_id = "";
		pay_id = "";
		reason = "";
		action_time = "";
		local_net = "";
	}

}
