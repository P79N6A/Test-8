package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleOfrTariffRelation;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSRuleOfrTariffRelation {
	
	private Logger LOGGER = Logger.getLogger(DSRuleOfrTariffRelation.class);
	
	private List<RuleOfrTariffRelation> lstore = null;
	
	public synchronized void load() throws BasicException{
		if(lstore == null){
			lstore = new ArrayList<RuleOfrTariffRelation>();
			try {
				lstore = S.get(RuleOfrTariffRelation.class).query(Condition.build("getAll"));
				if(lstore == null || lstore.isEmpty()){
					LOGGER.error("TABLE[RULE_OFR_TARIFF_RELATION] Shouldn't Be Empty!");
					throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
							"TABLE[RULE_OFR_TARIFF_RELATION] Shouldn't Be Empty!");
				}
			} catch (Exception e) {
				throw new BasicException(ErrorCode.ERR_GET_RULE_OFR_TARIFF_RELATION,ErrorCode.ERR_RULE_OFR_TARIFF_RELATION_MSG);
			}

		}
	}

	public synchronized List<RuleOfrTariffRelation> getByOfrAndGroup(String ofr_c_id,
			String State_group) {
		List<RuleOfrTariffRelation> lrotr = new ArrayList<RuleOfrTariffRelation>();
		for(RuleOfrTariffRelation rotr : lstore){
			if(ofr_c_id.equals(rotr.getOfr_c_id()) && State_group.equals(rotr.getState_group())){
				lrotr.add(rotr);
			}
		}
		if(lrotr == null || lrotr.isEmpty()){
			LOGGER.debug("--ofr_c_id:"+ofr_c_id+",state_group:"+State_group+",get ruleOfrTariffRelation is null----");
		}
		
		return lrotr;
	}
	

	public synchronized void refresh() throws BasicException {
		if (lstore == null) {
			LOGGER.info("Before Refresh RULE_OFR_TARIFF_RELATION Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh RULE_OFR_TARIFF_RELATION Size[" + lstore.size() + "]");
		}
		List<RuleOfrTariffRelation> lmirror = null;
		lmirror = S.get(RuleOfrTariffRelation.class).query(Condition.build("getAll"));
		if ((lmirror == null) || (lmirror.isEmpty())) {
			LOGGER.error("TABLE[RuleOfrSplit] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[RuleOfrSplit] Shouldn't Be Empty!");
		}
		
		lstore = lmirror;
		LOGGER.info("After Refresh RULE_OFR_TARIFF_RELATION Size[" + lstore.size() + "]");
	}

}
