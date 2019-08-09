package com.tydic.beijing.billing.interfacex.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.MaliceUser;
import com.tydic.beijing.billing.interfacex.biz.QueryMeliceUserOps;
import com.tydic.beijing.billing.interfacex.service.QueryMeliceUser;

public class QueryMeliceUserImpl implements QueryMeliceUser{
	private QueryMeliceUserOps ops;
	private Logger LOGGER=Logger.getLogger(QueryMeliceUserOps.class);
	public QueryMeliceUserOps getOps() {
		return ops;
	}
	public void setOps(QueryMeliceUserOps ops) {
		this.ops = ops;
	}
	@Override
	public List<MaliceUser> doQuery(String partition_id) {
		// TODO Auto-generated method stub
		List<MaliceUser> malice=new ArrayList<MaliceUser>();
		malice=ops.getMaliceUser(partition_id);
		LOGGER.debug("partition_id:"+partition_id);
		return malice;
	}
	
}
