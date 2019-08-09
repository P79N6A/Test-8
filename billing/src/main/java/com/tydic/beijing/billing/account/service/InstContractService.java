package com.tydic.beijing.billing.account.service;

import java.util.List;

import com.tydic.beijing.billing.dao.QPresentUserInfo;

public interface InstContractService {
	//public void InstPresentUserInfo() throws Exception;
	public List<QPresentUserInfo> InstPresentUserInfo(List<QPresentUserInfo> ListPresentUser);
}
