package com.tydic.beijing.billing.dto;

public class ValueAddedFileObj {

	private String serialNo="";
	private String version="";
	private String ticketType="";
	private String timeStamp="";
	private String hostID="";
	private String serviceScenarious="400";
	private String chargedParty="";
	private String callingParty="" ;
	private String calledParty="" ;
	private String orignialCallingParty="" ;
	private String orignialCalledParty="" ;
	private String payFlag="1";
	private String servID="";
	private String custID="";
	private String brand="";
	private String sessionId="";
	private String sessionTerminatedTime="";
	private String terminatedCause="";
	private String orignialHost="";
	private String balanceinfo="";
	private String accumlatorinfo="";
	private String tariffInfo="";
	private String masterProductID="";
	private String bearerCapability="";
	private String SessionBeginTime="";
	private String productId="";
	
	
	private String spc_ProductID="";
	private String sp_ProductID="";
	private String serviceID="";
	private String contentID="";
	private String orderMethodID="";
	private String pushID="";
	private String cp_ID="";
	private String spID="";
	private String messageId="";
	private String serviceCapacity="";
	private String chargingType="0";
	private String notes="";
	private String valueAddServiceType="";
	private String charging_Id="";
	private String apnNI="";
	private String clientIP="";
	private String qoS="";
	private String mtMode="0";
	private String mtAmount="";
	private String mtMoney="";
	private String mtChargeAdd="";
	private String string1="";
	private String string2="";
	private String string3="";
	private String string4="";
	private String int1="";
	private String int2="";
	private String partition_no="" ;
	private String insert_date="";
	
	public void fillData(ValueAddedChargeRequest info){
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHH24mmss");
//		String date=sdf.format(info.getOrderTime());
		String date=info.getOrderTime().replace(" ", "").replace("-", "").replace(":", "");
		this.timeStamp=date;
		this.chargedParty=info.getMSISDN();
		this.callingParty=date;
		this.orignialCallingParty=date;
		this.serialNo=info.getSessionid();
		this.sessionTerminatedTime=date;
		this.SessionBeginTime=date;
		this.productId=info.getProductId();
		this.orderMethodID=info.getContactChannle();
		
		
		
	}
	
	public String getValue(String str){
		String value=str.toLowerCase();
		if(value.equals("serialno")){
			return this.serialNo;
		}else if(value.equals("version")){
			return this.version;
		}else if(value.equals("tickettype")){
			return this.ticketType;
		}else if(value.equals("timestamp")){
			return this.timeStamp;
		}else if(value.equals("hostid")){
			return this.hostID;
		}else if(value.equals("servicescenarious")){
			return this.serviceScenarious;
		}else if(value.equals("chargedparty")){
			return this.chargedParty;
		}else if(value.equals("callingparty")){
			return this.callingParty;
		}else if(value.equals("calledparty")){
			return this.calledParty;
		}else if(value.equals("orignialcallingparty")){
			return this.orignialCallingParty;
		}else if(value.equals("orignialcalledparty")){
			return this.orignialCalledParty;
		}else if(value.equals("payflag")){
			return this.payFlag;
		}else if(value.equals("servid")){
			return this.servID;
		}else if(value.equals("custid")){
			return this.custID;
		}else if(value.equals("brand")){
			return this.brand;
		}else if(value.equals("sessionid")){
			return this.sessionId;
		}else if(value.equals("sessionterminatedtime")){
			return this.sessionTerminatedTime;
		}else if(value.equals("terminatedcause")){
			return this.terminatedCause;
		}else if(value.equals("orignialhost")){
			return this.orignialHost;
		}else if(value.equals("balanceinfo")){
			return this.balanceinfo;
		}else if(value.equals("accumlatorinfo")){
			return this.accumlatorinfo;
		}else if(value.equals("tariffinfo")){
			return this.tariffInfo;
		}else if(value.equals("masterproductid")){
			return this.masterProductID;
		}else if(value.equals("bearercapability")){
			return this.bearerCapability;
		}else if(value.equals("sessionbegintime")){
			return this.SessionBeginTime;
		}else if(value.equals("productid")){
			return this.productId;
		}else if(value.equals("spc_productid")){
			return this.spc_ProductID;
			
		}else if(value.equals("sp_productid")){
			return this.sp_ProductID;
		}else if(value.equals("serviceid")){
			return this.serviceID;
		}else if(value.equals("contentid")){
			return this.contentID;
		}else if(value.equals("ordermethodid")){
			return this.orderMethodID;
		}else if(value.equals("pushid")){
			return this.pushID;
		}else if(value.equals("cp_id")){
			return this.cp_ID;
		}else if(value.equals("spid")){
			return this.spID;
		}else if(value.equals("messageid")){
			return this.messageId;
		}else if(value.equals("servicecapacity")){
			return this.serviceCapacity;
		}else if(value.equals("chargingtype")){
			return this.chargingType;
		}else if(value.equals("notes")){
			return this.notes;
		}else if(value.equals("valueaddservicetype")){
			return this.valueAddServiceType;
		}else if(value.equals("charging-id")){
			return this.charging_Id;
		}else if(value.equals("apnni")){
			return this.apnNI;
		}else if(value.equals("clientip")){
			return this.clientIP;
		}else if(value.equals("qos")){
			return this.qoS;
		}else if(value.equals("mtmode")){
			return this.mtMode;
		}else if(value.equals("mtamount")){
			return this.mtAmount;
		}else if(value.equals("mtmoney")){
			return this.mtMoney;
		}else if(value.equals("mtchargeadd")){
			return this.mtChargeAdd;
		}else if(value.equals("string1")){
			return this.string1;
		}else if(value.equals("string2")){
			return this.string2;
		}else if(value.equals("string3")){
			return this.string3;
		}else if(value.equals("string4")){
			return this.string4;
		}else if(value.equals("int1")){
			return this.int1;
		}else if(value.equals("int2")){
			return this.int2;
		}else if(value.equals("partition_no")){
			return this.partition_no;
		}else if(value.equals("insert_date")){
			return this.insert_date;	
		}else{
			return "";
		}
		
		
	}
	
	
	
	
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getHostID() {
		return hostID;
	}
	public void setHostID(String hostID) {
		this.hostID = hostID;
	}
	public String getServiceScenarious() {
		return serviceScenarious;
	}
	public void setServiceScenarious(String serviceScenarious) {
		this.serviceScenarious = serviceScenarious;
	}
	public String getChargedParty() {
		return chargedParty;
	}
	public void setChargedParty(String chargedParty) {
		this.chargedParty = chargedParty;
	}
	public String getCallingParty() {
		return callingParty;
	}
	public void setCallingParty(String callingParty) {
		this.callingParty = callingParty;
	}
	public String getCalledParty() {
		return calledParty;
	}
	public void setCalledParty(String calledParty) {
		this.calledParty = calledParty;
	}
	public String getOrignialCallingParty() {
		return orignialCallingParty;
	}
	public void setOrignialCallingParty(String orignialCallingParty) {
		this.orignialCallingParty = orignialCallingParty;
	}
	public String getOrignialCalledParty() {
		return orignialCalledParty;
	}
	public void setOrignialCalledParty(String orignialCalledParty) {
		this.orignialCalledParty = orignialCalledParty;
	}
	public String getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	public String getServID() {
		return servID;
	}
	public void setServID(String servID) {
		this.servID = servID;
	}
	public String getCustID() {
		return custID;
	}
	public void setCustID(String custID) {
		this.custID = custID;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionTerminatedTime() {
		return sessionTerminatedTime;
	}
	public void setSessionTerminatedTime(String sessionTerminatedTime) {
		this.sessionTerminatedTime = sessionTerminatedTime;
	}
	public String getTerminatedCause() {
		return terminatedCause;
	}
	public void setTerminatedCause(String terminatedCause) {
		this.terminatedCause = terminatedCause;
	}
	public String getOrignialHost() {
		return orignialHost;
	}
	public void setOrignialHost(String orignialHost) {
		this.orignialHost = orignialHost;
	}
	public String getBalanceinfo() {
		return balanceinfo;
	}
	public void setBalanceinfo(String balanceinfo) {
		this.balanceinfo = balanceinfo;
	}
	public String getAccumlatorinfo() {
		return accumlatorinfo;
	}
	public void setAccumlatorinfo(String accumlatorinfo) {
		this.accumlatorinfo = accumlatorinfo;
	}
	public String getTariffInfo() {
		return tariffInfo;
	}
	public void setTariffInfo(String tariffInfo) {
		this.tariffInfo = tariffInfo;
	}
	public String getMasterProductID() {
		return masterProductID;
	}
	public void setMasterProductID(String masterProductID) {
		this.masterProductID = masterProductID;
	}
	public String getBearerCapability() {
		return bearerCapability;
	}
	public void setBearerCapability(String bearerCapability) {
		this.bearerCapability = bearerCapability;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getSpc_ProductID() {
		return spc_ProductID;
	}
	public void setSpc_ProductID(String spc_ProductID) {
		this.spc_ProductID = spc_ProductID;
	}
	public String getSp_ProductID() {
		return sp_ProductID;
	}
	public void setSp_ProductID(String sp_ProductID) {
		this.sp_ProductID = sp_ProductID;
	}
	public String getServiceID() {
		return serviceID;
	}
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	public String getContentID() {
		return contentID;
	}
	public void setContentID(String contentID) {
		this.contentID = contentID;
	}
	public String getOrderMethodID() {
		return orderMethodID;
	}
	public void setOrderMethodID(String orderMethodID) {
		this.orderMethodID = orderMethodID;
	}
	public String getPushID() {
		return pushID;
	}
	public void setPushID(String pushID) {
		this.pushID = pushID;
	}
	public String getCp_ID() {
		return cp_ID;
	}
	public void setCp_ID(String cp_ID) {
		this.cp_ID = cp_ID;
	}
	public String getSpID() {
		return spID;
	}
	public void setSpID(String spID) {
		this.spID = spID;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getServiceCapacity() {
		return serviceCapacity;
	}
	public void setServiceCapacity(String serviceCapacity) {
		this.serviceCapacity = serviceCapacity;
	}
	public String getChargingType() {
		return chargingType;
	}
	public void setChargingType(String chargingType) {
		this.chargingType = chargingType;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getValueAddServiceType() {
		return valueAddServiceType;
	}
	public void setValueAddServiceType(String valueAddServiceType) {
		this.valueAddServiceType = valueAddServiceType;
	}
	public String getCharging_Id() {
		return charging_Id;
	}
	public void setCharging_Id(String charging_Id) {
		this.charging_Id = charging_Id;
	}
	public String getApnNI() {
		return apnNI;
	}
	public void setApnNI(String apnNI) {
		this.apnNI = apnNI;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getQoS() {
		return qoS;
	}
	public void setQoS(String qoS) {
		this.qoS = qoS;
	}
	public String getMtMode() {
		return mtMode;
	}
	public void setMtMode(String mtMode) {
		this.mtMode = mtMode;
	}
	public String getMtAmount() {
		return mtAmount;
	}
	public void setMtAmount(String mtAmount) {
		this.mtAmount = mtAmount;
	}
	public String getMtMoney() {
		return mtMoney;
	}
	public void setMtMoney(String mtMoney) {
		this.mtMoney = mtMoney;
	}
	public String getMtChargeAdd() {
		return mtChargeAdd;
	}
	public void setMtChargeAdd(String mtChargeAdd) {
		this.mtChargeAdd = mtChargeAdd;
	}
	public String getString1() {
		return string1;
	}
	public void setString1(String string1) {
		this.string1 = string1;
	}
	public String getString2() {
		return string2;
	}
	public void setString2(String string2) {
		this.string2 = string2;
	}
	public String getString3() {
		return string3;
	}
	public void setString3(String string3) {
		this.string3 = string3;
	}
	public String getString4() {
		return string4;
	}
	public void setString4(String string4) {
		this.string4 = string4;
	}
	public String getInt1() {
		return int1;
	}
	public void setInt1(String int1) {
		this.int1 = int1;
	}
	public String getInt2() {
		return int2;
	}
	public void setInt2(String int2) {
		this.int2 = int2;
	}
	public String getSessionBeginTime() {
		return SessionBeginTime;
	}
	public void setSessionBeginTime(String sessionBeginTime) {
		SessionBeginTime = sessionBeginTime;
	}
	public String getPartition_no() {
		return partition_no;
	}
	public void setPartition_no(String partition_no) {
		this.partition_no = partition_no;
	}
	public String getInsert_date() {
		return insert_date;
	}
	public void setInsert_date(String insert_date) {
		this.insert_date = insert_date;
	}
	
	

}
