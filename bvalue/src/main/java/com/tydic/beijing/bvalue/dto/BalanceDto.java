package com.tydic.beijing.bvalue.dto;


public class BalanceDto {

	
	private long balance ;
	private String expDate ;
	
	public BalanceDto(){}
	
	public BalanceDto(long value,String exp){
		this.balance=value;
		this.expDate=exp;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	
	
	

	
	
}
