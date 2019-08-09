package com.tydic.beijing.billing.account.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.BilActBalanceAddUp;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.EndInfoUser;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.beijing.billing.dao.SystemTime;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class EndQuery {
	public static final List<BilActAddUp> QueryBilActAddUp(String userId,
			int acctMonth, String partitionNo) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.clear();
		filter.put("user_id", userId);
		filter.put("acct_month", acctMonth);
		filter.put("partition_no", partitionNo);
		return S.get(BilActAddUp.class).query(
				Condition.build("getBilActAddUp").filter(filter));
	}
	
	public static final List<BilActBalanceAddUp> QueryBilActBalanceAddUp(String userId,
			int acctMonth, String partitionNo) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.clear();
		filter.put("user_id", userId);
		filter.put("acct_month", acctMonth);
		filter.put("partition_no", partitionNo);
		return S.get(BilActBalanceAddUp.class).query(
				Condition.build("queryBalanceAddUp").filter(filter));
	}
	
	public static final long GetBillingId() {
		Sequences s = S.get(Sequences.class).queryFirst(
				Condition.build("querySeqBillingId"));
		return s.getSeq();
	}
	
	public static final String GetNow() {
		SystemTime s = S.get(SystemTime.class).queryFirst(Condition.build("getTimestamp"));
		return s.getTime();
	}
	
	public static final long GetBalance(long balanceId) {
		InfoPayBalance ipb = S.get(InfoPayBalance.class).get(balanceId);
		return ipb.getBalance();
	}
	
	public static final CodeAcctMonth GetAcctMonth(String use_tag) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("use_tag", use_tag);
		return S.get(CodeAcctMonth.class).queryFirst(Condition.build("queryByUseTag").filter(filter));
	}
	
	public static final long GetOperatorId() {
		Sequences s = S.get(Sequences.class).queryFirst(
				Condition.build("querySeqOperatorId"));
		return s.getSeq();
	}
	
	public static final List<EndInfoUser> GetEndInfoUser(int mod, int partition) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("mod", mod);
		filter.put("partition", partition);
		return S.get(EndInfoUser.class).query(Condition.build("query").filter(filter));
	}
}
