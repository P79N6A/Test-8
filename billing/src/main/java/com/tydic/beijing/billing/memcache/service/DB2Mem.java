package com.tydic.beijing.billing.memcache.service;

import com.tydic.beijing.billing.common.BasicException;

public interface DB2Mem {
	
	public static final int LOAD_FLAG = 1;
	public static final int RELOAD_FLAG = 2;
	public static final int THREAD_LOAD_FLAG = 3;
	
	public void load() throws BasicException;
	public void load(int mod, int partition) throws BasicException;
	public Boolean update(String serviceNumber) throws BasicException;
	public Boolean update(String serviceNumber, String userId) throws BasicException;
}
