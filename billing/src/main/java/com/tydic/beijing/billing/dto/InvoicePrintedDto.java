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
public class InvoicePrintedDto implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long BillCycleID;
    private Long Amount;
    private Date BillCycleStartDate;
    private Date BillCycleEndDate;
    private Date PrintDate;

    public InvoicePrintedDto() {
    }

    public InvoicePrintedDto(Long BillCycleID, Long Amount, 
            Date BillCycleStartDate, Date BillCycleEndDate, Date PrintDate) {
        this.BillCycleID = BillCycleID;
        this.Amount = Amount;
        this.BillCycleStartDate = BillCycleStartDate;
        this.BillCycleEndDate = BillCycleEndDate;
        this.PrintDate = PrintDate;
    }

    public Long getBillCycleID() {
        return BillCycleID;
    }

    public void setBillCycleID(Long BillCycleID) {
        this.BillCycleID = BillCycleID;
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

    public Date getPrintDate() {
        return PrintDate;
    }

    public void setPrintDate(Date PrintDate) {
        this.PrintDate = PrintDate;
    }

    @Override
    public String toString() {
        return "InvoicePrintedDto{" + "BillCycleID=" + BillCycleID + ", Amount=" + Amount + ", BillCycleStartDate=" + BillCycleStartDate + ", BillCycleEndDate=" + BillCycleEndDate + ", PrintDate=" + PrintDate + '}';
    }
    
}
