/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tydic.beijing.billing.dto;

import java.util.List;

/**
 *
 * @author wangshida
 */
public class QuerySubsAcctBalanceResponse extends OuterfResponseBase {
	private static final long serialVersionUID = 1L;
	private Long RemainAmount;
    private List<AmountDto> AmountDtoList;
    private Long OweAmount;
    private Long FreezeAmount;

    public QuerySubsAcctBalanceResponse() {
        super();
    }

    public Long getRemainAmount() {
        return RemainAmount;
    }

    public void setRemainAmount(Long RemainAmount) {
        this.RemainAmount = RemainAmount;
    }

    public List<AmountDto> getAmountDtoList() {
        return AmountDtoList;
    }

    public void setAmountDtoList(List<AmountDto> AmountDtoList) {
        this.AmountDtoList = AmountDtoList;
    }

    public Long getOweAmount() {
        return OweAmount;
    }

    public void setOweAmount(Long OweAmount) {
        this.OweAmount = OweAmount;
    }

    public Long getFreezeAmount() {
        return FreezeAmount;
    }

    public void setFreezeAmount(Long FreezeAmount) {
        this.FreezeAmount = FreezeAmount;
    }

    @Override
    public String toString() {
        return "Resp{" + "Status=" + Status + ", RemainAmount=" + RemainAmount + ", AmountDtoList=" + AmountDtoList + ", OweAmount=" + OweAmount + ", FreezeAmount=" + FreezeAmount + ", ErrorCode=" + ErrorCode + ", ErrorMessage=" + ErrorMessage + '}';
    }
}
