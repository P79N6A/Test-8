package com.tydic.beijing.billing.branch;

import com.tydic.beijing.billing.branch.dao.CreditSequenceUtils;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * db access tools<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 *
 */
public class DBKit {
	private DBKit() {

	}

	
	private static final String SEQUENCE_NAME = "sequence_name";

	public static long getFileSn(final String sequenceName) throws Exception {
		// Log.debug("Step in getFileSn(sequenceName=" + sequenceName + ")");
		CreditSequenceUtils s = S.get(CreditSequenceUtils.class).queryFirst(
				Condition.empty().filter(SEQUENCE_NAME, sequenceName));

		return s.getDuckduckgo();
	}


}
