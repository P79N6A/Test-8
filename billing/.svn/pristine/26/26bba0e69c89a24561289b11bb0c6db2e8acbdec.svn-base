/**
 * 
 */
package com.tydic.beijing.billing.rating.service.impl;

import org.apache.log4j.Logger;

/**
 * @author sung
 *
 */
public class ChargingUpdateImpl  {//extends ChargingUpdateAbstract implements ChargingUpdate{

	private Logger log=Logger.getLogger(ChargingUpdateImpl.class);


	
	
	
//	
//	
//	public void init(RatingMsg msg,RatingData data){
//		this.ratingMsg=msg;
//		this.ratingData=data;
//		
//	}
//	
//	
//	public void beforeRate(){
//		
//		//初始化conf
//		
//	}
//	
//	
//	/**
//	 * @author sung
//	 *
//	 * @return
//	 */
//	@Override
//	public int ratingUpdate() {
////		//批价
////		System.out.println("==================实扣=======================");
////		RateData iRealRateData=null;
////		RateData iReqRateData=null;
////		getUpdateRateMeasure(ParamData.OPER_REAL);
////		ratingData.setnOper(ParamData.OPER_REAL);
////		ratingData.setiRateMeasure(ratingData.getiRealRateMeasure());
////		setReqRateMeasure();//消息中的使用量
////				
//////		int ret=calcFeeUpdate(iRealRateData);
////		
////		iRealRateData=calcFeeUpdate();
////		
//////		if(iRealRateData==null){
//////			System.out.println("%%%%%%%%%%%%%%%%%%%%calcFeeUpdate return null value");
//////		}
////		
////		loadUnusedData( iRealRateData, RatingMacro.OPER_REAL );
////		setRatingSpanPoint();
////		setRBMessage( ratingData.getnOper());
////		
////		
////		System.out.println("=================申请预占==================");
////		getUpdateRateMeasure(RatingMacro.OPER_REQ);
////				
////		ratingData.setnOper(RatingMacro.OPER_REQ);
////				
////		ratingData.setiRateMeasure(ratingData.getiReqRateMeasure());
////		setReqRateMeasure();
////				
////		iReqRateData=calcFeeUpdate();
////		//更新B07
////		for(ChargeUnit iter:ratingData.getiRealChargeUnits()){
////			if(iter.getnMTChangeFlag()==0){//MT转换的金钱不计入
////				updateFeeInfo(iter.getnUnitType(),iter.getnOfrId(),iter.getLnOriCharge());
////			}
////		}
////		setRBMessage(ratingData.getnOper());
////		try {
////			setMergeBalanceReqMsg();
////		} catch (RatingException e) {
////			log.debug(e.printError());
////		}
////		ChargeUpdateReq iChargeReq=new ChargeUpdateReq();
////		ChargeUpdateResp iChargeResp =new ChargeUpdateResp();
////		List<ChargeUpdateReq> iChargeReqs=new ArrayList<ChargeUpdateReq>();
////		List<ChargeUpdateResp> iChargeResps=new ArrayList<ChargeUpdateResp>();
////		balanceUpdate( iChargeReq, iChargeResp );
////		iChargeReqs.add(iChargeReq);
////		iChargeResps.add(iChargeResp);
////			////检查返回结果中账本扣除情况和错误码，确定是否成功全部扣除
////		if( iChargeResp.getM_unResultCode() != 0 ){
////			    	
////		}
//////			    int nFeeType = ratingData.getnFeeType();
//////			    if( nFeeType != RatingMacro.ONLY_REQ_FEE && nFeeType != RatingMacro.NO_OPER_FEE )
//////			        balanceAocManger();  //OCS2未实现--省略
////		int nRet;
////		if( ( nRet = queryBalance() ) < 0 )
////			   return nRet;
////		List<RespondBalanceUsed> iUsed=new ArrayList<RespondBalanceUsed>();
////			    
////		for(ChargeUpdateResp iter :iChargeResps){
////			 iUsed.addAll(iter.getM_iRespondBalanceUseds());
////		}
////			    
////			  /// --保存每次成功后的实扣信息
////		getDeductInfo( iUsed, ratingData.getiDeductInfos());
////			    
////			  //设置session_information_ext
////		if( ( nRet = dealSessionInfo() ) < 0 )
////			return nRet;
////			    
////			  //设置金钱类RB返回信息
////		if( ratingMsg.getM_iBalanceInMsg().isbIsMoneyRequest()
////			            || ratingMsg.isNeedCheckTopValue()
////			            || ratingMsg.isNeedCheckRepetition()
////			            || ratingMsg.getM_iBalanceInMsg().getnRealCtrlType()  == 5
////			      ){
////			        setRetMessage();
////		}
////			  //如果是小区优惠需要更新tb_prd_prd_inst_attr
////		if( ratingMsg.getBaseMsg().getM_nIsUpdateAttr() == 2 ){
////			    	
////		}else if(ratingMsg.getBaseMsg().getM_nIsUpdateAttr()==1){
////			    	
////		}	
//			
//		return 0;
//	}
//
//	
//	
//	
//	
//	public void setRetMessage(){
//		
//		//先清理原来的内容，以防重复
//	    //如果是批价包月剔重的情况，因为最终的结果可能和批价结果不一致
//	    //需要清理后重新设置
//		
//	    //取B03组
////		int nRet=0;
////		for(BalanceContent iter :ratingMsg.getM_iBalanceInMsg().getLtDeduct())
////	    {
////	        setRealFee( iter.getLnAcctItemTypeId(), iter.getnUnitTypeId(), iter.getLnAmount(), nRet );
////	    }
////
////	    //预占信息
////		for(BalanceContent iter :ratingMsg.getM_iBalanceInMsg().getLtReserve())
////	    {
////			setRealFee( iter.getLnAcctItemTypeId(), iter.getnUnitTypeId(), iter.getLnAmount(), nRet );
////	    }
////		
////	    //返还信息B04
////		for(BalanceContent iter :ratingMsg.getM_iBalanceInMsg().getLtBack())
////	    {
////			setRealFee( iter.getLnAcctItemTypeId(), iter.getnUnitTypeId(), iter.getLnAmount(), nRet );
////	    }
////		
//		
//
//		
//	}
//
//	
//	
//	
//	public void setRealFee( int lnFeeTag_,int nFeeType_, int lnFeeValue_, int lnFeeLeftValue ){
//		
////	    List<BalanceContent> temps = new ArrayList<BalanceContent>();
////		BalanceContent iBalanceContent = new BalanceContent();
////	    iBalanceContent.setLnAcctItemTypeId(lnFeeTag_);
////	    iBalanceContent.setnUnitTypeId(nFeeType_); 
////	    iBalanceContent.setLnAmount(lnFeeValue_); 
////	    iBalanceContent.setLnLeftMoney(lnFeeLeftValue);
////	    temps.add(iBalanceContent);
////	    ratingData.setiRealFees(temps);
//
//	}
//
//
//
//	
//	public int dealSessionInfo(){
////		 int nMsgVersion = ratingMsg.getM_nMsgVersion();
////		    int nBillingFlag = ratingMsg.getM_nBillingFlag();
////		    if( ratingMsg.getBaseMsg().getM_nFeeType() == RatingMacro.NO_FEE_CMD && ratingMsg.getM_iExtMsg().isM_bFreeFlag() ) //如果是余额释放,并且无批价信息
////		        nBillingFlag=RatingMacro.SESSION_END;//删掉
////
////		    if ( nMsgVersion != RatingMacro.MSG_VER_02 || nBillingFlag > RatingMacro.SESSION_END )
////		        return 0;
////
////
////		    //分别处理Initial、Update、Term消息
////		    int nRet = 0;
////		    switch( nBillingFlag )
////		    {
////		        case RatingMacro.SESSION_BEGIN:
////		            //会话开始，插入会话
////		            ratingData.getiDCMTrack().start(); 
////		            ratingData.getiSessionTrackC().start();
////		            
////		            
////		            //nRet = DCMClient::createSession( m_ratingMsg, m_ratingData );
////		            ratingData.getiSessionTrackC().stop();   
////		            ratingData.getiDCMTrack().stop(); 
////		            if ( nRet < 0 )
////		                return nRet;
////		            break;
////		        case RatingMacro.SESSION_UPDATE:
////		            //会话更新，修改会话
////		        	ratingData.getiDCMTrack().start(); 
////		        	ratingData.getiSessionTrackU().start();
////		            //nRet = DCMClient::updateSession( m_ratingMsg, m_ratingData );
////		        	ratingData.getiSessionTrackU().stop();   
////		            ratingData.getiDCMTrack().stop(); 
////		            if ( nRet < 0 )
////		                return nRet;
////		            break;
////		        case RatingMacro.SESSION_END:
////		            //会话结束，删除会话
////		        	ratingData.getiDCMTrack().start(); 
////		            ratingData.getiSessionTrackD().start();
////		            //nRet = DCMClient::deleteSession( m_ratingMsg->m_iBaseMsg.m_strSessionId );
////		            ratingData.getiSessionTrackD().stop();   
////		            ratingData.getiDCMTrack().stop(); 
////		            if ( nRet < 0 )
////		                return nRet;
////		            break;
////		        default:
////		            break;
////		    }
//		
//		
//		return 0;
//	}
//	
//	
//	
//	public int getDeductInfo(List<RespondBalanceUsed> useds,List<DeductInfo> deductInfos){
//		
//		return 0;
//	}
//	
//
//
//	
//	public int queryBalance(){
//		
//		return 0;
//	}
//	
//
//
//	
//	
//	public int balanceUpdate(ChargeUpdateReq chargeReq,ChargeUpdateResp chargeResp){
////		int nRet = 0;
////
////	    nRet = prepareForBalance();
////
////	    if( nRet < 0 )
////	        return nRet;
////	    
////	    if( !ratingMsg.isNeedRating() ){
////	        if( ( nRet = getSession() ) < 0 )
////	            return nRet;
////	        
////	        ratingData.getiPrevSessionValue().print();
////	        
////	        //直接扣费类的补款时扣费历史记录表查询
////	        if( ( nRet = getDeductHistory() ) < 0 )
////	            return nRet;
////
////	        if( ( nRet = queryBalanceABM() ) < 0 )
////	            return nRet;
////	        
////	    }
////	    //封顶值检查
////	    if( ratingMsg.isNeedCheckTopValue()){	//ratingMsg.iBalanceInMsg.nRealCtrlType=-1 ==1?
////	        if( ( nRet = checkTopValue() ) < 0 )
////	            return nRet;
////	    }
////	    
////	    //剔重
////	    if( ratingMsg.isNeedCheckRepetition() || isFmtCheckRepetition() ){   // ==2?
////	        nRet = checkRepetition();
////	        if( nRet < 0 )
////	            return nRet;
////	        if( nRet == 1 )
////	            setDeductZero();
////	        else if( nRet == 0 )
////	            setRepetition();
////	    }
////
////	    
////	    //封顶值检查补款//在线模块必须是ISMP业务,离线模块必须是20业务
////	    if( ratingMsg.getM_nBillingFlag() == RatingMacro.EVENT_BACK ){
////	        nRet = backTopRepetition();
////	        if( nRet < 0 )
////	            return nRet;
////	    }
////	    
////	    //设置update时的B30组扣费信息
////	    
////	    if( ratingMsg.getM_iBalanceInMsg().isbIsMoneyRequest() )	//金钱类型请求
////	        setDeductMessage();
////	    
//////	    System.out.println("ltduct>>>size>>>>"+ratingMsg.getM_iBalanceInMsg().getLtDeduct().size());
////	    
////	    //直接扣费更新累积量
////	    if(conf.getDirectRatableFlag()==1){
////	    	if( ( nRet = updateDirectMoneyRatable() ) < 0 )
////	            return nRet;
////	    }
////	  //余额充值
////	    if( ( nRet = balanceRecharge() ) < 0 )
////	    {
////	        return nRet;
////	    }
////
////	    //余额返还
////	    if( ( nRet = balanceBack() ) < 0 )
////	    {
////	        return nRet;
////	    }
////
////	    //余额释放
////	    if( ( nRet = balanceFree() ) < 0 )
////	        return nRet;
////
////	    //余额预占
////	    nRet = balanceChargeUpdate( chargeReq, chargeResp );
////	    if( nRet < 0 )
////	        return nRet;
////	    
////	    
////	    
////	    
//	    
//		return 0;
//	}
//	
//	
//	
//	
//	
//	public int balanceChargeUpdate(ChargeUpdateReq   req ,ChargeUpdateResp  resp){
////		req.setM_strSessionId(ratingMsg.getBaseMsg().getM_strSessionId());
////	    req.setM_ulnServId(Long.parseLong(ratingMsg.getM_iUserMsg().getLnServId()));
////	    req.setM_nLatnId(ratingMsg.getM_iUserMsg().getnLatnId());
////	    req.setM_strEventTimeStamp(""+ratingMsg.getEventtime());
////
////
////	    for( BalanceContent iter : ratingMsg.getM_iBalanceInMsg().getLtReserve()){
////	        if( iter.getLnAmount() < 0 )
////	            continue;
////
////	        RequestAcctItem iRequestAcctItem;
//////	        if( ( nRet = getRequestAcctItemForABM( *iter, &iRequestAcctItem ) ) < 0 )
//////	        {
//////	            m_pRatingData->setResultCode( ERR_DEDUCT_UPDATE_FREE_BALANCE );
//////	            return nRet;
//////	        }
//////
//////	        pChargeReq_->m_iRequestAcctItems.push_back( iRequestAcctItem );
////	    }
////
//////	    if( bSendReal_ )
//////	    {
//////	        LOG_DEBUG( log_ )<<__FILE__<<":"<<__LINE__<<" - m_ltDeduct.size() ["<<m_pRatingMsg->m_iBalanceInMsg.m_ltDeduct.size()<<"]"<<endl;
//////
//////	        for( std::vector<BalanceContent>::iterator iter=m_pRatingMsg->m_iBalanceInMsg.m_ltDeduct.begin();
//////	                iter!=m_pRatingMsg->m_iBalanceInMsg.m_ltDeduct.end(); ++iter )
//////	        {
//////	            if( iter->m_lnAmount < 0 )
//////	                continue;
//////
//////	            struct UsedAcctItem iUsedAcctItem;
//////	            if( ( nRet = getUsedAcctItemForABM( *iter, &iUsedAcctItem ) ) < 0 )
//////	            {
//////	                m_pRatingData->setResultCode( ERR_DEDUCT_UPDATE_FREE_BALANCE );
//////	                return nRet;
//////	            }
//////
//////	            pChargeReq_->m_iUsedAcctItems.push_back( iUsedAcctItem );
//////	        }
//////	    }
//////
//////	    getRequestRatablesForABM( pChargeReq_->m_iRequestRatableUpdates );
//////
//////	    m_pRatingData->m_iABMTrack.start();
//////	    nRet = m_pABMClient->chargeUpdate( *pChargeReq_, iChargeResp_ );
//////	    m_pRatingData->m_iABMTrack.stop();
//////	    if( nRet < 0 )
//////	        return ERR_DEDUCT_UPDATE_FREE_BALANCE;
////
////	    return 0;
////		
//	}
//	
//	
//	public int balanceFree(){
//		
//		
//		return 0;
//	}
//	
//	public int balanceBack(){
//		
//		
//		return 0;
//	}
//	
//	public int balanceRecharge(){
//		
//		
//		return 0;
//	}
//	
//	
//	
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int updateDirectMoneyRatable(){
//		if ( ratingMsg.getM_nMsgType() == RatingMacro.CODE_BBR && ratingMsg.isRatingOk() )
//	        return 0;
//
//	    if (  ratingMsg.isDirectBalance() ){
//
//	        ChargeUnit iChargeUnit;
//	        ratingData.getiChargeUnitsForRatable().clear();
//	        if ( isQueryBackSuccess() && ratingMsg.isNeedQueryDeductHis() )
//	            return 0;
//
//	        int nRet = getPricingPlanId();
//	        if ( nRet < 0 )
//	            return nRet;
//
//	        //补款
//	        for( BalanceContent iter : ratingMsg.getM_iBalanceInMsg().getLtBack() ){
//	            iChargeUnit=new ChargeUnit();
//	            iChargeUnit.setLnAcctItemTypeId(iter.getLnAcctItemTypeId());
//	            iChargeUnit.setnUnitType(iter.getnUnitTypeId());
//	            iChargeUnit.setLnOriCharge(iter.getLnAmount());
//	            iChargeUnit.setLnCharge(iter.getLnAmount());
//	            ratingData.getiChargeUnitsForRatable().add(iChargeUnit);
//	        }
//	        //扣款
//	        for( BalanceContent iter : ratingMsg.getM_iBalanceInMsg().getLtDeduct() ){
//	            iChargeUnit=new ChargeUnit();
//	            iChargeUnit.setLnAcctItemTypeId(iter.getLnAcctItemTypeId());
//	            iChargeUnit.setnUnitType(iter.getnUnitTypeId());
//	            iChargeUnit.setLnOriCharge(iter.getLnAmount());
//	            iChargeUnit.setLnCharge(iter.getLnAmount());
//	            ratingData.getiChargeUnitsForRatable().add(iChargeUnit);
//	        }
//	        ratingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(ratingMsg.getVarMsg().getM_strCurrTime());
//
//	        nRet = updateRatableResources();
//
//	        return nRet;
//	    }
//
//	    return 0;
//		
//	}
//	
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int updateRatableResources(){
//		int nRet = 0;
//	    boolean bHave = false;
//	    boolean bNetEndFlag = false;
//	    boolean bIsFirstMonth1 = false;
//	    boolean bIsFirstMonth2 = false;
//	    List<String> iTempRatables=new ArrayList<String>();
//	    String strResourceCode;
//	    String szBuf = ratingMsg.getBaseMsg().getM_strStartTime();
//	    String strBillingNbr;
//	    long lnSessionTotalCharge = 0;
//	    long lnDuration = 0;
//
//	    RatableCondInParam iCondInParam=new RatableCondInParam();
//		
//	    int nMsgType = ratingMsg.getM_nMsgType();
////	    int nMsgVersion = ratingMsg.->getMsgVersion();
//	    int nBillingFlag = ratingMsg.getM_nBillingFlag();
//
//	    int nMoneyType;
//	    int nLifeType = 1;
//	    int nRefOffset = 0;
//	    int nStartValue = -1;
//	    int nEndValue = -1;
//	    int nRatableResourceType;
//	    long lnRatableValue = 0;
//	    long lnRatableTotal=0;
//	    boolean bIsMoney = false;
//	    String strRefType;
//	    String strRatableOwnerType;
//	    int nRatableOwnerLandId;
//	    long lnRatableOwnerId;
//
//	    String strYearMonth;
//	    String strACT="";
//
//	    long lnRealDuration = ratingData.getLnReqDuration();
//	    long lnRealTimes = ratingData.getLnReqTimes();
//	    long lnRealUpVolume = ratingData.getLnReqUpVolume();
//	    long lnRealDownVolume = ratingData.getLnReqDownVolume();
//	    long lnRealTotalVolume = ratingData.getLnReqTotalVolume();
//	    List<RequestRatableUpdate> iRequestRatableUpdates=new ArrayList<RequestRatableUpdate>();
//		
//	    iCondInParam.setM_pRatingData(ratingData);
//		iCondInParam.setM_pRatingMsg(ratingMsg);
//		iCondInParam.setM_szYYYY(szBuf.substring(0,4));
//		iCondInParam.setM_szMM(szBuf.substring(4,6));
//		iCondInParam.setM_szDD(szBuf.substring(6,8));
//		iCondInParam.setM_szHH(szBuf.substring(8,10));
//		iCondInParam.setM_szMI(szBuf.substring(10,12));
//		
//		//是否上网结束的业务请求消息
//		if(nBillingFlag==RatingMacro.SESSION_END){
//			bNetEndFlag = true;
//		}
//	    
//		if(ratingMsg.getM_iServInfo().getStrAcceptDate().equals(ratingMsg.getVarMsg().getM_strCurrTime())){
//			bIsFirstMonth1 = true;
//			
//		}
//		
//		if(ratingMsg.getM_iServInfo().getStrBasicState().equals("F0A")){
//			bIsFirstMonth1 = true;
//		}
//		
//		strBillingNbr = ratingMsg.getM_iExtMsg().getM_strBillingNbr();
//		//(集团短信提醒)获取销售ID段落对应关系
//		
//		ratingData.setiChargeUnitsForRatable(ratingData.getiRealChargeUnits());
//
//	    for( ChargeUnit iter : ratingData.getiChargeUnitsForRatable() ){
//	        if( iter.getnRatableFlag() == 1 ) //累计优惠后费用
//	            lnSessionTotalCharge += iter.getLnOriCharge();
//	        else
//	            lnSessionTotalCharge += iter.getLnCharge(); //确定哪个费用
//	    }
//	    lnSessionTotalCharge += ratingData.getiPrevSessionValue().getUnUsedMoney();
//
//	    lnDuration += lnRealDuration + ratingData.getiPrevSessionValue().getUsedDuration();
//
//	    List<String> iUsedRatables=new ArrayList<String>();
//	    List<String> iUnUsedRatables = new ArrayList<String>();
//		
//	    for(PlanDisct   plan :ratingData.getiPlanDiscts()){
//	    	iUsedRatables.clear();
//	        String code = getRatableResourceCode( nMsgType, plan.getnAtomOfrId());
//	        iUsedRatables.add(code);
//	        if( nRet < 0 )
//	            continue;
//	        if( iUsedRatables.isEmpty() )
//	            continue;
//
//	        ratingMsg.getM_iUserMsg().setLnOfrInstId(plan.getLnOfrInstId());
//	        ratingMsg.getM_iUserMsg().setnOfrInstLatnId(plan.getnOfrInstLatnId());
//	        
//	        //根据销售品订购的生效时间判断是否激活首月
//	        if( plan.getStrEffDate().equals(ratingMsg.getVarMsg().getM_strCurrTime()) )
//	            bIsFirstMonth2 = true;
//	        else
//	            bIsFirstMonth2 = false;
//	        if( ratingMsg.getM_iServInfo().getStrBasicState().equals("F0A")) //状态为F0A一定是激活首月
//	            bIsFirstMonth2 = true;
//	        
//	        for(String str :iUsedRatables){
//	        	strResourceCode = str;
//	        	nLifeType = 1;
//	        	strRefType = "R0C";
//	            nRefOffset = 0;
//	            nStartValue = -1;
//	            nEndValue = -1;
//	            lnRatableValue = 0;
//	            lnRatableTotal=0;
//	            bIsMoney = false;
//	            szBuf="";
//	            CodeRatableResource ratableResource=getRatableAttr(strResourceCode);
//	            if(null==ratableResource){
//	            	continue;
//	            }
//	        	nLifeType=ratableResource.getLife_type();
//	        	strRefType=ratableResource.getRef_type();
//	        	nRefOffset=ratableResource.getRef_offset();
//	        	nRatableResourceType=ratableResource.getRatable_resource_type();
//	        	nStartValue=ratableResource.getStart_value();
//	        	nEndValue=ratableResource.getEnd_value();
//	        	strRatableOwnerType=ratableResource.getOwner_type();
//	        	
//	        	bIsMoney = isMoneyRatableType( nRatableResourceType );
//	        	strYearMonth = DateUtil.getYearMonth( plan.getStrEffDate(), ratingMsg.getM_iRatingExtMsg().getM_strExtCurrTime(), nLifeType, nStartValue, nEndValue);
//	        	if(strYearMonth.isEmpty()){
//	        		continue;
//	        	}
//	        	
//	        	if( strRatableOwnerType == "80C" ){
//	                lnRatableOwnerId = plan.getLnOfrInstId();///    待确认
//	                nRatableOwnerLandId = plan.getnOfrInstLatnId();
//	            }else{
//	            	boolean find=false;
//	                for( String strRatable : iTempRatables){
//	                    if( strRatable.equals(strResourceCode) ){
//	                    	find=true;
//	                        break;
//	                    }
//	                }
//	                if( !find ){
//	                    iTempRatables.add( strResourceCode );
//	                }
//	                else
//	                    continue;
//
//	                nRatableOwnerLandId=getRatableLatnId( strRatableOwnerType );
//	                lnRatableOwnerId=Long.parseLong(getRatableOwnerId(strRatableOwnerType));
//	                
//	            }
//	        	bHave = true;
//	            if( (lnRatableTotal=getRatableResourceValue( strResourceCode, ratingData.getiRatableResourceValues() )) ==ErrorInfo.ERR_NOT_FOUND_RATABLE_VALUE ){
//	                bHave = false;
//	                lnRatableTotal = 0;
//	            }
//	        	long lnTempRatableValue =0 ;
//	        	lnTempRatableValue = getRatableValue( nRatableResourceType, lnRealDuration, lnRealTimes, lnRealUpVolume, lnRealDownVolume, lnRealTotalVolume );
//	        	nMoneyType=getMoneyTypeFromRatableType( nRatableResourceType );
//	        	for(ChargeUnit   unit :ratingData.getiChargeUnitsForRatable()){
//	        		
//	        		if( (strACT=ratingMsg.getValue( "ACT", 1 )).isEmpty() )
////	                    m_ratingMsg->addValue( "ACT", itCharge->m_lnAcctItemTypeId );     RatingMsg未实现
////	                else
////	                    m_ratingMsg->setValue( "ACT", itCharge->m_lnAcctItemTypeId );
//	        		
//	        		iCondInParam.setM_nPricingSectionId(unit.getLnPricingSectionId());
//	                iCondInParam.setM_nPlanIdA(unit.getnPlanId());
//	                iCondInParam.setM_nPlanIdB(plan.getnPricingPlanId());
//	                iCondInParam.setM_lnOfrInstIdA(unit.getLnOfrInstId());
//	                iCondInParam.setM_lnOfrInstIdB(plan.getLnOfrInstId());
//	                
//	                if( m_pRatableCondCheck.check( strResourceCode, iCondInParam ) < 0 )
//	                    continue;
//	                
//	                
//	              //非金钱的,如果满足条件,则不需要继续判断,因为非金钱不是根据批价结果来累积,
//	                //而是根据批价的申请量进行累计
//	                if( !bIsMoney )
//	                {
//	                    int nTmpRatableResourceType = getRatableResourceUnit( unit.getStrRatableCode() );
//	                    if( nTmpRatableResourceType == ErrorInfo.ERR_NOT_FOUND_RATABLE_CODE )
//	                    {
//	                        ///Log
//	                    }
//	                    if( nTmpRatableResourceType == nRatableResourceType )
//	                    {
//	                        if( nBillingFlag == RatingMacro.EVENT_BACK )
//	                            lnRatableValue -= calcRatableResourceValue( nRatableResourceType, unit.getLnBeforeDosage() );
//	                        else
//	                            lnRatableValue += calcRatableResourceValue( nRatableResourceType, unit.getLnBeforeDosage() );
//	                    }
//	                    else
//	                    {
//	                        lnRatableValue = lnTempRatableValue;
//	                        break;
//	                    }
//
//	                    continue;
//	                }
//	                if( unit.getnRatableFlag() == 1 ){
//	                    if( unit.getLnOriCharge() <= 0 )
//	                        continue;
//	                }else{
//	                    if( unit.getLnCharge() <= 0 )
//	                        continue;
//	                }
//	                
//	                if( bIsMoney && unit.getnUnitType() == nMoneyType ){
//	                    if( unit.getnRatableFlag()== 1 )
//	                        lnRatableValue += unit.getLnOriCharge();
//	                    else{
//	                        if( unit.getLnLeftMoney() > 0 )
//	                            lnRatableValue += unit.getLnOriCharge();
//	                        else
//	                            lnRatableValue += unit.getLnCharge();
//	                    }
//	                }
//	                
//	                
//	        	}
//	        	if( bIsMoney ){
//	                //如果是事件返还的话,需要增加负值
//	                if( nBillingFlag == RatingMacro.EVENT_BACK && lnRatableValue > 0 )
//	                    lnRatableValue = -lnRatableValue;
//	                if( nBillingFlag != RatingMacro.EVENT_BACK && lnRatableValue <= 0 )
//	                    continue;
//	            }else{
//	                if( nBillingFlag == RatingMacro.EVENT_BACK && lnRatableValue > 0 )
//	                    lnRatableValue = -lnRatableValue;
//	                if( ( lnRatableValue <= 0 && nBillingFlag != RatingMacro.EVENT_BACK ) || ( lnRatableValue == 0 && nBillingFlag == RatingMacro.EVENT_BACK ) )
//	                    continue;
//	            }
//	        	RequestRatableUpdate iRequestRatableUpdate=new RequestRatableUpdate();
//	            Ratable iRatable=new Ratable();
//
//	            iRequestRatableUpdate.setM_strOwnerType(strRatableOwnerType);
//	            iRequestRatableUpdate.setM_lnOwnerId(lnRatableOwnerId);
//	            iRequestRatableUpdate.setM_nLatnId(nRatableOwnerLandId);
//	            iRequestRatableUpdate.setM_strRatableResourceCode(strResourceCode);
//	            iRequestRatableUpdate.setM_lnRatableCycleId(Long.parseLong(strYearMonth));
//	            iRequestRatableUpdate.setM_lnBalance(lnRatableValue);
//
//	            iRequestRatableUpdates.add( iRequestRatableUpdate );
//
//	            lnRatableTotal += lnRatableValue;
//
//	            iRatable.setStrRatableCode(strResourceCode);
//	            iRatable.setnRatableType(nRatableResourceType);
//	            iRatable.setLnValue(lnRatableValue);
//	            iRatable.setLnRatableValue(lnRatableTotal);
//	            iRatable.setStrAcctDate(strYearMonth);
//	            iRatable.setLnOwnerID(lnRatableOwnerId);
//	            iRatable.setnOwnerLatnId(nRatableOwnerLandId);
//	            iRatable.setStrOwnerType(strRatableOwnerType);
//
//	            ratingData.getiRatableRefs().add(iRatable);
//	            //更新内存值
//	            updateRatableValueInMem( strResourceCode, lnRatableValue );
//	        	
//	            //自动调档EasyPlus套餐的短信提醒功能----略
//	            
//	        }
//	    }
//	    RatableUpdateReq iRatableReq = new RatableUpdateReq();
//	    RatableUpdateResp iRatableResp = new RatableUpdateResp();
//
//	    iRatableReq.setM_strSessionId(ratingMsg.getBaseMsg().getM_strSessionId());
//	    iRatableReq.setM_strServiceContextId("");
//	    iRatableReq.setM_ulnServId(Long.parseLong(ratingMsg.getM_iUserMsg().getLnServId()));
//	    iRatableReq.setM_nLatnId(ratingMsg.getM_iUserMsg().getnLatnId());
//	    iRatableReq.setM_iRequestRatableUpdates(iRequestRatableUpdates);
//
////	    ratingData->m_iABMTrack.start();
////	    nRet = m_pABMClient->updateRatableResources( iRatableReq, &iRatableResp );
////	    m_ratingData->m_iABMTrack.stop();
//		
//	    
//		return 0;
//	}
//	
//	
//	
//	
//	private int updateRatableValueInMem(String resourceCode,long ratableValue){
//		RatableResourceValue value=ratingData.getiRatableResourceValues().get(resourceCode);
//		if(value==null){
//			return -1;
//		}
//		
//		value.setM_lnBalance(value.getM_lnBalance()+(int)ratableValue);
//		ratingData.getiRatableResourceValues().put(resourceCode, value);
//		return 0;
//	}
//	
//	private long calcRatableResourceValue(int ratableResourceType, long dosage){
//		
//		
//		
//		return 0;
//	}
//	
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param resourceCode
//	 * @return
//	 */
//	private int getRatableResourceUnit(String resourceCode){
//		RatableResourceInfo iRatableInfo;
//		iRatableInfo=ratingData.getiRatableResourceInfos().get(resourceCode);
//		if( iRatableInfo==null ){
//	        return ErrorInfo.ERR_NOT_FOUND_RATABLE_CODE;
//	    }
//		return Integer.parseInt(iRatableInfo.getM_strRatableResourceType());
//		
//	}
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param resourceType
//	 * @param moneyType
//	 * @return
//	 */
//	
//	public int getMoneyTypeFromRatableType(int resourceType ){
//		int moneyType=0;
//		switch(resourceType){
//		case RatingMacro.RatableResourceType_Money :
//			moneyType=2;
//			break;
//		case RatingMacro.RatableResourceType_M:
//			moneyType=7;
//			break;
//		case RatingMacro.RatableResourceType_T:
//			moneyType=8;
//			break;
//		}
//		
//		return moneyType;
//		
//	}
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param resourceType
//	 * @param duration
//	 * @param times
//	 * @param upVolume
//	 * @param downVolume
//	 * @param totalVolume
//	 * @return
//	 */
//	public long getRatableValue(int resourceType,long duration ,long times,long upVolume,long downVolume,long totalVolume){
//		switch(resourceType){
//		case 1:
//			return duration;
//		case 2:   //时长(分钟)
//			return (duration+59)/60;   //duration单位是秒
//		case 3:
//			return times;
//		case 4:
//			return totalVolume;
//		case 7:
//			return upVolume;
//		case 8:
//			return downVolume;
//		default:	//其他计算资源不考虑
//			break;
//		}
//		
//		return 0;
//	}
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param resourceCode
//	 * @param values
//	 * @return
//	 */
//	public long getRatableResourceValue(String resourceCode,Map<String,RatableResourceValue> values){
//		long resourceValue=-1;
//		RatableResourceInfo info=null;
//		info=ratingData.getiRatableResourceInfos().get(resourceCode);
//		if( info==null ){
//	        return ErrorInfo.ERR_NOT_FOUND_RATABLE_CODE;
//	    }
//		RatableResourceValue value=null;
//		value=ratingData.getiRatableResourceValues().get(resourceCode);
//		if(value==null){
//			return ErrorInfo.ERR_NOT_FOUND_RATABLE_VALUE;
//		}else{
//			resourceValue=value.getM_lnBalance();
//		}
//		//如果事件补款,且携带累积量情况,我们对其累积量-1进行批价
//		//必须按照次数来补款的,支持目前的短信和彩信业务
//		if(ratingMsg.getM_nBillingFlag()==RatingMacro.EVENT_BACK && resourceValue >0 ){
//			resourceValue=-1;
//		}
//		return resourceValue;
//	}
//	
//	
//	
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param ownerType
//	 * @return
//	 */
//	public int getRatableLatnId(String ownerType){
//		int latnId = -1;
//	    if( ownerType == "80I" || ownerType=="80J"){
//	        latnId=ratingMsg.getM_iUserMsg().getnLatnId();
//	        
//	    }else if( ownerType == "80C" ) //如果有多个销售品实例,这个inst可能不准确
//	    {
//	        latnId=ratingMsg.getM_iUserMsg().getnOfrInstLatnId();
//	    }
//	    else
//	    {
//	    	latnId=ratingMsg.getM_iUserMsg().getnLatnId();
//	    }
//		return latnId;
//	}
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param ownerType
//	 * @return
//	 */
//	public String getRatableOwnerId(String ownerType){
//	    String ownerValue = "";
//	    if( ownerType == "80I" ){
//	        ownerValue = ratingMsg.getM_iUserMsg().getLnCustId();
//	    }
//	    else if( ownerType == "80J" ){
//	        ownerValue = ratingMsg.getM_iUserMsg().getLnAcctId();
//	    }
//	    else if( ownerType == "80C" ){ //如果有多个销售品实例,这个inst可能不准确
//	        ownerValue = ""+ratingMsg.getM_iUserMsg().getLnOfrInstId();
//	    }else{
//	        ownerValue = ratingMsg.getM_iUserMsg().getLnServId();
//	    }
//		return ownerValue;
//	}
//	
//	
//	
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param resourceType
//	 * @return
//	 */
//	public boolean isMoneyRatableType(int resourceType){
//		
//		if(resourceType==RatingMacro.RatableResourceType_Money || resourceType==RatingMacro.RatableResourceType_M
//				|| resourceType==RatingMacro.RatableResourceType_T){
//			return true;
//		}
//		return false;
//	}
//	
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param code
//	 * @return
//	 */
//	public CodeRatableResource getRatableAttr(String code){
//		CodeRatableResource resource=null;
//		DbUtil db=new DbUtilImpl();
//		resource=db.getRatableResourceAttr(code);
//		
//		return resource;
//	}
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param msgType
//	 * @param ofrId
//	 * @return
//	 */
//	
//	public String getRatableResourceCode(int msgType,int ofrId){
//		String code="";
//		DbUtil db=new DbUtilImpl();
//		List<RuleOfrResourceRel> rels=db.getAllResourceRels();
//		for(RuleOfrResourceRel rel :rels){
//			if(rel.getMsg_Types().equals(""+msgType) && rel.getOfr_B_Id()==ofrId){
//				code=rel.getResource_Code();
//				break;
//			}
//		}
//		return code;
//	}
//	
//	
//	
//	public int getPricingPlanId(){
//		
//		return 0;
//	}
//	
//	
//	
//	/**
//	 * 2.0未实现
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int backTopRepetition(){
//		
//		return 0;
//	}
//	
//	
//	/**
//	 * 2.0未实现
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int setRepetition(){
//		
//		return 0;
//	}
//	/**
//	 * 2.0未实现
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int setDeductZero(){
//		
//		return 0;
//	}
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int checkRepetition(){
//		
//		return 0;
//	}
//	/**
//	 * 2.0未实现
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public boolean isFmtCheckRepetition(){
//		
//		return false;
//	}
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int checkTopValue(){
//		
//		
//		return 0;
//	}
//	
//	
//	
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @return
//	 * @throws RatingException 
//	 */
//	public int setMergeBalanceReqMsg() throws RatingException{
//		if(ratingMsg.getM_iBalanceInMsg().isbIsQueryBalance()){
//			
//			BalanceContent iBalanceContent=new BalanceContent();
//	        ratingMsg.getM_iBalanceInMsg().getLtQuery().clear();
//	        iBalanceContent.setLnAcctItemTypeId(-1);
//	        iBalanceContent.setnUnitTypeId(-1);
//	        iBalanceContent.setLnAmount(-1);
//	        ratingMsg.getM_iBalanceInMsg().getLtQuery().add(iBalanceContent);
//	        
//		}
//		
//		int nRet = 0;
//	    if( ( nRet = setMergeBalReserve() ) < 0 && ( ratingData.getnServiceLimitFlag() == -1 ) )
//	        return nRet ;
//
//	    if( ( nRet = setMergeBalDeduct() ) < 0 )
//	        return nRet ;
//
//	    if( ( nRet = setMergeBalBack() ) < 0 )
//	        return nRet ;
//
//	    return nRet;
//		
//	}
//	
//	
//	
//	private int setMergeBalBack() throws RatingException{
//		if(ratingMsg.getBaseMsg().getM_nFeeType()==RatingMacro.BACK_FEE_CMD){  //返回B04组
//			if(ratingData.getiReqFees().isEmpty()){
//				throw new RatingException(ErrorInfo.DC_BERR_BALANCE_BACK_NULL,"返还信息不存在");
////				return ErrorInfo.DC_BERR_BALANCE_BACK_NULL;
//			}
//			//ISMP直接扣费请求需要先批价,保存批价结果时清空金钱请求信息
//			ratingMsg.getM_iBalanceInMsg().getLtBack().clear();
//			for(BalanceContent p :ratingData.getiReqFees()){
//				ratingMsg.getM_iBalanceInMsg().getLtBack().add(p);
//				
//			}
//			
//		}
//		return 0;
//	}
//	
//	
//	
//	private int setMergeBalDeduct(){
//		if(ratingMsg.getBaseMsg().getM_nFeeType()==RatingMacro.REAL_FEE_CMD){	//实扣B03组
//			if(ratingData.getiRealFees().isEmpty()){
//				return ErrorInfo.DC_BERR_BALANCE_DEDUCT_NULL;
//			}
//			//ISMP直接扣费请求需要先批价,保存批价结果时清空金钱请求信息
//			ratingMsg.getM_iBalanceInMsg().getLtDeduct().clear();
//			for(BalanceContent p :ratingData.getiRealFees()){
//				ratingMsg.getM_iBalanceInMsg().getLtDeduct().add(p);
//			}
//		}
//		
//		return 0;
//	}
//	
//	
//	
//	public int setMergeBalReserve(){
//		if(ratingMsg.getBaseMsg().getM_nFeeType() == RatingMacro.REQ_FEE_CMD){	//预占B01组
//			if(ratingData.getiReqFees().isEmpty()){
//				return ErrorInfo.DC_BERR_BALANCE_RESERVE_NULL;
//			}
//			for(BalanceContent p :ratingData.getiReqFees()){
//				ratingMsg.getM_iBalanceInMsg().getLtReserve().add(p); //申请使用量
//			}
//			
//		}
//		
//		return 0;
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param unitType
//	 * @param ofrId
//	 * @param money
//	 * @return
//	 */
//	public int updateFeeInfo(int unitType,long ofrId,long money){
//		if(unitType==2){
//			FeeInfo feeInfo=new FeeInfo();
//			feeInfo.setLnCurrOfrId(ofrId);
//			feeInfo.setLnMoney(money);
//			for(FeeInfo f : ratingData.getiFeeInfos()){
//				if(f.equals(feeInfo)){
//					f.setLnMoney(f.getLnMoney()+money);
//					break;
//				}
//			}
//			
//		}
//		return 0;
//	}
//	
//	
//	
//	
//	/**
//	 * 
//	 * @param oper
//	 */
//	public void setRBMessage(int oper){
//		if(oper==RatingMacro.OPER_REQ){
//			RateMeasure iRateMeasure=ratingData.getiRateMeasure();
//			if(iRateMeasure.getLnDuration()!=0){
//				ratingMsg.getM_iRatingOutMsg().setM_strAthDuration(""+iRateMeasure.getLnDuration());
//				
//			}
//			if(iRateMeasure.getLnTimes()!=0){
//				ratingMsg.getM_iRatingOutMsg().setM_strAthTimes(""+iRateMeasure.getLnTimes());
//			}
//			if(iRateMeasure.getLnUpVolume()!=0){
//				ratingMsg.getM_iRatingOutMsg().setM_strAthUpVolume(""+iRateMeasure.getLnUpVolume());
//			}
//			if(iRateMeasure.getLnDownVolume()!=0){
//				ratingMsg.getM_iRatingOutMsg().setM_strAthDownVolume(""+iRateMeasure.getLnDownVolume());
//			}
//			if(iRateMeasure.getLnTotalVolume()!=0){
//				ratingMsg.getM_iRatingOutMsg().setM_strAthTotalVolume(""+iRateMeasure.getLnTotalVolume());
//			}
//			//需要从帐户中扣除的信用,处理那些余额不足的信息,否则不需要补充0
//			for(ChargeUnit u:ratingData.getiMergedReqChargeUnits()){
//				setReqFee(u);
//				
//			}
//			System.out.println("**OPER_REQ***RatingOutMsg:"+ratingMsg.getM_iRatingOutMsg());
//		}else if(oper==RatingMacro.OPER_REAL){
//			//需要从帐户中扣除的信用,处理那些余额不足的信息,否则不需要补充0
//			
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
//	        setUnusedMoneyFromSession();//更新到内存中
//	        
////	        System.out.println("ratingData.m_istrUnusedMoneyInfo>>>>>>>");
////	        System.out.println(ratingData.getStrUnusedMoneyInfo());
////	        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//		}
//		
//	}
//	
//
//
//	
//	private boolean setUnusedMoneyFromSession(){
//		StringBuffer sf=new StringBuffer("");
//		if(ratingData.getnFeeType()==RatingMacro.REAL_REQ_FEE && ratingData.getnOper()==RatingMacro.OPER_REAL){
//			ratingData.setStrUnusedMoneyInfo("");
//			for(Fee f:ratingData.getiUnusedMoneys()){
//				sf.append(f.getLnAcctItemTypeId()).append(f.getnUnitType()).append(f.getLnTariff());
//				ratingData.setStrUnusedMoneyInfo(ratingData.getStrUnusedMoneyInfo()+sf.toString());
//			}
//			
//		}
//		return true;
//	}
//	
//	
//	
//	
//	
//	private int updateUnusedMoney(Fee fee){
//		boolean find=false;
//		if(fee.getnUnitType()!=2){   //非金钱账本
//			ratingData.getiUnusedMoneys().clear();
//			return 0;
//		}else{
//			for(Fee f:ratingData.getiUnusedMoneys()){
//				if(f.getLnAcctItemTypeId()==fee.getLnAcctItemTypeId() && f.getnUnitType()==fee.getnUnitType()){
//					f.setLnTariff(fee.getLnTariff());
//					find=true;
//					break;
//				}
//			}
//			
//			if(!find && fee.getLnTariff()!=0){
//				ratingData.getiUnusedMoneys().add(fee);
//			}
//		}
//		return 0;
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	private void setRealFee(ChargeUnit u){
//		BalanceContent iBalanceContent =new BalanceContent();
//
//	    iBalanceContent.setLnAcctItemTypeId(u.getLnAcctItemTypeId());
//	    iBalanceContent.setnUnitTypeId(u.getnUnitType());
//	    iBalanceContent.setLnAmount(u.getLnFee());
//	    iBalanceContent.setLnDosage(u.getLnBillingDosage());
//	    iBalanceContent.setLnLeftMoney(u.getLnLeftMoney());
//	    iBalanceContent.setnMeasureDomain(Integer.parseInt(u.getStrMeasureDomain()));
//
//	    ratingData.getiRealFees().add(iBalanceContent);
//	}
//	
//	
//	
//	
//	
//	
//	private void setReqFee(ChargeUnit u){
//		BalanceContent iBalanceContent=new BalanceContent();
//
//	    iBalanceContent.setLnAcctItemTypeId(u.getLnAcctItemTypeId());
//	    iBalanceContent.setnUnitTypeId(u.getnUnitType());
//	    iBalanceContent.setLnAmount(u.getLnFee());
//	    iBalanceContent.setLnDosage(u.getLnBillingDosage());
//	    iBalanceContent.setnMeasureDomain(Integer.parseInt(u.getStrMeasureDomain()));
//	    //补款的时候,如果是按msg_id进行补款,那么m_nLatnId和m_lnAcctBalanceId均不为-1,表示补款到对应的账本上,
//	    //批价补款时m_nLatnId和m_lnAcctBalanceId均为-1表示不确定账本ID补款
//	    iBalanceContent.setnLatnId(-1);
//	    iBalanceContent.setLnAcctBalanceId(-1);
//	    ratingData.getiReqFees().add(iBalanceContent);
//	    
//	    
//	}
//	
//	
//	
//	
//	
//	public void setRatingSpanPoint(){
//		
//		if(!ratingData.getStrSpanTime().equals("")){
//			if(!ratingData.getStrSpanTime().equals("NO")){
//				String str=ratingData.getStrSpanTime();
//				ratingMsg.getM_iRatingOutMsg().setM_strRatingSpanPoint(str);
//			}
//		}
//		
//		
//	}
//	
//	
//	
//	
//	
//	public int loadUnusedData(RateData rateData,int operType){
//		for(OfrRateData ofr:rateData.getiOfrResults()){
//			for(SectionRateData section:ofr.getiSectionResults()){
//				if(!ratingMsg.getM_iRatingExtMsg().getM_strDuration().equals("") && section.getStrMeasureDomain().equals("1")){
//					if(operType==RatingMacro.OPER_REAL){
//						long tmp=ratingData.getiCurrSessionValue().getUnUsedDuration();
//						tmp+=section.getLnUnusedDosage();
//						ratingData.getiCurrSessionValue().setUnUsedDuration(tmp);
//						tmp=ratingData.getiCurrSessionValue().getUsedDuration();
//						tmp+=section.getLnRatableUnusedDosage();
//						ratingData.getiCurrSessionValue().setUsedDuration(tmp);
//						
//					}
//					ratingData.setLnReqDuration(ratingData.getLnReqDuration()+section.getLnRatableUnusedDosage());
//					
//				}
//				if(!ratingMsg.getM_iRatingExtMsg().getM_strUpVolume().equals("") && section.getStrMeasureDomain().equals("4")){
//					if(operType==RatingMacro.OPER_REAL ){
//						long tmp=ratingData.getiCurrSessionValue().getUnUsedUpVolume();
//						tmp+=section.getLnUnusedDosage();
//						ratingData.getiCurrSessionValue().setUnUsedUpVolume(tmp);
//						tmp=ratingData.getiCurrSessionValue().getUsedUpVolume();
//						tmp+=section.getLnRatableUnusedDosage();
//						ratingData.getiCurrSessionValue().setUsedUpVolume(tmp);
//						
//					}
//					ratingData.setLnReqUpVolume(ratingData.getLnReqUpVolume()+section.getLnRatableUnusedDosage());
//				}
//				if(!ratingMsg.getM_iRatingExtMsg().getM_strDownVolume().equals("") && section.getStrMeasureDomain().equals("5")){
//					if(operType==RatingMacro.OPER_REAL){
//						long tmp=ratingData.getiCurrSessionValue().getUnUsedDownVolume();
//						tmp+=section.getLnUnusedDosage();
//						ratingData.getiCurrSessionValue().setUnUsedDownVolume(tmp);
//						tmp=ratingData.getiCurrSessionValue().getUsedDownVolume();
//						tmp+=section.getLnRatableUnusedDosage();
//						ratingData.getiCurrSessionValue().setUsedDownVolume(tmp);
//					}
//					ratingData.setLnReqDownVolume(ratingData.getLnReqDownVolume()+section.getLnRatableUnusedDosage());
//				}
//				if(!ratingMsg.getM_iRatingExtMsg().getM_strTotalVolume().equals("") && section.getStrMeasureDomain().equals("2")){
//					if(operType==RatingMacro.OPER_REAL){
//						long tmp=ratingData.getiCurrSessionValue().getUnUsedTotalVolume();
//						tmp+=section.getLnUnusedDosage();
//						ratingData.getiCurrSessionValue().setUnUsedTotalVolume(tmp);
//						tmp=ratingData.getiCurrSessionValue().getUsedTotalVolume();
//						tmp+=section.getLnRatableUnusedDosage();
//						ratingData.getiCurrSessionValue().setUsedTotalVolume(tmp);
//					}
//					ratingData.setLnReqTotalVolume(ratingData.getLnReqTotalVolume()+section.getLnRatableUnusedDosage());
//				}
//				
//			}
//		}
//		
//		return 0;
//	}
//	
//	
//	
//	
//	
//	
//	public RateData calcFeeUpdate(){
//		RateData realRateData=null;
//		ratingData.getiChargeUnits().clear();
//		ratingData.getiBeforeChargeUnits().clear();
////		List<PlanDisct> allAtoms=null;
//		// 是否需要查询定价计划,
//		if (ratingData.isbGetPlanDisctFlag()) {
////			DinnerConversionImpl pricingPlan = new DinnerConversionImpl(ratingMsg, ratingData);
//			dinnerConv =new DinnerConversionImpl(ratingMsg, ratingData);
////			RatableResourceExtractionImpl ratable = new RatableResourceExtractionImpl();
//			try {
////				dinnerConv.init();
//				dinnerConv.extractRateData();
//				
//			} catch (RatingException e) {
//				log.error(e.printError());
//			}
//		}
////				List<PlanDisct> allAtoms = ((DinnerConversionImpl)dinnerConv).getAllAtomsList();
////				ratingMsg=((DinnerConversionImpl)dinnerConv).getRatingMsg();
////				ratingData=((DinnerConversionImpl)dinnerConv).getRatingData();
//		int billFlag = ratingMsg.getM_nBillingFlag();
//		int nOper = ratingData.getnOper();
//		System.out.println("预占1/实扣2::"+nOper);
//		
//		if (!ratingData.isbIsReRating() && !(billFlag == ParamData.SESSION_UPDATE && nOper == ParamData.OPER_REQ)) {
//				ratableResource=new RatableResourceExtractionImpl(ratingMsg,ratingData);
////					((RatableResourceExtractionImpl)ratableResource).setAtomsList(allAtoms);
////					((RatableResourceExtractionImpl)ratableResource).setRatingMsg(ratingMsg);
//					// RatableResourceExtractionImpl ratable=new
//					// RatableResourceExtractionImpl(allAtoms,ratingMsg);
//				try {
//					if (!ratableResource.queryRatableResourceValue()) {
//							System.out.println("累积量查询失败!!!!");
//							return null;
//					}
//				} catch (RatingException e) {
//					log.error(e.printError());
//				}
////				ratingData.setiRatableResourceValues(((RatableResourceExtractionImpl)ratableResource).getRatableResourceValues());
////				ratingData.setiPlanDiscts(((DinnerConversionImpl)dinnerConv).getAllAtomsList());
//		}
//		if (ratingData.isbIsReRating()) {
//			if (billFlag == RatingMacro.SESSION_UPDATE && nOper == RatingMacro.OPER_REQ)
//				ratingData.setiTmpRatableResourceValues(ratingData.getiRealRatableResourceValues());
//			else
//				ratingData.setiTmpRatableResourceValues(ratingData.getiRatableResourceValues());
//		} else {
//			if (billFlag == RatingMacro.SESSION_UPDATE && nOper == RatingMacro.OPER_REQ)
//				ratingData.setiTmpRatableResourceValues(ratingData.getiRealRatableResourceValues());
//			else
//				ratingData.setiTmpRatableResourceValues(ratingData.getiRatableResourceValues());
//		}
////		RateDinnerFormulaImpl rate = new RateDinnerFormulaImpl( ((RatableResourceExtractionImpl)ratableResource), ratingMsg, ratingData);
//		RateDinnerFormula rate = new RateDinnerFormulaImpl( ratingMsg, ratingData);
////		int r = rate.rate();
////		if (r < 0) {
////			return null;
////		}
////		realRateData = rate.getResult();
//		
//		try {
//			realRateData=rate.rate();
//		} catch (RatingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		mergeChargeUnits(ratingData.getnOper());
//		if (ratingData.getnOper() == RatingMacro.OPER_REQ) {
//			ratingData.setiReqChargeUnits(ratingData.getiChargeUnits());
//		} else {
//			ratingData.setiRealChargeUnits(ratingData.getiChargeUnits());
//			ratingData.setiRealRatableResourceValues(realRateData.getiRatableValues());
//		}
//
//		
//		return realRateData;
//	}
//	
//	
//	//合并费用项相同的ChargeUnit
//	public void mergeChargeUnits(int oper){
//		List<ChargeUnit> iTmpChargeUnits = new ArrayList<ChargeUnit>();
//
//	    int lnItemTypeId = -1;
//	    boolean bFlag;
//	    for( ChargeUnit iter : ratingData.getiChargeUnits() ){
//	        bFlag = true;
//	        if( RatingMacro.CODE_ISMP_BRR == ratingMsg.getM_nMsgType() && lnItemTypeId != -1 )
//	            iter.setLnAcctItemTypeId(lnItemTypeId);
//	       
//	        for( ChargeUnit unit : iTmpChargeUnits ){
//	            //相同账目类型和帐本类型的,进行合并
//	            if( unit !=null && iter.getLnAcctItemTypeId() == unit.getLnAcctItemTypeId() && iter.getnUnitType() == unit.getnUnitType() ){
//	                if( iter.getnMTChangeFlag() == 1 && iter.getnUnitType()==2 ){ //MT转换金钱后的
//	                    break;
//	                }else{
//	                    unit.setLnOriCharge(unit.getLnOriCharge()+iter.getLnOriCharge());
//	                    unit.setLnCharge(unit.getLnCharge()+iter.getLnCharge());
//	                    unit.setLnUnusedMondey(unit.getLnUnusedMondey()+iter.getLnUnusedMondey());
//	                    unit.setLnLeftMoney(unit.getLnLeftMoney()+iter.getLnLeftMoney());
//	                    unit.setLnFee(unit.getLnFee()+iter.getLnFee());
//	                    unit.setLnCounts(unit.getLnCounts()+iter.getLnCounts());
//	                    unit.setLnBillingDosage(unit.getLnBillingDosage()+iter.getLnBillingDosage());
//	                    unit.setLnBeforeDosage(unit.getLnBeforeDosage()+iter.getLnBeforeDosage());
//	                    unit.setLnBeforeDosageFront(unit.getLnBeforeDosageFront()+iter.getLnBeforeDosageFront());
//	                    unit.print();
//	                    bFlag = false;
//
//	                    break;
//	                }
//	            }
//	        }
//
//	        if( bFlag )
//	            iTmpChargeUnits.add( iter );
//	    }
//	    if( oper == RatingMacro.OPER_REQ )
//	        ratingData.setiMergedReqChargeUnits(iTmpChargeUnits);
//	    else
//	        ratingData.setiMergedRealChargeUnits(iTmpChargeUnits);
//	    
//		
//	}
//
//
//
//	
//	
//	
//	
//	public void getUpdateRateMeasure(int oper){
//		//处理时间
//		ratingMsg.getM_iRatingExtMsg().setM_strExtStartTime(ratingMsg.getBaseMsg().getM_strStartTime());
//		ratingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(ratingMsg.getVarMsg().getM_strCurrTime());
//		ratingMsg.getM_iRatingExtMsg().setM_strExtLastTime(ratingMsg.getVarMsg().getM_strLastTime());
//		
//		if(oper==RatingMacro.OPER_REQ){// 申请
//			if(ratingMsg.getCommandMsg().getM_strReqDuration()!=0){//处理时长
//				ratingMsg.getM_iRatingExtMsg().setM_strDuration(""+ratingMsg.getCommandMsg().getM_strReqDuration());
//				ratingData.getiStartValue().setLnDuration(ratingData.getiPrevSessionValue().getUsedDuration()+ratingData.getiCurrSessionValue().getUsedDuration());
//				
//			}
//			if(ratingMsg.getCommandMsg().getM_strReqTimes()!=0){  //处理次数
//				ratingMsg.getM_iRatingExtMsg().setM_strTimes(""+ratingMsg.getCommandMsg().getM_strReqTimes());
//				ratingData.getiStartValue().setLnTimes(ratingData.getiPrevSessionValue().getUsedTimes()+ratingData.getiCurrSessionValue().getUsedTimes());
//			}
//			if(ratingMsg.getCommandMsg().getM_strReqUpVolume()!=0){ //处理上行流量
//				ratingMsg.getM_iRatingExtMsg().setM_strUpVolume(""+ratingMsg.getCommandMsg().getM_strReqUpVolume());
//				ratingData.getiStartValue().setLnUpVolume(ratingData.getiPrevSessionValue().getUsedUpVolume()+ratingData.getiCurrSessionValue().getUsedUpVolume());
//				ratingMsg.getM_iRatingExtMsg().setM_strUpVolumeFeeLast("0");
//				
//			}
//			if(ratingMsg.getCommandMsg().getM_strReqDownVolume()!=0){//处理下行流量
//				ratingMsg.getM_iRatingExtMsg().setM_strDownVolume(""+ratingMsg.getCommandMsg().getM_strReqDownVolume());
//				ratingData.getiStartValue().setLnDownVolume(ratingData.getiPrevSessionValue().getUsedDownVolume()+ratingData.getiCurrSessionValue().getUsedDownVolume());
//				ratingMsg.getM_iRatingExtMsg().setM_strDownVolumeFeeLast("0");
//			}
//			if(ratingMsg.getCommandMsg().getM_strReqTotalVolume()!=0){//处理总流量 
//				ratingMsg.getM_iRatingExtMsg().setM_strTotalVolume(""+ratingMsg.getCommandMsg().getM_strReqTotalVolume());
//				ratingData.getiStartValue().setLnTotalVolume(ratingData.getiPrevSessionValue().getUsedTotalVolume()+ratingData.getiCurrSessionValue().getUsedTotalVolume());
//				ratingMsg.getM_iRatingExtMsg().setM_strTotalVolumeFeeLast("0");
//			}
//			
//		}else{
//			long tempValue=0;
//			long reserveAmount=0;
//			long unUsedAmount=0;
//			long amount=0;
//			long lastAmount=0;
//			//因为每次Update都要实扣,所以取上次扣费时间作为本次实扣开始时间 
//			ratingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(ratingMsg.getVarMsg().getM_strLastTime());
//			if(ratingMsg.getCommandMsg().getM_strUsedDuration()!=0){//使用时长
//				System.out.println("获取已使用量:时长类型");
//				tempValue=ratingMsg.getCommandMsg().getM_strUsedDuration();//申请实扣
//				reserveAmount=ratingData.getiPrevSessionValue().getReserveDuration();//上次预占
//				unUsedAmount=ratingData.getiPrevSessionValue().getUnUsedDuration();//上次多扣
//				amount=0;
//				tempValue-=unUsedAmount;
//				if(tempValue<=0){
//					amount=0;
//					unUsedAmount=-tempValue;
//				}else if(tempValue >= reserveAmount){
//					amount=tempValue;
//					unUsedAmount=0;
//				}else{
//					amount=tempValue;
//					unUsedAmount=0;
//				}
//				ratingMsg.getM_iRatingExtMsg().setM_strDuration(""+amount);
//				ratingData.getiCurrSessionValue().setUsedDuration(amount);
//				ratingData.getiCurrSessionValue().setUnUsedDuration(unUsedAmount);
//				ratingData.getiStartValue().setLnDuration(ratingData.getiPrevSessionValue().getUsedDuration());//计费点A
//				
//			}
//			//处理次数
//			if(ratingMsg.getCommandMsg().getM_strUsedTimes()!=0 ){
//				System.out.println("获取已使用量:次数类型");
////				ratingMsg.getM_iRatingExtMsg().setM_strTimes(ratingMsg.getCommandMsg().getM_strUsedTimes());
////				ratingData.getiCurrSessionValue().setUsedTimes(Integer.parseInt(ratingMsg.getCommandMsg().getM_strUsedTimes()));
////				ratingData.getiStartValue().setLnTimes(ratingData.getiPrevSessionValue().getUsedTimes());
//				
//				ratingMsg.getM_iRatingExtMsg().setM_strTimes(""+ratingMsg.getCommandMsg().getM_strUsedTimes());
//				ratingData.getiCurrSessionValue().setUsedTimes(ratingMsg.getCommandMsg().getM_strUsedTimes());
//				ratingData.getiStartValue().setLnTimes(ratingData.getiPrevSessionValue().getUsedTimes());
//				
//			}
//			
//			//处理上行流量
//			if(ratingMsg.getCommandMsg().getM_strUsedUpVolume()!=0){
//				System.out.println("获取已使用量:上行流量");
//			//	tempValue=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedUpVolume());
//				tempValue=ratingMsg.getCommandMsg().getM_strUsedUpVolume();
//				//加上上次未成功扣费的B307消息
//				tempValue+=ratingData.getiPrevSessionValue().getCcgSpStart();
//				reserveAmount=ratingData.getiPrevSessionValue().getReserveUpVolume();
//				unUsedAmount=ratingData.getiPrevSessionValue().getUnUsedUpVolume();
//				tempValue-=unUsedAmount;
//				if(tempValue<=0){
//					amount=0;
//					unUsedAmount=-tempValue;
//				}else if(tempValue >= reserveAmount){
//					amount=tempValue;
//					unUsedAmount=0;
//				}else{
//					amount=tempValue;
//					unUsedAmount=0;
//				}
//				ratingMsg.getM_iRatingExtMsg().setM_strUpVolume(""+amount);
//				ratingData.getiCurrSessionValue().setUnUsedUpVolume(unUsedAmount);
//				ratingData.getiStartValue().setLnDownVolume(ratingData.getiPrevSessionValue().getUsedUpVolume());
//		//		lastAmount=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedUpVolumeFeeLast());
//				lastAmount=ratingMsg.getCommandMsg().getM_strUsedUpVolumeFeeLast();
//				lastAmount+=ratingData.getiPrevSessionValue().getCcgSpEnd();
//				ratingData.getiCurrSessionValue().setUsedUpVolume(amount+lastAmount);
//				ratingMsg.getM_iRatingExtMsg().setM_strUpVolumeFeeLast(""+lastAmount);
//				
//			}
//			//处理下行流量
//			if(ratingMsg.getCommandMsg().getM_strUsedDownVolume()!=0){
//				System.out.println("获取已使用量:下行流量");
//		//		tempValue=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedDownVolume());
//				tempValue=ratingMsg.getCommandMsg().getM_strUsedDownVolume();
//				tempValue+=ratingData.getiPrevSessionValue().getCcgSpStart();
//				reserveAmount=ratingData.getiPrevSessionValue().getReserveDownVolume();
//				unUsedAmount=ratingData.getiPrevSessionValue().getUnUsedDownVolume();
//				tempValue-=unUsedAmount;
//				if(tempValue <=0){
//					amount=0;
//					unUsedAmount=tempValue;
//					
//				}else if(tempValue>=reserveAmount){
//					amount=tempValue;
//					unUsedAmount=0;
//				}else{
//					amount=tempValue;
//					unUsedAmount=0;
//				}
//				ratingMsg.getM_iRatingExtMsg().setM_strDownVolume(""+amount);
//				ratingData.getiCurrSessionValue().setUnUsedDownVolume(unUsedAmount);
//				ratingData.getiStartValue().setLnDownVolume(ratingData.getiPrevSessionValue().getUsedDownVolume());
//	//			lastAmount=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedDownVolumeFeeLast());
//				lastAmount=ratingMsg.getCommandMsg().getM_strUsedDownVolumeFeeLast();
//				lastAmount+=ratingData.getiPrevSessionValue().getCcgSpEnd();
//				ratingData.getiCurrSessionValue().setUsedDownVolume(amount+lastAmount);
//				ratingMsg.getM_iRatingExtMsg().setM_strDownVolumeFeeLast(""+lastAmount);
//				
//			}
//			//处理总流量
//			if(ratingMsg.getCommandMsg().getM_strUsedTotalVolume()!=0){
//				System.out.println("获取已使用量:总流量");
//			//	tempValue=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedTotalVolume());
//				tempValue=ratingMsg.getCommandMsg().getM_strUsedTotalVolume();
//				tempValue+=ratingData.getiPrevSessionValue().getCcgSpStart();
//				reserveAmount=ratingData.getiPrevSessionValue().getReserveTotalVolume();
//				unUsedAmount=ratingData.getiPrevSessionValue().getUnUsedTotalVolume();
//				tempValue-=unUsedAmount;
//				if(tempValue <=0){
//					amount=0;
//					unUsedAmount=-tempValue;
//				}else if(tempValue >= reserveAmount){
//					amount=tempValue;
//					unUsedAmount=0;
//				}else{
//					amount=tempValue;
//					unUsedAmount=0;
//				}
//				ratingMsg.getM_iRatingExtMsg().setM_strTotalVolume(""+amount);
//				ratingData.getiCurrSessionValue().setUnUsedTotalVolume(unUsedAmount);
//				ratingData.getiStartValue().setLnTotalVolume(ratingData.getiPrevSessionValue().getUsedTotalVolume());
//	//			lastAmount=Long.parseLong(ratingMsg.getCommandMsg().getM_strUsedTotalVolumeFeeLast());
//				lastAmount=ratingMsg.getCommandMsg().getM_strUsedTotalVolumeFeeLast();
//				lastAmount+=ratingData.getiPrevSessionValue().getCcgSpEnd();
//				ratingData.getiCurrSessionValue().setUsedTotalVolume(amount+lastAmount);
//				ratingMsg.getM_iRatingExtMsg().setM_strTotalVolumeFeeLast(""+lastAmount);
//				
//			}
//		}
//		if(oper==RatingMacro.OPER_REQ){
//
//			RateMeasure rateMeasure=initRateMeasureFromMsg();
//			ratingData.setiReqRateMeasure(rateMeasure);
//			
//		}else{
//			
//			RateMeasure rateMeasure=initRateMeasureFromMsg();
//			ratingData.setiRealRateMeasure(rateMeasure);
//		}
//	}
//	
//	
//	
//	
//	/**
//	 *  获取使用量
//	 * @author sung
//	 *
//	 * @param measure
//	 * @return
//	 */
//	
//	public RateMeasure initRateMeasureFromMsg(){
//		RateMeasure measure=new RateMeasure();
//		RatingExtMsg ratingExtMsg=ratingMsg.getM_iRatingExtMsg();
//		String usedValue="";
//		//按时长计费
//	    if( !ratingExtMsg.getM_strDuration().isEmpty() ){
//	    	usedValue=ratingExtMsg.getM_strDuration();
//	        measure.setLnDuration(Integer.parseInt(usedValue));
//	    }
//	    //按流量
//	    if( !ratingExtMsg.getM_strTotalVolume().isEmpty() ){
//	    	usedValue=ratingExtMsg.getM_strTotalVolume();
//	    	measure.setLnTotalVolume(Integer.parseInt(usedValue));
//	    }
//	    //按次
//	    if( !ratingExtMsg.getM_strTimes().isEmpty() ){
//	    	usedValue=ratingExtMsg.getM_strTimes();
//	    	measure.setLnTimes(Integer.parseInt(usedValue));
//	    }	
//	    //上行流量
//	    if( !ratingExtMsg.getM_strUpVolume().isEmpty() ){
//	    	usedValue=ratingExtMsg.getM_strUpVolume();
//	    	measure.setLnUpVolume(Integer.parseInt(usedValue));
//	    }	
//	    //下行流量
//	    if( !ratingExtMsg.getM_strDownVolume().isEmpty()){
//	    	usedValue=ratingExtMsg.getM_strDownVolume();
//	    	measure.setLnDownVolume(Integer.parseInt(usedValue));
//	    }	
//	    //金钱
//	    	
//	    usedValue=ratingExtMsg.getM_strTotalVolumeFeeLast();
//	    
//	    measure.setLnLastTotalVolume(Integer.parseInt(usedValue==""?"0":usedValue));
//	    
//	    usedValue=ratingExtMsg.getM_strUpVolumeFeeLast();
//	    measure.setLnLastUpVolume(Integer.parseInt(usedValue==""?"0":usedValue));
//	    
//	    usedValue=ratingExtMsg.getM_strDownVolumeFeeLast();
//	    measure.setLnLastDownVolume(Integer.parseInt(usedValue==""?"0":usedValue));
//	    
//	    measure.setLnUnusedDuration(measure.getLnDuration());
//	    measure.setLnUnusedTotalVolume(measure.getLnTotalVolume());
//	    measure.setLnUnusedTimes(measure.getLnTimes());
//	    measure.setLnUnusedUpVolume(measure.getLnUpVolume());
//	    measure.setLnUnusedDownVolume(measure.getLnDownVolume());
//	    measure.setLnUnusedLastTotalVolume(measure.getLnLastTotalVolume());
//	    measure.setLnUnusedLastUpVolume(measure.getLnLastUpVolume());
//	    measure.setLnUnusedLastDownVolume(measure.getLnLastDownVolume());
//	    System.out.println("本次使用量 : *RateMeasure:"+measure);
//		return measure;
//	}
//	
//	
//	
//	
//	
//	public void setReqRateMeasure(){
//		//消息中的使用量
//		if(!ratingMsg.getM_iRatingExtMsg().getM_strDuration().isEmpty()){
//			ratingData.setLnReqDuration(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strDuration()));
//		}
//		if(!ratingMsg.getM_iRatingExtMsg().getM_strTimes().isEmpty()){
//			ratingData.setLnReqTimes(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strTimes()));
//			
//		}
//		if(!ratingMsg.getM_iRatingExtMsg().getM_strUpVolume().isEmpty()){
//			ratingData.setLnReqUpVolume(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strUpVolume()));
//			//考虑费率切换点之后的值,下同
//			ratingData.setLnReqUpVolume(ratingData.getLnReqUpVolume()+Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strUpVolumeFeeLast()));
//			
//		}
//		if(!ratingMsg.getM_iRatingExtMsg().getM_strDownVolume().isEmpty() ){
//			ratingData.setLnReqDownVolume(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strDownVolume()));
//			ratingData.setLnReqDownVolume(ratingData.getLnReqDownVolume()+Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strDownVolumeFeeLast()));
//			
//		}
//		if(!ratingMsg.getM_iRatingExtMsg().getM_strTotalVolume().isEmpty()){
//			ratingData.setLnReqTotalVolume(Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strTotalVolume()));
//			ratingData.setLnReqTotalVolume(ratingData.getLnReqTotalVolume()+Long.parseLong(ratingMsg.getM_iRatingExtMsg().getM_strTotalVolumeFeeLast()));
//			
//		}
//	}
//	
	
	
	
	
}
