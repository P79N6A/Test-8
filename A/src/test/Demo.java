package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Demo {
    
    public static void main(String[] args) {
    	List<String> dateList = new ArrayList<String>();
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		int lastday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int minday = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, lastday);
		String lastDayOfMonth = sdfdate.format(cal.getTime());
		cal.set(Calendar.DAY_OF_MONTH, minday);
		String minDayOfMonth = sdfdate.format(cal.getTime());
		String yearmonth = minDayOfMonth.substring(0, 6);
		String month_lastday = lastDayOfMonth + "235959";// 本月最后一天
		String month_minday = minDayOfMonth + "000000";
		dateList.add(month_lastday);
		dateList.add(month_minday);
		dateList.add(yearmonth);
		System.out.println(month_lastday);
		System.out.println(yearmonth);
    }
}