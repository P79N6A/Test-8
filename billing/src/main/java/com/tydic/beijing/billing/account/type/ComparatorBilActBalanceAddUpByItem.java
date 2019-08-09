package com.tydic.beijing.billing.account.type;

import java.util.Comparator;

import com.tydic.beijing.billing.dao.BilActBalanceAddUp;

public class ComparatorBilActBalanceAddUpByItem implements
		Comparator<BilActBalanceAddUp> {

	@Override
	public int compare(BilActBalanceAddUp o1, BilActBalanceAddUp o2) {
		String user_id1 = o1.getUser_id();
		String user_id2 = o2.getUser_id();
		String pay_id1 = o1.getPay_id();
		String pay_id2 = o2.getPay_id();
		int acct_month1 = o1.getAcct_month();
		int acct_month2 = o2.getAcct_month();
		int acct_item_code1 = o1.getAcct_item_code();
		int acct_item_code2 = o2.getAcct_item_code();
		
		if (user_id1.compareTo(user_id2) > 0) {
			return 1;
		} else if (user_id1.compareTo(user_id2) == 0
				&& pay_id1.compareTo(pay_id2) > 0) {
			return 1;
		} else if (user_id1.compareTo(user_id2) == 0
				&& pay_id1.compareTo(pay_id2) == 0 
				&& acct_month1 > acct_month2) {
			return 1;
		} else if (user_id1.compareTo(user_id2) == 0
				&& pay_id1.compareTo(pay_id2) == 0
				&& acct_month1 == acct_month2
				&& acct_item_code1 > acct_item_code2) {
			return 1;
		} else if (user_id1.compareTo(user_id2) == 0
				&& pay_id1.compareTo(pay_id2) == 0
				&& acct_month1 == acct_month2
				&& acct_item_code1 == acct_item_code2) {
			return 0;
		} else {
			return -1;
		}
	}

}
