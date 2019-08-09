/**
 * 
 */
package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * @author sung
 *
 */
public class CurrentBill implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Status="1";
	private String ErrorCode="";
	private long CurFee;
	private long PayForOthers;
	private long Balance ;
	private String ErrorMessage="";
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		this.Status = status;
	}
	public String getErrorCode() {
		return ErrorCode;
	}
	public void setErrorCode(String errorCode) {
		this.ErrorCode = errorCode;
	}
	public long getCurFee() {
		return CurFee;
	}
	public void setCurFee(long curFee) {
		this.CurFee = curFee;
	}
	public long getPayForOthers() {
		return PayForOthers;
	}
	public void setPayForOthers(long payForOthers) {
		PayForOthers = payForOthers;
	}
	public long getBalance() {
		return Balance;
	}
	public void setBalance(long balance) {
		this.Balance = balance;
	}
	public String getErrorMessage() {
		return ErrorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.ErrorMessage = errorMessage;
	}
	@Override
	public String toString() {
		String ret="Status["+Status+"],ErrorCode["+ErrorCode+"],CurFee["+CurFee+"],PayForOthers["+PayForOthers+"],Balance["+Balance+"],"
				+ "ErrorMessage["+ErrorMessage+"]";
		return ret;
	}
	
}

