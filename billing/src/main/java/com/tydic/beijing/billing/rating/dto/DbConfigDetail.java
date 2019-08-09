package com.tydic.beijing.billing.rating.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.RuleSystemSwitch;
import com.tydic.beijing.billing.rating.domain.CodeCountry;
import com.tydic.beijing.billing.rating.domain.CodeOfr;
import com.tydic.beijing.billing.rating.domain.CodeRatableResource;
import com.tydic.beijing.billing.rating.domain.CodeRecordType;
import com.tydic.beijing.billing.rating.domain.CodeSpecialNbr;
import com.tydic.beijing.billing.rating.domain.CrmCodeOfr;
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

/**
 * 计费侧数据库配置信息
 * @author zhanghengbo
 *
 */
public class DbConfigDetail {
	@Autowired
	DbUtil dbUtil;
	
	private static final Logger log = Logger.getLogger(DbConfigDetail.class);

	private List<RuleEventTypeTree> allRuleEventTypeTree ;
	private Map<String,RuleEventTypeTree> allRuleEventTypeTreeMap   =new HashMap<String,RuleEventTypeTree>()          ;      
	private List<RuleEvtPricingStrategy> allRuleEvtPricingStrategy            ;      
	private Map<String ,RuleEvtPricingStrategy> allRuleEvtPricingStrategyMap  = new HashMap<String ,RuleEvtPricingStrategy>() ;      
	private List<RuleDinnerSelect> allRuleDinnerSelect                        ;      
	private Map<String,RuleDinnerSelect> allRuleDinnerSelectMap =new HashMap<String,RuleDinnerSelect>()             ;      
	private List<CodeOfr> allCodeOfr                                          ;      
	private Map<String,CodeOfr> allCodeOfrMap      = new HashMap<String,CodeOfr>()                           ;      
	private List<RuleOfrGroup> allRuleOfrGroup                                ;      
	private Map<String,RuleOfrGroup> allRuleOfrGroupMap     = new HashMap<String,RuleOfrGroup>()                  ;      
	private List<RuleGroupFavMode> allRuleGroupFavMode                        ;      
	private Map<String,RuleGroupFavMode> allRuleGroupFavModeMap  = new HashMap<String,RuleGroupFavMode>()             ;      
	private List<RuleFormula> allRuleFormula                                  ;      
	private Map<String,RuleFormula> allRuleFormulaMap     = new HashMap<String,RuleFormula>()                    ;      
	private List<RuleOfrResourceRel> allRuleOfrResourceRel                    ;      
	private Map<String,RuleOfrResourceRel> allRuleOfrResourceRelMap  =new HashMap<String,RuleOfrResourceRel>()         ;      
	private List<CodeRatableResource> allCodeRatableResource                  ;      
	private Map<String,CodeRatableResource> allCodeRatableResourceMap  =new HashMap<String,CodeRatableResource>()       ;      
	private List<RuleRatableCond> allRuleRatableCond                          ;      
	private Map<String,RuleRatableCond> allRuleRatableCondMap   = new HashMap<String,RuleRatableCond>()              ;      
	private List<RuleStrategySectRel> allRuleStrategySectRel                  ;      
	private Map<String,RuleStrategySectRel> allRuleStrategySectRelMap     = new HashMap<String,RuleStrategySectRel>()    ;      
	private List<RuleRateCondition> allRuleRateCondition                      ;      
	private Map<String,RuleRateCondition> allRuleRateConditionMap      = new HashMap<String,RuleRateCondition>()       ;      
	private List<RuleSectionRule> allRuleSectionRule                          ;      
	private Map<String,RuleSectionRule> allRuleSectionRuleMap     = new HashMap<String,RuleSectionRule>()            ;      
	private List<RulePricingSection> allRulePricingSection                    ;      
	private Map<String,RulePricingSection> allRulePricingSectionMap    = new HashMap<String,RulePricingSection>()       ;      
	private List<RuleTariff> allRuleTariff                                    ;      
	private Map<String,RuleTariff> allRuleTariffMap      =new HashMap<String,RuleTariff>()                     ;      
	private List<RuleHoliday> allRuleHoliday                                  ;      
	private Map<String,RuleHoliday> allRuleHolidayMap   = new HashMap<String,RuleHoliday>()                      ;      
	                                                                                 
//	private List<CodeAcctMonth> allCodeAcctMonth                              ;      
//	private Map<String,CodeAcctMonth> allCodeAcctMonthMap  = new HashMap<String,CodeAcctMonth> ()                   ;      
	private List<RuleOfrSplit> allRuleOfrSplit                                ;      
	private Map<String,RuleOfrSplit> allRuleOfrSplitMap    = new HashMap<String,RuleOfrSplit>()                   ;      
	private List<CodeCountry> allCodeCountry                                  ;      
	private Map<String,CodeCountry> allCodeCountryMap    = new HashMap<String,CodeCountry>()                    ;      
	private List<CodeSpecialNbr> allCodeSpecialNbr;
	private Map<String,CodeSpecialNbr> allCodeSpecialNbrMap = new HashMap<String,CodeSpecialNbr>();
	//douwei add
	private List<RuleSystemSwitch> ruleSystemSwitch=new ArrayList<RuleSystemSwitch>();
	
	private List<RuleAutoRenew> ruleAutoRenews = new ArrayList<RuleAutoRenew>();
	private List<CrmCodeOfr> crmCodeOfrList = new ArrayList<CrmCodeOfr>();
	
	private boolean addResOnFirstCdr=false;
	
	
//	    private List<RuleEventTypeTree>      allRuleEventTypeTree = dbUtil.getAllRuleEventTypeTree();
//		private List<RuleEvtPricingStrategy> allRuleEvtPricingStrategy = dbUtil.getAllStrategy();
//		private List<RuleDinnerSelect>       allRuleDinnerSelect = dbUtil.getAllRuleDinnerSelect();
//		private List<CodeOfr>                allCodeOfr = dbUtil.getAllDinnerInfo();
//		private List<RuleOfrGroup>           allRuleOfrGroup = dbUtil.getAllGroupInfo();
//		private List<RuleGroupFavMode>       allRuleGroupFavMode = dbUtil.getAllGroupFav();
//		private List<RuleFormula>            allRuleFormula = dbUtil.getAllRuleFormula();
//		private List<RuleOfrResourceRel>     allRuleOfrResourceRel = dbUtil.getAllResourceRels();
//		private List<CodeRatableResource>    allCodeRatableResource = dbUtil.getSumInfo();
//		private List<RuleRatableCond>        allRuleRatableCond = dbUtil.getRatableConds();
//		private List<RuleStrategySectRel>    allRuleStrategySectRel = dbUtil.getAllStrategySecRels();
//		private List<RuleRateCondition>      allRuleRateCondition = dbUtil.getAllRuleRateCondition();
//		private List<RuleSectionRule>        allRuleSectionRule = dbUtil.getAllSectionRules();
//		private List<RulePricingSection>     allRulePricingSection = dbUtil.getAllPricingSections();
//		private List<RuleTariff>             allRuleTariff = dbUtil.getAllTariff();
//		private List<RuleHoliday>            allRuleHoliday = dbUtil.getAllHolidays();
	
   
	
	//批价用
	private    Map<String,CodeOfr> allCodeOfrInfo=new HashMap<String,CodeOfr>();
	private    List<RuleEvtPricingStrategy> strategys=new ArrayList<RuleEvtPricingStrategy>();
	private    Map<String,RuleEvtPricingStrategy> strategysMap=new HashMap<String,RuleEvtPricingStrategy>();
	private    List<RuleOfrGroup> allOfrGroup=new ArrayList<RuleOfrGroup>();
	private    Map<String,RuleOfrGroup> groupsMap=new HashMap<String,RuleOfrGroup>();
	private    List<RuleGroupFavMode> allGroupFav=new ArrayList<RuleGroupFavMode>();//所有组优惠 
	private    Map<String,RuleGroupFavMode> groupFavMap=new HashMap<String,RuleGroupFavMode>();
	private   List<LifeUserProduct> userOfrs=new ArrayList<LifeUserProduct>();
	private  	List<RuleOfrSplit>  ofrSplit=new ArrayList<RuleOfrSplit>();
	private  Map<String ,String>   ofrRel=new HashMap<String,String>();
	
//	private  List<InfoRatableHistory> ratableHistory=new ArrayList<InfoRatableHistory>();
	private  List<RuleOfrResourceRel> rels=new ArrayList<RuleOfrResourceRel>();
	private  Map<String,String> relsMap=new HashMap<String,String>();// key  套餐+消息类型
	private  List<CodeRatableResource> sumInfo=new ArrayList<CodeRatableResource>();//累积量信息
	private  Map<String ,CodeRatableResource> sumInfoMap=new HashMap<String,CodeRatableResource>();
	
	private List<RuleFormula>  formulas=new ArrayList<RuleFormula>();
	private Map<Integer,String> formulaMap=new HashMap<Integer,String>();
	
	private   List<RuleStrategySectRel> allRels=new ArrayList<RuleStrategySectRel>();
	private   List<RulePricingSection> allSections=new ArrayList<RulePricingSection>();
	private   Map<String,RulePricingSection> allSecMap=new HashMap<String,RulePricingSection>();
	private   List<RuleSectionRule> sectionRules=new ArrayList<RuleSectionRule>();//所有段落规则
	private   Map<Long,RuleSectionRule> secRulesMap=new HashMap<Long,RuleSectionRule>();
	
	
	private  List<CodeRecordType> recordTypes=new ArrayList<CodeRecordType>();
	private  Map<String,CodeRecordType> codeRecordMap=new HashMap<String,CodeRecordType>();
	private  List<RuleRateCondition> conds=new ArrayList<RuleRateCondition>();
	private  Map<Integer , List<RuleRateCondition>> condMap=new HashMap<Integer ,List<RuleRateCondition>>();
	
	private List<RulePricingSectionDisct> discounts=new ArrayList<RulePricingSectionDisct>();
	
	
	




	/**
	 * 根据时间获取当前账期
	 * @param date
	 * @return
	 */
//	public int getAcctMonth(Date date){
//		int acctMonth = -1;
//		for(CodeAcctMonth tmpcam:this.allCodeAcctMonth){
//			if(tmpcam.getBil_eff_date().equals(date) || tmpcam.getBil_exp_date().equals(date) ||
//					(tmpcam.getBil_eff_date().before(date) && tmpcam.getBil_exp_date().after(date))
//					){
//				//因为可能会有往月延迟话单，所以这里不再判断use_tag
//					acctMonth = tmpcam.getAcct_month();
//					break;
//			
//			}
//		}
//		return acctMonth;
//	}
	
	
	
	
	/**
	 * 读取数据库
	 */
	public void getDbConfig() {
		long startTime=System.currentTimeMillis();
		this.allRuleEventTypeTree = dbUtil.getAllRuleEventTypeTree();
		for(RuleEventTypeTree tmpr:allRuleEventTypeTree){
			this.allRuleEventTypeTreeMap.put(tmpr.getEvent_type_rule_tree_id()+"",tmpr);
		}
		
		this.allRuleEvtPricingStrategy = dbUtil.getAllStrategy();
		for(RuleEvtPricingStrategy tmpr:allRuleEvtPricingStrategy){
			this.allRuleEvtPricingStrategyMap.put(tmpr.getStrategy_id()+"",tmpr);
		}
		
		this.allRuleDinnerSelect = dbUtil.getAllRuleDinnerSelect();
		for(RuleDinnerSelect tmpr:allRuleDinnerSelect){
			this.allRuleDinnerSelectMap.put(tmpr.getOfr_b_id()+"",tmpr);
		}
		
		this.allCodeOfr = dbUtil.getAllCodeOfr();
		for(CodeOfr tmpr:allCodeOfr){
			this.allCodeOfrMap.put(tmpr.getOfr_b_id()+"",tmpr);
		}
		
		this.allRuleOfrGroup = dbUtil.getAllGroupInfo();
		for(RuleOfrGroup tmpr:allRuleOfrGroup){
			this.allRuleOfrGroupMap.put(tmpr.getOfr_Group()+"",tmpr);
		}
		
		this.allRuleGroupFavMode = dbUtil.getAllGroupFav();
		for(RuleGroupFavMode tmpr:allRuleGroupFavMode){
			this.allRuleGroupFavModeMap.put(tmpr.getOfr_Group()+"",tmpr);
		}
		
		this.allRuleFormula = dbUtil.getAllRuleFormula();
		for(RuleFormula tmpr:allRuleFormula){
			this.allRuleFormulaMap.put(tmpr.getMsg_Type()+"",tmpr);
		}
		
		this.allRuleOfrResourceRel = dbUtil.getAllResourceRels();
		for(RuleOfrResourceRel tmpr:allRuleOfrResourceRel){
			this.allRuleOfrResourceRelMap.put(tmpr.getOfr_Res_Id()+"",tmpr);
		}
		
		this.allCodeRatableResource = dbUtil.getSumInfo();
		for(CodeRatableResource tmpr:allCodeRatableResource){
			this.allCodeRatableResourceMap.put(tmpr.getRatable_resource_code()+"",tmpr);
		}
		
		this.allRuleRatableCond = dbUtil.getRatableConds();
		for(RuleRatableCond tmpr:allRuleRatableCond){
			this.allRuleRatableCondMap.put(tmpr.getRes_cond_id()+"",tmpr);
		}
		
		this.allRuleStrategySectRel = dbUtil.getAllStrategySecRels();
//		for(RuleStrategySectRel tmpr:allRuleStrategySectRel){
//			this.allRuleStrategySectRelMap.put(tmpr.getStrategy_id()+"",tmpr);
//		}
		
		this.allRuleRateCondition = dbUtil.getAllRuleRateCondition();
//		for(RuleRateCondition tmpr:allRuleRateCondition){
//			this.allRuleRateConditionMap.put(tmpr.getCond_id()+"",tmpr);
//		}
		
		this.allRuleSectionRule = dbUtil.getAllSectionRules();
		for(RuleSectionRule tmpr:allRuleSectionRule){
			this.allRuleSectionRuleMap.put(tmpr.getPricing_section()+"",tmpr);
		}
		
		this.allRulePricingSection = dbUtil.getAllPricingSections();
		for(RulePricingSection tmpr:allRulePricingSection){
			this.allRulePricingSectionMap.put(tmpr.getPricing_section()+"",tmpr);
		}
		
		this.allRuleTariff = dbUtil.getAllTariff();
		for(RuleTariff tmpr:allRuleTariff){
			this.allRuleTariffMap.put(tmpr.getTariff_id()+"",tmpr);
		}
		
		this.allRuleHoliday = dbUtil.getAllHolidays();
		for(RuleHoliday tmpr:allRuleHoliday){
			this.allRuleHolidayMap.put(tmpr.getHoliday_id()+"",tmpr);
		}
		
//		this.allCodeAcctMonth = dbUtil.getAllCodeAcctMonth();
//		for(CodeAcctMonth tmpr:allCodeAcctMonth){
//			this.allCodeAcctMonthMap.put(tmpr.getAcct_month()+"",tmpr);
//		}
		
		this.allRuleOfrSplit = dbUtil.getallRuleOfrSplit();
		for(RuleOfrSplit tmpr:allRuleOfrSplit){
			this.allRuleOfrSplitMap.put(tmpr.getProduct_id()+"",tmpr);
		}
		
		this.allCodeCountry = dbUtil.getAllCodeCountry();
		for(CodeCountry tmpr:allCodeCountry){
			this.allCodeCountryMap.put(tmpr.getArea_code()+"",tmpr);
		}
		
		this.allCodeSpecialNbr = dbUtil.getAllCodeSpecialNbr();
		for(CodeSpecialNbr tmpc:allCodeSpecialNbr){
			this.allCodeSpecialNbrMap.put(tmpc.getSpecial_nbr(), tmpc);
		}
		
		this.ruleAutoRenews = dbUtil.getAllRenews();
		this.crmCodeOfrList = dbUtil.getAllCrmOfr();
		
		
		
		String flag = dbUtil.selectAddResFlag();
		if(flag !=null && flag.equals("1")){
			addResOnFirstCdr=true;
		}else{
			addResOnFirstCdr = false;
		}
		
		
		
		//douwei add
		this.ruleSystemSwitch=dbUtil.getRuleSystemSwitch();
		//this.allRuleEventTypeTree = S.get(RuleEventTypeTree.class).query(Condition.build("getAllRuleEventTypeTree"));
		 
	    log.debug("allRuleEventTypeTree.size()     ="+ allRuleEventTypeTree.size()     );   
		log.debug("allRuleEvtPricingStrategy.size()="+ allRuleEvtPricingStrategy.size());   
		log.debug("allRuleDinnerSelect.size()      ="+ allRuleDinnerSelect.size()      );   
		log.debug("allCodeOfr.size()               ="+ allCodeOfr.size()               );   
		log.debug("allRuleOfrGroup.size()          ="+ allRuleOfrGroup.size()          );   
		log.debug("allRuleGroupFavMode.size()      ="+ allRuleGroupFavMode.size()      );   
		log.debug("allRuleFormula.size()           ="+ allRuleFormula.size()           );   
		log.debug("allRuleOfrResourceRel.size()    ="+ allRuleOfrResourceRel.size()    );   
		log.debug("allCodeRatableResource.size()   ="+ allCodeRatableResource.size()   );   
		log.debug("allRuleRatableCond.size()       ="+ allRuleRatableCond.size()       );   
		log.debug("allRuleStrategySectRel.size()   ="+ allRuleStrategySectRel.size()   );   
		log.debug("allRuleRateCondition.size()     ="+ allRuleRateCondition.size()     );   
		log.debug("allRuleSectionRule.size()       ="+ allRuleSectionRule.size()       );   
		log.debug("allRulePricingSection.size()    ="+ allRulePricingSection.size()    );   
		log.debug("allRuleTariff.size()            ="+ allRuleTariff.size()            );   
		log.debug("allRuleHoliday.size()           ="+ allRuleHoliday.size()           );  
		log.debug("ruleSystemSwitch.size()         ="+ ruleSystemSwitch.size()         );
		
		
		
		List<CodeOfr> ofrs=dbUtil.getAllDinnerInfo();
		for(CodeOfr ofr:ofrs){
			
			allCodeOfrInfo.put(""+ofr.getOfr_b_id(), ofr);
		}
		strategys=dbUtil.getAllStrategy();
		for(RuleEvtPricingStrategy iter:strategys){
			String key=""+iter.getPricing_plan_id()+iter.getEvent_type_id();
			strategysMap.put(key, iter);
		}
		allOfrGroup=dbUtil.getAllGroupInfo();
		for(RuleOfrGroup group:allOfrGroup){
			String key=""+group.getAtom_Ofr()+group.getMsg_Type();
			groupsMap.put(key, group);
		}
		allGroupFav=dbUtil.getAllGroupFav();
		
		for(RuleGroupFavMode favMode:allGroupFav){
			String key=""+favMode.getOfr_Group();
			
			groupFavMap.put(key, favMode);
		}
		
		ofrSplit=dbUtil.getallRuleOfrSplit();
		for(RuleOfrSplit  ros:ofrSplit){
			ofrRel.put(ros.getProduct_id(), ros.getOfr_b_id());
		}
		
		
		rels=dbUtil.getAllResourceRels();
		for(RuleOfrResourceRel  rel  : rels ){
			String[] msgtypes=rel.getMsg_Types().split(",");
			for(String str : msgtypes){
				relsMap.put(""+rel.getOfr_B_Id()+str, rel.getResource_Code());
			}
		}
		sumInfo=dbUtil.getSumInfo();
		for(CodeRatableResource iter:sumInfo){
			sumInfoMap.put(iter.getRatable_resource_code(), iter);
		}
		
//		ratableHistory=dbUtil.getAllRatableValue();
		
		formulas=dbUtil.getAllRuleFormula();
		for(RuleFormula rf :formulas){
			formulaMap.put(rf.getMsg_Type(), rf.getFormula());
		}
		
		allRels=dbUtil.getAllStrategySecRels();
//		for(RuleStrategySectRel rel:allRels){
//			relMaps.put(rel.getPricing_section()+rel.getStrategy_id(), rel);
//		}
		
		allSections=dbUtil.getAllPricingSections();
		for(RulePricingSection sec:allSections){
			allSecMap.put(""+sec.getPricing_section(), sec);
		}
		
		sectionRules=dbUtil.getAllSectionRules();
		for(RuleSectionRule rule:sectionRules){
			secRulesMap.put(rule.getPricing_section(), rule);
		}
		
		recordTypes=dbUtil.getAllCodeRecordType();
		for(CodeRecordType code:recordTypes){
			codeRecordMap.put(code.getRecord_code(), code);
		}

//		conds=dbUtil.getAllRuleRateCondition();
//		for(RuleRateCondition iter :conds){
//			if(condMap.containsKey((int)iter.getCond_id())){
//				condMap.get(iter.getCond_id()).add(iter);
//			}else{
//				List<RuleRateCondition> l=new ArrayList<RuleRateCondition>();
//				l.add(iter);
//				condMap.put((int)iter.getCond_id(), l);
//			}
//		}
//		

		conds=dbUtil.getAllRuleRateCondition();
		for(RuleRateCondition iter :conds){
			if(condMap.containsKey((int)iter.getCond_id())){
				condMap.get((int)iter.getCond_id()).add(iter);
			}else{
				List<RuleRateCondition> l=new ArrayList<RuleRateCondition>();
				l.add(iter);
				condMap.put((int)iter.getCond_id(), l);
			}
		}
		
		discounts =dbUtil.getAllSectionDiscount();
		
		long endTime=System.currentTimeMillis();
		//System.out.println("数据库数据读取..["+(endTime-startTime)+"]milliseconds");
	}
	
	
	
	
	
	public List<RuleFormula> getFormulas() {
		return formulas;
	}
	public Map<Integer, String> getFormulaMap() {
		return formulaMap;
	}
	public DbUtil getDbUtil() {
		return dbUtil;
	}
	public void setDbUtil(DbUtil dbUtil) {
		this.dbUtil = dbUtil;
	}
	public Map<String, CodeOfr> getAllCodeOfrInfo() {
		return allCodeOfrInfo;
	}
	public void setAllCodeOfrInfo(Map<String, CodeOfr> allCodeOfrInfo) {
		this.allCodeOfrInfo = allCodeOfrInfo;
	}
	public List<RuleEvtPricingStrategy> getStrategys() {
		return strategys;
	}
	public void setStrategys(List<RuleEvtPricingStrategy> strategys) {
		this.strategys = strategys;
	}
	public Map<String, RuleEvtPricingStrategy> getStrategysMap() {
		return strategysMap;
	}
	public void setStrategysMap(Map<String, RuleEvtPricingStrategy> strategysMap) {
		this.strategysMap = strategysMap;
	}
	public List<RuleOfrGroup> getAllOfrGroup() {
		return allOfrGroup;
	}
	public void setAllOfrGroup(List<RuleOfrGroup> allOfrGroup) {
		this.allOfrGroup = allOfrGroup;
	}
	public Map<String, RuleOfrGroup> getGroupsMap() {
		return groupsMap;
	}
	public void setGroupsMap(Map<String, RuleOfrGroup> groupsMap) {
		this.groupsMap = groupsMap;
	}
	public List<RuleGroupFavMode> getAllGroupFav() {
		return allGroupFav;
	}
	public void setAllGroupFav(List<RuleGroupFavMode> allGroupFav) {
		this.allGroupFav = allGroupFav;
	}
	public Map<String, RuleGroupFavMode> getGroupFavMap() {
		return groupFavMap;
	}
	public void setGroupFavMap(Map<String, RuleGroupFavMode> groupFavMap) {
		this.groupFavMap = groupFavMap;
	}
	public List<LifeUserProduct> getUserOfrs() {
		return userOfrs;
	}
	public void setUserOfrs(List<LifeUserProduct> userOfrs) {
		this.userOfrs = userOfrs;
	}
	public List<RuleOfrSplit> getOfrSplit() {
		return ofrSplit;
	}
	public void setOfrSplit(List<RuleOfrSplit> ofrSplit) {
		this.ofrSplit = ofrSplit;
	}
	public Map<String, String> getOfrRel() {
		return ofrRel;
	}
	public void setOfrRel(Map<String, String> ofrRel) {
		this.ofrRel = ofrRel;
	} 
	public List<RuleEventTypeTree> getAllRuleEventTypeTree() {
		return allRuleEventTypeTree;
	}
	public void setAllRuleEventTypeTree(List<RuleEventTypeTree> allRuleEventTypeTree) {
		this.allRuleEventTypeTree = allRuleEventTypeTree;
	}
	public List<RuleEvtPricingStrategy> getAllRuleEvtPricingStrategy() {
		return allRuleEvtPricingStrategy;
	}
	public void setAllRuleEvtPricingStrategy(
			List<RuleEvtPricingStrategy> allRuleEvtPricingStrategy) {
		this.allRuleEvtPricingStrategy = allRuleEvtPricingStrategy;
	}
	public List<RuleDinnerSelect> getAllRuleDinnerSelect() {
		return allRuleDinnerSelect;
	}
	public void setAllRuleDinnerSelect(List<RuleDinnerSelect> allRuleDinnerSelect) {
		this.allRuleDinnerSelect = allRuleDinnerSelect;
	}
	public List<CodeOfr> getAllCodeOfr() {
		return allCodeOfr;
	}
	public void setAllCodeOfr(List<CodeOfr> allCodeOfr) {
		this.allCodeOfr = allCodeOfr;
	}
	public List<RuleOfrGroup> getAllRuleOfrGroup() {
		return allRuleOfrGroup;
	}
	public void setAllRuleOfrGroup(List<RuleOfrGroup> allRuleOfrGroup) {
		this.allRuleOfrGroup = allRuleOfrGroup;
	}
	public List<RuleGroupFavMode> getAllRuleGroupFavMode() {
		return allRuleGroupFavMode;
	}
	public void setAllRuleGroupFavMode(List<RuleGroupFavMode> allRuleGroupFavMode) {
		this.allRuleGroupFavMode = allRuleGroupFavMode;
	}
	public List<RuleFormula> getAllRuleFormula() {
		return allRuleFormula;
	}
	public void setAllRuleFormula(List<RuleFormula> allRuleFormula) {
		this.allRuleFormula = allRuleFormula;
	}
	public List<RuleOfrResourceRel> getAllRuleOfrResourceRel() {
		return allRuleOfrResourceRel;
	}
	public void setAllRuleOfrResourceRel(
			List<RuleOfrResourceRel> allRuleOfrResourceRel) {
		this.allRuleOfrResourceRel = allRuleOfrResourceRel;
	}
	public List<CodeRatableResource> getAllCodeRatableResource() {
		return allCodeRatableResource;
	}
	public void setAllCodeRatableResource(
			List<CodeRatableResource> allCodeRatableResource) {
		this.allCodeRatableResource = allCodeRatableResource;
	}
	public List<RuleRatableCond> getAllRuleRatableCond() {
		return allRuleRatableCond;
	}
	public void setAllRuleRatableCond(List<RuleRatableCond> allRuleRatableCond) {
		this.allRuleRatableCond = allRuleRatableCond;
	}
	public List<RuleStrategySectRel> getAllRuleStrategySectRel() {
		return allRuleStrategySectRel;
	}
	public void setAllRuleStrategySectRel(
			List<RuleStrategySectRel> allRuleStrategySectRel) {
		this.allRuleStrategySectRel = allRuleStrategySectRel;
	}
	public List<RuleRateCondition> getAllRuleRateCondition() {
		return allRuleRateCondition;
	}
	public void setAllRuleRateCondition(List<RuleRateCondition> allRuleRateCondition) {
		this.allRuleRateCondition = allRuleRateCondition;
	}
	public List<RuleSectionRule> getAllRuleSectionRule() {
		return allRuleSectionRule;
	}
	public void setAllRuleSectionRule(List<RuleSectionRule> allRuleSectionRule) {
		this.allRuleSectionRule = allRuleSectionRule;
	}
	public List<RulePricingSection> getAllRulePricingSection() {
		return allRulePricingSection;
	}
	public void setAllRulePricingSection(
			List<RulePricingSection> allRulePricingSection) {
		this.allRulePricingSection = allRulePricingSection;
	}
	public List<RuleTariff> getAllRuleTariff() {
		return allRuleTariff;
	}
	public void setAllRuleTariff(List<RuleTariff> allRuleTariff) {
		this.allRuleTariff = allRuleTariff;
	}
	public List<RuleHoliday> getAllRuleHoliday() {
		return allRuleHoliday;
	}
	public void setAllRuleHoliday(List<RuleHoliday> allRuleHoliday) {
		this.allRuleHoliday = allRuleHoliday;
	}
	public List<RuleOfrSplit> getAllRuleOfrSplit() {
		return allRuleOfrSplit;
	}
	public void setAllRuleOfrSplit(List<RuleOfrSplit> allRuleOfrSplit) {
		this.allRuleOfrSplit = allRuleOfrSplit;
	}
//	public List<CodeAcctMonth> getAllCodeAcctMonth() {
//		return allCodeAcctMonth;
//	}
//	public void setAllCodeAcctMonth(List<CodeAcctMonth> allCodeAcctMonth) {
//		this.allCodeAcctMonth = allCodeAcctMonth;
//	}
//	public List<InfoRatableHistory> getRatableHistory() {
//		return ratableHistory;
//	}
//
//	public void setRatableHistory(List<InfoRatableHistory> ratableHistory) {
//		this.ratableHistory = ratableHistory;
//	}

	public List<RuleOfrResourceRel> getRels() {
		return rels;
	}
	public void setRels(List<RuleOfrResourceRel> rels) {
		this.rels = rels;
	}
	public List<CodeRatableResource> getSumInfo() {
		return sumInfo;
	}
	public void setSumInfo(List<CodeRatableResource> sumInfo) {
		this.sumInfo = sumInfo;
	}
	public Map<String, CodeRatableResource> getSumInfoMap() {
		return sumInfoMap;
	}

	public void setSumInfoMap(Map<String, CodeRatableResource> sumInfoMap) {
		this.sumInfoMap = sumInfoMap;
	}
	public  List<RuleStrategySectRel> getAllRels() {
		return allRels;
	}
	public  List<RulePricingSection> getAllSections() {
		return allSections;
	}
	public  Map<String, RulePricingSection> getAllSecMap() {
		return allSecMap;
	}
	public  List<RuleSectionRule> getSectionRules() {
		return sectionRules;
	}
	public  Map<Long, RuleSectionRule> getSecRulesMap() {
		return secRulesMap;
	}
	public List<CodeRecordType> getRecordTypes() {
		return recordTypes;
	}
	public Map<String, CodeRecordType> getCodeRecordMap() {
		return codeRecordMap;
	}
	public List<RuleRateCondition> getConds() {
		return conds;
	}
	public Map<Integer, List<RuleRateCondition>> getCondMap() {
		return condMap;
	}
	
	public Map<String, String> getRelsMap() {
		return relsMap;
	}




	public List<CodeCountry> getAllCodeCountry() {
		return allCodeCountry;
	}




	public void setAllCodeCountry(List<CodeCountry> allCodeCountry) {
		this.allCodeCountry = allCodeCountry;
	}




	public Map<String, RuleEventTypeTree> getAllRuleEventTypeTreeMap() {
		return allRuleEventTypeTreeMap;
	}




	public void setAllRuleEventTypeTreeMap(
			Map<String, RuleEventTypeTree> allRuleEventTypeTreeMap) {
		this.allRuleEventTypeTreeMap = allRuleEventTypeTreeMap;
	}




	public Map<String, RuleEvtPricingStrategy> getAllRuleEvtPricingStrategyMap() {
		return allRuleEvtPricingStrategyMap;
	}




	public void setAllRuleEvtPricingStrategyMap(
			Map<String, RuleEvtPricingStrategy> allRuleEvtPricingStrategyMap) {
		this.allRuleEvtPricingStrategyMap = allRuleEvtPricingStrategyMap;
	}




	public Map<String, RuleDinnerSelect> getAllRuleDinnerSelectMap() {
		return allRuleDinnerSelectMap;
	}




	public void setAllRuleDinnerSelectMap(
			Map<String, RuleDinnerSelect> allRuleDinnerSelectMap) {
		this.allRuleDinnerSelectMap = allRuleDinnerSelectMap;
	}




	public Map<String, CodeOfr> getAllCodeOfrMap() {
		return allCodeOfrMap;
	}




	public void setAllCodeOfrMap(Map<String, CodeOfr> allCodeOfrMap) {
		this.allCodeOfrMap = allCodeOfrMap;
	}




	public Map<String, RuleOfrGroup> getAllRuleOfrGroupMap() {
		return allRuleOfrGroupMap;
	}




	public void setAllRuleOfrGroupMap(Map<String, RuleOfrGroup> allRuleOfrGroupMap) {
		this.allRuleOfrGroupMap = allRuleOfrGroupMap;
	}




	public Map<String, RuleGroupFavMode> getAllRuleGroupFavModeMap() {
		return allRuleGroupFavModeMap;
	}




	public void setAllRuleGroupFavModeMap(
			Map<String, RuleGroupFavMode> allRuleGroupFavModeMap) {
		this.allRuleGroupFavModeMap = allRuleGroupFavModeMap;
	}




	public Map<String, RuleFormula> getAllRuleFormulaMap() {
		return allRuleFormulaMap;
	}




	public void setAllRuleFormulaMap(Map<String, RuleFormula> allRuleFormulaMap) {
		this.allRuleFormulaMap = allRuleFormulaMap;
	}




	public Map<String, RuleOfrResourceRel> getAllRuleOfrResourceRelMap() {
		return allRuleOfrResourceRelMap;
	}




	public void setAllRuleOfrResourceRelMap(
			Map<String, RuleOfrResourceRel> allRuleOfrResourceRelMap) {
		this.allRuleOfrResourceRelMap = allRuleOfrResourceRelMap;
	}




	public Map<String, CodeRatableResource> getAllCodeRatableResourceMap() {
		return allCodeRatableResourceMap;
	}




	public void setAllCodeRatableResourceMap(
			Map<String, CodeRatableResource> allCodeRatableResourceMap) {
		this.allCodeRatableResourceMap = allCodeRatableResourceMap;
	}




	public Map<String, RuleRatableCond> getAllRuleRatableCondMap() {
		return allRuleRatableCondMap;
	}




	public void setAllRuleRatableCondMap(
			Map<String, RuleRatableCond> allRuleRatableCondMap) {
		this.allRuleRatableCondMap = allRuleRatableCondMap;
	}




	public Map<String, RuleStrategySectRel> getAllRuleStrategySectRelMap() {
		return allRuleStrategySectRelMap;
	}




	public void setAllRuleStrategySectRelMap(
			Map<String, RuleStrategySectRel> allRuleStrategySectRelMap) {
		this.allRuleStrategySectRelMap = allRuleStrategySectRelMap;
	}




	public Map<String, RuleRateCondition> getAllRuleRateConditionMap() {
		return allRuleRateConditionMap;
	}




	public void setAllRuleRateConditionMap(
			Map<String, RuleRateCondition> allRuleRateConditionMap) {
		this.allRuleRateConditionMap = allRuleRateConditionMap;
	}




	public Map<String, RuleSectionRule> getAllRuleSectionRuleMap() {
		return allRuleSectionRuleMap;
	}




	public void setAllRuleSectionRuleMap(
			Map<String, RuleSectionRule> allRuleSectionRuleMap) {
		this.allRuleSectionRuleMap = allRuleSectionRuleMap;
	}




	public Map<String, RulePricingSection> getAllRulePricingSectionMap() {
		return allRulePricingSectionMap;
	}




	public void setAllRulePricingSectionMap(
			Map<String, RulePricingSection> allRulePricingSectionMap) {
		this.allRulePricingSectionMap = allRulePricingSectionMap;
	}




	public Map<String, RuleTariff> getAllRuleTariffMap() {
		return allRuleTariffMap;
	}




	public void setAllRuleTariffMap(Map<String, RuleTariff> allRuleTariffMap) {
		this.allRuleTariffMap = allRuleTariffMap;
	}




	public Map<String, RuleHoliday> getAllRuleHolidayMap() {
		return allRuleHolidayMap;
	}




	public void setAllRuleHolidayMap(Map<String, RuleHoliday> allRuleHolidayMap) {
		this.allRuleHolidayMap = allRuleHolidayMap;
	}



//
//	public Map<String, CodeAcctMonth> getAllCodeAcctMonthMap() {
//		return allCodeAcctMonthMap;
//	}
//
//
//
//
//	public void setAllCodeAcctMonthMap(
//			Map<String, CodeAcctMonth> allCodeAcctMonthMap) {
//		this.allCodeAcctMonthMap = allCodeAcctMonthMap;
//	}




	public Map<String, RuleOfrSplit> getAllRuleOfrSplitMap() {
		return allRuleOfrSplitMap;
	}




	public void setAllRuleOfrSplitMap(Map<String, RuleOfrSplit> allRuleOfrSplitMap) {
		this.allRuleOfrSplitMap = allRuleOfrSplitMap;
	}




	public Map<String, CodeCountry> getAllCodeCountryMap() {
		return allCodeCountryMap;
	}




	public void setAllCodeCountryMap(Map<String, CodeCountry> allCodeCountryMap) {
		this.allCodeCountryMap = allCodeCountryMap;
	}




	public List<CodeSpecialNbr> getAllCodeSpecialNbr() {
		return allCodeSpecialNbr;
	}




	public void setAllCodeSpecialNbr(List<CodeSpecialNbr> allCodeSpecialNbr) {
		this.allCodeSpecialNbr = allCodeSpecialNbr;
	}




	public Map<String, CodeSpecialNbr> getAllCodeSpecialNbrMap() {
		return allCodeSpecialNbrMap;
	}




	public void setAllCodeSpecialNbrMap(
			Map<String, CodeSpecialNbr> allCodeSpecialNbrMap) {
		this.allCodeSpecialNbrMap = allCodeSpecialNbrMap;
	}


	public List<RulePricingSectionDisct> getDiscounts() {
		return discounts;
	}
	
	public boolean isAddResOnFirstCdr() {
		return addResOnFirstCdr;
	}




    //douwei add
	public List<RuleSystemSwitch> getRuleSystemSwitch() {
		return ruleSystemSwitch;
	}
	
	public void setRuleSystemSwitch(List<RuleSystemSwitch> ruleSystemSwitch) {
		this.ruleSystemSwitch = ruleSystemSwitch;
	}

	/**
	 * douwei
	 * 用于通过开关名查询开关getBySwitchCode(String switchCode)
	 * return RuleSystemSwitch
	 * 如果返回值为空则 return null; 
	 */
	
	public RuleSystemSwitch getBySwitchCode(String switchCode){
//		
		if(ruleSystemSwitch!=null){
			for(RuleSystemSwitch rys:ruleSystemSwitch){
				if(rys.getSwitch_code().equals(switchCode)){
					return rys;
				}
			}
		}
		return null;
		
	}
	
	public List<RuleAutoRenew> getRenewCfg(int strategy_id){
		List<RuleAutoRenew> renews = new ArrayList<RuleAutoRenew>();
		for (RuleAutoRenew renew :this.ruleAutoRenews){
			if(strategy_id == renew.getStrategy_id()){
				renews.add(renew);
			}
			
		}
		return renews;
	}
	
	public boolean isMode(String ofrId){
		
		
		if(ofrId == null) return false;
		
		for(CrmCodeOfr codeOfr:crmCodeOfrList){
			if(codeOfr.getOfr_id().equals(ofrId) && codeOfr.getOfr_mode_type()!=null && codeOfr.getOfr_mode_type().equals("1")){
				return true;
			}
		}
		return false;
	}
}
