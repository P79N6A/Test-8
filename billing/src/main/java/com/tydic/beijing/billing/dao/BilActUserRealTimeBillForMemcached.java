package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

import com.tydic.uda.UdaAnnotationSetKey;

public class BilActUserRealTimeBillForMemcached implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String user_id;
	private List<BilActUserRealTimeBill> l_userbill;
	@Id
	public String getUser_id() {
		return user_id;
	}
	@UdaAnnotationSetKey
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public List<BilActUserRealTimeBill> getL_userbill() {
		return l_userbill;
	}
	public void setL_userbill(List<BilActUserRealTimeBill> l_userbill) {
		this.l_userbill = l_userbill;
	}
	@Override
	public String toString() {
		return "BilActUserRealTimeBillForMemcached [user_id=" + user_id
				+ ", l_userbill=" + l_userbill + "]";
	}
}
