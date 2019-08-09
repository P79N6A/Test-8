package com.tydic.beijing.billing.interfacex.service;

public interface BeforeAdjust {
	public void doAdjust() throws InterruptedException;
	public Boolean init(int _mod, int _mod_i);
}
