package com.tydic.beijing.billing.rating.domain;

import java.util.HashMap;
import java.util.Map;

public class RateConditionParam {

	private int m_nServiceId=0;
	private int m_nCondId=0;
	private int m_nRentArrear=0;
	private int m_nOnlineArrear=0;
	private int m_nArrearType =0;
	private int m_nDeductType=0;
	private int m_nBrandId=0;
	private boolean m_bLimitFee=false;
	private Map<String, RatableResourceValue> m_iRatables=new HashMap<String,RatableResourceValue>();		//累积量	
	private Map<String, RatableResourceValue> m_iTmpRatables=new HashMap<String,RatableResourceValue>();	//add by lhb 20110728累积量
	private Map<String, String> m_iServAttrs;	//用户静态属性
	private char m_cBillingObject;				//计费对象 1：主叫计费 2：被叫计费 3：三方计费
	private int  m_nGroupFlag=0;					//集团标识：-1：不参与集团优惠 0：集团内 1：集团外	
	private int  m_nBillingFlag=0;                //会话状态，1:Inital 2:Update 3:Term 4:Event 5:EventBack
	private long m_lnOriCharge=0;					//一次批价费用,用于单条话单的优惠，如当前话单的原始费用达到10元以上，话单打8折优惠
	private String m_szYYYY;
	private String m_szMM;
	private String m_szDD;	
	private String m_szHH;	
	private String m_szMI;	
	private int m_nOfrCellFlag=0;
	private int m_nOfrID=0; //记录当前扣费for_id add by sunm for 3G用户月租扣MT问题OCS-582
	private long m_lnBalanceAmount=0; //记录用户非专款专用账本余额总值 ad by sunm for【OCS-878】【青海】用户状态正常费用为0租费扣费写临时欠费

	private long m_lnPricingSectionId=0;
	private RatingMsg m_pRatingMsg;
	private RatingData m_pRatingData;
	public int getM_nServiceId() {
		return m_nServiceId;
	}
	public void setM_nServiceId(int m_nServiceId) {
		this.m_nServiceId = m_nServiceId;
	}
	public int getM_nCondId() {
		return m_nCondId;
	}
	public void setM_nCondId(int m_nCondId) {
		this.m_nCondId = m_nCondId;
	}
	public int getM_nRentArrear() {
		return m_nRentArrear;
	}
	public void setM_nRentArrear(int m_nRentArrear) {
		this.m_nRentArrear = m_nRentArrear;
	}
	public int getM_nOnlineArrear() {
		return m_nOnlineArrear;
	}
	public void setM_nOnlineArrear(int m_nOnlineArrear) {
		this.m_nOnlineArrear = m_nOnlineArrear;
	}
	public int getM_nArrearType() {
		return m_nArrearType;
	}
	public void setM_nArrearType(int m_nArrearType) {
		this.m_nArrearType = m_nArrearType;
	}
	public int getM_nDeductType() {
		return m_nDeductType;
	}
	public void setM_nDeductType(int m_nDeductType) {
		this.m_nDeductType = m_nDeductType;
	}
	public int getM_nBrandId() {
		return m_nBrandId;
	}
	public void setM_nBrandId(int m_nBrandId) {
		this.m_nBrandId = m_nBrandId;
	}
	public boolean isM_bLimitFee() {
		return m_bLimitFee;
	}
	public void setM_bLimitFee(boolean m_bLimitFee) {
		this.m_bLimitFee = m_bLimitFee;
	}
	public Map<String, RatableResourceValue> getM_iRatables() {
		return m_iRatables;
	}
	public void setM_iRatables(Map<String, RatableResourceValue> m_iRatables) {
		this.m_iRatables = m_iRatables;
	}
	public Map<String, RatableResourceValue> getM_iTmpRatables() {
		return m_iTmpRatables;
	}
	public void setM_iTmpRatables(Map<String, RatableResourceValue> m_iTmpRatables) {
		this.m_iTmpRatables = m_iTmpRatables;
	}
	public Map<String, String> getM_iServAttrs() {
		return m_iServAttrs;
	}
	public void setM_iServAttrs(Map<String, String> m_iServAttrs) {
		this.m_iServAttrs = m_iServAttrs;
	}
	public char getM_cBillingObject() {
		return m_cBillingObject;
	}
	public void setM_cBillingObject(char m_cBillingObject) {
		this.m_cBillingObject = m_cBillingObject;
	}
	public int getM_nGroupFlag() {
		return m_nGroupFlag;
	}
	public void setM_nGroupFlag(int m_nGroupFlag) {
		this.m_nGroupFlag = m_nGroupFlag;
	}
	public int getM_nBillingFlag() {
		return m_nBillingFlag;
	}
	public void setM_nBillingFlag(int m_nBillingFlag) {
		this.m_nBillingFlag = m_nBillingFlag;
	}
	public long getM_lnOriCharge() {
		return m_lnOriCharge;
	}
	public void setM_lnOriCharge(long m_lnOriCharge) {
		this.m_lnOriCharge = m_lnOriCharge;
	}
	
	
	public String getM_szYYYY() {
		return m_szYYYY;
	}
	public void setM_szYYYY(String m_szYYYY) {
		this.m_szYYYY = m_szYYYY;
	}
	public String getM_szMM() {
		return m_szMM;
	}
	public void setM_szMM(String m_szMM) {
		this.m_szMM = m_szMM;
	}
	public String getM_szDD() {
		return m_szDD;
	}
	public void setM_szDD(String m_szDD) {
		this.m_szDD = m_szDD;
	}
	public String getM_szHH() {
		return m_szHH;
	}
	public void setM_szHH(String m_szHH) {
		this.m_szHH = m_szHH;
	}
	public String getM_szMI() {
		return m_szMI;
	}
	public void setM_szMI(String m_szMI) {
		this.m_szMI = m_szMI;
	}
	public int getM_nOfrCellFlag() {
		return m_nOfrCellFlag;
	}
	public void setM_nOfrCellFlag(int m_nOfrCellFlag) {
		this.m_nOfrCellFlag = m_nOfrCellFlag;
	}
	public int getM_nOfrID() {
		return m_nOfrID;
	}
	public void setM_nOfrID(int m_nOfrID) {
		this.m_nOfrID = m_nOfrID;
	}
	public long getM_lnBalanceAmount() {
		return m_lnBalanceAmount;
	}
	public void setM_lnBalanceAmount(long m_lnBalanceAmount) {
		this.m_lnBalanceAmount = m_lnBalanceAmount;
	}
	public long getM_lnPricingSectionId() {
		return m_lnPricingSectionId;
	}
	public void setM_lnPricingSectionId(long m_lnPricingSectionId) {
		this.m_lnPricingSectionId = m_lnPricingSectionId;
	}
	public RatingMsg getM_pRatingMsg() {
		return m_pRatingMsg;
	}
	public void setM_pRatingMsg(RatingMsg m_pRatingMsg) {
		this.m_pRatingMsg = m_pRatingMsg;
	}
	public RatingData getM_pRatingData() {
		return m_pRatingData;
	}
	public void setM_pRatingData(RatingData m_pRatingData) {
		this.m_pRatingData = m_pRatingData;
	}
	
	
	
	
}
