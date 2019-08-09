/**
 * 
 */
package com.tydic.beijing.billing.rating.domain;

/**
 * @author sung
 *
 */
public class RatableResourceCond {

	
	private char m_nComOperators;
    private long m_lnGroupId;
    private long m_lnGroupSeq;
    private String m_strComType="";
    private String m_strItemCode=""; //record_type
    private String m_strItemValue="";
    private String m_strResourceCode="";
    
    
    public RatableResourceCond(){}
    
    public RatableResourceCond(RuleRatableCond cond){
    	this.m_nComOperators=cond.getCom_operators();
    	this.m_lnGroupId=cond.getGroup_id();
    	this.m_lnGroupSeq=cond.getGroup_seq();
    	this.m_strComType=cond.getCom_type();
    	this.m_strItemCode=cond.getRecord_type();
    	this.m_strItemValue=cond.getItem_value();
    	this.m_strResourceCode=cond.getResource_code();
    	
    }
	public int getM_nComOperators() {
		return m_nComOperators;
	}
	public void setM_nComOperators(char m_nComOperators) {
		this.m_nComOperators = m_nComOperators;
	}
	public long getM_lnGroupId() {
		return m_lnGroupId;
	}
	public void setM_lnGroupId(long m_lnGroupId) {
		this.m_lnGroupId = m_lnGroupId;
	}
	public long getM_lnGroupSeq() {
		return m_lnGroupSeq;
	}
	public void setM_lnGroupSeq(long m_lnGroupSeq) {
		this.m_lnGroupSeq = m_lnGroupSeq;
	}
	public String getM_strComType() {
		return m_strComType;
	}
	public void setM_strComType(String m_strComType) {
		this.m_strComType = m_strComType;
	}
	public String getM_strItemCode() {
		return m_strItemCode;
	}
	public void setM_strItemCode(String m_strItemCode) {
		this.m_strItemCode = m_strItemCode;
	}
	public String getM_strItemValue() {
		return m_strItemValue;
	}
	public void setM_strItemValue(String m_strItemValue) {
		this.m_strItemValue = m_strItemValue;
	}
	public String getM_strResourceCode() {
		return m_strResourceCode;
	}
	public void setM_strResourceCode(String m_strResourceCode) {
		this.m_strResourceCode = m_strResourceCode;
	}
    
    
    
    
}
