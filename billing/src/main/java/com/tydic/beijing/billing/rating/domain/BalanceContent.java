package com.tydic.beijing.billing.rating.domain;

public class BalanceContent {
	private  long lnAmount =0;                    //数量
	private  long lnAcctBalanceId =-1;             //只有按MsgId补款的时候使用，确定补款到对应的账本上
	private  int lnAcctItemTypeId =-1;            //账目类型
	private  int nUnitTypeId =-1;                 //账本单位
	private  int nLatnId =-1;                     //只有按MsgId补款的时候使用
	private  int nIsCredit =0;
	private  int lnLeftMoney =0;                 //只有实扣大于预占时有用，存放实扣-预占
	private  int lnDosage =0;                    //记录批价使用量(在nUnitTypeId为2时有用)
	private  int nMeasureDomain =0;              //表示lnDosage类型


	
	
	public long getLnAmount() {
		return lnAmount;
	}
	public void setLnAmount(long lnAmount) {
		this.lnAmount = lnAmount;
	}
	public long getLnAcctBalanceId() {
		return lnAcctBalanceId;
	}
	public void setLnAcctBalanceId(long lnAcctBalanceId) {
		this.lnAcctBalanceId = lnAcctBalanceId;
	}
	public int getLnAcctItemTypeId() {
		return lnAcctItemTypeId;
	}
	public void setLnAcctItemTypeId(int lnAcctItemTypeId) {
		this.lnAcctItemTypeId = lnAcctItemTypeId;
	}
	public int getnUnitTypeId() {
		return nUnitTypeId;
	}
	public void setnUnitTypeId(int nUnitTypeId) {
		this.nUnitTypeId = nUnitTypeId;
	}
	public int getnLatnId() {
		return nLatnId;
	}
	public void setnLatnId(int nLatnId) {
		this.nLatnId = nLatnId;
	}
	public int getnIsCredit() {
		return nIsCredit;
	}
	public void setnIsCredit(int nIsCredit) {
		this.nIsCredit = nIsCredit;
	}
	public int getLnLeftMoney() {
		return lnLeftMoney;
	}
	public void setLnLeftMoney(int lnLeftMoney) {
		this.lnLeftMoney = lnLeftMoney;
	}
	public int getLnDosage() {
		return lnDosage;
	}
	public void setLnDosage(int lnDosage) {
		this.lnDosage = lnDosage;
	}
	public int getnMeasureDomain() {
		return nMeasureDomain;
	}
	public void setnMeasureDomain(int nMeasureDomain) {
		this.nMeasureDomain = nMeasureDomain;
	}


}
