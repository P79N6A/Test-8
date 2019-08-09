package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;
import java.util.List;

public class QueryTradeResp implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Status;
	private String ErrorCode;
	private String ErrorMessage;
	private String JDPin;
	private long PageCount;
	private List<TradeLogDto> TradeLogDtoList;
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
	public String getJDPin() {
		return JDPin;
	}
	public void setJDPin(String jDPin) {
		JDPin = jDPin;
	}
	public long getPageCount() {
		return PageCount;
	}
	public void setPageCount(long pageCount) {
		PageCount = pageCount;
	}
	public List<TradeLogDto> getTradeLogDtoList() {
		return TradeLogDtoList;
	}
	public void setTradeLogDtoList(List<TradeLogDto> tradeLogDtoList) {
		TradeLogDtoList = tradeLogDtoList;
	}
	

	
	
	

}
