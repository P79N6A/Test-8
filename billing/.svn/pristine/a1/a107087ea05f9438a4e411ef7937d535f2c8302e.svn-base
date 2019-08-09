package com.tydic.beijing.billing.account.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.biz.Simulate;
import com.tydic.beijing.billing.account.core.Dao2File;
import com.tydic.beijing.billing.account.core.WriteOff;
import com.tydic.beijing.billing.account.service.SimulateWriteOffService;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.LogUsedBalanceLog;
import com.tydic.beijing.billing.dao.SystemTime;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class SimulateWriteOffImpl extends MyApplicationContextUtil implements
		SimulateWriteOffService {
	private final static Logger LOGGER = Logger.getLogger(SimulateWriteOffImpl.class);
	private SystemTime system_time;
	//private String time_stamp;
	private String now;
	private int acctMonth;
	private String partition_num;
	private CodeAcctMonth code_acct_month;
	private List<WriteOffDetail> write_off_detail;
	private List<LogUsedBalanceLog> l_LogUsedBalanceLog;
	
	//@Autowired
	public Simulate simulate;
	/*
	public Simulate getSimulate() {
		return simulate;
	}
	public void setSimulate(Simulate simulate) {
		this.simulate = simulate;
	}
	*/
	
	SimulateWriteOffImpl() {
		write_off_detail = new ArrayList<WriteOffDetail>();
		l_LogUsedBalanceLog = new ArrayList<LogUsedBalanceLog>();
		simulate = (Simulate) mycontext.getBean("Simulate");
	}
	
	@Override
	public boolean doSimulation(String user_id, boolean useMemcached) throws BasicException {
		return doSimulation(user_id, null, null, true);
	}
	
	@Override
	public boolean doSimulation(String user_id, Dao2File d2f_item, Dao2File d2f_log) throws BasicException {
		return doSimulation(user_id, d2f_item, d2f_log, true);
	}

	public boolean doSimulation(String user_id, Dao2File d2f_item, Dao2File d2f_log, boolean useMemcached) throws BasicException {
		if(d2f_item == null || d2f_log == null) {
			throw new BasicException(ErrorCode.ERR_PARAM_CONTENT, "doSimulation Dao2File is null");
		}
		//获得时间戳
		system_time = S.get(SystemTime.class).queryFirst(Condition.build("getTimestamp"));
		//time_stamp = system_time.getTimestamp();
		now = system_time.getTime();
		//提取时间戳之前的预占记录，写入销账明细的同时修改预占记录处理状态
		
		l_LogUsedBalanceLog.clear();
		/*
		l_LogUsedBalanceLog = S.get(LogUsedBalanceLog.class).query(Condition.build("queryByInsertTimestamp").filter("insert_timestamp", time_stamp));
		*/
		//获得账期
		code_acct_month = S.get(CodeAcctMonth.class).queryFirst(Condition.build("queryBySysdate"));
		if(code_acct_month == null) {
			LOGGER.error("CodeAcctMonth error");
			return false;
		}
		acctMonth = code_acct_month.getAcct_month();
		partition_num = code_acct_month.getPartition_no();
		
		//调用writeoff
		
		write_off_detail.clear();
		WriteOff wo = (WriteOff) mycontext.getBean("writeOff");
		try {
			//wo.writeOff(user_id, null, acctMonth, BasicType.WRITE_OFF_CALLER_SIMULATION, write_off_detail);
			//返回值 -1 欠费 触发信控
			wo.writeOff(user_id, null, acctMonth, BasicType.WRITE_OFF_CALLER_MONEY_ONLY, write_off_detail, null, useMemcached);
		} catch (Exception e) {
			LOGGER.error("User ID [" + user_id + "]" + "writeoff error:" + e.toString());
		}
		
		//写销账明细
		if (write_off_detail.isEmpty()) {
			LOGGER.warn("User ID [" + user_id + "]" + "writeoff detail is empty");
			return true;
		}
		try {
			//simulate.update(l_LogUsedBalanceLog, write_off_detail, now, partition_num, list_item, list_log);
			simulate.update(l_LogUsedBalanceLog, write_off_detail, now, partition_num, d2f_item, d2f_log);
		} catch (BasicException e) {
			if(e.getCode() == ErrorCode.RE_WRITEOFF) {
				doSimulation(user_id, d2f_item, d2f_log);
			} else {
				LOGGER.error("User ID [" + user_id + "] " + e.toString());
			}
		} catch (IOException e) {
			LOGGER.error("User ID [" + user_id + "] " + e.toString());
		}
		return true;
	}

}
