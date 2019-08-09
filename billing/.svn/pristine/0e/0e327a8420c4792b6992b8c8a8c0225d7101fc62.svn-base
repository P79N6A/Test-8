package com.tydic.beijing.billing.account.service;

import com.tydic.beijing.billing.account.core.Dao2File;
import com.tydic.beijing.billing.common.BasicException;

public interface SimulateWriteOffService {
	public boolean doSimulation(String user_id, boolean useMemcached) throws BasicException;
	public boolean doSimulation(String user_id, Dao2File d2f_item, Dao2File d2f_log) throws BasicException;
	public boolean doSimulation(String user_id, Dao2File d2f_item, Dao2File d2f_log, boolean useMemcached) throws BasicException;

}
