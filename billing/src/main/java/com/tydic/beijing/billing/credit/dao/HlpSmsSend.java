package com.tydic.beijing.billing.credit.dao;

import java.io.Serializable;

public class HlpSmsSend implements Serializable {

	private static final long serialVersionUID = 1L;
	private String msg_id;
	private String msisdn_send;
	private String msisdn_receive;
	private int priority;
	private String message_text;
	private String send_time;
	private String create_time;
	private int retry_times;

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getMsisdn_send() {
		return msisdn_send;
	}

	public void setMsisdn_send(String msisdn_send) {
		this.msisdn_send = msisdn_send;
	}

	public String getMsisdn_receive() {
		return msisdn_receive;
	}

	public void setMsisdn_receive(String msisdn_receive) {
		this.msisdn_receive = msisdn_receive;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getMessage_text() {
		return message_text;
	}

	public void setMessage_text(String message_text) {
		this.message_text = message_text;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getRetry_times() {
		return retry_times;
	}

	public void setRetry_times(int retry_times) {
		this.retry_times = retry_times;
	}

	@Override
	public String toString() {
		return "HlpSmsSend [msg_id=" + msg_id + ", msisdn_send=" + msisdn_send
				+ ", msisdn_receive=" + msisdn_receive + ", priority=" + priority
				+ ", message_text=" + message_text + ", send_time=" + send_time + ", create_time="
				+ create_time + ", retry_times=" + retry_times + "]";
	}

}
