package com.tydic.beijing.billing.rating.domain;

public class DeductRecord { //SMS/ISMP扣费历史表信息
	public static  int  nUnitTypeId = 0;       //结果单位类型
	public static  int  nMsgType = 0;          //消息类型
	public static  int  nLatnId = 0;
	public static  int lnAcctBalanceId = 0;
	public static  int lnAmount = 0;
	public static  String strAreaCode = "";
	public static  String strServiceNbr = "";
	public static  String strSmId = "";
	public static  String strRatableInfo = "";
	public static int lnAcctBalanceITemID = 0;//帐目类型
	public static  boolean bCredit= false;

    DeductRecord()
    {
        lnAcctBalanceITemID = -1;
        bCredit = false;
    }
}
