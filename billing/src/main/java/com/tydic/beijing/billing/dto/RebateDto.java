package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * 
 * @author Tian
 *
 */
public class RebateDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private long BillCycleID;
	private String BillCycleName;
	private long Charge;

	public long getBillCycleID() {
		return BillCycleID;
	}

	public void setBillCycleID(long billCycleID) {
		BillCycleID = billCycleID;
	}

	public String getBillCycleName() {
		return BillCycleName;
	}

	public void setBillCycleName(String billCycleName) {
		BillCycleName = billCycleName;
	}

	public long getCharge() {
		return Charge;
	}

	public void setCharge(long charge) {
		Charge = charge;
	}
}
