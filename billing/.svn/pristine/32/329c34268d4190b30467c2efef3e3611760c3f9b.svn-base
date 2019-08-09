package com.tydic.beijing.billing.rating.domain;

public class PricingSection {
	private   int  nOfrId=0; //折线前ofr_id，来自定价计划
	private   long  lnPricingSectionId=0;
	private   int  lnTariffId=0;
	private   int  lnDiscountId=0;
	private   int  nTariffType=0; //0:计量计费 1:时间计费
	private   int  nAcctItemType=0; //0:帐目项 1:帐目项代码
	private   int  nTailMode=2;                 //段落最后结果的位数处理模式 0:向上圆整 1:向下圆整 2:四舍五入圆整
	private   int  nRefFlag=0; //计费单位类型
	private   int  nSectionType=0; //0:免费段落 1:收费段落
	private   int  lnAcctItemId=0;
	private   String strMeasureDomain="-1";
	private long atomOfrId=0;
	private int nodeType = 0;//0:中间节点；1：叶子节点;2:根节点  段落节点类型
	
	private long lower =0L;  //一个模型里有一个模型里没有，转来转去也太坑爹了，
	private long upper =0L;
	
	
    //from PlanDisct
	public   int lnOfrInstId=0;
	public   boolean bIsFirstMonth=false;
    public  double  fCoefficient=0;
    
    //from tb_bil_pricing_section_rule
    public   long  lnCalcPriority=0;

    //from tb_bil_evt_pricing_strategy
    public   int nPricingPlanId=0;
    public   long lnEventTypeId=0;
    public   String strRefResourceCode="";

    //from tb_bil_strategy_section_rel
    public   long lnStrategyId=0;
    public   int nServiceId=0; //服务限制
    
    private long conditionId=0;
    
    
    public PricingSection(){}
    
    public PricingSection(RulePricingSection sec){
    	this.strMeasureDomain=sec.getMeasure_domain();
    	this.nTailMode=sec.getTail_mod();
    	this.nAcctItemType=sec.getAcct_item_type();
    	this.lnDiscountId=sec.getDiscount_id();
    	this.lnTariffId=sec.getTariff_id();
    	this.nTariffType=sec.getTariff_type();
    	this.nSectionType=sec.getSection_type();
    	
    }
    
	public int getnOfrId() {
		return nOfrId;
	}
	public void setnOfrId(int nOfrId) {
		this.nOfrId = nOfrId;
	}
	public long getLnPricingSectionId() {
		return lnPricingSectionId;
	}
	public void setLnPricingSectionId(long lnPricingSectionId) {
		this.lnPricingSectionId = lnPricingSectionId;
	}
	public int getLnTariffId() {
		return lnTariffId;
	}
	public void setLnTariffId(int lnTariffId) {
		this.lnTariffId = lnTariffId;
	}
	public int getLnDiscountId() {
		return lnDiscountId;
	}
	public void setLnDiscountId(int lnDiscountId) {
		this.lnDiscountId = lnDiscountId;
	}
	public int getnTariffType() {
		return nTariffType;
	}
	public void setnTariffType(int nTariffType) {
		this.nTariffType = nTariffType;
	}
	public int getnAcctItemType() {
		return nAcctItemType;
	}
	public void setnAcctItemType(int nAcctItemType) {
		this.nAcctItemType = nAcctItemType;
	}
	public int getnTailMode() {
		return nTailMode;
	}
	public void setnTailMode(int nTailMode) {
		this.nTailMode = nTailMode;
	}
	public int getnRefFlag() {
		return nRefFlag;
	}
	public void setnRefFlag(int nRefFlag) {
		this.nRefFlag = nRefFlag;
	}
	public int getnSectionType() {
		return nSectionType;
	}
	public void setnSectionType(int nSectionType) {
		this.nSectionType = nSectionType;
	}
	public int getLnAcctItemId() {
		return lnAcctItemId;
	}
	public void setLnAcctItemId(int lnAcctItemId) {
		this.lnAcctItemId = lnAcctItemId;
	}
	public String getStrMeasureDomain() {
		return strMeasureDomain;
	}
	public void setStrMeasureDomain(String strMeasureDomain) {
		this.strMeasureDomain = strMeasureDomain;
	}
	public int getLnOfrInstId() {
		return lnOfrInstId;
	}
	public void setLnOfrInstId(int lnOfrInstId) {
		this.lnOfrInstId = lnOfrInstId;
	}
	public boolean isbIsFirstMonth() {
		return bIsFirstMonth;
	}
	public void setbIsFirstMonth(boolean bIsFirstMonth) {
		this.bIsFirstMonth = bIsFirstMonth;
	}
	public double getfCoefficient() {
		return fCoefficient;
	}
	public void setfCoefficient(double fCoefficient) {
		this.fCoefficient = fCoefficient;
	}
	public long getLnCalcPriority() {
		return lnCalcPriority;
	}
	public void setLnCalcPriority(long lnCalcPriority) {
		this.lnCalcPriority = lnCalcPriority;
	}
	public int getnPricingPlanId() {
		return nPricingPlanId;
	}
	public void setnPricingPlanId(int nPricingPlanId) {
		this.nPricingPlanId = nPricingPlanId;
	}
	public long getLnEventTypeId() {
		return lnEventTypeId;
	}
	public void setLnEventTypeId(long lnEventTypeId) {
		this.lnEventTypeId = lnEventTypeId;
	}
	public String getStrRefResourceCode() {
		return strRefResourceCode;
	}
	public void setStrRefResourceCode(String strRefResourceCode) {
		this.strRefResourceCode = strRefResourceCode;
	}
	public long getLnStrategyId() {
		return lnStrategyId;
	}
	public void setLnStrategyId(long lnStrategyId) {
		this.lnStrategyId = lnStrategyId;
	}
	public int getnServiceId() {
		return nServiceId;
	}
	public void setnServiceId(int nServiceId) {
		this.nServiceId = nServiceId;
	}
    
    public long getAtomOfrId() {
		return atomOfrId;
	}
    public void setAtomOfrId(long atomOfrId) {
		this.atomOfrId = atomOfrId;
	}
  

	public long getConditionId() {
		return conditionId;
	}
	public void setConditionId(long conditionId) {
		this.conditionId = conditionId;
	}
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	public long getLower() {
		return lower;
	}
	public void setLower(long lower) {
		this.lower = lower;
	}
	public long getUpper() {
		return upper;
	}
	public void setUpper(long upper) {
		this.upper = upper;
	}
    
    

    
	
   

}
