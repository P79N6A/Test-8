/**
 * 
 */
package com.tydic.beijing.billing.dto;

import java.io.Serializable;

/**
 * @author sung
 *
 */
public class BalanceConsumeDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long BalanceType ; // 1 通话时长  2 短信彩信  3 数据流量
	private long Consume;		//已消费量  分钟/条/KB
	
	public BalanceConsumeDto(){}
	
	public BalanceConsumeDto(long type,long consume){
		this.BalanceType=type;
		this.Consume=consume;
	}
	public long getBalanceType() {
		return BalanceType;
	}
	public void setBalanceType(long balanceType) {
		this.BalanceType = balanceType;
	}
	public long getConsume() {
		return Consume;
	}
	public void setConsume(long consume) {
		this.Consume = consume;
	}
	
	@Override
	public String toString() {
		String ret="BalanceType["+BalanceType+"],Consume["+Consume+"]";
		return ret;
	}
	
	
	
}
