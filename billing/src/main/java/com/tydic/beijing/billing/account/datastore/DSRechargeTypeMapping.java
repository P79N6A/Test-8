package com.tydic.beijing.billing.account.datastore;

import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleRechargeTypeMapping;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSRechargeTypeMapping {
	private final static Logger LOGGER = Logger.getLogger(DSPayItemCode.class);
	private List<RuleRechargeTypeMapping> store = null;

	public DSRechargeTypeMapping() {
	}

	public synchronized void load() throws Exception {
		if (store == null) {
			store = S.get(RuleRechargeTypeMapping.class).query(
					Condition.empty());
			if (store == null) {
				LOGGER.error("TABLE[RULE_RECHARGE_TYPE_MAPPING] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[RULE_RECHARGE_TYPE_MAPPING] Shouldn't Be Empty!");
			}
		}
	}

	public synchronized RuleRechargeTypeMapping get(String rechargeType) {
		for (RuleRechargeTypeMapping rrtm : store) {
			if (rrtm.getRecharge_type().equals(rechargeType)) {
				return rrtm;
			}
		}
		return null;
	}

	public synchronized void refresh() throws BasicException {
		if (store == null) {
			LOGGER.info("Before Refresh RuleRechargeTypeMapping Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh RuleRechargeTypeMapping Size["
					+ store.size() + "]");
		}
		List<RuleRechargeTypeMapping> mirror = null;
		mirror = S.get(RuleRechargeTypeMapping.class).query(Condition.empty());
		if (mirror == null) {
			LOGGER.error("TABLE[RULE_RECHARGE_TYPE_MAPPING] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[RULE_RECHARGE_TYPE_MAPPING] Shouldn't Be Empty!");
		}
		store = mirror;
		LOGGER.info("After Refresh RuleRechargeTypeMapping Size["
				+ store.size() + "]");
	}
}
