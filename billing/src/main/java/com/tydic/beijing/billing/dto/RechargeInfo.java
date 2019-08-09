package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Tian
 *
 */
public class RechargeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String MSISDN; // MSISDN
	private String Sn; // Sn
	private List<RechargeInfoDto> RechargeInfoDtoList; // RechargeInfoDtoList
	private String UserEventCode; // UserEventCode
	private String ContactChannle; // ContactChannle
	private String JdAcctNbr; // JdAcctNbr
	private String CallBackUrl; // CallBackUrl
	
	private String OperId;
	private String GiftReason;

	public String getMSISDN() {
		return MSISDN;
	}

	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}

	public String getSn() {
		return Sn;
	}

	public void setSn(String sn) {
		Sn = sn;
	}

	public List<RechargeInfoDto> getRechargeInfoDtoList() {
		return RechargeInfoDtoList;
	}

	public void setRechargeInfoDtoList(List<RechargeInfoDto> rechargeInfoDtoList) {
		RechargeInfoDtoList = rechargeInfoDtoList;
	}

	public String getUserEventCode() {
		return UserEventCode;
	}

	public void setUserEventCode(String userEventCode) {
		UserEventCode = userEventCode;
	}

	public String getContactChannle() {
		return ContactChannle;
	}

	public void setContactChannle(String contactChannle) {
		ContactChannle = contactChannle;
	}

	public String getJdAcctNbr() {
		return JdAcctNbr;
	}

	public void setJdAcctNbr(String jdAcctNbr) {
		JdAcctNbr = jdAcctNbr;
	}

	public String getCallBackUrl() {
		return CallBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		CallBackUrl = callBackUrl;
	}

	public String getOperId() {
		return OperId;
	}

	public void setOperId(String operId) {
		OperId = operId;
	}

	public String getGiftReason() {
		return GiftReason;
	}

	public void setGiftReason(String giftReason) {
		GiftReason = giftReason;
	}

	@Override
	public String toString() {
		return "RechargeInfo [MSISDN=" + MSISDN + ", Sn=" + Sn
				+ ", RechargeInfoDtoList=" + RechargeInfoDtoList
				+ ", UserEventCode=" + UserEventCode + ", ContactChannle="
				+ ContactChannle + ", JdAcctNbr=" + JdAcctNbr
				+ ", CallBackUrl=" + CallBackUrl + ", OperId=" + OperId
				+ ", GiftReason=" + GiftReason + "]";
	}
}
