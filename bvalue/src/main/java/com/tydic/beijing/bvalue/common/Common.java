/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * TODO(用几句话描述这个类型)<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class Common {
	private static final String CNUMBER_PATTERN = "^[0-9]*$";

	/**
	 * 
	 * md5:返回16进制的md5值.<br/>
	 * 
	 * @param m
	 *            非null的String类型字符串
	 * @return
	 */
	public static String md5(String m) {

		return DigestUtils.md5Hex(m.getBytes());
	}

	/**
	 * 
	 * getSystemTime:获取一个14位的当前日期.<br/>
	 * 
	 * @return
	 */
	public static String getSystemTime() {
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 
	 * getUUID:实现为数据库获取一个唯一的主键id的代码 .<br/>
	 * 
	 * @return
	 */
	public static String getUUID() {
		String uu = UUID.randomUUID().toString();
		return uu.substring(0, 8) + uu.substring(9, 13) + uu.substring(14, 18)
				+ uu.substring(19, 23) + uu.substring(24);
	}

	public static boolean isNumber(final String number) {
		Pattern p = Pattern.compile(CNUMBER_PATTERN);
		Matcher m = p.matcher(number);
		return m.find();
	}

	public static boolean isMobileNumber(final String number) {
		if (!isNumber(number)) {
			return false;
		}
		if (number.length() != 11) {
			return false;
		}
		return true;
	}


}
