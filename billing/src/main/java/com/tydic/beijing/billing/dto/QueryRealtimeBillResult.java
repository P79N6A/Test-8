package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.List;

import com.tydic.beijing.billing.dao.BilActRealTimeBillForOracle;

/**
 * 
 * @author Tian
 *
 */
public class QueryRealtimeBillResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<BilActRealTimeBillForOracle> RealtimeBills;
	private String Status;
	private String ErrorCode;
	private String ErrorMessage;

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

	public List<BilActRealTimeBillForOracle> getRealtimeBills() {
		return RealtimeBills;
	}

	public void setRealtimeBills(List<BilActRealTimeBillForOracle> realtimeBills) {
		RealtimeBills = realtimeBills;
	}
}
