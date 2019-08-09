package com.tydic.beijing.billing.interfacex.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.UserPayInfo;
import com.tydic.beijing.billing.dto.BillCycleDto;
import com.tydic.beijing.billing.dto.QuerySubsBillPeriodInfo;
import com.tydic.beijing.billing.dto.SubsBillPeriod;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.QuerySubsBillPeriod;

public class QuerySubsBillPeriodImpl implements QuerySubsBillPeriod {

	private static Logger log = Logger.getLogger(QuerySubsBillPeriodImpl.class);

	@Override
	public SubsBillPeriod query(QuerySubsBillPeriodInfo info) {
		String nbr = info.getMSISDN();
		String channle = info.getContactChannle();
		log.debug("服务[QuerySubsBillPeriod],号码[" + nbr + "],渠道[" + channle + "]");
		DbTool db = new DbTool();
		SubsBillPeriod bill = new SubsBillPeriod();

		List<UserPayInfo> userInfos = db.queryUserInfoByNbr(nbr);
		if (userInfos == null || userInfos.isEmpty()) {
			log.error("错误码[ZSMART-CC-00003]:业务号码不存在");
			bill.setStatus("0");
			bill.setErrorCode("ZSMART-CC-00003");
			bill.setErrorMessage("业务号码不存在");
			return bill;
		}

		Date activeDate = userInfos.get(0).getActive_date();
		if (activeDate == null) {
			bill.setStatus("0");
			bill.setErrorCode("ZSMART-CC-00139");
			bill.setErrorMessage("订户未激活或未查询到激活日期");
			return bill;
		}

		List<CodeAcctMonth> acctMonth = db.queryCurrentSubsBillPeriod();

		Date currentDate = acctMonth.get(0).getAct_eff_date();
		for (CodeAcctMonth iter : acctMonth) {
			Date dateTmp = iter.getAct_eff_date();
			if (dateTmp.before(currentDate)) {
				currentDate = dateTmp;
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String currentStr = sdf.format(currentDate);
		Date current = currentDate;
		try {
			current = sdf.parse(currentStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Calendar calnow = Calendar.getInstance();
		calnow.clear();
		calnow.setTime(current); // 当前时间点
		System.out.println("calnow:" + calnow);

		Calendar calActive = Calendar.getInstance();
		calActive.setTime(activeDate); // 激活日期
		System.out.println("calActiveDate:" + calActive);
		String startMonth = "";

		Calendar calMin = Calendar.getInstance();
		calMin.setTime(calnow.getTime());
		calMin.add(Calendar.MONTH, -11); // 最小时间 包括当前时间点的前12个月

		// calnow.add(Calendar.MONTH, -1);
		String nowyear = sdf.format(calnow.getTime()).substring(0, 6); // 含当前时间点
																		// 网厅去掉当前月

		String activeStr = sdf.format(calActive.getTime()); // 激活时间

		String start = sdf.format(calMin.getTime()); // 最小时间
		log.debug("active day :" + activeStr);

		if (calActive.before(calMin) || calActive.equals(calMin)) {
			startMonth = start;

		} else if (calActive.after(calMin)) {
			startMonth = activeStr;
		}

		String endMonth = nowyear;

		startMonth += "01";
		log.debug("startMonth :" + startMonth);
		int maxDay = calnow.getActualMaximum(Calendar.DAY_OF_MONTH);
		endMonth += maxDay;
		log.debug("endMonth:" + endMonth);
		List<CodeAcctMonth> acctMonths = db.querySubsBillPeriod(startMonth, endMonth);
		if (acctMonths == null || acctMonths.isEmpty()) {
			log.debug("service QuerySubsBillPeriod  no data found !");
			bill.setStatus("1");
			// bill.setErrorCode("ZSMART-CC-00032");
			bill.setErrorMessage("没有查找到记录");
			return bill;
		}
		List<BillCycleDto> dtos = new ArrayList<BillCycleDto>();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

		for (CodeAcctMonth month : acctMonths) {
			String effDate = sdf2.format(month.getAct_eff_date());
			String expDate = sdf2.format(month.getAct_exp_date());
			String partition = month.getPartition_no();
			if (partition == null || partition.isEmpty()) {
				partition = effDate.substring(5, 7);
			}
			long acct_month = Long.parseLong(effDate.substring(0, 4) + partition);
			Date date;
			try {
				date = sdf2.parse(expDate);
				Calendar cal = Calendar.getInstance();
				cal.clear();
				cal.setTime(date);
				cal.add(Calendar.DAY_OF_MONTH, 1);
				String realEndDate = sdf2.format(cal.getTime());
				BillCycleDto cycle = new BillCycleDto(acct_month, effDate, realEndDate);
				dtos.add(cycle);

			} catch (ParseException e) {

				e.printStackTrace();
			}

		}
		bill.setBillCycleDtoList(dtos);

		// LOG
		log.debug(">>>>服务[QuerySubsBillPeriod],号码[" + info.getMSISDN() + "],渠道["
				+ info.getContactChannle() + "] query return :" + bill);
		for (BillCycleDto dto : dtos) {
			log.debug(">>>>服务[QuerySubsBillPeriod],号码[" + info.getMSISDN() + "],渠道["
					+ info.getContactChannle() + "] query return :" + dto);
		}

		return bill;

	}

	public static void main(String args[]) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "subsBillPeriodQuery.xml" });
		QuerySubsBillPeriodImpl impl = new QuerySubsBillPeriodImpl();
		QuerySubsBillPeriodInfo info = new QuerySubsBillPeriodInfo("17091922182", "165");
		impl.query(info);
	}

}
