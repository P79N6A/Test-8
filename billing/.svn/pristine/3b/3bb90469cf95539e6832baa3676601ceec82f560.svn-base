package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.List;

public class BalanceInMsg {
	private List<BalanceContent> ltFree  = new ArrayList<BalanceContent>();        //释放
	private List<BalanceContent> ltBack = new ArrayList<BalanceContent>();        //返回，解析B04组金钱类的保存位置
	private List<BalanceContent> ltQuery = new ArrayList<BalanceContent>();       //查询
	private List<BalanceContent> ltDeduct = new ArrayList<BalanceContent>();      //实扣，解析B03组金钱类的保存位置
	private List<BalanceContent> ltReserve = new ArrayList<BalanceContent>();     //预占，解析B01组金钱类的保存位置
	private List<BalanceContent> ltRecharge = new ArrayList<BalanceContent>();    //充值
	private List<BalanceContent> ltUsed = new ArrayList<BalanceContent>();       //更新使用金额，解析B30金钱的保存位置

	private   boolean bIsQueryBalance =false;              //ture:返回余额查询结果  false:不查询
	private   boolean bIsNegativeBalance =false;           //是否可以扣成负数表示  ture:可以  false:不可以
	private   boolean bIsMoneyRequest =false;              //金钱类型请求
    private   int  nRequestGroupCnt =0;              //ismp请求的组数，用于判断直接扣费消息是不是需要批价，对事件扣费和补款

    private   int  nRealCtrlType =-1;               //实扣附加控制类型 1:附加月封顶控制 2:附加月查重控制
    private   int  nRealCtrlPara =-1 ;               //实扣附加控制参数
    private   int  nRatableVacTick =0;             //累积量踢重
	
	public List<BalanceContent> getLtUsed() {
		return ltUsed;
	}
	public void setLtUsed(List<BalanceContent> ltUsed) {
		this.ltUsed = ltUsed;
	}
	public boolean isbIsQueryBalance() {
		return bIsQueryBalance;
	}
	public void setbIsQueryBalance(boolean bIsQueryBalance) {
		this.bIsQueryBalance = bIsQueryBalance;
	}
	public boolean isbIsNegativeBalance() {
		return bIsNegativeBalance;
	}
	public void setbIsNegativeBalance(boolean bIsNegativeBalance) {
		this.bIsNegativeBalance = bIsNegativeBalance;
	}
	public boolean isbIsMoneyRequest() {
		return bIsMoneyRequest;
	}
	public void setbIsMoneyRequest(boolean bIsMoneyRequest) {
		this.bIsMoneyRequest = bIsMoneyRequest;
	}
	public int getnRequestGroupCnt() {
		return nRequestGroupCnt;
	}
	public void setnRequestGroupCnt(int nRequestGroupCnt) {
		this.nRequestGroupCnt = nRequestGroupCnt;
	}
	public int getnRealCtrlType() {
		return nRealCtrlType;
	}
	public void setnRealCtrlType(int nRealCtrlType) {
		this.nRealCtrlType = nRealCtrlType;
	}
	public int getnRealCtrlPara() {
		return nRealCtrlPara;
	}
	public void setnRealCtrlPara(int nRealCtrlPara) {
		this.nRealCtrlPara = nRealCtrlPara;
	}
	public int getnRatableVacTick() {
		return nRatableVacTick;
	}
	public void setnRatableVacTick(int nRatableVacTick) {
		this.nRatableVacTick = nRatableVacTick;
	}
	public List<BalanceContent> getLtFree() {
		return ltFree;
	}
	public void setLtFree(List<BalanceContent> ltFree) {
		this.ltFree = ltFree;
	}
	public List<BalanceContent> getLtBack() {
		return ltBack;
	}
	public void setLtBack(List<BalanceContent> ltBack) {
		this.ltBack = ltBack;
	}
	public List<BalanceContent> getLtQuery() {
		return ltQuery;
	}
	public void setLtQuery(List<BalanceContent> ltQuery) {
		this.ltQuery = ltQuery;
	}
	public List<BalanceContent> getLtDeduct() {
		return ltDeduct;
	}
	public void setLtDeduct(List<BalanceContent> ltDeduct) {
		this.ltDeduct = ltDeduct;
	}
	public List<BalanceContent> getLtReserve() {
		return ltReserve;
	}
	public void setLtReserve(List<BalanceContent> ltReserve) {
		this.ltReserve = ltReserve;
	}
	public List<BalanceContent> getLtRecharge() {
		return ltRecharge;
	}
	public void setLtRecharge(List<BalanceContent> ltRecharge) {
		this.ltRecharge = ltRecharge;
	}
	
 
}
