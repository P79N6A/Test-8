package com.tydic.beijing.billing.rating.domain;

public class RatableResourceInfo {///code_ratable_resource 
	private  String m_strRatableResourceCode;
	private  String m_strDateType="1"; //0：按天 1：按月
	private  String m_strLifeType; //生存期类型： 0：按天 1：按账期 2:半年
	private  String m_strRefType; //R0A：取平均值（不包括当前累计周期） R0B：取和（不包括当前累计周期） R0C：取单一值 R0D：取平均值（包括当前累计周期） R0E：取和（包括当前累计周期
	private  int m_nRefOffset;
	private  int m_nStartValue;
	private  int m_nEndValue;
	private  String m_strRatableResourceType; //1－时长(秒)； 2－时长(分钟)；3－次数；4－总流量(k)；5－分(金额)；7－上行流量按K；8－下行流量 
	private  String m_strRatableOwnerType;//累计量属主类型:80A 事件80I  客户80J  帐户
	
	public RatableResourceInfo(){}
	
	public RatableResourceInfo(CodeRatableResource info){
		this.m_strRatableResourceCode=info.getRatable_resource_code();
		this.m_strDateType=info.getDate_type();
		this.m_strLifeType=""+info.getLife_type();
		this.m_strRefType=info.getRef_type();
		this.m_nRefOffset=info.getRef_offset();
		this.m_nStartValue=info.getStart_value();
		this.m_nEndValue=info.getEnd_value();
		this.m_strRatableResourceType=""+info.getRatable_resource_type();
		this.m_strRatableOwnerType=info.getOwner_type();
		
	}
	
	public String getM_strRatableResourceCode() {
		return m_strRatableResourceCode;
	}
	public void setM_strRatableResourceCode(String m_strRatableResourceCode) {
		this.m_strRatableResourceCode = m_strRatableResourceCode;
	}
	public String getM_strDateType() {
		return m_strDateType;
	}
	public void setM_strDateType(String m_strDateType) {
		this.m_strDateType = m_strDateType;
	}
	public String getM_strLifeType() {
		return m_strLifeType;
	}
	public void setM_strLifeType(String m_strLifeType) {
		this.m_strLifeType = m_strLifeType;
	}
	public String getM_strRefType() {
		return m_strRefType;
	}
	public void setM_strRefType(String m_strRefType) {
		this.m_strRefType = m_strRefType;
	}
	public int getM_nRefOffset() {
		return m_nRefOffset;
	}
	public void setM_nRefOffset(int m_nRefOffset) {
		this.m_nRefOffset = m_nRefOffset;
	}
	public int getM_nStartValue() {
		return m_nStartValue;
	}
	public void setM_nStartValue(int m_nStartValue) {
		this.m_nStartValue = m_nStartValue;
	}
	public int getM_nEndValue() {
		return m_nEndValue;
	}
	public void setM_nEndValue(int m_nEndValue) {
		this.m_nEndValue = m_nEndValue;
	}
	public String getM_strRatableResourceType() {
		return m_strRatableResourceType;
	}
	public void setM_strRatableResourceType(String m_strRatableResourceType) {
		this.m_strRatableResourceType = m_strRatableResourceType;
	}
	public String getM_strRatableOwnerType() {
		return m_strRatableOwnerType;
	}
	public void setM_strRatableOwnerType(String m_strRatableOwnerType) {
		this.m_strRatableOwnerType = m_strRatableOwnerType;
	}
	
    public void print(){
    	System.out.println("RatableResourceCode["+m_strRatableResourceCode+"]\n"+"DateType["+m_strDateType+"]\n"+"LifeType["+m_strLifeType+"]\n"+
    			"RefType["+m_strRefType+"]\n"+"RefOffset["+m_nRefOffset+"]\n"+"StartValue["+m_nStartValue+"]\n"+
    			"EndValue["+m_nEndValue+"]\nRatableResourceType["+m_strRatableResourceType+"]\nm_strRatableOwnerType["+m_strRatableOwnerType+"]\n");
    	
    	
    	
    	
    }
}
