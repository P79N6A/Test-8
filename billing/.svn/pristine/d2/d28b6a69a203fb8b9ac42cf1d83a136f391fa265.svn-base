package com.tydic.beijing.billing.interfacex.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.service.AccountProcess;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.QBeforeAdjust;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.BeforeAdjust;

public class BeforeAdjustImpl implements BeforeAdjust, Runnable {
	private final static Logger LOGGER = Logger
			.getLogger(BeforeAdjustImpl.class);
	private long interval;
	private Boolean run;
	private int mod;
	private int mod_i;
	private Map<Integer, CodeAcctMonth> acctMonth;
	private DbTool db;
	private AccountProcess accountProcess;

	@Override
	public Boolean init(int _mod, int _mod_i) {
		this.setRun(true);
		this.setMod(_mod);
		this.setMod_i(_mod_i);
		acctMonth = db.queryCodeAcctMonth();
		if (acctMonth == null || acctMonth.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public void doAdjust() throws InterruptedException {
		List<QBeforeAdjust> list = new ArrayList<QBeforeAdjust>();
		Map<String, Object> filter = new HashMap<String, Object>();
		List<BilActAddUp> baaus = null;
		CodeAcctMonth codeAcctMonth = null;
		String partition_no = null;
		int acct_month = 0;
		String user_id = null;
		String sys_date = null;
		while (run) {
			// 查询q_before_adjust
			list = db.getQBeforeAdjust();
			for (QBeforeAdjust q : list) {
				sys_date = db.getSysDate();
				// 获取累帐数据
				acct_month = q.getAcct_month();
				user_id = q.getUser_id();
				if (Math.abs(user_id.hashCode()) % mod != mod_i) {
					LOGGER.info("Math.abs(user_id.hashCode())%mod:"
							+ Math.abs(user_id.hashCode()) % mod + " mod_i:"
							+ mod_i);
					continue;
				}
				codeAcctMonth = acctMonth.get(Integer.valueOf(acct_month));
				if(codeAcctMonth == null) {
					LOGGER.error("acct_month:" + acct_month + "is a error acct month");
					q.setStatus(BasicType.BEFORE_FAIL);
					q.setUpdate_date(sys_date);
					db.updateQBeforeAdjust(q);
					continue;
				}
				if (codeAcctMonth.getAct_tag().equals(BasicType.USE_TAG_ACTIVE)) {
					LOGGER.error("before adjust Serial_no:" + q.getSerial_no()
							+ " acct_month:" + q.getAcct_month()
							+ " is not active!");
					q.setStatus(BasicType.BEFORE_FAIL);
					q.setUpdate_date(sys_date);
					db.updateQBeforeAdjust(q);
					continue;
				}
				partition_no = codeAcctMonth.getPartition_no();
				filter.clear();
				filter.put("acct_month", acct_month);
				filter.put("partition_no", partition_no);
				filter.put("user_id", user_id);
				baaus = db.queryBilActAddUp(filter);
				if(baaus == null || baaus.isEmpty()) {
					continue;
				}
				try {
					accountProcess.accountProcess(user_id, baaus);
				} catch (Exception e) {
					LOGGER.error("invoke accountProcess error " + e.toString());
					q.setStatus(BasicType.BEFORE_FAIL);
					q.setUpdate_date(sys_date);
					db.updateQBeforeAdjust(q);
				}
				q.setStatus(BasicType.BEFORE_OK);
				q.setUpdate_date(sys_date);
				db.updateQBeforeAdjust(q);
			}
			Thread.sleep(1000 * interval);
		}
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public Boolean getRun() {
		return run;
	}

	public void setRun(Boolean run) {
		this.run = run;
	}

	public int getMod() {
		return mod;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	public int getMod_i() {
		return mod_i;
	}

	public void setMod_i(int mod_i) {
		this.mod_i = mod_i;
	}

	public DbTool getDb() {
		return db;
	}

	public void setDb(DbTool db) {
		this.db = db;
	}

	public Map<Integer, CodeAcctMonth> getAcctMonth() {
		return acctMonth;
	}

	public void setAcctMonth(Map<Integer, CodeAcctMonth> acctMonth) {
		this.acctMonth = acctMonth;
	}

	public AccountProcess getAccountProcess() {
		return accountProcess;
	}

	public void setAccountProcess(AccountProcess accountProcess) {
		this.accountProcess = accountProcess;
	}

	@Override
	public void run() {
		try {
			doAdjust();
		} catch (InterruptedException e) {
			LOGGER.error("before adjust error:" + e.toString());
			System.exit(-1);
		}
	}
}
