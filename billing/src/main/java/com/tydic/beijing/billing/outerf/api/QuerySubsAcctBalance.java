/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.billing.outerf.api;

import com.tydic.beijing.billing.dto.QuerySubsAcctBalanceRequest;
import com.tydic.beijing.billing.dto.QuerySubsAcctBalanceResponse;

/**
 *
 * @author wangshida
 */
public interface QuerySubsAcctBalance {
	public QuerySubsAcctBalanceResponse querySubsAcctBalance(
			QuerySubsAcctBalanceRequest req);

	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName);
}
