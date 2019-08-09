package com.tydic.beijing.billing.account.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class DSAcctMonth {
	private final static Logger LOGGER = Logger.getLogger(DSAcctMonth.class);
	private Map<Integer, CodeAcctMonth> mstore = null;
	private List<CodeAcctMonth> lstore = null;

	public DSAcctMonth() {
	}

	public synchronized void load() throws Exception {
		if (lstore == null) {
			lstore = S.get(CodeAcctMonth.class).query(Condition.build("getAll"));
			if ((lstore == null) || (lstore.isEmpty())) {
				LOGGER.error("TABLE[CODE_ACCT_MONTH] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[CODE_ACCT_MONTH] Shouldn't Be Empty!");
			}
			mstore = new HashMap<Integer, CodeAcctMonth>();
			for (CodeAcctMonth cam : lstore) {
				mstore.put(cam.getAcct_month(), cam);
			}
		}
	}

	public synchronized CodeAcctMonth getByAcctMonth(int acctMonth) {
		return mstore.get(acctMonth);
	}

	public synchronized List<CodeAcctMonth> getByUseTags(String useTag) {
		List<CodeAcctMonth> cams = new ArrayList<CodeAcctMonth>();
		if (lstore != null) {
			for (CodeAcctMonth cam : lstore) {
				if (useTag.equals(cam.getUse_tag())) {
					cams.add(cam);
				}
			}
		}
		return cams;
	}

	public synchronized void refresh() throws BasicException {
		if (lstore == null) {
			LOGGER.info("Before Refresh CodeAcctMonth Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh CodeAcctMonth Size[" + lstore.size() + "]");
		}
		Map<Integer, CodeAcctMonth> mmirror = null;
		List<CodeAcctMonth> lmirror = null;
		lmirror = S.get(CodeAcctMonth.class).query(Condition.build("getAll"));
		if ((lmirror == null) || (lmirror.isEmpty())) {
			LOGGER.error("TABLE[CODE_ACCT_MONTH] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[CODE_ACCT_MONTH] Shouldn't Be Empty!");
		}
		mmirror = new HashMap<Integer, CodeAcctMonth>();
		for (CodeAcctMonth cam : lmirror) {
			mmirror.put(cam.getAcct_month(), cam);
		}
		lstore = lmirror;
		mstore = mmirror;
		LOGGER.info("After Refresh CodeAcctMonth Size[" + lstore.size() + "]");
	}
}
