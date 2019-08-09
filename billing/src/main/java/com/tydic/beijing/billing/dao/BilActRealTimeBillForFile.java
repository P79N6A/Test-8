package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.List;

public class BilActRealTimeBillForFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<BilActRealTimeBill> list;
	public List<BilActRealTimeBill> getList() {
		return list;
	}
	public void setList(List<BilActRealTimeBill> list) {
		this.list = list;
	}

}
