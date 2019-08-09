package com.tydic.beijing.billing.interfacex.service;

import com.tydic.beijing.billing.dto.QueryRealtimeBillResult;

/**
 * 
 * @author Tian
 *
 */
public interface QueryRealtimeBill {
	public QueryRealtimeBillResult query(String deviceNumber);
}
