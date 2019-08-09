package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/*
 * yuandao
 */
public class RetryCycleRentInfo implements Serializable {
	

	private static final long serialVersionUID = 1L;

	private String UserId;
	
	private String ContactChannle;

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getContactChannle() {
		return ContactChannle;
	}

	public void setContactChannle(String contactChannle) {
		ContactChannle = contactChannle;
	}
	
	

}
