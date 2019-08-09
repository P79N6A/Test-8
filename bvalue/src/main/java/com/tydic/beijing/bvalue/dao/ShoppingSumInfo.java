package com.tydic.beijing.bvalue.dao;

import java.util.List;

import org.apache.log4j.Logger;

public class ShoppingSumInfo {
	
	private static Logger log=Logger.getLogger(ShoppingSumInfo.class);
	
	private String jdpin;
	private String amount;
	private List<ShoppingDetail> items;
	public String getJdpin() {
		return jdpin;
	}
	public void setJdpin(String jdpin) {
		this.jdpin = jdpin;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public List<ShoppingDetail> getItems() {
		return items;
	}
	public void setItems(List<ShoppingDetail> items) {
		this.items = items;
	}
	
	
	public void print() {
		 
		log.debug("jdpin="+jdpin +",amount="+amount+",items.size="+items.size());

	}
	
	
	 
		

}
