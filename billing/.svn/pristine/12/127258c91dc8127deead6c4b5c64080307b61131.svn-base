/**
 * 
 */
package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * @author sung
 *
 */
public class RemainResourceDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ResourceType;	//资源类型
	private long TotalResource ;	//资源总量
	private long RemainResource;	//剩余资源量
	private long UsedResource ;
	private String EffDate  ;  //生效时间
	private String  ExpDate ;  //失效时间
	
	public RemainResourceDto(){}
	
	public RemainResourceDto(String resourceType,long total,long remain,long used,String eff,String exp){
		this.ResourceType=resourceType;
		this.TotalResource=total;
		this.RemainResource=remain;
		this.UsedResource=used;
		this.EffDate=eff;
		this.ExpDate=exp;
	}
	public String getResourceType() {
		return ResourceType;
	}
	public void setResourceType(String resourceType) {
		ResourceType = resourceType;
	}
	public long getTotalResource() {
		return TotalResource;
	}
	public void setTotalResource(long totalResource) {
		TotalResource = totalResource;
	}
	public long getRemainResource() {
		return RemainResource;
	}
	public void setRemainResource(long remainResource) {
		RemainResource = remainResource;
	}
	public long getUsedResource() {
		return UsedResource;
	}
	public void setUsedResource(long usedResource) {
		UsedResource = usedResource;
	}
	
	
	public String getEffDate() {
		return EffDate;
	}

	public void setEffDate(String effDate) {
		EffDate = effDate;
	}

	public String getExpDate() {
		return ExpDate;
	}

	public void setExpDate(String expDate) {
		ExpDate = expDate;
	}

	@Override
	public String toString() {
		String ret="ResourceType["+ResourceType+"],TotalResource["+TotalResource+"],RemainResource["+RemainResource+"],UsedResource["+UsedResource+"],EffDate["+
				EffDate+"],ExpDate["+ExpDate+"]";
		return ret;
	}
	
}
