package com.tydic.beijing.billing.account.type;

public final class CodeDefine {
	public static final int NON_TREATED = 0;
	public static final int TREATED = 1;
	//Q_ACCT_PROCESS process_tag
	public static final int PROCESS_TAG_NON_TREATED = 0;
	public static final int PROCESS_TAG_TREATED = 1;
	public static final int PROCESS_TAG_TREATING = 3;//暂时未使用
	public static final int PROCESS_TAG_ERROR = 2;//调用dubbo账务处理服务失败，但是该记录已进行累帐，需要把费用归零后重新处理
}
