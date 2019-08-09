package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;
import java.util.Date;

public class QueryTradeReq implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String JDPin ;
	private String AccessTag;
	private String TradeTypeCode;
	private Date StartTime ; //yyyy-mm-dd
	private Date EndTime;
	private long PageIndex;
	private long RowPerPage;
	public String getJDPin() {
		return JDPin;
	}
	public void setJDPin(String jDPin) {
		JDPin = jDPin;
	}
	public String getAccessTag() {
		return AccessTag;
	}
	public void setAccessTag(String accessTag) {
		AccessTag = accessTag;
	}
	public String getTradeTypeCode() {
		return TradeTypeCode;
	}
	public void setTradeTypeCode(String tradeTypeCode) {
		TradeTypeCode = tradeTypeCode;
	}

	public long getPageIndex() {
		return PageIndex;
	}
	public void setPageIndex(long pageIndex) {
		PageIndex = pageIndex;
	}
	public long getRowPerPage() {
		return RowPerPage;
	}
	public void setRowPerPage(long rowPerPage) {
		RowPerPage = rowPerPage;
	}
	public Date getStartTime() {
		return StartTime;
	}
	public void setStartTime(Date startTime) {
		StartTime = startTime;
	}
	public Date getEndTime() {
		return EndTime;
	}
	public void setEndTime(Date endTime) {
		EndTime = endTime;
	}
	
	



}
