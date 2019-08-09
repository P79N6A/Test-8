package com.tydic.beijing.billing.credit.memcache.dao;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Id;

import com.tydic.uda.UdaAnnotationSetKey;

public class InfoPayBalance4CreditMemcache implements Serializable {
	public static final String KEY_PREFIX = InfoPayBalance4CreditMemcache.class.getSimpleName();
	private static final long serialVersionUID = 1L;
	public String mem_key;
	public long balance_id;
	public String pay_id;
	public int balance_type_id;
	public long balance;
	public long real_balance;
	public long used_balance;
	public int latn_id;
	public Date eff_date;
	public Date exp_date;
	public String local_net;

	@Id
	public String getMem_key() {
		return mem_key;
	}

	@UdaAnnotationSetKey
	public void setMem_key(String mem_key) {
		this.mem_key = mem_key + pay_id;
	}

	public long getBalance_id() {
		return balance_id;
	}

	public void setBalance_id(long balance_id) {
		this.balance_id = balance_id;
	}

	public String getPay_id() {
		return pay_id;
	}

	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}

	public int getBalance_type_id() {
		return balance_type_id;
	}

	public void setBalance_type_id(int balance_type_id) {
		this.balance_type_id = balance_type_id;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public long getReal_balance() {
		return real_balance;
	}

	public void setReal_balance(long real_balance) {
		this.real_balance = real_balance;
	}

	public long getUsed_balance() {
		return used_balance;
	}

	public void setUsed_balance(long used_balance) {
		this.used_balance = used_balance;
	}

	public int getLatn_id() {
		return latn_id;
	}

	public void setLatn_id(int latn_id) {
		this.latn_id = latn_id;
	}

	public Date getEff_date() {
		return eff_date;
	}

	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}

	public String getLocal_net() {
		return local_net;
	}

	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}

	@Override
	public String toString() {
		return "InfoPayBalance4Credit [balance_id=" + balance_id + ", pay_id=" + pay_id
				+ ", balance_type_id=" + balance_type_id + ", balance=" + balance
				+ ", real_balance=" + real_balance + ", used_balance=" + used_balance
				+ ", latn_id=" + latn_id + ", eff_date=" + eff_date + ", exp_date=" + exp_date
				+ ", local_net=" + local_net + "]";
	}

	public void clear() {
		mem_key = "";
		balance_id = -1;
		pay_id = "";
		balance_type_id = -1;
		balance = 0;
		real_balance = 0;
		used_balance = 0;
		latn_id = -1;
		eff_date = null;
		exp_date = null;
		local_net = "";
	}

}
