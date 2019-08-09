package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleOfrSplit;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSRuleOfrSplit {
	
	private final static Logger LOGGER = Logger.getLogger(DSRuleOfrSplit.class);
	private List<RuleOfrSplit> lstore = null;
	
	
	public synchronized void load() throws BasicException{
		if(lstore == null){
			lstore = new ArrayList<RuleOfrSplit>();
			try {
				lstore = S.get(RuleOfrSplit.class).query(Condition.build("getAll"));

				if(lstore == null || lstore.isEmpty()){
					LOGGER.error("TABLE[RULE_OFR_SPLIT] Shouldn't Be Empty!");
					throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,"TABLE[RULE_OFR_SPLIT] Shouldn't Be Empty!");
				}
			} catch (Exception e) {
				throw new BasicException(ErrorCode.ERR_GET_RULE_OFR_SPLIT,ErrorCode.ERR_RULE_OFR_SPLIT_MSG);
			}
		}
	}

	public synchronized List<RuleOfrSplit> getByProductId(String product_id){
		List<RuleOfrSplit> lofr = new ArrayList<RuleOfrSplit>();
		for(RuleOfrSplit ros : lstore ){
			if(product_id.equals(ros.getProduct_id())){
				lofr.add(ros);
			}
		}
		if(lofr == null || lofr.isEmpty()){
			LOGGER.debug("TABLE[RULE_OFR_SPLIT] product_id [" + product_id + "] not get ofr_c_id");
		}
		return lofr;
	}
	
	public synchronized void refresh() throws BasicException {
		if (lstore == null) {
			LOGGER.info("Before Refresh ruleOfrSplit Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh ruleOfrSplit Size[" + lstore.size() + "]");
		}
		List<RuleOfrSplit> lmirror = null;
		lmirror = S.get(RuleOfrSplit.class).query(Condition.build("getAll"));
		if ((lmirror == null) || (lmirror.isEmpty())) {
			LOGGER.error("TABLE[RuleOfrSplit] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[RuleOfrSplit] Shouldn't Be Empty!");
		}
		
		lstore = lmirror;
		LOGGER.info("After Refresh RuleOfrSplit Size[" + lstore.size() + "]");
	}
	
	

}
