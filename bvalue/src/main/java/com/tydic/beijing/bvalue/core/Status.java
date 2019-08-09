package com.tydic.beijing.bvalue.core;

/**
 * 
 * SKU 状态信息<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 *
 */
public class Status {
	private int status;
	private String remark;

	public Status(int status, String remark) {
		super();
		this.status = status;
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Status [status=" + status + ", remark=" + remark + "]";
	}

}
