package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import oracle.net.aso.s;

import com.tydic.beijing.billing.rating.dto.DbUtil;
import com.tydic.beijing.billing.rating.dto.impl.DbUtilImpl;

public class RatingData {

	private int nOper=0; // 环节，1－预占；2－实扣
	private int nFeeType;
	private boolean bNeedForTest=false; // 是否区分大小片
	private boolean bExactFee=false; // 是否精确计费
	private boolean bCanBeNegative=false; // 能否扣成负数
	private boolean bZeroToNegative=false; // 余额为0时能否扣成负数
	private int nCellFlag=0;
	private int nServiceLimitFlag=0;
	private int nResultCode=0; // TODO:设置批价结果码
	private boolean bRemindBalanceState=false; // 余额提醒

	private RateMeasure iReqRateMeasure=new RateMeasure(); // 预占使用量，批价过程中值不会改变
	private RateMeasure iRealRateMeasure=new RateMeasure(); // 实扣使用量，批价过程中不会改变
	
	
	private RateMeasure iRateMeasure=new RateMeasure(); // 使用量，批价过程中使用，根据预占/实扣由iReqRateMeasure或者iRealRateMeasure设置

	private RateMeasure m_iRateMeasureForSection = new RateMeasure(); //各个domain使用量，从段落开始使用此RateMeasure
	
	private RateMeasure iFinalReqRateMeasure; // TODO:最终预占使用量，若有反算，保存反算后的值

	private RateMeasure pRateMeasure=new RateMeasure();
	private String m_strFormula ="";

	public String getM_strFormula() {
		return m_strFormula;
	}

	public void setM_strFormula(String m_strFormula) {
		this.m_strFormula = m_strFormula;
	}

	// 累积量更新使用
	private long lnReqDuration=0;
	private long lnReqTimes=0;
	private long lnReqUpVolume=0;
	private long lnReqDownVolume=0;
	private long lnReqTotalVolume=0;

	private List<TariffResult> iTariffResults=null; // 保存最终结果的TariffResult
	private List<RateInfo> iRateInfos=new ArrayList<RateInfo>();

	// 批价开始时间。批价开始之前设置为时间片开始时间，每次批一个段落的时候（如果按时间批价），则加上已批价部分的时长
	private int nTZ;
	private String strStartTime;
	private String iStartTime;
	private String iCurrTime;

	private int lnRateUnit=0; // 计费单位
	private double dRateValue=0;
	private int lnUnUsedDuration=0; // TODO:靠，这俩啥区别啊？
	private int lnRatableUnUsedDuration=0;
	private String strSpanTime=""; // 费率切换点 TODO:tariff中设置

	// deal()中根据会话设置
	private StartValue iStartValue=new StartValue();

	private SessionValue iPrevSessionValue = new SessionValue(); //用于保存上次会话信息，数据查询用
	private  SessionValue iCurrSessionValue = new SessionValue(); //用于保存本次会话信息，数据更新用
	// 内存表会话信息，每个时间片批价开始都需要从coherence加载
	private List< SessionInfo> iSessionInfos=new ArrayList<SessionInfo>();

	// TODO:DeductMgr中获取值
	private List<BalanceContent> iRealFees  =new ArrayList<BalanceContent>();
	private List<BalanceContent> iReqFees  =new ArrayList<BalanceContent>();

	// TotalBalance iFeeBalances; //保存当前的账本信息
	// TotalBalance iTotalBalances; //保存所有的账本信息
	// ReserveBalance iReserveBalances;

	// 可用余额信息
	private List<FreeBalance>  iFreeBalances ;

	private MTChangeInfo iMTChangeInfos;

	// 自费信息 B07
	private List<FeeInfo> iFeeInfos=new ArrayList<FeeInfo>();

	private List<Fee> iUnusedMoneys=new ArrayList<Fee>();
	private boolean bNonMoneyBalance=false; // TODO:是否用到金钱账本?

	// TODO:设置值
	private String strPayPlanInfo=""; // 支付计划信息
	private String strPricingPlanInfo=""; // 定价计划信息
	private String strUnusedMoneyInfo=""; // 用户定价信息

	// /TODO:calcFee中获取下面这些值
	private List<ChargeUnit> iTotalChargeUnits=new ArrayList<ChargeUnit>(); // 总的费用结构体链表
	private List<ChargeUnit> iChargeUnits=new ArrayList<ChargeUnit>();// 费用结构体链表(包括段落打折后费用)
	private List<ChargeUnit> iReqChargeUnits=new ArrayList<ChargeUnit>(); // 预占费用结构体链表(包括段落打折后费用)
	private List<ChargeUnit> iRealChargeUnits=new ArrayList<ChargeUnit>(); // 实扣费用结构体链表(包括段落打折后费用)
	private List<ChargeUnit> iMergedReqChargeUnits=new ArrayList<ChargeUnit>(); // 根据费用项代码合并后费用结构体链表(包括段落打折后费用)，预占费用
	private List<ChargeUnit> iMergedRealChargeUnits=new ArrayList<ChargeUnit>(); // 根据费用项代码合并后费用结构体链表(包括段落打折后费用)，实扣费用
	private List<ChargeUnit> iBeforeChargeUnits=new ArrayList<ChargeUnit>(); // 计费处理后费用结构体 费用为0的
	private List<ChargeUnit> iChargeUnitsForRatable=new ArrayList<ChargeUnit>(); // 记录段落打折前的费用,用于更新费用累计

	private List<Ratable> iRatableRefs=new ArrayList<Ratable>(); // 保存累积量值

	private List<DeductInfo> iDeductInfos=new ArrayList<DeductInfo>(); // 保存扣费信息

	// /反算
	private boolean bIsReRating=false;
	private boolean bGetPlanDisctFlag=true; // 是否需要查询定价计划。反算和实扣资源不足正算是不需要查。
	// ReRatingData iReRatingData;


	private BalanceData iBalanceData=new BalanceData(); // zhanghb add

	
	
	// /批价时获取一次
	// /{
	private List<PlanDisct> iPlanDiscts; // 折线前的定价计划
	private Map<String, PlanDisct> iPlanDisctMap=new HashMap<String,PlanDisct>(); // key: ofr_id
	private List<PlanDisct> iAtomPlanDiscts=new ArrayList<PlanDisct>(); // 折线后的定价计划
	// /NOTE:后续优化，将value改为在iAtomPlanDiscts中的索引什么的
	private Map<String, PlanDisct> iAtomPlanDisctMap=new HashMap<String,PlanDisct>(); // key: atoofr_id

	private Map<String, RatableResourceInfo> iPlanRatableResourceInfos = new HashMap<String,RatableResourceInfo>(); // key:
																// pricing_plan_id
	private Map<String, RatableResourceInfo> iRatableResourceInfos=new HashMap<String,RatableResourceInfo>(); // key:
															// ratable_resource_code
	private Map<String, RatableResourceValue> iRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:ratable_resource_code
	private Map<String, RatableResourceValue> iAllRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:ratable_resource_code
	private Map<String, RatableResourceValue> iTmpRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:
																	// ratable_resource_code
	private Map<String, RatableResourceValue> iRealRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:
																	// ratable_resource_code
																	// 实扣批完之后的累积量
	////zhanghb ratableresource param start
	

	private Map<String, CodeRatableResource> iPlanCodeRatableResources; // key:
	// pricing_plan_id
	private List<CodeRatableResource> allCodeRatableResource;
	private Map<String, CodeRatableResource> iCodeRatableResources=new HashMap<String,CodeRatableResource>(); // key:
	// ratable_resource_code
	private Map<String, InfoRatableHistory> iInfoRatableHistories=new HashMap<String,InfoRatableHistory>(); // key:ratable_resource_code
	private Map<String, InfoRatableHistory> iAllInfoRatableHistories; // key:ratable_resource_code
	private Map<String, InfoRatableHistory> iTmpInfoRatableHistories=new HashMap<String,InfoRatableHistory>(); // key:
			// ratable_resource_code
	private Map<String, InfoRatableHistory> iRealInfoRatableHistories=new HashMap<String,InfoRatableHistory>(); // key:
			// ratable_resource_code
	////zhanghb ratableresource param end

	private boolean bTmpRatableFlag=false;
	private List<String> iEffGroups =new ArrayList<String>(); // 有效的套餐组. key: group_id
	private Map<String, Integer> iGroupFavModes=new HashMap<String,Integer>(); // 套餐组优惠模式. key: group_id, value:fav_mode
	private Map<String, List<PlanDisct>> iOfrInGroups=new HashMap<String,List<PlanDisct>>(); // 每个group中包含的原子套餐. key: group_id.
	private PlanDisctPool iPlanDisctPool; // 按优先级排序
	// /}

	//zhanghb add start
	private Map<String, List<UserDiscts>> iCodeOfrInGroups=new HashMap<String,List<UserDiscts>>(); 
	//private Map<String, CodeOfr> iCodeOfrs = new HashMap<String,CodeOfr>();
	//private List<CodeOfr> userCodeOfrs = new ArrayList<CodeOfr>();
	private List<UserDiscts> listOfUserDiscts = new ArrayList<UserDiscts>();
 	//zhanghb add end
	
	// 批价时间及批价轨迹记录
	// /{
	TimeTrack iTotalTrack;
	TimeTrack iParseTrack;
	TimeTrack iFormatTrack;
	TimeTrack iRatingTrack;
	TimeTrack iBalanceTrack;
	TimeTrack iDCMTrack;
	TimeTrack iABMTrack;
	TimeTrack iPrdInstTrack; // TB_PRD_PRD_INST
	TimeTrack iIvpnGroupTrack; // TB_PRD_IVPN_GROUP + TB_PRD_IVPN_GROUP_USER
	TimeTrack iOfrInstTrack; // TB_PRD_OFR_INST + TB_PRD_OFR
	TimeTrack iGroupUserTrack; // TB_PRD_GROUP_USER
	TimeTrack iSessionTrackQ; // SESSION_INFORMATION_EXT
	TimeTrack iSessionTrackC; // SESSION_INFORMATION_EXT
	TimeTrack iSessionTrackU; // SESSION_INFORMATION_EXT
	TimeTrack iSessionTrackD; // SESSION_INFORMATION_EXT
	TimeTrack iAutoRenewOfrTrackQ; // TB_PRD_AUTORENEW_OFR
	TimeTrack iAutoRenewUserInforTrackQ; // TB_PRD_AUTORENEW_USER_INFOR
	TimeTrack iAutoRenewUserInforTrackC; // TB_PRD_AUTORENEW_USER_INFOR
	TimeTrack iPrdInstAttrCountTrackQ; // TB_PRD_PRD_INST_ATTR_COUNT
	TimeTrack iPrdInstAttrCountTrackU; // TB_PRD_PRD_INST_ATTR_COUNT
	TimeTrack iPrdInstAttrCountTrackC; // TB_PRD_PRD_INST_ATTR_COUNT
	TimeTrack iDeductHistoryTrack; // TB_BIL_DEDUCT_RECORD_HISTORY
	TimeTrack iTopValueTrackQ; // TB_BIL_TOP_VALUE_CUMULATE
	TimeTrack iTopValueTrackC; // TB_BIL_TOP_VALUE_CUMULATE
	TimeTrack iTopValueTrackU; // TB_BIL_TOP_VALUE_CUMULATE

	PlanDisct iPool;
	PlanDisct iAllocs;
	private String strBillingTrack;

	class PlanDisctPool {

	};

//	PlanDisct get() {
//		PlanDisct p = iPool;
//
//		iAllocs.PlanDisct(p);
//
//		return p;
//	}

	void release() {
		for (PlanDisct iter = iAllocs; iter != iAllocs;) {

		}

	}

	PlanDisct getPlanDisct() {
		return null;
	}

	public void init(RatingMsg msg) {
		//System.out.println("消息类型:"+msg.getM_iRatingExtMsg().getM_nExtFeeType());
		
		 nFeeType = msg.getM_iRatingExtMsg().getM_nExtFeeType();
		// iMsgBuffer.clear();
		// iRateMeasureForSection.init(); //各个domain使用量，从段落开始使用此RateMeasure
		// lnTotalDosage = 0; //总的使用量
		// lnChargedDosage = 0; //已批价的使用量
		// lnUnusedDosage = 0; //未用使用量


		// iTariffResults.clear(); //保存最终结果的TariffResult
		// iRateInfos.clear();

		// iStartValue.init();
		
		
		int billingFlag=msg.getM_nBillingFlag();
		String sessionId=msg.getBaseMsg().getM_strSessionId();
		if(billingFlag==RatingMacro.SESSION_UPDATE||billingFlag==RatingMacro.SESSION_END ){
			
			getSession(sessionId);

		}
		// iPrevSessionValue.init(); //用于保存上次会话信息，数据查询用
		// iCurrSessionValue.init(); //用于保存本次会话信息，数据更新用
		// 内存表会话信息，每个时间片批价开始都需要从coherence加载
		// iSessionInfos.clear();

		// iRealFees.clear();
		// iReqFees.clear();

		// iFeeBalances.clear(); //保存当前的账本信息
		// iTotalBalances.clear(); //保存所有的账本信息

		// iReserveBalances.clear();
		// iFreeBalances.clear();

		// iMTChangeInfos.clear();

		// iFeeInfos.clear();
		// iFeeInfo.init();

		// iUnusedMoneys.clear();

		// iTotalChargeUnits.clear(); //总的费用结构体链表
		// iChargeUnits.clear(); //费用结构体链表(包括段落打折后费用)
		// iBeforeChargeUnits.clear(); //计费处理后费用结构体 费用为0的
		// iChargeUnitsForRatable.clear(); //记录段落打折前的费用,用于更新费用累计

		// iRatableRefs.clear(); //保存累积量值

		// iDeductInfos.clear(); //保存扣费信息

		// iBalanceData.init();

		// iPlanDisctPool.release();

		// iPlanDiscts.clear(); //折线前的定价计划
		// iAtomPlanDiscts.clear(); //折线后的定价计划

		// iRatables.clear(); //累积量 Key:plan_id
		// iRatableValues.clear();
		// iTmpRatableResouceValues.clear();
		// iEffGroups.clear(); //有效的套餐组. key: group_id

		// iRateMeasure.init();

		// strStartTime = msg_.getAllSessionStartTimes();

		// DateTimeParser::parse("%Y%m%f%H%M%S", strStartTime, iStartTime, nTZ);
		iCurrTime = iStartTime;
	}

	
	
	private boolean getSession(String sessionId){
		DbUtil db=new DbUtilImpl();
		List<SessionInformationExt> sessionInfos=db.getSessionInfoById(sessionId);
		if(sessionInfos.isEmpty()){
			return false;
		}
		SessionInfo session=new SessionInfo();
		for(SessionInformationExt s :sessionInfos){
			session.sessionInit(s);
			strPayPlanInfo=s.getPay_Plan_Info();
			strPricingPlanInfo = s.getPricing_Plan_Info();
            strUnusedMoneyInfo = s.getUnused_Money();
            iSessionInfos.add(session);

            iPrevSessionValue.setTabUsedDuration(s.getUsed_Duration());
            iPrevSessionValue.setTabUsedTimes(s.getUsed_Times());
            iPrevSessionValue.setTabUsedUpVolume(s.getUsed_Upvolume());
            iPrevSessionValue.setTabUsedDownVolume(s.getUsed_Downvolume());
            iPrevSessionValue.setTabUsedTotalVolume(s.getUsed_Totalvolume());
            
            switch(s.getUnit_Type()){
            case RatingMacro.CREDITUNIT_MONEY:
            	iPrevSessionValue.setReserveMoney(s.getReserved_Amount());
                iPrevSessionValue.setUsedMoney(s.getUsed_Amount());
                iPrevSessionValue.setUnUsedMoney(s.getUnused_Amount());
                break;
            case RatingMacro.CREDITUNIT_TIMELEN:
                iPrevSessionValue.setReserveDuration(s.getReserved_Amount());
                iPrevSessionValue.setUsedDuration(s.getUsed_Amount());
                iPrevSessionValue.setUnUsedDuration(s.getUnused_Amount());
                break;
            case RatingMacro.CREDITUNIT_TOTALVAL:
                iPrevSessionValue.setReserveTotalVolume(s.getReserved_Amount());
                iPrevSessionValue.setUsedTotalVolume(s.getUsed_Amount());
                iPrevSessionValue.setUnUsedTotalVolume(s.getUnused_Amount());
                iPrevSessionValue.setCcgSpStart(s.getSwitchpoint_Start());
                iPrevSessionValue.setCcgSpEnd(s.getSwitchpoint_End());
                break;
            case RatingMacro.CREDITUNIT_UPVAL:
                iPrevSessionValue.setReserveUpVolume(s.getReserved_Amount());
                iPrevSessionValue.setUsedUpVolume(s.getUsed_Amount());
                iPrevSessionValue.setUnUsedUpVolume(s.getUnused_Amount());
                iPrevSessionValue.setCcgSpStart(s.getSwitchpoint_Start());
                iPrevSessionValue.setCcgSpStart(s.getSwitchpoint_End());
                break;
            case RatingMacro.CREDITUNIT_DOWNVAL:
                iPrevSessionValue.setReserveDownVolume(s.getReserved_Amount());
                iPrevSessionValue.setUsedDownVolume(s.getUsed_Amount());
                iPrevSessionValue.setUnUsedDownVolume(s.getUnused_Amount());
                iPrevSessionValue.setCcgSpStart(s.getSwitchpoint_Start());
                iPrevSessionValue.setCcgSpEnd(s.getSwitchpoint_End());
                break;
            case RatingMacro.CREDITUNIT_COUNT:
                iPrevSessionValue.setReserveTimes(s.getReserved_Amount());
                iPrevSessionValue.setUsedTimes(s.getUsed_Amount());
                iPrevSessionValue.setUnUsedTimes(s.getUnused_Amount());
                break;
            default: 
                break;
            }
		}
		
		return true;
	}
	
	
	int addPlanDisct(PlanDisct aPlan) {

		return 0;
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

	public int getnOper() {
		return nOper;
	}

	public void setnOper(int nOper) {
		this.nOper = nOper;
	}

	public int getnFeeType() {
		return nFeeType;
	}

	public void setnFeeType(int nFeeType) {
		this.nFeeType = nFeeType;
	}

	public boolean isbNeedForTest() {
		return bNeedForTest;
	}

	public void setbNeedForTest(boolean bNeedForTest) {
		this.bNeedForTest = bNeedForTest;
	}

	public boolean isbExactFee() {
		return bExactFee;
	}

	public void setbExactFee(boolean bExactFee) {
		this.bExactFee = bExactFee;
	}

	public boolean isbCanBeNegative() {
		return bCanBeNegative;
	}

	public void setbCanBeNegative(boolean bCanBeNegative) {
		this.bCanBeNegative = bCanBeNegative;
	}

	public boolean isbZeroToNegative() {
		return bZeroToNegative;
	}

	public void setbZeroToNegative(boolean bZeroToNegative) {
		this.bZeroToNegative = bZeroToNegative;
	}

	public int getnCellFlag() {
		return nCellFlag;
	}

	public void setnCellFlag(int nCellFlag) {
		this.nCellFlag = nCellFlag;
	}

	public int getnServiceLimitFlag() {
		return nServiceLimitFlag;
	}

	public void setnServiceLimitFlag(int nServiceLimitFlag) {
		this.nServiceLimitFlag = nServiceLimitFlag;
	}

	public int getnResultCode() {
		return nResultCode;
	}

	public void setnResultCode(int nResultCode) {
		this.nResultCode = nResultCode;
	}

	public boolean isbRemindBalanceState() {
		return bRemindBalanceState;
	}

	public void setbRemindBalanceState(boolean bRemindBalanceState) {
		this.bRemindBalanceState = bRemindBalanceState;
	}

	public RateMeasure getiReqRateMeasure() {
		return iReqRateMeasure;
	}

	public void setiReqRateMeasure(RateMeasure iReqRateMeasure) {
		this.iReqRateMeasure = iReqRateMeasure;
	}

	public RateMeasure getiRealRateMeasure() {
		return iRealRateMeasure;
	}

	public void setiRealRateMeasure(RateMeasure iRealRateMeasure) {
		this.iRealRateMeasure = iRealRateMeasure;
	}

	public RateMeasure getiRateMeasure() {
		return iRateMeasure;
	}

	public void setiRateMeasure(RateMeasure iRateMeasure) {
		this.iRateMeasure = iRateMeasure;
	}

	public RateMeasure getiFinalReqRateMeasure() {
		return iFinalReqRateMeasure;
	}

	public void setiFinalReqRateMeasure(RateMeasure iFinalReqRateMeasure) {
		this.iFinalReqRateMeasure = iFinalReqRateMeasure;
	}

	public RateMeasure getpRateMeasure() {
		return pRateMeasure;
	}

	public void setpRateMeasure(RateMeasure pRateMeasure) {
		this.pRateMeasure = pRateMeasure;
	}

	public long getLnReqDuration() {
		return lnReqDuration;
	}

	public void setLnReqDuration(long lnReqDuration) {
		this.lnReqDuration = lnReqDuration;
	}

	public long getLnReqTimes() {
		return lnReqTimes;
	}

	public void setLnReqTimes(long lnReqTimes) {
		this.lnReqTimes = lnReqTimes;
	}

	public long getLnReqUpVolume() {
		return lnReqUpVolume;
	}

	public void setLnReqUpVolume(long lnReqUpVolume) {
		this.lnReqUpVolume = lnReqUpVolume;
	}

	public long getLnReqDownVolume() {
		return lnReqDownVolume;
	}

	public void setLnReqDownVolume(long lnReqDownVolume) {
		this.lnReqDownVolume = lnReqDownVolume;
	}

	public long getLnReqTotalVolume() {
		return lnReqTotalVolume;
	}

	public void setLnReqTotalVolume(long lnReqTotalVolume) {
		this.lnReqTotalVolume = lnReqTotalVolume;
	}

	
	public List<TariffResult> getiTariffResults() {
		return iTariffResults;
	}

	public void setiTariffResults(List<TariffResult> iTariffResults) {
		this.iTariffResults = iTariffResults;
	}

	

	public List<RateInfo> getiRateInfos() {
		return iRateInfos;
	}

	public void setiRateInfos(List<RateInfo> iRateInfos) {
		this.iRateInfos = iRateInfos;
	}

	public int getnTZ() {
		return nTZ;
	}

	public void setnTZ(int nTZ) {
		this.nTZ = nTZ;
	}

	public String getStrStartTime() {
		return strStartTime;
	}

	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}

	

	public String getiStartTime() {
		return iStartTime;
	}

	public void setiStartTime(String iStartTime) {
		this.iStartTime = iStartTime;
	}

	public String getiCurrTime() {
		return iCurrTime;
	}

	public void setiCurrTime(String iCurrTime) {
		this.iCurrTime = iCurrTime;
	}

	public int getLnRateUnit() {
		return lnRateUnit;
	}

	public void setLnRateUnit(int lnRateUnit) {
		this.lnRateUnit = lnRateUnit;
	}

	public double getdRateValue() {
		return dRateValue;
	}

	public void setdRateValue(double dRateValue) {
		this.dRateValue = dRateValue;
	}

	public int getLnUnUsedDuration() {
		return lnUnUsedDuration;
	}

	public void setLnUnUsedDuration(int lnUnUsedDuration) {
		this.lnUnUsedDuration = lnUnUsedDuration;
	}

	public int getLnRatableUnUsedDuration() {
		return lnRatableUnUsedDuration;
	}

	public void setLnRatableUnUsedDuration(int lnRatableUnUsedDuration) {
		this.lnRatableUnUsedDuration = lnRatableUnUsedDuration;
	}

	public String getStrSpanTime() {
		return strSpanTime;
	}

	public void setStrSpanTime(String strSpanTime) {
		this.strSpanTime = strSpanTime;
	}

	public StartValue getiStartValue() {
		return iStartValue;
	}

	public void setiStartValue(StartValue iStartValue) {
		this.iStartValue = iStartValue;
	}

	public SessionValue getiPrevSessionValue() {
		return iPrevSessionValue;
	}

	public void setiPrevSessionValue(SessionValue iPrevSessionValue) {
		this.iPrevSessionValue = iPrevSessionValue;
	}

	public SessionValue getiCurrSessionValue() {
		return iCurrSessionValue;
	}

	public void setiCurrSessionValue(SessionValue iCurrSessionValue) {
		this.iCurrSessionValue = iCurrSessionValue;
	}

	

	public List<BalanceContent> getiRealFees() {
		return iRealFees;
	}

	public void setiRealFees(List<BalanceContent> iRealFees) {
		this.iRealFees = iRealFees;
	}

	public List<BalanceContent> getiReqFees() {
		return iReqFees;
	}

	public void setiReqFees(List<BalanceContent> iReqFees) {
		this.iReqFees = iReqFees;
	}

	public List<FreeBalance> getiFreeBalances() {
		return iFreeBalances;
	}

	public void setiFreeBalances(List<FreeBalance> iFreeBalances) {
		this.iFreeBalances = iFreeBalances;
	}

	public MTChangeInfo getiMTChangeInfos() {
		return iMTChangeInfos;
	}

	public void setiMTChangeInfos(MTChangeInfo iMTChangeInfos) {
		this.iMTChangeInfos = iMTChangeInfos;
	}

	

	public List<FeeInfo> getiFeeInfos() {
		return iFeeInfos;
	}

	public void setiFeeInfos(List<FeeInfo> iFeeInfos) {
		this.iFeeInfos = iFeeInfos;
	}

	public List<Fee> getiUnusedMoneys() {
		return iUnusedMoneys;
	}

	public void setiUnusedMoneys(List<Fee> iUnusedMoneys) {
		this.iUnusedMoneys = iUnusedMoneys;
	}

	public boolean isbNonMoneyBalance() {
		return bNonMoneyBalance;
	}

	public void setbNonMoneyBalance(boolean bNonMoneyBalance) {
		this.bNonMoneyBalance = bNonMoneyBalance;
	}

	public String getStrPayPlanInfo() {
		return strPayPlanInfo;
	}

	public void setStrPayPlanInfo(String strPayPlanInfo) {
		this.strPayPlanInfo = strPayPlanInfo;
	}

	public String getStrPricingPlanInfo() {
		return strPricingPlanInfo;
	}

	public void setStrPricingPlanInfo(String strPricingPlanInfo) {
		this.strPricingPlanInfo = strPricingPlanInfo;
	}

	public String getStrUnusedMoneyInfo() {
		return strUnusedMoneyInfo;
	}

	public void setStrUnusedMoneyInfo(String strUnusedMoneyInfo) {
		this.strUnusedMoneyInfo = strUnusedMoneyInfo;
	}

	

	public List<ChargeUnit> getiTotalChargeUnits() {
		return iTotalChargeUnits;
	}

	public void setiTotalChargeUnits(List<ChargeUnit> iTotalChargeUnits) {
		this.iTotalChargeUnits = iTotalChargeUnits;
	}

	public List<ChargeUnit> getiChargeUnits() {
		return iChargeUnits;
	}

	public void setiChargeUnits(List<ChargeUnit> iChargeUnits) {
		this.iChargeUnits = iChargeUnits;
	}

	public List<ChargeUnit> getiReqChargeUnits() {
		return iReqChargeUnits;
	}

	public void setiReqChargeUnits(List<ChargeUnit> iReqChargeUnits) {
		this.iReqChargeUnits = iReqChargeUnits;
	}

	public List<ChargeUnit> getiRealChargeUnits() {
		return iRealChargeUnits;
	}

	public void setiRealChargeUnits(List<ChargeUnit> iRealChargeUnits) {
		this.iRealChargeUnits = iRealChargeUnits;
	}

	public List<ChargeUnit> getiMergedReqChargeUnits() {
		return iMergedReqChargeUnits;
	}

	public void setiMergedReqChargeUnits(List<ChargeUnit> iMergedReqChargeUnits) {
		this.iMergedReqChargeUnits = iMergedReqChargeUnits;
	}

	public List<ChargeUnit> getiMergedRealChargeUnits() {
		return iMergedRealChargeUnits;
	}

	public void setiMergedRealChargeUnits(List<ChargeUnit> iMergedRealChargeUnits) {
		this.iMergedRealChargeUnits = iMergedRealChargeUnits;
	}

	public List<ChargeUnit> getiBeforeChargeUnits() {
		return iBeforeChargeUnits;
	}

	public void setiBeforeChargeUnits(List<ChargeUnit> iBeforeChargeUnits) {
		this.iBeforeChargeUnits = iBeforeChargeUnits;
	}

	public List<ChargeUnit> getiChargeUnitsForRatable() {
		return iChargeUnitsForRatable;
	}

	public void setiChargeUnitsForRatable(List<ChargeUnit> iChargeUnitsForRatable) {
		this.iChargeUnitsForRatable = iChargeUnitsForRatable;
	}

	

	public List<Ratable> getiRatableRefs() {
		return iRatableRefs;
	}

	public void setiRatableRefs(List<Ratable> iRatableRefs) {
		this.iRatableRefs = iRatableRefs;
	}

	public List<DeductInfo> getiDeductInfos() {
		return iDeductInfos;
	}

	public void setiDeductInfos(List<DeductInfo> iDeductInfos) {
		this.iDeductInfos = iDeductInfos;
	}

	public boolean isbIsReRating() {
		return bIsReRating;
	}

	public void setbIsReRating(boolean bIsReRating) {
		this.bIsReRating = bIsReRating;
	}

	public boolean isbGetPlanDisctFlag() {
		return bGetPlanDisctFlag;
	}

	public void setbGetPlanDisctFlag(boolean bGetPlanDisctFlag) {
		this.bGetPlanDisctFlag = bGetPlanDisctFlag;
	}

	public BalanceData getiBalanceData() {
		return iBalanceData;
	}

	public void setiBalanceData(BalanceData iBalanceData) {
		this.iBalanceData = iBalanceData;
	}
 
	public Map<String, PlanDisct> getiPlanDisctMap() {
		return iPlanDisctMap;
	}

	public void setiPlanDisctMap(Map<String, PlanDisct> iPlanDisctMap) {
		this.iPlanDisctMap = iPlanDisctMap;
	}



	public List<PlanDisct> getiPlanDiscts() {
		return iPlanDiscts;
	}

	public void setiPlanDiscts(List<PlanDisct> iPlanDiscts) {
		this.iPlanDiscts = iPlanDiscts;
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

	public Map<String, RatableResourceInfo> getiPlanRatableResourceInfos() {
		return iPlanRatableResourceInfos;
	}

	public void setiPlanRatableResourceInfos(
			Map<String, RatableResourceInfo> iPlanRatableResourceInfos) {
		this.iPlanRatableResourceInfos = iPlanRatableResourceInfos;
	}

	public Map<String, RatableResourceInfo> getiRatableResourceInfos() {
		return iRatableResourceInfos;
	}

	public void setiRatableResourceInfos(
			Map<String, RatableResourceInfo> iRatableResourceInfos) {
		this.iRatableResourceInfos = iRatableResourceInfos;
	}

	public Map<String, RatableResourceValue> getiRatableResourceValues() {
		return iRatableResourceValues;
	}

	public void setiRatableResourceValues(
			Map<String, RatableResourceValue> iRatableResourceValues) {
		this.iRatableResourceValues = iRatableResourceValues;
	}

	public Map<String, RatableResourceValue> getiAllRatableResourceValues() {
		return iAllRatableResourceValues;
	}

	public void setiAllRatableResourceValues(
			Map<String, RatableResourceValue> iAllRatableResourceValues) {
		this.iAllRatableResourceValues = iAllRatableResourceValues;
	}

	public Map<String, RatableResourceValue> getiTmpRatableResourceValues() {
		return iTmpRatableResourceValues;
	}

	public void setiTmpRatableResourceValues(
			Map<String, RatableResourceValue> iTmpRatableResourceValues) {
		this.iTmpRatableResourceValues = iTmpRatableResourceValues;
	}

	public Map<String, RatableResourceValue> getiRealRatableResourceValues() {
		return iRealRatableResourceValues;
	}

	public void setiRealRatableResourceValues(
			Map<String, RatableResourceValue> iRealRatableResourceValues) {
		this.iRealRatableResourceValues = iRealRatableResourceValues;
	}

	public boolean isbTmpRatableFlag() {
		return bTmpRatableFlag;
	}

	public void setbTmpRatableFlag(boolean bTmpRatableFlag) {
		this.bTmpRatableFlag = bTmpRatableFlag;
	}





 
	public List<String> getiEffGroups() {
		return iEffGroups;
	}

	public void setiEffGroups(List<String> iEffGroups) {
		this.iEffGroups = iEffGroups;
	}

	public Map<String, Integer> getiGroupFavModes() {
		return iGroupFavModes;
	}

	public void setiGroupFavModes(Map<String, Integer> iGroupFavModes) {
		this.iGroupFavModes = iGroupFavModes;
	}

	public Map<String, List<PlanDisct>> getiOfrInGroups() {
		return iOfrInGroups;
	}

	public void setiOfrInGroups(Map<String, List<PlanDisct>> iOfrInGroups) {
		this.iOfrInGroups = iOfrInGroups;
	}

	public PlanDisctPool getiPlanDisctPool() {
		return iPlanDisctPool;
	}

	public void setiPlanDisctPool(PlanDisctPool iPlanDisctPool) {
		this.iPlanDisctPool = iPlanDisctPool;
	}

	public TimeTrack getiTotalTrack() {
		return iTotalTrack;
	}

	public void setiTotalTrack(TimeTrack iTotalTrack) {
		this.iTotalTrack = iTotalTrack;
	}

	public TimeTrack getiParseTrack() {
		return iParseTrack;
	}

	public void setiParseTrack(TimeTrack iParseTrack) {
		this.iParseTrack = iParseTrack;
	}

	public TimeTrack getiFormatTrack() {
		return iFormatTrack;
	}

	public void setiFormatTrack(TimeTrack iFormatTrack) {
		this.iFormatTrack = iFormatTrack;
	}

	public TimeTrack getiRatingTrack() {
		return iRatingTrack;
	}

	public void setiRatingTrack(TimeTrack iRatingTrack) {
		this.iRatingTrack = iRatingTrack;
	}

	public TimeTrack getiBalanceTrack() {
		return iBalanceTrack;
	}

	public void setiBalanceTrack(TimeTrack iBalanceTrack) {
		this.iBalanceTrack = iBalanceTrack;
	}

	public TimeTrack getiDCMTrack() {
		return iDCMTrack;
	}

	public void setiDCMTrack(TimeTrack iDCMTrack) {
		this.iDCMTrack = iDCMTrack;
	}

	public TimeTrack getiABMTrack() {
		return iABMTrack;
	}

	public void setiABMTrack(TimeTrack iABMTrack) {
		this.iABMTrack = iABMTrack;
	}

	public TimeTrack getiPrdInstTrack() {
		return iPrdInstTrack;
	}

	public void setiPrdInstTrack(TimeTrack iPrdInstTrack) {
		this.iPrdInstTrack = iPrdInstTrack;
	}

	public TimeTrack getiIvpnGroupTrack() {
		return iIvpnGroupTrack;
	}

	public void setiIvpnGroupTrack(TimeTrack iIvpnGroupTrack) {
		this.iIvpnGroupTrack = iIvpnGroupTrack;
	}

	public TimeTrack getiOfrInstTrack() {
		return iOfrInstTrack;
	}

	public void setiOfrInstTrack(TimeTrack iOfrInstTrack) {
		this.iOfrInstTrack = iOfrInstTrack;
	}

	public TimeTrack getiGroupUserTrack() {
		return iGroupUserTrack;
	}

	public void setiGroupUserTrack(TimeTrack iGroupUserTrack) {
		this.iGroupUserTrack = iGroupUserTrack;
	}

	public TimeTrack getiSessionTrackQ() {
		return iSessionTrackQ;
	}

	public void setiSessionTrackQ(TimeTrack iSessionTrackQ) {
		this.iSessionTrackQ = iSessionTrackQ;
	}

	public TimeTrack getiSessionTrackC() {
		return iSessionTrackC;
	}

	public void setiSessionTrackC(TimeTrack iSessionTrackC) {
		this.iSessionTrackC = iSessionTrackC;
	}

	public TimeTrack getiSessionTrackU() {
		return iSessionTrackU;
	}

	public void setiSessionTrackU(TimeTrack iSessionTrackU) {
		this.iSessionTrackU = iSessionTrackU;
	}

	
	
	
	public RateMeasure getM_iRateMeasureForSection() {
		return m_iRateMeasureForSection;
	}

	public void setM_iRateMeasureForSection(RateMeasure m_iRateMeasureForSection) {
		this.m_iRateMeasureForSection = m_iRateMeasureForSection;
	}

	public List<CodeRatableResource> getAllCodeRatableResource() {
		return allCodeRatableResource;
	}

	public void setAllCodeRatableResource(
			List<CodeRatableResource> allCodeRatableResource) {
		this.allCodeRatableResource = allCodeRatableResource;
	}

	public TimeTrack getiSessionTrackD() {
		return iSessionTrackD;
	}

	public void setiSessionTrackD(TimeTrack iSessionTrackD) {
		this.iSessionTrackD = iSessionTrackD;
	}

	public TimeTrack getiAutoRenewOfrTrackQ() {
		return iAutoRenewOfrTrackQ;
	}

	public void setiAutoRenewOfrTrackQ(TimeTrack iAutoRenewOfrTrackQ) {
		this.iAutoRenewOfrTrackQ = iAutoRenewOfrTrackQ;
	}

	public TimeTrack getiAutoRenewUserInforTrackQ() {
		return iAutoRenewUserInforTrackQ;
	}

	public void setiAutoRenewUserInforTrackQ(TimeTrack iAutoRenewUserInforTrackQ) {
		this.iAutoRenewUserInforTrackQ = iAutoRenewUserInforTrackQ;
	}

	public TimeTrack getiAutoRenewUserInforTrackC() {
		return iAutoRenewUserInforTrackC;
	}

	public void setiAutoRenewUserInforTrackC(TimeTrack iAutoRenewUserInforTrackC) {
		this.iAutoRenewUserInforTrackC = iAutoRenewUserInforTrackC;
	}

	public TimeTrack getiPrdInstAttrCountTrackQ() {
		return iPrdInstAttrCountTrackQ;
	}

	public void setiPrdInstAttrCountTrackQ(TimeTrack iPrdInstAttrCountTrackQ) {
		this.iPrdInstAttrCountTrackQ = iPrdInstAttrCountTrackQ;
	}

	public TimeTrack getiPrdInstAttrCountTrackU() {
		return iPrdInstAttrCountTrackU;
	}

	public void setiPrdInstAttrCountTrackU(TimeTrack iPrdInstAttrCountTrackU) {
		this.iPrdInstAttrCountTrackU = iPrdInstAttrCountTrackU;
	}

	public TimeTrack getiPrdInstAttrCountTrackC() {
		return iPrdInstAttrCountTrackC;
	}

	public void setiPrdInstAttrCountTrackC(TimeTrack iPrdInstAttrCountTrackC) {
		this.iPrdInstAttrCountTrackC = iPrdInstAttrCountTrackC;
	}

	public TimeTrack getiDeductHistoryTrack() {
		return iDeductHistoryTrack;
	}

	public void setiDeductHistoryTrack(TimeTrack iDeductHistoryTrack) {
		this.iDeductHistoryTrack = iDeductHistoryTrack;
	}

	public TimeTrack getiTopValueTrackQ() {
		return iTopValueTrackQ;
	}

	public void setiTopValueTrackQ(TimeTrack iTopValueTrackQ) {
		this.iTopValueTrackQ = iTopValueTrackQ;
	}

	public TimeTrack getiTopValueTrackC() {
		return iTopValueTrackC;
	}

	public void setiTopValueTrackC(TimeTrack iTopValueTrackC) {
		this.iTopValueTrackC = iTopValueTrackC;
	}

	public TimeTrack getiTopValueTrackU() {
		return iTopValueTrackU;
	}

	public void setiTopValueTrackU(TimeTrack iTopValueTrackU) {
		this.iTopValueTrackU = iTopValueTrackU;
	}

	public PlanDisct getiPool() {
		return iPool;
	}

	public void setiPool(PlanDisct iPool) {
		this.iPool = iPool;
	}

	public PlanDisct getiAllocs() {
		return iAllocs;
	}

	public void setiAllocs(PlanDisct iAllocs) {
		this.iAllocs = iAllocs;
	}

	public String getStrBillingTrack() {
		return strBillingTrack;
	}

	public void setStrBillingTrack(String strBillingTrack) {
		this.strBillingTrack = strBillingTrack;
	}

	public void setiSessionInfos(List<SessionInfo> iSessionInfos) {
		this.iSessionInfos = iSessionInfos;
	}
	public List<SessionInfo> getiSessionInfos() {
		return iSessionInfos;
	}

	public Map<String, CodeRatableResource> getiPlanCodeRatableResources() {
		return iPlanCodeRatableResources;
	}

	public void setiPlanCodeRatableResources(
			Map<String, CodeRatableResource> iPlanCodeRatableResources) {
		this.iPlanCodeRatableResources = iPlanCodeRatableResources;
	}

	public Map<String, CodeRatableResource> getiCodeRatableResources() {
		return iCodeRatableResources;
	}

	public void setiCodeRatableResources(
			Map<String, CodeRatableResource> iCodeRatableResources) {
		this.iCodeRatableResources = iCodeRatableResources;
	}

	public Map<String, InfoRatableHistory> getiInfoRatableHistories() {
		return iInfoRatableHistories;
	}

	public void setiInfoRatableHistories(
			Map<String, InfoRatableHistory> iInfoRatableHistories) {
		this.iInfoRatableHistories = iInfoRatableHistories;
	}

	public Map<String, InfoRatableHistory> getiAllInfoRatableHistories() {
		return iAllInfoRatableHistories;
	}

	public void setiAllInfoRatableHistories(
			Map<String, InfoRatableHistory> iAllInfoRatableHistories) {
		this.iAllInfoRatableHistories = iAllInfoRatableHistories;
	}

	public Map<String, InfoRatableHistory> getiTmpInfoRatableHistories() {
		return iTmpInfoRatableHistories;
	}

	public void setiTmpInfoRatableHistories(
			Map<String, InfoRatableHistory> iTmpInfoRatableHistories) {
		this.iTmpInfoRatableHistories = iTmpInfoRatableHistories;
	}

	public Map<String, InfoRatableHistory> getiRealInfoRatableHistories() {
		return iRealInfoRatableHistories;
	}

	public void setiRealInfoRatableHistories(
			Map<String, InfoRatableHistory> iRealInfoRatableHistories) {
		this.iRealInfoRatableHistories = iRealInfoRatableHistories;
	}
	
	
	



	public List<UserDiscts> getListOfUserDiscts() {
		return listOfUserDiscts;
	}

	public void setListOfUserDiscts(List<UserDiscts> listOfUserDiscts) {
		this.listOfUserDiscts = listOfUserDiscts;
	}





	public Map<String, List<UserDiscts>> getiCodeOfrInGroups() {
		return iCodeOfrInGroups;
	}

	public void setiCodeOfrInGroups(Map<String, List<UserDiscts>> iCodeOfrInGroups) {
		this.iCodeOfrInGroups = iCodeOfrInGroups;
	}

	public long getStartValue( String measuredomain){

	    long startvalue;
	    if( measuredomain.equals("1") ) //按时长计费
	        startvalue = iStartValue.getLnDuration();
	    else if( measuredomain.equals("2")  ) //按流量计费
	        startvalue = iStartValue.getLnTotalVolume();
	    else if( measuredomain.equals("3")  ) //按次计费
	        startvalue = iStartValue.getLnTimes();
	    else if( measuredomain.equals("4")  ) //上行流量
	        startvalue = iStartValue.getLnUpVolume();
	    else if( measuredomain.equals("5")  ) //下行流量
	        startvalue = iStartValue.getLnDownVolume();
	    else if( measuredomain.equals("7")   )
	        startvalue = 0;
	    else
	    	startvalue = 0;

	    return startvalue;
	}
	
	
	
	public void init(){
		   this.nOper=0; // 环节，1－预占；2－实扣
			this. nFeeType = -1;
			this. bNeedForTest=false; // 是否区分大小片
			this. bExactFee=false; // 是否精确计费
			this. bCanBeNegative=false; // 能否扣成负数
			this. bZeroToNegative=false; // 余额为0时能否扣成负数
			this. nCellFlag=0;
			this. nServiceLimitFlag=0;
			this. nResultCode=0; // TODO:设置批价结果码
			this. bRemindBalanceState=false; // 余额提醒
			this. iReqRateMeasure=new RateMeasure(); // 预占使用量，批价过程中值不会改变
			this. iRealRateMeasure=new RateMeasure(); // 实扣使用量，批价过程中不会改变
			this. iRateMeasure=new RateMeasure(); // 使用量，批价过程中使用，根据预占/实扣由iReqRateMeasure或者iRealRateMeasure设置
			this. m_iRateMeasureForSection = new RateMeasure(); //各个domain使用量，从段落开始使用此RateMeasure
			this. iFinalReqRateMeasure = new RateMeasure(); // TODO:最终预占使用量，若有反算，保存反算后的值
			this. pRateMeasure=new RateMeasure();
			this. m_strFormula ="";
			this. lnReqDuration=0;
			this. lnReqTimes=0;
			this. lnReqUpVolume=0;
			this. lnReqDownVolume=0;
			this. lnReqTotalVolume=0;
			this. iTariffResults=null; // 保存最终结果的TariffResult
			this. iRateInfos=new ArrayList<RateInfo>();
			this. nTZ =-1;
			this. strStartTime ="";
			this. iStartTime="";
			this. iCurrTime="";
			this. lnRateUnit=0; // 计费单位
			this. dRateValue=0;
			this. lnUnUsedDuration=0; // TODO:靠，这俩啥区别啊？
			this. lnRatableUnUsedDuration=0;
			this. strSpanTime=""; // 费率切换点 TODO:tariff中设置
			this. iStartValue=new StartValue();
			this. iPrevSessionValue = new SessionValue(); //用于保存上次会话信息，数据查询用
			this. iCurrSessionValue = new SessionValue(); //用于保存本次会话信息，数据更新用
			this. iSessionInfos=new ArrayList<SessionInfo>();
		  this.iRealFees  =new ArrayList<BalanceContent>();
		  this.iReqFees  =new ArrayList<BalanceContent>();
			this. iFreeBalances = new ArrayList<FreeBalance>();
			this. iMTChangeInfos = new MTChangeInfo();
			this. iFeeInfos=new ArrayList<FeeInfo>();
			this. iUnusedMoneys=new ArrayList<Fee>();
			this. bNonMoneyBalance=false; // TODO:是否用到金钱账本?
			this. strPayPlanInfo=""; // 支付计划信息
			this. strPricingPlanInfo=""; // 定价计划信息
			this. strUnusedMoneyInfo=""; // 用户定价信息
		  this.iTotalChargeUnits=new ArrayList<ChargeUnit>(); // 总的费用结构体链表
		  this.iChargeUnits=new ArrayList<ChargeUnit>();// 费用结构体链表(包括段落打折后费用)
		  this.iReqChargeUnits=new ArrayList<ChargeUnit>(); // 预占费用结构体链表(包括段落打折后费用)
		  this.iRealChargeUnits=new ArrayList<ChargeUnit>(); // 实扣费用结构体链表(包括段落打折后费用)
		  this.iMergedReqChargeUnits=new ArrayList<ChargeUnit>(); // 根据费用项代码合并后费用结构体链表(包括段落打折后费用)，预占费用
		  this.iMergedRealChargeUnits=new ArrayList<ChargeUnit>(); // 根据费用项代码合并后费用结构体链表(包括段落打折后费用)，实扣费用
		  this.iBeforeChargeUnits=new ArrayList<ChargeUnit>(); // 计费处理后费用结构体 费用为0的
		  this.iChargeUnitsForRatable=new ArrayList<ChargeUnit>(); // 记录段落打折前的费用,用于更新费用累计
			this. iRatableRefs=new ArrayList<Ratable>(); // 保存累积量值
			this. iDeductInfos=new ArrayList<DeductInfo>(); // 保存扣费信息
			this. bIsReRating=false;
			this. bGetPlanDisctFlag=true; // 是否需要查询定价计划。反算和实扣资源不足正算是不需要查。
			this. iBalanceData=new BalanceData(); // zhanghb add
			this. iPlanDiscts = new ArrayList<PlanDisct>(); // 折线前的定价计划
			this. iPlanDisctMap=new HashMap<String,PlanDisct>(); // key: ofr_i
			this. iAtomPlanDiscts=new ArrayList<PlanDisct>(); // 折线后的定价计
			this. iAtomPlanDisctMap=new HashMap<String,PlanDisct>(); // key: atoofr_id
			this. iPlanRatableResourceInfos = new HashMap<String,RatableResourceInfo>(); // key:
			this. iRatableResourceInfos=new HashMap<String,RatableResourceInfo>(); // key:
			this. iRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:ratable_resource_code
			this. iAllRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:ratable_resource_code
			this. iTmpRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:
			this. iRealRatableResourceValues=new HashMap<String,RatableResourceValue>(); // key:
			this. iPlanCodeRatableResources = new HashMap<String, CodeRatableResource>(); // key:
			this. allCodeRatableResource = new ArrayList<CodeRatableResource>();
			this. iCodeRatableResources=new HashMap<String,CodeRatableResource>(); // key:
			this. iInfoRatableHistories=new HashMap<String,InfoRatableHistory>(); // key:ratable_resource_code
			this. iAllInfoRatableHistories = new  HashMap<String, InfoRatableHistory>(); // key:ratable_resource_code
			this. iTmpInfoRatableHistories=new HashMap<String,InfoRatableHistory>(); // key:
			this. iRealInfoRatableHistories=new HashMap<String,InfoRatableHistory>(); // key:
			this. bTmpRatableFlag=false;
			this. iEffGroups =new ArrayList<String>(); // 有效的套餐组. key: group_id
			this. iGroupFavModes=new HashMap<String,Integer>(); // 套餐组优惠模式. key: group_id, value:fav_mode
			this. iOfrInGroups=new HashMap<String,List<PlanDisct>>(); // 每个group中包含的原子套餐. key: group_id.
		  this.iPlanDisctPool = new PlanDisctPool(); // 按优先级排序
			this. iCodeOfrInGroups=new HashMap<String,List<UserDiscts>>(); 
			this. listOfUserDiscts = new ArrayList<UserDiscts>();

	}
	
}
