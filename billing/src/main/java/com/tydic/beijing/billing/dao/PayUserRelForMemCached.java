package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

import com.tydic.uda.UdaAnnotationSetKey;

public class PayUserRelForMemCached implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String user_id;
	private List<PayUserRel> userPayUserRels;
	@Id
	public String getUser_id() {
		return user_id;
	}
	@UdaAnnotationSetKey
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public List<PayUserRel> getUserPayUserRels() {
		return userPayUserRels;
	}
	public void setUserPayUserRels(List<PayUserRel> userPayUserRels) {
		this.userPayUserRels = userPayUserRels;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
