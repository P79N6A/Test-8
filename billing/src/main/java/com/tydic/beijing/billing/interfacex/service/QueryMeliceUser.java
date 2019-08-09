package com.tydic.beijing.billing.interfacex.service;

import java.util.List;

import com.tydic.beijing.billing.dao.MaliceUser;

public interface QueryMeliceUser {
	public List<MaliceUser> doQuery(String partition_id);
}
