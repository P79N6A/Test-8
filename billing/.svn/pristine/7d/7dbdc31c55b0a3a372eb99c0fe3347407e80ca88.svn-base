package com.tydic.beijing.billing.rating.domain;

import java.security.Key;

public class Tariff {

	private long m_lnTariffId=0;
    private int m_nTariffSn=0;
	private int m_lnRateUnit=0;				//计费单元
	private long m_nFeeRate=0L;		//费率 单位为粒
	private int m_nTailMode=0;				//尾数模式 1:向下圆整 2:向上圆整 3:四舍五入
    private int m_nDisctValue=100;			//折扣率，如15%的折扣率为15
    private int m_nDisctValueBase=100;      //折扣率基数，如15%的折扣率基数为100，
    private int m_nDateType=0;
    private int m_nHolidayId=0;			//节假日ID
    private int m_nUnitTypeId=2;          //单位 默认取2，表示分（金钱） **表无对应字段 将费率单位标识作为费率单位类型
    private long m_strLower=0;
    private long  m_strUpper=0;
    
    public Tariff(){}
    
    public Tariff(RuleTariff tariff){
    	this.m_lnTariffId=tariff.getTariff_id();
    	this.m_nTariffSn=tariff.getTariff_sn();
    	this.m_lnRateUnit=tariff.getRate_unit();
    	this.m_nFeeRate=tariff.getFee_rate();
    	this.m_nTailMode=tariff.getTail_mod();
    	this.m_nDisctValue=tariff.getDisct_value();
    	this.m_nDisctValueBase=tariff.getDisct_value_base();
    	this.m_nDateType=tariff.getDate_type();
    	this.m_nHolidayId=tariff.getHoliday_id();
    	this.m_strLower=tariff.getLower();
    	this.m_strUpper=tariff.getUpper();
    	this.m_nUnitTypeId=tariff.getTariff_unit_id();

    }
	public long getM_lnTariffId() {
		return m_lnTariffId;
	}
	public void setM_lnTariffId(long m_lnTariffId) {
		this.m_lnTariffId = m_lnTariffId;
	}
	public int getM_nTariffSn() {
		return m_nTariffSn;
	}
	public void setM_nTariffSn(int m_nTariffSn) {
		this.m_nTariffSn = m_nTariffSn;
	}
	public int getM_lnRateUnit() {
		return m_lnRateUnit;
	}
	public void setM_lnRateUnit(int m_lnRateUnit) {
		this.m_lnRateUnit = m_lnRateUnit;
	}
	public long getM_nFeeRate() {
		return m_nFeeRate;
	}
	public void setM_nFeeRate(long m_nFeeRate) {
		this.m_nFeeRate = m_nFeeRate;
	}
	public int getM_nTailMode() {
		return m_nTailMode;
	}
	public void setM_nTailMode(int m_nTailMode) {
		this.m_nTailMode = m_nTailMode;
	}
	public int getM_nDisctValue() {
		return m_nDisctValue;
	}
	public void setM_nDisctValue(int m_nDisctValue) {
		this.m_nDisctValue = m_nDisctValue;
	}
	public int getM_nDisctValueBase() {
		return m_nDisctValueBase;
	}
	public void setM_nDisctValueBase(int m_nDisctValueBase) {
		this.m_nDisctValueBase = m_nDisctValueBase;
	}
	public int getM_nDateType() {
		return m_nDateType;
	}
	public void setM_nDateType(int m_nDateType) {
		this.m_nDateType = m_nDateType;
	}
	public int getM_nHolidayId() {
		return m_nHolidayId;
	}
	public void setM_nHolidayId(int m_nHolidayId) {
		this.m_nHolidayId = m_nHolidayId;
	}
	public int getM_nUnitTypeId() {
		return m_nUnitTypeId;
	}
	public void setM_nUnitTypeId(int m_nUnitTypeId) {
		this.m_nUnitTypeId = m_nUnitTypeId;
	}
	public long getM_strLower() {
		return m_strLower;
	}
	public void setM_strLower(long m_strLower) {
		this.m_strLower = m_strLower;
	}
	public long getM_strUpper() {
		return m_strUpper;
	}
	public void setM_strUpper(long m_strUpper) {
		this.m_strUpper = m_strUpper;
	}
    
    /**
     * @author sung
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
    	if(this==obj){
    		return true;
    	}
    	if(obj==null){
    		return false;
    	}
    	if(getClass() !=obj.getClass()){
    		return false;
    	}
    	final Tariff other=(Tariff)obj;
    	if(m_lnTariffId !=other.getM_lnTariffId()){
    		return false;
    	}
    	if(m_nDateType !=other.getM_nDateType()){
    		return false;
    	}
    	if(m_nTariffSn !=other.getM_nTariffSn()){
    		return false;
    	}
    	return true;
    }
    
    
    /**
     * @author sung
     *
     * @return
     */
    @Override
    public String toString() {
    	String str="m_lnTariffId["+m_lnTariffId+"],m_nTariffSn["+m_nTariffSn+"],m_nFeeRate["+m_nFeeRate+"],m_strLower"+
    			m_strLower+"],m_strUpper"+m_strUpper+"],m_nDateType["+m_nDateType+"]";
    	return str;
    }
}
