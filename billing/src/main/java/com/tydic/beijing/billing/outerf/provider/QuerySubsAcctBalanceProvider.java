/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.billing.outerf.provider;

import com.tydic.beijing.billing.outerf.api.QuerySubsAcctBalance;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.JDNToNewMsisdn;
import com.tydic.beijing.billing.dto.QuerySubsAcctBalanceRequest;
import com.tydic.beijing.billing.dto.QuerySubsAcctBalanceResponse;

/**
 *
 * @author wangshida
 */
public class QuerySubsAcctBalanceProvider extends ProviderBase implements
		QuerySubsAcctBalance {

	public QuerySubsAcctBalanceProvider() {
		super();
	}

	@Override
	public QuerySubsAcctBalanceResponse querySubsAcctBalance(
			QuerySubsAcctBalanceRequest req) {
		logger.info("querySubsAcctBalance() in...");
		logger.info(String.format("querySubsAcctBalance() req:%s", req));
		long beginTime = System.currentTimeMillis();
		
		//add by wangtao begin
		String msisdn = req.getMSISDN();
		msisdn = JDNToNewMsisdn.jdnToNewMsisdn(msisdn,BasicType.STARTSTR);
		req.setMSISDN(msisdn);
		//add by wangtao end
		
		QuerySubsAcctBalanceResponse resp = (QuerySubsAcctBalanceResponse) busi
				.dispose(req);
		long endTime = System.currentTimeMillis();
		logger.info(String.format("querySubsAcctBalance() resp:%s", resp));
		logger.info("querySubsAcctBalance() done Elapsed time["
				+ (endTime - beginTime) + "ms]");
		return resp;
	}

	@Override
	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) {
		busi.refresh(refreshBatchId, datastoreName, serviceName);
	}

}
