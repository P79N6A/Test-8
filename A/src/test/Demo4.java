package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Demo4 {

	public static void main(String[] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
		String product_eff="2019/06/11 15:00:45";
		String product_exp="2019/07/01 00:00:00";
		try {
			Date pff = dateFormat.parse(product_eff);
			Date act = dateFormat.parse(product_exp);
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal.setTime(pff);
			cal2.setTime(act);
			int month1 = cal2.get(Calendar.MONTH) - cal.get(Calendar.MONTH);
			int month2 = (cal2.get(Calendar.YEAR) - cal .get(Calendar.YEAR))*12;
			System.out.println(month1+"`````````"+month2);
			int month = cal.get(Calendar.DAY_OF_MONTH);
			int maxDate1 = cal.get(Calendar.DATE);
			cal.set(Calendar.DATE, 1);
			cal.roll(Calendar.DATE, -1);
			int maxDate2 = cal.get(Calendar.DATE);
			Date time = cal.getTime();
			String format = dateFormat.format(time);
			System.out.println("ʱ�䣺"+format);
			System.out.println("maxDate2:"+maxDate2);
			double scale=0;
			scale = (double) (maxDate2 - month + 1) / (double) maxDate2;
			System.out.println("month"+month);
			System.out.println(cal.get(Calendar.MONTH));
			if (pff.before(dateFormat.parse("2019/06/30 15:00:45"))) {
				System.out.println("true");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
