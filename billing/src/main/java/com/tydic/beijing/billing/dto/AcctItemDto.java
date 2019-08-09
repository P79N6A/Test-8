package com.tydic.beijing.billing.dto;

public class AcctItemDto implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Integer BillCycleID;
	private Long Charge;
	private String GLName;
	private String ParentGLName;
	
	public AcctItemDto(Integer BillCycleID,Long Charge,String GLName,String ParentGLName){
		this.BillCycleID=BillCycleID;
		this.Charge=Charge;
		this.GLName=GLName;
		this.ParentGLName=ParentGLName;
	}

	public Integer getBillCycleID() {
		return BillCycleID;
	}

	public void setBillCycleID(Integer billCycleID) {
		BillCycleID = billCycleID;
	}

	public Long getCharge() {
		return Charge;
	}

	public void setCharge(Long charge) {
		Charge = charge;
	}

	public String getGLName() {
		return GLName;
	}

	public void setGLName(String gLName) {
		GLName = gLName;
	}

	public String getParentGLName() {
		return ParentGLName;
	}

	public void setParentGLName(String parentGLName) {
		ParentGLName = parentGLName;
	}
}
