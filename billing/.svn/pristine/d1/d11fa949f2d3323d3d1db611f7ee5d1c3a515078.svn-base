package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 套餐批价结果
 * 
 * @author dongxuanyi
 * 
 */
public class OfrRateData {
	private int nOfrId;
	private int lnAtomOfrId;
	private long amount ;
	private int lnAcctItemTypeId;

	private int lnDosage; // 需要批价的使用量
	private int lnChargedDosage; // 本次批价的使用量
	private int lnUnusedDosage;
	private int lnRatableUnusedDosage;
	private List<SectionRateData> iSectionResults= new ArrayList<SectionRateData>();
	private RateMeasure iRateMeasure=new RateMeasure();

	private Map<String,RatableResourceValue> iRatableValues=new HashMap<String,RatableResourceValue>(); //累积量
	
	
	private int ofrRateResultFlag =-1;
	 
	private boolean isDirect=false;
	
	
	public long getOfrChargedDosage(){
		
		if(amount >0){
			return amount ;
		}
		
		long retvalue =0L;
		for(SectionRateData tmpsr:iSectionResults){
			retvalue = retvalue + tmpsr.getSectionChargedDosage();
		}
		return retvalue;
	}
	
	
//	void init() {
//		nOfrId = -1;
//		lnAtomOfrId = -1;
//
//		lnDosage = 0;
//		lnChargedDosage = 0;
//		lnUnusedDosage = 0;
//		lnRatableUnusedDosage = 0;
//
//	}
//
//	void init(OfrRateData src_) {
//		nOfrId = src_.nOfrId;
//		lnAtomOfrId = src_.lnAtomOfrId;
//
//		lnDosage = src_.lnDosage;
//		lnChargedDosage = src_.lnChargedDosage;
//		lnUnusedDosage = src_.lnUnusedDosage;
//		lnRatableUnusedDosage = src_.lnRatableUnusedDosage;
//
//		iSectionResults = src_.iSectionResults;
//		iRateMeasure = src_.iRateMeasure;
//	}

	
	public int getLnAcctItemTypeId() {
		return lnAcctItemTypeId;
	}


	public void setLnAcctItemTypeId(int lnAcctItemTypeId) {
		this.lnAcctItemTypeId = lnAcctItemTypeId;
	}


	public long getAmount() {
		return amount;
	}


	public void setAmount(long amount) {
		this.amount = amount;
	}


	public void addSectionRateResultWithoutDosage(SectionRateData result_)
    {
        
        if( result_.getiTariffResults().size() == 0 )
        {
            return;
        }

        iSectionResults.add(result_);
    }

	
	public void calcRateMeasure(){
        
        if(iSectionResults.size()==0 ){
            return;
        }
        Map<Integer, List<Integer> > iAcctSecResults=new HashMap<Integer,List<Integer>>();
        Map<Long, Dosage> iAcctDosages=new HashMap<Long,Dosage>();
        Map<Long, String> iAcctMeasureDomain=new HashMap<Long,String>();
        int nSize = iSectionResults.size();
        System.out.println("nSize>>>>>>"+nSize);
        for(int i=0; i<nSize; ++i){
        	List<Integer> list=new ArrayList<Integer>();
        	iAcctSecResults.put(iSectionResults.get(i).getLnAcctItemTypeId(), list);
        	iAcctSecResults.get(iSectionResults.get(i).getLnAcctItemTypeId()).add(i);
//        	System.out.println("acctitemtypeid>>>"+iSectionResults.get(i).getLnAcctItemTypeId());
        }
        Set<Integer> keys=iAcctSecResults.keySet();
        for(int key:keys){
        	List<Integer> value=iAcctSecResults.get(key);
        	String strMeasureDomain="";
            Dosage iDosage=new Dosage();
        	for(Integer i:value){
        		
        		strMeasureDomain = iSectionResults.get(i).getStrMeasureDomain();
        		iDosage.setM_lnDosage(iDosage.getM_lnDosage()+iSectionResults.get(i).getLnDosage());
        		iDosage.setM_lnChargedDosage(iDosage.getM_lnChargedDosage()+iSectionResults.get(i).getLnChargedDosage());
        		iDosage.setM_lnUnusedDosage(iDosage.getM_lnUnusedDosage()+iSectionResults.get(i).getLnUnusedDosage());
        		iDosage.setM_lnRatableUnusedDosage(iDosage.getM_lnRatableUnusedDosage()+iSectionResults.get(i).getLnRatableUnusedDosage());
        		iDosage.setM_lnLastDosage(iDosage.getM_lnLastDosage()+iSectionResults.get(i).getLnLastDosage());
        		iDosage.setM_lnUnusedLastDosage(iDosage.getM_lnUnusedLastDosage()+iSectionResults.get(i).getLnUnusedLastDosage());
        		
        	}
        	iAcctDosages.put((long)key,iDosage);
        	iAcctMeasureDomain.put((long)key, strMeasureDomain);
        	
        }
        
        long lnResultAcct = 0;
        long lnMax = 0;
        Set<Long> lset=iAcctDosages.keySet();
        for(Long iter :lset ){
        	Dosage d=iAcctDosages.get(iter);
            if( d.getM_lnDosage() > lnMax )
            {
                lnMax = d.getM_lnDosage();
                lnResultAcct = iter;
            }
        }
        
        
        String strMeasureDomain = iAcctMeasureDomain.get(lnResultAcct);
        System.out.println("lnResultAcct>>>>"+lnResultAcct);
        
        lnDosage += iAcctDosages.get(lnResultAcct).getM_lnDosage();
        lnChargedDosage += iAcctDosages.get(lnResultAcct).getM_lnChargedDosage();
        lnUnusedDosage += iAcctDosages.get(lnResultAcct).getM_lnUnusedDosage();
        lnRatableUnusedDosage += iAcctDosages.get(lnResultAcct).getM_lnRatableUnusedDosage();

        iRateMeasure.addChargedDosage(iAcctDosages.get(lnResultAcct).getM_lnDosage());
        iRateMeasure.addUnusedDosage(strMeasureDomain, iAcctDosages.get(lnResultAcct).getM_lnUnusedDosage());

        iRateMeasure.addChargedLastDosage(strMeasureDomain, iAcctDosages.get(lnResultAcct).getM_lnLastDosage());
        iRateMeasure.addUnusedLastDosage(strMeasureDomain, iAcctDosages.get(lnResultAcct).getM_lnUnusedLastDosage());
    }
	
	
	int getUnchargeDosage() {
		return lnDosage - lnChargedDosage;
	}

	void addChargedDosage(int dosage_) {
		lnChargedDosage += dosage_;
	}

	public void addSectionRateResult(SectionRateData result_) {

		lnDosage += result_.getLnDosage();
		lnChargedDosage += result_.getLnChargedDosage();
		lnUnusedDosage += result_.getLnUnusedDosage();
		lnRatableUnusedDosage += result_.getLnRatableUnusedDosage();
		
		
		iRateMeasure.addChargedDosage( result_.getLnDosage());
		iRateMeasure.addUnusedDosage(result_.getStrMeasureDomain(), result_.getLnUnusedDosage());

		iRateMeasure.addChargedLastDosage(result_.getStrMeasureDomain(), result_.getLnLastDosage());
		iRateMeasure.addUnusedLastDosage(result_.getStrMeasureDomain(), result_.getLnUnusedLastDosage());
	}


//	void calcRateMeasure() {
//
//		Map iAcctSecResults;
//		Map iAcctDosages;
//		Map iAcctMeasureDomain;
//
//		int lnResultAcct = 0;
//		int lnMax = 0;
//
//	}

	public int getRateUnit() {
		int lnRateUnit = 0;

		return lnRateUnit;
	}

	public double getRateValue() {
		double dRateValue = 0.0;

		return dRateValue;
	}

	public double sum() {
		double sum = 0.0;
		for(SectionRateData sectionRateData :iSectionResults){
			sum =sum+sectionRateData.getdFee();
		}

		return sum;
	}

	public int getnOfrId() {
		return nOfrId;
	}

	public void setnOfrId(int nOfrId) {
		this.nOfrId = nOfrId;
	}

	public int getLnAtomOfrId() {
		return lnAtomOfrId;
	}

	public void setLnAtomOfrId(int lnAtomOfrId) {
		this.lnAtomOfrId = lnAtomOfrId;
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

	public List<SectionRateData> getiSectionResults() {
		return iSectionResults;
	}

	public void setiSectionResults(List<SectionRateData> iSectionResults) {
		this.iSectionResults = iSectionResults;
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


	public int getOfrRateResultFlag() {
		return ofrRateResultFlag;
	}


	public void setOfrRateResultFlag(int ofrRateResultFlag) {
		this.ofrRateResultFlag = ofrRateResultFlag;
	}

	public boolean isDirect() {
		return isDirect;
	}

	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}

	public void print(){
		System.out.println("nOfrId>>>"+nOfrId);
		System.out.println("lnAtomOfrId>>>"+lnAtomOfrId);
		System.out.println("lnDosage>>>"+lnDosage);
		System.out.println("lnChargedDosage>>>"+lnChargedDosage);
		System.out.println("lnUnusedDosage>>>"+lnUnusedDosage);
		System.out.println("lnRatableUnusedDosage>>>"+lnRatableUnusedDosage);
		System.out.println("ofrRateResultFlag>>>"+ofrRateResultFlag);
		System.out.println("iRatableValues.size"+iRatableValues.size());
		System.out.println("iSectionResults.size>>>"+iSectionResults.size());
		System.out.println("iRateMeasure>>>"+iRateMeasure);
		
		
	}
	
	
}
