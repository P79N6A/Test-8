package com.tydic.beijing.billing.outerf.busi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActBill;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CodeActAcctItem;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.UserPresentHis;
import com.tydic.beijing.billing.dto.AcctItemDto;
import com.tydic.beijing.billing.dto.QuerySubsBillRequest;
import com.tydic.beijing.billing.dto.QuerySubsBillResponse;
import com.tydic.beijing.billing.dto.RebateDto;
import com.tydic.beijing.billing.outerf.architecture.Disposable;
import com.tydic.beijing.billing.outerf.architecture.LogAble;

/**
 *
 * @author zhuhz
 * 
 *         1. 获取USER_ID SELECT USER_ID FROM INFO_USER WHERE DEVICE_NUMBER=?
 * 
 *         2. 获取PAY_ID SELECT PAY_ID FROM LIFE_USER_PAY WHERE USER_ID=?
 * 
 *         3. 获取失效时间 SELECT exp_date FROM LIFE_USER_PAY WHERE USER_ID=?
 */
public class QuerySubsBillBusi extends LogAble implements Disposable {
	private static final String STATUS_OK = "1";
	private static final String STATUS_FAIL = "0";

	public QuerySubsBillBusi() {
	}

	public String getUserId(String device_number) {
		InfoUser iu = DataCenter.getInfoUserByDeviceNumber(device_number);
		if (iu != null) {
			return iu.getUser_id();
		} else {
			return null;
		}
	}

	public Integer getAcctMonth(Integer acct_month) {
		CodeAcctMonth cam = DataCenter
				.getCodeAcctMonthByBillingCycleId(acct_month);
		if (cam != null) {
			return cam.getAcct_month();
		} else {
			return null;
		}
	}

	@Override
	public Object dispose(Object input) {
		QuerySubsBillRequest request = (QuerySubsBillRequest) input;
		logger.debug(String.format("dispose() request: %s", request));
		QuerySubsBillResponse response = new QuerySubsBillResponse();
		boolean ret = checkInputParameters(request);
		if (ret) {
			String user_id = getUserId(request.getMSISDN());
			if (user_id == null) {
				logger.error("QuerySubsBill Input MSISDN["
						+ request.getMSISDN() + "] NOT FOUND!");
				response.setStatus(STATUS_FAIL);
				response.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				response.setErrorMessage("QuerySubsBill Input MSISDN["
						+ request.getMSISDN() + "] NOT FOUND!");
				return response;
			}
			Integer acct_month = getAcctMonth(request.getBillCycleID());
			if (acct_month == null) {
				logger.error("QuerySubsBill Input BillCycleID["
						+ request.getBillCycleID() + "] NOT FOUND!");
				response.setStatus(STATUS_FAIL);
				response.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				response.setErrorMessage("QuerySubsBill Input BillCycleID["
						+ request.getBillCycleID() + "] NOT FOUND!");
				return response;
			}
			long totalFee = 0L;
			long totalDeduct = 0L;
			long totalPayable = 0L;
			List<BilActBill> babs = getBillsByAcctMonth(user_id, acct_month);
			if (babs != null) {
				Map<Integer, String> allItemCodes = getAllItemCode();
				List<AcctItemDto> acctItemDtoList = new ArrayList<AcctItemDto>();
				for (BilActBill bab : babs) {
					String glName = allItemCodes.get(bab.getAcct_item_code());
					if (glName == null) {
						logger.warn("Bil_Act_Bill.Acct_Item_Code["
								+ bab.getAcct_item_code()
								+ "] NOT FOUND in Table[Code_Act_Acct_Item]!");
					}
					String parentGlName = getParentOfAcctItemCode(bab
							.getAcct_item_code());
					if (parentGlName == null) {
						logger.warn("Bil_Act_Bill.Acct_Item_Code["
								+ bab.getAcct_item_code()
								+ "] Parent Name NOT FOUND in Table[Rule_Parameters]!");
					}
					AcctItemDto aiDto = new AcctItemDto(
							request.getBillCycleID(), bab.getFee(), glName,
							parentGlName);
					acctItemDtoList.add(aiDto);
					totalFee += bab.getFee();
					totalDeduct += bab.getWrite_off_fee();
					totalPayable += bab.getOwe_fee();
				}
				response.setAcctItemDtoList(acctItemDtoList);
			} else {
				response.setAcctItemDtoList(null);
			}
			response.setTotalFee(totalFee);
			response.setTotalDeduct(totalDeduct);
			response.setTotalPayable(totalPayable);
			UserPresentHis uph = DataCenter.getRebateList(user_id, acct_month);
			if (uph != null) {
				List<RebateDto> rebateDtoList = new ArrayList<RebateDto>();
				RebateDto rDto = new RebateDto();
				rDto.setBillCycleID(request.getBillCycleID());
				rDto.setBillCycleName(Long.toString(request.getBillCycleID()));
				rDto.setCharge(uph.getCycle_value());
				rebateDtoList.add(rDto);
				response.setRebateDtoList(rebateDtoList);
			} else {
				response.setRebateDtoList(null);
			}

			response.setStatus(STATUS_OK);
			response.setErrorCode(null);
			response.setErrorMessage(null);

			logger.debug(String.format("dispose() response: %s", response));
		} else {
			response.setStatus(STATUS_FAIL);
			response.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			response.setErrorMessage("QuerySubsBill Input Parameters Check Failed!");
		}
		return response;
	}

	private String getParentOfAcctItemCode(int acctItemCode) {
		RuleParameters rp = DataCenter.getRuleParameters(acctItemCode);
		if (rp != null) {
			return rp.getPara_char2();
		}
		return null;
	}

	private Map<Integer, String> getAllItemCode() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<CodeActAcctItem> caats = DataCenter.getAllItemCode();
		if ((caats != null) && (!caats.isEmpty())) {
			for (CodeActAcctItem caat : caats) {
				map.put(caat.getAcct_item_code(), caat.getAcct_item_name());
			}
		}
		return map;
	}

	private List<BilActBill> getBillsByAcctMonth(String userId, int acctMonth) {
		return DataCenter.getBilActBillByAcctMonth(userId, acctMonth);
	}

	private boolean checkInputParameters(QuerySubsBillRequest request) {
		if (request != null) {
			if ((request.getMSISDN() == null)
					|| (request.getMSISDN().trim().equals(""))) {
				logger.error("QuerySubsBill Input Parameter[MSISDN] Check Failed!");
				return false;
			}
			if ((request.getBillCycleID() == null)
					|| (request.getBillCycleID() == 0)) {
				logger.error("QuerySubsBill Input Parameter[BillCycleID] Check Failed!");
				return false;
			}
			if ((request.getContactChannle() == null)
					|| (request.getContactChannle().trim().equals(""))) {
				logger.error("QuerySubsBill Input Parameter[ContactChannle] Check Failed!");
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) {
		logger.info("Has Nothing to do!");
		return;
	}
}
