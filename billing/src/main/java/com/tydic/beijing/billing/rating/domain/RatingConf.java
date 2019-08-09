package com.tydic.beijing.billing.rating.domain;

import java.util.BitSet;

import org.apache.log4j.Logger;
/**
 * 批价配置变量集合
 * @author dongxuanyi
 *
 */
public class RatingConf {

	private static Logger log = Logger.getLogger(RatingConf.class);
	int BALANCE_BITSET_LEN = 32;
	private BitSet bsBalanceType = new BitSet(BALANCE_BITSET_LEN);// 账本类型的bitset
	private String strISMPServiceType = "";
	private String strISMPServiceTypeValue = "";
	private String strISMPServiceFirstType = "";
	private String strISMPServiceSecondType = "";
	private String iIsmpServiceTypeValues = "";
	private int nISMPServiceType = 0;// 集团用户销售品业务控制开关
	private boolean bInGrpUserPrdFlag = false; // in业务， 默认值false
	private boolean bSmsGrpUserPrdFlag = false; // sms业务， 默认值false
	private boolean bSmsGrpFlag = false; // sms业务集团控制开关， 默认值false
	private boolean bIsmpGrpFlag = false; // ismp业务集团控制开关，默认值false
	private boolean bCcgGrpFlag = false; // ccg业务集团控制开关，默认值false
	private boolean bDslGrpFlag = false; // dsl业务集团控制开关，默认值false
	private boolean bMoneyBalNotPositiveFlag = false; // 用户首次使用业务，是否判断金钱账本余额标识
	private boolean bAcOpsInfoFlag = false; // 激活文件中是否增加对端号码信息标识
	private boolean bIsmpControlFlag = false; // 补款取表里还是消息里的金额标志位 默认值为false
	private boolean bIsmpDisplayNum = false; // 补款取表里还是消息里的金额标志位 默认值为false
	private boolean bDisplayCheckdupFlag = false; // VAC踢重是否采用OCSConfig的CHECKDUP值
	private boolean bPerNbrAcctMappingFlag = false; // 联通在信按号码匹配费用项开关
	private boolean bSmsSpcStrategyFlag = false; // sms业务个性化策略控制开关， 默认值false
	private boolean bIsmpSpcStrategyFlag = false; // ismp业务个性化策略控制开关，默认值false
	private boolean bCcgSpcStrategyFlag = false; // ccg业务个性化策略控制开关，默认值false
	private boolean bDslSpcStrategyFlag = false; // dsl业务个性化策略控制开关，默认值false
	private boolean isDisplayFormat = false; // 是否打印format消息
	private boolean bSmsRefundFlag = false; // 短信补款是否需要查询扣费历史记录表，默认为false
	private boolean bIsCanbeNegative = false; // 是否允许把余额扣成负值
	private boolean bIsCanbeZeroToNegative = false;
	private boolean bIsForTest = false; // 是否分大小时间片
	private boolean bIsExactFee = false; // 是否需要精确计费
	private int iExactFeeEvents = 0; // 需要精确计费的事件类型 TODO
	private int nTariffChangeFlag = 0; // 费率切换开关
	private int nBusy2IdleFlag = 0; // 忙闲时切换开关
	private int nGGSNArrearControlFlag = 0;
	private int nBackRateFlag = 0; // 如果在扣费历史表中没找到扣费纪录的话单是否进行批价开关.默认值true，对补款消息进行批价
	private int nBackRateErrorCode = 20001;// 返回给SM的批价错误码。如果关闭补款批价开关,必须设置这个值。默认值为20001
	private int iOperContTypes = 0;
	private int nMTypeRate = 0; // 1M对应的金钱
	private int nTTypeRate = 0; // 1T对应的金钱
	private String strAttrCode = "";
	private long lnAlarmBalanceLower = 0; // 最小余额提醒阀值
	private int nDirectRatableFlag = 0;
	private String strCheckDup = ""; // 封顶踢重标识
	private String strMainCode_01 = "";
	private String strMainCode_02 = "";
	private String strSecondCode_01 = "";
	private String strSecondCode_02 = "";
	private int unABMNum;

	Configure getConfigure;

	Configure pRawConf;

	void init() {
		// m_bsBalanceType.reset();

		int nValue = 0;
		String strValue = null;
		String iVec;

		// 读取配置,余额是否可以扣成负的，1－可以，0不可以
		bIsCanbeNegative = false;

		// 读取配置,余额为0时，是否可以扣成负的，1－可以，0不可以
		bIsCanbeZeroToNegative = false;

		// 是否区分大小时间片,1:区分 0:不区分
		bIsForTest = false;

		// 短信扣费是否写扣费记录历史表，补款是否查扣费历史记录表
		bSmsRefundFlag = false;

		if (!pRawConf.getParam("system", "MoneyBalanceType", strValue)) {
			bsBalanceType.set(2);
		} else {

		}

		bPerNbrAcctMappingFlag = false;
		if (pRawConf.getParam("system", "PerNbrAcctMappingFlag", strValue)) {
			// if(1 == ::atoi(strValue.c_str()))
			bPerNbrAcctMappingFlag = true;
		}

		// 默认不打开
		strISMPServiceType = "R402|R73";
		if (pRawConf.getParam("system", "EnableServiceType", strValue)) {
			if (!strValue.isEmpty())
				strISMPServiceType = strValue;
		}
		// split(m_strISMPServiceType, '|', iVec);
		// if( iVec.size() == 2 )
		// {
		// m_strISMPServiceFirstType = iVec[0].empty() ? "-1":iVec[0];
		// m_strISMPServiceSecondType = iVec[1].empty() ? "-1":iVec[1];
		// }

		// 默认值为90
		nISMPServiceType = 90;
		pRawConf.getParam("system", "EnableServiceTypeValue", nISMPServiceType);

		// 读取配置文件中的是否需要精确计费,1:需要 0:不需要
		bIsExactFee = false;
		if (pRawConf.getParam("system", "IsExactFee", nValue)) {
			if (nValue == 0)
				bIsExactFee = false;
			else
				bIsExactFee = true;
		}
		strAttrCode = "CELL";
		pRawConf.getParam("system", "Attr_Code", strAttrCode);

		nTariffChangeFlag = 0;
		pRawConf.getParam("system", "TariffChange", nTariffChangeFlag);

		nBusy2IdleFlag = 0;
		pRawConf.getParam("system", "Busy2Idle", nBusy2IdleFlag);

		nBackRateFlag = 1;
		pRawConf.getParam("system", "IsBackRatFlag", nBackRateFlag);

		nBackRateErrorCode = 20001;
		pRawConf.getParam("system", "BackRatErrNum", nBackRateErrorCode);

		strValue = "";
		pRawConf.getParam("system", "BackRatOperContType", strValue);
		if (!strValue.isEmpty()) {

		}

		nMTypeRate = 30;
		pRawConf.getParam("system", "MTypeChangeRate", nMTypeRate);

		nTTypeRate = 20;
		pRawConf.getParam("system", "TTypeChangeRate", nTTypeRate);

		nGGSNArrearControlFlag = 0;
		pRawConf.getParam("system", "GgsnArrearControl", nGGSNArrearControlFlag);

		unABMNum = 0;
		pRawConf.getParam("system", "ABMNum", unABMNum);

		// 根据什么来获取封顶踢重的账期，默认为R401和R503
		strCheckDup = "R401|R72;R503|R80";
		pRawConf.getParam("system", "CHECKDUP", strValue);
		if (strValue == "")
			strCheckDup = "R401|R72;R503|R80";
		// parseCheckDup();

		bDisplayCheckdupFlag = false;
		if (pRawConf.getParam("system", "DisplayCheckdupFlag", strValue)) {
			if (strValue.equals("1"))
				bDisplayCheckdupFlag = true;
		}

		bIsmpControlFlag = false;
		if (pRawConf.getParam("system", "ISMPControlFlag", strValue)) {
			if (strValue.equals("1"))
				bIsmpControlFlag = true;
		}

		bIsmpDisplayNum = false;
		if (pRawConf.getParam("system", "ISMPDisplayNum", strValue)) {
			if (strValue.equals("1"))
				bIsmpDisplayNum = true;
		}
	}

	public void parseCheckDup() {

		// 对其分割
		// R401|R73;R503|R80
		strMainCode_01 = "-1";
		strMainCode_02 = "-1";

		strSecondCode_01 = "-1";
		strSecondCode_02 = "-1";
		String iVecLevel1 = "";
		String iVecLevel2 = "";

	}

	public Configure getConfigure() {
		log.debug("getConfigure:" + pRawConf);

		return pRawConf;
	}

	public boolean getParam(String group_, String param_, String value_) {
		log.debug("getParam:" + group_ + "," + param_ + "," + value_);

		return pRawConf.getParam(group_, param_, value_);
	}

	public boolean getParam(String group_, String param_, int value_) {
		log.debug("getParam:" + group_ + "," + param_ + "," + value_);

		return pRawConf.getParam(group_, param_, value_);
	}

	public boolean getParam(String group_, String param_, long value_) {
		log.debug("getParam:" + group_ + "," + param_ + "," + value_);

		return pRawConf.getParam(group_, param_, value_);
	}

	public boolean isMoneyBalanceType(int nUnitTypeId_) {
		log.debug("isMoneyBalanceType:" + nUnitTypeId_);

		if (nUnitTypeId_ > 0 && nUnitTypeId_ < BALANCE_BITSET_LEN)
			return bsBalanceType.get(nUnitTypeId_);// test(nUnitTypeId_);
		return false;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		RatingConf.log = log;
	}

	public int getBALANCE_BITSET_LEN() {
		return BALANCE_BITSET_LEN;
	}

	public void setBALANCE_BITSET_LEN(int bALANCE_BITSET_LEN) {
		BALANCE_BITSET_LEN = bALANCE_BITSET_LEN;
	}

	public BitSet getBsBalanceType() {
		return bsBalanceType;
	}

	public void setBsBalanceType(BitSet bsBalanceType) {
		this.bsBalanceType = bsBalanceType;
	}

	public String getStrISMPServiceType() {
		return strISMPServiceType;
	}

	public void setStrISMPServiceType(String strISMPServiceType) {
		this.strISMPServiceType = strISMPServiceType;
	}

	public String getStrISMPServiceTypeValue() {
		return strISMPServiceTypeValue;
	}

	public void setStrISMPServiceTypeValue(String strISMPServiceTypeValue) {
		this.strISMPServiceTypeValue = strISMPServiceTypeValue;
	}

	public String getStrISMPServiceFirstType() {
		return strISMPServiceFirstType;
	}

	public void setStrISMPServiceFirstType(String strISMPServiceFirstType) {
		this.strISMPServiceFirstType = strISMPServiceFirstType;
	}

	public String getStrISMPServiceSecondType() {
		return strISMPServiceSecondType;
	}

	public void setStrISMPServiceSecondType(String strISMPServiceSecondType) {
		this.strISMPServiceSecondType = strISMPServiceSecondType;
	}

	public String getiIsmpServiceTypeValues() {
		return iIsmpServiceTypeValues;
	}

	public void setiIsmpServiceTypeValues(String iIsmpServiceTypeValues) {
		this.iIsmpServiceTypeValues = iIsmpServiceTypeValues;
	}

	public int getnISMPServiceType() {
		return nISMPServiceType;
	}

	public void setnISMPServiceType(int nISMPServiceType) {
		this.nISMPServiceType = nISMPServiceType;
	}

	public boolean isbInGrpUserPrdFlag() {
		return bInGrpUserPrdFlag;
	}

	public void setbInGrpUserPrdFlag(boolean bInGrpUserPrdFlag) {
		this.bInGrpUserPrdFlag = bInGrpUserPrdFlag;
	}

	public boolean isbSmsGrpUserPrdFlag() {
		return bSmsGrpUserPrdFlag;
	}

	public void setbSmsGrpUserPrdFlag(boolean bSmsGrpUserPrdFlag) {
		this.bSmsGrpUserPrdFlag = bSmsGrpUserPrdFlag;
	}

	public boolean isbSmsGrpFlag() {
		return bSmsGrpFlag;
	}

	public void setbSmsGrpFlag(boolean bSmsGrpFlag) {
		this.bSmsGrpFlag = bSmsGrpFlag;
	}

	public boolean isbIsmpGrpFlag() {
		return bIsmpGrpFlag;
	}

	public void setbIsmpGrpFlag(boolean bIsmpGrpFlag) {
		this.bIsmpGrpFlag = bIsmpGrpFlag;
	}

	public boolean isbCcgGrpFlag() {
		return bCcgGrpFlag;
	}

	public void setbCcgGrpFlag(boolean bCcgGrpFlag) {
		this.bCcgGrpFlag = bCcgGrpFlag;
	}

	public boolean isbDslGrpFlag() {
		return bDslGrpFlag;
	}

	public void setbDslGrpFlag(boolean bDslGrpFlag) {
		this.bDslGrpFlag = bDslGrpFlag;
	}

	public boolean isbMoneyBalNotPositiveFlag() {
		return bMoneyBalNotPositiveFlag;
	}

	public void setbMoneyBalNotPositiveFlag(boolean bMoneyBalNotPositiveFlag) {
		this.bMoneyBalNotPositiveFlag = bMoneyBalNotPositiveFlag;
	}

	public boolean isbAcOpsInfoFlag() {
		return bAcOpsInfoFlag;
	}

	public void setbAcOpsInfoFlag(boolean bAcOpsInfoFlag) {
		this.bAcOpsInfoFlag = bAcOpsInfoFlag;
	}

	public boolean isbIsmpControlFlag() {
		return bIsmpControlFlag;
	}

	public void setbIsmpControlFlag(boolean bIsmpControlFlag) {
		this.bIsmpControlFlag = bIsmpControlFlag;
	}

	public boolean isbIsmpDisplayNum() {
		return bIsmpDisplayNum;
	}

	public void setbIsmpDisplayNum(boolean bIsmpDisplayNum) {
		this.bIsmpDisplayNum = bIsmpDisplayNum;
	}

	public boolean isbDisplayCheckdupFlag() {
		return bDisplayCheckdupFlag;
	}

	public void setbDisplayCheckdupFlag(boolean bDisplayCheckdupFlag) {
		this.bDisplayCheckdupFlag = bDisplayCheckdupFlag;
	}

	public boolean isbPerNbrAcctMappingFlag() {
		return bPerNbrAcctMappingFlag;
	}

	public void setbPerNbrAcctMappingFlag(boolean bPerNbrAcctMappingFlag) {
		this.bPerNbrAcctMappingFlag = bPerNbrAcctMappingFlag;
	}

	public boolean isbSmsSpcStrategyFlag() {
		return bSmsSpcStrategyFlag;
	}

	public void setbSmsSpcStrategyFlag(boolean bSmsSpcStrategyFlag) {
		this.bSmsSpcStrategyFlag = bSmsSpcStrategyFlag;
	}

	public boolean isbIsmpSpcStrategyFlag() {
		return bIsmpSpcStrategyFlag;
	}

	public void setbIsmpSpcStrategyFlag(boolean bIsmpSpcStrategyFlag) {
		this.bIsmpSpcStrategyFlag = bIsmpSpcStrategyFlag;
	}

	public boolean isbCcgSpcStrategyFlag() {
		return bCcgSpcStrategyFlag;
	}

	public void setbCcgSpcStrategyFlag(boolean bCcgSpcStrategyFlag) {
		this.bCcgSpcStrategyFlag = bCcgSpcStrategyFlag;
	}

	public boolean isbDslSpcStrategyFlag() {
		return bDslSpcStrategyFlag;
	}

	public void setbDslSpcStrategyFlag(boolean bDslSpcStrategyFlag) {
		this.bDslSpcStrategyFlag = bDslSpcStrategyFlag;
	}

	public boolean isDisplayFormat() {
		return isDisplayFormat;
	}

	public void setDisplayFormat(boolean isDisplayFormat) {
		this.isDisplayFormat = isDisplayFormat;
	}

	public boolean isbSmsRefundFlag() {
		return bSmsRefundFlag;
	}

	public void setbSmsRefundFlag(boolean bSmsRefundFlag) {
		this.bSmsRefundFlag = bSmsRefundFlag;
	}

	public boolean isbIsCanbeNegative() {
		return bIsCanbeNegative;
	}

	public void setbIsCanbeNegative(boolean bIsCanbeNegative) {
		this.bIsCanbeNegative = bIsCanbeNegative;
	}

	public boolean isbIsCanbeZeroToNegative() {
		return bIsCanbeZeroToNegative;
	}

	public void setbIsCanbeZeroToNegative(boolean bIsCanbeZeroToNegative) {
		this.bIsCanbeZeroToNegative = bIsCanbeZeroToNegative;
	}

	public boolean isbIsForTest() {
		return bIsForTest;
	}

	public void setbIsForTest(boolean bIsForTest) {
		this.bIsForTest = bIsForTest;
	}

	public boolean isbIsExactFee() {
		return bIsExactFee;
	}

	public void setbIsExactFee(boolean bIsExactFee) {
		this.bIsExactFee = bIsExactFee;
	}

	public int getiExactFeeEvents() {
		return iExactFeeEvents;
	}

	public void setiExactFeeEvents(int iExactFeeEvents) {
		this.iExactFeeEvents = iExactFeeEvents;
	}

	public int getnTariffChangeFlag() {
		return nTariffChangeFlag;
	}

	public void setnTariffChangeFlag(int nTariffChangeFlag) {
		this.nTariffChangeFlag = nTariffChangeFlag;
	}

	public int getnBusy2IdleFlag() {
		return nBusy2IdleFlag;
	}

	public void setnBusy2IdleFlag(int nBusy2IdleFlag) {
		this.nBusy2IdleFlag = nBusy2IdleFlag;
	}

	public int getnGGSNArrearControlFlag() {
		return nGGSNArrearControlFlag;
	}

	public void setnGGSNArrearControlFlag(int nGGSNArrearControlFlag) {
		this.nGGSNArrearControlFlag = nGGSNArrearControlFlag;
	}

	public int getnBackRateFlag() {
		return nBackRateFlag;
	}

	public void setnBackRateFlag(int nBackRateFlag) {
		this.nBackRateFlag = nBackRateFlag;
	}

	public int getnBackRateErrorCode() {
		return nBackRateErrorCode;
	}

	public void setnBackRateErrorCode(int nBackRateErrorCode) {
		this.nBackRateErrorCode = nBackRateErrorCode;
	}

	public int getiOperContTypes() {
		return iOperContTypes;
	}

	public void setiOperContTypes(int iOperContTypes) {
		this.iOperContTypes = iOperContTypes;
	}

	public int getnMTypeRate() {
		return nMTypeRate;
	}

	public void setnMTypeRate(int nMTypeRate) {
		this.nMTypeRate = nMTypeRate;
	}

	public int getnTTypeRate() {
		return nTTypeRate;
	}

	public void setnTTypeRate(int nTTypeRate) {
		this.nTTypeRate = nTTypeRate;
	}

	public String getStrAttrCode() {
		return strAttrCode;
	}

	public void setStrAttrCode(String strAttrCode) {
		this.strAttrCode = strAttrCode;
	}

	public long getLnAlarmBalanceLower() {
		return lnAlarmBalanceLower;
	}

	public void setLnAlarmBalanceLower(long lnAlarmBalanceLower) {
		this.lnAlarmBalanceLower = lnAlarmBalanceLower;
	}

	public int getnDirectRatableFlag() {
		return nDirectRatableFlag;
	}

	public void setnDirectRatableFlag(int nDirectRatableFlag) {
		this.nDirectRatableFlag = nDirectRatableFlag;
	}

	public String getStrCheckDup() {
		return strCheckDup;
	}

	public void setStrCheckDup(String strCheckDup) {
		this.strCheckDup = strCheckDup;
	}

	public String getStrMainCode_01() {
		return strMainCode_01;
	}

	public void setStrMainCode_01(String strMainCode_01) {
		this.strMainCode_01 = strMainCode_01;
	}

	public String getStrMainCode_02() {
		return strMainCode_02;
	}

	public void setStrMainCode_02(String strMainCode_02) {
		this.strMainCode_02 = strMainCode_02;
	}

	public String getStrSecondCode_01() {
		return strSecondCode_01;
	}

	public void setStrSecondCode_01(String strSecondCode_01) {
		this.strSecondCode_01 = strSecondCode_01;
	}

	public String getStrSecondCode_02() {
		return strSecondCode_02;
	}

	public void setStrSecondCode_02(String strSecondCode_02) {
		this.strSecondCode_02 = strSecondCode_02;
	}

	public int getUnABMNum() {
		return unABMNum;
	}

	public void setUnABMNum(int unABMNum) {
		this.unABMNum = unABMNum;
	}

	public Configure getGetConfigure() {
		return getConfigure;
	}

	public void setGetConfigure(Configure getConfigure) {
		this.getConfigure = getConfigure;
	}

	public Configure getpRawConf() {
		return pRawConf;
	}

	public void setpRawConf(Configure pRawConf) {
		this.pRawConf = pRawConf;
	}

}
