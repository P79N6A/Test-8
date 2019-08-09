package com.tydic.beijing.billing.account.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.biz.End;
import com.tydic.beijing.billing.account.biz.EndQuery;
import com.tydic.beijing.billing.account.service.GetMonthEndUser;
import com.tydic.beijing.billing.account.service.MonthEndWriteOff;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.EndInfoUser;

public class GetMonthEndUserImpl extends MyApplicationContextUtil implements
		GetMonthEndUser, Runnable {
	private final static Logger LOGGER = Logger
			.getLogger(GetMonthEndUserImpl.class);

	private MonthEndWriteOff monthendWriteoff;
	private End end;
	private int mod;
	private int partition;

	public MonthEndWriteOff getMonthendWriteoff() {
		return monthendWriteoff;
	}

	public void setMonthendWriteoff(MonthEndWriteOff monthendWriteoff) {
		this.monthendWriteoff = monthendWriteoff;
	}

	@Override
	public void run() {
		starMonthEnd();
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public End getEnd() {
		return end;
	}

	public void setEnd(End end) {
		this.end = end;
	}

	@Override
	public void starMonthEnd() {
		String userId = null;
		long doNum = 0;
		List<EndInfoUser> endList = null;
		Boolean RUN = true;
		while (RUN) {
			endList = EndQuery.GetEndInfoUser(mod, partition);
			if (endList == null || endList.isEmpty()) {
				RUN = false;
				continue;
			}
			for (EndInfoUser user : endList) {
				userId = user.getUser_id();
				LOGGER.debug("do user :" + userId);
				long time1 = System.currentTimeMillis();
				user.setEnd_status(BasicType.END_STATUS_ING);
				end.modifyEndStatus(user);
				try {
					monthendWriteoff.doMonthEndWriteOff(userId);
				} catch (Exception e) {
					user.setEnd_status(BasicType.END_STATUS_ERR);
					end.modifyEndStatus(user);
					LOGGER.error(e.toString());
					e.printStackTrace();
					System.exit(-1);
				}
				user.setEnd_status(BasicType.END_STATUS_ED);
				end.modifyEndStatus(user);
				if (doNum++ % 500 == 0) {
					LOGGER.info("=========month end do num [" + doNum
							+ "]=========");
				}
				long time2 = System.currentTimeMillis();
				LOGGER.debug("run monthendWriteoff.doMonthEndWriteOff(userId) time: "
						+ (time2 - time1));
			}
		}
		LOGGER.info("=========month end do num [" + doNum + "]=========");
	}
}
