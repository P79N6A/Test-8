package com.tydic.beijing.billing.rating.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import sun.security.action.GetIntegerAction;

import com.tydic.beijing.billing.rating.domain.ChargeUnit;
import com.tydic.beijing.billing.rating.domain.Discount;
import com.tydic.beijing.billing.rating.domain.Fee;
import com.tydic.beijing.billing.rating.domain.FeeInfo;
import com.tydic.beijing.billing.rating.domain.OfrRateData;
import com.tydic.beijing.billing.rating.domain.ParamData;
import com.tydic.beijing.billing.rating.domain.PlanDisct;
import com.tydic.beijing.billing.rating.domain.PricingSection;
import com.tydic.beijing.billing.rating.domain.RatableResourceInfo;
import com.tydic.beijing.billing.rating.domain.RatableResourceValue;
import com.tydic.beijing.billing.rating.domain.RateData;
import com.tydic.beijing.billing.rating.domain.RateInfo;
import com.tydic.beijing.billing.rating.domain.RateMeasure;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.RuleFormula;
import com.tydic.beijing.billing.rating.domain.RulePricingSectionDisct;
import com.tydic.beijing.billing.rating.domain.SectionRateData;
import com.tydic.beijing.billing.rating.domain.TariffResult;
import com.tydic.beijing.billing.rating.domain.TokenNode;
import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.dto.impl.DbUtilImpl;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;
import com.tydic.beijing.billing.rating.service.ErrorInfo;
import com.tydic.beijing.billing.rating.service.RateDinnerFormula;
import com.tydic.beijing.billing.rating.service.RatingException;
import com.tydic.beijing.billing.rating.util.DateUtil;


public class RateDinnerFormulaImpl implements RateDinnerFormula {
	private Logger log=Logger.getLogger(RateDinnerFormulaImpl.class);
	


	private RatingMsg ratingMsg;
	private RatingData ratingData;
	private DinnerConversionImpl dinnerConversion;  
	private RatableResourceExtractionImpl resource;
	private RateData result=null;
	private String ruleFormula="";//业务公式
	private DbConfigDetail  dbConfig=null;
	
	private GroupRatingSvc  groupRateA;
	
	private GroupRatingSvc groupRateB;
	
	private String currTime="";
	
	private List<RulePricingSectionDisct> discounts=null;
	@Autowired
	private ApplicationContextHelper applicationContextHelper;

	public RateDinnerFormulaImpl(RatingMsg msg,RatingData ratingData){
		this.ratingMsg=msg;
		this.ratingData=ratingData;
	}
	
	
	public RateDinnerFormulaImpl(RatingMsg msg ,RatingData ratingData ,DinnerConversionImpl data,RatableResourceExtractionImpl resource){
		this.ratingMsg=msg;
		this.ratingData=ratingData;
		this.dinnerConversion=data;
		this.resource=resource;
	}
	
	@Override
	public RateData rate() throws RatingException {
		
		log.debug("rate()::根据消息获取时间:::为:"+ratingMsg.getBaseMsg().getM_strStartTime());
		log.debug("this.currTime:::"+this.currTime);
		
		groupRateA=new GroupRatingSvc(ratingMsg,ratingData,dinnerConversion,resource);
		groupRateB=new GroupRatingSvc(ratingMsg,ratingData,dinnerConversion,resource);
		
		groupRateA.setCurrTime(currTime);
		groupRateB.setCurrTime(currTime);
		
		if(dbConfig==null){
			dbConfig=(DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
			discounts=dbConfig.getDiscounts();
		}
		if(!getFormulaAndAdjust()){
			throw new RatingException(ErrorInfo.ERR_INVALID_FORMULA_SYNTAX,"产品公式不存在或格式错误");
		}
		RateMeasure iRateMeasure=ratingData.getiRateMeasure();
		Map<String,RatableResourceValue> iRatableValues=ratingData.getiTmpRatableResourceValues();
		log.debug("业务公式:"+ruleFormula);
		
		TokenNode resultNode=new TokenNode();
		List<TokenNode> resultGroup=new ArrayList<TokenNode>();
		List<String> groups=new ArrayList<String>();
		List<String>  opers=new ArrayList<String>();
		
		getSubFormula(groups,opers);
		
		
		if(groups.size()==1){
			TokenNode nodeA=new TokenNode();
			if(!nodeA.isM_bIsResult()){
				nodeA.getM_iRateData().setiRateMeasure(iRateMeasure);
				nodeA.getM_iRateData().setiRatableValues(iRatableValues);
				nodeA.setM_nValue(groups.get(0));
				nodeA.setiTokenType(RatingMacro.TOKENNODE_TYPE_OPERAND);
				groupRateA.setGroupNode(nodeA);
				if(groupRateA.rateGroup()==ErrorInfo.ERR_IN_RATING)
					throw new RatingException(ErrorInfo.ERR_IN_RATING,"批价错误");
				result=groupRateA.getGroupRateResult();
				
			}
			
		}else{
		
		
		while(groups.size()>=1){
			TokenNode nodeA=new TokenNode();
			if(!resultGroup.isEmpty()){
				nodeA=resultGroup.get(0);
			}else{
				nodeA.setiTokenType(RatingMacro.TOKENNODE_TYPE_OPERAND);
				nodeA.setM_nValue(groups.get(0));
				groups.remove(0);
			}
			
			TokenNode oper=new TokenNode();
			oper.setiTokenType(RatingMacro.TOKENNODE_TYPE_OPERATOR);
			oper.setM_nValue(opers.get(0));
			opers.remove(0);
			
			TokenNode nodeB=new TokenNode();
			nodeB.setiTokenType(RatingMacro.TOKENNODE_TYPE_OPERAND);
			nodeB.setM_nValue(groups.get(0));
			groups.remove(0);
			
			switch(oper.getM_nValue().charAt(0)){
			case RatingMacro.FORMULA_FAV_BEST:
				if(!nodeA.isM_bIsResult()){
					nodeA.getM_iRateData().setiRateMeasure(iRateMeasure);
					nodeA.getM_iRateData().setiRatableValues(iRatableValues);

					groupRateA.setGroupNode(nodeA);

					if(groupRateA.rateGroup()==ErrorInfo.ERR_IN_RATING){
						throw new RatingException(ErrorInfo.ERR_IN_RATING,"批价错误");
					}
					log.debug("套餐组A:"+nodeA.getM_nValue());
					groupRateA.getGroupRateResult().print(); 
				}
				
				if(!nodeB.isM_bIsResult()){
					nodeB.getM_iRateData().setiRateMeasure(iRateMeasure);
					nodeB.getM_iRateData().setiRatableValues(iRatableValues);

					groupRateB.setGroupNode(nodeB);

					if(groupRateB.rateGroup()==ErrorInfo.ERR_IN_RATING){
						throw new RatingException(ErrorInfo.ERR_IN_RATING,"批价错误");
					}
					log.debug("套餐组B:"+nodeB.getM_nValue());
					groupRateB.getGroupRateResult().print(); 
				}
				
				if(groupRateA.getGroupRateResult().compareTo(groupRateB.getGroupRateResult())<0){
					resultNode.setM_iRateData(groupRateA.getGroupRateResult());
					resultNode.setM_nValue(nodeA.getM_nValue());
				}else{
					resultNode.setM_iRateData(groupRateB.getGroupRateResult());
					resultNode.setM_nValue(nodeB.getM_nValue());
				}
				break;
			case RatingMacro.FORMULA_FAV_ADD:
				if(!nodeA.isM_bIsResult()){
					nodeA.getM_iRateData().setiRateMeasure(iRateMeasure);
					nodeA.getM_iRateData().setiRatableValues(iRatableValues);
					groupRateA.setGroupNode(nodeA);
					if(groupRateA.rateGroup()==ErrorInfo.ERR_IN_RATING){
						throw new RatingException(ErrorInfo.ERR_IN_RATING,"批价错误");
					}
					
				}
				//根据上个套餐组批价结果初始化当前套餐组,包括未批价使用量等
				nodeB.getM_iRateData().init(nodeA.getM_iRateData());
				groupRateB.setGroupNode(nodeB);
				if(groupRateB.rateGroup()==ErrorInfo.ERR_IN_RATING){
					throw new RatingException(ErrorInfo.ERR_IN_RATING,"批价错误");
				}
				resultNode.setM_iRateData(groupRateB.getGroupRateResult());
				resultNode.setM_nValue(nodeB.getM_nValue());
				
				break;
			case RatingMacro.FORMULA_FAV_MUTEX:
				if(nodeA.isM_bIsResult()){
					resultNode.setM_iRateData(nodeA.getM_iRateData());
					resultNode.setM_nValue(nodeA.getM_nValue());
				}else{
					nodeA.getM_iRateData().setiRateMeasure(iRateMeasure);
					nodeA.getM_iRateData().setiRatableValues(iRatableValues);
					groupRateA.setGroupNode(nodeA);
					if(groupRateA.rateGroup()==ErrorInfo.ERR_IN_RATING){
						nodeB.getM_iRateData().setiRateMeasure(iRateMeasure);
						nodeB.getM_iRateData().setiRatableValues(iRatableValues);
						groupRateB.setGroupNode(nodeB);
						if(groupRateB.rateGroup()==ErrorInfo.ERR_IN_RATING){
							throw new RatingException(ErrorInfo.ERR_IN_RATING,"批价错误");
						}else{
							resultNode.setM_iRateData(groupRateB.getGroupRateResult());
							resultNode.setM_nValue(nodeB.getM_nValue());
						}
					}else{
						resultNode.setM_iRateData(groupRateA.getGroupRateResult());
					}
					
				}
				break;
			}
			resultNode.setiTokenType(RatingMacro.TOKENNODE_TYPE_OPERAND);
			resultNode.setM_bIsResult(true);
			resultGroup.clear();
			resultGroup.add(resultNode);
			
		}	
		
		TokenNode node=resultGroup.get(0);
		if(node.isM_bIsResult()){
			result=node.getM_iRateData();
		}else{
			node.getM_iRateData().setiRateMeasure(iRateMeasure);
			node.getM_iRateData().setiRatableValues(iRatableValues);
			GroupRatingSvc groupRate=(GroupRatingSvc)applicationContextHelper.getBean("GroupRatingSvc");
			groupRate.setGroupNode(node);
			if(groupRate.rateGroup()==ErrorInfo.ERR_IN_RATING){
				throw new RatingException(ErrorInfo.ERR_IN_RATING,"批价错误");
			}else{
				result=groupRate.getGroupRateResult();
			}
		}
		}
		getTariffResultFromRateResult(result);
		getRateInfoFromTariffResult();
		
		setFeeInfo(result);
		
		discount();

	    return result;
		
		
	}
	
	
	
	public int setFeeInfo(RateData rateData){
		if(rateData.getiOfrIds().isEmpty()){
			return -1;
		}

		ratingData.setLnRateUnit(rateData.getLnRateUnit());
		ratingData.setdRateValue(rateData.getdRateValue());
//		setCurrentPlanId(nPricingPlanId);    //回写主套餐定价计划----暂时省略
		getUnusedMoneyFromSession();
		getFeeInfo(rateData);
		//保留多扣的钱到session中
		
		ratingData.setLnUnUsedDuration(0);
		ratingData.setLnRatableUnUsedDuration(0);
		
		//设置vpn类型   ----ratingMsg未实现--略
		
		//设置是否亲情号码-------ratingMsg未实现---略
		
		//设置主被 叫集团信息-------略
		
//		if(ratingMsg.getOper()==RatingMacro.OPER_REAL){  //处理实扣消息时记录下来的费用   才设置进去
//			List<OfrRateData> ofrs=rateData.getiOfrResults();
//			for(int i=0 ; i<ofrs.size() ; ++i){
////				log.debug("nofrId======"+ofrs.get(i).getnOfrId());//1010001
//				setFeeInfo(ofrs.get(i).getnOfrId(),0);
//			}
//		}
//		
		
		return 0;
	}
	
	
	
	private void setFeeInfo(long ofrId,long money){
		FeeInfo iFeeInfo=new FeeInfo();
		iFeeInfo.setLnCurrOfrId(ofrId);
		iFeeInfo.setLnMoney(money);
		
		List<FeeInfo> feeInfos=ratingData.getiFeeInfos();

		boolean find=false;
		FeeInfo fee=new FeeInfo();
		for(int i=0 ; i< feeInfos.size() ; ++ i){
			fee=feeInfos.get(i);
			if(fee.getLnCurrOfrId()==iFeeInfo.getLnCurrOfrId() ){
				find=true;
				break;
			}
		}
		if(find){
			fee.setLnMoney(money);
			
		}else{
			ratingData.getiFeeInfos().add(iFeeInfo);
		}
	}
	
	/**
	 * @author sung
	 *
	 * @param data
	 * @return
	 */
	private int getFeeInfo(RateData data){
		ratingMsg.getM_iRatingExtMsg().setM_lnMoney(0);
		List<OfrRateData> results=data.getiOfrResults();
		int size=results.size();
		int i=0;
		for(;i<size;++i){
			OfrRateData ofr=results.get(i);
			PlanDisct plan=dinnerConversion.getiAtomPlanDisctMap().get(""+ofr.getLnAtomOfrId());
			
			if(plan==null){
				log.debug("查找没有此PLANDISCT");
			}
			List<SectionRateData> secResults=ofr.getiSectionResults();
			int len=secResults.size();
			int j=0;
			for(;j<len;++j){
				SectionRateData sec=secResults.get(j);
				if(sec.getiTariffResults().isEmpty()){
					continue;
				}
				ChargeUnit iChargeUnit=new ChargeUnit();
				PricingSection iPricingSection=plan.getiPricingSectionMap().get(""+sec.getLnPricingSectionId());
				//设置B07的值
				iChargeUnit.setLnServId(ratingMsg.getM_iServInfo().getLnServId()); 
				iChargeUnit.setStrMeasureDomain(""+iPricingSection.getStrMeasureDomain());
//				iChargeUnit.setLnBillingDosage(sec.getLnChargedDosage());
//				//更新累积量会用到
//				iChargeUnit.setLnBeforeDosage(sec.getLnChargedDosage()+sec.getLnRatableUnusedDosage());
				//暂时设置为0
				iChargeUnit.setLnBeforeDosageFront(0);
				iChargeUnit.setLnCounts(sec.getnChargedCount());
				iChargeUnit.setLnRateUnit(sec.getLnRateUnit());
				//pricingSection的AcctItemType标识
				iChargeUnit.setLnAcctItemTypeId(iPricingSection.getLnAcctItemId());  
				iChargeUnit.setLnOriCharge((long)sec.getdOrigFee());
				iChargeUnit.setLnCharge((long)sec.getdOrigFee());
				iChargeUnit.setnUnitType(sec.getnUnitType());
				iChargeUnit.setLnFee(sec.getLnTotalFee());
				iChargeUnit.setLnDiscountId(iPricingSection.getLnDiscountId());
				iChargeUnit.setnRatableFlag(0);
				iChargeUnit.setStrRatableCode(iPricingSection.getStrRefResourceCode());
				iChargeUnit.setnMTChangeFlag(0);
				iChargeUnit.setnTailMode(iPricingSection.getnTailMode());
				iChargeUnit.setLnUnusedMondey((long)sec.getdUnusedFee());
				iChargeUnit.setLnPricingSectionId(iPricingSection.getLnPricingSectionId());
				iChargeUnit.setLnLeftMoney(0);
				iChargeUnit.setnPlanId(plan.getnPricingPlanId());
				iChargeUnit.setnOfrId(plan.getnOfrId());
				iChargeUnit.setLnOfrInstId(plan.getLnOfrInstId());
				
				//使用上次的,保留本次的(对于多圆整的钱),只有在实扣时才用
//				if(( ratingData.getnFeeType() == RatingMacro.REAL_REQ_FEE && ratingData.getnOper()== RatingMacro.OPER_REAL )||
//						ratingData.getnFeeType()==RatingMacro.ONLY_REAL_FEE){
					
					Fee ifee=new Fee();
					Fee itFee;
					List<Fee> fees=ratingData.getiUnusedMoneys();
//					log.debug("fees size:"+fees.size());
					int s=fees.size();
					int n=0;
					for(; n<s ; ++n){
						itFee= fees.get(n);
						if(itFee.getLnTariff()==0){
							continue;
						}
						if(itFee.getLnAcctItemTypeId() == iPricingSection.getLnAcctItemId() && itFee.getnUnitType()==sec.getnUnitType()){
							//未圆整的金钱
							double dTempNowCharge=iChargeUnit.getLnOriCharge()* RatingMacro.TARIFF_PRECISION*1.0 -sec.getdUnusedFee();
							//减去上一次多圆整的值
							double dTempCharge=dTempNowCharge-itFee.getLnTariff(); //厘
							sec.setdUnusedFee(0);
							if(dTempCharge <=0){
								itFee.setLnTariff(itFee.getLnTariff()-(long)dTempNowCharge);
								iChargeUnit.setLnOriCharge(0);
								
							}else{
								//再圆整一次
								iChargeUnit.setLnOriCharge(DateUtil.getIntByTailMode(dTempCharge/RatingMacro.TARIFF_PRECISION, iPricingSection.getnTailMode()));
								itFee.setLnTariff((long)(iChargeUnit.getLnOriCharge() * RatingMacro.TARIFF_PRECISION-dTempCharge));
								
							}
							iChargeUnit.setLnUnusedMondey(itFee.getLnTariff());
							break;
						}
						
					}
					//term的时候,丢弃最优一次批价多余的金钱
					if(sec.getdUnusedFee() !=0){
						ifee.setLnAcctItemTypeId(iPricingSection.getLnAcctItemId());
						ifee.setnUnitType(sec.getnUnitType());
						ifee.setLnTariff((long)sec.getdUnusedFee()); //本次批价多出的金钱
						addUnusedMoney(ifee);
					}
//				}
				if(sec.getnUnitType()==2){
					ratingMsg.getM_iRatingExtMsg().setM_lnMoney(ratingMsg.getM_iRatingExtMsg().getM_lnMoney()+iChargeUnit.getLnOriCharge());
				}
				ratingData.getiChargeUnits().add(iChargeUnit);
			}
		}
		
		for(int u=0 ;u< ratingData.getiChargeUnits().size();++u){
			ratingData.getiChargeUnits().get(u).show();
		}
		
		return 0;
	}
	
	
	
	private int getUnusedMoneyFromSession(){
//		ratingData.getiUnusedMoneys().clear();
//		if((ratingData.getnFeeType()==RatingMacro.ONLY_REAL_FEE || (ratingData.getnFeeType()==RatingMacro.REAL_REQ_FEE && ratingMsg.getOper()==RatingMacro.OPER_REAL))
//			&& !ratingData.getStrUnusedMoneyInfo().isEmpty() && !ratingData.isbIsReRating()	){
//			
//			String ltStruct[]=ratingData.getStrUnusedMoneyInfo().split(";");
//			int size=ltStruct.length;
//			int i=0;
//			for(;i<size;++i){
//				Fee iFee=new Fee();
//				if(ltStruct[i].equals("")){
//					continue;
//				}
//				String ltElement[]=ltStruct[i].split("|");
//				iFee.setLnAcctItemTypeId(Integer.parseInt(ltElement[0]));
//				iFee.setnUnitType(Integer.parseInt(ltElement[1]));
//				iFee.setLnTariff(Long.parseLong(ltElement[2]));
//				addUnusedMoney(iFee);
//			}
//		}
		return 0;
	}
	
	
	private int addUnusedMoney( Fee iFee){
		Iterator<Fee> iter=ratingData.getiUnusedMoneys().iterator();
		boolean find=false;
		while(iter.hasNext()){
			Fee fee=iter.next();
			if(fee.getLnAcctItemTypeId()==iFee.getLnAcctItemTypeId() && fee.getnUnitType()==iFee.getnUnitType()){
				fee.setLnTariff(fee.getLnTariff()+iFee.getLnTariff());
				find=true;
				break;
			}
		}
		if(!find){
			ratingData.getiUnusedMoneys().add(iFee);
		}
		return 0;
	}
	/*
	 * 回写消息
	 * 待ratingMsg实现
	 */
	public void setCurrentPlanId(int planId){
		String strValue;
		//
	}
	public void getTariffResultFromRateResult(RateData data){
		List<TariffResult> tariffResults=new ArrayList<TariffResult>();
		
		List<OfrRateData> rataDatas=data.getiOfrResults();
		
		for(OfrRateData iter:rataDatas){
			for(SectionRateData sec:iter.getiSectionResults()){
				for(TariffResult t:sec.getiTariffResults()){
					tariffResults.add(t);
//					log.debug("tariffResults >>>>>"+t);
				}
			}
		}
		ratingData.setiTariffResults(tariffResults);
	}
	
	
	public void getRateInfoFromTariffResult(){
		RateInfo iRateInfo=new RateInfo();
		Iterator<TariffResult> iter = ratingData.getiTariffResults().iterator();
	    while(  iter.hasNext() ){
	    	TariffResult result=iter.next();
	        iRateInfo.setLnPricingSectionId(result.getLnPricingSectionId());
	        iRateInfo.setLnTariffID(result.getLnTariffID());
	        iRateInfo.setLnRateUnit(result.getLnRateUnit());
	        iRateInfo.setdRateValue(result.getdRateValue());
	        iRateInfo.setnRateType(result.getnMeasureDomain());
	        iRateInfo.setnBeginTime(result.getnBeginTime());
	        if( iRateInfo.getnRateType().equals("3") )
	            iRateInfo.setnRateType("6");
	        else if( iRateInfo.getnRateType().equals("2") )
	            iRateInfo.setnRateType("3");

	        addRateInfo( iRateInfo );
	    }
	}
	
	public void addRateInfo(RateInfo info){
		List<RateInfo> iRateInfos = ratingData.getiRateInfos();
		if(!iRateInfos.contains(info)){
			iRateInfos.add(info);
		}
	    
	}
	
	
	
	public int setFeeInfo(){
		
		return 0;
	}

	public int discount(){
		List<ChargeUnit> chargeUnits=ratingData.getiChargeUnits();
		for(ChargeUnit iter:chargeUnits){
			int ret=discount(iter);
			if(ret<0){
				return ret;
			}
		}
		return 0;
	}
	
	/*
	 * 打折后费用单位为分,原始费用单位为厘,未用金钱单位为厘 
	 */
	public int discount(ChargeUnit chargeUnit){
		if(chargeUnit.getLnDiscountId()==-1){
			long lnOriCharge=chargeUnit.getLnOriCharge();
			chargeUnit.setLnCharge(lnOriCharge);  //厘
			int chgValue=DateUtil.getIntByTailMode(lnOriCharge/RatingMacro.TARIFF_PRECISION, chargeUnit.getnTailMode());  //圆整到分
			chargeUnit.setLnOriCharge(chgValue);
			chargeUnit.setLnUnusedMondey(chargeUnit.getLnOriCharge()*RatingMacro.TARIFF_PRECISION-lnOriCharge);
			return 0;
		}
		chargeUnit.setLnCharge(chargeUnit.getLnOriCharge());
		
		List<Discount> iDiscounts=new ArrayList<Discount>();
		iDiscounts=getDiscount(chargeUnit.getLnDiscountId());
		if(iDiscounts.isEmpty()){
			return 0;
		}
		
		long lnBaseMoney=0;
		lnBaseMoney=getRatableResourceValue(iDiscounts.get(0).getResourceCode(), ratingData.getiTmpRatableResourceValues());
		lnBaseMoney=lnBaseMoney * RatingMacro.TARIFF_PRECISION;
		
		long lnRealUsedMoney = chargeUnit.getLnOriCharge() - chargeUnit.getLnUnusedMondey();

	    long lnBegin = 0;
	    long lnEnd = 0;

	    chargeUnit.setLnOriCharge(lnRealUsedMoney);

	    for( Discount iter : iDiscounts ){
	       

	        long lnDisctMoney = 0;
	        lnBegin = iter.getLower() * RatingMacro.TARIFF_PRECISION;
	        lnEnd = iter.getUpper() * RatingMacro.TARIFF_PRECISION;

	        chargeUnit.setnRatableFlag(iter.getRatableFlag());
	        if( ( lnBaseMoney>= lnEnd ) || ( lnBaseMoney+ lnRealUsedMoney <=lnBegin ) )
	            continue;

	        ///[begin,end]区间内的部分需要打折
	        if( lnBaseMoney <= lnBegin ){
	        
	            if( lnBaseMoney + lnRealUsedMoney <= lnEnd ){
	                lnDisctMoney = lnBaseMoney + lnRealUsedMoney - lnBegin;
	                chargeUnit.setLnCharge(chargeUnit.getLnCharge()-(lnDisctMoney - lnDisctMoney*iter.getDiscount()/100));
	                break;
	            }else{
	                lnDisctMoney = lnEnd - lnBegin;
	                lnBaseMoney = lnEnd;
	                chargeUnit.setLnCharge(chargeUnit.getLnCharge()-(lnDisctMoney-lnDisctMoney*iter.getDiscount()/100));
	                
	                lnRealUsedMoney = lnRealUsedMoney - lnDisctMoney;
	            }
	        }else{
	            if( lnBaseMoney + lnRealUsedMoney <= lnEnd ){
	                lnDisctMoney = lnRealUsedMoney;
	                chargeUnit.setLnCharge(chargeUnit.getLnCharge()-(lnDisctMoney-lnDisctMoney*iter.getDiscount()/100));
	                break;
	            }else{
	                lnDisctMoney = lnEnd - lnBaseMoney;
	                lnBaseMoney = lnEnd;
	                chargeUnit.setLnCharge(chargeUnit.getLnCharge()-(lnDisctMoney-lnDisctMoney*iter.getDiscount()/100));
	                lnRealUsedMoney = lnRealUsedMoney - lnDisctMoney;
	            }
	        } 
	    }

	    //交换数据,将打折后结果放到m_lnOriCharge中,m_lnDisctCharge不会使用
	    long lnOriCharge = chargeUnit.getLnOriCharge();

	    //防止打折再打折的情况
	    double dDisctCharge = chargeUnit.getLnCharge() * 1.0 ;
	    dDisctCharge = dDisctCharge>0? dDisctCharge:0;
	    chargeUnit.setLnCharge(DateUtil.getIntByTailMode(dDisctCharge/RatingMacro.TARIFF_PRECISION,chargeUnit.getnTailMode()));//圆整到分
	    chargeUnit.setLnUnusedMondey((long)(chargeUnit.getLnCharge()*RatingMacro.TARIFF_PRECISION - dDisctCharge));
	    chargeUnit.setLnOriCharge(chargeUnit.getLnCharge());
	    chargeUnit.setLnCharge(lnOriCharge);
		return 0;
	}
	
	
	
	
	
	
	
	
	
	private long getRatableResourceValue(String ratableCode, Map<String,RatableResourceValue>  values){
		long value=0;
		RatableResourceInfo resourceInfo=resource.getiRatableResourceInfos().get(ratableCode);
		if(resourceInfo==null){
			return ErrorInfo.ERR_NOT_FOUND_RATABLE_CODE;
		}
		RatableResourceValue resourceValue=values.get(ratableCode);
		if(resourceValue==null){
			return -1;
		}else{
			value=resourceValue.getM_lnBalance();
		}
		
		//如果事先补款,且携带累积量情况, 我们对其累积量-1进行批价
		//必须按照次数来补款的,支持目前的短信和彩信业务
		if(ratingMsg.getM_nBillingFlag() == RatingMacro.EVENT_BACK  && value >0){
			value -=1;
		}
		return value;
	}
	
	
	
	
	
	private List<Discount> getDiscount(long discountId){
//		DbUtil db=new DbUtilImpl();
		List<Discount> ret=new ArrayList<Discount>();
		
//		discounts=db.getAllSectionDiscount();
		if(discounts != null){
			for(RulePricingSectionDisct disct : discounts){
				if(disct.getDiscount_id()==discountId){
					Discount dis=new Discount(disct);
					ret.add(dis);
				}
			}
		}
		return ret;
	}
	
	
	
	//获取业务公式
	
		private boolean getFormulaAndAdjust(){
			int msgType=ratingMsg.getM_nMsgType();

			ruleFormula=dbConfig.getFormulaMap().get(msgType);
			if(ruleFormula==null)
				return false;
			ruleFormula=ruleFormula.trim();
			if(!checkFormula(ruleFormula)){
				log.debug("格式校验错误");
				return false;
			}
			return true;
		}
		
		
		//业务公式格式校验
		private boolean checkFormula(String ruleFormula){
			int len=ruleFormula.length();
			char str[]=ruleFormula.toCharArray();
			for(char s:str){
				if (s==ParamData.MUTEX_FAV_MODE || s==ParamData.BEST_FAV_MODE || s==ParamData.OVERLAY_FAV_MODE){
					continue;
				}else if(Character.isDigit(s)){
					continue;
//				}else if(groupIds.contains(""+s)){
//					continue;
				}else{
					log.debug("业务公式包含不识别字符");
					return false;
					
				}
			}
			int i=0;
			String formula=ruleFormula;
			List<String> groupIds=dinnerConversion.getiEffGroups();
			String[] arr=formula.split(",");
			for(String ar :arr){
				if(ar.indexOf(",")==-1 && ar.indexOf("!")==-1 && ar.indexOf("&")==-1){
					if(!groupIds.contains(ar))
						return false;
					int size=ar.length();
					if(formula.length()>size){
						size++;
						formula=formula.substring(size);
					}
				}else{
					String[] arr2=ar.split("!");
					for(String ar2 :arr2){
						if(ar2.indexOf(",")==-1 && ar2.indexOf("!")==-1 && ar2.indexOf("&")==-1){
							if(!groupIds.contains(ar2)){
								return false;
							}
							int size=ar2.length();
							if(formula.length()>size){
								size++;
								formula=formula.substring(size);
							}
						}else{
							String[] arr3=ar2.split("&");
							for(String ar3 :arr3){
								if(!groupIds.contains(ar3)){
									return false;
								}
								int size=ar3.length();
								if(formula.length()>size){
									size++;
									formula=formula.substring(size);
								}
							}
						}
						
					}
				}
				
			}
			
			return true ;
		}
		
		
		
		public void getSubFormula(List<String> groups,List<String> opers){
			
			String[] arr=ruleFormula.split(",");
			for(String ar :arr){
				if(ar.indexOf(",")==-1 && ar.indexOf("!")==-1 && ar.indexOf("&")==-1){
					groups.add(ar);
					int size=ar.length();
					if(ruleFormula.length()>size){
						opers.add(ruleFormula.substring(size,size+1));
						size++;
						ruleFormula=ruleFormula.substring(size);
					}
				}else{
					String[] arr2=ar.split("!");
					for(String ar2 :arr2){
						if(ar2.indexOf(",")==-1 && ar2.indexOf("!")==-1 && ar2.indexOf("&")==-1){
							groups.add(ar2);
							int size=ar2.length();
							if(ruleFormula.length()>size){
								opers.add(ruleFormula.substring(size,size+1));
								size++;
								ruleFormula=ruleFormula.substring(size);
							}
						}else{
							String[] arr3=ar2.split("&");
							for(String ar3 :arr3){
								groups.add(ar3);
								int size=ar3.length();
								if(ruleFormula.length()>size){
									opers.add(ruleFormula.substring(size,size+1));
									size++;
									ruleFormula=ruleFormula.substring(size);
								}
							}
						}
						
					}
				}
				
			}
		}
		
	public RateData getResult() {
		return result;
	}
	
	public void setResult(RateData result) {
		this.result = result;
	}
	
	public RatingData getRatingData() {
		return ratingData;
	}
	
	public void setCurrTime(String currTime) {
		this.currTime = currTime;
	}
	public String getCurrTime() {
		return currTime;
	}
	
	
}
