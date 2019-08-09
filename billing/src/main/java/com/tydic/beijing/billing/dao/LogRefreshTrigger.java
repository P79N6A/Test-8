package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class LogRefreshTrigger implements Serializable {
	private static final long serialVersionUID = 1L;
	private int refresh_batch_id;
	private String service_name;
	private String service_ip;
	private String service_port;
	private String service_pid;
	private String datastore_name;
	private int refresh_now;
	private String refresh_status;
	private String memo;

	public int getRefresh_batch_id() {
		return refresh_batch_id;
	}

	public void setRefresh_batch_id(int refresh_batch_id) {
		this.refresh_batch_id = refresh_batch_id;
	}

	public String getService_name() {
		return service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public String getService_ip() {
		return service_ip;
	}

	public void setService_ip(String service_ip) {
		this.service_ip = service_ip;
	}

	public String getService_port() {
		return service_port;
	}

	public void setService_port(String service_port) {
		this.service_port = service_port;
	}

	public String getService_pid() {
		return service_pid;
	}

	public void setService_pid(String service_pid) {
		this.service_pid = service_pid;
	}

	public String getDatastore_name() {
		return datastore_name;
	}

	public void setDatastore_name(String datastore_name) {
		this.datastore_name = datastore_name;
	}

	public int getRefresh_now() {
		return refresh_now;
	}

	public void setRefresh_now(int refresh_now) {
		this.refresh_now = refresh_now;
	}

	public String getRefresh_status() {
		return refresh_status;
	}

	public void setRefresh_status(String refresh_status) {
		this.refresh_status = refresh_status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
