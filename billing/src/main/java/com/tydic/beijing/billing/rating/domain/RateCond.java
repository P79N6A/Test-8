/**
 * 
 */
package com.tydic.beijing.billing.rating.domain;

/**
 * @author sung
 *
 */
public class RateCond {

	private int m_nComOperators;
    private long m_lnGroupId;
    private String m_strSourceType;
    private String  m_strValueType;
    private String  m_strComType;
    private String  m_strItemCode;
    private String  m_strItemValue;
    
    public RateCond(){}
    
    public RateCond(RuleRateCondition rule){
    	this.m_nComOperators=(int)rule.getCom_operators();
    	this.m_lnGroupId=rule.getGroup_id();
    	this.m_strComType=rule.getCom_type();
    	this.m_strItemCode=rule.getItem_code();
    	this.m_strItemValue=rule.getItem_value();
    	
    }
	public int getM_nComOperators() {
		return m_nComOperators;
	}
	public void setM_nComOperators(int m_nComOperators) {
		this.m_nComOperators = m_nComOperators;
	}
	public long getM_lnGroupId() {
		return m_lnGroupId;
	}
	public void setM_lnGroupId(long m_lnGroupId) {
		this.m_lnGroupId = m_lnGroupId;
	}
	public String getM_strSourceType() {
		return m_strSourceType;
	}
	public void setM_strSourceType(String m_strSourceType) {
		this.m_strSourceType = m_strSourceType;
	}
	public String getM_strValueType() {
		return m_strValueType;
	}
	public void setM_strValueType(String m_strValueType) {
		this.m_strValueType = m_strValueType;
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
    
    
    
    
}
