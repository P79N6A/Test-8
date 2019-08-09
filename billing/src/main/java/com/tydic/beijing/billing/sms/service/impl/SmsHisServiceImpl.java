package com.tydic.beijing.billing.sms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.tydic.beijing.billing.sms.biz.SmsDbImpl;
import com.tydic.beijing.billing.sms.dao.SmsSendLog;
import com.tydic.beijing.billing.sms.dao.TbSmsSendHis;

public class SmsHisServiceImpl  {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(SmsHisServiceImpl.class);

	private SmsDbImpl db;

	public SmsDbImpl getDb() {
		return db;
	}

	public void setDb(SmsDbImpl db) {
		this.db = db;
	}
	
	public void run(){
		while(true){
			try {
				log.debug("DealHis run");
				DealHis();
				log.debug("Deal His sleep 3s");
				log.debug("-----------------------------------------------------------------------------");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception p) {
				p.printStackTrace();
			}
		}
	}
	
	public void DealHis() throws Exception {
		try{
//			log.debug("in DealHis");
//			List<TbSmsSendHis> ListTbSmsSendHis = new ArrayList<TbSmsSendHis>();
//			ListTbSmsSendHis = db.GetSmsHisList();
//			log.debug("ListTbSmsSendHis length:"+ListTbSmsSendHis.size());
//			if(ListTbSmsSendHis.size() > 0){
//				for(TbSmsSendHis tb_sms_send_his:ListTbSmsSendHis){
//					log.debug("tb_sms_send_his:"+tb_sms_send_his.toString());
//					SmsSendLog sms_send_log = new SmsSendLog();
//					sms_send_log.setJob_number(tb_sms_send_his.getJob_number());
//					sms_send_log.setMessage_text(tb_sms_send_his.getMessage_text());
//					sms_send_log.setMsg_id(tb_sms_send_his.getMsg_id());
//					sms_send_log.setMsisdn_receive(tb_sms_send_his.getMsisdn_receive());
//					sms_send_log.setMsisdn_send(tb_sms_send_his.getMsisdn_send());
//					sms_send_log.setPara_key(tb_sms_send_his.getPara_key());
//					sms_send_log.setPriority(tb_sms_send_his.getMsg_prior());
//					sms_send_log.setSend_time(tb_sms_send_his.getSend_time());
//					log.debug("11111111111111111111");
//					db.DealSmsHis(sms_send_log);
//					log.debug("msg_id:"+sms_send_log.getMsg_id()+"delete tb_sms_send_his and insert sms_send_log");
//				}
//			}
			db.updateLogAndSmsSend();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
