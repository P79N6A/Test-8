package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;
import java.util.Date;

public class TradeLogDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String TradeTypeCode;
	private String OrderNo;
	private long OrderAmount;
	private long BValue;
	private Date OperateTime;
	private String Desc;
	public String getTradeTypeCode() {
		return TradeTypeCode;
	}
	public void setTradeTypeCode(String tradeTypeCode) {
		TradeTypeCode = tradeTypeCode;
	}
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public long getOrderAmount() {
		return OrderAmount;
	}
	public void setOrderAmount(long orderAmount) {
		OrderAmount = orderAmount;
	}
	public long getBValue() {
		return BValue;
	}
	public void setBValue(long bValue) {
		BValue = bValue;
	}
	public Date getOperateTime() {
		return OperateTime;
	}
	public void setOperateTime(Date operateTime) {
		OperateTime = operateTime;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	
	
 


}
