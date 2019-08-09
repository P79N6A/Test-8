package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.Date;

public class TbSmsSend implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg_id;
	private String msisdn_send;
	private String msisdn_receive ;
	private String message_text;
	private Date send_time;
	private String process_tag;
	private String para_key;
	
	private String deal_flag ;
	private String complete_time;
	private String job_number;
	private long msg_prior;
	
	private int amount ;

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

	public String getMessage_text() {
		return message_text;
	}

	public void setMessage_text(String message_text) {
		this.message_text = message_text;
	}

	public Date getSend_time() {
		return send_time;
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}

	public String getProcess_tag() {
		return process_tag;
	}

	public void setProcess_tag(String process_tag) {
		this.process_tag = process_tag;
	}

	public String getPara_key() {
		return para_key;
	}

	public void setPara_key(String para_key) {
		this.para_key = para_key;
	}

	public String getDeal_flag() {
		return deal_flag;
	}

	public void setDeal_flag(String deal_flag) {
		this.deal_flag = deal_flag;
	}

	public String getComplete_time() {
		return complete_time;
	}

	public void setComplete_time(String complete_time) {
		this.complete_time = complete_time;
	}

	public String getJob_number() {
		return job_number;
	}

	public void setJob_number(String job_number) {
		this.job_number = job_number;
	}

	public long getMsg_prior() {
		return msg_prior;
	}

	public void setMsg_prior(long msg_prior) {
		this.msg_prior = msg_prior;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "TbSmsSend [msg_id=" + msg_id + ", msisdn_send=" + msisdn_send
				+ ", msisdn_receive=" + msisdn_receive + ", message_text="
				+ message_text + ", send_time=" + send_time + ", process_tag="
				+ process_tag + ", para_key=" + para_key + ", deal_flag="
				+ deal_flag + ", complete_time=" + complete_time
				+ ", job_number=" + job_number + ", msg_prior=" + msg_prior
				+ ", amount=" + amount + "]";
	}
	
	
	
}
