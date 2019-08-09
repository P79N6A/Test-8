package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.List;

public class QryPromDetailResponse  implements Serializable{
	private static final long serialVersionUID = 1L;
	private String Status;
	private List<ReleaseFeeDto> ReleaseFeeDtoList;
	private String ErrorCode;
	private String ErrorMessage;
	public String getStatus() {
		return Status;
	}
	@Override
	public String toString() {
		return "QryPromDetailResponse [Status=" + Status + ", ReleaseFeeDtoList=" + ReleaseFeeDtoList + ", ErrorCode="
				+ ErrorCode + ", ErrorMessage=" + ErrorMessage + "]";
	}
	public void setStatus(String status) {
		Status = status;
	}
	public List<ReleaseFeeDto> getReleaseFeeDtoList() {
		return ReleaseFeeDtoList;
	}
	public void setReleaseFeeDtoList(List<ReleaseFeeDto> releaseFeeDtoList) {
		ReleaseFeeDtoList = releaseFeeDtoList;
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
