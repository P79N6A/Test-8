package com.tydic.beijing.billing.dao;


public class QTransferDownload {
	private String callId ;   
	private String caller ;   
	private String origCalled  ;   
	private String called ;   
	private String recordUrl; 
	private String processTime;   
	private String tryTimes;   
	private String errMsg ;
	private int state;
	
	private String partitionId;//add 20160628
	
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
	}
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	
	public String getOrigCalled() {
		return origCalled;
	}
	public void setOrigCalled(String origCalled) {
		this.origCalled = origCalled;
	}
	public String getCalled() {
		return called;
	}
	public void setCalled(String called) {
		this.called = called;
	}
	public String getRecordUrl() {
		return recordUrl;
	}
	public void setRecordUrl(String recordUrl) {
		this.recordUrl = recordUrl;
	}
	public String getProcessTime() {
		return processTime;
	}
	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}
	public String getTryTimes() {
		return tryTimes;
	}
	public void setTryTimes(String tryTimes) {
		this.tryTimes = tryTimes;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	 

	public String toString(){
		return "CDR100Transfer [callId=" + callId + ",caller=" + caller 
				+ ",origCalled=" + origCalled  + ",called=" + called+ ",recordUrl=" + recordUrl
				+ ",processTime=" + processTime + ",tryTimes=" + tryTimes + ",errMsg=" + errMsg + "]";
	}
	public String getPartitionId() {
		return partitionId;
	}
	public void setPartitionId(String partitionId) {
		this.partitionId = partitionId;
	}
	
	


}
