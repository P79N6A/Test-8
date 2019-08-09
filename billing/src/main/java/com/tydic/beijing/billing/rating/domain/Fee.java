package com.tydic.beijing.billing.rating.domain;

public class Fee {
	
	public long lnTariff;						//计费费用
	public long  lnAcctItemTypeId;				//帐目类型
	public int  nUnitType;
	public long getLnTariff() {
		return lnTariff;
	}
	public void setLnTariff(long lnTariff) {
		this.lnTariff = lnTariff;
	}
	public long getLnAcctItemTypeId() {
		return lnAcctItemTypeId;
	}
	public void setLnAcctItemTypeId(long lnAcctItemTypeId) {
		this.lnAcctItemTypeId = lnAcctItemTypeId;
	}
	public int getnUnitType() {
		return nUnitType;
	}
	public void setnUnitType(int nUnitType) {
		this.nUnitType = nUnitType;
	}	

	
	
}
