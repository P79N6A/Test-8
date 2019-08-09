package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

import com.tydic.uda.UdaAnnotationSetKey;

public final class LifeUserPayForMemCached implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String user_id;
	private List<LifeUserPay> userLifeUserPays;

	@Id
	public String getUser_id() {
		return user_id;
	}
	@UdaAnnotationSetKey
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<LifeUserPay> getUserLifeUserPays() {
		return userLifeUserPays;
	}

	public void setUserLifeUserPays(List<LifeUserPay> userLifeUserPays) {
		this.userLifeUserPays = userLifeUserPays;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
