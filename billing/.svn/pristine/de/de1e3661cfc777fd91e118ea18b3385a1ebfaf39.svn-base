package com.tydic.beijing.billing.rating.domain;

public final class RatingMacro {


public static final int TARIFF_PRECISION  = 1000000; //资费最小单元进位
public static final int BULKSIZE =100;

//时长和流量的单元值
public static final int VOLUME_UNIT_VALUE    =  1024;
public static final int VOLUME_UNIT_VALUE_CADE  =    10240;
public static final int TIME_UNIT_VALUE     =     60;

//资源直扣时，各种业务圆整单位
public static final int UNIT_VALUE_MONEY = 1;
public static final int UNIT_VALUE_VOICE = 60; // 60s
public static final int UNIT_VALUE_GGSN = 1024;
public static final int UNIT_VALUE_SMS = 1;
public static final int UNIT_VALUE_MMS = 1;
public static final int UNIT_VALUE_VAC = 1;

//std::string m_strMeasureDomain; //计费资源	Char（2）	01：时长 02：流量 04：上行流量 05：下行流量 06：按次
public static final int MEASURE_DURATION = 1;
public static final int MEASURE_TOTALVOL = 3;
public static final int MEASURE_UPVOL = 4;
public static final int MEASURE_DOWNVOL = 5;
public static final int MEASURE_TIMES = 6;

///消息字段的宏/常量定义
public static final int LONG_TYPE_SIZE = 8; // 长整形所占字节数
public static final int INT_TYPE_SIZE = 4;


//版本信息
public static final int MSG_VER_01         = 1;  //合并前版本
public static final int MSG_VER_02         = 2;  //合并后版本

public static final long EVENT_VOICE_INTERNAL = 10039; //JD 国内拨打国内语音事件码 用于判断3秒不计费

public static final int NORATING_3SECONDS_ACCTITEM = 138; //3秒不计费的账目项

//MsgType(消息类型),控制消息
public static final String DOMAIN_ECR     =    "01"  ;  //建立连接请求
public static final String DOMAIN_ECA     =    "02"  ;  //建立连接应答
public static final String DOMAIN_KAR     =    "30"  ;  //心跳请求
public static final String DOMAIN_KAA     =    "31"  ;  //心跳应答
public static final String DOMAIN_ACR     =    "40"  ;  //终止连接请求
public static final String DOMAIN_ACA     =    "41"  ;  //终止连接应答

//MsgType(消息类型),业务消息
public static final String DOMAIN_IN_BRR       = "60"  ;  // 语音业务定价请求
public static final String DOMAIN_IN_BRA       = "61"  ;  // 语音业务定价应答
public static final String DOMAIN_SMS_BRR      = "70"  ;  // 短信业务定价请求
public static final String DOMAIN_SMS_BRA      = "71"  ;  // 短信业务定价应答
public static final String DOMAIN_CCG_BRR      = "80"  ;  // CCG业务定价请求
public static final String DOMAIN_CCG_BRA      = "81"  ;  // CCG业务定价应答
public static final String DOMAIN_ISMP_BRR     = "90"  ;  // IMSP业务定价请求
public static final String DOMAIN_ISMP_BRA     = "91"  ;  // IMSP业务定价应
public static final String DOMAIN_POC_BRR      = "100" ;  //POC定价请求
public static final String DOMAIN_POC_BRA      = "101" ;  //POC定价应答
public static final String DOMAIN_IM_BRR       = "110" ;  //IM定价请求
public static final String DOMAIN_IM_BRA       = "111" ;  //IM定价应答
public static final String DOMAIN_DSL_BRR      =  "120";   // DSL业务定价请求
public static final String DOMAIN_DSL_BRA      =  "121";   // DSL业务定价应答
public static final String DOMAIN_NORATING_BRR =  "130";   // 无需批价请求
public static final String DOMAIN_NORATING_BRA =  "131";   // 无需批价应答

public static final String DOMAIN_BBR     =    "20"  ;  // 余额操作请求
public static final String DOMAIN_BBA     =    "21"  ;  // 余额操作应答


//MsgType(消息类型),控制消息,整型表示
public static final int CODE_ECR    =       1    ;   //连接建立请求
public static final int CODE_ECA    =       2    ;   //连接建立应答
public static final int CODE_KAR    =       30   ;   //心跳请求      
public static final int CODE_KAA    =       31   ;   //心跳应答
public static final int CODE_ACR    =       40   ;   //终止连接请求  
public static final int CODE_ACA    =       41   ;   //终止连接应答 

//MsgType(消息类型),业务消息,整型表示
public static final int CODE_IN_BRR        = 60    ;  //批价请求  EVENT_BACK     CODE_SMS_BRR CODE_ISMP_BRR
public static final int CODE_IN_BRA        = 61    ;  //批价应答      
public static final int CODE_SMS_BRR       = 70    ;  //批价请求      
public static final int CODE_SMS_BRA       = 71    ;  //批价应答      
public static final int CODE_CCG_BRR       = 80    ;  //批价请求      
public static final int CODE_CCG_BRA       = 81    ;  //批价应答      
public static final int CODE_ISMP_BRR      = 90    ;  //批价请求      
public static final int CODE_ISMP_BRA      = 91    ;  //批价应答 
public static final int CODE_POC_BRR       = 100   ;  //POC批价请求
public static final int CODE_POC_BRA       = 101   ;  //POC批价应答
public static final int CODE_IM_BRR        = 110   ;  //IM批价请求
public static final int CODE_IM_BRA        = 111   ;  //IM批价应答 
public static final int CODE_DSL_BRR       = 120   ;  // DSL业务定价请求    
public static final int CODE_DSL_BRA       = 121   ;  // DSL业务定价应答
public static final int CODE_NORATING_BRR  = 130   ;  // 无需批价请求      
public static final int CODE_NORATING_BRA  = 131   ;  // 无需批价应答

public static final int CODE_MMS_BRR  = 180   ;  // 彩信请求
public static final int CODE_MMS_BRA  = 181   ;  // 彩信应答

public static final int CODE_BBR       =    20   ;   //余额操作请求  
public static final int CODE_BBA       =    21   ;   //余额操作应答  


//计费命令
public static final int NO_FEE_CMD     = 0X00000000;  //没有费用命令
public static final int REQ_FEE_CMD    = 0X00000001;  //请求 B01组
public static final int FREE_FEE_CMD   = 0X00000002;  //释放 B02组
public static final int REAL_FEE_CMD   = 0X00000004;  //实扣 B03组
public static final int BACK_FEE_CMD   = 0X00000008;  //返回 B04组
public static final int VOUCHER_CMD    = 0X00000010;  //充值 B05组
public static final int BAL_QUERY_CMD  = 0X00000020;  //查询 B21组
public static final int USED_FEE_CMD   = 0X00000040;  //使用 B30组
public static final int ALL_RB_CMD     = 0XFFFFFFFF;  //所有命令

public static final int POST_PAY_CMD =  88;//后付费 feetype  zhanghb add

//计费命令(整型)
public static final int NO_OPER_FEE     = -1; //增加一个和NO_FEE_CMD对应的命令
public static final int ONLY_REQ_FEE    = 0;  //只有预占
public static final int ONLY_REAL_FEE   = 1;  //只有实扣
public static final int REAL_REQ_FEE    = 2;  //实扣+预占
public static final int EVENT_BACK_FEE  = 3;  //事件返还

//申请资源类型
public static final int REQ_TYPE_RES      = 0X00000001;    //资源，如时长、流量、次数等
public static final int REQ_TYPE_MONEY    = 0X00000002;    //金钱

//操作类型
public static final int OPER_FREE     = 0;    //释放
public static final int OPER_REQ      = 1;    //申请
public static final int OPER_REAL     = 2;    //实扣

//会话类型,OCS和CM的接口使用
public static final int SESSION_BEGIN   = 1;
public static final int SESSION_UPDATE  = 2;
public static final int SESSION_END     = 3;
public static final int EVENT_FEE       = 4;
public static final int EVENT_BACK      = 5;

//信用单位
public static final int CREDITUNIT_TIMELEN    = 1;
public static final int CREDITUNIT_MONEY      = 2;
public static final int CREDITUNIT_TOTALVAL   = 3;
public static final int CREDITUNIT_UPVAL      = 4;
public static final int CREDITUNIT_DOWNVAL    = 5;
public static final int CREDITUNIT_COUNT      = 6;

//消息头必选信息
public static final String PARA_REQ_ID            = "001"    ;
public static final String PARA_SESSION_ID        = "000"    ;
public static final String PARA_ERROR_CODE        = "003"    ;
public static final String PARA_MSGTYPE           = "100"    ;
public static final String PARA_EVENTTIMESTAMP    = "101"    ;

//签权信息
public static final String PARA_ORIGIN_HOST     =   "210"      ;
public static final String PARA_USERNAME        =   "211"      ;
public static final String PARA_PASSWORD        =   "212"      ;

//预占信用
public static final String PARA_R_REQ_FEE       =   "R69"     ;
public static final String PARA_R_REQ_FEE_A     =   "R6901"   ;
public static final String PARA_R_REQ_FEE_B     =   "R6902"   ;
public static final String PARA_R_REQ_FEE_C     =   "R6903"   ;
public static final String PARA_R_REQ_FEE_D     =   "R6904"   ;  //账本ID
public static final String PARA_R_REQ_FEE_E     =   "R6905"   ;  //latn_id,组网标识

//实扣信用
public static final String PARA_R_REL_FEE     =     "B54"   ;
public static final String PARA_R_REL_FEE_A   =     "B5401" ;
public static final String PARA_R_REL_FEE_B   =     "B5402" ;
public static final String PARA_R_REL_FEE_C   =     "B5403" ;

public static final String PARA_B_ACCT_ID     = "B22"            ;
public static final String PARA_B_PRD_INST_ID =     "B23"        ;
public static final String PARA_B_LATN_ID     = "B24"            ;

public static final String  PARA_B_RATING_RES_MES_INFO   ="B09"        ; //计费资源信息
public static final String  PARA_B_RES_TYPE     		 ="B091"       ; //资源类型
public static final String  PARA_B_RATING_TYPE           ="B092"       ; //计费数量

//计费信息
public static final String PARA_REP_FLAG        =   "002"    ;   //重发标记
public static final String PARA_R_REQ_TYPE      =   "R01"    ;   //计费类型
public static final String PARA_R_REL_STIME     =   "R70"    ;   //会话上次实际扣费开始时间
public static final String PARA_R_REQ_STIME     =   "R05"    ;   //本次计费请求开始时间
public static final String PARA_R_RATABLE_FLAG  =   "R39"    ;   //是否进行使用量累计标识

//Balance和Rating合并后的可变计费信息
public static final String  PARA_R_VAR_FEE          = "R60"    ;   //可变计费信息
public static final String  PARA_R_SEND_FLAG        = "R601"   ;   //重发标记
public static final String  PARA_R_REQ_TYPE_02      = "R602"   ;   //计费类型
public static final String  PARA_R_REL_STIME_02     = "R603"   ;   //会话上次实际扣费开始时间
public static final String  PARA_R_REQ_STIME_02     = "R604"   ;   //本次计费请求开始时间
public static final String  PARA_R_RATABLE_FLAG_02  = "R605"   ;   //是否进行使用量累计标识
public static final String  PARA_R_ACTIVE_USER      = "R606"   ;   //激活用户
public static final String  PARA_R_VPN_TYPE         = "R607"   ;   //vpn类型

public static final String PARA_R_RELATION_NBR             ="R608"   ;   //是否是亲情号码
public static final String PARA_R_CALLING_GROUP_ID         = "R612"  ;  //主叫集团ID
public static final String PARA_R_CALLED_GROUP_ID          = "R613"  ;  //被叫集团ID
public static final String PARA_B_BRR_ACTIVE               = "B6061" ;  //离线直接扣费激活
public static final String PARA_B_BRR_MSGTYPE              = "B6060" ;
public static final String PARA_B_BRR_RATABLE_FLAG_OFFLINE ="B6062"  ;
public static final String PAPA_B_BRR_DEDUCTTYPE           ="B6062"  ; //0-直接扣费 1-批价成功后的扣费(默认) 2-批价失败后的扣费

//基本信息
public static final String PARA_R_CALLING_NBR      ="R02"     ;  //主叫号码
public static final String PARA_R_CALLED_NBR       ="R03"     ;  //被叫号码
public static final String PARA_R_CALL_TYPE        ="R23"     ;  //呼叫类型
public static final String PARA_R_CLG_VST_AC       ="R102"    ;  //主叫拜访区号
public static final String PARA_R_CLD_VST_AC       ="R108"    ;  //被叫拜访区号
public static final String PARA_R_CLG_HOM_AC       ="R100"    ;  //主叫归属区号
public static final String PARA_R_CLD_HOM_AC       ="R104"    ;  //被叫归属区号
public static final String PARA_R_CLG_MSCCODE      ="R60"     ;  //主叫交换机
public static final String PARA_R_CLD_MSCCODE      ="R61"     ;  //被叫交换机
public static final String PARA_R_CLG_LAC          ="R28"     ;  //主叫小区
public static final String PARA_R_CLD_LAC          ="R29"     ;  //被叫小区
public static final String PARA_R_CLG_CELL         ="R26"     ;  //主叫基站
public static final String PARA_R_CLD_CELL         ="R27"     ;  //被叫基站
public static final String PARA_R_CLG_PARTNER      ="R103"    ;  //主叫运营商
public static final String PARA_R_CLD_PARTNER      ="R106"    ;  //被叫运营商
public static final String PARA_R_PAY_FLAG         ="R40"     ;  //用户付费属性标识
public static final String PARA_R_START_TIME       ="R71"     ;  //摘机时间
public static final String PARA_R_CALLED_ACCESSNBR ="R75"     ;  //被叫接入码
public static final String PARA_R_CALLED_SHORTNBR  = "R1016"  ;   //被叫短号
public static final String PARA_R_CONNECT_NBR      ="R25"     ;  //连接号码
public static final String PARA_R_VALIDITY_TIME    ="R41"     ;  //授权有效时间
public static final String PARA_R_RATING_GROUP     ="R43"     ;  //Rating-Group
public static final String PARA_R_CHARGE_NBR       ="R46"     ;  //计费号码
public static final String PARA_R_SPAN_POINT       ="R53"     ;  //费率切换点
public static final String PARA_R_ATH_FLAG         ="R04"     ;  //最后一次授权标识
public static final String PARA_R_DISTANT_TYPE     ="R112"    ;  //长途类型
public static final String PARA_R_ROME_TYPE        ="R114"    ;  //漫游类型
public static final String PARA_R_SERVICE_CODE     ="R65"     ;  //服务代码
public static final String PARA_R_PRODUCT_ID_01    ="R72"     ;  //产品id
public static final String PARA_R_PRODUCT_ID_02    ="R401"    ;  //产品id
public static final String PARA_R_SPC_PRD_ID       ="R503"    ;  //SPC-PRD-ID，产品构成ID
public static final String PARA_R_ATTR_CELL        ="R615"    ;  //请求判断是否是小区优惠
public static final String PARA_R_IMSI             ="R67";   //imsi

public static final String PARA_R_CHARGED_NBR   =   "R01"    ;   //计费方号码
public static final String PARA_R_CHARGED_H_AC  =   "R5012"  ;   //计费方归属费率区
public static final String PARA_R_SM_ID         =   "R202"   ;   //短信消息ID
public static final String PARA_R_ISMP_ID       =   "R409"   ;   //ISMP短信ID

// ADD BY ZHANGPENG AT 2009.10.14
public static final String PARA_R_SGSN_ADDR    ="R30"   ;       //SGSN地址
public static final String PARA_R_GGSN_ADDR    ="R31"   ;     //GGSN地址
public static final String PARA_R_SGSN_MCC_MNC ="R300"  ;        //SGSN_MCC_MNC标识
public static final String PARA_R_APN_NET_FLAG ="R34"   ;        //APN网络标识

//POC、IM业务新增基本信息
public static final String PARA_R_POC_SERVER_ROLE  ="R701"   ;   //POC服务器角色
public static final String PARA_R_POC_SESSION_TYPE ="R702"   ;   //POC会话类型
public static final String PARA_R_POC_PARTICI_NUM  ="R703"   ;   //POC会话参与者人数
public static final String PARA_R_POC_FEATURE_TYPE ="R704"   ;   //POC业务特征类型
public static final String PARA_R_IM_SERVER_ROLE   ="R801"   ;   //IM服务角色
public static final String PARA_R_IM_SESSION_TYPE  ="R802"   ;   //IM会话类型
public static final String PARA_R_IM_PARTICI_NUM   ="R803"   ;   //IM会话参与者人数
public static final String PARA_R_IM_FEATURE_TYPE  ="R804"   ;   //IM业务特征类型


//Balance和Rating合并后有变动的基本信息
public static final String PARA_R_CALL_TYPE_02     ="R103"     ; //呼叫类型 语音
public static final String PARA_R_ROME_TYPE_02     =   "R5011" ;     //漫游类型
public static final String PARA_R_DISTANT_TYPE_02  =   "R5010" ;     //长途类型
public static final String PARA_R_VALIDITY_TIME_02 ="R309"     ; //授权有效时间
public static final String PARA_R_CLG_VST_AC_02    ="R505"     ; //主叫拜访区号
public static final String PARA_R_CLD_PARTNER_02   ="R509"     ; //被叫运营商
public static final String PARA_R_CLG_HOM_AC_02    ="R504"     ; //主叫归属区号
public static final String PARA_R_CLD_HOM_AC_02    ="R507"     ; //被叫归属区号
public static final String PARA_R_CLG_PARTNER_02   ="R506"     ; //主叫运营商
public static final String PARA_R_CLD_VST_AC_02    ="R508"     ; //被叫拜访区号
public static final String PARA_R_CONNECT_NBR_02   ="R105"     ; //连接号码
public static final String PARA_R_PAY_FLAG_02      ="R85"      ; //用户付费属性标识
public static final String PARA_R_SPAN_POINT_02    ="R610"     ; //费率切换点
public static final String PARA_R_ATH_FLAG_02      ="R611"     ; //最后一次授权标识
public static final String PARA_R_CALLING_CELL_02  ="R106"     ; //主叫号码基站
public static final String PARA_R_CALLING_MSC_02   ="R1012"    ; //主叫号码交换机
public static final String PARA_R_CALLED_CELL_02   ="R107"     ; //被叫号码基站

public static final String PARA_R_CALL_TYPE_70     ="R203"     ; //呼叫类型 短信

//命令消息信息
public static final String PARA_R_REQ_DURATION    = "R06"  ;     //请求的时长
public static final String PARA_R_ATH_DURATION    = "R07"  ;     //授权的时长
public static final String PARA_R_REQ_TIMES       = "R08"  ;     //授权使用次数
public static final String PARA_R_ATH_TIMES       = "R09"  ;     //请求使用次数
public static final String PARA_R_REQ_UPVAL       = "R10"  ;     //请求的上行流量
public static final String PARA_R_ATH_UPVAL       = "R11"  ;     //授权的上行流量
public static final String PARA_R_REQ_DOWNVAL     = "R12"  ;     //请求的下行流量
public static final String PARA_R_ATH_DOWNVAL     = "R13"  ;     //授权的下行流量
public static final String PARA_R_REQ_TOTALVAL    = "R14"  ;     //请求的总流量
public static final String PARA_R_ATH_TOTALVAL    = "R15"  ;     //授权的总流量
public static final String PARA_R_REL_DURATION    = "R16"  ;     //实际使用的时长
public static final String PARA_R_REL_TIMES       = "R17"  ;     //实际使用次数
public static final String PARA_R_REL_UPVAL       = "R18"  ;     //实际使用上行流量
public static final String PARA_R_REL_DOWNVAL     = "R19"  ;     //实际使用下行流量
public static final String PARA_R_REL_TOTALVAL    = "R20"  ;     //实际使用总流量

//rating-group组
public static final String PARA_R_RATING_GROUP_INFO  =  "R50"    ;
public static final String PARA_R_RATING_GROUP_ID    ="R501"     ;
public static final String PARA_R_RATING_DURTION     = "R502"    ;
public static final String PARA_R_RATING_UPLINK      =   "R503"  ;
public static final String PARA_R_RATING_DOWNLINK    ="R504"     ;

//余额消息信息
public static final String PARA_B_RESERVE        =  "B01"       ;        //余额预占      
public static final String PARA_B_RESERVE_A      =  "B0101"     ;        //  余额类型    
public static final String PARA_B_RESERVE_B      =  "B0102"     ;        //  预占单位    
public static final String PARA_B_RESERVE_C      =  "B0103"     ;        //  预占数量    
public static final String PARA_B_FREE           =  "B02"       ;        //余额释放      
public static final String PARA_B_FREE_A         =  "B0201"     ;        //  余额类型    
public static final String PARA_B_FREE_B         =  "B0202"     ;        //  释放单位    
public static final String PARA_B_FREE_C         =  "B0203"     ;        //  释放数量    
public static final String PARA_B_DEDUCT         =  "B03"       ;        //余额扣除      
public static final String PARA_B_DEDUCT_A       =  "B0301"     ;        //  余额类型    
public static final String PARA_B_DEDUCT_B       =  "B0302"     ;        //  扣除单位    
public static final String PARA_B_DEDUCT_C       =  "B0303"     ;        //  扣除数量    
                    
public static final String PARA_B_BACK           =  "B04"       ;        //余额返还      
public static final String PARA_B_BACK_A         =  "B0401"     ;        //  余额类型    
public static final String PARA_B_BACK_B         =  "B0402"     ;        //  返还单位    
public static final String PARA_B_BACK_C         =  "B0403"     ;        //  返还数量    
public static final String PARA_B_BACK_D         =  "B0404"     ;        //  返还账本标识 
public static final String PARA_B_BACK_E         =  "B0405"     ;        //  返还本地网标识 
                    
public static final String PARA_B_VOUCHER        =  "B05"       ;        //余额充值      
public static final String PARA_B_VOUCHER_A      =  "B0501"     ;        //  余额类型    
public static final String PARA_B_VOUCHER_B      =  "B0502"     ;        //  充值单位    
public static final String PARA_B_VOUCHER_C      =  "B0503"     ;        //  充值数量    
public static final String PARA_B_RESOURCE_MARK  =  "B06"       ;        //余额来源类型  
public static final String PARA_B_QUERY          =  "B21"       ;        //余额实际数量  
public static final String PARA_B_QUERY_A        =  "B211"      ;        //  余额类型    
public static final String PARA_B_QUERY_B        =  "B212"      ;        //  余额单位    
public static final String PARA_B_QUERY_C        =  "B213"      ;        //  余额数量    
public static final String PARA_B_CHARGE_NBR     =  "B08"       ;        //计费号码      
public static final String PARA_B_CHG_HOM_AC     =  "B09"       ;        //计费区号

// Balance和Rating合并后的余额消息信息
public static final String PARA_B_RESERVE_RATING_GROUP   ="B010"       ;  //请求Rating-Group
public static final String PARA_B_RESERVE_PRODUCT_ID     ="B012"       ;  //ProductID,IM&POC业务专用
public static final String PARA_B_RESERVE_ACCOUNT_TYPE   ="B015"       ;  //申请账目类型
public static final String PARA_B_RESERVE_CREDIT_UNIT    ="B016"       ;  //请求信用单位
public static final String PARA_B_RESERVE_CREDIT_COUNT   ="B017"       ;  //请求信用单位
//                  String                               =             ;
public static final String PARA_B_FREE_RATING_GROUP      ="B020"       ;  //释放Rating-Group
public static final String PARA_B_FREE_PRODUCT_ID        ="B022"       ;  //ProductID,IM&POC业务专用
public static final String PARA_B_FREE_ACCOUNT_TYPE      ="B025"       ;  //释放账目类型
public static final String PARA_B_FREE_CREDIT_UNIT       ="B026"       ;   //释放信用单位
public static final String PARA_B_FREE_CREDIT_COUNT      ="B027"       ;  //释放信用单位
//                  String                               =             ;
public static final String PARA_B_REAL_RATING_GROUP      ="B030"       ;  //实扣Rating-Group
public static final String PARA_B_REAL_SPAN_FLAG         ="B031"       ;  //费率切换标示
public static final String PARA_B_REAL_PRODUCT_ID        ="B032"       ;  //ProductID,IM&POC业务专用
public static final String PARA_B_REAL_ACCOUNT_TYPE      ="B035"       ;  //实扣账目类型
public static final String PARA_B_REAL_CREDIT_UNIT       ="B036"       ;  //实扣信用单位
public static final String PARA_B_REAL_CREDIT_COUNT      ="B037"       ;  //实扣信用单位
public static final String PARA_B_REAL_NEGATIVE_BAL      ="B038"       ;  //余额扣成负标识
public static final String PARA_B_REAL_CTRL_TYPE         ="B0391"      ;  //实扣附加控制类型
public static final String PARA_B_REAL_CTRL_PARA         ="B0392"      ;  //实扣附加控制参数
//                  String                               =             ;
public static final String PARA_B_USED                   ="B30"        ;  //更新使用量命令
public static final String PARA_B_USED_RATING_GROUP      ="B301"       ;  //使用Rating-Group
public static final String PARA_B_USED_PRODUCT_ID        ="B302"       ;  //使用ProductID,IM&POC业务专用
public static final String PARA_B_USED_CREDIT_UNIT       ="B306"       ;  //使用信用单位
public static final String PARA_B_USED_CREDIT_COUNT      ="B307"       ;  //使用信用数量
public static final String PARA_B_USED_CREDIT_LAST_COUNT =     "B308"  ;       //流量费率点之后的使用量
//                  String                               =             ;
public static final String PARA_B_BACK_RATING_GROUP      ="B040"       ;  //返还Rating-Group
public static final String PARA_B_BACK_PRODUCT_ID        ="B042"       ;  //ProductID,IM&POC业务专用
public static final String PARA_B_BACK_ACCOUNT_TYPE      ="B045"       ;  //返还账目类型
public static final String PARA_B_BACK_CREDIT_UNIT       ="B046"       ;  //返还信用单位
public static final String PARA_B_BACK_CREDIT_COUNT      ="B047"       ;  //返还信用单位
//                  String                               =             ;
public static final String PARA_B_VOUCHER_RATING_GROUP   ="B050"       ;  //充值Rating-Group
public static final String PARA_B_VOUCHER_PRODUCT_ID     ="B052"       ;  //ProductID,IM&POC业务专用
public static final String PARA_B_VOUCHER_ACCOUNT_TYPE   ="B055"       ;  //充值账目类型
public static final String PARA_B_VOUCHER_CREDIT_UNIT    ="B056"       ;  //充值信用单位
public static final String PARA_B_VOUCHER_CREDIT_COUNT   ="B057"       ;  //充值信用单位
//                  String                               =             ;
public static final String PARA_B_ATH_RATING             ="B10"        ;  //请求Rating-Group
public static final String PARA_B_ATH_RATING_GROUP       ="B100"       ;  //请求Rating-Group
public static final String PARA_B_ATH_PRODUCT_ID         ="B102"       ;  //ProductID,IM&POC业务专用
public static final String PARA_B_ATH_ACCOUNT_TYPE       ="B105"       ;  //申请账目类型
public static final String PARA_B_ATH_CREDIT_UNIT        ="B106"       ;  //请求信用单位
public static final String PARA_B_ATH_CREDIT_COUNT       ="B107"       ;  //请求信用单位
//                  String                               =             ;
public static final String PARA_B_RATE_QUERY             ="B20"        ;  //费率查询信息
public static final String PARA_B_RATE_QUERY_BEGINTIME   ="B201"       ; //计费开始时间
public static final String PARA_B_RATE_QUERY_TYPE        ="B202"       ; //费率单位
public static final String PARA_B_RATE_QUERY_UNIT        ="B203"       ; //费率步长
public static final String PARA_B_RATE_QUERY_VALUE       ="B204"       ; //费率价格
//                  String                               =             ;
public static final String PARA_B_RATABLE                ="B06"        ; //累积量信息
public static final String PARA_B_RATABLE_CODE           ="B061"       ; //累积量代码
public static final String PARA_B_RATABLE_TYPE           ="B062"       ; //累积量类型
public static final String PARA_B_RATABLE_VALUE          ="B063"       ; //本次累积量
public static final String PARA_B_RATABLE_TOTAL_VALUE    ="B064"       ; //总累积量
//                  String                               =             ;
public static final String PARA_B_ACCT_CHG_INFO          ="B08"        ; //账本改变信息
public static final String PARA_B_ACCT_CHG_BAL_ID        ="B081"       ; //账本ID
public static final String PARA_B_ACCT_CHG_UNIT_TYPE     ="B082"       ; //单位类型
public static final String PARA_B_ACCT_CHG_AMOUNT        ="B083"       ; //数量
public static final String PARA_B_ACCT_CHG_BALANCE       ="B084"       ; //改变后余额
public static final String PARA_B_ACCT_CHG_BALANCE_TYPE  ="B085"       ; //账本类型
public static final String PARA_B_ACCT_CHG_ACCTITEM      ="B086"       ; //帐目类型
public static final String PARA_B_ACCT_CHG_PAY_ID        ="B087"       ; //帐目类型

//                  String                               =             ;
public static final String PARA_B_ACCT_DEDUCT_INFO       =   "B54"     ;   // 实际扣除
public static final String PARA_B_ACCT_DEDUCT_BAL_ID     =   "B5401"   ;     //账目项
public static final String PARA_B_ACCT_DEDUCT_UNIT_TYPE  =   "B5402"   ;     //单位类型
public static final String PARA_B_ACCT_DEDUCT_AMOUNT     =   "B5403"   ;     //数量
//                  String                               =             ;
public static final String PARA_B_MT_INFO                =      "B15"  ;
public static final String PARA_B_MT_RATING_MODE         =     "B151"  ;  //批价模式,0 - 非MT计费1 - M计费2 - T 计费
public static final String PARA_B_MT_COUNT               =  "B152"    ;//M/T的数量
public static final String PARA_B_MT_EXCHANGE_MONEY      = "B153"    ;//M/T折合金钱后的数量
public static final String PARA_B_MT_EXT_MONEY           =  "B154"    ;//附加费用,如果没有附加的金钱,则填0
//                                           
// DSL业务新增字段  String                            
public static final String PARA_R_ACCESS_SERVER_IP       ="R901"       ; // 接入服务器ip
public static final String PARA_R_USER_IP                ="R902"       ; // 用户ip
public static final String PARA_R_DLS_VALIDITY_TIME      ="R903"       ; // 授权有效时间
public static final String PARA_R_PRD_SPEC_ID            ="R904"       ; // 产品规格ID
//
public static final String PARA_R_CHARGE_OBJ_NBR   =      "R04"    ;     // 计费对象号码
//public static final PARA_R_CHARGE_OBJ_AREACODE    "R04"       // 计费对象区号(注:计费对象号码对应的区号是R504)

//离线一次批价出现跨策略跨段落时候未扣的量
public static final String PARA_B_DCFMT_UNUSED           ="R57"      ;   //未扣组
public static final String PARA_B_DCFMT_UNUSED_UNIT      ="R571"     ;   //单位
public static final String PARA_B_DCFMT_UNUSED_MOUNT     ="R572"     ;   //未扣的量
public static final String PARA_B_DCFMT_UNUSED_USEDMOUNT =    "R573" ;       //已经扣的量

// 自定义字段
public static final String PARA_STARTTIME        =  "StartTime";
public static final String PARA_R_OTHER_NBR      =  "NMO"      ; //对端号码  
public static final String PARA_R_CHG_VST_AC     =  "VA3"      ; //计费方拜访区号
public static final String PARA_R_OTH_VST_AC     =  "VAO"      ; //对端拜访区号 
public static final String PARA_R_CHG_HOM_AC     =  "HA3"      ; //计费方归属区号  
public static final String PARA_R_OTH_HOM_AC     =  "HAO"      ; //对端归属区号
public static final String PARA_R_CHG_MSCCODE    =  "MS3"      ; //计费方交换机
public static final String PARA_R_OTH_MSCCODE    =  "MSO"      ; //对端交换机 
public static final String PARA_R_CHG_LAC        =  "LA3"      ; //计费方小区
public static final String PARA_R_OTH_LAC        =  "LAO"      ; //对端小区
public static final String PARA_R_CHG_PARTNER    =  "PT3"      ; //计费方运营商
public static final String PARA_R_OTH_PARTNER    =  "PTO"      ; //对端运营商
public static final String PARA_R_CHG_CELL       =  "CL3"      ; //计费方基站
public static final String PARA_R_OTH_CELL       =  "CLO"      ; //对端基站
public static final String PARA_R_ISMP_ACCTITEM  =  "IAI"      ; //ISMP已计费话单帐目类型
public static final String PARA_R_ISMP_UNITTYPE  =  "IUT"      ; //ISMP已计费话单单位类型
public static final String PARA_R_ISMP_CHGVALUE  =  "ICV"      ; //ISMP已计费话单消费额度

//AIX平台移植新增宏@{
public static final String PARA_R_ELIMINATE_SIGN =  "R200"   ; //离线剔除标志
public static final String PARA_R_MSG_ID         =  "R202"   ; //短消息ID
public static final String PARA_R_SERVICE_ID     =  "R77"    ; //即时消息改造增加业务标识service_id


//ISMP业务批价,需要带话单中的实扣金额
public static final String PARA_R_CHARGE_MONEY         ="R66"  ;
public static final String PARA_R_SERVICE_ENABLE_TTYPE = "R402";     // ISMP的SERVICE_ENABLE_TTYPE
public static final String PARA_R_ISMP_SERVICETYPE     ="R73"  ;     //ismp业务类型
public static final String PARA_R_ISMP_SP_CODE         ="R48"  ;         //版本1消息字段,对应版本2的R406
public static final String PARA_R_PRD_OFFER_ID         ="R80"  ;  //销售品实例ID
public static final String PARA_R_BILLING_TYPE         ="R410" ;  //计费类型

//累积量信息
public static final String PARA_R_RATABLE               = "L00"    ;     //累积量信息
public static final String PARA_R_RATABLE_OWNER_ID      = "L001"   ;     //累积量属主       m_Owner_ID
public static final String PARA_R_RATABLE_OWNER_TYPE    = "L002"   ;     //累积量属主类型 m_strOwner_Type
public static final String PARA_R_RATABLE_RESOURCE_ID   = "L003"   ;     //累积量标识       m_strRatableCode
public static final String PARA_R_RATABLE_CYCLE_ID      = "L004"   ;     //帐期                ACCT_date
public static final String PARA_R_RATABLE_BLANCE        = "L005"   ;     //累积量更新数量 m_nValue

public static final String PARA_R_RATABLE_OWNER_LATN_ID   = "L006"   ;   //累积量属主本地网标识 m_Owner_latn_id
public static final String PARA_R_RATABLE_TOTAL           = "L007"   ;   //累积量属主本地网标识 m_Owner_latn_id
public static final String PARA_R_RATABLE_TYPE            = "L008"   ;   //累积量属主本地网标识 m_Owner_latn_id

public static final int  EXTERN_MONEY_T  =    8 ;  //T金钱
public static final int  EXTERN_MONEY_M  =    7 ; //M金钱
public static final int  EXTERN_MONEY    =  2   ; //金钱(分)


public static final int BILLING_CYCLE_NATURE_DAY    =    1  ;     //自然天。如：2009-09-02 00:00:00~2009-09-02 23:59:59
public static final int BILLING_CYCLE_DYNAMIC_DAY_1 =    2  ;     //动态天类型1。如：2009-09-02 01:00:00~2009-09-03 00:59:59
public static final int BILLING_CYCLE_DYNAMIC_DAY_2 =    3  ;     //动态天类型2。如：2009-09-02 01:00:00~2009-09-03 23:59:59
public static final int BILLING_CYCLE_NATURE_MONTH  =    4  ;     //自然月。如：2009-09-01~2009-09-30
public static final int BILLING_CYCLE_DYNAMIC_MONTH =    5  ;     //动态月。如：2009-09-02~2009-10-01
public static final int BILLING_CYCLE_NATURE_WEEK   =    6  ;     //自然周。如：2009-08-30~2009-09-05.其中2009-08-30为星期天
public static final int BILLING_CYCLE_DYNAMIC_WEEK  =    7  ;     //动态周。如：2009-09-01~2009-09-07. 其中2009-09-01为星期2
		//tokennode 类型
        public static final int TOKENNODE_TYPE_OPERATOR = 1;
        public static final int TOKENNODE_TYPE_OPERAND= 2;
        public static final int TOKENNODE_TYPE_LEFTPAREN= 3;
        public static final int TOKENNODE_TYPE_RIGHTPAREN= 4;
        public static final int TOKENNODE_TYPE_UNKNOWN= 5;
                               
        //套餐公式内产品组之间的关系类型
        public static final String MUTEX_FAV_MODE = "0"; //排它
        public static final String BEST_FAV_MODE ="1";//取优
        public static final String ADD_FAV_MODE = "2";//叠加
//费率类型                               
public static final int NORMAL_TARIFF=0;
public static final int HOLIDAY_TARIFF=1;
public static final int NOTHOLIDAY_TARIFF=2;
public static final int INVALID_TARIFF=3;


public static final int GROUP_LENGTH=5;

//组内优惠模式

public static final int GROUP_FAVMODE_MUTEX=0 ;// 排 他
public static final int GROUP_FAVMODE_BEST=1 ;//取优
public static final int GROUP_FAVMODE_ADD=2;  //叠加
public static final int GROUP_FAVMODE_ACCTBEST=3;  //账目项取优


//累积量类型
public static final int RatableResourceType_Second = 1;
public static final int RatableResourceType_Minute = 2;
public static final int RatableResourceType_Time = 3;
public static final int RatableResourceType_TotalVolume = 4;
public static final int RatableResourceType_Money = 5;
public static final int RatableResourceType_UpVolume = 7;
public static final int RatableResourceType_DownVolume = 8;
public static final int RatableResourceType_M = 9;
public static final int RatableResourceType_T = 0;


//累积量账期类型
public static final int LifeType_Day=0;
public static final int LifeType_Month=1;
public static final int LifeType_HalfYear=2;


//段落条件
public static final String SectionCond_Compare_Type="10";

//段落规则
public static final int SectionRefFlag_Resource=0;//参考累积量
public static final int SectionRefFlag_Time=1;	//参考时间

public static final String Character_Space =" ";

public static final int UnitType_None=0;
public static final int UnitType_Duration =1;	//时间  秒
public static final int UnitType_Money=2;		//金钱  分
public static final int UnitType_TotalVolume=3;
public static final int UnitType_UpVolume =4;
public static final int UnitType_DownVolume=5;
public static final int UnitType_Times=6;
public static final int UnitType_M=7;
public static final int UnitType_T=8;



//public static final int MeasureDomain_None        = -1;
//public static final int MeasureDomain_Duration    = 1; //时长
//public static final int MeasureDomain_TotalVolume = 2;
//public static final int MeasureDomain_Times       = 3;
//public static final int MeasureDomain_UpVolume    = 4;
//public static final int MeasureDomain_DownVolume  = 5;
//public static final int MeasureDomain_Money       = 7;

//累积量参考类型
public static final String REFTYPE_SINGLE="R0C"; //单一
public static final String REFTYPE_AV="R0A";// 平均值 (不含当前累计账期)
public static final String REFTYPE_TOTAL="R0B";// 取和 (不含当前累计账期)


//段落批价域

public static final String SECTION_DOMAIN_NONE="-1";
public static final String SECTION_DOMAIN_DURATION="1";
public static final String SECTION_DOMAIN_TOTALVOLUME="2";
public static final String SECTION_DOMAIN_TIMES="3";
public static final String SECTION_DOMAIN_UPVOLUME="4";
public static final String SECTION_DOMAIN_DOWNVOLUME="5";
public static final String SECTION_DOMAIN_MONEY="7";

public static final char FORMULA_FAV_BEST=',';
public static final char FORMULA_FAV_ADD='&';
public static final char FORMULA_FAV_MUTEX='!';

public static final String USER_STATE_ACTIVE_NOT="F0A";	//未激活


public static final int TARIFF_TAILMODE_UP=0;//向上
public static final int TARIFF_TAILMODE_DOWN=1;
public static final int TARIFF_TAILMODE_ROUND=2;


public static final long  RATE_NO_STRATEGY=-9; //事件策略表无记录,套餐组返回

public static final int VOICE_MIN_LENGTH = 3;

public static final String PLMN_ID = "PLMN_ID";

public static final String LTMS = "13010910170"; //联通秘书号码 13010910198


}
