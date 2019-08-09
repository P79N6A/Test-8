package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.QUserReasonSend;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class MultiStopCallerDisplayProcess {

	public static final  Logger log = LoggerFactory.getLogger(MultiStopCallerDisplayProcess.class);
	
	@Transactional(rollbackFor=Exception.class)
	public void StopCallerDisplay() throws Exception {
		// TODO Auto-generated method stub
		log.debug("=======开始进行月末批量停来显，在月底最后25号执行完======");
		List<InfoUser> allInfoList=new ArrayList<InfoUser>();
		//当前月份
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime=sdf.format(new Date());
		//得到所有红名单用户的用户信息
		
		allInfoList=S.get(InfoUser.class).query(Condition.build("queryInfoAllList"));
		String currentMonth=currentTime.substring(4, 6);
		String currentYear=currentTime.substring(0,4);
		log.debug("=======本月份是"+currentMonth);
		log.debug("=======本年是"+currentYear);
		//上月
		String lastTime=getLastMonth(currentTime);
		String lastMonth=lastTime.substring(4, 6);
		log.debug("=======上月份1号凌晨是"+lastTime);
		List<InfoUser> infoUsers=S.get(InfoUser.class).query(Condition.build("queryNeedStopUser").filter("lastTime",lastTime).filter("currentMonth", currentMonth).filter("lastMonth", lastMonth));
		
		if (infoUsers==null) {
			log.debug("没有两月未通话的用户!");
			return;
		}else {
				for(int i=0;i<infoUsers.size();i++){
					InfoUser infoUser=infoUsers.get(i);
					if (!flagInfoAllList(infoUser,allInfoList)) {
						setReasonSend(infoUser);
					}
					
				}
		}
	
	}

	/**
	 * 上个月份
	 * @param
	 * @return
	 */
	private String getLastMonth(String current){
		String month=current.substring(4, 6);
		String year=current.substring(0,4);
		String lastTime;
		if (month.equals("01")) {
			int currentyear=Integer.parseInt(year);
			String lastyear=String.valueOf(currentyear-1);
			lastTime=lastyear+"12"+"01000000";
			return lastTime;
		}
		int currentmonth=Integer.parseInt(month);
		String lastmonth=String.valueOf(currentmonth-1);
		if (lastmonth.length()==1) {
			lastmonth="0"+lastmonth;
		}
		lastTime=year+lastmonth+"01000000";
		return lastTime;
	}
	
	/**
	 * 判断未通话两个月的用户是否有红名单用户
	 * @param infoUser
	 * @param listInfoUsers
	 * @return
	 */
	private boolean flagInfoAllList(InfoUser infoUser,List<InfoUser> listInfoUsers){
		boolean flag=false;
		log.debug("该用户是=================>"+infoUser.getUser_id());
		if(listInfoUsers!=null && listInfoUsers.size()>0){
			for(int i=0;i<listInfoUsers.size();i++){
				 if(infoUser.getUser_id().equals(listInfoUsers.get(i).getUser_id())) {
					log.debug("该用户是:"+listInfoUsers.get(i));
					flag=true;
					break;
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 将102插入q_user_reason_send表 
	 * @param infoUser
	 */
	
	private void setReasonSend(InfoUser infoUser){
		QUserReasonSend qUserReasonSend = new QUserReasonSend();
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//YYYY-MM-DD HH24:MI:SS
		qUserReasonSend.setActive_type("");
		qUserReasonSend.setCharge_id("");
		qUserReasonSend.setEnqueue_date(sdfdate.format(new Date()));
		qUserReasonSend.setLocal_net(infoUser.getLocal_net());
		qUserReasonSend.setReason_code("300");
		qUserReasonSend.setTele_type(infoUser.getTele_type());
		qUserReasonSend.setUser_no(infoUser.getUser_id());
		
		qUserReasonSend.setSerial_num(S.get(Sequences.class)
				.queryFirst(Condition.build("queryQReasonSn")).getSeq());

       S.get(QUserReasonSend.class).create(qUserReasonSend);
	}
	
}
