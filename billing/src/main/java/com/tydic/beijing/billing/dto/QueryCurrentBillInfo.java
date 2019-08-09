/**
 * 
 */
package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * @author sung
 *
 */
public class QueryCurrentBillInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String MSISDN;
	private String ContactChannle;
	
	public QueryCurrentBillInfo(){}
	
	public QueryCurrentBillInfo(String msisdn,String contactChannle){
		this.MSISDN=msisdn;
		this.ContactChannle=contactChannle;
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
