package com.tydic.beijing.billing.credit.dao;

import java.io.Serializable;

public class QCreditInfoUserCredit implements Serializable {

	private static final long serialVersionUID = 1L;

	public String serial_num;
	public int action;
	public String enqueue_date;
	public String local_net;
	public String user_id;
	public String credit_type;
	public long credit_number;
	public String eff_date;
	public String exp_date;
	public String eff_flag;
	public String row_id;

	public String getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getEnqueue_date() {
		return enqueue_date;
	}

	public void setEnqueue_date(String enqueue_date) {
		this.enqueue_date = enqueue_date;
	}

	public String getLocal_net() {
		return local_net;
	}

	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCredit_type() {
		return credit_type;
	}

	public void setCredit_type(String credit_type) {
		this.credit_type = credit_type;
	}

	public long getCredit_number() {
		return credit_number;
	}

	public void setCredit_number(long credit_number) {
		this.credit_number = credit_number;
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

	public String getEff_flag() {
		return eff_flag;
	}

	public void setEff_flag(String eff_flag) {
		this.eff_flag = eff_flag;
	}

	public String getRow_id() {
		return row_id;
	}

	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}

	@Override
	public String toString() {
		return "QCreditInfoUserCredit [serial_num=" + serial_num + ", action=" + action
				+ ", enqueue_date=" + enqueue_date + ", local_net=" + local_net + ", user_id="
				+ user_id + ", credit_type=" + credit_type + ", credit_number=" + credit_number
				+ ", eff_date=" + eff_date + ", exp_date=" + exp_date + ", eff_flag=" + eff_flag
				+ ", row_id=" + row_id + "]";
	}

}
