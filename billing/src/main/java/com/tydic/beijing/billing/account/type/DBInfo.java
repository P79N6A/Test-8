package com.tydic.beijing.billing.account.type;

public class DBInfo {
	private String db_driver;
	private String username;
	private String password;
	private String db_url;
	private String userSql;
	private int fetchSize;
	public String getDb_driver() {
		return db_driver;
	}
	public void setDb_driver(String db_driver) {
		this.db_driver = db_driver;
	}
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
	public String getUserSql() {
		return userSql;
	}
	public void setUserSql(String userSql) {
		this.userSql = userSql;
	}
	public int getFetchSize() {
		return fetchSize;
	}
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}
}
