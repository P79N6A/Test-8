package com.tydic.beijing.billing.rating.domain;

public class ExtMsg {

	private    long          m_nEvtTypeId;              //事件类型
	private     String  m_strCallingAreaId;        //主叫营业区
	private     String  m_strCalledAreaId;         //被叫营业区
	private     String  m_strCallingSpeNbrState;   //主叫特服计费状态
	private     String  m_strCalledSpeNbrState;    //被叫特服计费状态
	private     String  m_strBillingAreaCode;      //计费号码区号
	private     String  m_strBillingNbr;           //计费              号码
	private    boolean         m_bFreeFlag =false;               //释放标识,false:不需要释放
	private    boolean         m_bReChargeFlag =false;           //充值标识,false:不需要充值
	 
	private    int          m_nPoCIMServerRole;        //POC,IM服务器角色  R701                 R801
	private    int          m_nPoCIMSessionType;       //POC,IM会话类型    R702                 R802
	private    int          m_nSessionParticipateNum;  //参与者人数        R703                 R803
	private    int          m_nIMFeatureType;          //IM业务特征类型    R804
	private     String  m_strServiceEnableType;    //ISMP              Service-Enable-Type  R402
	public long getM_nEvtTypeId() {
		return m_nEvtTypeId;
	}
	public void setM_nEvtTypeId(long m_nEvtTypeId) {
		this.m_nEvtTypeId = m_nEvtTypeId;
	}
	public String getM_strCallingAreaId() {
		return m_strCallingAreaId;
	}
	public void setM_strCallingAreaId(String m_strCallingAreaId) {
		this.m_strCallingAreaId = m_strCallingAreaId;
	}
	public String getM_strCalledAreaId() {
		return m_strCalledAreaId;
	}
	public void setM_strCalledAreaId(String m_strCalledAreaId) {
		this.m_strCalledAreaId = m_strCalledAreaId;
	}
	public String getM_strCallingSpeNbrState() {
		return m_strCallingSpeNbrState;
	}
	public void setM_strCallingSpeNbrState(String m_strCallingSpeNbrState) {
		this.m_strCallingSpeNbrState = m_strCallingSpeNbrState;
	}
	public String getM_strCalledSpeNbrState() {
		return m_strCalledSpeNbrState;
	}
	public void setM_strCalledSpeNbrState(String m_strCalledSpeNbrState) {
		this.m_strCalledSpeNbrState = m_strCalledSpeNbrState;
	}
	public String getM_strBillingAreaCode() {
		return m_strBillingAreaCode;
	}
	public void setM_strBillingAreaCode(String m_strBillingAreaCode) {
		this.m_strBillingAreaCode = m_strBillingAreaCode;
	}
	public String getM_strBillingNbr() {
		return m_strBillingNbr;
	}
	public void setM_strBillingNbr(String m_strBillingNbr) {
		this.m_strBillingNbr = m_strBillingNbr;
	}
	public int getM_nPoCIMServerRole() {
		return m_nPoCIMServerRole;
	}
	public void setM_nPoCIMServerRole(int m_nPoCIMServerRole) {
		this.m_nPoCIMServerRole = m_nPoCIMServerRole;
	}
	public int getM_nPoCIMSessionType() {
		return m_nPoCIMSessionType;
	}
	public void setM_nPoCIMSessionType(int m_nPoCIMSessionType) {
		this.m_nPoCIMSessionType = m_nPoCIMSessionType;
	}
	public int getM_nSessionParticipateNum() {
		return m_nSessionParticipateNum;
	}
	public void setM_nSessionParticipateNum(int m_nSessionParticipateNum) {
		this.m_nSessionParticipateNum = m_nSessionParticipateNum;
	}
	public int getM_nIMFeatureType() {
		return m_nIMFeatureType;
	}
	public void setM_nIMFeatureType(int m_nIMFeatureType) {
		this.m_nIMFeatureType = m_nIMFeatureType;
	}
	public boolean isM_bFreeFlag() {
		return m_bFreeFlag;
	}
	public void setM_bFreeFlag(boolean m_bFreeFlag) {
		this.m_bFreeFlag = m_bFreeFlag;
	}
	public boolean isM_bReChargeFlag() {
		return m_bReChargeFlag;
	}
	public void setM_bReChargeFlag(boolean m_bReChargeFlag) {
		this.m_bReChargeFlag = m_bReChargeFlag;
	}
	
	public String getM_strServiceEnableType() {
		return m_strServiceEnableType;
	}
	public void setM_strServiceEnableType(String m_strServiceEnableType) {
		this.m_strServiceEnableType = m_strServiceEnableType;
	}

	public void print(){

		if( m_strServiceEnableType !=null && m_strServiceEnableType .length()>0) System.out.println("extMsg.m_strServiceEnableType ="+m_strServiceEnableType )  ;    //ISMP              Service-Enable-Type  R402  
		if( m_strCallingAreaId     !=null && m_strCallingAreaId     .length()>0) System.out.println("extMsg.m_strCallingAreaId     ="+m_strCallingAreaId     )  ;    //主叫营业区
		if( m_strCalledAreaId      !=null && m_strCalledAreaId      .length()>0) System.out.println("extMsg.m_strCalledAreaId      ="+m_strCalledAreaId      )  ;    //被叫营业区
		if( m_strCallingSpeNbrState!=null && m_strCallingSpeNbrState.length()>0) System.out.println("extMsg.m_strCallingSpeNbrState="+m_strCallingSpeNbrState)  ;    //主叫特服计费状态
		if( m_strCalledSpeNbrState !=null && m_strCalledSpeNbrState .length()>0) System.out.println("extMsg.m_strCalledSpeNbrState ="+m_strCalledSpeNbrState )  ;    //被叫特服计费状态
		if( m_strBillingAreaCode   !=null && m_strBillingAreaCode   .length()>0) System.out.println("extMsg.m_strBillingAreaCode   ="+m_strBillingAreaCode   )  ;    //计费号码区号
		if( m_strBillingNbr        !=null && m_strBillingNbr        .length()>0) System.out.println("extMsg.m_strBillingNbr        ="+m_strBillingNbr        )  ;    //计费              号码
		      	       
	System.out.println("extMsg.m_bFreeFlag =false        ="+m_bFreeFlag         );        //释放标识,false:不需要释放
	System.out.println("extMsg.m_bReChargeFlag =false    ="+m_bReChargeFlag     );        //充值标识,false:不需要充值
	System.out.println("extMsg.m_nEvtTypeId              ="+m_nEvtTypeId              ); //事件类型	 
	System.out.println("extMsg.m_nPoCIMServerRole        ="+m_nPoCIMServerRole        ); //POC,IM服务器角色  R701                 R801
	System.out.println("extMsg.m_nPoCIMSessionType       ="+m_nPoCIMSessionType       ); //POC,IM会话类型    R702                 R802
	System.out.println("extMsg.m_nSessionParticipateNum  ="+m_nSessionParticipateNum  ); //参与者人数        R703                 R803
	System.out.println("extMsg.m_nIMFeatureType          ="+m_nIMFeatureType          ); //IM业务特征类型    R804

	}
	
}
