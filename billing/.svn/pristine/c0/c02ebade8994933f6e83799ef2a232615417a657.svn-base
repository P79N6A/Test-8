package com.tydic.beijing.billing.memcache.service.impl;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.memcache.service.DB2Mem;

public class LoadThread extends MyApplicationContextUtil implements Runnable{

	private int mod;
	private int partition;
	
	public LoadThread(int _mod, int _partition) {
		mod = _mod;
		partition = _partition;
	}
	
	private final static Logger LOGGER = Logger.getLogger(LoadThread.class);
	@Override
	public void run() {
		//DB2Mem db2mem = new DB2MemImpl();
		DB2Mem db2mem = (DB2Mem) mycontext.getBean("DB2MemImpl");
		try {
			db2mem.load(mod, partition);
		} catch (Exception ex) {
			LOGGER.error(ex.toString());
		}
	}
	
}
