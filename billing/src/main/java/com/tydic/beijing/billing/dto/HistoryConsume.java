package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistoryConsume implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Status="1";
	private long TotalFee;
	private List<BillDto> BillDtoList =new ArrayList<BillDto>();
	private String ErrorCode="" ;
	private String ErrorMessage="" ;
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public long getTotalFee() {
		return TotalFee;
	}
	public void setTotalFee(long totalFee) {
		TotalFee = totalFee;
	}
	public List<BillDto> getBillDtoList() {
		return BillDtoList;
	}
	public void setBillDtoList(List<BillDto> billDtoList) {
		BillDtoList = billDtoList;
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
		String ret="Status["+Status+"],TotalFee["+TotalFee+"],BillDtoList size["+BillDtoList.size()+
				"],ErrorCode["+ErrorCode+"],ErrorMessage["+ErrorMessage+"]";
		return ret;
	}
	
	
	
}
