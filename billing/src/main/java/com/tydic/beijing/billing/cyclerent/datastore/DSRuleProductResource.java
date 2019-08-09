package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleProductResource;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSRuleProductResource {
	private final static Logger LOGGER = Logger.getLogger(DSAcctMonth.class);
	private Map<String, RuleProductResource> mstore = null;
	private List<RuleProductResource> lstore = null;

	public DSRuleProductResource() {
	}

	public synchronized void load() throws Exception {
		if (lstore == null) {
			lstore = S.get(RuleProductResource.class).query(Condition.build("getAll"));
			if ((lstore == null) || (lstore.isEmpty())) {
				LOGGER.error("TABLE[RULE_PRODUCT_RESOURCE] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[RULE_PRODUCT_RESOURCE] Shouldn't Be Empty!");
			}
			mstore = new HashMap<String,RuleProductResource>();
			for (RuleProductResource rpp : lstore) {
				mstore.put(rpp.getProduct_id(), rpp);
			}
		}
	}

	public synchronized RuleProductResource getByProductId(int productId) {
		return mstore.get(productId);
	}
	public synchronized List<RuleProductResource> getByProductid(String productId) {
		List<RuleProductResource> lrpp = new ArrayList<RuleProductResource>();
		for(RuleProductResource rpp : lstore){
			if(rpp.getProduct_id().equals(productId)){
				lrpp.add(rpp);
			}
		}
		return lrpp;
	}


	public synchronized void refresh() throws BasicException {
		if (lstore == null) {
			LOGGER.info("Before Refresh RULE_PRODUCT_RESOURCE Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh RULE_PRODUCT_RESOURCE Size[" + lstore.size() + "]");
		}
		Map<String, RuleProductResource> mmirror = null;
		List<RuleProductResource> lmirror = null;
		lmirror = S.get(RuleProductResource.class).query(Condition.build("getAll"));
		if ((lmirror == null) || (lmirror.isEmpty())) {
			LOGGER.error("TABLE[RuleProductResource] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[RuleProductResource] Shouldn't Be Empty!");
		}
		mmirror = new HashMap<String, RuleProductResource>();
		for (RuleProductResource rpp : lmirror) {
			mmirror.put(rpp.getProduct_id(), rpp);
		}
		lstore = lmirror;
		mstore = mmirror;
		LOGGER.info("After Refresh RuleProductResource Size[" + lstore.size() + "]");
	}
}
