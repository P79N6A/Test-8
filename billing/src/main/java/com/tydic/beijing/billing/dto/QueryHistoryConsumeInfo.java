package com.tydic.beijing.billing.dto;

import java.io.Serializable;

public class QueryHistoryConsumeInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String MSISDN;
	private String ContactChannle;
	
	public QueryHistoryConsumeInfo(){}
	
	public QueryHistoryConsumeInfo(String msisdn,String channle){
		this.MSISDN=msisdn;
		this.ContactChannle=channle;
		
	}

	public String getMSISDN() {
		return MSISDN;
	}

	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}

	public String getContactChannle() {
		return ContactChannle;
	}

	public void setContactChannle(String contactChannle) {
		ContactChannle = contactChannle;
	}
	
	
	
}
