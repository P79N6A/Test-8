package com.tydic.beijing.billing.account.type;

public class TariffInfo {
	public int tariiff_id;//费用代码
	public long fee;//费用
	
	public int getTariiff_id() {
		return tariiff_id;
	}
	public void setTariiff_id(int tariiff_id) {
		this.tariiff_id = tariiff_id;
	}
	public long getFee() {
		return fee;
	}
	public void setFee(long fee) {
		this.fee = fee;
	}
}
