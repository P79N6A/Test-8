package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

import com.tydic.uda.UdaAnnotationSetKey;

public class LifeServiceAttrForMemcache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String jdnDeviceNumber;
	private List<SynchronizeInfo> synchronizeInfoList;
	
	@Id
	public String getJdnDeviceNumber() {
		return jdnDeviceNumber;
	}
	@UdaAnnotationSetKey
	public void setJdnDeviceNumber(String jdnDeviceNumber) {
		this.jdnDeviceNumber = jdnDeviceNumber;
	}
	public List<SynchronizeInfo> getSynchronizeInfoList() {
		return synchronizeInfoList;
	}
	public void setSynchronizeInfoList(List<SynchronizeInfo> synchronizeInfoList) {
		this.synchronizeInfoList = synchronizeInfoList;
	}
	@Override
	public String toString() {
		return "LifeServiceAttrForMemcache [jdnDeviceNumber=" + jdnDeviceNumber
				+ ", synchronizeInfoList=" + synchronizeInfoList + "]";
	}
	
	
}
