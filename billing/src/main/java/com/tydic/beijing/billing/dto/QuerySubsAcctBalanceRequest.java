/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.billing.dto;

/**
 *
 * @author wangshida
 */
public class QuerySubsAcctBalanceRequest implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String MSISDN = null;
    private String ContactChannle = null;

    public QuerySubsAcctBalanceRequest() {
    }
    
    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public String getContactChannle() {
        return ContactChannle;
    }

    public void setContactChannle(String ContactChannle) {
        this.ContactChannle = ContactChannle;
    }

    @Override
    public String toString() {
        return "Request{" + "MSISDN=" + MSISDN + ", ContactChannle=" + ContactChannle + '}';
    }
}
