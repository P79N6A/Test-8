package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.sql.Date;

public class PrivnumMessReceiveHis implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String appkey;
	private String ts;
	private String magdgt;
	private String requestId;
	private String tela;
	private String telx;
	private String telb;
	private String telc;
	private String subid;
	private String callType;
	private String callTime;
	private String callId;
	private String callRecording;
	private String telz;
	private String ringingTime;
	private String startTime;
	private String releaseTime;
	private String releaseDir;
	private String releaseCause;
	private String recordMode;
	private String recordUrl;
	private String callinFinishFlag;
	private String businessFlag;
	private String runStatus;
	private Date videoLoadTime;
	private String videoFileName;

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getMagdgt() {
		return magdgt;
	}

	public void setMagdgt(String magdgt) {
		this.magdgt = magdgt;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTela() {
		return tela;
	}

	public void setTela(String tela) {
		this.tela = tela;
	}

	public String getTelx() {
		return telx;
	}

	public void setTelx(String telx) {
		this.telx = telx;
	}

	public String getTelb() {
		return telb;
	}

	public void setTelb(String telb) {
		this.telb = telb;
	}

	public String getTelc() {
		return telc;
	}

	public void setTelc(String telc) {
		this.telc = telc;
	}

	public String getSubid() {
		return subid;
	}

	public void setSubid(String subid) {
		this.subid = subid;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getCallRecording() {
		return callRecording;
	}

	public void setCallRecording(String callRecording) {
		this.callRecording = callRecording;
	}

	public String getTelz() {
		return telz;
	}

	public void setTelz(String telz) {
		this.telz = telz;
	}

	public String getRingingTime() {
		return ringingTime;
	}

	public void setRingingTime(String ringingTime) {
		this.ringingTime = ringingTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getReleaseDir() {
		return releaseDir;
	}

	public void setReleaseDir(String releaseDir) {
		this.releaseDir = releaseDir;
	}

	public String getReleaseCause() {
		return releaseCause;
	}

	public void setReleaseCause(String releaseCause) {
		this.releaseCause = releaseCause;
	}

	public String getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(String recordMode) {
		this.recordMode = recordMode;
	}

	public String getRecordUrl() {
		return recordUrl;
	}

	public void setRecordUrl(String recordUrl) {
		this.recordUrl = recordUrl;
	}

	public String getCallinFinishFlag() {
		return callinFinishFlag;
	}

	public void setCallinFinishFlag(String callinFinishFlag) {
		this.callinFinishFlag = callinFinishFlag;
	}

	public String getBusinessFlag() {
		return businessFlag;
	}

	public void setBusinessFlag(String businessFlag) {
		this.businessFlag = businessFlag;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public Date getVideoLoadTime() {
		return videoLoadTime;
	}

	public void setVideoLoadTime(Date videoLoadTime) {
		this.videoLoadTime = videoLoadTime;
	}

	public String getVideoFileName() {
		return videoFileName;
	}

	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}

	@Override
	public String toString() {
		return "PrivnumMessReceiveHis{" +
				"appkey='" + appkey + '\'' +
				", ts='" + ts + '\'' +
				", magdgt='" + magdgt + '\'' +
				", requestId='" + requestId + '\'' +
				", tela='" + tela + '\'' +
				", telx='" + telx + '\'' +
				", telb='" + telb + '\'' +
				", telc='" + telc + '\'' +
				", subid='" + subid + '\'' +
				", callType='" + callType + '\'' +
				", callTime='" + callTime + '\'' +
				", callId='" + callId + '\'' +
				", callRecording='" + callRecording + '\'' +
				", telz='" + telz + '\'' +
				", ringingTime='" + ringingTime + '\'' +
				", startTime='" + startTime + '\'' +
				", releaseTime='" + releaseTime + '\'' +
				", releaseDir='" + releaseDir + '\'' +
				", releaseCause='" + releaseCause + '\'' +
				", recordMode='" + recordMode + '\'' +
				", recordUrl='" + recordUrl + '\'' +
				", callinFinishFlag='" + callinFinishFlag + '\'' +
				", businessFlag='" + businessFlag + '\'' +
				", runStatus='" + runStatus + '\'' +
				", videoLoadTime=" + videoLoadTime +
				", videoFileName='" + videoFileName + '\'' +
				'}';
	}
}
