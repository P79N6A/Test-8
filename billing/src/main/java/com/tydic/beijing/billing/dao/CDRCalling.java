package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.Date;

public class CDRCalling implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long rownum;
	private String ServiceType;
	private Date startTime;
	private Date endTime;
	private String callingparty;
	private String calledparty;
	private String forwardingflag;
	private String fowardingnumber;
	private long callduration;
	private String roamingType;
	private long payFlag;
	private String call_type;
	private String tariffinfo;
	private String longdistancetype;
	private String callingpartyvisitedcity;
    private String callId;
    private String recordUrl;


 

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
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

    public String getCallingparty() {
        return callingparty;
    }

    public void setCallingparty(String callingparty) {
        this.callingparty = callingparty;
    }

    public String getCalledparty() {
        return calledparty;
    }

    public void setCalledparty(String calledparty) {
        this.calledparty = calledparty;
    }

    public String getForwardingflag() {
        return forwardingflag;
    }

    public void setForwardingflag(String forwardingflag) {
        this.forwardingflag = forwardingflag;
    }

 
	public String getFowardingnumber() {
		return fowardingnumber;
	}
	public void setFowardingnumber(String fowardingnumber) {
		this.fowardingnumber = fowardingnumber;
	}
	public long getCallduration() {
		return callduration;
	}
	public void setCallduration(long callduration) {
		this.callduration = callduration;
	}
	public String getRoamingType() {
		return roamingType;
	}
	public void setRoamingType(String roamingType) {
		this.roamingType = roamingType;
	}
	public String getTariffinfo() {
		return tariffinfo;
	}
	public void setTariffinfo(String tariffinfo) {
		this.tariffinfo = tariffinfo;
	}
	public long getRownum() {
		return rownum;
	}
	public void setRownum(long rownum) {
		this.rownum = rownum;
	}
	public void setPayFlag(long payFlag) {
		this.payFlag = payFlag;
	}
	public long getPayFlag() {
		return payFlag;
	}
	public String getCall_type() {
		return call_type;
	}
	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}
	public String getLongdistancetype() {
		return longdistancetype;
	}
	public void setLongdistancetype(String longdistancetype) {
		this.longdistancetype = longdistancetype;
	}
	public String getCallingpartyvisitedcity() {
		return callingpartyvisitedcity;
	}
	public void setCallingpartyvisitedcity(String callingpartyvisitedcity) {
		this.callingpartyvisitedcity = callingpartyvisitedcity;
	}
    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    @Override
    public String toString() {
        return "CDRCalling{" +
                "rownum=" + rownum +
                ", ServiceType='" + ServiceType + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", callingparty='" + callingparty + '\'' +
                ", calledparty='" + calledparty + '\'' +
                ", forwardingflag='" + forwardingflag + '\'' +
                ", fowardingnumber='" + fowardingnumber + '\'' +
                ", callduration=" + callduration +
                ", roamingType='" + roamingType + '\'' +
                ", payFlag=" + payFlag +
                ", call_type='" + call_type + '\'' +
                ", tariffinfo='" + tariffinfo + '\'' +
                ", longdistancetype='" + longdistancetype + '\'' +
                ", callingpartyvisitedcity='" + callingpartyvisitedcity + '\'' +
                ", callId='" + callId + '\'' +
                ", recordUrl='" + recordUrl + '\'' +
                '}';
    }
}
