package com.tydic.beijing.billing.rating.domain;

public class FeeInfo {
	
	private   long lnCurrOfrId;                 //B071  商品ID
	private   long lnMoney;                      //B072  商品费用

    void init()
    {
        lnCurrOfrId = -1;
        lnMoney = 0;
    }

	public long getLnCurrOfrId() {
		return lnCurrOfrId;
	}

	public void setLnCurrOfrId(long lnCurrOfrId) {
		this.lnCurrOfrId = lnCurrOfrId;
	}

	public long getLnMoney() {
		return lnMoney;
	}

	public void setLnMoney(long lnMoney) {
		this.lnMoney = lnMoney;
	}
    
    /**
     * @author sung
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
    	if(this==obj){
    		return true;
    	}
    	if(obj==null){
    		return false;
    	}
    	if(getClass() !=obj.getClass()){
    		
    		return false;
    	}
    	
    	final FeeInfo other=(FeeInfo)obj;
    	if(lnCurrOfrId==other.getLnCurrOfrId()){
    		return true;
    	}else{
    		return false;
    	}
    	    	
    }
    
    

}
