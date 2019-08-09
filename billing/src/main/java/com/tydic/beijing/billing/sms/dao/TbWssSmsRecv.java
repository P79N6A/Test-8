package com.tydic.beijing.billing.sms.dao;

import java.io.Serializable;
import java.sql.Date;

public class TbWssSmsRecv implements Serializable {
	private static final long serialVersionUID = 1L;

	private long ID;
	private Date SUBMIT_DATE;
	private String MSISDN;
	private String PASSWAY_NUMBER;
	private String PUB_PORT;
	private String MSG_CONTENT;
	private String process_tag;
	private String REMARK;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public Date getSUBMIT_DATE() {
		return SUBMIT_DATE;
	}
	public void setSUBMIT_DATE(Date sUBMIT_DATE) {
		SUBMIT_DATE = sUBMIT_DATE;
	}
	public String getMSISDN() {
		return MSISDN;
	}
	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}
	public String getPASSWAY_NUMBER() {
		return PASSWAY_NUMBER;
	}
	public void setPASSWAY_NUMBER(String pASSWAY_NUMBER) {
		PASSWAY_NUMBER = pASSWAY_NUMBER;
	}
	public String getPUB_PORT() {
		return PUB_PORT;
	}
	public void setPUB_PORT(String pUB_PORT) {
		PUB_PORT = pUB_PORT;
	}
	public String getMSG_CONTENT() {
		return MSG_CONTENT;
	}
	public void setMSG_CONTENT(String mSG_CONTENT) {
		MSG_CONTENT = mSG_CONTENT;
	}
	public String getProcess_tag() {
		return process_tag;
	}
	public void setProcess_tag(String process_tag) {
		this.process_tag = process_tag;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	@Override
	public String toString() {
		return "TB_WSS_SMS_RECV [ID=" + ID + ", SUBMIT_DATE=" + SUBMIT_DATE
				+ ", MSISDN=" + MSISDN + ", PASSWAY_NUMBER=" + PASSWAY_NUMBER
				+ ", PUB_PORT=" + PUB_PORT + ", MSG_CONTENT=" + MSG_CONTENT
				+ ", process_tag=" + process_tag + ", REMARK=" + REMARK + "]";
	}
}
