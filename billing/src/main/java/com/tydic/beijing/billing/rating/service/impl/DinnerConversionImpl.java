package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.rating.domain.CodeOfr;
import com.tydic.beijing.billing.rating.domain.ParamData;
import com.tydic.beijing.billing.rating.domain.PlanDisct;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.RuleDinnerSelect;
import com.tydic.beijing.billing.rating.domain.RuleEvtPricingStrategy;
import com.tydic.beijing.billing.rating.domain.RuleFormula;
import com.tydic.beijing.billing.rating.domain.RuleGroupFavMode;
import com.tydic.beijing.billing.rating.domain.RuleOfrGroup;
import com.tydic.beijing.billing.rating.domain.RuleOfrSplit;
import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.dto.impl.DbUtilImpl;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;
import com.tydic.beijing.billing.rating.service.DinnerConversion;
import com.tydic.beijing.billing.rating.service.ErrorInfo;
import com.tydic.beijing.billing.rating.service.RatingException;
import com.tydic.beijing.billing.rating.util.DateUtil;




public class DinnerConversionImpl implements DinnerConversion{

	private   Logger log = Logger.getLogger(DinnerConversionImpl.class);
	
	@Autowired
	private RatingMsg ratingMsg;

	private RatingData ratingData;

	private DbConfigDetail  dbConfig=null;
	
	private String currTime="";
	@Autowired
	private ApplicationContextHelper applicationContextHelper;
	
	//全部数据
	private     Map<String,CodeOfr> allCodeOfrInfo=new HashMap<String,CodeOfr>();
	private     Map<String,RuleEvtPricingStrategy> strategysMap=new HashMap<String,RuleEvtPricingStrategy>();
	private     Map<String,RuleOfrGroup> groupsMap=new HashMap<String,RuleOfrGroup>();
	private     Map<String,RuleGroupFavMode> groupFavMap=new HashMap<String,RuleGroupFavMode>();
	private    List<LifeUserProduct> userOfrs=new ArrayList<LifeUserProduct>();
	
	//结果
	private List<String> iEffGroups =new ArrayList<String>(); // 有效的套餐组. key: group_id
	private Map<String, List<PlanDisct>> iOfrInGroups=new HashMap<String,List<PlanDisct>>(); // 每个group中包含的原子套餐. key: group_id.
	private List<PlanDisct> iAtomPlanDiscts=new ArrayList<PlanDisct>(); // 折线后的定价计划
	private Map<String, PlanDisct> iAtomPlanDisctMap=new HashMap<String,PlanDisct>();
	private Map<String, Integer> iGroupFavModes=new HashMap<String,Integer>();
	
	
	
	public void init(){

	}
	
	
	
	public DinnerConversionImpl(){}
	
	public DinnerConversionImpl(RatingMsg msg ,RatingData data){
		ratingMsg=msg;
		ratingData=data;
	}
	
	@Override
	public boolean extractRateData() throws RatingException {
		if(dbConfig==null){
			dbConfig=(DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
			allCodeOfrInfo=dbConfig.getAllCodeOfrInfo();
			strategysMap=dbConfig.getStrategysMap();
			groupsMap=dbConfig.getGroupsMap();
			groupFavMap=dbConfig.getGroupFavMap();
		}
		if(!getBillOfrPricingPlan()){
			System.out.println("获取折线套餐错误");
			throw new RatingException(ErrorInfo.ATOMS_EMPTY,"获取用户折线套餐为空");
		}
		if(!getOfrGroupAndPrior()){
			System.out.println("获取套餐组错误");
			throw new RatingException(ErrorInfo.ERR_NOT_FOUND_OFR_GROUP,"未找到套餐所属套餐组");
		}
		if(!getOfrGroupFav()){
			System.out.println("获取套餐组优惠信息失败");
			throw new RatingException(ErrorInfo.ERR_NOT_FOUND_GROUP_FAV_MODE,"未找到套餐组对应优惠信息");
		}
//		if(!getFormulaAndAdjust()){
//			log.debug("业务公式格式错误:业务公式只包括套餐组号及优惠模式操作符");
//			throw new RatingException(ErrorInfo.ERR_INVALID_FORMULA_SYNTAX,"业务公式格式错误");
//		}
		return true;
	}
	
	
	//设置激活日期
	private void setActiveDate(){
		
	}
	//设置首月标志
	private void setFirstMonthFlag(){
		
	}
	 //支持首月偏移量套餐
    private void calcCoefficient(){}

	//套餐折线
	private boolean getBillOfrPricingPlan() {
		log.debug("套餐折现");
		int msgType=ratingMsg.getM_nMsgType();
		String ofrId=""+ratingMsg.getM_iUserMsg().getnOfrId();
		
		
		ofrId=dbConfig.getOfrRel().get(ofrId);
		System.out.println("折线:消息类型:"+msgType+"ofr_b_id:"+ofrId);
		if(!getAtomDinners(ofrId,msgType)){ //得到原子套餐定义信息集合
			log.error("获取原子套餐定义信息错误!");
			return false;
		}
//		if(!filterPlanWithEvent()){//增加策略信息
//			return false;
//		}
		calcCoefficient();
		return true;
	}
	
	
	//获取套餐分组信息
	private boolean getOfrGroupAndPrior(){
		
//		ratingData.getiEffGroups().clear();
//		ratingData.getiOfrInGroups().clear();
		log.debug("获取套餐产品组");

//		List<PlanDisct> allAtomsList=ratingData.getiAtomPlanDiscts();
		
		int msgType=ratingMsg.getM_nMsgType();
		String groupId="";
		String key="";
		RuleOfrGroup group=null;
		boolean findGroup;
		for(PlanDisct atom:iAtomPlanDiscts){
			findGroup=false;
			key=""+atom.getnAtomOfrId()+msgType;
			group=groupsMap.get(key);
			if(null != group){
				findGroup=true;
				groupId=group.getOfr_Group();
				atom.setcOfrGroup(groupId);
				atom.setnCalcPriority(group.getPriority());
//				groupIds.add(groupId);
//				ratingData.getiEffGroups().add(groupId);
				iEffGroups.add(groupId);
				
//				if(groupDinners.containsKey(groupId)){
				if(iOfrInGroups.containsKey(groupId)){
//					List<PlanDisct> value=ratingData.getiOfrInGroups().get(groupId);
					List<PlanDisct> value=iOfrInGroups.get(groupId);
					if(!value.contains(atom))
						value.add(atom);
				}else{
					List<PlanDisct> dinners=new ArrayList<PlanDisct>();
					dinners.add(atom);
//					groupDinners.put(groupId, dinners);
//					ratingData.getiOfrInGroups().put(groupId, dinners);
					iOfrInGroups.put(groupId, dinners);
				}
			}
				
			if(!findGroup){
				log.debug("套餐["+atom.getnAtomOfrId()+"]套餐组配置错误");
				return false;
			}
		}
		
//		log.debug("ratingdata add data :m_iEffGroups.size:"+ratingData.getiEffGroups());
//		log.debug("ratingData add data : m_iOfrInGroups.size:"+ratingData.getiOfrInGroups());
		
		return true;
	}
	
	
	
	//获取组优惠信息
	
	private boolean getOfrGroupFav(){
		
		log.debug("获取套餐优惠");
		iGroupFavModes.clear();
//		List<PlanDisct> allAtomsList=ratingData.getiAtomPlanDiscts();
		
		boolean findFav=false;
		RuleGroupFavMode  favMode=null;
		for (PlanDisct atom: iAtomPlanDiscts ){
			favMode=groupFavMap.get(atom.getcOfrGroup());
			if(favMode==null){
				break;
			}else{
//				log.info("ratingData add data m_iGroupFavModes: m_iGroupFavModes.size["+ratingData.getiGroupFavModes().size()+"]");
				iGroupFavModes.put(atom.getcOfrGroup(), favMode.getFav_Mode());
				findFav=true;
			}
			
		}
		if(!findFav)
			return false;
		
		return true;
	}
	
	//获取业务公式
	
//	private boolean getFormulaAndAdjust(){
//		int msgType=ratingMsg.getM_nMsgType();
//		DbUtil db=new DbUtilImpl();
//		RuleFormula formula=db.getFormulaByMsgType(msgType);
//		if(formula==null)
//			return false;
//		ruleFormula=formula.getFormula().trim();
//		if(!checkFormula(ruleFormula)){
//			return false;
//		}
//		return true;
//	}
	
	//获取原子套餐定义信息集合
	
	private boolean getAtomDinners(String oferId,int msgType){
		userOfrs=ratingMsg.getUserinfoForMemcached().getUserProducts();
		List<RuleDinnerSelect> dinnerSelectList=null;
		int innetDays=-1;//入网天数
		int innetMonths=-1;//入网月数
		int dinnerEffDays=-1;//套餐生效天数
		int dinnerEffMonths=-1;//套餐生效月数
		
		String effDate="";
		String ofrEffDate="";
		String msgDate="";
		
		
		long eventTypeId=ratingMsg.getM_iExtMsg().getM_nEvtTypeId();
		System.out.println("事件类型:"+eventTypeId);
		log.debug("事件类型:"+eventTypeId);
		
		DbUtil db=new DbUtilImpl();
		dinnerSelectList=db.getAllAtomDinners(oferId, msgType);//套餐B+消息类型
		if(dinnerSelectList==null){
			log.error("获取折线套餐查询无数据");
			return false;
		}
		if(ratingMsg.getM_strUserState().equals(RatingMacro.USER_STATE_ACTIVE_NOT)){
			innetDays=0;
			innetMonths=0;
			dinnerEffDays=0;
			dinnerEffMonths=0;
		}else{
			
			effDate=ratingMsg.getM_iServInfo().getStrAcceptDate();
			ofrEffDate=new SimpleDateFormat("yyyyMMddHH24mmss").format(userOfrs.get(0).getEff_date());
			msgDate=ratingMsg.getBaseMsg().getM_strStartTime();
			
			innetDays=getInnetDays(effDate,msgDate);
			innetMonths=getInnetMonths(effDate,msgDate);
			dinnerEffDays=getDinnerEffDays(ofrEffDate,msgDate);
			dinnerEffMonths=getDinnerEffMonths(ofrEffDate,msgDate);
		}
		for(RuleDinnerSelect dinner:dinnerSelectList){
			int offsetType=dinner.getOffset_type();
			switch(offsetType){
			case ParamData.INNET_OFFSET_DAY://入网日期偏移
				if(innetDays<dinner.getOffset_low() || innetDays >= dinner.getOffset_upper()){
					continue;
				}
				break;
			case ParamData.INNET_OFFSET_MONTH:
				if(innetMonths<dinner.getOffset_low() || innetMonths >= dinner.getOffset_upper())
					continue;
				break;
			case ParamData.DINNER_OFFSET_DAY:
				if(dinnerEffDays<dinner.getOffset_low() || dinnerEffDays >= dinner.getOffset_upper())
					continue;
				break;
			case ParamData.DINNER_OFFSET_MONTH:
				if(dinnerEffMonths<dinner.getOffset_low() || dinnerEffMonths >= dinner.getOffset_upper())
					continue;
				break;
			default:
				break;
			}
			int latn=ratingMsg.getM_iUserMsg().getnLatnId();
			System.out.println("latn:"+latn);
			if(dinner.getLatn_id()==latn || dinner.getLatn_id()==-1){
				PlanDisct pd=new PlanDisct();
				pd.setnAtomOfrId(dinner.getAtom_ofr());
				pd.setnOfrId(dinner.getOfr_b_id());
				
				CodeOfr dinnerInfo=allCodeOfrInfo.get(""+dinner.getAtom_ofr());
				if(dinnerInfo==null){
					log.error("查询套餐无定义信息["+dinner.getAtom_ofr()+"]");
					return false;
				}
				pd.setnPricingPlanId(dinnerInfo.getPricing_plan_id());
				pd.setLatn(dinnerInfo.getLatn_id());
//				userOfrs=db.getUserProduct(""+ratingMsg.getM_iUserMsg().getLnServId());
				
				LifeUserProduct userP=null;
				String ofrId=""+ratingMsg.getM_iUserMsg().getnOfrId();//b套餐
				for(LifeUserProduct p:userOfrs){
					if(p.getOfr_id().equals(ofrId)){
						userP=p;
						break;
					}
				}
				pd.setStrEffDate(new SimpleDateFormat("yyyyMMddHHmmss").format(userP.getEff_date()));
				pd.setStrExpDate(new SimpleDateFormat("yyyyMMddHHmmss").format(userP.getExp_date()));
				
				
				String key=""+pd.getnPricingPlanId()+eventTypeId;
				RuleEvtPricingStrategy strategy=strategysMap.get(key);
				if(strategy!=null){
					pd.setLnStrategyId(strategy.getStrategy_id());
					pd.setAcct_item_type(strategy.getAcc_item_type());	//资源账目项
					pd.setOp_unit(strategy.getOp_unit());
					pd.setTail_mode(strategy.getTail_mod());
					
				}else{
					log.debug("根据事件及定价计划获取策略为空! 套餐["+pd.getnPricingPlanId()+"],"+"事件["+eventTypeId+"]");
					pd.setLnStrategyId(RatingMacro.RATE_NO_STRATEGY);
//					continue;
					
				}
				
				
//				ratingData.addAtomPlanDisct(pd);
				addAtomPlanDisct(pd);
//				log.info("ratingData add data iAtomPlanDiscts|iAtomPlanDisctMap: ");
//				log.info("iAtomPlanDiscts.size["+ratingData.getiAtomPlanDiscts().size()+"],iAtomPlanDisctMap.size["+ratingData.getiAtomPlanDisctMap().size()+"]");
//				allAtomsList.add(pd);
			}
		}
//		if(allAtomsList.isEmpty())
//			return false;
		return true;
	}
	
	
	//增加策略信息
//	private boolean filterPlanWithEvent() {
//		
//		
//		long eventTypeId=ratingMsg.getM_iExtMsg().getM_nEvtTypeId();
//		List<PlanDisct> allAtomsList=ratingData.getiAtomPlanDiscts();
//		
//		for(PlanDisct p : allAtomsList){
//			String key=""+p.getnPricingPlanId()+eventTypeId;
//			RuleEvtPricingStrategy strategy=strategysMap.get(key);
//			if(strategy!=null){
//				p.setLnStrategyId(strategy.getStrategy_id());
//				p.setAcct_item_type(strategy.getAcc_item_type());	//资源账目项
//				p.setOp_unit(strategy.getOp_unit());
//				p.setTail_mode(strategy.getTail_mod());
//				
//			}
//			else
//				allAtomsList.remove(p);
//		}
//		if(allAtomsList.isEmpty()){
//			return false;
//		}
//		return true;
//	}
	
	
	//业务公式格式校验
	private boolean checkFormula(String ruleFormula){
		int len=ruleFormula.length();
		char str[]=ruleFormula.toCharArray();
		for(char s:str){
			if (s==ParamData.MUTEX_FAV_MODE || s==ParamData.BEST_FAV_MODE || s==ParamData.OVERLAY_FAV_MODE){
				continue;
			}else if(Character.isDigit(s)){
				continue;
//			}else if(groupIds.contains(""+s)){
//				continue;
			}else{
				return false;
				
			}
		}
		int i=0;
		String formula=ruleFormula;
//		List<String> groupIds=ratingData.getiEffGroups();
		while(i<len){
			String subString=formula.substring(i,i+RatingMacro.GROUP_LENGTH);
			if(iEffGroups .contains(subString)){
				i+=RatingMacro.GROUP_LENGTH+1;
				continue;
			}else{
				return false;
			}
		}
		return true ;
	}
	

	private int getInnetDays(String start,String end){
		Date accept=DateUtil.toDate(start);
		Date msg=DateUtil.toDate(end);
	
		return DateUtil.getIntervalDays(accept, msg);
	}
	
	private int getInnetMonths(String start,String end){

		Date acceptDate=DateUtil.toDate(start);
		Date msgDate=DateUtil.toDate(end);
		return DateUtil.getIntervalMonths(acceptDate, msgDate);	
	}
	private int getDinnerEffDays(String start ,String end){
		
		Date ofrEffDate=DateUtil.toDate(start);
		Date msgDate=DateUtil.toDate(end);
		return DateUtil.getIntervalDays(ofrEffDate, msgDate);
	}
	private int getDinnerEffMonths(String start,String end){
		int days=getDinnerEffDays(start,end);
		return days/30;
	}
	private Date getMsgDate(){
		Date msgDate=null;
		if(ratingMsg.getVarMsg().getM_strCurrTime()==""){
			msgDate=Calendar.getInstance().getTime();
		}else{
			msgDate=DateUtil.toDate(ratingMsg.getVarMsg().getM_strCurrTime());
		}
		return msgDate;
	}

	
	
	public int addAtomPlanDisct(PlanDisct aPlan) {
		Iterator<PlanDisct> iter=iAtomPlanDiscts.iterator();
		boolean find=false;
		while(iter.hasNext()){
			PlanDisct plan=iter.next();
			if(plan.getnPricingPlanId()== aPlan.getnPricingPlanId() && plan.getnOfrId()==aPlan.getnOfrId() && plan.getnAtomOfrId()==aPlan.getnAtomOfrId()){
				find=true;
				break;
			}
			
		}
		if(!find){
			iAtomPlanDiscts.add(aPlan);
			iAtomPlanDisctMap.put(""+aPlan.getnAtomOfrId(), aPlan);
		}
		return 0;
	}
	
	
	
	public void setRatingData(RatingData ratingData) {
		this.ratingData = ratingData;
	}
	
	public void setRatingMsg(RatingMsg ratingMsg) {
		this.ratingMsg = ratingMsg;
	}



	public List<String> getiEffGroups() {
		return iEffGroups;
	}



	public void setiEffGroups(List<String> iEffGroups) {
		this.iEffGroups = iEffGroups;
	}



	public Map<String, List<PlanDisct>> getiOfrInGroups() {
		return iOfrInGroups;
	}



	public void setiOfrInGroups(Map<String, List<PlanDisct>> iOfrInGroups) {
		this.iOfrInGroups = iOfrInGroups;
	}



	public List<PlanDisct> getiAtomPlanDiscts() {
		return iAtomPlanDiscts;
	}



	public void setiAtomPlanDiscts(List<PlanDisct> iAtomPlanDiscts) {
		this.iAtomPlanDiscts = iAtomPlanDiscts;
	}



	public Map<String, PlanDisct> getiAtomPlanDisctMap() {
		return iAtomPlanDisctMap;
	}



	public void setiAtomPlanDisctMap(Map<String, PlanDisct> iAtomPlanDisctMap) {
		this.iAtomPlanDisctMap = iAtomPlanDisctMap;
	}
	
	
	public Map<String, RuleGroupFavMode> getGroupFavMap() {
		return groupFavMap;
	}
	
	public void setGroupFavMap(Map<String, RuleGroupFavMode> groupFavMap) {
		this.groupFavMap = groupFavMap;
	}
	
	
	public Map<String, Integer> getiGroupFavModes() {
		return iGroupFavModes;
	}
	
	public void setiGroupFavModes(Map<String, Integer> iGroupFavModes) {
		this.iGroupFavModes = iGroupFavModes;
	}
	
	public String getCurrTime() {
		return currTime;
	}
	public void setCurrTime(String currTime) {
		this.currTime = currTime;
	}
	
	
	
}
