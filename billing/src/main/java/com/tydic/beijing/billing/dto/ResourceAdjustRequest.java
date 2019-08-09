package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Tian
 *
 */
public class ResourceAdjustRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sessionId;
	private String jdPin;
	private String MSISDN;
	private String channelNo;
	private String requestTime;
	private List<ResourceDto> ResourceDtoList;
	
	private String staffId;
	
	//20141222 jd需求，增加支持短信，增加赠送活动类型，赠送原因
	private String activityType; 
	private String reason;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getJdPin() {
		return jdPin;
	}

	public void setJdPin(String jdPin) {
		this.jdPin = jdPin;
	}

	public String getMSISDN() {
		return MSISDN;
	}

	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public List<ResourceDto> getResourceDtoList() {
		return ResourceDtoList;
	}

	public void setResourceDtoList(List<ResourceDto> resourceDtoList) {
		ResourceDtoList = resourceDtoList;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
}
