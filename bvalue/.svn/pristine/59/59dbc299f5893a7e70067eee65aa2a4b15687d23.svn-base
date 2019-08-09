package com.tydic.beijing.bvalue.common;

public class BValueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errCode;
	private String errMsg;
	
	private String strCode="";
	public BValueException(int code, String msg) {
		this.errCode = code;
		this.errMsg = msg;
	}
	
	public BValueException(String code,String msg){
		this.strCode=code;
		this.errMsg=msg;
	}
	
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getStrCode() {
		return strCode;
	}
	public void setStrCode(String strCode) {
		this.strCode = strCode;
	}
	
	
}
