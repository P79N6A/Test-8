package com.tydic.beijing.billing.account.service.impl;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;

public class CallBackLoadThread extends MyApplicationContextUtil implements Runnable {
	private int mod;
	private int partition;
	public CallBackLoadThread(int _mod,int _partition) {
		// TODO Auto-generated constructor stub
		this.mod=_mod;
		this.partition=_partition;
	}
	private final static Logger LOGGER = Logger.getLogger(CallBackLoadThread.class);
	@Override
	public void run() {
		// TODO Auto-generated method stub
		RechargeCallbackImpl reCallback=(RechargeCallbackImpl)mycontext.getBean("rechargeCallback");
		try {
			reCallback.process(mod,partition);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e.toString());
		}
		
	}

}
