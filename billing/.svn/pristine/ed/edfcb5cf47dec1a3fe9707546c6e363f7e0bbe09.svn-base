package com.tydic.beijing.billing.account.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.tydic.beijing.billing.account.biz.AccountProcessOps;
import com.tydic.beijing.billing.account.core.Dao2File;
import com.tydic.beijing.billing.account.service.AccountProcess;
import com.tydic.beijing.billing.account.service.SimulateWriteOffService;
import com.tydic.beijing.billing.account.type.BilActUserRealTimeBill;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.BilActRealTimeBill;
import com.tydic.beijing.billing.dao.LogActPreWriteoff;

public class AccountProcessImpl extends MyApplicationContextUtil implements AccountProcess, InitializingBean {
	
	private final static Logger LOGGER = Logger.getLogger(AccountProcessImpl.class);

	/*
	@Autowired
	private SimulateWriteOffService simulateWriteOff;
	@Autowired
	private AccountProcessOps accountProcessOps;

	public void setSimulateWriteOff(SimulateWriteOffService simulateWriteOff) {
		this.simulateWriteOff = simulateWriteOff;
	}

	public AccountProcessOps getAccountProcessOps() {
		return accountProcessOps;
	}

	public void setAccountProcessOps(AccountProcessOps accountProcessOps) {
		this.accountProcessOps = accountProcessOps;
	}

	public SimulateWriteOffService getSimulateWriteOff() {
		return simulateWriteOff;
	}
	*/
	//Dao队列 用于文件写入
	Dao2File d2f_item;
	Dao2File d2f_log;
	Dao2File d2f_userBill;
	long channel_credit;

	public AccountProcessImpl() {
		d2f_item = new BilActRealTimeBill();
		d2f_log = new LogActPreWriteoff();
		d2f_userBill = new BilActUserRealTimeBill();
	}
	
	@Override
	public void accountProcess(String userId, List<BilActAddUp> baaus) throws Exception {
		LOGGER.info(" List<BilActAddUp> baaus size" + baaus.size());
		SimulateWriteOffService simulateWriteOff = (SimulateWriteOffService) mycontext
				.getBean("SimulateWriteOffImpl");
		AccountProcessOps accountProcessOps = (AccountProcessOps) mycontext
				.getBean("accountProcessOps");
		// TO-DO [账务优惠][帐前调账]blah, blah, blah...
		//修改为全量更新
		// 生成实时用户账单
		try {
			accountProcessOps.createBill(baaus, d2f_userBill);
			//simulateWriteOff.doSimulation(userId, list_item, list_log);
			simulateWriteOff.doSimulation(userId, d2f_item, d2f_log);
		} catch (Exception ex) {
			LOGGER.error("accountProcess error " + ex.toString());
			throw ex;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String str = System.getProperty("CREDIT.CHANNELNO", "000");
		LOGGER.info("CREDIT.CHANNELNO : " + str);
		d2f_item.setChannel_credit(Long.parseLong(str));
		d2f_log.setChannel_credit(Long.parseLong(str));
		d2f_userBill.setChannel_credit(Long.parseLong(str));
		new Thread(d2f_item).start();
		new Thread(d2f_log).start();
		new Thread(d2f_userBill).start();
	}

}
