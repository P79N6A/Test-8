/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.tap3.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期相关辅助类<br/>
 * PS:SimpleDateFormat("yyyyMMddHHmmss") 格式一定要用小写yyyy，大写YYYY有bug
 * 
 * 
 * PS：大量使用SimpleDateFormat，非线程安全
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
@Deprecated
public class DateUtil {
	/**
	 * 
	 * getSystemTime:获取一个14位的当前日期，类型字符串.<br/>
	 * 
	 * @return
	 */
	public static String getSystemTime() {
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 
	 * getCurrMonth:返回数值型当前月份，1-2位.<br/>
	 * 
	 * @return
	 */
	public static int getCurrMonthForTypeDecimal() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		return month;
	}

	/**
	 * 
	 * getCurrMonth:返回字符串当前月份，2位.<br/>
	 * 
	 * @return
	 */
	public static String getCurrMonthForTypeString() {
		Calendar cal = Calendar.getInstance();
		String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		return month;
	}

	/**
	 * 
	 * dateToString:日期类型转成14位日子字符串.<br/>
	 * 
	 * @param calendar
	 * @return
	 */
	public static String dateToString(final Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 
	 * changeToInternalFormat:yyyy-MM-dd HH-mm-ss到yyyyMMddHHmmss.<br/>
	 * 
	 * @param stringDate
	 * @return
	 * @throws ParseException
	 */
	public static String changeToInternalFormat(final String stringDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Calendar cal = Calendar.getInstance();

		cal.setTime(sdf.parse(stringDate));
		return dateToString(cal);
	}

	/**
	 * 
	 * getNextMonthOfSystemTime:下月今天，如果是20150131104100，则结果是201502281104100.
	 * 
	 * @return
	 */
	public static String getNextMonthOfGiverTime(final Calendar calendar) {
		calendar.add(Calendar.MONTH, 1);
		return dateToString(calendar);
	}

	/**
	 * 
	 * getBValueDate:<br/>
	 * 
	 * 返回B值的失效时间.<br/>
	 * 上半年生效，下半年12月31日失效（最后一天）.<br/>
	 * 下半年生肖，第二年6月30日失效.<br/>
	 * 
	 * @return
	 */
	public static String getBValueExpDate(final Calendar calendar) {
		int currMonth = calendar.get(Calendar.MONTH);
		String expDate = null;
		if (currMonth < 6) {
			calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			expDate = dateToString(calendar);
		} else {
			calendar.add(Calendar.YEAR, 1);
			calendar.set(Calendar.MONTH, 5);// 6月结束
			calendar.set(Calendar.DAY_OF_MONTH, 30);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			expDate = dateToString(calendar);
		}
		return expDate;
	}

	public static String getBValueEffDate(final Calendar calendar) {
		int currMonth = calendar.get(Calendar.MONTH);
		String expDate = null;
		if (currMonth < 6) {
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			expDate = dateToString(calendar);
		} else {
			calendar.set(Calendar.MONTH, 6);// 7月开始
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			expDate = dateToString(calendar);
		}
		return expDate;
	}

	/**
	 * 
	 * getLastSecondOfCurrMonth:
	 * 返回当月最后一天，格式为长度14位的日期（例如：20150131595959），类型字符串.<br/>
	 * 
	 * @return
	 */
	public static String getEndOfCurrMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		return sdf.format(cal.getTime());
	}

	/**
	 * 
	 * getFirstSecondOfCurrMonth:返回当前时间所属月份的第一天，例:20150101000000.<br/>
	 * 
	 * @return
	 */
	public static String getBeginningOfCurrMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);

		return sdf.format(cal.getTime());
	}

	/**
	 * 
	 * getBeginningOfGivenMonth:返回给定时间所属当月的月初.<br/>
	 * 
	 * @param currDate
	 * @return
	 * @throws ParseException
	 */
	public static String getBeginningOfGivenMonth(final String givenDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(givenDate));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		return sdf.format(cal.getTime());
	}

	public static String getEndOfGivenMonth(final String givenDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(givenDate));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return sdf.format(cal.getTime());
	}

	/**
	 * 
	 * getCalendarOfGivenDate:给定字符串时间返回Calendar.<br/>
	 * 
	 * @param givenDate
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getCalendarOfGivenDate(final String givenDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(givenDate));
		return cal;
	}

	/**
	 * 
	 * getMonth:返回2个日期相差几个月.<br/>
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static int getEndReduceStart(String startDate, String endDate) throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		int monthday;
		Date startDate1 = f.parse(startDate);
		Date endDate1 = f.parse(endDate);

		Calendar starCal = Calendar.getInstance();
		starCal.setTime(startDate1);

		// 开始
		int sYear = starCal.get(Calendar.YEAR);
		int sMonth = starCal.get(Calendar.MONTH);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate1);
		// 结束
		int eYear = endCal.get(Calendar.YEAR);
		int eMonth = endCal.get(Calendar.MONTH);
		monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));

		return monthday;

	}

	/**
	 * 
	 * getYear:返回年.<br/>
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getYear(final String date) throws ParseException {
		return getCalendarOfGivenDate(date).get(Calendar.YEAR);
	}

	/**
	 * 
	 * getMonthOfGivenDate:返回给定时间的月份.<br/>
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthOfGivenDate(final String date) throws ParseException {
		return getCalendarOfGivenDate(date).get(Calendar.MONTH) + 1;
	}

	/**
	 * 
	 * checkSameMonth:检查是否为当月.<br/>
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static boolean checkSameMonth(final String date) throws ParseException {

		int in_day = getCalendarOfGivenDate(date).get(Calendar.MONTH);
		int now_day = Calendar.getInstance().get(Calendar.MONTH);

		int in_year = getCalendarOfGivenDate(date).get(Calendar.YEAR);
		int now_year = Calendar.getInstance().get(Calendar.YEAR);
		return in_day == now_day && in_year == now_year;
	}

	/**
	 * 
	 * checkSameMonthAAndB:检查2个时间是否为同一个月.<br/>
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static boolean checkSameMonthAAndB(final String date1, final String date2)
			throws ParseException {
		Calendar cal1 = getCalendarOfGivenDate(date1);
		Calendar cal2 = getCalendarOfGivenDate(date2);
		int in_day = cal1.get(Calendar.MONTH);
		int now_day = cal2.get(Calendar.MONTH);

		int in_year = cal1.get(Calendar.YEAR);
		int now_year = cal2.get(Calendar.YEAR);
		return in_day == now_day && in_year == now_year;
	}

	/**
	 * 
	 * getBeginningOfNextMonth:返回下月第一天，格式:YYYY0101000000.<br/>
	 * 
	 * @return
	 */
	public static String getBeginningOfNextMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);// 月份加1
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		return dateToString(cal);
	}
}
