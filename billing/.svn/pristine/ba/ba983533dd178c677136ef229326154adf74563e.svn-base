/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.billing.outerf.busi;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.account.datastore.DSAcctMonth;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActBill;
import com.tydic.beijing.billing.dao.BilActRealTimeBillForOracle;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoPayBalanceAsync;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserReleaseCal;
import com.tydic.beijing.billing.dao.LogRefreshTrigger;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.beijing.billing.dto.AmountDto;
import com.tydic.beijing.billing.dto.QuerySubsAcctBalanceRequest;
import com.tydic.beijing.billing.dto.QuerySubsAcctBalanceResponse;
import com.tydic.beijing.billing.outerf.architecture.Disposable;
import com.tydic.beijing.billing.outerf.architecture.LogAble;
import com.tydic.beijing.billing.util.RecordAssembler;
import com.tydic.uda.service.S;

/**
 *
 * @author wangshida 1. 获取USER_ID SELECT USER_ID FROM INFO_USER WHERE
 *         DEVICE_NUMBER=?
 * 
 *         2. 获取PAY_ID SELECT PAY_ID FROM LIFE_USER_PAY WHERE USER_ID=?
 * 
 *         3. 获取余额 UNIT_TYPE_ID=0 Amount加起来 注意！BALANCE_TYPE_ID =
 *         0的UNIT_TYPE_ID=0!!!!!!!!!!
 * 
 *         4. 获取欠费？冻结金额 SELECT OWE_FEE AS '欠费' FROM BIL_ACT_BILL WHERE
 *         USER_ID=?，累加
 * 
 *         SELECT AMOUNT FROM TABLE TB_BIL_PRESENT_USER_INFO WHERE USER_ID=?，累加
 * 
 *         5. 获取余额分项列表 SELECT REAL_BALANCE AS '余额', BALANCE_TYPE_ID AS '余额类型
 *         1:默认现金 2:不可退现金 3:赠费 4:白条' FROM INFO_PAY_BALANCE WHERE PAY_ID=?
 *         BALANCE_TYPE_ID = 0，1默认现金
 * 
 *         CODE_BIL_BALANCE_TYPE：UNIT_TYPE_ID=0，REFUND_FLAG=0，2不可退
 * 
 *         CODE_BIL_BALANCE_TYPE：UNIT_TYPE_ID=0，PRE_FLAG=1，3曾肥
 * 
 *         白条没有
 */
public class QuerySubsAcctBalanceBusi extends LogAble implements Disposable {
	private static final String STATUS_OK = "1";
	private static final String STATUS_FAIL = "0";

	private int flag4firstrun = 0;
	private DSAcctMonth acctMonths;
	private DataOps ops;

	public QuerySubsAcctBalanceBusi() {
		super();
	}

	public String getUserId(String device_number) {
		InfoUser iu = DataCenter.getInfoUserByDeviceNumber(device_number);
		if (iu != null) {
			return iu.getUser_id();
		} else {
			return null;
		}
	}

	public String getPayId(String user_id) {
		List<PayUserRel> purs = DataCenter.getPayUserRelList(user_id);
		if (purs == null) {
			return null;
		} else {
			for (PayUserRel pur : purs) {
				if (pur.getDefault_tag().trim().equals("0")) {
					return pur.getPay_id();
				}
			}
			logger.error("UserId[" + user_id + "] DO NOT HAVE A Default PayId");
			return null;
		}
	}

	public long spiltInfoPayBalance(List<InfoPayBalance> input,
			List<InfoPayBalance> defaultCashList,
			List<InfoPayBalance> cannotPayBackList,
			List<InfoPayBalance> prescentList, List<InfoPayBalance> baiTiaoList) {
		List<CodeBilBalanceType> typelist = DataCenter
				.getCodeBilBalanceTypeAll();
		Map<Integer, CodeBilBalanceType> map = new HashMap<Integer, CodeBilBalanceType>();
		for (CodeBilBalanceType cbbt : typelist) {
			map.put(cbbt.getBalance_type_id(), cbbt);
		}
		List<RuleParameters> rps = DataCenter.getBaiTiaoBalanceTypeId();
		long RemainAmount = 0L;
		Date date = new Date(System.currentTimeMillis());
		String strDate = date.toString();
		for (InfoPayBalance e : input) {
			String effDate = e.getEff_date().toString();
			String expDate = e.getExp_date().toString();
			if ((effDate.compareTo(strDate) <= 0)
					&& (expDate.compareTo(strDate) >= 0)) {
				CodeBilBalanceType cbbt = map.get(e.getBalance_type_id());
				if (cbbt == null) {
					logger.error("PayId[" + e.getPay_id()
							+ "]AccountBook BalanceTypeId["
							+ e.getBalance_type_id() + "] NOT FOUND!");
					continue;
				}
				if (cbbt.getUnit_type_id() != BasicType.UNIT_TYPE_MONEY) {
					logger.info("PayId[" + e.getPay_id()
							+ "]AccountBook BalanceId[" + e.getBalance_id()
							+ "]UnitTypeId[" + cbbt.getUnit_type_id() + "]!");
					continue;
				}
				if (e.getBalance_type_id() == 1 || e.getBalance_type_id()==28) {
					defaultCashList.add(e);
					RemainAmount += e.getReal_balance();
				} else {
					if (isBaiTiaoAcctBook(rps, cbbt.getBalance_type_id())) {
						baiTiaoList.add(e);
					} else if ((cbbt.getPre_flag() != null)
							&& (cbbt.getPre_flag().trim().equals("1"))) {
						prescentList.add(e);
					} else if ((cbbt.getRefund_flag() != null)
							&& (cbbt.getRefund_flag().trim().equals("0"))) {
						cannotPayBackList.add(e);
					}
					RemainAmount += e.getReal_balance();
				}
			}
		}
		return RemainAmount;
	}

	private boolean isBaiTiaoAcctBook(List<RuleParameters> rps,
			int balanceTypeId) {
		if ((rps == null) || (rps.isEmpty())) {
			logger.error("getBaiTiaoBalanceTypeId Failed, Set 3 as Default!");
			if (balanceTypeId == 3) {
				return true;
			}
		} else {
			for (RuleParameters rp : rps) {
				if (rp.getPara_num1() == balanceTypeId) {
					return true;
				}
			}
		}
		return false;
	}

	public void addAmountDto(List<AmountDto> l, Long Amounttype,
			List<InfoPayBalance> balancelist) {
		if ((balancelist != null) && (!balancelist.isEmpty())) {
			long realBalances = 0L;
			for (InfoPayBalance e : balancelist) {
				realBalances += e.getReal_balance();
			}
			l.add(new AmountDto(Amounttype, realBalances));
		}
	}

	public boolean dealBalance(QuerySubsAcctBalanceResponse response,
			String user_id, String pay_id, Long OweAmount) {
		List<InfoPayBalance> ipbs = DataCenter.getInfoPayBalanceByPayId(pay_id);
		List<CodeAcctMonth> cams = acctMonths
				.getByUseTags(BasicType.USE_TAG_ACTIVE);
		if ((cams != null) && (!cams.isEmpty())) {
			for (CodeAcctMonth cam : cams) {
				if (BasicType.ACT_TAG_OPEN.equals(cam.getAct_tag())) {
					logger.info("Opening...Should Calc Async Recharge Parts!");
					List<InfoPayBalanceAsync> ipbas = DataCenter
							.getInfoPayBalanceAsyncByPayId(pay_id);
					if ((ipbas != null) && (!ipbas.isEmpty()) && (ipbs != null)) {
						for (InfoPayBalanceAsync ipba : ipbas) {
							for (InfoPayBalance ipb : ipbs) {
								if (ipba.getBalance_id() == ipb.getBalance_id()) {
									ipb.setBalance(ipba.getBalance()
											+ ipb.getBalance());
									ipb.setReal_balance(ipba.getBalance()
											+ ipb.getReal_balance());
									break;
								}
							}
						}
					}
					break;
				}
			}
		}
		if ((ipbs == null) || (ipbs.isEmpty())) {
			logger.error("UserId[" + user_id + "] Default PayId[" + pay_id
					+ "] AccountBook NOT FOUND!");
			return false;
		}
		List<InfoPayBalance> defaultCashList = new ArrayList<>();
		List<InfoPayBalance> cannotPayBackList = new ArrayList<>();
		List<InfoPayBalance> prescentList = new ArrayList<>();
		List<InfoPayBalance> baiTiaoList = new ArrayList<>();
		Long RemainAmount = 0L;
		RemainAmount = spiltInfoPayBalance(ipbs, defaultCashList,
				cannotPayBackList, prescentList, baiTiaoList);
		response.setRemainAmount(RemainAmount - OweAmount);
		List<AmountDto> l = new ArrayList<>();
		this.addAmountDto(l, 1L, defaultCashList); // 1:默认现金
		this.addAmountDto(l, 2L, cannotPayBackList); // 2:不可退现金
		this.addAmountDto(l, 3L, prescentList); // 3:赠费
		this.addAmountDto(l, 4L, baiTiaoList); // 4:白条
		response.setAmountDtoList(l);
		return true;
	}

	public Long calcOweAmount(String user_id, String pay_id) {
		List<BilActBill> babs = DataCenter.getBilActBillByUserId(user_id);
		if (babs == null) {
			return 0L;
		}
		Long sum = 0L;
		for (BilActBill bab : babs) {
			sum += bab.getOwe_fee();
		}
		logger.debug("UserId[" + user_id + "] History Owe Fee[" + sum + "]");
		List<CodeAcctMonth> cams = acctMonths
				.getByUseTags(BasicType.USE_TAG_ACTIVE);
		if ((cams == null) || (cams.isEmpty())) {
			logger.error("Can't Find Acitve AcctMonth");
		} else {
			for (CodeAcctMonth cam : cams) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM");
				int acctMonth = cam.getAcct_month();
				logger.debug("Loading AcctMonth[" + acctMonth
						+ "]Realtime Bills...");
				String currentMonth = sdf.format(cam.getAct_eff_date());
				List<BilActRealTimeBillForOracle> brts = DataCenter
						.getRealtimeBill(acctMonth, currentMonth, user_id,
								pay_id);
				if ((brts != null) && (!brts.isEmpty())) {
					for (BilActRealTimeBillForOracle brt : brts) {
						sum += brt.getNon_deduct_fee();
					}
				}
			}
		}
		logger.debug("UserId[" + user_id + "] Total Owe Fee[" + sum + "]");
		return sum;
	}

	public Long calcFreezeAmount(String user_id) {
		// List<TbBilPresentUserInfo> l = DataCenter
		// .getTbBilPresentUserInfoByUserId(user_id);
		// if (l == null) {
		// return 0L;
		// }
		// Long sum = 0L;
		// Date date = new Date(System.currentTimeMillis());
		// for (TbBilPresentUserInfo e : l) {
		// if (((e.getEff_date().compareTo(date) <= 0) && (e.getExp_date()
		// .compareTo(date) >= 0))) {
		// sum += e.getValue_left();
		// }
		// }

		// 模型修改，改为查询LIFE_USER_RELEASE_CAL
		List<LifeUserReleaseCal> l = DataCenter
				.getLifeUserReleaseCalByUserId(user_id);
		if (l == null) {
			return 0L;
		}
		Long sum = 0L;

		for (LifeUserReleaseCal e : l) {
			sum += e.getBalance();

		}

		return sum;
	}

	@Override
	public Object dispose(Object input) {
		long beginProcess = System.currentTimeMillis();
		QuerySubsAcctBalanceRequest request = (QuerySubsAcctBalanceRequest) input;
		logger.debug(String.format("dispose() request: %s", request));
		QuerySubsAcctBalanceResponse response = new QuerySubsAcctBalanceResponse();
		if (flag4firstrun == 0) {
			try {
				acctMonths.load();
				flag4firstrun = 1;
			} catch (Exception e) {
				logger.error("Loading Table[CODE_ACCT_MONTH] Failed!["
						+ e.getMessage() + "]");
				response.setStatus(STATUS_FAIL);
				response.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				response.setErrorMessage("余额查询号码[" + request.getMSISDN()
						+ "]加载帐期错误");
				return response;
			}
		}
		boolean ret = checkInputParameters(request);
		if (ret) {
			String user_id = null;
			String pay_id = null;
			UserInfoForMemCached userInfo = S.get(UserInfoForMemCached.class)
					.get(request.getMSISDN());
			if (userInfo == null) {
				logger.info("QuerySubsAcctBalance Input MSISDN["
						+ request.getMSISDN() + "] Scratch from DB!");
				user_id = getUserId(request.getMSISDN());
				if (user_id == null) {
					logger.error("QuerySubsAcctBalance Input MSISDN["
							+ request.getMSISDN() + "] NOT FOUND!");
					response.setStatus(STATUS_FAIL);
					response.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
					response.setErrorMessage("余额查询号码[" + request.getMSISDN()
							+ "]未找到");
					return response;
				}
				pay_id = this.getPayId(user_id);
			} else {
				InfoUser iu = userInfo.getInfoUser();
				if (iu != null) {
					user_id = iu.getUser_id();
				}
				List<PayUserRel> purs = userInfo.getPayUserRels();
				if ((purs != null) && (!purs.isEmpty())) {
					for (PayUserRel pur : purs) {
						if (pur.getDefault_tag().trim().equals("0")) {
							pay_id = pur.getPay_id();
							break;
						}
					}
				}
			}
			if (user_id == null) {
				logger.error("QuerySubsAcctBalance Input MSISDN["
						+ request.getMSISDN() + "] NOT FOUND!");
				response.setStatus(STATUS_FAIL);
				response.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				response.setErrorMessage("余额查询号码[" + request.getMSISDN()
						+ "]未找到");
				return response;
			}
			if ((pay_id == null) || (pay_id.trim().equals(""))) {
				logger.error("QuerySubsAcctBalance Input MSISDN["
						+ request.getMSISDN() + "] DON'T Have Default PayId!");
				response.setStatus(STATUS_FAIL);
				response.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				response.setErrorMessage("余额查询号码[" + request.getMSISDN()
						+ "]未找到主账户");
				return response;
			}
			Long OweAmount = calcOweAmount(user_id, pay_id);
			Long FreezeAmount = this.calcFreezeAmount(user_id);
			response.setRemainAmount(0L);
			response.setAmountDtoList(null);
			ret = dealBalance(response, user_id, pay_id, OweAmount);
			if (!ret) {
				logger.error("QuerySubsAcctBalance Gather AccountBook Info Failed!");
				response.setStatus(STATUS_FAIL);
				response.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				response.setErrorMessage("获取余额信息失败");
			} else {
				response.setStatus(STATUS_OK);
				response.setErrorCode(null);
				response.setErrorMessage(null);
				response.setFreezeAmount(FreezeAmount);
				response.setOweAmount(OweAmount);
			}
		} else {
			logger.error("QuerySubsAcctBalance Input Parameters Check Failed!");
			response.setStatus(STATUS_FAIL);
			response.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			response.setErrorMessage("参数校验失败");
		}
		logger.debug(String.format("dispose() response: %s", response));
		logger.info("dispose() done");
		long endProcess = System.currentTimeMillis();
		logger.trace("[STAT][QuerySubsAcctBalance]Total["
				+ (endProcess - beginProcess) + "]ms");
		return response;
	}

	private boolean checkInputParameters(QuerySubsAcctBalanceRequest request) {
		if (request != null) {
			if ((request.getMSISDN() == null)
					|| (request.getMSISDN().trim().equals(""))) {
				logger.error("QuerySubsAcctBalance Input Parameter[MSISDN] Check Failed!");
				return false;
			}
			if ((request.getContactChannle() == null)
					|| (request.getContactChannle().trim().equals(""))) {
				logger.error("QuerySubsAcctBalance Input Parameter[ContactChannle] Check Failed!");
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) {
		LogRefreshTrigger lrt = null;
		if (BasicType.DS_CODE_ACCT_MONTH.equals(datastoreName)) {
			try {
				acctMonths.refresh();
				lrt = RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
						datastoreName, serviceName,
						BasicType.REFRESH_STATUS_OK, null);
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				lrt = RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
						datastoreName, serviceName,
						BasicType.REFRESH_STATUS_FAIL, ex.getMessage());
			} finally {
				try {
					ops.updateLogRefreshTrigger(lrt);
				} catch (Exception ex) {
					logger.error("Insert LogRefreshTrigger Failed!["
							+ ex.getMessage() + "]");
				}
			}
		}
	}

	public DSAcctMonth getAcctMonths() {
		return acctMonths;
	}

	public void setAcctMonths(DSAcctMonth acctMonths) {
		this.acctMonths = acctMonths;
	}

	public DataOps getOps() {
		return ops;
	}

	public void setOps(DataOps ops) {
		this.ops = ops;
	}
}
