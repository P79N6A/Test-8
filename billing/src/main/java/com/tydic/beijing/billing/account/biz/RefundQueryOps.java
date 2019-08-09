package com.tydic.beijing.billing.account.biz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.datastore.DSBalanceType;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.SystemTime;
import com.tydic.beijing.billing.dto.RefundQueryResult;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class RefundQueryOps {
	private final static Logger LOGGER = Logger.getLogger(RefundQueryOps.class);

	private DSBalanceType balanceTypes;
	private int flag4firstrun = 0;

	public RefundQueryResult queryRefund(String payId) throws Exception {
		if (flag4firstrun == 0) {
			balanceTypes.load();
			flag4firstrun = 1;
		}
		RefundQueryResult result = new RefundQueryResult();
		List<InfoPayBalance> ipbs = S.get(InfoPayBalance.class).query(
				Condition.build("queryByPayId").filter("pay_id", payId));
		SystemTime currentTime = S.get(SystemTime.class).queryFirst(
				Condition.build("getTimestamp"));
		String time = currentTime.getTime().substring(0, 10);
		for (Iterator<InfoPayBalance> iter = ipbs.iterator(); iter.hasNext();) {
			InfoPayBalance ipb = iter.next();
			if ((ipb.getEff_date().toString().compareTo(time) > 0)
					|| (ipb.getExp_date().toString().compareTo(time) < 0)) {
				iter.remove();
			}
		}
		if (LOGGER.isDebugEnabled()) {
			for (InfoPayBalance ipb : ipbs) {
				LOGGER.debug("[ACTIVE BALANCE INFO]" + ipb.toString());
			}
		}

		long amountRefundable = 0L;
		long amountNonRefundable = 0L;
		List<Long> refundableBalanceIds = new ArrayList<Long>();
		List<Integer> refundableBalanceIdPriority = new ArrayList<Integer>();
		List<InfoPayBalance> allInfoPayBalance=new ArrayList<InfoPayBalance>();
		for (InfoPayBalance ipb : ipbs) {
			CodeBilBalanceType cbbt = balanceTypes
					.get(ipb.getBalance_type_id());
			if (cbbt == null) {
				LOGGER.error("BalanceTypeId[" + ipb.getBalance_type_id()
						+ "] NOT FOUND in table[CODE_BIL_BALANCE_TYPE]!");
				throw new BasicException(
						ErrorCode.ERR_BALANCE_TYPE_ID_NOT_FOUND,
						"BalanceTypeId["
								+ ipb.getBalance_type_id()
								+ "] NOT FOUND in table[CODE_BIL_BALANCE_TYPE]!");
			}
			if (cbbt.getUnit_type_id() == BasicType.UNIT_TYPE_MONEY) {
				allInfoPayBalance.add(ipb);
				if (cbbt.getRefund_flag().trim().equals("1")) {
					// amountRefundable += ipb.getBalance();
					amountRefundable += ipb.getReal_balance();
					refundableBalanceIds.add(ipb.getBalance_id());
					refundableBalanceIdPriority.add(cbbt.getPriority());
				} else {
					// amountNonRefundable += ipb.getBalance();
					 amountNonRefundable += ipb.getReal_balance();
				}
			}
		}
		result.setRefundable(amountRefundable);
		result.setNonRefundable(amountNonRefundable);
		sortBalanceIds(refundableBalanceIds, refundableBalanceIdPriority);
		result.setRefundableBalanceIds(refundableBalanceIds);
		result.setAllInfoPayBalance(allInfoPayBalance);
		return result;
	}

	private void sortBalanceIds(List<Long> refundableBalanceIds,
			List<Integer> refundableBalanceIdPriority) {
		if (refundableBalanceIds != null) {
			Long[] ids = new Long[refundableBalanceIds.size()];
			Integer[] priorities = new Integer[refundableBalanceIdPriority
					.size()];
			refundableBalanceIds.toArray(ids);
			refundableBalanceIdPriority.toArray(priorities);
			for (int i = 0; i < priorities.length; i++) {
				for (int j = i; j < priorities.length; j++) {
					if (priorities[i] < priorities[j]) {
						int x = priorities[j];
						priorities[j] = priorities[i];
						priorities[i] = x;
						long y = ids[j];
						ids[j] = ids[i];
						ids[i] = y;
					}
				}
			}
			refundableBalanceIds.clear();
			for (long l : ids) {
				refundableBalanceIds.add(l);
				LOGGER.debug("[SORTED BALANCEID][" + l + "]");
			}
		}
	}

	public DSBalanceType getBalanceTypes() {
		return balanceTypes;
	}

	public void setBalanceTypes(DSBalanceType balanceTypes) {
		this.balanceTypes = balanceTypes;
	}
}
