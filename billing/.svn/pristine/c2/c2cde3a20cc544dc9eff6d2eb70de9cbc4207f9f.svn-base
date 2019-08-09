package com.tydic.beijing.billing.rating.service.impl;




public class ChargingImpl{// implements Charging {

//	private static Logger log = Logger.getLogger(ChargingImpl.class);
//	int nRet = 0;
//	// RBConf pConf;
//	DataStore m_pDataStore;
//	RatingMsg pRatingMsg;
//	RatingData pRatingData;
//
//	MsgParsingImpl msgParsing=null;
//	String strFormula; // 套餐公式
//	private  RateData updateFee; 
//	// OperCtrl pOperCtrl;
//	// RateCondCheck pRateCondCheck;
//	// RatableCondCheck pRatableCondCheck;
//
//	private ChargingUpdate chargingUpdate=new ChargingUpdateImpl();
//	private RBConf conf=new RBConf();
//	private RatableCondCheck m_pRatableCondCheck=new RatableCondCheck();
//	public ChargingImpl(){}
//	public ChargingImpl(RatingMsg ratingMsg,RatingData ratingData){
//		this.pRatingData=ratingData;
//		this.pRatingMsg=ratingMsg;
//	}
//	public String charge() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String inverseCharge() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public RateData calcFee() {
//		RateData realRateData=null;
//		//是否需要查询定价计划
//		if(pRatingData.isbGetPlanDisctFlag()){
//			DinnerConversion pricingPlan=new DinnerConversionImpl(pRatingMsg,pRatingData);
//			try {
//				pricingPlan.extractRateData();
//				
//			} catch (RatingException e) {
//				log.error(e.printError()); 
//				e.printStackTrace();
//			}
//			
//		}
//		//业务控制
//		if (isNeedCtrl()) {
//			log.debug("选取业务控制判断逻辑");
//			int iVocFlag = 0;
//			// /
//			nRet = checkForbid(pRatingData, pRatingMsg, iVocFlag);
//			if (0 != nRet) // 业务控制
//			{
//				// pRatingMsg.setOperaResultCode( nRet );
//				// return ERR_OPERACTRL_CODE;
//			}
//		}
//
//		// /获取套餐分组信息
//		if ((nRet = getOfrGroupAndPrior()) < 0) {
//			return null;
//		}
//		// 组内优惠
//		if ((nRet = getOfrGroupFav()) < 0) {
//			return null;
//		}
//		// 套餐公式
//		if ((nRet = getFormulaAndAdjust()) < 0) {
//			return null;
//		}
//
//		// /应用套餐公式
////		nRet = rate(data_);
////		if (nRet < 0)
////			return nRet;
//
//		return realRateData;
//	}
//
//	/**
//	 * 获取定价计划
//	 * 
//	 * @author dongxuanyi
//	 * 
//	 */
//	public int getIVPNPricingPlanId() {// 查找本方集团信息-计费类型-话单类型
//		// TODO 自动生成的方法存根
//
//		return 0;
//	}
//
//	/**
//	 * 将不需要进行业务控制判断逻辑选取出来：1.版本一消息.2.返还消息　3.term消息 false：不需要 true：需要
//	 * 
//	 * @author dongxuanyi
//	 * 
//	 */
//	public boolean isNeedCtrl() {
//		// TODO 自动生成的方法存根
//		return true;
//	}
//
//	/**
//	 * 
//	 * 业务控制
//	 * 
//	 * @author dongxuanyi
//	 * 
//	 */
//	public int checkForbid(RatingData data_, RatingMsg msg_, int nVocFlag_) {
//		// TODO 自动生成的方法存根
//		RatingData pRatingData = data_;
//		RatingMsg pRatingMsg = msg_;
//		// bIsReqTime = false;
//		// strReqTime = "";
//
//		PlanDisct iter; // 定价计划的缓冲列表
//		int nResult = 0;
//
//		return 0;
//	}
//
//	/**
//	 * 
//	 * 业务控制
//	 * 
//	 * @author dongxuanyi
//	 * 
//	 */
//	public int checkForbid(String strRefType_, int nRefID_, int nVocFlag_) {
//		// TODO 自动生成的方法存根
//
//		return 0;
//	}
//
//	/**
//	 * 
//	 * 产品组和优先级
//	 * 
//	 * @author dongxuanyi
//	 * 
//	 */
//	public int getOfrGroupAndPrior() {
//		// TODO 自动生成的方法存根
//		PlanDisct iter, end;
//		// PrdOfrGroup iOfrGroup;
//
//		int nMsgType = pRatingMsg.getM_nMsgType();
//
//
//		return 0;
//	}
//
//	/**
//	 * 
//	 * 组内优惠
//	 * 
//	 * @author dongxuanyi
//	 * 
//	 */
//	public int getOfrGroupFav() {
//		// TODO 自动生成的方法存根
//
//		return 0;
//	}
//
//	/**
//	 * 
//	 * 套餐公式
//	 * 
//	 * @author dongxuanyi
//	 * 
//	 */
//	public int getFormulaAndAdjust() {
//		// TODO 自动生成的方法存根
//
//		return 0;
//	}
//
//	public int rate(RateData result_) {
//		int nRet = 0;
//		TokenNode iNodeStack;
//		TokenNode iCurrNode;
//		TokenNode iResultNode;
//		RateMeasure iRateMeasure; // 使用量
//		TokenNode iLeftIter, iMidIter, iRightIter;
//		String iter;
//		String end;
//
//		return 0;
//	}
//
//	public String ratingInit(){
//		
//		
//		return "0";
//	}
//	
//	
//	public int ratingUpdate(){
//		chargingUpdate.init(pRatingMsg, pRatingData);
//		return chargingUpdate.ratingUpdate();
//	}
//	
//	
//	
//	/*
//	 * update 
//	 */
//	public int balanceUpdate(ChargeUpdateReq chargeReq,ChargeUpdateResp chargeResp){
//		int nRet = 0;
//
//	    nRet = prepareForBalance();
//	    if( nRet < 0 )
//	        return nRet;
//	    
//	    if( !pRatingMsg.isNeedRating() ){
//	        if( ( nRet = getSession() ) < 0 )
//	            return nRet;
//
//	        //直接扣费类的补款时扣费历史记录表查询
//	        if( ( nRet = getDeductHistory() ) < 0 )
//	            return nRet;
//
//	        if( ( nRet = queryBalanceABM() ) < 0 )
//	            return nRet;
//	        
//	    }
//	    //封顶值检查
//	    if( pRatingMsg.isNeedCheckTopValue()){
//	        if( ( nRet = checkTopValue() ) < 0 )
//	            return nRet;
//	    }
//	    
//	    //剔重
//	    if( pRatingMsg.isNeedCheckRepetition() || isFmtCheckRepetition() ){
//	        nRet = checkRepetition();
//	        if( nRet < 0 )
//	            return nRet;
//	        if( nRet == 1 )
//	            setDeductZero();
//	        else if( nRet == 0 )
//	            setRepetition();
//	    }
//
//	    
//	    //封顶值检查补款//在线模块必须是ISMP业务,离线模块必须是20业务
//	    if( pRatingMsg.getM_nBillingFlag() == RatingMacro.EVENT_BACK ){
//	        nRet = backTopRepetition();
//	        if( nRet < 0 )
//	            return nRet;
//	    }
//	    
//	    //设置update时的B30组扣费信息
//	    if( pRatingMsg.getM_iBalanceInMsg().isbIsMoneyRequest() )
//	        setDeductMessage();
//	    
//	    //直接扣费更新累积量
//	    if(conf.getDirectRatableFlag()==1){
//	    	if( ( nRet = updateDirectMoneyRatable() ) < 0 )
//	            return nRet;
//	    }
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
//	public int updateDirectMoneyRatable(){
//		if ( pRatingMsg.getM_nMsgType() == RatingMacro.CODE_BBR && pRatingMsg.isRatingOk() )
//	        return 0;
//
//	    if (  pRatingMsg.isDirectBalance() ){
//
//	        ChargeUnit iChargeUnit;
//	        pRatingData.getiChargeUnitsForRatable().clear();
//	        if ( isQueryBackSuccess() && pRatingMsg.isNeedQueryDeductHis() )
//	            return 0;
//
//	        int nRet = getPricingPlanId();
//	        if ( nRet < 0 )
//	            return nRet;
//
//	        //补款
//	        for( BalanceContent iter : pRatingMsg.getM_iBalanceInMsg().getLtBack() ){
//	            iChargeUnit=new ChargeUnit();
//	            iChargeUnit.setLnAcctItemTypeId(iter.getLnAcctItemTypeId());
//	            iChargeUnit.setnUnitType(iter.getnUnitTypeId());
//	            iChargeUnit.setLnOriCharge(iter.getLnAmount());
//	            iChargeUnit.setLnCharge(iter.getLnAmount());
//	            pRatingData.getiChargeUnitsForRatable().add(iChargeUnit);
//	        }
//	        //扣款
//	        for( BalanceContent iter : pRatingMsg.getM_iBalanceInMsg().getLtDeduct() ){
//	            iChargeUnit=new ChargeUnit();
//	            iChargeUnit.setLnAcctItemTypeId(iter.getLnAcctItemTypeId());
//	            iChargeUnit.setnUnitType(iter.getnUnitTypeId());
//	            iChargeUnit.setLnOriCharge(iter.getLnAmount());
//	            iChargeUnit.setLnCharge(iter.getLnAmount());
//	            pRatingData.getiChargeUnitsForRatable().add(iChargeUnit);
//	        }
//	        pRatingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(pRatingMsg.getVarMsg().getM_strCurrTime());
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
//	    String szBuf = pRatingMsg.getBaseMsg().getM_strStartTime();
//	    String strBillingNbr;
//	    long lnSessionTotalCharge = 0;
//	    long lnDuration = 0;
//
//	    RatableCondInParam iCondInParam=new RatableCondInParam();
//		
//	    int nMsgType = pRatingMsg.getM_nMsgType();
////	    int nMsgVersion = pRatingMsg.->getMsgVersion();
//	    int nBillingFlag = pRatingMsg.getM_nBillingFlag();
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
//	    long lnRealDuration = pRatingData.getLnReqDuration();
//	    long lnRealTimes = pRatingData.getLnReqTimes();
//	    long lnRealUpVolume = pRatingData.getLnReqUpVolume();
//	    long lnRealDownVolume = pRatingData.getLnReqDownVolume();
//	    long lnRealTotalVolume = pRatingData.getLnReqTotalVolume();
//	    List<RequestRatableUpdate> iRequestRatableUpdates=new ArrayList<RequestRatableUpdate>();
//		
//	    iCondInParam.setM_pRatingData(pRatingData);
//		iCondInParam.setM_pRatingMsg(pRatingMsg);
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
//		if(pRatingMsg.getM_iServInfo().getStrAcceptDate().equals(pRatingMsg.getVarMsg().getM_strCurrTime())){
//			bIsFirstMonth1 = true;
//			
//		}
//		
//		if(pRatingMsg.getM_iServInfo().getStrBasicState().equals("F0A")){
//			bIsFirstMonth1 = true;
//		}
//		
//		strBillingNbr = pRatingMsg.getM_iExtMsg().getM_strBillingNbr();
//		//(集团短信提醒)获取销售ID段落对应关系
//		
//		pRatingData.setiChargeUnitsForRatable(pRatingData.getiRealChargeUnits());
//
//	    for( ChargeUnit iter : pRatingData.getiChargeUnitsForRatable() ){
//	        if( iter.getnRatableFlag() == 1 ) //累计优惠后费用
//	            lnSessionTotalCharge += iter.getLnOriCharge();
//	        else
//	            lnSessionTotalCharge += iter.getLnCharge(); //确定哪个费用
//	    }
//	    lnSessionTotalCharge += pRatingData.getiPrevSessionValue().getUnUsedMoney();
//
//	    lnDuration += lnRealDuration + pRatingData.getiPrevSessionValue().getUsedDuration();
//
//	    List<String> iUsedRatables=new ArrayList<String>();
//	    List<String> iUnUsedRatables = new ArrayList<String>();
//		
//	    for(PlanDisct   plan :pRatingData.getiPlanDiscts()){
//	    	iUsedRatables.clear();
//	        String code = getRatableResourceCode( nMsgType, plan.getnAtomOfrId());
//	        iUsedRatables.add(code);
//	        if( nRet < 0 )
//	            continue;
//	        if( iUsedRatables.isEmpty() )
//	            continue;
//
//	        pRatingMsg.getM_iUserMsg().setLnOfrInstId(plan.getLnOfrInstId());
//	        pRatingMsg.getM_iUserMsg().setnOfrInstLatnId(plan.getnOfrInstLatnId());
//	        
//	        //根据销售品订购的生效时间判断是否激活首月
//	        if( plan.getStrEffDate().equals(pRatingMsg.getVarMsg().getM_strCurrTime()) )
//	            bIsFirstMonth2 = true;
//	        else
//	            bIsFirstMonth2 = false;
//	        if( pRatingMsg.getM_iServInfo().getStrBasicState().equals("F0A")) //状态为F0A一定是激活首月
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
//	        	strYearMonth = DateUtil.getYearMonth( plan.getStrEffDate(), pRatingMsg.getM_iRatingExtMsg().getM_strExtCurrTime(), nLifeType, nStartValue, nEndValue);
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
//	            if( (lnRatableTotal=getRatableResourceValue( strResourceCode, pRatingData.getiRatableResourceValues() )) ==ErrorInfo.ERR_NOT_FOUND_RATABLE_VALUE ){
//	                bHave = false;
//	                lnRatableTotal = 0;
//	            }
//	        	long lnTempRatableValue =0 ;
//	        	lnTempRatableValue = getRatableValue( nRatableResourceType, lnRealDuration, lnRealTimes, lnRealUpVolume, lnRealDownVolume, lnRealTotalVolume );
//	        	nMoneyType=getMoneyTypeFromRatableType( nRatableResourceType );
//	        	for(ChargeUnit   unit :pRatingData.getiChargeUnitsForRatable()){
//	        		
//	        		if( (strACT=pRatingMsg.getValue( "ACT", 1 )).isEmpty() )
////	                    m_pRatingMsg->addValue( "ACT", itCharge->m_lnAcctItemTypeId );     RatingMsg未实现
////	                else
////	                    m_pRatingMsg->setValue( "ACT", itCharge->m_lnAcctItemTypeId );
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
//	            pRatingData.getiRatableRefs().add(iRatable);
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
//	    iRatableReq.setM_strSessionId(pRatingMsg.getBaseMsg().getM_strSessionId());
//	    iRatableReq.setM_strServiceContextId("");
//	    iRatableReq.setM_ulnServId(Long.parseLong(pRatingMsg.getM_iUserMsg().getLnServId()));
//	    iRatableReq.setM_nLatnId(pRatingMsg.getM_iUserMsg().getnLatnId());
//	    iRatableReq.setM_iRequestRatableUpdates(iRequestRatableUpdates);
//
////	    pRatingData->m_iABMTrack.start();
////	    nRet = m_pABMClient->updateRatableResources( iRatableReq, &iRatableResp );
////	    m_pRatingData->m_iABMTrack.stop();
//		
//	    
//		return 0;
//	}
//	
//	
//	
//	
//	private int updateRatableValueInMem(String resourceCode,long ratableValue){
//		RatableResourceValue value=pRatingData.getiRatableResourceValues().get(resourceCode);
//		if(value==null){
//			return -1;
//		}
//		
//		value.setM_lnBalance(value.getM_lnBalance()+(int)ratableValue);
//		pRatingData.getiRatableResourceValues().put(resourceCode, value);
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
//		iRatableInfo=pRatingData.getiRatableResourceInfos().get(resourceCode);
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
//	public int getRatableResourceValue(String resourceCode,Map<String,RatableResourceValue> values){
//		int resourceValue=-1;
//		RatableResourceInfo info=null;
//		info=pRatingData.getiRatableResourceInfos().get(resourceCode);
//		if( info==null ){
//	        return ErrorInfo.ERR_NOT_FOUND_RATABLE_CODE;
//	    }
//		RatableResourceValue value=null;
//		value=pRatingData.getiRatableResourceValues().get(resourceCode);
//		if(value==null){
//			return ErrorInfo.ERR_NOT_FOUND_RATABLE_VALUE;
//		}else{
//			//resourceValue=value.getM_lnBalance();
//		}
//		//如果事件补款,且携带累积量情况,我们对其累积量-1进行批价
//		//必须按照次数来补款的,支持目前的短信和彩信业务
//		if(pRatingMsg.getM_nBillingFlag()==RatingMacro.EVENT_BACK && resourceValue >0 ){
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
//	        latnId=pRatingMsg.getM_iUserMsg().getnLatnId();
//	        
//	    }else if( ownerType == "80C" ) //TODO:如果有多个销售品实例,这个inst可能不准确
//	    {
//	        latnId=pRatingMsg.getM_iUserMsg().getnOfrInstLatnId();
//	    }
//	    else
//	    {
//	    	latnId=pRatingMsg.getM_iUserMsg().getnLatnId();
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
//	        ownerValue = pRatingMsg.getM_iUserMsg().getLnCustId();
//	    }
//	    else if( ownerType == "80J" ){
//	        ownerValue = pRatingMsg.getM_iUserMsg().getLnAcctId();
//	    }
//	    else if( ownerType == "80C" ){ //TODO:如果有多个销售品实例,这个inst可能不准确
//	        ownerValue = ""+pRatingMsg.getM_iUserMsg().getLnOfrInstId();
//	    }else{
//	        ownerValue = pRatingMsg.getM_iUserMsg().getLnServId();
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
//	/**
//	 * 设置实扣返回消息(取2.0部分实现)
//	 * @author sung
//	 *
//	 */
//	
//	public void setDeductMessage(){
//		int nBillingFlag = pRatingMsg.getM_nBillingFlag();
//		if(nBillingFlag==RatingMacro.SESSION_UPDATE){
//			//取B30组
//			for(BalanceContent b :pRatingMsg.getM_iBalanceInMsg().getLtUsed()){
//				pRatingMsg.getM_iBalanceInMsg().getLtDeduct().add(b);
//				long money=pRatingData.getiCurrSessionValue().getUsedMoney();
//				money+=b.getLnAmount();
//				pRatingData.getiCurrSessionValue().setUsedMoney(money);
//				
//			}
//		}
//		
//	}
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
//	/*
//	 * update 
//	 */
//	public void getUpdateRateMeasure(int oper){
//		//处理时间
//		pRatingMsg.getM_iRatingExtMsg().setM_strExtStartTime(pRatingMsg.getBaseMsg().getM_strStartTime());
//		pRatingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(pRatingMsg.getVarMsg().getM_strCurrTime());
//		pRatingMsg.getM_iRatingExtMsg().setM_strExtLastTime(pRatingMsg.getVarMsg().getM_strLastTime());
//		
//		if(oper==RatingMacro.OPER_REQ){// 申请
//			if(pRatingMsg.getCommandMsg().getM_strReqDuration()!=0){//处理时长
//				pRatingMsg.getM_iRatingExtMsg().setM_strDuration(""+pRatingMsg.getCommandMsg().getM_strReqDuration());
//				pRatingData.getiStartValue().setLnDuration(pRatingData.getiPrevSessionValue().getUsedDuration()+pRatingData.getiCurrSessionValue().getUsedDuration());
//				
//			}
//			if(pRatingMsg.getCommandMsg().getM_strReqTimes()!=0){  //处理次数
//				pRatingMsg.getM_iRatingExtMsg().setM_strTimes(""+pRatingMsg.getCommandMsg().getM_strReqTimes());
//				pRatingData.getiStartValue().setLnTimes(pRatingData.getiPrevSessionValue().getUsedTimes()+pRatingData.getiCurrSessionValue().getUsedTimes());
//			}
//			if(pRatingMsg.getCommandMsg().getM_strReqUpVolume()!=0){ //处理上行流量
//				pRatingMsg.getM_iRatingExtMsg().setM_strUpVolume(""+pRatingMsg.getCommandMsg().getM_strReqUpVolume());
//				pRatingData.getiStartValue().setLnUpVolume(pRatingData.getiPrevSessionValue().getUsedUpVolume()+pRatingData.getiCurrSessionValue().getUsedUpVolume());
//				pRatingMsg.getM_iRatingExtMsg().setM_strUpVolumeFeeLast("0");
//				
//			}
//			if(pRatingMsg.getCommandMsg().getM_strReqDownVolume()!=0){//处理下行流量
//				pRatingMsg.getM_iRatingExtMsg().setM_strDownVolume(""+pRatingMsg.getCommandMsg().getM_strReqDownVolume());
//				pRatingData.getiStartValue().setLnDownVolume(pRatingData.getiPrevSessionValue().getUsedDownVolume()+pRatingData.getiCurrSessionValue().getUsedDownVolume());
//				pRatingMsg.getM_iRatingExtMsg().setM_strDownVolumeFeeLast("0");
//			}
//			if(pRatingMsg.getCommandMsg().getM_strReqTotalVolume()!=0){//处理总流量 
//				pRatingMsg.getM_iRatingExtMsg().setM_strTotalVolume(""+pRatingMsg.getCommandMsg().getM_strReqTotalVolume());
//				pRatingData.getiStartValue().setLnTotalVolume(pRatingData.getiPrevSessionValue().getUsedTotalVolume()+pRatingData.getiCurrSessionValue().getUsedTotalVolume());
//				pRatingMsg.getM_iRatingExtMsg().setM_strTotalVolumeFeeLast("0");
//			}
//			
//		}else{
//			long tempValue=0;
//			long reserveAmount=0;
//			long unUsedAmount=0;
//			long amount=0;
//			long lastAmount=0;
//			//因为每次Update都要实扣,所以取上次扣费时间作为本次实扣开始时间 
//			pRatingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(pRatingMsg.getVarMsg().getM_strLastTime());
//			if(pRatingMsg.getCommandMsg().getM_strUsedDuration()!=0){//使用时长
//				tempValue=pRatingMsg.getCommandMsg().getM_strUsedDuration();//申请实扣
//				reserveAmount=pRatingData.getiPrevSessionValue().getReserveDuration();//上次预占
//				unUsedAmount=pRatingData.getiPrevSessionValue().getUnUsedDuration();//上次多扣
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
//				pRatingMsg.getM_iRatingExtMsg().setM_strDuration(""+amount);
//				pRatingData.getiCurrSessionValue().setUsedDuration(amount);
//				pRatingData.getiCurrSessionValue().setUnUsedDuration(unUsedAmount);
//				pRatingData.getiStartValue().setLnDuration(pRatingData.getiPrevSessionValue().getUsedDuration());//计费点A
//				
//			}
//			//处理次数
//			if(pRatingMsg.getCommandMsg().getM_strUsedTimes()!=0 ){
////				pRatingMsg.getM_iRatingExtMsg().setM_strTimes(pRatingMsg.getCommandMsg().getM_strUsedTimes());
////				pRatingData.getiCurrSessionValue().setUsedTimes(Integer.parseInt(pRatingMsg.getCommandMsg().getM_strUsedTimes()));
////				pRatingData.getiStartValue().setLnTimes(pRatingData.getiPrevSessionValue().getUsedTimes());
//				
//				pRatingMsg.getM_iRatingExtMsg().setM_strTimes(""+pRatingMsg.getCommandMsg().getM_strUsedTimes());
//				pRatingData.getiCurrSessionValue().setUsedTimes(pRatingMsg.getCommandMsg().getM_strUsedTimes());
//				pRatingData.getiStartValue().setLnTimes(pRatingData.getiPrevSessionValue().getUsedTimes());
//				
//			}
//			
//			//处理上行流量
//			if(pRatingMsg.getCommandMsg().getM_strUsedUpVolume()!=0){
//			//	tempValue=Long.parseLong(pRatingMsg.getCommandMsg().getM_strUsedUpVolume());
//				tempValue=pRatingMsg.getCommandMsg().getM_strUsedUpVolume();
//				//加上上次未成功扣费的B307消息
//				tempValue+=pRatingData.getiPrevSessionValue().getCcgSpStart();
//				reserveAmount=pRatingData.getiPrevSessionValue().getReserveUpVolume();
//				unUsedAmount=pRatingData.getiPrevSessionValue().getUnUsedUpVolume();
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
//				pRatingMsg.getM_iRatingExtMsg().setM_strUpVolume(""+amount);
//				pRatingData.getiCurrSessionValue().setUnUsedUpVolume(unUsedAmount);
//				pRatingData.getiStartValue().setLnDownVolume(pRatingData.getiPrevSessionValue().getUsedUpVolume());
//		//		lastAmount=Long.parseLong(pRatingMsg.getCommandMsg().getM_strUsedUpVolumeFeeLast());
//				lastAmount=pRatingMsg.getCommandMsg().getM_strUsedUpVolumeFeeLast();
//				lastAmount+=pRatingData.getiPrevSessionValue().getCcgSpEnd();
//				pRatingData.getiCurrSessionValue().setUsedUpVolume(amount+lastAmount);
//				pRatingMsg.getM_iRatingExtMsg().setM_strUpVolumeFeeLast(""+lastAmount);
//				
//			}
//			//处理下行流量
//			if(pRatingMsg.getCommandMsg().getM_strUsedDownVolume()!=0){
//		//		tempValue=Long.parseLong(pRatingMsg.getCommandMsg().getM_strUsedDownVolume());
//				tempValue=pRatingMsg.getCommandMsg().getM_strUsedDownVolume();
//				tempValue+=pRatingData.getiPrevSessionValue().getCcgSpStart();
//				reserveAmount=pRatingData.getiPrevSessionValue().getReserveDownVolume();
//				unUsedAmount=pRatingData.getiPrevSessionValue().getUnUsedDownVolume();
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
//				pRatingMsg.getM_iRatingExtMsg().setM_strDownVolume(""+amount);
//				pRatingData.getiCurrSessionValue().setUnUsedDownVolume(unUsedAmount);
//				pRatingData.getiStartValue().setLnDownVolume(pRatingData.getiPrevSessionValue().getUsedDownVolume());
//	//			lastAmount=Long.parseLong(pRatingMsg.getCommandMsg().getM_strUsedDownVolumeFeeLast());
//				lastAmount=pRatingMsg.getCommandMsg().getM_strUsedDownVolumeFeeLast();
//				lastAmount+=pRatingData.getiPrevSessionValue().getCcgSpEnd();
//				pRatingData.getiCurrSessionValue().setUsedDownVolume(amount+lastAmount);
//				pRatingMsg.getM_iRatingExtMsg().setM_strDownVolumeFeeLast(""+lastAmount);
//				
//			}
//			//处理总流量
//			if(pRatingMsg.getCommandMsg().getM_strUsedTotalVolume()!=0){
//			//	tempValue=Long.parseLong(pRatingMsg.getCommandMsg().getM_strUsedTotalVolume());
//				tempValue=pRatingMsg.getCommandMsg().getM_strUsedTotalVolume();
//				tempValue+=pRatingData.getiPrevSessionValue().getCcgSpStart();
//				reserveAmount=pRatingData.getiPrevSessionValue().getReserveTotalVolume();
//				unUsedAmount=pRatingData.getiPrevSessionValue().getUnUsedTotalVolume();
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
//				pRatingMsg.getM_iRatingExtMsg().setM_strTotalVolume(""+amount);
//				pRatingData.getiCurrSessionValue().setUnUsedTotalVolume(unUsedAmount);
//				pRatingData.getiStartValue().setLnTotalVolume(pRatingData.getiPrevSessionValue().getUsedTotalVolume());
//	//			lastAmount=Long.parseLong(pRatingMsg.getCommandMsg().getM_strUsedTotalVolumeFeeLast());
//				lastAmount=pRatingMsg.getCommandMsg().getM_strUsedTotalVolumeFeeLast();
//				lastAmount+=pRatingData.getiPrevSessionValue().getCcgSpEnd();
//				pRatingData.getiCurrSessionValue().setUsedTotalVolume(amount+lastAmount);
//				pRatingMsg.getM_iRatingExtMsg().setM_strTotalVolumeFeeLast(""+lastAmount);
//				
//			}
//		}
//		if(oper==RatingMacro.OPER_REQ){
////			pRatingData.setiReqRateMeasure(null);
//			RateMeasure rateMeasure=new RateMeasure();
//			rateMeasure=initRateMeasureFromMsg(rateMeasure);
//			pRatingData.setiReqRateMeasure(rateMeasure);
//			
////			initRateMeasureFromMsg(pRatingData.getiReqRateMeasure());
//			System.out.println("***ReqRateMeasure***:"+pRatingData.getiReqRateMeasure());
//		}else{
////			pRatingData.setiRealRateMeasure(null);
//			RateMeasure rateMeasure=new RateMeasure();
//			rateMeasure=initRateMeasureFromMsg(rateMeasure);
//			pRatingData.setiRealRateMeasure(rateMeasure);
////			initRateMeasureFromMsg(pRatingData.getiRealRateMeasure());
//			System.out.println("***RealRateMeasure***:"+pRatingData.getiRealRateMeasure());
//		}
//	}
//	
//	/*
//	 * update
//	 */
//	public RateMeasure initRateMeasureFromMsg(RateMeasure measure){
//		
//		RatingExtMsg ratingExtMsg=pRatingMsg.getM_iRatingExtMsg();
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
////	    System.out.println("*RateMeasure:"+measure);
//		return measure;
//	}
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
////		for(BalanceContent iter :pRatingMsg.getM_iBalanceInMsg().getLtDeduct())
////	    {
////	        setRealFee( iter.getLnAcctItemTypeId(), iter.getnUnitTypeId(), iter.getLnAmount(), nRet );
////	    }
////
////	    //预占信息
////		for(BalanceContent iter :pRatingMsg.getM_iBalanceInMsg().getLtReserve())
////	    {
////			setRealFee( iter.getLnAcctItemTypeId(), iter.getnUnitTypeId(), iter.getLnAmount(), nRet );
////	    }
////		
////	    //返还信息B04
////		for(BalanceContent iter :pRatingMsg.getM_iBalanceInMsg().getLtBack())
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
//	public void setRealFee( int lnFeeTag_,int nFeeType_, int lnFeeValue_, int lnFeeLeftValue ){
//		
//	    List<BalanceContent> temps = new ArrayList<BalanceContent>();
//		BalanceContent iBalanceContent = new BalanceContent();
//	    iBalanceContent.setLnAcctItemTypeId(lnFeeTag_);
//	    iBalanceContent.setnUnitTypeId(nFeeType_); 
//	    iBalanceContent.setLnAmount(lnFeeValue_); 
//	    iBalanceContent.setLnLeftMoney(lnFeeLeftValue);
//	    temps.add(iBalanceContent);
//	    pRatingData.setiRealFees(temps);
//
//	}
//	
//	public int dealSessionInfo(){
//		 int nMsgVersion = pRatingMsg.getM_nMsgVersion();
//		    int nBillingFlag = pRatingMsg.getM_nBillingFlag();
//		    if( pRatingMsg.getBaseMsg().getM_nFeeType() == RatingMacro.NO_FEE_CMD && pRatingMsg.getM_iExtMsg().isM_bFreeFlag() ) //如果是余额释放,并且无批价信息
//		        nBillingFlag=RatingMacro.SESSION_END;//删掉
//
//		    if ( nMsgVersion != RatingMacro.MSG_VER_02 || nBillingFlag > RatingMacro.SESSION_END )
//		        return 0;
//
//
//		    //分别处理Initial、Update、Term消息
//		    int nRet = 0;
//		    switch( nBillingFlag )
//		    {
//		        case RatingMacro.SESSION_BEGIN:
//		            //会话开始，插入会话
//		            pRatingData.getiDCMTrack().start(); 
//		            pRatingData.getiSessionTrackC().start();
//		            
//		            
//		            //nRet = DCMClient::createSession( m_pRatingMsg, m_pRatingData );
//		            pRatingData.getiSessionTrackC().stop();   
//		            pRatingData.getiDCMTrack().stop(); 
//		            if ( nRet < 0 )
//		                return nRet;
//		            break;
//		        case RatingMacro.SESSION_UPDATE:
//		            //会话更新，修改会话
//		        	pRatingData.getiDCMTrack().start(); 
//		        	pRatingData.getiSessionTrackU().start();
//		            //nRet = DCMClient::updateSession( m_pRatingMsg, m_pRatingData );
//		        	pRatingData.getiSessionTrackU().stop();   
//		            pRatingData.getiDCMTrack().stop(); 
//		            if ( nRet < 0 )
//		                return nRet;
//		            break;
//		        case RatingMacro.SESSION_END:
//		            //会话结束，删除会话
//		        	pRatingData.getiDCMTrack().start(); 
//		            pRatingData.getiSessionTrackD().start();
//		            //nRet = DCMClient::deleteSession( m_pRatingMsg->m_iBaseMsg.m_strSessionId );
//		            pRatingData.getiSessionTrackD().stop();   
//		            pRatingData.getiDCMTrack().stop(); 
//		            if ( nRet < 0 )
//		                return nRet;
//		            break;
//		        default:
//		            break;
//		    }
//		
//		
//		return 0;
//	}
//	
//	
//	public int getDeductInfo(List<RespondBalanceUsed> useds,List<DeductInfo> deductInfos){
//		
//		return 0;
//	}
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
//	
//	//待实现
//	public int queryBalanceABM(){
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
//	public int getDeductHistory(){
//		if(pRatingMsg.isNeedQueryDeductHis() && !isQueryBackSuccess()){
//			String strSmId;
//	        List<DeductHistory> iDeductHistory=null;
//
//	        int nMsgType = pRatingMsg.getM_nMsgType();
//	        if( nMsgType == RatingMacro.CODE_ISMP_BRR ) // VAC,ISMP补款
//	            strSmId = pRatingMsg.getBaseMsg().getM_strISMPId();
//	        else if( nMsgType == RatingMacro.CODE_SMS_BRR ) //短信补款
//	            strSmId = pRatingMsg.getBaseMsg().getM_strSmId();
//	        else
//	            return ErrorInfo.ERR_NOT_FOUND_SMS_ID;
//
//	        if( strSmId.isEmpty() )
//	            return ErrorInfo.ERR_NOT_FOUND_SMS_ID;
//	        
//	        iDeductHistory = getDeductHistory( strSmId );
//	        if( null==iDeductHistory  ){
//	        	return ErrorInfo.ERR_NOT_FOUND_DEDUCT_HISTORY;
//	        }
//
//	        boolean bHasRecord = false;
//	        boolean bHasBacked = false;
//	        String strAreaCode="";
//	        String strServiceNbr="";
//	        String strRatableInfo="";
//	        for( DeductHistory history : iDeductHistory){
//	            if( history.getM_nMsgType() != nMsgType )
//	                continue;
//	            if( history.getM_nBackCount() > 0 ){
//	                bHasBacked = true;
//	                continue;
//	            }
//
//	            bHasRecord = true;
//	            strAreaCode = history.getM_strAreaCode();
//	            strServiceNbr = history.getM_strServiceNbr();
//	            strRatableInfo = history.getM_strRatableInfo();
//
//	            BalanceContent iBalanceContent=new BalanceContent();
//
//	            iBalanceContent.setLnAcctItemTypeId(history.getM_lnAcctItemTypeId());
//	            iBalanceContent.setnUnitTypeId(history.getM_nUnitTypeId());
//	            iBalanceContent.setLnAmount(history.getM_lnAmount());
//
//	            //按MsgId进行补款的时候,补到对应的账本上去
//	            iBalanceContent.setLnAcctBalanceId(history.getM_lnAcctBalanceId());
//	            iBalanceContent.setnLatnId(history.getM_nLatnId());
//	            iBalanceContent.setnIsCredit(history.getM_nIsCredit());
//
//	            pRatingData.getiReqFees().add(iBalanceContent);
//	        }
//	        int nRet = 0;
//	        if( bHasRecord ){
////	            int nRet = 0;
//	            //如果查询成功,则替换计费号码
//	            pRatingMsg.getM_iExtMsg().setM_strBillingAreaCode(strAreaCode);
//	            pRatingMsg.getM_iExtMsg().setM_strBillingNbr(strServiceNbr);
//	            pRatingMsg.getBaseMsg().setM_strChargedHomeAreaCode(strAreaCode);
//	            pRatingMsg.getBaseMsg().setM_strChargedNbr(strServiceNbr);
//
//	            //回滚累计量
//	            nRet = rollbackRatable( strRatableInfo );
//	            if ( nRet < 0 )
//	                return nRet;
//
//	            //更新状态
//	            nRet = updateState( strSmId );
//	            if ( nRet < 0 )
//	                return nRet;
//	        }else{
//	        	if( bHasBacked ){
//
//	                return ErrorInfo.ERR_BUSINESS_RECORD_HAS_REFUND;
//	            }
//
//	            return ErrorInfo.ERR_SQL_QUERY_NOT_FOUND_DEDUCT_HISTORY;
//	        }
//	        
//	        if( isQueryBackSuccess() ){
//	            nRet = setBackBalance();
//	            if( nRet < 0 )
//	                return nRet;
//	        }
//	        
//		}
//		return 0;
//	}
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int setBackBalance(){
//		if( pRatingMsg.getBaseMsg().getM_nFeeType()==RatingMacro.BACK_FEE_CMD ){
//	        if( pRatingData.getiReqFees().isEmpty() ){
//	            //如果按扣费记录历史补款,vReq_fee是空,所以日志级别改为MY_WARNING
//	            return ErrorInfo.DC_BERR_BALANCE_BACK_NULL;
//	        }
//
//	        //ISMP直接扣费请求需要先批价,保存批价结果时清空金钱请求信息
//	        pRatingMsg.getM_iBalanceInMsg().getLtBack().clear();
//
//	        for( BalanceContent b :pRatingData.getiReqFees()){
//	            pRatingMsg.getM_iBalanceInMsg().getLtBack().add(b);
//	        }
//	    }
//		
//		return 0;
//	}
//	public int updateState(String smId){
//		
//		return 0;
//	}
//	
//	public int rollbackRatable(String ratableInfo){
//		
//		return 0;
//	}
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param smId
//	 * @return
//	 */
//	public List<DeductHistory> getDeductHistory(String smId){
//		List<DeductHistory> deductHistory=new ArrayList<DeductHistory>();
//		DbUtil db=new DbUtilImpl();
//		List<TbDeductRecordHistory> his=null;
//		his=db.getDeductHistoryBySmid(smId);
//		if(null==his){
//			return null;
//		}
//		for(TbDeductRecordHistory history:his){
//			DeductHistory dh=new DeductHistory();
//			dh.initDeductHistory(history);
//			deductHistory.add(dh);
//		}
//		return deductHistory;
//	}
//	
//	/**
//	 * 判断查询扣费历史是否成功,如果成功就不批价
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public boolean isQueryBackSuccess(){
//		return !pRatingData.getiReqFees().isEmpty();
//	}
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int getSession(){
//	    
//	    int nBillingFlag = pRatingMsg.getM_nBillingFlag();
//	    SessionValue sv=new SessionValue();
//	    
//	    pRatingData.setiPrevSessionValue(sv);
//	    int ret = 0;
//	    //查询会话信息
//	    if(nBillingFlag==RatingMacro.SESSION_END || nBillingFlag==RatingMacro.SESSION_UPDATE){
//	    	ret=getSession(pRatingMsg.getBaseMsg().getM_strSessionId());
//	    	
//	    }
//		
//		return ret;
//	}
//	
//	
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @param sessionId
//	 * @return
//	 */
//	public int getSession(String sessionId){
//		SessionInformationExt session=new SessionInformationExt();
//		DbUtil db=new DbUtilImpl();
//		session=db.getSessionInformationBySessionId(Long.parseLong(sessionId));
//		if(null==session){
//			return ErrorInfo.ERR_SQL_SESSION_INFO_NOT_FOUND;
//		}
//		SessionInfo sessionInfo=new SessionInfo();
//		sessionInfo.sessionInit(session);
//		pRatingData.setStrPayPlanInfo(session.getPay_Plan_Info());
//		pRatingData.setStrPricingPlanInfo(session.getPricing_Plan_Info());
//		pRatingData.setStrUnusedMoneyInfo(session.getUnused_Money());
//		pRatingData.getiSessionInfos().add(sessionInfo);
//		pRatingData.getiPrevSessionValue().setTabUsedDuration(session.getUsed_Duration());
//		pRatingData.getiPrevSessionValue().setTabUsedTimes(session.getUsed_Times());
//		pRatingData.getiPrevSessionValue().setTabUsedTotalVolume(session.getUsed_Totalvolume());
//		pRatingData.getiPrevSessionValue().setTabUsedUpVolume(session.getUsed_Upvolume());
//		pRatingData.getiPrevSessionValue().setTabUsedDownVolume(session.getUsed_Downvolume());
//		int unitType=session.getUnit_Type();
//		switch(unitType){
//		case RatingMacro.CREDITUNIT_MONEY:
//            pRatingData.getiPrevSessionValue().setReserveMoney(session.getReserved_Amount());
//            pRatingData.getiPrevSessionValue().setUsedMoney(session.getUsed_Amount());
//            pRatingData.getiPrevSessionValue().setUnUsedMoney(session.getUnused_Amount());
//            break;
//        case RatingMacro.CREDITUNIT_TIMELEN:
//            pRatingData.getiPrevSessionValue().setReserveDuration(session.getReserved_Amount());
//            pRatingData.getiPrevSessionValue().setUsedDuration(session.getUsed_Amount());
//            pRatingData.getiPrevSessionValue().setUnUsedDuration(session.getUnused_Amount());
//            break;
//        case RatingMacro.CREDITUNIT_TOTALVAL:
//            pRatingData.getiPrevSessionValue().setReserveTotalVolume(session.getReserved_Amount());
//            pRatingData.getiPrevSessionValue().setUsedTotalVolume(session.getUsed_Amount());
//            pRatingData.getiPrevSessionValue().setUnUsedTotalVolume(session.getUnused_Amount());
//            pRatingData.getiPrevSessionValue().setCcgSpStart(session.getSwitchpoint_Start());
//            pRatingData.getiPrevSessionValue().setCcgSpEnd(session.getSwitchpoint_End());
//            break;
//        case RatingMacro.CREDITUNIT_UPVAL:
//            pRatingData.getiPrevSessionValue().setReserveUpVolume(session.getReserved_Amount());
//            pRatingData.getiPrevSessionValue().setUsedUpVolume(session.getUsed_Amount());
//            pRatingData.getiPrevSessionValue().setUnUsedUpVolume(session.getUnused_Amount());
//            pRatingData.getiPrevSessionValue().setCcgSpStart(session.getSwitchpoint_Start());
//            pRatingData.getiPrevSessionValue().setCcgSpEnd(session.getSwitchpoint_End());
//            break;
//        case RatingMacro.CREDITUNIT_DOWNVAL:
//            pRatingData.getiPrevSessionValue().setReserveDownVolume(session.getReserved_Amount());
//            pRatingData.getiPrevSessionValue().setUsedDownVolume(session.getUsed_Amount());
//            pRatingData.getiPrevSessionValue().setUnUsedDownVolume(session.getUnused_Amount());
//            pRatingData.getiPrevSessionValue().setCcgSpStart(session.getSwitchpoint_Start());
//            pRatingData.getiPrevSessionValue().setCcgSpEnd(session.getSwitchpoint_End());
//            break;
//        case RatingMacro.CREDITUNIT_COUNT:
//            pRatingData.getiPrevSessionValue().setReserveTimes(session.getReserved_Amount());
//            pRatingData.getiPrevSessionValue().setUsedTimes(session.getUsed_Amount());
//            pRatingData.getiPrevSessionValue().setUnUsedTimes(session.getUnused_Amount());
//            break;
//        default: 
//            break;
//		}
//		
//		return 0;
//	}
//	/**
//	 * 
//	 * @author sung
//	 *
//	 * @return
//	 */
//	public int setMergeBalanceReqMsg(){
//		if(pRatingMsg.getM_iBalanceInMsg().isbIsQueryBalance()){
//			BalanceContent iBalanceContent=new BalanceContent();
//	        pRatingMsg.getM_iBalanceInMsg().getLtQuery().clear();
//	        iBalanceContent.setLnAcctItemTypeId(-1);
//	        iBalanceContent.setnUnitTypeId(-1);
//	        iBalanceContent.setLnAmount(-1);
//	        pRatingMsg.getM_iBalanceInMsg().getLtQuery().add(iBalanceContent);
//	        
//		}
//		
//		int nRet = 0;
//	    if( ( nRet = setMergeBalReserve() ) < 0 && ( pRatingData.getnServiceLimitFlag() == -1 ) )
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
//	/*
//	 * update 
//	 */
//	private int setMergeBalBack(){
//		if(pRatingMsg.getBaseMsg().getM_nFeeType()==RatingMacro.BACK_FEE_CMD){
//			if(pRatingData.getiReqFees().isEmpty()){
//				return ErrorInfo.DC_BERR_BALANCE_BACK_NULL;
//			}
//			//ISMP直接扣费请求需要先批价,保存批价结果时清空金钱请求信息
//			pRatingMsg.getM_iBalanceInMsg().getLtBack().clear();
//			for(BalanceContent p :pRatingData.getiReqFees()){
//				pRatingMsg.getM_iBalanceInMsg().getLtBack().add(p);
//				
//			}
//			
//		}
//		return 0;
//	}
//	
//	/*
//	 * update  
//	 */
//	private int setMergeBalDeduct(){
//		if(pRatingMsg.getBaseMsg().getM_nFeeType()==RatingMacro.REAL_FEE_CMD){
//			if(pRatingData.getiRealFees().isEmpty()){
//				return ErrorInfo.DC_BERR_BALANCE_DEDUCT_NULL;
//			}
//			//ISMP直接扣费请求需要先批价,保存批价结果时清空金钱请求信息
//			pRatingMsg.getM_iBalanceInMsg().getLtDeduct().clear();
//			for(BalanceContent p :pRatingData.getiRealFees()){
//				pRatingMsg.getM_iBalanceInMsg().getLtDeduct().add(p);
//			}
//		}
//		
//		return 0;
//	}
//	
//	
//	/*
//	 * update 
//	 */
//	public int setMergeBalReserve(){
//		if(pRatingMsg.getBaseMsg().getM_nFeeType() == RatingMacro.REQ_FEE_CMD){
//			if(pRatingData.getiReqFees().isEmpty()){
//				return ErrorInfo.DC_BERR_BALANCE_RESERVE_NULL;
//			}
//			for(BalanceContent p :pRatingData.getiReqFees()){
//				pRatingMsg.getM_iBalanceInMsg().getLtReserve().add(p);
//			}
//			
//		}
//		
//		return 0;
//	}
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
//			for(FeeInfo f : pRatingData.getiFeeInfos()){
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
//	/**
//	 * 
//	 * @param oper
//	 */
//	public void setRBMessage(int oper){
//		if(oper==RatingMacro.OPER_REQ){
//			RateMeasure iRateMeasure=pRatingData.getiRateMeasure();
//			if(iRateMeasure.getLnDuration()!=0){
//				pRatingMsg.getM_iRatingOutMsg().setM_strAthDuration(""+iRateMeasure.getLnDuration());
//				
//			}
//			if(iRateMeasure.getLnTimes()!=0){
//				pRatingMsg.getM_iRatingOutMsg().setM_strAthTimes(""+iRateMeasure.getLnTimes());
//			}
//			if(iRateMeasure.getLnUpVolume()!=0){
//				pRatingMsg.getM_iRatingOutMsg().setM_strAthUpVolume(""+iRateMeasure.getLnUpVolume());
//			}
//			if(iRateMeasure.getLnDownVolume()!=0){
//				pRatingMsg.getM_iRatingOutMsg().setM_strAthDownVolume(""+iRateMeasure.getLnDownVolume());
//			}
//			if(iRateMeasure.getLnTotalVolume()!=0){
//				pRatingMsg.getM_iRatingOutMsg().setM_strAthTotalVolume(""+iRateMeasure.getLnTotalVolume());
//			}
//			//需要从帐户中扣除的信用,处理那些余额不足的信息,否则不需要补充0
//			for(ChargeUnit u:pRatingData.getiMergedReqChargeUnits()){
//				setReqFee(u);
//				
//			}
//			System.out.println("**OPER_REQ***RatingOutMsg:"+pRatingMsg.getM_iRatingOutMsg());
//		}else if(oper==RatingMacro.OPER_REAL){
//			//需要从帐户中扣除的信用,处理那些余额不足的信息,否则不需要补充0
//			
//			Fee iFee=new Fee();
//	        for( ChargeUnit u: pRatingData.getiMergedRealChargeUnits())
//	        {
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
//		}
//		
//	}
//	
//	/*
//	 * update
//	 */
//	private boolean setUnusedMoneyFromSession(){
//		StringBuffer sf=new StringBuffer("");
//		if(pRatingData.getnFeeType()==RatingMacro.REAL_REQ_FEE && pRatingData.getnOper()==RatingMacro.OPER_REAL){
//			pRatingData.setStrUnusedMoneyInfo("");
//			for(Fee f:pRatingData.getiUnusedMoneys()){
//				sf.append(f.getLnAcctItemTypeId()).append(f.getnUnitType()).append(f.getLnTariff());
//				pRatingData.setStrUnusedMoneyInfo(pRatingData.getStrUnusedMoneyInfo()+sf.toString());
//			}
//			
//		}
//		return true;
//	}
//	
//	/*
//	 * update
//	 */
//	private int updateUnusedMoney(Fee fee){
//		boolean find=false;
//		if(fee.getnUnitType()!=2){   //非金钱账本
//			pRatingData.getiUnusedMoneys().clear();
//			return 0;
//		}else{
//			for(Fee f:pRatingData.getiUnusedMoneys()){
//				if(f.getLnAcctItemTypeId()==fee.getLnAcctItemTypeId() && f.getnUnitType()==fee.getnUnitType()){
//					f.setLnTariff(fee.getLnTariff());
//					find=true;
//					break;
//				}
//			}
//			
//			if(!find && fee.getLnTariff()!=0){
//				pRatingData.getiUnusedMoneys().add(fee);
//			}
//		}
//		return 0;
//	}
//	
//	
//	/*
//	 * update
//	 */
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
//	    pRatingData.getiRealFees().add(iBalanceContent);
//	}
//	
//	/*
//	 * update
//	 */
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
//	    pRatingData.getiReqFees().add(iBalanceContent);
//	    
//	    
//	}
//	
//	/*
//	 * update
//	 */
//	public int loadUnusedData(RateData rateData,int operType){
//		for(OfrRateData ofr:rateData.getiOfrResults()){
//			for(SectionRateData section:ofr.getiSectionResults()){
//				if(!pRatingMsg.getM_iRatingExtMsg().getM_strDuration().equals("") && section.getStrMeasureDomain().equals("1")){
//					if(operType==RatingMacro.OPER_REAL){
//						long tmp=pRatingData.getiCurrSessionValue().getUnUsedDuration();
//						tmp+=section.getLnUnusedDosage();
//						pRatingData.getiCurrSessionValue().setUnUsedDuration(tmp);
//						tmp=pRatingData.getiCurrSessionValue().getUsedDuration();
//						tmp+=section.getLnRatableUnusedDosage();
//						pRatingData.getiCurrSessionValue().setUsedDuration(tmp);
//						
//					}
//					pRatingData.setLnReqDuration(pRatingData.getLnReqDuration()+section.getLnRatableUnusedDosage());
//					
//				}
//				if(!pRatingMsg.getM_iRatingExtMsg().getM_strUpVolume().equals("") && section.getStrMeasureDomain().equals("4")){
//					if(operType==RatingMacro.OPER_REAL ){
//						long tmp=pRatingData.getiCurrSessionValue().getUnUsedUpVolume();
//						tmp+=section.getLnUnusedDosage();
//						pRatingData.getiCurrSessionValue().setUnUsedUpVolume(tmp);
//						tmp=pRatingData.getiCurrSessionValue().getUsedUpVolume();
//						tmp+=section.getLnRatableUnusedDosage();
//						pRatingData.getiCurrSessionValue().setUsedUpVolume(tmp);
//						
//					}
//					pRatingData.setLnReqUpVolume(pRatingData.getLnReqUpVolume()+section.getLnRatableUnusedDosage());
//				}
//				if(!pRatingMsg.getM_iRatingExtMsg().getM_strDownVolume().equals("") && section.getStrMeasureDomain().equals("5")){
//					if(operType==RatingMacro.OPER_REAL){
//						long tmp=pRatingData.getiCurrSessionValue().getUnUsedDownVolume();
//						tmp+=section.getLnUnusedDosage();
//						pRatingData.getiCurrSessionValue().setUnUsedDownVolume(tmp);
//						tmp=pRatingData.getiCurrSessionValue().getUsedDownVolume();
//						tmp+=section.getLnRatableUnusedDosage();
//						pRatingData.getiCurrSessionValue().setUsedDownVolume(tmp);
//					}
//					pRatingData.setLnReqDownVolume(pRatingData.getLnReqDownVolume()+section.getLnRatableUnusedDosage());
//				}
//				if(!pRatingMsg.getM_iRatingExtMsg().getM_strTotalVolume().equals("") && section.getStrMeasureDomain().equals("2")){
//					if(operType==RatingMacro.OPER_REAL){
//						long tmp=pRatingData.getiCurrSessionValue().getUnUsedTotalVolume();
//						tmp+=section.getLnUnusedDosage();
//						pRatingData.getiCurrSessionValue().setUnUsedTotalVolume(tmp);
//						tmp=pRatingData.getiCurrSessionValue().getUsedTotalVolume();
//						tmp+=section.getLnRatableUnusedDosage();
//						pRatingData.getiCurrSessionValue().setUsedTotalVolume(tmp);
//					}
//					pRatingData.setLnReqTotalVolume(pRatingData.getLnReqTotalVolume()+section.getLnRatableUnusedDosage());
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
//	/*
//	 * update
//	 */
//	public void setRatingSpanPoint(){
//		
//		if(!pRatingData.getStrSpanTime().equals("")){
//			if(!pRatingData.getStrSpanTime().equals("NO")){
//				String str=pRatingData.getStrSpanTime();
//				pRatingMsg.getM_iRatingOutMsg().setM_strRatingSpanPoint(str);
//			}
//		}
//		
//		
//	}
//
//	
//	
//	public void mergeChargeUnits(int oper){
//		
//		
//		
//	}
//	
//	public int calcFeeTerm(RateData data_)
//	{
//
//
//		return 0;
//
//	}
//	
//	public int ratingTerm(){
//		int nRet = 0;
//		getTermRateMeasure(ParamData.OPER_REAL);
//		pRatingData.setnOper(ParamData.OPER_REAL);
//		RateData iRateData = null;
//	    pRatingData.setiRateMeasure(pRatingData.getiRealRateMeasure());
//	    nRet = calcFeeTerm(iRateData);
//	    if( nRet < 0 )
//	        return nRet;
//	    ///更新B07
//	    for(ChargeUnit iter :pRatingData.getiChargeUnits()){
//	    	if( iter.getnMTChangeFlag() == 0  ) //MT转换的金钱不计入
//	        {
//	    		updateFeeInfo(iter.getnUnitType(),iter.getnOfrId(),iter.getLnOriCharge());
//	        }
//	    	
//	    }
//	    loadUnusedData( iRateData, RatingMacro.OPER_REAL  );
//	    setRatingSpanPoint();
//	    setRBMessage( pRatingData.getnOper() );
//
//	    setMergeBalanceReqMsg();
//	    
//	    int nBillingFlag = pRatingMsg.getM_nBillingFlag();
//	    if( nBillingFlag == RatingMacro.SESSION_END)
//	    {
//	    	ChargeTermReq iChargeReq=new ChargeTermReq();
//	    	ChargeTermResp iChargeResp = null;
//	        pRatingData.getiBalanceTrack().start(); 
////	        nRet = balanceForTerm(iChargeResp);
//	        
//	        
//	        
//	        iChargeReq.setM_strSessionId(pRatingMsg.getBaseMsg().getM_strSessionId());
//	        iChargeReq.setM_ulnServId(Long.parseLong(pRatingMsg.getM_iUserMsg().getLnServId()));
//	        iChargeReq.setM_nLatnId(pRatingMsg.getM_iUserMsg().getnLatnId());
//	        iChargeReq.setM_strEventTimeStamp(""+pRatingMsg.getEventtime());
//	        
//	        
//	        //iChargeResp=balanceTerm(iChargeReq);
//	        
//	        pRatingData.getiBalanceTrack().stop();
//	        if( nRet < 0 )
//	            //return nRet;
//
//	        //检查返回结果中账本扣除情况和错误码，确定是否成功全部扣除
//	        if( iChargeResp.getUnResultCode() != 0 )
//	        {
//	             int nErrCode = iChargeResp.getUnResultCode();
//	            // if( nErrCode != ABM_ERR_RES_BALANCE_NOT_ENOUGH_REAL
//	            //     )
//	            //     return NO_ENOUGH_BALANCE;
//
//	            //if( nErrCode == ABM_ERR_RES_BALANCE_NOT_ENOUGH_REAL )
//	            //{
//	                //TODO:处理中间更新中扣费，实扣大于预占且资源账本余额不足,需要反算扣费金额，重新发送到ABM
//	            //}
//	            //else
//	            //{
//	              
//	                //return ERR_IN_BALANCE;
//	            //}
//	        }
//	    }
//	    else //事件类扣费
//	    {
//	        BalanceDeductResp iChargeResp = null;
//	        pRatingData.getiBalanceTrack().start(); 
//	        nRet = balanceForTermEvent(iChargeResp);
//	        pRatingData.getiBalanceTrack().stop();
//	        if( nRet < 0 )
//	            return nRet;
//
//	        //检查返回结果中账本扣除情况和错误码，确定是否成功全部扣除
//	        if( iChargeResp.getM_unResultCode() != 0 )
//	        {
//	            return RatingErrorCode.ERR_IN_BALANCE;
//	        }
//	    }
//	    
//	    int nFeeType = pRatingData.getnFeeType();
//	    if( nFeeType != RatingMacro.ONLY_REQ_FEE && nFeeType != RatingMacro.NO_OPER_FEE )
//	        balanceAocManger();
//
//	    //设置session_information_ext
//	    if( ( nRet = dealSessionInfo() ) < 0 )
//	        return nRet;
//
//	    //设置金钱类RB返回信息
//	    if( pRatingMsg.getM_iBalanceInMsg().isbIsMoneyRequest() 
//	            || pRatingMsg.isNeedCheckTopValue()
//	            || pRatingMsg.isNeedCheckRepetition() 
//	            || pRatingMsg.getM_iBalanceInMsg().getnRealCtrlType() == 5
//	      )
//	    {
//	        setRetMessage();
//	    }
//
//	    //如果是小区优惠需要更新tb_prd_prd_inst_attr
//	    if( pRatingMsg.getBaseMsg().getM_nIsUpdateAttr() == 2 )
//	    {
//	        //nRet = updatePrdInstAttr( pRatingMsg.getBaseMsg().getM_lnPrdAttrAccNbr() , pRatingMsg.getBaseMsg().getM_nPrdAttrLatnId(), pRatingMsg.getBaseMsg().getM_nAttrCount() );
//	        //if( nRet < 0 )
//	            //return nRet;
//	    }
//	    else if( pRatingMsg.getBaseMsg().getM_nIsUpdateAttr() == 1 )
//	    {
//	       // nRet = insertPrdInstAttr( pRatingMsg.getBaseMsg().getM_lnPrdAttrAccNbr() ,pRatingMsg.getBaseMsg().getM_nPrdAttrLatnId() , pRatingMsg.getBaseMsg().getM_nAttrCount()  );
//	       // if( nRet < 0 )
//	            //return nRet;
//	    }
//	    
//	    
//	    
//	    return 0;
//	}
//	
//	
//	//public ChargeTermResp balanceTerm(ChargeTermReq req){
//	//	prepareForBalance();
////		if( !m_pRatingMsg->isNeedRating() )
////	    {
////	        if( ( nRet = getSession() ) < 0 )
////	            return nRet;
////
////	        //Ö±½Ó¿Û·ÑÀàµÄ²¹¿îÊ±¿Û·ÑÀúÊ·¼ÇÂ¼±í²éÑ¯
////	        if( ( nRet = getDeductHistory() ) < 0 )
////	            return nRet;
////
////	        if( ( nRet = queryBalanceABM() ) < 0 )
////	            return nRet;
////	    }
////
////	    //·â¶¥Öµ¼ì²é
////	    if( m_pRatingMsg->isNeedCheckTopValue() )
////	    {
////	        if( ( nRet = checkTopValue() ) < 0 )
////	            return nRet;
////	    }
////
////	    //ÌÞÖØ
////	    if( m_pRatingMsg->isNeedCheckRepetition() || isFmtCheckRepetition() )
////	    {
////	        nRet = checkRepetition();
////	        if( nRet < 0 )
////	            return nRet;
////	        if( nRet == 1 )
////	            setDeductZero();
////	        else if( nRet == 0 )
////	            setRepetition();
////	    }
////
////	    //·â¶¥Öµ¼ì²é²¹¿î //ÔÚÏßÄ£¿é±ØÐëÊÇISMPÒµÎñ£¬ÀëÏßÄ£¿é±ØÐëÊÇ20ÒµÎñ¡£
////	    if( m_pRatingMsg->getBillingFlag() == EVENT_BACK )
////	    {
////	        nRet = backTopRepetition();
////	        if( nRet < 0 )
////	            return nRet;
////	    }
////
////	    //ÉèÖÃUpdateÊ±µÄB30×é¿Û·ÑÐÅÏ¢
////	    if( m_pRatingMsg->m_iBalanceInMsg.m_bIsMoneyRequest )
////	        setDeductMessage();
////
////	    //Ö±½Ó¿Û·Ñ¸üÐÂÀÛ»ýÁ¿
////	    if( m_pConf->getDirectRatableFlag() == 1 )
////	    {
////	        if( ( nRet = updateDirectMoneyRatable() ) < 0 )
////	            return nRet;
////	    }
////
////	    //Óà¶î³äÖµ
////	    if( ( nRet = balanceRecharge() ) < 0 )
////	    {
////	        return nRet;
////	    }
////
////	    //Óà¶î·µ»¹
////	    if( ( nRet = balanceBack() ) < 0 )
////	    {
////	        return nRet;
////	    }
////
////	    //Óà¶îÊÍ·Å
////	    if( ( nRet = balanceFree() ) < 0 )
////	        return nRet;
//		
//		//return new BalanceChargeTerminal( req).deal();
//	//}
//	
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
//	 * @return
//	 */
//	public BalanceDeductResp balanceEvent(){
//		prepareForBalance();
//		if( !pRatingMsg.isNeedRating() )
//	    {
//	        if( ( nRet = getSession() ) < 0 )
//	            return null;
//
//	        //直接扣费类的补款时扣费历史记录表查询
//	        if( ( nRet = getDeductHistory() ) < 0 )
//	            return null;
//
//	        if( ( nRet = queryBalanceABM() ) < 0 )
//	            return null;
//	    }
//		
//		
//		
//		
//		return null;
//	}
//	
//	
//	
//	
//	
//	
//	
//	int balanceForTermEvent(BalanceDeductResp pChargeResp){
//		
//		
//		
//		return 0;
//	}
//	
//	
//	int balanceAocManger()
//	{
//	    //TODO:实现 balanceAocManger (总部短信提醒信规范)
//	    return 0;
//	}
//
//	
//	
//	
//	/*
//	 * update 
//	 */
//	
//	public int prepareForBalance()
//	{
//	    int nBillingFlag = pRatingMsg.getM_nBillingFlag();
//	    int nMsgType =  pRatingMsg.getM_nMsgType();
//
//	    BalanceData iBalanceData = pRatingData.getiBalanceData();
//
//	    if ( nBillingFlag == RatingMacro.EVENT_FEE || nBillingFlag == RatingMacro.EVENT_FEE || nBillingFlag == RatingMacro.SESSION_BEGIN ){
//	        iBalanceData.setM_strTimeStamp(pRatingMsg.getVarMsg().getM_strCurrTime()); 
//	    }else{
//	        if ( RatingMacro.CODE_CCG_BRR == nMsgType && RatingMacro.SESSION_BEGIN != nBillingFlag )
//	        	iBalanceData.setM_strTimeStamp(pRatingMsg.getVarMsg().getM_strLastTime());
//	        else if ( RatingMacro.CODE_IN_BRR == nMsgType && nBillingFlag != RatingMacro.SESSION_END )
//	        	iBalanceData.setM_strTimeStamp(pRatingMsg.getVarMsg().getM_strCurrTime()); 
//	        else
//	        	iBalanceData.setM_strTimeStamp(pRatingMsg.getBaseMsg().getM_strStartTime());
//	    }
//
//	    if ( iBalanceData.getM_strTimeStamp().isEmpty() ){
//	    	iBalanceData.setM_strTimeStamp(DateUtil.getCurrentTime());
//	    }
//
//	    iBalanceData.setM_strSessionId(pRatingMsg.getBaseMsg().getM_strSessionId());
//	    iBalanceData.setM_strAreaCode(pRatingMsg.getBaseMsg().getM_strChargedHomeAreaCode());
//	    iBalanceData.setM_strAccNbr(pRatingMsg.getBaseMsg().getM_strChargedNbr());
//	    iBalanceData.setM_lnServId(pRatingMsg.getM_iChargedMsg().getM_lnChargedServId());
//	    iBalanceData.setM_lnAcctId(pRatingMsg.getM_iChargedMsg().getM_lnChargedAcctId());
//	    iBalanceData.setM_lnCustId(Long.parseLong(pRatingMsg.getM_iChargedMsg().getM_lnChargedCustId()));
//	    iBalanceData.setM_nLatnId(pRatingMsg.getM_iChargedMsg().getM_nChargedLatnId());
//	    iBalanceData.setM_nBillingFlag(nBillingFlag);
//	    iBalanceData.setM_nMsgType(nMsgType);
//	    iBalanceData.setM_bFreeFlag(pRatingMsg.getM_iExtMsg().isM_bFreeFlag());
//	   
//		pRatingData.setiBalanceData(iBalanceData);
//		return 0;
//	}
//	
//	public int balanceForTerm(ChargeTermResp pChargeResp)
//	{
//		 int nRet = 0;
//
//		    nRet = prepareForBalance();
//		    if( nRet < 0 )
//		        return nRet;
//
//		    if( !pRatingMsg.isNeedRating() )
//		    {
//		       // if( ( nRet = getSession() ) < 0 )
//		           // return nRet;
//
//		        //直接扣费类的补款时扣费历史记录表查询
//		        //if( ( nRet = getDeductHistory() ) < 0 )
//		            //return nRet;
//
//		        //if( ( nRet = queryBalanceABM() ) < 0 )
//		            //return nRet;
//		    }
//
//		    //封顶值检查
//		    if( pRatingMsg.isNeedCheckTopValue() )
//		    {
//		        //if( ( nRet = checkTopValue() ) < 0 )
//		            return nRet;
//		    }
//
//		    //剔重
////		    if( pRatingMsg.isNeedCheckRepetition() || isFmtCheckRepetition() )
////		    {
////		        nRet = checkRepetition();
////		        if( nRet < 0 )
////		            return nRet;
////		        if( nRet == 1 )
////		            setDeductZero();
////		        else if( nRet == 0 )
////		            setRepetition();
////		    }
//
//		    //封顶值检查补款 //在线模块必须是ISMP业务，离线模块必须是20业务。
//		    if( pRatingMsg.getM_nBillingFlag() == RatingMacro.EVENT_BACK )
//		    {
//		        //nRet = backTopRepetition();
//		        if( nRet < 0 )
//		            return nRet;
//		    }
//
//		    //设置Update时的B30组扣费信息
//		    //if( pRatingMsg.getM_iBalanceInMsg().isbIsMoneyRequest())
//		        //setDeductMessage();
//
//		    //直接扣费更新累积量
//		    //if( m_pConf.getDirectRatableFlag() == 1 )
//		    //{
//		        //if( ( nRet = updateDirectMoneyRatable() ) < 0 )
//		            //return nRet;
//		    //}
//
//		    //余额充值
//		    //if( ( nRet = balanceRecharge() ) < 0 )
//		    //{
//		       // return nRet;
//		    //}
//
//		    //余额返还
//		    //if( ( nRet = balanceBack() ) < 0 )
//		    //{
//		    //    return nRet;
//		    //}
//
//		    //余额释放
//		   // if( ( nRet = balanceFree() ) < 0 )
//		        //return nRet;
//
////		    int nBillingFlag = pRatingMsg.getM_nBillingFlag();
//		    //if( nBillingFlag == RatingMacro.EVENT_FEE )
//		       // nRet = balanceChargeTerm(pChargeResp);
//		    //else
//		       // nRet = balanceChargeTerm(pChargeResp);
//
//		    
//		
//		
//			return 0;
//	}
//	
//	
//	public void getTermRateMeasure (int nOper){
//		//处理时间
//		pRatingMsg.getM_iRatingExtMsg().setM_strExtStartTime(pRatingMsg.getBaseMsg().getM_strStartTime());
//		pRatingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(pRatingMsg.getVarMsg().getM_strCurrTime());
//		pRatingMsg.getM_iRatingExtMsg().setM_strExtLastTime(pRatingMsg.getVarMsg().getM_strLastTime());
//	    //获取消息版本及会话类型
//	    int nMsgVersion = pRatingMsg.getM_nMsgVersion();
//	    String szTmp = "";
//	    long lnAmount = 0;
//	    long lnTempValue = 0;
//	    long lnUnUsedAmount = 0;
//	    long lnReservedAmount = 0;
//	    long lnLastAmount =0;
//
//	    //因为每次Update都要实扣，所以取上次扣费时间作为本次实扣开始时间
//	    pRatingMsg.getM_iRatingExtMsg().setM_strExtCurrTime(pRatingMsg.getVarMsg().getM_strLastTime());
//	    
//	    if( nMsgVersion == ParamData.OPER_REAL)
//	    {
//	        //Term消息通过总量-已使用量得到
//	        //处理时长
//	        if ( pRatingMsg.getCommandMsg().getM_strRealDuration()!=0)
//	        {
//	            
//	        	lnAmount=pRatingMsg.getCommandMsg().getM_strRealDuration()-pRatingData.getiPrevSessionValue().getUsedDuration();
//	            if ( lnAmount < 0 )
//	            {
//	                lnAmount = 0;
//	            }
//	            pRatingMsg.getM_iRatingExtMsg().setM_strDuration(szTmp);
//	            pRatingData.getiCurrSessionValue().setUsedDuration(lnAmount);
//	            pRatingData.getiStartValue().setLnDuration(pRatingData.getiPrevSessionValue().getUsedDuration());
//	           
//	          
//	        }
//	        
//	        //处理次数
//	        if ( pRatingMsg.getCommandMsg().getM_strRealTimes() !=0 )
//	        {
//	            lnAmount=pRatingMsg.getCommandMsg().getM_strRealTimes()-pRatingData.getiPrevSessionValue().getUsedTimes();
//	            if ( lnAmount < 0 )
//	            {
//	                lnAmount = 0;
//	            }
//	            pRatingMsg.getM_iRatingExtMsg().setM_strTimes(szTmp);
//	            pRatingData.getiCurrSessionValue().setUsedTimes(lnAmount);
//	            pRatingData.getiStartValue().setLnTimes(pRatingData.getiPrevSessionValue().getUsedTimes());
//	           
//	        }
//	        
//	        
//	        //处理上行流量
//	        if (  pRatingMsg.getCommandMsg().getM_strUsedUpVolume() !=0  )
//	        {
//	            lnTempValue = pRatingMsg.getCommandMsg().getM_strUsedUpVolume();
//	            //加上 上次未成功扣费的B307信息
//	            lnTempValue += pRatingData.getiPrevSessionValue().getCcgSpStart();
//	            lnReservedAmount = pRatingData.getiPrevSessionValue().getReserveUpVolume();
//	            lnUnUsedAmount = pRatingData.getiPrevSessionValue().getUnUsedUpVolume();
//	            lnTempValue -= lnUnUsedAmount;
//	            if ( lnTempValue <= 0 )
//	            {
//	                lnAmount = 0;
//	                lnUnUsedAmount = -lnTempValue;
//	            }
//	            else if ( lnTempValue >= lnReservedAmount )
//	            {
//	                lnAmount = lnTempValue;
//	                lnUnUsedAmount = 0;
//	            }
//	            else
//	            {
//	                lnAmount=lnTempValue;
//	                lnUnUsedAmount=0;
//	            }
//	            pRatingMsg.getM_iRatingExtMsg().setM_strUpVolume(szTmp);
//	            pRatingData.getiCurrSessionValue().setUnUsedUpVolume(lnUnUsedAmount);
//	            pRatingData.getiStartValue().setLnUpVolume(pRatingData.getiPrevSessionValue().getUsedUpVolume());
//	            lnLastAmount = pRatingMsg.getCommandMsg().getM_strUsedUpVolumeFeeLast();
//	            //加上 上次未扣费成功的B308
//	            lnLastAmount += pRatingData.getiPrevSessionValue().getCcgSpEnd();
//	            pRatingData.getiCurrSessionValue().setUsedUpVolume(lnAmount + lnLastAmount);
//	            szTmp="0";
//	            pRatingMsg.getM_iRatingExtMsg().setM_strUpVolumeFeeLast(szTmp);
//	        }
//	        
//	        //处理下行流量
//	        if ( pRatingMsg.getCommandMsg().getM_strUsedDownVolume() !=0  )
//	        {
//	            lnTempValue = pRatingMsg.getCommandMsg().getM_strUsedDownVolume();
//	            //加上 上次未成功扣费的B307信息
//	            lnTempValue += pRatingData.getiPrevSessionValue().getCcgSpStart();
//
//	            lnReservedAmount = pRatingData.getiPrevSessionValue().getReserveDownVolume();
//	            lnUnUsedAmount = pRatingData.getiPrevSessionValue().getUnUsedDownVolume();
//	            lnTempValue -= lnUnUsedAmount;
//	            if ( lnTempValue <= 0 )
//	            {
//	                lnAmount = 0;
//	                lnUnUsedAmount = -lnTempValue;
//	            }
//	            else if ( lnTempValue >= lnReservedAmount )
//	            {
//	                lnAmount = lnTempValue;
//	                lnUnUsedAmount = 0;
//	            }
//	            else
//	            {
//	                lnAmount=lnTempValue;
//	                lnUnUsedAmount=0;
//	            }
//	            pRatingMsg.getM_iRatingExtMsg().setM_strDownVolume(szTmp);
//	            pRatingData.getiCurrSessionValue().setUsedDownVolume(lnAmount); 
//	            pRatingData.getiCurrSessionValue().setUnUsedDownVolume(lnUnUsedAmount);
//	            pRatingData.getiStartValue().setLnDownVolume(pRatingData.getiPrevSessionValue().getUsedDownVolume());
//	            lnLastAmount = pRatingMsg.getCommandMsg().getM_strUsedDownVolumeFeeLast();
//	            //加上 上次未扣费成功的B308
//	            lnLastAmount += pRatingData.getiPrevSessionValue().getCcgSpEnd();
//	            pRatingData.getiCurrSessionValue().setUsedDownVolume(lnAmount +lnLastAmount);
//	            szTmp="0";
//	            pRatingMsg.getM_iRatingExtMsg().setM_strDownVolumeFeeLast(szTmp);
//	        }
//	        
//	        
//        //处理总流量
//        if ( pRatingMsg.getCommandMsg().getM_strUsedTotalVolume() !=0 )
//        {
//            lnTempValue = pRatingMsg.getCommandMsg().getM_strUsedTotalVolume();
//            //加上 上次未成功扣费的B307信息
//            lnTempValue += pRatingData.getiPrevSessionValue().getCcgSpStart();
//
//            lnReservedAmount = pRatingData.getiPrevSessionValue().getReserveTotalVolume();
//            lnUnUsedAmount = pRatingData.getiPrevSessionValue().getUnUsedTotalVolume(); 
//            lnTempValue -= lnUnUsedAmount;
//            if ( lnTempValue <= 0 )
//            {
//                lnAmount = 0;
//                lnUnUsedAmount = -lnTempValue;
//            }
//            else if ( lnTempValue >= lnReservedAmount )
//            {
//                lnAmount = lnTempValue;
//                lnUnUsedAmount = 0;
//            }
//            else
//            {
//                lnAmount=lnTempValue;
//                lnUnUsedAmount=0;
//            }
//
//            pRatingMsg.getM_iRatingExtMsg().setM_strTotalVolume(szTmp);
//            
//            pRatingData.getiCurrSessionValue().setUnUsedTotalVolume(lnUnUsedAmount); 
//            pRatingData.getiStartValue().setLnTotalVolume(pRatingData.getiPrevSessionValue().getUsedTotalVolume()); 
//            lnLastAmount = pRatingMsg.getCommandMsg().getM_strUsedTotalVolumeFeeLast();
//            //加上 上次未扣费成功的B308
//            lnLastAmount += pRatingData.getiPrevSessionValue().getCcgSpEnd();
//
//            pRatingData.getiCurrSessionValue().setUsedTotalVolume(lnAmount + lnLastAmount);
//            szTmp = "0";
//            pRatingMsg.getM_iRatingExtMsg().setM_strTotalVolumeFeeLast(szTmp); 
//        }
//    }
//    else
//    {
//        //处理时长
//        if ( pRatingMsg.getCommandMsg().getM_strRealDuration() !=0 )
//        {
//            pRatingMsg.getM_iRatingExtMsg().setM_strDuration(String.valueOf(pRatingMsg.getCommandMsg().getM_strRealDuration())); 
//            pRatingData.getiCurrSessionValue().setUsedDuration(pRatingMsg.getCommandMsg().getM_strRealDuration()); 
//            pRatingData.getiStartValue().setLnDuration(pRatingData.getiPrevSessionValue().getUsedDuration()); 
//        }
//        //处理次数
//        if ( pRatingMsg.getCommandMsg().getM_strRealTimes() !=0 )
//        {
//            pRatingMsg.getM_iRatingExtMsg().setM_strTimes(String.valueOf(pRatingMsg.getCommandMsg().getM_strRealTimes())); 
//            pRatingData.getiCurrSessionValue().setUsedTimes(pRatingMsg.getCommandMsg().getM_strRealTimes());
//            pRatingData.getiStartValue().setLnTimes(pRatingData.getiPrevSessionValue().getUsedTimes());
//        }
//
//        //处理上行流量
//        if ( pRatingMsg.getCommandMsg().getM_strRealUpVolume() !=0  )
//        {
//            pRatingMsg.getM_iRatingExtMsg().setM_strUpVolume(String.valueOf(pRatingMsg.getCommandMsg().getM_strRealUpVolume())); 
//            pRatingData.getiCurrSessionValue().setUsedUpVolume(pRatingMsg.getCommandMsg().getM_strRealUpVolume()); 
//            pRatingData.getiStartValue().setLnUpVolume(pRatingData.getiPrevSessionValue().getUsedUpVolume()); 
//        }
//
//        //处理下行流量
//        if ( pRatingMsg.getCommandMsg().getM_strRealDownVolume() !=0  )
//        {
//            pRatingMsg.getM_iRatingExtMsg().setM_strDownVolume(String.valueOf(pRatingMsg.getCommandMsg().getM_strRealDownVolume())); 
//            pRatingData.getiCurrSessionValue().setUsedDownVolume(pRatingMsg.getCommandMsg().getM_strReqDownVolume()); 
//            pRatingData.getiStartValue().setLnDownVolume(pRatingData.getiPrevSessionValue().getUsedDownVolume()); 
//        }
//
//        //处理总流量
//        if ( pRatingMsg.getCommandMsg().getM_strRealTotalVolume() !=0 )
//        {
//            pRatingMsg.getM_iRatingExtMsg().setM_strTotalVolume(String.valueOf(pRatingMsg.getCommandMsg().getM_strRealTotalVolume())); 
//            pRatingData.getiCurrSessionValue().setUsedTotalVolume(pRatingMsg.getCommandMsg().getM_strRealTotalVolume()); 
//            pRatingData.getiStartValue().setLnTotalVolume(pRatingData.getiPrevSessionValue().getUsedTotalVolume()); 
//        }
//    }
//
//
//	    ///NOTE:设置批价使用量
//	    initRateMeasureFromMsg(pRatingData.getiReqRateMeasure());	
//		
//		
//		
//		
//		
//	}
//	
//	
//
//	
//	public void ratingEventBack(){
//		
//	}
//	
//	
//	
//	/*
//	 * update
//	 */
//	public void setReqRateMeasure(){
//		//消息中的使用量
//		if(!pRatingMsg.getM_iRatingExtMsg().getM_strDuration().isEmpty()){
//			pRatingData.setLnReqDuration(Long.parseLong(pRatingMsg.getM_iRatingExtMsg().getM_strDuration()));
//		}
//		if(!pRatingMsg.getM_iRatingExtMsg().getM_strTimes().isEmpty()){
//			pRatingData.setLnReqTimes(Long.parseLong(pRatingMsg.getM_iRatingExtMsg().getM_strTimes()));
//			
//		}
//		if(!pRatingMsg.getM_iRatingExtMsg().getM_strUpVolume().isEmpty()){
//			pRatingData.setLnReqUpVolume(Long.parseLong(pRatingMsg.getM_iRatingExtMsg().getM_strUpVolume()));
//			//考虑费率切换点之后的值,下同
//			pRatingData.setLnReqUpVolume(pRatingData.getLnReqUpVolume()+Long.parseLong(pRatingMsg.getM_iRatingExtMsg().getM_strUpVolumeFeeLast()));
//			
//		}
//		if(!pRatingMsg.getM_iRatingExtMsg().getM_strDownVolume().isEmpty() ){
//			pRatingData.setLnReqDownVolume(Long.parseLong(pRatingMsg.getM_iRatingExtMsg().getM_strDownVolume()));
//			pRatingData.setLnReqDownVolume(pRatingData.getLnReqDownVolume()+Long.parseLong(pRatingMsg.getM_iRatingExtMsg().getM_strDownVolumeFeeLast()));
//			
//		}
//		if(!pRatingMsg.getM_iRatingExtMsg().getM_strTotalVolume().isEmpty()){
//			pRatingData.setLnReqTotalVolume(Long.parseLong(pRatingMsg.getM_iRatingExtMsg().getM_strTotalVolume()));
//			pRatingData.setLnReqTotalVolume(pRatingData.getLnReqTotalVolume()+Long.parseLong(pRatingMsg.getM_iRatingExtMsg().getM_strTotalVolumeFeeLast()));
//			
//		}
//	}
//	
//	public void setpRatingData(RatingData pRatingData) {
//		this.pRatingData = pRatingData;
//	}
//	
//	public void setpRatingMsg(RatingMsg pRatingMsg) {
//		this.pRatingMsg = pRatingMsg;
//	}
//	
//	/**
//	 * @param msgParsing the msgParsing to set
//	 */
//	public void setMsgParsing(MsgParsingImpl msgParsing) {
//		this.msgParsing = msgParsing;
//	}
//	/**
//	 * @return the msgParsing
//	 */
//	public MsgParsingImpl getMsgParsing() {
//		return msgParsing;
//	}
	
	
}
