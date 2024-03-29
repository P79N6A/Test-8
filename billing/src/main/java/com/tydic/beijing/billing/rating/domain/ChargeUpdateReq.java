package com.tydic.beijing.billing.rating.domain;

import java.util.List;

public class ChargeUpdateReq {

	private String m_strSessionId;
    private String m_strEventTimeStamp;
    private String m_strOriginHost;
    private String m_strOriginRealm;
    private String m_strDestinationRealm;
    private long m_unAuthApplicationId;
    private String m_strServiceContextId;
    private int  m_unCCRequestType;
    private int m_unCCRequestNumber;
    private int m_unRequestedAction;
    private  long m_ulnServId;
    private int m_nLatnId;
    private List<UsedAcctItem> m_iUsedAcctItems;
    private List<RequestAcctItem> m_iRequestAcctItems;
    private  List<RequestRatableUpdate> m_iRequestRatableUpdates;
	public String getM_strSessionId() {
		return m_strSessionId;
	}
	public void setM_strSessionId(String m_strSessionId) {
		this.m_strSessionId = m_strSessionId;
	}
	public String getM_strEventTimeStamp() {
		return m_strEventTimeStamp;
	}
	public void setM_strEventTimeStamp(String m_strEventTimeStamp) {
		this.m_strEventTimeStamp = m_strEventTimeStamp;
	}
	public String getM_strOriginHost() {
		return m_strOriginHost;
	}
	public void setM_strOriginHost(String m_strOriginHost) {
		this.m_strOriginHost = m_strOriginHost;
	}
	public String getM_strOriginRealm() {
		return m_strOriginRealm;
	}
	public void setM_strOriginRealm(String m_strOriginRealm) {
		this.m_strOriginRealm = m_strOriginRealm;
	}
	public String getM_strDestinationRealm() {
		return m_strDestinationRealm;
	}
	public void setM_strDestinationRealm(String m_strDestinationRealm) {
		this.m_strDestinationRealm = m_strDestinationRealm;
	}
	public long getM_unAuthApplicationId() {
		return m_unAuthApplicationId;
	}
	public void setM_unAuthApplicationId(long m_unAuthApplicationId) {
		this.m_unAuthApplicationId = m_unAuthApplicationId;
	}
	public String getM_strServiceContextId() {
		return m_strServiceContextId;
	}
	public void setM_strServiceContextId(String m_strServiceContextId) {
		this.m_strServiceContextId = m_strServiceContextId;
	}
	public int getM_unCCRequestType() {
		return m_unCCRequestType;
	}
	public void setM_unCCRequestType(int m_unCCRequestType) {
		this.m_unCCRequestType = m_unCCRequestType;
	}
	public int getM_unCCRequestNumber() {
		return m_unCCRequestNumber;
	}
	public void setM_unCCRequestNumber(int m_unCCRequestNumber) {
		this.m_unCCRequestNumber = m_unCCRequestNumber;
	}
	public int getM_unRequestedAction() {
		return m_unRequestedAction;
	}
	public void setM_unRequestedAction(int m_unRequestedAction) {
		this.m_unRequestedAction = m_unRequestedAction;
	}
	public long getM_ulnServId() {
		return m_ulnServId;
	}
	public void setM_ulnServId(long m_ulnServId) {
		this.m_ulnServId = m_ulnServId;
	}
	public int getM_nLatnId() {
		return m_nLatnId;
	}
	public void setM_nLatnId(int m_nLatnId) {
		this.m_nLatnId = m_nLatnId;
	}
	public List<UsedAcctItem> getM_iUsedAcctItems() {
		return m_iUsedAcctItems;
	}
	public void setM_iUsedAcctItems(List<UsedAcctItem> m_iUsedAcctItems) {
		this.m_iUsedAcctItems = m_iUsedAcctItems;
	}
	public List<RequestAcctItem> getM_iRequestAcctItems() {
		return m_iRequestAcctItems;
	}
	public void setM_iRequestAcctItems(List<RequestAcctItem> m_iRequestAcctItems) {
		this.m_iRequestAcctItems = m_iRequestAcctItems;
	}
	public List<RequestRatableUpdate> getM_iRequestRatableUpdates() {
		return m_iRequestRatableUpdates;
	}
	public void setM_iRequestRatableUpdates(
			List<RequestRatableUpdate> m_iRequestRatableUpdates) {
		this.m_iRequestRatableUpdates = m_iRequestRatableUpdates;
	}
    
    
    
}
