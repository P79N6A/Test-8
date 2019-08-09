package com.tydic.beijing.billing.account.biz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.LogQRefund;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.QRefund;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class RefundOps {
	private final static Logger LOGGER = Logger.getLogger(RefundOps.class);

	private final static String ACCESS_TAG_WITHDRAW = "1";

	public List<QRefund> scanQRefund() {
		return S.get(QRefund.class).query(Condition.empty());
	}

	public void updateQState(QRefund qr, long realRefund, int state) {
		qr.setState(state);
		S.get(QRefund.class).update(qr);
		LogQRefund lqr = new LogQRefund();
		lqr.setLog_serial_no(qr.getSerial_no());
		lqr.setRefund(realRefund);
		lqr.setState(state);
		S.get(LogQRefund.class).update(lqr);
	}

	public long updateBalance4RealRefund(QRefund qr, List<Long> balanceIds,
			long totalRefundAmount, String operateType) {
		long realRefund = totalRefundAmount;
		InfoPayBalance ipb = null;
		Map<String, Object> filters = new HashMap<String, Object>();
		for (long balanceId : balanceIds) {
			if (totalRefundAmount <= 0L) {
				break;
			}
			ipb = S.get(InfoPayBalance.class).get(balanceId);
			if (ipb == null) {
				LOGGER.error("BalanceId[" + balanceId
						+ "] Missing when RefundService recheck!");
				continue;
			}
			long refundAmount = 0L;
			if (ipb.getReal_balance() < totalRefundAmount) {
				refundAmount = ipb.getReal_balance();
			} else {
				refundAmount = totalRefundAmount;
			}
			filters.clear();
			filters.put("balance_id", balanceId);
			filters.put("refund", refundAmount);
			filters.put("exp_date", getLastDayOfCurrentMonth());
			S.get(InfoPayBalance.class).batch(
					Condition.build("updateBalance2Zero").filter(filters), ipb);
			S.get(BilActAccesslog.class)
					.create(assembleBilActAccesslog(qr, ipb, refundAmount,
							operateType, null));
			LOGGER.info("BalanceId[" + balanceId + "] Erased By RefundService!");
			totalRefundAmount -= refundAmount;
		}
		realRefund -= totalRefundAmount;
		return realRefund;
	}

	public void updateBalance4NonRefund(QRefund qr, String operateType) {
		if ((qr.getUser_id() == null) || (qr.getUser_id().trim().equals(""))) {
			LOGGER.error("Q_Refund Record[" + qr.getSerial_no()
					+ "] User_ID is NULL!");
			return;
		}
		List<PayUserRel> purs = S.get(PayUserRel.class).query(
				Condition.build("getByUserIdForRefund").filter("user_id",
						qr.getUser_id()));
		if ((purs == null) || (purs.isEmpty())) {
			LOGGER.error("DeviceNumber[" + qr.getDevice_number()
					+ "] Pay Relation Not Found!");
			return;
		}
		PayUserRel pur = purs.get(0);
		LOGGER.info("Q_Refund Record[" + qr.getSerial_no()
				+ "] Begin Process NonRefund Belong to PayId["
				+ pur.getPay_id() + "]...");
		List<InfoPayBalance> ipbs = S.get(InfoPayBalance.class).query(
				Condition.build("queryBalance4RefundSpecific").filter(
						"pay_id", pur.getPay_id()));
		Map<String, Object> filters = new HashMap<String, Object>();
		for (InfoPayBalance ipb : ipbs) {
			filters.clear();
			filters.put("balance_id", ipb.getBalance_id());
			filters.put("refund", ipb.getReal_balance());
			filters.put("exp_date", getLastDayOfCurrentMonth());
			S.get(InfoPayBalance.class).batch(
					Condition.build("updateBalance2Zero").filter(filters), ipb);
			S.get(BilActAccesslog.class)
					.create(assembleBilActAccesslog(qr, ipb, ipb.getReal_balance(),
							operateType, "撤单/开户返销：不可退账本也清0，置失效"));
			LOGGER.info("BalanceId[" + ipb.getBalance_id() + "] Erased By RefundService[bukaixin]!");
		}
	}

	private String getLastDayOfCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(date);
	}
	
	private BilActAccesslog assembleBilActAccesslog(QRefund qr,
			InfoPayBalance ipb, long refundAmount, String operateType, String refundReason) {
		BilActAccesslog baa = new BilActAccesslog();
		baa.setOperate_id(qr.getSerial_no());
		baa.setOperate_type(operateType);
		baa.setPartition_id(Calendar.getInstance().get(Calendar.MONTH) + 1);
		baa.setPay_id(ipb.getPay_id());
		baa.setBalance_id(ipb.getBalance_id());
		baa.setBalance_type_id(ipb.getBalance_type_id());
		baa.setAccess_tag(ACCESS_TAG_WITHDRAW);
		baa.setMoney(refundAmount);
		baa.setOld_balance(ipb.getBalance());
		baa.setNew_balance(ipb.getBalance() - refundAmount);
		baa.setLocal_net(ipb.getLocal_net());
		baa.setReserve_3(refundReason);
		return baa;
	}
}
