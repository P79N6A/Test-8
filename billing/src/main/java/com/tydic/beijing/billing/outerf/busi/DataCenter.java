/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.billing.outerf.busi;

import com.tydic.beijing.billing.dao.BilActBill;
import com.tydic.beijing.billing.dao.BilActRealTimeBillForOracle;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CodeActAcctItem;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoPayBalanceAsync;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserReleaseCal;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.TbBilPresentUserInfo;
import com.tydic.beijing.billing.dao.UserPresentHis;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wangshida
 */
public class DataCenter {
	static public InfoUser getInfoUserByDeviceNumber(String device_number) {
		return S.get(InfoUser.class).queryFirst(
				Condition.build("getInfoUserByDeviceNumber").filter(
						"DEVICE_NUMBER", device_number));
	}

	static public PayUserRel getPayUserRelByUserId(String user_id) {
		return S.get(PayUserRel.class).queryFirst(
				Condition.build("getByUserId").filter("USER_ID", user_id));
	}

	static public List<PayUserRel> getPayUserRelList(String user_id) {
		return S.get(PayUserRel.class).query(
				Condition.build("getByUserId").filter("USER_ID", user_id));
	}

	static public List<BilActBill> getBilActBillByUserId(String user_id) {
		return S.get(BilActBill.class).query(
				Condition.build("getByUserId").filter("USER_ID", user_id));
	}

	static public List<TbBilPresentUserInfo> getTbBilPresentUserInfoByUserId(
			String user_id) {
		return S.get(TbBilPresentUserInfo.class).query(
				Condition.build("getByUserId").filter("USER_ID", user_id));
	}

	static public List<RuleParameters> getBaiTiaoBalanceTypeId() {
		return S.get(RuleParameters.class).query(
				Condition.build("getBaiTiaoBalanceTypeId"));
	}

	static public List<CodeBilBalanceType> getCodeBilBalanceTypeAll() {
		return S.get(CodeBilBalanceType.class).query(Condition.build("getAll"));
	}

	static public List<InfoPayBalance> getInfoPayBalanceByPayId(String pay_id) {
		return S.get(InfoPayBalance.class).query(
				Condition.build("getByPayId").filter("PAY_ID", pay_id));
	}

	/*
	 * zhuhz
	 */
	// 出账账单 列表
	static public List<BilActBill> getBilActBillByUserIdList(String user_id) {
		return S.get(BilActBill.class).query(
				Condition.build("getByUserIdList").filter("USER_ID", user_id));
	}

	// 出账账单
	static public BilActBill getBilActBillByUserIdNext(String user_id) {
		return S.get(BilActBill.class).queryFirst(
				Condition.build("getByUserIdNext").filter("USER_ID", user_id));
	}

	static public BilActBill getBilActBillByUserIdNexts(String user_id,
			Integer acct_month) {
		return S.get(BilActBill.class).queryFirst(
				Condition.build("getByUserIdNext").filter("USER_ID", user_id)
						.filter("ACCT_MONTH", acct_month));
	}

	// 账期定义表
	static public CodeAcctMonth getCodeAcctMonthByAcctMonth(Integer acct_month) {
		return S.get(CodeAcctMonth.class).queryFirst(
				Condition.build("getByAcctMonth").filter("Acct_Month",
						acct_month));
	}

	static public CodeAcctMonth getCodeAcctMonthByBillingCycleId(
			long billingCycleId) {
		return S.get(CodeAcctMonth.class).queryFirst(
				Condition.build("getByBillingCycleId").filter(
						"Billing_Cycle_Id", billingCycleId));
	}

	static public CodeAcctMonth getActiveAcctMonth() {
		return S.get(CodeAcctMonth.class).queryFirst(
				Condition.build("getActiveAcctMonth"));
	}

	static public List<BilActRealTimeBillForOracle> getRealtimeBill(
			int acctMonth, String currentMonth, String userId, String payId) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("month", currentMonth);
		filter.put("acct_month", acctMonth);
		filter.put("user_id", userId);
		filter.put("pay_id", payId);
		return S.get(BilActRealTimeBillForOracle.class).query(
				Condition.build("getRealtimeBill").filter(filter));
	}

	static public List<BilActBill> getBilActBillByAcctMonth(String userId,
			int acctMonth) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("USER_ID", userId);
		filters.put("ACCT_MONTH", acctMonth);
		return S.get(BilActBill.class).query(
				Condition.build("queryBills").filter(filters));
	}

	static public List<CodeActAcctItem> getAllItemCode() {
		return S.get(CodeActAcctItem.class).query(
				Condition.build("getAllItemCode"));
	}

	static public UserPresentHis getRebateList(String userId, int acctMonth) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("User_Id", userId);
		filters.put("Acct_Month", acctMonth);
		return S.get(UserPresentHis.class).queryFirst(
				Condition.build("getRebate").filter(filters));
	}

	// 明细账目定义
	static public CodeActAcctItem getCodeActAcctItemByAcctItemCode(
			Integer acct_item_code) {
		return S.get(CodeActAcctItem.class).queryFirst(
				Condition.build("getByAcctItemCode").filter("Acct_Item_Code",
						acct_item_code));
	}

	static public RuleParameters getRuleParameters(Integer acct_item_code) {
		return S.get(RuleParameters.class).queryFirst(
				Condition.build("getParentAcctItemCode").filter("para_char1",
						acct_item_code.toString()));
	}

	static public List<InfoPayBalanceAsync> getInfoPayBalanceAsyncByPayId(
			String payId) {
		return S.get(InfoPayBalanceAsync.class).query(
				Condition.build("getByPayId").filter("pay_id", payId));
	}

	public static List<LifeUserReleaseCal> getLifeUserReleaseCalByUserId(
			String user_id) {
//		RuleParameters ruleParameters=S.get(RuleParameters.class).queryFirst(Condition.build("query9000RuleParameters"));
//		String contracts=ruleParameters.getPara_char1();
		return S.get(LifeUserReleaseCal.class).query(
				Condition.build("getByUserId").filter("USER_ID", user_id));
	}
}
