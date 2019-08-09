package com.tydic.beijing.billing.interfacex.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.RuleProductResource;
import com.tydic.beijing.billing.dto.BValueExchangeRequest;
import com.tydic.beijing.billing.dto.BaseResponse;
import com.tydic.beijing.billing.dto.OrderExtraPackageRequest;
import com.tydic.beijing.billing.dto.PackageDetailDto;
import com.tydic.beijing.billing.dto.ResourceChargeParaIn;
import com.tydic.beijing.billing.dto.ResourceChargeParaInList;
import com.tydic.beijing.billing.dto.ResourceChargeParaOut;
import com.tydic.beijing.billing.interfacex.service.OrderExtraPackage;
import com.tydic.beijing.billing.interfacex.service.ResourceChargeService;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class OrderExtraPackageImpl implements OrderExtraPackage {
	private final static Logger LOGGER = Logger
			.getLogger(OrderExtraPackageImpl.class);

	private static final String STATUS_OK = "1";
	private static final String STATUS_FAIL = "0";

	private static final String BVALUE_VOICE_PRD_ID = "BVALUE_EXCHANGE_V";
	private static final String BVALUE_GGSN_PRD_ID = "BVALUE_EXCHANGE_G";
	private static final String BVALUE_SMS_PRD_ID = "BVALUE_EXCHANGE_S";

	private static final String ORDEREXTRAPKG_OPERATE_TYPE = "13";
	private static final String BVALUEEXCHG_OPERATE_TYPE = "14";

	private static final int PRODUCT_TYPE_B_EXCHANGE = 4;
	private static final int PRODUCT_TYPE_EXTRA_PACKAGE = 2;

	private ResourceChargeService resourceCharge;

	@Override
	public BaseResponse doOrder(OrderExtraPackageRequest info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse doExchange(BValueExchangeRequest info) {
		LOGGER.debug("OrderExtraPackage[BValueExchange] Input:[" + info + "]");
		BaseResponse result = new BaseResponse();
		this.resetResult(result);
		this.checkInputParameter4Exchange(info, result);
		if (result.getStatus().equals(STATUS_FAIL)) {
			return result;
		}
		PayUserRel pur = this.getDefaultPayId(info.getMSISDN(), result);
		if ((pur == null) || (result.getStatus().equals(STATUS_FAIL))) {
			return result;
		}
		ResourceChargeParaIn resourceChargeParaIn = new ResourceChargeParaIn();
		resourceChargeParaIn.setSN(info.getSn());
		resourceChargeParaIn.setPayId(pur.getPay_id());
		resourceChargeParaIn.setOperateType(BVALUEEXCHG_OPERATE_TYPE);
		List<ResourceChargeParaInList> rcps = new ArrayList<ResourceChargeParaInList>();
		List<PackageDetailDto> pdds = info.getPackageDetailDtoList();
		for (PackageDetailDto pdd : pdds) {
			long pkgType = pdd.getPackageType(); // 1-语音 2-流量 3-短信
			RuleProductResource rpr = null;
			if (pkgType == 1L) {
				rpr = getBalanceTypeId(BVALUE_VOICE_PRD_ID, pkgType,
						PRODUCT_TYPE_B_EXCHANGE);
			} else if (pkgType == 2L) {
				rpr = getBalanceTypeId(BVALUE_GGSN_PRD_ID, pkgType,
						PRODUCT_TYPE_B_EXCHANGE);
			} else if (pkgType == 3L) {
				rpr = getBalanceTypeId(BVALUE_SMS_PRD_ID, pkgType,
						PRODUCT_TYPE_B_EXCHANGE);
			} else {
				LOGGER.error("OrderExtraPackage[BValueExchange] Input PackageType["
						+ pkgType + "] Not Supported YET!");
			}
			if (rpr == null) {
				LOGGER.error("OrderExtraPackage[BValueExchange]DeviceNumber["
						+ info.getMSISDN() + "] BalanceTypeId Can NOT Found!");
				result.setStatus(STATUS_FAIL);
				result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				result.setErrorMessage("B值兑换资源，资源账本类型无法确定！");
				return result;
			}
			ResourceChargeParaInList rcp = new ResourceChargeParaInList();
			rcp.setBalanceTypeId(rpr.getBalance_type_id());
			rcp.setChargeValue(pdd.getQuantity());
			rcp.setEffDate(pdd.getEffDate() + "000000");
			rcp.setExpDate(pdd.getExpDate() + "235959");
			rcps.add(rcp);
		}
		resourceChargeParaIn.setResourceChargeParaInList(rcps);
		ResourceChargeParaOut resourceChargeParaOut = null;
		try {
			resourceChargeParaOut = resourceCharge.charge(resourceChargeParaIn);
		} catch (Exception e) {
			LOGGER.error("OrderExtraPackage[BValueExchange]DeviceNumber["
					+ info.getMSISDN() + "]Charge Failed![" + e.getMessage()
					+ "]");
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("B值兑换资源，资源到账异常！");
			return result;
		}
		LOGGER.debug("[ResourceChargeParaOut][" + resourceChargeParaOut + "]");
		if ((resourceChargeParaOut == null)
				|| (resourceChargeParaOut.getStatus() == null)
				|| (resourceChargeParaOut.getStatus().trim()
						.equals(STATUS_FAIL))) {
			LOGGER.error("OrderExtraPackage[BValueExchange]DeviceNumber["
					+ info.getMSISDN() + "]Charge Failed!");
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("B值兑换资源，资源到账异常！");
			return result;
		}
		LOGGER.debug("OrderExtraPackage[BValueExchange] Output:[" + result
				+ "]");
		return result;
	}

	private RuleProductResource getBalanceTypeId(String prdId, long pkgType,
			int prdType) {
		List<RuleProductResource> rprs = S.get(RuleProductResource.class)
				.query(Condition.build("selectRuleProductResource").filter(
						"product_id", prdId));
		if ((rprs == null) || (rprs.isEmpty())) {
			LOGGER.warn("ProductId["
					+ prdId
					+ "] Can NOT Found any Records in Table[RULE_PRODUCT_RESOURCE]!");
			return null;
		}
		for (RuleProductResource rpr : rprs) {
			if ((rpr.getResource_type() == pkgType)
					&& (rpr.getProduct_type() == prdType)) {
				LOGGER.debug("OrderExtraPackage[BValueExchange]PRD_ID[" + prdId
						+ "]Found BALANCE_TYPE_ID[" + rpr.getBalance_type_id()
						+ "]");
				return rpr;
			}
		}
		return null;
	}

	private PayUserRel getDefaultPayId(String deviceNumber, BaseResponse result) {
		InfoUser iu = S.get(InfoUser.class).queryFirst(
				Condition.build("getUserInfoByDeviceNumber").filter(
						"device_number", deviceNumber));
		if (iu == null) {
			LOGGER.error("OrderExtraPackage[BValueExchange]DeviceNumber["
					+ deviceNumber + "] Not Exisit!");
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_DEVICE_NUMBER_NONEXIST);
			result.setErrorMessage(ErrorCode.ERR_MSG_DEVICE_NUMBER_NONEXIST);
			return null;
		}
		PayUserRel pur = S.get(PayUserRel.class).queryFirst(
				Condition.build("getPayInfoByUserId").filter("user_id",
						iu.getUser_id()));
		if (pur == null) {
			LOGGER.error("OrderExtraPackage[BValueExchange]UserId["
					+ iu.getUser_id() + "] Default PayId Not Found!");
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_PAYID_NONEXSIT);
			result.setErrorMessage(ErrorCode.ERR_MSG_PAYID_NONEXSIT);
			return null;
		}
		return pur;
	}

	private void checkInputParameter4Exchange(BValueExchangeRequest info,
			BaseResponse result) {
		if (info == null) {
			LOGGER.error("OrderExtraPackage[BValueExchange] Input Parameter is NULL!");
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("输入参数为空！");
		} else if ((info.getSn() == null) || (info.getSn().trim().equals(""))) {
			LOGGER.error("OrderExtraPackage[BValueExchange] Input Parameter SN is NULL!");
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("业务流水为空！");
		} else if ((info.getMSISDN() == null)
				|| (info.getMSISDN().trim().equals(""))) {
			LOGGER.error("OrderExtraPackage[BValueExchange][SN:" + info.getSn()
					+ "] Input Parameter MSISDN is NULL!");
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_DEVICE_NUMBER_NULL);
			result.setErrorMessage(ErrorCode.ERR_MSG_DEVICE_NUMBER_NULL);
		} else if ((info.getContactChannel() == null)
				|| (info.getContactChannel().trim().equals(""))) {
			LOGGER.error("OrderExtraPackage[BValueExchange][SN:" + info.getSn()
					+ "] Input Parameter ContactChannel is NULL!");
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("接触渠道为空！");
		} else if ((info.getPackageDetailDtoList() == null)
				|| (info.getPackageDetailDtoList().isEmpty())) {
			LOGGER.error("OrderExtraPackage[BValueExchange][SN:" + info.getSn()
					+ "] Input Parameter PackageDetailDtoList is NULL!");
			result.setStatus(STATUS_FAIL);
			result.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			result.setErrorMessage("兑换信息为空！");
		}
	}

	private void resetResult(BaseResponse result) {
		result.setStatus(STATUS_OK);
		result.setErrorCode(null);
		result.setErrorMessage(null);
	}

	public ResourceChargeService getResourceCharge() {
		return resourceCharge;
	}

	public void setResourceCharge(ResourceChargeService resourceCharge) {
		this.resourceCharge = resourceCharge;
	}
}
