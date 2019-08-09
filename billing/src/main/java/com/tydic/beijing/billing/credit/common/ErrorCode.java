package com.tydic.beijing.billing.credit.common;

/**
 * 
 * @author Tian
 *
 */
public final class ErrorCode {
	public static final int CHARGE_DATE_ERROR = -70001; // 账期不对
	public static final int BIL_ACT_BILL_ERROR = -70002; // 历史账单不存在
	public static final int BIL_ACT_REAL_BILL_ERROR = -70003; // 实时账单不存在
	public static final int PROC_ID_NOT_EXISTS = -70004; // 进程id不存在，-DPROC_ID=0
	public static final int CREDIT_THRESHOLD_ERROR = -70004; // 信控阀值未设置

	public static final int SRC_DIR_NOT_EXISTS = -70005; // 信控阀值未设置
	public static final int BAK_DIR_NOT_EXISTS = -70006; // 信控阀值未设置
	// 用户不存在memcache，
	public static final int INFO_USER_NOT_IN_MEMCACHE = -70007;
}
