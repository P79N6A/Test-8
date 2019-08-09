package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class VoiceCdr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tariffinfo;
	public String getTariffinfo() {
		return tariffinfo;
	}
	public void setTariffinfo(String tariffinfo) {
		this.tariffinfo = tariffinfo;
	}
	
	
	
}
