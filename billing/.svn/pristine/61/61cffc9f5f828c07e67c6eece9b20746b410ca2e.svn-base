package com.tydic.beijing.billing.rating.domain;

import org.apache.log4j.Logger;

public class ChargeUnit {
	private Logger logger=Logger.getLogger(ChargeUnit.class);
	private   String       lnServId="-1";					//产品实例	Int	计费号码的产品实例
	private  String  strMeasureDomain=""; //计费资源	Char（2）	01：时长 02：流量 04：上行流量 05：下行流量 06：按次
    private   int       lnBillingDosage=0;			//计费时长	(流量)	Int	修改为long
    private   int       lnBeforeDosage=0;				//Int	修改为long
    private   int       lnBeforeDosageFront=0;			//Int	修改为long
    private   int       lnCounts=0;					//计费次数	Int	修改为long
    private   int       lnAcctItemTypeId=0;			//帐目类型	Int	
    private   int         nPresentAcctItemTypeId=0;		//赠送帐目类型	Int
    private   long       lnOriCharge=0;				//标准费用	Long	
    private   long       lnCharge=0;				//优惠费用	Long	
    private   int         nCycleTypeId=0;				//帐期类型	Int	
    private   int         nRatingAcctId=0;			//转移合同号	Int	
    private   int         nUnitType=0;					//信用单位类型,1-秒（时长）;2-分（金额）;3-kb总流量;4-kb上行流量;5-kb下行流量
    private   int       lnFee=0;
    private   int       lnDiscountId=-1;				//段落打折（0－100）
    private   int         nRatableFlag=0;                              //段落打折累积标志，0:为打折前，1:为打折后

    private  String  strRatableCode="";            //累积量
	private   int       lnRateUnit=0;                   //费率单元
	private   int         nMTChangeFlag=0;//是否是MT转换的
	private   int         nTailMode=3;
	private   long       lnUnusedMondey=0;
	private   long       lnPricingSectionId=0;
	private   int       lnLeftMoney=0;   //实扣大于预占时，存放（实扣-预占）
	private   int         nPlanId=0;
	private   int         nOfrId=-1;
	private   int       lnOfrInstId=0;          //套餐OfrInstId标示
	public String getLnServId() {
		return lnServId;
	}
	public void setLnServId(String lnServId) {
		this.lnServId = lnServId;
	}
	public String getStrMeasureDomain() {
		return strMeasureDomain;
	}
	public void setStrMeasureDomain(String strMeasureDomain) {
		this.strMeasureDomain = strMeasureDomain;
	}
	public int getLnBillingDosage() {
		return lnBillingDosage;
	}
	public void setLnBillingDosage(int lnBillingDosage) {
		this.lnBillingDosage = lnBillingDosage;
	}
	public int getLnBeforeDosage() {
		return lnBeforeDosage;
	}
	public void setLnBeforeDosage(int lnBeforeDosage) {
		this.lnBeforeDosage = lnBeforeDosage;
	}
	public int getLnBeforeDosageFront() {
		return lnBeforeDosageFront;
	}
	public void setLnBeforeDosageFront(int lnBeforeDosageFront) {
		this.lnBeforeDosageFront = lnBeforeDosageFront;
	}
	public int getLnCounts() {
		return lnCounts;
	}
	public void setLnCounts(int lnCounts) {
		this.lnCounts = lnCounts;
	}
	public int getLnAcctItemTypeId() {
		return lnAcctItemTypeId;
	}
	public void setLnAcctItemTypeId(int lnAcctItemTypeId) {
		this.lnAcctItemTypeId = lnAcctItemTypeId;
	}
	public int getnPresentAcctItemTypeId() {
		return nPresentAcctItemTypeId;
	}
	public void setnPresentAcctItemTypeId(int nPresentAcctItemTypeId) {
		this.nPresentAcctItemTypeId = nPresentAcctItemTypeId;
	}
	
	public long getLnOriCharge() {
		return lnOriCharge;
	}
	public void setLnOriCharge(long lnOriCharge) {
		this.lnOriCharge = lnOriCharge;
	}
	public long getLnCharge() {
		return lnCharge;
	}
	public void setLnCharge(long lnCharge) {
		this.lnCharge = lnCharge;
	}
	public int getnCycleTypeId() {
		return nCycleTypeId;
	}
	public void setnCycleTypeId(int nCycleTypeId) {
		this.nCycleTypeId = nCycleTypeId;
	}
	public int getnRatingAcctId() {
		return nRatingAcctId;
	}
	public void setnRatingAcctId(int nRatingAcctId) {
		this.nRatingAcctId = nRatingAcctId;
	}
	public int getnUnitType() {
		return nUnitType;
	}
	public void setnUnitType(int nUnitType) {
		this.nUnitType = nUnitType;
	}
	public int getLnFee() {
		return lnFee;
	}
	public void setLnFee(int lnFee) {
		this.lnFee = lnFee;
	}
	public int getLnDiscountId() {
		return lnDiscountId;
	}
	public void setLnDiscountId(int lnDiscountId) {
		this.lnDiscountId = lnDiscountId;
	}
	public int getnRatableFlag() {
		return nRatableFlag;
	}
	public void setnRatableFlag(int nRatableFlag) {
		this.nRatableFlag = nRatableFlag;
	}
	public String getStrRatableCode() {
		return strRatableCode;
	}
	public void setStrRatableCode(String strRatableCode) {
		this.strRatableCode = strRatableCode;
	}
	public int getLnRateUnit() {
		return lnRateUnit;
	}
	public void setLnRateUnit(int lnRateUnit) {
		this.lnRateUnit = lnRateUnit;
	}
	public int getnMTChangeFlag() {
		return nMTChangeFlag;
	}
	public void setnMTChangeFlag(int nMTChangeFlag) {
		this.nMTChangeFlag = nMTChangeFlag;
	}
	public int getnTailMode() {
		return nTailMode;
	}
	public void setnTailMode(int nTailMode) {
		this.nTailMode = nTailMode;
	}
	public long getLnUnusedMondey() {
		return lnUnusedMondey;
	}
	public void setLnUnusedMondey(long lnUnusedMondey) {
		this.lnUnusedMondey = lnUnusedMondey;
	}
	
	public long getLnPricingSectionId() {
		return lnPricingSectionId;
	}
	public void setLnPricingSectionId(long lnPricingSectionId) {
		this.lnPricingSectionId = lnPricingSectionId;
	}
	public int getLnLeftMoney() {
		return lnLeftMoney;
	}
	public void setLnLeftMoney(int lnLeftMoney) {
		this.lnLeftMoney = lnLeftMoney;
	}
	public int getnPlanId() {
		return nPlanId;
	}
	public void setnPlanId(int nPlanId) {
		this.nPlanId = nPlanId;
	}
	public int getnOfrId() {
		return nOfrId;
	}
	public void setnOfrId(int nOfrId) {
		this.nOfrId = nOfrId;
	}
	public int getLnOfrInstId() {
		return lnOfrInstId;
	}
	public void setLnOfrInstId(int lnOfrInstId) {
		this.lnOfrInstId = lnOfrInstId;
	}
    
    public void print(){
    	logger.debug("ChargeUnit::print()");
        
        logger.debug(" - lnServId               ["+lnServId+"]");
        logger.debug(" - strMeasureDomain       ["+strMeasureDomain+"]");
        logger.debug(" - lnBillingDosage        ["+lnBillingDosage+"]");
        logger.debug(" - lnBeforeDosage         ["+lnBeforeDosage+"]");
        logger.debug(" - lnBeforeDosageFront    ["+lnBeforeDosageFront+"]");
        logger.debug(" - lnCounts               ["+lnCounts+"]");
        logger.debug(" - lnAcctItemTypeId       ["+lnAcctItemTypeId+"]");
        logger.debug(" - nPresentAcctItemTypeId ["+nPresentAcctItemTypeId+"]");
        logger.debug(" - lnOriCharge            ["+lnOriCharge+"]");
        logger.debug(" - lnCharge               ["+lnCharge+"]");
        logger.debug(" - nCycleTypeId           ["+nCycleTypeId+"]");
        logger.debug(" - nRatingAcctId          ["+nRatingAcctId+"]");
        logger.debug(" - nUnitType              ["+nUnitType+"]");
        logger.debug(" - lnFee                  ["+lnFee+"]");
        logger.debug(" - lnDiscountId           ["+lnDiscountId+"]");
        logger.debug(" - nRatableFlag           ["+nRatableFlag+"]");
        logger.debug(" - strRatableCode         ["+strRatableCode+"]");
        logger.debug(" - lnRateUnit             ["+lnRateUnit+"]");
        logger.debug(" - nMTChangeFlag          ["+nMTChangeFlag+"]");
        logger.debug(" - nTailMode              ["+nTailMode+"]");
        logger.debug(" - lnUnusedMondey         ["+lnUnusedMondey+"]");
        logger.debug(" - lnPricingSectionId     ["+lnPricingSectionId+"]");
        logger.debug(" - lnLeftMoney            ["+lnLeftMoney+"]");
        logger.debug(" - nPlanId                ["+nPlanId+"]");
        logger.debug(" - lnOfrInstId            ["+lnOfrInstId+"]");
        logger.debug(" - nOfrId                 ["+nOfrId+"]");
    }
    
    
    
    public void show(){
    	System.out.println("ChargeUnit::print()");
        
        System.out.println(" - lnServId               ["+lnServId+"]");
        System.out.println(" - strMeasureDomain       ["+strMeasureDomain+"]");
        System.out.println(" - lnBillingDosage        ["+lnBillingDosage+"]");
        System.out.println(" - lnBeforeDosage         ["+lnBeforeDosage+"]");
        System.out.println(" - lnBeforeDosageFront    ["+lnBeforeDosageFront+"]");
        System.out.println(" - lnCounts               ["+lnCounts+"]");
        System.out.println(" - lnAcctItemTypeId       ["+lnAcctItemTypeId+"]");
        System.out.println(" - nPresentAcctItemTypeId ["+nPresentAcctItemTypeId+"]");
        System.out.println(" - lnOriCharge            ["+lnOriCharge+"]");
        System.out.println(" - lnCharge               ["+lnCharge+"]");
        System.out.println(" - nCycleTypeId           ["+nCycleTypeId+"]");
        System.out.println(" - nRatingAcctId          ["+nRatingAcctId+"]");
        System.out.println(" - nUnitType              ["+nUnitType+"]");
        System.out.println(" - lnFee                  ["+lnFee+"]");
        System.out.println(" - lnDiscountId           ["+lnDiscountId+"]");
        System.out.println(" - nRatableFlag           ["+nRatableFlag+"]");
        System.out.println(" - strRatableCode         ["+strRatableCode+"]");
        System.out.println(" - lnRateUnit             ["+lnRateUnit+"]");
        System.out.println(" - nMTChangeFlag          ["+nMTChangeFlag+"]");
        System.out.println(" - nTailMode              ["+nTailMode+"]");
        System.out.println(" - lnUnusedMondey         ["+lnUnusedMondey+"]");
        System.out.println(" - lnPricingSectionId     ["+lnPricingSectionId+"]");
        System.out.println(" - lnLeftMoney            ["+lnLeftMoney+"]");
        System.out.println(" - nPlanId                ["+nPlanId+"]");
        System.out.println(" - lnOfrInstId            ["+lnOfrInstId+"]");
        System.out.println(" - nOfrId                 ["+nOfrId+"]");
    }
   
    
}
