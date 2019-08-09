/**
 * 
 */
package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sung
 *
 */
public class RemainResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Status="1";
	private List<RemainResourceDto> RemainResourceDtoList =new ArrayList<RemainResourceDto>();
	// added by tian@10150112 for: 电信网厅改造
	private List<RemainResourceDto> PackageResourceUsageList =new ArrayList<RemainResourceDto>();
	private List<RemainResourceDto> OtherResourceUsageList =new ArrayList<RemainResourceDto>();
	
	private String ErrorCode="";
	private String ErrorMessage="";
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public List<RemainResourceDto> getRemainResourceDtoList() {
		return RemainResourceDtoList;
	}
	public void setRemainResourceDtoList(
			List<RemainResourceDto> remainResourceDtoList) {
		this.RemainResourceDtoList = remainResourceDtoList;
	}
		
	public List<RemainResourceDto> getPackageResourceUsageList() {
		return PackageResourceUsageList;
	}
	public void setPackageResourceUsageList(
			List<RemainResourceDto> packageResourceUsageList) {
		PackageResourceUsageList = packageResourceUsageList;
	}
	public List<RemainResourceDto> getOtherResourceUsageList() {
		return OtherResourceUsageList;
	}
	public void setOtherResourceUsageList(
			List<RemainResourceDto> otherResourceUsageList) {
		OtherResourceUsageList = otherResourceUsageList;
	}
	public String getErrorCode() {
		return ErrorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.ErrorCode = errorCode;
	}
	public String getErrorMessage() {
		return ErrorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "RemainResource [Status=" + Status + ", RemainResourceDtoList="
				+ RemainResourceDtoList + ", PackageResourceUsageList="
				+ PackageResourceUsageList + ", OtherResourceUsageList="
				+ OtherResourceUsageList + ", ErrorCode=" + ErrorCode
				+ ", ErrorMessage=" + ErrorMessage + "]";
	}	
}
