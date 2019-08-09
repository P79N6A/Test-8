package com.tydic.beijing.billing.rating.dto.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.cyclerent.service.impl.CycleRentForRatingImpl;
import com.tydic.beijing.billing.dao.BalanceConsumeData;
import com.tydic.beijing.billing.dao.BalanceConsumeResource;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CurrentBillDto;
import com.tydic.beijing.billing.dao.HlpSmsSend;
import com.tydic.beijing.billing.dao.InfoPay;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.QUserReasonSend;
import com.tydic.beijing.billing.dao.RemainBalance;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.RuleSystemSwitch;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.beijing.billing.dto.BaseResponse;
import com.tydic.beijing.billing.rating.domain.CodeCountry;
import com.tydic.beijing.billing.rating.domain.CodeOfr;
import com.tydic.beijing.billing.rating.domain.CodeRatableResource;
import com.tydic.beijing.billing.rating.domain.CodeRecordType;
import com.tydic.beijing.billing.rating.domain.CodeSpecialNbr;
import com.tydic.beijing.billing.rating.domain.CrmCodeOfr;
import com.tydic.beijing.billing.rating.domain.InfoRatableHistory;
import com.tydic.beijing.billing.rating.domain.RatingDeductLog;
import com.tydic.beijing.billing.rating.domain.RatingErrorCode;
import com.tydic.beijing.billing.rating.domain.RatingException;
import com.tydic.beijing.billing.rating.domain.RuleAutoRenew;
import com.tydic.beijing.billing.rating.domain.RuleDinnerSelect;
import com.tydic.beijing.billing.rating.domain.RuleEventTypeTree;
import com.tydic.beijing.billing.rating.domain.RuleEvtPricingStrategy;
import com.tydic.beijing.billing.rating.domain.RuleFormula;
import com.tydic.beijing.billing.rating.domain.RuleGroupFavMode;
import com.tydic.beijing.billing.rating.domain.RuleHoliday;
import com.tydic.beijing.billing.rating.domain.RuleOfrGroup;
import com.tydic.beijing.billing.rating.domain.RuleOfrResourceRel;
import com.tydic.beijing.billing.rating.domain.RuleOfrSplit;
import com.tydic.beijing.billing.rating.domain.RulePricingSection;
import com.tydic.beijing.billing.rating.domain.RulePricingSectionDisct;
import com.tydic.beijing.billing.rating.domain.RuleRatableCond;
import com.tydic.beijing.billing.rating.domain.RuleRateCondition;
import com.tydic.beijing.billing.rating.domain.RuleSectionRule;
import com.tydic.beijing.billing.rating.domain.RuleStrategySectRel;
import com.tydic.beijing.billing.rating.domain.RuleTariff;
import com.tydic.beijing.billing.rating.domain.SessionInformationExt;
import com.tydic.beijing.billing.rating.domain.TbDeductRecordHistory;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class DbUtilImpl implements DbUtil{
	
	private static final Logger log = Logger.getLogger(DbUtilImpl.class);

//	@Override
//	public List<RuleOfrResourceRel> getSumCode(String msgType) {
//		List<RuleOfrResourceRel> sumCode=S.get(RuleOfrResourceRel.class).query(Condition.build("getSumCode").filter("msgType", msgType));
//		
//		return sumCode;
//	}

	@Override
	public List<CodeRatableResource> getSumInfo() {
		List<CodeRatableResource> sumInfo=S.get(CodeRatableResource.class).query(Condition.build("getSumInfo"));

		return sumInfo;	
	}
	
	@Override
	public  List<RuleDinnerSelect> getAllAtomDinners(String dinnerId,int msgType) {
		List<RuleDinnerSelect> selects=null;
		selects=S.get(RuleDinnerSelect.class).query(Condition.build("getAllAtomDinners").filter("dinnerId", dinnerId).filter("msgType",msgType));
		return selects;
	}
	
	@Override
	public List<CodeOfr> getAllDinnerInfo() {
		List<CodeOfr> dinnerInfos=null;
		dinnerInfos=S.get(CodeOfr.class).query(Condition.build("getAllDinnerInfo"));
		return dinnerInfos;
	}
	
	@Override
	public List<CodeOfr> getAllCodeOfr() {
		List<CodeOfr>  listofCodeOfr=S.get(CodeOfr.class).query(Condition.build("getAllCodeOfr"));
		return listofCodeOfr;
	}
	
	
	@Override
	public RuleSectionRule getRuleSectionRule(long pricingSection) {
		// TODO Auto-generated method stub
		RuleSectionRule ruleSectionRule = S.get(RuleSectionRule.class).queryFirst(Condition.build("getRuleSectionRule").filter("pricing_section", pricingSection));
		return ruleSectionRule;
	}

	@Override
	public List<RuleSectionRule> getSonRuleSectionRule(long upperPricingSection){
		List<RuleSectionRule> sonRule = S.get(RuleSectionRule.class).query(Condition.build("getSonRuleSectionRule").filter("upper_pricing_section", upperPricingSection ));
		return sonRule;
	}

	@Override
	public List<LifeUserProduct> getUserProduct(String userId) {
		
		List<LifeUserProduct> listLifeUserProduct = new ArrayList<LifeUserProduct>();
		List<LifeUserProduct> listLifeUserProductForRate = new ArrayList<LifeUserProduct>();
		listLifeUserProduct =  S.get(LifeUserProduct.class).query(Condition.build("getUserProduct").filter("userId",userId));
		
		////所有的套餐ID都改为B套餐
		for(LifeUserProduct lifeUserProduct :listLifeUserProduct){
			
			RuleOfrSplit ruleOfrSplit = getOfrB(lifeUserProduct.getOfr_id());
			if(ruleOfrSplit != null && ruleOfrSplit.getOfr_b_id() !=null && ruleOfrSplit.getOfr_b_id().length() >0){
				lifeUserProduct.setOfr_id(ruleOfrSplit.getOfr_b_id());
			}
			
			listLifeUserProductForRate.add(lifeUserProduct);
		}
		
		return listLifeUserProductForRate;
	}
	
//	@Override
//	public String getSumValue(String resourceCode, String ownerType,
//			long ownerId, int latnId, int acctMonth) {
//		
//		List<InfoRatableHistory> value= S.get(InfoRatableHistory.class).query(Condition.build("getSumValue").filter("resourceCode", resourceCode).filter("ownerType", ownerType).filter("ownerId", ownerId).filter("latnId",latnId).filter("acctMonth", acctMonth));
//		Iterator<InfoRatableHistory> it=value.iterator();
//		if(it.hasNext()){
//			return it.next().getValue();
//		}else{
//			return null;
//		}
//	}
	
//	@Override
//	public CodeOfr getDinnerInfoByDinnerId(String dinnerId) {
//		CodeOfr dinnerInfo=S.get(CodeOfr.class).get(dinnerId);
//		return dinnerInfo;
//	}
	
	@Override
	public RuleEvtPricingStrategy getStrategyInfo(String pricingPlanId,String eventTypeId) {
		RuleEvtPricingStrategy strategy=S.get(RuleEvtPricingStrategy.class).queryFirst(Condition.build("getStrategyInfo").filter("pricingPlanId",pricingPlanId).filter("eventTypeId", eventTypeId));
		return strategy;
	}
	
//	@Override
//	public List<RuleOfrGroup> getGroupInfoByMsgType(String msgType) {
//		List<RuleOfrGroup> groupInfo=S.get(RuleOfrGroup.class).query(Condition.build("getGroupInfoByMsgType").filter("msgType", msgType));
//		
//		return groupInfo;
//	}
	
	@Override
	public List<RuleGroupFavMode> getAllGroupFav() {
		List<RuleGroupFavMode> groupFav=null;
		groupFav=S.get(RuleGroupFavMode.class).query(Condition.build("getAllGroupFav"));
		return groupFav;
	}
	
	@Override
	public RuleFormula getFormulaByMsgType(int msgType) {
		RuleFormula formula=S.get(RuleFormula.class).get(msgType);
		return formula;
	}
	
	
	@Override
	public List<InfoRatableHistory> getAllRatableValue() {
		List<InfoRatableHistory> historyInfo=null;
		historyInfo=S.get(InfoRatableHistory.class).query(Condition.build("getAllRatableValue"));
		return historyInfo;
	}
	
	
	@Override
	public List<RuleStrategySectRel> getAllStrategySecRels() {
		List<RuleStrategySectRel> rels=null;
		rels=S.get(RuleStrategySectRel.class).query(Condition.build("getAllStrategySecRels"));
		
		return rels;
	}
	
	
	@Override
	public List<RuleStrategySectRel> getRuleStrategySectRel(long strategyId) {
		// TODO Auto-generated method stub
		List<RuleStrategySectRel> listruleStrategySectRel = S.get(RuleStrategySectRel.class).query(Condition.build("getStrategySecRels").filter("strategy_id",strategyId));
		return listruleStrategySectRel;
	}
	
	

	@Override
	public RulePricingSection getRulePricingSection(long pricingSection) {
		// TODO Auto-generated method stub
		RulePricingSection rulePricingSection = S.get(RulePricingSection.class).queryFirst(Condition.build("getRulePricingSection").filter("pricing_section",pricingSection));
		return rulePricingSection;
	}

	@Override
	public List<RulePricingSection> getAllPricingSections() {
		List<RulePricingSection> sections=null;
		sections=S.get(RulePricingSection.class).query(Condition.build("getAllPricingSections"));
		
		return sections;
	}
	
	
	@Override
	public List<RuleSectionRule> getAllSectionRules() {
		List<RuleSectionRule> rules=null;
		rules=S.get(RuleSectionRule.class).query(Condition.build("getAllSectionRules"));
		
		return rules;
	}
	
	@Override
	public InfoUser getUserInfo(String deviceNumber) {
		InfoUser infoUser = S.get(InfoUser.class).queryFirst(Condition.build("getUserbyDeviceNumber").filter("device_number", deviceNumber));
		return infoUser;
	}
	
	
	@Override
	public List<RuleTariff> getAllTariff() {
		List<RuleTariff> tariffs=S.get(RuleTariff.class).query(Condition.build("getAllTariff"));
		return tariffs;
	}
	
	
	

	@Override
	public RuleOfrSplit getOfrB(String ofrId) {
		// TODO Auto-generated method stub

        RuleOfrSplit ruleOfrSplit =  S.get(RuleOfrSplit.class).queryFirst(Condition.build("getRuleOfrSplit").filter("ofr_id", ofrId));
		return ruleOfrSplit;
	}
	
	
	@Override
	public List<RuleHoliday> getAllHolidays() {
		List<RuleHoliday> holidays=null;
		holidays=S.get(RuleHoliday.class).query(Condition.build("getAllHolidays"));
		return holidays;
	}
	
	@Override
	public List<RuleEvtPricingStrategy> getAllStrategy() {
		List<RuleEvtPricingStrategy> strategy=null;
		strategy=S.get(RuleEvtPricingStrategy.class).query(Condition.build("getAllStrategy"));
		return strategy;
	}
	
	
	@Override
	public List<RuleOfrGroup> getAllGroupInfo() {
		List<RuleOfrGroup> groups=null;
		groups=S.get(RuleOfrGroup.class).query(Condition.build("getAllGroupInfo"));
		return groups;
	}
	
	@Override
	public List<RuleOfrResourceRel> getAllResourceRels() {
		List<RuleOfrResourceRel> rels=null;
		rels=S.get(RuleOfrResourceRel.class).query(Condition.build("getAllResourceRels"));
		
		return rels;
	}

	@Override
	public RuleEventTypeTree getRootEventTree(long operListId) {
      
		long starttime =System.currentTimeMillis();
		RuleEventTypeTree eventTreeRoot =  S.get(RuleEventTypeTree.class).queryFirst(Condition.build("getRootEventTree").filter("oper_list_id",operListId).filter("up_event_type_rule_tree_id",-1));
		long endtime =System.currentTimeMillis();
		log.info("获取事件码根节点，耗时"+(endtime-starttime));
		return eventTreeRoot;
	}

	@Override
	public List<RuleEventTypeTree> getEventTreebyFather(long fathertreeid) {
		long starttime =System.currentTimeMillis();
		List<RuleEventTypeTree> eventTreeList =  S.get(RuleEventTypeTree.class).query(Condition.build("getRootEventTreebyFather").filter("up_event_type_rule_tree_id",fathertreeid));
		long endtime =System.currentTimeMillis();
		log.info("根据父节点获取事件树子节点，耗时"+(endtime-starttime));
		return eventTreeList;
	}
	
//	@Override
//	public List<RuleEventTypeTree> getAllRuleEventTypeTree(long fathereventid) {
//		List<RuleEventTypeTree> eventTreeList =  S.get(RuleEventTypeTree.class).query(Condition.build("getAllRuleEventTypeTree").filter("event_type_rule_tree_id",fathereventid));
//		
//		return eventTreeList;
//	}
	
	@Override
	public List<RuleEventTypeTree> getAllRuleEventTypeTree() {
		List<RuleEventTypeTree> eventTreeList =  S.get(RuleEventTypeTree.class).query(Condition.build("getAllRuleEventTypeTree"));
		
		return eventTreeList;
	}
	 
	public List<RuleRateCondition> getRuleRateCondition( long condid ){
		List<RuleRateCondition> listRuleRateCondition = S.get(RuleRateCondition.class).query(Condition.build("getRuleRateCondition").filter("cond_id",condid));
		return listRuleRateCondition;
	}
	
	public List<RuleRateCondition> getAllRuleRateCondition(){
		List<RuleRateCondition> listRuleRateCondition = S.get(RuleRateCondition.class).query(Condition.build("getAllRuleRateCondition"));
		return listRuleRateCondition;
	}
	
	/**
	 * @author sung
	 *
	 * @return
	 */
	@Override
	public List<RulePricingSectionDisct> getAllSectionDiscount() {
		List<RulePricingSectionDisct> discounts=null;
		discounts=S.get(RulePricingSectionDisct.class).query(Condition.build("getAllSectionDiscount"));
		return discounts;
	}

	@Override
	public CodeRatableResource getCodeRatableResource(String ratableResourceCode) {
		CodeRatableResource codeRatableResource = S.get(CodeRatableResource.class).queryFirst(Condition.build("getCodeRatableResource").filter("ratable_resource_code",ratableResourceCode));

		return codeRatableResource;
	}
	
	
	/**
	 * @author sung
	 *
	 * @param sessionId
	 * @return
	 */
	@Override
	public SessionInformationExt getSessionInformationBySessionId(
			long sessionId) {
		
		SessionInformationExt list=null;
		list= S.get(SessionInformationExt.class).get(sessionId);

		return list;
	}
	
	/**
	 * @author sung
	 *
	 * @param id
	 * @return
	 */
	@Override
	public List<TbDeductRecordHistory> getDeductHistoryBySmid(String id) {
		List<TbDeductRecordHistory> record=null;
		record= S.get(TbDeductRecordHistory.class).query(Condition.build("getDeductHistoryBySmid").filter("smId", id));
		return record;
	}
	
	
	/**
	 * @author sung
	 *
	 * @param code
	 * @return
	 */
	@Override
	public CodeRatableResource getRatableResourceAttr(String code) {
		CodeRatableResource attr=null;
		attr=S.get(CodeRatableResource.class).get(code);
		
		return attr;
	}
	
	/**
	 * @author sung
	 *
	 * @return
	 */
	@Override
	public List<RuleRatableCond> getRatableConds() {
		List<RuleRatableCond> list=null;
		list=S.get(RuleRatableCond.class).query(Condition.build("getRatableConds"));
		return list;
	}
	
	/**
	 * @author sung
	 *
	 * @return
	 */
	@Override
	public List<CodeRecordType> getAllCodeRecordType() {
		List<CodeRecordType>  list=null;
		list=S.get(CodeRecordType.class).query(Condition.build("getAllCodeRecordType"));
		
		return list;
	}

	@Override
	public CodeAcctMonth getCodeAcctMonth(Date date) {
       CodeAcctMonth codeAcctMonth = 
    		   S.get(CodeAcctMonth.class).queryFirst(Condition.build("getCodeAcctMonth").filter("bil_eff_date", date).filter("bil_exp_date",date));

		
		return codeAcctMonth;
	}


	@Override
	public int updateInfoPayBalance(long balanceId, long realBalance,long usedBalance) {

		InfoPayBalance infoPayBalance = new InfoPayBalance();
		infoPayBalance.setBalance_id(balanceId);
		//infoPayBalance.setPay_id(payId);
		infoPayBalance.setReal_balance(realBalance);
		infoPayBalance.setUsed_balance(usedBalance);
		int n =0;

		n = S.get(InfoPayBalance.class).update(infoPayBalance);
	
		return n;
	}
	
	
	public void createInfoPayBalance(InfoPayBalance infoPayBalance) {

		S.get(InfoPayBalance.class).create(infoPayBalance);
	
	}
	
	public void createBilActAccesslog(BilActAccesslog baa){
		S.get(BilActAccesslog.class).create(baa);
	}
	
	
	public long getBalanceId() {
		Sequences s = S.get(Sequences.class).queryFirst(
				Condition.build("queryBalanceId"));
		return s.getSeq();
	}

	@Override
	public List<RuleTariff> getAllTariffbyTariffId(int tariffId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RuleDinnerSelect> getAllRuleDinnerSelect() {
		return S.get(RuleDinnerSelect.class).query(Condition.build("getAllRuleDinnerSelect"));
	}

	@Override
	public List<RuleFormula> getAllRuleFormula() {
		return S.get(RuleFormula.class).query(Condition.build("getAllRuleFormula"));
	}
	
	
	@Override
	public List<SessionInformationExt> getSessionInfoById(String sessionId) {
		List<SessionInformationExt> ret=null;
		ret=S.get(SessionInformationExt.class).query(Condition.build("getSessionInfoById").filter("sessionId", sessionId));
		return ret;
	}
	@Override
	public List<CodeAcctMonth> getAllCodeAcctMonth() {
		return S.get(CodeAcctMonth.class).query(Condition.build("getAllCodeAcctMonth"));
		
	}
	
	
//	@Override
//	public LifeUserPay getAcctPayInfoById(String userId) {
//		LifeUserPay  pay=S.get(LifeUserPay.class).get(userId);
//		
//		return pay;
//	}
	
	
	@Override
	public InfoPay getAcctInfoById(String payId) {
		InfoPay pay=S.get(InfoPay.class).get(payId);
		return pay;
	}

	@Override
	public List<RuleOfrSplit> getallRuleOfrSplit() {
		return S.get(RuleOfrSplit.class).query(Condition.build("getAllRuleOfrSplit"));
	}
	
	
	@Override
	public CodeCountry getCodeCountryInfo(String areaCode) {
		return S.get(CodeCountry.class).get(areaCode);
		
		
	}
	
	@Override
	public List<CodeCountry> getAllCodeCountry(){
		return S.get(CodeCountry.class).query(Condition.build("getAllCodeCountry"));
	}
	
	
	/**
	 * @author sung
	 *
	 * @param irh
	 */
	@Override
	public void addRatableHistory(InfoRatableHistory irh) {
		
		S.get(InfoRatableHistory.class).create(irh);
		
	}
	
	
	/**
	 * @author sung
	 *
	 * @param irh
	 */
	@Override
	public void updateRatableHistory(InfoRatableHistory irh) {
		S.get(InfoRatableHistory.class).update(irh);
		
		
	}

	@Override
	public List<CodeSpecialNbr> getAllCodeSpecialNbr() {
	
		return  S.get(CodeSpecialNbr.class).query(Condition.build("getAllCodeSpecialNbr"));
	}
	
	
	public InfoRatableHistory queryRatableHistory(InfoRatableHistory irh){
		
		return S.get(InfoRatableHistory.class).queryFirst(Condition.build("queryRatableHistory").filter("resource_code", irh.getResource_code()).filter("owner_type", irh.getOwner_type()).filter("owner_id", irh.getOwner_id()).filter("latn_id", irh.getLatn_id()).filter("acct_month", irh.getAcct_month()));
		

	}


	public List<RemainBalance> queryBalance(String pay_id ){
		return S.get(RemainBalance.class).query(Condition.build("queryBalance").filter("pay_id", pay_id));
	}
	
	
	@Override
	public CurrentBillDto queryCurrentBill(String pay_id, String month) {
		Map<String,Object> filter=new HashMap<String,Object>();
		filter.put("pay_id", pay_id);
		filter.put("month", month);
		return S.get(CurrentBillDto.class).queryFirst(Condition.build("queryCurrentBill").filter(filter));
		
	}
	
	
	@Override
	public List<BalanceConsumeData> queryBalanceFeeConsume(String payId, String month) {
		Map<String,Object> filter=new HashMap<String,Object>();
		filter.put("pay_id", payId);
		filter.put("month", month);
		return S.get(BalanceConsumeData.class).query(Condition.build("queryBalanceFeeConsume").filter(filter));
		
	}
	@Override
	public List<BalanceConsumeResource> queryBalanceConsume(String payId) {
		
		return S.get(BalanceConsumeResource.class).query(Condition.build("queryBalanceConsume").filter("pay_id", payId));
	}

	@Override
	public void createActivelog(QUserReasonSend qUserReasonSend ) {

		qUserReasonSend.setSerial_num(S.get(Sequences.class)
				.queryFirst(Condition.build("queryQReasonSn")).getSeq());

       S.get(QUserReasonSend.class).create(qUserReasonSend);
       
	}

	public void createActiveLogAndResource(QUserReasonSend qUserReasonSend,CycleRentForRatingImpl rent,boolean addRes) throws RatingException{
		createActivelog(qUserReasonSend);
		if(addRes){
		BaseResponse res = rent.doProcess(qUserReasonSend.getUser_no());
		String status = res.getStatus();
		if(status.equals("1")){
			log.debug("资源到账失败："+res.getErrorMessage());
			throw new RatingException(RatingErrorCode.ERR_ADD_RES,"资源到账失败");
		}
		log.debug("资源到账返回："+res.getStatus()+":"+res.getErrorCode()+"："+res.getErrorMessage());
		
		}
	}
	
	
	
	@Override
	public List<InfoUser> getInfoUserbyImsi(String deviceNumber) {
		
		List<InfoUser> infoUserList =  S.get(InfoUser.class).query(Condition.build("getInfoUserbyImsi").filter("device_number", deviceNumber));
		
		return infoUserList;
	}

	@Override
	public void createDeductLog(RatingDeductLog ratingDeductLog) {
		
		 S.get(RatingDeductLog.class).create(ratingDeductLog);
		
	}

	@Override
	public InfoPayBalance getInfoPayBalanceAfterUpdate(long balanceId) {
		InfoPayBalance infoPayBalance = new InfoPayBalance();
		infoPayBalance = S.get(InfoPayBalance.class).queryFirst(Condition.build("getInfoPayBalanceAfterUpdate").filter("balance_id",balanceId));
		return infoPayBalance;
	}

	@Override
	public List<InfoPayBalance> getSumResourceBalance(String payId,
			int balanceTypeId,String startTime) {
		
		Map<String,Object> filter=new HashMap<String,Object>();
		filter.put("pay_id", payId);
		filter.put("balanceTypeId", balanceTypeId);
		filter.put("start_Time",startTime);
		return S.get(InfoPayBalance.class).query(Condition.build("getSumResourceBalance").filter(filter)); 
	}

	@Override
	public void sendResourceOverSms(String chargedPhone, int msgType,String createTime,int balanceTypeId) {
		
		String msg ="";
		Calendar cal = Calendar.getInstance();
		if(msgType ==60) {
		
			if(balanceTypeId ==10){
				msg = "|aoc.dic.voiceafteruse";
			}else if (balanceTypeId == 5){
				msg = "|aoc.dic.localvoiceafteruse";
			}else if (balanceTypeId == 4) {
				msg = "|aoc.dic.cityvoiceafteruse";
			}
			
		}else if (msgType ==70){
			msg = "|aoc.dic.smsafteruse";
		}else if (msgType ==80){
			msg = "|aoc.dic.flowafteruse";
		}else{
			return;
		}
		
		if(msg==null || msg.length()==0){
			return ;
		}
		
		//msg= msg +"|" +cal.get(cal.YEAR)+"|"+(cal.get(cal.MONTH)+1)+"|"+cal.get(cal.DAY_OF_MONTH)+"|"+cal.get(cal.HOUR_OF_DAY)+"|"+cal.get(cal.MINUTE);
		msg= msg +"|" +(cal.get(cal.MONTH)+1)+"|"+cal.get(cal.DAY_OF_MONTH)+"|"+cal.get(cal.HOUR_OF_DAY)+"|"+cal.get(cal.MINUTE);
		
		HlpSmsSend sendHis = new HlpSmsSend();
		sendHis.setMsisdn_send("10023");
		sendHis.setMsisdn_receive(chargedPhone);
		sendHis.setMessage_text(msg);
		sendHis.setCreate_time(createTime);
	    S.get(HlpSmsSend.class).create(sendHis);
	}

	@Override
	public String selectAddResFlag() {
		RuleParameters  param = S.get(RuleParameters.class).queryFirst(Condition.build("addResOrNot"));
		if(param !=null)
			return param.getPara_char1();
		else
			return "";
	}
	
	public List<RuleSystemSwitch> getRuleSystemSwitch(){
		return S.get(RuleSystemSwitch.class).query(Condition.build("getRuleSystemSwitch"));
	}
	
	public List<RuleAutoRenew> getAllRenews(){
		return S.get(RuleAutoRenew.class).query(Condition.build("getAllRenews"));
	}
	
	public List<CrmCodeOfr> getAllCrmOfr(){
		return S.get(CrmCodeOfr.class).query(Condition.build("getAllCrmOfr"));
	}
	
	@Override
	public InfoPayBalance getInfoPayBalancebyTime(String payId,int balanceTypeId,String effDay) {
		InfoPayBalance infoPayBalance =  S.get(InfoPayBalance.class).queryFirst(Condition.build("getInfoPayBalancebyTime").filter("payId",payId)
				.filter("balanceTypeId", balanceTypeId).filter("effDay", effDay));
		return infoPayBalance;
	}
	
}
