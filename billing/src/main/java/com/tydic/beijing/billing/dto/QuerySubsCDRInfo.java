package com.tydic.beijing.billing.dto;

import java.io.Serializable;

public class QuerySubsCDRInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String MSISDN;
	private long BillCycleID=0;
	private String StartTime;
	private String EndTime;
	private String ServiceType;
	private String ContactChannle;
	private long PageIndex=0;
	private long RowPerPage=0;
	
	//JD-523
	private String QueryCallType;  // 1主叫    ,  2 被叫   
	private String QueryNumber ;
	
	public QuerySubsCDRInfo(){}
	
	public QuerySubsCDRInfo(String nbr,long cycle,String start,String end,String service,String channle,long index,long row,
			String queryNbr,String queryType){
		this.MSISDN=nbr;
		this.BillCycleID=cycle;
		this.StartTime=start;
		this.EndTime=end;
		this.ServiceType=service;
		this.ContactChannle=channle;
		this.PageIndex=index;
		this.RowPerPage=row;
		this.QueryNumber=queryNbr;
		this.QueryCallType=queryType;
	}
	public String getMSISDN() {
		return MSISDN;
	}
	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}
	public long getBillCycleID() {
		return BillCycleID;
	}
	public void setBillCycleID(long billCycleID) {
		BillCycleID = billCycleID;
	}
	
	public String getServiceType() {
		return ServiceType;
	}
	public void setServiceType(String serviceType) {
		ServiceType = serviceType;
	}
	public String getContactChannle() {
		return ContactChannle;
	}
	public void setContactChannle(String contactChannle) {
		ContactChannle = contactChannle;
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

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getQueryCallType() {
		return QueryCallType;
	}

	public void setQueryCallType(String queryCallType) {
		QueryCallType = queryCallType;
	}

	public String getQueryNumber() {
		return QueryNumber;
	}

	public void setQueryNumber(String queryNumber) {
		QueryNumber = queryNumber;
	}

	@Override
	public String toString() {
		return "QuerySubsCDRInfo{" +
				"MSISDN='" + MSISDN + '\'' +
				", BillCycleID=" + BillCycleID +
				", StartTime='" + StartTime + '\'' +
				", EndTime='" + EndTime + '\'' +
				", ServiceType='" + ServiceType + '\'' +
				", ContactChannle='" + ContactChannle + '\'' +
				", PageIndex=" + PageIndex +
				", RowPerPage=" + RowPerPage +
				", QueryCallType='" + QueryCallType + '\'' +
				", QueryNumber='" + QueryNumber + '\'' +
				'}';
	}
}
