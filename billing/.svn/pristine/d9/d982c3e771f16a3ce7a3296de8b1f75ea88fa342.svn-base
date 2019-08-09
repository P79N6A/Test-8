package com.tydic.beijing.billing.account.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.icu.util.Calendar;
import com.tydic.beijing.billing.account.core.WriteOff;
import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.BilActBill;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.LogActWriteOffLog;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class HistoryWriteOff {
	private final static Logger LOGGER = Logger
			.getLogger(HistoryWriteOff.class);

	private final static int OWE_TAG_OWE = 0;
	private final static int OWE_TAG_NONE = 1;
	@Autowired
	private WriteOff writeOff;
	private List<WriteOffDetail> writeOffDetail;

	public HistoryWriteOff() {
		writeOffDetail = new ArrayList<WriteOffDetail>();
	}

	private InfoPayBalance assembleInfoPayBalance(long balanceId, long canWriteOffBalance, long canWriteOffRealBalance) {
		InfoPayBalance ipbIncremental = new InfoPayBalance();
		ipbIncremental.setBalance_id(balanceId);
		ipbIncremental.setBalance(-canWriteOffBalance);
		ipbIncremental.setReal_balance(-canWriteOffRealBalance);
		return ipbIncremental;
	}

	private BilActBill assembleBilActBill(WriteOffDetail wod, int oweTag) {
		BilActBill babIncremental = new BilActBill();
		babIncremental.setUser_id(wod.getUser_Id());
		babIncremental.setPay_id(wod.getPay_Id());
		babIncremental.setAcct_item_code(wod.getAcct_item_code());
		babIncremental.setAcct_month(wod.getAcct_month());
		babIncremental.setWrite_off_fee(wod.getWriteoff_fee());
		babIncremental.setOwe_fee(-wod.getWriteoff_fee());
		babIncremental.setOwe_tag(oweTag);
		babIncremental.setUpdate_time(QueryInfo.getDBSystemTimeIssue()
				.getTime());
		babIncremental.setInvoice_fee(wod.getInvoice_fee());
		return babIncremental;
	}

	private LogActWriteOffLog assembleLogActWriteOffLog(WriteOffDetail wod,
			String serialNo, String writeoffSn) {
		LogActWriteOffLog lawo = new LogActWriteOffLog();
		lawo.setWriteoff_log_id(writeoffSn);
		lawo.setPay_charge_id(serialNo);
		lawo.setUser_id(wod.getUser_Id());
		lawo.setPay_id(wod.getPay_Id());
		lawo.setAcct_month(wod.getAcct_month());
		lawo.setBilling_id(wod.getBillingId());
		lawo.setAcct_item_code(wod.getAcct_item_code());
		lawo.setFee(wod.getFee());
		lawo.setBalance_id(wod.getBalance_id());
		lawo.setBalance_type_id(wod.getBalance_type_id());
		lawo.setWriteoff_fee(wod.getWriteoff_fee());
		lawo.setOld_fee(-1L);
		lawo.setNew_fee(-1L);
		lawo.setOld_ownfee(wod.getBefore_fee());
		lawo.setNew_ownfee(wod.getAfter_fee());
		lawo.setOld_balance(wod.getBefore_balance());
		lawo.setNew_balance(wod.getAfter_balance());
		lawo.setWriteoff_time(QueryInfo.getDBSystemTimeIssue().getTime());
		return lawo;
	}

	private BilActAccesslog assembleBilActAccesslog(WriteOffDetail wod,
			String serialNo, String writeoffSn) {
		BilActAccesslog baal = new BilActAccesslog();
		baal.setOperate_id(writeoffSn);
		baal.setOperate_type("2");
		baal.setPartition_id(Calendar.getInstance().get(Calendar.MONTH) + 1);
		baal.setPay_id(wod.getPay_Id());
		baal.setBalance_id(wod.getBalance_id());
		baal.setBalance_type_id(wod.getBalance_type_id());
		baal.setAccess_tag("1");
		baal.setMoney(wod.getWriteoff_fee());
		baal.setOld_balance(wod.getBefore_balance());
		baal.setNew_balance(wod.getAfter_balance());
		baal.setLocal_net(null);
		return baal;
	}

	public void process(String serialNo, String userId, boolean useMemcached)
			throws Exception {
		reset();
		writeOff.writeOff(userId, null, 0, BasicType.WRITE_OFF_CALLER_RECHARGE,
				writeOffDetail, null, useMemcached);
		for (WriteOffDetail wod : writeOffDetail) {
			// 增量更新余额表
			long canWriteOffBalance = 0L;
			long canWriteOffRealBalance = 0L;
			InfoPayBalance ipbOriginal = S.get(InfoPayBalance.class).get(
					wod.getBalance_id());
			if (ipbOriginal == null) {
				LOGGER.error("Recheck BalanceId[" + wod.getBalance_id()
						+ "] Can NOT FOUND!");
				throw new BasicException(-1, "Can NOT Find BalanceId["
						+ wod.getBalance_id() + "]");
			} else {
				canWriteOffBalance = Math.min(ipbOriginal.getBalance(),
						wod.getWriteoff_fee());
				canWriteOffRealBalance = Math.min(
						ipbOriginal.getReal_balance(), wod.getWriteoff_fee());
				LOGGER.debug("After Calc WriteOff_Fee[" + wod.getWriteoff_fee()
						+ "] canWriteOffBalance[" + canWriteOffBalance
						+ "] canWriteOffRealBalance[" + canWriteOffRealBalance
						+ "]");
			}
			InfoPayBalance ipbIncremental = assembleInfoPayBalance(
					wod.getBalance_id(), canWriteOffBalance, canWriteOffRealBalance);
			S.get(InfoPayBalance.class).batch(
					Condition.build("update4Recharge").filter("balance_id",
							ipbIncremental.getBalance_id()), ipbIncremental);
			// 增量更新账单表
			BilActBill babIncremental = null;
			if (wod.getAfter_fee() <= 0L) {
				babIncremental = assembleBilActBill(wod, OWE_TAG_NONE);
			} else {
				babIncremental = assembleBilActBill(wod, OWE_TAG_OWE);
			}
			Map<String, Object> filters = new HashMap<String, Object>();
			filters.put("user_id", wod.getUser_Id());
			filters.put("pay_id", wod.getPay_Id());
			filters.put("acct_item_code", wod.getAcct_item_code());
			filters.put("acct_month", wod.getAcct_month());
			S.get(BilActBill.class).batch(
					Condition.build("update4Recharge").filter(filters),
					babIncremental);
			String writeoffSn = UUID.randomUUID().toString();
			// 记录缴费销账日志
			LogActWriteOffLog lawo = assembleLogActWriteOffLog(wod, serialNo,
					writeoffSn);
			S.get(LogActWriteOffLog.class).create(lawo);
			// 记录存取款记录表
			BilActAccesslog baal = assembleBilActAccesslog(wod, serialNo,
					writeoffSn);
			S.get(BilActAccesslog.class).create(baal);
		}
	}

	private void reset() {
		writeOffDetail.clear();
	}
}
