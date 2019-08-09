package com.tydic.beijing.billing.rating.domain;

public class TariffResult {
	private   long   lnPricingSectionId=-1;
	private   long   lnTariffID=-1;		//资费标识
	private   int   lnRateUnit=0;		//计费单位步长
	private   long   lnAcctItemTypeId=0; 
	private   String   nMeasureDomain="";   //
	private   long   nBeginTime=0;  //费率段开始的时间,单位为秒.如从第1分钟起B2001=0(秒)，从第4分钟起AoC-Start-Time=180(秒)
	private   int   nCount=0;                 //批价单位数
	private   int   nUnitTypeId=0;            //
	private   long   lnDosage=0;               //批价使用量
	private   long   lnUnusedDosage=0;         //未批价使用量  应该是批价了但是用户未使用的量吧???zhanghb add
	private   long   lnRatableUnusedDosage=0;  //累积量未批价使用量  应该是批价了但是用户未使用的量吧???zhanghb add
	private   long   lnLastDosage=0;           //B308部分批价使用量
	private   long   lnUnusedLastDosage=0;     //B308部分未批价使用量
	private double  dRateValue=0;		//价格,单位为分,精确到厘
	private double  dFee=0;                   //圆整前的费用  单位厘
	private   int   lnFee=0;                  //圆整后的费用  单位分
	private   double   lnUnusedMoney=0;    //未用费用
	
	
	
	public long getLnPricingSectionId() {
		return lnPricingSectionId;
	}
	public void setLnPricingSectionId(long lnPricingSectionId) {
		this.lnPricingSectionId = lnPricingSectionId;
	}
	public long getLnTariffID() {
		return lnTariffID;
	}
	public void setLnTariffID(long lnTariffID) {
		this.lnTariffID = lnTariffID;
	}
	public int getLnRateUnit() {
		return lnRateUnit;
	}
	public void setLnRateUnit(int lnRateUnit) {
		this.lnRateUnit = lnRateUnit;
	}
	public long getLnAcctItemTypeId() {
		return lnAcctItemTypeId;
	}
	public void setLnAcctItemTypeId(long lnAcctItemTypeId) {
		this.lnAcctItemTypeId = lnAcctItemTypeId;
	}
	public String getnMeasureDomain() {
		return nMeasureDomain;
	}
	public void setnMeasureDomain(String nMeasureDomain) {
		this.nMeasureDomain = nMeasureDomain;
	}
	public long getnBeginTime() {
		return nBeginTime;
	}
	public void setnBeginTime(long nBeginTime) {
		this.nBeginTime = nBeginTime;
	}
	public int getnCount() {
		return nCount;
	}
	public void setnCount(int nCount) {
		this.nCount = nCount;
	}
	public int getnUnitTypeId() {
		return nUnitTypeId;
	}
	public void setnUnitTypeId(int nUnitTypeId) {
		this.nUnitTypeId = nUnitTypeId;
	}
	public long getLnDosage() {
		return lnDosage;
	}
	public void setLnDosage(long lnDosage) {
		this.lnDosage = lnDosage;
	}
	public long getLnUnusedDosage() {
		return lnUnusedDosage;
	}
	public void setLnUnusedDosage(long lnUnusedDosage) {
		this.lnUnusedDosage = lnUnusedDosage;
	}
	public long getLnRatableUnusedDosage() {
		return lnRatableUnusedDosage;
	}
	public void setLnRatableUnusedDosage(long lnRatableUnusedDosage) {
		this.lnRatableUnusedDosage = lnRatableUnusedDosage;
	}
	public long getLnLastDosage() {
		return lnLastDosage;
	}
	public void setLnLastDosage(long lnLastDosage) {
		this.lnLastDosage = lnLastDosage;
	}
	public long getLnUnusedLastDosage() {
		return lnUnusedLastDosage;
	}
	public void setLnUnusedLastDosage(long lnUnusedLastDosage) {
		this.lnUnusedLastDosage = lnUnusedLastDosage;
	}
	public double getdRateValue() {
		return dRateValue;
	}
	public void setdRateValue(double dRateValue) {
		this.dRateValue = dRateValue;
	}
	public double getdFee() {
		return dFee;
	}
	public void setdFee(double dFee) {
		this.dFee = dFee;
	}
	public int getLnFee() {
		return lnFee;
	}
	public void setLnFee(int lnFee) {
		this.lnFee = lnFee;
	}
	public double getLnUnusedMoney() {
		return lnUnusedMoney;
	}
	public void setLnUnusedMoney(double lnUnusedMoney) {
		this.lnUnusedMoney = lnUnusedMoney;
	}

    @Override
    public String toString() {
    	String ret="lnPricingSectionId["+lnPricingSectionId+"],lnTariffID["+lnTariffID+"],lnRateUnit["+lnRateUnit+"],lnAcctItemTypeId["+
    			lnAcctItemTypeId+"],nMeasureDomain["+nMeasureDomain+"],nBeginTime["+nBeginTime+"],nCount["+nCount+"],nUnitTypeId["+
    			nUnitTypeId+"],lnDosage["+lnDosage+"],lnUnusedDosage["+lnUnusedDosage+"],lnRatableUnusedDosage["+lnRatableUnusedDosage+
    			"],lnLastDosage["+lnLastDosage+"],lnUnusedLastDosage["+lnUnusedLastDosage+"],dRateValue["+dRateValue+"],dFee["+
    			dFee+"],lnFee["+lnFee+"],lnUnusedMoney["+lnUnusedMoney+"]";
    	return ret;
    }

}
