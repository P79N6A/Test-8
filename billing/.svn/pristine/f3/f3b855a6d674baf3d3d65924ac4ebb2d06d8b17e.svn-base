package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubsBillPeriod implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Status="1";
	private List<BillCycleDto> BillCycleDtoList =new ArrayList<BillCycleDto>();
	private String ErrorCode;
	private String ErrorMessage;
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public List<BillCycleDto> getBillCycleDtoList() {
		return BillCycleDtoList;
	}
	public void setBillCycleDtoList(List<BillCycleDto> billCycleDtoList) {
		BillCycleDtoList = billCycleDtoList;
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
		
		String ret="Status["+Status+"],BillCycleDtoList size["+BillCycleDtoList.size()+"],ErrorCode["+ErrorCode+"],ErrorMessage["+
				ErrorMessage+"]";
		return ret;
	}
	
	
}
