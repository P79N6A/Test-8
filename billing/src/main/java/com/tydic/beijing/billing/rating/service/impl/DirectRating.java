/**
 * 
 */
package com.tydic.beijing.billing.rating.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.rating.domain.BalanceContent;
import com.tydic.beijing.billing.rating.domain.BaseMsg;
import com.tydic.beijing.billing.rating.domain.ChargeUnit;
import com.tydic.beijing.billing.rating.domain.CommandMsg;
import com.tydic.beijing.billing.rating.domain.ExtMsg;
import com.tydic.beijing.billing.rating.domain.Fee;
import com.tydic.beijing.billing.rating.domain.ParamData;
import com.tydic.beijing.billing.rating.domain.RatableResourceValue;
import com.tydic.beijing.billing.rating.domain.RateData;
import com.tydic.beijing.billing.rating.domain.RateMeasure;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingExtMsg;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.ServInfo;
import com.tydic.beijing.billing.rating.domain.StartValue;
import com.tydic.beijing.billing.rating.domain.UserMsg;
import com.tydic.beijing.billing.rating.domain.VarMsg;
import com.tydic.beijing.billing.rating.service.DinnerConversion;
import com.tydic.beijing.billing.rating.service.RatableResourceExtraction;
import com.tydic.beijing.billing.rating.service.RateDinnerFormula;
import com.tydic.beijing.billing.rating.service.RatingException;

/**
 * @author sung
 *
 */
public class DirectRating {

	private Logger log=Logger.getLogger(DirectRating.class);
	
	
	private RatingMsg ratingMsg;
	
	private RatingData ratingData;

	private DinnerConversionImpl dinner;
	
	private RatableResourceExtractionImpl  ratableResource;
	
	private RateDinnerFormulaImpl  rate;

	private RateData realRateData;
	
//	private RatableResourceUpdate  resourceUpdate;
	public DirectRating(){}
	
	public DirectRating(RatingMsg msg , RatingData data) {
		ratingMsg=msg;
		ratingData=data;
	
	}
	public RateData executeRating( ) throws RatingException {
		
		log.debug("[批价开始]");
		RateData result=null;
////		ratingData.setnOper(ParamData.OPER_REAL);
//		ratingMsg.setOper(ParamData.OPER_REAL);
//
////		getUpdateRateMeasure(ratingData.getnOper());
//		getTermRateMeasure();
//		ratingData.setiRateMeasure(ratingData.getiRealRateMeasure());
//		setRequestRateMeasure();
//		try {
//			result=calcFee();
//		} catch (RatingException e) {
//			log.error(e.printError());
//			
//			throw new RatingException(e.getErrorCode(),e.printError());
//			
//		}
//		
//			
//		setRBMessage( ratingMsg.getOper());
//		//更新累积量
////		resourceUpdate.updateRatableResourceValue();
		
		
		return result;
	}
	
	
	
	
	public RateData calcFee() throws RatingException{
		
		log.debug("根据消息获取时间::::calcFee:::"+ratingMsg.getBaseMsg().getM_strStartTime());
		
//		
//		log.debug("[批价计算]");
//		dinner=new DinnerConversionImpl(ratingMsg,ratingData);
//
//		
//		
////		RateData realRateData=null;
//		ratingData.getiChargeUnits().clear();
//		ratingData.getiBeforeChargeUnits().clear();
//
//		
//		dinner.extractRateData();
//		
//		ratableResource=new RatableResourceExtractionImpl(ratingMsg,dinner);
//		
//		if (!ratableResource.queryRatableResourceValue()) {
//				log.debug("累积量查询失败!!!!");
//				return null;
//		}
//		
//
//		
//		ratingData.setiTmpRatableResourceValues(ratableResource.getiRatableResourceValues());
//		rate=new RateDinnerFormulaImpl(ratingMsg,ratingData,dinner,ratableResource);
//		
//		rate.setCurrTime(ratingMsg.getBaseMsg().getM_strStartTime());
//		
//		realRateData=rate.rate();
//		ratingData=rate.getRatingData();
//		
//		mergeChargeUnits(ratingMsg.getOper());
//		if (ratingMsg.getOper() == RatingMacro.OPER_REQ) {
//			ratingData.setiReqChargeUnits(ratingData.getiChargeUnits());
//		} else {
//			ratingData.setiRealChargeUnits(ratingData.getiChargeUnits());
//			ratingData.setiRealRatableResourceValues(realRateData.getiRatableValues());
//		}

		
		return realRateData;
	}
	
	
	
	//合并费用项相同的ChargeUnit
		public void mergeChargeUnits(int oper){
			List<ChargeUnit> iTmpChargeUnits = new ArrayList<ChargeUnit>();

		    int lnItemTypeId = -1;
		    boolean bFlag;
		    for( ChargeUnit iter : ratingData.getiChargeUnits() ){
		        bFlag = true;
		        if( RatingMacro.CODE_ISMP_BRR == ratingMsg.getM_nMsgType() && lnItemTypeId != -1 )
		            iter.setLnAcctItemTypeId(lnItemTypeId);
		       
		        for( ChargeUnit unit : iTmpChargeUnits ){
		            //相同账目类型和帐本类型的,进行合并
		            if( unit !=null && iter.getLnAcctItemTypeId() == unit.getLnAcctItemTypeId() && iter.getnUnitType() == unit.getnUnitType() ){
		                if( iter.getnMTChangeFlag() == 1 && iter.getnUnitType()==2 ){ //MT转换金钱后的
		                    break;
		                }else{
		                    unit.setLnOriCharge(unit.getLnOriCharge()+iter.getLnOriCharge());
		                    unit.setLnCharge(unit.getLnCharge()+iter.getLnCharge());
		                    unit.setLnUnusedMondey(unit.getLnUnusedMondey()+iter.getLnUnusedMondey());
		                    unit.setLnLeftMoney(unit.getLnLeftMoney()+iter.getLnLeftMoney());
		                    unit.setLnFee(unit.getLnFee()+iter.getLnFee());
		                    unit.setLnCounts(unit.getLnCounts()+iter.getLnCounts());
		                    unit.setLnBillingDosage(unit.getLnBillingDosage()+iter.getLnBillingDosage());
		                    unit.setLnBeforeDosage(unit.getLnBeforeDosage()+iter.getLnBeforeDosage());
		                    unit.setLnBeforeDosageFront(unit.getLnBeforeDosageFront()+iter.getLnBeforeDosageFront());
		                    unit.print();
		                    bFlag = false;

		                    break;
		                }
		            }
		        }

		        if( bFlag )
		            iTmpChargeUnits.add( iter );
		    }
		    if( oper == RatingMacro.OPER_REQ )
		        ratingData.setiMergedReqChargeUnits(iTmpChargeUnits);
		    else
		        ratingData.setiMergedRealChargeUnits(iTmpChargeUnits);
		    
			
		}
		
		
	
	
	public void setRequestRateMeasure(){
		//消息中的使用量
		if(!ratingMsg.getM_iRatingExtMsg().getM_strDuration().isEmpty()){
			ratingData.setLnReqDuration(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strDuration()));
		}
		if(!ratingMsg.getM_iRatingExtMsg().getM_strTimes().isEmpty()){
			ratingData.setLnReqTimes(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strTimes()));
			
		}
		if(!ratingMsg.getM_iRatingExtMsg().getM_strUpVolume().isEmpty()){
			ratingData.setLnReqUpVolume(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strUpVolume()));
			//考虑费率切换点之后的值,下同
			ratingData.setLnReqUpVolume(ratingData.getLnReqUpVolume()+Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strUpVolumeFeeLast()));
			
		}
		if(!ratingMsg.getM_iRatingExtMsg().getM_strDownVolume().isEmpty() ){
			ratingData.setLnReqDownVolume(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strDownVolume()));
			ratingData.setLnReqDownVolume(ratingData.getLnReqDownVolume()+Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strDownVolumeFeeLast()));
			
		}
		if(!ratingMsg.getM_iRatingExtMsg().getM_strTotalVolume().isEmpty()){
			ratingData.setLnReqTotalVolume(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strTotalVolume()));
			ratingData.setLnReqTotalVolume(ratingData.getLnReqTotalVolume()+Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strTotalVolumeFeeLast()));
			
		}
	}
	
	
	
	
//	public void fillMeasureData(){
//		RateMeasure rateMeasure=initRateMeasureFromMsg();
//		ratingData.setiRealRateMeasure(rateMeasure);
//		
//	}
	
	
	
	
	
	
	/**
	 *  获取使用量
	 * @author sung
	 *
	 * @param measure
	 * @return
	 */
	
//	public RateMeasure initRateMeasureFromMsg(){
//		RateMeasure measure=new RateMeasure();
////		RatingExtMsg ratingExtMsg=ratingMsg.getM_iRatingExtMsg();//修改获取
//		CommandMsg ratingExtMsg=ratingMsg.getCommandMsg();
//		ratingData.setiCurrTime(ratingMsg.getBaseMsg().getM_strStartTime());
//		
//		long usedValue=0;
//		//按时长计费
////	    if( !ratingExtMsg.getM_strDuration().isEmpty() ){
////		usedValue=ratingExtMsg.getM_strDuration();
//		if(ratingExtMsg.getM_strUsedDuration()!=0){
//			usedValue=ratingExtMsg.getM_strUsedDuration();
//	        measure.setLnDuration(usedValue);
//	    }
//	    //按流量
////	    if( !ratingExtMsg.getM_strTotalVolume().isEmpty() ){
////	    	usedValue=ratingExtMsg.getM_strTotalVolume();
//		if( ratingExtMsg.getM_strUsedTotalVolume()!=0 ){
//	    	usedValue=ratingExtMsg.getM_strUsedTotalVolume();
//	    	measure.setLnTotalVolume(usedValue);
//	    }
//	    //按次
////	    if( !ratingExtMsg.getM_strTimes().isEmpty() ){
////	    	usedValue=ratingExtMsg.getM_strTimes();
//		if( ratingExtMsg.getM_strUsedTimes()!=0 ){
//	    	usedValue=ratingExtMsg.getM_strUsedTimes();
//	    	measure.setLnTimes(usedValue);
//	    }	
//	    //上行流量
////	    if( !ratingExtMsg.getM_strUpVolume().isEmpty() ){
////	    	usedValue=ratingExtMsg.getM_strUpVolume();
//		if( ratingExtMsg.getM_strUsedUpVolume()!=0 ){
//	    	usedValue=ratingExtMsg.getM_strUsedUpVolume();
//	    	measure.setLnUpVolume(usedValue);
//	    }	
//	    //下行流量
////	    if( !ratingExtMsg.getM_strDownVolume().isEmpty()){
////	    	usedValue=ratingExtMsg.getM_strDownVolume();
//		if( ratingExtMsg.getM_strUsedDownVolume()!=0){
//	    	usedValue=ratingExtMsg.getM_strUsedDownVolume();
//	    	measure.setLnDownVolume(usedValue);
//	    }	
//	    //金钱
//	    	
////	    usedValue=ratingExtMsg.getM_strTotalVolumeFeeLast();
////	    
////	    measure.setLnLastTotalVolume(Integer.parseInt(usedValue==""?"0":usedValue));
////	    
////	    usedValue=ratingExtMsg.getM_strUpVolumeFeeLast();
////	    measure.setLnLastUpVolume(Integer.parseInt(usedValue==""?"0":usedValue));
////	    
////	    usedValue=ratingExtMsg.getM_strDownVolumeFeeLast();
////	    measure.setLnLastDownVolume(Integer.parseInt(usedValue==""?"0":usedValue));
//	    
//	    measure.setLnUnusedDuration(measure.getLnDuration());
//	    measure.setLnUnusedTotalVolume(measure.getLnTotalVolume());
//	    measure.setLnUnusedTimes(measure.getLnTimes());
//	    measure.setLnUnusedUpVolume(measure.getLnUpVolume());
//	    measure.setLnUnusedDownVolume(measure.getLnDownVolume());
//	    measure.setLnUnusedLastTotalVolume(measure.getLnLastTotalVolume());
//	    measure.setLnUnusedLastUpVolume(measure.getLnLastUpVolume());
//	    measure.setLnUnusedLastDownVolume(measure.getLnLastDownVolume());
//	    log.debug("本次使用量 : *RateMeasure:"+measure);
//		return measure;
//	}
	
	
	
	public RateMeasure initRateMeasureFromMsg(){
		RateMeasure measure=new RateMeasure();
		RatingExtMsg ratingExtMsg=ratingMsg.getM_iRatingExtMsg();
		String usedValue="";
		//按时长计费
	    if( !ratingExtMsg.getM_strDuration().isEmpty() ){
	    	usedValue=ratingExtMsg.getM_strDuration();
	        measure.setLnDuration(Integer.parseInt(usedValue));
	    }
	    //按流量
	    if( !ratingExtMsg.getM_strTotalVolume().isEmpty() ){
	    	usedValue=ratingExtMsg.getM_strTotalVolume();
	    	measure.setLnTotalVolume(Integer.parseInt(usedValue));
	    }
	    //按次
	    if( !ratingExtMsg.getM_strTimes().isEmpty() ){
	    	usedValue=ratingExtMsg.getM_strTimes();
	    	measure.setLnTimes(Integer.parseInt(usedValue));
	    }	
	    //上行流量
	    if( !ratingExtMsg.getM_strUpVolume().isEmpty() ){
	    	usedValue=ratingExtMsg.getM_strUpVolume();
	    	measure.setLnUpVolume(Integer.parseInt(usedValue));
	    }	
	    //下行流量
	    if( !ratingExtMsg.getM_strDownVolume().isEmpty()){
	    	usedValue=ratingExtMsg.getM_strDownVolume();
	    	measure.setLnDownVolume(Integer.parseInt(usedValue));
	    }	
	    //金钱
	    	
	    usedValue=ratingExtMsg.getM_strTotalVolumeFeeLast();
	    
	    measure.setLnLastTotalVolume(Integer.parseInt(usedValue==""?"0":usedValue));
	    
	    usedValue=ratingExtMsg.getM_strUpVolumeFeeLast();
	    measure.setLnLastUpVolume(Integer.parseInt(usedValue==""?"0":usedValue));
	    
	    usedValue=ratingExtMsg.getM_strDownVolumeFeeLast();
	    measure.setLnLastDownVolume(Integer.parseInt(usedValue==""?"0":usedValue));
	    
	    measure.setLnUnusedDuration(measure.getLnDuration());
	    measure.setLnUnusedTotalVolume(measure.getLnTotalVolume());
	    measure.setLnUnusedTimes(measure.getLnTimes());
	    measure.setLnUnusedUpVolume(measure.getLnUpVolume());
	    measure.setLnUnusedDownVolume(measure.getLnDownVolume());
	    measure.setLnUnusedLastTotalVolume(measure.getLnLastTotalVolume());
	    measure.setLnUnusedLastUpVolume(measure.getLnLastUpVolume());
	    measure.setLnUnusedLastDownVolume(measure.getLnLastDownVolume());
	    log.debug("本次使用量 : *RateMeasure:"+measure);
	    log.debug("本次使用量 : *RateMeasure:"+measure);
		return measure;
	}
	
	
	
	
	/**
	 * 
	 * @param oper
	 */
	public void setRBMessage(int oper){
		
//			Fee iFee=new Fee();
//	        for( ChargeUnit u: ratingData.getiMergedRealChargeUnits()){
//	            setRealFee( u );
//
//	            iFee.setLnAcctItemTypeId(u.getLnAcctItemTypeId());
//	            iFee.setnUnitType(u.getnUnitType());
//	            iFee.setLnTariff(u.getLnUnusedMondey());//本次批价多出的金钱.
//
//	            updateUnusedMoney( iFee );
//	        }
////	        setUnusedMoneyFromSession();//更新到内存中
//	        
//	        ratingData.setiRealFees(realRateData.getiRealFees());
		
	}
	
	
	
	private int updateUnusedMoney(Fee fee){
		boolean find=false;
		if(fee.getnUnitType()!=2){   //非金钱账本
			ratingData.getiUnusedMoneys().clear();
			return 0;
		}else{
			for(Fee f:ratingData.getiUnusedMoneys()){
				if(f.getLnAcctItemTypeId()==fee.getLnAcctItemTypeId() && f.getnUnitType()==fee.getnUnitType()){
					f.setLnTariff(fee.getLnTariff());
					find=true;
					break;
				}
			}
			
			if(!find && fee.getLnTariff()!=0){
				ratingData.getiUnusedMoneys().add(fee);
			}
		}
		return 0;
	}
	
	
	
	
	private void setRealFee(ChargeUnit u){
		log.debug("设置RB返回数据(chargeUnit):");
//		u.show();
//		BalanceContent iBalanceContent =new BalanceContent();
//
//	    iBalanceContent.setLnAcctItemTypeId(u.getLnAcctItemTypeId());
//	    iBalanceContent.setnUnitTypeId(u.getnUnitType());
//	    iBalanceContent.setLnAmount(u.getLnFee());
//	    iBalanceContent.setLnDosage(u.getLnBillingDosage());
//	    iBalanceContent.setLnLeftMoney(u.getLnLeftMoney());
//	    iBalanceContent.setnMeasureDomain(Integer.parseInt(u.getStrMeasureDomain()));
//	    realRateData.getiRealFees().add(iBalanceContent);
//	    ratingData.getiRealFees().add(iBalanceContent);
	}
	


	public void getUpdateRateMeasure(int oper){
		//处理时间
		
		//TODO 新增
		ratingMsg.getVarMsg().setM_strCurrTime(ratingMsg.getBaseMsg().getM_strStartTime());
		ratingMsg.getVarMsg().setM_strLastTime(ratingMsg.getBaseMsg().getM_strStartTime());
		ratingData.setiCurrTime(ratingMsg.getBaseMsg().getM_strStartTime());
		
		
		
		ratingMsg.getM_iRatingExtMsg().setM_strExtStartTime(ratingMsg.getBaseMsg().getM_strStartTime());
		ratingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(ratingMsg.getVarMsg().getM_strCurrTime());
		ratingMsg.getM_iRatingExtMsg().setM_strExtLastTime(ratingMsg.getVarMsg().getM_strLastTime());
		
		if(oper==RatingMacro.OPER_REQ){// 申请
			if(ratingMsg.getCommandMsg().getM_strReqDuration()!=0){//处理时长
				ratingMsg.getM_iRatingExtMsg().setM_strDuration(""+ratingMsg.getCommandMsg().getM_strReqDuration());
				ratingData.getiStartValue().setLnDuration(ratingData.getiPrevSessionValue().getUsedDuration()+ratingData.getiCurrSessionValue().getUsedDuration());
				
			}
			if(ratingMsg.getCommandMsg().getM_strReqTimes()!=0){  //处理次数
				ratingMsg.getM_iRatingExtMsg().setM_strTimes(""+ratingMsg.getCommandMsg().getM_strReqTimes());
				ratingData.getiStartValue().setLnTimes(ratingData.getiPrevSessionValue().getUsedTimes()+ratingData.getiCurrSessionValue().getUsedTimes());
			}
			if(ratingMsg.getCommandMsg().getM_strReqUpVolume()!=0){ //处理上行流量
				ratingMsg.getM_iRatingExtMsg().setM_strUpVolume(""+ratingMsg.getCommandMsg().getM_strReqUpVolume());
				ratingData.getiStartValue().setLnUpVolume(ratingData.getiPrevSessionValue().getUsedUpVolume()+ratingData.getiCurrSessionValue().getUsedUpVolume());
				ratingMsg.getM_iRatingExtMsg().setM_strUpVolumeFeeLast("0");
				
			}
			if(ratingMsg.getCommandMsg().getM_strReqDownVolume()!=0){//处理下行流量
				ratingMsg.getM_iRatingExtMsg().setM_strDownVolume(""+ratingMsg.getCommandMsg().getM_strReqDownVolume());
				ratingData.getiStartValue().setLnDownVolume(ratingData.getiPrevSessionValue().getUsedDownVolume()+ratingData.getiCurrSessionValue().getUsedDownVolume());
				ratingMsg.getM_iRatingExtMsg().setM_strDownVolumeFeeLast("0");
			}
			if(ratingMsg.getCommandMsg().getM_strReqTotalVolume()!=0){//处理总流量 
				ratingMsg.getM_iRatingExtMsg().setM_strTotalVolume(""+ratingMsg.getCommandMsg().getM_strReqTotalVolume());
				ratingData.getiStartValue().setLnTotalVolume(ratingData.getiPrevSessionValue().getUsedTotalVolume()+ratingData.getiCurrSessionValue().getUsedTotalVolume());
				ratingMsg.getM_iRatingExtMsg().setM_strTotalVolumeFeeLast("0");
			}
			
		}else{
			long tempValue=0;
			long reserveAmount=0;
			long unUsedAmount=0;
			long amount=0;
			long lastAmount=0;
			//因为每次Update都要实扣,所以取上次扣费时间作为本次实扣开始时间 
			ratingMsg.getVarMsg().setM_strLastTime(ratingMsg.getBaseMsg().getM_strStartTime());
			
			ratingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(ratingMsg.getVarMsg().getM_strLastTime());
			if(ratingMsg.getCommandMsg().getM_strUsedDuration()!=0){//使用时长
				log.debug("获取已使用量:时长类型");
				tempValue=ratingMsg.getCommandMsg().getM_strUsedDuration();//申请实扣
				reserveAmount=ratingData.getiPrevSessionValue().getReserveDuration();//上次预占
				unUsedAmount=ratingData.getiPrevSessionValue().getUnUsedDuration();//上次多扣
				amount=0;
				tempValue-=unUsedAmount;
				if(tempValue<=0){
					amount=0;
					unUsedAmount=-tempValue;
				}else if(tempValue >= reserveAmount){
					amount=tempValue;
					unUsedAmount=0;
				}else{
					amount=tempValue;
					unUsedAmount=0;
				}
				ratingMsg.getM_iRatingExtMsg().setM_strDuration(""+amount);
				ratingData.getiCurrSessionValue().setUsedDuration(amount);
				ratingData.getiCurrSessionValue().setUnUsedDuration(unUsedAmount);
				ratingData.getiStartValue().setLnDuration(ratingData.getiPrevSessionValue().getUsedDuration());//计费点A
				
			}
			//处理次数
			if(ratingMsg.getCommandMsg().getM_strUsedTimes()!=0 ){
				log.debug("获取已使用量:次数类型");
//				ratingMsg.getM_iRatingExtMsg().setM_strTimes(ratingMsg.getCommandMsg().getM_strUsedTimes());
//				ratingData.getiCurrSessionValue().setUsedTimes(Integer.parseInt(ratingMsg.getCommandMsg().getM_strUsedTimes()));
//				ratingData.getiStartValue().setLnTimes(ratingData.getiPrevSessionValue().getUsedTimes());
				
				ratingMsg.getM_iRatingExtMsg().setM_strTimes(""+ratingMsg.getCommandMsg().getM_strUsedTimes());
				ratingData.getiCurrSessionValue().setUsedTimes(ratingMsg.getCommandMsg().getM_strUsedTimes());
				ratingData.getiStartValue().setLnTimes(ratingData.getiPrevSessionValue().getUsedTimes());
				
			}
			
			//处理上行流量
			if(ratingMsg.getCommandMsg().getM_strUsedUpVolume()!=0){
				log.debug("获取已使用量:上行流量");
			//	tempValue=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedUpVolume());
				tempValue=ratingMsg.getCommandMsg().getM_strUsedUpVolume();
				//加上上次未成功扣费的B307消息
				tempValue+=ratingData.getiPrevSessionValue().getCcgSpStart();
				reserveAmount=ratingData.getiPrevSessionValue().getReserveUpVolume();
				unUsedAmount=ratingData.getiPrevSessionValue().getUnUsedUpVolume();
				tempValue-=unUsedAmount;
				if(tempValue<=0){
					amount=0;
					unUsedAmount=-tempValue;
				}else if(tempValue >= reserveAmount){
					amount=tempValue;
					unUsedAmount=0;
				}else{
					amount=tempValue;
					unUsedAmount=0;
				}
				ratingMsg.getM_iRatingExtMsg().setM_strUpVolume(""+amount);
				ratingData.getiCurrSessionValue().setUnUsedUpVolume(unUsedAmount);
				ratingData.getiStartValue().setLnDownVolume(ratingData.getiPrevSessionValue().getUsedUpVolume());
		//		lastAmount=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedUpVolumeFeeLast());
				lastAmount=ratingMsg.getCommandMsg().getM_strUsedUpVolumeFeeLast();
				lastAmount+=ratingData.getiPrevSessionValue().getCcgSpEnd();
				ratingData.getiCurrSessionValue().setUsedUpVolume(amount+lastAmount);
				ratingMsg.getM_iRatingExtMsg().setM_strUpVolumeFeeLast(""+lastAmount);
				
			}
			//处理下行流量
			if(ratingMsg.getCommandMsg().getM_strUsedDownVolume()!=0){
				log.debug("获取已使用量:下行流量");
		//		tempValue=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedDownVolume());
				tempValue=ratingMsg.getCommandMsg().getM_strUsedDownVolume();
				tempValue+=ratingData.getiPrevSessionValue().getCcgSpStart();
				reserveAmount=ratingData.getiPrevSessionValue().getReserveDownVolume();
				unUsedAmount=ratingData.getiPrevSessionValue().getUnUsedDownVolume();
				tempValue-=unUsedAmount;
				if(tempValue <=0){
					amount=0;
					unUsedAmount=tempValue;
					
				}else if(tempValue>=reserveAmount){
					amount=tempValue;
					unUsedAmount=0;
				}else{
					amount=tempValue;
					unUsedAmount=0;
				}
				ratingMsg.getM_iRatingExtMsg().setM_strDownVolume(""+amount);
				ratingData.getiCurrSessionValue().setUnUsedDownVolume(unUsedAmount);
				ratingData.getiStartValue().setLnDownVolume(ratingData.getiPrevSessionValue().getUsedDownVolume());
	//			lastAmount=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedDownVolumeFeeLast());
				lastAmount=ratingMsg.getCommandMsg().getM_strUsedDownVolumeFeeLast();
				lastAmount+=ratingData.getiPrevSessionValue().getCcgSpEnd();
				ratingData.getiCurrSessionValue().setUsedDownVolume(amount+lastAmount);
				ratingMsg.getM_iRatingExtMsg().setM_strDownVolumeFeeLast(""+lastAmount);
				
			}
			//处理总流量
			if(ratingMsg.getCommandMsg().getM_strUsedTotalVolume()!=0){
				log.debug("获取已使用量:总流量");
			//	tempValue=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedTotalVolume());
				tempValue=ratingMsg.getCommandMsg().getM_strUsedTotalVolume();
				tempValue+=ratingData.getiPrevSessionValue().getCcgSpStart();
				reserveAmount=ratingData.getiPrevSessionValue().getReserveTotalVolume();
				unUsedAmount=ratingData.getiPrevSessionValue().getUnUsedTotalVolume();
				tempValue-=unUsedAmount;
				if(tempValue <=0){
					amount=0;
					unUsedAmount=-tempValue;
				}else if(tempValue >= reserveAmount){
					amount=tempValue;
					unUsedAmount=0;
				}else{
					amount=tempValue;
					unUsedAmount=0;
				}
				ratingMsg.getM_iRatingExtMsg().setM_strTotalVolume(""+amount);
				ratingData.getiCurrSessionValue().setUnUsedTotalVolume(unUsedAmount);
				ratingData.getiStartValue().setLnTotalVolume(ratingData.getiPrevSessionValue().getUsedTotalVolume());
	//			lastAmount=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedTotalVolumeFeeLast());
				lastAmount=ratingMsg.getCommandMsg().getM_strUsedTotalVolumeFeeLast();
				lastAmount+=ratingData.getiPrevSessionValue().getCcgSpEnd();
				ratingData.getiCurrSessionValue().setUsedTotalVolume(amount+lastAmount);
				ratingMsg.getM_iRatingExtMsg().setM_strTotalVolumeFeeLast(""+lastAmount);
				
			}
		}
		if(oper==RatingMacro.OPER_REQ){

			RateMeasure rateMeasure=initRateMeasureFromMsg();
			ratingData.setiReqRateMeasure(rateMeasure);
			
		}else{
			
			RateMeasure rateMeasure=initRateMeasureFromMsg();
			ratingData.setiRealRateMeasure(rateMeasure);
		}
	}
	
	
	
	

	public void getTermRateMeasure (){
		//处理时间
		
		//TODO 新增
				ratingMsg.getVarMsg().setM_strCurrTime(ratingMsg.getBaseMsg().getM_strStartTime());
				ratingMsg.getVarMsg().setM_strLastTime(ratingMsg.getBaseMsg().getM_strStartTime());
				ratingData.setiCurrTime(ratingMsg.getBaseMsg().getM_strStartTime());
				
				
		ratingMsg.getM_iRatingExtMsg().setM_strExtStartTime(ratingMsg.getBaseMsg().getM_strStartTime());
		ratingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(ratingMsg.getVarMsg().getM_strCurrTime());
		ratingMsg.getM_iRatingExtMsg().setM_strExtLastTime(ratingMsg.getVarMsg().getM_strLastTime());
	    //获取消息版本及会话类型
	    int nMsgVersion = ratingMsg.getM_nMsgVersion();
	    String szTmp = "";
	    long lnAmount = 0;
	    long lnTempValue = 0;
	    long lnUnUsedAmount = 0;
	    long lnReservedAmount = 0;
	    long lnLastAmount =0;

	    //因为每次Update都要实扣，所以取上次扣费时间作为本次实扣开始时间
	    ratingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(ratingMsg.getVarMsg().getM_strLastTime());
	    
	    if( nMsgVersion == 2){
	    
	        //Term消息通过总量-已使用量得到
	        //处理时长
	        if ( ratingMsg.getCommandMsg().getM_strRealDuration()!=0)
	        {
	            
	        	lnAmount=ratingMsg.getCommandMsg().getM_strRealDuration()-ratingData.getiPrevSessionValue().getUsedDuration();
	            if ( lnAmount < 0 ){
	            
	                lnAmount = 0;
	            }
	            ratingMsg.getM_iRatingExtMsg().setM_strDuration(""+lnAmount);
	            ratingData.getiCurrSessionValue().setUsedDuration(lnAmount);
	            ratingData.getiStartValue().setLnDuration(ratingData.getiPrevSessionValue().getUsedDuration());
	           
	          
	        }
	        
	        //处理次数
	        if ( ratingMsg.getCommandMsg().getM_strRealTimes() !=0 ){
	        
	            lnAmount=ratingMsg.getCommandMsg().getM_strRealTimes()-ratingData.getiPrevSessionValue().getUsedTimes();
	            if ( lnAmount < 0 ){
	            
	                lnAmount = 0;
	            }
	            ratingMsg.getM_iRatingExtMsg().setM_strTimes(""+lnAmount);
	            ratingData.getiCurrSessionValue().setUsedTimes(lnAmount);
	            ratingData.getiStartValue().setLnTimes(ratingData.getiPrevSessionValue().getUsedTimes());
	           
	        }
	        
	        
	        //处理上行流量
	        if (  ratingMsg.getCommandMsg().getM_strUsedUpVolume() !=0  )
	        {
	            lnTempValue = ratingMsg.getCommandMsg().getM_strUsedUpVolume();
	            //加上 上次未成功扣费的B307信息
	            lnTempValue += ratingData.getiPrevSessionValue().getCcgSpStart();
	            lnReservedAmount = ratingData.getiPrevSessionValue().getReserveUpVolume();
	            lnUnUsedAmount = ratingData.getiPrevSessionValue().getUnUsedUpVolume();
	            lnTempValue -= lnUnUsedAmount;
	            if ( lnTempValue <= 0 ){
	            
	                lnAmount = 0;
	                lnUnUsedAmount = -lnTempValue;
	            }else if ( lnTempValue >= lnReservedAmount ){
	            
	                lnAmount = lnTempValue;
	                lnUnUsedAmount = 0;
	            }else{
	            
	                lnAmount=lnTempValue;
	                lnUnUsedAmount=0;
	            }
	            ratingMsg.getM_iRatingExtMsg().setM_strUpVolume(""+lnAmount);
	            ratingData.getiCurrSessionValue().setUnUsedUpVolume(lnUnUsedAmount);
	            ratingData.getiStartValue().setLnUpVolume(ratingData.getiPrevSessionValue().getUsedUpVolume());
	            lnLastAmount = ratingMsg.getCommandMsg().getM_strUsedUpVolumeFeeLast();
	            //加上 上次未扣费成功的B308
	            lnLastAmount += ratingData.getiPrevSessionValue().getCcgSpEnd();
	            ratingData.getiCurrSessionValue().setUsedUpVolume(lnAmount + lnLastAmount);
	            ratingMsg.getM_iRatingExtMsg().setM_strUpVolumeFeeLast(""+lnLastAmount);
	        }
	        
	        //处理下行流量
	        if ( ratingMsg.getCommandMsg().getM_strUsedDownVolume() !=0  )
	        {
	            lnTempValue = ratingMsg.getCommandMsg().getM_strUsedDownVolume();
	            //加上 上次未成功扣费的B307信息
	            lnTempValue += ratingData.getiPrevSessionValue().getCcgSpStart();

	            lnReservedAmount = ratingData.getiPrevSessionValue().getReserveDownVolume();
	            lnUnUsedAmount = ratingData.getiPrevSessionValue().getUnUsedDownVolume();
	            lnTempValue -= lnUnUsedAmount;
	            if ( lnTempValue <= 0 ){
	            
	                lnAmount = 0;
	                lnUnUsedAmount = -lnTempValue;
	            }else if ( lnTempValue >= lnReservedAmount ){
	            
	                lnAmount = lnTempValue;
	                lnUnUsedAmount = 0;
	            }else{
	            
	                lnAmount=lnTempValue;
	                lnUnUsedAmount=0;
	            }
	            ratingMsg.getM_iRatingExtMsg().setM_strDownVolume(""+lnAmount);
	            ratingData.getiCurrSessionValue().setUsedDownVolume(lnAmount); 
	            ratingData.getiCurrSessionValue().setUnUsedDownVolume(lnUnUsedAmount);
	            ratingData.getiStartValue().setLnDownVolume(ratingData.getiPrevSessionValue().getUsedDownVolume());
	            lnLastAmount = ratingMsg.getCommandMsg().getM_strUsedDownVolumeFeeLast();
	            //加上 上次未扣费成功的B308
	            lnLastAmount += ratingData.getiPrevSessionValue().getCcgSpEnd();
	            ratingData.getiCurrSessionValue().setUsedDownVolume(lnAmount +lnLastAmount);
	            
	            ratingMsg.getM_iRatingExtMsg().setM_strDownVolumeFeeLast(""+lnLastAmount);
	        }
	        
	        
        //处理总流量
        if ( ratingMsg.getCommandMsg().getM_strUsedTotalVolume() !=0 )
        {
            lnTempValue = ratingMsg.getCommandMsg().getM_strUsedTotalVolume();
            //加上 上次未成功扣费的B307信息
            lnTempValue += ratingData.getiPrevSessionValue().getCcgSpStart();

            lnReservedAmount = ratingData.getiPrevSessionValue().getReserveTotalVolume();
            lnUnUsedAmount = ratingData.getiPrevSessionValue().getUnUsedTotalVolume(); 
            lnTempValue -= lnUnUsedAmount;
            if ( lnTempValue <= 0 ){
            
                lnAmount = 0;
                lnUnUsedAmount = -lnTempValue;
            }else if ( lnTempValue >= lnReservedAmount ){
            
                lnAmount = lnTempValue;
                lnUnUsedAmount = 0;
            }else{
            
                lnAmount=lnTempValue;
                lnUnUsedAmount=0;
            }

            ratingMsg.getM_iRatingExtMsg().setM_strTotalVolume(""+lnAmount);
            
            ratingData.getiCurrSessionValue().setUnUsedTotalVolume(lnUnUsedAmount); 
            ratingData.getiStartValue().setLnTotalVolume(ratingData.getiPrevSessionValue().getUsedTotalVolume()); 
            lnLastAmount = ratingMsg.getCommandMsg().getM_strUsedTotalVolumeFeeLast();
            //加上 上次未扣费成功的B308
            lnLastAmount += ratingData.getiPrevSessionValue().getCcgSpEnd();

            ratingData.getiCurrSessionValue().setUsedTotalVolume(lnAmount + lnLastAmount);
          
            ratingMsg.getM_iRatingExtMsg().setM_strTotalVolumeFeeLast(""+lnLastAmount); 
        }
    }
    else
    {
        //处理时长
        if ( ratingMsg.getCommandMsg().getM_strRealDuration() !=0 )
        {
            ratingMsg.getM_iRatingExtMsg().setM_strDuration(""+ratingMsg.getCommandMsg().getM_strRealDuration()); 
            ratingData.getiCurrSessionValue().setUsedDuration(ratingMsg.getCommandMsg().getM_strRealDuration()); 
            ratingData.getiStartValue().setLnDuration(ratingData.getiPrevSessionValue().getUsedDuration()); 
        }
        //处理次数
        if ( ratingMsg.getCommandMsg().getM_strRealTimes() !=0 )
        {
            ratingMsg.getM_iRatingExtMsg().setM_strTimes(""+ratingMsg.getCommandMsg().getM_strRealTimes()); 
            ratingData.getiCurrSessionValue().setUsedTimes(ratingMsg.getCommandMsg().getM_strRealTimes());
            ratingData.getiStartValue().setLnTimes(ratingData.getiPrevSessionValue().getUsedTimes());
        }

        //处理上行流量
        if ( ratingMsg.getCommandMsg().getM_strRealUpVolume() !=0  )
        {
            ratingMsg.getM_iRatingExtMsg().setM_strUpVolume(""+ratingMsg.getCommandMsg().getM_strRealUpVolume()); 
            ratingData.getiCurrSessionValue().setUsedUpVolume(ratingMsg.getCommandMsg().getM_strRealUpVolume()); 
            ratingData.getiStartValue().setLnUpVolume(ratingData.getiPrevSessionValue().getUsedUpVolume()); 
        }

        //处理下行流量
        if ( ratingMsg.getCommandMsg().getM_strRealDownVolume() !=0  )
        {
            ratingMsg.getM_iRatingExtMsg().setM_strDownVolume(""+ratingMsg.getCommandMsg().getM_strRealDownVolume()); 
            ratingData.getiCurrSessionValue().setUsedDownVolume(ratingMsg.getCommandMsg().getM_strReqDownVolume()); 
            ratingData.getiStartValue().setLnDownVolume(ratingData.getiPrevSessionValue().getUsedDownVolume()); 
        }

        //处理总流量
        if ( ratingMsg.getCommandMsg().getM_strRealTotalVolume() !=0 )
        {
            ratingMsg.getM_iRatingExtMsg().setM_strTotalVolume(""+ratingMsg.getCommandMsg().getM_strRealTotalVolume()); 
            ratingData.getiCurrSessionValue().setUsedTotalVolume(ratingMsg.getCommandMsg().getM_strRealTotalVolume()); 
            ratingData.getiStartValue().setLnTotalVolume(ratingData.getiPrevSessionValue().getUsedTotalVolume()); 
        }
    }


	    ///NOTE:设置批价使用量
	    ratingData.setiRealRateMeasure(initRateMeasureFromMsg());	
				
	}
	
	public RatingMsg getRatingMsg() {
		return ratingMsg;
	}
	public RatingData getRatingData() {
		return ratingData;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
