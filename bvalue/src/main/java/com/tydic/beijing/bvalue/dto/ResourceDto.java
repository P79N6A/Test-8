package com.tydic.beijing.bvalue.dto;


public class ResourceDto {

	/**
	 * 资源类型
	 * 0:金钱
	 * 1:语音
	 * 2:数据
	 * 3:短信
	 */
	private String resourceType;
	/**
	 * 资源量
	 * 单位：
	 * 金钱为分
	 * 语音为分钟
	 * 数据为兆
	 * 短信为条
	 */
	private String totalResource;
	/**
	 * 面额
	 * 单位：
	 * 张
	 */
	private String denomination;

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public ResourceDto(){}
	
	public ResourceDto(String type,String resource,String denomination){
		this.resourceType=type;
		this.totalResource=resource;
		this.denomination=denomination;
	}
	
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getTotalResource() {
		return totalResource;
	}

	public void setTotalResource(String totalResource) {
		this.totalResource = totalResource;
	}

	@Override
	public String toString() {
		return "ResourceDto [resourceType=" + resourceType + ", totalResource=" + totalResource
				+", denomination="+denomination
				+ "]";
	}
    
}
