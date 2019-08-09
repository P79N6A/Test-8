package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeList;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DSCodeList {
	private final static Logger LOGGER = Logger.getLogger(DSCodeList.class);
	private Map<String, String> store = null;

	public DSCodeList() {
	}

	public synchronized void load() throws Exception {
		if (store == null) {
			List<CodeList> aais = S.get(CodeList.class).query(
					Condition.empty());
			if (aais == null) {
				LOGGER.error("TABLE[CODE_LIST] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[CODE_LIST] Shouldn't Be Empty!");
			}
			store = new HashMap<String, String>();
			for (CodeList aai : aais) {
				if(aai.getType_code().equals("proto_type")){
					store.put(aai.getCode_id(), aai.getMacro_code());
				}
			}
		}
	}

	public synchronized String get(String code_id) throws BasicException {
		if(store == null || store.isEmpty()){
			LOGGER.debug("-----code_list table type_code = proto_type is null ----");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"code_list table type_code = proto_type is null");
		}
		LOGGER.debug("%%%%%%%%%%%%%%%%%%%%%%%%%%%%code_id:"+code_id);
		LOGGER.debug("------------store.get(code_id)"+store.get(code_id));
		return store.get(code_id);
	}

	public synchronized void refresh() throws BasicException {
		if (store == null) {
			LOGGER.info("Before Refresh CODE_LIST Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh CODE_LIST Size[" + store.size()
					+ "]");
		}
		Map<String, String> mirror = null;
		List<CodeList> aais = S.get(CodeList.class).query(
				Condition.empty());
		if (aais == null) {
			LOGGER.error("TABLE[CODE_LIST] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[CODE_LIST] Shouldn't Be Empty!");
		}
		mirror = new HashMap<String, String>();
		for (CodeList aai : aais) {
			if(aai.getType_code().equals("proto_type")){
				mirror.put(aai.getCode_id(), aai.getMacro_code());
			}
		}
		store = mirror;
		LOGGER.info("After Refresh CODE_LIST Size[" + store.size() + "]");
	}
}
