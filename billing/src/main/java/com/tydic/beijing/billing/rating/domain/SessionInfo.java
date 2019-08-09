package com.tydic.beijing.billing.rating.domain;

public class SessionInfo {

    private long m_lnSessionInfoId;
    private long m_lnPrdInstId;
    private long m_lnUnUsedAmount;   //未实扣值
    private long m_lnUsedAmount;      //累计实扣值
    private long m_lnReservedAmount;//预占值
    private int  m_nLatnId;
    private int  m_nUnitType;

    private long m_lnCCGSPStart; //费率切换点前使用量B307
    private long m_lnCCGSPEnd;   //费率切换点后使用量B308
    private long m_lnUsedDuration;     //已用时长
    private long m_lnUsedTimes;        //已用次数
    private long m_lnUsedUpVolume;     //已用上行流量
    private long m_lnUsedDownVolume;   //已用下行流量
    private long m_lnUsedTotalVolume;  //已用总流量

    private String  m_strSessionId;
    private String  m_strServiceNbr;
    private String  m_strAreaCode;
    private String  m_strModifyTime;

    private String  m_strUnusedMoneyInfo;
	  private String  m_strPayPlanInfo;
	  private String  m_strPricingPlanInfo;
    
    //NOTE:ServInfo字段，暂时不用
    private long m_lnServId;      //用户标识
    private long m_lnAcctId;      //帐户标识
    private long m_lnCustId;      //客户标识
    private int  m_nOfrId;       //基础销售品
    private int  m_nLatnId1;      //本地网标识
    private long m_lnExchId;      //局向
    private String  m_strUserTypeId;  //用户类型
    private String  m_strUrbanFlag;   //城乡标识
    private String  m_strRegionId;    //区域标识
    private String  m_strIFIVPN;      //是否参加IVPN
    private String  m_strBasicState;
    private String  m_strAcceptDate; //受理日期
	public long getM_lnSessionInfoId() {
		return m_lnSessionInfoId;
	}
	public void setM_lnSessionInfoId(long m_lnSessionInfoId) {
		this.m_lnSessionInfoId = m_lnSessionInfoId;
	}
	public long getM_lnPrdInstId() {
		return m_lnPrdInstId;
	}
	public void setM_lnPrdInstId(long m_lnPrdInstId) {
		this.m_lnPrdInstId = m_lnPrdInstId;
	}
	public long getM_lnUnUsedAmount() {
		return m_lnUnUsedAmount;
	}
	public void setM_lnUnUsedAmount(long m_lnUnUsedAmount) {
		this.m_lnUnUsedAmount = m_lnUnUsedAmount;
	}
	public long getM_lnUsedAmount() {
		return m_lnUsedAmount;
	}
	public void setM_lnUsedAmount(long m_lnUsedAmount) {
		this.m_lnUsedAmount = m_lnUsedAmount;
	}
	public long getM_lnReservedAmount() {
		return m_lnReservedAmount;
	}
	public void setM_lnReservedAmount(long m_lnReservedAmount) {
		this.m_lnReservedAmount = m_lnReservedAmount;
	}
	public int getM_nLatnId() {
		return m_nLatnId;
	}
	public void setM_nLatnId(int m_nLatnId) {
		this.m_nLatnId = m_nLatnId;
	}
	public int getM_nUnitType() {
		return m_nUnitType;
	}
	public void setM_nUnitType(int m_nUnitType) {
		this.m_nUnitType = m_nUnitType;
	}
	public long getM_lnCCGSPStart() {
		return m_lnCCGSPStart;
	}
	public void setM_lnCCGSPStart(long m_lnCCGSPStart) {
		this.m_lnCCGSPStart = m_lnCCGSPStart;
	}
	public long getM_lnCCGSPEnd() {
		return m_lnCCGSPEnd;
	}
	public void setM_lnCCGSPEnd(long m_lnCCGSPEnd) {
		this.m_lnCCGSPEnd = m_lnCCGSPEnd;
	}
	public long getM_lnUsedDuration() {
		return m_lnUsedDuration;
	}
	public void setM_lnUsedDuration(long m_lnUsedDuration) {
		this.m_lnUsedDuration = m_lnUsedDuration;
	}
	public long getM_lnUsedTimes() {
		return m_lnUsedTimes;
	}
	public void setM_lnUsedTimes(long m_lnUsedTimes) {
		this.m_lnUsedTimes = m_lnUsedTimes;
	}
	public long getM_lnUsedUpVolume() {
		return m_lnUsedUpVolume;
	}
	public void setM_lnUsedUpVolume(long m_lnUsedUpVolume) {
		this.m_lnUsedUpVolume = m_lnUsedUpVolume;
	}
	public long getM_lnUsedDownVolume() {
		return m_lnUsedDownVolume;
	}
	public void setM_lnUsedDownVolume(long m_lnUsedDownVolume) {
		this.m_lnUsedDownVolume = m_lnUsedDownVolume;
	}
	public long getM_lnUsedTotalVolume() {
		return m_lnUsedTotalVolume;
	}
	public void setM_lnUsedTotalVolume(long m_lnUsedTotalVolume) {
		this.m_lnUsedTotalVolume = m_lnUsedTotalVolume;
	}
	public String getM_strSessionId() {
		return m_strSessionId;
	}
	public void setM_strSessionId(String m_strSessionId) {
		this.m_strSessionId = m_strSessionId;
	}
	public String getM_strServiceNbr() {
		return m_strServiceNbr;
	}
	public void setM_strServiceNbr(String m_strServiceNbr) {
		this.m_strServiceNbr = m_strServiceNbr;
	}
	public String getM_strAreaCode() {
		return m_strAreaCode;
	}
	public void setM_strAreaCode(String m_strAreaCode) {
		this.m_strAreaCode = m_strAreaCode;
	}
	public String getM_strModifyTime() {
		return m_strModifyTime;
	}
	public void setM_strModifyTime(String m_strModifyTime) {
		this.m_strModifyTime = m_strModifyTime;
	}
	public String getM_strUnusedMoneyInfo() {
		return m_strUnusedMoneyInfo;
	}
	public void setM_strUnusedMoneyInfo(String m_strUnusedMoneyInfo) {
		this.m_strUnusedMoneyInfo = m_strUnusedMoneyInfo;
	}
	public String getM_strPayPlanInfo() {
		return m_strPayPlanInfo;
	}
	public void setM_strPayPlanInfo(String m_strPayPlanInfo) {
		this.m_strPayPlanInfo = m_strPayPlanInfo;
	}
	public String getM_strPricingPlanInfo() {
		return m_strPricingPlanInfo;
	}
	public void setM_strPricingPlanInfo(String m_strPricingPlanInfo) {
		this.m_strPricingPlanInfo = m_strPricingPlanInfo;
	}
	public long getM_lnServId() {
		return m_lnServId;
	}
	public void setM_lnServId(long m_lnServId) {
		this.m_lnServId = m_lnServId;
	}
	public long getM_lnAcctId() {
		return m_lnAcctId;
	}
	public void setM_lnAcctId(long m_lnAcctId) {
		this.m_lnAcctId = m_lnAcctId;
	}
	public long getM_lnCustId() {
		return m_lnCustId;
	}
	public void setM_lnCustId(long m_lnCustId) {
		this.m_lnCustId = m_lnCustId;
	}
	public int getM_nOfrId() {
		return m_nOfrId;
	}
	public void setM_nOfrId(int m_nOfrId) {
		this.m_nOfrId = m_nOfrId;
	}
	public int getM_nLatnId1() {
		return m_nLatnId1;
	}
	public void setM_nLatnId1(int m_nLatnId1) {
		this.m_nLatnId1 = m_nLatnId1;
	}
	public long getM_lnExchId() {
		return m_lnExchId;
	}
	public void setM_lnExchId(long m_lnExchId) {
		this.m_lnExchId = m_lnExchId;
	}
	public String getM_strUserTypeId() {
		return m_strUserTypeId;
	}
	public void setM_strUserTypeId(String m_strUserTypeId) {
		this.m_strUserTypeId = m_strUserTypeId;
	}
	public String getM_strUrbanFlag() {
		return m_strUrbanFlag;
	}
	public void setM_strUrbanFlag(String m_strUrbanFlag) {
		this.m_strUrbanFlag = m_strUrbanFlag;
	}
	public String getM_strRegionId() {
		return m_strRegionId;
	}
	public void setM_strRegionId(String m_strRegionId) {
		this.m_strRegionId = m_strRegionId;
	}
	public String getM_strIFIVPN() {
		return m_strIFIVPN;
	}
	public void setM_strIFIVPN(String m_strIFIVPN) {
		this.m_strIFIVPN = m_strIFIVPN;
	}
	public String getM_strBasicState() {
		return m_strBasicState;
	}
	public void setM_strBasicState(String m_strBasicState) {
		this.m_strBasicState = m_strBasicState;
	}
	public String getM_strAcceptDate() {
		return m_strAcceptDate;
	}
	public void setM_strAcceptDate(String m_strAcceptDate) {
		this.m_strAcceptDate = m_strAcceptDate;
	}
    
    
    public void sessionInit(SessionInformationExt session){
    	this.m_lnSessionInfoId=session.getSession_Info_Id();
    	this.m_lnUnUsedAmount = session.getUnused_Amount();
        this.m_lnUsedAmount = session.getUsed_Amount();
        this.m_lnReservedAmount = session.getReserved_Amount();
        this.m_nUnitType = (int)session.getUnit_Type();
        this.m_strServiceNbr = session.getService_Nbr();
        this.m_strAreaCode = session.getArea_Code();
        this.m_strModifyTime = session.getModify_Time();
        
      //保存在会话表中的用户信息,暂时没用
        this.m_lnServId = session.getPrd_Inst_Id();
        this.m_lnAcctId = session.getAcct_Id();
        this.m_lnCustId = session.getCust_Id();
        this.m_nOfrId = (int)session.getOfr_Id();
        this.m_nLatnId1 = (int)session.getLatn_Id();
        this.m_lnExchId = session.getExch_Id();
        this.m_strUserTypeId = session.getUser_Type_Id();
        this.m_strUrbanFlag = session.getUrban_Flag();
        this.m_strRegionId = session.getRegion_Id();
        this.m_strIFIVPN = session.getIf_Ivpn();
        this.m_strBasicState = session.getBasic_State();
        this.m_strAcceptDate = session.getAccept_Date();
        
        if (this.m_nUnitType == RatingMacro.CREDITUNIT_TOTALVAL
                ||this.m_nUnitType == RatingMacro.CREDITUNIT_UPVAL
                ||this.m_nUnitType == RatingMacro.CREDITUNIT_DOWNVAL)
            {
        	this.m_lnCCGSPStart = session.getSwitchpoint_Start();
        	this.m_lnCCGSPEnd = session.getSwitchpoint_End();
            }
        this.m_lnUsedDuration = session.getUsed_Duration();
        this.m_lnUsedTimes = session.getUsed_Times();
        this.m_lnUsedUpVolume = session.getUsed_Upvolume();
        this.m_lnUsedDownVolume = session.getUsed_Downvolume();
        this.m_lnUsedTotalVolume = session.getUsed_Totalvolume();

        this.m_strSessionId = session.getSession_Id();
        this.m_lnPrdInstId = session.getPrd_Inst_Id();
        this.m_nLatnId = (int)session.getLatn_Id();

        this.m_strPayPlanInfo = session.getPay_Plan_Info();
        this.m_strPricingPlanInfo = session.getPricing_Plan_Info();
        this.m_strUnusedMoneyInfo = session.getUnused_Money();
        
    }
    
    
}
