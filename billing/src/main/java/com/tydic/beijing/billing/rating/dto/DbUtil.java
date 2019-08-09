package com.tydic.beijing.billing.rating.dto;

import java.util.Date;
import java.util.List;

import com.tydic.beijing.billing.cyclerent.service.impl.CycleRentForRatingImpl;
import com.tydic.beijing.billing.dao.BalanceConsumeData;
import com.tydic.beijing.billing.dao.BalanceConsumeResource;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CurrentBillDto;
import com.tydic.beijing.billing.dao.InfoPay;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.QUserReasonSend;
import com.tydic.beijing.billing.dao.RemainBalance;
import com.tydic.beijing.billing.dao.RuleSystemSwitch;
import com.tydic.beijing.billing.rating.domain.CodeCountry;
import com.tydic.beijing.billing.rating.domain.CodeOfr;
import com.tydic.beijing.billing.rating.domain.CodeRatableResource;
import com.tydic.beijing.billing.rating.domain.CodeRecordType;
import com.tydic.beijing.billing.rating.domain.CodeSpecialNbr;
import com.tydic.beijing.billing.rating.domain.CrmCodeOfr;
import com.tydic.beijing.billing.rating.domain.InfoRatableHistory;
import com.tydic.beijing.billing.rating.domain.RatingDeductLog;
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

public interface DbUtil {

	/*
	 * 套餐累计量关系表
	 * 根据套餐标识+消息类型查找累计量ID
	 */
//	public List<RuleOfrResourceRel> getSumCode(String msgType);
	
	/*
	 * 累计量定义表
	 * 根据累计量ID查询累计量信息
	 */
	public List<CodeRatableResource> getSumInfo();
	
	public CodeRatableResource getCodeRatableResource(String ratableResourceCode);
	
	/*
	 * 获取原子套餐
	 */
	public  List<RuleDinnerSelect> getAllAtomDinners(String dinnerId,int msgType);
	
	public List<RuleDinnerSelect> getAllRuleDinnerSelect();
	
	
	public List<LifeUserProduct> getUserProduct(String userId);
	
//	public String getSumValue(String resourceCode,String ownerType,long ownerId,int latnId,int acctMonth);
	
	//根据原子套餐标识查询套餐定义表
//	public CodeOfr getDinnerInfoByDinnerId(String dinnerId);
	//根据定价计划及事件类型获取策略
	public RuleEvtPricingStrategy getStrategyInfo(String pricingPlanId,String eventTypeId);
	//根据消息类型获取所有套餐分组信息
//	public List<RuleOfrGroup> getGroupInfoByMsgType(String msgType);
	//获取所有组优惠信息
	public List<RuleGroupFavMode> getAllGroupFav();
	//根据消息类型获取业务公式
	public RuleFormula getFormulaByMsgType(int msgType);
	public List<RuleFormula> getAllRuleFormula();
	//获取所有累积量历史信息
	public List<InfoRatableHistory> getAllRatableValue();
	//获取所有策略段落关系
	public List<RuleStrategySectRel> getAllStrategySecRels();
	//根据策略id获取其策略段落关系
	public List<RuleStrategySectRel> getRuleStrategySectRel(long strategyId);
	
	//获取所有段落定义信息
	public List<RulePricingSection> getAllPricingSections();
	//根据pricingsectionid获取段落信息
	public RulePricingSection getRulePricingSection(long pricingSection);
	//获取所有段落规则
	public List<RuleSectionRule> getAllSectionRules();
	
	public RuleSectionRule getRuleSectionRule(long pricingSection);
	public List<RuleSectionRule> getSonRuleSectionRule(long upperPricingSection);
	
	//获取所有费率
	public List<RuleTariff> getAllTariff();
	public List<RuleTariff> getAllTariffbyTariffId(int tariffId);
	//获取用户
	public InfoUser getUserInfo(String deviceNumber);
	//获取B套餐
	public RuleOfrSplit getOfrB(String ofrId);
	//获取所有节假日信息
	public List<RuleHoliday> getAllHolidays();
	//获取所有原子套餐定义信息
	public List<CodeOfr>getAllDinnerInfo();
	//获取所有策略关系
	public List<RuleEvtPricingStrategy> getAllStrategy();
	//获取所有产品组信息
	public List<RuleOfrGroup> getAllGroupInfo();
	//获取所有累积量关系
	public List<RuleOfrResourceRel> getAllResourceRels();
	
	public RuleEventTypeTree getRootEventTree(long operListId);
	
	public List<RuleEventTypeTree> getEventTreebyFather(long fathertreeid);
	
	//public List<RuleEventTypeTree> getAllRuleEventTypeTree(long fathereventid);
	public List<RuleEventTypeTree> getAllRuleEventTypeTree();
	public List<RuleRateCondition> getRuleRateCondition(long condid);
	public List<RuleRateCondition> getAllRuleRateCondition();

	
	//获取所有折扣信息
	public List<RulePricingSectionDisct> getAllSectionDiscount();
	
	//获取session信息
	
	public SessionInformationExt getSessionInformationBySessionId(long sessionId);
	
	//获取扣费历史
	public List<TbDeductRecordHistory> getDeductHistoryBySmid(String smId);
	//获取累积量属性
	public CodeRatableResource getRatableResourceAttr(String code);
	//获取累积量条件
	public List<RuleRatableCond> getRatableConds();
	//获取所有记录类型代码
	public List<CodeRecordType> getAllCodeRecordType();
	//获取账期
	public CodeAcctMonth getCodeAcctMonth(Date date);
	
	/**
	 * 销账处理
	 * @param balanceId 账本
	 * @param value 操作值
	 * @param operationType 操作类型 1 预占 2销账
	 * @return
	 */
	public int updateInfoPayBalance( long balanceId, long realBalance,long usedBalance);
	
	
	
	//获取session信息
	public List<SessionInformationExt> getSessionInfoById(String sessionId);
	public List<CodeAcctMonth> getAllCodeAcctMonth();
	//public LifeUserPay getAcctPayInfoById(String userId);
	public InfoPay   getAcctInfoById(String payId);

	public List<RuleOfrSplit> getallRuleOfrSplit();
	public List<CodeOfr> getAllCodeOfr();
	public CodeCountry getCodeCountryInfo(String areaCode);
	
	
	public void addRatableHistory(InfoRatableHistory irh);
	
	public void updateRatableHistory(InfoRatableHistory irh);

	public List<CodeCountry> getAllCodeCountry();
	
	public List<CodeSpecialNbr> getAllCodeSpecialNbr();
	
	public InfoRatableHistory queryRatableHistory(InfoRatableHistory irh);
	public List<RemainBalance> queryBalance(String pay_id);
	public CurrentBillDto queryCurrentBill(String pay_id,String month);
	
	public List<BalanceConsumeData> queryBalanceFeeConsume(String payId,String month);
	public List<BalanceConsumeResource> queryBalanceConsume(String payId);

	public void createActivelog(QUserReasonSend qUserReasonSend );
	public void createActiveLogAndResource(QUserReasonSend qUserReasonSend,CycleRentForRatingImpl rent,boolean addRes)throws RatingException;
	public List<InfoUser> getInfoUserbyImsi(String string);

	public void createDeductLog(RatingDeductLog ratingDeductLog);

	public InfoPayBalance getInfoPayBalanceAfterUpdate(long balanceId);

	public List<InfoPayBalance> getSumResourceBalance(String userId,
			int balanceTypeId,String startTime);

	public void sendResourceOverSms(String chargedPhone, int msgType,String createTime,int balanceTypeId);
	
	public String selectAddResFlag();
	
	
	public List<RuleSystemSwitch> getRuleSystemSwitch();
	
	public List<RuleAutoRenew> getAllRenews();
	public List<CrmCodeOfr> getAllCrmOfr();
	
	public void createInfoPayBalance(InfoPayBalance infoPayBalance);
	
	public long getBalanceId() ;
	public void createBilActAccesslog(BilActAccesslog baa);

	public InfoPayBalance getInfoPayBalancebyTime(String payId,int balanceTypeId,String effDay) ;
}
