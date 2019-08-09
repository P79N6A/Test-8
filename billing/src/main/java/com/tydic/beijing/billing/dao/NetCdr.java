package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class NetCdr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long totalDataFlow ;

	public long getTotalDataFlow() {
		return totalDataFlow;
	}

	public void setTotalDataFlow(long totalDataFlow) {
		this.totalDataFlow = totalDataFlow;
	}
	
	
}
