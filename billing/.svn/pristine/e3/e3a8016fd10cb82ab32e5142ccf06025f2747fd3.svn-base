package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubsCDR implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Status="1";
	private String ErrorCode="";
	private String ErrorMessage="";
	
	private List<CDRDto> CDRDtoList=new ArrayList<CDRDto>();
	
	private long PageCount;
	
	
	// JD-523
	private long TotalRecord;
	private long TotalCharge;
	private long TotalFlow;
	
	
	public List<CDRDto> getCDRDtoList() {
		return CDRDtoList;
	}

	public void setCDRDtoList(List<CDRDto> cDRDtoList) {
		CDRDtoList = cDRDtoList;
	}

	public long getPageCount() {
		return PageCount;
	}

	public void setPageCount(long pageCount) {
		PageCount = pageCount;
	}

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

	public long getTotalRecord() {
		return TotalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		TotalRecord = totalRecord;
	}

	public long getTotalCharge() {
		return TotalCharge;
	}

	public void setTotalCharge(long totalCharge) {
		TotalCharge = totalCharge;
	}

	public long getTotalFlow() {
		return TotalFlow;
	}

	public void setTotalFlow(long totalFlow) {
		TotalFlow = totalFlow;
	}


	@Override
	public String toString() {
		return "SubsCDR{" +
				"Status='" + Status + '\'' +
				", ErrorCode='" + ErrorCode + '\'' +
				", ErrorMessage='" + ErrorMessage + '\'' +
				", CDRDtoList=" + CDRDtoList +
				", PageCount=" + PageCount +
				", TotalRecord=" + TotalRecord +
				", TotalCharge=" + TotalCharge +
				", TotalFlow=" + TotalFlow +
				'}';
	}
}
