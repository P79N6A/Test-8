package com.tydic.beijing.billing.interfacex.biz;

import java.util.List;

import com.tydic.beijing.billing.dao.LogActPay;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class RechargeQueryOps {
	public List<LogActPay> queryRechargeLogBySerialNo(String serialNo) {
		return S.get(LogActPay.class).query(
				Condition.build("queryBySerialNo")
						.filter("serial_no", serialNo));
	}
}
