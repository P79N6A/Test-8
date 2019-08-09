package com.tydic.beijing.billing.cyclerent.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeTradeTypeCode;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/*
 * yuandao
 */
public class DSCodeTradeTypeCode {
	private static final Logger LOGGER = Logger.getLogger(DSCodeTradeTypeCode.class);
	private List<CodeTradeTypeCode> lstore = null;
	private Map<String,CodeTradeTypeCode> mstore = null;
	
	public DSCodeTradeTypeCode(){
		
	}
	
	public synchronized void load() throws Exception {
		if(lstore == null){
			lstore = S.get(CodeTradeTypeCode.class).query(Condition.build("getAll"));
			if ((lstore == null) || (lstore.isEmpty())) {
				LOGGER.error("TABLE[CODE_TRADE_TYPE_CODE] Shouldn't Be Empty!");
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
						"TABLE[CODE_TRADE_TYPE_CODE] Shouldn't Be Empty!");
			}
			mstore = new HashMap<String,CodeTradeTypeCode>();
			for(CodeTradeTypeCode cttc : lstore){
				mstore.put(cttc.getTrade_type_code(), cttc);
			}
		}
	}
	
	public synchronized CodeTradeTypeCode getByTradeTypeCode(String tradeTypeCode) {
		return mstore.get(tradeTypeCode);
	}
	
	public synchronized void refresh() throws BasicException {
		if (lstore == null) {
			LOGGER.info("Before Refresh CodeAcctMonth Size[0][Uninitialized YET!]");
		} else {
			LOGGER.info("Before Refresh CodeAcctMonth Size[" + lstore.size() + "]");
		}
		List<CodeTradeTypeCode> lcttc = null;
		Map<String,CodeTradeTypeCode> mcttc = new HashMap<String,CodeTradeTypeCode>();
		lcttc = S.get(CodeTradeTypeCode.class).query(Condition.build("getAll"));
		if ((lcttc == null) || (lcttc.isEmpty())) {
			LOGGER.error("TABLE[CODE_TRADE_TYPE_CODE] Shouldn't Be Empty!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[CODE_TRADE_TYPE_CODE] Shouldn't Be Empty!");
		}
		for(CodeTradeTypeCode cttc : lcttc){
			mcttc.put(cttc.getTrade_type_code(), cttc);
		}
		lstore = lcttc;
		mstore = mcttc;
		LOGGER.info("After Refresh CodeTradeTypeCode Size[" + lstore.size() + "]");
	}

}
