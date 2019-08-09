package com.tydic.beijing.billing.account.service;

import com.tydic.beijing.billing.dto.RechargeInfo;
import com.tydic.beijing.billing.dto.RechargeResult;

/**
 * 充值缴费接口类
 * 
 * @author Tian
 *
 */
public interface Recharge {
	public RechargeResult recharge(RechargeInfo info);

	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName);
	//add by hanjianqiang JD-1944 145号码充值
	public RechargeResult rechargeOutIntf(RechargeInfo info);
}
