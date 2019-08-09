package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleTariffConfInfo;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSRuleTariffConfInfo {
	
	private Logger LOGGER = Logger.getLogger(DSRuleTariffConfInfo.class);
	private List<RuleTariffConfInfo> lstore = null;
	
	
	public synchronized void load() throws BasicException{
		if(lstore == null){
			lstore = new ArrayList<RuleTariffConfInfo>();
			try {
				lstore = S.get(RuleTariffConfInfo.class).query(Condition.build("getAll"));
				if(lstore == null || lstore.isEmpty()){
					LOGGER.error("TABLE[RULE_TARIFF_CONF_INFO] Shouldn't Be Empty!");
					throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
							"TABLE[RULE_TARIFF_CONF_INFO] Shouldn't Be Empty!");
				}
			} catch (Exception e) {
				throw new BasicException(ErrorCode.ERR_GET_RULE_TARIFF_CONF_INFO,ErrorCode.ERR_RULE_TARIFF_CONF_INFO_MSG);
			}

		}
	}

	public synchronized RuleTariffConfInfo getByTariffId(int tariff_id) {
		for(RuleTariffConfInfo rtci : lstore){
			if(tariff_id == rtci.getTariff_id()){
				return rtci;
			}
		}
		return null;
	}

	public void refresh() throws BasicException {
		if (lstore == null) {
			LOGGER.info("Before Refresh RuleTariffConfInfo Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh RuleTariffConfInfo Size[" + lstore.size() + "]");
		}
		List<RuleTariffConfInfo> lmirror = null;
		lmirror = S.get(RuleTariffConfInfo.class).query(Condition.build("getAll"));
		if ((lmirror == null) || (lmirror.isEmpty())) {
			LOGGER.error("TABLE[RuleTariffConfInfo] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[RuleTariffConfInfo] Shouldn't Be Empty!");
		}
		
		lstore = lmirror;
		LOGGER.info("After Refresh RuleTariffConfInfo Size[" + lstore.size() + "]");
		
	}

	
	

}
