/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue.dao.mem;

import java.io.Serializable;
import java.util.HashMap;

import javax.persistence.Id;

import com.tydic.beijing.bvalue.dao.InfoPayBalance;

public class InfoPayBalanceMem implements Serializable {
	private static final long serialVersionUID = 1L;

	private String user_id;
	private HashMap<String, InfoPayBalance> infoMap;


	@Id
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public HashMap<String, InfoPayBalance> getInfoMap() {
		return infoMap;
	}

	public void setInfoMap(HashMap<String, InfoPayBalance> infoMap) {
		this.infoMap = infoMap;
	}

	@Override
	public String toString() {
		return "InfoPayBalanceMem [user_id=" + user_id + ", infoMap=" + infoMap + "]";
	}

}
