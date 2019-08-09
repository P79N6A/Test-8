package com.tydic.beijing.billing.rating.domain;

public final class RatingErrorCode {
	
	public static final int ERR_MSG_NULL   = -33000; //请求消息为空
	public static final int  ERR_SERVICE_TYPE_NOT_EXIST               =        -31000 ; // 业务类型不存在
	public static final int  ABM_ERR_NO_USER          =                        -34001 ; //用户不存在
	public static final int ERR_NOT_FIND_OPER_TYPE_ITEM = -30361;  ///未找到相应的事件类型
	public static final int  ERR_MSG_NO_SESSIONID                            = -30003;  //  业务请求消息中没有会话ID
	public static final int  ERR_MSG_NO_REQ_ID                               = -30121;  // 没有请求id
	public static final int  ERR_NO_CHARGEDNBR                               = -33001; //没有计费号码
	public static final int  ERR_MSG_NO_CALLING_NBR                          = -30006;  // 业务请求消息中没有主叫号码
	public static final int  ERR_MSG_NO_CLG_HM_AC                            = -30115;  // 没有主叫归属区号
	public static final int  ERR_MSG_NO_PLAYFLAG                             = -30114;  // 没有用户属性表示
	public static final int  ERR_MSG_NO_CLG_VST_AC                           = -30074;  // 业务请求消息中没有主叫拜访区号
	public static final int  ERR_MSG_NO_CLG_PARTNER                          = -30118;  // 没有主叫运营商
	public static final int  ERR_MSG_NO_CALLED_NBR                           = -30008;  // 业务请求消息中没有被叫号码
	public static final int  ERR_MSG_NO_CLD_PARTNER                          = -30119;  // 没有被叫运营商
	public static final int  ERR_MSG_NO_CALLING_TYPE                         = -30044;  // 业务请求消息中没有话单类型
	public static final int  ERR_MSG_NO_ISMP_ID                              = -30127;   //没有ISMP_id
	public static final int  ERR_SERTYPE_NOT_MATCH_SESSIONTYPE        =        -31001 ; // 业务类型和会话类型不匹配
	public static final int  ERR_MSG_FORMAT               =        -30000 ; //消息格式错误
	public static final int  ERR_NO_MEASUREDOMAIN  = -33002; //没有找到计费资源类型
	public static final int  ERR_INVALID_FORMULA_SYNTAX     =-30333; //产品公式语法错误
	public static final int  ERR_IN_RATING                  =-30337; //批价错误
	public static final int  ERR_GET_RATABLETYPE   = -33003 ;//没有找到累计量类型
	public static final int  ERR_GET_SECTION      = -33004 ;//没有找到相应段落
	public static final int  ERR_GET_TARIFF  = -33005;//没有找到费率信息
	public static final int ERR_TARIFF_CAN_NOT_ZERO             =-30300  ;// 资费打折基数不能为零
	public static final int  ERR_TARIFF_NOT_CLOSED           = -30228 ;// 按计量，分段上限、下限没有闭合
	public static final int ERR_SQL_QUERY_RATABLE      =-10456  ;// 查询累积量异常
	public static final int ERR_NOT_FOUND_STRATERY_BY_PLAN_ID   =-30305  ;// 根据定价计划没有找到定价策略
	public static final int  ERR_NOT_FOUND_PRODUCT_FORMULA  =-30332; //没有找到产品公式
	public static final int  ERR_NOT_FOUND_GROUP_FAV_MODE   =-30331; //没有找到销售品组的优惠模式
	public static final int  ERR_NOT_FOUND_OFR_GROUP        =-30330; //没有找到销售品归属的组
	public static final int ERR_NOT_BASE_TAFIFF_OUTPUT          =-30306  ;// 没有找到对应的资费
	public static final int  ERR_GET_BILLING_CYCLE          =  -10433 ; //获取账期失败
	public static final int ERR_MSG_DEDUCT  =-33006;//消息类型异常
	public static final int ERR_GET_ACCTUNITTYPE  = -33007; //获取账务unittype编码错误
	public static final int  ERR_INVOKE_ACCT =  -33008; //调用销账函数失败
	public static final int  ERR_ROME_NOMONEY =  -33009; //国际漫游类话单未包含消费金额
	public static final int  ERR_MEMCACHE_TIMEOUT =  -33010; //读取memcached超时
	public static final int ERR_NOUSER_BYIMSI = -33011 ;///根据imsi查找用户异常
	public static final int ERR_TOO_OLD                     = -30233; //两个月之前的历史话单不再批价
	public static final int ERR_BALANCE_MINUS =  -33012;   //资源账本扣到了负数
	public static final int ERR_INVALID_ACCTMONTH = -33013;   //无效账期  use_tag =0 act_tag =0
	public static final int ERR_ACCTMONTH_CONFIG = -33014;    //账期配置异常
	public static final int ERR_MEMCACHE_READ =  -33015;  //读取memcached异常
	public static final int ERR_RULE_SYSTEM_SWITCH  =-33016;//读取开关配置表异常
	
	
	
	
	
	public static final int  ERR_REQINFO_NOT_EXIST                    =        -31002 ; // 预占信息不存在
	public static final int  ERR_REALINFO_NOT_EXIST                   =        -31003 ; // 实扣信息不存在
	public static final int  ERR_BACKINFO_NOT_EXIST                   =        -31004 ; // 返还信息不存在
	public static final int  ERR_NO_COMMD_EXIST                       =        -31005 ; // 命令信息不存在，B01,B02,B03,B04,B21均不存在的情况
	public static final int  ERR_SQL_QUERY_OFRID_CELL                 =        -31006 ; //查找定价计划和基站关系时发生错误
	public static final int  ERR_SQL_QUERY_TIMERESTRCITION            =        -31007 ; //查找定价计划和基站关系时发生错误
	public static final int  ERR_SQL_QUERY_OPERA_CTRL                 =        -31008 ; //查找定价计划和基站关系时发生错误
	public static final int  ERR_SQL_QUERY_RESOPERACOND               =        -31009 ;
	public static final int  ERR_NORATING_REQ_MSG                     =        -32000 ;// 不批价消息格式错误 直接扣费和查询信息都不存在 ;
	
	public static final int  ERR_MSG_NO_DOMAIN                               = -30001;  //  业务请求消息中没有message domain域
	public static final int  ERR_MSG_NO_TIMESTAMP                            = -30002;  //  业务请求消息中没有时间戳
	
	public static final int  ERR_MSG_NO_BILLING_FLAG                         = -30004;  //  业务请求消息中没有计费类型
	
	
	public static final int  ERR_MSG_NO_FREE_FLAG                            = -30010;  // 业务请求消息中没有免费标识
	public static final int  ERR_MSG_NO_BEGINTIME                            = -30012;  // 业务请求消息中没有计费开始时间
	public static final int  ERR_MSG_NO_REQ_DURATION                         = -30014;  // 业务请求消息中没有请求会话时长
	public static final int  ERR_MSG_NO_CHARGED_NBR                          = -30015;  // 业务请求消息中没有计费号码
	public static final int  ERR_MSG_NO_CHARGED_H_AC                         = -30016;  // 业务请求消息中没有计费号码归属费率区
	public static final int  ERR_MSG_NO_REQ_TIMES                            = -30018;  // 业务请求消息中没有请求使用次数
	public static final int  ERR_MSG_NO_REQ_UPVOLUME                         = -30022;  // 业务请求消息中没有请求上行流量
	public static final int  ERR_MSG_NO_REQ_DOWNVOLUME                       = -30026;  // 业务请求消息中没有请求下行流量
	public static final int  ERR_MSG_NO_REQ_TOTALVOLUME                      = -30030;  // 业务请求消息中没有请求总流量
	
	
	public static final int  ERR_MSG_NO_CLD_VST_AC                           = -30076;  // 业务请求消息中没有被叫拜访区号
	public static final int  ERR_MSG_NO_BILLING_OBJ                          = -30078;  // 业务请求消息中没有计费对象
	public static final int  ERR_MSG_NO_BILLING_OBJ_AC                       = -30086;  // 业务请求消息中没有计费对象区号
	public static final int  ERR_MSG_NO_LASTTIME                             = -30094;  // 业务请求消息中没有会话上次扣费开始时间
	public static final int  ERR_MSG_NO_TOP_VALUE                            = -30095;  // 业务请求带有封顶标识，但是封顶值不存在或有误
	public static final int  ERR_MSG_NOT_VAP                                 = -30108;  // 没有找到主叫拜访省
	public static final int  ERR_MSG_NOT_VDP                                 = -30109;  // 没有找到被叫拜访省
	public static final int  ERR_MSG_NOT_CAP                                 = -30110;  // 没有找到主叫归属省号
	public static final int  ERR_MSG_NOT_CDP                                 = -30111;  // 没有找到被叫归属省号
	public static final int  ERR_MSG_NOT_CAT                                 = -30112;  // 没有找到主叫区号类型
	public static final int  ERR_MSG_NOT_CDT                                 = -30113;  // 没有找到被叫区号类型
	
	
	public static final int  ERR_MSG_NO_CLD_HM_AC                            = -30116;  // 没有被叫归属区号
	public static final int  ERR_MSG_NO_VALIDITY_TIME                        = -30117;  // 没有授权有效时间
	
	
	public static final int  ERR_MSG_NO_RATABLE_FLAG                         = -30120;  // 没有使用量累计标识
	
	public static final int  ERR_MSG_NO_RESEND_FLAG                          = -30122;  // 没有重发标记
	public static final int  ERR_MSG_NO_SERVER_ROLE                          = -30123;  //没有PoC Server Role，R701
	public static final int  ERR_MSG_NO_RATING_GROUP                         = -30124;  //没有Rating Group = CCG业务;  
	public static final int  ERR_MSG_NO_PRODUCT_ID                           = -30125;  //没有ProductID = POC,IM业务;  
	public static final int  ERR_MSG_NO_SM_ID                                = -30126;   //没有SM_id
	
	public static final int  ERR_MSG_NO_SERVICE_ENABLE_TYPE                  = -30128;   //没有R402
	
	
	
	
	
	
	public static final int  ERR_NOT_FOUND_SECTION_RULE     =-30334; //没有找到段落规则
	public static final int  ERR_NOT_FOUND_OFR_CELL         =-30335; //没有找到销售品小区信息
	public static final int  ERR_INVALID_TARIFF_DATA        =-30336; //tariff配置错误
	
	public static final int  ERR_IN_BALANCE                 =-30338; //扣费错误
	
	
	public static final int ERR_NOT_FOUND_PRICING_SECTION       =-30301  ;// 没有找到定价段落
	public static final int ERR_TARIFF_CAN_NOT_DEV              =-30302  ;// 资费打折基数不能为零tb_bil_tariff.disct_value_base
	public static final int ERR_NOT_FIND_SERV_INFO              =-30303  ;// 查询用户信息错误
	public static final int ERR_NOT_FIND_PRICING_PLAN_ID        =-30304  ;// 没有找到定价计划
	
	
	public static final int ERR_NOT_FIND_ATTR_REF               =-30307  ;// 没有找到用户属性定义
	public static final int ERR_NOT_FOUND_PRICING_SECTION_DISCT =-30308  ;// 没有找到定价段落
	public static final int ERR_NOT_FOUND_MEASURE               =-30309  ;// 没有找到计量信息
	public static final int ERR_NOT_FOUND_RATABLE_CODE          =-30310  ;// 没有找到累积量代码
	public static final int ERR_NOT_FOUND_RATABLE_NOMATCH_DATA  =-30311  ;// 事件返还不在同一个帐期
	public static final int ERR_NOT_FIND_RECORD_SOURCE_TYPE     =-30312  ;// 没有找到source_type
	public static final int ERR_NOT_FOUND_TARIFF                =-30314  ;// 没有找到计费标准
	public static final int ERR_NOT_FIND_STRATERY               =-30316  ;// 没有找到定价策略
	public static final int ERR_NOT_FIND_DISCT_STRATERY         =-30317  ;// 没有找到优惠定价策略
	public static final int ERR_NOT_FIND_HOLIDAY                =-30318  ;// 没有找到节假日信息
	public static final int ERR_NOT_FIND_RECORD_VALUE_TYPE      =-30319  ;// 没有找到value_type
	
	
	
	public static final int	INVALID_CONDITION_NULL  = -30941;// 比较条件为NULL
	
	
	public static final int  ERR_NOT_DEFINE_REFOBJECT_TARIFF = -30227 ;// 没有定义计量单位
	
	public static final int  ERR_TARIFF_NOT_CLOSED_TIME      = -30229 ;// 按时间，分段上限、下限没有闭合
	public static final int  ERR_ACCOUNT_CANCELLATION        = -30230 ; //预付费用户立即销户业务，返回离线错误码
	public static final int  NO_ENOUGH_BALANCE_ZERO          = -30232 ; // 余额不足


	public static final int ERR_ADD_RES=-4000;//资源到账失败
	
	
	
	
	 
	
	                     
}
