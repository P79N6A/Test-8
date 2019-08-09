package com.tydic.beijing.billing.account.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeBilPayBalanceCode;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class DSPayBalanceCode {

	private final static Logger LOGGER = Logger
			.getLogger(DSPayBalanceCode.class);
	private List<CodeBilPayBalanceCode> store = null;

	public DSPayBalanceCode() {
	}

	public synchronized void load() throws Exception {
		if (store == null) {
			store = S.get(CodeBilPayBalanceCode.class).query(Condition.empty());
			if (store == null) {
				LOGGER.error("TABLE[CODE_BIL_PAY_BALANCE_CODE] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[CODE_BIL_PAY_BALANCE_CODE] Shouldn't Be Empty!");
			}
		}
	}

	public synchronized List<CodeBilPayBalanceCode> get(int payBalanceCode) {
		List<CodeBilPayBalanceCode> pbcs = new ArrayList<CodeBilPayBalanceCode>();
		for (CodeBilPayBalanceCode pbc : store) {
			if (pbc.getPaybalance_code() == payBalanceCode) {
				pbcs.add(pbc);
			}
		}
		return pbcs;
	}

	public synchronized void refresh() throws BasicException {
		if (store == null) {
			LOGGER.info("Before Refresh CodeBilPayBalanceCode Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh CodeBilPayBalanceCode Size[" + store.size()
					+ "]");
		}			
		List<CodeBilPayBalanceCode> mirror = null;
		mirror = S.get(CodeBilPayBalanceCode.class).query(Condition.empty());
		if (mirror == null) {
			LOGGER.error("TABLE[CODE_BIL_PAY_BALANCE_CODE] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[CODE_BIL_PAY_BALANCE_CODE] Shouldn't Be Empty!");
		}
		store = mirror;
		LOGGER.info("After Refresh CodeBilPayBalanceCode Size[" + store.size()
				+ "]");
	}
}
