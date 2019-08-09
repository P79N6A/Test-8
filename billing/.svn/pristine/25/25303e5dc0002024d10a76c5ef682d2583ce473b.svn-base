package com.tydic.beijing.billing.rating.domain;

import java.util.HashMap;
import java.util.Map;

public class RatingExtMsg {

	   private String m_strDuration ="";           //时长 本次更新值
	   private String m_strTimes ="";              //使用次数
	   private String m_strUpVolume ="";           //上行流量
	   private String m_strDownVolume ="";         //下行流量
	   private String m_strTotalVolume ="";        //总流量
	   
	   private String m_strUpVolumeFeeLast ="0";                //上行流量费率切换点之后的使用量
	   private String m_strDownVolumeFeeLast ="0";          //下行流量费率切换点之后的使用量
	   private String m_strTotalVolumeFeeLast ="0";            //总流量费率切换点之后的使用量
	   private String m_strExtStartTime ="";       //会话开始时间
	   private String m_strExtLastTime ="";        //会话上次实际扣费开始时间（计算实际扣费）
	   private String m_strExtCurrTime ="";        //本次计费请求开始时间（计算预扣费用）
	   private long   m_lnMoney =0L;
	   private int    m_nExtFeeType =-1;               //标识消息业务 0:仅有预占 1:仅有实扣 2:既有预占又有实扣
	   
	   private Map<String, String> m_iServAttrMap = new HashMap<String,String>();
	   
	   public String getM_strDuration() {
		return m_strDuration;
	}
	public void setM_strDuration(String m_strDuration) {
		this.m_strDuration = m_strDuration;
	}
	public String getM_strTimes() {
		return m_strTimes;
	}
	public void setM_strTimes(String m_strTimes) {
		this.m_strTimes = m_strTimes;
	}
	public String getM_strUpVolume() {
		return m_strUpVolume;
	}
	public void setM_strUpVolume(String m_strUpVolume) {
		this.m_strUpVolume = m_strUpVolume;
	}
	public String getM_strDownVolume() {
		return m_strDownVolume;
	}
	public void setM_strDownVolume(String m_strDownVolume) {
		this.m_strDownVolume = m_strDownVolume;
	}
	public String getM_strTotalVolume() {
		return m_strTotalVolume;
	}
	public void setM_strTotalVolume(String m_strTotalVolume) {
		this.m_strTotalVolume = m_strTotalVolume;
	}
	public String getM_strUpVolumeFeeLast() {
		return m_strUpVolumeFeeLast;
	}
	public void setM_strUpVolumeFeeLast(String m_strUpVolumeFeeLast) {
		this.m_strUpVolumeFeeLast = m_strUpVolumeFeeLast;
	}
	public String getM_strDownVolumeFeeLast() {
		return m_strDownVolumeFeeLast;
	}
	public void setM_strDownVolumeFeeLast(String m_strDownVolumeFeeLast) {
		this.m_strDownVolumeFeeLast = m_strDownVolumeFeeLast;
	}
	public String getM_strTotalVolumeFeeLast() {
		return m_strTotalVolumeFeeLast;
	}
	public void setM_strTotalVolumeFeeLast(String m_strTotalVolumeFeeLast) {
		this.m_strTotalVolumeFeeLast = m_strTotalVolumeFeeLast;
	}
	public String getM_strExtStartTime() {
		return m_strExtStartTime;
	}
	public void setM_strExtStartTime(String m_strExtStartTime) {
		this.m_strExtStartTime = m_strExtStartTime;
	}
	public String getM_strExtLastTime() {
		return m_strExtLastTime;
	}
	public void setM_strExtLastTime(String m_strExtLastTime) {
		this.m_strExtLastTime = m_strExtLastTime;
	}
	public String getM_strExtCurrTime() {
		return m_strExtCurrTime;
	}
	public void setM_strExtCurrTime(String m_strExtCurrTime) {
		this.m_strExtCurrTime = m_strExtCurrTime;
	}
	public long getM_lnMoney() {
		return m_lnMoney;
	}
	public void setM_lnMoney(long m_lnMoney) {
		this.m_lnMoney = m_lnMoney;
	}
	public int getM_nExtFeeType() {
		return m_nExtFeeType;
	}
	public void setM_nExtFeeType(int m_nExtFeeType) {
		this.m_nExtFeeType = m_nExtFeeType;
	}
	
	
	public Map<String, String> getM_iServAttrMap() {
		return m_iServAttrMap;
	}
	public void setM_iServAttrMap(Map<String, String> m_iServAttrMap) {
		this.m_iServAttrMap = m_iServAttrMap;
	}
	public void print(){
		
	     if(m_strDuration          !=null) System.out.println("ratingextmsg.m_strDuration          ="+  m_strDuration             );                                  //时长 本次更新值
			 if(m_strTimes             !=null) System.out.println("ratingextmsg.m_strTimes             ="+  m_strTimes                );                                  //使用次数
			 if(m_strUpVolume          !=null) System.out.println("ratingextmsg.m_strUpVolume          ="+  m_strUpVolume             );                                  //上行流量
			 if(m_strDownVolume        !=null) System.out.println("ratingextmsg.m_strDownVolume        ="+  m_strDownVolume           );                                  //下行流量
			 if(m_strTotalVolume       !=null) System.out.println("ratingextmsg.m_strTotalVolume       ="+  m_strTotalVolume          );                                  //总流量
			 if(m_strUpVolumeFeeLast   !=null) System.out.println("ratingextmsg.m_strUpVolumeFeeLast   ="+  m_strUpVolumeFeeLast      );                                              //上行流量费率切换点之后的使用量
			 if(m_strDownVolumeFeeLast !=null) System.out.println("ratingextmsg.m_strDownVolumeFeeLast ="+  m_strDownVolumeFeeLast    );                                          //下行流量费率切换点之后的使用量
			 if(m_strTotalVolumeFeeLast!=null) System.out.println("ratingextmsg.m_strTotalVolumeFeeLast="+  m_strTotalVolumeFeeLast   );                                             //总流量费率切换点之后的使用量
			 if(m_strExtStartTime      !=null) System.out.println("ratingextmsg.m_strExtStartTime      ="+  m_strExtStartTime         );                                  //会话开始时间
			 if(m_strExtLastTime       !=null) System.out.println("ratingextmsg.m_strExtLastTime       ="+  m_strExtLastTime          );                                  //会话上次实际扣费开始时间（计算实际扣费）
			 if(m_strExtCurrTime       !=null) System.out.println("ratingextmsg.m_strExtCurrTime       ="+  m_strExtCurrTime          );                                  //本次计费请求开始时间（计算预扣费用）
			 if(m_lnMoney >0) System.out.println("ratingextmsg.m_lnMoney              ="+  m_lnMoney                 );                   
			 if(m_nExtFeeType   >0) System.out.println("ratingextmsg.m_nExtFeeType          ="+  m_nExtFeeType             );                                      //标识消息业务 0:仅有预占 1:仅有实扣 2:既有预占又有实扣
			           
		
	}
	
	
	
}
