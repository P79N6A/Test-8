package com.tydic.beijing.billing.rating.domain;

/**
 * 套餐累积量关系 rule_ofr_resource_rel
 * @author sung
 *
 */
public class RuleOfrResourceRel {

	private int ofr_Res_Id;    
	private int   ofr_B_Id ;     
	private String resource_Code; 
	private String  msg_Types ;	//逗号分隔
	public int getOfr_Res_Id() {
		return ofr_Res_Id;
	}
	public void setOfr_Res_Id(int ofr_Res_Id) {
		this.ofr_Res_Id = ofr_Res_Id;
	}
	public int getOfr_B_Id() {
		return ofr_B_Id;
	}
	public void setOfr_B_Id(int ofr_B_Id) {
		this.ofr_B_Id = ofr_B_Id;
	}
	public String getResource_Code() {
		return resource_Code;
	}
	public void setResource_Code(String resource_Code) {
		this.resource_Code = resource_Code;
	}
	public String getMsg_Types() {
		return msg_Types;
	}
	public void setMsg_Types(String msg_Types) {
		this.msg_Types = msg_Types;
	}
	
	
	   
	
	
	
}
