package com.tydic.beijing.billing.credit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.credit.common.BasicException;
import com.tydic.beijing.billing.credit.common.ErrorCode;
import com.tydic.beijing.billing.credit.dao.BilActBill4Credit;
import com.tydic.beijing.billing.credit.dao.BilActRealTimeBill4Credit;
import com.tydic.beijing.billing.credit.dao.BilActResourceAddUp;
import com.tydic.beijing.billing.credit.dao.CreditPara;
import com.tydic.beijing.billing.credit.dao.CreditProcPara;
import com.tydic.beijing.billing.credit.dao.CreditSequenceUtils;
import com.tydic.beijing.billing.credit.dao.InfoUser4Credit;
import com.tydic.beijing.billing.credit.dao.LogCreditFlowOvertop;
import com.tydic.beijing.billing.credit.dao.QBlockCredit;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUser;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUserCredit;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * db access tools<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 *
 */
public class DBKit {
	public DBKit() {

	}

	/**
	 * 
	 * getCreditPara: get Credit parameters.<br/>
	 * 
	 * @return key -> value
	 */
	public  Map<String, String> getCreditPara() throws BasicException, Exception {
		Map<String, String> ret = new HashMap<String, String>();

		List<CreditPara> list = S.get(CreditPara.class).query(Condition.empty());
		for (CreditPara c : list) {
			ret.put(c.getName(), c.getValue());
		}
		return ret;
	}

	/**
	 * 
	 * getCreditProcPara:get Credit process para
	 * 
	 * @param procId
	 * @return
	 */
	public  Map<String, String> getCreditProcPara(final String procId) throws BasicException,
			Exception {
		Map<String, String> ret = new HashMap<String, String>();
		// Map<Integer, CreditProcPara> ret = new HashMap<Integer,
		// CreditProcPara>();
		List<CreditProcPara> list = S.get(CreditProcPara.class).query(
				Condition.empty().filter("proc_id", procId));
		for (CreditProcPara c : list) {
			ret.put(c.getName(), c.getValue());
		}
		return ret;

	}

	/**
	 * 
	 * getChargeDate:get current charge date.<br/>
	 * 
	 * @param currDate
	 *            current time
	 * @return
	 * @throws Exception
	 */
	public  CodeAcctMonth getChargeDate() throws BasicException, Exception {
		List<CodeAcctMonth> tmp = S.get(CodeAcctMonth.class).query(Condition.empty());

		if (tmp.isEmpty()) {
			throw new BasicException(ErrorCode.CHARGE_DATE_ERROR, "charge date is null");
		}
		CodeAcctMonth cm = tmp.get(0);
		return cm;
	}

	/**
	 * 
	 * getQBlock:query q_block.<br/>
	 * 
	 * @param mod
	 * @param remainder
	 * @return
	 */
	public  List<QBlockCredit> getQBlock(final String mod, final String remainder)
			throws BasicException, Exception {
		List<QBlockCredit> ret = S.get(QBlockCredit.class).query(
				Condition.empty().filter("mod", mod).filter("remainder", remainder));

		if (ret.isEmpty()) {
			return null;
		}
		return ret;
	}

	/**
	 * 
	 * getQCreditInfoUserCredit:query q_credit_info_user_credit.<br/>
	 * 
	 * @param mod
	 * @param remainder
	 * @return
	 */
	public  List<QCreditInfoUserCredit> getQCreditInfoUserCredit(final String mod,
			final String remainder) throws BasicException, Exception {
		List<QCreditInfoUserCredit> ret = S.get(QCreditInfoUserCredit.class).query(
				Condition.empty().filter("mod", mod).filter("remainder", remainder));

		if (ret.isEmpty()) {
			return null;
		}
		return ret;
	}

	/**
	 * 
	 * getQCreditInfoUser:get q_credit_info_user.<br/>
	 * 
	 * @param mod
	 * @param remainder
	 * @return
	 * @throws Exception
	 */
	public  List<QCreditInfoUser> getQCreditInfoUser(final String mod, final String remainder)
			throws BasicException, Exception {
		List<QCreditInfoUser> ret = S.get(QCreditInfoUser.class).query(
				Condition.empty().filter("mod", mod).filter("remainder", remainder));
		if (ret.isEmpty()) {
			return null;
		}
		return ret;
	}

	/**
	 * 
	 * getBilActBill:query bil_act_bill, user_id=?.<br/>
	 * 
	 * @param userId
	 * @return
	 * @throws BasicException
	 * @throws Exception
	 */
	public  BilActBill4Credit getBilActBill(final String userId) throws BasicException,
			Exception {
		List<BilActBill4Credit> list = S.get(BilActBill4Credit.class).query(
				Condition.empty().filter("user_id", userId));

		if (list.isEmpty()) {
			// throw new BasicException(ErrorCode.BIL_ACT_BILL_ERROR, "user_id:"
			// + userId
			// + " not exists in bil_act_bill");
			return null;
		}
		BilActBill4Credit ret = list.get(0);
		return ret;
	}

	/**
	 * 
	 * getBilActRealTimeBill:query bil_act_readl_time_bill, user_id=?.<br/>
	 * 
	 * @param chargeDate
	 * @param userId
	 * @return
	 * @throws BasicException
	 * @throws Exception
	 */
	public  BilActRealTimeBill4Credit getBilActRealTimeBill(final String chargeDate,
			final String userId) throws BasicException, Exception {
		List<BilActRealTimeBill4Credit> list = S.get(BilActRealTimeBill4Credit.class).query(
				Condition.empty().filter("charge_date", chargeDate).filter("user_id", userId));

		if (list.isEmpty()) {
			// throw new BasicException(ErrorCode.BIL_ACT_REAL_BILL_ERROR,
			// "user_id:" + userId
			// + " not exists in bil_act_real_time_bill");
			return null;
		}
		BilActRealTimeBill4Credit ret = list.get(0);
		return ret;
	}

	private static final String SEQUENCE_NAME = "sequence_name";

	/**
	 * 
	 * getFileSn:get file sn.<br/>
	 * 
	 * @param sequenceName
	 * @return
	 * @throws Exception
	 */
	public  long getFileSn(final String sequenceName) throws Exception {
		// Log.debug("Step in getFileSn(sequenceName=" + sequenceName + ")");
		CreditSequenceUtils s = S.get(CreditSequenceUtils.class).queryFirst(
				Condition.empty().filter(SEQUENCE_NAME, sequenceName));

		return s.getDuckduckgo();
	}

	public  InfoUser4Credit getInfoUser(final String userId) throws Exception {
		List<InfoUser4Credit> list = S.get(InfoUser4Credit.class).query(
				Condition.empty().filter("user_id", userId));
		if (list.isEmpty()) {
			return null;
		}

		InfoUser4Credit ret = list.get(0);
		return ret;
	}

	/**
	 * 
	 * getBilActResourceAddUp:流量封顶，累积量表 .<br/>
	 * 
	 * @param partitonNo
	 * @param userId
	 * @param acctMonth
	 * @return
	 * @throws BasicException
	 * @throws Exception
	 */
	public  BilActResourceAddUp getBilActResourceAddUp(final String partitionNo,
			final String userId, final int acctMonth) throws BasicException, Exception {
		BilActResourceAddUp ret = S.get(BilActResourceAddUp.class).queryFirst(
				Condition.empty().filter("partitionNo", partitionNo).filter("userId", userId)
						.filter("acctMonth", acctMonth));

		return ret;
	}

	public  LogCreditFlowOvertop getLogCreditFlowOvertop(final String userId,
			final int acctMonth) {
		LogCreditFlowOvertop ret = S.get(LogCreditFlowOvertop.class).queryFirst(
				Condition.empty().filter("userId", userId).filter("acctMonth", acctMonth));

		return ret;
	}
	
	public  LogCreditFlowOvertop getLogCreditFlowOvertopforspeed(final String userId,
			final int acctMonth,String speed) {
		LogCreditFlowOvertop ret = S.get(LogCreditFlowOvertop.class).queryFirst(
				Condition.build("getLogCreditFlowOvertopforspeed").filter("userId", userId).filter("acctMonth", acctMonth).filter("speed",speed));

		return ret;
	}

	public static String getSequenceName() {
		return SEQUENCE_NAME;
	}
	
	
	
	public  BilActResourceAddUp queryForTrafficLimit(final String partitionNo,
			final String userId, final int acctMonth) throws BasicException, Exception {
		BilActResourceAddUp ret = S.get(BilActResourceAddUp.class).queryFirst(
				Condition.build("queryForTrafficLimit").filter("partitionNo", partitionNo).filter("userId", userId)
						.filter("acctMonth", acctMonth));

		return ret;
	}
	
	
	public  List<RuleParameters> queryRuleParameters() throws BasicException, Exception {
		
		return S.get(RuleParameters.class).query(Condition.build("queryRuleParameters"));
	}
	
public  InfoUser queryInfoUser(String userId) throws BasicException, Exception {
		
		return S.get(InfoUser.class).queryFirst(Condition.build("queryInfoUser").filter("userId",userId));
	}
	
	
	
	
	
}