package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.RuleSystemSwitch;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSRuleSystemSwitch {
	private static final Logger LOGGER = Logger.getLogger(DSRuleSystemSwitch.class);
	private List<RuleSystemSwitch> lstore = null;
	public DSRuleSystemSwitch(){
		
	}
	public synchronized void load() throws BasicException{
		if(lstore == null){
			lstore = S.get(RuleSystemSwitch.class).query(Condition.build("queryAll"));
			if ((lstore == null) || (lstore.isEmpty())) {
				LOGGER.error("TABLE[RULE_SYSTEM_SWITCH] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[RULE_SYSTEM_SWITCH] Shouldn't Be Empty!");
			}
			
		}
	}
	
	public synchronized List<RuleSystemSwitch> get(){
		List<RuleSystemSwitch> list = new ArrayList<RuleSystemSwitch>();
		for(RuleSystemSwitch rss : lstore){
			if(rss.getDomain_code() == BasicType.RESOURCE_PARA_DOMAIN_CODE && rss.getProgram_name().equals(BasicType.CYCLE_RENT_SWITCH)){
				list.add(rss);
			}
		}
		return list;
	}
	

}
