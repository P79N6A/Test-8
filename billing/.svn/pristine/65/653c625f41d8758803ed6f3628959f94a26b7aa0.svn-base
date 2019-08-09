package com.tydic.beijing.billing.account.thread;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.account.biz.Sum;
import com.tydic.beijing.billing.account.core.SumCharge;
import com.tydic.beijing.billing.account.service.AccountProcess;
import com.tydic.beijing.billing.account.type.CodeDefine;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.dao.QAcctProcess;
import com.tydic.beijing.billing.dao.SystemTime;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class SumThread extends MyApplicationContextUtil implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(SumThread.class);
	@Autowired
	private SumCharge sumcharge;

	public int channel_no;
	public int mod;
	public int mod_i;
	private AccountProcess accountProcess;
	private Sum sum;

	public SumThread() {
		sum = (Sum) mycontext.getBean("Sum");
		sumcharge = (SumCharge) mycontext.getBean("SumCharge");
		accountProcess = (AccountProcess) mycontext.getBean("accountProcess");
	}

	public SumCharge getSumcharge() {
		return sumcharge;
	}

	public int getChannel_no() {
		return channel_no;
	}

	public void setChannel_no(int channel_no) {
		this.channel_no = channel_no;
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

	public void run() {
		int q_num = 0;
		long q_time = 0;
		int retry = 0;
		Boolean TRY = true;
		List<QAcctProcess> qap = null;
		while (true) {
			qap = S.get(QAcctProcess.class).query(
					Condition.build("queryByChannelNo").filter("channel_no", channel_no));
			LOGGER.debug("get size[" + qap.size() + "]");
			for (QAcctProcess q : qap) {
				if( Math.abs(q.getUser_id().hashCode())/10%mod != mod_i ) {
				LOGGER.info("Math.abs(q.getUser_id().hashCode())/10%mod:"+ Math.abs(q.getUser_id().hashCode())/10%mod + " mod_i:" + mod_i);
					continue;
				}
				if (q_num == 0) {
					q_time = System.currentTimeMillis();
				}
				
				long runtime1 = System.currentTimeMillis();
				int re_sum = sumcharge.doSum(q);
				long runtime2 = System.currentTimeMillis();
				if(re_sum < 0) {
					continue;
				}
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
							LOGGER.error("dubbo invoke accountProcess fail" + e.toString());
							// 调用dubbo账务处理服务失败，但是q表中费用已经进行累帐，需要把费用归零，重新处理。
							SystemTime st = S.get(SystemTime.class).queryFirst(
									Condition.build("getTimestamp"));
							QAcctProcess q_tmp = new QAcctProcess();
							q_tmp.setSession_id(q.getSession_id());
							q_tmp.setProcess_tag(CodeDefine.PROCESS_TAG_ERROR);
							q_tmp.setUpdate_time(st.getTime());
							sum.update_QAcctProcessHis(q_tmp);

							TRY = false;
							retry = 0;
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
			qap.clear();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				LOGGER.debug("Sleep 5000 to get QAcctProcess");
			}
			LOGGER.debug("get QAcctProcess again!");
		}
	}

}
