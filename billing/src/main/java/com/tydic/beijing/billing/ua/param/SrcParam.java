/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua.param;

import java.util.HashMap;

import com.tydic.beijing.billing.ua.dao.UaSrcFile;
import com.tydic.beijing.billing.ua.dao.UaSrcRecord;

/**
 * TODO(用几句话描述这个类)<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class SrcParam {
	private UaSrcFile uaSrcFile;
	private HashMap<Integer, UaSrcRecord> uaSrcRecordMap;

	public UaSrcFile getUaSrcFile() {
		return uaSrcFile;
	}

	public void setUaSrcFile(UaSrcFile uaSrcFile) {
		this.uaSrcFile = uaSrcFile;
	}

	public HashMap<Integer, UaSrcRecord> getUaSrcRecordMap() {
		return uaSrcRecordMap;
	}

	public void setUaSrcRecordMap(HashMap<Integer, UaSrcRecord> uaSrcRecordList) {
		this.uaSrcRecordMap = uaSrcRecordList;
	}

}