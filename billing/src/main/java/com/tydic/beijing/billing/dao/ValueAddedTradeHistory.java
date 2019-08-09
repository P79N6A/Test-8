package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.tydic.beijing.billing.dto.ValueAddedChargeRequest;
import com.tydic.beijing.billing.dto.ValueAddedChargeResponse;
import com.tydic.beijing.billing.dto.ValueAddedFileObj;

public class ValueAddedTradeHistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String   trade_id ;             
	private  String   session_id  ;          
	private  String   msisdn  ;              
	private  String   user_event_code ;      
	private  String   contact_channle;       
	private  String   valueadded_order_id;   
	private  String   order_time;            
	private  String   product_id ;           
	private  String   valueadded_name ;      
	private  String   fee_type;              
	private  long   valueadded_charge_fee ;
	private  String   url;                   
	private  String   user_id  ;             
	private  String   service_scenarious ;   
	private  String   pay_flag ;             
	private  long   serialno ;             
	private  String   tariff_info ;          
	private  long   remain_fee ; 
	private String charging_type;
	private String mt_mode;
	
	private  String   status ;               
	private  String   error_code ="";           
	private  String   error_message ="";
	private String local_net;
	private String partition_no;
	private int acct_month;
	
	public ValueAddedTradeHistory(){}
	
	public ValueAddedTradeHistory(ValueAddedChargeRequest info){
		this.session_id=info.getSessionid();
		this.msisdn=info.getMSISDN();
		this.user_event_code=info.getUserEventCode();
		this.contact_channle=info.getContactChannle();
		this.valueadded_order_id=info.getValueAddedOrderId();
//		this.order_time=new SimpleDateFormat("yyyyMMddHHmmss").format(info.getOrderTime());
		this.order_time=info.getOrderTime();
		this.product_id=info.getProductId();
		this.valueadded_name=info.getValueAddedName();
		this.fee_type=info.getFeeType();
		this.valueadded_charge_fee=info.getValueAddedChargeFee();
		this.url=info.getUrl();
		
		
	}
	
	public void fillData(ValueAddedChargeResponse result){
		this.status=result.getStatus();
		this.error_code=result.getErrorCode();
		this.error_message=result.getErrorMessage();
	}
	
	public void fillData(ValueAddedFileObj obj){
		this.tariff_info=obj.getTariffInfo();
		this.user_id=obj.getServID();
		this.service_scenarious=obj.getServiceScenarious();
		this.pay_flag=obj.getPayFlag();
		this.charging_type=obj.getChargingType();
		this.mt_mode=obj.getMtMode();
		
		
	}
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getUser_event_code() {
		return user_event_code;
	}
	public void setUser_event_code(String user_event_code) {
		this.user_event_code = user_event_code;
	}
	public String getContact_channle() {
		return contact_channle;
	}
	public void setContact_channle(String contact_channle) {
		this.contact_channle = contact_channle;
	}
	public String getValueadded_order_id() {
		return valueadded_order_id;
	}
	public void setValueadded_order_id(String valueadded_order_id) {
		this.valueadded_order_id = valueadded_order_id;
	}
	
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getValueadded_name() {
		return valueadded_name;
	}
	public void setValueadded_name(String valueadded_name) {
		this.valueadded_name = valueadded_name;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getService_scenarious() {
		return service_scenarious;
	}
	public void setService_scenarious(String service_scenarious) {
		this.service_scenarious = service_scenarious;
	}
	public String getPay_flag() {
		return pay_flag;
	}
	public void setPay_flag(String pay_flag) {
		this.pay_flag = pay_flag;
	}
	
	public String getTariff_info() {
		return tariff_info;
	}
	public void setTariff_info(String tariff_info) {
		this.tariff_info = tariff_info;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_message() {
		return error_message;
	}
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	public long getValueadded_charge_fee() {
		return valueadded_charge_fee;
	}

	public void setValueadded_charge_fee(long valueadded_charge_fee) {
		this.valueadded_charge_fee = valueadded_charge_fee;
	}

	public long getSerialno() {
		return serialno;
	}

	public void setSerialno(long serialno) {
		this.serialno = serialno;
	}

	public long getRemain_fee() {
		return remain_fee;
	}

	public void setRemain_fee(long remain_fee) {
		this.remain_fee = remain_fee;
	}

	public String getCharging_type() {
		return charging_type;
	}

	public void setCharging_type(String charging_type) {
		this.charging_type = charging_type;
	}

	public String getMt_mode() {
		return mt_mode;
	}

	public void setMt_mode(String mt_mode) {
		this.mt_mode = mt_mode;
	}
	public String getLocal_net() {
		return local_net;
	}
	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}
	public String getPartition_no() {
		return partition_no;
	}
	public void setPartition_no(String partition_no) {
		this.partition_no = partition_no;
	}
	public void setAcct_month(int acct_month) {
		this.acct_month = acct_month;
	}
	public int getAcct_month() {
		return acct_month;
	}
	
	
	
	
}
