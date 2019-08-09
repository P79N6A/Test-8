package com.tydic.beijing.billing.account.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.account.biz.QueryInfo;
import com.tydic.beijing.billing.account.datastore.DSAcctItemCode;
import com.tydic.beijing.billing.account.datastore.DSAcctMonth;
import com.tydic.beijing.billing.account.datastore.DSBalanceType;
import com.tydic.beijing.billing.account.datastore.DSPayBalanceCode;
import com.tydic.beijing.billing.account.datastore.DSPayItemCode;
import com.tydic.beijing.billing.account.datastore.DSSpePayment;
import com.tydic.beijing.billing.account.type.UserBillSummary;
import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActBill;
import com.tydic.beijing.billing.dao.BilActUserRealTimeBill;
import com.tydic.beijing.billing.dao.BilActUserRealTimeBillForMemcached;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CodeActAcctItem;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.CodeBilPayBalanceCode;
import com.tydic.beijing.billing.dao.CodeBilPayItemCode;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.LogRefreshTrigger;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.PayUserRelForMemCached;
import com.tydic.beijing.billing.dao.RuleBilSpePayment;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.beijing.billing.util.RecordAssembler;
import com.tydic.uda.service.S;

/**
 * 
 * @author Tian
 *
 */
public class WriteOff {
	private final static Logger LOGGER = Logger.getLogger(WriteOff.class);
	private final static String PAY_REL_DEFAULT_TAG = "0"; // 主账户
	private final static int DEFAULT_BALANCE_TYPE_ID = 1; // 默认主资金账本类型ID
	private final static int PAY_BALANCE_CODE_NONE = -1; // 无付费账本编码
	private final static int PAY_ITEM_CODE_NONE = -1; // 无付费账目项编码
	private final static int SPE_PAYMENT_TYPE_NONE = -1; // 非专款专用类型
	private final static int ACCT_ITEM_CODE_INCLUDE = 1; // 包含该费用项
	private final static String INVOICE_FLAG_ENABLE = "1"; // 可打印发票

	private final static boolean MEMCACHED_SWITCH_ON = true; // 使用缓存
	private final static boolean MEMCACHED_SWITCH_OFF = false; // 不使用缓存

	@Autowired
	private DSPayBalanceCode payBalanceCodes;
	@Autowired
	private DSPayItemCode payItemCodes;
	@Autowired
	private DSAcctItemCode acctItemCodes;
	@Autowired
	private DSSpePayment spePayments;
	@Autowired
	private DSBalanceType balanceTypes;
	@Autowired
	private DSAcctMonth acctMonths;

	public WriteOff() {
	}

	/**
	 * 
	 * @param userId
	 *            用户ID
	 * @param usedInfo
	 *            账目项，费用对的列表
	 * @param acctMonth
	 *            帐期
	 * @param caller
	 *            调用者
	 * @param writeOffDetail
	 *            销账明细
	 * @return 返回码
	 * @throws Exception
	 */
	public int writeOff(String userId, List<UserBillSummary> usedInfo,
			int acctMonth, int caller, List<WriteOffDetail> writeOffDetail)
			throws Exception {
		return writeOff(userId, usedInfo, acctMonth, caller, writeOffDetail,
				null, MEMCACHED_SWITCH_ON);
	}

	public int writeOff(String userId, List<UserBillSummary> usedInfo,
			int acctMonth, int caller, List<WriteOffDetail> writeOffDetail,
			UserInfoForMemCached infoInMem) throws Exception {
		return writeOff(userId, usedInfo, acctMonth, caller, writeOffDetail,
				infoInMem, MEMCACHED_SWITCH_ON);
	}

	/**
	 * 
	 * @param userId
	 *            用户ID
	 * @param usedInfo
	 *            账目项，费用对的列表
	 * @param acctMonth
	 *            帐期
	 * @param caller
	 *            调用者
	 * @param writeOffDetail
	 *            销账明细
	 * @param infoInMem
	 *            传入聚合对象
	 * @param useMemcached
	 *            是否使用memcached
	 * @return 返回码
	 * @throws Exception
	 */
	public int writeOff(String userId, List<UserBillSummary> usedInfo,
			int acctMonth, int caller, List<WriteOffDetail> writeOffDetail,
			UserInfoForMemCached infoInMem, boolean useMemcached)
			throws Exception {
		long beginWriteOff = System.currentTimeMillis();
		// 检查入参
		if (((caller == BasicType.WRITE_OFF_CALLER_RATING)
				|| (caller == BasicType.WRITE_OFF_CALLER_CUTEGG) || (caller == BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY))
				&& (usedInfo == null)) {
			// Rating related call [usedInfo] Can't be NULL
			LOGGER.error("Parameter[UsedInfo] Check Failed!");
			throw new BasicException(ErrorCode.ERR_PARAM_CONTENT,
					"Parameter[UsedInfo] Check Failed!");
		}
		if (writeOffDetail == null) {
			LOGGER.error("Parameter[WriteOffDetail] CAN'T Be NULL!");
			throw new BasicException(ErrorCode.ERR_PARAM_CONTENT,
					"Parameter[WriteOffDetail] CAN'T Be NULL!");
		} else {
			writeOffDetail.clear();
		}
		// 获取基准时间 YYYY-MM-DD
		String baseDate = getBaseDate(usedInfo, acctMonth, caller);
		if ((baseDate == null) || (baseDate.length() != 10)) {
			LOGGER.error("Get Base Date Error[" + baseDate + "]!");
			throw new BasicException(ErrorCode.ERR_PARAM_CONTENT,
					"Get Base Date Error[" + baseDate + "]!");
		}
		// 查找有效支付关系
		List<PayUserRel> lups = null;
		long beginGetInfo = System.currentTimeMillis();
		if (infoInMem != null) {
			lups = getActivePayRelation(infoInMem, baseDate);
		} else if (userId != null) {
			lups = getActivePayRelation(userId, useMemcached, baseDate);
		} else {
			LOGGER.error("Parameter[UserId] Check Failed!");
			throw new BasicException(ErrorCode.ERR_PARAM_CONTENT,
					"Parameter[UserId] Check Failed!");
		}
		long endGetInfo = System.currentTimeMillis();
		LOGGER.debug("[STAT][WriteOff][getActivePayRelation]["
				+ (endGetInfo - beginGetInfo) + "]ms");
		// 加载用户实时账单
		long beginGetBill = System.currentTimeMillis();
		List<UserBillSummary> ubss = getOrderedUserBill(userId, usedInfo,
				acctMonth, caller, useMemcached);
		long endGetBill = System.currentTimeMillis();
		LOGGER.debug("[STAT][WriteOff][getOrderedUserBill]["
				+ (endGetBill - beginGetBill) + "]ms");
		if ((ubss == null) || (ubss.isEmpty())) {
			LOGGER.warn("[WriteOff]FOUND NO FIT BILL!");
			return 0;
		}
		// 开始销账
		long beginProcess = System.currentTimeMillis();
		int ret = process(lups, ubss, caller, writeOffDetail, baseDate);
		long endProcess = System.currentTimeMillis();
		LOGGER.debug("[STAT][WriteOff][process][" + (endProcess - beginProcess)
				+ "]ms");
		if (LOGGER.isDebugEnabled()) {
			for (WriteOffDetail wod : writeOffDetail) {
				LOGGER.debug("[DETAIL WRITEOFF]" + wod.toString());
			}
		}
		// for rating specific
		if ((caller == BasicType.WRITE_OFF_CALLER_RATING)
				|| (caller == BasicType.WRITE_OFF_CALLER_CUTEGG)
				|| (caller == BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY)) {
			for (Iterator<WriteOffDetail> iter = writeOffDetail.iterator(); iter
					.hasNext();) {
				WriteOffDetail wod = iter.next();
				int flag = -1;
				for (UserBillSummary ubs : usedInfo) {
					if ((wod.getUser_Id().equals(ubs.getUser_id()))
							&& (wod.getAcct_month() == ubs.getAcct_month())
							&& (wod.getAcct_item_code() == ubs
									.getAcct_item_code())
							&& (wod.getBillSource().equalsIgnoreCase("OUTCOME"))) {
						flag = 0;
						break;
					}
				}
				if (flag != 0) {
					iter.remove();
				}
			}
		}
		long endWriteOff = System.currentTimeMillis();
		LOGGER.debug("[STAT][WriteOff]Total[" + (endWriteOff - beginWriteOff)
				+ "]ms");
		return ret;
	}

	private int process(List<PayUserRel> lups, List<UserBillSummary> ubss,
			int caller, List<WriteOffDetail> wods, String baseDate)
			throws Exception {
		LOGGER.debug("WriteOff Begin........................................................................");
		BalanceInfoSummary defaultBalanceInfo = null;
		for (PayUserRel lup : lups) {
			LOGGER.debug("[PAY RELATION]" + lup.toString());
			List<BalanceInfoSummary> biss = getActiveOrderedBalanceInfo(
					lup.getPay_id(), lup.getPaybalance_code(), baseDate);
			eliminateSpecTypeAccountBook(biss, caller);
			if ((biss == null) || (biss.isEmpty())) {
				continue;
			}
			if (lup.getDefault_tag().trim().equals(PAY_REL_DEFAULT_TAG)) {
				for (BalanceInfoSummary bisTemp : biss) {
					if (bisTemp.getPayBalance().getBalance_type_id() == DEFAULT_BALANCE_TYPE_ID) {
						defaultBalanceInfo = bisTemp;
					}
				}
				if (defaultBalanceInfo == null) {
					LOGGER.debug("A Little Tricky-Y!");
					defaultBalanceInfo = biss.get(biss.size() - 1);
				}
			}
			long canUsedTotal = 0L;
			double billRatio = 0.0D;
			if ((lup.getLimit_type().equals(BasicType.LIMIT_TYPE_FIXED_FIXED))
					|| ((lup.getLimit_type()
							.equals(BasicType.LIMIT_TYPE_FIXED_RATIO)))) {
				canUsedTotal = lup.getLimit_valuea();
			}
			if (lup.getLimit_type().equals(BasicType.LIMIT_TYPE_BILL_RATIO)) {
				billRatio = lup.getLimit_valuea()
						/ (double) lup.getLimit_valueb();
			}
			if (lup.getLimit_type().equals(BasicType.LIMIT_TYPE_NONE)) {
				for (BalanceInfoSummary bis : biss) {
					for (Iterator<UserBillSummary> iter = ubss.iterator(); iter
							.hasNext();) {
						UserBillSummary ubs = iter.next();
						if (isBalanceCanBeUsed(lup, bis, ubs)) {
							long balance = getBalanceBasedCallerType(bis,
									caller);
							long fee = ubs.getActive_fee();
							if (balance > 0) {
								fillWriteOffDetail(bis, ubs,
										Math.min(balance, fee), caller, wods);
							}
						}
						if (ubs.getActive_fee() <= 0L) {
							iter.remove();
						}
					}
				}
			} else if (lup.getLimit_type().equals(
					BasicType.LIMIT_TYPE_FIXED_FIXED)) {
				LOGGER.debug("PayId[" + lup.getPay_id() + "] LimitType["
						+ lup.getLimit_type() + "] CanUsedBalance["
						+ canUsedTotal + "]");
				for (BalanceInfoSummary bis : biss) {
					for (Iterator<UserBillSummary> iter = ubss.iterator(); iter
							.hasNext();) {
						if (canUsedTotal <= 0L) {
							break;
						}
						UserBillSummary ubs = iter.next();
						if (isBalanceCanBeUsed(lup, bis, ubs)) {
							long balance = getBalanceBasedCallerType(bis,
									caller);
							long fee = ubs.getActive_fee();
							if (balance > 0) {
								long mini = Math.min(Math.min(balance, fee),
										canUsedTotal);
								fillWriteOffDetail(bis, ubs, mini, caller, wods);
								canUsedTotal -= mini;
							}
							if (ubs.getActive_fee() <= 0L) {
								iter.remove();
							}
						}
					}
				}
			} else if (lup.getLimit_type().equals(
					BasicType.LIMIT_TYPE_FIXED_RATIO)) {
				long feeTotal = calcCanBeWrittenOffFee(lup, biss, ubss);
				LOGGER.debug("PayId[" + lup.getPay_id() + "] LimitType["
						+ lup.getLimit_type() + "] CanUsedBalance["
						+ canUsedTotal + "] CanWrittenOffFee[" + feeTotal
						+ "].");
				for (BalanceInfoSummary bis : biss) {
					for (Iterator<UserBillSummary> iter = ubss.iterator(); iter
							.hasNext();) {
						if ((canUsedTotal <= 0L) || (feeTotal <= 0L)) {
							continue;
						}
						UserBillSummary ubs = iter.next();
						if (isBalanceCanBeUsed(lup, bis, ubs)) {
							long balance = getBalanceBasedCallerType(bis,
									caller);
							long fee = ubs.getActive_fee();
							long canUsedThisItem = (canUsedTotal * fee)
									/ feeTotal;
							canUsedThisItem = Math.min(canUsedThisItem, fee);
							if (balance > 0) {
								fillWriteOffDetail(bis, ubs,
										Math.min(balance, canUsedThisItem),
										caller, wods);
							}
							if (ubs.getActive_fee() <= 0L) {
								iter.remove();
							}
						}
					}
				}
			} else if (lup.getLimit_type().equals(
					BasicType.LIMIT_TYPE_BILL_RATIO)) {
				for (BalanceInfoSummary bis : biss) {
					for (Iterator<UserBillSummary> iter = ubss.iterator(); iter
							.hasNext();) {
						UserBillSummary ubs = iter.next();
						if (isBalanceCanBeUsed(lup, bis, ubs)) {
							long balance = getBalanceBasedCallerType(bis,
									caller);
							long fee = Math.round(ubs.getActive_fee()
									* billRatio);
							if (balance > 0) {
								fillWriteOffDetail(bis, ubs,
										Math.min(balance, fee), caller, wods);
							}
							if (ubs.getActive_fee() <= 0L) {
								iter.remove();
							}
						}
					}
				}
			}
		}
		// check: 是否完全销完
		if (!ubss.isEmpty()) {
			LOGGER.debug("UserId[" + lups.get(0).getUser_id()
					+ "] STILL Owe some DAMN Money!");
			if ((caller == BasicType.WRITE_OFF_CALLER_MONEY_ONLY)
					|| (caller == BasicType.WRITE_OFF_CALLER_MONTHEND)) {
				fillNonWriteOffDetail(lups.get(0).getUser_id(),
						defaultBalanceInfo, ubss, caller, wods);
			}
			return -1;
		}
		LOGGER.debug("WriteOff End...........................................................................Nicely!");
		return 0;
	}

	private void eliminateSpecTypeAccountBook(List<BalanceInfoSummary> biss,
			int caller) {
		if (biss == null) {
			return;
		}
		if (caller == BasicType.WRITE_OFF_CALLER_MONEY_ONLY) {
			for (Iterator<BalanceInfoSummary> iter = biss.iterator(); iter
					.hasNext();) {
				if (iter.next().getBalanceType().getUnit_type_id() != BasicType.UNIT_TYPE_MONEY) {
					iter.remove();
				}
			}
		} else if (caller == BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY) {
			for (Iterator<BalanceInfoSummary> iter = biss.iterator(); iter
					.hasNext();) {
				if (iter.next().getBalanceType().getUnit_type_id() == BasicType.UNIT_TYPE_MONEY) {
					iter.remove();
				}
			}
		}
	}

	/**
	 * 
	 * @param bis
	 * @param caller
	 * @return 根据调用者返回相应的余额
	 */
	private long getBalanceBasedCallerType(BalanceInfoSummary bis, int caller) {
		long balance;
		switch (caller) {
		case BasicType.WRITE_OFF_CALLER_SIMULATION:
		case BasicType.WRITE_OFF_CALLER_MONTHEND:
		case BasicType.WRITE_OFF_CALLER_RECHARGE:
		case BasicType.WRITE_OFF_CALLER_MONEY_ONLY:
			balance = bis.getPayBalance().getBalance();
			break;
		case BasicType.WRITE_OFF_CALLER_RATING:
		case BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY:
			balance = bis.getPayBalance().getReal_balance()
					- bis.getPayBalance().getUsed_balance();
			break;
		case BasicType.WRITE_OFF_CALLER_CUTEGG:
			balance = bis.getPayBalance().getBalance()
					- bis.getPayBalance().getUsed_balance();
			break;
		default:
			balance = 0L;
		}
		return balance;
	}

	/**
	 * 
	 * @param lup
	 * @param biss
	 * @param ubss
	 * @return 可由次账户支付的费用总额
	 * @throws Exception
	 */
	private long calcCanBeWrittenOffFee(PayUserRel lup,
			List<BalanceInfoSummary> biss, List<UserBillSummary> ubss)
			throws Exception {
		long feeTotal = 0L;
		for (BalanceInfoSummary bis : biss) {
			for (UserBillSummary ubs : ubss) {
				if (isBalanceCanBeUsed(lup, bis, ubs)) {
					feeTotal += ubs.getFee();
				}
			}
		}
		return feeTotal;
	}

	/**
	 * 
	 * @param bis
	 * @param ubs
	 * @param used
	 * @param caller
	 * @param wods
	 */
	private void fillWriteOffDetail(BalanceInfoSummary bis,
			UserBillSummary ubs, long used, int caller,
			List<WriteOffDetail> wods) {
		WriteOffDetail wod = new WriteOffDetail();
		wod.setUser_Id(ubs.getUser_id());
		wod.setPay_Id(bis.getPayBalance().getPay_id());
		wod.setAcct_month(ubs.getAcct_month());
		wod.setUnit_type_id(ubs.getUnit_type_id());
		wod.setAcct_item_code(ubs.getAcct_item_code());
		wod.setFee(ubs.getFee());
		wod.setRaw_fee(ubs.getRaw_fee());
		wod.setAdjust_fee(ubs.getAdjust_fee());
		wod.setDiscount_fee(ubs.getDiscount_fee());
		wod.setBefore_fee(ubs.getActive_fee());
		wod.setBalance_id(bis.getPayBalance().getBalance_id());
		wod.setBalance_type_id(bis.getBalanceType().getBalance_type_id());
		wod.setBillingId(ubs.getBilling_id());

		switch (caller) {
		case BasicType.WRITE_OFF_CALLER_SIMULATION:
		case BasicType.WRITE_OFF_CALLER_MONTHEND:
		case BasicType.WRITE_OFF_CALLER_RECHARGE:
		case BasicType.WRITE_OFF_CALLER_MONEY_ONLY:
			wod.setBalance(bis.getPayBalance().getBalance());
			wod.setBefore_balance(bis.getPayBalance().getBalance());
			bis.getPayBalance().setBalance(
					bis.getPayBalance().getBalance() - used);
			wod.setAfter_balance(bis.getPayBalance().getBalance());
			break;
		case BasicType.WRITE_OFF_CALLER_RATING:
		case BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY:
			wod.setBalance(bis.getPayBalance().getReal_balance()
					- bis.getPayBalance().getUsed_balance());
			wod.setBefore_balance(bis.getPayBalance().getReal_balance()
					- bis.getPayBalance().getUsed_balance());
			bis.getPayBalance().setReal_balance(
					bis.getPayBalance().getReal_balance() - used);
			wod.setAfter_balance(bis.getPayBalance().getReal_balance()
					- bis.getPayBalance().getUsed_balance());
			break;
		case BasicType.WRITE_OFF_CALLER_CUTEGG:
			wod.setBalance(bis.getPayBalance().getBalance()
					- bis.getPayBalance().getUsed_balance());
			wod.setBefore_balance(bis.getPayBalance().getBalance()
					- bis.getPayBalance().getUsed_balance());
			bis.getPayBalance().setBalance(
					bis.getPayBalance().getBalance() - used);
			wod.setAfter_balance(bis.getPayBalance().getBalance()
					- bis.getPayBalance().getUsed_balance());
			break;
		}
		ubs.setActive_fee(ubs.getActive_fee() - used);
		wod.setWriteoff_fee(used);
		wod.setAfter_fee(ubs.getActive_fee());
		wod.setBillSource(ubs.getSource());
		// added by tian@20141215 for: support invoice bill
		wod.setInvoice_fee(0L);
		if ((caller == BasicType.WRITE_OFF_CALLER_MONTHEND)
				|| (caller == BasicType.WRITE_OFF_CALLER_RECHARGE)) {
			CodeBilBalanceType cbbt = bis.getBalanceType();
			if ((cbbt != null)
					&& (cbbt.getInv_flag().trim().equals(INVOICE_FLAG_ENABLE))) {
				wod.setInvoice_fee(used);
			}
		}
		wods.add(wod);
	}

	/**
	 * 
	 * @param biss
	 * @param ubss
	 * @param caller
	 * @param wods
	 * @throws Exception
	 */
	private void fillNonWriteOffDetail(String userId, BalanceInfoSummary bis,
			List<UserBillSummary> ubss, int caller, List<WriteOffDetail> wods)
			throws Exception {
		if (ubss == null) {
			LOGGER.debug("A Little Tricky!");
			return;
		}
		if (bis == null) {
			LOGGER.debug("A Little Tricky-X!");
			return;
		}
		for (UserBillSummary ubs : ubss) {
			WriteOffDetail wod = new WriteOffDetail();
			wod.setUser_Id(ubs.getUser_id());
			wod.setAcct_month(ubs.getAcct_month());
			wod.setUnit_type_id(ubs.getUnit_type_id());
			wod.setAcct_item_code(ubs.getAcct_item_code());
			wod.setFee(ubs.getFee());
			wod.setRaw_fee(ubs.getRaw_fee());
			wod.setAdjust_fee(ubs.getAdjust_fee());
			wod.setDiscount_fee(ubs.getDiscount_fee());
			wod.setBefore_fee(ubs.getActive_fee());
			wod.setBillingId(ubs.getBilling_id());
			if (ubs.getUnit_type_id() != BasicType.UNIT_TYPE_MONEY) {
				wod.setPay_Id(bis.getPayBalance().getPay_id());
				wod.setBalance_id(BasicType.UNKNOWN_BALANCE_ID);
				wod.setBalance_type_id(BasicType.UNKNOWN_BALANCE_TYPE_ID);
				wod.setBalance(0L);
				wod.setBefore_balance(0L);
				wod.setAfter_balance(0L);
			} else {
				wod.setPay_Id(bis.getPayBalance().getPay_id());
				wod.setBalance_id(bis.getPayBalance().getBalance_id());
				wod.setBalance_type_id(bis.getBalanceType()
						.getBalance_type_id());

				switch (caller) {
				case BasicType.WRITE_OFF_CALLER_SIMULATION:
				case BasicType.WRITE_OFF_CALLER_MONTHEND:
				case BasicType.WRITE_OFF_CALLER_RECHARGE:
				case BasicType.WRITE_OFF_CALLER_MONEY_ONLY:
					wod.setBalance(bis.getPayBalance().getBalance());
					wod.setBefore_balance(bis.getPayBalance().getBalance());
					wod.setAfter_balance(bis.getPayBalance().getBalance());
					break;
				case BasicType.WRITE_OFF_CALLER_RATING:
				case BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY:
					wod.setBalance(bis.getPayBalance().getReal_balance()
							- bis.getPayBalance().getUsed_balance());
					wod.setBefore_balance(bis.getPayBalance().getReal_balance()
							- bis.getPayBalance().getUsed_balance());
					wod.setAfter_balance(bis.getPayBalance().getReal_balance()
							- bis.getPayBalance().getUsed_balance());
					break;
				case BasicType.WRITE_OFF_CALLER_CUTEGG:
					wod.setBalance(bis.getPayBalance().getBalance()
							- bis.getPayBalance().getUsed_balance());
					wod.setBefore_balance(bis.getPayBalance().getBalance()
							- bis.getPayBalance().getUsed_balance());
					wod.setAfter_balance(bis.getPayBalance().getBalance()
							- bis.getPayBalance().getUsed_balance());
					break;
				}
			}
			ubs.setActive_fee(ubs.getActive_fee());
			wod.setWriteoff_fee(0L);
			wod.setAfter_fee(ubs.getActive_fee());
			wod.setBillSource(ubs.getSource());
			// added by tian@20141215 for: support invoice item for month end
			// bill
			wod.setInvoice_fee(0L);
			wods.add(wod);
		}
	}

	/**
	 * 
	 * @param lup
	 * @param bis
	 * @param ubs
	 * @return 账本是否可用于销此账目项
	 * @throws Exception
	 */
	private boolean isBalanceCanBeUsed(PayUserRel lup, BalanceInfoSummary bis,
			UserBillSummary ubs) throws Exception {
		if (ubs.getUnit_type_id() == bis.getBalanceType().getUnit_type_id()) {
			if ((isAcctItemCodeMatched(ubs.getAcct_item_code(), bis
					.getBalanceType().getSpe_payment_id()))
					&& (isPayItemCodeAllowed(ubs.getAcct_item_code(),
							lup.getPayitem_code()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param acctItemCode
	 * @param payItemCode
	 * @return 账目项是否在CodeBilPayItemCode中存在
	 */
	private boolean isPayItemCodeAllowed(int acctItemCode, int payItemCode) {
		if (payItemCode == PAY_ITEM_CODE_NONE) {
			return true;
		} else {
			List<CodeBilPayItemCode> pics = payItemCodes.get(payItemCode);
			for (CodeBilPayItemCode pic : pics) {
				if (pic.getAcct_item_code() == acctItemCode) {
					return true;
				}
			}
		}
		LOGGER.debug("AcctItemCode[" + acctItemCode
				+ "] NOT define in PayItemCode[" + payItemCode + "]");
		return false;
	}

	/**
	 * 
	 * @param acctItemCode
	 * @param spePaymentId
	 * @return 账目项是否匹配
	 * @throws Exception
	 */
	private boolean isAcctItemCodeMatched(int acctItemCode, int spePaymentId)
			throws Exception {
		if (SPE_PAYMENT_TYPE_NONE == spePaymentId) {
			return true;
		}
		List<RuleBilSpePayment> bsps = spePayments.get(spePaymentId);
		// check
		int count = 0;
		for (RuleBilSpePayment bsp : bsps) {
			if (bsp.getInclude_tag() == ACCT_ITEM_CODE_INCLUDE) {
				count++;
			}
		}
		if ((count != 0) && (count != bsps.size())) {
			LOGGER.error("Spe_Payment_Id[" + spePaymentId
					+ "] has DOUBLE include_tag!");
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"Spe_Payment_Id[" + spePaymentId
							+ "] has DOUBLE include_tag!");
		}
		for (RuleBilSpePayment bsp : bsps) {
			if (bsp.getInclude_tag() == ACCT_ITEM_CODE_INCLUDE) {
				if (bsp.getAcct_item_code() == acctItemCode) {
					return true;
				}
			} else {
				if (bsp.getAcct_item_code() == acctItemCode) {
					LOGGER.debug("AcctItemCode[" + acctItemCode
							+ "] NOT Match SpePaymentId[" + spePaymentId + "]");
					return false;
				}
			}
		}
		if (count == 0) {
			return true;
		}
		LOGGER.debug("AcctItemCode[" + acctItemCode
				+ "] NOT Match SpePaymentId[" + spePaymentId + "]");
		return false;
	}

	/**
	 * 
	 * @param payId
	 * @return 可用账本信息
	 */
	private List<BalanceInfoSummary> getActiveOrderedBalanceInfo(String payId,
			int payBalanceCode, String baseDate) throws Exception {
		List<InfoPayBalance> ipbs = QueryInfo.getBalanceByPayId(payId);
		if ((ipbs == null) || (ipbs.isEmpty())) {
			LOGGER.info("PayId[" + payId
					+ "] NO RECORD FOUND in table[INFO_PAY_BALANCE]");
			return null;
		}
		if (LOGGER.isDebugEnabled()) {
			for (InfoPayBalance ipb : ipbs) {
				LOGGER.debug("[RAW BALANCE INFO]" + ipb.toString());
			}
		}
		for (Iterator<InfoPayBalance> iter = ipbs.iterator(); iter.hasNext();) {
			InfoPayBalance ipb = iter.next();
			if ((ipb.getEff_date().toString().compareTo(baseDate) <= 0)
					&& (ipb.getExp_date().toString().compareTo(baseDate) >= 0)) {
				if (payBalanceCode != PAY_BALANCE_CODE_NONE) {
					List<CodeBilPayBalanceCode> pbcs = payBalanceCodes
							.get(payBalanceCode);
					int flag = 0;
					for (CodeBilPayBalanceCode pbc : pbcs) {
						if (ipb.getBalance_type_id() == pbc
								.getBalance_type_id()) {
							flag = 1;
							break;
						}
					}
					if (flag == 0) {
						LOGGER.info("[NOT DEFINED IN PayBalanceCode, REMOVED]"
								+ ipb.toString());
						iter.remove();
					}
				}
			} else {
				LOGGER.info("[OUT OF DATE, REMOVED]" + ipb.toString());
				iter.remove();
			}
		}
		int count = 0;
		BalanceInfoSummary[] arrayBiss = new BalanceInfoSummary[ipbs.size()];
		for (InfoPayBalance ipb : ipbs) {
			BalanceInfoSummary bis = new BalanceInfoSummary();
			bis.setPayBalance(ipb);
			CodeBilBalanceType cbbt = balanceTypes
					.get(ipb.getBalance_type_id());
			if (cbbt == null) {
				LOGGER.error("BalanceTypeId[" + ipb.getBalance_type_id()
						+ "] NOT FOUND in table[CODE_BIL_BALANCE_TYPE]!");
				throw new BasicException(
						ErrorCode.ERR_BALANCE_TYPE_ID_NOT_FOUND,
						"BalanceTypeId["
								+ ipb.getBalance_type_id()
								+ "] NOT FOUND in table[CODE_BIL_BALANCE_TYPE]!");
			}
			bis.setBalanceType(cbbt);
			if (payBalanceCode != PAY_BALANCE_CODE_NONE) {
				List<CodeBilPayBalanceCode> pbcs = payBalanceCodes
						.get(payBalanceCode);
				for (CodeBilPayBalanceCode pbc : pbcs) {
					if (ipb.getBalance_type_id() == pbc.getBalance_type_id()) {
						bis.setPayBalanceCode(pbc);
						break;
					}
				}
			}
			arrayBiss[count] = bis;
			count++;
		}
		for (int i = 0; i < arrayBiss.length; i++) {
			for (int j = i; j < arrayBiss.length; j++) {
				if (payBalanceCode != PAY_BALANCE_CODE_NONE) {
					if (arrayBiss[i].getPayBalanceCode().getPriority() < arrayBiss[j]
							.getPayBalanceCode().getPriority()) {
						BalanceInfoSummary temp = arrayBiss[i];
						arrayBiss[i] = arrayBiss[j];
						arrayBiss[j] = temp;
					} else if (arrayBiss[i].getPayBalanceCode().getPriority() == arrayBiss[j]
							.getPayBalanceCode().getPriority()) {
						if (arrayBiss[i]
								.getPayBalance()
								.getExp_date()
								.compareTo(
										arrayBiss[j].getPayBalance()
												.getExp_date()) > 0) {
							BalanceInfoSummary temp = arrayBiss[i];
							arrayBiss[i] = arrayBiss[j];
							arrayBiss[j] = temp;
						}
					}
				} else {
					if (arrayBiss[i].getBalanceType().getPriority() < arrayBiss[j]
							.getBalanceType().getPriority()) {
						BalanceInfoSummary temp = arrayBiss[i];
						arrayBiss[i] = arrayBiss[j];
						arrayBiss[j] = temp;
					} else if (arrayBiss[i].getBalanceType().getPriority() == arrayBiss[j]
							.getBalanceType().getPriority()) {
						if (arrayBiss[i]
								.getPayBalance()
								.getExp_date()
								.compareTo(
										arrayBiss[j].getPayBalance()
												.getExp_date()) > 0) {
							BalanceInfoSummary temp = arrayBiss[i];
							arrayBiss[i] = arrayBiss[j];
							arrayBiss[j] = temp;
						}
					}
				}
			}
		}
		List<BalanceInfoSummary> biss = new ArrayList<BalanceInfoSummary>();
		for (BalanceInfoSummary bis : arrayBiss) {
			biss.add(bis);
			LOGGER.debug("[ACTIVE BALANCE INFO]"
					+ bis.getPayBalance().toString());
		}
		return biss;
	}

	/**
	 * 
	 * @param userId
	 * @return 生效的支付关系列表
	 * @throws Exception
	 */
	private List<PayUserRel> getActivePayRelation(String userId,
			boolean useMemcached, String baseDate) throws Exception {
		long beginQuery = System.currentTimeMillis();
		List<PayUserRel> lups = null;
		if (useMemcached == MEMCACHED_SWITCH_OFF) {
			lups = QueryInfo.getPayUserRelByUserId(userId);
		} else {
			PayUserRelForMemCached lupmc = S.get(PayUserRelForMemCached.class)
					.get(userId);
			if (lupmc != null) {
				lups = lupmc.getUserPayUserRels();
			}
		}

		long endQuery = System.currentTimeMillis();
		LOGGER.debug("[STAT][WriteOff][getPayUserRelByUserId]["
				+ (endQuery - beginQuery) + "]ms");
		if (lups == null) {
			LOGGER.error("Query User[" + userId + "] Pay-Relation Failed!");
			throw new BasicException(ErrorCode.ERR_PAY_RELATION_ABNORMAL,
					"Query User[" + userId + "] Pay-Relation Failed!");
		}
		// check
		int flag = 0;
		for (Iterator<PayUserRel> iter = lups.iterator(); iter.hasNext();) {
			PayUserRel lup = iter.next();
			LOGGER.debug("[RAW PAY RELATION]" + lup.toString());
			if ((lup.getEff_date().toString().compareTo(baseDate) <= 0)
					&& (lup.getExp_date().toString().compareTo(baseDate) >= 0)) {
				if (lup.getDefault_tag().equals(PAY_REL_DEFAULT_TAG)) {
					if (flag == 0) {
						flag++;
					} else if (flag == 1) {
						LOGGER.error("UserId[" + userId
								+ "] has Multi-Main-PayId!");
						throw new BasicException(
								ErrorCode.ERR_PAY_RELATION_ABNORMAL, "UserId["
										+ userId + "] has Multi-Main-PayId!");
					}
				}
			} else {
				LOGGER.info("[OUT OF DATE, REMOVED]" + lup.toString());
				iter.remove();
			}
		}
		if (flag == 0) {
			LOGGER.error("UserId[" + userId + "] Main-PayId Not Found!");
			throw new BasicException(ErrorCode.ERR_PAY_RELATION_ABNORMAL,
					"UserId[" + userId + "] Main-PayId Not Found!");
		}
		if (LOGGER.isDebugEnabled()) {
			for (PayUserRel lup : lups) {
				LOGGER.debug("[ACTIVE PAY RELATION]" + lup.toString());
			}
		}
		return lups;
	}

	private List<PayUserRel> getActivePayRelation(
			UserInfoForMemCached infoInMem, String baseDate) throws Exception {
		List<PayUserRel> lups = infoInMem.getPayUserRels();
		if (lups == null) {
			LOGGER.error("Get Device Number[" + infoInMem.getDevice_number()
					+ "] Pay-Relation Failed!");
			throw new BasicException(ErrorCode.ERR_PAY_RELATION_ABNORMAL,
					"Get Device Number[" + infoInMem.getDevice_number()
							+ "] Pay-Relation Failed!");
		}
		String userId = infoInMem.getInfoUser().getUser_id();
		// check
		int flag = 0;
		for (Iterator<PayUserRel> iter = lups.iterator(); iter.hasNext();) {
			PayUserRel lup = iter.next();
			LOGGER.debug("[RAW PAY RELATION]" + lup.toString());
			if ((lup.getEff_date().toString().compareTo(baseDate) <= 0)
					&& (lup.getExp_date().toString().compareTo(baseDate) >= 0)) {
				if (lup.getDefault_tag().equals(PAY_REL_DEFAULT_TAG)) {
					if (flag == 0) {
						flag++;
					} else if (flag == 1) {
						LOGGER.error("UserId[" + userId
								+ "] has Multi-Main-PayId!");
						throw new BasicException(
								ErrorCode.ERR_PAY_RELATION_ABNORMAL, "UserId["
										+ userId + "] has Multi-Main-PayId!");
					}
				}

			} else {
				LOGGER.info("[OUT OF DATE, REMOVED]" + lup.toString());
				iter.remove();
			}
		}
		if (flag == 0) {
			LOGGER.error("UserId[" + userId + "] Main-PayId Not Found!");
			throw new BasicException(ErrorCode.ERR_PAY_RELATION_ABNORMAL,
					"UserId[" + userId + "] Main-PayId Not Found!");
		}
		if (LOGGER.isDebugEnabled()) {
			for (PayUserRel lup : lups) {
				LOGGER.debug("[ACTIVE PAY RELATION]" + lup.toString());
			}
		}
		return lups;
	}

	/**
	 * 
	 * @param userId
	 * @param usedInfo
	 * @param acctMonth
	 * @param caller
	 * @return
	 */
	private List<UserBillSummary> getOrderedUserBill(String userId,
			List<UserBillSummary> usedInfo, int acctMonth, int caller,
			boolean useMemcached) throws Exception {
		List<UserBillSummary> ubss = new ArrayList<UserBillSummary>();
		List<BilActUserRealTimeBill> urtbs = null;
		List<BilActBill> babs = null;
		long beginQuery = System.currentTimeMillis();
		switch (caller) {
		case BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY:
			break;
		case BasicType.WRITE_OFF_CALLER_RATING:
		case BasicType.WRITE_OFF_CALLER_CUTEGG:
			// support two MEMCACHED clusters at same time need to know more
			// CONFIG Details
			urtbs = getRealTimeBill(userId, acctMonth);
			break;
		case BasicType.WRITE_OFF_CALLER_MONTHEND:
		case BasicType.WRITE_OFF_CALLER_SIMULATION:
		case BasicType.WRITE_OFF_CALLER_MONEY_ONLY:
			if (useMemcached == MEMCACHED_SWITCH_OFF) {
				urtbs = getRealTimeBill(userId, acctMonth);
			} else {
				BilActUserRealTimeBillForMemcached urtbmc = S.get(
						BilActUserRealTimeBillForMemcached.class).get(
						BasicType.MEMCACHED_BILL_PREFIX + userId);
				if (urtbmc != null) {
					urtbs = urtbmc.getL_userbill();
				}
			}
			break;
		case BasicType.WRITE_OFF_CALLER_RECHARGE:
			babs = getHistoryBill(userId);
			break;
		default:
			LOGGER.error("Unknown Caller Type[" + caller + "]");
			throw new BasicException(ErrorCode.ERR_UNKNOWN_CALLER_TYPE,
					"Unknown Caller Type[" + caller + "]");
		}
		long endQuery = System.currentTimeMillis();
		LOGGER.debug("[STAT][WriteOff][queryBills][" + (endQuery - beginQuery)
				+ "]ms");
		if (urtbs != null) {
			for (BilActUserRealTimeBill urtb : urtbs) {
				if (urtb.getOrg_fee() <= 0L) {
					continue;
				}
				UserBillSummary ubs = new UserBillSummary();
				ubs.setUser_id(urtb.getUser_id());
				ubs.setAcct_month(urtb.getAcct_month());
				ubs.setAcct_item_code(urtb.getAcct_item_code());
				ubs.setUnit_type_id(urtb.getUnit_type_id());
				ubs.setFee(urtb.getFee());
				ubs.setActive_fee(urtb.getFee());
				ubs.setRaw_fee(urtb.getOrg_fee());
				ubs.setAdjust_fee(urtb.getAdjust_before());
				ubs.setDiscount_fee(urtb.getDiscount_fee());
				ubs.setSource("REAL_TIME_BILL");
				ubs.setBilling_id(-1L);
				ubss.add(ubs);
			}
		}
		if (babs != null) {
			for (BilActBill bab : babs) {
				if (bab.getOwe_fee() <= 0L) {
					continue;
				}
				UserBillSummary ubs = new UserBillSummary();
				ubs.setUser_id(bab.getUser_id());
				ubs.setAcct_month(bab.getAcct_month());
				ubs.setAcct_item_code(bab.getAcct_item_code());
				ubs.setUnit_type_id(bab.getUnit_type_id());
				ubs.setFee(bab.getOwe_fee());
				ubs.setActive_fee(bab.getOwe_fee());
				ubs.setRaw_fee(bab.getFee());
				ubs.setAdjust_fee(bab.getAdjust_before());
				ubs.setDiscount_fee(bab.getDiscount_fee());
				ubs.setSource("HISTORY_BILL");
				ubs.setBilling_id(bab.getBilling_id());
				ubss.add(ubs);
			}
		}
		if (usedInfo != null) {
			for (UserBillSummary ubs : usedInfo) {
				ubs.setActive_fee(ubs.getFee());
				ubs.setRaw_fee(ubs.getFee());
				ubs.setSource("OUTCOME");
				ubs.setBilling_id(-1L);
			}
			ubss.addAll(usedInfo);
		}
		if (ubss.size() != 0) {
			sortUserBill(ubss);
		}
		// modified@20141110 for: WRITE_OFF_CALLER_MONEY_ONLY 不需要剔除
		if ((caller == BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY)
				|| (caller == BasicType.WRITE_OFF_CALLER_MONTHEND)) {
			eliminateSpecTypeBill(ubss, caller);
		}
		return ubss;
	}

	/**
	 * 剔除指定类型的账单
	 * 
	 * @param ubss
	 * @param caller
	 */
	private void eliminateSpecTypeBill(List<UserBillSummary> ubss, int caller) {
		if (ubss == null) {
			return;
		}
		if (caller == BasicType.WRITE_OFF_CALLER_MONTHEND) {
			for (Iterator<UserBillSummary> iter = ubss.iterator(); iter
					.hasNext();) {
				if (iter.next().getUnit_type_id() != BasicType.UNIT_TYPE_MONEY) {
					iter.remove();
				}
			}
		} else if (caller == BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY) {
			for (Iterator<UserBillSummary> iter = ubss.iterator(); iter
					.hasNext();) {
				if (iter.next().getUnit_type_id() == BasicType.UNIT_TYPE_MONEY) {
					iter.remove();
				}
			}
		}
	}

	/**
	 * 账单排序
	 * 
	 * @param ubss
	 * @throws Exception
	 */
	private void sortUserBill(List<UserBillSummary> ubss) throws Exception {
		int[] orderNumber = new int[ubss.size()];
		int counter = 0;
		for (UserBillSummary ubs : ubss) {
			CodeActAcctItem caai = acctItemCodes.get(ubs.getAcct_item_code());
			if (caai == null) {
				LOGGER.error("AcctItemCode[" + ubs.getAcct_item_code()
						+ "] NOT FOUND in table[CODE_ACT_ACCT_ITEM]!");
				throw new BasicException(
						ErrorCode.ERR_ACCT_ITEM_CODE_NOT_FOUND, "AcctItemCode["
								+ ubs.getAcct_item_code()
								+ "] NOT FOUND in table[CODE_ACT_ACCT_ITEM]!");
			}
			orderNumber[counter] = caai.getOrder_number();
			counter++;
		}
		UserBillSummary[] arrayUbss = new UserBillSummary[ubss.size()];
		ubss.toArray(arrayUbss);
		for (int i = 0; i < orderNumber.length; i++) {
			for (int j = i; j < orderNumber.length; j++) {
				if ((arrayUbss[i].getAcct_month() > arrayUbss[j]
						.getAcct_month())
						|| ((arrayUbss[i].getAcct_month() == arrayUbss[j]
								.getAcct_month()) && (orderNumber[i] > orderNumber[j]))) {
					int temp = orderNumber[i];
					orderNumber[i] = orderNumber[j];
					orderNumber[j] = temp;
					UserBillSummary tempUbs = arrayUbss[i];
					arrayUbss[i] = arrayUbss[j];
					arrayUbss[j] = tempUbs;
				}
			}
		}
		ubss.clear();
		for (UserBillSummary ubs : arrayUbss) {
			ubss.add(ubs);
			LOGGER.debug("[SORTED BILL]" + ubs.toString());
		}
	}

	/**
	 * 
	 * @param userId
	 * @return 出账账单
	 */
	private List<BilActBill> getHistoryBill(String userId) {
		List<BilActBill> babs = QueryInfo.getActBillByUserId(userId);
		if ((babs == null) || (babs.isEmpty())) {
			LOGGER.warn("UserId[" + userId
					+ "] NO RECORD FOUND in Table[bil_act_bill]!");
			return null;
		}
		return babs;
	}

	/**
	 * 
	 * @param userId
	 * @return 实时账单
	 */
	private List<BilActUserRealTimeBill> getRealTimeBill(String userId,
			int acctMonth) throws Exception {
		List<BilActUserRealTimeBill> rtbs = QueryInfo.getRealTimeBillByUserId(
				userId, acctMonth, getMonthSuffixByAcctMonth(acctMonth));
		if ((rtbs == null) || (rtbs.isEmpty())) {
			LOGGER.warn("UserId[" + userId
					+ "] NO RECORD FOUND in Table[bil_act_user_real_time_bill_"
					+ getMonthSuffixByAcctMonth(acctMonth) + "]!");
			return null;
		}
		return rtbs;
	}

	/**
	 * 
	 * @return 当前时间 YYYY-MM-DD
	 */
	private String getBaseDate(List<UserBillSummary> usedInfo, int acctMonth,
			int caller) {
		if (((caller == BasicType.WRITE_OFF_CALLER_RATING)
				|| (caller == BasicType.WRITE_OFF_CALLER_CUTEGG) || (caller == BasicType.WRITE_OFF_CALLER_RESOURCE_ONLY))
				&& (usedInfo != null)) {
			if (!usedInfo.isEmpty()) {
				String tmpBillDate = usedInfo.get(0).getBill_date();
				if (tmpBillDate.length() != 8) {
					return null;
				} else {
					String baseDate = tmpBillDate.substring(0, 4);
					baseDate += "-";
					baseDate += tmpBillDate.substring(4, 6);
					baseDate += "-";
					baseDate += tmpBillDate.substring(6, 8);
					return baseDate;
				}
			}
		} else if (caller == BasicType.WRITE_OFF_CALLER_RECHARGE) {
			java.sql.Date currentDate = new java.sql.Date(
					System.currentTimeMillis());
			return currentDate.toString();
		} else {
			CodeAcctMonth cam = acctMonths.getByAcctMonth(acctMonth);
			if (cam != null) {
				java.util.Date expDate = cam.getAct_exp_date();
				if (expDate != null) {
					java.sql.Date tmpDate = new java.sql.Date(expDate.getTime());
					return tmpDate.toString();
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param acctMonth
	 * @return 账务月
	 */
	private String getMonthSuffixByAcctMonth(int acctMonth) throws Exception {
		CodeAcctMonth cam = acctMonths.getByAcctMonth(acctMonth);
		if (cam == null) {
			LOGGER.error("AcctMonth[" + acctMonth
					+ "] NOT FOUND in [DS]TABLE[CODE_ACCT_MONTH]!");
			throw new BasicException(ErrorCode.ERR_ACCT_MONTH_NOT_FOUND,
					"AcctMonth[" + acctMonth
							+ "] NOT FOUND in [DS]TABLE[CODE_ACCT_MONTH]!");
		}
		return cam.getPartition_no();
	}

	public LogRefreshTrigger refresh(int refreshBatchId, String datastoreName,
			String serviceName) {
		try {
			if (BasicType.DS_CODE_BIL_PAY_BALANCE_CODE.equals(datastoreName)) {
				payBalanceCodes.refresh();
			} else if (BasicType.DS_CODE_BIL_PAY_ITEM_CODE
					.equals(datastoreName)) {
				payItemCodes.refresh();
			} else if (BasicType.DS_CODE_ACT_ACCT_ITEM.equals(datastoreName)) {
				acctItemCodes.refresh();
			} else if (BasicType.DS_RULE_BIL_SPE_PAYMENT.equals(datastoreName)) {
				spePayments.refresh();
			} else if (BasicType.DS_CODE_BIL_BALANCE_TYPE.equals(datastoreName)) {
				balanceTypes.refresh();
			} else if (BasicType.DS_CODE_ACCT_MONTH.equals(datastoreName)) {
				acctMonths.refresh();
			} else {
				LOGGER.warn("Unknown Datastore Name[" + datastoreName + "]!");
				return RecordAssembler.assembleLogRefreshTrigger(
						refreshBatchId, datastoreName, serviceName,
						BasicType.REFRESH_STATUS_FAIL,
						"Unknown Datastore Name[" + datastoreName + "]!");
			}
			return RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
					datastoreName, serviceName, BasicType.REFRESH_STATUS_OK,
					null);
		} catch (BasicException ex) {
			LOGGER.error(ex.getMessage());
			return RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
					datastoreName, serviceName, BasicType.REFRESH_STATUS_FAIL,
					ex.getMessage());
		}
	}

	public DSPayBalanceCode getPayBalanceCodes() {
		return payBalanceCodes;
	}

	public void setPayBalanceCodes(DSPayBalanceCode payBalanceCodes) {
		this.payBalanceCodes = payBalanceCodes;
	}

	public DSPayItemCode getPayItemCodes() {
		return payItemCodes;
	}

	public void setPayItemCodes(DSPayItemCode payItemCodes) {
		this.payItemCodes = payItemCodes;
	}

	public DSAcctItemCode getAcctItemCodes() {
		return acctItemCodes;
	}

	public void setAcctItemCodes(DSAcctItemCode acctItemCodes) {
		this.acctItemCodes = acctItemCodes;
	}

	public DSSpePayment getSpePayments() {
		return spePayments;
	}

	public void setSpePayments(DSSpePayment spePayments) {
		this.spePayments = spePayments;
	}

	public DSBalanceType getBalanceTypes() {
		return balanceTypes;
	}

	public void setBalanceTypes(DSBalanceType balanceTypes) {
		this.balanceTypes = balanceTypes;
	}

	public DSAcctMonth getAcctMonths() {
		return acctMonths;
	}

	public void setAcctMonths(DSAcctMonth acctMonths) {
		this.acctMonths = acctMonths;
	}
}

/**
 * 存放账本及账本类型的便利类
 * 
 * @author Tian
 *
 */
class BalanceInfoSummary {
	private InfoPayBalance payBalance;
	private CodeBilBalanceType balanceType;
	private CodeBilPayBalanceCode payBalanceCode;

	public InfoPayBalance getPayBalance() {
		return payBalance;
	}

	public void setPayBalance(InfoPayBalance payBalance) {
		this.payBalance = payBalance;
	}

	public CodeBilBalanceType getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(CodeBilBalanceType balanceType) {
		this.balanceType = balanceType;
	}

	public CodeBilPayBalanceCode getPayBalanceCode() {
		return payBalanceCode;
	}

	public void setPayBalanceCode(CodeBilPayBalanceCode payBalanceCode) {
		this.payBalanceCode = payBalanceCode;
	}

}
