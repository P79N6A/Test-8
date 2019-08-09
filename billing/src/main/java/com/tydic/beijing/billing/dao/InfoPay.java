/**
 * 
 */
package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sung
 *
 */

public class InfoPay implements Serializable{

	private static final long serialVersionUID = 1L;
	private String pay_id;
	private String pay_name;
	private String cust_id;
	private Date create_date;
	private Date exp_date;
	private String prepay_flag;
	private String local_net;

	public String getPay_id() {
		return pay_id;
	}

	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}

	public String getPay_name() {
		return pay_name;
	}

	public void setPay_name(String pay_name) {
		this.pay_name = pay_name;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}

	public String getPrepay_flag() {
		return prepay_flag;
	}

	public void setPrepay_flag(String prepay_flag) {
		this.prepay_flag = prepay_flag;
	}

	public String getLocal_net() {
		return local_net;
	}

	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}

	@Override
	public String toString() {
		return "InfoPay [pay_id=" + pay_id + ", pay_name=" + pay_name
				+ ", cust_id=" + cust_id + ", create_date=" + create_date
				+ ", exp_date=" + exp_date + ", prepay_flag=" + prepay_flag
				+ ", local_net=" + local_net + "]";
	}

}
