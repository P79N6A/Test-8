package com.tydic.beijing.billing.rating.domain;

import java.util.Date;

public class RuleDinnerSelect {

	private int ofr_b_id ;
	private int   latn_id  ;
	private int   msg_type ;
	private int   offset_type ;
	private int   atom_ofr ;
	private long   offset_low ;
	private long   offset_upper ;
	private Date   eff_date  ;
	private Date   exp_date  ;
	public int getOfr_b_id() {
		return ofr_b_id;
	}
	public void setOfr_b_id(int ofr_b_id) {
		this.ofr_b_id = ofr_b_id;
	}
	public int getLatn_id() {
		return latn_id;
	}
	public void setLatn_id(int latn_id) {
		this.latn_id = latn_id;
	}
	public int getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(int msg_type) {
		this.msg_type = msg_type;
	}
	public int getOffset_type() {
		return offset_type;
	}
	public void setOffset_type(int offset_type) {
		this.offset_type = offset_type;
	}
	public int getAtom_ofr() {
		return atom_ofr;
	}
	public void setAtom_ofr(int atom_ofr) {
		this.atom_ofr = atom_ofr;
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
	public long getOffset_low() {
		return offset_low;
	}
	public void setOffset_low(long offset_low) {
		this.offset_low = offset_low;
	}
	public long getOffset_upper() {
		return offset_upper;
	}
	public void setOffset_upper(long offset_upper) {
		this.offset_upper = offset_upper;
	}
	
	
}
