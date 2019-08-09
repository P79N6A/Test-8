package com.tydic.beijing.billing.common;

/**
 * 
 * @author Tian
 *
 */
public final class ErrorCode {
	public static final int ERR_PARAM_CONTENT = -40001; // 参数内容错误，如null
	public static final int ERR_PARAM_NUM = -40002; // 参数个数错
	
	public static final int ERR_PAY_RELATION_ABNORMAL = -41001; // 付费关系异常（如关系未找到，多主账户，无主账户）
	public static final int ERR_ACCT_MONTH_NOT_FOUND = -41002; // acct_month在code_acct_month中未找到有效记录
	public static final int ERR_ACCT_ITEM_CODE_NOT_FOUND = -41003; // acct_item_code在code_act_acct_item中未找到记录
	public static final int ERR_BALANCE_TYPE_ID_NOT_FOUND = -41004; // balance_type_id在code_bil_balance_type中未找到记录
	public static final int ERR_PAY_ID_NOT_FOUND = -41005; // pay_id未找到
	public static final int ERR_ITMESTAMP_NOT_FOUND = -41006; // v_dual查找时间戳失败
	public static final int ERR_ADJUST_MODE = -41007; //adjust mode error
			
	public static final int ERR_DATABASE_DATA_ABNORMAL = -42001; // 数据库数据异常（如码表为空等）

	public static final int ERR_UNKNOWN_CALLER_TYPE = -49001; // 销账函数caller type不识别
	public static final int ERR_UNSUPPORTED_CALLER_TYPE = -49002; // 销账函数caller type不识别

	public static final int ERR_MEM_CONN_DB = -50001; // memcache init DB连接报错
	public static final int ERR_MEM_INVOKE = -50002; // memcache invoke ERROR
	public static final int ERR_INSIDE_INVOKE = -50003; //内部调用失败
	
	public static final int RE_WRITEOFF = -9000;//余额产生并发，需要重新simulate
	public static final int ERR_GET_RULE_GROUP_STATE_RELATION = -70007;
	public static final int ERR_GET_RULE_ITEM_CODE_RELATION = -70015;
	public static final int ERR_GET_RULE_OFR_SPLIT = -70005;
	public static final int ERR_GET_RULE_TARIFF_CONF_INFO = -70006;
	
	//文件入库
	public static final int ERR_FILE2DB_ITEM = -60001;
	public static final int ERR_FILE2DB_LAST_ITEM_NO_MONTH = -60002;
	public static final int ERR_FILE2DB_RENAME = -60003;

	// 以下兼容ZSMART返回码
	public static final String ERR_DEVICE_NUMBER_NULL = "ZSMART-CC-00001"; // 业务号码不能为空
	public static final String ERR_DEVICE_NUMBER_NONEXIST = "ZSMART-CC-00003"; // 业务号码不存在
	public static final String ERR_PAYID_NONEXSIT = "ZSMART-CC-00016"; // 账户不存在

	public static final String ERR_UNCLASSIFIED = "ZSMART-CC-00000"; // 未归类错误
	
	public static final String ERR_MSG_DEVICE_NUMBER_NULL = "业务号码不能为空";
	public static final String ERR_MSG_DEVICE_NUMBER_NONEXIST = "业务号码不存在";
	public static final String ERR_MSG_PAYID_NONEXSIT = "账户不存在";
	
	// 充值部分
	public static final String ERR_RECHARGE_SERIALNO_NULL = "ZSMART-CC-00073"; // 充值流水不能为空
	public static final String ERR_RECHARGE_JDACCOUNT_NULL = "ZSMART-CC-00074"; // 京东账号不能为空
	public static final String ERR_RECHARGE_RECHARGE_TYPE_NULL = "ZSMART-CC-00075"; // 充值类型不能为空
	public static final String ERR_RECHARGE_AMOUNT_NULL = "ZSMART-CC-00076"; // 充值金额不能为空
	public static final String ERR_RECHARGE_RECHARGE_DETAIL_NULL = "ZSMART-CC-00077"; // 充值列表不能为空
	public static final String ERR_RECHARGE_SCAN_ASYNC_TABLE_EX = "ZSMART-CC-00092"; // 扫描异步充值表异常
	
	public static final String ERR_MSG_RECHARGE_SERIALNO_NULL = "充值流水不能为空";
	public static final String ERR_MSG_RECHARGE_JDACCOUNT_NULL = "京东账号不能为空";
	public static final String ERR_MSG_RECHARGE_RECHARGE_TYPE_NULL = "充值类型不能为空";
	public static final String ERR_MSG_RECHARGE_AMOUNT_NULL = "充值金额不能为空";
	public static final String ERR_MSG_RECHARGE_RECHARGE_DETAIL_NULL = "充值列表不能为空";
	public static final String ERR_MSG_RECHARGE_SCAN_ASYNC_TABLE_EX = "扫描异步充值表异常";
	
	
	// 星美销账错误码--资源转换，资源转增部分
	public static final String ERR_CODE_PARAMETER_EMPTY = "100";
	public static final String ERR_CODE_PARAMETER_FORMAT = "101";
	public static final String ERR_CODE_MEMBER_DATA_PARTIAL = "201";
	public static final String ERR_CODE_MEMBER_NOT_EXIST = "202";
	public static final String ERR_CODE_TRANS_RULE_NOT_EXIST = "207";
	public static final String ERR_CODE_SYSTEM_ABNORMAL = "700";
	public static final String ERR_CODE_BALANCE_TYPE_NOT_TRANS = "505";
	public static final String ERR_CODE_BALANCE_TYPE_NOT_PRESENT = "506";
	public static final String ERR_CODE_SELECT_RECORD_EMPTY = "300";
	public static final String ERR_CODE_ACT_TAG_INVILID = "990";
	public static final String ERR_CODE_IN_BALANCE_ID = "991";
	public static final String ERR_CODE_BALANCE_CHANGE_SNAP_SHOT_NULL = "992";
	public static final String ERR_CODE_RULE_PARAMETERS_NULL = "993";
	public static final String ERR_CODE_OUT_BALANCE_TYPE_MARRY = "994";
	public static final String ERR_CODE_OTHER_EXCEPTION = "888";
	public static final String ERR_CODE_CUST_INFO_EMPTY = "777";
	public static final String ERR_CODE_LIANG_HAO = "333";
	public static final String ERR_CODE_OUT_BALANCE_MIN = "4001";
	public static final String ERR_CODE_OUT_BALANCE_NOT_ORDER = "4002";
	public static final String ERR_CODE_SHUJU_CONFIG = "4003";
	public static final String ERR_CODE_PARA_SHUJU = "4004";
	public static final String ERR_CODE_WRITE_FILE= "4005";

	public static final String ERR_GET_RULE_OFR_TARIFF_RELATION = "-70008";
	public static final String ERR_CODE_ACCT_MONTH_MSG = "查询code_acct_month表出错";
	public static final String ERR_INFO_USER_MSG = "用户不存在或者查询info_user表出错";
	public static final String ERR_CURR_MONTH_USER_MSG = "本月激活用户不计算月租";
	public static final String ERR_RULE_OFR_SPLIT_MSG = "查询rule_ofr_split表出错";
	public static final String ERR_RULE_TARIFF_CONF_INFO_MSG = "查询rule_tariff_conf_info表出错";
	public static final String ERR_RULE_GROUP_STATE_RELATION_MSG = "查询rule_group_state_relation表出错";
	public static final String ERR_RULE_OFR_TARIFF_RELATION_MSG = "查询rule_ofr_tariff_relation表出错";
	public static final String ERR_LIFE_USER_PRODCUT_MSG = "查询life_user_product表出错";
	public static final String ERR_RULE_CYCLE_HISTORY_MSG = "查询rule_cycle_history表出错";
	public static final String ERR_INSERT_CYCLE_HISTORY_MSG = "插入rule_cycle_history表出错";
	public static final String ERR_DELETE_CYCLE_HISTORY_MSG = "删除rule_cycle_history表数据出错";
	public static final String ERR_INSERT_Q_ACCT_PROCESS_MSG = "插入q_acct_process表出错";
	public static final String ERR_DATABASE_DATA_BANORMAL_MSG = "数据库数据异常（如码表为空）";
	public static final String ERR_WRITE_CDR_MSG = "写文件出错";
	public static final String ERR_FILE_SN_MSG = "获取文件序列失败";
	public static final String ERR_PRODUCT_TIME_MSG = "订购的产品生效时间大于账期月的失效时间或者产品失效时间小于账期月的失效时间";
	public static final String ERR_RULE_ITEM_CODE_REALTION_MSG = "查询rule_item_code_relation出错";
	public static final String ERR_BIL_ACT_REAL_TIME_BILL_MSG = "查询用户账户实时账单表出错";
	public static final String ERR_MSG_WRITE_FILE = "写文件失败";
	public static final String ERR_MSG_PARA_CDR_PATH_NULL = "入参文件目录未配置，请到rule_paramertes表中配置";
	public static final String ERR_MSG_PARA_TARIFF_CONF = "表rule_tariff_conf_info.REF_MEMBER_TYPE配置错误";
	public static final String ERR_MSG_OUT_BALANCE_NOT_ORDER = "转出额度不符合要求";
	public static final String ERR_MSG_OUT_BALANCE_MIN = "转出用户转出类型的账本总额度小于转出额度";
	public static final String ERR_MSG_OUT_BALANCE_TYPE_EMPTY = "转出用户没有当前转出资源账本";
	public static final String ERR_MSG_LIANG_HAO = "靓号不能转增";
	public static final String ERR_MSG_CUST_INFO_EMPTY = "获取会员信息失败";
	public static final String ERR_MSG_OUT_BALANCE_TYPE_MARRY = "转出账本类型与转出账本不匹配";
	public static final String ERR_MSG_RULE_PARAMETERS_NULL = "参数配置表不能为空";
	public static final String ERR_MSG_BALANCE_CHANGE_SNAP_SHOT_NULL = "余额变动返回结果快照不能为空";
	public static final String ERR_MSG_IN_BALANCE_ID = "获取转入账本失败";
	public static final String ERR_MSG_ACT_TAG_INVILID = "当前账期下该交易无法进行";
	public static final String ERR_MSG_SELECT_RECORD_EMPTY = "查询无记录";
	public static final String ERR_MSG_BALANCE_TYPE_NOT_PRESENT = "该账本不能转赠";
	public static final String ERR_MSG_BALANCE_TYPE_NOT_TRANS = "该账本不能转换";
	public static final String ERR_MSG_SYSTEM_ABNORMAL = "系统异常";
	public static final String ERR_MSG_TRANS_RULE_NOT_EXIST = "查询转换规则不存在";
	public static final String ERR_MSG__NOT_EXIST = "会员不存在";
	public static final String ERR_MSG_MEMBER_DATA_PARTIAL = "会员资料不完善";		
	public static final String ERR_MSG_PARAMETER_FORMAT = "入参格式错误";
	public static final String ERR_MSG_PARAMETER_EMPTY = "入参缺失";
	
	public static final int ERR_COMMODITY_TYPE = -70001;
	//月租
	public static final String ERR_CODE_INFO_USER_EMPTY = "40000";
	public static final String ERR_CODE_ORDER_INFO_NOT_NULL = "40001";
	public static final String ERR_MSG_ORDER_INFO_NOT_NULL = "用户没有订购信息";
	public static final String ERR_MSG_INFO_USER_EMPTY = "用户资料不全";
	public static final String ERR_CODE_LogCycleHistory_NOT_NULL ="40002";
	public static final String ERR_MSG_LogCycleHistory_NOT_NULL="收租日志为空";
	
}
