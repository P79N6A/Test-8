package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

public class InfoUserExternalAccount implements Serializable {
	private static final long serialVersionUID = 1L;
	private String external_account_id;
	private String user_id;
	private String jd_pin;
	private String external_system_code;
	private String external_account_code;
	private String eff_date;
	private String exp_date;
	private String create_date;

	public String getExternal_account_id() {
		return external_account_id;
	}

	public void setExternal_account_id(String external_account_id) {
		this.external_account_id = external_account_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getJd_pin() {
		return jd_pin;
	}

	public void setJd_pin(String jd_pin) {
		this.jd_pin = jd_pin;
	}

	public String getExternal_system_code() {
		return external_system_code;
	}

	public void setExternal_system_code(String external_system_code) {
		this.external_system_code = external_system_code;
	}

	public String getExternal_account_code() {
		return external_account_code;
	}

	public void setExternal_account_code(String external_account_code) {
		this.external_account_code = external_account_code;
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

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	@Override
	public String toString() {
		return "InfoUserExternalAccount [external_account_id=" + external_account_id + ", user_id="
				+ user_id + ", jd_pin=" + jd_pin + ", external_system_code=" + external_system_code
				+ ", external_account_code=" + external_account_code + ", eff_date=" + eff_date
				+ ", exp_date=" + exp_date + ", create_date=" + create_date + "]";
	}

}
