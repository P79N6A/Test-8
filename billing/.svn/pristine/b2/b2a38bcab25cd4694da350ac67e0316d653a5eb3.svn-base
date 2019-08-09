package com.tydic.beijing.billing.rating.service;

import java.util.List;

import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.rating.domain.RateData;

public interface BalanceDeduct {
	
	/**
	 * 销账
	 * @param rateData
	 * @param operationType 1预占 2扣费
	 * @return -1失败 0成功 1余额不足，需反算
	 * @throws Exception
	 */
	public int deductBalance(RateData rateData,int operationType,List<WriteOffDetail> listDetail) throws Exception;
	
	public int getDeductBalanceInfo(RateData rateData,List<WriteOffDetail> writeOffDetail) throws Exception ;

}
