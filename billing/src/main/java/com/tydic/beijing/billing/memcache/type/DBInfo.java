package com.tydic.beijing.billing.memcache.type;

public class DBInfo {
	private String username;
	private String password;
	private String db_url;
	private String acct_month;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDb_url() {
		return db_url;
	}
	public void setDb_url(String db_url) {
		this.db_url = db_url;
	}
	public String getAcct_month() {
		return acct_month;
	}
	public void setAcct_month(String acct_month) {
		this.acct_month = acct_month;
	}
}
