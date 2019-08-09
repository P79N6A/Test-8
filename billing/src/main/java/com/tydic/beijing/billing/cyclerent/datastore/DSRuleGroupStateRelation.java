package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleGroupStateRelation;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSRuleGroupStateRelation {
	
	private Logger LOGGER = Logger.getLogger(DSRuleGroupStateRelation.class);
	
	private List<RuleGroupStateRelation> lstore = null;
	
	public synchronized void load() throws BasicException{
		if(lstore == null){
			lstore = new ArrayList<RuleGroupStateRelation>();

			try {
				lstore = S.get(RuleGroupStateRelation.class).query(Condition.build("getAll"));
				
				if(lstore == null || lstore.isEmpty()){
					LOGGER.error("TABLE[RULE_GROUP_STATE_RELATION] Shouldn't Be Empty!");
					throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
							"TABLE[RULE_GROUP_STATE_RELATION] Shouldn't Be Empty!");
				}
			} catch (Exception e) {
				throw new BasicException(ErrorCode.ERR_GET_RULE_GROUP_STATE_RELATION,ErrorCode.ERR_RULE_GROUP_STATE_RELATION_MSG);
			}

		}
	}

	public synchronized List<RuleGroupStateRelation> getByUserStatus(String user_status) {
		List<RuleGroupStateRelation> lgsr = new ArrayList<RuleGroupStateRelation>();
		for(RuleGroupStateRelation rgsr : lstore){
			if(user_status.equals(rgsr.getUser_state())){
				lgsr.add(rgsr);
			}
		}
		if(lgsr == null || lgsr.isEmpty()){
			LOGGER.debug("TABLE[RULE_GROUP_STATE_RELATION] Shouldn't Be Empty!");
		}
		return lgsr;
	}
	
	public synchronized List<RuleGroupStateRelation> getByGroup(String group) {
		List<RuleGroupStateRelation> lgsr = new ArrayList<RuleGroupStateRelation>();
		for(RuleGroupStateRelation rgsr : lstore){
			if(group.equals(rgsr.getState_group())){
				lgsr.add(rgsr);
			}
		}
		if(lgsr == null || lgsr.isEmpty()){
			LOGGER.debug("TABLE[RULE_GROUP_STATE_RELATION] Shouldn't Be Empty!");
		}
		return lgsr;
	}

	public void refresh() throws BasicException {
		if (lstore == null) {
			LOGGER.info("Before Refresh RuleGroupStateRelation Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh RuleGroupStateRelation Size[" + lstore.size() + "]");
		}
		List<RuleGroupStateRelation> lmirror = null;
		lmirror = S.get(RuleGroupStateRelation.class).query(Condition.build("getAll"));
		if ((lmirror == null) || (lmirror.isEmpty())) {
			LOGGER.error("TABLE[RuleGroupStateRelation] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[RuleGroupStateRelation] Shouldn't Be Empty!");
		}
		
		lstore = lmirror;
		LOGGER.info("After Refresh RuleGroupStateRelation Size[" + lstore.size() + "]");
	}



}
