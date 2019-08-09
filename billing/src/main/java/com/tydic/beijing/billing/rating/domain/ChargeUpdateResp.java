package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.List;

public class ChargeUpdateResp {

	private String m_strSessionId;
	private int m_unResultCode;
	private String m_strOriginHost;
	private String m_strOriginRealm;
	private int m_unAuthApplicationId;
	private int m_unCCRequestType;
	private int m_unCCRequestNumber;
	private int m_unRequestedAction;
	private List<GrantedAcctBalance> m_iGrantedAcctBalances=new ArrayList<GrantedAcctBalance>();
    private List<RespondRatableQuery> m_iRespondRatableQueries=new ArrayList<RespondRatableQuery>();
    private int m_unFinalUnitAction;
    private List<RespondBalanceUsed> m_iRespondBalanceUseds=new ArrayList<RespondBalanceUsed>();
	public String getM_strSessionId() {
		return m_strSessionId;
	}
	public void setM_strSessionId(String m_strSessionId) {
		this.m_strSessionId = m_strSessionId;
	}
	public int getM_unResultCode() {
		return m_unResultCode;
	}
	public void setM_unResultCode(int m_unResultCode) {
		this.m_unResultCode = m_unResultCode;
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
	public int getM_unAuthApplicationId() {
		return m_unAuthApplicationId;
	}
	public void setM_unAuthApplicationId(int m_unAuthApplicationId) {
		this.m_unAuthApplicationId = m_unAuthApplicationId;
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
	public List<GrantedAcctBalance> getM_iGrantedAcctBalances() {
		return m_iGrantedAcctBalances;
	}
	public void setM_iGrantedAcctBalances(
			List<GrantedAcctBalance> m_iGrantedAcctBalances) {
		this.m_iGrantedAcctBalances = m_iGrantedAcctBalances;
	}
	public List<RespondRatableQuery> getM_iRespondRatableQueries() {
		return m_iRespondRatableQueries;
	}
	public void setM_iRespondRatableQueries(
			List<RespondRatableQuery> m_iRespondRatableQueries) {
		this.m_iRespondRatableQueries = m_iRespondRatableQueries;
	}
	public int getM_unFinalUnitAction() {
		return m_unFinalUnitAction;
	}
	public void setM_unFinalUnitAction(int m_unFinalUnitAction) {
		this.m_unFinalUnitAction = m_unFinalUnitAction;
	}
	public List<RespondBalanceUsed> getM_iRespondBalanceUseds() {
		return m_iRespondBalanceUseds;
	}
	public void setM_iRespondBalanceUseds(
			List<RespondBalanceUsed> m_iRespondBalanceUseds) {
		this.m_iRespondBalanceUseds = m_iRespondBalanceUseds;
	}
    
    
    
    
    
}
