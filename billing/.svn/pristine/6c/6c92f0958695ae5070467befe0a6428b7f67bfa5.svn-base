package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

///段落批价结果
public class SectionRateData {

	private long lnPricingSectionId=0;
	private int lnDosage=0; // 圆整后的批价使用量
	private long lnChargedDosage=0; // lnDosage-lnUnusedDosage 批价的使用量
	private int lnUnusedDosage=0; // 圆整掉的使用量
	private int lnRatableUnusedDosage=0; // 需要累积的圆整掉的使用量
	private int lnLastDosage=0;
	private int lnUnusedLastDosage=0;
	private int lnRateUnit=0; // 批价单位
	private int nChargedCount=0; // 批价单位数
	private int nUnitType=0; // 单位类型
	private double dRateValue=0; // 单位价格
	private double dOrigFee=0; // 打折前费用值。
	private double dFee=0; // 最终费用(厘)
	private double dUnusedFee=0; // 未用费用
	private int lnTotalFee=0; // 规整后的费用(分)
	private int lnAcctItemTypeId=-1;
	private String strMeasureDomain=""; // 计费单位类型
	private RateMeasure iRateMeasure=new RateMeasure();
	private Map<String, RatableResourceValue> iRatableValues=new HashMap<String,RatableResourceValue>(); // 累积量
	private List<TariffResult> iTariffResults=new ArrayList<TariffResult>();
	
	
	public long getSectionChargedDosage(){
		long retvalue=0L;
		for(TariffResult tmptf:iTariffResults){
			retvalue =  retvalue+ tmptf.getLnDosage();
		}
		return retvalue;
	}
	
	
	
	public long getSectionfee(){
		long retvalue=0L;
		for(TariffResult tmptf:iTariffResults){
			retvalue =  retvalue+ tmptf.getLnFee();
		}
		return retvalue;
	}
	
	
	
	public void setAttrFromTariffResults(){
		for(TariffResult iter : iTariffResults)
        {
            lnUnusedDosage += iter.getLnUnusedDosage();
            lnRatableUnusedDosage += iter.getLnRatableUnusedDosage();

            lnDosage += iter.getLnDosage();
            nChargedCount += iter.getnCount();
            dFee += iter.getdFee();
            lnTotalFee += iter.getLnFee();

            lnLastDosage += iter.getLnLastDosage();
            lnUnusedLastDosage += iter.getLnUnusedLastDosage();

            lnRateUnit = iter.getLnRateUnit();
            nUnitType = iter.getnUnitTypeId();
        }

        dOrigFee = dFee;
        lnChargedDosage = lnDosage - lnUnusedDosage;
        dUnusedFee = dFee - lnTotalFee*RatingMacro.TARIFF_PRECISION;
	}
	public long getLnPricingSectionId() {
		return lnPricingSectionId;
	}
	public void setLnPricingSectionId(long lnPricingSectionId) {
		this.lnPricingSectionId = lnPricingSectionId;
	}
	public int getLnDosage() {
		return lnDosage;
	}
	public void setLnDosage(int lnDosage) {
		this.lnDosage = lnDosage;
	}
	public long getLnChargedDosage() {
		return lnChargedDosage;
	}
	public void setLnChargedDosage(long lnChargedDosage) {
		this.lnChargedDosage = lnChargedDosage;
	}
	public int getLnUnusedDosage() {
		return lnUnusedDosage;
	}
	public void setLnUnusedDosage(int lnUnusedDosage) {
		this.lnUnusedDosage = lnUnusedDosage;
	}
	public int getLnRatableUnusedDosage() {
		return lnRatableUnusedDosage;
	}
	public void setLnRatableUnusedDosage(int lnRatableUnusedDosage) {
		this.lnRatableUnusedDosage = lnRatableUnusedDosage;
	}
	public int getLnLastDosage() {
		return lnLastDosage;
	}
	public void setLnLastDosage(int lnLastDosage) {
		this.lnLastDosage = lnLastDosage;
	}
	public int getLnUnusedLastDosage() {
		return lnUnusedLastDosage;
	}
	public void setLnUnusedLastDosage(int lnUnusedLastDosage) {
		this.lnUnusedLastDosage = lnUnusedLastDosage;
	}
	public int getLnRateUnit() {
		return lnRateUnit;
	}
	public void setLnRateUnit(int lnRateUnit) {
		this.lnRateUnit = lnRateUnit;
	}
	public int getnChargedCount() {
		return nChargedCount;
	}
	public void setnChargedCount(int nChargedCount) {
		this.nChargedCount = nChargedCount;
	}
	public int getnUnitType() {
		return nUnitType;
	}
	public void setnUnitType(int nUnitType) {
		this.nUnitType = nUnitType;
	}
	public double getdRateValue() {
		return dRateValue;
	}
	public void setdRateValue(double dRateValue) {
		this.dRateValue = dRateValue;
	}
	public double getdOrigFee() {
		return dOrigFee;
	}
	public void setdOrigFee(double dOrigFee) {
		this.dOrigFee = dOrigFee;
	}
	public double getdFee() {
		return dFee;
	}
	public void setdFee(double dFee) {
		this.dFee = dFee;
	}
	public double getdUnusedFee() {
		return dUnusedFee;
	}
	public void setdUnusedFee(double dUnusedFee) {
		this.dUnusedFee = dUnusedFee;
	}
	public int getLnTotalFee() {
		return lnTotalFee;
	}
	public void setLnTotalFee(int lnTotalFee) {
		this.lnTotalFee = lnTotalFee;
	}
	public int getLnAcctItemTypeId() {
		return lnAcctItemTypeId;
	}
	public void setLnAcctItemTypeId(int lnAcctItemTypeId) {
		this.lnAcctItemTypeId = lnAcctItemTypeId;
	}
	public String getStrMeasureDomain() {
		return strMeasureDomain;
	}
	public void setStrMeasureDomain(String strMeasureDomain) {
		this.strMeasureDomain = strMeasureDomain;
	}
	public RateMeasure getiRateMeasure() {
		return iRateMeasure;
	}
	public void setiRateMeasure(RateMeasure iRateMeasure) {
		this.iRateMeasure = iRateMeasure;
	}
	public Map<String, RatableResourceValue> getiRatableValues() {
		return iRatableValues;
	}
	public void setiRatableValues(Map<String, RatableResourceValue> iRatableValues) {
		this.iRatableValues = iRatableValues;
	}
	public List<TariffResult> getiTariffResults() {
		return iTariffResults;
	}
	public void setiTariffResults(List<TariffResult> iTariffResults) {
		this.iTariffResults = iTariffResults;
	}
	
	
	

	@Override
	public String toString() {
		String str="lnPricingSectionId["+lnPricingSectionId+"],lnDosage["+lnDosage+"],lnChargedDosage["+lnChargedDosage+"],lnUnusedDosage["+lnUnusedDosage+
				"],lnRatableUnusedDosage["+lnRatableUnusedDosage+"],lnLastDosage["+lnLastDosage+"],lnUnusedLastDosage["+lnUnusedLastDosage+"],lnRateUnit["+
				lnRateUnit+"],nChargedCount["+nChargedCount+"],nUnitType["+nUnitType+"],dRateValue["+dRateValue+"],dOrigFee["+dOrigFee+"],dFee["+
				dFee+"],dUnusedFee["+dUnusedFee+"],lnTotalFee["+lnTotalFee+"],lnAcctItemTypeId["+lnAcctItemTypeId+"],strMeasureDomain["+
				strMeasureDomain+"]";
				
		return str;
	}
	
	

	
}
