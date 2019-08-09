package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.dao.RuleProductResourceExt;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSRuleProductResourceExt {
	private final static Logger LOGGER = Logger.getLogger(DSRuleProductResourceExt.class);
	private List<RuleProductResourceExt> lstore = null;
	
	public synchronized void load() throws Exception {
		if (lstore == null) {
			List<RuleProductResourceExt> lrpre = S.get(RuleProductResourceExt.class).query(
					Condition.empty());
			if (lrpre == null) {
				LOGGER.debug("TABLE[RuleProductResourceExt] Shouldn't Be Empty!");
				//throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					//	"TABLE[RuleProductResourceExt] Shouldn't Be Empty!");
			}else{
				LOGGER.debug(lrpre);
			}
		}
	}
	
	public synchronized List<RuleProductResourceExt> getByProductid(String productId) {
		List<RuleProductResourceExt> lrpp = new ArrayList<RuleProductResourceExt>();
		for(RuleProductResourceExt rppe : lstore){
			if(rppe.getProduct_id().equals(productId)){
				lrpp.add(rppe);
			}
		}
		return lrpp;
	}
	
	
	public synchronized void refresh() throws BasicException {
		if (lstore == null) {
			LOGGER.info("Before Refresh RuleProductResourceExt Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh RuleProductResourceExt Size[" + lstore.size() + "]");
		}
		List<RuleProductResourceExt> lmirror = null;
		lmirror = S.get(RuleProductResourceExt.class).query(Condition.build("getAll"));
		if ((lmirror == null) || (lmirror.isEmpty())) {
			LOGGER.debug("TABLE[RuleProductResourceExt]  Empty!");
//			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
//					"TABLE[RuleProductResource] Shouldn't Be Empty!");
		}

		lstore = lmirror;
		LOGGER.info("After Refresh RuleProductResourceExt Size[" + lstore.size() + "]");
	}
	
	
	
	
}
