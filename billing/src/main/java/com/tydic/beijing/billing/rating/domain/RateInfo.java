package com.tydic.beijing.billing.rating.domain;

public class RateInfo {
	
	public   long   lnPricingSectionId=0;
	public   long   lnTariffID=0;		//资费标识
	public   int   lnRateUnit=0;		//计费单位步长
	public   String   nRateType="";   //根据MeasureDomain获取
	public   double  dRateValue=0;		//价格,单位为分,精确到厘
	public   long   nBeginTime=0;  //费率段开始的时间,单位为秒.如从第1分钟起B2001=0(秒)，从第4分钟起AoC-Start-Time=180(秒)    
    
	

    	

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

		public String getnRateType() {
			return nRateType;
		}

		public void setnRateType(String nRateType) {
			this.nRateType = nRateType;
		}

		public double getdRateValue() {
			return dRateValue;
		}

		public void setdRateValue(double dRateValue) {
			this.dRateValue = dRateValue;
		}

		public long getnBeginTime() {
			return nBeginTime;
		}

		public void setnBeginTime(long nBeginTime) {
			this.nBeginTime = nBeginTime;
		}
    	
    	
    	
    	
    	
}
