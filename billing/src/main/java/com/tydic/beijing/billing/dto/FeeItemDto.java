/**
 * 
 */
package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * @author sung
 *
 */
public class FeeItemDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long FeeType;
	private long Charge;
	
	public FeeItemDto(long feeType,long charge){
		this.FeeType=feeType;
		this.Charge=charge;
	}
	public long getFeeType() {
		return FeeType;
	}
	public void setFeeType(long feeType) {
		this.FeeType = feeType;
	}
	public long getCharge() {
		return Charge;
	}
	public void setCharge(long charge) {
		this.Charge = charge;
	}
	
	@Override
	public String toString() {
		String ret="FeeType["+FeeType+"],Charge["+Charge+"]";
		return ret;
	}
}
