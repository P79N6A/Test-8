/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua.dao;

import java.io.Serializable;
import java.sql.Date;

/**
 * 获取接入码 <br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class SmSprAccessNumber implements Serializable {

	private static final long serialVersionUID = 1L;
	public String access_number;
	public int number_length;
	public String carrier_type;
	public Date eff_date;
	public Date exp_date;

	public final String getAccess_number() {
		return access_number;
	}

	public final void setAccess_number(String access_number) {
		this.access_number = access_number;
	}

	public final int getNumber_length() {
		return number_length;
	}

	public final void setNumber_length(int number_length) {
		this.number_length = number_length;
	}

	public final String getCarrier_type() {
		return carrier_type;
	}

	public final void setCarrier_type(String carrier_type) {
		this.carrier_type = carrier_type;
	}

	public final Date getEff_date() {
		return eff_date;
	}

	public final void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}

	public final Date getExp_date() {
		return exp_date;
	}

	public final void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}

	@Override
	public String toString() {
		return "SmSprAccessNumber [access_number=" + access_number + ", number_length="
				+ number_length + ", carrier_type=" + carrier_type + ", eff_date=" + eff_date
				+ ", exp_date=" + exp_date + "]";
	}
}
