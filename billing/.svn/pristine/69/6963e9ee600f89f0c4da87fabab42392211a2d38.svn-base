package com.tydic.beijing.billing.account.type;

import java.util.Comparator;

public class ComparatorWriteOffDetailByBalance implements Comparator<WriteOffDetail> {

	@Override
	public int compare(WriteOffDetail o1, WriteOffDetail o2) {
		if (o1.getBalance_id() - o2.getBalance_id() > 0) {
			return 1;
		} else if(o1.getBalance_id() - o2.getBalance_id() == 0) {
			return 0;
		} else {
			return -1;
		}
	}

}
