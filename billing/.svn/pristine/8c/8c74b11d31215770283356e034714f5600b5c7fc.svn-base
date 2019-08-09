package com.tydic.beijing.billing.rating.service.impl;


public class PricingSectionImpl{ //implements PricingSectionRate{

//	private Logger log=Logger.getLogger(PricingSectionImpl.class);
//	
//
//	private RatingMsg ratingMsg;
//
//	private RatingData ratingData;
//	
//	private RatableResourceExtractionImpl resource;
//	@Autowired
//	private ApplicationContextHelper applicationContextHelper;
//	private DbConfigDetail  dbConfig=null;
//	private   List<RuleStrategySectRel> allRels=new ArrayList<RuleStrategySectRel>();
//
//	private   Map<String,RulePricingSection> allSecMap=new HashMap<String,RulePricingSection>();
//
//	private   Map<Long,RuleSectionRule> secRulesMap=new HashMap<Long,RuleSectionRule>();
//	
//	private Map<Long,PricingSection> rateSectionInfosMap=new HashMap<Long,PricingSection>();
//	private Map<Integer,List<PricingSection>> acctSections=new HashMap<Integer,List<PricingSection>>();
//	private List<RuleSectionRule> userSectionRules=new ArrayList<RuleSectionRule>();//用户段落规则
//	private List<List<PricingSectionRuleNode>> chains=new ArrayList<List<PricingSectionRuleNode>>();
//	
//
//	private PricingSectionRuleTree tree=null;
//	
//	private RateCondCheck sectionCheck = new RateCondCheck();
//	
//	private TariffCalcImpl  tariff;
//	private long dosage=0;
//	private long startValue=0;
//	private long lastDosage=0;
//	private long leftDosage=0;
//	private long addValue=0;
//	
//	
//
//	
//	public PricingSectionImpl(RatingMsg ratingMsg,RatingData ratingData,RatableResourceExtractionImpl resource){
//		this.ratingMsg=ratingMsg;
//		this.ratingData=ratingData;
//		this.resource=resource;
//	}
//	
//	
//	/**
//	 * @author sung
//	 *
//	 */
//	@Override
//	public void init() {
//		dosage=0;
//		startValue=0;
//		lastDosage=0;
//		leftDosage=0;
//		addValue=0;
//		
//	}
//	
//	
//	
//	@Override
//	public OfrRateData rateOfr(PlanDisct plan,OfrRateData rateData) throws RatingException {
//		if(dbConfig==null){
//			dbConfig=(DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
//			allRels=dbConfig.getAllRels();
//			allSecMap=dbConfig.getAllSecMap();
//			secRulesMap=dbConfig.getSecRulesMap();
//		}
//		log.debug("批价套餐:"+plan.getnAtomOfrId());
//		if(!getPricingSections(plan)){
//			throw new RatingException(ErrorInfo.NOT_VALID_PRICING_PLAN,"找不到策略["+plan.getLnStrategyId()+"]");
//		}
//		int msgType=ratingMsg.getM_nMsgType();
//
//		rateData.setnOfrId(plan.getnOfrId());
//
//		rateData.setLnAtomOfrId(plan.getnAtomOfrId());
//
//		//根据主被叫关系是否在优惠小区查询优惠套餐
//		if(plan.getnOfrAttr() ==1){
//			ratingData.setnCellFlag(getOfrCellFlag(plan.getnOfrId()));
//		}
//		boolean bHasValidSection=false;
//
//		int domain =rateData.getiRateMeasure().getM_iUsedMeasureDomain();
//		Set<Map.Entry<Integer, List<PricingSection>>> acctEntries=acctSections.entrySet();
//
//		Map<String,RatableResourceValue> iRatableValues=rateData.getiRatableValues();
//		
//		
//		if(acctEntries!=null){
//			Iterator<Map.Entry<Integer, List<PricingSection>>> it=acctEntries.iterator();
//			while(it.hasNext()){
//				Map.Entry<Integer, List<PricingSection>> en=it.next();
//				List<PricingSection> secs=en.getValue();
//				Collections.sort(secs,new Comparator<PricingSection>(){
//					public int compare(PricingSection o1, PricingSection o2) {
//						return (int)(o2.getLnCalcPriority()-o1.getLnCalcPriority());  //降序
//					}
//					
//				});
//
//				RateMeasure iRateMeasure= rateData.getiRateMeasure();
//				
//				for(PricingSection section:secs){
//					
//					
//					if(domain==MeasureDomain.MeasureDomain_None){
//						domain=Integer.parseInt(section.getStrMeasureDomain());
//					}
//					if(msgType==ParamData.CODE_CCG_BRR && domain !=Integer.parseInt(section.getStrMeasureDomain())){
//						continue;
//					}
//					SectionRateData sectionResult=new SectionRateData();
//
//					sectionResult.setiRateMeasure(iRateMeasure);
//					sectionResult.setiRatableValues(iRatableValues);
//
//					if(section.getNodeType()!=2){
//						continue;
//					}
//
//					try{
//						sectionResult=rateSection(section , sectionResult);
//					}catch(RatingException e){
//						continue;
//					}
//					
//					bHasValidSection=true;
//					iRateMeasure.addChargedDosage(sectionResult.getStrMeasureDomain(), sectionResult.getLnDosage());
//					iRateMeasure.addUnusedDosage(sectionResult.getStrMeasureDomain(),sectionResult.getLnUnusedDosage());
//					iRateMeasure.addChargedLastDosage(sectionResult.getStrMeasureDomain(), sectionResult.getLnLastDosage());
//		            iRateMeasure.addUnusedLastDosage(sectionResult.getStrMeasureDomain(), sectionResult.getLnUnusedLastDosage());
//		            iRatableValues = sectionResult.getiRatableValues();
//		            
//		            rateData.addSectionRateResultWithoutDosage(sectionResult);
//				}
//			}
//
//			rateData.setiRatableValues(iRatableValues);
//			rateData.calcRateMeasure();
//			
//		    if( bHasValidSection )
////		    	
//		    	rateData.getiRateMeasure().setM_iUsedMeasureDomain(domain);
//		    ///优惠小区报音设置  ---------省略
//		}
////		log.debug("===============ofrRateResult=======================");
////		ofrRateResult.print();
////		log.debug("===================================================");
////		return ofrRateResult;
//		return rateData;
//	}
//	
//	
//	
//	
//	
//	//暂不实现
//	private int getOfrCellFlag(int ofrId){
//		
//		
//		
//		return 0;
//	}
//	
//
//	
//	//获取策略段落关系
//	public boolean getStrategySecRels(PlanDisct plan){
//
//		acctSections.clear();
//		userSectionRules.clear();
//		boolean find = false;
//		for (RuleStrategySectRel rel : allRels) {
//			long strategyId = plan.getLnStrategyId();
//			
//
//			if(!ratingMsg.isNeedRating()){
//				log.debug("不需要批价");
//				log.debug("不需要批价");
//				return true;
//			}
//			if (strategyId == rel.getStrategy_id() ) {
//				find = true;
//
//				long sectionId = rel.getPricing_section();
//				RulePricingSection section = allSecMap.get("" + sectionId);
//
//				PricingSection pSection = new PricingSection(section);
//				pSection.setLnStrategyId(strategyId);
//				pSection.setnOfrId(plan.getnOfrId());
//				pSection.setAtomOfrId(plan.getnAtomOfrId());
//				pSection.setLnPricingSectionId(sectionId);
//				pSection.setLnEventTypeId(ratingMsg.getM_iExtMsg().getM_nEvtTypeId());
//				RuleSectionRule rule = secRulesMap.get(sectionId);
//				if (rule != null) {
//					userSectionRules.add(rule);
//					pSection.setLnCalcPriority(rule.getPriority());
//					pSection.setStrRefResourceCode(rule.getRef_resource_code());
//					pSection.setConditionId(rule.getCond_id());
//					pSection.setNodeType(rule.getNode_type());
//					//add 1016
//					pSection.setLower(rule.getLower());
//					pSection.setUpper(rule.getUpper());
//					pSection.setnRefFlag(rule.getRef_flag());
//					
//					
//				}
//				int acctItemId = Integer.parseInt(section.getAcct_item_id());
//				
//				pSection.setLnAcctItemId(acctItemId);
//
//				rateSectionInfosMap.put(sectionId, pSection);
//				plan.getiPricingSections().add(pSection);
//				plan.getiPricingSectionMap().put("" + sectionId, pSection);
//				if (!acctSections.containsKey(acctItemId)) {
//					List<PricingSection> sectionList = new ArrayList<PricingSection>();
//					sectionList.add(pSection);
//					acctSections.put(acctItemId, sectionList);
//				} else {
//					if (!acctSections.get(acctItemId).contains(pSection))
//						acctSections.get(acctItemId).add(pSection);
//				}
//
//			}
//
//		}
//		if (!find) {
//			return false;
//		}
//		
//
//		return true;
//	}
//	
//	
//	
//	
//	public boolean buildTree(){
//
//		Iterator<RuleSectionRule> it=userSectionRules.iterator();
//		tree=new PricingSectionRuleTree();
//		while(it.hasNext()){
//			RuleSectionRule rule=it.next();
//			PricingSectionRuleNode node=new PricingSectionRuleNode();
//			PricingSectionRuleRecord record=new PricingSectionRuleRecord(rule.getPricing_section(),rule.getUpper_pricing_section(),
//					rule.getPriority(),rule.getCond_id(),rule.getLower(),rule.getUpper(),rule.getRef_flag(),rule.getRef_resource_code());
//			node.setRecord(record);
//			long sectionId=rule.getPricing_section();
//			tree.getNodes().put(sectionId, node);
//			if(tree.getNodeRels().containsKey(sectionId)){
//				node.addChild(tree.getNodeRels().get(sectionId));
//			}
//			tree.getNodeRels().remove(sectionId);
//			if(node.getRecord().getUpPricingSectionId()!=-1){
//				long pSectionId=node.getRecord().getUpPricingSectionId();
//				PricingSectionRuleNode pNode=tree.getNodes().get(pSectionId);
//				if(pNode!=null){
//					pNode.addChild(node);
//				}else{
//					tree.getNodeRels().put(pSectionId, node);
//				}
//			}else{
//				tree.getRootNodes().put(node.getRecord().getPricingSectionId(), node);
//			}
//		}
//		return true;
//	}
//	
//
//	
//	public boolean getPricingSections(PlanDisct plan ){
//		if(!getStrategySecRels(plan)){
//			
//			return false;
//		}
//
//		return true;
//	}
//	
//	
//	
//	
//	
//	public void setRatingMsg(RatingMsg ratingMsg) {
//		this.ratingMsg = ratingMsg;
//	}
//
//
//	
//	private boolean getPricingSectionRuleChain(long sectionId){
//		chains= new ArrayList<List<PricingSectionRuleNode>>();
//		PricingSectionRuleTree tree=this.tree;
//		PricingSectionRuleNode node=tree.getRootNodes().get(sectionId);
//		if(node==null){
//			return false;
//		}
//		List<PricingSectionRuleNode> chain=new ArrayList<PricingSectionRuleNode>();
//		chain.add(node);
//		List<List<PricingSectionRuleNode>> allChains=new ArrayList<List<PricingSectionRuleNode>>();
//		allChains.add(chain);
//		while(!allChains.isEmpty()){
//			List<PricingSectionRuleNode> firstChain=allChains.get(0);
//			allChains.remove(0);
//			node=firstChain.get(firstChain.size()-1);
//			if(node.getChildren().isEmpty()){
//				chains.add(firstChain);
//				continue;
//			}
//			List<PricingSectionRuleNode> tmp=new ArrayList<PricingSectionRuleNode>();
//			for(PricingSectionRuleNode child:node.getChildren()){
//				tmp=firstChain;
//				tmp.add(child);
//				allChains.add(tmp);
//			}
//			
//		}
//		return true;
//	}
//	
//	private boolean check(PricingSection section){
//		long conid=section.getConditionId();
//		
//		return true;
//	}
//	
//	
//	private SectionRateData rateSection(PricingSection section ,SectionRateData sectionResult) throws RatingException{
//		tariff=new TariffCalcImpl(ratingMsg,ratingData);
//		log.debug("rateSection 批价段落:"+section.getLnPricingSectionId());
//		boolean success=false;
//		boolean validSection=false;
//		
//		buildTree();
//		if(!getPricingSectionRuleChain(section.getLnPricingSectionId())){
//			log.debug("段落规则错误");
//
//			 throw new RatingException(ErrorInfo.ERR_NOT_FOUND_SECTION_RULE,"段落规则错误[ "+section.getLnPricingSectionId()+"]");
//		}
////		if(!check(section)){
////			return -1;
////		}
//		Iterator<List<PricingSectionRuleNode>> it=chains.iterator();
//		RateConditionParam condParam=new RateConditionParam();
//		
//		ratingData.setpRateMeasure(sectionResult.getiRateMeasure());
//		
//		sectionResult.setLnPricingSectionId(section.getLnPricingSectionId());
//		sectionResult.setStrMeasureDomain(""+section.getStrMeasureDomain());
//		sectionResult.setLnAcctItemTypeId(section.getLnAcctItemId());
//		
//		ratingData.setnServiceLimitFlag(-1);
//		
//		ratingData.setiTmpRatableResourceValues(sectionResult.getiRatableValues());
//
//		initCondInParam(section,condParam);
//		condParam.setM_lnPricingSectionId(section.getLnPricingSectionId());
//		
//		condParam.setM_iRatables(sectionResult.getiRatableValues());
//		
//		
//		while(it.hasNext()){
//			long lower=0;
//			long upper=999999999;
//			List<PricingSectionRuleNode> nodes=it.next();
//			for(PricingSectionRuleNode node:nodes){
//				condParam.setM_nCondId(node.getRecord().getCondId());
//				condParam.setM_lnPricingSectionId(node.getRecord().getPricingSectionId());
//				
//				sectionCheck.setParam(condParam);
//				if(!sectionCheck.check()){
//					break;
//				}
//				if(!checkSectionRuleLimit(node,condParam)){//段落规则校验
//					 break;
//				}
//				String strResourceCode=node.getRecord().getRefResourceCode();
//				if(!node.getChildren().isEmpty()){//非叶子节点
//					continue;
//				}
//				//段落上下限
//				if(node.getRecord().getRefFlag()==0){
//					lower=node.getRecord().getLower();
//					upper=node.getRecord().getUpper();
//				}
//				//首月系数
//				if(section.isbIsFirstMonth()){
//					lower=(long)Math.ceil(lower*section.getfCoefficient());
//					upper=(long)Math.ceil(upper*section.getfCoefficient());
//				}
//				validSection=true;
//				int ret=0;
//				if((ret=getDosage(section,strResourceCode,lower,upper))==1){//无需批价
//
//					return null;
//				}
//				if(ret==-1){
//					break;
//				}
//				//如果是离线消息而且是流量
//				if(ratingMsg.getM_nMsgVersion()==1 && (section.getStrMeasureDomain().equals("2")
//						|| section.getStrMeasureDomain().equals("4") || section.getStrMeasureDomain().equals("5"))){
//					
//				}
//				
//				tariff.initCalcData(section,dosage,startValue,lastDosage);
//				try {
//					tariff.calcCharge();
//				} catch (RatingException e) {
//					log.debug(e.printError());
//					break;
//				}
//				
//				sectionResult.setiTariffResults(tariff.getResult()); 
//				//更新临时累计量
//				setTmpRatableValue(strResourceCode,addValue,ratingData.getiTmpRatableResourceValues());
//				setTmpRatableValue( strResourceCode, addValue, condParam.getM_iRatables());
//	            setTmpRatableValue( strResourceCode, addValue, condParam.getM_iTmpRatables());
//	            success=true;
//			}
//			if(success){//有一条规则批价成功后不继续
//				break;
//			}
//		}
//		if(!validSection){  //没有满足条件段落
//
//			return null;
//		}
//		
//		if(!success){
//			
//			throw new RatingException(ErrorInfo.ERR_IN_RATE_SECTION ,"段落批价错误");
//			
//		}
//		sectionResult.setiRatableValues(condParam.getM_iRatables());
//		sectionResult.setAttrFromTariffResults();
//
//		log.debug("*SectionRateData*=");
//		log.debug(sectionResult);
//
//		return sectionResult;
//	}
//	
//	
//	
//	private boolean checkSectionRuleLimit(PricingSectionRuleNode node,RateConditionParam param){
//		if(node.getRecord().getRefFlag()==RatingMacro.SectionRefFlag_Resource){
//			long lnRatable=0;
//			RatableResourceValue value=param.getM_iRatables().get(node.getRecord().getRefResourceCode());
//			if(value==null){
//				return true;
//			}
//			
//			lnRatable=value.getM_lnBalance();
//			if(lnRatable >=node.getRecord().getLower() && lnRatable < node.getRecord().getUpper()){
//				
//				return true;
//			}else{
//				return false;
//			}
//			
//			
//			
//		}else if(node.getRecord().getRefFlag()==RatingMacro.SectionRefFlag_Time){
//			int nValue=Integer.parseInt(param.getM_szHH());
//			if(nValue >= node.getRecord().getLower() && nValue < node.getRecord().getUpper()){
//				return true;
//			}else{
//				return false;
//			}
//			
//			
//		}
//		
//		return true;
//	}
//	
//	
//	
//	
//	
//	
//	private void initCondInParam(PricingSection section,RateConditionParam param){
//		param.setM_nServiceId(section.getnServiceId());
//		param.setM_pRatingMsg(ratingMsg);
//		param.setM_pRatingData(ratingData);
//		param.setM_iServAttrs(ratingMsg.getM_iRatingExtMsg().getM_iServAttrMap());
//		
////		log.debug("isbTmpRatableFlag====="+ratingData.isbTmpRatableFlag());
//		if(!ratingData.isbTmpRatableFlag()){
//			param.setM_iTmpRatables(ratingData.getiTmpRatableResourceValues());//保存本次批价过程中改变过的累积量值
//			ratingData.setbTmpRatableFlag(true);
//		}
//		String time=ratingMsg.getBaseMsg().getM_strStartTime();//计费开始时间
//		param.setM_szYYYY(time.substring(0,4));
//		param.setM_szMM(time.substring(4,6));
//		param.setM_szDD(time.substring(6,8));
//		param.setM_szHH(time.substring(8,10));
//		param.setM_szMI(time.substring(10,12));
//		setCondPrd(  param ,section.getnPricingPlanId());
//		
//	}
//	
//	
//	//暂不实现
//	private void setCondPrd(RateConditionParam param ,int planId){
//		
////		std::string strValue;
////	    if( m_pRatingMsg->getValue( "PRD", strValue ) < 0 )
////	        m_pRatingMsg->addValue( "PRD", planId_ );
////	    else
////	        m_pRatingMsg->setValue( "PRD", planId_ );
//	}
//	
//	
//	
//	
//	
//	//把更新以后的累积量值addValue写入临时累积量集合
//	private void setTmpRatableValue(String resourceCode,long addValue,Map<String,RatableResourceValue> resources){
//		if(resourceCode=="-1"){
//			log.debug("resourceCode>>>>>>"+resourceCode);
//			return ;
//		}
//		RatableResourceValue rat=resources.get(resourceCode);
//		if(null !=rat){
//			rat.setM_lnBalance((int)addValue);
//		}
//		
//	}
//	private int getDosage(PricingSection section, String resourceCode,long lower,long upper){
//		dosage=0;
//		leftDosage=0;
//		addValue=0;
//		startValue=0;
//		lastDosage=0;
//		if(resourceCode.isEmpty()||resourceCode.equals("-1")){
//			String measure=section.getStrMeasureDomain();
//
//			dosage=ratingData.getpRateMeasure().getUnchargeDosage(measure);
//			lastDosage=ratingData.getpRateMeasure().getUnchargedLastDosage(""+measure);
//			getStartValue(measure);
//		}else{
//			String measure=section.getStrMeasureDomain();
//			dosage=ratingData.getpRateMeasure().getUnchargeDosage(measure);
//			lastDosage=ratingData.getpRateMeasure().getUnchargedLastDosage(""+measure);
//			getStartValue(measure);
//			//获取累积量值
//			long resourceValue=getRatableResourceValue(resourceCode,ratingData.getiTmpRatableResourceValues());
//			if(resourceValue<0){
//				return -1;
//			}
//			//补款判断省略
//			int ret=0;
//			if((ret=getBillDosage(resourceCode,dosage,lower,upper,resourceValue))==1){//无需批价  累积量不在段落上下限范围内
//				return 1;
//			}
//			if(ret==-1){
//				return ret;
//			}
//			if(section.getStrMeasureDomain().equals("1")){
//				ratingData.setiCurrTime(DateUtil.addSeconds(ratingData.getiCurrTime(), (int)dosage, 14));
//			}
//		}
//		log.debug("getDosage:dosage="+dosage+",lastDosage="+lastDosage);
//		
//		return 0;
//	}
//	
//	private int getBillDosage(String resourceCode,long dosage,long lower,long upper,long resourceValue){
//		long billingDosage=0;
//		long vleftDosage=0;
//		
//		if(resourceValue >= upper || resourceValue < lower){
//			
//			this.leftDosage=dosage;
//			this.dosage=0;
//			return 1;
//		}
//		int ret=getRatableResourceUnit(resourceCode);
//		if(ret==-1){
//			log.debug("无此累积量信息["+resourceCode+"]");
//			return ret;
//		}
//		long value=0;
//		long unit=0;
//		switch(ret){
//			
//		case 1://时长(秒)
//		case 3://次数
//		case 4://总流量
//		case 5://分(金额)
//		case 7: //上行流量
//        case 8: //下行流量
//        case 9: //M
//        case 0: //T	
//        	value=dosage;
//        	unit=1;
//        	break;
//        case 2://时长 分钟
//        	value=(dosage%60)==0?dosage/60:dosage/60+1;
//        	unit=60;
//        	break;
//		}
//		if(resourceValue+value>upper){
//			billingDosage=(upper-resourceValue)*unit;
//			vleftDosage=dosage-billingDosage;
//			resourceValue=upper;
//		}else{
//			billingDosage=dosage;
//			resourceValue+=value;
//			vleftDosage=-1;
//		}
//		this.dosage=billingDosage;
//		this.leftDosage=vleftDosage;
//		this.addValue=resourceValue;//累积量更新以后的值
//		
//		return 0;
//	}
//	
//	private int getRatableResourceUnit(String resourceCode){
//		Set<Map.Entry<String, RatableResourceInfo>> entries=resource.getiRatableResourceInfos().entrySet();
//		Iterator<Map.Entry<String, RatableResourceInfo>> it=entries.iterator();
//		while(it.hasNext()){
//			Map.Entry<String, RatableResourceInfo>e=it.next();
//			String key=e.getKey();
//			if(key.equals(resourceCode)){
//				RatableResourceInfo value=e.getValue();
//				return Integer.parseInt(value.getM_strRatableResourceType());
//				//1－时长(秒)； 2－时长(分钟)；3－次数；4－总流量(k)；5－分(金额)；7－上行流量按K；8－下行流量 
//			}
//		}
//		return -1;
//	}
//	private long getRatableResourceValue(String resourceCode,Map<String,RatableResourceValue> values){
//		if(values.isEmpty()){
//			return 0;
//		}
//		Set<Map.Entry<String, RatableResourceValue>> entries=values.entrySet();
//		Iterator<Map.Entry<String, RatableResourceValue>> it=entries.iterator();
//		while(it.hasNext()){
//			Map.Entry<String, RatableResourceValue> e=it.next();
//			String key=e.getKey();
//			if(key.equals(resourceCode)){
//				RatableResourceValue value=e.getValue();
//				return value.getM_lnBalance();
//			}
//		}
//		return -1;
//	}
//	
//	private boolean getStartValue(String measure){
//		if( measure.equals(RatingMacro.SECTION_DOMAIN_DURATION) ) //按时长计费
//	        startValue = ratingData.getiStartValue().getLnDuration();
//	    else if( measure .equals(RatingMacro.SECTION_DOMAIN_TOTALVOLUME) ) //按流量计费
//	        startValue = ratingData.getiStartValue().getLnTotalVolume();
//	    else if( measure .equals(RatingMacro.SECTION_DOMAIN_TIMES) ) //按次计费
//	        startValue= ratingData.getiStartValue().getLnTimes();
//	    else if( measure.equals(RatingMacro.SECTION_DOMAIN_UPVOLUME)  ) //上行流量
//	        startValue = ratingData.getiStartValue().getLnUpVolume();
//	    else if( measure .equals(RatingMacro.SECTION_DOMAIN_DOWNVOLUME) ) //下行流量
//	        startValue = ratingData.getiStartValue().getLnDownVolume();
//	    else if( measure .equals(RatingMacro.SECTION_DOMAIN_MONEY) )
//	        startValue = 0;
//	    else
//	    	return false;
//		return true;
//		
//	}
	
	
}
