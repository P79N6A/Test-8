package com.tydic.beijing.billing.account.service;

import java.util.Date;

import com.tydic.beijing.billing.dao.InfoPayBalance;

/**
 * 创建装本
 * 
 * @author Tian
 *
 */
public interface CreateAccountBook {
	// 供开户使用
	public int create(String localNet, String payId, Date effDate);

	// 供充值使用
	public int create(String localNet, String payId, int balanceTypeId,
			Date effDate, Date expDate);

	// 供充值使用（返回InfoPayBalance，返回null代表失败)
	public InfoPayBalance createNew(String localNet, String payId, int balanceTypeId,
			Date effDate, Date expDate);
	
	// 供充值使用（返回InfoPayBalance，返回null代表失败)
	public InfoPayBalance createNew(String localNet, String payId, int balanceTypeId,
			Date effDate, Date expDate, String attrName, String attrValue);
}
