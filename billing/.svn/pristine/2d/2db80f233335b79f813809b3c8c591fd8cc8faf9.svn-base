package com.tydic.beijing.billing.sms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.sms.biz.SmsDbImpl;
import com.tydic.beijing.billing.sms.dao.HlpSmsContant;
import com.tydic.beijing.billing.sms.dao.HlpSmsException;
import com.tydic.beijing.billing.sms.dao.HlpSmsSend;
import com.tydic.beijing.billing.sms.dao.HlpSystemParameter;
import com.tydic.beijing.billing.sms.dao.TbSmsSendHis;

public class SmsServiceImpl  {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);
	private  Map<String,HlpSystemParameter> paraMap = new HashMap<String,HlpSystemParameter>();
	private ExecutorService executorService ;
	final private BlockingQueue<TbSmsSendHis> smsQue= new ArrayBlockingQueue<TbSmsSendHis>(5000);
	private int poolSize;
	private int amount;
	final private AtomicLong pendingSmsVisits = new AtomicLong();
	private SmsDbImpl db;

	public SmsDbImpl getDb() {
		return db;
	}

	public void setDb(SmsDbImpl db) {
		this.db = db;
	}

	public static int GetHour() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	
	public void run(){
		try {
			paraMap = db.GetHlpSystemParameterList();
			executorService = Executors.newFixedThreadPool(poolSize);
			
			List<HlpSmsSend> hlpSmsSendList =  new ArrayList<HlpSmsSend>();
			while(pendingSmsVisits.get()==0 ){
				long begin = System.currentTimeMillis() ;

				hlpSmsSendList.clear();
				hlpSmsSendList = db.GetUserList(amount);
				
				if(hlpSmsSendList != null && hlpSmsSendList.size()>0){
//					log.debug("++++get sms total : {}",hlpSmsSendList.size());
					startDealMsg(hlpSmsSendList);
				}else{
					Thread.sleep(3000);
				}
				TbSmsSendHis item = null ;
				while(pendingSmsVisits.get()>0 || smsQue.size()>0){
					try {
						item = smsQue.poll(2,TimeUnit.SECONDS);
						if(item != null ){
							db.updateSmsHis(item);
						}else{
							log.debug("++++++queue get element null ");
						}
					} catch (InterruptedException e) {
						if(item != null)
							log.error(">>>>>exception:msgId {}",item.getMsg_id());
						log.error(">>>>>>"+e.getMessage());
					}
				}
				long end = System.currentTimeMillis();
				log.debug("batch deal done ! size {} costs {} mills",hlpSmsSendList.size(),end-begin);
			}
			
			executorService.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
			
		}catch(HlpSmsException e){
			log.error("++++errorCode = {} , errorMsg = {}",e.getErrorCode() , e.getErrorMsg());
		} catch (Exception p) {
			p.printStackTrace();
		}
	}


	
	
	public void startDealMsg(final List<HlpSmsSend> smsList){
		pendingSmsVisits.incrementAndGet();
		executorService.execute(new Runnable(){

			@Override
			public void run() {
				perform(smsList);
			}
			
		});
	}
	
	
	public void perform(final List<HlpSmsSend> smsList){
		for(HlpSmsSend iter : smsList){
			if (null!=iter && null!=iter.getMsisdn_receive() && !iter.getMsisdn_receive().equals("")) {
				if (iter.getMsisdn_receive().length()>11) {
					db.deleteHlpSmsSend(iter);
					log.error("该接收用户是JN用户，msisdn_receive是："+iter.getMsisdn_receive());
					continue;
				}
			}
			TbSmsSendHis tbSmsSendHis = new TbSmsSendHis();
			String[] SmsInfo = iter.getMessage_text().split("\\|");
			String paraKey = SmsInfo[1];
			tbSmsSendHis.setPara_key(paraKey);
			//--匹配模板
			log.debug("+++++para_key:{}",paraKey);
			HlpSystemParameter param = paraMap.get(paraKey);
			if(param == null ){	//数据捞取已过滤
				log.debug("+++++no parameters msg{}",iter.getMsg_id());
				db.updateHlpSmsForErr(iter.getMsg_id(), HlpSmsContant.NO_PARAMETER);
				continue;
			}
			if (GetHour() >= param.getBegin_time() && GetHour() < param.getEnd_time()) {
				
				if(paraKey.equals(HlpSmsContant.HALT_ALARM_KEY) && !isUser201(iter.getMsisdn_receive())){
					log.debug("+++++++ wrong status for halt user msg{}",iter.getMsg_id());
					db.updateHlpSmsForErr(iter.getMsg_id(),HlpSmsContant.WRONG_STATUS_HALT_USER);
					continue;
				}
				
				String text = param.getPara_value();
				//--拼接短信
				for (int i = 0; i < (SmsInfo.length - 2); i++) {
					String temp = "{" + i + "}";
					text = text.replace(temp, SmsInfo[i + 2]);
				}
				tbSmsSendHis.setMessage_text(text);
				tbSmsSendHis.setJob_number(param.getPara_number());
				tbSmsSendHis.setMsg_prior(param.getPara_priority());
				tbSmsSendHis.setMsisdn_receive(iter.getMsisdn_receive());
				tbSmsSendHis.setMsisdn_send(iter.getMsisdn_send());
				tbSmsSendHis.setSend_time(iter.getSend_time());
				tbSmsSendHis.setMsg_id(iter.getMsg_id());
				//--插入tb_sms_send_his，删除hlp_sms_send
				try{
					if(smsQue.size()<200){
						smsQue.put(tbSmsSendHis);
					}else{
						db.updateSmsHis(tbSmsSendHis);
						log.debug("insert tb_sms_send_his, delete hlp_sms_send done! msg{}",iter.getMsg_id());
					}
					
				} catch (Exception e) {
					log.error("insert and delete msg{} error : {}",iter.getMsg_id(),e.getMessage());
					db.updateHlpSmsForErr(iter.getMsg_id(),HlpSmsContant.DB_UPDATE_ERROR);
				}	
						
			}
				
		}
		pendingSmsVisits.decrementAndGet();
	}
	
	
	
	public boolean isUser201(String deviceNumber){
		InfoUser user = db.queryUserStatus(deviceNumber);
		if(user != null ){
			if(!StringUtils.isBlank(user.getUser_status()) && user.getUser_status().equals("201")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
}
/*
	@Override
	public void DealHis() throws Exception {
		try{
			log.debug("in DealHis");
			List<TbSmsSendHis> ListTbSmsSendHis = new ArrayList<TbSmsSendHis>();
			ListTbSmsSendHis = db.GetSmsHisList();
			log.debug("ListTbSmsSendHis length:"+ListTbSmsSendHis.size());
			if(ListTbSmsSendHis.size() >= 0){
				for(TbSmsSendHis tb_sms_send_his:ListTbSmsSendHis){
					log.debug("tb_sms_send_his:"+tb_sms_send_his.toString());
					SmsSendLog sms_send_log = new SmsSendLog();
					sms_send_log.setJob_number(tb_sms_send_his.getJob_number());
					sms_send_log.setMessage_text(tb_sms_send_his.getMessage_text());
					sms_send_log.setMsg_id(tb_sms_send_his.getMsg_id());
					sms_send_log.setMsisdn_receive(tb_sms_send_his.getMsisdn_receive());
					sms_send_log.setMsisdn_send(tb_sms_send_his.getMsisdn_send());
					sms_send_log.setPara_key(tb_sms_send_his.getPara_key());
					sms_send_log.setPriority(tb_sms_send_his.getMsg_prior());
					sms_send_log.setSend_time(tb_sms_send_his.getSend_time());
					log.debug("11111111111111111111");
					db.DealSmsHis(sms_send_log);
					log.debug("msg_id:"+sms_send_log.getMsg_id()+"delete tb_sms_send_his and insert sms_send_log");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/

	
