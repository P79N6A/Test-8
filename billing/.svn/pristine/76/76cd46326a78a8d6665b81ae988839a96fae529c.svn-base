package com.tydic.beijing.billing.rating.domain;
/**
 * 计费基本信息,在整个会话中不会更改，CM在处理过程中可以以封装好的字符串存储在session store中。
由代码名称和值组成，格式如：[000=1234567]，或者[R02=1888888888]
 * @author zhanghengbo
 *
 */
public class BaseMsg {

	  private  String m_strMsgType ="";                //message m_nType
	  private  String m_strEvtTimeStamp ="";           //EventTimeStamp
	  private  String m_strSessionId ="";              //会话ID
	  private  String m_strRequstId ="";               //请求序列号
	  private  String m_strCallingHomeAreaCode ="";    //主叫(或连接)号码归属费率区
	  private  String m_strCallingVisitAreaCode ="";   //主叫(或连接)号码拜访费率区
	  private  String m_strCallingPartner ="";     //主叫(或连接)号码归属运营商
	  private  String m_strCallingNbr ="";         //主叫号码
	  private  String m_strCalledHomeAreaCode =""; //被叫号码归属费率区
	  private  String m_strCalledVisitAreaCode ="";    //被叫号码拜访费率区
	  private  String m_strCalledPartner ="";          //被叫号码归属运营商
	  private  String m_strCalledNbr ="";              //被叫号码
	  private  String m_strAccessNbr ="";              //接入号码
	  private  String m_strStartTime ="";              //会话开始时间
	 
	  private  String m_strCallingType ="";            //话单类型
	  private  String m_strThirdNbr ="";               //第三方号码
	  private  String m_strPayFlag ="";                //用户付费属性标识
	  private  String m_strValidityTime ="";           //Validity Time
	  private  String m_strRatingGroup ="";            //rating group
	  private  String m_strChargedNbr ="";             //计费方号码,RB接口中的R01
	  private  String m_strChargedHomeAreaCode ="";    //计费方号码归属费率区,RB接口中的R5012
	  private  String m_strShortNbr ="";                 //被叫短号
	 
	  private  int        m_nFeeType ;                  //计费类型
	  private  boolean       m_bRatingQueryFlag =false;          //费率查询标示B20
 
	  private  String m_strCallingCell ="";            //主叫号码基站
	  private  String m_strCallingMscId ="";            //主叫号码交换机ID
	  private  String m_strCalledCell ="";            //主叫号码基站
	 
	  private  int        m_nAttrCellFlag ;             //小区优惠标识 应答消息中的R615
	  private  int        m_nReqAttrCellFlag ;          //请求消息中的R615
	 
	  private  int       m_nIsUpdateAttr ;             //是否需要更新tb_prd_prd_inst_attr 0:不需要，1：insert 2：update
	  private  int       m_nAttrCount ;                //小区优惠的统计
	  private  String  m_strAttrCode ="";              //小区标识
	  private  int      m_lnPrdAttrAccNbr;           //用户属性标识
      private  int       m_nPrdAttrLatnId ;
		   
      private  String m_strSmId ="";//短信SM_ID
	  private  String m_strISMPId ="";
	  private  String m_strProductId ="";              //ISMP-ProductId R72 R401
	  private  String m_strSpcPrdId ="";               //ISMP-SpcPrdId,产品构成ID
	  private  int m_nActiveFlag ;//计划用户标志
	  private  int m_nFmtOperType ;//离线业务类型，短线，增值等
	  
	  public String getM_strMsgType() {
		return m_strMsgType;
	}
	public void setM_strMsgType(String m_strMsgType) {
		this.m_strMsgType = m_strMsgType;
	}
	public String getM_strEvtTimeStamp() {
		return m_strEvtTimeStamp;
	}
	public void setM_strEvtTimeStamp(String m_strEvtTimeStamp) {
		this.m_strEvtTimeStamp = m_strEvtTimeStamp;
	}
	public String getM_strSessionId() {
		return m_strSessionId;
	}
	public void setM_strSessionId(String m_strSessionId) {
		this.m_strSessionId = m_strSessionId;
	}
	public String getM_strRequstId() {
		return m_strRequstId;
	}
	public void setM_strRequstId(String m_strRequstId) {
		this.m_strRequstId = m_strRequstId;
	}
	public String getM_strCallingHomeAreaCode() {
		return m_strCallingHomeAreaCode;
	}
	public void setM_strCallingHomeAreaCode(String m_strCallingHomeAreaCode) {
		this.m_strCallingHomeAreaCode = m_strCallingHomeAreaCode;
	}
	public String getM_strCallingVisitAreaCode() {
		return m_strCallingVisitAreaCode;
	}
	public void setM_strCallingVisitAreaCode(String m_strCallingVisitAreaCode) {
		this.m_strCallingVisitAreaCode = m_strCallingVisitAreaCode;
	}
	public String getM_strCallingPartner() {
		return m_strCallingPartner;
	}
	public void setM_strCallingPartner(String m_strCallingPartner) {
		this.m_strCallingPartner = m_strCallingPartner;
	}
	public String getM_strCallingNbr() {
		return m_strCallingNbr;
	}
	public void setM_strCallingNbr(String m_strCallingNbr) {
		this.m_strCallingNbr = m_strCallingNbr;
	}
	public String getM_strCalledHomeAreaCode() {
		return m_strCalledHomeAreaCode;
	}
	public void setM_strCalledHomeAreaCode(String m_strCalledHomeAreaCode) {
		this.m_strCalledHomeAreaCode = m_strCalledHomeAreaCode;
	}
	public String getM_strCalledVisitAreaCode() {
		return m_strCalledVisitAreaCode;
	}
	public void setM_strCalledVisitAreaCode(String m_strCalledVisitAreaCode) {
		this.m_strCalledVisitAreaCode = m_strCalledVisitAreaCode;
	}
	public String getM_strCalledPartner() {
		return m_strCalledPartner;
	}
	public void setM_strCalledPartner(String m_strCalledPartner) {
		this.m_strCalledPartner = m_strCalledPartner;
	}
	public String getM_strCalledNbr() {
		return m_strCalledNbr;
	}
	public void setM_strCalledNbr(String m_strCalledNbr) {
		this.m_strCalledNbr = m_strCalledNbr;
	}
	public String getM_strAccessNbr() {
		return m_strAccessNbr;
	}
	public void setM_strAccessNbr(String m_strAccessNbr) {
		this.m_strAccessNbr = m_strAccessNbr;
	}
	public String getM_strStartTime() {
		return m_strStartTime;
	}
	public void setM_strStartTime(String m_strStartTime) {
		this.m_strStartTime = m_strStartTime;
	}
	public String getM_strCallingType() {
		return m_strCallingType;
	}
	public void setM_strCallingType(String m_strCallingType) {
		this.m_strCallingType = m_strCallingType;
	}
	public String getM_strThirdNbr() {
		return m_strThirdNbr;
	}
	public void setM_strThirdNbr(String m_strThirdNbr) {
		this.m_strThirdNbr = m_strThirdNbr;
	}
	public String getM_strPayFlag() {
		return m_strPayFlag;
	}
	public void setM_strPayFlag(String m_strPayFlag) {
		this.m_strPayFlag = m_strPayFlag;
	}
	public String getM_strValidityTime() {
		return m_strValidityTime;
	}
	public void setM_strValidityTime(String m_strValidityTime) {
		this.m_strValidityTime = m_strValidityTime;
	}
	public String getM_strRatingGroup() {
		return m_strRatingGroup;
	}
	public void setM_strRatingGroup(String m_strRatingGroup) {
		this.m_strRatingGroup = m_strRatingGroup;
	}
	public String getM_strChargedNbr() {
		return m_strChargedNbr;
	}
	public void setM_strChargedNbr(String m_strChargedNbr) {
		this.m_strChargedNbr = m_strChargedNbr;
	}
	public String getM_strChargedHomeAreaCode() {
		return m_strChargedHomeAreaCode;
	}
	public void setM_strChargedHomeAreaCode(String m_strChargedHomeAreaCode) {
		this.m_strChargedHomeAreaCode = m_strChargedHomeAreaCode;
	}
	public String getM_strShortNbr() {
		return m_strShortNbr;
	}
	public void setM_strShortNbr(String m_strShortNbr) {
		this.m_strShortNbr = m_strShortNbr;
	}
	public int getM_nFeeType() {
		return m_nFeeType;
	}
	public void setM_nFeeType(int m_nFeeType) {
		this.m_nFeeType = m_nFeeType;
	}
	public boolean isM_bRatingQueryFlag() {
		return m_bRatingQueryFlag;
	}
	public void setM_bRatingQueryFlag(boolean m_bRatingQueryFlag) {
		this.m_bRatingQueryFlag = m_bRatingQueryFlag;
	}
	public String getM_strCallingCell() {
		return m_strCallingCell;
	}
	public void setM_strCallingCell(String m_strCallingCell) {
		this.m_strCallingCell = m_strCallingCell;
	}
	public String getM_strCallingMscId() {
		return m_strCallingMscId;
	}
	public void setM_strCallingMscId(String m_strCallingMscId) {
		this.m_strCallingMscId = m_strCallingMscId;
	}
	public String getM_strCalledCell() {
		return m_strCalledCell;
	}
	public void setM_strCalledCell(String m_strCalledCell) {
		this.m_strCalledCell = m_strCalledCell;
	}
	public int getM_nAttrCellFlag() {
		return m_nAttrCellFlag;
	}
	public void setM_nAttrCellFlag(int m_nAttrCellFlag) {
		this.m_nAttrCellFlag = m_nAttrCellFlag;
	}
	public int getM_nReqAttrCellFlag() {
		return m_nReqAttrCellFlag;
	}
	public void setM_nReqAttrCellFlag(int m_nReqAttrCellFlag) {
		this.m_nReqAttrCellFlag = m_nReqAttrCellFlag;
	}
	public int getM_nIsUpdateAttr() {
		return m_nIsUpdateAttr;
	}
	public void setM_nIsUpdateAttr(int m_nIsUpdateAttr) {
		this.m_nIsUpdateAttr = m_nIsUpdateAttr;
	}
	public int getM_nAttrCount() {
		return m_nAttrCount;
	}
	public void setM_nAttrCount(int m_nAttrCount) {
		this.m_nAttrCount = m_nAttrCount;
	}
	public String getM_strAttrCode() {
		return m_strAttrCode;
	}
	public void setM_strAttrCode(String m_strAttrCode) {
		this.m_strAttrCode = m_strAttrCode;
	}
	public int getM_lnPrdAttrAccNbr() {
		return m_lnPrdAttrAccNbr;
	}
	public void setM_lnPrdAttrAccNbr(int m_lnPrdAttrAccNbr) {
		this.m_lnPrdAttrAccNbr = m_lnPrdAttrAccNbr;
	}
	public int getM_nPrdAttrLatnId() {
		return m_nPrdAttrLatnId;
	}
	public void setM_nPrdAttrLatnId(int m_nPrdAttrLatnId) {
		this.m_nPrdAttrLatnId = m_nPrdAttrLatnId;
	}
	public String getM_strSmId() {
		return m_strSmId;
	}
	public void setM_strSmId(String m_strSmId) {
		this.m_strSmId = m_strSmId;
	}
	public String getM_strISMPId() {
		return m_strISMPId;
	}
	public void setM_strISMPId(String m_strISMPId) {
		this.m_strISMPId = m_strISMPId;
	}
	public String getM_strProductId() {
		return m_strProductId;
	}
	public void setM_strProductId(String m_strProductId) {
		this.m_strProductId = m_strProductId;
	}
	public String getM_strSpcPrdId() {
		return m_strSpcPrdId;
	}
	public void setM_strSpcPrdId(String m_strSpcPrdId) {
		this.m_strSpcPrdId = m_strSpcPrdId;
	}
	public int getM_nActiveFlag() {
		return m_nActiveFlag;
	}
	public void setM_nActiveFlag(int m_nActiveFlag) {
		this.m_nActiveFlag = m_nActiveFlag;
	}
	public int getM_nFmtOperType() {
		return m_nFmtOperType;
	}
	public void setM_nFmtOperType(int m_nFmtOperType) {
		this.m_nFmtOperType = m_nFmtOperType;
	}

	
	public void print(){
				 
	}
	
}
