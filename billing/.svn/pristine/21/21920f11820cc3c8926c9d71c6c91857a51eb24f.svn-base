/**
 * 
 */
package com.tydic.beijing.billing.rating.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.rating.domain.BalanceContent;
import com.tydic.beijing.billing.rating.domain.BalanceData;
import com.tydic.beijing.billing.rating.domain.ChargeUnit;
import com.tydic.beijing.billing.rating.domain.CodeRatableResource;
import com.tydic.beijing.billing.rating.domain.DeductHistory;
import com.tydic.beijing.billing.rating.domain.PlanDisct;
import com.tydic.beijing.billing.rating.domain.RBConf;
import com.tydic.beijing.billing.rating.domain.Ratable;
import com.tydic.beijing.billing.rating.domain.RatableCondCheck;
import com.tydic.beijing.billing.rating.domain.RatableCondInParam;
import com.tydic.beijing.billing.rating.domain.RatableResourceInfo;
import com.tydic.beijing.billing.rating.domain.RatableResourceValue;
import com.tydic.beijing.billing.rating.domain.RatableUpdateReq;
import com.tydic.beijing.billing.rating.domain.RatableUpdateResp;
import com.tydic.beijing.billing.rating.domain.RatingData;
import com.tydic.beijing.billing.rating.domain.RatingMacro;
import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.RequestRatableUpdate;
import com.tydic.beijing.billing.rating.domain.RuleOfrResourceRel;
import com.tydic.beijing.billing.rating.domain.SessionInfo;
import com.tydic.beijing.billing.rating.domain.SessionInformationExt;
import com.tydic.beijing.billing.rating.domain.SessionValue;
import com.tydic.beijing.billing.rating.domain.TbDeductRecordHistory;
import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.dto.impl.DbUtilImpl;
import com.tydic.beijing.billing.rating.service.ChargingUpdate;
import com.tydic.beijing.billing.rating.service.DinnerConversion;
import com.tydic.beijing.billing.rating.service.ErrorInfo;
import com.tydic.beijing.billing.rating.service.PricingSectionRate;
import com.tydic.beijing.billing.rating.service.RatableResourceExtraction;
import com.tydic.beijing.billing.rating.service.RateDinnerFormula;
import com.tydic.beijing.billing.rating.util.DateUtil;

/**
 * @author sung
 *
 */
public abstract class ChargingUpdateAbstract {

	protected RatingMsg ratingMsg;
	protected RatingData ratingData;
	
	protected RBConf conf=new RBConf();
	protected RatableCondCheck m_pRatableCondCheck;
	protected DinnerConversion  dinnerConv;
	
	protected RatableResourceExtraction ratableResource;
	
	protected RateDinnerFormula   formulaRate;
	
	protected PricingSectionRate   pricingSection;
	
	public int prepareForBalance(){
	    int nBillingFlag = ratingMsg.getM_nBillingFlag();
	    int nMsgType =  ratingMsg.getM_nMsgType();

	    BalanceData iBalanceData = ratingData.getiBalanceData();

	    if ( nBillingFlag == RatingMacro.EVENT_FEE || nBillingFlag == RatingMacro.EVENT_FEE || nBillingFlag == RatingMacro.SESSION_BEGIN ){
	    	System.out.println("prepareForBalance   ------if   -----");
	        iBalanceData.setM_strTimeStamp(ratingMsg.getVarMsg().getM_strCurrTime()); 
	    }else{
	    	System.out.println("prepareForBalance   ------else   -----");
	        if ( RatingMacro.CODE_CCG_BRR == nMsgType && RatingMacro.SESSION_BEGIN != nBillingFlag )
	        	iBalanceData.setM_strTimeStamp(ratingMsg.getVarMsg().getM_strLastTime());
	        else if ( RatingMacro.CODE_IN_BRR == nMsgType && nBillingFlag != RatingMacro.SESSION_END )
	        	iBalanceData.setM_strTimeStamp(ratingMsg.getVarMsg().getM_strCurrTime()); 
	        else
	        	iBalanceData.setM_strTimeStamp(ratingMsg.getBaseMsg().getM_strStartTime());
	    }

	    if ( iBalanceData.getM_strTimeStamp().isEmpty() ){
	    	iBalanceData.setM_strTimeStamp(DateUtil.getCurrentTime());
	    }

	    iBalanceData.setM_strSessionId(ratingMsg.getBaseMsg().getM_strSessionId());
	    iBalanceData.setM_strAreaCode(ratingMsg.getBaseMsg().getM_strChargedHomeAreaCode());
	    iBalanceData.setM_strAccNbr(ratingMsg.getBaseMsg().getM_strChargedNbr());
	    iBalanceData.setM_lnServId(ratingMsg.getM_iChargedMsg().getM_lnChargedServId());
	    iBalanceData.setM_lnAcctId(ratingMsg.getM_iChargedMsg().getM_lnChargedAcctId());
	    String customer=ratingMsg.getM_iChargedMsg().getM_lnChargedCustId();
	    if(customer == null || customer.equals("")){
	    	iBalanceData.setM_lnCustId(0);
	    }else{
	    	iBalanceData.setM_lnCustId(Long.parseLong(customer));
	    }
//	    iBalanceData.setM_lnCustId(Long.parseLong(ratingMsg.getM_iChargedMsg().getM_lnChargedCustId()));
	    iBalanceData.setM_nLatnId(ratingMsg.getM_iChargedMsg().getM_nChargedLatnId());
	    iBalanceData.setM_nBillingFlag(nBillingFlag);
	    iBalanceData.setM_nMsgType(nMsgType);
	    iBalanceData.setM_bFreeFlag(ratingMsg.getM_iExtMsg().isM_bFreeFlag());	//释放标识
	   
		
		return 0;
	}
	
	
	/**
	 * 
	 * @author sung
	 *
	 * @return
	 */
	public int getSession(){
	    
	    int nBillingFlag = ratingMsg.getM_nBillingFlag();
	    SessionValue sv=new SessionValue();
	    
	    ratingData.setiPrevSessionValue(sv);
	    int ret = 0;
	    //查询会话信息
	    if(nBillingFlag==RatingMacro.SESSION_END || nBillingFlag==RatingMacro.SESSION_UPDATE){
	    	ret=getSession(ratingMsg.getBaseMsg().getM_strSessionId());
	    	
	    }
		
		return ret;
	}
	
	
	/**
	 * 
	 * @author sung
	 *
	 * @param sessionId
	 * @return
	 */
	public int getSession(String sessionId){
		SessionInformationExt session=new SessionInformationExt();
		DbUtil db=new DbUtilImpl();
		session=db.getSessionInformationBySessionId(Long.parseLong(sessionId));
		if(null==session){
			return ErrorInfo.ERR_SQL_SESSION_INFO_NOT_FOUND;
		}
		SessionInfo sessionInfo=new SessionInfo();
		sessionInfo.sessionInit(session);
		ratingData.setStrPayPlanInfo(session.getPay_Plan_Info());
		ratingData.setStrPricingPlanInfo(session.getPricing_Plan_Info());
		ratingData.setStrUnusedMoneyInfo(session.getUnused_Money());
		ratingData.getiSessionInfos().add(sessionInfo);
		ratingData.getiPrevSessionValue().setTabUsedDuration(session.getUsed_Duration());
		ratingData.getiPrevSessionValue().setTabUsedTimes(session.getUsed_Times());
		ratingData.getiPrevSessionValue().setTabUsedTotalVolume(session.getUsed_Totalvolume());
		ratingData.getiPrevSessionValue().setTabUsedUpVolume(session.getUsed_Upvolume());
		ratingData.getiPrevSessionValue().setTabUsedDownVolume(session.getUsed_Downvolume());
		int unitType=session.getUnit_Type();
		switch(unitType){
		case RatingMacro.CREDITUNIT_MONEY:
            ratingData.getiPrevSessionValue().setReserveMoney(session.getReserved_Amount());
            ratingData.getiPrevSessionValue().setUsedMoney(session.getUsed_Amount());
            ratingData.getiPrevSessionValue().setUnUsedMoney(session.getUnused_Amount());
            break;
        case RatingMacro.CREDITUNIT_TIMELEN:
            ratingData.getiPrevSessionValue().setReserveDuration(session.getReserved_Amount());
            ratingData.getiPrevSessionValue().setUsedDuration(session.getUsed_Amount());
            ratingData.getiPrevSessionValue().setUnUsedDuration(session.getUnused_Amount());
            break;
        case RatingMacro.CREDITUNIT_TOTALVAL:
            ratingData.getiPrevSessionValue().setReserveTotalVolume(session.getReserved_Amount());
            ratingData.getiPrevSessionValue().setUsedTotalVolume(session.getUsed_Amount());
            ratingData.getiPrevSessionValue().setUnUsedTotalVolume(session.getUnused_Amount());
            ratingData.getiPrevSessionValue().setCcgSpStart(session.getSwitchpoint_Start());
            ratingData.getiPrevSessionValue().setCcgSpEnd(session.getSwitchpoint_End());
            break;
        case RatingMacro.CREDITUNIT_UPVAL:
            ratingData.getiPrevSessionValue().setReserveUpVolume(session.getReserved_Amount());
            ratingData.getiPrevSessionValue().setUsedUpVolume(session.getUsed_Amount());
            ratingData.getiPrevSessionValue().setUnUsedUpVolume(session.getUnused_Amount());
            ratingData.getiPrevSessionValue().setCcgSpStart(session.getSwitchpoint_Start());
            ratingData.getiPrevSessionValue().setCcgSpEnd(session.getSwitchpoint_End());
            break;
        case RatingMacro.CREDITUNIT_DOWNVAL:
            ratingData.getiPrevSessionValue().setReserveDownVolume(session.getReserved_Amount());
            ratingData.getiPrevSessionValue().setUsedDownVolume(session.getUsed_Amount());
            ratingData.getiPrevSessionValue().setUnUsedDownVolume(session.getUnused_Amount());
            ratingData.getiPrevSessionValue().setCcgSpStart(session.getSwitchpoint_Start());
            ratingData.getiPrevSessionValue().setCcgSpEnd(session.getSwitchpoint_End());
            break;
        case RatingMacro.CREDITUNIT_COUNT:
            ratingData.getiPrevSessionValue().setReserveTimes(session.getReserved_Amount());
            ratingData.getiPrevSessionValue().setUsedTimes(session.getUsed_Amount());
            ratingData.getiPrevSessionValue().setUnUsedTimes(session.getUnused_Amount());
            break;
        default: 
            break;
		}
		
		return 0;
	}
	
	
	
	
	/**
	 * 
	 * @author sung
	 *
	 * @return
	 */
	public int getDeductHistory(){
		if(ratingMsg.isNeedQueryDeductHis() && !isQueryBackSuccess()){
			String strSmId;
	        List<DeductHistory> iDeductHistory=null;

	        int nMsgType = ratingMsg.getM_nMsgType();
	        if( nMsgType == RatingMacro.CODE_ISMP_BRR ) // VAC,ISMP补款
	            strSmId = ratingMsg.getBaseMsg().getM_strISMPId();
	        else if( nMsgType == RatingMacro.CODE_SMS_BRR ) //短信补款
	            strSmId = ratingMsg.getBaseMsg().getM_strSmId();
	        else
	            return ErrorInfo.ERR_NOT_FOUND_SMS_ID;

	        if( strSmId.isEmpty() )
	            return ErrorInfo.ERR_NOT_FOUND_SMS_ID;
	        
	        iDeductHistory = getDeductHistory( strSmId );
	        if( null==iDeductHistory  ){
	        	return ErrorInfo.ERR_NOT_FOUND_DEDUCT_HISTORY;
	        }

	        boolean bHasRecord = false;
	        boolean bHasBacked = false;
	        String strAreaCode="";
	        String strServiceNbr="";
	        String strRatableInfo="";
	        for( DeductHistory history : iDeductHistory){
	            if( history.getM_nMsgType() != nMsgType )
	                continue;
	            if( history.getM_nBackCount() > 0 ){
	                bHasBacked = true;
	                continue;
	            }

	            bHasRecord = true;
	            strAreaCode = history.getM_strAreaCode();
	            strServiceNbr = history.getM_strServiceNbr();
	            strRatableInfo = history.getM_strRatableInfo();

	            BalanceContent iBalanceContent=new BalanceContent();

	            iBalanceContent.setLnAcctItemTypeId(history.getM_lnAcctItemTypeId());
	            iBalanceContent.setnUnitTypeId(history.getM_nUnitTypeId());
	            iBalanceContent.setLnAmount(history.getM_lnAmount());

	            //按MsgId进行补款的时候,补到对应的账本上去
	            iBalanceContent.setLnAcctBalanceId(history.getM_lnAcctBalanceId());
	            iBalanceContent.setnLatnId(history.getM_nLatnId());
	            iBalanceContent.setnIsCredit(history.getM_nIsCredit());

	            ratingData.getiReqFees().add(iBalanceContent);
	        }
	        int nRet = 0;
	        if( bHasRecord ){
//	            int nRet = 0;
	            //如果查询成功,则替换计费号码
	            ratingMsg.getM_iExtMsg().setM_strBillingAreaCode(strAreaCode);
	            ratingMsg.getM_iExtMsg().setM_strBillingNbr(strServiceNbr);
	            ratingMsg.getBaseMsg().setM_strChargedHomeAreaCode(strAreaCode);
	            ratingMsg.getBaseMsg().setM_strChargedNbr(strServiceNbr);

	            //回滚累计量
	            nRet = rollbackRatable( strRatableInfo );
	            if ( nRet < 0 )
	                return nRet;

	            //更新状态
	            nRet = updateState( strSmId );
	            if ( nRet < 0 )
	                return nRet;
	        }else{
	        	if( bHasBacked ){

	                return ErrorInfo.ERR_BUSINESS_RECORD_HAS_REFUND;
	            }

	            return ErrorInfo.ERR_SQL_QUERY_NOT_FOUND_DEDUCT_HISTORY;
	        }
	        
	        if( isQueryBackSuccess() ){
	            nRet = setBackBalance();
	            if( nRet < 0 )
	                return nRet;
	        }
	        
		}
		return 0;
	}
	
	
	
	
public int updateState(String smId){
		
		return 0;
	}




public int rollbackRatable(String ratableInfo){
		
		return 0;
	}



	
	/**
	 * 
	 * @author sung
	 *
	 * @param smId
	 * @return
	 */
	public List<DeductHistory> getDeductHistory(String smId){
		List<DeductHistory> deductHistory=new ArrayList<DeductHistory>();
		DbUtil db=new DbUtilImpl();
		List<TbDeductRecordHistory> his=null;
		his=db.getDeductHistoryBySmid(smId);
		if(null==his){
			return null;
		}
		for(TbDeductRecordHistory history:his){
			DeductHistory dh=new DeductHistory();
			dh.initDeductHistory(history);
			deductHistory.add(dh);
		}
		return deductHistory;
	}
	
	
	
	
	/**
	 * 判断查询扣费历史是否成功,如果成功就不批价
	 * @author sung
	 *
	 * @return
	 */
	public boolean isQueryBackSuccess(){
		return !ratingData.getiReqFees().isEmpty();
	}
	
	
	//待实现
			public int queryBalanceABM(){
				
				return 0;
			}
			
			
			/**
			 * 
			 * @author sung
			 *
			 * @return
			 */
			public int setBackBalance(){
				if( ratingMsg.getBaseMsg().getM_nFeeType()==RatingMacro.BACK_FEE_CMD ){
			        if( ratingData.getiReqFees().isEmpty() ){
			            //如果按扣费记录历史补款,vReq_fee是空,所以日志级别改为MY_WARNING
			            return ErrorInfo.DC_BERR_BALANCE_BACK_NULL;
			        }

			        //ISMP直接扣费请求需要先批价,保存批价结果时清空金钱请求信息
			        ratingMsg.getM_iBalanceInMsg().getLtBack().clear();

			        for( BalanceContent b :ratingData.getiReqFees()){
			            ratingMsg.getM_iBalanceInMsg().getLtBack().add(b);
			        }
			    }
				
				return 0;
			}		
			
			/**
			 * 设置实扣返回消息
			 * @author sung
			 *
			 */
			
			public void setDeductMessage(){
				System.out.println("设置实扣返回消息>>>>>>>>>>");
				int nBillingFlag = ratingMsg.getM_nBillingFlag();
				
				if(ratingMsg.getM_nMsgVersion()==2 && nBillingFlag==RatingMacro.SESSION_UPDATE){
					//取B30组
					for(BalanceContent b :ratingMsg.getM_iBalanceInMsg().getLtUsed()){
						ratingMsg.getM_iBalanceInMsg().getLtDeduct().add(b);
						long money=ratingData.getiCurrSessionValue().getUsedMoney();
						money+=b.getLnAmount();
						ratingData.getiCurrSessionValue().setUsedMoney(money);
						
					}
				}else{
					//取B03组
					for(BalanceContent bc :ratingMsg.getM_iBalanceInMsg().getLtDeduct()){
						//减去session_information中的已经扣除的余额
						bc.setLnAmount((int)(bc.getLnAmount()-ratingData.getiCurrSessionValue().getUsedMoney()));
						if(bc.getLnAmount()==0)
							bc.setLnAmount(0);
					}
				}
				
			}
			
			
			
			/**
			 * 
			 * @author sung
			 *
			 * @return
			 */
			public int updateDirectMoneyRatable(){
				if ( ratingMsg.getM_nMsgType() == RatingMacro.CODE_BBR && ratingMsg.isRatingOk() )
			        return 0;

			    if (  ratingMsg.isDirectBalance() ){

			        ChargeUnit iChargeUnit;
			        ratingData.getiChargeUnitsForRatable().clear();
			        if ( isQueryBackSuccess() && ratingMsg.isNeedQueryDeductHis() )
			            return 0;

			        int nRet = getPricingPlanId();
			        if ( nRet < 0 )
			            return nRet;

			        //补款
			        for( BalanceContent iter : ratingMsg.getM_iBalanceInMsg().getLtBack() ){
			            iChargeUnit=new ChargeUnit();
			            iChargeUnit.setLnAcctItemTypeId(iter.getLnAcctItemTypeId());
			            iChargeUnit.setnUnitType(iter.getnUnitTypeId());
			            iChargeUnit.setLnOriCharge(iter.getLnAmount());
			            iChargeUnit.setLnCharge(iter.getLnAmount());
			            ratingData.getiChargeUnitsForRatable().add(iChargeUnit);
			        }
			        //扣款
			        for( BalanceContent iter : ratingMsg.getM_iBalanceInMsg().getLtDeduct() ){
			            iChargeUnit=new ChargeUnit();
			            iChargeUnit.setLnAcctItemTypeId(iter.getLnAcctItemTypeId());
			            iChargeUnit.setnUnitType(iter.getnUnitTypeId());
			            iChargeUnit.setLnOriCharge(iter.getLnAmount());
			            iChargeUnit.setLnCharge(iter.getLnAmount());
			            ratingData.getiChargeUnitsForRatable().add(iChargeUnit);
			        }
			        ratingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(ratingMsg.getVarMsg().getM_strCurrTime());

			        nRet = updateRatableResources();

			        return nRet;
			    }

			    return 0;
				
			}
			
			
			/**
			 * 
			 * @author sung
			 *
			 * @return
			 */
			public int updateRatableResources(){
				int nRet = 0;
			    boolean bHave = false;
			    boolean bNetEndFlag = false;
			    boolean bIsFirstMonth1 = false;
			    boolean bIsFirstMonth2 = false;
			    List<String> iTempRatables=new ArrayList<String>();
			    String strResourceCode;
			    String szBuf = ratingMsg.getBaseMsg().getM_strStartTime();
			    String strBillingNbr;
			    long lnSessionTotalCharge = 0;
			    long lnDuration = 0;

			    RatableCondInParam iCondInParam=new RatableCondInParam();
				
			    int nMsgType = ratingMsg.getM_nMsgType();
//			    int nMsgVersion = ratingMsg.->getMsgVersion();
			    int nBillingFlag = ratingMsg.getM_nBillingFlag();

			    int nMoneyType;
			    int nLifeType = 1;
			    int nRefOffset = 0;
			    int nStartValue = -1;
			    int nEndValue = -1;
			    int nRatableResourceType;
			    long lnRatableValue = 0;
			    long lnRatableTotal=0;
			    boolean bIsMoney = false;
			    String strRefType;
			    String strRatableOwnerType;
			    int nRatableOwnerLandId;
			    long lnRatableOwnerId;

			    String strYearMonth;
			    String strACT="";

			    long lnRealDuration = ratingData.getLnReqDuration();
			    long lnRealTimes = ratingData.getLnReqTimes();
			    long lnRealUpVolume = ratingData.getLnReqUpVolume();
			    long lnRealDownVolume = ratingData.getLnReqDownVolume();
			    long lnRealTotalVolume = ratingData.getLnReqTotalVolume();
			    List<RequestRatableUpdate> iRequestRatableUpdates=new ArrayList<RequestRatableUpdate>();
				
			    iCondInParam.setM_pRatingData(ratingData);
				iCondInParam.setM_pRatingMsg(ratingMsg);
				iCondInParam.setM_szYYYY(szBuf.substring(0,4));
				iCondInParam.setM_szMM(szBuf.substring(4,6));
				iCondInParam.setM_szDD(szBuf.substring(6,8));
				iCondInParam.setM_szHH(szBuf.substring(8,10));
				iCondInParam.setM_szMI(szBuf.substring(10,12));
				
				//是否上网结束的业务请求消息
				if(nBillingFlag==RatingMacro.SESSION_END){
					bNetEndFlag = true;
				}
			    
				if(ratingMsg.getM_iServInfo().getStrAcceptDate().equals(ratingMsg.getVarMsg().getM_strCurrTime())){
					bIsFirstMonth1 = true;
					
				}
				
				if(ratingMsg.getM_iServInfo().getStrBasicState().equals("F0A")){
					bIsFirstMonth1 = true;
				}
				
				strBillingNbr = ratingMsg.getM_iExtMsg().getM_strBillingNbr();
				//(集团短信提醒)获取销售ID段落对应关系
				
				ratingData.setiChargeUnitsForRatable(ratingData.getiRealChargeUnits());

			    for( ChargeUnit iter : ratingData.getiChargeUnitsForRatable() ){
			        if( iter.getnRatableFlag() == 1 ) //累计优惠后费用
			            lnSessionTotalCharge += iter.getLnOriCharge();
			        else
			            lnSessionTotalCharge += iter.getLnCharge(); //确定哪个费用
			    }
			    lnSessionTotalCharge += ratingData.getiPrevSessionValue().getUnUsedMoney();

			    lnDuration += lnRealDuration + ratingData.getiPrevSessionValue().getUsedDuration();

			    List<String> iUsedRatables=new ArrayList<String>();
			    List<String> iUnUsedRatables = new ArrayList<String>();
				
			    for(PlanDisct   plan :ratingData.getiPlanDiscts()){
			    	iUsedRatables.clear();
			        String code = getRatableResourceCode( nMsgType, plan.getnAtomOfrId());
			        iUsedRatables.add(code);
			        if( nRet < 0 )
			            continue;
			        if( iUsedRatables.isEmpty() )
			            continue;

			        ratingMsg.getM_iUserMsg().setLnOfrInstId(plan.getLnOfrInstId());
			        ratingMsg.getM_iUserMsg().setnOfrInstLatnId(plan.getnOfrInstLatnId());
			        
			        //根据销售品订购的生效时间判断是否激活首月
			        if( plan.getStrEffDate().equals(ratingMsg.getVarMsg().getM_strCurrTime()) )
			            bIsFirstMonth2 = true;
			        else
			            bIsFirstMonth2 = false;
			        if( ratingMsg.getM_iServInfo().getStrBasicState().equals("F0A")) //状态为F0A一定是激活首月
			            bIsFirstMonth2 = true;
			        
			        for(String str :iUsedRatables){
			        	strResourceCode = str;
			        	nLifeType = 1;
			        	strRefType = "R0C";
			            nRefOffset = 0;
			            nStartValue = -1;
			            nEndValue = -1;
			            lnRatableValue = 0;
			            lnRatableTotal=0;
			            bIsMoney = false;
			            szBuf="";
			            CodeRatableResource ratableResource=getRatableAttr(strResourceCode);
			            if(null==ratableResource){
			            	continue;
			            }
			        	nLifeType=ratableResource.getLife_type();
			        	strRefType=ratableResource.getRef_type();
			        	nRefOffset=ratableResource.getRef_offset();
			        	nRatableResourceType=ratableResource.getRatable_resource_type();
			        	nStartValue=ratableResource.getStart_value();
			        	nEndValue=ratableResource.getEnd_value();
			        	strRatableOwnerType=ratableResource.getOwner_type();
			        	
			        	bIsMoney = isMoneyRatableType( nRatableResourceType );
			        	strYearMonth = DateUtil.getYearMonth( plan.getStrEffDate(), ratingMsg.getM_iRatingExtMsg().getM_strExtCurrTime(), nLifeType, nStartValue, nEndValue);
			        	if(strYearMonth.isEmpty()){
			        		continue;
			        	}
			        	
			        	if( strRatableOwnerType == "80C" ){
			                lnRatableOwnerId = plan.getLnOfrInstId();///    待确认
			                nRatableOwnerLandId = plan.getnOfrInstLatnId();
			            }else{
			            	boolean find=false;
			                for( String strRatable : iTempRatables){
			                    if( strRatable.equals(strResourceCode) ){
			                    	find=true;
			                        break;
			                    }
			                }
			                if( !find ){
			                    iTempRatables.add( strResourceCode );
			                }
			                else
			                    continue;

			                nRatableOwnerLandId=getRatableLatnId( strRatableOwnerType );
			                lnRatableOwnerId=Long.parseLong(getRatableOwnerId(strRatableOwnerType));
			                
			            }
			        	bHave = true;
			            if( (lnRatableTotal=getRatableResourceValue( strResourceCode, ratingData.getiRatableResourceValues() )) ==ErrorInfo.ERR_NOT_FOUND_RATABLE_VALUE ){
			                bHave = false;
			                lnRatableTotal = 0;
			            }
			        	long lnTempRatableValue =0 ;
			        	lnTempRatableValue = getRatableValue( nRatableResourceType, lnRealDuration, lnRealTimes, lnRealUpVolume, lnRealDownVolume, lnRealTotalVolume );
			        	nMoneyType=getMoneyTypeFromRatableType( nRatableResourceType );
			        	for(ChargeUnit   unit :ratingData.getiChargeUnitsForRatable()){
			        		
			        		if( (strACT=ratingMsg.getValue( "ACT", 1 )).isEmpty() )
//			                    m_ratingMsg->addValue( "ACT", itCharge->m_lnAcctItemTypeId );     RatingMsg未实现
//			                else
//			                    m_ratingMsg->setValue( "ACT", itCharge->m_lnAcctItemTypeId );
			        		
			        		iCondInParam.setM_nPricingSectionId(unit.getLnPricingSectionId());
			                iCondInParam.setM_nPlanIdA(unit.getnPlanId());
			                iCondInParam.setM_nPlanIdB(plan.getnPricingPlanId());
			                iCondInParam.setM_lnOfrInstIdA(unit.getLnOfrInstId());
			                iCondInParam.setM_lnOfrInstIdB(plan.getLnOfrInstId());
			                
			                if( m_pRatableCondCheck.check( strResourceCode, iCondInParam ) < 0 )
			                    continue;
			                
			                
			              //非金钱的,如果满足条件,则不需要继续判断,因为非金钱不是根据批价结果来累积,
			                //而是根据批价的申请量进行累计
			                if( !bIsMoney )
			                {
			                    int nTmpRatableResourceType = getRatableResourceUnit( unit.getStrRatableCode() );
			                    if( nTmpRatableResourceType == ErrorInfo.ERR_NOT_FOUND_RATABLE_CODE )
			                    {
			                        ///Log
			                    }
			                    if( nTmpRatableResourceType == nRatableResourceType )
			                    {
			                        if( nBillingFlag == RatingMacro.EVENT_BACK )
			                            lnRatableValue -= calcRatableResourceValue( nRatableResourceType, unit.getLnBeforeDosage() );
			                        else
			                            lnRatableValue += calcRatableResourceValue( nRatableResourceType, unit.getLnBeforeDosage() );
			                    }
			                    else
			                    {
			                        lnRatableValue = lnTempRatableValue;
			                        break;
			                    }

			                    continue;
			                }
			                if( unit.getnRatableFlag() == 1 ){
			                    if( unit.getLnOriCharge() <= 0 )
			                        continue;
			                }else{
			                    if( unit.getLnCharge() <= 0 )
			                        continue;
			                }
			                
			                if( bIsMoney && unit.getnUnitType() == nMoneyType ){
			                    if( unit.getnRatableFlag()== 1 )
			                        lnRatableValue += unit.getLnOriCharge();
			                    else{
			                        if( unit.getLnLeftMoney() > 0 )
			                            lnRatableValue += unit.getLnOriCharge();
			                        else
			                            lnRatableValue += unit.getLnCharge();
			                    }
			                }
			                
			                
			        	}
			        	if( bIsMoney ){
			                //如果是事件返还的话,需要增加负值
			                if( nBillingFlag == RatingMacro.EVENT_BACK && lnRatableValue > 0 )
			                    lnRatableValue = -lnRatableValue;
			                if( nBillingFlag != RatingMacro.EVENT_BACK && lnRatableValue <= 0 )
			                    continue;
			            }else{
			                if( nBillingFlag == RatingMacro.EVENT_BACK && lnRatableValue > 0 )
			                    lnRatableValue = -lnRatableValue;
			                if( ( lnRatableValue <= 0 && nBillingFlag != RatingMacro.EVENT_BACK ) || ( lnRatableValue == 0 && nBillingFlag == RatingMacro.EVENT_BACK ) )
			                    continue;
			            }
			        	RequestRatableUpdate iRequestRatableUpdate=new RequestRatableUpdate();
			            Ratable iRatable=new Ratable();

			            iRequestRatableUpdate.setM_strOwnerType(strRatableOwnerType);
			            iRequestRatableUpdate.setM_lnOwnerId(lnRatableOwnerId);
			            iRequestRatableUpdate.setM_nLatnId(nRatableOwnerLandId);
			            iRequestRatableUpdate.setM_strRatableResourceCode(strResourceCode);
			            iRequestRatableUpdate.setM_lnRatableCycleId(Long.parseLong(strYearMonth));
			            iRequestRatableUpdate.setM_lnBalance(lnRatableValue);

			            iRequestRatableUpdates.add( iRequestRatableUpdate );

			            lnRatableTotal += lnRatableValue;

			            iRatable.setStrRatableCode(strResourceCode);
			            iRatable.setnRatableType(nRatableResourceType);
			            iRatable.setLnValue(lnRatableValue);
			            iRatable.setLnRatableValue(lnRatableTotal);
			            iRatable.setStrAcctDate(strYearMonth);
			            iRatable.setLnOwnerID(lnRatableOwnerId);
			            iRatable.setnOwnerLatnId(nRatableOwnerLandId);
			            iRatable.setStrOwnerType(strRatableOwnerType);

			            ratingData.getiRatableRefs().add(iRatable);
			            //更新内存值
			            updateRatableValueInMem( strResourceCode, lnRatableValue );
			        	
			            //自动调档EasyPlus套餐的短信提醒功能----略
			            
			        }
			    }
			    RatableUpdateReq iRatableReq = new RatableUpdateReq();
			    RatableUpdateResp iRatableResp = new RatableUpdateResp();

			    iRatableReq.setM_strSessionId(ratingMsg.getBaseMsg().getM_strSessionId());
			    iRatableReq.setM_strServiceContextId("");
			    iRatableReq.setM_ulnServId(Long.parseLong(ratingMsg.getM_iUserMsg().getLnServId()));
			    iRatableReq.setM_nLatnId(ratingMsg.getM_iUserMsg().getnLatnId());
			    iRatableReq.setM_iRequestRatableUpdates(iRequestRatableUpdates);

//			    ratingData->m_iABMTrack.start();
//			    nRet = m_pABMClient->updateRatableResources( iRatableReq, &iRatableResp );
//			    m_ratingData->m_iABMTrack.stop();
				
			    
				return 0;
			}
	
			
			
			
			private int updateRatableValueInMem(String resourceCode,long ratableValue){
				RatableResourceValue value=ratingData.getiRatableResourceValues().get(resourceCode);
				if(value==null){
					return -1;
				}
				
				value.setM_lnBalance(value.getM_lnBalance()+(int)ratableValue);
				ratingData.getiRatableResourceValues().put(resourceCode, value);
				return 0;
			}
			
			private long calcRatableResourceValue(int ratableResourceType, long dosage){
				
				
				
				return 0;
			}
			
			
			/**
			 * 
			 * @author sung
			 *
			 * @param resourceCode
			 * @return
			 */
			private int getRatableResourceUnit(String resourceCode){
				RatableResourceInfo iRatableInfo;
				iRatableInfo=ratingData.getiRatableResourceInfos().get(resourceCode);
				if( iRatableInfo==null ){
			        return ErrorInfo.ERR_NOT_FOUND_RATABLE_CODE;
			    }
				return Integer.parseInt(iRatableInfo.getM_strRatableResourceType());
				
			}
			
			/**
			 * 
			 * @author sung
			 *
			 * @param resourceType
			 * @param moneyType
			 * @return
			 */
			
			public int getMoneyTypeFromRatableType(int resourceType ){
				int moneyType=0;
				switch(resourceType){
				case RatingMacro.RatableResourceType_Money :
					moneyType=2;
					break;
				case RatingMacro.RatableResourceType_M:
					moneyType=7;
					break;
				case RatingMacro.RatableResourceType_T:
					moneyType=8;
					break;
				}
				
				return moneyType;
				
			}
			/**
			 * 
			 * @author sung
			 *
			 * @param resourceType
			 * @param duration
			 * @param times
			 * @param upVolume
			 * @param downVolume
			 * @param totalVolume
			 * @return
			 */
			public long getRatableValue(int resourceType,long duration ,long times,long upVolume,long downVolume,long totalVolume){
				switch(resourceType){
				case 1:
					return duration;
				case 2:   //时长(分钟)
					return (duration+59)/60;   //duration单位是秒
				case 3:
					return times;
				case 4:
					return totalVolume;
				case 7:
					return upVolume;
				case 8:
					return downVolume;
				default:	//其他计算资源不考虑
					break;
				}
				
				return 0;
			}
			
			/**
			 * 
			 * @author sung
			 *
			 * @param resourceCode
			 * @param values
			 * @return
			 */
			public long getRatableResourceValue(String resourceCode,Map<String,RatableResourceValue> values){
				long resourceValue=-1;
				RatableResourceInfo info=null;
				info=ratingData.getiRatableResourceInfos().get(resourceCode);
				if( info==null ){
			        return ErrorInfo.ERR_NOT_FOUND_RATABLE_CODE;
			    }
				RatableResourceValue value=null;
				value=ratingData.getiRatableResourceValues().get(resourceCode);
				if(value==null){
					return ErrorInfo.ERR_NOT_FOUND_RATABLE_VALUE;
				}else{
					resourceValue=value.getM_lnBalance();
				}
				//如果事件补款,且携带累积量情况,我们对其累积量-1进行批价
				//必须按照次数来补款的,支持目前的短信和彩信业务
				if(ratingMsg.getM_nBillingFlag()==RatingMacro.EVENT_BACK && resourceValue >0 ){
					resourceValue=-1;
				}
				return resourceValue;
			}
			
			
			
			
			/**
			 * 
			 * @author sung
			 *
			 * @param ownerType
			 * @return
			 */
			public int getRatableLatnId(String ownerType){
				int latnId = -1;
			    if( ownerType == "80I" || ownerType=="80J"){
			        latnId=ratingMsg.getM_iUserMsg().getnLatnId();
			        
			    }else if( ownerType == "80C" ) //如果有多个销售品实例,这个inst可能不准确
			    {
			        latnId=ratingMsg.getM_iUserMsg().getnOfrInstLatnId();
			    }
			    else
			    {
			    	latnId=ratingMsg.getM_iUserMsg().getnLatnId();
			    }
				return latnId;
			}
			
			/**
			 * 
			 * @author sung
			 *
			 * @param ownerType
			 * @return
			 */
			public String getRatableOwnerId(String ownerType){
			    String ownerValue = "";
			    if( ownerType == "80I" ){
			        ownerValue = ratingMsg.getM_iUserMsg().getLnCustId();
			    }
			    else if( ownerType == "80J" ){
			        ownerValue = ratingMsg.getM_iUserMsg().getLnAcctId();
			    }
			    else if( ownerType == "80C" ){ //如果有多个销售品实例,这个inst可能不准确
			        ownerValue = ""+ratingMsg.getM_iUserMsg().getLnOfrInstId();
			    }else{
			        ownerValue = ratingMsg.getM_iUserMsg().getLnServId();
			    }
				return ownerValue;
			}
			
			
			
			
			/**
			 * 
			 * @author sung
			 *
			 * @param resourceType
			 * @return
			 */
			public boolean isMoneyRatableType(int resourceType){
				
				if(resourceType==RatingMacro.RatableResourceType_Money || resourceType==RatingMacro.RatableResourceType_M
						|| resourceType==RatingMacro.RatableResourceType_T){
					return true;
				}
				return false;
			}
			
			
			/**
			 * 
			 * @author sung
			 *
			 * @param code
			 * @return
			 */
			public CodeRatableResource getRatableAttr(String code){
				CodeRatableResource resource=null;
				DbUtil db=new DbUtilImpl();
				resource=db.getRatableResourceAttr(code);
				
				return resource;
			}
			
			/**
			 * 
			 * @author sung
			 *
			 * @param msgType
			 * @param ofrId
			 * @return
			 */
			
			public String getRatableResourceCode(int msgType,int ofrId){
				String code="";
				DbUtil db=new DbUtilImpl();
				List<RuleOfrResourceRel> rels=db.getAllResourceRels();
				for(RuleOfrResourceRel rel :rels){
					if(rel.getMsg_Types().equals(""+msgType) && rel.getOfr_B_Id()==ofrId){
						code=rel.getResource_Code();
						break;
					}
				}
				return code;
			}
			
			
			
			public int getPricingPlanId(){
				
				return 0;
			}
			
			
			
			
			
}
