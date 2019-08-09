package com.tydic.beijing.billing.account.biz;

import java.util.List;

import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoPayBalanceAsync;
import com.tydic.beijing.billing.dao.QRechargeCallback;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class RechargeAsync2SyncOps {
	public List<InfoPayBalanceAsync> scanInfoPayBalanceAsync() {
		return S.get(InfoPayBalanceAsync.class).query(
				Condition.build("get1By1"));
	}

	public InfoPayBalance getInfoPayBalanceByBalanceId(long balanceId) {
		return S.get(InfoPayBalance.class).get(balanceId);
	}

	public void updateInfoPayBalanceAsyncState(InfoPayBalanceAsync ipba,
			int asyncSate) {
		ipba.setAsync_state(asyncSate);
		S.get(InfoPayBalanceAsync.class).update(ipba);
	}

	public void doAsync2Sync(InfoPayBalance ipb, InfoPayBalanceAsync ipba,
			int asyncSate) {
		S.get(InfoPayBalance.class).batch(
				Condition.build("update4Recharge").filter("balance_id",
						ipb.getBalance_id()), ipb);
		this.updateInfoPayBalanceAsyncState(ipba, asyncSate);
	}

	public void updateQRechargeCallback(int oldState, int newState) {
		QRechargeCallback qrcb = new QRechargeCallback();
		qrcb.setState(newState);
		S.get(QRechargeCallback.class).batch(
				Condition.build("updateAsync2Sync").filter("state", oldState),
				qrcb);
	}
}
