package com.tydic.beijing.billing.interfacex.biz;

import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.MaliceUser;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.DataService;
import com.tydic.uda.service.S;

public class QueryMeliceUserOps {
	private Logger LOGGER=Logger.getLogger(QueryMeliceUserOps.class);
	public List<MaliceUser> getMaliceUser(String partition_id){
		LOGGER.info(partition_id);
			// TODO Auto-generated method stub
//			return  S.get(MaliceUser.class).query(Condition.build("getUserDeviceNumberForMalice")
//					.filter("partition_id",partition_id));
		LOGGER.info("---------------statr--------------------");
		DataService<MaliceUser> dataService = S.get(MaliceUser.class);
		LOGGER.info(dataService.toString());
		Condition con = Condition.build("getUserDeviceNumberForMalice").filter("partition_id", partition_id);
		LOGGER.info(con.toString());
		List<MaliceUser> query = dataService.query(con);
		LOGGER.info(query.toArray());
		return query;
	}
}
