package com.tydic.beijing.billing.interfacex.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.dao.BilActRealTimeBillForOracle;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class QueryRealtimeBillOps {

	public InfoUser getUserInfoByDeviceNumber(String deviceNumber) {
		return S.get(InfoUser.class).queryFirst(
				Condition.build("getUserInfoByDeviceNumber").filter(
						"device_number", deviceNumber));
	}

	public PayUserRel getPayInfoByUserId(String userId) {
		return S.get(PayUserRel.class)
				.queryFirst(
						Condition.build("getPayInfoByUserId").filter("user_id",
								userId));
	}

	public List<BilActRealTimeBillForOracle> getRealtimeBills(String userId,
			String payId, String month) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("user_id", userId);
		filter.put("pay_id", payId);
		filter.put("month", month);
		return S.get(BilActRealTimeBillForOracle.class).query(
				Condition.build("getRealtimeBills").filter(filter));
	}
}
