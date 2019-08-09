/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * java在执行javascript时，不允许直接放入map类型，所以在这简单封装一下<br/>
 * _SRC 代表ua_src_record<br/>
 * _MEDIAL 代表ua_medial_record<br/>
 * _SEARCH 工具函数类<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class MapWrapper {

	private HashMap<String, String> kv = null;

	public MapWrapper(HashMap<String, String> kv) {
		this.kv = kv;
	}

	public String g(String key) {
		String value = kv.get(key);// 防止取值时是null
		return value == null ? "" : value;
	}

	public String get(String key) {
		String value = kv.get(key);// 防止取值时是null
		return value == null ? "" : value;
	}

	public void p(String key, String value) {
		kv.put(key, value);
	}

	/**
	 * 
	 * init:初始化"". <br/>
	 */
	public void init() {
		for (Entry<String, String> e : kv.entrySet()) {
			kv.put(e.getKey(), "");
		}
	}

	/**
	 * 
	 * clear:清空. <br/>
	 *
	 */
	public void clear() {
		kv.clear();
	}

	public HashMap<String, String> getKv() {
		return kv;
	}

	public void setKv(HashMap<String, String> kv) {
		this.kv = kv;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(120);
		for (Entry<String, String> e : kv.entrySet()) {
			sb.append(e.getKey() + "=" + e.getValue());
			sb.append(',');
		}
		return sb.toString();
	}

	public String toString2() {
		StringBuffer sb = new StringBuffer(120);
		for (Entry<String, String> e : kv.entrySet()) {
			if (e.getValue() == null) {
				continue;
			}
			sb.append(e.getKey() + "=" + e.getValue());
			sb.append(',');
		}
		return sb.toString();
	}

}