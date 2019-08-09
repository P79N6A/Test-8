package com.tydic.beijing.billing.sms.dao;

import java.io.Serializable;
import java.sql.Date;

public class RuleSmsRecvOpt implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sms_template;
	private String operate_mode;
	private String operate_value;
	private String eff_falg;
	private Date create_time;
	private Date update_time;
	private String staff_id;
	private String remark;
	private String reserve_1;
	private String reserve_2;
	private String reserve_3;
	private String reserve_4;
	private String reserve_5;
	private String reserve_6;
	private String reserve_7;
	private String reserve_8;
	public String getSms_template() {
		return sms_template;
	}
	public void setSms_template(String sms_template) {
		this.sms_template = sms_template;
	}
	public String getOperate_mode() {
		return operate_mode;
	}
	public void setOperate_mode(String operate_mode) {
		this.operate_mode = operate_mode;
	}
	public String getOperate_value() {
		return operate_value;
	}
	public void setOperate_value(String operate_value) {
		this.operate_value = operate_value;
	}
	public String getEff_falg() {
		return eff_falg;
	}
	public void setEff_falg(String eff_falg) {
		this.eff_falg = eff_falg;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReserve_1() {
		return reserve_1;
	}
	public void setReserve_1(String reserve_1) {
		this.reserve_1 = reserve_1;
	}
	public String getReserve_2() {
		return reserve_2;
	}
	public void setReserve_2(String reserve_2) {
		this.reserve_2 = reserve_2;
	}
	public String getReserve_3() {
		return reserve_3;
	}
	public void setReserve_3(String reserve_3) {
		this.reserve_3 = reserve_3;
	}
	public String getReserve_4() {
		return reserve_4;
	}
	public void setReserve_4(String reserve_4) {
		this.reserve_4 = reserve_4;
	}
	public String getReserve_5() {
		return reserve_5;
	}
	public void setReserve_5(String reserve_5) {
		this.reserve_5 = reserve_5;
	}
	public String getReserve_6() {
		return reserve_6;
	}
	public void setReserve_6(String reserve_6) {
		this.reserve_6 = reserve_6;
	}
	public String getReserve_7() {
		return reserve_7;
	}
	public void setReserve_7(String reserve_7) {
		this.reserve_7 = reserve_7;
	}
	public String getReserve_8() {
		return reserve_8;
	}
	public void setReserve_8(String reserve_8) {
		this.reserve_8 = reserve_8;
	}
	@Override
	public String toString() {
		return "RuleSmsRecvOpt [sms_template=" + sms_template
				+ ", operate_mode=" + operate_mode + ", operate_value="
				+ operate_value + ", eff_falg=" + eff_falg + ", create_time="
				+ create_time + ", update_time=" + update_time + ", staff_id="
				+ staff_id + ", remark=" + remark + ", reserve_1=" + reserve_1
				+ ", reserve_2=" + reserve_2 + ", reserve_3=" + reserve_3
				+ ", reserve_4=" + reserve_4 + ", reserve_5=" + reserve_5
				+ ", reserve_6=" + reserve_6 + ", reserve_7=" + reserve_7
				+ ", reserve_8=" + reserve_8 + "]";
	}
	
}
