package com.tydic.beijing.billing.account.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.biz.RechargeAsync2SyncOps;
import com.tydic.beijing.billing.account.datastore.DSAcctMonth;
import com.tydic.beijing.billing.account.service.RechargeAsync2Sync;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoPayBalanceAsync;

public class RechargeAsync2SyncImpl implements RechargeAsync2Sync {
	private final static Logger LOGGER = Logger
			.getLogger(RechargeAsync2SyncImpl.class);

	// private final static int ASYNC_STATE_INIT = 0;
	private final static int ASYNC_STATE_OK = 2;
	private final static int ASYNC_STATE_FAIL = 9;

	private final static int Q_STATE_INIT = 0;
	private final static int Q_STATE_ASYNC_INIT = 10;

	RechargeAsync2SyncOps ops;
	DSAcctMonth acctMonths;

	@Override
	public void process() {
		// check
		try {
			acctMonths.load();
		} catch (Exception e) {
			LOGGER.error("Loading Table[CODE_ACCT_MONTH] Failed!["
					+ e.getMessage() + "]");
			return;
		}
		List<CodeAcctMonth> cams = acctMonths
				.getByUseTags(BasicType.USE_TAG_ACTIVE);
		if ((cams == null) || (cams.isEmpty())) {
			LOGGER.error("CAN NOT FIND ANY Active AcctMonth in Table[CODE_ACCT_MONTH]!");
			return;
		} else {
			for (CodeAcctMonth cam : cams) {
				if (cam.getAct_tag().equals(BasicType.ACT_TAG_OPEN)) {
					LOGGER.error("Opening on progress...Async2Sync disallowed!");
					return;
				}
			}
		}
		while (true) {
			List<InfoPayBalanceAsync> ipbas = ops.scanInfoPayBalanceAsync();
			if ((ipbas == null) || (ipbas.isEmpty())) {
				LOGGER.info("Work Nicely Done! Sponsored By Specialized! Yeah, I am kidding about the second part!!");
				break;
			}
			for (InfoPayBalanceAsync ipba : ipbas) {
				LOGGER.debug("Processing BalanceId[" + ipba.getBalance_id()
						+ "]...");
				// check
				InfoPayBalance ipb = ops.getInfoPayBalanceByBalanceId(ipba
						.getBalance_id());
				if (ipb == null) {
					LOGGER.error("Async2Sync CAN NOT FIND BalanceId["
							+ ipba.getBalance_id() + "]Info!");
					ops.updateInfoPayBalanceAsyncState(ipba, ASYNC_STATE_FAIL);
					continue;
				}
				// process
				InfoPayBalance ipbIncrement = assembleInfoPayBalance(
						ipba.getBalance_id(), ipba.getBalance());
				ops.doAsync2Sync(ipbIncrement, ipba, ASYNC_STATE_OK);
				LOGGER.debug("Processing BalanceId[" + ipba.getBalance_id()
						+ "]...Done!");
			}
		}
		LOGGER.info("Update QRechargeCallback Async State to Sync State...");
		ops.updateQRechargeCallback(Q_STATE_ASYNC_INIT, Q_STATE_INIT);
		LOGGER.info("Update QRechargeCallback Async State to Sync State...Done!");
	}

	private InfoPayBalance assembleInfoPayBalance(long balanceId, long amount) {
		InfoPayBalance ipbIncremental = new InfoPayBalance();
		ipbIncremental.setBalance_id(balanceId);
		ipbIncremental.setBalance(amount);
		ipbIncremental.setReal_balance(amount);
		return ipbIncremental;
	}

	public DSAcctMonth getAcctMonths() {
		return acctMonths;
	}

	public void setAcctMonths(DSAcctMonth acctMonths) {
		this.acctMonths = acctMonths;
	}

	public RechargeAsync2SyncOps getOps() {
		return ops;
	}

	public void setOps(RechargeAsync2SyncOps ops) {
		this.ops = ops;
	}
}
