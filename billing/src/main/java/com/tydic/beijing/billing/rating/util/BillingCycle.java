package com.tydic.beijing.billing.rating.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tydic.beijing.billing.rating.domain.ParamData;

public class BillingCycle {

	private String billingStartTime="";//yyyymmddhh24miss  计费请求开始时间
	private String startDate="";
	private String endDate="";
	private int billingCycleType;
	
	public BillingCycle(String billingStartTime,int billingCycleType){
		this.billingStartTime=billingStartTime;
		this.billingCycleType=billingCycleType;
		getBillingCycle();
	}
	//自然天 如:2009-09-02 00:00:00~2009-09-02 23:59:59
	private void setNatureDay(){
		this.startDate=billingStartTime.substring(0, 8)+"000000";
		this.endDate=billingStartTime.substring(0,8)+"235959";
	}
	//动态天类型1  如:2009-09-02 01:00:00~2009-09-03 00:59:59
	private void setDynamicDay1(){
		this.startDate=billingStartTime;
		this.endDate=DateUtil.addSeconds(billingStartTime, 24*3600-1, 14);
	}
	//动态天类型2 如:2009-09-02 01:00:00~2009-09-03 23:59:59
	private void setDynamicDay2(){
		this.startDate=billingStartTime;
		this.endDate=DateUtil.addSeconds(billingStartTime.substring(0,8)+"235959", 24*3600, 14);
	}
	
	//自然月  如:2009-09-01~2009-09-30
	private void setNatureMonth(){
		this.startDate=billingStartTime.substring(0,6)+"01";
		this.endDate=billingStartTime.substring(0,6)+Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	//动态月 如:2009-09-02~2009-10-01
	private void setDynamicMonth(){
		this.startDate=billingStartTime.substring(0,8);
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		int firstDay=cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		int lastDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if(Integer.parseInt(startDate.substring(6, 8))==firstDay){
			cal.set(Calendar.DAY_OF_MONTH,lastDay );
			this.endDate=sdf.format(cal.getTime());
		}else{
			cal.add(Calendar.MONTH, 1);
			lastDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH, lastDay-1);
			this.endDate=sdf.format(cal.getTime());
		}
	}
	//自然周 如:2009-08-30~2009-09-05.其中2009-08-30为星期天，周日-周六，共7天
	private void setNatureWeek(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		this.startDate=sdf.format(cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, 6);
		this.endDate=sdf.format(cal.getTime());

	}
	
	//动态周  如:2009-09-01~2009-09-07. 其中2009-09-01为星期2，本周二到下周一
	private void setDynamicWeed(){
		this.startDate=billingStartTime.substring(0,8);
		this.endDate=DateUtil.addSeconds(startDate, 6*24*3600, 8);
	}
	
	
	public boolean getBillingCycle(){
		if(null==billingStartTime || billingStartTime.length()<14)
			return false;
		if(billingCycleType==ParamData.BILLING_CYCLE_NATURE_DAY){
			setNatureDay();
		}else if(billingCycleType==ParamData.BILLING_CYCLE_DYNAMIC_DAY_1){
			setDynamicDay1();
		}else if(billingCycleType==ParamData.BILLING_CYCLE_DYNAMIC_DAY_2){
			setDynamicDay2();
		}else if(billingCycleType==ParamData.BILLING_CYCLE_NATURE_MONTH){
			setNatureMonth();
		}else if(billingCycleType==ParamData.BILLING_CYCLE_DYNAMIC_MONTH){
			setDynamicMonth();
		}else if(billingCycleType==ParamData.BILLING_CYCLE_NATURE_WEEK){
			setNatureWeek();
		}else if(billingCycleType==ParamData.BILLING_CYCLE_DYNAMIC_WEEK){
			setDynamicWeed();
		}else{
			setNatureMonth();
		}
		return true;
	}
	
	public String getStartDate() {
		
		return startDate;
	}
	 
	public String getEndDate() {
		return endDate;
	}
	
	public static void main(String args[]){
		
		
		
	}
}
