/**
 * 
 */
package com.tydic.beijing.billing.rating.domain;

/**
 * @author sung
 *
 */
public class TbDeductRecordHistory {

	private long Pkid;
	private long SmId;
	private long Acct_Balance_Id;
	private int Latn_Id;
	private String Area_Code;
	private String Service_Nbr;
	private int Msg_Type;
	private int Amount;
	private int Unit_Type_Id;
	private int Back_Count;
	private String Ratable_Info;
	private int Acct_Item_Type_Id;
	private int Is_Credit;
	private String Create_Time;
	public long getPkid() {
		return Pkid;
	}
	public void setPkid(long pkid) {
		Pkid = pkid;
	}
	
	public long getSmId() {
		return SmId;
	}
	
	public void setSmId(long smId) {
		SmId = smId;
	}
	public long getAcct_Balance_Id() {
		return Acct_Balance_Id;
	}
	public void setAcct_Balance_Id(long acct_Balance_Id) {
		Acct_Balance_Id = acct_Balance_Id;
	}
	
	public String getArea_Code() {
		return Area_Code;
	}
	public void setArea_Code(String area_Code) {
		Area_Code = area_Code;
	}
	public String getService_Nbr() {
		return Service_Nbr;
	}
	public void setService_Nbr(String service_Nbr) {
		Service_Nbr = service_Nbr;
	}
	
	public int getAmount() {
		return Amount;
	}
	public void setAmount(int amount) {
		Amount = amount;
	}
	
	public int getBack_Count() {
		return Back_Count;
	}
	public void setBack_Count(int back_Count) {
		Back_Count = back_Count;
	}
	public String getRatable_Info() {
		return Ratable_Info;
	}
	public void setRatable_Info(String ratable_Info) {
		Ratable_Info = ratable_Info;
	}
	
	
	public String getCreate_Time() {
		return Create_Time;
	}
	public void setCreate_Time(String create_Time) {
		Create_Time = create_Time;
	}
	public int getLatn_Id() {
		return Latn_Id;
	}
	public void setLatn_Id(int latn_Id) {
		Latn_Id = latn_Id;
	}
	public int getMsg_Type() {
		return Msg_Type;
	}
	public void setMsg_Type(int msg_Type) {
		Msg_Type = msg_Type;
	}
	public int getUnit_Type_Id() {
		return Unit_Type_Id;
	}
	public void setUnit_Type_Id(int unit_Type_Id) {
		Unit_Type_Id = unit_Type_Id;
	}
	
	public int getIs_Credit() {
		return Is_Credit;
	}
	public void setIs_Credit(int is_Credit) {
		Is_Credit = is_Credit;
	}
	public int getAcct_Item_Type_Id() {
		return Acct_Item_Type_Id;
	}
	public void setAcct_Item_Type_Id(int acct_Item_Type_Id) {
		Acct_Item_Type_Id = acct_Item_Type_Id;
	}
	
	
	
	
}
