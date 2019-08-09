package com.tydic.beijing.billing.credit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/**
	 * 
	 * calculate: calculate formula.<br/>
	 * 
	 * @param history
	 * @param balance
	 * @param credit
	 * @param threshold
	 * @return result
	 * 
	 */
	protected long calculate(final long credit, final long history, final long realtime) {

		long ret = credit - history - realtime;
		return ret;
	}

	/**
	 * 
	 * getSystemTime:get system time .<br/>
	 * 
	 * @return
	 */
	public String getSystemTime() {
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	protected void exitApp(final int status) {
		System.exit(status);
	}

	protected void exitApp() {
		exitApp(-1);
	}
}
