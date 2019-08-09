package com.tydic.beijing.bvalue.dao;

/**
 * @author zhanghengbo
 *
 */
public class CdrAbnormalMonitor {

	  private String abnormal_type      ;
	  private String abnormal_keyword_1 ;
	  private String abnormal_keyword_2 ;
	  private String abnormal_keyword_3 ;
	  private String abnormal_keyword_4 ;
	  private String create_time        ;//yyyymmddhh24miss
	  private String deal_state         ;
	/**
	 * @return
	 */
	public String getAbnormal_type() {
		return abnormal_type;
	}
	public void setAbnormal_type(String abnormal_type) {
		this.abnormal_type = abnormal_type;
	}
	public String getAbnormal_keyword_1() {
		return abnormal_keyword_1;
	}
	public void setAbnormal_keyword_1(String abnormal_keyword_1) {
		this.abnormal_keyword_1 = abnormal_keyword_1;
	}
	public String getAbnormal_keyword_2() {
		return abnormal_keyword_2;
	}
	public void setAbnormal_keyword_2(String abnormal_keyword_2) {
		this.abnormal_keyword_2 = abnormal_keyword_2;
	}
	public String getAbnormal_keyword_3() {
		return abnormal_keyword_3;
	}
	public void setAbnormal_keyword_3(String abnormal_keyword_3) {
		this.abnormal_keyword_3 = abnormal_keyword_3;
	}
	public String getAbnormal_keyword_4() {
		return abnormal_keyword_4;
	}
	public void setAbnormal_keyword_4(String abnormal_keyword_4) {
		this.abnormal_keyword_4 = abnormal_keyword_4;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDeal_state() {
		return deal_state;
	}
	public void setDeal_state(String deal_state) {
		this.deal_state = deal_state;
	}
	  
}
