package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.rating.domain.Holiday;
import com.tydic.beijing.billing.rating.domain.PricingSection;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.RuleHoliday;
import com.tydic.beijing.billing.rating.domain.RuleTariff;
import com.tydic.beijing.billing.rating.domain.Tariff;
import com.tydic.beijing.billing.rating.domain.TariffResult;
import com.tydic.beijing.billing.rating.dto.DbConfigDetail;
import com.tydic.beijing.billing.rating.dto.impl.DbUtilImpl;
import com.tydic.beijing.billing.rating.service.ApplicationContextHelper;
import com.tydic.beijing.billing.rating.service.ErrorInfo;
import com.tydic.beijing.billing.rating.service.RatingException;
import com.tydic.beijing.billing.rating.service.TariffCalc;
import com.tydic.beijing.billing.rating.util.DateUtil;

public class TariffCalcImpl implements TariffCalc{

	private Logger log=Logger.getLogger(TariffCalcImpl.class);
	private PricingSection section;
	
	private RatingMsg ratingMsg;
	
	private RatingData ratingData;
	private DbConfigDetail  dbConfig=null;
	@Autowired
	private ApplicationContextHelper applicationContextHelper;
	private long dosage;
	private long lastDosage;
	private long startValue;
	private List<TariffResult> result=new ArrayList<TariffResult>();
	private List<RuleTariff> allTariffs=new ArrayList<RuleTariff>();
	private DbUtilImpl db=new DbUtilImpl();
	private List<Tariff> totalTariffs=new ArrayList<Tariff>();
	private List<Tariff> normalTariffs=new ArrayList<Tariff>();
	private List<Tariff> holidayTariffs=new ArrayList<Tariff>();
	private List<Tariff> notHolidayTariffs=new ArrayList<Tariff>();
	private int indexTol=0;
	private int indexNor=0;
	private int indexHol=0;
	private int indexNot=0;
	private int indexTmpTol=0;
	private int indexTmpNor=0;
	private int indexTmpHol=0;
	private int indexTmpNot=0;
	public List<TariffResult> getResult() {
		return result;
	}
	
//	public TariffCalcImpl(){}
	
	public void initCalcData(PricingSection section,long dosage,long startValue,long lastDosage){
		this.section=section;
		this.dosage=dosage;
		this.lastDosage=lastDosage;
		this.startValue=startValue;
		totalTariffs.clear();
		normalTariffs.clear();
		holidayTariffs.clear();
		notHolidayTariffs.clear();
		result.clear();
	}
	
	
	public TariffCalcImpl(RatingMsg ratingMsg,RatingData ratingData){
		
		this.ratingData=ratingData;
		this.ratingMsg=ratingMsg;
		
	}
	
	
	@Override
	public int calcCharge() throws RatingException {
		if(dbConfig==null){
			dbConfig=(DbConfigDetail) applicationContextHelper.getBean("dbConfigDetail");
			allTariffs=dbConfig.getAllRuleTariff();
		}
		switch(section.getnTariffType()){
		case 0://剂量计费
			
			calcChargeByFlux();
			
			break;
		case 1://时间计费
			calcChargeByTime();
			break;
			
		default:
			throw new RatingException( ErrorInfo.ERR_NOT_DEFINE_REFOBJECT_TARIFF,"没有定义计量单位");
		}
		return 0;
	}
	
	
	private int calcChargeByFlux() throws RatingException{
		if(dosage==0){
			return 0;
		}
		
		if(section.getLnTariffId()==-1){
			return 0;
		}
		if(!getAllTariffs(section.getLnTariffId())){
			throw new RatingException(ErrorInfo.ERR_NOT_FOUND_TARIFF,"找不到费率");
		}
		String currTime=ratingData.getiCurrTime();
		boolean isHoliday=false ;
		String strCurrTime=currTime;

		Tariff curr=null;
		Tariff tmpCurr=null;
		
		curr=moveToFirstTariff();
		

		if(curr.getM_nDateType()==RatingMacro.NORMAL_TARIFF){
			isHoliday=false;
		}else{
			isHoliday=true;
		}
		long numberA=startValue;
		long numberB=startValue+dosage;
		log.debug("numberA=startValue["+numberA+"],numberB=["+numberB+"],startValue["+startValue+"],dosage["+dosage+"]");
		
		long lnStartValue=0;
		long lnEndValue=0;
		long lnEndValueSpan=-1;
		double dSpanValues = 0.0;
		int nCurCounts = 0;
		long lnRealDosage=0;
		long lnRoundDosage=dosage;
		long lnChangePoint=0;
		while(section.getLnTariffId()==curr.getM_lnTariffId()){
			if(numberA==numberB)
				break;

			curr=getCurrTariff(curr,indexNor,indexHol,indexNot,isHoliday,strCurrTime);
			if(curr.getM_nDisctValueBase()==0){
				throw new RatingException(ErrorInfo.ERR_TARIFF_CAN_NOT_ZERO,"资费打折基数不能为0");
			}
			log.debug("费率:id:"+curr.getM_lnTariffId()+"|feeRate:"+curr.getM_nFeeRate()+"|lower:"+curr.getM_strLower()+"upper:"+curr.getM_strUpper());
			TariffResult iTariffResult=new TariffResult();
	        iTariffResult.setLnPricingSectionId(section.getLnPricingSectionId());    
	        iTariffResult.setLnAcctItemTypeId(section.getLnAcctItemId());
	        iTariffResult.setLnTariffID(section.getLnTariffId());
	        iTariffResult.setnBeginTime(lnChangePoint);
	        
			lnStartValue=curr.getM_strLower();
			lnEndValue=curr.getM_strUpper();
			nCurCounts=0;
			if( lnEndValueSpan != -1 )
	            lnEndValue += lnEndValueSpan;
			//计费点A在该费率段中
			if( numberA >= lnStartValue && numberA < lnEndValue ){
				if(curr.getM_lnRateUnit()==(lnEndValue - lnStartValue )){//计费单元
					if(numberA==lnStartValue){
						dSpanValues = 1.0;
	                    nCurCounts = 1;
					}else{
						dSpanValues = 0.0;
	                    nCurCounts = 0;
					}
					if(numberB <= lnEndValue){
						numberA=numberB;
					}else{
						numberA=lnEndValue;
					}
				}else{//普通资费
					if(numberB <=lnEndValue){
						lnRealDosage=numberB-numberA;
						lnRoundDosage=lnRealDosage;
						dSpanValues=1.0*lnRoundDosage /curr.getM_lnRateUnit();
						nCurCounts=getIntByTailMode(dSpanValues,curr.getM_nTailMode());
						lnRoundDosage=nCurCounts*curr.getM_lnRateUnit();
						long lnDosageDiff=lnRoundDosage-lnRealDosage;
						long lnUnused=lnDosageDiff>0? lnDosageDiff :0;
						
						iTariffResult.setLnRatableUnusedDosage(lnUnused);
						numberA=numberB;
					}else{
						lnRealDosage=lnEndValue-numberA;
						lnRoundDosage=lnRealDosage;
						dSpanValues=1.0*lnRoundDosage /curr.getM_lnRateUnit();
						nCurCounts=getIntByTailMode(dSpanValues,curr.getM_nTailMode());
						long tmpNumberA=numberA;
						numberA=lnEndValue;
						
						if(isTariffChange()){
							tmpCurr=curr;

							indexTmpTol=indexTol;
							indexTmpNor=indexNor;
							indexTmpHol=indexHol;
							indexTmpNot=indexNot;
							
							if((tmpCurr=getNextTmpTariff(tmpCurr,indexTmpNor,indexTmpHol,indexTmpNot))==null){
								return ErrorInfo.ERR_TARIFF_NOT_CLOSED;
							}
							log.debug("下一费率:id:"+tmpCurr.getM_lnTariffId()+"|feeRate:"+tmpCurr.getM_nFeeRate()+"|lower:"+tmpCurr.getM_strLower()+"upper:"+tmpCurr.getM_strUpper());


							if(section.getnTariffType()==1){//如果按照时长计费时，需要考虑是否跨节假日，因此要重新获取资费
//								String strTmpCurrTime;
								long secondsToAdd=nCurCounts*curr.getM_lnRateUnit();
								long lnTmp=tmpNumberA+secondsToAdd;//原A到end
								String tmpDateTime=ratingData.getiCurrTime();
								String strTmpDateTime=DateUtil.addSeconds(tmpDateTime, (int)secondsToAdd, 14);

								tmpCurr=getCurrTariff(tmpCurr, indexTmpNor,indexTmpHol,indexTmpNot, isHoliday, strTmpDateTime);
								while(true){
									if(lnTmp >= tmpCurr.getM_strLower() && lnTmp <=tmpCurr.getM_strUpper()){
										break;
									}

									if((tmpCurr=getNextTariff(tmpCurr,indexTmpNor,indexTmpHol,indexTmpNot))==null){
										return ErrorInfo.ERR_TARIFF_NOT_CLOSED;
									}
								}
							}// end ==01
							
							if(curr.getM_nFeeRate() > tmpCurr.getM_nFeeRate()){
								
								if(nCurCounts >dSpanValues){
									nCurCounts--;
								}
								lnRoundDosage=nCurCounts * curr.getM_lnRateUnit();
								long tmpNumberB=numberB;
								//当前tariff少批价的部分，加到下一个A/B区间上去
	                            numberB += ( lnEndValue - tmpNumberA - lnRoundDosage );
	                            //lnEndValue - lnTmpNumberA - lnRoundDosage是当前tarriff少批价的部分，需要由iTmpCurrIter批。当iTmpCurrIter的上限在lnTmpNumberB和lnNumberB之间时，
	                            //这部分dosage的批价结果才会影响最终的费用
	                            if ( numberB > tmpCurr.getM_strUpper() && tmpNumberB <= tmpCurr.getM_strUpper() )
	                                lnEndValueSpan = lnEndValue - tmpNumberA - lnRoundDosage;
							}else{//当前tariff费率不比下一个高
								if( nCurCounts < dSpanValues ) //向下圆整和四舍五入的情况
	                                nCurCounts++;
								lnRoundDosage = nCurCounts * curr.getM_lnRateUnit() ;
								//当前tariff多批价的部分，从下一个A/B区间上减去
	                            numberB -= ( lnRoundDosage + tmpNumberA - lnEndValue );
								
							}
						}else {   // end 费率切换点
							lnRoundDosage = nCurCounts * curr.getM_lnRateUnit();
	                        long lnTmpUsedDosage = lnRoundDosage - lnRealDosage;
	                        long lnDosageDiff = lnTmpUsedDosage>0 ? lnTmpUsedDosage : 0;
	                        iTariffResult.setLnRatableUnusedDosage(lnDosageDiff);   
							
						}
					}//numberB>lnEndValue
				}//普通资费
				if( nCurCounts > 0 )
	            {
	                iTariffResult.setnCount(nCurCounts);
	                iTariffResult.setLnDosage(nCurCounts*curr.getM_lnRateUnit());
	                iTariffResult.setdFee(1.0*nCurCounts*curr.getM_nFeeRate()*curr.getM_nDisctValue()/curr.getM_nDisctValueBase());
	            }
				iTariffResult.setnUnitTypeId(curr.getM_nUnitTypeId());
				if( section.getnTariffType()==1 )
	            {

	                long seconds=nCurCounts*curr.getM_lnRateUnit();
	                strCurrTime=DateUtil.addSeconds(strCurrTime, (int)seconds, 14);
	            }
				iTariffResult.setLnRateUnit(curr.getM_lnRateUnit());
	            iTariffResult.setdRateValue(1.0*curr.getM_nFeeRate()*curr.getM_nDisctValue()/(curr.getM_nDisctValueBase()*RatingMacro.TARIFF_PRECISION));
	            
	            iTariffResult.setLnFee(getIntByTailMode(iTariffResult.getdFee()/RatingMacro.TARIFF_PRECISION, section.getnTailMode()));
	        
	            iTariffResult.setLnUnusedMoney(iTariffResult.getLnFee()*RatingMacro.TARIFF_PRECISION-iTariffResult.getdFee());
	            
	            iTariffResult.setnMeasureDomain(""+section.getStrMeasureDomain());
	            lnChangePoint = lnEndValue;
	            if( numberA == numberB )
	            {
	                int lnTmpUnusedDosage = (int)(lnRoundDosage - lnRealDosage);
	                //未使用的值 只对最后一次的圆整
	                //此段落的所有tariff对dosage批价，中间的未用dosage在tariff之间处理掉
	                iTariffResult.setLnUnusedDosage(lnTmpUnusedDosage>0? lnTmpUnusedDosage:0);
	                //.m_lnUnusedDosage = ( lnTmpUnusedDosage > 0 ? lnTmpUnusedDosage:0 );
	            }
	            
	            result.add(iTariffResult);
	            log.debug("费率计算结果:"+iTariffResult);
			}
			if(numberA < numberB && (curr=getNextTariff(curr, indexNor, indexHol, indexNot))==null){
				return ErrorInfo.ERR_TARIFF_NOT_CLOSED;
			}
		}
		
		
		return 0;
	}
	
	/**
	 * 计时计费
	 * @author sung
	 *
	 * @return
	 * @throws RatingException 
	 */
	private int calcChargeByTime() throws RatingException{
		if(dosage==0 && lastDosage==0)
			return 0;
		
		log.debug("段落:"+section.getLnPricingSectionId()+"费率:"+section.getLnTariffId());
		if(!getAllTariffs(section.getLnTariffId())){
			throw new RatingException(ErrorInfo.ERR_NOT_FOUND_TARIFF,"找不到费率["+section.getLnTariffId()+"]");
		}
		
		String strCurrTime="";
		String strSpanStartTime=ratingData.getiCurrTime();
		String endTime=ratingData.getiCurrTime();
		int curCounts=0;
		long interval=0;
		double spanValues=0;
		boolean isHoliday=false;
//		String strSpanStartTime;
		String strSpanEndTime;
		String strSpanTime="";
		int retry=0;
		int totalRetry=totalTariffs.size();
		
		long leftDosage=dosage;
		long lnLastDosage=lastDosage;
		long tmpCurrentDosage=0;
		long tariffUnusedDosage=0;
		
		boolean calcWraped=false;
		double rateValue=0;
		
		Tariff curr=null;
		Tariff tmpCurr=null; 
		
		curr=moveToFirstTariff();
		
		if(curr.getM_nDateType()==RatingMacro.NORMAL_TARIFF){
			isHoliday=false;
		}else{
			isHoliday=true;
		}
		while(section.getLnTariffId()==curr.getM_lnTariffId()){
			if(leftDosage==0){
				break;
			}
			
			TariffResult iTariffResult=new TariffResult();
			
			curr=getCurrTariff(curr,indexNor,indexHol,indexNot,isHoliday,strSpanStartTime);
			long lnSpanStartTime=Long.parseLong(strSpanStartTime.substring(8,14));
			log.debug("计时计费==lnSpanStartTime["+lnSpanStartTime+"],lower["+curr.getM_strLower()+"],upper["+curr.getM_strUpper()+"]");
			if(lnSpanStartTime < curr.getM_strLower() || lnSpanStartTime >= curr.getM_strUpper()){
//				log.debug("超出上下限范围!!");
				if(retry <= totalRetry){
					retry++;

					if((curr=getNextTariff(curr,indexNor,indexHol,indexNor))==null){
						if(!calcWraped){
							calcWraped=true;
							curr=moveToFirstTariff();
							retry=0;
						}else{
							throw new RatingException( ErrorInfo.ERR_TARIFF_NOT_CLOSED_TIME,"费率配置错误,上下限没有闭合");
						}
					}
					continue;
				}else{
					throw new RatingException( ErrorInfo.ERR_TARIFF_NOT_CLOSED_TIME,"费率配置错误,上下限没有闭合");
				}
			}
			if(curr.getM_nDisctValueBase()==0){
				throw new RatingException( ErrorInfo.ERR_TARIFF_CAN_NOT_ZERO,"打折基数不能为0");
			}
			calcWraped=false;
			strSpanEndTime=strSpanStartTime.substring(0,8);
//			log.debug("strSpanStarttime--->"+strSpanStartTime+"|strSpanEndTime--->"+strSpanEndTime);
			strSpanEndTime+=curr.getM_strUpper();
//			log.debug("strSpanStarttime--->"+strSpanStartTime+"|strSpanEndTime--->"+strSpanEndTime);
			if(curr.getM_nDateType()==RatingMacro.HOLIDAY_TARIFF){
				if(strSpanEndTime.substring(8,14).equals("999999")){
					strSpanEndTime=strSpanEndTime.substring(0,8)+"235959";
				}
			}
			endTime=DateUtil.genDateTime(strSpanEndTime);
			interval=DateUtil.getIntervalSeconds(strSpanStartTime, strSpanEndTime);
			log.debug("startTime["+strSpanStartTime+"],endTime["+strSpanEndTime+"],interval["+interval+"]");
			
			if(interval<0){
				throw new RatingException( ErrorInfo.ERR_INVALID_TARIFF_DATA,"费率配置错误");
			}
			if(strSpanTime.isEmpty() || strSpanTime=="NO"){
				if(ratingMsg.getBaseMsg().getM_strValidityTime().isEmpty()){  //R309
					strSpanTime=strSpanEndTime;
					strSpanTime=new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.toDate(strSpanTime));
					
				}else{
					if(Long.parseLong(ratingMsg.getBaseMsg().getM_strValidityTime())<interval){
						strSpanTime="NO";
					}else{
						strSpanTime=strSpanEndTime;
						strSpanTime=new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.toDate(strSpanTime));
					}
				}
				ratingData.setStrSpanTime(strSpanTime);
			}
			
			if(!section.getStrMeasureDomain().equals("1")){//非时长
				rateValue=1.0*curr.getM_nFeeRate()*curr.getM_nDisctValue()/(curr.getM_nDisctValueBase()*RatingMacro.TARIFF_PRECISION);
				tmpCurrentDosage=leftDosage;
				spanValues=1.0*leftDosage/curr.getM_lnRateUnit();
				curCounts=getIntByTailMode(spanValues,curr.getM_nTailMode());
				long lnTmpUnusedDosage=0;
				long lnTmpTmp=-1;
				if(isBusy2Idle()&& lnLastDosage !=0){
					tmpCurr=curr;
					indexTmpTol=indexTol;
					indexTmpNor=indexNor;
					indexTmpHol=indexHol;
					indexTmpNot=indexNot;
					if((tmpCurr=getNextTmpTariff(tmpCurr, indexTmpNor, indexTmpHol, indexTmpNot))==null){

						tmpCurr=moveToFirstTmpTariff(tmpCurr);
					}
					
					String strTmpSpanStartTime = strSpanEndTime;
					if(strTmpSpanStartTime.substring(8,14).equals("240000")){
						strTmpSpanStartTime=new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.toDate(strTmpSpanStartTime));
					}else{
						strTmpSpanStartTime=DateUtil.addSeconds(strTmpSpanStartTime, 2,14);
					}

					tmpCurr=getCurrTariff(tmpCurr,indexTmpNor,indexTmpHol,indexTmpNot,isHoliday,strTmpSpanStartTime);
					
					int ret;

					if((tmpCurr=getExactCurrTariff(tmpCurr,indexTmpNor,indexTmpHol,indexTmpNot,strTmpSpanStartTime))==null){	
						return ErrorInfo.ERR_TARIFF_NOT_CLOSED_TIME;
					}
					if(tmpCurr.getM_nDisctValueBase()==0){
						return ErrorInfo.ERR_TARIFF_CAN_NOT_ZERO;
					}
					if(curr.getM_nFeeRate() >tmpCurr.getM_nFeeRate()){
						if(curCounts >spanValues){
							curCounts--;
						}
						leftDosage=curCounts*curr.getM_lnRateUnit();
						lnTmpUnusedDosage=leftDosage-tmpCurrentDosage;
						lnLastDosage=lnLastDosage+(-lnTmpUnusedDosage>0?(-lnTmpUnusedDosage):0);
						
					}else{
						if(curCounts <spanValues){
							curCounts++;
						}
						leftDosage=curCounts*curr.getM_lnRateUnit();
						lnTmpUnusedDosage=leftDosage-tmpCurrentDosage;
						lnTmpTmp=lnTmpUnusedDosage-lastDosage;
						lnLastDosage=(lnLastDosage-lnTmpUnusedDosage)>0?lnLastDosage-lnTmpUnusedDosage:0;
						
					}
				}else{
					log.debug("isBusy2Idle()&&lnLastDosage!=0  条件不符");
					leftDosage=curCounts * curr.getM_lnRateUnit();
					lnTmpUnusedDosage=leftDosage-tmpCurrentDosage;
				}
				if(lnLastDosage==0){
					iTariffResult.setLnUnusedDosage(lnTmpUnusedDosage>0?lnTmpUnusedDosage:0);
					if(lnTmpTmp >=0 ){
						iTariffResult.setLnUnusedDosage(lnTmpTmp);
					}
				}
				if(isBusy2Idle()){
					if(lnLastDosage==0){
						if(lnTmpTmp >=0){
							tariffUnusedDosage=(lnTmpTmp >0 ? lnTmpTmp:0);
							iTariffResult.setLnRatableUnusedDosage(iTariffResult.getLnRatableUnusedDosage()+tariffUnusedDosage);
						}else{
							tariffUnusedDosage=(lnTmpUnusedDosage >0 ?lnTmpUnusedDosage :0);
							iTariffResult.setLnRatableUnusedDosage(iTariffResult.getLnRatableUnusedDosage()+tariffUnusedDosage);
						}
					}
				}else{
					tariffUnusedDosage = lnTmpUnusedDosage >0 ?lnTmpUnusedDosage :0;
					iTariffResult.setLnRatableUnusedDosage(iTariffResult.getLnRatableUnusedDosage()+tariffUnusedDosage);
				}
				iTariffResult.setLnPricingSectionId(section.getLnPricingSectionId());
				
//				log.debug("iTariffResult.lnPricingSectionId>>>>>"+iTariffResult.getLnPricingSectionId());
				iTariffResult.setLnAcctItemTypeId(section.getLnAcctItemId());
				iTariffResult.setLnTariffID(section.getLnTariffId());
				iTariffResult.setnCount(curCounts);
				iTariffResult.setLnDosage(curCounts*curr.getM_lnRateUnit());
				iTariffResult.setLnRateUnit(curr.getM_lnRateUnit());
				iTariffResult.setdRateValue(rateValue);
				iTariffResult.setdFee(1.0*curr.getM_nFeeRate()*curCounts*curr.getM_nDisctValue()/curr.getM_nDisctValueBase());
				iTariffResult.setLnFee(getIntByTailMode(iTariffResult.getdFee()/RatingMacro.TARIFF_PRECISION, curr.getM_nTailMode()));
				iTariffResult.setLnUnusedMoney(iTariffResult.getLnFee()*RatingMacro.TARIFF_PRECISION-(long)iTariffResult.getdFee());
				iTariffResult.setnBeginTime(0);
				iTariffResult.setnMeasureDomain(""+section.getStrMeasureDomain());
				iTariffResult.setnUnitTypeId(curr.getM_nUnitTypeId());
				result.add(iTariffResult);
				//-------------------begin处理B308 ,费率切换点后的费用(非时长)------------------------------------
				if(lnLastDosage !=0){
					if((curr=getNextTariff(curr,indexNor,indexHol,indexNot))==null){

						curr=moveToFirstTariff();

					}
					strSpanStartTime = strSpanEndTime;
					if (strSpanEndTime.substring(8, 14).equals("240000")) {
						strSpanStartTime = new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.toDate(strSpanStartTime));

					} else {
						strSpanStartTime = DateUtil.addSeconds(strSpanStartTime, 2, 14);
					}
					while (curr.getM_lnTariffId() == section.getLnTariffId()) {
						if (lnLastDosage <= 0) {
							break;
						}
						TariffResult iLastTariffResult = new TariffResult();
//						curr = getCurrTariff(curr, nor, hol, not, isHoliday,strSpanStartTime);
						curr=getCurrTariff(curr,indexNor,indexHol,indexNot,isHoliday,strSpanStartTime);
						
						long lnSpanStartTime2 = Long.parseLong(strSpanStartTime.substring(8));
						if (lnSpanStartTime2 < curr.getM_strLower() || lnSpanStartTime2 >= curr.getM_strUpper()) {
//							if ((curr = getNextTariff(curr, nor, hol, not)) == null) {
							if((curr=getNextTariff(curr,indexNor,indexHol,indexNor))==null){

								curr=moveToFirstTariff();

							}

						}
						if (curr.getM_nDisctValueBase() == 0) {
							throw new RatingException( ErrorInfo.ERR_TARIFF_CAN_NOT_ZERO,"打折基数不能为0");
						}
						strSpanEndTime = strSpanStartTime.substring(0, 8);
						strSpanEndTime += curr.getM_strUpper();
						if (curr.getM_nDateType() == RatingMacro.HOLIDAY_TARIFF) {
							if (strSpanEndTime.substring(8, 14).equals("999999")) {
								strSpanEndTime = strSpanEndTime.substring(0, 8)	+ "235959";
							}
						}
						interval = DateUtil.getIntervalSeconds(strSpanStartTime, strSpanEndTime);
						if (strSpanTime.isEmpty() || strSpanTime == "NO") {
							if (ratingMsg.getBaseMsg().getM_strValidityTime().isEmpty()) {// R309
								strSpanTime = strSpanEndTime;
								strSpanTime = new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.toDate(strSpanTime));

							} else {
								if (Long.parseLong(ratingMsg.getBaseMsg().getM_strValidityTime()) < interval) {
									strSpanTime = "NO";
								} else {

									strSpanTime = strSpanEndTime;
									strSpanTime = new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.toDate(strSpanTime));
								}
							}
							ratingData.setStrSpanTime(strSpanTime);
						}
						if (!section.getStrMeasureDomain() .equals("1")) { // 不按照时长计费
							rateValue = 1.0	* curr.getM_nFeeRate()* curr.getM_nDisctValue()/ (curr.getM_nDisctValueBase() * RatingMacro.TARIFF_PRECISION);
							tmpCurrentDosage = lnLastDosage;
							spanValues = 1.0 * lnLastDosage	/ curr.getM_lnRateUnit();
							curCounts = getIntByTailMode(spanValues,curr.getM_nTailMode());
							lnLastDosage = curCounts * curr.getM_lnRateUnit();

							iLastTariffResult.setLnDosage(lnLastDosage);
							long lnTmpUnusedDosage2 = lnLastDosage- tmpCurrentDosage;
							iLastTariffResult.setLnUnusedDosage(lnTmpUnusedDosage2 > 0 ? lnTmpUnusedDosage2: 0);
							iLastTariffResult.setLnRatableUnusedDosage(iLastTariffResult.getLnRatableUnusedDosage()+ iLastTariffResult.getLnUnusedDosage());

							iLastTariffResult.setLnLastDosage(lnLastDosage);
							iLastTariffResult.setLnUnusedLastDosage(iLastTariffResult.getLnUnusedDosage());

							iLastTariffResult.setLnPricingSectionId(section.getLnPricingSectionId());
							iLastTariffResult.setLnAcctItemTypeId(section.getLnAcctItemId());
							iLastTariffResult.setLnTariffID(section.getLnTariffId());
							iLastTariffResult.setnUnitTypeId(curr.getM_nUnitTypeId());
							iLastTariffResult.setLnRateUnit(curr.getM_lnRateUnit());
							iLastTariffResult.setdRateValue(rateValue);
							iLastTariffResult.setnBeginTime(leftDosage);
							double dfee = 1.0 * curr.getM_nFeeRate()* curCounts * curr.getM_nDisctValue()/ curr.getM_nDisctValueBase();
							iLastTariffResult.setdFee(dfee);
							int nfee = getIntByTailMode(iLastTariffResult.getdFee()/ RatingMacro.TARIFF_PRECISION,curr.getM_nTailMode());
							iLastTariffResult.setLnFee(nfee);
							long money = iLastTariffResult.getLnFee()* RatingMacro.TARIFF_PRECISION	- (long) iLastTariffResult.getdFee();
							iLastTariffResult.setLnUnusedMoney(money);
							iLastTariffResult.setnMeasureDomain(""+section.getStrMeasureDomain());
							result.add(iLastTariffResult);
							break;
						}
					}
				}
				//-----------处理B308,费率切换点后的费用(非时长)----end-------
				return 0;
			}//end ----不按时长资费 
			
			//----------按时长计费--------begin---------
			iTariffResult.setnBeginTime(dosage-leftDosage);
			log.debug("按时长计费dosage-leftDosage=="+dosage+"-"+leftDosage+"="+(dosage-leftDosage)+",interval===="+interval);
			if(leftDosage > interval){
				curCounts=(int)interval /curr.getM_lnRateUnit();
				if( interval%curr.getM_lnRateUnit() !=0){
					curCounts++;
				}
				if(isBusy2Idle()){
					boolean bWraped=false;
					tmpCurr=curr;
					
					indexTmpNor=indexNor;
					indexTmpHol=indexHol;
					indexTmpNot=indexNot;					
					if((tmpCurr=getNextTmpTariff(tmpCurr,indexTmpNor,indexTmpHol,indexTmpNot))==null){

						tmpCurr=moveToFirstTmpTariff(tmpCurr);
						bWraped=true;
							

					}
					String strTmpSpanStartTime = strSpanEndTime;
					int nRetTmp=0;
					if(strTmpSpanStartTime.substring(8,14).equals("240000")){
						strTmpSpanStartTime=new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.toDate(strTmpSpanStartTime));
					}else{
						strTmpSpanStartTime=DateUtil.addSeconds(strTmpSpanStartTime, 2, 14);
					}
					tmpCurr=getCurrTariff(tmpCurr,indexTmpNor,indexTmpHol,indexTmpNot,isHoliday,strTmpSpanStartTime);
					
					tmpCurr=getExactCurrTariff(tmpCurr,indexTmpNor,indexTmpHol,indexTmpNot,strTmpSpanStartTime);
							
					if(null==tmpCurr){
						if(bWraped){
							return nRetTmp;
						}else{
							bWraped=true;

							tmpCurr=moveToFirstTmpTariff(tmpCurr);
							tmpCurr=getExactCurrTariff(tmpCurr,indexTmpNor,indexTmpHol,indexTmpNot,strTmpSpanStartTime);
							
							if(null==tmpCurr){
									return nRetTmp;
							}
						}
					}
					if(tmpCurr.getM_nFeeRate() <curr.getM_nFeeRate()){
						int nTmpCurCounts = curCounts;
						if(interval % curr.getM_lnRateUnit() !=0){
							curCounts--;
						}
						leftDosage-=curCounts*curr.getM_lnRateUnit(); //5750
						strSpanStartTime=DateUtil.addSeconds(strTmpSpanStartTime, nTmpCurCounts*curr.getM_lnRateUnit(), 14);
					}else{
							leftDosage-=curCounts*curr.getM_lnRateUnit();
							strSpanStartTime=DateUtil.addSeconds(strTmpSpanStartTime, curCounts*curr.getM_lnRateUnit(), 14);
							
					}
					
				}else{
						leftDosage-=curCounts*curr.getM_lnRateUnit();
						strSpanStartTime=DateUtil.addSeconds(strSpanStartTime, curCounts*curr.getM_lnRateUnit(), 14);
				}
					
			}else{
					tmpCurrentDosage = leftDosage;
		            spanValues = 1.0*leftDosage/curr.getM_lnRateUnit();
		            curCounts = getIntByTailMode(spanValues, curr.getM_nTailMode());
		            leftDosage = curCounts * curr.getM_lnRateUnit();
		            long lnTmpUnusedDosage = leftDosage - tmpCurrentDosage;
//		            log.debug("tmpCurrentDosage="+tmpCurrentDosage+",spanValues="+spanValues+",curCounts="+curCounts+
//		            		",leftDosage="+leftDosage+
//		            		",lnTmpUnusedDosage="+lnTmpUnusedDosage+",tariffresult.ratableUnusedDosage="+
//		            		iTariffResult.getLnRatableUnusedDosage()+lnTmpUnusedDosage);
		            
		            iTariffResult.setLnUnusedDosage(lnTmpUnusedDosage>0 ?lnTmpUnusedDosage:0);
		            iTariffResult.setLnRatableUnusedDosage(iTariffResult.getLnRatableUnusedDosage()+iTariffResult.getLnUnusedDosage());
		            leftDosage = 0;
		            
			}
			iTariffResult.setLnPricingSectionId(section.getLnPricingSectionId());
			
			iTariffResult.setnCount(curCounts);
	        iTariffResult.setLnDosage(curCounts*curr.getM_lnRateUnit());
	        iTariffResult.setLnRateUnit(curr.getM_lnRateUnit());
	        double dfee=1.0 * curr.getM_nFeeRate()*curCounts*curr.getM_nDisctValue()/curr.getM_nDisctValueBase();
	        iTariffResult.setdFee(dfee);
	        int nfee=getIntByTailMode(iTariffResult.getdFee()/RatingMacro.TARIFF_PRECISION, curr.getM_nTailMode());
	        iTariffResult.setLnFee(nfee); 
	        long money=iTariffResult.getLnFee()*RatingMacro.TARIFF_PRECISION-(long)iTariffResult.getdFee();
	        iTariffResult.setLnUnusedMoney(money);
	        dfee=1.0 * curr.getM_nFeeRate()*curr.getM_nDisctValue()/(curr.getM_nDisctValueBase()*RatingMacro.TARIFF_PRECISION);
	        iTariffResult.setdRateValue(dfee); 
	        		
	        iTariffResult.setnUnitTypeId(curr.getM_nUnitTypeId());
	        iTariffResult.setLnTariffID(section.getLnTariffId());
	        iTariffResult.setnMeasureDomain(""+section.getStrMeasureDomain());
	        result.add(iTariffResult);
	        log.debug("费率计算结果:"+iTariffResult);
	        //------------------------end 按时长资费---------------------------------------------
	        if((curr=getNextTariff(curr,indexNor,indexHol,indexNot))==null){

	        	curr=moveToFirstTariff();

	        }
		}
		
		return 0;
	}

	public ListIterator<Tariff> moveToFirstTotalTariff(ListIterator<Tariff> tol){
		tol=totalTariffs.listIterator();
		tol.next();
		return tol;
	}
	public ListIterator<Tariff> moveToFirstNormalTariff(ListIterator<Tariff> tol,ListIterator<Tariff> nor){
		nor=normalTariffs.listIterator();
		if(totalTariffs.get(tol.previousIndex()).getM_nDateType()==RatingMacro.NORMAL_TARIFF){
			nor.next();
		}
		return nor;
	}
	
	public ListIterator<Tariff> moveToFirstHolidayTariff(ListIterator<Tariff> tol,ListIterator<Tariff> holiday){
		if(totalTariffs.get(tol.previousIndex()).getM_nDateType()!=RatingMacro.NORMAL_TARIFF){
			holiday.next();
		}
		return holiday;
	}
	
	private Tariff moveToFirstTmpTariff(Tariff curr){
		curr=totalTariffs.get(0);
		indexTmpNor=0;
		indexTmpHol=0;
		indexTmpNot=0;
		return curr;
	}
	
//	public int getExactCurrTariff(Tariff curr,ListIterator<Tariff> nor,ListIterator<Tariff> hol,ListIterator<Tariff> not,String time){
//		int nRetry = 0;
//	    int nTotalRetry = totalTariffs.size();
//	    boolean bCalcWraped = false;
//	    long lnSpanStartTime=Long.parseLong(time.substring(8));
//	    log.debug("lnSpanStartTime==="+lnSpanStartTime+"/"+Thread.currentThread().getStackTrace()[2].getLineNumber());
//	    while(lnSpanStartTime < curr.getM_strLower() || lnSpanStartTime >= curr.getM_strUpper()){
//	    	if(nRetry <= nTotalRetry){
//	    		nRetry++;
////	    		log.debug("before====curr.tariffId["+curr.getM_lnTariffId()+"],nor.preindex["+nor.previousIndex()+"],"
////    					+ "hol.preindex["+hol.previousIndex()+"],not.preindex["+not.previousIndex()+"]");
//	    		if((curr=getNextTariff(curr, nor, hol, not))==null){
////	    			log.debug("after====curr.tariffId["+curr.getM_lnTariffId()+"],nor.preindex["+nor.previousIndex()+"],"
////	    					+ "hol.preindex["+hol.previousIndex()+"],not.preindex["+not.previousIndex()+"]");
//	    			if(!bCalcWraped){
//	    				bCalcWraped=true;
//	    				curr=totalTariffs.get(0);
//	    				if(curr.getM_nDateType()==RatingMacro.NORMAL_TARIFF){
//	    					nor=normalTariffs.listIterator();
//	    					nor.next();
//	    				}else{
//	    					hol=holidayTariffs.listIterator();
//	    					hol.next();
//	    					not=notHolidayTariffs.listIterator();
//	    					not.next();
//	    				}
//	    				
//	    			}else{
//	    				return ErrorInfo.ERR_TARIFF_NOT_CLOSED_TIME;
//	    			}
//	    		}else{
//	    			if(curr.getM_nDateType()==RatingMacro.NORMAL_TARIFF){
//	    				nor.next();
//	    			}else if(curr.getM_nDateType()==RatingMacro.HOLIDAY_TARIFF){
//	    				hol.next();
//	    			}else if(curr.getM_nDateType()==RatingMacro.NOTHOLIDAY_TARIFF){
//	    				not.next();
//	    			}
//	    		}
//	    		continue;
//	    	}else{
//	    		return ErrorInfo.ERR_TARIFF_NOT_CLOSED_TIME;
//	    	}
//	    }
//	    if(curr.getM_nDisctValueBase()==0){
//	    	return ErrorInfo.ERR_TARIFF_CAN_NOT_ZERO;
//	    }
//	    return 0;
//	}
	
	public Tariff getExactCurrTariff(Tariff curr,int nor,int hol,int not,String time){
		int nRetry = 0;
	    int nTotalRetry = totalTariffs.size();
	    boolean bCalcWraped = false;
	    long lnSpanStartTime=Long.parseLong(time.substring(8));
	    log.debug("lnSpanStartTime==="+lnSpanStartTime+"/"+Thread.currentThread().getStackTrace()[2].getLineNumber());
	    while(lnSpanStartTime < curr.getM_strLower() || lnSpanStartTime >= curr.getM_strUpper()){
	    	if(nRetry <= nTotalRetry){
	    		nRetry++;
	    		
	    		if((curr=getNextTmpTariff(curr, nor, hol, not))==null){
	    			
	    			if(!bCalcWraped){
	    				bCalcWraped=true;
//	    				curr=totalTariffs.get(0);
//	    				if(curr.getM_nDateType()==RatingMacro.NORMAL_TARIFF){
//	    					nor=normalTariffs.listIterator();
//	    					nor.next();
//	    				}else{
//	    					hol=holidayTariffs.listIterator();
//	    					hol.next();
//	    					not=notHolidayTariffs.listIterator();
//	    					not.next();
//	    				}
	    				curr=moveToFirstTmpTariff(curr);
	    				
	    			}else{
//	    				return ErrorInfo.ERR_TARIFF_NOT_CLOSED_TIME;
	    				return null;
	    			}
//	    		}else{
//	    			if(curr.getM_nDateType()==RatingMacro.NORMAL_TARIFF){
//	    				nor.next();
//	    			}else if(curr.getM_nDateType()==RatingMacro.HOLIDAY_TARIFF){
//	    				hol.next();
//	    			}else if(curr.getM_nDateType()==RatingMacro.NOTHOLIDAY_TARIFF){
//	    				not.next();
//	    			}
//	    		}
	    		}
	    		continue;
	    	}else{
//	    		return ErrorInfo.ERR_TARIFF_NOT_CLOSED_TIME;
	    		return null;
	    	}
	    }
	    if(curr.getM_nDisctValueBase()==0){
//	    	return ErrorInfo.ERR_TARIFF_CAN_NOT_ZERO;
	    	return null;
	    }
	    return curr;
	}
	
	
	private Tariff moveToFirstTariff(){
		Tariff curr=totalTariffs.get(0);
		indexTol=0;
		indexNor=0;
		indexHol=0;
		indexNot=0;
		return curr;
	}
	/*
	 * 忙闲时切换开关   待实现
	 */
	public boolean isBusy2Idle(){
		return false;
	}
	
//	private Tariff getNextTmpTariff(Tariff curr,ListIterator<Tariff> normal,ListIterator<Tariff> holiday,ListIterator<Tariff> notHoliday){
//		if(curr.equals(normalTariffs.get(normal.previousIndex()))){
//			if(normal.nextIndex()>=normalTariffs.size()){
//				return null;
//			}
//			Tariff t=normalTariffs.get(normal.nextIndex());
//			if(null==t){
//				return null;
//			}
//			curr=t;
//		}else if(curr.equals(holidayTariffs.get(holiday.previousIndex()))){
//			if(holiday.nextIndex()>=holidayTariffs.size()){
//				return null;
//			}
//			Tariff t=holidayTariffs.get(holiday.nextIndex());
//			if(null==t){
//				return null;
//			}
//			curr=t;
//		}else if(curr.equals(notHolidayTariffs.get(notHoliday.previousIndex()))){
//			if(notHoliday.nextIndex()>=notHolidayTariffs.size()){
//				return null;
//			}
//			Tariff t=notHolidayTariffs.get(notHoliday.nextIndex());
//			if(null==t){
//				return null;
//			}
//			curr=t;
//		}
//		return curr;
//	}
	
	
//	private Tariff getCurrTariff(Tariff curr,ListIterator<Tariff> normal,ListIterator<Tariff> holiday, ListIterator<Tariff> notHoliday,boolean isHoliday,String time){
//		if(isHoliday){
//			if(isHoliday(curr.getM_nHolidayId(),time)){
//				
//				curr=holidayTariffs.get(holiday.previousIndex());
//			}else{
//				curr=notHolidayTariffs.get(notHoliday.previousIndex());
//			}
//		}else{
//
//			curr=normalTariffs.get(normal.previousIndex());
//		}
//		return curr;
//	}
	
	private Tariff getCurrTariff(Tariff curr,int normal,int holiday,int notHoliday,boolean isHoliday,String time){
		if(isHoliday){
			if(isHoliday(curr.getM_nHolidayId(),time)){
				curr=holidayTariffs.get(holiday);
			}else{
				curr=notHolidayTariffs.get(notHoliday);
			}
		}else{
			curr=normalTariffs.get(normal);
		}
		return curr;
	}
	
	
//	private Tariff getNextTariff(Tariff curr,ListIterator<Tariff> normal,ListIterator<Tariff> holiday,ListIterator<Tariff> notHoliday){
//		Tariff t=null;
//		if(curr.equals(normalTariffs.get(normal.previousIndex()))){
//			if(normal.nextIndex()!=normalTariffs.size()){
//				t=normal.next();
//				if(null==t){
//					return null;
//				}
//				curr=t;
//			}
//		}else if(curr.equals(holidayTariffs.get(holiday.previousIndex()))){
//			t=holiday.next();
//			if(null==t){
//				return null;
//			}
//			curr=t;
//		}else if(curr.equals(notHolidayTariffs.get(notHoliday.previousIndex()))){
//			t=notHoliday.next();
//			if(null==t){
//				return null;
//			}
//			curr=t;
//		}
////		log.debug("getNextTariff()........curr.tariffId["+curr.getM_lnTariffId()+"],normal.preindex["+normal.previousIndex()+"],"
////				+ "holiday.preindex["+holiday.previousIndex()+"],notholiday.preindex["+notHoliday.previousIndex()+"]");
//		return curr;
//	}
	
	private Tariff getNextTariff(Tariff curr,int normal,int holiday,int notHoliday){
		if(curr.equals(normalTariffs.get(normal))){
			indexNor++;
			if(indexNor==normalTariffs.size()){
				return null;
			}
			curr=normalTariffs.get(indexNor);
		}else if(curr.equals(holidayTariffs.get(holiday))){
			indexHol++;
			if(indexHol==holidayTariffs.size()){
				return null;
			}
			curr=holidayTariffs.get(indexHol);
		}else if(curr.equals(notHolidayTariffs.get(notHoliday))){
			indexNot++;
			if(indexNot==notHolidayTariffs.size()){
				return null;
			}
			curr=notHolidayTariffs.get(indexNor);
		}
		return curr;
	}
	
	private Tariff getNextTmpTariff(Tariff curr,int normal,int holiday,int notHoliday){
		if(curr.equals(normalTariffs.get(normal))){
			indexTmpNor++;
			if(indexTmpNor==normalTariffs.size()){
				return null;
			}
			curr=normalTariffs.get(indexTmpNor);
		}else if(curr.equals(holidayTariffs.get(holiday))){
			indexTmpHol++;
			if(indexTmpHol==holidayTariffs.size()){
				return null;
			}
			curr=holidayTariffs.get(indexTmpHol);
		}else if(curr.equals(notHolidayTariffs.get(notHoliday))){
			indexTmpNot++;
			if(indexTmpNot==notHolidayTariffs.size()){
				return null;
			}
			curr=notHolidayTariffs.get(indexTmpNot);
		}
		return curr;
	}
	
	
	private boolean isTariffChange(){
		//从RBConf 获取费率切换开关
		return true;
	}
	
	private int getIntByTailMode(double value,int mode){
		
		if(mode==RatingMacro.TARIFF_TAILMODE_UP){//向上
			return (int)Math.ceil(value);
		}else if(mode==RatingMacro.TARIFF_TAILMODE_DOWN){//向下
			return(int)Math.floor(value);
		}else if(mode==RatingMacro.TARIFF_TAILMODE_ROUND){//四舍五入
			return (int)Math.round(value);
		}else
			return (int)value;
	}
	
	
	private boolean isHoliday(int holidayId,String time){
		if (holidayId==-1){
			return false;
		}
		List<Holiday> holidays=getHolidays(holidayId);
		for(Holiday h:holidays){
			
			if(h.getHolidayType()=="1"){//日期
				if(Integer.parseInt(time.substring(0,8))>=Integer.parseInt(h.getBeginTime().substring(0,8)) &&
						Integer.parseInt(time.substring(0,8))<=Integer.parseInt(h.getEndTime().substring(0,8))	){
					return true;
				}
				
			}else if(h.getHolidayType()=="2"){//周
				int day=DateUtil.getWeekDay(time);
				if(day>=Integer.parseInt(h.getBeginTime()) &&  day<= Integer.parseInt(h.getEndTime())){
					return true;
				}
			}else if(h.getHolidayType()=="3"){//月日
				if(Integer.parseInt(time.substring(4,8))>=Integer.parseInt(h.getBeginTime().substring(4,8)) &&
						Integer.parseInt(time.substring(4,8))<=Integer.parseInt(h.getEndTime().substring(4,8))	){
					return true;
				}
			}else if(h.getHolidayType()=="4"){//时间 
				if(Integer.parseInt(time.substring(8,14))>=Integer.parseInt(h.getBeginTime().substring(8,14)) &&
						Integer.parseInt(time.substring(8,14))<=Integer.parseInt(h.getEndTime().substring(8,14))	){
					return true;
				}
			}
		}
		return false;
	}
	
	private List<Holiday> getHolidays(int holidayId){
		List<RuleHoliday> ruleHoliday=db.getAllHolidays();
		List<Holiday> holidays=new ArrayList<Holiday>();
		for(RuleHoliday h:ruleHoliday){
			if(holidayId==h.getHoliday_id()){
				Holiday holiday=new Holiday(h);
				holidays.add(holiday);
			}
		}
		return holidays;
	}
	
	
	
	
	private boolean getAllTariffs(int tariffId){
		if(!getTotalTariffs(tariffId)){
			return false;
		}
		if(totalTariffs.get(0).getM_nDateType()==RatingMacro.NORMAL_TARIFF){
			getNormalTariffs(tariffId);
			
		}else{                               
			getHolidayTariffs(tariffId);
			getNotHolidayTariffs(tariffId);
		}
		return true;
	}
	
	
	
	private boolean getNotHolidayTariffs(int tariffId){
		if(allTariffs.size()==0)
			allTariffs=db.getAllTariff();
		boolean find=false;
		for(RuleTariff t:allTariffs){
			if(tariffId==t.getTariff_id() && t.getDate_type()==RatingMacro.NOTHOLIDAY_TARIFF){
				find=true;
				Tariff tariff=new Tariff(t);
				notHolidayTariffs.add(tariff);
			}
		}
		if(!find){
			return false;
		}
		return true;
	}
	
	
	private boolean getHolidayTariffs(int tariffId){
		if(allTariffs.size()==0)
			allTariffs=db.getAllTariff();
		boolean find=false;
		for(RuleTariff t:allTariffs){
			if(tariffId==t.getTariff_id() && t.getDate_type()==RatingMacro.HOLIDAY_TARIFF){
				find=true;
				Tariff tariff=new Tariff(t);
				holidayTariffs.add(tariff);
			}
		}
		if(!find){
			return false;
		}
		return true;
	}
	
	
	private boolean getNormalTariffs(int tariffId){
		if(allTariffs.size()==0)
			allTariffs=db.getAllTariff();
		boolean find=false;
		for(RuleTariff t:allTariffs){
			if(tariffId==t.getTariff_id() && t.getDate_type()==RatingMacro.NORMAL_TARIFF){
				find=true;
				Tariff tariff=new Tariff(t);
				normalTariffs.add(tariff);
			}
		}
		if(!find){
			return false;
		}
		return true;
		
	}
	
	
	private boolean getTotalTariffs(int tariffId){
		if(allTariffs.size()==0)
			allTariffs=db.getAllTariff();
		boolean find=false;
		for(RuleTariff t:allTariffs){
			if(tariffId==t.getTariff_id()){
				find=true;
				Tariff tariff=new Tariff(t);
				totalTariffs.add(tariff);
			}
		}
		if(!find){
			return false;
		}
		return true;
	}
	
	
	
	
}
