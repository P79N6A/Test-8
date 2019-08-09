package com.tydic.beijing.billing.tap3.core;

public class AsnTag {
	/**
	 * 标记值
	 */
	public int tag;

	/**
	 * 长度
	 */
	public int length;

	public void clear() {
		tag = 0;
		length = 0;
	}
}
