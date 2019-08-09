package com.tydic.beijing.billing.rating.domain;

public class ServInfo {
	private  String  lnServId="-1";      //用户标识
	private  int  lnAcctId=0;;      //帐户标识
	private  String  lnCustId="";      //客户标识
	private  int  nOfrId=0;;       //基础销售品
	private  int  nLatnId=0;;      //本地网标识
	private  int  lnExchId=0;;      //局向
	private  String  strUserTypeId= "";;  //用户类型
	private  String  strUrbanFlag= "";   //城乡标识
	private  String  strReserve1= "";    //保留
	private  String  strReserve2= "";    //保留
	private  String  strRegionId= "";    //区域标识
	private  String  strIFIVPN= "";      //是否参加IVPN
	private  String  strBasicState= "";
	private  String  strExtState= "";
	private  String  strAcceptDate= ""; //受理日期
	public String getLnServId() {
		return lnServId;
	}
	public void setLnServId(String lnServId) {
		this.lnServId = lnServId;
	}
	public int getLnAcctId() {
		return lnAcctId;
	}
	public void setLnAcctId(int lnAcctId) {
		this.lnAcctId = lnAcctId;
	}
	public String getLnCustId() {
		return lnCustId;
	}
	public void setLnCustId(String lnCustId) {
		this.lnCustId = lnCustId;
	}
	public int getnOfrId() {
		return nOfrId;
	}
	public void setnOfrId(int nOfrId) {
		this.nOfrId = nOfrId;
	}
	public int getnLatnId() {
		return nLatnId;
	}
	public void setnLatnId(int nLatnId) {
		this.nLatnId = nLatnId;
	}
	public int getLnExchId() {
		return lnExchId;
	}
	public void setLnExchId(int lnExchId) {
		this.lnExchId = lnExchId;
	}
	public String getStrUserTypeId() {
		return strUserTypeId;
	}
	public void setStrUserTypeId(String strUserTypeId) {
		this.strUserTypeId = strUserTypeId;
	}
	public String getStrUrbanFlag() {
		return strUrbanFlag;
	}
	public void setStrUrbanFlag(String strUrbanFlag) {
		this.strUrbanFlag = strUrbanFlag;
	}
	public String getStrReserve1() {
		return strReserve1;
	}
	public void setStrReserve1(String strReserve1) {
		this.strReserve1 = strReserve1;
	}
	public String getStrReserve2() {
		return strReserve2;
	}
	public void setStrReserve2(String strReserve2) {
		this.strReserve2 = strReserve2;
	}
	public String getStrRegionId() {
		return strRegionId;
	}
	public void setStrRegionId(String strRegionId) {
		this.strRegionId = strRegionId;
	}
	public String getStrIFIVPN() {
		return strIFIVPN;
	}
	public void setStrIFIVPN(String strIFIVPN) {
		this.strIFIVPN = strIFIVPN;
	}
	public String getStrBasicState() {
		return strBasicState;
	}
	public void setStrBasicState(String strBasicState) {
		this.strBasicState = strBasicState;
	}
	public String getStrExtState() {
		return strExtState;
	}
	public void setStrExtState(String strExtState) {
		this.strExtState = strExtState;
	}
	public String getStrAcceptDate() {
		return strAcceptDate;
	}
	public void setStrAcceptDate(String strAcceptDate) {
		this.strAcceptDate = strAcceptDate;
	}
	
	
}
