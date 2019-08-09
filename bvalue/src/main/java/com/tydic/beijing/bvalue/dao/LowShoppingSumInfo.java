package com.tydic.beijing.bvalue.dao;

import org.apache.log4j.Logger;

public class LowShoppingSumInfo {
	
	private static Logger log=Logger.getLogger(LowShoppingSumInfo.class);
	
	private String jdpin;
	private long bvalue;
	// private List<ShoppingDetail> items;
	
	public String getJdpin() {
		return jdpin;
	}




	public void setJdpin(String jdpin) {
		this.jdpin = jdpin;
	}




	public long getBvalue() {
		return bvalue;
	}




	public void setBvalue(long bvalue) {
		this.bvalue = bvalue;
	}
	
	public void print() {
		 
		log.debug("jdpin="+jdpin +",bvalue="+bvalue);

	}
	 
		

}
