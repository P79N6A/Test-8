package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateData implements Comparable<RateData>{
	private int lnDosage=0; // 本次需要批价的使用量
	private int lnChargedDosage=0; // 本次批价的使用量
	private int lnUnusedDosage=0;
	private int lnRatableUnusedDosage=0;
	private int lnRateUnit=0; // 计费单位(NOTE:只是最后一个批价成功的tariff的rate_unit)
	private double dRateValue=0; // 单位价格(最后一个，类似lnRateUnit)
	private List<OfrRateData> iOfrResults = new ArrayList<OfrRateData>(); // 套餐组的批价结果(如果是叠加套餐，则包括所有叠加组的批价结果)
	private List<Integer> iOfrIds= new ArrayList<Integer>(); // 批价成功销售品id(也可以通过iOfrResults得到)
	private RateMeasure iRateMeasure=new RateMeasure();
	private Map<String, RatableResourceValue> iRatableValues=new HashMap<String,RatableResourceValue>(); // 累积量
	
	
	
	public long getSumChargedDosage(){
		long retvalue =0L;
		for(OfrRateData tmpord:iOfrResults){
			retvalue = retvalue + tmpord.getOfrChargedDosage();
		}
		return retvalue;
	}


	public void init(RateData src_) {
		lnDosage = src_.lnDosage;
		lnChargedDosage = src_.lnChargedDosage;
		lnUnusedDosage = src_.lnUnusedDosage;
		lnRatableUnusedDosage = src_.lnRatableUnusedDosage;
		lnRateUnit = src_.lnRateUnit;
		dRateValue = src_.dRateValue;
		iOfrResults = src_.iOfrResults;
		iOfrIds = src_.iOfrIds;
		iRateMeasure = src_.iRateMeasure;
		iRatableValues = src_.iRatableValues;
	}

	int uncharged() {
		return (lnDosage - lnChargedDosage);
	}
	
	
    public double sum(){
    	double resultfee =0.0;
    	for(OfrRateData ofrRateData:iOfrResults){
    		resultfee = resultfee+ofrRateData.sum();
    	}

    	return resultfee;
    }

	public void addOfrRateResult(OfrRateData result_) {
		if(result_.getiSectionResults().isEmpty()){
			return ;
		}
		lnDosage += result_.getLnDosage();
		lnChargedDosage += result_.getLnChargedDosage();
		lnUnusedDosage += result_.getLnUnusedDosage();
		lnRatableUnusedDosage += result_.getLnRatableUnusedDosage();
		lnRateUnit = result_.getRateUnit();
		dRateValue = result_.getRateValue();
		
		iOfrResults.add(result_);
		iOfrIds.add(result_.getnOfrId());
		
		iRateMeasure = result_.getiRateMeasure();
		iRatableValues=result_.getiRatableValues();
		
		
	}

	
	@Override
	public int compareTo(RateData other) {
		System.out.println("this.sum()...."+sum()+",other.sum()...."+other.sum());
		if(sum()<other.sum()){
			return -1;
		}else if(sum()==other.sum()){
			return 0;
		}
		
		return 1;
			
	}
	
	
	
	
	void addChargedDosage(int dosage_) {
		lnChargedDosage += dosage_;
	}



	public int getLnDosage() {
		return lnDosage;
	}

	public void setLnDosage(int lnDosage) {
		this.lnDosage = lnDosage;
	}

	public int getLnChargedDosage() {
		return lnChargedDosage;
	}

	public void setLnChargedDosage(int lnChargedDosage) {
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

	public int getLnRateUnit() {
		return lnRateUnit;
	}

	public void setLnRateUnit(int lnRateUnit) {
		this.lnRateUnit = lnRateUnit;
	}

	public double getdRateValue() {
		return dRateValue;
	}

	public void setdRateValue(double dRateValue) {
		this.dRateValue = dRateValue;
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

	public List<OfrRateData> getiOfrResults() {
		return iOfrResults;
	}

	public void setiOfrResults(List<OfrRateData> iOfrResults) {
		this.iOfrResults = iOfrResults;
	}


	public List<Integer> getiOfrIds() {
		return iOfrIds;
	}


	public void setiOfrIds(List<Integer> iOfrIds) {
		this.iOfrIds = iOfrIds;
	}

	
	public void print(){
		System.out.println("[RateData]");
		System.out.println("lnDosage>>>"+lnDosage);
		System.out.println("lnChargedDosage>>>"+lnChargedDosage);
		System.out.println("lnUnusedDosage>>>"+lnUnusedDosage);
		System.out.println("lnRatableUnusedDosage>>>"+lnRatableUnusedDosage);
		System.out.println("lnRateUnit>>>"+lnRateUnit);
		System.out.println("iOfrResults.size>>>"+iOfrResults.size());
		int i=0;
		for(OfrRateData ofr:iOfrResults){
			System.out.print("iOfrResults["+i+"]=");
			ofr.print();
			i++;
		}
		System.out.println("iOfrIds.size>>>"+iOfrIds.size());
		System.out.println("iRatableValues.size>>>"+iRatableValues.size());
		System.out.println("iRateMeasure>>>"+iRateMeasure);
	}
	
	
	

}
