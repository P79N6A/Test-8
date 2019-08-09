package com.tydic.beijing.billing.rating.domain;

import java.util.Date;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.rating.CopyOfMain;

/**
 * 三户保存的是订购套餐，计费最初拿到的是b套餐，最终批价用的是原子套餐，所以建立这个类，用户保存前后映射关系
 * @author zhanghengbo
 *
 */
public class UserDiscts {
	
	private static final Logger log = Logger.getLogger(UserDiscts.class);
	
	private String ofrId;   //用户订购套餐 来自lifeuserproduct
	private int ofr_b_id ;      //计费b套餐
	
	private int atomId;  //原子套餐,下面的也都是原子套餐的属性
	private String  ofr_name ;   
	private int   ofr_type_id;   
	private int   brand_id ;     
	private String   ofr_desc ;       
	private int   pricing_plan_id; 
	private Date  crt_date ;       
	private Date  eff_date;        
	private Date  exp_date ;       
	private Date  mod_date ;       
	private int   latn_id ;        
	private int   priority ;       
	private int   ratable_flag ;
	public String getOfrId() {
		return ofrId;
	}
	public void setOfrId(String ofrId) {
		this.ofrId = ofrId;
	}
	public int getOfr_b_id() {
		return ofr_b_id;
	}
	public void setOfr_b_id(int ofr_b_id) {
		this.ofr_b_id = ofr_b_id;
	}
	public int getAtomId() {
		return atomId;
	}
	public void setAtomId(int atomId) {
		this.atomId = atomId;
	}
	public String getOfr_name() {
		return ofr_name;
	}
	public void setOfr_name(String ofr_name) {
		this.ofr_name = ofr_name;
	}
	public int getOfr_type_id() {
		return ofr_type_id;
	}
	public void setOfr_type_id(int ofr_type_id) {
		this.ofr_type_id = ofr_type_id;
	}
	public int getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}
	public String getOfr_desc() {
		return ofr_desc;
	}
	public void setOfr_desc(String ofr_desc) {
		this.ofr_desc = ofr_desc;
	}
	public int getPricing_plan_id() {
		return pricing_plan_id;
	}
	public void setPricing_plan_id(int pricing_plan_id) {
		this.pricing_plan_id = pricing_plan_id;
	}
	public Date getCrt_date() {
		return crt_date;
	}
	public void setCrt_date(Date crt_date) {
		this.crt_date = crt_date;
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
	public Date getMod_date() {
		return mod_date;
	}
	public void setMod_date(Date mod_date) {
		this.mod_date = mod_date;
	}
	public int getLatn_id() {
		return latn_id;
	}
	public void setLatn_id(int latn_id) {
		this.latn_id = latn_id;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getRatable_flag() {
		return ratable_flag;
	}
	public void setRatable_flag(int ratable_flag) {
		this.ratable_flag = ratable_flag;
	}
	
	
	public UserDiscts(CodeOfr codeOfr){

		this.atomId           = codeOfr.getOfr_b_id();
		this.ofr_name         = codeOfr.getOfr_name();   
		this.ofr_type_id      = codeOfr.getOfr_type_id();   
		this.brand_id         = codeOfr.getBrand_id();     
		this.ofr_desc         = codeOfr.getOfr_desc();       
		this.pricing_plan_id  = codeOfr.getPricing_plan_id(); 
		this.crt_date         = codeOfr.getCrt_date();       
		this.eff_date         = codeOfr.getEff_date();        
		this.exp_date         = codeOfr.getExp_date();       
		this.mod_date         = codeOfr.getMod_date();       
		this.latn_id          = codeOfr.getLatn_id();        
		this.priority         = codeOfr.getPriority();       
		this.ratable_flag     = codeOfr.getRatable_flag();
		
	}
	
	
	public void print(){
		String outmsg = this.ofrId            +"&&"+          
				this.ofr_b_id         +"&&"+          
				this.atomId           +"&&"+          
				this.ofr_name         +"&&"+          
				this.ofr_type_id      +"&&"+          
				this.brand_id         +"&&"+          
				this.ofr_desc         +"&&"+          
				this.pricing_plan_id  +"&&"+          
				this.crt_date         +"&&"+          
				this.eff_date         +"&&"+          
				this.exp_date         +"&&"+          
				this.mod_date         +"&&"+          
				this.latn_id          +"&&"+          
				this.priority         +"&&"+          
				this.ratable_flag                    ;
		
		log.debug(outmsg);

	}

	
	
}
