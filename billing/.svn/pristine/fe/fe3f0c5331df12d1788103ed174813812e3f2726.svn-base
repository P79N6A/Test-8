package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Id;
import com.tydic.beijing.billing.dto.ResourceChargeParaInList;

public final class ResourceChargeParaIn implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String PayId;
	private String OperateType;
	private String SN;
	List<ResourceChargeParaInList> resourceChargeParaInList;
	
	@Id
	public String getPayId() {
		return PayId;
	}
	public void setPayId(String payId) {
		PayId = payId;
	}
	public String getOperateType() {
		return OperateType;
	}
	public void setOperateType(String operateType) {
		OperateType = operateType;
	}
	public String getSN() {
		return SN;
	}
	public void setSN(String sN) {
		SN = sN;
	}
	public List<ResourceChargeParaInList> getResourceChargeParaInList(){
		return resourceChargeParaInList;
	}
	public void setResourceChargeParaInList(List<ResourceChargeParaInList> ParaInList){
		resourceChargeParaInList = ParaInList;
	}
	@Override
	public String toString() {
		return "ResourceChargeParaIn [PayId=" + PayId + ", OperateType="
				+ OperateType + ", SN=" + SN + ", resourceChargeParaInList="
				+ resourceChargeParaInList + "]";
	}

}
