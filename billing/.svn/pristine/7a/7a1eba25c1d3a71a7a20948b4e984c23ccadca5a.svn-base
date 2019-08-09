package com.tydic.beijing.billing.account.type;

import java.util.Comparator;

import com.tydic.beijing.billing.dao.BilActBalanceAddUp;

public class ComparatorBilActBalanceAddUpByBalance implements
		Comparator<BilActBalanceAddUp> {

	@Override
	public int compare(BilActBalanceAddUp o1, BilActBalanceAddUp o2) {
		if (o1.getBalance_id() - o2.getBalance_id() > 0) {
			return 1;
		} else if(o1.getBalance_id() - o2.getBalance_id() == 0) {
			return 0;
		} else {
			return -1;
		}
	}

}
