package com.tydic.beijing.billing.account.datastore;

import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleUserEventMapping;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class DSUserEventMapping {
	private final static Logger LOGGER = Logger
			.getLogger(DSUserEventMapping.class);
	private List<RuleUserEventMapping> store = null;

	public DSUserEventMapping() {
	}

	public synchronized void load() throws Exception {
		if (store == null) {
			store = S.get(RuleUserEventMapping.class).query(Condition.empty());
			if (store == null) {
				LOGGER.error("TABLE[RULE_USER_EVENT_MAPPING] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[RULE_USER_EVENT_MAPPING] Shouldn't Be Empty!");
			}
		}
	}

	public synchronized RuleUserEventMapping get(String userEventCode,
			String bossTypeDomain) {
		for (RuleUserEventMapping ruem : store) {
			if (ruem.getUser_event_code().trim().equals(userEventCode.trim())) {
				if (ruem.getBoss_type_domain().trim()
						.equals(bossTypeDomain.trim())) {
					return ruem;
				}
			}
		}
		return null;
	}

	public synchronized void refresh() throws BasicException {
		if (store == null) {
			LOGGER.info("Before Refresh RuleUserEventMapping Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh RuleUserEventMapping Size["
					+ store.size() + "]");
		}
		List<RuleUserEventMapping> mirror = null;
		mirror = S.get(RuleUserEventMapping.class).query(Condition.empty());
		if (mirror == null) {
			LOGGER.error("TABLE[RULE_USER_EVENT_MAPPING] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[RULE_USER_EVENT_MAPPING] Shouldn't Be Empty!");
		}
		store = mirror;
		LOGGER.info("After Refresh RuleUserEventMapping Size[" + store.size()
				+ "]");
	}
}
