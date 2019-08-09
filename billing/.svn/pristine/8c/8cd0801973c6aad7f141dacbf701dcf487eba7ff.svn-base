package com.tydic.beijing.billing.rating.domain;

public  final class ParamData {

	/*
	 * 帐期类型
	 */
       
	public static final int ACCOUNT_DAY=1;
	public static final int ACCOUNT_MONTH=2;
	public static final int ACCOUNT_HALFYEAR=3;
	/*
	 * 属主类型
	 * 80I 客户   80A 产品实例   80C 销售品实例    80J 帐户
	 */

	public static final String OWNERTYPE_PRODINST="80A";
	public static final String OWNERTYPE_SELLINST="80C";
	public static final String OWNERTYPE_CUSTOMER="80I";
	public static final String OWNERTYPE_ACCOUNT="80J";
	
	
	/*
	 * 1       //自然天。如：2009-09-02 00:00:00~2009-09-02 23:59:59
	 * 2       //动态天类型1。如：2009-09-02 01:00:00~2009-09-03 00:59:59
	 * 3       //动态天类型2。如：2009-09-02 01:00:00~2009-09-03 23:59:59
	 * 4       //自然月。如：2009-09-01~2009-09-30
	 * 5       //动态月。如：2009-09-02~2009-10-01
	 * 6       //自然周。如：2009-08-30~2009-09-05.其中2009-08-30为星期天
	 * 7       //动态周。如：2009-09-01~2009-09-07. 其中2009-09-01为星期2
	 */

	public static final int BILLING_CYCLE_NATURE_DAY=1;
	public static final int BILLING_CYCLE_DYNAMIC_DAY_1=2;
	public static final int BILLING_CYCLE_DYNAMIC_DAY_2=3;
	public static final int BILLING_CYCLE_NATURE_MONTH=4;
	public static final int BILLING_CYCLE_DYNAMIC_MONTH=5;
	public static final int BILLING_CYCLE_NATURE_WEEK=6;
	public static final int BILLING_CYCLE_DYNAMIC_WEEK=7;
	
	//累计量参考类型
	
	public static final String REFTYPE_R0C="R0C";
	public static final String REFTYPE_R0D="R0D";
	public static final String REFTYPE_R0E="R0E";
	
//	public enum SumLifeType{
//		LifeType_Day(0),LifeType_Month(1),LifeType_HalfYear(2);
//		private SumLifeType(int value){this.value=value;}
//		private int value;
//		public int value(){return value;}
//	}

	/*
	 * 计费命令 -1 增加一个和NO_FEE_CMD对应的命令
	 * 0 只有预占
	 * 1只有实扣
	 * 2 实扣+预占
	 * 3 事件返还
	 */

	public static final int FEECMD_NOOPER=-1;
	public static final int FEECMD_REQ=0;
	public static final int FEECMD_REAL=1;
	public static final int FEECMD_REALREQ=2;
	public static final int FEECMD_EVENTBACK=3;
	
	
	/*
	 * 会话类型 
	 */

	public static final int SESSION_BEGIN=1;
	public static final int SESSION_UPDATE=2;
	public static final int SESSION_END=3;
	public static final int SESSION_FEE=4;
	public static final int SESSION_BACK=5;
	
	//操作类型
	public static final int OPER_FREE=0;
	public static final int OPER_REQ=1;
	public static final int OPER_REAL=2;
	
	//用户状态
	public static final String USER_ACTIVE_N="001";  //未激活  原编码F0A
	
	//入网日期偏移类型
	public static final int INNET_OFFSET_DAY=0;
	public static final int INNET_OFFSET_MONTH=1;
	public static final int DINNER_OFFSET_DAY=2;
	public static final int DINNER_OFFSET_MONTH=3;
	
	//业务公式操作符
	public static final char BEST_FAV_MODE=',';//取优
	public static final char OVERLAY_FAV_MODE='&';//叠加
	public static final char MUTEX_FAV_MODE='!';//排它
	
	//MsgType(消息类型),业务消息,整型表示
	public static final int CODE_IN_BRR =  60 ;     //批价请求  EVENT_BACK     CODE_SMS_BRR CODE_ISMP_BRR
	public static final int CODE_IN_BRA =  61 ;     //批价应答      
	public static final int CODE_SMS_BRR = 70 ;     //批价请求      
	public static final int CODE_SMS_BRA =  71 ;     //批价应答      
	public static final int CODE_CCG_BRR =  80 ;     //批价请求      
	public static final int CODE_CCG_BRA =  81 ;     //批价应答      
	public static final int CODE_ISMP_BRR =  90 ;     //批价请求      
	public static final int CODE_ISMP_BRA =  91 ;     //批价应答 
	public static final int CODE_POC_BRR =  100 ;    //POC批价请求
	public static final int CODE_POC_BRA =  101 ;    //POC批价应答
	public static final int CODE_IM_BRR  =  110  ;   //IM批价请求
	public static final int CODE_IM_BRA  =  111 ;    //IM批价应答 
	public static final int CODE_DSL_BRR =  120  ;   // DSL业务定价请求    
	public static final int CODE_DSL_BRA =   121 ;    // DSL业务定价应答
	public static final int CODE_NORATING_BRR = 130;     // 无需批价请求      
	public static final int CODE_NORATING_BRA = 131 ;    // 无需批价应答

	public static final int CODE_BBR  = 20 ;     //余额操作请求  
	public static final int CODE_BBA  =  21 ;     //余额操作应答  
	
	
	
}
