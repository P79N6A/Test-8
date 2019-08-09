package com.tydic.beijing.billing.credit.common;

/**
 * 
 * @author Tian
 *
 */
public class BasicException extends Exception {
	private static final long serialVersionUID = 1L;
	private int code;
	private String message;

	public BasicException(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return "ERR_CODE[" + code + "]:" + message;
	}
}
