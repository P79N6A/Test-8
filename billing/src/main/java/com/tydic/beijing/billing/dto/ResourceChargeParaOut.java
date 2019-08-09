package com.tydic.beijing.billing.dto;

import java.util.List;
import java.io.Serializable;
import javax.persistence.Id;

import com.tydic.beijing.billing.dto.ResourceChargeParaOutList;

public final class ResourceChargeParaOut implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String Status;
	private String ErrorMessage;
	List<ResourceChargeParaOutList> resourceChargeParaOutList;
	
	@Id
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getErrorMessage() {
		return ErrorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}
	public void addResourceChargeParaOutList(ResourceChargeParaOutList paraOutList){
		resourceChargeParaOutList.add(paraOutList);
	}
	
	public List<ResourceChargeParaOutList> getResourceChargeParaOutList(){
		return resourceChargeParaOutList;
	}

	public void setResourceChargeParaOutList(List<ResourceChargeParaOutList> list){
		this.resourceChargeParaOutList = list;
	}
	@Override
	public String toString() {
		return "ResourceChargeParaOut [Status=" + Status + ", ErrorMessage="
				+ ErrorMessage + ", resourceChargeParaOutList="
				+ resourceChargeParaOutList + "]";
	}
}
