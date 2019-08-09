package com.tydic.beijing.billing.rating.domain;
/**
 * 计费可变信息，在整个会话的每次RB请求中都会变化。由代码名称和值组成，格式如：[002=0]，或者[R01=1]
 * @author zhanghengbo
 *
 */
public class VarMsg {
		
    private String m_strResendFlag ="";         //重发标记
    private String  m_strLastTime ="";               //会话上次实际扣费开始时间（计算实际扣费）
    private String  m_strCurrTime ="";               //本次计费请求开始时间（计算预扣费用）
    private int    m_nRatableFlag ;            //是否进行使用量累计标识
    private int    m_nBillingFlag ;            //计费类型
    private int    m_nActiveUserFlag ;               //是否需要激活，1－需要，其它－不需要

    private int    m_nVPNType ;                      //vpn类型,0.网内;1.网间;2.非VPN,默认值2
    private int    m_nRelationNbr;                  //是否是亲情号码，0:否,1:是,默认值0
    private int   m_lnCallingGroupId ;               //主叫ivpn-id，返回给CM的R612
    private int   m_lnCalledGroupId;                //被叫ivpn-id，返回给CM的R613
    private int   m_nAcctItemType ;               //更加R402查找账目类型
	public String getM_strResendFlag() {
		return m_strResendFlag;
	}
	public void setM_strResendFlag(String m_strResendFlag) {
		this.m_strResendFlag = m_strResendFlag;
	}
	public String getM_strLastTime() {
		return m_strLastTime;
	}
	public void setM_strLastTime(String m_strLastTime) {
		this.m_strLastTime = m_strLastTime;
	}
	public String getM_strCurrTime() {
		return m_strCurrTime;
	}
	public void setM_strCurrTime(String m_strCurrTime) {
		this.m_strCurrTime = m_strCurrTime;
	}
	public int getM_nRatableFlag() {
		return m_nRatableFlag;
	}
	public void setM_nRatableFlag(int m_nRatableFlag) {
		this.m_nRatableFlag = m_nRatableFlag;
	}
	public int getM_nBillingFlag() {
		return m_nBillingFlag;
	}
	public void setM_nBillingFlag(int m_nBillingFlag) {
		this.m_nBillingFlag = m_nBillingFlag;
	}
	public int getM_nActiveUserFlag() {
		return m_nActiveUserFlag;
	}
	public void setM_nActiveUserFlag(int m_nActiveUserFlag) {
		this.m_nActiveUserFlag = m_nActiveUserFlag;
	}
	public int getM_nVPNType() {
		return m_nVPNType;
	}
	public void setM_nVPNType(int m_nVPNType) {
		this.m_nVPNType = m_nVPNType;
	}
	public int getM_nRelationNbr() {
		return m_nRelationNbr;
	}
	public void setM_nRelationNbr(int m_nRelationNbr) {
		this.m_nRelationNbr = m_nRelationNbr;
	}
	public int getM_lnCallingGroupId() {
		return m_lnCallingGroupId;
	}
	public void setM_lnCallingGroupId(int m_lnCallingGroupId) {
		this.m_lnCallingGroupId = m_lnCallingGroupId;
	}
	public int getM_lnCalledGroupId() {
		return m_lnCalledGroupId;
	}
	public void setM_lnCalledGroupId(int m_lnCalledGroupId) {
		this.m_lnCalledGroupId = m_lnCalledGroupId;
	}
	public int getM_nAcctItemType() {
		return m_nAcctItemType;
	}
	public void setM_nAcctItemType(int m_nAcctItemType) {
		this.m_nAcctItemType = m_nAcctItemType;
	}
    
    public void print(){
    	    //System.out.println("start print varmsg:");
    	   if(m_strResendFlag != null && m_strResendFlag .length()>0       ) System.out.println("varMsg.m_strResendFlag    ="+m_strResendFlag    );      //重发标记
    	   if(m_strLastTime   != null && m_strLastTime   .length()>0       ) System.out.println("varMsg.m_strLastTime      ="+m_strLastTime      );          //会话上次实际扣费开始时间（计算实际扣费）
    	   if(m_strCurrTime   != null && m_strCurrTime   .length()>0       ) System.out.println("varMsg.m_strCurrTime      ="+m_strCurrTime      );           //本次计费请求开始时间（计算预扣费用）
    	   
    	             System.out.println("varMsg.   m_nActiveUserFlag      ="+   m_nActiveUserFlag      );                     
    	   			     System.out.println("varMsg.   m_nVPNType             ="+   m_nVPNType              );          
    	   			     System.out.println("varMsg.   m_nRelationNbr     ="+       m_nRelationNbr      );                  
    	   			     System.out.println("varMsg.  m_lnCallingGroupId      ="+  m_lnCallingGroupId           );  
    	   			     System.out.println("varMsg.  m_lnCalledGroupId       ="+  m_lnCalledGroupId         );        
    	   			     System.out.println("varMsg.  m_nAcctItemType         ="+  m_nAcctItemType              );  
    	   			  
    }

}
