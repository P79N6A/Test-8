/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua.param;

import java.util.LinkedHashMap;
import java.util.List;

import com.tydic.beijing.billing.ua.dao.UaDstFile;
import com.tydic.beijing.billing.ua.dao.UaDstRecord;

/**
 * TODO(用几句话描述这个类)<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class DstParam {

	private List<UaDstFile> dstFileList = null;
	private LinkedHashMap<Integer, List<UaDstRecord>> dstFileMap = null;

	public List<UaDstFile> getDstFileList() {
		return dstFileList;
	}

	public void setDstFileList(List<UaDstFile> dstFileList) {
		this.dstFileList = dstFileList;
	}

	public LinkedHashMap<Integer, List<UaDstRecord>> getDstFileMap() {
		return dstFileMap;
	}

	public void setDstFileMap(LinkedHashMap<Integer, List<UaDstRecord>> dstFileMap) {
		this.dstFileMap = dstFileMap;
	}

}
