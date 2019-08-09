package com.tydic.beijing.billing.account.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dto.BalanceChangeInfo;
import com.tydic.beijing.billing.dto.BalanceChangeRequest;
import com.tydic.beijing.billing.dto.BalanceChangeResponse;
import com.tydic.beijing.billing.dto.BalanceChangeSnapshot;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class BalanceChangeOps {
	private final static Logger LOGGER = Logger
			.getLogger(BalanceChangeOps.class);

	// move out to com.tydic.beijing.billing.common----------------------------
	public final static String STATUS_OK = "0";
	public final static String STATUS_FAIL = "1";

	public final static String CHANGE_TYPE_ADD = "0";
	public final static String CHANGE_TYPE_MINUS = "1";

	public final static String ERR_CODE_PARAMETER_EMPTY = "10001";
	public final static String ERR_CODE_PAYID_EMPTY = "10002";
	public final static String ERR_CODE_SERIALNO_EMPTY = "10003";
	public final static String ERR_CODE_OPERTYPE_EMPTY = "10004";
	public final static String ERR_CODE_OPERCHANNEL_EMPTY = "10005";
	public final static String ERR_CODE_OPERTIME_EMPTY = "10006";
	public final static String ERR_CODE_BALANCECHANGEINFO_EMPTY = "10007";
	public final static String ERR_CODE_CHANGETYPE_EMPTY = "10008";

	public final static String ERR_CODE_BALANCEID_NOTEXIST = "20001";
	public final static String ERR_CODE_BALANCETYPEID_MISMATCH = "20002";
	public final static String ERR_CODE_CHANGETYPE_UNKNOWN = "20003";
	public final static String ERR_CODE_BALANCE_NOTENOUGH = "20004";

	public final static String ERR_MSG_PARAMETER_EMPTY = "入参为空";
	public final static String ERR_MSG_PAYID_EMPTY = "账户标识为空";
	public final static String ERR_MSG_SERIALNO_EMPTY = "流水号为空";
	public final static String ERR_MSG_OPERTYPE_EMPTY = "操作类型为空";
	public final static String ERR_MSG_OPERCHANNEL_EMPTY = "接触渠道为空";
	public final static String ERR_MSG_OPERTIME_EMPTY = "操作时间为空";
	public final static String ERR_MSG_BALANCECHANGEINFO_EMPTY = "余额变动信息为空";
	public final static String ERR_MSG_CHANGETYPE_EMPTY = "余额变动类型为空";

	public final static String ERR_MSG_BALANCEID_NOTEXIST = "账本标识不存在";
	public final static String ERR_MSG_BALANCETYPEID_MISMATCH = "账本类型不匹配";
	public final static String ERR_MSG_CHANGETYPE_UNKNOWN = "余额变动类型不识别";
	public final static String ERR_MSG_BALANCE_NOTENOUGH = "余额不足";
	// -------------------------------------------------------------------------

	private final static int DATE_LENGTH = 14;

	public BalanceChangeResponse doProcess(BalanceChangeRequest request,
			boolean changeRealBalanceOnly, boolean canOverUsed) throws Exception, BasicException {
		BalanceChangeResponse response = new BalanceChangeResponse();
		resetBalanceChangeResponse(response);
		checkInputParameters(request, response);
		if (STATUS_FAIL.equalsIgnoreCase(response.getStatus())) {
			return response;
		}
		List<BalanceChangeInfo> balanceChangeInfo = request
				.getBalanceChangeInfo();
		List<BalanceChangeSnapshot> bcSnapshots = new ArrayList<BalanceChangeSnapshot>();
		for (BalanceChangeInfo info : balanceChangeInfo) {
			// select * from info_pay_balance where balance_id = ?
			InfoPayBalance ipb = S.get(InfoPayBalance.class).get(
					info.getBalanceId());
			if (ipb == null) {
				throw new BasicException(ERR_CODE_BALANCEID_NOTEXIST,
						ERR_MSG_BALANCEID_NOTEXIST);
			}
			if (ipb.getBalance_type_id() != info.getBalanceTypeId()) {
				throw new BasicException(ERR_CODE_BALANCETYPEID_MISMATCH,
						ERR_MSG_BALANCETYPEID_MISMATCH);
			}
			String changeType = info.getChangeType();
			long changeValue = 0L;
			long afterChangeBalance = 0L;
			if (changeType == null) {
				throw new BasicException(ERR_CODE_CHANGETYPE_EMPTY,
						ERR_MSG_CHANGETYPE_EMPTY);
			} else if (changeType.equalsIgnoreCase(CHANGE_TYPE_ADD)) {
				changeValue = info.getChangeValue();
			} else if (changeType.equalsIgnoreCase(CHANGE_TYPE_MINUS)) {
				if ((canOverUsed) || (ipb.getReal_balance() >= info.getChangeValue())) {
					changeValue = -info.getChangeValue();
				} else {
					LOGGER.info("Ask for [" + changeValue + "], but BalanceId["
							+ info.getBalanceId() + "] only have["
							+ ipb.getReal_balance() + "]!");
					throw new BasicException(ERR_CODE_BALANCE_NOTENOUGH,
							ERR_MSG_BALANCE_NOTENOUGH);
				}
			} else {
				throw new BasicException(ERR_CODE_CHANGETYPE_UNKNOWN,
						ERR_MSG_CHANGETYPE_UNKNOWN);
			}
			BalanceChangeSnapshot snapshot = new BalanceChangeSnapshot();
			InfoPayBalance updateIpb = new InfoPayBalance();
			updateIpb.setBalance_id(info.getBalanceId());
			if (changeRealBalanceOnly) {
				updateIpb.setBalance(0);
				updateIpb.setReal_balance(changeValue);
				afterChangeBalance = ipb.getBalance();
			} else {
				updateIpb.setBalance(changeValue);
				updateIpb.setReal_balance(changeValue);
				afterChangeBalance = ipb.getBalance() + changeValue;
			}
			// update info_pay_balance set balance = balance + ?, real_balance =
			// real_balance + ? where balance_id = ?
			S.get(InfoPayBalance.class).batch(
					Condition.build("update4Recharge").filter("balance_id",
							info.getBalanceId()), updateIpb);
			fillBalanceChangeSnapshot(snapshot, info, ipb.getBalance(),
					afterChangeBalance);
			bcSnapshots.add(snapshot);
			// insert into bil_act_accesslog ...
			S.get(BilActAccesslog.class).create(
					assembleBilActAccesslog(request, info, ipb,
							afterChangeBalance));
		}
		response.setBalanceChangeSnapshot(bcSnapshots);
		return response;
	}

	private BilActAccesslog assembleBilActAccesslog(
			BalanceChangeRequest request, BalanceChangeInfo info,
			InfoPayBalance ipb, long afterChangeBalance) {
		BilActAccesslog baa = new BilActAccesslog();
		baa.setOperate_id(request.getSerialNo());
		baa.setOperate_type(request.getOperType());
		String payTime = QueryInfo.getDBSystemTimeIssue().getTime();
		int partitionId = 0;
		try {
			partitionId = Integer.parseInt(payTime.substring(5, 7));
		} catch (Exception ex) {
			LOGGER.error("BalanceChangeOps assembleBilActAccesslog Parse[Integer.parseInt('"
					+ payTime + "'.substring(4, 6))] to Int Failed!");
		}
		baa.setPartition_id(partitionId);
		baa.setPay_id(request.getPayId());
		baa.setBalance_id(info.getBalanceId());
		baa.setBalance_type_id(info.getBalanceTypeId());
		baa.setAccess_tag(info.getChangeType());
		baa.setMoney(info.getChangeValue());
		baa.setOld_balance(ipb.getBalance());
		baa.setNew_balance(afterChangeBalance);
		baa.setLocal_net(ipb.getLocal_net());
		baa.setOperate_time(payTime);
		baa.setReserve_1(request.getOuterSerialNo()); // 外部流水
		baa.setReserve_2(request.getSystemId()); // 接入系统标识
		baa.setReserve_3(request.getOperStaff()); // 操作员
		// baa.setReserve_4(null); JD系统使用了该字段，所以不建议使用
		return baa;
	}

	private void checkInputParameters(BalanceChangeRequest request,
			BalanceChangeResponse response) {
		if (request == null) {
			fillBalanceChangeResponse(response, STATUS_FAIL,
					ERR_CODE_PARAMETER_EMPTY, ERR_MSG_PARAMETER_EMPTY, null);
		} else if ((request.getPayId() == null)
				|| (request.getPayId().isEmpty())) {
			fillBalanceChangeResponse(response, STATUS_FAIL,
					ERR_CODE_PAYID_EMPTY, ERR_MSG_PAYID_EMPTY, null);
		} else if ((request.getSerialNo() == null)
				|| (request.getSerialNo().isEmpty())) {
			fillBalanceChangeResponse(response, STATUS_FAIL,
					ERR_CODE_SERIALNO_EMPTY, ERR_MSG_SERIALNO_EMPTY, null);
		} else if ((request.getOperType() == null)
				|| (request.getOperType().isEmpty())) {
			fillBalanceChangeResponse(response, STATUS_FAIL,
					ERR_CODE_OPERTYPE_EMPTY, ERR_MSG_OPERTYPE_EMPTY, null);
		} else if ((request.getBalanceChangeInfo() == null)
				|| (request.getBalanceChangeInfo().isEmpty())) {
			fillBalanceChangeResponse(response, STATUS_FAIL,
					ERR_CODE_BALANCECHANGEINFO_EMPTY,
					ERR_MSG_BALANCECHANGEINFO_EMPTY, null);
		} else if ((request.getOperTime() == null)
				|| (request.getOperTime().isEmpty())
				|| (request.getOperTime().length() != DATE_LENGTH)) {
			fillBalanceChangeResponse(response, STATUS_FAIL,
					ERR_CODE_OPERTIME_EMPTY, ERR_MSG_OPERTIME_EMPTY, null);
		}
	}

	private void fillBalanceChangeSnapshot(BalanceChangeSnapshot snapshot,
			BalanceChangeInfo info, long beforeBalance, long afterBalance) {
		snapshot.setBalanceId(info.getBalanceId());
		snapshot.setBalanceTypeId(info.getBalanceTypeId());
		snapshot.setChangeType(info.getChangeType());
		snapshot.setChangeValue(info.getChangeValue());
		snapshot.setBeforeChangeBalance(beforeBalance);
		snapshot.setAfterChangeBalance(afterBalance);
		snapshot.setEffDate(info.getEffDate());
		snapshot.setExpDate(info.getExpDate());
	}

	private void fillBalanceChangeResponse(BalanceChangeResponse response,
			String status, String resultCode, String resultMessage,
			List<BalanceChangeSnapshot> balanceChangeSnapshot) {
		response.setStatus(status);
		response.setResultCode(resultCode);
		response.setResultMessage(resultMessage);
		response.setBalanceChangeSnapshot(balanceChangeSnapshot);
		if (STATUS_OK.equalsIgnoreCase(response.getStatus())) {
			LOGGER.debug(response.toString());
		} else {
			LOGGER.error(response.toString());
		}
	}

	private void resetBalanceChangeResponse(BalanceChangeResponse response) {
		fillBalanceChangeResponse(response, STATUS_OK, null, null, null);
	}
}
