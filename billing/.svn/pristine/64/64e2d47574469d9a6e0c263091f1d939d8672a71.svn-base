/**
 * 
 */
package com.tydic.beijing.billing.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sung
 *
 */
public class CurrentConsume implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Status="1";
	private long TotalFee;
	private List<FeeItemDto> FeeItemDtoList=new ArrayList<FeeItemDto>();
	private List<BalanceConsumeDto> BalanceConsumeDtoList  =new ArrayList<BalanceConsumeDto>();
	private String ErrorCode="";
	private String ErrorMessage="";
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		this.Status = status;
	}
	public long getTotalFee() {
		return TotalFee;
	}
	public void setTotalFee(long totalFee) {
		this.TotalFee = totalFee;
	}
	public List<FeeItemDto> getFeeItemDtoList() {
		return FeeItemDtoList;
	}
	public void setFeeItemDtoList(List<FeeItemDto> feeItemDtoList) {
		this.FeeItemDtoList = feeItemDtoList;
	}
	public List<BalanceConsumeDto> getBalanceConsumeDtoList() {
		return BalanceConsumeDtoList;
	}
	public void setBalanceConsumeDtoList(
			List<BalanceConsumeDto> balanceConsumeDtoList) {
		this.BalanceConsumeDtoList = balanceConsumeDtoList;
	}
	public String getErrorCode() {
		return ErrorCode;
	}
	public void setErrorCode(String errorCode) {
		this.ErrorCode = errorCode;
	}
	public String getErrorMessage() {
		return ErrorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.ErrorMessage = errorMessage;
	}
	
	
	@Override
	public String toString() {
		String ret="Status["+Status+"],TotalFee["+TotalFee+"],errorCode["+ErrorCode+"],errorMessage["+ErrorMessage+"],"
				+"FeeItemDtoList size["+FeeItemDtoList.size()+"],BalanceConsumeDtoList size["+BalanceConsumeDtoList.size()+"]";
		return ret;
	}
	
	
}
