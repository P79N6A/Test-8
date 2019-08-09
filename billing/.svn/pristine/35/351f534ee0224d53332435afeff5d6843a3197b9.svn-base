package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSCodeAcctMonth {
	private final static Logger LOGGER = Logger.getLogger(DSCodeAcctMonth.class);
	private Map<Integer, CodeAcctMonth> store = null;

	public DSCodeAcctMonth() {
	}

	public synchronized void load() throws Exception {
		if (store == null) {
			List<CodeAcctMonth> cams = S.get(CodeAcctMonth.class).query(
					Condition.build("queryBySysdate"));
			if (cams == null || cams.isEmpty()) {
				LOGGER.error("TABLE[CODE_ACCT_MONTH] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[CODE_ACCT_MONTH] Shouldn't Be Empty!");
			}
			store = new HashMap<Integer, CodeAcctMonth>();
			for (CodeAcctMonth cam : cams) {
				store.put(cam.getAcct_month(), cam);
			}
		}
	}

	public synchronized CodeAcctMonth get(int acctMonth) {
		return store.get(acctMonth);
	}

	public synchronized void refresh() throws BasicException {
		if (store == null) {
			LOGGER.info("Before Refresh CodeAcctMonth Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh CodeAcctMonth Size[" + store.size()
					+ "]");
		}
		Map<Integer, CodeAcctMonth> mirror = null;
		List<CodeAcctMonth> cams = S.get(CodeAcctMonth.class).query(
				Condition.build("queryBySysdate"));
		if (cams == null || cams.isEmpty()) {
			LOGGER.error("TABLE[CODE_ACCT_MONTH] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[CODE_ACCT_MONTH] Shouldn't Be Empty!");
		}
		mirror = new HashMap<Integer, CodeAcctMonth>();
		for (CodeAcctMonth cam : cams) {
			mirror.put(cam.getAcct_month(), cam);
		}
		store = mirror;
		LOGGER.info("After Refresh CodeAcctMonth Size[" + store.size() + "]");
	}
}
