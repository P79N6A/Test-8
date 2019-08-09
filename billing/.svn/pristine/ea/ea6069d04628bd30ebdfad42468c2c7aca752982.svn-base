package com.tydic.beijing.billing.account.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.account.biz.Sum;
import com.tydic.beijing.billing.account.core.SumCharge;
import com.tydic.beijing.billing.account.service.AccountProcess;
import com.tydic.beijing.billing.account.service.SumChargeService;
import com.tydic.beijing.billing.account.type.CodeDefine;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.dao.QAcctProcess;
import com.tydic.beijing.billing.dao.SystemTime;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class SumChargeImpl extends MyApplicationContextUtil implements
		SumChargeService {

	private final static Logger LOGGER = Logger.getLogger(SumChargeImpl.class);
	@Autowired
	private SumCharge sumcharge;
	public int channel_no;
	private AccountProcess accountProcess;
	private Sum sum;
	
	SumChargeImpl() {
		sum = (Sum) mycontext.getBean("Sum");
	}

	public void setAccountProcess(AccountProcess accountProcess) {
		this.accountProcess = accountProcess;
	}

	public SumCharge getSumcharge() {
		return sumcharge;
	}

	public void setSumcharge(SumCharge sumcharge) {
		this.sumcharge = sumcharge;
	}

	public int getChannel_no() {
		return channel_no;
	}

	public void setChannel_no(int channel_no) {
		this.channel_no = channel_no;
	}

	@Override
	public boolean sumCharge() {
		int q_num = 0;
		long q_time = 0;
		int retry = 0;
		Boolean TRY = true;
		// sumcharge = new SumCharge();
		// 获取q表数据，循环调用sum进行用户累账
		long begintime = System.currentTimeMillis();
		List<QAcctProcess> qap = S.get(QAcctProcess.class).query(
				Condition.build("queryByChannelNo").filter("channel_no",
						channel_no));
		for (QAcctProcess q : qap) {
			// q.getxxx()
			// System.out.println(q.getBalance_info());
			if (q_num == 0) {
				q_time = System.currentTimeMillis();
			}
			long runtime1 = System.currentTimeMillis();
			sumcharge.doSum(q);
			long runtime2 = System.currentTimeMillis();

			// 调用dubbo帐务处理服务
			while (TRY) {
				try {
					
					accountProcess.accountProcess(q.getUser_id(),
							sumcharge.getList_baau());
					TRY = false;
				} catch (Exception e) {
					if (retry < 4) {
						retry++;
					} else {
						LOGGER.error("dubbo invoke accountProcess fail " + e.toString());
						// 调用dubbo账务处理服务失败，但是q表中费用已经进行累帐，需要把费用归零，重新处理。
						SystemTime st = S.get(SystemTime.class).queryFirst(
								Condition.build("getTimestamp"));
						QAcctProcess q_tmp = new QAcctProcess();
						q_tmp.setSession_id(q.getSession_id());
						q_tmp.setProcess_tag(CodeDefine.PROCESS_TAG_ERROR);
						q_tmp.setUpdate_time(st.getTime());
						sum.update_QAcctProcessHis(q_tmp);
						
						TRY = false;
					}
				}
			}
			TRY = true;

			long runtime3 = System.currentTimeMillis();
			LOGGER.debug("run doSum time :["
					+ Long.toString(runtime2 - runtime1) + "]");
			LOGGER.debug("run accountProcess time :["
					+ Long.toString(runtime3 - runtime2) + "]");
			LOGGER.debug("run QAcctProcess time :["
					+ Long.toString(runtime3 - runtime1) + "]");
			q_num++;
			if (q_num % 100 == 0) {
				q_num = 0;
				LOGGER.debug("run 100 q time :["
						+ Long.toString(System.currentTimeMillis() - q_time)
						+ "]");
			}
		}
		long endtime = System.currentTimeMillis();
		LOGGER.debug("run " + qap.size() + " time :["
				+ Long.toString(endtime - begintime) + "]");
		/*
		 * for(Iterator<QAcctProcess> i= qap.iterator(); i.hasNext(); ) {
		 * sc.doSum(i); QAcctProcess q = i.next(); }
		 */
		return true;
	}

}
