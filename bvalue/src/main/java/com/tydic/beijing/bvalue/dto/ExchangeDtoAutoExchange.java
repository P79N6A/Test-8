package com.tydic.beijing.bvalue.dto;

import java.io.Serializable;
import java.util.List;

public class ExchangeDtoAutoExchange implements Serializable {

	private static final long serialVersionUID = 1L;

	private String operationType;
	private String exchageID;
	private String bValueExchangeMode;
	private String topBValue;
	private String resourceExchangeMode;
	private List<ResourceDto> resourceDtoList;
	private String effDate;
	private String expDate;

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getExchageID() {
		return exchageID;
	}

	public void setExchageID(String exchageID) {
		this.exchageID = exchageID;
	}

	public String getbValueExchangeMode() {
		return bValueExchangeMode;
	}

	public void setbValueExchangeMode(String bValueExchangeMode) {
		this.bValueExchangeMode = bValueExchangeMode;
	}

	public String getTopBValue() {
		return topBValue;
	}

	public void setTopBValue(String topBValue) {
		this.topBValue = topBValue;
	}

	public String getResourceExchangeMode() {
		return resourceExchangeMode;
	}

	public void setResourceExchangeMode(String resourceExchangeMode) {
		this.resourceExchangeMode = resourceExchangeMode;
	}

	public List<ResourceDto> getResourceDtoList() {
		return resourceDtoList;
	}

	public void setResourceDtoList(List<ResourceDto> resourceDtoList) {
		this.resourceDtoList = resourceDtoList;
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

	@Override
	public String toString() {
		return "ExchangeDtoAutoExchange [operationType=" + operationType + ", exchageId="
				+ exchageID + ", bValueExchangeMode=" + bValueExchangeMode + ", topBValue="
				+ topBValue + ", resourceExchangeMode=" + resourceExchangeMode
				+ ", resourceDtoList=" + resourceDtoList + ", effDate=" + effDate + ", expDate="
				+ expDate + "]";
	}

}
