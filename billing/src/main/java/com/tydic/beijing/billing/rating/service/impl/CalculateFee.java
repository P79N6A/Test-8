package com.tydic.beijing.billing.rating.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.cyclerent.service.impl.CycleRentForRatingImpl;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.QUserReasonSend;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.beijing.billing.rating.domain.CodeOfr;
import com.tydic.beijing.billing.rating.domain.CodeRatableResource;
import com.tydic.beijing.billing.rating.domain.InfoRatableHistory;
import com.tydic.beijing.billing.rating.domain.OfrRateData;
import com.tydic.beijing.billing.rating.domain.ParamData;
import com.tydic.beijing.billing.rating.domain.PlanDisct;
import com.tydic.beijing.billing.rating.domain.RateData;
import com.tydic.beijing.billing.rating.domain.RateMeasure;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingErrorCode;
import com.tydic.beijing.billing.rating.domain.RatingException;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.RuleAutoRenew;
import com.tydic.beijing.billing.rating.domain.RuleDinnerSelect;
import com.tydic.beijing.billing.rating.domain.RuleEvtPricingStrategy;
import com.tydic.beijing.billing.rating.domain.RuleFormula;
import com.tydic.beijing.billing.rating.domain.RuleGroupFavMode;
import com.tydic.beijing.billing.rating.domain.RuleHoliday;
import com.tydic.beijing.billing.rating.domain.RuleOfrGroup;
import com.tydic.beijing.billing.rating.domain.RuleOfrResourceRel;
import com.tydic.beijing.billing.rating.domain.RuleOfrSplit;
import com.tydic.beijing.billing.rating.domain.RulePricingSection;
import com.tydic.beijing.billing.rating.domain.RuleRateCondition;
import com.tydic.beijing.billing.rating.domain.RuleSectionRule;
import com.tydic.beijing.billing.rating.domain.RuleStrategySectRel;
import com.tydic.beijing.billing.rating.domain.RuleTariff;
import com.tydic.beijing.billing.rating.domain.SectionRateData;
import com.tydic.beijing.billing.rating.domain.TariffResult;
import com.tydic.beijing.billing.rating.domain.TokenNode;
import com.tydic.beijing.billing.rating.domain.UserDiscts;
import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;
import com.tydic.beijing.billing.rating.util.DateUtil;
//import org.apache.log4j.Logger;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class CalculateFee {

    @Autowired
    private DbUtil dbUtil;

	@Autowired
	private ApplicationContextHelper applicationContextHelper;
	@Autowired
	private DeductResourceAcct deductResourceAcct;
	
	private DbConfigDetail dbConfigDetail;
	
	//private static final Logger log = Logger.getLogger(CalculateFee.class);
	private static final Logger log = LoggerFactory.getLogger(CalculateFee.class);
	///////批价
	@Transactional(rollbackFor=Exception.class)
	public RateData calcFee(RatingMsg ratingMsg,RatingData ratingData) throws Exception{
		
		
		log.debug("获得事件码："+ratingMsg.getM_iExtMsg().getM_nEvtTypeId());
		
		RateData resultRateData = new RateData();
		
		checkActive(ratingMsg);

		dbConfigDetail = (DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
		//userinfoForMemcached = ratingMsg.getUserinfoForMemcached();
		
		getPricingPlanId(ratingMsg,ratingData);
		
		//RateMeasure tmp = ratingData.getiRateMeasure();
		
		///业务控制没有了
		//获取套餐费分组信息
		getOfrGroupAndPrior(ratingMsg,ratingData);
	  ////组内优惠
		getOfrGroupFav(ratingData);
		///业务公式
		getFormulaAndAdjust(ratingMsg,ratingData);
		
		resultRateData = rate(ratingMsg,ratingData);
		
		return resultRateData;
	}
	
	
	/**
	 * 判断用户是否已激活，如果未激活，
	 * @param ratingMsg.
	 */
private void checkActive(RatingMsg ratingMsg) throws Exception {
		
	InfoUser infoUser = ratingMsg.getUserinfoForMemcached().getInfoUser();
	
	 if (ratingMsg.getBaseMsg().getM_strMsgType().equals("70")) { //短信
		  if (ratingMsg.getMsgMap().get(RatingMacro.PARA_R_CALL_TYPE_70).equals("2")){
				//被叫 不激活
				return;
			}
	 }
	
	if(infoUser.getUser_status().equals("501")){//未激活
		QUserReasonSend qUserReasonSend = new QUserReasonSend();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//YYYY-MM-DD HH24:MI:SS
		qUserReasonSend.setActive_type("02");
		qUserReasonSend.setCharge_id("");
		qUserReasonSend.setEnqueue_date(sdf.format(new Date()));
		qUserReasonSend.setLocal_net(infoUser.getLocal_net());
		qUserReasonSend.setReason_code("99");
		//qUserReasonSend.setSerial_num(serial_num);   直接写在dataservice里，不再提前获取
		qUserReasonSend.setTele_type(infoUser.getTele_type());
		qUserReasonSend.setUser_no(infoUser.getUser_id());
		
		CycleRentForRatingImpl  rent = (CycleRentForRatingImpl)applicationContextHelper.getBean("cycleRent");
		log.debug("首话单激活，资源到账。。。");
		dbConfigDetail = (DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
		dbUtil.createActiveLogAndResource(qUserReasonSend,rent,dbConfigDetail.isAddResOnFirstCdr());
//		dbUtil.createActiveLogAndResource(qUserReasonSend,rent);
		log.debug("首话单激活，资源到账  结束。。");
	}
		
	}

	////计费核心
	private RateData rate(RatingMsg ratingMsg,RatingData ratingData)  throws Exception {
		RateData rateData  =new RateData();
		rateData.setiRateMeasure(ratingData.getiRateMeasure());
		
		//int nRet = 0;
	    List<TokenNode> iNodeStack = new ArrayList<TokenNode>();
	    TokenNode iCurrNode ,leftNode,midNode,rightNode;
	    TokenNode iResultNode = new TokenNode();
	    RateMeasure iRateMeasure = ratingData.getiRateMeasure(); //批价使用量
	    String formula = "("+ ratingData.getM_strFormula() +")";
 
        String formulaMember="" ;
        RateData tmpRateData =new RateData();
	    
	    //遍历套餐公式
        /**
         * 	    	运算符	说明	含义
	    	,	逗号	取优运算
	    	:	冒号	费用项取优运算
	    	*	星号	叠加运算
	    	^	尖号	排他运算符
	    	\	反斜杠	费用项排他
	    	;	分号	费用项取优
         */
	    for(int i=0;i<formula.length();i++){
	    	//iCurrNode  = new TokenNode();
	    	char chara=formula.charAt(i);
	    	if(isFormulaChar(chara)){
	    		formulaMember = formulaMember+chara;
	    		if(  nextisFormulaChar(formula,i)){		////如果下一个字符也是普通字符，则continue    		
	    			continue;
	    		}else{ //如果下一个字符不是特殊字符了，则截取之前的字符串
	    			iCurrNode  = new TokenNode();
	
	    			iCurrNode.setiTokenType(RatingMacro.TOKENNODE_TYPE_OPERAND);
		    		iCurrNode.setM_nValue(formulaMember);
		    		iCurrNode.setM_iRateData(rateData);
		    		iCurrNode.setM_bIsResult(false);
			    	iNodeStack.add(iCurrNode);
			    	///formulaMember="";
	    		}
	    	}else if (isFormulaOperator(chara+"")){
	    		iCurrNode  = new TokenNode();
	    		iCurrNode.setiTokenType(RatingMacro.TOKENNODE_TYPE_OPERATOR);
	    		if(chara ==','){//取优
	    			iCurrNode.setM_nValue(RatingMacro.BEST_FAV_MODE);
	    		}else if (chara =='*'){//叠加
	    			iCurrNode.setM_nValue(RatingMacro.ADD_FAV_MODE);
	    		}else if (chara =='^'){//排它
	    			iCurrNode.setM_nValue(RatingMacro.MUTEX_FAV_MODE);
	    		}else{
	    			
	    		}
	    		iCurrNode.setM_bIsResult(false);
		    	iNodeStack.add(iCurrNode);
	    		
	    	} else if (chara =='('){
	    		iCurrNode  = new TokenNode();
	    		iCurrNode.setiTokenType(RatingMacro.TOKENNODE_TYPE_LEFTPAREN);
	    		iCurrNode.setM_bIsResult(false);
		    	iNodeStack.add(iCurrNode);
	    	}else if (chara ==')'){ ////(
	    		iCurrNode  = new TokenNode();
	    		iCurrNode.setiTokenType(RatingMacro.TOKENNODE_TYPE_RIGHTPAREN);
	    		iCurrNode.setM_bIsResult(false);
		    	iNodeStack.add(iCurrNode);
	    	}
   
	    	
	    	while(iNodeStack.size()>=3){
	    		
	    		leftNode  = iNodeStack.get(iNodeStack.size()-3);
	    		midNode = iNodeStack.get(iNodeStack.size()-2);
	    		rightNode = iNodeStack.get(iNodeStack.size()-1);
	    		//如果左右两个节点都是产品组，中间节点是逻辑操作符，则进行批价逻辑处理
	    		if(leftNode.getiTokenType() == RatingMacro.TOKENNODE_TYPE_OPERAND && rightNode.getiTokenType() == RatingMacro.TOKENNODE_TYPE_OPERAND
	    				&& midNode.getiTokenType() == RatingMacro.TOKENNODE_TYPE_OPERATOR){
	    			
	    			if(midNode.getM_nValue() == RatingMacro.BEST_FAV_MODE){
	    				//取优模式
	    				if(!leftNode.isM_bIsResult() ){
	    				    tmpRateData = leftNode.getM_iRateData();
	    					tmpRateData.setiRateMeasure(iRateMeasure);
	    					leftNode.setM_iRateData(tmpRateData);
	    					
	    					leftNode = rateGroup(ratingMsg,ratingData,leftNode);
	    				}
	    				if(!rightNode.isM_bIsResult()){
	    					
	    					tmpRateData = rightNode.getM_iRateData();
	    					tmpRateData.setiRateMeasure(iRateMeasure);
	    					rightNode.setM_iRateData(tmpRateData);
	    					
	    					rightNode = rateGroup(ratingMsg,ratingData,rightNode);
	    					
	    				}
	    				
	    				if(compareRateResult(leftNode,rightNode)){////如果left节点计算结果更省
	    					iResultNode.setM_iRateData(leftNode.getM_iRateData());
	    				}else{
	    					iResultNode.setM_iRateData(rightNode.getM_iRateData());
	    				}

	    			}else if (midNode.getM_nValue() == RatingMacro.ADD_FAV_MODE){
	    				//叠加模式
	    				if(!leftNode.isM_bIsResult() ){
	    				    tmpRateData = leftNode.getM_iRateData();
	    					tmpRateData.setiRateMeasure(iRateMeasure);
	    					leftNode.setM_iRateData(tmpRateData);
	    					
	    					leftNode = rateGroup(ratingMsg,ratingData,leftNode);
	    				}
	    				rightNode.setM_iRateData(leftNode.getM_iRateData());
	    				rightNode = rateGroup(ratingMsg,ratingData,rightNode);
	    				iResultNode.setM_iRateData(rightNode.getM_iRateData());
	    
	    			}else {
	    				//排它模式
	    				if(leftNode.isM_bIsResult()){
	    					iResultNode.setM_iRateData(leftNode.getM_iRateData());
	    				}else{
	    					
	    					tmpRateData = leftNode.getM_iRateData();
	    					tmpRateData.setiRateMeasure(iRateMeasure);
	    					leftNode.setM_iRateData(tmpRateData);
	    					leftNode = rateGroup(ratingMsg,ratingData,leftNode);
	    					
	    					if(leftNode.getRateReturnCode()<0)/////如果leftNode节点批价失败或者有使用量未批完，则继续使用rightNode节点批价
	    					{
	    						tmpRateData = rightNode.getM_iRateData();
		    					tmpRateData.setiRateMeasure(iRateMeasure);
		    					rightNode.setM_iRateData(tmpRateData);
		    					
		    					rightNode = rateGroup(ratingMsg,ratingData,rightNode);
		    					iResultNode.setM_iRateData(rightNode.getM_iRateData());
		    					
	    					}else{
	    						iResultNode.setM_iRateData(leftNode.getM_iRateData());
	    					}
	    					
	    					
	    				}
	    			}
	    				
	    			////批价结束后，批价结果作为一个node，替换掉原来的3个node
	    			iResultNode.setiTokenType(RatingMacro.TOKENNODE_TYPE_OPERAND);
	    			iResultNode.setM_bIsResult(true);
	    			iNodeStack.remove(iNodeStack.size()-1);
	    			iNodeStack.remove(iNodeStack.size()-1);
	    			iNodeStack.remove(iNodeStack.size()-1);
	    			iNodeStack.add(iResultNode);	
	    		}
	    		//如果leftnode是(，rightNode是)，midnode是产品组节点，则用中间的mid节点替换掉原来的3个node
	    		else if(leftNode.getiTokenType() == RatingMacro.TOKENNODE_TYPE_LEFTPAREN &&
	    				midNode.getiTokenType() == RatingMacro.TOKENNODE_TYPE_OPERAND
	    				&& rightNode.getiTokenType() == RatingMacro.TOKENNODE_TYPE_RIGHTPAREN){
	    			
	    			iNodeStack.clear();
	    			iNodeStack.add(midNode);
	    		}
	    	 
	    		
	    	}///while 循环结束
	 
	    }//遍历套餐公式结束
	    
	    
	    if( iNodeStack.size() != 1 || iNodeStack.get(0).getiTokenType() != RatingMacro.TOKENNODE_TYPE_OPERAND ){
	    	throw new RatingException(RatingErrorCode.ERR_INVALID_FORMULA_SYNTAX,"批价异常，得到了多个结果节点");
	    }
	    else
	    {
	        iResultNode = iNodeStack.get(0);
	        if( iResultNode.isM_bIsResult() )
	        {
	        	rateData = iResultNode.getM_iRateData();
	        } else {
	        	rateData.setiRateMeasure(iRateMeasure);
	        	iResultNode.setM_iRateData(rateData);
	        	iResultNode = rateGroup(ratingMsg,ratingData,iResultNode);
	        }
	    }
	    
 	
		return iResultNode.getM_iRateData();
	}

    /**
     * 产品组批价
     * @param node
     * @return
     */
	private TokenNode rateGroup(RatingMsg ratingMsg,RatingData ratingData,TokenNode tokenNode ) throws Exception {
	
		
		 
		 RateData iRateTmp = tokenNode.getM_iRateData();
		 RateMeasure iRateMeasure = iRateTmp.getiRateMeasure();
		 String group = tokenNode.getM_nValue();

		 //List<PlanDisct> listPlanDisct = ratingData.getiOfrInGroups().get(group);
		 //List<CodeOfr> iCodeOfrs = ratingData.getiCodeOfrInGroups().get(group);
		 List<UserDiscts> iCodeOfrs = ratingData.getiCodeOfrInGroups().get(group);
				 
		 
		log.info("用户产品组"+group+"下的套餐数量："+iCodeOfrs.size());
		 
		///获取该产品组的组内优惠模式
		int nFavMode = ratingData.getiGroupFavModes().get(group);//会不会这个组没有组内优惠模式呢？
		
		//sortListPlanDisct(listPlanDisct);
		
		
	    //OfrRateData iOfrRateCurr = new OfrRateData(); 
	    OfrRateData iOfrRateTemp = new OfrRateData();
	    boolean bSuccess = false;
	    
	    if(nFavMode == 	Integer.parseInt(RatingMacro.MUTEX_FAV_MODE)){////排它
	    	 for(UserDiscts tmpco:iCodeOfrs){
	    		 OfrRateData iOfrRateCurr = new OfrRateData();
	    		 iOfrRateCurr.setiRateMeasure(iRateMeasure); //改为判断ratingData.irateMeasure
	    		 log.debug("套餐"+tmpco.getAtomId()+"开始前待批价使用量"+iRateMeasure.getUnchargeDosage(iRateMeasure.getM_iUsedMeasureDomain()+"") +"，已批价使用量"+iRateMeasure.getChargedDosage());
	    		 //log.debug("tmplog"+iRateMeasure.toString());
	    		 iOfrRateCurr = rateOfr(ratingMsg,ratingData,tmpco,iOfrRateCurr);
	    		 iRateMeasure = iOfrRateCurr.getiRateMeasure();
	    		 //log.debug("tmplog"+iRateMeasure.toString());
	    		 log.debug("套餐"+tmpco.getAtomId()+"结束后待批价使用量"+iRateMeasure.getUnchargeDosage(iRateMeasure.getM_iUsedMeasureDomain()+"")+",已批价使用量"+iRateMeasure.getChargedDosage());
	    		 iRateTmp.getiOfrResults().add(iOfrRateCurr);
	    		 
	    		 //之前这里为什么要注释调？
	    		 if(iRateMeasure.getUnchargeDosage(iRateMeasure.getM_iUsedMeasureDomain()+"")==0){
	    			 break;
	    		 }
	    		 
	    	 }
	    	 
	    	 
	    	 
	    	 //iRateTmp.addOfrRateResult(iOfrRateCurr);
	    	 
	    	
	    }
	    else if(nFavMode == Integer.parseInt(RatingMacro.BEST_FAV_MODE)){ ///取优   如果取优模式下一个一个套餐不能完全批完使用量，如何处理???
	    	
	    	  boolean bFirstOfr = true;
	    	  for(UserDiscts tmpco:iCodeOfrs){
	    		  OfrRateData iOfrRateCurr = new OfrRateData();
		    	  iOfrRateCurr.setiRateMeasure(iRateMeasure);
		    	  iOfrRateCurr = rateOfr(ratingMsg,ratingData,tmpco,iOfrRateCurr);
		    	  if(iOfrRateCurr.getOfrRateResultFlag()  < 0){
		    		  break;
		    	  } else {
	                    if( bFirstOfr )
	                    {
	                        iOfrRateTemp = iOfrRateCurr;
	                        bFirstOfr = false;
	                    }
	                    else
	                    {
	                        if( iOfrRateCurr.sum() < iOfrRateTemp.sum() )
	                            iOfrRateTemp = iOfrRateCurr;
	                    }
	                }
	            }

	                iRateTmp.addOfrRateResult( iOfrRateTemp );
	                bSuccess = true;
	
	    	
	    } else if(nFavMode == Integer.parseInt(RatingMacro.ADD_FAV_MODE)){ ////叠加
	    	
	    	
	    	for(UserDiscts tmpco:iCodeOfrs){
	    		OfrRateData iOfrRateCurr = new OfrRateData();
		    	  iOfrRateCurr.setiRateMeasure(iRateMeasure);
		    	  iOfrRateCurr = rateOfr(ratingMsg,ratingData,tmpco,iOfrRateCurr);
		    	  if(iOfrRateCurr.getOfrRateResultFlag()  < 0){
		    		  break;
		    	  } else 
	                {
	                 //   iRateMeasure = iOfrRateCurr.getiRateMeasure(); /////这里修改了iRateMeasure,后面的段落批价是否有用到???

	                    iRateTmp.addOfrRateResult( iOfrRateCurr );

	                    if( iRateMeasure.isNoLeftDosage() )
	                        break;
	                }
	            }

	            if( iRateMeasure.isNoLeftDosage() )
	                bSuccess = true;
	    	
	    	
	    }
	    
	    
	    //更新ratemeasure使用量  List<SectionRateData> iSectionResults=
	    
//	    long chargeddosage =0L;
//	    for(SectionRateData tmpsec:iOfrRateCurr.getiSectionResults()){
//	    	//List<TariffResult> iTariffResults
//	    	for(TariffResult tmptf:tmpsec.getiTariffResults()){
//	    		chargeddosage = chargeddosage + tmptf.getLnDosage();
//	    	}
//	    }
//	    
//	    if(chargeddosage>0){//如果资源直扣够的话，tariffresult是没有结果的
//	    	iOfrRateCurr.getiRateMeasure().updateChargedDosage(chargeddosage);
//	    }
	    

	    log.debug("需要批价使用量"+iRateMeasure.getLnTotalVolume());
	    log.debug("已经批价使用量"+iRateMeasure.getLnChargedTotalVolume());
		 
		if(iRateMeasure.isNoLeftDosage() || !ratingMsg.isNeedRating()){ //如果使用量都已经批完
			tokenNode.setM_iRateData(iRateTmp);
			return tokenNode;
		}else{
			throw new RatingException(RatingErrorCode.ERR_IN_RATING,"批价错误");
		}
		 
	}
	
	

	/**
	 * 套餐批价
	 * @param planDisct
	 * @param iOfrRateCurr
	 * @return
	 */
	private OfrRateData rateOfr(RatingMsg ratingMsg,RatingData ratingData,UserDiscts codeOfr, OfrRateData iOfrRateCurr) throws Exception {

		//List<PricingSection> iPricingSections=new ArrayList<PricingSection>(); //定价计划下所有的段落
		
		//iOfrRateCurr.setnOfrId(codeOfr.getOfr_b_id());
		iOfrRateCurr.setLnAtomOfrId(codeOfr.getAtomId());
		
		//根据事件和定价计划找到策略，策略-1需要做资源直扣，非-1需要做批价
		
		long lnPlanId = codeOfr.getPricing_plan_id();
	    long lnEventTypeId  = ratingMsg.getM_iExtMsg().getM_nEvtTypeId();
	    RuleEvtPricingStrategy iStrategy = new RuleEvtPricingStrategy(); /////策略
	    List<RuleSectionRule> iRuleSectionRules = new ArrayList<RuleSectionRule>();
	    int retCode =-1;

	    List<RuleStrategySectRel> iRels = new ArrayList<RuleStrategySectRel>();
	    List<WriteOffDetail> lwriteOffDetail = new ArrayList<WriteOffDetail>();
		
	    for(RuleEvtPricingStrategy tmpstrategy:dbConfigDetail.getAllRuleEvtPricingStrategy()){
	    	if(tmpstrategy.getPricing_plan_id() ==lnPlanId && tmpstrategy.getEvent_type_id() ==lnEventTypeId){
	    		iStrategy = tmpstrategy;
	    		break;
	    	}
	    }
	    
	    if(iStrategy == null || iStrategy.getStrategy_id() == 0){
//	    	throw new RatingException(RatingErrorCode.ERR_NOT_FOUND_STRATERY_BY_PLAN_ID,"没有找到策略");
	    	return iOfrRateCurr;
	    }
	    
	    log.debug("获得的策略："+iStrategy.getStrategy_id());
	    
	  ///看是否有自动续订的配置
	    List<RuleAutoRenew> renews = dbConfigDetail.getRenewCfg((int)iStrategy.getStrategy_id());
	    
	    //先判断是资源直扣还是要做批价
	    //再判断是否需要批价 国际漫游类话单会直接带钱下来，不需要批价,只需要查找到账目项，并且把话单自带的钱作为批价结果返回去
		if(iStrategy.getStrategy_id() ==-1){//资源直扣
			
			
			if(iStrategy.getAcc_item_type().equals("-1")){//不需要扣资源账本
				 return iOfrRateCurr;
			 }
			
			
			if( ratingMsg.getM_iExtMsg().getM_nEvtTypeId() == RatingMacro.EVENT_VOICE_INTERNAL 
					&& iOfrRateCurr.getiRateMeasure().getLnDuration()< RatingMacro.VOICE_MIN_LENGTH){ //如果是国内拨打国内  三秒不计费  直接返回，去遍历批价资费
				 return iOfrRateCurr;
			}
			
			 if(!ratingMsg.isNeedRating()){
					//如果不需要批价，则直接写入批价结果，并且返回批价成功
//					List<BalanceContent> iRealFees  =new ArrayList<BalanceContent>();
//					BalanceContent tmpbalanceContent = new BalanceContent();
//					tmpbalanceContent.setLnAmount(0);
//					tmpbalanceContent.setLnAcctItemTypeId(Integer.parseInt(iStrategy.getAcc_item_type()));
//					tmpbalanceContent.setnUnitTypeId(2);//分
//					iRealFees.add(tmpbalanceContent);
//					ratingData.setiRealFees(iRealFees);
//					
//					RateMeasure tmprateMeasure  = iOfrRateCurr.getiRateMeasure();
//					tmprateMeasure.setLnChargedDuration(tmprateMeasure.getLnDuration());
//					tmprateMeasure.setlncharg
				 
				 iOfrRateCurr.getiRateMeasure().complete();
				 iOfrRateCurr.setAmount(ratingMsg.getM_iBalanceInMsg().getLtDeduct().get(0).getLnAmount());
				 iOfrRateCurr.setDirect(true);
					
					return iOfrRateCurr;
					
				} else {
					
				retCode=deductResourceAcct.deductResourceAcctBalance(Integer.parseInt(iStrategy.getAcc_item_type()),  lwriteOffDetail, ratingMsg, ratingData,iOfrRateCurr,-1);
				
				
//				long value =0L;
//				for(WriteOffDetail tmpwod: lwriteOffDetail){
//					value = value + tmpwod.getWriteoff_fee();
//				}
//				
//				iOfrRateCurr.getiRateMeasure().addChargedDosage(value);
				
				
				
				
			}
			
		}else if (renews!=null && renews.size()>0){
			//自动续订
			int renewFee =0;
			for(RuleAutoRenew renew:renews){
				
				 int unitTypeId = getUnitTypeId(ratingMsg);
				 long roundValue = getRoundValue(unitTypeId,iOfrRateCurr);
				 
				 lwriteOffDetail =   new ArrayList<WriteOffDetail>();
				    
				if(roundValue==0){//没有要扣的使用量了
					break;
				}
				
				if(renew.getDeal_type()==1){//扣资源
				   //扣
					retCode=deductResourceAcct.deductResourceAcctBalance(Integer.parseInt(iStrategy.getAcc_item_type()),  lwriteOffDetail, ratingMsg, ratingData,iOfrRateCurr,renew.getBalance_type_id());

				
				}else if (renew.getDeal_type()==2){//续订
					while(true){						    
						if(getRoundValue(unitTypeId,iOfrRateCurr)==0){//没有要扣的使用量了
							break;
						}
						
						SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
						InfoPayBalance oldInfoPayBalance = dbUtil.getInfoPayBalancebyTime(ratingMsg.getUserinfoForMemcached().getDefaultPayInfo().getPay_id(),renew.getBalance_type_id(),ratingMsg.getAllSessionStartTimes().substring(0,8) );
						
						if(oldInfoPayBalance == null ){
						
						//先送资源账本
						InfoPayBalance ipb = new InfoPayBalance();
						ipb.setBalance(renew.getBalance_amount());
						ipb.setPay_id(ratingMsg.getUserinfoForMemcached().getDefaultPayInfo().getPay_id());
						ipb.setBalance_id(dbUtil.getBalanceId());
						ipb.setBalance_type_id(renew.getBalance_type_id());
						
						ipb.setEff_date(new java.sql.Date(sdf.parse( ratingMsg.getAllSessionStartTimes().substring(0,8)+"000000" ).getTime()));
						if(renew.getEff_type()==1){
							ipb.setExp_date(new java.sql.Date(sdf.parse( ratingMsg.getAllSessionStartTimes().substring(0,8)+"235959" ).getTime()));
						}else if (renew.getEff_type()==2){
							Calendar ca = Calendar.getInstance();  
							ca.setTime(sdf.parse( ratingMsg.getAllSessionStartTimes() ));
				            ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
				            
							ipb.setExp_date(new java.sql.Date(ca.getTime().getTime()));
						}else{
							ipb.setExp_date(new java.sql.Date(sdf.parse( ratingMsg.getAllSessionStartTimes().substring(0,8)+"235959" ).getTime()));
						}
						ipb.setLatn_id(1);
						ipb.setLocal_net("1");
						ipb.setReal_balance(renew.getBalance_amount());
						ipb.setUsed_balance(0L);
						dbUtil.createInfoPayBalance(ipb);
						
						BilActAccesslog baa = new BilActAccesslog();
						baa.setPartition_id(Integer.parseInt(sdf.format(new Date()).substring(4, 6)));
						baa.setPay_id(ipb.getPay_id());
						baa.setBalance_id(ipb.getBalance_id());
						baa.setBalance_type_id(ipb.getBalance_type_id());
						baa.setAccess_tag("0");
						baa.setMoney(ipb.getBalance());
						baa.setOld_balance(0);
						baa.setNew_balance(ipb.getBalance());
						baa.setLocal_net(ipb.getLocal_net());
						baa.setOperate_time(sdf.format(new Date()));
						baa.setReserve_1(""); // 外部流水
						baa.setReserve_2(""); // 接入系统标识
						baa.setReserve_3(""); // 操作员
						baa.setOperate_id(ipb.getBalance_id()+"");
						baa.setOperate_type("36");
						dbUtil.createBilActAccesslog(baa);
						}else{
							//当前有账本，需要加到当前账本上
							long oldBalance = oldInfoPayBalance.getReal_balance();
							oldInfoPayBalance.setBalance(renew.getBalance_amount());
							oldInfoPayBalance.setReal_balance(renew.getBalance_amount());
											 
							S.get(InfoPayBalance.class).batch(
									Condition.build("update4Recharge").filter("balance_id",
											oldInfoPayBalance.getBalance_id()), oldInfoPayBalance);
							
							BilActAccesslog baa = new BilActAccesslog();
							baa.setPartition_id(Integer.parseInt(sdf.format(new Date()).substring(4, 6)));
							baa.setPay_id(oldInfoPayBalance.getPay_id());
							baa.setBalance_id(oldInfoPayBalance.getBalance_id());
							baa.setBalance_type_id(oldInfoPayBalance.getBalance_type_id());
							baa.setAccess_tag("0");
							baa.setMoney(oldInfoPayBalance.getBalance());
							baa.setOld_balance(oldBalance);
							baa.setNew_balance(oldBalance+ renew.getBalance_amount());
							baa.setLocal_net(oldInfoPayBalance.getLocal_net());
							baa.setOperate_time(sdf.format(new Date()));
							baa.setReserve_1(""); // 外部流水
							baa.setReserve_2(""); // 接入系统标识
							baa.setReserve_3(""); // 操作员
							baa.setOperate_id(oldInfoPayBalance.getBalance_id()+"");
							baa.setOperate_type("36");
							dbUtil.createBilActAccesslog(baa);
							
						}
						
						retCode=deductResourceAcct.deductResourceAcctBalance(Integer.parseInt(iStrategy.getAcc_item_type()),  lwriteOffDetail, ratingMsg, ratingData,iOfrRateCurr,renew.getBalance_type_id());

						//补上
						renewFee = renewFee +renew.getCycle_fee();
						
					}
					
					//while结束后再计算总费用
					TariffResult iTariffResult = new TariffResult();
					iTariffResult.setdFee(renewFee);
					iTariffResult.setLnAcctItemTypeId(renew.getAcct_item_code());
					iTariffResult.setnCount(1);
					iTariffResult.setnUnitTypeId(unitTypeId);
					iTariffResult.setLnFee(renewFee);
					
					List<TariffResult> iTariffResults = new ArrayList<TariffResult>();
					iTariffResults.add(iTariffResult);
					
					SectionRateData iSecResult = new SectionRateData();
					iSecResult.setLnAcctItemTypeId(renew.getAcct_item_code());
					iSecResult.setiTariffResults(iTariffResults);
					List<SectionRateData> iSecResults = new ArrayList<SectionRateData>();
					iSecResults.add(iSecResult);
					iOfrRateCurr.getiSectionResults().addAll(iSecResults);
					
				}
			}
			
			
			
		} else{
			
			
			if(!ratingMsg.isNeedRating()){
			 iOfrRateCurr.getiRateMeasure().complete();
			 
			 iOfrRateCurr.setDirect(true);
			 iOfrRateCurr.setAmount(ratingMsg.getM_iBalanceInMsg().getLtDeduct().get(0).getLnAmount());
			 iOfrRateCurr.setLnAcctItemTypeId(Integer.parseInt(iStrategy.getAcc_item_type()));
				
				return iOfrRateCurr;
				
			}
			
			//如果是国内拨打国内  三秒不计费
			if( ratingMsg.getM_iExtMsg().getM_nEvtTypeId() == RatingMacro.EVENT_VOICE_INTERNAL 
					&& iOfrRateCurr.getiRateMeasure().getLnDuration() < RatingMacro.VOICE_MIN_LENGTH){ 
				 iOfrRateCurr.getiRateMeasure().complete();
				 
				 iOfrRateCurr.setDirect(true);
				 iOfrRateCurr.setAmount(0);
				 iOfrRateCurr.setLnAcctItemTypeId(RatingMacro.NORATING_3SECONDS_ACCTITEM);
					
					return iOfrRateCurr;
			}
			
			
			//对于策略非-1的，需要做批价
			 getRuleSectionRules(ratingMsg,codeOfr,iRuleSectionRules );/////是否应该把段落条件过滤也放到这里实现??? zhanghb add

			 //0629
			 if(iRuleSectionRules.size()==0){
				 return iOfrRateCurr;
			 }
			 
			sortRuleSectionRule(iRuleSectionRules);
			///查找用户所有累积量
			//getAllRatableResourceInfos(ratingMsg,ratingData);  累积量暂时用不到，先注释
			/////查找用户当前的实际累积量信息
			//queryRatableResourcesHistory(ratingMsg,ratingData);
			
			//RateMeasure rateMeasureForEverySection = new RateMeasure(iOfrRateCurr.getiRateMeasure());
			

			long chargedvalue = iOfrRateCurr.getiRateMeasure().getChargedDosage();
		
			    for(RuleSectionRule tmprsr:iRuleSectionRules) ///当一个策略配置了多个段落，就是需要多次批价，例如，呼转需要批呼转费和语音费
			    {
			    	
			    	//iOfrRateCurr.setiRateMeasure(rateMeasureForEverySection);
			    	//iOfrRateCurr.getiRateMeasure().initChargedDetail();
			    	iOfrRateCurr.getiRateMeasure().updateChargedDosage(chargedvalue);
			        List<SectionRateData> iSecResult =rateSection(ratingMsg,ratingData,tmprsr,iOfrRateCurr);

			        long lndosageForSection =0L;
			        
			        if(iSecResult != null){
			        	for(SectionRateData tmpsrd:iSecResult){
			        		lndosageForSection = lndosageForSection + tmpsrd.getLnChargedDosage();
			        	}
			        	
			        	iOfrRateCurr.getiSectionResults().addAll(iSecResult);
			        }
			        
			        iOfrRateCurr.getiRateMeasure().addChargedDosage(lndosageForSection);
			        
			        ///ratingData.setM_iRateMeasureForSection(iOfrRateCurr.getiRateMeasure());
			    }
	 
		}
 
		return iOfrRateCurr;
		
		
	}
	
	private int getUnitTypeId(RatingMsg ratingMsg) throws Exception {
		
		String msgType = ratingMsg.getBaseMsg().getM_strMsgType();
		int unitTypeId =-1;
		if(msgType ==null || msgType.length() ==0){
			throw new RatingException(RatingErrorCode.ERR_MSG_DEDUCT,"消息类型异常!");
		}
		if(msgType.equals("60")){
			unitTypeId =BasicType.UNIT_TYPE_VOICE;
		}else if (msgType.equals("70")){
			unitTypeId =BasicType.UNIT_TYPE_SMS;
		}else if (msgType.equals("80")){
			unitTypeId = BasicType.UNIT_TYPE_GGSN;
		}else if (msgType.equals("90")){
			unitTypeId = BasicType.UNIT_TYPE_VAC;
		}else{
			throw new RatingException(RatingErrorCode.ERR_GET_ACCTUNITTYPE,"没有消息类型对应的账务unittype");
		}
		
		return unitTypeId;
	}
	
	private long getRoundValue( int unitTypeId,OfrRateData ofrRateData) {
		 //根据unittype做圆整，unit对应着不同业务的单位类型
			int unitValue =1;
			long primaryValue =0L;
			if(unitTypeId ==1){//语音
				unitValue =RatingMacro.UNIT_VALUE_VOICE;
				primaryValue = ofrRateData.getiRateMeasure().getUnchargeDosage("1");
			}else if(unitTypeId ==2){//数据业务
				unitValue =RatingMacro.UNIT_VALUE_GGSN;
				primaryValue = ofrRateData.getiRateMeasure().getUnchargeDosage("2");
			}else if(unitTypeId ==3){//短信业务
				unitValue =RatingMacro.UNIT_VALUE_SMS;
				primaryValue = ofrRateData.getiRateMeasure().getUnchargeDosage("3");
			}else if(unitTypeId ==5){//vac
				unitValue =RatingMacro.UNIT_VALUE_VAC;
				primaryValue = ofrRateData.getiRateMeasure().getUnchargeDosage("3");
			}
			
			long roundValue =getLong(1.0*primaryValue/unitValue,0);
			
			return roundValue;
	    }
	
	private long getLong(double dSpanValues, int tail_mod) {
		
		  long nValue = (long)dSpanValues;
	        if( tail_mod == 0 ){
	            if( dSpanValues - nValue > 0.0000 )
	                nValue++;
	        }else if( tail_mod == 1 ){
	            ; //no op
	        }else{
	            if( dSpanValues - nValue >= 0.5 )
	                nValue++;
	        }
	        return nValue;
}

	
	/**
	 * 获取原子套餐
	 * @param ofr_b_id
	 * @param m_nMsgType
	 * @return
	 */
//	private int getAtomOfrId(CodeOfr codeOfr, int m_nMsgType) throws Exception {
//		
//		if(codeOfr.getOfr_type_id()  ==1){//如果ofrtypeid是1，本身就是原子套餐
//			return codeOfr.getOfr_b_id();
//		}
//		
//		List<RuleDinnerSelect> dinnerSelectList = dbConfigDetail.getAllRuleDinnerSelect();
//		int innetDays=-1;//入网天数
//		int innetMonths=-1;//入网月数
//		int dinnerEffDays=-1;//套餐生效天数
//		int dinnerEffMonths=-1;//套餐生效月数
//		
//		if(ratingMsg.getM_strUserState()!=ParamData.USER_ACTIVE_N){//用户已激活
//			innetDays=getInnetDays();
//			innetMonths=getInnetMonths();
//			dinnerEffDays=getDinnerEffDays();
//			dinnerEffMonths=getDinnerEffMonths();
//		}
//		
//		for(RuleDinnerSelect dinner:dinnerSelectList){
//			if(dinner.getOfr_b_id() != codeOfr.getOfr_b_id()){
//				continue;
//			}
//			int offsetType=dinner.getOffset_type();
//			switch(offsetType){
//			case ParamData.INNET_OFFSET_DAY://入网日期偏移
//				if(innetDays<dinner.getOffset_low() || innetDays >= dinner.getOffset_upper()){
//					continue;
//				}
//				break;
//			case ParamData.INNET_OFFSET_MONTH:
//				if(innetMonths<dinner.getOffset_low() || innetMonths >= dinner.getOffset_upper())
//					continue;
//				break;
//			case ParamData.DINNER_OFFSET_DAY:
//				if(dinnerEffDays<dinner.getOffset_low() || dinnerEffDays >= dinner.getOffset_upper())
//					continue;
//				break;
//			case ParamData.DINNER_OFFSET_MONTH:
//				if(dinnerEffMonths<dinner.getOffset_low() || dinnerEffMonths >= dinner.getOffset_upper())
//					continue;
//				break;
//			default:
//				break;
//			}
//			
//			boolean dinnerExist =false;
//			for(CodeOfr tmpco:dbConfigDetail.getAllCodeOfr()){
//				if(tmpco.getOfr_b_id() == dinner.getAtom_ofr()){
//					dinnerExist = true;
//					break;
//				}
//			}
//			if(dinnerExist == false){
//				throw new Exception("原子套餐不存在");
//			}
//			
//			
//			return dinner.getAtom_ofr();
//		}
//		
//		
//		
//		
//		return 0;
//	}

	private int getInnetDays(RatingMsg ratingMsg){
	//	Date acceptDate=ratingMsg.getServeInfo().getAcceptDate();//servInfo暂未实现,使用测试数据
		Calendar cal=Calendar.getInstance();
		cal.set(2014, 5,21,0,0,0);
		Date acceptDate=cal.getTime();
		Date msgDate=getMsgDate(ratingMsg);
		return DateUtil.getIntervalDays(acceptDate, msgDate);
	}
	
	private int getInnetMonths(RatingMsg ratingMsg){
//		Date acceptDate=ratingMsg.getServeInfo().getAcceptDate();//servInfo暂未实现,使用测试数据
		Calendar cal=Calendar.getInstance();
		cal.set(2014, 5,21,0,0,0);
		Date acceptDate=cal.getTime();
		Date msgDate=getMsgDate(ratingMsg);
		return DateUtil.getIntervalMonths(acceptDate, msgDate);	
	}
	private int getDinnerEffDays(RatingMsg ratingMsg){
		//获取ofr_b_id生效时间,暂使用测试数据
		Calendar cal=Calendar.getInstance();
		cal.set(2014, 5,21,0,0,0);
		Date ofrEffDate=cal.getTime();
		Date msgDate=getMsgDate(ratingMsg);
		return DateUtil.getIntervalDays(ofrEffDate, msgDate);
	}
	private int getDinnerEffMonths(RatingMsg ratingMsg){
		int days=getDinnerEffDays(ratingMsg);
		return days/30;
	}
	private Date getMsgDate(RatingMsg ratingMsg){
		Date msgDate=null;
		if(ratingMsg.getVarMsg().getM_strCurrTime()==""){
			msgDate=Calendar.getInstance().getTime();
		}else{
			msgDate=DateUtil.toDate(ratingMsg.getVarMsg().getM_strCurrTime());
		}
		return msgDate;
	}
	
	/**
	 * 递归方法，段落批价
	 * @param pricingSection
	 * @return
	 */
	private List<SectionRateData> rateSection(RatingMsg ratingMsg,RatingData ratingData,RuleSectionRule ruleSectionRule, OfrRateData ofrRateDatas) throws Exception {
		//节点类型 0:中间节点；1：叶子节点;2:根节点
		int nodeType = ruleSectionRule.getNode_type();
		
		List<SectionRateData> listofSectionRateData = new ArrayList<SectionRateData>();

		if(! checkCondition(ratingMsg,ratingData,ruleSectionRule.getCond_id())){ //cond判断 如果不满足，直接返回计算下一个段落规则
			return null;
		}
//		if(! checkSectionRuleLimit(ratingMsg,ratingData,ruleSectionRule)){
//			return ;
//		}
		
		log.debug("本次要批价的段落"+ruleSectionRule.getPricing_section());

		if(nodeType == 0 || nodeType ==2){
			List<RuleSectionRule>  sonRuleSectionRules = new ArrayList<RuleSectionRule>();// dbUtil.getSonRuleSectionRule(ruleSectionRule.getPricing_section());
			for(RuleSectionRule tmprsr:dbConfigDetail.getAllRuleSectionRule()){
				if(tmprsr.getUpper_pricing_section() == ruleSectionRule.getPricing_section()){
					sonRuleSectionRules.add(tmprsr);
				}
			}
			
			log.info("获得子段落数量"+sonRuleSectionRules.size());
			for(RuleSectionRule sectionrule:sonRuleSectionRules){
				List<SectionRateData> tmpsectionRateData = rateSection(ratingMsg,ratingData,sectionrule,ofrRateDatas);
                if(tmpsectionRateData != null && tmpsectionRateData.size()>0){
                	listofSectionRateData.addAll(tmpsectionRateData);
                }
				
				
			}
			
		} else if (nodeType ==1){
			
			//获取该段落可以批价的使用量 getDosage start
			
			long upper = ruleSectionRule.getUpper();
			long lower = ruleSectionRule.getLower();
			long lnDosage = 0L;
            long lnStartValue = 0L;
            long lnLastDosage = 0L;
            long lnLeftDosage = 0L;
            
            long billDosage =0L;
            
		RulePricingSection rulePricingSection  = new RulePricingSection();
			for(RulePricingSection tmprps:dbConfigDetail.getAllRulePricingSection()){
				if(tmprps.getPricing_section() == ruleSectionRule.getPricing_section()){
					rulePricingSection = tmprps;
					break;
				}
			}
			
			
			
			if(rulePricingSection == null || rulePricingSection.getPricing_section() ==0 ){ //没有找到段落子节点详细信息
				throw new RatingException(RatingErrorCode.ERR_GET_SECTION,"根据段落ID"+ruleSectionRule.getPricing_section()+"没有找到段落信息");
			}
        
			lnDosage = ofrRateDatas.getiRateMeasure().getUnchargeDosage(rulePricingSection.getMeasure_domain());
			//lnStartValue = ratingData.getStartValue(rulePricingSection.getMeasure_domain());
			//lnLastDosage = ratingData.getM_iRateMeasureForSection().getUnchargedLastDosage(rulePricingSection.getMeasure_domain());
			
			if(ruleSectionRule.getRef_flag() ==0 && !ruleSectionRule.getRef_resource_code().equals("-1") ){//累积量        0参考累积量 1	参考时间
				
				 Map<String, InfoRatableHistory> iInfoRatableHistories = ratingData.getiInfoRatableHistories();
				 if(iInfoRatableHistories.containsKey(ruleSectionRule.getRef_resource_code())){
					 
					 long lnResourceValue = Long.parseLong( iInfoRatableHistories.get(ruleSectionRule.getRef_resource_code()).getValue() );
					 ////getBillDosage
					 if(lnResourceValue >= upper || lnResourceValue < lower){
						 billDosage =0;
						 lnLeftDosage = lnDosage;
					 }
					 
					//累积量类型 1－时长(秒)； 2－时长(分钟)；3－次数；4－总流量(k)；5－分(金额)；7－上行流量按K；8－下行流量
					 int ratableResourceType =0; // dbUtil.getCodeRatableResource(ruleSectionRule.getRef_resource_code()).getRatable_resource_type();
					 for(CodeRatableResource tmpcrr:dbConfigDetail.getAllCodeRatableResource()){
						 if(tmpcrr.getRatable_resource_code() == ruleSectionRule.getRef_resource_code() ){
							 ratableResourceType = tmpcrr.getRatable_resource_type();
							 break;
						 }
					 }
					 
					 if(ratableResourceType <=0){
						 throw new RatingException(RatingErrorCode.ERR_GET_RATABLETYPE,"获取累积量类型异常");
					 }
					 
					 long  lnValue = 0;
					 long  lnUnit = 1;
					  switch( ratableResourceType )
					    {
					        case 1: //时长(秒)
					        case 3: //次数
					        case 4: //总流量
					        case 5: //分(金额)
					        case 7: //上行流量
					        case 8: //下行流量
					        case 9: //M
					        case 0: //T
					            lnValue = lnDosage;
					            lnUnit = 1;
					            break;
					        case 2: //时长(分钟)
					            lnValue = ( ( lnDosage % 60 ) == 0 )?lnDosage/60:lnDosage/60+1;
					            lnUnit = 60;
					            break;
					        default:    //其他计算资源,暂不考虑
					            break;
					    }
					  
					   if( ( lnResourceValue + lnValue ) > upper )
					    {
					        //跨越多个累积量段
					        billDosage = ( upper - lnResourceValue ) * lnUnit; //计费剂量
					        lnLeftDosage = lnDosage - billDosage;      //剩余剂量
					        lnResourceValue = upper;
					    }
					    else
					    {
					        billDosage = lnDosage;
					        lnResourceValue += lnValue;
					        lnLeftDosage = -1;
					    }
					 
					 
					   lnDosage = billDosage;
					   //////数据业务按照时长计费的场景，暂不实现
					   
				 }
			
			}
			
 
				 List<TariffResult> iTariffResults= calcCharge(ratingMsg,ratingData,rulePricingSection,lnDosage);
				 SectionRateData tmpsectionratedata = new SectionRateData();
				 tmpsectionratedata.setLnAcctItemTypeId(Integer.parseInt(rulePricingSection.getAcct_item_id()));
				 tmpsectionratedata.setiTariffResults(iTariffResults);
				 
				 
				 long lnChargedDosage = 0L;
				 for(TariffResult tmptr:iTariffResults){
					 lnChargedDosage = lnChargedDosage + tmptr.getLnDosage();
				 }
				 tmpsectionratedata.setLnChargedDosage(lnChargedDosage);
				 
	//zhanghb add 0929			 ofrRateDatas.getiRateMeasure().addChargedDosage(lnChargedDosage);
				// ofrRateDatas.getiRateMeasure().getUnchargeDosage(rulePricingSection.getMeasure_domain());
				 
				 listofSectionRateData.add(tmpsectionratedata);
	 
			
		} 

		return listofSectionRateData;

		
	}

 

	private List<TariffResult> calcCharge(RatingMsg ratingMsg,RatingData ratingData,RulePricingSection rulePricingSection,
			long lnDosage ) throws Exception {
		
		List<TariffResult> iTariffResults = new ArrayList<TariffResult>();
		
		if(rulePricingSection.getSection_type()==0){//免费段落
			
			TariffResult tmptariffResult =  new TariffResult();
			tmptariffResult.setLnPricingSectionId(rulePricingSection.getPricing_section());
			tmptariffResult.setLnTariffID(rulePricingSection.getTariff_id());
			tmptariffResult.setLnAcctItemTypeId(Long.parseLong(rulePricingSection.getAcct_item_id())); //目前直接放acctitemid，以后如果acctitemtype字段用起来，还需要修改
			tmptariffResult.setnMeasureDomain(rulePricingSection.getMeasure_domain());
			tmptariffResult.setnCount(1);
			tmptariffResult.setnUnitTypeId(2);//目前是分
			tmptariffResult.setLnDosage(lnDosage);
			tmptariffResult.setdRateValue(0);
			tmptariffResult.setdFee(0);
			tmptariffResult.setLnFee(0);
			
			
			
			iTariffResults.add(tmptariffResult);
			
			
			//还要更新已批使用量
			//TODO 
			
		 
			
		}else{//收费段落
			if(rulePricingSection.getTariff_type() == 0){ //计量计费
				iTariffResults = calcChargeByFlux(ratingMsg,ratingData,rulePricingSection,lnDosage);
			}else if (rulePricingSection.getTariff_type() == 1){//时间计费
				//iTariffResults = calcChargeByTime(ratingMsg,ratingData,rulePricingSection,lnDosage);
			} 
		}

		return iTariffResults;
	}

	/**
	 * 按时长计费
	 * @param rulePricingSection
	 * @param lnDosage
	 * @param lnStartValue
	 * @param lnLastDosage
	 * @param iTariffResults
	 */
	private void calcChargeByTime(RatingMsg ratingMsg,RatingData ratingData,RulePricingSection rulePricingSection,
			long lnDosage, long lnStartValue, long lnLastDosage,
			List<TariffResult> iTariffResults) {
		// TODO Auto-generated method stub
		
	}
/**
 * 按流量计费
 * @param rulePricingSection
 * @param lnDosage
 * @param lnStartValue
 * @param lnLastDosage
 * @param iTariffResults
 */
	private List<TariffResult> calcChargeByFlux(RatingMsg ratingMsg,RatingData ratingData ,RulePricingSection rulePricingSection,
			long lnDosage ) throws Exception {
		List<RuleTariff> iTotalTariffs = new ArrayList<RuleTariff>();
		List<RuleTariff> iNormalTariffs = new ArrayList<RuleTariff>();
		List<RuleTariff> iHolidayTariffs = new ArrayList<RuleTariff>();
		List<RuleTariff> iNotHolidayTariffs = new ArrayList<RuleTariff>();
		List<RuleTariff> iTariffs = new ArrayList<RuleTariff>();
		
		List<TariffResult> iTariffResults = new ArrayList<TariffResult>();
		
		getAllTariffs(rulePricingSection.getTariff_id(),iTotalTariffs,iNormalTariffs,iHolidayTariffs,iNotHolidayTariffs);
	    if(iTotalTariffs.size() ==0){
	    	log.error("根据费率id"+rulePricingSection.getTariff_id()+"没有找到费率记录");
	    	throw new RatingException(RatingErrorCode.ERR_GET_TARIFF,"根据费率id"+rulePricingSection.getTariff_id()+"没有找到费率记录");
	    }
		
		String strCurrTime = ratingMsg.getBaseMsg().getM_strStartTime();
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmmss");
	    Date iCurrTime = sdf.parse(strCurrTime);

	    int nCurCounts = 0;
	    double dSpanValues = 0.0;
	    double dTotalFee = 0.0;  //为了提高精度，用一个double型做中间变量
	    long lnNumberA = 0;
	    long lnNumberB = lnNumberA + lnDosage;
	    long lnChangePoint = 0;//费率切换点值
	    boolean bIsHoliday = false;

	    long lnStartValue = 0;
	    long lnEndValue = 0;
	    long lnEndValueSpan = -1;
	    long lnRoundDosage=lnDosage;//取圆整之后的使用量 
	    long lnRealDosage = 0;
		
	    if(iTotalTariffs.get(0).getDate_type() == RatingMacro.NORMAL_TARIFF){
	    	bIsHoliday = false;
	    }else{
	    	bIsHoliday = true;
	    }
		
		RuleTariff  iCurrIter = iTotalTariffs.get(0);
		
		//moveToFirstTariff( iCurrIter, iTotalTariffs, iNormalIter, iNormalTariffs, iHolidayIter ,iHolidayTariffs, iNotHolidayIter ,iNotHolidayTariffs);
        int index =0;
        
        if( bIsHoliday ){
	        if( isHoliday( iCurrIter.getHoliday_id(), strCurrTime ) )
	        	iTariffs = iHolidayTariffs;
	        else
	        	iTariffs = iNotHolidayTariffs;
	    }else
	    	iTariffs = iNormalTariffs;
        
		
	    while(true){
	    	
	    	//getCurrTariff(iCurrIter ,iNormalTariffs,iHolidayTariffs,iNotHolidayTariffs, index,bIsHoliday,strCurrTime);
	    	
	    	 if( lnNumberA == lnNumberB )/////此次批价使用量为0 
		            break;
	    	 
	    	iCurrIter = iTariffs.get(index);
	    	
	    	if( iCurrIter.getDisct_value_base() == 0 )////折扣率基数 
	            throw new RatingException(RatingErrorCode.ERR_TARIFF_CAN_NOT_ZERO,"费率折扣基数不能为0");

	       
	        
	        TariffResult iTariffResult = new TariffResult();
	        iTariffResult.setLnPricingSectionId(rulePricingSection.getPricing_section());
	        iTariffResult.setLnAcctItemTypeId(Long.parseLong(rulePricingSection.getAcct_item_id()));
	        iTariffResult.setLnTariffID(rulePricingSection.getTariff_id());
	        iTariffResult.setnBeginTime(lnChangePoint);

	        nCurCounts = 0;
	        lnStartValue = iCurrIter.getLower();
	        lnEndValue = iCurrIter.getUpper();

//	        if( lnEndValueSpan != -1 )
//	            lnEndValue += lnEndValueSpan;
	        
	      //计费点A在该费率段中
	        if( lnNumberA >= lnStartValue && lnNumberA < lnEndValue ){
	        	
	        	//平行资费
	            if( iCurrIter.getRate_unit() == ( lnEndValue - lnStartValue ) ){
	                if( lnNumberA == lnStartValue ){
	                    dSpanValues = 1.0;
	                    nCurCounts = 1;
	                }else {
	                    //平行资费以经扣过费不再扣费
	                    dSpanValues = 0.0;
	                    nCurCounts = 0;
	                }
	                //根据B点位置移动A点
	                if ( lnNumberB <= lnEndValue )  {
	                    lnNumberA = lnNumberB;
	                } else {
	                    lnNumberA = lnEndValue;
	                }
	            } else {
	            	//普通资费
	            	  if( lnNumberB <= lnEndValue ) {
	                      lnRealDosage = lnNumberB - lnNumberA;
	                      lnRoundDosage = lnRealDosage;

	                      dSpanValues = 1.0 *lnRoundDosage / iCurrIter.getRate_unit();//计算使用了几个单位??? 
	                      nCurCounts = getInt( dSpanValues, iCurrIter.getTail_mod() );//取整 

	                      lnRoundDosage = 1L*nCurCounts * iCurrIter.getRate_unit();//得到取圆整之后的使用量  

	                      long lnDosageDiff = lnRoundDosage - lnRealDosage ;
	                      long lnUnused = lnDosageDiff>0 ? lnDosageDiff : 0;//批价了但用户实际没有使用的流量 
	                      //多扣费的后续要返回给用户，少扣费的部分免掉
	                      iTariffResult.setLnRatableUnusedDosage(lnUnused);

	                      lnNumberA = lnNumberB;
	                  } else { //lnNumberB > lnEndValue
	                      lnRealDosage = lnEndValue - lnNumberA;
	                      lnRoundDosage = lnRealDosage;

	                      dSpanValues = 1.0*( lnRoundDosage ) / iCurrIter.getRate_unit();
	                      nCurCounts = getInt( dSpanValues, iCurrIter.getTail_mod() );//费率表的tail_mode用于计算使用量换算成多少个单位

	                      //long lnTmpNumberA = lnNumberA;
	                      lnNumberA = lnEndValue;

                          lnRoundDosage = 1L * nCurCounts * iCurrIter.getRate_unit();
                          long lnTmpUsedDosage = lnRoundDosage - lnRealDosage;
                          long lnDosageDiff = lnTmpUsedDosage>0 ? lnTmpUsedDosage : 0;
                          iTariffResult.setLnRatableUnusedDosage(lnDosageDiff);
	                  }

	            }
	            
	            
	            if( nCurCounts > 0 )
	            {
	                iTariffResult.setnCount(nCurCounts);
	                iTariffResult.setLnDosage(1L*nCurCounts*iCurrIter.getRate_unit());
	                dTotalFee += 1.0 * nCurCounts * iCurrIter.getRate_unit() * iCurrIter.getDisct_value() / iCurrIter.getDisct_value_base(); //总费用算出来没用啊???TODO
	            }
	            iTariffResult.setnUnitTypeId(iCurrIter.getTariff_unit_id()); 

//	            iCurrTime.addSeconds( nCurCounts * iCurrIter->m_lnRateUnit );///流量计费你addSecond干毛???
//	            iCurrTime.toString( strCurrTime );

	            iTariffResult.setLnRateUnit(iCurrIter.getRate_unit());
	            iTariffResult.setdRateValue(1.0*iCurrIter.getFee_rate() * iCurrIter.getDisct_value() /( iCurrIter.getDisct_value_base() * RatingMacro.TARIFF_PRECISION));
	            iTariffResult.setdFee((1.0 * iCurrIter.getFee_rate() *nCurCounts * iCurrIter.getDisct_value() /iCurrIter.getDisct_value_base() )/RatingMacro.TARIFF_PRECISION);//圆整前的费用
	            iTariffResult.setLnFee(getInt(iTariffResult.getdFee(),  rulePricingSection.getTail_mod()));//圆整后的费用
	            iTariffResult.setnMeasureDomain(rulePricingSection.getMeasure_domain());

	            lnChangePoint = lnEndValue;

	            // if( lnNumberA <= 0 )
	            if( lnNumberA == lnNumberB ){
	                long lnTmpUnusedDosage = lnRoundDosage - lnRealDosage;
	                iTariffResult.setLnUnusedDosage(lnTmpUnusedDosage > 0 ? lnTmpUnusedDosage:0);
	            }
	            
	            log.debug("flux->tariffresult-->"+iTariffResult.toString());
	            iTariffResults.add(iTariffResult);
	        }
	        
	        index++;
	        
	        if( lnNumberA < lnNumberB     ){
	        	if(index >= iTariffs.size()){
	        		log.error("获取下一个费率异常！");
		            throw new RatingException(RatingErrorCode.ERR_TARIFF_NOT_CLOSED,"获取下一个费率异常！");
	        	}else {
	        		iCurrIter = iTariffs.get(index);
	        	}
	            
	        } //end if
	        
	    }
	    
	    return iTariffResults;

	}

	/**
	 * 获取当前费率
	 * @param iCurrIter
	 * @param iNormalTariffs
	 * @param iHolidayTariffs
	 * @param iNotHolidayTariffs
	 * @param index
	 * @param bIsHoliday
	 * @param strCurrTime
	 */
 private void getCurrTariff(RuleTariff iCurrIter,
		List<RuleTariff> iNormalTariffs, List<RuleTariff> iHolidayTariffs,
		List<RuleTariff> iNotHolidayTariffs, int index, boolean bIsHoliday,
		String strCurrTime) {
	    if( bIsHoliday )
	    {
	        if( isHoliday( iCurrIter.getHoliday_id(), strCurrTime ) )
	        	iCurrIter = iHolidayTariffs.get(index);
	        else
	        	iCurrIter = iNotHolidayTariffs.get(index);
	    }
	    else
	    	iCurrIter = iNormalTariffs.get(index);

}

/**
  * 取整
  * @param dSpanValues
  * @param tail_mod  0向上取整   1向下取整  2四舍五入
  * @return
  */
	private int getInt(double dSpanValues, int tail_mod) {
		  int nValue = (int)dSpanValues;

	        if( tail_mod == 0 ) //向上
	        {
	            if( dSpanValues - nValue > 0.0000 )
	                nValue++;
	        }
	        else if( tail_mod == 1 )
	        {
	            ; //no op
	        }
	        else
	        {
	            if( dSpanValues - nValue >= 0.5 )
	                nValue++;
	        }

	        return nValue;
}

	/**
	 * 获取第一条费率
	 * @param iCurrIter
	 * @param iTotalTariffs
	 * @param iNormalIter
	 * @param iNormalTariffs
	 * @param iHolidayIter
	 * @param iHolidayTariffs
	 * @param iNotHolidayIter
	 * @param iNotHolidayTariffs
	 */
	private void moveToFirstTariff(RuleTariff iCurrIter,
		List<RuleTariff> iTotalTariffs, RuleTariff iNormalIter,
		List<RuleTariff> iNormalTariffs, RuleTariff iHolidayIter,
		List<RuleTariff> iHolidayTariffs, RuleTariff iNotHolidayIter,
		List<RuleTariff> iNotHolidayTariffs) {
		
		iCurrIter = iTotalTariffs.get(0);
	    if( iCurrIter.getDate_type()  == RatingMacro.NORMAL_TARIFF )
	    	iNormalIter = iNormalTariffs.get(0);
	    else
	    {
	    	iHolidayIter = iHolidayTariffs.get(0);
	    	iNotHolidayIter = iNotHolidayTariffs.get(0);
	    }
}

 

 

	/**
	 * 判断当前时间是否节假日
	 * @param holiday_id
	 * @param strCurrTime
	 * @return
	 */
	private boolean isHoliday(int holiday_id, String strCurrTime) {
		
		List<RuleHoliday> iHolidays = dbConfigDetail.getAllRuleHoliday();		

		 for(RuleHoliday rh:iHolidays){
			 if(rh.getHoliday_id() == holiday_id){
				 
				 if( rh.getHoliday_type().equals("01")){//01:日期（年月日） 02:星期 03:日期（月日） 04:时间
					 
					 if(Long.parseLong(strCurrTime.substring(0,8)) >= Long.parseLong(rh.getBegin_time().substring(0,8))
							 && Long.parseLong(strCurrTime.substring(0,8)) <= Long.parseLong(rh.getEnd_time().substring(0,8)) ){
						 return true;
					 }
					 
				 }else if( rh.getHoliday_type().equals("02") ){
					 
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					 Date dt;
					try {
						dt = sdf.parse(strCurrTime);
					} catch (ParseException e) {
						return false;
					}
				     Calendar cal = Calendar.getInstance();
				     cal.setTime(dt);
				     int weekno= cal.get(Calendar.DAY_OF_WEEK) -1;
				     
				     if(weekno >= Integer.parseInt(rh.getBegin_time()) 
				    		 && weekno <= Integer.parseInt(rh.getEnd_time()) ){
				    	 return true;
				     }
				     
				        
				 }else if( rh.getHoliday_type().equals("03") ){
					 
					 if(Long.parseLong(strCurrTime.substring(4,8)) >= Long.parseLong(rh.getBegin_time().substring(0,4))
							 && Long.parseLong(strCurrTime.substring(4,8)) <= Long.parseLong(rh.getEnd_time().substring(0,4)) ){
						 return true;
					 }
					 
				 }else if( rh.getHoliday_type().equals("04") ){
					 if(Long.parseLong(strCurrTime.substring(4,8)) >= Long.parseLong(rh.getBegin_time().substring(8,14))
							 && Long.parseLong(strCurrTime.substring(4,8)) <= Long.parseLong(rh.getEnd_time().substring(8,14)) ){
						 return true;
					 }
				 }
			 
				 
			 }
		 }

		return false;
	}

	/**
	 * 获取所有费率，分类放到不同的list
	 * @param iTotalTariffs
	 * @param iNormalTariffs
	 * @param iHolidayTariffs
	 * @param iNotHolidayTariffs
	 */
	private void getAllTariffs(long tariffId,List<RuleTariff> iTotalTariffs,
		List<RuleTariff> iNormalTariffs, List<RuleTariff> iHolidayTariffs,
		List<RuleTariff> iNotHolidayTariffs) {
		
		for(RuleTariff tmprt:dbConfigDetail.getAllRuleTariff()){
			if(tmprt.getTariff_id() == tariffId){
				iTotalTariffs.add(tmprt);
			}
		}
		
		for(RuleTariff ta:iTotalTariffs){
			if(ta.getDate_type()  == RatingMacro.NORMAL_TARIFF	){
				iNormalTariffs.add(ta);
			}else if (ta.getDate_type()  == RatingMacro.HOLIDAY_TARIFF) {
				iHolidayTariffs.add(ta);				
			}else if (ta.getDate_type()  == RatingMacro.NOTHOLIDAY_TARIFF) {
				iNotHolidayTariffs.add(ta);				
			}
		}
		
		sortRuleTariff(iNormalTariffs);
		sortRuleTariff(iHolidayTariffs);
		sortRuleTariff(iNotHolidayTariffs);
}

	//对扎到的费率列表进行排序
	private void sortRuleTariff(List<RuleTariff> listofTariffs) {
		
		for(int i=0;i<listofTariffs.size();i++){
			//	PlanDisct iplanDisct = listPlanDisct.get(i);
				for(int j=i+1;j<listofTariffs.size();j++){
				//	PlanDisct jplanDisct = listPlanDisct.get(j);
					if(listofTariffs.get(i).getTariff_sn() < listofTariffs.get(j).getTariff_sn()){
						RuleTariff tmprsr = listofTariffs.get(i);
						listofTariffs.set(i, listofTariffs.get(j));
						listofTariffs.set(j, tmprsr);		
						tmprsr =null;
					}
				}
			}
		
	}

	/**
	 * 检查段落规则上下限是否满足
	 * @param ruleSectionRule
	 * @return
	 * @throws Exception
	 */
	private boolean checkSectionRuleLimit(RatingMsg ratingMsg,RatingData ratingData,RuleSectionRule ruleSectionRule) throws Exception {
		//0	参考累积量	1 参考时间
		int refFlag = ruleSectionRule.getRef_flag();
		if(refFlag == 0){
			//参考累积量
			Map<String, InfoRatableHistory> infoRatableHistories = ratingData.getiInfoRatableHistories();
			if(infoRatableHistories == null || infoRatableHistories.size() ==0 || !infoRatableHistories.containsKey(ruleSectionRule.getRef_resource_code()))
			{
				return true;//为什么没有累积量信息时也返回true??
			}
			
			long ratableValue = Long.parseLong(infoRatableHistories.get( ruleSectionRule.getRef_resource_code() ).getValue());
			
			if(ratableValue >= ruleSectionRule.getLower() && ratableValue <ruleSectionRule.getUpper()){
				return true;
			}

		}else if(refFlag ==1){
			//参考时间
			String startTime = ratingMsg.getBaseMsg().getM_strStartTime();
			int nValue = Integer.parseInt( startTime.substring(8, 10) );
		        if( nValue >= ruleSectionRule.getLower()   &&  nValue < ruleSectionRule.getUpper() )
		            return true;
		}

		return false;
	}

	/**
	 * 根据段落条件id判断计费请求是否适用当前段落
	 * @param conditionId
	 * @return
	 * @throws Exception
	 */
	private boolean checkCondition(RatingMsg ratingMsg,RatingData ratingData,long conditionId) throws Exception {
		
		if(conditionId ==-1){//段落条件=-1认为是不需要条件，都成功
			return true;
		}
		
		List<Long> listofGroupId = new ArrayList<Long>();

		List<RuleRateCondition> listRuleRateCondition = new ArrayList<RuleRateCondition>();
		for(RuleRateCondition tmprrc:dbConfigDetail.getAllRuleRateCondition()){
			if(tmprrc.getCond_id() == conditionId){
				listRuleRateCondition.add(tmprrc);
				
				if(!listofGroupId.contains(tmprrc.getGroup_id())){
					listofGroupId.add(tmprrc.getGroup_id());
				}
				
			}
		}

		boolean groupSatisfy = false;

		if(listRuleRateCondition == null || listRuleRateCondition.size() ==0){
			return true;
		}
		
		log.debug("condid"+conditionId+"下有"+listofGroupId.size()+"个groupid");
		 
		
		//group_id之间是或的关系，同一groupid下的gorupsn之间是与的关系
        for(Long groupid:listofGroupId){
        	//同一个groupid
        	groupSatisfy = false;
        	
        	for(RuleRateCondition tmpCond:listRuleRateCondition){
        		if(groupid != tmpCond.getGroup_id()){
        			continue;
        		}
        		
        		String strValueA = getValueFromMsgMapbyKey(tmpCond.getItem_code(),ratingMsg.getMsgMap());   //ratingMsg.getValue(tmpCond.getItem_code(), 1);
        		String strValueB ="";
        		
        		if(tmpCond.getCom_type().equals("10")){//10元素与值的比较
        			strValueB = tmpCond.getItem_value();	
        		}else if(tmpCond.getCom_type().equals("20")){//20元素与元素的比较
        			strValueB = getValueFromMsgMapbyKey(tmpCond.getItem_value(),ratingMsg.getMsgMap());  //ratingMsg.getValue(tmpCond.getItem_value(), 1);        			
        		}else{        			
        		}
        		
        		//海航畅聊包 拨打所有海航通信用户免费
        		if(tmpCond.getItem_code()!=null && tmpCond.getItem_code().equals("HNUSER")){
        			
        			String calledNumber = ratingMsg.getBaseMsg().getM_strCalledNbr();
        			log.debug("海航获取被叫号码："+calledNumber);
        			UserInfoForMemCached calledUserinfoForMemcached = null;
        			try {
        				calledUserinfoForMemcached = S.get(UserInfoForMemCached.class).get(calledNumber);
        				
        			} catch (Exception e) {
        				e.printStackTrace();
        				log.error(e.getLocalizedMessage());
        				throw new RatingException(RatingErrorCode.ERR_MEMCACHE_READ,"海航根据被叫号码读取memcached异常");
        			}
        			
        			if(calledUserinfoForMemcached !=null && calledUserinfoForMemcached.getInfoUser() !=null ){
        				strValueA="1";//如果被叫是海航用户，则置为1
        			}else{
        				log.debug("被叫号码"+calledNumber+"不是海航用户");
        			}
        			
        		}
        		
        		//海航畅聊包版本2，拨打海航内部号码免费,infouser表user_type=004是内部用户
        		if(tmpCond.getItem_code()!=null && tmpCond.getItem_code().equals("HNSTF")){
        			
        			String calledNumber = ratingMsg.getBaseMsg().getM_strCalledNbr();
        			log.debug("海航获取被叫号码："+calledNumber);
        			UserInfoForMemCached calledUserinfoForMemcached = null;
        			try {
        				calledUserinfoForMemcached = S.get(UserInfoForMemCached.class).get(calledNumber);
        				
        			} catch (Exception e) {
        				e.printStackTrace();
        				log.error(e.getLocalizedMessage());
        				throw new RatingException(RatingErrorCode.ERR_MEMCACHE_READ,"海航根据被叫号码读取memcached异常");
        			}
        			
        			if(calledUserinfoForMemcached !=null && calledUserinfoForMemcached.getInfoUser() !=null && 
        					calledUserinfoForMemcached.getInfoUser().getUser_type().equals("004")){
        				strValueA="1";//如果被叫是海航用户，则置为1
        			}else{
        				log.debug("被叫号码"+calledNumber+"不是海航用户");
        			}
        			
        		}
        		
        		
        		log.debug("左值:"+strValueA+"右值:"+strValueB);
        		if(compare(strValueA,tmpCond.getCom_operators(),strValueB)){
        			groupSatisfy = true;
        		} else{
        			groupSatisfy =false;
        			break;
        		}
   
        	}

        	if(groupSatisfy){
        		//break;
        		return true;
        	}
        	
        }

		return false;
	}
	
	//因为map中可能含有list对象，所以单独写一个get
	private String getValueFromMsgMapbyKey(String itemCode,Map msgMap) {
		
		String retValue="";
		
		for(Object key:msgMap.keySet()){
		  if(msgMap.get(key) instanceof String){ //如果value是string
			  if(((String)key).equals(itemCode)){
				  retValue = (String)msgMap.get(key);
				  break;
			  }
		  }else if (msgMap.get(key) instanceof List){//如果value是list
			  for(Object tmp:(List)msgMap.get(key)){
				  Map innermap = (Map) tmp;
				  for(Object tmpo:innermap.keySet()){
					  if(((String)tmpo).equals(itemCode)){
						  retValue = (String)innermap.get(tmpo);
						  break;
					  }
				  }
				  
				  if(retValue.length()>0)
					  break;
				  
			  }
			  
		  }
		}
		
		return retValue;
	}

	/**
	 * 比较左右值
	 * @param strItemCodeA
	 * @param comOperator 取值范围 10(=),20(!=),30(like) ,31(not like),40(>),41(>=),50(<)
	 * @param strItemCodeB
	 * @return
	 */
	private boolean compare(String strItemCodeA,int comOperator ,String strItemCodeB){
		
		String[] itemCodeBList ;
		//int i =0;
		
		if(comOperator == 10) // =
		{
			if( strItemCodeA.equals(strItemCodeB))
				   return true;
		} else if (comOperator ==20){//!=
			if( !strItemCodeA.equals(strItemCodeB))
				   return true;
		} else if (comOperator  ==30 ){// like 
			if( strItemCodeA.substring(0, strItemCodeB.length()).equals(strItemCodeB))
				   return true;
		} else if (comOperator ==31) {//not like
			if(! strItemCodeA.substring(0, strItemCodeB.length()).equals(strItemCodeB))
				   return true;
	     } else if (comOperator == 40){// >
			
			if(strItemCodeA.compareTo(strItemCodeB) >0)//Long.parseLong(strItemCodeA) > Long.parseLong(strItemCodeB)
			return true;
		} else if (comOperator == 41){// >=
			
			if(strItemCodeA.compareTo(strItemCodeB) >=0)
			return true;
		} else if (comOperator == 50){// <
			
			if(strItemCodeA.compareTo(strItemCodeB) <0)
			return true;
		} else if (comOperator == 51){// <=
			
			if(strItemCodeA.compareTo(strItemCodeB) <=0)
			return true;
		} else if (comOperator ==60){//包含
			
			 itemCodeBList = strItemCodeB.split(",");

			   for(String codeb:itemCodeBList){
				   if( strItemCodeA.equals(codeb))
					   return true;
			   }
		} else if (comOperator ==61){///不包含
			itemCodeBList = strItemCodeB.split(",");
 
			   for(String codeb:itemCodeBList){
				   if( strItemCodeA.equals(codeb))
					 return false;
			   }
			   
			  return true;
			   
		} else if (comOperator ==62) {//包含相似
			itemCodeBList = strItemCodeB.split(",");

			   for(String codeb:itemCodeBList){
				   if(strItemCodeA.substring(0, Math.min(codeb.length(), strItemCodeA.length())).equals(codeb))
					   return true;
			   }
			   
		} else if (comOperator ==63){//不包含相似
			itemCodeBList = strItemCodeB.split(",");

			   for(String codeb:itemCodeBList){
				   if(strItemCodeA.substring(0, Math.min(codeb.length(), strItemCodeA.length())).equals(codeb))
					  return false;
			   }
			   
			   return true;
			   
		} else {
			//如果有后续其他比较类型，再增加
		}

		return false;
	}

	/**
	 * 根据用户累积量配置信息获取相关累积量历史信息
	 */
	private void queryRatableResourcesHistory(RatingMsg ratingMsg,RatingData ratingData) throws Exception {
		//获取用户相关的累积量配置信息
		
		List<CodeRatableResource> allCodeRatableResource = ratingData.getAllCodeRatableResource();
		if(allCodeRatableResource == null || allCodeRatableResource.size() ==0){
			return ;
		}
		List<InfoRatableHistory> allInfoRatableHistories = dbUtil.getAllRatableValue(); //TODO 这不行啊。。。。这得多慢啊

		Map<String, InfoRatableHistory> iInfoRatableHistories= new HashMap<String, InfoRatableHistory>();
		
		int strYearMonth =0;///账期
		int latnId = ratingMsg.getM_iUserMsg().getnLatnId();
		long ownerId = 0L;

		////遍历用户相关的累积量配置信息
		for(int i=0;i<allCodeRatableResource.size();i++){
			////遍历累积量历史表，查找用户配置相对应的实际累积信息
			CodeRatableResource codeRatableResource = allCodeRatableResource.get(i);
			strYearMonth = getYearMonth();//TODO 实现getyearmonth
			if(codeRatableResource.getOwner_type().equals("80A")){///用户id
				ownerId = Long.parseLong(ratingMsg.getM_iUserMsg().getLnServId());
			}
			if(codeRatableResource.getOwner_type().equals("80I")){ ///客户
				ownerId = Long.parseLong(ratingMsg.getM_iUserMsg().getLnCustId());
			}
			if(codeRatableResource.getOwner_type().equals("80J")){ ///账户
				ownerId = Long.parseLong(ratingMsg.getM_iUserMsg().getLnAcctId());
			}
			
			for(InfoRatableHistory tmphis:allInfoRatableHistories){
				
				if( tmphis.getAcct_month() == strYearMonth && tmphis.getLatn_id() ==latnId 
				    && tmphis.getOwner_type() ==codeRatableResource.getOwner_type() 
				    && tmphis.getOwner_id() == ownerId &&
				    tmphis.getResource_code() == codeRatableResource.getRatable_resource_code()
						){
					iInfoRatableHistories.put(tmphis.getResource_code(), tmphis);
				}
				
			}
			
			
			
		}
		
		log.info("获取了用户当前累积量数量:"+iInfoRatableHistories.size());
		ratingData.setiInfoRatableHistories(iInfoRatableHistories);
		
		
		
	}

	///获取账期
	private int getYearMonth() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 对段落进行排序
	 * @param iPricingSections
	 * @return
	 */
	private void  sortRuleSectionRule(  //???
			List<RuleSectionRule> iPricingSections) {

				for(int i=0;i<iPricingSections.size();i++){
				//	PlanDisct iplanDisct = listPlanDisct.get(i);
					for(int j=i+1;j<iPricingSections.size();j++){
					//	PlanDisct jplanDisct = listPlanDisct.get(j);
						if(iPricingSections.get(i).getPriority() < iPricingSections.get(j).getPriority()){
							RuleSectionRule tmprsr = iPricingSections.get(i);
							iPricingSections.set(i, iPricingSections.get(j));
							iPricingSections.set(j, tmprsr);		
							tmprsr =null;
						}
					}
				}
				
		
				
	}

	
	/**
	 * 获取用户累积量配置信息,判断用户需要做哪种类型的累积
	 */
	private void getAllRatableResourceInfos(RatingMsg ratingMsg,RatingData ratingData) throws Exception {
		
		int nMsgType = ratingMsg.getM_nMsgType();
		 Map<String, CodeRatableResource> iCodeRatableResources=new HashMap<String,CodeRatableResource>(); 
		
		
	  //  List<String> iResouceCodes;
	   // List<String> iAllResouceCodes;
	    List<RuleOfrResourceRel>  allRuleOfrResourceRel = dbConfigDetail.getAllRuleOfrResourceRel();

	    List<RuleOfrResourceRel> resultRuleOfrResourceRel = new ArrayList<RuleOfrResourceRel>();
		//遍历用户套餐，查找累积量配置信息
	    for(PlanDisct planDisct:ratingData.getiPlanDiscts()){
	    	for(RuleOfrResourceRel ruleOfrResourceRel :allRuleOfrResourceRel){
	    		if(planDisct.getnOfrId() == ruleOfrResourceRel.getOfr_B_Id() && nMsgType == Integer.parseInt(ruleOfrResourceRel.getMsg_Types()))
	    		
	    		resultRuleOfrResourceRel.add(ruleOfrResourceRel);
	    	}
	    }
	    
	    List<CodeRatableResource> allCodeRatableResource = dbConfigDetail.getAllCodeRatableResource();
	    List<CodeRatableResource> userCodeRatableResource = new ArrayList<CodeRatableResource>();
	    		
	 //   List<CodeRatableResource> resultCodeRatableResource = new ArrayList<CodeRatableResource>();
	    
	    for(RuleOfrResourceRel ruleOfrResourceRel:resultRuleOfrResourceRel){
	    	boolean findRatableResource = false;
	    	for(CodeRatableResource codeRatableResource:allCodeRatableResource){
	    		
	    		if(ruleOfrResourceRel.getResource_Code() ==codeRatableResource.getRatable_resource_code() ){
	    			findRatableResource =true;
	    			iCodeRatableResources.put(""+ruleOfrResourceRel.getResource_Code(),codeRatableResource);
	    			userCodeRatableResource.add(codeRatableResource);
	    			break;
	    		}
	    		
	    	}
            if(findRatableResource == false){
            	throw new RatingException(RatingErrorCode.ERR_SQL_QUERY_RATABLE,"查询累积量异常");
            }
	    	
	    }
	    
	    ratingData.setiCodeRatableResources(iCodeRatableResources);
	    ratingData.setAllCodeRatableResource(userCodeRatableResource);
	}

	
	/**
	 * 根据套餐获取段落信息
	 * @param planDisct
	 * @return
	 */
	private void  getRuleSectionRules(RatingMsg ratingMsg,UserDiscts codeOfr,List<RuleSectionRule> iRuleSectionRules ) throws Exception {   ////没有必要每次都把所有策略查出来吧??? List<RuleSectionRule> iRuleSectionRules

	    long lnPlanId = codeOfr.getPricing_plan_id();
	    long lnEventTypeId  = ratingMsg.getM_iExtMsg().getM_nEvtTypeId();
	    RuleEvtPricingStrategy iStrategy = new RuleEvtPricingStrategy(); /////策略
	    //List<RuleSectionRule> iRuleSectionRules = new ArrayList<RuleSectionRule>();

	    List<RuleStrategySectRel> iRels = new ArrayList<RuleStrategySectRel>();
		
	    for(RuleEvtPricingStrategy tmpstrategy:dbConfigDetail.getAllRuleEvtPricingStrategy()){
	    	if(tmpstrategy.getPricing_plan_id() ==lnPlanId && tmpstrategy.getEvent_type_id() ==lnEventTypeId){
	    		iStrategy = tmpstrategy;
	    		break;
	    	}
	    }
	    
	    log.debug("资费"+codeOfr.getAtomId()+"的品牌id是"+codeOfr.getBrand_id());
	    if (iStrategy == null || iStrategy.getStrategy_id() == 0) {
	    	
	    	if( codeOfr.getBrand_id() ==101){
	    		throw new RatingException(RatingErrorCode.ERR_NOT_FOUND_STRATERY_BY_PLAN_ID,"没有找到策略");
	    	}else{
	    		return ;
	    	}   	
	    }
	    
	    //iRels = dbUtil.getRuleStrategySectRel(iStrategy.getStrategy_id());
	    for(RuleStrategySectRel tmprel:dbConfigDetail.getAllRuleStrategySectRel()){
	    	if(tmprel.getStrategy_id() == iStrategy.getStrategy_id()){
	    		iRels.add(tmprel);
	    	}
	    }

	    if(iRels ==null ||iRels.size()==0){
	    	//再判断是否需要批价
	    	if(ratingMsg.getMsgMap().containsKey(RatingMacro.PARA_R_ROME_TYPE_02) && ratingMsg.getMsgMap().get(RatingMacro.PARA_R_ROME_TYPE_02).equals("6")){
	    		throw new RatingException(RatingErrorCode.ERR_ROME_NOMONEY,"国际漫游话单未包含消费金额");
	    	}else{
	    		throw new RatingException(RatingErrorCode.ERR_NOT_FOUND_STRATERY_BY_PLAN_ID,"没有找到策略段落关系");
	    	}
	    	
	    }
	    
	    ////开始遍历策略段落关系(rule_section_rule)
	    //iRuleSectionRules = dbConfigDetail.getAllRuleSectionRule();
	    for(RuleSectionRule tmprsr:dbConfigDetail.getAllRuleSectionRule()){
	    	for(RuleStrategySectRel tmprssr:iRels){
	    		if(tmprssr.getPricing_section() == tmprsr.getPricing_section()){
	    			iRuleSectionRules.add(tmprsr);
	    			break;
	    		}
	    	}
	    }
	    
//	    for(RuleStrategySectRel tmprssr:iRels){
//	    	if( dbConfigDetail.getAllRuleSectionRuleMap().containsKey(tmprssr.getPricing_section()+"") ){
//	    			iRuleSectionRules.add(dbConfigDetail.getAllRuleSectionRuleMap().get(tmprssr.getPricing_section()));
//	    	}
//    		
//    	}
	    
	    
	    
	    
	    log.debug("找到段落数量："+iRuleSectionRules.size());

	    //return iRuleSectionRules;

	}
	
	/**
	 * 根据planDisct的nCalcPriority 对套餐列表进行排序
	 * @param listPlanDisct
	 * @return
	 */
//	private void sortListPlanDisct(List<CodeOfr> listPlanDisct) {
//		for(int i=0;i<listPlanDisct.size();i++){
//		//	PlanDisct iplanDisct = listPlanDisct.get(i);
//			for(int j=i+1;j<listPlanDisct.size();j++){
//			//	PlanDisct jplanDisct = listPlanDisct.get(j);
//				if(listPlanDisct.get(i).getPriority() < listPlanDisct.get(j).getPriority()){
//					CodeOfr tmpplanDisct = listPlanDisct.get(i);
//					listPlanDisct.set(i, listPlanDisct.get(j));
//					listPlanDisct.set(j, tmpplanDisct);		
//					tmpplanDisct =null;
//				}
//			}
//		}
//		
//	}

	///业务公式
	private void getFormulaAndAdjust(RatingMsg ratingMsg,RatingData ratingData) throws Exception {
		
		int msgType = ratingMsg.getM_nMsgType();
		RuleFormula ruleFormula = null;
		SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
		Date iCurrTime = sdf.parse(ratingMsg.getAllSessionStartTimes());
		
		//log.debug("getFormulaAndAdjust==时间=="+ratingMsg.getAllSessionStartTimes());
		
		for(RuleFormula tmprf:dbConfigDetail.getAllRuleFormula()){
           if(tmprf.getMsg_Type() == msgType && (
             tmprf.getEff_Date().equals(iCurrTime) || tmprf.getExp_Date().equals(iCurrTime) || (iCurrTime.after(tmprf.getEff_Date()) && iCurrTime.before(tmprf.getExp_Date()) )		   
        		   )){//如果消息类型匼并且时间满足
        	   ruleFormula = tmprf;
        	   break;
           }
		}
		
		if(ruleFormula == null|| ruleFormula.getFormula() ==null || ruleFormula.getFormula().length() ==0){
			//ruleFormula =dbUtil.getFormulaByMsgType(-1); 如果没有找到，则需要根据-1消息类型再找一次
			for(RuleFormula tmprf:dbConfigDetail.getAllRuleFormula()){
		           if(tmprf.getMsg_Type() == -1 && (
		             tmprf.getEff_Date().equals(iCurrTime) || tmprf.getExp_Date().equals(iCurrTime) || (iCurrTime.after(tmprf.getEff_Date()) && iCurrTime.before(tmprf.getExp_Date()) )		   
		        		   )){//如果消息类型匼并且时间满足
		        	   ruleFormula = tmprf;
		        	   break;
		           }
				}	
		}
		if(ruleFormula==null || ruleFormula.getFormula() ==null || ruleFormula.getFormula().length() ==0){
			throw new RatingException(RatingErrorCode.ERR_NOT_FOUND_PRODUCT_FORMULA,"没有找到套餐公式");
		}
		
		String formula = ruleFormula.getFormula();
		
		List<String> listformula = getListfromFormula(formula);
		//公式格式校验
		if(!checkFormulaSyntax(listformula)){
			throw new RatingException(RatingErrorCode.ERR_INVALID_FORMULA_SYNTAX,"套餐公式错误");
		}
		
		///判断用户产品组是否都包含在公式里，如果有产品组不在公式里，返回错误
		
		//简化套餐公式
		formula = simplifyFormula(ratingData,listformula);
		//最终得到的套餐公式是空的
		if(formula == null||formula.length() ==0 || formula.equals("")){
			throw new RatingException(RatingErrorCode.ERR_INVALID_FORMULA_SYNTAX,"简化套餐公式错误");
		}
		
		
		ratingData.setM_strFormula(formula);//获取的公式最后放到ratingdata里
		
	}

	private List<String> getListfromFormula(String formula) {
		String strFormula ="("+formula+")";
		String formulaMember="";
		List<String> formulalist = new ArrayList<String>();
		
		
		for(int i=0;i<strFormula.length();i++){
			char chara=strFormula.charAt(i);
	    	if(isFormulaChar(chara)){
	    		formulaMember = formulaMember+chara;
	    		if(  nextisFormulaChar(strFormula,i)){		////如果下一个字符也是普通字符，则continue    		
	    			continue;
	    		}else{ //如果下一个字符不是特殊字符了，则截取之前的字符串
	    			formulalist.add(formulaMember);
			    	formulaMember="";
	    		}
	    	}else {
	    		formulaMember = formulaMember + chara;
	    		formulalist.add(formulaMember);
	    		formulaMember="";
	    	}
		}
		
    	for(int i=0;i<formulalist.size();i++){
    		log.debug(formulalist.get(i));
    	}

		return formulalist;
	}

	//简化业务公式
	private String simplifyFormula(RatingData ratingData,List<String> listformula) {

		List<String> iEffGroups = ratingData.getiEffGroups();
		String formula="";
		List<String> listofFormulaForRemove = new ArrayList<String>();

	    for( int i =1; i<listformula.size(); ++i )//遍历业务公式
	    {
	        String c = listformula.get(i);
	        String left = listformula.get(i-1);
	        if( c.equals("(") )
	        {
	            //no ops
	        }
	        else if( c.equals(")") )
	        {
	            if( left.equals("(")) //空()，删除
	            {
	            	listformula.set(i, RatingMacro.Character_Space);
	            	listformula.set(i-1, RatingMacro.Character_Space);
	            	listofFormulaForRemove.add(RatingMacro.Character_Space);
	            	listofFormulaForRemove.add(RatingMacro.Character_Space);
	            }
	            else
	            {
	                if( isFormulaOperand( left ) && listformula.get(i-2).equals("(")) //(A) -> A
	                {
	                	listformula.set(i,  RatingMacro.Character_Space);
		            	listformula.set(i-2,  RatingMacro.Character_Space);
		            	listofFormulaForRemove.add(RatingMacro.Character_Space);
		            	listofFormulaForRemove.add(RatingMacro.Character_Space);
	                }
	            }
	        }
	        else if( isFormulaOperand( c ) )
	        {
	            if( !iEffGroups.contains(c) ) //有效套餐组中找不到，则删除
	            {
	                if( left.equals("(")) //(A -> (
	                {
	                	listformula.set(i,  RatingMacro.Character_Space);
	                	listofFormulaForRemove.add(RatingMacro.Character_Space);
	                }
	                else if( isFormulaOperator( left ) ) //op A ->
	                {
	                	listformula.set(i, RatingMacro.Character_Space);
		            	listformula.set(i-1, RatingMacro.Character_Space);
		            	listofFormulaForRemove.add(RatingMacro.Character_Space);
		            	listofFormulaForRemove.add(RatingMacro.Character_Space);
	                }
	            }
	            else
	            {
	            }
	        }
	        else //operator
	        {
	            if( left.equals("(")) //( op -> (
	            {
	            	listformula.set(i, RatingMacro.Character_Space);
	            	listofFormulaForRemove.add(RatingMacro.Character_Space);
	            }
	        }
	    }
	    
	    listformula.removeAll(listofFormulaForRemove);
	    
	    
	    while(listformula.get(0).equals("(") && listformula.get(listformula.size()-1).equals(")")) //外围的无用括号
	    {
	    	listformula.remove(listformula.size()-1);
	    	listformula.remove(0);
	    }

	    if( listformula.size() ==0 )
	        return "";
	    
//	    for(int i=0;i<listformula.size();i++){
//	    	log.debug("printformula=="+listformula.get(i));
//	    }
	    

	    int nOper,nId,nLeft,nRight;
	    nOper = nId = nLeft = nRight = 0;
	    for ( int i=0; i<listformula.size(); ++i )
	    {
	    	String str = listformula.get(i);
	        if ( isFormulaOperand( str ) )
	            nId ++;
	        else if ( isFormulaOperator( str) )
	            nOper ++;
	        else if ( str.equals("(") )
	            nLeft ++;
	        else if ( str.equals(")"))
	            nRight ++;
	    }
	    if ( ( nId - nOper ) > 1 )
	        return "";
	    else if ( ( nId - nOper ) < 1 )
	        return "";
	    if ( nLeft > nRight )
	        return "";
	    else if ( nLeft < nRight )
	        return "";

	    formula = removeTrivialParens( listformula );

		return formula;
	}

	private String removeTrivialParens(List<String> listformula) {
//TODO 去括号
		String formula = "";
		for(String tmpstr :listformula){
			formula = formula +tmpstr;
		}
		 
		return formula;
	}

	//检查业务公式格式
	public boolean checkFormulaSyntax(List<String> formulalist) {
		
//		for(String tmpstr:formulalist){
//			log.debug(tmpstr);
//		}
		
		int iLastTokenType ,iCurrTokenType;
		String isParenPaired =""; //用于判断公式里出现的括号是否成对并顺序匹配

    	int nLen = formulalist.size();

        iLastTokenType = RatingMacro.TOKENNODE_TYPE_LEFTPAREN;

        for( int i=0; i<nLen; i++ )
        {
            String str = formulalist.get(i);
            if( str.equals("(") )
                iCurrTokenType = RatingMacro.TOKENNODE_TYPE_LEFTPAREN;
            else if( str.equals(")") )
                iCurrTokenType = RatingMacro.TOKENNODE_TYPE_RIGHTPAREN;
            else if( isFormulaOperand( str ) )
                iCurrTokenType = RatingMacro.TOKENNODE_TYPE_OPERAND;
            else if( isFormulaOperator( str ) )
                iCurrTokenType = RatingMacro.TOKENNODE_TYPE_OPERATOR;
            else
                return false;

            if( iLastTokenType == RatingMacro.TOKENNODE_TYPE_LEFTPAREN )
            {
                if( iCurrTokenType == RatingMacro.TOKENNODE_TYPE_OPERATOR )
                    return false;
            }
            else if( iLastTokenType == RatingMacro.TOKENNODE_TYPE_OPERATOR )
            {
                if( iCurrTokenType == RatingMacro.TOKENNODE_TYPE_RIGHTPAREN )
                    return false;
            }
            else if( iLastTokenType == RatingMacro.TOKENNODE_TYPE_OPERAND )
            {
                if( iCurrTokenType == RatingMacro.TOKENNODE_TYPE_OPERAND || iCurrTokenType == RatingMacro.TOKENNODE_TYPE_LEFTPAREN )
                    return false;
            }
            else //right paren
            {
                if( iCurrTokenType == RatingMacro.TOKENNODE_TYPE_LEFTPAREN || iCurrTokenType == RatingMacro.TOKENNODE_TYPE_OPERAND )
                    return false;
            }
            
            if( str.equals("(") )
            {
            	isParenPaired = isParenPaired+"("; 
            }
            else if( str.equals(")") )
            {
                if(isParenPaired.length() ==0 )
                    return false;
                else
                	isParenPaired = isParenPaired.substring(0,isParenPaired.length()-1);
            }
            iLastTokenType = iCurrTokenType;
        }
        
        if( isParenPaired.length()>0 ){
        	return false;
        }

		return true;
	}

	private boolean isFormulaOperand(String str) {
		
		if(str.length()>1){
			return true;
		}

		if(str.indexOf("(") >=0 || str.indexOf(")") >=0 || str.indexOf("*") >=0 || str.indexOf(",") >=0 || str.indexOf("^") >=0){
			return false;
		}
		return true;
	}

	/**
	 * 获取组内优惠模式
	 * @throws Exception
	 */
	private void getOfrGroupFav(RatingData ratingData) throws Exception{
	
		Map<String, Integer> iGroupFavModes = ratingData.getiGroupFavModes();
		List<String> iEffGroups = ratingData.getiEffGroups();

		for(String tmpgroup:iEffGroups){
			int favMode =-1;
			for(RuleGroupFavMode tmpfavmode:dbConfigDetail.getAllRuleGroupFavMode()){
				if(tmpfavmode.getOfr_Group() == Integer.parseInt(tmpgroup)){
					favMode = tmpfavmode.getFav_Mode();
					break;
				}
			}
			
			if(favMode ==-1){
				throw new RatingException(RatingErrorCode.ERR_NOT_FOUND_GROUP_FAV_MODE,"产品组"+tmpgroup+"没有找到优惠模式");
			}
			
			iGroupFavModes.put(tmpgroup, favMode);
			
		}
	
          ratingData.setiGroupFavModes(iGroupFavModes);
		    
	}

	////获取套餐分组信息
	private void getOfrGroupAndPrior(RatingMsg ratingMsg,RatingData ratingData) throws Exception {
	    RuleOfrGroup iOfrGroup =null ;

	    int nMsgType = ratingMsg.getM_nMsgType();
	    List<String> iEffGroups = new ArrayList<String>();
	   // List<CodeOfr> userCodeOfrs = ratingData.getUserCodeOfrs();
	    List<UserDiscts> listOfUserDiscts = ratingData.getListOfUserDiscts();
	    Map<String, List<UserDiscts>> iCodeOfrInGroups=new HashMap<String,List<UserDiscts>>();

	    //for(CodeOfr tmpco:userCodeOfrs){
	    for(UserDiscts tmpud:listOfUserDiscts){
	    	for(RuleOfrGroup tmprog:dbConfigDetail.getAllRuleOfrGroup()){
	    		if(tmprog.getAtom_Ofr() == tmpud.getAtomId() && tmprog.getMsg_Type() == nMsgType){
	    			iOfrGroup = tmprog;
	    			break;
	    		}
	    	}
	    	
	    	//如果没有找到则根据-1消息类型再找一次
	    	if(iOfrGroup == null){
	    		for(RuleOfrGroup tmprog:dbConfigDetail.getAllRuleOfrGroup()){
		    		if(tmprog.getAtom_Ofr() == tmpud.getAtomId() && tmprog.getMsg_Type() == -1){
		    			iOfrGroup = tmprog;
		    			break;
		    		}
		    	}
	    	}
	    	
	    	if(iOfrGroup == null){
	    		throw new RatingException(RatingErrorCode.ERR_NOT_FOUND_OFR_GROUP,"原子套餐"+tmpud.getAtomId()+"没有找到产品组");
	    	}
	    	
	    	if(iCodeOfrInGroups.containsKey(iOfrGroup.getOfr_Group())){
	    		List<UserDiscts> listCodeOfr = iCodeOfrInGroups.get(iOfrGroup.getOfr_Group());
	    		listCodeOfr.add(tmpud);
	    		iCodeOfrInGroups.remove(iOfrGroup.getOfr_Group());
	    		iCodeOfrInGroups.put(iOfrGroup.getOfr_Group(), listCodeOfr);
	    		
	    		
	    	}else{
	    		List<UserDiscts> listCodeOfr = new ArrayList<UserDiscts>();
	    		listCodeOfr.add(tmpud);
	    		iCodeOfrInGroups.put(iOfrGroup.getOfr_Group(), listCodeOfr);
	    	}
	    	
	    	if(!iEffGroups.contains(iOfrGroup.getOfr_Group())){
	    		iEffGroups.add(iOfrGroup.getOfr_Group());
	    	}


	    }
	    
	    //LOG
//	    for(String key:iCodeOfrInGroups.keySet()){
//	    	List<CodeOfr> listCodeOfr = iCodeOfrInGroups.get(key);
//	    	log.debug("当前产品组=="+key);
//	    	for(CodeOfr co:listCodeOfr){
//	    	    co.print();
//	    	}
//	    }
	   
           ratingData.setiEffGroups(iEffGroups);
           ratingData.setiCodeOfrInGroups(iCodeOfrInGroups);
		
	}
	//获取套餐信息
	private void getPricingPlanId(RatingMsg ratingMsg,RatingData ratingData) throws Exception {
		
		List<LifeUserProduct>  listOfUserProducts = new ArrayList<LifeUserProduct>();
		List<UserDiscts> listOfUserDiscts = new ArrayList<UserDiscts>();

//	    ///IVPN集团定价计划
//	    getIVPNPricingPlanId();
//	    ///VPN集团定价计划
//	    getVPNPricingPlanId();
//	    ///VPN用户集团定价计划
//	    getUserVPNPricingPlanId();
//	//ff亲友定价计划 
//	    getFFPricingPlanId();
//	//根据用户id、客户id、生失效时间、本地网标识 查找用户可用的资费信息 
		listOfUserProducts = getFavPricingPlan(ratingMsg);/////普通优惠销售品
	    
	//获取用户基础套餐  zhanghb add 标准套餐
	    getBasePlanID(ratingMsg,listOfUserProducts);
	    
	    //获取B套餐和原子套餐
	    listOfUserDiscts = getCodeOfrandAtoM(ratingMsg,ratingData,listOfUserProducts);
	    
	    log.debug("最终获得的用户原子套餐数量--->"+listOfUserDiscts.size());
	    printatomdiscts(ratingData);
	    
	//从套餐列表删除不适用的用户套餐   zhanghb add 
	    delInvalidPricingPlanId(ratingMsg,ratingData);
	    
	    sortUserdiscts(ratingData);
	    
	    printatomdiscts(ratingData); ///排序后再次打印原子套餐列表

//	    ///设置激活日期
//	    setActiveDate();
//	    ///设置首月标志
//	    setFirstMonthFlag();
//	    ///获取小区优惠标志
//	    setCellFlag();
//	    //支持首月偏移量套餐
//	    calcCoefficient();
	    ///套餐折线
	    //getBillOfrPricingPlan(); 放在前面获取套餐的时候实现了

		
	}
	
	private void sortUserdiscts(RatingData ratingData) {
		
		List<UserDiscts> listOfUserDsicts = ratingData.getListOfUserDiscts();
		
		
		for(int i=0;i<listOfUserDsicts.size();i++){
				for(int j=i+1;j<listOfUserDsicts.size();j++){
					if(listOfUserDsicts.get(i).getPriority() < listOfUserDsicts.get(j).getPriority()){
						UserDiscts tmpud = listOfUserDsicts.get(i);
						listOfUserDsicts.set(i, listOfUserDsicts.get(j));
						listOfUserDsicts.set(j, tmpud);		
						tmpud =null;
					}
				}
			}
		
		ratingData.setListOfUserDiscts(listOfUserDsicts);
		
		
	}

	private void printatomdiscts(RatingData ratingData) {

		 List<UserDiscts> listofuserdiscts = ratingData.getListOfUserDiscts();
		 
		 for(UserDiscts tmpdisct:listofuserdiscts){
			 tmpdisct.print();
		 }

		
	}

	/**
	 * 获取用户订购映射的B套餐和原子套餐
	 * @param listOfUserDiscts
	 * @param listOfUserProducts
	 */
	private List<UserDiscts> getCodeOfrandAtoM(RatingMsg ratingMsg,RatingData ratingData, List<LifeUserProduct>  listOfUserProducts) throws Exception {
		
		List<RuleOfrSplit> allRuleOfrSplit = dbConfigDetail.getAllRuleOfrSplit();
		List<CodeOfr> allCodeOfr = dbConfigDetail.getAllCodeOfr();
		List<UserDiscts> listOfuserDiscts =  new ArrayList<UserDiscts>();
		
		//log.debug("listOfUserProducts.size=="+listOfUserProducts.size());
		
//		log.debug("获得用户套餐数："+listOfUserProducts.size()+",ofrid="+listOfUserProducts.get(0).getOfr_id());
//		listOfUserProducts.get(0).setOfr_id("252");
//		listOfUserProducts.get(0).setProduct_id("252");
		
		for(LifeUserProduct lifeUserProduct:listOfUserProducts){
			
			CodeOfr codeOfr = null ;
			CodeOfr atomCodeOfr = null;
			RuleOfrSplit ruleOfrSplit = null ;
			for(RuleOfrSplit tmpsplit:allRuleOfrSplit){
				if(tmpsplit.getProduct_id().equals(lifeUserProduct.getProduct_id())){//改为取product_id
					ruleOfrSplit = tmpsplit;
					break;
				}
			}

			if(ruleOfrSplit == null || ruleOfrSplit.getOfr_b_id()==null){
				continue;
				//throw new RatingException(RatingErrorCode.ERR_NOT_BASE_TAFIFF_OUTPUT,"没有找到"+lifeUserProduct.getProduct_id()+"对应的B套餐映射关系");
			}
			
			for(CodeOfr tmpofr:allCodeOfr){
				if(ruleOfrSplit.getOfr_b_id() ==null){
					log.debug("=========ruleOfrSplit:["+ruleOfrSplit.getOfr_b_id()+"]");
					continue;
				}
				log.debug("=========ruleOfrSplit:["+ruleOfrSplit.getOfr_b_id()+"]");
				log.debug("=========tmpofr:["+tmpofr.getOfr_b_id()+"]");
				if( ruleOfrSplit.getOfr_b_id().equals(""+tmpofr.getOfr_b_id() )){
					codeOfr = tmpofr;
					break;
				}				
			}
			
			if(codeOfr == null){
				throw new RatingException(RatingErrorCode.ERR_NOT_BASE_TAFIFF_OUTPUT,"没有找到"+lifeUserProduct.getProduct_id()+"对应的B套餐");
			}
			
			
			if(codeOfr.getOfr_type_id()  ==1){//如果ofrtypeid是1，本身就是原子套餐
				UserDiscts userDiscts = new UserDiscts(codeOfr);
				userDiscts.setOfr_b_id(codeOfr.getOfr_b_id());
				userDiscts.setOfrId(lifeUserProduct.getOfr_id());
			
				addIntoListOfuserDiscts(listOfuserDiscts,userDiscts);
				//listOfuserDiscts.add(userDiscts);
		     }else{
		    	 //不是原子套餐，需要到套餐折线表查原子套餐
				List<RuleDinnerSelect> dinnerSelectList = dbConfigDetail.getAllRuleDinnerSelect();
				int innetDays=-1;//入网天数
				int innetMonths=-1;//入网月数
				int dinnerEffDays=-1;//套餐生效天数
				int dinnerEffMonths=-1;//套餐生效月数
		
				if(!ratingMsg.getUserinfoForMemcached().getInfoUser().getUser_status().equals(ParamData.USER_ACTIVE_N)){//用户已激活
					innetDays=getInnetDays(ratingMsg);
					innetMonths=getInnetMonths(ratingMsg);
					dinnerEffDays=getDinnerEffDays(ratingMsg);
					dinnerEffMonths=getDinnerEffMonths(ratingMsg);
				}
			
			for(RuleDinnerSelect dinner:dinnerSelectList){
				  if(dinner.getOfr_b_id() != codeOfr.getOfr_b_id() 
						  || dinner.getMsg_type() != ratingMsg.getM_nMsgType()  ){
					  continue;
				  }
				
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
					
				String latn=ratingMsg.getUserinfoForMemcached().getInfoUser().getLocal_net();
				if(dinner.getLatn_id()== Integer.parseInt(latn) 
						|| dinner.getLatn_id() == -1){//-1是通用折线
					
					int atomofrid = dinner.getAtom_ofr();
					CodeOfr atomcodeofr = null;
					for(CodeOfr tmpofr:allCodeOfr){
						if(atomofrid ==tmpofr.getOfr_b_id() ){
							atomcodeofr = tmpofr;
							break;
						}				
					}
					
					if(atomcodeofr == null){
						throw new RatingException(RatingErrorCode.ERR_NOT_BASE_TAFIFF_OUTPUT,"原子套餐"+atomofrid+"在资费表不存在");
					}

					
					UserDiscts userDiscts = new UserDiscts(atomcodeofr);
					userDiscts.setOfr_b_id(codeOfr.getOfr_b_id());
					userDiscts.setOfrId(lifeUserProduct.getOfr_id());
					addIntoListOfuserDiscts(listOfuserDiscts,userDiscts);
					//listOfuserDiscts.add(userDiscts);
				
					
				}
			}
          }
			//、、、、、
	
		}
		
		if(listOfuserDiscts.size() ==0){
			throw new RatingException(RatingErrorCode.ERR_NOT_BASE_TAFIFF_OUTPUT,"没有找到用户原子套餐");
		}
		
		ratingData.setListOfUserDiscts(listOfuserDiscts);
		
		return listOfuserDiscts;
	}

	
	


//如果list里还没有这个原子套餐，就加上
	private void addIntoListOfuserDiscts(List<UserDiscts> listOfuserDiscts,
			UserDiscts userDiscts) {
	
		boolean existFlag =false;
		for(UserDiscts tmpud:listOfuserDiscts){
			if(tmpud.getAtomId() == userDiscts.getAtomId()){
				existFlag= true;
				break;
			}
		}
		
		if(existFlag == false){
			listOfuserDiscts.add(userDiscts);
		}
		
	}


	/**
	 * 删除不适用的资费
	 */
	private void delInvalidPricingPlanId(RatingMsg ratingMsg,RatingData ratingData) {
		//List<CodeOfr> userCodeOfrs = ratingData.getUserCodeOfrs();
		List<UserDiscts> listOfUserDiscts = ratingData.getListOfUserDiscts();
		List<UserDiscts> listresult = new ArrayList<UserDiscts>();
		long eventTypeId = ratingMsg.getM_iExtMsg().getM_nEvtTypeId();
		for(UserDiscts tmpud:listOfUserDiscts){
			//时间判断

			//策略判断
			for(RuleEvtPricingStrategy tmpstrategy:dbConfigDetail.getAllRuleEvtPricingStrategy()){
				if(tmpstrategy.getEvent_type_id() == eventTypeId && tmpstrategy.getPricing_plan_id() == tmpud.getPricing_plan_id()){
					listresult.add(tmpud);
					break;
				}
			}

		}

		ratingData.setListOfUserDiscts(listOfUserDiscts);
	}

	//获取用户基础套餐，也就是user表里记录的ofrid
	private void getBasePlanID(RatingMsg ratingMsg,List<LifeUserProduct>  listOfUserProducts) throws Exception {
		
		//InfoUser infoUser = ratingMsg.getInfoUser();
		InfoUser infoUser = ratingMsg.getUserinfoForMemcached().getInfoUser();
		if(infoUser.getProduct_id() == null || infoUser.getProduct_id().length() ==0){
			return ;
		}
		
		boolean existFlag = false;
		for(LifeUserProduct lifeUserProduct:listOfUserProducts){
			if(lifeUserProduct.getOfr_id().equals(infoUser.getProduct_id())){
				existFlag = true;
				break;
			}
		}
		
		if(existFlag == false){
			
			SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmmss");
			LifeUserProduct baseUserProduct = new LifeUserProduct();
			baseUserProduct.setUser_id(infoUser.getUser_id());
			baseUserProduct.setOfr_id(infoUser.getProduct_id());
			baseUserProduct.setProduct_id(infoUser.getProduct_id());
			baseUserProduct.setEff_date(sdf.parse("20000101000000"));
			baseUserProduct.setExp_date(sdf.parse("30001231235959"));
			
			listOfUserProducts.add(baseUserProduct);
		}
		
	   
//		List<CodeOfr> userCodeOfrs = ratingData.getUserCodeOfrs();
//		int baseOfrId = ratingMsg.getM_iUserMsg().getnOfrId();
//		boolean existFlag = false;
//		int atomId = getAtomOfrId(baseOfrId,ratingMsg.getM_nMsgType());///获取原子套餐
//		
//		for(CodeOfr tmpuserco:userCodeOfrs){
//			if(tmpuserco.getOfr_b_id() == atomId){
//				existFlag =true;
//			}
//		}
//		
//		if(existFlag == false){
//			for(CodeOfr tmpco:dbConfigDetail.getAllCodeOfr()){
//				if(tmpco.getOfr_b_id() == atomId){
//					tmpco.setOfr_desc(""+baseOfrId);
//					userCodeOfrs.add(tmpco);
//					break;
//				}
//			}
//		}
//
//		ratingData.setUserCodeOfrs(userCodeOfrs);
	}

	/**
	 * 获取用户套餐
	 */
	private List<LifeUserProduct> getFavPricingPlan(RatingMsg ratingMsg) throws Exception {
		
		List<LifeUserProduct> listOfLifeUserProducts = ratingMsg.getUserinfoForMemcached().getUserProducts();   // userinfoForMemcached.getUserProducts();
		List<LifeUserProduct> listResult = new ArrayList<LifeUserProduct>();
		//log.debug("资费订购数量===="+listOfLifeUserProducts.size());
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmmss");
		Date currTime = sdf.parse(ratingMsg.getAllSessionStartTimes());

		for(LifeUserProduct lifeUserProduct:listOfLifeUserProducts){
			if( (lifeUserProduct.getEff_date().before(currTime) ||lifeUserProduct.getEff_date().equals(currTime)) 
					&&(lifeUserProduct.getExp_date().after(currTime) || lifeUserProduct.getExp_date().equals(currTime)) ){
				//时间符合的加入到listresult
				listResult.add(lifeUserProduct);
			}
		}

		return listResult;
		
	}

	private boolean isFormulaChar(char c) {
		
		if((c>='0' && c<='9')|| (c>='a' && c<='z') ||(c>='A' &&c <='Z')  ){
			return true;
		}
		return false;
	}

	private boolean nextisFormulaChar(String str,int i){
		
		if(i < str.length()-1){
		  char charnext =str.charAt(i+1);
		  if((charnext>='0' && charnext<='9')|| (charnext>='a' && charnext<='z') ||(charnext>='A' && charnext <='Z')  ){
			  return true;
		  }
				return false;
		}

		return false;
	}
	
	/**
     * 判断套餐公式的某个节点是否逻辑操作符
     * @param formulaMember
     * @return
     */
	private boolean isFormulaOperator(String c) {
		//if(c =='*'||c =='^'||c ==','){
			if(c.equals("*") ||c.equals("^") ||c.equals(",")  ){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 比较左右两个批价结果，如果leftnode更省，则返回true，否则返回false
	 * @param leftNode
	 * @param rightNode
	 * @return
	 */
	
     private boolean compareRateResult(TokenNode leftNode, TokenNode rightNode) {
    	 RateData leftRateData = leftNode.getM_iRateData();
    	 RateData rightRateData = rightNode.getM_iRateData();
    	 
    	 if(leftRateData.sum()<rightRateData.sum()){
    		 return true;
    	 }
		return false;
	}
	
	
	
}
