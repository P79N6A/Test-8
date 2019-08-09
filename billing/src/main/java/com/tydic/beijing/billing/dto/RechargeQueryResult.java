package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * 
 * @author Tian
 *
 */
public class RechargeQueryResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private String Status; // Status
	private String Sn; // Sn
	private long Charge; // Charge
	private String RechargeTime; // RechargeTime
	private String ErrorCode; // ErrorCode
	private String ErrorMessage; // ErrorMessage
	private long RechargeStatus; // RechargeStatus

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSn() {
		return Sn;
	}

	public void setSn(String sn) {
		Sn = sn;
	}

	public long getCharge() {
		return Charge;
	}

	public void setCharge(long charge) {
		Charge = charge;
	}

	public String getRechargeTime() {
		return RechargeTime;
	}

	public void setRechargeTime(String rechargeTime) {
		RechargeTime = rechargeTime;
	}

	public String getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}

	public long getRechargeStatus() {
		return RechargeStatus;
	}

	public void setRechargeStatus(long rechargeStatus) {
		RechargeStatus = rechargeStatus;
	}
}
