package com.tydic.beijing.billing.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.memcache.service.impl.DB2MemImpl;
import com.tydic.uda.UdaAnnotationSetKey;

public class UserInfoForMemCached implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(UserInfoForMemCached.class);

	private String device_number;
	private String keyString;//Key
	private String teleUserType;//1普通用户 2外网用户
	private InfoUser infoUser;
	private List<LifeUserProduct> userProducts = new ArrayList<LifeUserProduct>();
	private List<InfoPay> userInfoPays = new ArrayList<InfoPay>();
	private List<PayUserRel> payUserRels = new ArrayList<PayUserRel>();
	private List<UserCorpInfo> userCorpInfos = new ArrayList<UserCorpInfo>();
	
	public String getDevice_number() {
		return device_number;
	}
	public void setDevice_number(String device_number) {
		this.device_number = device_number;
	}
	
	@Id
	public String getKeyString() {
		return keyString;
	}
	@UdaAnnotationSetKey
	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}
	public InfoUser getInfoUser() {
		return infoUser;
	}
	public void setInfoUser(InfoUser infoUser) {
		this.infoUser = infoUser;
	}
	public List<LifeUserProduct> getUserProducts() {
		return userProducts;
	}
	public void setUserProducts(List<LifeUserProduct> userProducts) {
		this.userProducts = userProducts;
	}
	public List<InfoPay> getUserInfoPays() {
		return userInfoPays;
	}
	public void setUserInfoPays(List<InfoPay> userInfoPays) {
		this.userInfoPays = userInfoPays;
	}
	public List<PayUserRel> getPayUserRels() {
		return payUserRels;
	}
	public void setPayUserRels(List<PayUserRel> payUserRels) {
		this.payUserRels = payUserRels;
	}
	public String getTeleUserType() {
		return teleUserType;
	}
	public void setTeleUserType(String teleUserType) {
		this.teleUserType = teleUserType;
	}
	public List<UserCorpInfo> getUserCorpInfos() {
		return userCorpInfos;
	}
	public void setUserCorpInfos(List<UserCorpInfo> userCorpInfos) {
		this.userCorpInfos = userCorpInfos;
	}
	
	public PayUserRel getDefaultPayInfo(){
		for(PayUserRel payUserRel:this.payUserRels){
			if(payUserRel.getDefault_tag().equals("0") && payUserRel.getEff_flag().equals("0")){//有效的并且是默认的主账户
				return payUserRel;
			}
		}
		return null;
	}
	
	public String getOfrIdbyTime(String cdrTime){
		try {
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
			for(LifeUserProduct lup:this.userProducts){
				if(lup.getEff_date().before(sdf.parse(cdrTime))
						&& lup.getExp_date().after(sdf.parse(cdrTime)))
					return lup.getOfr_id();
			}
		} catch (ParseException e) {
			LOGGER.error("根据话单时间获取用户销售品异常",e);
			return null;
		}
		return null;
	}
	
}

//package com.tydic.beijing.billing.dao;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.Id;
//
//import com.tydic.uda.UdaAnnotationSetKey;
//
//public class UserInfoForMemCached implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//	
//	private String device_number; 
//	private InfoUser infoUser;
//	private List<LifeUserProduct> userProducts = new ArrayList<LifeUserProduct>();
//	private List<InfoPay> userInfoPays = new ArrayList<InfoPay>();
//	private List<PayUserRel> payUserRels = new ArrayList<PayUserRel>();
//	@Id
//	public String getDevice_number() {
//		return device_number;
//	}
//	@UdaAnnotationSetKey
//	public void setDevice_number(String device_number) {
//		this.device_number = device_number;
//	}
//	public InfoUser getInfoUser() {
//		return infoUser;
//	}
//	public void setInfoUser(InfoUser infoUser) {
//		this.infoUser = infoUser;
//	}
//	public List<LifeUserProduct> getUserProducts() {
//		return userProducts;
//	}
//	public void setUserProducts(List<LifeUserProduct> userProducts) {
//		this.userProducts = userProducts;
//	}
//	public List<InfoPay> getUserInfoPays() {
//		return userInfoPays;
//	}
//	public void setUserInfoPays(List<InfoPay> userInfoPays) {
//		this.userInfoPays = userInfoPays;
//	}
//	public List<PayUserRel> getPayUserRels() {
//		return payUserRels;
//	}
//	public void setPayUserRels(List<PayUserRel> payUserRels) {
//		this.payUserRels = payUserRels;
//	}
//	
//}
