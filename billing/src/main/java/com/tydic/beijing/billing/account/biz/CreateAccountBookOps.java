package com.tydic.beijing.billing.account.biz;

import java.util.List;

import com.tydic.beijing.billing.account.datastore.DSBalanceType;
import com.tydic.beijing.billing.account.service.impl.CreateAccountBookImpl;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoPayBalanceAttr;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class CreateAccountBookOps {

	private DSBalanceType balanceTypes;
	private int flag4firstrun = 0;

	public InfoPayBalance process(InfoPayBalance ipb, String attrName, String attrValue)
			throws Exception {
		if (flag4firstrun == 0) {
			balanceTypes.load();
			flag4firstrun = 1;
		}
		checkBalanceTypeId(ipb);
		// 请求创建账本类型为默认账本时，校验默认账本是否已存在，如已存在即返回
		if (CreateAccountBookImpl.DEFAULT_BALANCE_TYPE_ID == ipb.getBalance_type_id()) {
			InfoPayBalance defaultIpb = queryDefaultAcctBook(ipb.getPay_id());
			if (defaultIpb != null) {
				return defaultIpb;
			}
		}
		ipb.setBalance_id(getBalanceId());
		S.get(InfoPayBalance.class).create(ipb);
		if ((attrName != null) && (attrValue != null)) {
			InfoPayBalanceAttr ipba = new InfoPayBalanceAttr();
			ipba.setBalance_id(ipb.getBalance_id());
			ipba.setBalance_type_id(ipb.getBalance_type_id());
			ipba.setPay_id(ipb.getPay_id());
			ipba.setAttr_name(attrName);
			ipba.setAttr_value(attrValue);
			S.get(InfoPayBalanceAttr.class).create(ipba);
		}
		return ipb;
	}

	private InfoPayBalance queryDefaultAcctBook(String payId) {
		List<InfoPayBalance> ipbs = S.get(InfoPayBalance.class).query(Condition.build("queryByPayId").filter("pay_id", payId));
		if ((ipbs != null) && (!ipbs.isEmpty())) {
			for (InfoPayBalance ipb : ipbs) {
				if (CreateAccountBookImpl.DEFAULT_BALANCE_TYPE_ID == ipb.getBalance_type_id()) {
					return ipb;
				}
			}
		}
		return null;
	}
	
	private void checkBalanceTypeId(InfoPayBalance ipb) throws BasicException {
		CodeBilBalanceType cbbt = balanceTypes.get(ipb.getBalance_type_id());
		if (cbbt == null) {
			throw new BasicException(ErrorCode.ERR_BALANCE_TYPE_ID_NOT_FOUND,
					"Create Account Book Check BalanceTypeId["
							+ ipb.getBalance_type_id() + "] NOT FOUND!");
		}
	}

	private long getBalanceId() {
		Sequences s = S.get(Sequences.class).queryFirst(
				Condition.build("queryBalanceId"));
		return s.getSeq();
	}

	public DSBalanceType getBalanceTypes() {
		return balanceTypes;
	}

	public void setBalanceTypes(DSBalanceType balanceTypes) {
		this.balanceTypes = balanceTypes;
	}
}
