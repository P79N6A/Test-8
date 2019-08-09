package com.tydic.beijing.billing.rating.domain;

public class RatableResourceValue {
	private  int m_nLatnId=0;
	private  String m_lnOwnerId="";
	private  int m_lnRatableCycleId=-1;
	private  long m_lnBalance=0;
	private  String m_strOwnerType="";
	private  String m_strRatableResourceCode="";
	
	public RatableResourceValue(){
		
	}
	
	public RatableResourceValue(int latn,String ownerId,int cycleId,int balance,String ownerType,String resourceCode){
		this.m_nLatnId=latn;
		this.m_lnOwnerId=ownerId;
		this.m_lnBalance=balance;
		this.m_strOwnerType=ownerType;
		this.m_strRatableResourceCode=resourceCode;
		this.m_lnRatableCycleId=cycleId;
	}
	
	public int getM_nLatnId() {
		return m_nLatnId;
	}
	public void setM_nLatnId(int m_nLatnId) {
		this.m_nLatnId = m_nLatnId;
	}
	public String getM_lnOwnerId() {
		return m_lnOwnerId;
	}
	public void setM_lnOwnerId(String m_lnOwnerId) {
		this.m_lnOwnerId = m_lnOwnerId;
	}
	public int getM_lnRatableCycleId() {
		return m_lnRatableCycleId;
	}
	public void setM_lnRatableCycleId(int m_lnRatableCycleId) {
		this.m_lnRatableCycleId = m_lnRatableCycleId;
	}
	public long getM_lnBalance() {
		return m_lnBalance;
	}
	public void setM_lnBalance(long m_lnBalance) {
		this.m_lnBalance = m_lnBalance;
	}
	public String getM_strOwnerType() {
		return m_strOwnerType;
	}
	public void setM_strOwnerType(String m_strOwnerType) {
		this.m_strOwnerType = m_strOwnerType;
	}
	public String getM_strRatableResourceCode() {
		return m_strRatableResourceCode;
	}
	public void setM_strRatableResourceCode(String m_strRatableResourceCode) {
		this.m_strRatableResourceCode = m_strRatableResourceCode;
	}

	public void add(long value){
		m_lnBalance+=value;
	}
	
	
    @Override
    public String toString() {
    	String str="m_nLatnId["+m_nLatnId+"],m_lnOwnerId["+m_lnOwnerId+"],m_lnRatableCycleId["+m_lnRatableCycleId+"],m_lnBalance["+
    			m_lnBalance+"],m_strOwnerType["+m_strOwnerType+"],m_strRatableResourceCode["+m_strRatableResourceCode+"]";
    	
    	return str;
    }
    
    
}
