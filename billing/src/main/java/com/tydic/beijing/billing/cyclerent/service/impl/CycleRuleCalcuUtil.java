package com.tydic.beijing.billing.cyclerent.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.LifeProductResourceExtRel;
import com.tydic.beijing.billing.dto.ResourceChargeParaInList;

public class CycleRuleCalcuUtil {
	private static final Logger LOGGER = Logger.getLogger(CycleRuleCalcuUtil.class);
	public static final String DATEFORMAT_TIMESTAMP = "yyyyMMddHHmmssSSS";
	public static final String DATEFORMAT_DATETIME = "yyyyMMddHHmmss";
	public static final String DATEFORMAT_DATETIME_READABLE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEFORMATE_YEARMONTH = "yyyyMM";
	public static final int YEAR_RETURN = 0;

	public static final int MONTH_RETURN = 1;

	public static final int DAY_RETURN = 2;

	public static final int HOUR_RETURN = 3;

	public static final int MINUTE_RETURN = 4;

	public static final int SECOND_RETURN = 5;

	public static final String YYYY = "yyyy";

	public static final String YYYYMM = "yyyy-MM";

	public static final String YYYYMMDD = "yyyy-MM-dd";

	public static final String YYYYMMDDHH = "yyyy-MM-dd HH";

	public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";

	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	private static final int CYCLE_TYPE_DAY = 1;
	private static final int CYCLE_TYPE_MONTH = 2;
	private static final String EFF_FLAG_NOW = "0";
	private static final String EFF_FLAG_MORROW = "1";
	private static final String EFF_FLAG_CIYT = "2";
	private static final int ONE_TEM_FIVE = 15;
	public CycleRuleCalcuUtil(){
		
	}
	/**
	 * 提供给rating使用
	 * @param eff_flag 生效方式:0-立即，1-次日，2-次月
	 * @param cycle_type 周期类型:1-日，2-自然月
	 * @param cycle_value 周期长度
	 * @param rcpCommon 保存计算出的账本生失效时间
	 * @param act_eff_date 账期生效时间
	 * @param product_eff_date 套餐生效时间
	 * @throws ParseException
	 */
	public void setEffAndExpDate(String eff_flag,long cycle_type,int cycle_value,
			ResourceChargeParaInList rcpCommon, Date act_eff_date,Date product_eff_date)
			throws ParseException {
		Date start_date = act_eff_date;
		Date end_date = product_eff_date;
		if(act_eff_date.after(product_eff_date)){
			start_date = product_eff_date;
			end_date = act_eff_date;
		}
		Calendar start_cal = Calendar.getInstance(); // 获取时间参数
		Calendar end_cal = Calendar.getInstance(); // 获取时间参数
		start_cal.setTime(start_date);
		end_cal.setTime(end_date);
		String eff_date = setEffDate(start_cal, eff_flag);
		String exp_date = setExpDate(end_cal,  cycle_type,cycle_value);

		rcpCommon.setEffDate(eff_date);
		rcpCommon.setExpDate(exp_date);

		LOGGER.debug("--eff_date = " + eff_date + ",exp_date = " + exp_date);
	}
	/**
	 * 月租服务，月租批量，资源到账服务，批量资源到账使用
	 * @param eff_flag 生效方式:0-立即，1-次日，2-次月
	 * @param cycle_type 周期类型:1-日，2-自然月
	 * @param cycle_value 周期长度
	 * @param start_date 计算账本生效时间设置的起始时间
	 * @return 账本生失效时间
	 * @throws ParseException
	 */
	public ResourceChargeParaInList getEffAndExpDate(String eff_flag,long cycle_type,int cycle_value,Date start_date) throws ParseException {
		ResourceChargeParaInList rcpCommon = new ResourceChargeParaInList();
		Calendar cal = Calendar.getInstance(); // 获取时间参数
		cal.setTime(start_date);
		String eff_date = setEffDate(cal, eff_flag);
		String exp_date = setExpDate(cal, cycle_type,cycle_value);
		rcpCommon.setEffDate(eff_date);
		rcpCommon.setExpDate(exp_date);
		LOGGER.debug("--eff_date = " + eff_date + ",exp_date = " + exp_date);
		return rcpCommon;
	}
	/**
	 * 
	 * @param cal 起始时间
	 * @param cycle_type 周期类型:1-日，2-自然月
	 * @param cycle_value 周期长度
	 * @return 计算出的账本失效时间
	 */
	private String setExpDate(Calendar cal, long cycle_type,int cycle_value) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String exp_date = "";
		if (cycle_type == CYCLE_TYPE_DAY) {
			cal.add(Calendar.DATE, cycle_value - 1);
		} else if (cycle_type == CYCLE_TYPE_MONTH) {
			cal.set(Calendar.DATE, 1);
			cal.add(Calendar.MONTH, cycle_value);
			cal.add(Calendar.DATE, -1);
		}
		exp_date = df.format(cal.getTime());
		exp_date += "235959";
		return exp_date;
	}
	/**
	 * 
	 * @param cal 起始时间
	 * @param eff_flag 生效方式:0-立即，1-次日，2-次月
	 * @return 计算出的账本生效时间
	 */
	private String setEffDate(Calendar cal, String eff_flag) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String eff_date = "";
		if (eff_flag.equals(EFF_FLAG_NOW)) {// 立即生效

		} else if (eff_flag.equals(EFF_FLAG_MORROW)) {// 次日生效
			cal.add(Calendar.DATE, 1);
		} else if (eff_flag.equals(EFF_FLAG_CIYT)) {// 次月生效
			LOGGER.debug("---次月-------");
			cal.set(Calendar.DATE, 1);// 设为当前月的1号
			cal.add(Calendar.MONTH, 1);
		}
		eff_date = df.format(cal.getTime());
		eff_date += "000000";
		return eff_date;
	}
	/**
	 * 
	 * @param act_eff_date
	 * @param product_eff_date
	 * @return 获取最新时间
	 */
	public Date getNewDate(Date act_eff_date, Date product_eff_date) {
		if(product_eff_date.after(act_eff_date)){
			return product_eff_date;
		}
		return act_eff_date;
	}
	/**
	 * 
	 * @param offset_mode  '1:全价全量
							2:半价半量(15号之前包含15号生效全价全量，15号之后生效半价半量)
							3:按百分比收取租费并到账资源，剩余天数/当月天数（向上取整）
							4:偏移期不到账资源'
	 * @param product_eff_date 套餐生效时间
	 * @param act_eff_date 账期生效时间
	 * @return 比例
	 */
	public double getScale(int offset_mode, Date product_eff_date,Date act_eff_date) {
		double scale = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(product_eff_date);
		int month = cal.get(Calendar.DAY_OF_MONTH);
		if (product_eff_date.before(act_eff_date) || product_eff_date.equals(act_eff_date)) {
			LOGGER.debug("----return 1------");
			return 1;
		}
		switch (offset_mode) {
		case 1:
			scale = 1;
			break;
		case 2:
			if (month <= ONE_TEM_FIVE) {
				scale = 1;
			} else {
				scale = 0.5;
			}
			break;
		case 3:
			cal.set(Calendar.DATE, 1);
			cal.roll(Calendar.DATE, -1);
			int maxDate = cal.get(Calendar.DATE);
			scale = (double) (maxDate - month + 1) / (double) maxDate;
			LOGGER.debug("-----day[" + month + "],maxDate[" + maxDate
					+ "]-----------");
			break;
		case 4:
			scale = 0;
			break;
		default:
			LOGGER.debug(" OFFSET_MODE VALEUE IN [1,2,3,4],now offset_mode = "
					+ offset_mode);
			break;
		}
		LOGGER.debug("---OFFSET_MODE[" + offset_mode
				+ "] count result :" + scale);
		return scale;
	}
	/**
	 * 查询该套餐在指定时间间隔中是否到账过特权资源
	 * @param lprer :历史到账记录
	 * @param user_product_id 用户产品订购实例
	 * @param balance_type_id 账本类型id
	 * @param acct_month 账期
	 * @param process_cycle 指定时间间隔
	 * @return
	 */
	public LifeProductResourceExtRel getExeLogByPk(
			List<LifeProductResourceExtRel> lprer, String user_product_id,
			int balance_type_id, int acct_month, long process_cycle) {
		if(lprer != null && !lprer.isEmpty()){
			for(Iterator<LifeProductResourceExtRel> iter = lprer.iterator(); iter.hasNext();){
				LifeProductResourceExtRel lifeProductResourceExtRel = iter.next();
				if(lifeProductResourceExtRel.getUser_product_id().equals(user_product_id) && lifeProductResourceExtRel.getBalance_type_id() == balance_type_id){
					if((acct_month - lifeProductResourceExtRel.getAcct_month()) <= process_cycle){
						return lifeProductResourceExtRel;
					}
				}
			}
		}
		return null;
	}
	/**
	 * 获取指定时间间隔内同一组内同一账本类型的套餐到账记录
	 * @param group_id 套餐组id
	 * @param process_cycle 指定时间间隔
	 * @param balance_type_id 
	 * @param l 
	 * @return 
	 */
	public List<LifeProductResourceExtRel> getExtLogByGroupId(List<LifeProductResourceExtRel> lprer,String group_id,long acct_month,
			long process_cycle, int balance_type_id) {
		List<LifeProductResourceExtRel> lstore = new ArrayList<LifeProductResourceExtRel>();
		if(lprer != null && !lprer.isEmpty()){
			for(LifeProductResourceExtRel lifeProductResourceExtRel : lprer){
				if((lifeProductResourceExtRel.getGroup_id().equals(group_id)) && ((acct_month - lifeProductResourceExtRel.getAcct_month()) <= process_cycle ) && (lifeProductResourceExtRel.getBalance_type_id() == balance_type_id)){
					lstore.add(lifeProductResourceExtRel);
				}
			}
		}
		return lstore;
	}
	/**
	 * 找出指定的账本信息
	 * @param listInfoPayBalance 账本列表
	 * @param balance_id 账本id
	 * @return
	 */
	public InfoPayBalance getInfoPayBalanceByBalanceId(
			List<InfoPayBalance> listInfoPayBalance, long balance_id) {
		if(listInfoPayBalance != null && !listInfoPayBalance.isEmpty()){
			for(InfoPayBalance ipb : listInfoPayBalance){
				if(ipb.getBalance_id() == balance_id){
					return ipb;
				}
			}
		}
		return null;
	}
	  /**
     * 取有效账本
     *
     * @param ipbs
     * @param payTime
     */
    public void filterInactiveAccountBook(List<InfoPayBalance> ipbs,
                                           String payTime) {
        if ((ipbs != null) && (!ipbs.isEmpty())) {
            String currentDate = payTime.substring(0, 10);
            for (Iterator<InfoPayBalance> iter = ipbs.iterator(); iter
                    .hasNext(); ) {
                InfoPayBalance ipb = iter.next();
                if ((ipb.getEff_date().toString().compareTo(currentDate) > 0)
                        || (ipb.getExp_date().toString().compareTo(currentDate) < 0)) {
                    iter.remove();
                }
            }
        }
    }
    
    /**
	 * 
	 * @param state_ref_type
	 *            1:开户时间，2：激活时间，3：套餐生效时间
	 * @param create_date
	 * @param active_date
	 * @param product_eff_date
	 * @param act__date
	 * @return 基准时间 格式：YYYYMMDDHH24MISS,-1表示不是首月
	 * @throws ParseException
	 */
	public static String getBaseDate(int state_ref_type, Date create_date,
			Date active_date, Date product_eff_date, Date act_date)
			throws ParseException {
		LOGGER.debug("--state_ref_type:" + state_ref_type + ",create_date:"
				+ create_date + ",active_date=" + active_date
				+ ",product_eff_date=" + product_eff_date + ",act_date="
				+ act_date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATEFORMATE_YEARMONTH);
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(DATEFORMAT_DATETIME);
		Date baseDate = new Date();// 基准时间
		switch (state_ref_type) {
		case 1:
			baseDate = create_date;
			break;
		case 2:
			baseDate = active_date;
			break;
		case 3:
			baseDate = product_eff_date;
			break;
		default:
			break;
		}
		long diff_value = CycleRuleCalcuUtil.getBetween(
				simpleDateFormat.format(baseDate),
				simpleDateFormat.format(act_date), DATEFORMATE_YEARMONTH, MONTH_RETURN);
		LOGGER.debug("--diff_value="+diff_value+",baseDate="+simpleDateFormat2.format(baseDate));
		if (diff_value > 0) {
			// 不是首月
			return "-1";
		}
		return simpleDateFormat2.format(baseDate);
	}

	public static long getBetween(String beginTime, String endTime,
			String formatPattern, int returnPattern) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
		Date beginDate = simpleDateFormat.parse(beginTime);
		Date endDate = simpleDateFormat.parse(endTime);
		System.out.println("---beginDate="+beginDate);
		System.out.println("---endDate="+endDate);
		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		beginCalendar.setTime(beginDate);
		endCalendar.setTime(endDate);
		switch (returnPattern) {
		case YEAR_RETURN:
			return getByField(beginCalendar, endCalendar, Calendar.YEAR);
		case MONTH_RETURN:
			return getByField(beginCalendar, endCalendar, Calendar.YEAR)
					* 12
					+ CycleRuleCalcuUtil.getByField(beginCalendar, endCalendar,
							Calendar.MONTH);
		case DAY_RETURN:
			return CycleRuleCalcuUtil.getTime(beginDate, endDate)
					/ (24 * 60 * 60 * 1000);
		case HOUR_RETURN:
			return CycleRuleCalcuUtil.getTime(beginDate, endDate) / (60 * 60 * 1000);
		case MINUTE_RETURN:
			return CycleRuleCalcuUtil.getTime(beginDate, endDate) / (60 * 1000);
		case SECOND_RETURN:
			return CycleRuleCalcuUtil.getTime(beginDate, endDate) / 1000;
		default:
			return 0;
		}
	}

	private static long getByField(Calendar beginCalendar,
			Calendar endCalendar, int calendarField) {
		return endCalendar.get(calendarField)
				- beginCalendar.get(calendarField);
	}

	private static long getTime(Date beginDate, Date endDate) {
		return endDate.getTime() - beginDate.getTime();
	}
}
