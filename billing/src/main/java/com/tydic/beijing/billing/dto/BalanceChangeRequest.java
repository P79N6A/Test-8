package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 账本变动请求参数类
 * 
 * @author Tian
 *
 */
public class BalanceChangeRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String payId;
	private String userId;
	private String deviceNumber;
	private String serialNo;
	private String operType;
	private String operChannel;
	private List<BalanceChangeInfo> balanceChangeInfo;
	private String localNet;
	private String operStaff;
	private String operTime;
	private String outerSerialNo;
	private String systemId;

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getOperChannel() {
		return operChannel;
	}

	public void setOperChannel(String operChannel) {
		this.operChannel = operChannel;
	}

	public List<BalanceChangeInfo> getBalanceChangeInfo() {
		return balanceChangeInfo;
	}

	public void setBalanceChangeInfo(List<BalanceChangeInfo> balanceChangeInfo) {
		this.balanceChangeInfo = balanceChangeInfo;
	}

	public String getLocalNet() {
		return localNet;
	}

	public void setLocalNet(String localNet) {
		this.localNet = localNet;
	}

	public String getOperStaff() {
		return operStaff;
	}

	public void setOperStaff(String operStaff) {
		this.operStaff = operStaff;
	}

	public String getOperTime() {
		return operTime;
	}

	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}

	public String getOuterSerialNo() {
		return outerSerialNo;
	}

	public void setOuterSerialNo(String outerSerialNo) {
		this.outerSerialNo = outerSerialNo;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Override
	public String toString() {
		return "BalanceChangeRequest [payId=" + payId + ", userId=" + userId
				+ ", deviceNumber=" + deviceNumber + ", serialNo=" + serialNo
				+ ", operType=" + operType + ", operChannel=" + operChannel
				+ ", balanceChangeInfo=" + balanceChangeInfo + ", localNet="
				+ localNet + ", operStaff=" + operStaff + ", operTime="
				+ operTime + ", outerSerialNo=" + outerSerialNo + ", systemId="
				+ systemId + "]";
	}
}
