package com.tydic.beijing.billing.cyclerent.biz;

import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.datastore.DSAcctMonth;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.cyclerent.datastore.DSCodeTradeTypeCode;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CodeTradeTypeCode;
import com.tydic.beijing.billing.dto.ControlActTagResult;

public class ControlTradeActTagOps {
	private static Logger LOGGER = Logger
			.getLogger(ControlTradeActTagOps.class);
	private static final String USETAG = "1";
	private static final int STATE_OK = 0;
	private static final int STATE_FAIL = 1;
	private int flag4firstrun = 0;
	private static final DSAcctMonth acctMonths = new DSAcctMonth();
	private static final DSCodeTradeTypeCode codeTradeTypeCodes = new DSCodeTradeTypeCode();

	public ControlActTagResult doProcess(String tradeTypeCode) throws Exception {
		ControlActTagResult result = new ControlActTagResult();
		if (tradeTypeCode == null || tradeTypeCode.isEmpty()) {
			resetResult(result, STATE_FAIL, null, null);
			return result;
		}
		LOGGER.debug("--tradeTypeCode = " + tradeTypeCode);

		resetResult(result, STATE_OK, null, null);
		if (flag4firstrun == 0) {
			acctMonths.load();
			codeTradeTypeCodes.load();
			flag4firstrun = 1;
		}

		List<CodeAcctMonth> lcam = acctMonths.getByUseTags(USETAG);
		String actTag = "";
		if (lcam != null && !lcam.isEmpty()) {
			for (CodeAcctMonth cam : lcam) {
				actTag = cam.getAct_tag();
			}
		}
		if (actTag == null || actTag.isEmpty()) {
			resetResult(result, STATE_FAIL, null, null);
			return result;
		}
		CodeTradeTypeCode cttc = codeTradeTypeCodes
				.getByTradeTypeCode(tradeTypeCode);
		if (cttc != null && cttc.getUseable_act_tag() != null) {
			String[] args = cttc.getUseable_act_tag().split("|");
			if (args == null || args.length == 0) {
				LOGGER.debug("TradeTypeCode = " + tradeTypeCode
						+ " not exist CodeTradeTypeCode Table !! ");
				resetResult(result, STATE_FAIL, actTag, null);
				return result;
			}
			for (int i = 0; i < args.length; i++) {
				if (actTag.equals(args[i])) {
					resetResult(result, STATE_OK, actTag,
							cttc.getUseable_act_tag());
					return result;
				}
			}
			resetResult(result, STATE_FAIL, actTag, cttc.getUseable_act_tag());
			LOGGER.debug("---tradeTypeCode[" + tradeTypeCode + "],act_tag["
					+ actTag + "] is invalid ");
		} else {
			LOGGER.debug("TradeTypeCode = " + tradeTypeCode
					+ " not exist CodeTradeTypeCode Table !! ");
			resetResult(result, STATE_FAIL, actTag, null);
		}
		return result;
	}

	private void resetResult(ControlActTagResult result, int state,
			String use_tag, String useable_act_tag) {
		result.setResult(state);
		result.setUse_tag(use_tag);
		result.setUseable_act_tag(useable_act_tag);
		LOGGER.debug(result.toString());
	}

	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) throws BasicException {

		if (BasicType.DS_CODE_ACCT_MONTH.equals(datastoreName)) {
			acctMonths.refresh();
		} else if (BasicType.DS_CODE_TRADE_TYPE_CODE.equals(datastoreName)) {
			codeTradeTypeCodes.refresh();
		} else {
			LOGGER.warn("Unknown Datastore Name[" + datastoreName + "]!");
			return;
		}

	}

}
