package com.tydic.beijing.billing.account.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.tydic.beijing.billing.account.biz.BalanceChangeOps;
//import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.account.biz.ContractDb;
import com.tydic.beijing.billing.account.service.AccountProcess;
import com.tydic.beijing.billing.account.service.CreateAccountBook;
import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.LifeUserReleaseCal;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DealContractService implements Runnable {
private static org.slf4j.Logger log = LoggerFactory.getLogger(DealContractService.class);
	
	private ContractDb db;
	private CreateAccountBook ipb;
	private BalanceChangeOps bco;
	private int mod = 1;
	private int partion = 1;
	
//	private String channelId;
//	private String externalSystemId;
//	private String staffId;
	
	
    private DealContractServiceProcess presentProcess ;
	private AccountProcess accountProcess;	
	 

	public DealContractServiceProcess getPresentProcess() {
		return presentProcess;
	}

	public void setPresentProcess(DealContractServiceProcess presentProcess) {
		this.presentProcess = presentProcess;
	}

//	public String getChannelId() {
//		return channelId;
//	}
//
//	public void setChannelId(String channelId) {
//		this.channelId = channelId;
//	}
//
//	public String getExternalSystemId() {
//		return externalSystemId;
//	}
//
//	public void setExternalSystemId(String externalSystemId) {
//		this.externalSystemId = externalSystemId;
//	}
//
//	public String getStaffId() {
//		return staffId;
//	}
//
//	public void setStaffId(String staffId) {
//		this.staffId = staffId;
//	}

	public int getMod() {
		return mod;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	public int getPartion() {
		return partion;
	}

	public void setPartion(int partion) {
		this.partion = partion;
	}

	public CreateAccountBook getIpb() {
		return ipb;
	}

	public void setIpb(CreateAccountBook ipb) {
		this.ipb = ipb;
	}

	public BalanceChangeOps getBco() {
		return bco;
	}

	public void setBco(BalanceChangeOps bco) {
		this.bco = bco;
	}

	public ContractDb getDb() {
		return db;
	}

	public void setDb(ContractDb db) {
		this.db = db;
	}
	
	public void run(){
		try {
			while(true){
				DealPresent();
				Thread.sleep(3000);
				log.debug("mod:"+mod+", partion:"+partion);
				log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String SmsText(String ... params){
		String sms_text;
		switch(params[0]){
		case "20003":
		case "20002":{
			sms_text = "|"+"aoc.dic.contract20002"+"|";
			for(int i=1; i<=params.length; i++){
				sms_text = sms_text + params[i] + "|";
			}
		}default:{
			sms_text = "|"+"aoc.dic.contract20002"+"|";
			for(int i=1; i<=params.length; i++){
				sms_text = sms_text + params[i] + "|";
			}
		}
		}
		log.debug("message_text:"+sms_text);
		return sms_text;
	}
	
	public long GetUserBalance(String pay_id) throws Exception{
		long balance = 0;
		List<InfoPayBalance> ListInfoPayBalance = new ArrayList<InfoPayBalance>();
		List<CodeBilBalanceType> ListCodeBilBalanceType = new ArrayList<CodeBilBalanceType>();
		ListInfoPayBalance = db.QueryInfoPayBalance(pay_id);
		ListCodeBilBalanceType = db.GetCodeBilBalanceType();
		
		for(InfoPayBalance info_pay_balance : ListInfoPayBalance){
			for(CodeBilBalanceType code_bil_balance_type : ListCodeBilBalanceType){
				if(code_bil_balance_type.getUnit_type_id()==0 && code_bil_balance_type.getBalance_type_id()==info_pay_balance.getBalance_type_id()){
					balance = balance + info_pay_balance.getReal_balance();
					log.debug("balance:"+balance);
				}
			}
		}
		
		return balance;
	}
	
	public void Deal() throws Exception {
		
	}
	

	public void DealPresent() throws Exception {
		try{
			log.debug("==in DealPresent");
			String addup_flag = "";
			long balance_id = 0;
			
			SimpleDateFormat sdf =new SimpleDateFormat("MM");
			List<LifeUserReleaseCal> ListContractInstId = new ArrayList<LifeUserReleaseCal>();
//			List<CodeBilBalanceType> ListCodeBilBalanceType = new ArrayList<CodeBilBalanceType>();
////			List<RuleParameters> ListRuleParameters = new ArrayList<RuleParameters>();
//			ListCodeBilBalanceType = db.GetCodeBilBalanceType();
////			ListRuleParameters = db.GetRuleParameters();   //TODO 只用到了8000，改为只查8000的配置 
//			Calendar now = Calendar.getInstance();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//			
//			balance_id = 0;
//			long sum_balance = 0;
			//ListContractInstId = db.GetUserNeedPresent(mod, partion);
			
			while (true){
				long time1 =  System.currentTimeMillis();
			ListContractInstId = db.GetUserNeedPresent(mod,partion); //TODO 10W+用户一次获取？
			long time2 =  System.currentTimeMillis();
			log.debug("-----------------------获取待处理用户耗时-------------------------："+(time2-time1));
			if(ListContractInstId == null || ListContractInstId.size()==0){
				break;
			}
			
			Map<String, Object> filter = new HashMap<String, Object>();
			CodeAcctMonth codeAcctMonth =  S.get(CodeAcctMonth.class).queryFirst(Condition.build("queryBySysdate"));
						
			for(LifeUserReleaseCal tmpcal : ListContractInstId){
				//return;
				presentProcess.DealPresent(tmpcal);
				
				//销账
				String user_id = tmpcal.getUser_id();
				filter.clear();
		
				filter.put("acct_month", codeAcctMonth.getAcct_month());
				filter.put("partition_no", sdf.format(codeAcctMonth.getAct_eff_date()));
				filter.put("user_id", user_id);
		
				List<BilActAddUp> baaus = S.get(BilActAddUp.class).query(
						Condition.build("getBilActAddUp").filter(filter));

				try {
					accountProcess.accountProcess(user_id, baaus);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			
			/*<service data="com.tydic.beijing.billing.dao.CodeAcctMonth"
		type="jdbc">
		<query name="queryBySysdate">
			acct.codeacctmonth.queryBySysdate()
		</query>
		<query name="queryByAcctMonth">*/
			
//			for(LifeUserReleaseCal contract_inst_id : ListContractInstId){
//				
//				long time1 =  System.currentTimeMillis();
//				sum_balance = 0;
//				HlpSmsSend hlp_sms_send = new HlpSmsSend();
//				List<BalanceChangeRequest> ListBalanceChangeRequest = new ArrayList<BalanceChangeRequest>();
//				List<LogActTrade> ListLogActTrade = new ArrayList<LogActTrade>();
//				List<LogActTradeRelease> ListLogActTradeRelease = new ArrayList<LogActTradeRelease>();
//				List<LifeUserContractFrozen> ListLifeUserContractFrozen = new ArrayList<LifeUserContractFrozen>();
//				List<LifeUserReleaseCal> ListLifeUserReleaseCal = new ArrayList<LifeUserReleaseCal>();
//				log.debug("contract_inst_id.getContract_inst_id():"+contract_inst_id.getContract_inst_id());
//				ListLifeUserReleaseCal = db.GetLifeUserReleaseCal(contract_inst_id.getContract_inst_id()); //根据contractinstid获取返还明细
//				for(LifeUserReleaseCal lurc_balance : ListLifeUserReleaseCal){
//					sum_balance += lurc_balance.getBalance();
//				}
//				log.debug("sum_balance:"+sum_balance);
//				log.debug("instid"+contract_inst_id.getContract_inst_id()+"获得需返还记录数："+ListLifeUserReleaseCal.size());
//				for(LifeUserReleaseCal life_user_release_cal : ListLifeUserReleaseCal){
//					
//					addup_flag = null;
//					
//					log.debug("==GetLifeUserReleaseCal:"+life_user_release_cal.toString());
//					log.debug("life_user_release_cal:"+life_user_release_cal.toString());
//					for(CodeBilBalanceType code_bil_balance_type : ListCodeBilBalanceType){
//						if(code_bil_balance_type.getBalance_type_id() == life_user_release_cal.getBalance_type_id()){
//							addup_flag = code_bil_balance_type.getAddup_falg(); 
//							break;
//						}
//					}
//					if(addup_flag==null || addup_flag.length()<=0){
//						log.error("####balance_type_id:"+life_user_release_cal.getBalance_type_id()+", not in code_bil_balance_type");
//						life_user_release_cal.setProcess_state("2");
//						db.UpdateReleaseCalError(life_user_release_cal);
//						continue;
//					}
//					log.debug("balance_type_id:"+life_user_release_cal.getBalance_type_id()+", addup_flag:"+addup_flag);
//					
//					PayUserRel pay_user_rel = new PayUserRel();
//					pay_user_rel = db.GetPayUserRel(life_user_release_cal.getUser_id());   //TODO 考虑改为从Memcached获得  是否会出现多个有效支付关系？？？？
//					if(pay_user_rel==null){
//						log.error("####user_id:"+life_user_release_cal.getUser_id()+", not in pay_user_rel");
//						life_user_release_cal.setProcess_state("2");
//						db.UpdateReleaseCalError(life_user_release_cal);
//						continue;
//					}
//					//账本变更公共类入参
//					BalanceChangeRequest balanceChangeRequest = new BalanceChangeRequest();
//					BalanceChangeInfo balanceChangeInfo = new BalanceChangeInfo();
//					List<BalanceChangeInfo> ListBalanceChangeInfo = new ArrayList<BalanceChangeInfo>();
//					
//					balanceChangeRequest.setPayId(pay_user_rel.getPay_id());
//					balanceChangeRequest.setOperType("3");
//					balanceChangeRequest.setSerialNo("Contract"+life_user_release_cal.getUser_id()+"|"+life_user_release_cal.getFrozen_id()+"|"+now.get(Calendar.YEAR)+(now.get(Calendar.MONTH)+1)+now.get(Calendar.DAY_OF_MONTH));
//					balanceChangeRequest.setOperTime(sdf.format(new java.util.Date()));
//					balanceChangeRequest.setOperChannel("Contract");
////					for(RuleParameters rule_parameters : ListRuleParameters){
////						if(rule_parameters.getDomain_code() == 8000){
////							if(rule_parameters.getPara_name().equals("ChannelId")){
////								balanceChangeRequest.setOperChannel(rule_parameters.getPara_char2());
////							}
////							if(rule_parameters.getPara_name().equals("ExternalSystemId")){
////								balanceChangeRequest.setSystemId(rule_parameters.getPara_char2());
////							}
////							if(rule_parameters.getPara_name().equals("StaffId")){
////								balanceChangeRequest.setOperStaff(rule_parameters.getPara_char2());
////							}
////						}
////					}
//					
//					balanceChangeRequest.setOperChannel(channelId);
//					balanceChangeRequest.setSystemId(externalSystemId);
//				    balanceChangeRequest.setOperStaff(staffId);					
//					
//					//--1:可累加
//					if(addup_flag.equals("1")){
//						InfoPayBalance ipb_balance = db.IsBalanceIdExist(pay_user_rel.getPay_id(), life_user_release_cal.getEff_date(), life_user_release_cal.getExp_date(), life_user_release_cal.getBalance_type_id());
//						if(ipb_balance==null || ipb_balance.getBalance_id()==0){
//							balance_id = 0;
//						}else{
//							balance_id = ipb_balance.getBalance_id();
//						}
//					}else{
//						balance_id = 0;
//					}
//					if(balance_id == 0){
//						InfoPayBalance info_pay_balance = new InfoPayBalance();
//						info_pay_balance = ipb.createNew(String.valueOf(pay_user_rel.getLatn_id()), pay_user_rel.getPay_id(), (int)life_user_release_cal.getBalance_type_id(), life_user_release_cal.getEff_date(), life_user_release_cal.getExp_date());
//						if(info_pay_balance==null){
//							life_user_release_cal.setProcess_state("2");
//							db.UpdateReleaseCalError(life_user_release_cal);
//							log.error("info_pay_balance is null");
//							continue;
//						}
//						balance_id = info_pay_balance.getBalance_id();
//					}
//					balanceChangeInfo.setBalanceId(balance_id);
//					balanceChangeInfo.setBalanceTypeId((int)life_user_release_cal.getBalance_type_id());
//					balanceChangeInfo.setChangeType("0");
//					balanceChangeInfo.setChangeValue(life_user_release_cal.getBalance());
//					ListBalanceChangeInfo.add(balanceChangeInfo);
//					balanceChangeRequest.setBalanceChangeInfo(ListBalanceChangeInfo);
//					log.debug("balanceChangeRequest:"+balanceChangeRequest.toString());
//					
//					LogActTradeRelease log_act_trade_release = new LogActTradeRelease();
//					//log_act_trade_release.setRelease_operate_id(String.valueOf(db.GetNextReleaseOperId()));
//					log_act_trade_release.setRelease_operate_id("Contract"+life_user_release_cal.getUser_id()+"|"+life_user_release_cal.getFrozen_id()+"|"+now.get(Calendar.YEAR)+(now.get(Calendar.MONTH)+1)+now.get(Calendar.DAY_OF_MONTH));
//					log_act_trade_release.setBalance_type_id(life_user_release_cal.getBalance_type_id());
//					log_act_trade_release.setUser_id(life_user_release_cal.getUser_id());
//					log_act_trade_release.setFrozen_id(life_user_release_cal.getFrozen_id());
//					log_act_trade_release.setRelease_balance(life_user_release_cal.getBalance());
//					log_act_trade_release.setPay_id(pay_user_rel.getPay_id());
//					log_act_trade_release.setBalance_id(balance_id);
//					log.debug("log_act_trade_release:"+log_act_trade_release.toString());
//					
//					LogActTrade log_act_trade = new LogActTrade();
//					log_act_trade.setTrade_id(log_act_trade_release.getRelease_operate_id());
//					log_act_trade.setTrade_type_code(3);
//					log_act_trade.setTrader_type("2");
//					log_act_trade.setUser_id(life_user_release_cal.getUser_id());
//					log_act_trade.setExternal_trade_id(log_act_trade_release.getRelease_operate_id());
////					for(RuleParameters rule_parameters : ListRuleParameters){
////						if(rule_parameters.getDomain_code() == 8000){
////							if(rule_parameters.getPara_name().equals("ExternalSystemId")){
////								log_act_trade.setExternal_system_id(rule_parameters.getPara_char2());
////							}
////							if(rule_parameters.getPara_name().equals("ChannelId")){
////								log_act_trade.setChannel_id(rule_parameters.getPara_char2());
////							}
////							if(rule_parameters.getPara_name().equals("StaffId")){
////								log_act_trade.setStaff_id(rule_parameters.getPara_char2());
////							}
////						}
////					}
//					
//					balanceChangeRequest.setOperChannel(channelId);
//					balanceChangeRequest.setSystemId(externalSystemId);
//				    balanceChangeRequest.setOperStaff(staffId);	
//					
//					
//					log.debug("log_act_trade:"+log_act_trade.toString());
//					
//					LifeUserContractFrozen life_user_contract_frozen = new LifeUserContractFrozen();
//					life_user_contract_frozen.setFrozen_id(life_user_release_cal.getFrozen_id());
//					life_user_contract_frozen.setBalance_type_id(life_user_release_cal.getBalance_type_id());
//					life_user_contract_frozen.setBalance(life_user_release_cal.getBalance());
//					life_user_contract_frozen.setUser_id(life_user_release_cal.getUser_id());
//					
//					
//					String text;
//					//String text = SmsText(life_user_release_cal.getRemark(), String.valueOf(life_user_release_cal.getBalance()), String.valueOf(GetUserBalance(pay_user_rel.getPay_id())));
//					if(life_user_release_cal.getRemark().equals("20002") || life_user_release_cal.getRemark().equals("20003")){
//						text = "|aoc.dic.contract20002|"+sum_balance/100;
//					}else{
//						text = "|aoc.dic.contract|"+sum_balance/100;
//					}
//					
//					InfoUser info_user  = new InfoUser();
//					info_user = db.QueryInfoUser(life_user_release_cal.getUser_id());   //TODO 查了一次infouser
//					if(info_user == null){
//						log.error("send sms, msisdn_receive is null, user_id:"+life_user_release_cal.getUser_id());
//						life_user_release_cal.setProcess_state("2");
//						db.UpdateReleaseCalError(life_user_release_cal);
//						continue;
//					}
//					
//					hlp_sms_send.setMsisdn_receive(info_user.getDevice_number());
//					hlp_sms_send.setMessage_text(text);
//					hlp_sms_send.setMsisdn_send("10023");
//					hlp_sms_send.setMsg_id(String.valueOf(db.GetNextMsgId()));
//					log.debug("==msg_id:"+hlp_sms_send.getMsg_id());
//					ListBalanceChangeRequest.add(balanceChangeRequest);
//					ListLogActTrade.add(log_act_trade);
//					ListLogActTradeRelease.add(log_act_trade_release);
//					ListLifeUserContractFrozen.add(life_user_contract_frozen);
//					//--数据库更新插入操作
//					//db.DoPresent(balanceChangeRequest, log_act_trade, log_act_trade_release, life_user_contract_frozen, life_user_release_cal, hlp_sms_send);
//					
//				}//-- end of for
//				log.debug("do balance");
//				
//				long time98 =  System.currentTimeMillis();
//				
//				log.debug("instid"+contract_inst_id.getContract_inst_id()+"数据准备耗时"+(time98-time1));
//				
//				if(hlp_sms_send != null && hlp_sms_send.getMsg_id() !=null && hlp_sms_send.getMsg_id().length()>0){
//					db.DoBalance(ListBalanceChangeRequest, ListLogActTrade, ListLogActTradeRelease, ListLifeUserContractFrozen, ListLifeUserReleaseCal, hlp_sms_send);
//				}
//				
//				long time99 =  System.currentTimeMillis();
//				log.debug("instid"+contract_inst_id.getContract_inst_id()+"总耗时"+(time99-time1));
//				
//			}
			
			
		}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public AccountProcess getAccountProcess() {
		return accountProcess;
	}

	public void setAccountProcess(AccountProcess accountProcess) {
		this.accountProcess = accountProcess;
	}
}
