package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * 
 * @author Tian
 *
 */
public class ValueAddedChargeRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String Sessionid;
	private String MSISDN;
	private String UserEventCode;
	private String ContactChannle;
	private String ValueAddedOrderId ;
	private String OrderTime;
	private String ProductId;
	private String ValueAddedName;
	private String FeeType;
	private long ValueAddedChargeFee ;
	private String Url ;
	
	public ValueAddedChargeRequest(){}
	
	public ValueAddedChargeRequest(String sessionId,String nbr,String event,String channle,String orderId,String orderTime,
			String productId,String valueAddedName,String feeType,long fee,String url){
		
		this.Sessionid=sessionId;
		this.MSISDN=nbr;
		this.UserEventCode=event;
		this.ContactChannle=channle;
		this.ValueAddedOrderId=orderId;
		this.OrderTime=orderTime;
		this.ProductId=productId;
		this.ValueAddedName=valueAddedName;
		this.FeeType=feeType;
		this.ValueAddedChargeFee=fee;
		this.Url=url;
	}
	
	public String getSessionid() {
		return Sessionid;
	}
	public void setSessionid(String sessionid) {
		Sessionid = sessionid;
	}
	public String getMSISDN() {
		return MSISDN;
	}
	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}
	public String getUserEventCode() {
		return UserEventCode;
	}
	public void setUserEventCode(String userEventCode) {
		UserEventCode = userEventCode;
	}
	public String getContactChannle() {
		return ContactChannle;
	}
	public void setContactChannle(String contactChannle) {
		ContactChannle = contactChannle;
	}
	public String getValueAddedOrderId() {
		return ValueAddedOrderId;
	}
	public void setValueAddedOrderId(String valueAddedOrderId) {
		ValueAddedOrderId = valueAddedOrderId;
	}
	public String getOrderTime() {
		return OrderTime;
	}
	public void setOrderTime(String orderTime) {
		OrderTime = orderTime;
	}
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public String getValueAddedName() {
		return ValueAddedName;
	}
	public void setValueAddedName(String valueAddedName) {
		ValueAddedName = valueAddedName;
	}
	public String getFeeType() {
		return FeeType;
	}
	public void setFeeType(String feeType) {
		FeeType = feeType;
	}
	public long getValueAddedChargeFee() {
		return ValueAddedChargeFee;
	}
	public void setValueAddedChargeFee(long valueAddedChargeFee) {
		ValueAddedChargeFee = valueAddedChargeFee;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	
	
	@Override
	public String toString() {
		String ret="Sessionid["+Sessionid+"],MSISDN["+MSISDN+"],UserEventCode["+UserEventCode+"],ContactChannle["
	+ContactChannle+"],ValueAddedOrderId["+ValueAddedOrderId+"],OrderTime["+OrderTime+"],ProductId["+ProductId+"],ValueAddedName["
	+ValueAddedName+"],FeeType["+FeeType+"],ValueAddedChargeFee["+ValueAddedChargeFee+"],Url["+Url+"]";
		
		return ret;
	}
	
	
}
