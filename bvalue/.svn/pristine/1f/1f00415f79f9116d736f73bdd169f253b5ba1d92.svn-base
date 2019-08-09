package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class InfoPayBalanceBilling implements Serializable {
	private static final long serialVersionUID = 1L;

	private long balance;
	private int type;
	private String eff_date;
	private String exp_date;
	
    public InfoPayBalanceBilling(){
		
	}

	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getEff_date() {
		return eff_date;
	}
	public void setEff_date(String eff_date) {
		this.eff_date = eff_date;
	}
	public String getExp_date() {
		return exp_date;
	}
	public void setExp_date(String exp_date) {
		this.exp_date = exp_date;
	}
	public InfoPayBalanceBilling(InfoPayBalanceBilling ipbb){
		this.balance=ipbb.balance;
		this.type=ipbb.type;
		this.eff_date=ipbb.eff_date;
		this.exp_date=ipbb.exp_date;
	}
	
	public boolean compare(InfoPayBalanceBilling ipbb){
		if(this.type == ipbb.type && this.exp_date.equals(ipbb.exp_date))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "InfoPayBalanceBilling [balance=" + balance + ", type=" + type +", eff_date="
				+ eff_date + ", exp_date=" + exp_date + "]";
	}
	
}
