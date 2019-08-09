package com.tydic.beijing.billing.rating.domain;

public class Ratable {
	private   int nRatableType=-1;                 //B062  累计量类型
	private  long lnValue=-1;                     //B063  本次累计数值---int改为long
	private  long lnRatableValue=-1;               //B064  累计后数值
	private  long lnOwnerID=-1;
	private  String strOwnerType="";
	private   int  nOwnerLatnId=-1;
	private  String strAcctDate="";               //当前累积量帐期
	private  String strRatableCode="";        //B061  累计量代码 基础累积量

    void init()
    {
        nRatableType = -1;
        lnValue = -1;
        lnRatableValue = -1;
        lnOwnerID = -1;
        strOwnerType = "";
        nOwnerLatnId = -1;
        strAcctDate = "-1";
        strRatableCode = "-1";
    }

	public int getnRatableType() {
		return nRatableType;
	}

	public void setnRatableType(int nRatableType) {
		this.nRatableType = nRatableType;
	}

	public long getLnValue() {
		return lnValue;
	}

	public void setLnValue(long lnValue) {
		this.lnValue = lnValue;
	}

	public long getLnRatableValue() {
		return lnRatableValue;
	}

	public void setLnRatableValue(long lnRatableValue) {
		this.lnRatableValue = lnRatableValue;
	}

	public long getLnOwnerID() {
		return lnOwnerID;
	}

	public void setLnOwnerID(long lnOwnerID) {
		this.lnOwnerID = lnOwnerID;
	}

	public String getStrOwnerType() {
		return strOwnerType;
	}

	public void setStrOwnerType(String strOwnerType) {
		this.strOwnerType = strOwnerType;
	}

	public int getnOwnerLatnId() {
		return nOwnerLatnId;
	}

	public void setnOwnerLatnId(int nOwnerLatnId) {
		this.nOwnerLatnId = nOwnerLatnId;
	}

	public String getStrAcctDate() {
		return strAcctDate;
	}

	public void setStrAcctDate(String strAcctDate) {
		this.strAcctDate = strAcctDate;
	}

	public String getStrRatableCode() {
		return strRatableCode;
	}

	public void setStrRatableCode(String strRatableCode) {
		this.strRatableCode = strRatableCode;
	}
    
    
    
    
    
}
