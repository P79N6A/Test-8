package com.tydic.beijing.billing.account.biz;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LogActTrade;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.beijing.billing.dao.LifeUserContract;
import com.tydic.beijing.billing.dao.LifeUserContractFrozen;
import com.tydic.beijing.billing.dao.LifeUserReleaseCal;
import com.tydic.beijing.billing.dao.LogActTradeRelease;
import com.tydic.beijing.billing.dao.QPresentUserInfo;
import com.tydic.beijing.billing.dao.QPresentUserInfoHis;
import com.tydic.beijing.billing.dao.RuleContract;
import com.tydic.beijing.billing.dao.RuleResourceRelease;
import com.tydic.beijing.billing.dto.BalanceChangeRequest;
import com.tydic.beijing.billing.dto.BalanceChangeResponse;
import com.tydic.beijing.billing.sms.dao.HlpSmsSend;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class ContractDb {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(ContractDb.class);
	private BalanceChangeOps bco = new BalanceChangeOps();
	
	public BalanceChangeOps getBco() {
		return bco;
	}

	public void setBco(BalanceChangeOps bco) {
		this.bco = bco;
	}

	public List<QPresentUserInfo> GetPresentUser() throws Exception{
		log.debug("in GetPresentUser");
		List<QPresentUserInfo> ListPresentUser = new ArrayList<QPresentUserInfo>();
		ListPresentUser = S.get(QPresentUserInfo.class).query(Condition.build("getPresentUserList"));
		return ListPresentUser;
	}
	
	public RuleContract GetRuleContract(String contract_id) throws Exception{
		RuleContract rule_contract = new RuleContract();
		rule_contract = S.get(RuleContract.class).queryFirst(Condition.build("getRuleContract").filter("contract_id", contract_id));
		return rule_contract;
	}
	
	public List<RuleResourceRelease> GetRuleResourceRelease(String contract_id) throws Exception{
		List<RuleResourceRelease> ListRuleResourceRelease = new ArrayList<RuleResourceRelease>();
		ListRuleResourceRelease = S.get(RuleResourceRelease.class).query(Condition.build("getRuleResourceReleaseList").filter("contract_id", contract_id));
		return ListRuleResourceRelease;
	}
	
	public RuleResourceRelease QueryRuleResourceRelease(String contract_id) throws Exception{
		RuleResourceRelease rule_resource_release = new RuleResourceRelease();
		rule_resource_release = S.get(RuleResourceRelease.class).queryFirst(Condition.build("getRuleResourceReleaseList").filter("contract_id", contract_id));
		return rule_resource_release;
	}
	
	public long GetNextForzenId() throws Exception{
		Sequences s = S.get(Sequences.class).queryFirst(Condition.build("getNextForzenId"));
		return s.getSeq();
	}
	
	public long GetNextBalanceId() throws Exception{
		Sequences s = S.get(Sequences.class).queryFirst(Condition.build("queryBalanceId"));
		return s.getSeq();
	}
	
	public long GetNextMsgId() throws Exception{
		Sequences s = S.get(Sequences.class).queryFirst(Condition.build("queryMsgId"));
		return s.getSeq();
	}
	
	public void UpdateQPresentInfoState(QPresentUserInfo q_present_user_info) throws Exception{
		S.get(QPresentUserInfo.class).update(q_present_user_info);
	}
	
	public void DealInstPresent(QPresentUserInfo q_present_user_info, QPresentUserInfoHis q_present_user_info_his, LifeUserContract life_user_contract, LifeUserContractFrozen life_user_contract_forzen, List<LifeUserReleaseCal> ListLifeUserReleaseCal) throws Exception{
		S.get(LifeUserContract.class).create(life_user_contract);
		S.get(LifeUserContractFrozen.class).create(life_user_contract_forzen);
		for(LifeUserReleaseCal life_user_release_cal : ListLifeUserReleaseCal){
			S.get(LifeUserReleaseCal.class).create(life_user_release_cal);
		}
		S.get(QPresentUserInfoHis.class).create(q_present_user_info_his);
		S.get(QPresentUserInfo.class).batch(Condition.build("delQPresentUserInfo").filter("user_id", q_present_user_info.getUser_id()), new QPresentUserInfo());
	}
	
	public List<LifeUserReleaseCal> GetLifeUserReleaseCal(String contract_inst_id) throws Exception{
		List<LifeUserReleaseCal> ListLifeUserReleaseCal = new ArrayList<LifeUserReleaseCal>();
		ListLifeUserReleaseCal = S.get(LifeUserReleaseCal.class).query(Condition.build("GetLifeUserReleaseCal").filter("contract_inst_id", contract_inst_id));
		return ListLifeUserReleaseCal;
	}
	
	public List<LifeUserReleaseCal> GetUserNeedPresent(int mod, int partion) throws Exception{
		List<LifeUserReleaseCal> ListLifeUserReleaseCal = new ArrayList<LifeUserReleaseCal>();
		ListLifeUserReleaseCal = S.get(LifeUserReleaseCal.class).query(Condition.build("GetUserNeedPresentList").filter("mod", mod).filter("partion", partion));
		return ListLifeUserReleaseCal;
	}
	/*
	public List<LifeUserReleaseCal> GetUserNeedPresent() throws Exception{
		List<LifeUserReleaseCal> ListLifeUserReleaseCal = new ArrayList<LifeUserReleaseCal>();
		ListLifeUserReleaseCal = S.get(LifeUserReleaseCal.class).query(Condition.build("GetUserNeedPresentList"));
		return ListLifeUserReleaseCal;
	}
	*/
	
	
	public List<CodeBilBalanceType> GetCodeBilBalanceType() throws Exception{
		List<CodeBilBalanceType> ListCodeBilBalanceType = new ArrayList<CodeBilBalanceType>();
		ListCodeBilBalanceType = S.get(CodeBilBalanceType.class).query(Condition.build("getAll"));
		return ListCodeBilBalanceType;
	}
	
	public void UpdateReleaseCalError(LifeUserReleaseCal life_user_release_cal){
		S.get(LifeUserReleaseCal.class).update(life_user_release_cal);
	}
	
	public long GetNextReleaseOperId() throws Exception{
		Sequences s = S.get(Sequences.class).queryFirst(Condition.build("GetNextReleaseOperId"));
		return s.getSeq();
	}
	
	public PayUserRel GetPayUserRel(String user_id){
		PayUserRel pay_user_rel = new PayUserRel();
		pay_user_rel = S.get(PayUserRel.class).queryFirst(Condition.build("queryByUserId").filter("user_id", user_id));
		return pay_user_rel;
	}
	
	public List<RuleParameters> GetRuleParameters() throws Exception{
		List<RuleParameters> ListRuleParameters = new ArrayList<RuleParameters>();
		ListRuleParameters = S.get(RuleParameters.class).query(Condition.build("queryAll"));
		return ListRuleParameters;
	}
	
	public InfoPayBalance IsBalanceIdExist(String pay_id, Date eff_date, Date exp_date, long balance_type_id) throws Exception{
		InfoPayBalance info_pay_balance = new InfoPayBalance();
		info_pay_balance = S.get(InfoPayBalance.class).queryFirst(Condition.build("queryByEffAndExpDate").filter("pay_id", pay_id).filter("balance_type_id", balance_type_id).filter("eff_date", eff_date).filter("exp_date", exp_date));
		return info_pay_balance;
	}
	
	public List<InfoPayBalance> QueryInfoPayBalance(String pay_id){
		List<InfoPayBalance> ListInfoPayBalance = new ArrayList<InfoPayBalance>();
		ListInfoPayBalance = S.get(InfoPayBalance.class).query(Condition.build("QueryInfoPayBalance").filter("pay_id", pay_id));
		return ListInfoPayBalance;
	}
	
	public InfoUser QueryInfoUser(String user_id) throws Exception{
		InfoUser info_user = new InfoUser();
		info_user = S.get(InfoUser.class).queryFirst(Condition.build("queryByUserId").filter("user_id", user_id));
		return info_user;
	}
	
	public void DoPresent(BalanceChangeRequest balanceChangeRequest, LogActTrade log_act_trade, LogActTradeRelease log_act_trade_release, LifeUserContractFrozen life_user_contract_frozen, LifeUserReleaseCal life_user_release_cal, HlpSmsSend hlp_sms_send) throws BasicException, Exception{
		BalanceChangeResponse balanceChangeResponse = bco.doProcess(balanceChangeRequest, false, false);
		if(balanceChangeResponse==null || balanceChangeResponse.getStatus()==null || balanceChangeResponse.getStatus().length()<=0 || balanceChangeResponse.getStatus().equals("1")){
			life_user_release_cal.setProcess_state("2");
			UpdateReleaseCalError(life_user_release_cal);
			log.error("balanceChangeResponse getResultMessage:"+balanceChangeResponse.getResultMessage());
			return;
		}
		S.get(LogActTrade.class).create(log_act_trade);
		S.get(LogActTradeRelease.class).create(log_act_trade_release);
		S.get(LifeUserContractFrozen.class).update(life_user_contract_frozen);
		
		
		if(life_user_release_cal.getRemark().equals("10003") || life_user_release_cal.getRemark().equals("10002")){
			LifeUserReleaseCal life_user_release_cal1 = this.IsexistLifeUserReleaseCal(life_user_release_cal.getUser_id(), life_user_release_cal.getRemark());
			if(life_user_release_cal1==null){
				if(life_user_release_cal.getRemark().equals("10003")){
					hlp_sms_send.setMessage_text("|aoc.dic.contract|50");
				}else{
					hlp_sms_send.setMessage_text("|aoc.dic.contract|30");
				}
				S.get(HlpSmsSend.class).create(hlp_sms_send);
			}
		}else{
			S.get(HlpSmsSend.class).create(hlp_sms_send);
		}
		life_user_release_cal.setProcess_state("1");
		S.get(LifeUserReleaseCal.class).update(life_user_release_cal);
	}

	public void CloseContract(QPresentUserInfo q_present_user_info){
		
		LifeUserReleaseCal life_user_release_cal = new LifeUserReleaseCal();
		life_user_release_cal.setContract_inst_id(q_present_user_info.getPresent_user_id());
		life_user_release_cal.setProcess_state("3");
		//添加remark注释
		String remark="";
		if (q_present_user_info!=null && q_present_user_info.getServ_code()!=null && (!q_present_user_info.getServ_code().equals(""))) {
			remark=getremark(q_present_user_info.getServ_code());
			life_user_release_cal.setRemark(remark);
		}
		
		QPresentUserInfoHis q_present_user_info_his = new QPresentUserInfoHis();
		q_present_user_info_his.setEff_date(q_present_user_info.getEff_date());
		q_present_user_info_his.setExp_date(q_present_user_info.getExp_date());
		q_present_user_info_his.setOperate_type(q_present_user_info.getOperate_type());
		q_present_user_info_his.setPresent_user_id(q_present_user_info.getPresent_user_id());
		q_present_user_info_his.setProtocol_id(q_present_user_info.getProtocol_id());
		q_present_user_info_his.setResource_type(q_present_user_info.getResource_type());
		q_present_user_info_his.setResource_value(q_present_user_info.getResource_value());
		q_present_user_info_his.setServ_code(q_present_user_info.getServ_code());
		q_present_user_info_his.setUser_id(q_present_user_info.getUser_id());
		q_present_user_info_his.setState(q_present_user_info.getState());
		S.get(LifeUserReleaseCal.class).batch(Condition.build("UpdateLifeUserReleaseCalClose").filter("process_state", life_user_release_cal.getProcess_state()).filter("contract_inst_id", life_user_release_cal.getContract_inst_id()).filter("remark", remark));
		S.get(QPresentUserInfoHis.class).create(q_present_user_info_his);
		S.get(QPresentUserInfo.class).batch(Condition.build("delQPresentUserInfo").filter("user_id", q_present_user_info.getUser_id()), new QPresentUserInfo());
	}
	
	
	
	/**
	 * @param servCode 服务编码
	 * @return 描述
	 */
	public String getremark(String servCode){
		String remark="";
		if (servCode.equals("027")) {
			remark="撤单";
		}else if (servCode.equals("016")) {
			remark="销户";
		}else if (servCode.equals("019")) {
			remark="返销";
		}else if (servCode.equals("B44")){
			remark="批销";
		}else if (servCode.equals("G21")){
			remark="后台欠费拆机";
		}else {
			
		}
		return remark;
	}
	
	
	public LifeUserReleaseCal IsexistLifeUserReleaseCal(String user_id, String contract_id){
		LifeUserReleaseCal life_user_release_cal = new LifeUserReleaseCal();
		life_user_release_cal = S.get(LifeUserReleaseCal.class).queryFirst(Condition.build("IsexistLifeUserReleaseCal").filter("user_id", user_id).filter("contract_id", contract_id));
		return life_user_release_cal;
	}
	
	public  List<QPresentUserInfo> GetPresentUserId(){
		List<QPresentUserInfo> ListPresentUserId = new ArrayList<QPresentUserInfo>();
		ListPresentUserId = S.get(QPresentUserInfo.class).query(Condition.build("GetPresentUserId"));
		return ListPresentUserId;
	}
	
	public  List<QPresentUserInfo> GetQPresentUserInfo(String present_user_id){
		List<QPresentUserInfo> ListQPresentUserInfo = new ArrayList<QPresentUserInfo>();
		ListQPresentUserInfo = S.get(QPresentUserInfo.class).query(Condition.build("GetQPresentUserInfo").filter("present_user_id", present_user_id));
		return ListQPresentUserInfo;
	}
	

	public void DealInst(List<QPresentUserInfo> ListQPresentUserInfo, List<QPresentUserInfoHis> ListQPresentUserInfoHis, LifeUserContract life_user_contract, List<LifeUserContractFrozen> ListLifeUserContractFrozen, List<LifeUserReleaseCal> ListLifeUserReleaseCal) throws Exception{
		S.get(LifeUserContract.class).create(life_user_contract);
		for(LifeUserContractFrozen life_user_contract_forzen : ListLifeUserContractFrozen){
			S.get(LifeUserContractFrozen.class).create(life_user_contract_forzen);
		}
		for(LifeUserReleaseCal life_user_release_cal : ListLifeUserReleaseCal){
			S.get(LifeUserReleaseCal.class).create(life_user_release_cal);
		}
		for(QPresentUserInfoHis q_present_user_info_his : ListQPresentUserInfoHis){
			S.get(QPresentUserInfoHis.class).create(q_present_user_info_his);
		}
		for(QPresentUserInfo q_present_user_info : ListQPresentUserInfo){
			S.get(QPresentUserInfo.class).batch(Condition.build("delQPresentUserInfo").filter("present_user_id", q_present_user_info.getPresent_user_id()), new QPresentUserInfo());
		}
	}
	

	public void DoBalance(List<BalanceChangeRequest> ListBalanceChangeRequest, List<LogActTrade> ListLogActTrade, List<LogActTradeRelease> ListLogActTradeRelease, List<LifeUserContractFrozen> ListLifeUserContractFrozen, List<LifeUserReleaseCal> ListLifeUserReleaseCal, HlpSmsSend hlp_sms_send) throws BasicException, Exception{
		for(BalanceChangeRequest balanceChangeRequest : ListBalanceChangeRequest){
			BalanceChangeResponse balanceChangeResponse = bco.doProcess(balanceChangeRequest, false, false);
			if(balanceChangeResponse==null || balanceChangeResponse.getStatus()==null || balanceChangeResponse.getStatus().length()<=0 || balanceChangeResponse.getStatus().equals("1")){
				log.error("balanceChangeResponse getResultMessage:"+balanceChangeResponse.getResultMessage());
				return;
			}
		}
		for(LogActTrade log_act_trade:ListLogActTrade){
			S.get(LogActTrade.class).create(log_act_trade);
		}
		for(LogActTradeRelease log_act_trade_release : ListLogActTradeRelease){
			S.get(LogActTradeRelease.class).create(log_act_trade_release);
		}
		for(LifeUserContractFrozen life_user_contract_frozen : ListLifeUserContractFrozen){
			S.get(LifeUserContractFrozen.class).update(life_user_contract_frozen);
		}
		for(LifeUserReleaseCal life_user_release_cal : ListLifeUserReleaseCal){
			life_user_release_cal.setProcess_state("1");
			S.get(LifeUserReleaseCal.class).update(life_user_release_cal);
		}
		S.get(HlpSmsSend.class).create(hlp_sms_send);
	}
	
	public LifeUserContract getByUIDAndCIID(String user_id,String contract_inst_id){
		return S.get(LifeUserContract.class).queryFirst(Condition.build("queryByUIDAndCIID").filter("user_id",user_id).
				filter("contract_inst_id", contract_inst_id));
	}
	
}
