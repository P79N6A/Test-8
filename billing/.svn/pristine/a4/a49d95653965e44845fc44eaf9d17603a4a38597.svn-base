package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 余额变动响应参数
 * 
 * @author Tian
 *
 */
public class BalanceChangeResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String status;
	private String resultCode;
	private String resultMessage;
	private List<BalanceChangeSnapshot> balanceChangeSnapshot;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public List<BalanceChangeSnapshot> getBalanceChangeSnapshot() {
		return balanceChangeSnapshot;
	}

	public void setBalanceChangeSnapshot(
			List<BalanceChangeSnapshot> balanceChangeSnapshot) {
		this.balanceChangeSnapshot = balanceChangeSnapshot;
	}

	@Override
	public String toString() {
		return "BalanceChangeResponse [status=" + status + ", resultCode="
				+ resultCode + ", resultMessage=" + resultMessage
				+ ", balanceChangeSnapshot=" + balanceChangeSnapshot + "]";
	}
}
