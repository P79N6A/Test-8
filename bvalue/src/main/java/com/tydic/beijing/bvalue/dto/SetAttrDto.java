package com.tydic.beijing.bvalue.dto;

public class SetAttrDto {

	private String mode;
	private String attrCode;
	private String attrValue;
	private String effDate;
	private String expDate;
	
	public SetAttrDto(){}
	
	public SetAttrDto(String mode,String code,String value,String eff,String exp){
		this.mode=mode;
		this.attrCode=code;
		this.attrValue=value;
		this.effDate=eff;
		this.expDate=exp;
	}
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getAttrCode() {
		return attrCode;
	}
	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}
	public String getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	public String getEffDate() {
		return effDate;
	}
	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	
	
	
}
