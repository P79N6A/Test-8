package com.tydic.beijing.billing.common;

import java.util.ArrayList;
import java.util.List;

public final class BasicType {
	public static final int UNIT_TYPE_MONEY = 0;
	public static final int UNIT_TYPE_VOICE = 1;// 语音
	public static final int UNIT_TYPE_GGSN = 2;// 流量
	public static final int UNIT_TYPE_SMS = 3;// 短信
	public static final int UNIT_TYPE_MMS = 4;// 数据
	public static final int UNIT_TYPE_VAC = 5;// 增值
	public static final int UNIT_TYPE_WLAN = 6;// wlan
	public static final int UNIT_TYPE_XING_COIN = 9;// 星币
	public static final int UNIT_TYPE_TICKET = 8;// 电影票

	public final static int WRITE_OFF_CALLER_SIMULATION = 1; // 模拟销账
	public final static int WRITE_OFF_CALLER_MONTHEND = 2; // 月结销账
	public final static int WRITE_OFF_CALLER_RATING = 3; // 计费
	public final static int WRITE_OFF_CALLER_RECHARGE = 4; // 缴费
	public final static int WRITE_OFF_CALLER_CUTEGG = 5; // 无账务模式
	public final static int WRITE_OFF_CALLER_MONEY_ONLY = 6; // 只使用金钱账本
	public final static int WRITE_OFF_CALLER_RESOURCE_ONLY = 7; // 只是用资源账本

	public final static String LIMIT_TYPE_NONE = "0"; // 无限额
	public final static String LIMIT_TYPE_FIXED_FIXED = "1"; // 固定限额
	public final static String LIMIT_TYPE_FIXED_RATIO = "2"; // 固定限额，按比例
	public final static String LIMIT_TYPE_BILL_RATIO = "3"; // 按账单额比例

	public final static String MEMCACHED_CLUSTER_TAG_0 = "0@@"; // memcached集群标记
	public final static String MEMCACHED_CLUSTER_TAG_1 = "1@@"; // memcached集群标记
	public final static String MEMCACHED_CLUSTER_TAG_2 = "2@@"; // memcached集群标记
	public final static String MEMCACHED_BILL_PREFIX = "bill_"; // memcached中存在支付关系和用户账单，用bill_标示用户账单

	public final static int INVOICE_TAG_N = 0;// 月结发票打印标志 0未打印
	public final static int INVOICE_TAG_Y = 1;// 月结发票打印标志 1已打印

	public final static long UNKNOWN_BALANCE_ID = -1628;
	public final static String UNKNOWN_PAY_ID = "-1633";
	public final static int UNKNOWN_BALANCE_TYPE_ID = -1634;

	public final static String ADJUST_MODE_PLUS = "1"; // 调账 固定值调增
	public final static String ADJUST_MODE_MINUS = "2"; // 调账 固定值调减

	public static final String SENDSMSMSG_SYSTEMID = "6";
	public static final String SENDSMSMSG_PROCESSTAG_SUCCESS = "1";
	public static final String SENDSMSMSG_PROCESSTAG_FAIL = "2";
	public static final String SENDSMSMSG_PROCESSTAG_0001 = "3"; // 短信网关操作失败
	public static final String SENDSMSMSG_PROCESSTAG_0002 = "4";// 充值平台操作超时
	public static final String SENDSMSMSG_PROCESSTAG_1001 = "5";// 找不到发送号码归属
	public static final String SENDSMSMSG_PROCESSTAG_1002 = "6";// 输入参数错误
	public static final String SENDSMSMSG_PROCESSTAG_SUCC = "0000";
	public static final String SENDSMSMSG_PROCESSTAG_ERR1 = "0001";
	public static final String SENDSMSMSG_PROCESSTAG_ERR2 = "0002";
	public static final String SENDSMSMSG_PROCESSTAG_ERR3 = "1001";
	public static final String SENDSMSMSG_PROCESSTAG_ERR4 = "1002";
	public static final String SENDSMSMSG_NOTEMPLATE_SERVICE = "1015";
	public static final String VALUEADDED_SCENARIOUS = "400";
	public static final long SUBCDR_NET_UNIT = 1024;

	public static final String FILEHEADER = "JIDOSP";
	public static final int FILE_SERIAL_LENGTH = 16;

	// for refresh configuration
	public final static String DS_CODE_BIL_PAY_BALANCE_CODE = "DSCodeBilPayBalanceCode";
	public final static String DS_CODE_BIL_PAY_ITEM_CODE = "DSCodeBilPayItemCode";
	public final static String DS_CODE_ACT_ACCT_ITEM = "DSCodeActAcctItem";
	public final static String DS_RULE_BIL_SPE_PAYMENT = "DSRuleBilSpePayment";
	public final static String DS_CODE_BIL_BALANCE_TYPE = "DSCodeBilBalanceType";
	public final static String DS_RULE_RECHARGE_TYPE_MAPPING = "DSRuleRechargeTypeMapping";
	public final static String DS_CODE_ACCT_MONTH = "DSCodeAcctMonth";
	public final static String DS_RULE_USER_EVENT_MAPPING = "DSRuleUserEventMapping";
	public final static String DS_CODE_TRADE_TYPE_CODE = "DSCodeTradeTypeCode";
	public final static String DS_TB_BIL_TRANS_RULE = "DSTbBilTransRule";
	public final static String DS_RULE_PARAMETERS = "DSRuleParameters";
	public static final String DS_RULE_OFR_SPLIT="DSRuleOfrSplit";          
	public static final String DS_RULE_OFR_TARIFF_RELATION="DSRuleOfrTariffRelation"; 
	public static final String DS_RULE_GROUP_STATE_RELATION="DSRuleGroupStateRelation";
	public static final String DS_RULE_TARIFF_CONF_INFO="DSRuleTariffConfInfo";    
	public static final String DS_RULE_PRODUCT_RESOURCE="DSRuleProductResource";   

	public final static String REFRESH_STATUS_OK = "OK";
	public final static String REFRESH_STATUS_FAIL = "FAIL";

	// for open
	public final static String USE_TAG_ACTIVE = "1";
	public final static String ACT_TAG_NORMAL = "0";
	public final static String ACT_TAG_OPEN = "2";

	// for end writeoff owe_tag
	public final static int END_OWE_TAG_Y = 0;// 欠费
	public final static int END_OWE_TAG_N = 1;// 未欠费 用户当月全部账单销清才修改为1
	// for end writeoff invoice_tag
	public final static int END_INVOICE_TAG_Y = 1;// 已打印
	public final static int END_INVOICE_TAG_N = 0;// 未打印
	// BilActAccesslog operate_type
	public final static String OPERATE_TYPE_WRITEOFF = "2";// 销账流水
	public final static String OPERATE_TYPE_ONLINE_PAYMENT = "25";// 线上消费支付
	public final static String OPERATE_TYPE_ONLINE_PAYMENT_CANCEL = "26";// 线上消费取消
	public final static String OPERATE_TYPE_OFFLINE_PAYMENT = "27";// 线下消费支付
	public final static String OPERATE_TYPE_OFFLINE_PAYMENT_CANCEL = "28";// 线下消费取消

	// BilActAccesslog access_tag
	public final static String ACCESS_TAG_DEPOSIT = "0";// 存款
	public final static String ACCESS_TAG_DRAW = "1";// 取款
	// code_acct_month act_tag
	public final static String ACT_TAG_MONTHEND = "2";// 出账
	// end_info_user END_STATUS
	public final static int END_STATUS_NO = 0;// 未出账
	public final static int END_STATUS_ING = 1;// 出账中
	public final static int END_STATUS_ED = 2;// 出账完成
	public final static int END_STATUS_ERR = -1;// 出账错误
	// q_before_adjust status
	public final static int BEFORE_OK = 1;
	public final static int BEFORE_FAIL = 2;

	// 星币

	public static final int XingCoinBalanceType = 8;

	// 星币生成途径
	public static final String XingCoinAction_Consume = "1"; // 消费
	public static final String XingCoinAction_Activity = "2"; // 活动
	public static final String XingCoinAction_Comment = "3";// 评价
	public static final String XingCoinAction_ShowOrder = "4";// 晒单
	public static final String XingCoinAction_Survey = "5";// 参与调查
	public static final String XingCoinAction_MobileOrder = "6"; // 移动下单
	public static final String XingCoinAction_Consume_Ticket = "7";// 消费影票

	// 星币生成方法
	public static final String XingCoinMethod_Percent = "1";// 按比例
	public static final String XingCoinMethod_Fixed = "2";// 固定值
	public static final String XingCoinMethod_unit = "3";// 按单位量

	public static final String XM_RESULT_SUCC = "0"; // 成功
	public static final String XM_RESULT_FAIL = "1"; // 失败

	// 资源转换和资源转增部分
	public static final int RESOURCE_PARA_DOMAIN_CODE = 8000; // 资源域
	public static final String RESOURCE_CHANGE = "23";// 资源转换编码
	public static final String RESOURCE_PRESENT = "24";// 资源转增编码
	public static final String CYCLE_RENT = "36";//月租服务编码
	public static final String CYCLE_RENT_SWITCH = "CYCLERENT";//月租服务编码
	public static final String CARD_SWITCH = "card";
	public static final String PROTOCOL_SWITCH = "protocol";
	public static final String OPER_CHANNEL = "10002";// 资源转换接触渠道
	public static final String RESOURC_CHANGE_ATTR_NAME = "ResourceExchange";
	public static final String RESOURCE_PRESENT_ATTR_NAME = "ResourceGiven";

	public static final String RESOURC_CHANGE_ATTR_VALUE = "resourceChange";// 资源转换--账本属性
	public static final String RESOURCE_PRESENT_ATTR_VALUE = "resourcePresent";//

	public static final String TRADE_TYPE_VIP_CARD = "1";// 交易类型，1-会员卡号，2-手机号码，3-会员唯一标识
															// 卡片
	public static final String TRADE_TYPE_DEVICE_NUMBER = "2";// 交易类型，1-会员卡号，2-手机号码，3-会员唯一标识
	public static final String TRADE_TYPE_VIP_MEMBER_ID = "3";// 交易类型，1-会员卡号，2-手机号码，3-会员唯一标识
	public static final int BALANCE_TYPE_XB = 8;//星币
	public static final int BALANCE_TYPE_YY = 10;//语音
	public static final int BALANCE_TYPE_SJ = 11;//数据
	public static final int BALANCE_TYPE_DX = 12;//短信
	public static final int BALANCE_TYPE_2DYP = 101;//2D影票
	public static final int BALANCE_TYPE_3DYP = 102;//3D影票
	public static final String CRM_SERVICE_CHANNEL_CODE = "2000";
	public static final String CRM_SERVICE_EXT_SYSTEM = "200";

	// 消费支付
	public static final String COMMODITY_TYPE_LIANG = "1";// 靓号
	public static final String COMMODITY_TYPE_DINNER = "2";// 套餐
	public static final String COMMODITY_TYPE_CONTRACT = "3";// 合约机
	public static final String COMMODITY_TYPE_GENERAL = "4";// 普通商品

	public static final String PAY_CODE_XINGCOIN = "8";// 星币
	public static final String PAY_CODE_TICKET = "7";// 资源 电影票
	public static final String PAY_CODE_MONEY = "0";// 现金账本
	public static final String PAY_CODE_ZHIFUBAO = "11"; // 支付宝
	public static final String PAY_CODE_YUEBAO = "12";// 余额宝
	public static final String PAY_CODE_BANK = "14"; // 网上银行

	public static final int CANCEL_TYPE_Y = 1;// 取消
	public static final int CANCEL_TYPE_N = 0;// 未取消

	public static final int BALANCE_TYPE_DEFAUL = 1;// 默认账本，商品支付时第三方支付充值账本

	public static final String COMMODITY_GROUP_DEFAUL = "0";
	public static final String BALANCE_TYPE_GROUP_DEFAUL = "1";

	public static final String OrderExtraPackage_failed = "0"; // 失败
	public static final String OrderExtraPackage_succ = "1"; // 成功

	// 接入系统标识 线下
	public static final String SYSTEM_ID_DINGXIN = "106"; // 鼎新：106
	public static final String SYSTEM_ID_HAIXING = "107"; // 海信：107

	// crm qry_type
	public static final String QRY_TYPE_PHONE_NUMBER = "1"; // 1 手机号码
	public static final String QRY_TYPE_MEMBERSHIP_CARD_NUMBER = "2"; // 2 会员卡号
	public static final String QRY_TYPE_ONLINE_LOGIN = "3"; // 3 线上登录账号
	public static final String QRY_TYPE_MEMBER_CODES = "4"; // 4 会员编码
	public static final String QRY_TYPE_ID_NUMBER = "5"; // 5 身份证号码

	// 支付可用资源类型
	public static final int PAYMENT_LIMIT_TYPE_OFF_DINGXIN[] = { UNIT_TYPE_MONEY };
	public static final int PAYMENT_LIMIT_TYPE_OFF_HAIXIN[] = { UNIT_TYPE_MONEY };
	public static final int PAYMENT_LIMIT_TYPE_ON[] = { UNIT_TYPE_MONEY,
			UNIT_TYPE_TICKET, UNIT_TYPE_XING_COIN };

	// 活力值行为类型
	public static final String ENERGY_TYPE_CONSUME = "2"; // 消费
	public static final String ENERGY_TYPE_LOGIN = "1";// 登录
	public static final String ENERGY_TYPE_SUGGESTION = "3"; // 建议
	public static final String ENERGY_TYPE_SURVEY = "4";// 问卷调查
	public static final String ENERGY_TYPE_CONSUMETIMES = "5";// 每月达到三次消费
	public static final String ENERGY_TYPE_SHARE = "6";// 分享转发
	public static final String ENERGY_TYPE_COMMENT = "7"; // 评价商品
	public static final String ENERGY_TYPE_SHOWORDER = "8";
	public static final String ENERGY_TYPE_COMMENT_DEL = "9";// 评价被删除
	public static final String ENERGY_TYPE_SHOWORDER_DEL = "10"; // 晒单被删除
	public static final String ENERGY_TYPE_GOODS_RETURN = "11";// 退货
	public static final String ENERGY_TYPE_LEVEL_DOWN = "12";// 会员降级
	public static final String ENERGY_TYPE_TURN_NEW = "13"; // 鼎新会员余额合并 传活力值
	public static final String ENERGY_TYPE_OPEN = "14";// 激活赠送 传活力值
	public static final String ENERGY_TYPE_QUIT = "15";// 销户返销 传活力值

	//消费取消
	//退订方式
	public static final String CANCEL_CLASS_PART = "101"; // 101：部分退
	public static final String CANCEL_CLASS_ALL = "102"; // 102：全部退
	//退订支付方式
	public static final String CANCEL_TYPE_CASH = "101"; //101：现金
	public static final String CANCEL_TYPE_OLD = "102"; //102：原方式
	
	//支付第三方冲正
	public static final String PAY_TYPE_THIRD_CANCEL = "6"; 
	
	
	
	//月租
	public static final String CONSULT_MEMBER_FAMILY = "FAMILY";//参考亲情成员数量
	public static final String CONSULT_MEMBER_VICECARD = "VICECARD";//参考副卡数量
	public static final String CONSULT_MEMBER_GROUP = "GROUP";//参考集团成员数量
	public static final int NOT_ARGEEMENT_TYPE = 0;//非合约
	public static final int ARGEEMENT_TYPE_NORMAL = 1;//合约套餐费
	public static final int ARGEEMENT_TYPE_ABBORMAL = 2;//合约停机保号费
	
	public static final String EVENT_TYPE_ID_CYCLE = "10131";//月租事件
	public static final String EVENT_TYPE_ID_MIN = "20131";//保底
	
	//星币计算 金钱方式
	public static final String XingCoinCalculator_montyFlag_Y = "1";//1 金钱消费
	public static final String XingCoinCalculator_montyFlag_N = "2";//2非金钱消费
	
	//短信发送号码定义
	public static final String MSISDN_SEND = "10023";
	public static final String USE_TAG = "1";
	public static final String ACT_TAG = "0";
	
	//京牛渠道
	public static final String STARTSTR = "110";
	
}
