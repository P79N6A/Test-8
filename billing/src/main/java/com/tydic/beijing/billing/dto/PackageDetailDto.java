package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * B值兑换资源使用
 * 
 * @author Tian
 *
 */
public class PackageDetailDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private long PackageType;
	private long Quantity;
	private String EffDate; // 格式:YYYYMMDD
	private String ExpDate; // 格式:YYYYMMDD
	private String Description;

	public long getPackageType() {
		return PackageType;
	}

	public void setPackageType(long packageType) {
		PackageType = packageType;
	}

	public long getQuantity() {
		return Quantity;
	}

	public void setQuantity(long quantity) {
		Quantity = quantity;
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

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	@Override
	public String toString() {
		return "PackageDetailDto [PackageType=" + PackageType + ", Quantity="
				+ Quantity + ", EffDate=" + EffDate + ", ExpDate=" + ExpDate
				+ ", Description=" + Description + "]";
	}

}
