/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.billing.dto;

import java.util.Date;

/**
 *
 * @author wangshida
 */
public class InvoiceDto implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long BillCycleID;
    private Long InvoicableAmount;
    private Long Amount;
    private Date BillCycleStartDate;
    private Date BillCycleEndDate;

    public InvoiceDto() {
    }

    public InvoiceDto(Long BillCycleID, Long InvoicableAmount, Long Amount, Date BillCycleStartDate, Date BillCycleEndDate) {
        this.BillCycleID = BillCycleID;
        this.InvoicableAmount = InvoicableAmount;
        this.Amount = Amount;
        this.BillCycleStartDate = BillCycleStartDate;
        this.BillCycleEndDate = BillCycleEndDate;
    }

    public Long getBillCycleID() {
        return BillCycleID;
    }

    public void setBillCycleID(Long BillCycleID) {
        this.BillCycleID = BillCycleID;
    }

    public Long getInvoicableAmount() {
        return InvoicableAmount;
    }

    public void setInvoicableAmount(Long InvoicableAmount) {
        this.InvoicableAmount = InvoicableAmount;
    }

    public Long getAmount() {
        return Amount;
    }

    public void setAmount(Long Amount) {
        this.Amount = Amount;
    }

    public Date getBillCycleStartDate() {
        return BillCycleStartDate;
    }

    public void setBillCycleStartDate(Date BillCycleStartDate) {
        this.BillCycleStartDate = BillCycleStartDate;
    }

    public Date getBillCycleEndDate() {
        return BillCycleEndDate;
    }

    public void setBillCycleEndDate(Date BillCycleEndDate) {
        this.BillCycleEndDate = BillCycleEndDate;
    }

    @Override
    public String toString() {
        return "InvoiceDto{" + "BillCycleID=" + BillCycleID + ", InvoicableAmount=" + InvoicableAmount + ", Amount=" + Amount + ", BillCycleStartDate=" + BillCycleStartDate + ", BillCycleEndDate=" + BillCycleEndDate + '}';
    }
}
