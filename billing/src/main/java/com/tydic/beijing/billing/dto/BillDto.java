package com.tydic.beijing.billing.dto;

import java.io.Serializable;

public class BillDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private long BillCycleID;
	private long Amount;
	private String BillCycleStartDate ;
	private String BillCycleEndDate;
	
	public BillDto(){}
	
	public BillDto(long cycle,long amount,String startDate,String endDate){
		this.BillCycleID=cycle;
		this.Amount=amount;
		this.BillCycleStartDate=startDate;
		this.BillCycleEndDate=endDate;
	}
	
	public long getBillCycleID() {
		return BillCycleID;
	}
	public void setBillCycleID(long billCycleID) {
		BillCycleID = billCycleID;
	}
	public long getAmount() {
		return Amount;
	}
	public void setAmount(long amount) {
		Amount = amount;
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
		String ret="BillCycleID["+BillCycleID+"],Amount["+Amount+"],BillCycleStartDate["+BillCycleStartDate+"],BillCycleEndDate["+BillCycleEndDate+"]";
		return ret;
	}
	
	
}
