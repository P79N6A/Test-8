package com.tydic.beijing.billing.rating.domain;

public class RequestRatableUpdate {

	private String m_strOwnerType;
    private long m_lnOwnerId;
    private int m_nLatnId;
    private String m_strRatableResourceCode;
    private long m_lnRatableCycleId;
    private long m_lnBalance;
	public String getM_strOwnerType() {
		return m_strOwnerType;
	}
	public void setM_strOwnerType(String m_strOwnerType) {
		this.m_strOwnerType = m_strOwnerType;
	}
	public long getM_lnOwnerId() {
		return m_lnOwnerId;
	}
	public void setM_lnOwnerId(long m_lnOwnerId) {
		this.m_lnOwnerId = m_lnOwnerId;
	}
	public int getM_nLatnId() {
		return m_nLatnId;
	}
	public void setM_nLatnId(int m_nLatnId) {
		this.m_nLatnId = m_nLatnId;
	}
	public String getM_strRatableResourceCode() {
		return m_strRatableResourceCode;
	}
	public void setM_strRatableResourceCode(String m_strRatableResourceCode) {
		this.m_strRatableResourceCode = m_strRatableResourceCode;
	}
	public long getM_lnRatableCycleId() {
		return m_lnRatableCycleId;
	}
	public void setM_lnRatableCycleId(long m_lnRatableCycleId) {
		this.m_lnRatableCycleId = m_lnRatableCycleId;
	}
	public long getM_lnBalance() {
		return m_lnBalance;
	}
	public void setM_lnBalance(long m_lnBalance) {
		this.m_lnBalance = m_lnBalance;
	}
    
    
    
}
