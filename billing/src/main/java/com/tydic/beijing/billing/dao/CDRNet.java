package com.tydic.beijing.billing.dao;

import java.util.Date;

public class CDRNet {

	private String serviceType;
	private Date startTime;
	private Date endTime;
	private long payFlag;
	private String roamingtype;
	private String total_flow;
	private String tariffinfo;
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getRoamingtype() {
		return roamingtype;
	}
	public void setRoamingtype(String roamingtype) {
		this.roamingtype = roamingtype;
	}
	public String getTotal_flow() {
		return total_flow;
	}
	public void setTotal_flow(String total_flow) {
		this.total_flow = total_flow;
	}
	public String getTariffinfo() {
		return tariffinfo;
	}
	public void setTariffinfo(String tariffinfo) {
		this.tariffinfo = tariffinfo;
	}
	public void setPayFlag(long payFlag) {
		this.payFlag = payFlag;
	}
	public long getPayFlag() {
		return payFlag;
	}
	
	
	
	
}
