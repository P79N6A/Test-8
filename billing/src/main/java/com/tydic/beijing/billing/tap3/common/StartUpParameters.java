package com.tydic.beijing.billing.tap3.common;

/**
 * 
 * 启动参数<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 *
 */
public class StartUpParameters {
	/**
	 * tap3处理线程数
	 */
	private int tap3HandlerThreads;

	public int getTap3HandlerThreads() {
		return tap3HandlerThreads;
	}

	public void setTap3HandlerThreads(int tap3HandlerThreads) {
		this.tap3HandlerThreads = tap3HandlerThreads;
	}

}
