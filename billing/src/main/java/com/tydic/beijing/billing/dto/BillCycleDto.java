package com.tydic.beijing.billing.dto;

import java.io.Serializable;

public class BillCycleDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private long BillCycleID;
	private String BillCycleStartDate;
	private String BillCycleEndDate ;
	
	public BillCycleDto(){}
	
	public BillCycleDto(long cycle,String startDate,String endDate){
		this.BillCycleID=cycle;
		this.BillCycleStartDate=startDate;
		this.BillCycleEndDate=endDate;
	}
	
	
	public long getBillCycleID() {
		return BillCycleID;
	}
	public void setBillCycleID(long billCycleID) {
		BillCycleID = billCycleID;
	}
	public String getBillCycleStartDate() {
		return BillCycleStartDate;
	}
	public void setBillCycleStartDate(String billCycleStartDate) {
		BillCycleStartDate = billCycleStartDate;
	}
	public String getBillCycleEndDate() {
		return BillCycleEndDate;
	}
	public void setBillCycleEndDate(String billCycleEndDate) {
		BillCycleEndDate = billCycleEndDate;
	}
	
	@Override
	public String toString() {
		String ret="BillCycleID["+BillCycleID+"],BillCycleStartDate["+BillCycleStartDate+"],BillCycleEndDate["+BillCycleEndDate+"]";
		return ret;
	}
	
	
}
