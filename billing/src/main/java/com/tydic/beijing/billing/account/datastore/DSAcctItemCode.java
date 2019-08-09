package com.tydic.beijing.billing.account.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeActAcctItem;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class DSAcctItemCode {
	private final static Logger LOGGER = Logger.getLogger(DSAcctItemCode.class);
	private Map<Integer, CodeActAcctItem> store = null;

	public DSAcctItemCode() {
	}

	public synchronized void load() throws Exception {
		if (store == null) {
			List<CodeActAcctItem> aais = S.get(CodeActAcctItem.class).query(
					Condition.empty());
			if (aais == null) {
				LOGGER.error("TABLE[CODE_ACT_ACCT_ITEM] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[CODE_ACT_ACCT_ITEM] Shouldn't Be Empty!");
			}
			store = new HashMap<Integer, CodeActAcctItem>();
			for (CodeActAcctItem aai : aais) {
				store.put(aai.getAcct_item_code(), aai);
			}
		}
	}

	public synchronized CodeActAcctItem get(int acctItemCode) {
		return store.get(acctItemCode);
	}

	public synchronized void refresh() throws BasicException {
		if (store == null) {
			LOGGER.info("Before Refresh CodeAcctItemCode Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh CodeAcctItemCode Size[" + store.size()
					+ "]");
		}
		Map<Integer, CodeActAcctItem> mirror = null;
		List<CodeActAcctItem> aais = S.get(CodeActAcctItem.class).query(
				Condition.empty());
		if (aais == null) {
			LOGGER.error("TABLE[CODE_ACT_ACCT_ITEM] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[CODE_ACT_ACCT_ITEM] Shouldn't Be Empty!");
		}
		mirror = new HashMap<Integer, CodeActAcctItem>();
		for (CodeActAcctItem aai : aais) {
			mirror.put(aai.getAcct_item_code(), aai);
		}
		store = mirror;
		LOGGER.info("After Refresh CodeAcctItemCode Size[" + store.size() + "]");
	}
}
