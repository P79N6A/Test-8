package com.tydic.beijing.billing.account.biz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.datastore.DSAcctMonth;
import com.tydic.beijing.billing.account.datastore.DSBalanceType;
import com.tydic.beijing.billing.account.datastore.DSRechargeTypeMapping;
import com.tydic.beijing.billing.account.datastore.DSUserEventMapping;
import com.tydic.beijing.billing.account.service.CreateAccountBook;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.HlpSmsSend;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoPayBalanceAsync;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LogActPay;
import com.tydic.beijing.billing.dao.LogRefreshTrigger;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.QRechargeCallback;
import com.tydic.beijing.billing.dao.QUserReasonSend;
import com.tydic.beijing.billing.dao.RuleRechargeTypeMapping;
import com.tydic.beijing.billing.dao.RuleUserEventMapping;
import com.tydic.beijing.billing.dao.Sequences;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.beijing.billing.dto.RechargeInfo;
import com.tydic.beijing.billing.dto.RechargeInfoDto;
import com.tydic.beijing.billing.dto.RechargeResult;
import com.tydic.beijing.billing.util.RecordAssembler;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class RechargeOps {
	private final static Logger LOGGER = Logger.getLogger(RechargeOps.class);
	private final static String RESULT_STATUS_OK = "1"; // 返回成功
	private final static String RESULT_STATUS_FAIL = "0"; // 返回失败
	private final static String PAY_REL_DEFAULT_TAG = "0"; // 主账户
	private final static String PAY_TYPE_RECHARGE = "1";
	private final static String ROLLBACK_FLAG_NO = "0";

	private final static String USER_EVENT_CODE_OPEN = "1";

	// added by tian@20150501 for: 预销户状态不允许充值
	private final static String USER_STATUS_YUXIAOHU = "401"; // 用户状态预销户
	private final static String USER_STATUS_INACTIVE = "501"; // 用户状态未激活
	private final static String ACTIVE_REASON_RECHARGE = "99"; // 充值
	private final static String ACTIVE_TYPE_RECHARGE = "00"; // 充值激活

	private final static String ACCESS_TAG_WITHDRAW = "0";

	private final static String SMS_SEND_MSISDN = "10023";

	private final static int Q_RECHARGE_CALLBACK_STATE_INIT = 0;
	private final static int Q_RECHARGE_CALLBACK_STATE_ASYNC_INIT = 10;

	// updated by tian@20150501 for: 充值赠送，B值兑换需特殊处理
	public final static String RECHARGE_TYPE_PROM = "Z";
	public final static String RECHARGE_TYPE_BV_EXCHANGE = "BV"; // BV*均属于此类

	private final static int DATETIME_LEN = 14;
	private final static int DATE_LEN = 8;

	private final static String ACCTBOOK_ADDUP_FLAG_Y = "1"; // 对应code_bil_balance_type中addup_flag,
																// 1-可累加到匹配的账本上

	private DSBalanceType balanceTypes;
	private DSRechargeTypeMapping rechargeTypeMapping;
	private CreateAccountBook createBook;
	private DSUserEventMapping userEventMapping;
	private DSAcctMonth acctMonths;

	private int flag4firstrun = 0;

	public RechargeOps() {
	}

	public boolean recharge(RechargeInfo info, RechargeResult result)
			throws Exception {
		if (result != null) {
			reset(result);
			checkInputParameters(info, result);
			UserInfoForMemCached userInfo = getUserInfoByDeviceNumber(
					info.getMSISDN(), result);
			process(userInfo, info, BasicType.UNIT_TYPE_MONEY, result);
			LOGGER.debug("Recharge DeviceNumber[" + info.getMSISDN()
					+ "] Done!");
			return true;
		}
		return false;
	}

	private void checkInputParameters(RechargeInfo info, RechargeResult result)
			throws Exception {
		result.setSn(info.getSn());
		if (info.getMSISDN() == null) {
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_DEVICE_NUMBER_NULL);
			result.setErrorMessage(ErrorCode.ERR_MSG_DEVICE_NUMBER_NULL);
		} else if (info.getSn() == null) {
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_RECHARGE_SERIALNO_NULL);
			result.setErrorMessage(ErrorCode.ERR_MSG_RECHARGE_SERIALNO_NULL);
		} else if (info.getRechargeInfoDtoList() == null) {
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_RECHARGE_RECHARGE_DETAIL_NULL);
			result.setErrorMessage(ErrorCode.ERR_MSG_RECHARGE_RECHARGE_DETAIL_NULL);
		} else if (info.getJdAcctNbr() == null) {
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_RECHARGE_JDACCOUNT_NULL);
			result.setErrorMessage(ErrorCode.ERR_MSG_RECHARGE_JDACCOUNT_NULL);
		} else if (info.getCallBackUrl() == null) {
			// result.setStatus(RESULT_STATUS_FAIL);
			// result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			// result.setErrorMessage("回调URL为空");
		} else if (info.getContactChannle() == null) {
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("缴费渠道不能为空");
		}
		if (result.getStatus().endsWith(RESULT_STATUS_FAIL)) {
			LOGGER.error("Recharge Check Input Parameters Failed!["
					+ result.getErrorMessage() + "]");
			throw new BasicException(result.getErrorCode(),
					result.getErrorMessage());
		}
	}

	private UserInfoForMemCached getUserInfoByDeviceNumber(String deviceNumber,
			RechargeResult result) throws Exception {
		UserInfoForMemCached userInfo = null;
		InfoUser iu = S.get(InfoUser.class).queryFirst(
				Condition.build("queryByDeviceNo").filter("device_number",
						deviceNumber));
		if (iu != null) {
			List<PayUserRel> purs = S.get(PayUserRel.class).query(
					Condition.build("queryByUserId").filter("user_id",
							iu.getUser_id()));
			if (purs != null) {
				userInfo = new UserInfoForMemCached();
				userInfo.setDevice_number(deviceNumber);
				userInfo.setInfoUser(iu);
				userInfo.setPayUserRels(purs);
				userInfo.setUserInfoPays(null);
				userInfo.setUserProducts(null);
				return userInfo;
			}
		}
		{
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_DEVICE_NUMBER_NONEXIST);
			result.setErrorMessage(ErrorCode.ERR_MSG_DEVICE_NUMBER_NONEXIST);
			LOGGER.error("Recharge DeviceNumber[" + deviceNumber
					+ "] NONEXIST!");
			throw new BasicException(result.getErrorCode(),
					result.getErrorMessage());
		}
	}

	private int getBalanceTypeId(String rechargeType, RechargeResult result)
			throws Exception {
		RuleRechargeTypeMapping ratm = rechargeTypeMapping.get(rechargeType);
		if (ratm == null) {
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("RechargeType找不到对应的账本类型ID");
			LOGGER.error("RechargeType[" + rechargeType + "]找不到对应的账本类型ID");
			throw new BasicException(result.getErrorCode(),
					result.getErrorMessage());
		} else {
			return ratm.getBalance_type_id();
		}
	}

	private String getDefaultPayId(UserInfoForMemCached userInfo,
			RechargeResult result) throws Exception {
		List<PayUserRel> lups = userInfo.getPayUserRels();
		for (PayUserRel lup : lups) {
			if (lup.getDefault_tag().equalsIgnoreCase(PAY_REL_DEFAULT_TAG)) {
				return lup.getPay_id();
			}
		}
		result.setStatus(RESULT_STATUS_FAIL);
		result.setErrorCode(ErrorCode.ERR_PAYID_NONEXSIT);
		result.setErrorMessage(ErrorCode.ERR_MSG_PAYID_NONEXSIT);
		LOGGER.error("Recharge DeviceNumber[" + userInfo.getDevice_number()
				+ "]找不到主账户");
		throw new BasicException(result.getErrorCode(),
				result.getErrorMessage());
	}

	private InfoPayBalance getMatchedAccountBook(List<InfoPayBalance> ipbs,
			int balanceTypeId, int unitTypeId, String effDate, String expDate)
			throws Exception {
		for (InfoPayBalance ipb : ipbs) {
			if (ipb.getBalance_type_id() == balanceTypeId) {
				CodeBilBalanceType cbbt = balanceTypes.get(ipb
						.getBalance_type_id());
				if (cbbt == null) {
					LOGGER.error("BalanceTypeId[" + ipb.getBalance_type_id()
							+ "] NOT FOUND in table[CODE_BIL_BALANCE_TYPE]!");
					throw new BasicException(
							ErrorCode.ERR_BALANCE_TYPE_ID_NOT_FOUND,
							"BalanceTypeId["
									+ ipb.getBalance_type_id()
									+ "] NOT FOUND in table[CODE_BIL_BALANCE_TYPE]!");
				}
				if (cbbt.getUnit_type_id() == unitTypeId) {
					if ((effDate == null) || effDate.trim().isEmpty()
							|| (expDate == null) || expDate.trim().isEmpty()) {
						LOGGER.debug("Input EffDate[" + effDate
								+ "] or ExpDate[" + expDate + "] is Empty!");
						return ipb;
					} else if ((effDate.length() != DATETIME_LEN)
							|| (expDate.length() != DATETIME_LEN)) {
						LOGGER.error("Input EffDate[" + effDate
								+ "] or ExpDate[" + expDate + "] Illegal!");
						throw new BasicException(ErrorCode.ERR_UNCLASSIFIED,
								"Input EffDate[" + effDate + "] or ExpDate["
										+ expDate + "] Illegal!");
					} else {
						// 规整为格式YYYYMMDD
						String truncExpDate = expDate.substring(0, DATE_LEN);
						String ipbExpDate = ipb.getExp_date().toString()
								.replaceAll("-", "");
						if (truncExpDate.equals(ipbExpDate)) {
							return ipb;
						}
					}
				}
			}
		}
		return null;
	}

	private int getUnitTypeId(int balanceTypeId) throws BasicException {
		CodeBilBalanceType cbbt = balanceTypes.get(balanceTypeId);
		if (cbbt == null) {
			LOGGER.error("BalanceTypeId[" + balanceTypeId
					+ "] NOT FOUND in table[CODE_BIL_BALANCE_TYPE]!");
			throw new BasicException(ErrorCode.ERR_BALANCE_TYPE_ID_NOT_FOUND,
					"BalanceTypeId[" + balanceTypeId
							+ "] NOT FOUND in table[CODE_BIL_BALANCE_TYPE]!");
		}
		return cbbt.getUnit_type_id();
	}

	private InfoPayBalance assembleInfoPayBalance(long balanceId, long amount) {
		InfoPayBalance ipbIncremental = new InfoPayBalance();
		ipbIncremental.setBalance_id(balanceId);
		ipbIncremental.setBalance(amount);
		ipbIncremental.setReal_balance(amount);
		return ipbIncremental;
	}

	private InfoPayBalanceAsync assembleInfoPayBalanceAsync(String sn,
			RechargeInfoDto rai, InfoPayBalance ipb) {
		InfoPayBalanceAsync ipba = new InfoPayBalanceAsync();
		ipba.setSn(sn);
		ipba.setBalance_id(ipb.getBalance_id());
		ipba.setPay_id(ipb.getPay_id());
		ipba.setBalance_type_id(ipb.getBalance_type_id());
		ipba.setBalance(rai.getAmount());
		ipba.setAsync_state(0);
		ipba.setUuid(UUID.randomUUID().toString());
		return ipba;
	}

	private LogActPay assembleLogActPay(UserInfoForMemCached userInfo,
			RechargeInfo info, RechargeInfoDto rai, InfoPayBalance ipb,
			String payId, int unitTypeId, String payTime) {
		LogActPay lap = new LogActPay();
		lap.setPay_charge_id(info.getSn());
		lap.setService_nbr(info.getMSISDN());
		lap.setUser_id(userInfo.getInfoUser().getUser_id());
		lap.setPay_id(payId);
		lap.setPay_time(payTime);
		lap.setPay_type(PAY_TYPE_RECHARGE);
		lap.setPay_channel(info.getContactChannle());
		lap.setCancel_tag(ROLLBACK_FLAG_NO);
		lap.setBalance_id(ipb.getBalance_id());
		lap.setUnit_type_id(unitTypeId);
		lap.setRece_fee(rai.getAmount());
		lap.setOld_all_balance(ipb.getBalance());
		lap.setNew_all_balance(ipb.getBalance() + rai.getAmount());
		lap.setOld_own_fee(0L);
		lap.setNew_own_fee(0L);
		lap.setCancel_charge_id(" ");
		lap.setLocal_net(ipb.getLocal_net());
		lap.setReserve_1(info.getUserEventCode());
		lap.setReserve_2(info.getJdAcctNbr());
		lap.setReserve_3(info.getCallBackUrl());
		lap.setReserve_4(rai.getRechargeType());
		lap.setReserve_5(info.getOperId());
		lap.setReserve_6(info.getGiftReason());
		return lap;
	}

	private QRechargeCallback assembleQRechargeCallback(
			UserInfoForMemCached userInfo, RechargeInfo info,
			String rechargeDetail, String payId, String payTime, int state) {
		QRechargeCallback qrc = new QRechargeCallback();
		qrc.setSerial_no(info.getSn());
		qrc.setDevice_number(info.getMSISDN());
		qrc.setUser_id(userInfo.getInfoUser().getUser_id());
		qrc.setPay_id(payId);
		qrc.setRecharge_detail(rechargeDetail);
		qrc.setCallbackurl(info.getCallBackUrl());
		qrc.setPay_time(payTime);
		qrc.setState(state);
		return qrc;
	}

	private HlpSmsSend assembleHlpSmsSend(RechargeInfo info, long totalAmount,
			long totalBalance, String payTime) {
		HlpSmsSend hss = new HlpSmsSend();
		hss.setMsg_id(S.get(Sequences.class)
				.queryFirst(Condition.build("queryMsgId")).getSeq());
		hss.setMsisdn_send(SMS_SEND_MSISDN);
		hss.setMsisdn_receive(info.getMSISDN());
		hss.setPriority(123);
		// assemble text
		StringBuilder sb = new StringBuilder("|aoc.dic.recharge|");
		sb.append(payTime.substring(8, 10));
		sb.append("|");
		sb.append(payTime.substring(11, 13));
		sb.append("|");
		sb.append(payTime.substring(14, 16));
		sb.append("|");
		sb.append(String.format("%.2f", totalAmount / 100.0f));
		sb.append("|");
		sb.append(String.format("%.2f", totalBalance / 100.0f));
		hss.setMessage_text(sb.toString());
		hss.setCreate_time(payTime);
		hss.setRetry_times(0);
		return hss;
	}

	private QUserReasonSend assembleQUserReasonSend(
			UserInfoForMemCached userInfo, RechargeInfo info, String payTime) {
		QUserReasonSend qurs = new QUserReasonSend();
		qurs.setSerial_num(S.get(Sequences.class)
				.queryFirst(Condition.build("queryQReasonSn")).getSeq());
		qurs.setUser_no(userInfo.getInfoUser().getUser_id());
		qurs.setTele_type(userInfo.getInfoUser().getTele_type());
		qurs.setReason_code(ACTIVE_REASON_RECHARGE);
		qurs.setEnqueue_date(payTime);
		qurs.setLocal_net(userInfo.getInfoUser().getLocal_net());
		qurs.setActive_type(ACTIVE_TYPE_RECHARGE);
		qurs.setCharge_id(info.getSn());
		return qurs;
	}

	private BilActAccesslog assembleBilActAccesslog(RechargeInfo info,
			RechargeInfoDto rai, InfoPayBalance ipb, String payId,
			String payTime) {
		BilActAccesslog baa = new BilActAccesslog();
		baa.setOperate_id(info.getSn());
		String operateType = null;
		RuleUserEventMapping ruem = userEventMapping.get(
				info.getUserEventCode(), "Recharge");
		if (ruem == null) {
			LOGGER.error("UserEventCode["
					+ info.getUserEventCode()
					+ "]Domain[Recharge] NOT FOUND In Table[Rule_User_Event_Mapping]");
			operateType = "1";
		} else {
			operateType = ruem.getBoss_type_code();
		}
		baa.setOperate_type(operateType);
		int partitionId = 0;
		try {
			partitionId = Integer.parseInt(payTime.substring(5, 7));
		} catch (Exception ex) {
			LOGGER.error("Recharge assembleBilActAccesslog Parse[Integer.parseInt('"
					+ payTime + "'.substring(4, 6))] to Int Failed!");
		}
		baa.setPartition_id(partitionId);
		baa.setPay_id(payId);
		baa.setBalance_id(ipb.getBalance_id());
		baa.setBalance_type_id(ipb.getBalance_type_id());
		baa.setAccess_tag(ACCESS_TAG_WITHDRAW);
		baa.setMoney(rai.getAmount());
		baa.setOld_balance(ipb.getBalance());
		baa.setNew_balance(ipb.getBalance() + rai.getAmount());
		baa.setLocal_net(ipb.getLocal_net());
		baa.setOperate_time(payTime);
		baa.setReserve_4(rai.getRechargeType());
		return baa;
	}

	private long calcAllBalance(List<InfoPayBalance> ipbs, long totalAmount)
			throws BasicException {
		long totalBalance = totalAmount;
		for (InfoPayBalance ipb : ipbs) {
			if (getUnitTypeId(ipb.getBalance_type_id()) == BasicType.UNIT_TYPE_MONEY) {
				totalBalance += (ipb.getReal_balance() - ipb.getUsed_balance());
			}
		}
		return totalBalance;
	}

	private void checkUserStatusYUXIAOHU(UserInfoForMemCached userInfo,
			RechargeResult result) throws Exception {
		if (userInfo != null) {
			InfoUser iu = userInfo.getInfoUser();
			if (iu != null) {
				if ((iu.getUser_status() != null)
						&& (iu.getUser_status()
								.equalsIgnoreCase(USER_STATUS_YUXIAOHU))) {
					LOGGER.error("Can NOT Recharge when ["
							+ iu.getDevice_number()
							+ "] UserStatus is YUXIAOHU!");
					result.setStatus(RESULT_STATUS_FAIL);
					result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
					result.setErrorMessage("用户[" + iu.getDevice_number()
							+ "]状态为预销户，不允许充值");
					throw new BasicException(result.getErrorCode(),
							result.getErrorMessage());
				} else {
					return;
				}
			}
		}
	}

	private Date assembleSQLDate(String s) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		java.util.Date date = sdf.parse(s);
		Date d = new Date(date.getTime());
		return d;
	}

	private void process(UserInfoForMemCached userInfo, RechargeInfo info,
			int unitTypeId, RechargeResult result) throws Exception {
		long totalAmount = 0L;
		long hisAsyncAmount = 0L;
		Map<Long, Long> asyncBalanceInfo = null;
		// added by tian@20150501 for: 预销户状态不允许充值
		checkUserStatusYUXIAOHU(userInfo, result);
		StringBuilder rechargeDetail = new StringBuilder("");
		String payId = getDefaultPayId(userInfo, result);
		List<InfoPayBalance> ipbs = S.get(InfoPayBalance.class).query(
				Condition.build("queryByPayId").filter("pay_id", payId));
		String payTime = QueryInfo.getDBSystemTimeIssue().getTime();
		// 按时间筛选有效账本，包含各种unit_type
		filterInactiveAccountBook(ipbs, payTime);
		// 查看是否异步缴费 0-正常，1-异步
		int openFlag = 0;
		// added by tian@20150119 for: 充值赠送时不发送短信
		boolean isGift = false;
		// added by tian@20150501 for: B值兑换话费不发短信，不触发充值回调
		boolean isBVExchange = false;
		List<CodeAcctMonth> cams = acctMonths
				.getByUseTags(BasicType.USE_TAG_ACTIVE);
		if ((cams != null) && (!cams.isEmpty())) {
			for (CodeAcctMonth cam : cams) {
				if (BasicType.ACT_TAG_OPEN.equals(cam.getAct_tag())) {
					LOGGER.info("Opening....SN[" + info.getSn()
							+ "]Enter Async Progress........");
					openFlag = 1;
					break;
				}
			}
		}
		if (openFlag != 0) {
			// 异步缴费记录
			List<InfoPayBalanceAsync> ipbas = S.get(InfoPayBalanceAsync.class)
					.query(Condition.build("getByPayId")
							.filter("pay_id", payId));
			if ((ipbas != null) && (!ipbas.isEmpty())) {
				// 整理异步缴费结果集
				asyncBalanceInfo = new HashMap<Long, Long>();
				for (InfoPayBalanceAsync ipba : ipbas) {
					hisAsyncAmount += ipba.getBalance();
					Long temp = asyncBalanceInfo.get(ipba.getBalance_id());
					if (temp != null) {
						asyncBalanceInfo.put(ipba.getBalance_id(),
								temp + ipba.getBalance());
					} else {
						asyncBalanceInfo.put(ipba.getBalance_id(),
								ipba.getBalance());
					}
				}
				// 将异步缴费结果与账本合并
				for (InfoPayBalance ipb : ipbs) {
					Long temp = asyncBalanceInfo.get(ipb.getBalance_id());
					if (temp != null) {
						ipb.setBalance(temp + ipb.getBalance());
					}
				}
			}
		}
		List<RechargeInfoDto> rais = info.getRechargeInfoDtoList();
		for (RechargeInfoDto rai : rais) {
			// added by tian@20150119 for: 充值赠送时不发送短信
			if (rai.getRechargeType().trim()
					.equalsIgnoreCase(RECHARGE_TYPE_PROM)) {
				isGift = true;
			} else if (rai.getRechargeType().trim().toUpperCase()
					.startsWith(RECHARGE_TYPE_BV_EXCHANGE)) {
				isBVExchange = true;
			}
			if (rai.getAmount() < 0L) {
				continue;
			}
			int balanceTypeId = getBalanceTypeId(rai.getRechargeType(), result);
			int iUnitTypeId = getUnitTypeId(balanceTypeId);
			if (iUnitTypeId != unitTypeId) {
				LOGGER.error("Only [" + unitTypeId
						+ "] Supported!BalanceTypeId[" + balanceTypeId
						+ "]UnitTypeId[" + iUnitTypeId
						+ "] Can't be Processed!");
				result.setStatus(RESULT_STATUS_FAIL);
				result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				result.setErrorMessage("充值类型对应的账本类型的资源类型[" + iUnitTypeId
						+ "]暂不支持");
				throw new BasicException(result.getErrorCode(),
						result.getErrorMessage());
			}
			InfoPayBalance ipb = getMatchedAccountBook(ipbs, balanceTypeId,
					unitTypeId, rai.getEffDate(), rai.getExpDate());
			if (ipb == null) {
				int ret = -1;
				if (isBVExchange) {
					Date effDate = null;
					Date expDate = null;
					try {
						effDate = assembleSQLDate(rai.getEffDate());
						expDate = assembleSQLDate(rai.getExpDate());
					} catch (ParseException ex) {
						LOGGER.error("Input EffDate[" + rai.getEffDate()
								+ "] or ExpDate[" + rai.getExpDate()
								+ "] String-Parse-to-Date Exception!");
						result.setStatus(RESULT_STATUS_FAIL);
						result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
						result.setErrorMessage("入参 EffDate[" + rai.getEffDate()
								+ "]或ExpDate[" + rai.getExpDate()
								+ "] String-Parse-to-Date 异常!");
						throw new BasicException(result.getErrorCode(),
								result.getErrorMessage());
					}
					LOGGER.debug("Create New AcctBook EffDate[" + effDate
							+ "]ExpDate[" + expDate + "]");
					ret = createBook.create(userInfo.getInfoUser()
							.getLocal_net(), payId, balanceTypeId, effDate,
							expDate);
				} else {
					ret = createBook.create(userInfo.getInfoUser()
							.getLocal_net(), payId, balanceTypeId, new Date(
							System.currentTimeMillis()), new Date(
							2556097843923L));
				}
				if (ret == 0) {
					// modified by tian@20150122 for: JIRA[JD-683]
					List<InfoPayBalance> ipbs_temp = S
							.get(InfoPayBalance.class).query(
									Condition.build("queryByPayId").filter(
											"pay_id", payId));
					ipb = getMatchedAccountBook(ipbs_temp, balanceTypeId,
							unitTypeId, rai.getEffDate(), rai.getExpDate());
					ipbs.add(ipb);
				} else {
					result.setStatus(RESULT_STATUS_FAIL);
					result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
					result.setErrorMessage("创建账本异常");
					LOGGER.error("Create Account Book for PayId[" + payId
							+ "] Failed!");
					throw new BasicException(result.getErrorCode(),
							result.getErrorMessage());
				}
			}
			// 增量更新余额表
			if (openFlag == 0) {
				S.get(InfoPayBalance.class).batch(
						Condition.build("update4Recharge").filter("balance_id",
								ipb.getBalance_id()),
						assembleInfoPayBalance(ipb.getBalance_id(),
								rai.getAmount()));
			} else {
				S.get(InfoPayBalanceAsync.class).create(
						assembleInfoPayBalanceAsync(info.getSn(), rai, ipb));
			}
			// 记录缴费日志
			S.get(LogActPay.class).create(
					assembleLogActPay(userInfo, info, rai, ipb, payId,
							unitTypeId, payTime));
			// 记录存取款日志
			S.get(BilActAccesslog.class).create(
					assembleBilActAccesslog(info, rai, ipb, payId, payTime));
			rechargeDetail.append(ipb.getBalance_id());
			rechargeDetail.append(":");
			rechargeDetail.append(rai.getAmount());
			rechargeDetail.append(":");
			rechargeDetail.append(rai.getRechargeType());
			rechargeDetail.append(";");
			if (iUnitTypeId == BasicType.UNIT_TYPE_MONEY) {
				totalAmount += rai.getAmount();
			}
			// added by tian@20141216 for: 一次充值多个类型对应一个账本时，账本的new_old余额平滑向上
			ipb.setBalance(ipb.getBalance() + rai.getAmount());
		}
		// 记录Q_Recharge_Callback
		if (!isBVExchange) {
			if (openFlag == 0) {
				S.get(QRechargeCallback.class).create(
						assembleQRechargeCallback(userInfo, info,
								rechargeDetail.toString(), payId, payTime,
								Q_RECHARGE_CALLBACK_STATE_INIT));
			} else {
				S.get(QRechargeCallback.class).create(
						assembleQRechargeCallback(userInfo, info,
								rechargeDetail.toString(), payId, payTime,
								Q_RECHARGE_CALLBACK_STATE_ASYNC_INIT));
			}
		}
		
		//add by wangtao begin 
		//:当判断 号码为“110”开头的号码并通过md5转换后的字符串后
		//（11位号码通过md5方法转换后，结果类似1bc479cf81b6bd2d8c74d1eb03903c13，长度一定会大于11位），则不发送短信。
		String device_number = userInfo.getDevice_number();
		if(device_number.length()>11){
			isGift = true;
		}
		//add by wangtao end
		
		// 记录HLP_SMS_SEND
		if ((totalAmount > 0) && (!isGift) && (!isBVExchange)) {
			if (openFlag == 0) {
				S.get(HlpSmsSend.class).create(
						assembleHlpSmsSend(info, totalAmount,
								calcAllBalance(ipbs, totalAmount), payTime));
			} else {
				S.get(HlpSmsSend.class).create(
						assembleHlpSmsSend(
								info,
								totalAmount,
								calcAllBalance(ipbs, totalAmount
										+ hisAsyncAmount), payTime));
			}
		}
		// 记录Q_USER_REASON_SEND
		if ((!info.getUserEventCode().equals(USER_EVENT_CODE_OPEN))
				&& (!isBVExchange)) {
			if (userInfo.getInfoUser().getUser_status().trim()
					.equals(USER_STATUS_INACTIVE)) {
				S.get(QUserReasonSend.class).create(
						assembleQUserReasonSend(userInfo, info, payTime));
			}
		}
	}

	private void filterInactiveAccountBook(List<InfoPayBalance> ipbs,
			String payTime) {
		if ((ipbs != null) && (!ipbs.isEmpty())) {
			String currentDate = payTime.substring(0, 10);
			for (Iterator<InfoPayBalance> iter = ipbs.iterator(); iter
					.hasNext();) {
				InfoPayBalance ipb = iter.next();
				if ((ipb.getEff_date().toString().compareTo(currentDate) > 0)
						|| (ipb.getExp_date().toString().compareTo(currentDate) < 0)) {
					iter.remove();
				}
			}
		}
	}

	private void reset(RechargeResult result) throws Exception {
		if (flag4firstrun == 0) {
			balanceTypes.load();
			rechargeTypeMapping.load();
			userEventMapping.load();
			acctMonths.load();
			flag4firstrun = 1;
		}
		result.setStatus(RESULT_STATUS_OK);
		result.setErrorCode(null);
		result.setErrorMessage(null);
	}

	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) {
		LogRefreshTrigger lrt = null;
		try {
			if (BasicType.DS_RULE_RECHARGE_TYPE_MAPPING.equals(datastoreName)) {
				rechargeTypeMapping.refresh();
			} else if (BasicType.DS_RULE_USER_EVENT_MAPPING
					.equals(datastoreName)) {
				userEventMapping.refresh();
			} else if (BasicType.DS_CODE_BIL_BALANCE_TYPE.equals(datastoreName)) {
				balanceTypes.refresh();
			} else if (BasicType.DS_CODE_ACCT_MONTH.equals(datastoreName)) {
				acctMonths.refresh();
			} else {
				LOGGER.warn("Unknown Datastore Name[" + datastoreName + "]!");
				lrt = RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
						datastoreName, serviceName,
						BasicType.REFRESH_STATUS_FAIL,
						"Unknown Datastore Name[" + datastoreName + "]!");
			}
			lrt = RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
					datastoreName, serviceName, BasicType.REFRESH_STATUS_OK,
					null);
		} catch (BasicException ex) {
			LOGGER.error(ex.getMessage());
			lrt = RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
					datastoreName, serviceName, BasicType.REFRESH_STATUS_FAIL,
					ex.getMessage());
		} finally {
			try {
				S.get(LogRefreshTrigger.class).create(lrt);
			} catch (Exception ex) {
				LOGGER.error("Insert LogRefreshTrigger Failed!["
						+ ex.getMessage() + "]");
			}
		}
	}

	/**
	 * 145号码充值  JD-1944
	 * @param info
	 * @return
	 */
	public RechargeResult recharge145(RechargeInfo info, String url){
		
		RechargeResult result = new RechargeResult();
		result.setSn(info.getSn());
		httpRecharge(info, result, url);
		LOGGER.debug("recharge145 end"+result);
		return result;
	}
	
	/**
	 * 调用飞猫充值服务 JD-1944
	 * @param info
	 * @param result
	 */
	private void httpRecharge(RechargeInfo info, RechargeResult result, String url){
		
		LOGGER.debug("httpRecharge URL[" + url + "]");
		JSONObject params = JSONObject.fromObject(info);
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		client.getHttpConnectionManager().getParams().setSoTimeout(5000);
		try{
			LOGGER.debug("Request JSON[" + params + "]");
			LOGGER.debug("Request JSONMD5[" + convertMD5(params.toString()) + "]");
			
			PostMethod post = new PostMethod(url);
			post.setRequestHeader("Accept", "application/json");
			post.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			RequestEntity requestEntity = new StringRequestEntity(convertMD5(params.toString()),"application/json","UTF-8");
			post.setRequestEntity(requestEntity);
			
			client.executeMethod(post);
			
			LOGGER.debug("Request URL[" + url + "] StatusLine[" + post.getStatusCode() + "]");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), "utf-8"));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = reader.readLine()) != null) {
				stringBuffer.append(str);
			}
			LOGGER.debug("调用飞猫充值平台返回:"+stringBuffer);
			int code = post.getStatusCode();
			if (code == HttpStatus.SC_OK) {
				result.setStatus(RESULT_STATUS_OK);
			}else{
				LOGGER.error("调用飞猫充值平台返回失败  httpStatusCode="+code);
				result.setStatus(RESULT_STATUS_FAIL);
				result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				result.setErrorMessage("调用飞猫充值平台失败"+stringBuffer);
			}
			post.releaseConnection();
		}catch(Exception e){
			LOGGER.error(info.getMSISDN()+"调用飞猫充值平台失败,异常信息为:"+e.getMessage());
			result.setStatus(RESULT_STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("调用飞猫充值平台失败");
		}
		
	}
	
	/**
	 * 飞猫充值接口  JD-1944
	 * 请求参数整体 MD5(32位) 加密
	 * @param s
	 * @return
	 */
	public static String makeMD5(String s) {
		String result = null;
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(s.getBytes("UTF-8"));
			byte b[] = md5.digest(); // 128位2进制, 8*16
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256; // 高位2字节清0
				if (i < 16)
					buf.append("0"); // 低位1字节补0
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			LOGGER.debug("After Sign Encryption[" + result + "]");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("makeMD5:NoSuchAlgorithmException e!!!");
			LOGGER.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("makeMD5:UnsupportedEncodingException e!!!");
			LOGGER.error(e.getMessage());
		}
		return result;
	}
	
	/** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    }  
    
	public DSAcctMonth getAcctMonths() {
		return acctMonths;
	}

	public void setAcctMonths(DSAcctMonth acctMonths) {
		this.acctMonths = acctMonths;
	}

	public DSBalanceType getBalanceTypes() {
		return balanceTypes;
	}

	public void setBalanceTypes(DSBalanceType balanceTypes) {
		this.balanceTypes = balanceTypes;
	}

	public DSRechargeTypeMapping getRechargeTypeMapping() {
		return rechargeTypeMapping;
	}

	public void setRechargeTypeMapping(DSRechargeTypeMapping rechargeTypeMapping) {
		this.rechargeTypeMapping = rechargeTypeMapping;
	}

	public CreateAccountBook getCreateBook() {
		return createBook;
	}

	public void setCreateBook(CreateAccountBook createBook) {
		this.createBook = createBook;
	}

	public DSUserEventMapping getUserEventMapping() {
		return userEventMapping;
	}

	public void setUserEventMapping(DSUserEventMapping userEventMapping) {
		this.userEventMapping = userEventMapping;
	}

}
