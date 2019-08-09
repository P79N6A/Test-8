package com.tydic.beijing.billing.tools.biz;

import java.util.List;

import com.tydic.beijing.billing.dao.LogRefreshTrigger;
import com.tydic.beijing.billing.dao.RuleRefreshTrigger;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class RefreshTriggerOps {
	public List<RuleRefreshTrigger> getRuleRefreshTriggerActive() {
		return S.get(RuleRefreshTrigger.class).query(
				Condition.build("queryAllActive"));
	}
	
	public void updateRuleRefreshTrigger(RuleRefreshTrigger rrt) {
		S.get(RuleRefreshTrigger.class).update(rrt);
	}
	
	public void insertLogRefreshTrigger(LogRefreshTrigger lrt) {
		S.get(LogRefreshTrigger.class).update(lrt);
	}
}
