package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * 
 * @author Tian
 *
 */
public class ResourceAdjustResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String Status; // Status
	private String ErrorCode; // ErrorCode
	private String ErrorMessage; // ErrorMessage

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
}
