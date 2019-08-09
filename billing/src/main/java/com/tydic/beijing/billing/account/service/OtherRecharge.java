package com.tydic.beijing.billing.account.service;

import com.tydic.beijing.billing.dto.RechargeInfo;
import com.tydic.beijing.billing.dto.RechargeResult;

public abstract interface OtherRecharge
{
  public abstract RechargeResult otherRecharge(RechargeInfo paramRechargeInfo);
}