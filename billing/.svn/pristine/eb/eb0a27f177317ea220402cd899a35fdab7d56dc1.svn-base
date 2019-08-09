package com.tydic.beijing.billing.sms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.tydic.beijing.billing.sms.biz.SmsDbImpl;
import com.tydic.beijing.billing.sms.dao.HlpSmsSend;
import com.tydic.beijing.billing.sms.dao.RuleSmsRecvOpt;
import com.tydic.beijing.billing.sms.dao.TbWssSmsRecv;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
//import com.tydic.unicom.notrac.service.interfaces.NoTracModifySubsPlanServ;
//import com.tydic.unicom.crm.busi.po.PubOutputDto;
//import com.tydic.unicom.crm.busi.po.SALE_ORDER_INST;
//import com.tydic.unicom.crm.busi.po.SALE_PROD_INST;
//import com.tydic.unicom.crm.busi.po.SALE_OFFER_INST;
//import com.tydic.unicom.crm.busi.po.ModifySubsPlanInDto;

public class DealSmsRecv extends MyApplicationContextUtil {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(DealSmsRecv.class);
	private static List<RuleSmsRecvOpt> ListRuleSmsRecvOpt = new ArrayList<RuleSmsRecvOpt>();
	
	private SmsDbImpl db;

	public SmsDbImpl getDb() {
		return db;
	}

	public void setDb(SmsDbImpl db) {
		this.db = db;
	}
	
	public boolean CheckData(TbWssSmsRecv tb_wss_sms_recv){
		log.debug("in CheckData tb_wss_sms_recv:"+tb_wss_sms_recv.toString());
		if(tb_wss_sms_recv.getMSISDN()==null || tb_wss_sms_recv.getMSISDN().length()<=0){
			return false;
		}
		if(tb_wss_sms_recv.getMSG_CONTENT()==null || tb_wss_sms_recv.getMSG_CONTENT().length()<=0){
			return false;
		}
		return true;
	}
	
	
	public void run(){
		try {
			ListRuleSmsRecvOpt.clear();
			ListRuleSmsRecvOpt = db.GetRuleSmsRecvOptList();
			while(true){
				this.Deal();
				log.debug("---------------------------------------------");
				Thread.sleep(3000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Deal() throws Exception{
		int ret = 0;
		try{
			log.debug("deal sms recv now");
			List<TbWssSmsRecv> ListTbWssSmsRecv = new ArrayList<TbWssSmsRecv>();
			ListTbWssSmsRecv = db.GetTbWssSmsRecvList();
			for(TbWssSmsRecv tb_wss_sms_recv : ListTbWssSmsRecv){
				log.debug("tb_wss_sms_recv:"+tb_wss_sms_recv.toString());
				ret = -1;
				HlpSmsSend hlp_sms_send = new HlpSmsSend();
				hlp_sms_send.setMsisdn_receive(tb_wss_sms_recv.getMSISDN());
				hlp_sms_send.setMsg_id(String.valueOf(db.GetNextMsgId()));
				hlp_sms_send.setMsisdn_send("10023");
				hlp_sms_send.setPriority(50);
				
				if(!this.CheckData(tb_wss_sms_recv)){
					log.error("tb_wss_sms_recv error, update tb_wss_sms_recv 2");
					tb_wss_sms_recv.setProcess_tag("2");
					db.UpdateTbWssSmsRecv(tb_wss_sms_recv);
					continue;
				}
				
				for(RuleSmsRecvOpt rule_sms_recv_opt : ListRuleSmsRecvOpt){
					if(rule_sms_recv_opt.getEff_falg().equals("0")){
						tb_wss_sms_recv.setProcess_tag("2");
						db.UpdateTbWssSmsRecv(tb_wss_sms_recv);
						continue;
					}
					if(rule_sms_recv_opt.getSms_template().equals(tb_wss_sms_recv.getMSG_CONTENT())){
						if(rule_sms_recv_opt.getOperate_mode().equals("SENDSMS")){
							hlp_sms_send.setMessage_text(rule_sms_recv_opt.getOperate_value());
							tb_wss_sms_recv.setProcess_tag("1");
						}else if(rule_sms_recv_opt.getOperate_mode().equals("DINNERORDER")){
							log.debug("call crm");
							//ret = this.CallCrmService(rule_sms_recv_opt, tb_wss_sms_recv);
							if(ret == 1){
								tb_wss_sms_recv.setProcess_tag("1");
								hlp_sms_send.setMessage_text("|dic.billing.smsrecvdone|");
							}else{
								tb_wss_sms_recv.setProcess_tag("2");
								hlp_sms_send.setMessage_text("|dic.billing.smsrecvfail|");
							}
						}
						break;
					}
				}
				if(hlp_sms_send.getMessage_text() == null || hlp_sms_send.getMessage_text().length()<=0){
					log.debug("not find peizhi");
					tb_wss_sms_recv.setProcess_tag("2");
					db.UpdateTbWssSmsRecv(tb_wss_sms_recv);
					continue;
				}
				
				log.debug("send sms:"+hlp_sms_send.toString());
				db.UpdateAndSendSms(tb_wss_sms_recv, hlp_sms_send);
			}//--end of for
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	public int CallCrmService(RuleSmsRecvOpt rule_sms_recv_opt, TbWssSmsRecv tb_wss_sms_recv){
		log.debug("gas_bag_code:"+rule_sms_recv_opt.toString());
		log.debug("tb_wss_sms_recv:"+tb_wss_sms_recv.toString());
		NoTracModifySubsPlanServ crmService = (NoTracModifySubsPlanServ) mycontext.getBean("NoTracModifySubsPlanServ");
		
		PubOutputDto pubOutputDto = new PubOutputDto();
		List<SALE_OFFER_INST> sale_offer_inst_list = new ArrayList<SALE_OFFER_INST>();
		
		SALE_ORDER_INST sale_order_inst = new SALE_ORDER_INST();
		sale_order_inst.setCHANNEL_CODE("10014");
		
		SALE_PROD_INST sale_prod_inst = new SALE_PROD_INST();
		sale_prod_inst.setACC_NBR(tb_wss_sms_recv.getMSISDN());
		sale_prod_inst.setSERVICE_OFFER_ID("889");
		
		SALE_OFFER_INST sale_offer_inst = new SALE_OFFER_INST();
		sale_offer_inst.setOFFER_ID(rule_sms_recv_opt.getOperate_value());
		sale_offer_inst.setSERVICE_OFFER_ID("250");
		sale_offer_inst_list.add(sale_offer_inst);
		
		ModifySubsPlanInDto modifySubsPlanInDto = new ModifySubsPlanInDto();
		modifySubsPlanInDto.setSALE_ORDER_INST(sale_order_inst);
		modifySubsPlanInDto.setSALE_PROD_INST(sale_prod_inst);
		modifySubsPlanInDto.setSALE_OFFER_INST(sale_offer_inst_list);
		try {
			pubOutputDto = crmService.noTracModSubsFuncSMI(modifySubsPlanInDto);
			log.debug("ErrorCode:"+pubOutputDto.getErrorCode());
			log.debug("ErrorMessage:"+pubOutputDto.getErrorMessage());
			if(pubOutputDto.getStatus().equals("0")){//--失败
				return 0;
			}else if(pubOutputDto.getStatus().equals("1")){//--成功
				return 1;
			}
		} catch (PubOutputDto e) {
			e.printStackTrace();
		}
		return -1;
	}
	*/
	
	
}
