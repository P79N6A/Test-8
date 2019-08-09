package com.tydic.beijing.billing.dao;


public class CDR100TransferJDN {
	
	private String callId ;       
	private String cdrType;       
	private String cdrSubType ;   
	private String callingNbr;  
	private String calledNbr  ;      
	private String sessionBeginTime ; 
	private String sessionEndTime;
	private int duration  ; 
	private String receiveTime;
	private String status;
	private String dealTime;
	private String filename;	
	private String partitionId;

	private String SupplierCode;
	private String IntoIVRcall;
	private String TransferCall;
	private String IntoIVRStartTime;
	
	private String CallerStartTime;
	private String CallerRingTime;
	private String CallerAnswerTime;
	private String CallerEndTime;
	private String CalledStartTime;
	private String CalledRingTime;
	private String CalledAnswerTime;
	private String CalledEndTime;
	private String CallerResult;
	private String CalledResult;
	private String EndPart;
	private String RecordUrl;
	private String OtherData;
	private String CallingShowNbr;
	private String CalledShowNbr;
	private String CallErrCode;
	
	
	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getCdrType() {
		return cdrType;
	}

	public void setCdrType(String cdrType) {
		this.cdrType = cdrType;
	}

	public String getCdrSubType() {
		return cdrSubType;
	}

	public void setCdrSubType(String cdrSubType) {
		this.cdrSubType = cdrSubType;
	}

	public String getCallingNbr() {
		return callingNbr;
	}

	public void setCallingNbr(String callingNbr) {
		this.callingNbr = callingNbr;
	}

	public String getCalledNbr() {
		return calledNbr;
	}

	public void setCalledNbr(String calledNbr) {
		this.calledNbr = calledNbr;
	}

	public String getSessionBeginTime() {
		return sessionBeginTime;
	}

	public void setSessionBeginTime(String sessionBeginTime) {
		this.sessionBeginTime = sessionBeginTime;
	}

	public String getSessionEndTime() {
		return sessionEndTime;
	}

	public void setSessionEndTime(String sessionEndTime) {
		this.sessionEndTime = sessionEndTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(String partitionId) {
		this.partitionId = partitionId;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSupplierCode() {
		return SupplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		SupplierCode = supplierCode;
	}

	public String getIntoIVRcall() {
		return IntoIVRcall;
	}

	public void setIntoIVRcall(String intoIVRcall) {
		IntoIVRcall = intoIVRcall;
	}

	public String getTransferCall() {
		return TransferCall;
	}

	public void setTransferCall(String transferCall) {
		TransferCall = transferCall;
	}

	public String getIntoIVRStartTime() {
		return IntoIVRStartTime;
	}

	public void setIntoIVRStartTime(String intoIVRStartTime) {
		IntoIVRStartTime = intoIVRStartTime;
	}

	public String getCallerStartTime() {
		return CallerStartTime;
	}

	public void setCallerStartTime(String callerStartTime) {
		CallerStartTime = callerStartTime;
	}

	public String getCallerRingTime() {
		return CallerRingTime;
	}

	public void setCallerRingTime(String callerRingTime) {
		CallerRingTime = callerRingTime;
	}

	public String getCallerAnswerTime() {
		return CallerAnswerTime;
	}

	public void setCallerAnswerTime(String callerAnswerTime) {
		CallerAnswerTime = callerAnswerTime;
	}

	public String getCallerEndTime() {
		return CallerEndTime;
	}

	public void setCallerEndTime(String callerEndTime) {
		CallerEndTime = callerEndTime;
	}

	public String getCalledStartTime() {
		return CalledStartTime;
	}

	public void setCalledStartTime(String calledStartTime) {
		CalledStartTime = calledStartTime;
	}

	public String getCalledRingTime() {
		return CalledRingTime;
	}

	public void setCalledRingTime(String calledRingTime) {
		CalledRingTime = calledRingTime;
	}

	public String getCalledAnswerTime() {
		return CalledAnswerTime;
	}

	public void setCalledAnswerTime(String calledAnswerTime) {
		CalledAnswerTime = calledAnswerTime;
	}

	public String getCalledEndTime() {
		return CalledEndTime;
	}

	public void setCalledEndTime(String calledEndTime) {
		CalledEndTime = calledEndTime;
	}

	public String getCallerResult() {
		return CallerResult;
	}

	public void setCallerResult(String callerResult) {
		CallerResult = callerResult;
	}

	public String getCalledResult() {
		return CalledResult;
	}

	public void setCalledResult(String calledResult) {
		CalledResult = calledResult;
	}

	public String getEndPart() {
		return EndPart;
	}

	public void setEndPart(String endPart) {
		EndPart = endPart;
	}

	public String getRecordUrl() {
		return RecordUrl;
	}

	public void setRecordUrl(String recordUrl) {
		RecordUrl = recordUrl;
	}

	public String getOtherData() {
		return OtherData;
	}

	public void setOtherData(String otherData) {
		OtherData = otherData;
	}

	
	
	public String getCallingShowNbr() {
		return CallingShowNbr;
	}

	public void setCallingShowNbr(String callingShowNbr) {
		CallingShowNbr = callingShowNbr;
	}

	public String getCalledShowNbr() {
		return CalledShowNbr;
	}

	public void setCalledShowNbr(String calledShowNbr) {
		CalledShowNbr = calledShowNbr;
	}

	public String getCallErrCode() {
		return CallErrCode;
	}

	public void setCallErrCode(String callErrCode) {
		CallErrCode = callErrCode;
	}

	@Override
	public String toString() {
		return "CDR100TransferJDN [callId=" + callId + ", cdrType=" + cdrType + ", cdrSubType=" + cdrSubType
				+ ", callingNbr=" + callingNbr + ", calledNbr=" + calledNbr + ", sessionBeginTime=" + sessionBeginTime
				+ ", sessionEndTime=" + sessionEndTime + ", duration=" + duration + ", receiveTime=" + receiveTime
				+ ", status=" + status + ", dealTime=" + dealTime + ", filename=" + filename + ", partitionId="
				+ partitionId + ", SupplierCode=" + SupplierCode + ", IntoIVRcall=" + IntoIVRcall + ", TransferCall="
				+ TransferCall + ", IntoIVRStartTime=" + IntoIVRStartTime + ", CallerStartTime=" + CallerStartTime
				+ ", CallerRingTime=" + CallerRingTime + ", CallerAnswerTime=" + CallerAnswerTime + ", CallerEndTime="
				+ CallerEndTime + ", CalledStartTime=" + CalledStartTime + ", CalledRingTime=" + CalledRingTime
				+ ", CalledAnswerTime=" + CalledAnswerTime + ", CalledEndTime=" + CalledEndTime + ", CallerResult="
				+ CallerResult + ", CalledResult=" + CalledResult + ", EndPart=" + EndPart + ", RecordUrl=" + RecordUrl
				+ ", OtherData=" + OtherData + ", CallingShowNbr=" + CallingShowNbr + ", CalledShowNbr=" + CalledShowNbr
				+ ", CallErrCode=" + CallErrCode + "]";
	}	
	
}
