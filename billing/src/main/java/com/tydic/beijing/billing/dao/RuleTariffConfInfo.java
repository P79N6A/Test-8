package com.tydic.beijing.billing.dao;

import java.io.Serializable;

public class RuleTariffConfInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private long tariff_id;
	private String local_net;
	private int tariff_mode;
	private String ref_member_type;
	private long member_eff_mode;
	private String member_state_group;
	private long upper_value;
	private long lower_value;
	private long tariff_value;
	private long acct_item_code;
	private int item_group;
	private String eff_date;
	private String exp_date;
	private long event_type_id;
	private String tariff_name;
	private String remark;
	private String product_id;
	private String user_product_id;
	private String cyc_flag;
	public String getCyc_flag() {
		return cyc_flag;
	}

	public void setCyc_flag(String cyc_flag) {
		this.cyc_flag = cyc_flag;
	}

	public String getUser_product_id() {
		return user_product_id;
	}

	public void setUser_product_id(String user_product_id) {
		this.user_product_id = user_product_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public long getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(long tariff_id) {
		this.tariff_id = tariff_id;
	}

	public String getLocal_net() {
		return local_net;
	}

	public void setLocal_net(String local_net) {
		this.local_net = local_net;
	}

	public int getTariff_mode() {
		return tariff_mode;
	}

	public void setTariff_mode(int tariff_mode) {
		this.tariff_mode = tariff_mode;
	}

	public String getRef_member_type() {
		return ref_member_type;
	}

	public void setRef_member_type(String ref_member_type) {
		this.ref_member_type = ref_member_type;
	}

	public long getMember_eff_mode() {
		return member_eff_mode;
	}

	public void setMember_eff_mode(long member_eff_mode) {
		this.member_eff_mode = member_eff_mode;
	}

	public String getMember_state_group() {
		return member_state_group;
	}

	public void setMember_state_group(String member_state_group) {
		this.member_state_group = member_state_group;
	}

	public long getUpper_value() {
		return upper_value;
	}

	public void setUpper_value(long upper_value) {
		this.upper_value = upper_value;
	}

	public long getLower_value() {
		return lower_value;
	}

	public void setLower_value(long lower_value) {
		this.lower_value = lower_value;
	}

	public long getTariff_value() {
		return tariff_value;
	}

	public void setTariff_value(long tariff_value) {
		this.tariff_value = tariff_value;
	}

	public long getAcct_item_code() {
		return acct_item_code;
	}

	public void setAcct_item_code(long acct_item_code) {
		this.acct_item_code = acct_item_code;
	}

	public long getItem_group() {
		return item_group;
	}

	public void setItem_group(int item_group) {
		this.item_group = item_group;
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

	public long getEvent_type_id() {
		return event_type_id;
	}

	public void setEvent_type_id(long event_type_id) {
		this.event_type_id = event_type_id;
	}

	public String getTariff_name() {
		return tariff_name;
	}

	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Object clone() {
		RuleTariffConfInfo o = null;
		try {
			o = (RuleTariffConfInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	public String toString() {
		return "RuleTariffConfInfo [tariff_id=" + tariff_id + ",local_net="
				+ local_net + ",tariff_mode=" + tariff_mode
				+ ",ref_member_type=" + ref_member_type + ",member_eff_mode="
				+ member_eff_mode + ",member_state_group=" + member_state_group
				+ ",upper_value=" + upper_value + ",lower_value=" + lower_value
				+ ",tariff_value=" + tariff_value + ",acct_item_code="
				+ acct_item_code + ",item_group=" + item_group + ",eff_date="
				+ eff_date + ",exp_date=" + exp_date + ",event_type_id="
				+ event_type_id + ",tariff_name=" + tariff_name + ",remark="
				+ remark +",product_id=" +product_id+",user_product_id="+user_product_id+ "]";
	}
}
