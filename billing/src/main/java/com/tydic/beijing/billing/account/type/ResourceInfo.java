package com.tydic.beijing.billing.account.type;

public class ResourceInfo {
	/*
	 * B091	资源类型编码	String	1000：语音免费计费量
	 * 							1001：语音付费计费量
	 * 							2000：短信免费计费量
	 * 							2001：短信付费计费量
	 * 							3000：数据免费计费量
	 * 							3001：数据付费计费量
	 */
	private long resource_id;//资源代码
	/*
	 * B092	计费量	Long	        语音业务单位：秒
	 * 							短信业务单位：条
	 * 							数据业务单位：KB
	 */
	private long resource_value;//资源值
	
	public long getResource_value() {
		return resource_value;
	}
	public void setResource_value(long resource_value) {
		this.resource_value = resource_value;
	}
	public long getResource_id() {
		return resource_id;
	}
	public void setResource_id(long resource_id) {
		this.resource_id = resource_id;
	}
}
