package com.tydic.beijing.billing.tools.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.datastore.DSAcctItemCode;
import com.tydic.beijing.billing.dao.LogRefreshTrigger;
import com.tydic.beijing.billing.tools.biz.RefreshTriggerOps;
import com.tydic.beijing.billing.tools.service.Demo4Refresh;
import com.tydic.beijing.billing.util.RecordAssembler;

public class Demo4RefreshImpl implements Demo4Refresh {

	@Autowired
	private RefreshTriggerOps ops;
	@Autowired
	private DSAcctItemCode acctItemCodes;

	@Override
	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) {
		String refreshStatus = null;
		String memo = null;
		// refresh 
		try {
			acctItemCodes.load();
			refreshStatus = "OK";
		} catch (Exception e) {
			refreshStatus = "FAIL";
			memo = "Refresh Failed!";
			e.printStackTrace();
		}
		// update log_refresh_trigger
		LogRefreshTrigger lrt = RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
				datastoreName, serviceName, refreshStatus, memo);
		ops.insertLogRefreshTrigger(lrt);
	}

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "demo4refresh.xml" });
		context.start();
		while (true) {
			Thread.sleep(1000000L);
		}
	}
}
