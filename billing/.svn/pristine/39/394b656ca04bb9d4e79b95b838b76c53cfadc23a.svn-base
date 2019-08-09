package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanDisct {
	public   boolean    bDerived=false;			    //销售品是否为参考对象和计算对象分开的
	public   boolean    bDisct=false;               //是否优惠
    public   int   nGroupFlag=3;           //集团标识0-集团内    1-集团间 2-计费号码集团，对端非集团  3-不参与集团
    public   int   nFamilyNbrFlag=0;       //是否亲情号码标识    0:否； 1：是
    
    
    public   int   lnOfrInstId=-1;   //产品实例标识
    public   int   nOfrInstLatnId=-1;       //产品实例本地网标识
    
    public   boolean    bIsFirstMonth=false;        //解决批价需偏移量参考支持
    public   int   nSpecialType=-1;         //特殊类型0:是销售品属性；1:是用户属性
    public   int   nOfrValueType=-1;        //销售品特殊类型
    public  double  fCoefficient=1;         //如果nSpecialType  为0，则fCoefficient为销售品属性计算段落分段系数;  如果nSpecialType为1，则Coefficient为计算出累计量分段类型
    public   int   lnCallingGroupId=0;     //主叫的集团（IVPN标识）
    public   int   lnCalledGroupId=0;      //被叫的集团（IVPN标识）
    public  String strObjectType="C";          //对象类型（默认为0）
    public   int   nOfrType=-1;
    
    public   int   nAtomOfrId=-1;
    public   int   nPricingPlanId=0;       //定价计划标识
    public   int   nOfrId=-1;               //销售品标识
    private int latn=-1;
    public   long   lnStrategyId=0;         //定价计划+事件   ->     策略    1:1
    
    public   int   nOfrAttr=0;             //0:普通优惠销售品    1:小区优惠销售品
    public   int   nCellFlag=0;
    
    public   int   nCalcPriority=0;        //组内的优先级
    public  String    cOfrGroup="";
    public  String strElementType="";     //明细实例类型   A1-用户 D1-客户 E1-帐户 F1-集团  U1-集团用户销售品
    public  String strEffDate="";        //生效日期
    public  String strExpDate="";        //失效日期
    public  String strActiveDate="";     //激活日期
    
    
    //资源扣减增加属性
    private String acct_item_type="";
    private int op_unit=-1;
    private int tail_mode=-1;
    
    
    List<PricingSection> iPricingSections=new ArrayList<PricingSection>(); //定价计划下所有的段落
    Map<String, PricingSection> iPricingSectionMap=new HashMap<String,PricingSection>(); //key: pricing_section_id TODO
	public boolean isbDerived() {
		return bDerived;
	}
	public void setbDerived(boolean bDerived) {
		this.bDerived = bDerived;
	}
	public boolean isbDisct() {
		return bDisct;
	}
	public void setbDisct(boolean bDisct) {
		this.bDisct = bDisct;
	}
	public int getnGroupFlag() {
		return nGroupFlag;
	}
	public void setnGroupFlag(int nGroupFlag) {
		this.nGroupFlag = nGroupFlag;
	}
	public int getnFamilyNbrFlag() {
		return nFamilyNbrFlag;
	}
	public void setnFamilyNbrFlag(int nFamilyNbrFlag) {
		this.nFamilyNbrFlag = nFamilyNbrFlag;
	}
	public int getnOfrId() {
		return nOfrId;
	}
	public void setnOfrId(int nOfrId) {
		this.nOfrId = nOfrId;
	}
	public int getnPricingPlanId() {
		return nPricingPlanId;
	}
	public void setnPricingPlanId(int nPricingPlanId) {
		this.nPricingPlanId = nPricingPlanId;
	}
	public int getLnOfrInstId() {
		return lnOfrInstId;
	}
	public void setLnOfrInstId(int lnOfrInstId) {
		this.lnOfrInstId = lnOfrInstId;
	}
	public int getnOfrInstLatnId() {
		return nOfrInstLatnId;
	}
	public void setnOfrInstLatnId(int nOfrInstLatnId) {
		this.nOfrInstLatnId = nOfrInstLatnId;
	}
	public boolean isbIsFirstMonth() {
		return bIsFirstMonth;
	}
	public void setbIsFirstMonth(boolean bIsFirstMonth) {
		this.bIsFirstMonth = bIsFirstMonth;
	}
	public int getnSpecialType() {
		return nSpecialType;
	}
	public void setnSpecialType(int nSpecialType) {
		this.nSpecialType = nSpecialType;
	}
	public int getnOfrValueType() {
		return nOfrValueType;
	}
	public void setnOfrValueType(int nOfrValueType) {
		this.nOfrValueType = nOfrValueType;
	}
	public double getfCoefficient() {
		return fCoefficient;
	}
	public void setfCoefficient(double fCoefficient) {
		this.fCoefficient = fCoefficient;
	}
	public int getLnCallingGroupId() {
		return lnCallingGroupId;
	}
	public void setLnCallingGroupId(int lnCallingGroupId) {
		this.lnCallingGroupId = lnCallingGroupId;
	}
	public int getLnCalledGroupId() {
		return lnCalledGroupId;
	}
	public void setLnCalledGroupId(int lnCalledGroupId) {
		this.lnCalledGroupId = lnCalledGroupId;
	}
	public String getStrObjectType() {
		return strObjectType;
	}
	public void setStrObjectType(String strObjectType) {
		this.strObjectType = strObjectType;
	}
	public int getnOfrType() {
		return nOfrType;
	}
	public void setnOfrType(int nOfrType) {
		this.nOfrType = nOfrType;
	}
	public int getnAtomOfrId() {
		return nAtomOfrId;
	}
	public void setnAtomOfrId(int nAtomOfrId) {
		this.nAtomOfrId = nAtomOfrId;
	}
	public int getnOfrAttr() {
		return nOfrAttr;
	}
	public void setnOfrAttr(int nOfrAttr) {
		this.nOfrAttr = nOfrAttr;
	}
	public int getnCellFlag() {
		return nCellFlag;
	}
	public void setnCellFlag(int nCellFlag) {
		this.nCellFlag = nCellFlag;
	}
	public long getLnStrategyId() {
		return lnStrategyId;
	}
	public void setLnStrategyId(long lnStrategyId) {
		this.lnStrategyId = lnStrategyId;
	}
	public int getnCalcPriority() {
		return nCalcPriority;
	}
	public void setnCalcPriority(int nCalcPriority) {
		this.nCalcPriority = nCalcPriority;
	}
	public String getcOfrGroup() {
		return cOfrGroup;
	}
	public void setcOfrGroup(String cOfrGroup) {
		this.cOfrGroup = cOfrGroup;
	}
	public String getStrElementType() {
		return strElementType;
	}
	public void setStrElementType(String strElementType) {
		this.strElementType = strElementType;
	}
	public String getStrEffDate() {
		return strEffDate;
	}
	public void setStrEffDate(String strEffDate) {
		this.strEffDate = strEffDate;
	}
	public String getStrExpDate() {
		return strExpDate;
	}
	public void setStrExpDate(String strExpDate) {
		this.strExpDate = strExpDate;
	}
	public String getStrActiveDate() {
		return strActiveDate;
	}
	public void setStrActiveDate(String strActiveDate) {
		this.strActiveDate = strActiveDate;
	}
	public List<PricingSection> getiPricingSections() {
		return iPricingSections;
	}
	public void setiPricingSections(List<PricingSection> iPricingSections) {
		this.iPricingSections = iPricingSections;
	}
	public Map<String, PricingSection> getiPricingSectionMap() {
		return iPricingSectionMap;
	}
	public void setiPricingSectionMap(Map<String, PricingSection> iPricingSectionMap) {
		this.iPricingSectionMap = iPricingSectionMap;
	}
	public int getLatn() {
		return latn;
	}
	public void setLatn(int latn) {
		this.latn = latn;
	}
	public String getAcct_item_type() {
		return acct_item_type;
	}
	public void setAcct_item_type(String acct_item_type) {
		this.acct_item_type = acct_item_type;
	}
	public int getOp_unit() {
		return op_unit;
	}
	public void setOp_unit(int op_unit) {
		this.op_unit = op_unit;
	}
	public int getTail_mode() {
		return tail_mode;
	}
	public void setTail_mode(int tail_mode) {
		this.tail_mode = tail_mode;
	}
	

	
	
	@Override
	public String toString() {
		String ret="nAtomOfrId["+nAtomOfrId+"],nPricingPlanId["+nPricingPlanId+"]";
		return ret;
	}

}
