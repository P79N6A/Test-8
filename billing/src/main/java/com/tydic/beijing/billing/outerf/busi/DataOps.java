package com.tydic.beijing.billing.outerf.busi;

import com.tydic.beijing.billing.dao.LogRefreshTrigger;
import com.tydic.uda.service.S;

public class DataOps {
	public void updateLogRefreshTrigger(LogRefreshTrigger lrt) {
		S.get(LogRefreshTrigger.class).create(lrt);
	}
}
