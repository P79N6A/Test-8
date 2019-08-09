package com.tydic.beijing.billing.rating.domain;

public class FreeBalance {
	public   int lnPayPlanId;			//付费计划ID
	public   int lnAcctBalanceId;		//账本ID
	public   int lnBalanceTypeId;		//账本类型
	public   int lnAcctItemTypeId;
	public   int lnFreeBalance;			//可用余额
	public   int lnBalance;				//账本余额
	public   int lnCycleCell;			//账本上限
	public   int lnUpperValue;			//用户上限
	public   int lnSprePaymentId;		//专款专用
	public   int lnUnitTypeId;			//单位类型
	public   int nAcctBalanceLatnId;     //账本归属latn_id
	public   int nIsCredit;
	public   int lnConstBalance;			//账本信息，余额扣除前的
	public String strEffDate;		//生效日期
	public String strExpDate;		//失效日期	
	
	FreeBalance()
	{
		lnPayPlanId         = -1;
		lnAcctBalanceId     = -1;
        lnAcctItemTypeId    = -1;
		lnBalanceTypeId     = -1;
		lnFreeBalance       = -1;
		lnCycleCell         = -1;
		lnUpperValue        = -1;
		lnSprePaymentId     = -1;
		lnUnitTypeId        = -1;
		lnBalance           = -1;
        nAcctBalanceLatnId  = -1;
		nIsCredit           = 0;
		lnConstBalance      = 0;
	}

}
