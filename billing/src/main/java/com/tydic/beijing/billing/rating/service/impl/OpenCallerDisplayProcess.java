package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.CDRCalling;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.QUserReasonSend;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.beijing.billing.rating.domain.InfoUserReason;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class OpenCallerDisplayProcess {
	public static final  Logger log = LoggerFactory.getLogger(OpenCallerDisplayProcess.class);
	private static final int forFlagOver=1;
	private static final int forFlag=0;
	private SmsOpenCallerDisplay openCallerDSms;

	public SmsOpenCallerDisplay getOpenCallerDSms() {
		return openCallerDSms;
	}

	public void setOpenCallerDSms(SmsOpenCallerDisplay openCallerDSms) {
		this.openCallerDSms = openCallerDSms;
	}
	private String sleepTime;
	
	public String getSleepTime() {
		return sleepTime;
	}


	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}


	public void OpenCallDisplay() throws Exception {
		// TODO Auto-generated method stub
//		log.debug("===============实时调用，开来显服务=============");
//		List<InfoUserReason> listOfInfoUserReason=new ArrayList<InfoUserReason>();
//		List<CDRCalling> listOfCdrCalling=new ArrayList<CDRCalling>();
//		InfoUser infoUser=new InfoUser();
//		long begintime=System.currentTimeMillis();
//		long beginNum=0;
//		long endNum=100;
//		while(true){
//			listOfInfoUserReason=getListOfInfoUserReason(beginNum, endNum);//查询停掉来显的用户
//			int flag=openCallerDisplay(listOfInfoUserReason,begintime,beginNum, endNum,listOfCdrCalling,infoUser);
//			if (flag==1) {
//				beginNum=0;
//				endNum=100;
//			}else {
//				beginNum+=100;
//				endNum+=100;
//			}
//		}
	}
//	
	
	
	//核心方法
	public int openCallerDisplay(List<InfoUserReason> listOfInfoUserReason,
							long begintime,long beginNum,long endNum,List<CDRCalling> listOfCdrCalling,InfoUser infoUser) throws Exception{
		
		if (null==listOfInfoUserReason || listOfInfoUserReason.size()==0) {
//			beginNum=0;
//			endNum=100;
			long sleeptime=Long.parseLong(sleepTime);
			long endtime=System.currentTimeMillis();
			long interTime=endtime-begintime;
			log.debug("====循环完成一次查询,耗时:"+interTime);
			Thread.sleep(sleeptime);
			return forFlagOver;
		}
		long allforbengintime=System.currentTimeMillis();
		for(int i=0;i<listOfInfoUserReason.size();i++){
			long forbengintime=System.currentTimeMillis();
			InfoUserReason infoUserReason=listOfInfoUserReason.get(i);
			log.debug("这次的infoUserReason是"+infoUserReason.toString());
			log.debug("开始查找是否有话单：");
			listOfCdrCalling=getListOfCdrCalling(infoUserReason);
			log.debug("查找完毕!");
			if (null!=listOfCdrCalling && listOfCdrCalling.size()>0) {
				//如果有记录，则开来显
				infoUser=getInfoUser(infoUserReason);
				if (null!=infoUser) {//如果有该用户并且非双停状态
					openCallerDSms.setReasonSend(infoUser);//插入多原因表
				}else {
					log.error("没有用户信息!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					continue;
				}
			}
			long forendtime=System.currentTimeMillis();
			long forinterTime=forendtime-forbengintime;
			log.debug("一次for循环需要的时间:"+forinterTime+"开始时间"+forbengintime+"结束时间"+forendtime+"循环次数");
		}
		long allforendtime=System.currentTimeMillis();
		long allforinterTime=allforendtime-allforbengintime;
		log.debug("100次循环耗时："+allforinterTime+"开始时间"+allforbengintime+"结束时间"+allforendtime);
//		beginNum+=100;
//		endNum+=100;
		return forFlag;
	}

	
	/**
	 * 停来显的用户列表
	 * @param beginNum
	 * @param endNum
	 * @return
	 */
	public List<InfoUserReason> getListOfInfoUserReason(long beginNum,long endNum){
		List<InfoUserReason> listOfInfoUserReason=new ArrayList<InfoUserReason>();
		listOfInfoUserReason=S.get(InfoUserReason.class).query(Condition.build("queryStopCaller").filter("beginNum", beginNum).filter("endNum", endNum));
		return listOfInfoUserReason;
	}
	
	
	/**
	 * 查看停来显之后语音详单是否有记录
	 * @param infoUserReason
	 * @return
	 */
	private List<CDRCalling> getListOfCdrCalling(InfoUserReason infoUserReason){
		List<CDRCalling> listOfCdrCalling=new ArrayList<CDRCalling>();
			log.debug("开始查找是否有话单了===========================");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
			String currentTime=sdf.format(new Date());
			String userId=infoUserReason.getUser_id();
			String currentMonth=currentTime.substring(4, 6);
			Date sessionBeginTime=infoUserReason.getEff_date();
			String beginTime = sdf.format(sessionBeginTime);
//			listOfCdrCalling=S.get(CDRCalling.class).query(Condition.build("queryCdrCallingByUserId").filter("currentMonth",currentMonth).filter("userId", userId).filter("sessionBeginTime",sessionBeginTime));
			listOfCdrCalling=S.get(CDRCalling.class).query(Condition.build("queryCdrCallingByUserId").filter("userId", userId).filter("sessionBeginTime",beginTime).filter("currentMonth",currentMonth));

		return listOfCdrCalling;
	}
	
	/**
	 * 通过user_id得到用户信息
	 * @param infoUserReason
	 * @return
	 */
	private InfoUser getInfoUser(InfoUserReason infoUserReason){
		List<InfoUser> infoUsers=new ArrayList<InfoUser>();
		InfoUser infoUser=null;
		String userId=infoUserReason.getUser_id();
		infoUsers=S.get(InfoUser.class).query(Condition.build("queryOpenByUserId").filter("user_id",userId));
		if (null!=infoUsers&&infoUsers.size()>0) {
			infoUser=infoUsers.get(0);
		}else {
			log.error("没有用户信息！！！");
		}
		return infoUser;
	}
	
	
	
	/**
	 * 将301插入q_user_reason_send表 ,开来显
	 * @param infoUser
	 */
	
//	private void setReasonSend(InfoUser infoUser){
//		QUserReasonSend qUserReasonSend = new QUserReasonSend();
//		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//YYYY-MM-DD HH24:MI:SS
//		qUserReasonSend.setActive_type("");
//		qUserReasonSend.setCharge_id("");
//		qUserReasonSend.setEnqueue_date(sdfdate.format(new Date()));
//		qUserReasonSend.setLocal_net(infoUser.getLocal_net());
//		qUserReasonSend.setReason_code("301");
//		qUserReasonSend.setTele_type(infoUser.getTele_type());
//		qUserReasonSend.setUser_no(infoUser.getUser_id());
//		
//		qUserReasonSend.setSerial_num(S.get(Sequences.class)
//				.queryFirst(Condition.build("queryQReasonSn")).getSeq());
//
//       S.get(QUserReasonSend.class).create(qUserReasonSend);
//	}
}
