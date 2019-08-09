package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * 基本返回
 * 
 * @author Tian
 *
 */
public class BaseResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	protected String Status;
	protected String ErrorCode;
	protected String ErrorMessage;

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
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

	@Override
	public String toString() {
		return "BaseResponse [Status=" + Status + ", ErrorCode=" + ErrorCode
				+ ", ErrorMessage=" + ErrorMessage + "]";
	}
}
