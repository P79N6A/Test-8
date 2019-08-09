package com.tydic.beijing.billing.common;

/**
 * 
 * @author Tian
 *
 */
public class BasicException extends Exception {
	private static final long serialVersionUID = 1L;
	private int code;
	private String codeStr;
	private String message;

	public BasicException(int code, String message) {
		this.code = code;
		this.codeStr = "NULL";
		this.message = message;
	}

	public BasicException(String codeStr, String message) {
		this.code = -1;
		this.codeStr = codeStr;
		this.message = message;
	}

	public int getCode() {
		return this.code;
	}
	
	public String getMessage(){
		return this.message;
	}

	public String getCodeStr() {
		return codeStr;
	}

	public void setCodeStr(String codeStr) {
		this.codeStr = codeStr;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ERR_CODE[" + code + ":" + codeStr + "]:" + message;
	}
}
