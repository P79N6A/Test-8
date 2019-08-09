package com.tydic.beijing.billing.branch;

import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;
public class batchRe2AtOps {
	private static Logger LOGGER = Logger.getLogger(batchRe2AtOps.class);


	public List<InfoUser> selectInfoUser(int mod_id) throws Exception{
		return S.get(InfoUser.class).query(Condition.build("").filter("mod_id", mod_id));
	}

	public List<LifeUserProduct> selectLifeUserProduct(String user_id) throws Exception{
		return S.get(LifeUserProduct.class).query(Condition.build("").filter("user_id", user_id));
	}


}
