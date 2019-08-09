package com.tydic.beijing.billing.account.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.biz.AccountProcessOps;
import com.tydic.beijing.billing.account.biz.End;
import com.tydic.beijing.billing.account.biz.EndQuery;
import com.tydic.beijing.billing.account.core.WriteOff;
import com.tydic.beijing.billing.account.service.MonthEndWriteOff;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.BilActBalanceAddUp;
import com.tydic.beijing.billing.dao.CodeAcctMonth;

public class MonthEndWriteOffImpl extends MyApplicationContextUtil implements MonthEndWriteOff{
	private final static Logger LOGGER = Logger.getLogger(MonthEndWriteOffImpl.class);
	
	private CodeAcctMonth acctMonth;
	@Override
	public boolean doMonthEndWriteOff(String user_id) {
		LOGGER.debug("do userId:" + user_id);
		//获得账期
		if(acctMonth == null) {
			synchronized (this) {
				acctMonth = EndQuery.GetAcctMonth(BasicType.ACT_TAG_MONTHEND);
				if(acctMonth == null) {
					LOGGER.error("Code Acct Month ERROR has no use_tag = 2");
					return false;
				}
			}
		}
		
		LOGGER.debug("=======doMonthEndWriteOff begin=======");
		AccountProcessOps accountProcessOps = (AccountProcessOps) mycontext
				.getBean("accountProcessOps");
		WriteOff wo = (WriteOff) mycontext.getBean("writeOff");
		End end = (End) mycontext.getBean("end");
		List<WriteOffDetail> write_off_detail = new ArrayList<WriteOffDetail>();
		
		//bil_act_add_up中获取用户全量费用
		List<BilActAddUp> list_Baau = EndQuery.QueryBilActAddUp(user_id, acctMonth.getAcct_month(), acctMonth.getPartition_no());
		if(list_Baau.isEmpty() || list_Baau == null) {
			LOGGER.info("user:" + user_id + " has no BilActAddUp fee bill");
		}
		//加载到memcached供销账函数
		//公用账务处理（账前）
		try {
			accountProcessOps.createBill(list_Baau);
		} catch (Exception e) {
			LOGGER.error("accountProcessOps.createBill ERROR " + e.toString());
			return false;
		}
		//调用销账函数
		try {
			wo.writeOff(user_id, null, acctMonth.getAcct_month(), BasicType.WRITE_OFF_CALLER_MONTHEND, write_off_detail, null, true);
		} catch (Exception e) {
			LOGGER.error("wo.writeOff ERROR " + e.toString());
			return false;
		}
		//获取资源直扣数据
		List<BilActBalanceAddUp> list_Babau = EndQuery.QueryBilActBalanceAddUp(user_id, acctMonth.getAcct_month(), acctMonth.getPartition_no());
		if(list_Babau.isEmpty() || list_Baau == null) {
			LOGGER.info("user:" + user_id + " has no BilActBalanceAddUp fee bill");
		}
		//根据销账明细写表
		String now = EndQuery.GetNow();
		if(now == null) {
			LOGGER.error("get Systime error");
			return false;
		}
		end.update(write_off_detail, list_Babau, now, acctMonth.getPartition_no());

		LOGGER.debug("=======doMonthEndWriteOff end=======");
		return true;
	}

}
