package com.tydic.beijing.billing.account.datastore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeBilPayItemCode;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class DSPayItemCode {
	private final static Logger LOGGER = Logger.getLogger(DSPayItemCode.class);
	private List<CodeBilPayItemCode> store = null;

	public DSPayItemCode() {
	}

	public synchronized void load() throws Exception {
		if (store == null) {
			store = S.get(CodeBilPayItemCode.class).query(Condition.empty());
			if (store == null) {
				LOGGER.error("TABLE[CODE_BIL_PAY_ITEM_CODE] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[CODE_BIL_PAY_ITEM_CODE] Shouldn't Be Empty!");
			}
		}
	}

	public synchronized List<CodeBilPayItemCode> get(int payItemCode) {
		List<CodeBilPayItemCode> pics = new ArrayList<CodeBilPayItemCode>();
		for (CodeBilPayItemCode pic : store) {
			if (pic.getPayitem_code() == payItemCode) {
				pics.add(pic);
			}
		}
		return pics;
	}

	public synchronized void refresh() throws BasicException {
		if (store == null) {
			LOGGER.info("Before Refresh CodeBilPayItemCode Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh CodeBilPayItemCode Size[" + store.size()
					+ "]");
		}		
		List<CodeBilPayItemCode> mirror = null;
		mirror = S.get(CodeBilPayItemCode.class).query(Condition.empty());
		if (mirror == null) {
			LOGGER.error("TABLE[CODE_BIL_PAY_ITEM_CODE] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[CODE_BIL_PAY_ITEM_CODE] Shouldn't Be Empty!");
		}
		store = mirror;
		LOGGER.info("After Refresh CodeBilPayItemCode Size[" + store.size()
				+ "]");
	}
}
