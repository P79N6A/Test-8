package com.tydic.beijing.billing.rating.service;

public class RatingException extends Exception{

	private int lineNumber=0;
	private String fileName="";
	private int errorCode=0;
	private String errorMessage="";
	public String printError(){
		String error="["+fileName+":"+lineNumber+"]:"+"errorCode["+errorCode+"],"+errorMessage;
		return error;
	}
	
	public RatingException(int errorCode,String errorMessage){
		this.errorCode=errorCode;
		this.errorMessage=errorMessage;
		this.lineNumber=Thread.currentThread().getStackTrace()[2].getLineNumber();
		this.fileName=Thread.currentThread().getStackTrace()[2].getFileName();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
