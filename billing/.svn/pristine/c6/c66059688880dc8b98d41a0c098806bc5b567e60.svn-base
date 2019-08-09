package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.QUserReasonSend;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class SmsOpenCallerDisplay {
	/**
	 * 将301插入q_user_reason_send表 ,开来显
	 * @param infoUser
	 */
	
	@Transactional(rollbackFor=Exception.class)
	public void setReasonSend(InfoUser infoUser){
		QUserReasonSend qUserReasonSend = new QUserReasonSend();
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//YYYY-MM-DD HH24:MI:SS
		qUserReasonSend.setActive_type("");
		qUserReasonSend.setCharge_id("");
		qUserReasonSend.setEnqueue_date(sdfdate.format(new Date()));
		qUserReasonSend.setLocal_net(infoUser.getLocal_net());
		qUserReasonSend.setReason_code("301");
		qUserReasonSend.setTele_type(infoUser.getTele_type());
		qUserReasonSend.setUser_no(infoUser.getUser_id());
		
		qUserReasonSend.setSerial_num(S.get(Sequences.class)
				.queryFirst(Condition.build("queryQReasonSn")).getSeq());

       S.get(QUserReasonSend.class).create(qUserReasonSend);
	}
}
