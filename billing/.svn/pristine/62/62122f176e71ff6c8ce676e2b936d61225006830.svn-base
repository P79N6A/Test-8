package com.tydic.beijing.billing.rating.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
/**
 * 计费消息，包括基本信息、可变信息和命令信息
 * @author zhanghengbo
 *
 */
public class RatingMsg  {
	
	private int m_nMsgType;
	private int m_nMsgVersion;
	private BaseMsg baseMsg=new BaseMsg();
	private VarMsg varMsg=new VarMsg();
	private CommandMsg commandMsg=new CommandMsg();
	private RatingExtMsg m_iRatingExtMsg=new RatingExtMsg();
	private ExtMsg m_iExtMsg=new ExtMsg();
    private BalanceInMsg m_iBalanceInMsg=new BalanceInMsg();
    private BalanceOutMsg m_iBalanceOutMsg;
    private ServInfo m_iServInfo = new ServInfo();
    private UserMsg m_iUserMsg = new UserMsg();
    private InfoUser infoUser;//既然我们有infosuer，为什么还要使用usermsg这个老的模型????????  zhanghb add
    private RatingOutMsg m_iRatingOutMsg=new RatingOutMsg();
    private ChargedPartyMsg m_iChargedMsg=new ChargedPartyMsg();
    
    private UserInfoForMemCached userinfoForMemcached;

	//private RuleEventTypeTree ruleEventTypeTree;   事件码改为放在 m_iextmsg
	
	private int m_nDirectBalaceFlag; //是否直扣消息 0否 1是
	private int m_nBillingFlag; //会话类型 R602取值 1:Initial 2:Update 3:Term 4:Event 5:EvtBack -1:error
	private int m_nCreditMeasureRef;            ///信用单位
	//private boolean bExistLastValue; //是否跨费率点
	
	private Map msgMap=new HashMap(); /////解析的map也放到ratingmsg里
	private int m_nRatableFlagOffline;
	private String strRequestMsg; //接收到的统一接入消息
	
	private String acctMonthId ; //用于返回给ua，给后续出账分库
	private String partitionNo ;  //用于返回给ua，给后续出账分库
	 
	
	public Map getMsgMap() {
		return msgMap;
	}

	public void setMsgMap(Map msgMap) {
		this.msgMap = msgMap;
	}



	// temp param
	private long dbtime;
	private long eventtime;
	
	
	public long getDbtime() {
		return dbtime;
	}

	public void setDbtime(long dbtime) {
		this.dbtime = dbtime;
	}

	public long getEventtime() {
		return eventtime;
	}

	public void setEventtime(long eventtime) {
		this.eventtime = eventtime;
	}

	//是否需要查询封顶值（ISMP直接扣费）
	public boolean isNeedCheckTopValue()
	{
	    
	    return m_iBalanceInMsg.getnRealCtrlType()==1?true:false;
	}
	
	//是否需要进行剔重（ISMP直接扣费）
	public boolean isNeedCheckRepetition()
	{
	    
	    return m_iBalanceInMsg.getnRealCtrlType()==2?true:false;
	}
	
	
	
	public InfoUser getInfoUser() {
		return infoUser;
	}

	public void setInfoUser(InfoUser infoUser) {
		this.infoUser = infoUser;
	}



	private String m_strUserState="";
	
	
	public RatingExtMsg getM_iRatingExtMsg() {
		return m_iRatingExtMsg;
	}
	public void setM_iRatingExtMsg(RatingExtMsg m_iRatingExtMsg) {
		this.m_iRatingExtMsg = m_iRatingExtMsg;
	}
 
	
	public int getM_nMsgType() {
		return m_nMsgType;
	}
	public void setM_nMsgType(int m_nMsgType) {
		this.m_nMsgType = m_nMsgType;
	}
	public int getM_nMsgVersion() {
		return m_nMsgVersion;
	}
	public void setM_nMsgVersion(int m_nMsgVersion) {
		this.m_nMsgVersion = m_nMsgVersion;
	}
	public ExtMsg getM_iExtMsg() {
		return m_iExtMsg;
	}
	public void setM_iExtMsg(ExtMsg m_iExtMsg) {
		this.m_iExtMsg = m_iExtMsg;
	}
	public BaseMsg getBaseMsg() {
		return baseMsg;
	}
	public void setBaseMsg(BaseMsg baseMsg) {
		this.baseMsg = baseMsg;
	}
	public VarMsg getVarMsg() {
		return varMsg;
	}
	public void setVarMsg(VarMsg varMsg) {
		this.varMsg = varMsg;
	}
	public CommandMsg getCommandMsg() {
		return commandMsg;
	}
	public void setCommandMsg(CommandMsg commandMsg) {
		this.commandMsg = commandMsg;
	}
	
	public int getM_nDirectBalaceFlag() {
		return m_nDirectBalaceFlag;
	}
	public void setM_nDirectBalaceFlag(int m_nDirectBalaceFlag) {
		this.m_nDirectBalaceFlag = m_nDirectBalaceFlag;
	}

	
	
	public BalanceInMsg getM_iBalanceInMsg() {
		return m_iBalanceInMsg;
	}
	public void setM_iBalanceInMsg(BalanceInMsg m_iBalanceInMsg) {
		this.m_iBalanceInMsg = m_iBalanceInMsg;
	}
	public int getM_nCreditMeasureRef() {
		return m_nCreditMeasureRef;
	}
	public void setM_nCreditMeasureRef(int m_nCreditMeasureRef) {
		this.m_nCreditMeasureRef = m_nCreditMeasureRef;
	}
	public int getM_nBillingFlag() {
		return m_nBillingFlag;
	}
	public void setM_nBillingFlag(int m_nBillingFlag) {
		this.m_nBillingFlag = m_nBillingFlag;
	}
	public String getM_strUserState() {
		return m_strUserState;
	}
	public void setM_strUserState(String m_strUserState) {
		this.m_strUserState = m_strUserState;
	}
	
	
	
	public ServInfo getM_iServInfo() {
		return m_iServInfo;
	}
	public void setM_iServInfo(ServInfo m_iServInfo) {
		this.m_iServInfo = m_iServInfo;
	}
	public UserMsg getM_iUserMsg() {
		return m_iUserMsg;
	}
	public void setM_iUserMsg(UserMsg m_iUserMsg) {
		this.m_iUserMsg = m_iUserMsg;
	}
	
	
	
	
	public int getM_nRatableFlagOffline() {
		return m_nRatableFlagOffline;
	}

	public void setM_nRatableFlagOffline(int m_nRatableFlagOffline) {
		this.m_nRatableFlagOffline = m_nRatableFlagOffline;
	}

	public BalanceOutMsg getM_iBalanceOutMsg() {
		return m_iBalanceOutMsg;
	}
	public void setM_iBalanceOutMsg(BalanceOutMsg m_iBalanceOutMsg) {
		this.m_iBalanceOutMsg = m_iBalanceOutMsg;
	}
	public RatingOutMsg getM_iRatingOutMsg() {
		return m_iRatingOutMsg;
	}
	public void setM_iRatingOutMsg(RatingOutMsg m_iRatingOutMsg) {
		this.m_iRatingOutMsg = m_iRatingOutMsg;
	}
	
	
	
	
	public String getStrRequestMsg() {
		return strRequestMsg;
	}

	public void setStrRequestMsg(String strRequestMsg) {
		this.strRequestMsg = strRequestMsg;
	}
	public ChargedPartyMsg getM_iChargedMsg() {
		return m_iChargedMsg;
	}

	public void setM_iChargedMsg(ChargedPartyMsg m_iChargedMsg) {
		this.m_iChargedMsg = m_iChargedMsg;
	}
	
	
	public boolean isRatingOk(){
		return m_nRatableFlagOffline ==1;
	}
	
	public boolean isDirectBalance(){
		return m_nDirectBalaceFlag ==1;
	}

	/**
	 * 获取统一接入消息中某个参数
	 */
	public String getValue(String param,int index){
		//TODO 这个getvalue到底要干毛用???
    	String paramValue="";
    	int n=0;
    	
//    	if(strRequestMsg.indexOf(param) >=0){
//    		paramValue = strRequestMsg.substring(strRequestMsg.indexOf(param)+param.length(), strRequestMsg.indexOf("]", strRequestMsg.indexOf(param)+param.length()));

    	for(Object key:msgMap.keySet()){
    		if(msgMap.get(key) instanceof String){
    			if(key.equals(param))
    				n++;
    			if(n== index)
    				return  (String)msgMap.get(key);
    		}else{
    			List<Map> listMap = (List<Map>) msgMap.get(key);
    			for(Map tmpMap:listMap){
    				for(Object key2:tmpMap.keySet()){
    					if(key.equals(param))
    	    				n++;
    	    			if(n== index)
    	    				return (String)tmpMap.get(key);
    				}
    				
    			}
    			
    		}
    		
    	}
    	
    	
    	return paramValue;
    }
	/**
	 * 是否需要批价 
	 * @return
	 */
	public boolean isNeedRating(){
		
		//对于直接下了费用的，是不需要批价的
		if(msgMap ==null){
			return true;			
		}
		
	    if(msgMap.containsKey("needRating")){
	    	
	    	String strNeedRating = (String) msgMap.get("needRating");
	    	
	    	if(strNeedRating.equals("N"))
	    		return false;
	    }
		

		
		
	    return true;
	}
	
	
	/*
	 *补款消息是否需要按sm-id查询扣费记录表
	 *目前只有ISMP的彩信中心消息需要查询，因为无计费号码
	 */
	public boolean isNeedQueryDeductHis()
	{
	    if (RatingMacro.EVENT_BACK==m_nBillingFlag)
	        return true;

	    return false;
	}

	
	public UserInfoForMemCached getUserinfoForMemcached() {
		return userinfoForMemcached;
	}

	public void setUserinfoForMemcached(UserInfoForMemCached userinfoForMemcached) {
//		InfoUser iu=userinfoForMemcached.getInfoUser();
//		if(iu==null){
//			//System.out.println("InfoUser 无数据");
//			return ;
//		}
//		//System.out.println("ofr_id:"+iu.getOfr_id());
//		List<LifeUserProduct> products=userinfoForMemcached.getUserProducts();
////		m_iUserMsg.setnOfrId(Integer.parseInt(iu.getOfr_id()));
//		m_iUserMsg.setnOfrId(Integer.parseInt(products.get(0).getOfr_id()));
//		m_iUserMsg.setLnServId(iu.getUser_id());
//		m_iUserMsg.setnLatnId(Integer.parseInt(iu.getLocal_net()));
//		m_iServInfo.setStrAcceptDate(new SimpleDateFormat("yyyyMMddHH24MMss").format(iu.getCreate_date()));//入网时间
//		m_iServInfo.setLnServId(iu.getUser_id());
		this.userinfoForMemcached = userinfoForMemcached;
		
	}

	public String getAllSessionStartTimes(){
		
		 if( baseMsg.getM_strStartTime() == null || baseMsg.getM_strStartTime().length() ==0 )
		    {
		        ///取一点时间
			    SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHH24mmss");
                return sdf.format(new Date());
		    }

		    return baseMsg.getM_strStartTime();
		
	}
	
	//////打印成员
	public void print(){
		try {
//			System.out.println("m_nMsgType="+m_nMsgType);
//			System.out.println("m_nMsgVersion="+m_nMsgVersion);
//			System.out.println("m_nDirectBalaceFlag="+m_nDirectBalaceFlag);
//			System.out.println("m_nBillingFlag="+m_nBillingFlag);
//			System.out.println("m_nCreditMeasureRef="+m_nCreditMeasureRef);
			
			baseMsg.print();
			varMsg.print();
			commandMsg.print();
			
			if(m_iRatingExtMsg == null){
				//System.out.println("m_iRatingExtMsg == null");
			}
			
			m_iRatingExtMsg.print();
			m_iExtMsg.print();
			//m_iBalanceInMsg.print();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	/*
	 * private int m_nMsgType;
	private int m_nMsgVersion;
	private BaseMsg baseMsg;
	private VarMsg varMsg;
	private CommandMsg commandMsg;
	private RatingExtMsg m_iRatingExtMsg;
	private ExtMsg m_iExtMsg;
    private BalanceInMsg m_iBalanceInMsg;

	//private RuleEventTypeTree ruleEventTypeTree;   事件码改为放在 m_iextmsg
	
	private int m_nDirectBalaceFlag; //是否直扣消息 0否 1是
	private int m_nBillingFlag; //会话类型 R602取值 1:Initial 2:Update 3:Term 4:Event 5:EvtBack -1:error
	private int m_nCreditMeasureRef;            ///信用单位
	//private boolean bExistLastValue; //是否跨费率点 
	
	*/
	
	
	public void init(){
		this.m_nMsgType = -1;
		this.m_nMsgVersion =-1;
		this.baseMsg=new BaseMsg();
		this.varMsg=new VarMsg();
		this.commandMsg=new CommandMsg();
		this.m_iRatingExtMsg=new RatingExtMsg();
		this.m_iExtMsg=new ExtMsg();
		this.m_iBalanceInMsg=new BalanceInMsg();
		this.m_iBalanceOutMsg = new BalanceOutMsg();
		this.m_iServInfo = new ServInfo();
		this.m_iUserMsg = new UserMsg();
		this.infoUser = new InfoUser();//既然我们有infosuer，为什么还要使用usermsg这个老的模型????????  zhanghb add
		this.m_iRatingOutMsg=new RatingOutMsg();
		this.m_iChargedMsg=new ChargedPartyMsg() ;   
		this.userinfoForMemcached = new UserInfoForMemCached();
		this.m_nDirectBalaceFlag =0; //是否直扣消息 0否 1是
		this.m_nBillingFlag = -1; //会话类型 R602取值 1:Initial 2:Update 3:Term 4:Event 5:EvtBack -1:error
		this.m_nCreditMeasureRef = -1;            ///信用单位
		this.msgMap=new HashMap(); /////解析的map也放到ratingmsg里
		this.m_nRatableFlagOffline = -1;
		this.strRequestMsg = ""; //接收到的统一接入消息
	}

	public String getAcctMonthId() {
		return acctMonthId;
	}

	public void setAcctMonthId(String acctMonthId) {
		this.acctMonthId = acctMonthId;
	}

	public String getPartitionNo() {
		return partitionNo;
	}

	public void setPartitionNo(String partitionNo) {
		this.partitionNo = partitionNo;
	}
	

}
