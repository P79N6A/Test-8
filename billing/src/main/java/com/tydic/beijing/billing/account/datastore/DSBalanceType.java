package com.tydic.beijing.billing.account.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSBalanceType {
	private final static Logger LOGGER = Logger.getLogger(DSBalanceType.class);
	private Map<Integer, CodeBilBalanceType> store = null;

	public DSBalanceType() {
	}

	public synchronized void load() throws Exception {
		if (store == null) {
			List<CodeBilBalanceType> bts = S.get(CodeBilBalanceType.class)
					.query(Condition.empty());
			if (bts == null) {
				LOGGER.error("TABLE[CODE_BIL_BALANCE_TYPE] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[CODE_BIL_BALANCE_TYPE] Shouldn't Be Empty!");
			}
			store = new HashMap<Integer, CodeBilBalanceType>();
			for (CodeBilBalanceType bt : bts) {
				store.put(bt.getBalance_type_id(), bt);
			}
		}
	}

	public synchronized CodeBilBalanceType get(int balanceTypeId) {
		return store.get(balanceTypeId);
	}

	public synchronized void refresh() throws BasicException {
		if (store == null) {
			LOGGER.info("Before Refresh CodeBilBalanceType Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh CodeBilBalanceType Size[" + store.size()
					+ "]");
		}		
		Map<Integer, CodeBilBalanceType> mirror = null;
		List<CodeBilBalanceType> bts = S.get(CodeBilBalanceType.class).query(
				Condition.empty());
		if (bts == null) {
			LOGGER.error("TABLE[CODE_BIL_BALANCE_TYPE] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[CODE_BIL_BALANCE_TYPE] Shouldn't Be Empty!");
		}
		mirror = new HashMap<Integer, CodeBilBalanceType>();
		for (CodeBilBalanceType bt : bts) {
			mirror.put(bt.getBalance_type_id(), bt);
		}
		store = mirror;
		LOGGER.info("After Refresh CodeBilBalanceType Size[" + store.size()
				+ "]");
	}
}
