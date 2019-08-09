package com.tydic.beijing.billing.account.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleBilSpePayment;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSSpePayment {
	private final static Logger LOGGER = Logger.getLogger(DSSpePayment.class);
	private List<RuleBilSpePayment> store = null;

	public DSSpePayment() {
	}

	public synchronized void load() throws Exception {
		if (store == null) {
			store = S.get(RuleBilSpePayment.class).query(Condition.empty());
			if (store == null) {
				LOGGER.error("TABLE[RULE_BIL_SPE_PAYMENT] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[RULE_BIL_SPE_PAYMENT] Shouldn't Be Empty!");
			}
		}
	}

	public synchronized List<RuleBilSpePayment> get(int spePaymentId) {
		List<RuleBilSpePayment> bsps = new ArrayList<RuleBilSpePayment>();
		for (RuleBilSpePayment bsp : store) {
			if (bsp.getSpe_payment_id() == spePaymentId) {
				bsps.add(bsp);
			}
		}
		return bsps;
	}

	public synchronized void refresh() throws BasicException {
		if (store == null) {
			LOGGER.info("Before Refresh RuleBilSpePayment Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh RuleBilSpePayment Size[" + store.size()
					+ "]");
		}
		List<RuleBilSpePayment> mirror = null;
		mirror = S.get(RuleBilSpePayment.class).query(Condition.empty());
		if (mirror == null) {
			LOGGER.error("TABLE[RULE_BIL_SPE_PAYMENT] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[RULE_BIL_SPE_PAYMENT] Shouldn't Be Empty!");
		}
		store = mirror;
		LOGGER.info("After Refresh RuleBilSpePayment Size[" + store.size()
				+ "]");
	}
}
