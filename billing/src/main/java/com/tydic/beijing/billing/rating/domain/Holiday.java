package com.tydic.beijing.billing.rating.domain;

public class Holiday {

	private int holidayId=0;
	private int holidaySn=0;
	private String holidayType="";//01:日期 02:周六日 03:周期性节假日 04:时间
	private String beginTime="";
	private String endTime="";
	       
	public Holiday(){}
	
	public Holiday(RuleHoliday holiday){
		this.holidayId=holiday.getHoliday_id();
		this.holidaySn=holiday.getHoliday_sn();
		this.holidayType=""+holiday.getHoliday_type();
		this.beginTime=holiday.getBegin_time();
		this.endTime=holiday.getEnd_time();
	}
	public int getHolidayId() {
		return holidayId;
	}
	public void setHolidayId(int holidayId) {
		this.holidayId = holidayId;
	}
	public int getHolidaySn() {
		return holidaySn;
	}
	public void setHolidaySn(int holidaySn) {
		this.holidaySn = holidaySn;
	}
	public String getHolidayType() {
		return holidayType;
	}
	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
	
}
