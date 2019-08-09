package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleItemCodeRelation;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;


public class DSRuleItemCodeRelation {
	
	private final static Logger LOGGER = Logger.getLogger(DSRuleItemCodeRelation.class);
	List<RuleItemCodeRelation> lstore = null;

	public void load() throws BasicException {
		if(lstore == null){
			lstore = new ArrayList<RuleItemCodeRelation>();
			try {
				lstore = S.get(RuleItemCodeRelation.class).query(Condition.build("getAll"));

				if(lstore == null || lstore.isEmpty()){
					LOGGER.error("TABLE[RULE_ITEM_CODE_RELATION] Shouldn't Be Empty!");
					throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,"TABLE[RULE_ITEM_CODE_RELATION] Shouldn't Be Empty!");
				}
			} catch (Exception e) {
				throw new BasicException(ErrorCode.ERR_GET_RULE_ITEM_CODE_RELATION,ErrorCode.ERR_RULE_ITEM_CODE_REALTION_MSG);
			}
		}
		
	}

	public List<RuleItemCodeRelation> getRuleItemCodeRelation() {
		return lstore;
	}

}
