/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua.param;

/**
 * TODO(用几句话描述这个类)<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class ProcessParam {
	private String fileType;
	private int recordSerial;
	private StringBuffer ruleProcess = new StringBuffer(4096);

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getRecordSerial() {
		return recordSerial;
	}

	public void setRecordSerial(int recordSerial) {
		this.recordSerial = recordSerial;
	}

	public String getRuleProcess() {
		return ruleProcess.toString();
	}

	public void append(String s) {
		ruleProcess.append(s);
	}

	public void append(char s) {
		ruleProcess.append(s);
	}

	@Override
	public String toString() {
		return "ProcessParam [fileType=" + fileType + ", recordSerial=" + recordSerial
				+ ", ruleProcess=" + ruleProcess.toString() + "]";
	}

}
