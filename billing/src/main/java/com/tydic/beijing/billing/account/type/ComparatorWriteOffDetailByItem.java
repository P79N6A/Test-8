package com.tydic.beijing.billing.account.type;

import java.util.Comparator;

public class ComparatorWriteOffDetailByItem implements
		Comparator<WriteOffDetail> {

	@Override
	public int compare(WriteOffDetail o1, WriteOffDetail o2) {
		String user_id1 = o1.getUser_Id();
		String user_id2 = o2.getUser_Id();
		String pay_id1 = o1.getPay_Id();
		String pay_id2 = o2.getPay_Id();
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
