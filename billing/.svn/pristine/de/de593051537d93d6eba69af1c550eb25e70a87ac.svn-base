/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.tap3.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.tap3.core.Node;
import com.tydic.beijing.billing.tap3.core.Record;

/**
 * configuration<br/>
 * 1.program configuration <br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public enum Configuration {
	/**
	 * Singleton
	 */
	INSTANCE;

	/**
	 * program configuration
	 */
	private Map<String, String> parameters = new HashMap<String, String>(36);

	private Map<Integer, Node> bciMap;
	private Map<Integer, Node> atiMap;
	private Map<Integer, Node> ntwMap;
	private Map<Integer, Node> moMap;
	private Map<Integer, Node> mtMap;
	private Map<Integer, Node> gprsMap;
	private Map<Integer, Node> aclMap;

	private List<Record> moOutLst;// 语音、 短信主叫
	private List<Record> mtOutLst;// 语音、 短信被叫
	private List<Record> gpOutLst;// 短信被叫

	/**
	 * 
	 * put:put value to Map.<br/>
	 * 
	 * @param key
	 * @param value
	 */
	public void put(String key, String value) {
		parameters.put(key, value);
	}

	/**
	 * 
	 * get:get value from Map.<br/>
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return parameters.get(key);
	}

	public Map<Integer, Node> getBciMap() {
		return bciMap;
	}

	public void setBciMap(Map<Integer, Node> bciMap) {
		this.bciMap = bciMap;
	}

	public Map<Integer, Node> getAtiMap() {
		return atiMap;
	}

	public void setAtiMap(Map<Integer, Node> atiMap) {
		this.atiMap = atiMap;
	}

	public Map<Integer, Node> getNtwMap() {
		return ntwMap;
	}

	public void setNtwMap(Map<Integer, Node> ntwMap) {
		this.ntwMap = ntwMap;
	}

	public Map<Integer, Node> getMoMap() {
		return moMap;
	}

	public void setMoMap(Map<Integer, Node> moMap) {
		this.moMap = moMap;
	}

	public Map<Integer, Node> getMtMap() {
		return mtMap;
	}

	public void setMtMap(Map<Integer, Node> mtMap) {
		this.mtMap = mtMap;
	}

	public Map<Integer, Node> getGprsMap() {
		return gprsMap;
	}

	public void setGprsMap(Map<Integer, Node> gprsMap) {
		this.gprsMap = gprsMap;
	}

	public Map<Integer, Node> getAclMap() {
		return aclMap;
	}

	public void setAclMap(Map<Integer, Node> aclMap) {
		this.aclMap = aclMap;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public List<Record> getMoOutLst() {
		return moOutLst;
	}

	public void setMoOutLst(List<Record> moOutLst) {
		this.moOutLst = moOutLst;
	}

	public List<Record> getMtOutLst() {
		return mtOutLst;
	}

	public void setMtOutLst(List<Record> mtOutLst) {
		this.mtOutLst = mtOutLst;
	}

	public List<Record> getGpOutLst() {
		return gpOutLst;
	}

	public void setGpOutLst(List<Record> gpOutLst) {
		this.gpOutLst = gpOutLst;
	}

}
