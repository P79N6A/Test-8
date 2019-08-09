package com.tydic.beijing.billing.rating.service.impl;

import java.text.ParseException;
import java.util.Date;

import java.text.SimpleDateFormat;
import com.tydic.beijing.billing.dao.HlpSmsSend;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;
import com.tydic.uda.service.S;

public class LiangHaoSendMsg {
	public static final  Logger log = LoggerFactory.getLogger(LiangHaoSendMsg.class);
	public void senSmsToLiangHao(InfoUser info,long allRealBalance,Date exp_date) throws ParseException{
		log.debug("用户发送短信开始!!!");
		Date date=new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String datenow=dateFormat.format(date);
		log.debug("开始时间类型转换成功"+datenow);
		String expdate=dateFormat.format(exp_date);
		log.debug("结束时间："+expdate);
		HlpSmsSend hlpss=new HlpSmsSend();
		//发短信日
		String yearnow=datenow.substring(0,4);
		String monthnow=datenow.substring(4,6);
		String daynow=datenow.substring(6,8);
		String hournow=datenow.substring(8,10);
		//单位转换为元
		log.debug("剩余金钱为："+allRealBalance);
		double RealBalance=allRealBalance/100.0;
		log.debug("剩余金钱为==============="+RealBalance);
		//截止日
		String expyear=expdate.substring(0,4);
		String expmonth=expdate.substring(4,6);
		String expday=expdate.substring(6,8);
		String exphour="24";
//		String exphour=expdate.substring(8,10);
//		if (exphour.equals("00")) {
//			exphour="24";
//		}else {
//			int hour=Integer.parseInt(exphour);
//			hour=hour+1;
//			exphour=hour+"";
//		}
		log.debug("结束时间类型转换成功"+exphour);
		
		String msg="|aoc.dic.89balancetypealarm|"+yearnow+"|"+monthnow+"|"+daynow+"|"+hournow+"|"+RealBalance+"|"+expyear+"|"+expmonth+"|"+expday+"|"+exphour;
		hlpss.setMessage_text(msg);
		hlpss.setMsisdn_receive(info.getDevice_number());
		hlpss.setMsisdn_send("10023");
		hlpss.setPriority(50);
		hlpss.setCreate_time(datenow);
		hlpss.setRetry_times(0);
		S.get(HlpSmsSend.class).create(hlpss);
	}
}
