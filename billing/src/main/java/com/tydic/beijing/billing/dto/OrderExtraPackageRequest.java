package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * 加油包订购入参
 * 
 * @author Tian
 *
 */
public class OrderExtraPackageRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String Sn;
	private String MSISDN;
	private String ContactChannel;
	private String PackageId;

	public String getSn() {
		return Sn;
	}

	public void setSn(String sn) {
		Sn = sn;
	}

	public String getMSISDN() {
		return MSISDN;
	}

	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}

	public String getContactChannel() {
		return ContactChannel;
	}

	public void setContactChannel(String contactChannel) {
		ContactChannel = contactChannel;
	}

	public String getPackageId() {
		return PackageId;
	}

	public void setPackageId(String packageId) {
		PackageId = packageId;
	}

	@Override
	public String toString() {
		return "OrderExtraPackageInfo [Sn=" + Sn + ", MSISDN=" + MSISDN
				+ ", ContactChannel=" + ContactChannel + ", PackageId="
				+ PackageId + "]";
	}
}
