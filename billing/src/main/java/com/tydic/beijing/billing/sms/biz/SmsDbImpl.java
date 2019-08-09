package com.tydic.beijing.billing.sms.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.beijing.billing.sms.dao.HlpSmsContant;
import com.tydic.beijing.billing.sms.dao.HlpSmsException;
import com.tydic.beijing.billing.sms.dao.HlpSmsSend;
import com.tydic.beijing.billing.sms.dao.HlpSystemParameter;
import com.tydic.beijing.billing.sms.dao.RuleSmsRecvOpt;
import com.tydic.beijing.billing.sms.dao.SmsSendLog;
import com.tydic.beijing.billing.sms.dao.TbSmsSendHis;
import com.tydic.beijing.billing.sms.dao.TbWssSmsRecv;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class SmsDbImpl{
	private static org.slf4j.Logger log = LoggerFactory.getLogger(SmsDbImpl.class);
	
	public List<HlpSmsSend> GetUserList(int amount) throws Exception{
		List<HlpSmsSend> ListHlpSmsSend = new ArrayList<HlpSmsSend>();
		ListHlpSmsSend = S.get(HlpSmsSend.class).query(Condition.build("getsmslist").filter("amount", amount));
		return ListHlpSmsSend;
	}
	
	public Map<String,HlpSystemParameter> GetHlpSystemParameterList() throws HlpSmsException,Exception{
		List<HlpSystemParameter> ListHlpSystemParameter = new ArrayList<HlpSystemParameter>();
		ListHlpSystemParameter = S.get(HlpSystemParameter.class).query(Condition.build("GetHlpSystemParameterList"));
		Map<String, HlpSystemParameter> paraMap = new HashMap<String,HlpSystemParameter>();
		
		if(ListHlpSystemParameter !=null){
			for(HlpSystemParameter iter : ListHlpSystemParameter){
				paraMap.put(iter.getPara_key(), iter);
			}
		}else{
			log.debug("++++++hlpSystemParameter not exists++++++");
			throw new HlpSmsException(HlpSmsContant.SMS_PARAM_NOT_EXISTS,"未配置短信模版参数");
		}
		return paraMap;
	}
	
	public HlpSystemParameter GetParaValue(String parakey) throws Exception{
		HlpSystemParameter hlp_system_para = S.get(HlpSystemParameter.class).queryFirst(Condition.build("getparavalue").filter("para_key", parakey));
		return hlp_system_para;
	}

	public void updateSmsHis(TbSmsSendHis tb_sms_send_his) throws Exception {
		log.debug("++++insert into tb_sms_send_his:{}",tb_sms_send_his.toString());
		S.get(TbSmsSendHis.class).create(tb_sms_send_his);
		log.debug("+++++delete hlp_sms_send, msg_id:{}",tb_sms_send_his.getMsg_id());
		S.get(HlpSmsSend.class).batch(Condition.build("deletehlpsmssend").filter("msg_id", tb_sms_send_his.getMsg_id()), new HlpSmsSend());
		//S.get(HlpSmsSend.class).remove(tb_sms_send_his.getMsg_id());
	}

	public int updateHlpSmsForErr(String msgId,int reasonCode){
//		log.debug("update halt sms for error status . msg : {}",msgId);
		return S.get(HlpSmsSend.class).update(Condition.build("updateHlpSmsForErr").filter("reasonCode", reasonCode).filter("msgId", msgId));
	}
	
	public int deleteHlpSmsSend(HlpSmsSend hlpSmsSend){
		return S.get(HlpSmsSend.class).batch(Condition.build("deleteHlpSmsSent").filter("msg_id",hlpSmsSend.getMsg_id()));
	}
	
	public List<TbSmsSendHis> GetSmsHisList() throws Exception {
		List<TbSmsSendHis> ListTbSmsSendHis = new ArrayList<TbSmsSendHis>();
		ListTbSmsSendHis = S.get(TbSmsSendHis.class).query(Condition.build("getSmsHisList"));
		return ListTbSmsSendHis;
	}
	
	public void updateSmsHis(SmsSendLog sms_send_log) throws Exception{
		log.debug("insert sms_send_log, msg_id:"+sms_send_log.getMsg_id());
		S.get(SmsSendLog.class).create(sms_send_log);
		log.debug("delete tb_sms_send_his, msg_id:"+sms_send_log.getMsg_id());
		S.get(TbSmsSendHis.class).batch(Condition.build("deleteTbSmsSendHis").filter("msg_id", sms_send_log.getMsg_id()), new TbSmsSendHis());
	}
	
	public List<TbWssSmsRecv> GetTbWssSmsRecvList() throws Exception {
		List<TbWssSmsRecv> ListTbWssSmsRecv = new ArrayList<TbWssSmsRecv>();
		ListTbWssSmsRecv = S.get(TbWssSmsRecv.class).query(Condition.build("GetTbWssSmsRecvList"));
		return ListTbWssSmsRecv;
	}
	
	public void UpdateTbWssSmsRecv(TbWssSmsRecv tb_wss_sms_recv) throws Exception{
		S.get(TbWssSmsRecv.class).update(tb_wss_sms_recv);
	}
	
	public long GetNextMsgId() throws Exception{
		Sequences s = S.get(Sequences.class).queryFirst(Condition.build("GetNextMsgId"));
		return s.getSeq();
	}
	
	public void createSms(HlpSmsSend hlp_sms_send) throws Exception{
		S.get(HlpSmsSend.class).create(hlp_sms_send);
	}
	
	public List<RuleSmsRecvOpt> GetRuleSmsRecvOptList() throws Exception {
		List<RuleSmsRecvOpt> ListRuleSmsRecvOpt = new ArrayList<RuleSmsRecvOpt>();
		ListRuleSmsRecvOpt = S.get(RuleSmsRecvOpt.class).query(Condition.build("GetRuleSmsRecvOptList"));
		return ListRuleSmsRecvOpt;
	}
	
	public void UpdateAndSendSms(TbWssSmsRecv tb_wss_sms_recv, HlpSmsSend hlp_sms_send){
		S.get(TbWssSmsRecv.class).update(tb_wss_sms_recv);
		S.get(HlpSmsSend.class).create(hlp_sms_send);
	}
	
	public InfoUser queryUserStatus(String deviceNumber){
		InfoUser user = S.get(InfoUser.class).queryFirst(Condition.build("queryUserStatus").filter("deviceNumber", deviceNumber));
		return user;
	}
	
	
	
	public void updateLogAndSmsSend(){
		S.get(SmsSendLog.class).update(Condition.build("addAll"));
		S.get(TbSmsSendHis.class).remove(Condition.build("delSuccSms"));
	}
}
