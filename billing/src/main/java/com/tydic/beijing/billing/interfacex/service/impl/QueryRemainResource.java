package com.tydic.beijing.billing.interfacex.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.LifeProductResourceRel;
import com.tydic.beijing.billing.dao.RemainBalance;
import com.tydic.beijing.billing.dao.UserPayInfo;
import com.tydic.beijing.billing.dto.RemainResource;
import com.tydic.beijing.billing.dto.RemainResourceDto;
import com.tydic.beijing.billing.dto.RemainResourceQueryInfo;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.QueryRemainResourceList;

/**
 * @author sung
 *
 */
public class QueryRemainResource implements QueryRemainResourceList {

	private static Logger log = Logger.getLogger(QueryRemainResource.class);
	private final static String RESULT_STATUS_FAIL = "0"; // 返回失败
	private final static String RESULT_STATUS_OK = "1"; // 返回成功

	private final static String MAIN_PRODUCT_FLAG = "1"; // 主产品标志

	private static Map<String, String> unitTypeRes = new HashMap<String, String>();

	@Override
	public RemainResource query(RemainResourceQueryInfo info) {
		String nbr = info.getMSISDN();
		log.debug("QueryRemainResource Input:[" + info + "]");

		RemainResource resource = new RemainResource();
		if (info.getMSISDN() == null || info.getMSISDN().trim().equals("")) {
			log.error("Input DeviceNumber CAN NOT Be NULL!");
			resource.setStatus(RESULT_STATUS_FAIL);
			resource.setErrorCode(ErrorCode.ERR_DEVICE_NUMBER_NULL);
			resource.setErrorMessage(ErrorCode.ERR_MSG_DEVICE_NUMBER_NULL);
			return resource;
		}

		DbTool db = new DbTool();

		List<UserPayInfo> userInfos = db.queryUserInfoByNbr(nbr);

		if (userInfos == null || userInfos.isEmpty()) {
			log.error("DeviceNumber[" + nbr + "] Not Exist!");
			resource.setStatus(RESULT_STATUS_FAIL);
			resource.setErrorCode(ErrorCode.ERR_DEVICE_NUMBER_NONEXIST);
			resource.setErrorMessage(ErrorCode.ERR_MSG_DEVICE_NUMBER_NONEXIST);
			return resource;
		}

		List<RemainBalance> balanceAll = null;

		int findPay = 0;
		String payId = "";
		String userId = null;
		//
		for (UserPayInfo iter : userInfos) {
			payId = iter.getPay_id();
			userId = iter.getUser_id();
			if (payId != null && !payId.isEmpty()) {
				findPay = 1;
			}
			String tag = iter.getDefault_tag();
			if (tag != null && tag.equals("0")) {
				findPay = 2;
				break;
			}
		}
		if (findPay == 0) {
			log.debug("DeviceNumber[" + nbr + "] PayId Not Found!");
			resource.setStatus(RESULT_STATUS_FAIL);
			resource.setErrorCode(ErrorCode.ERR_PAYID_NONEXSIT);
			resource.setErrorMessage(ErrorCode.ERR_MSG_PAYID_NONEXSIT);
			return resource;
		} else if (findPay == 1) {
			log.debug("DeviceNumber[" + nbr + "] Default PayId Not Found!");
			resource.setStatus(RESULT_STATUS_FAIL);
			resource.setErrorCode(ErrorCode.ERR_PAYID_NONEXSIT);
			resource.setErrorMessage(ErrorCode.ERR_MSG_PAYID_NONEXSIT);
			return resource;
		} else if (findPay == 2) {
			balanceAll = db.queryBalance(payId);

		}

		if (balanceAll == null || balanceAll.isEmpty()) {
			log.debug("DeviceNumber[" + nbr
					+ "] No Available Resource AcctBook Found!");
			resource.setStatus(RESULT_STATUS_OK);
			resource.setErrorCode(null);
			resource.setErrorMessage("没有查找到记录");
			return resource;
		}

		if (unitTypeRes.size() == 0) {
			unitTypeRes = db.queryUnitType();
		}
		// added by tian@20150126 for: 网厅改版需求
		boolean isPkgKind = true;
		List<LifeProductResourceRel> lprrs = db
				.getLifeProductResourceRel(userId);

		Set<Long> balanceIds = getValidBalanceIdOfMainPkg(lprrs);
		if (balanceIds == null) {
			isPkgKind = false;
		}
		List<RemainResourceDto> dtos = new ArrayList<RemainResourceDto>();
		List<RemainResourceDto> pkgDtos = new ArrayList<RemainResourceDto>();
		List<RemainResourceDto> othDtos = new ArrayList<RemainResourceDto>();

		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<RemainResourceDto> voiceRRD = new ArrayList<RemainResourceDto>();
		List<RemainResourceDto> ggsnRRD = new ArrayList<RemainResourceDto>();
		List<RemainResourceDto> smsRRD = new ArrayList<RemainResourceDto>();

		List<RemainResourceDto> pkgVoiceRRD = new ArrayList<RemainResourceDto>();
		List<RemainResourceDto> pkgGgsnRRD = new ArrayList<RemainResourceDto>();
		List<RemainResourceDto> pkgSmsRRD = new ArrayList<RemainResourceDto>();

		List<RemainResourceDto> othVoiceRRD = new ArrayList<RemainResourceDto>();
		List<RemainResourceDto> othGgsnRRD = new ArrayList<RemainResourceDto>();
		List<RemainResourceDto> othSmsRRD = new ArrayList<RemainResourceDto>();

		for (RemainBalance balance : balanceAll) {
			switch (balance.getUnit_type_id()) {
			case 1:
				dtoAssembleHelper(voiceRRD, balance,
						unitTypeRes.get("" + balance.getBalance_type_id()),
						simpleFormat);
				if (isPkgKind) {
					if (balanceIds.contains(balance.getBalance_id())) {
						dtoAssembleHelper(
								pkgVoiceRRD,
								balance,
								unitTypeRes.get(""
										+ balance.getBalance_type_id()),
								simpleFormat);
					} else {
						dtoAssembleHelper(
								othVoiceRRD,
								balance,
								unitTypeRes.get(""
										+ balance.getBalance_type_id()),
								simpleFormat);
					}
				}
				break;
			case 2:
				dtoAssembleHelper(ggsnRRD, balance,
						unitTypeRes.get("" + balance.getBalance_type_id()),
						simpleFormat);
				if (isPkgKind) {
					if (balanceIds.contains(balance.getBalance_id())) {
						dtoAssembleHelper(
								pkgGgsnRRD,
								balance,
								unitTypeRes.get(""
										+ balance.getBalance_type_id()),
								simpleFormat);
					} else {
						dtoAssembleHelper(
								othGgsnRRD,
								balance,
								unitTypeRes.get(""
										+ balance.getBalance_type_id()),
								simpleFormat);
					}
				}
				break;
			case 3:
				dtoAssembleHelper(smsRRD, balance,
						unitTypeRes.get("" + balance.getBalance_type_id()),
						simpleFormat);
				if (isPkgKind) {
					if (balanceIds.contains(balance.getBalance_id())) {
						dtoAssembleHelper(
								pkgSmsRRD,
								balance,
								unitTypeRes.get(""
										+ balance.getBalance_type_id()),
								simpleFormat);
					} else {
						dtoAssembleHelper(
								othSmsRRD,
								balance,
								unitTypeRes.get(""
										+ balance.getBalance_type_id()),
								simpleFormat);
					}
				}
				break;
			default: {
				log.warn("Unsupport UnitTypeId[" + balance.getUnit_type_id()
						+ "]");
			}
			}

		}
		if (!voiceRRD.isEmpty()) {
			dtos.addAll(voiceRRD);
		}
		if (!ggsnRRD.isEmpty()) {
			dtos.addAll(ggsnRRD);
		}
		if (!smsRRD.isEmpty()) {
			dtos.addAll(smsRRD);
		}
		if (isPkgKind) {
			if (!pkgVoiceRRD.isEmpty()) {
				pkgDtos.addAll(pkgVoiceRRD);
			}
			if (!pkgGgsnRRD.isEmpty()) {
				pkgDtos.addAll(pkgGgsnRRD);
			}
			if (!pkgSmsRRD.isEmpty()) {
				pkgDtos.addAll(pkgSmsRRD);
			}
			if (!othVoiceRRD.isEmpty()) {
				othDtos.addAll(othVoiceRRD);
			}
			if (!othGgsnRRD.isEmpty()) {
				othDtos.addAll(othGgsnRRD);
			}
			if (!othSmsRRD.isEmpty()) {
				othDtos.addAll(othSmsRRD);
			}
		} else {
			pkgDtos = null;
			othDtos = dtos;
		}

		resource.setRemainResourceDtoList(dtos);
		resource.setPackageResourceUsageList(pkgDtos);
		resource.setOtherResourceUsageList(othDtos);

		for (RemainResourceDto dto : dtos) {
			log.debug(dto);
		}
		log.debug(resource);

		return resource;
	}

	private void dtoAssembleHelper(List<RemainResourceDto> destDtos,
			RemainBalance source, String resourceType,
			SimpleDateFormat simpleFormat) {
		int flag = 0;
		String effDate = simpleFormat.format(source.getEff_date());
		String expDate = simpleFormat.format(source.getExp_date());
		for (RemainResourceDto rrd : destDtos) {
			if (effDate.equalsIgnoreCase(rrd.getEffDate())
					&& expDate.equalsIgnoreCase(rrd.getExpDate())
					&& rrd.getResourceType().equals(resourceType)) {
				log.debug("rrd.getTotalResource()=============>"
						+ rrd.getTotalResource());
				rrd.setTotalResource(rrd.getTotalResource()
						+ source.getBalance());
				rrd.setRemainResource(rrd.getRemainResource()
						+ source.getReal_balance());
				rrd.setUsedResource(rrd.getUsedResource()
						+ (source.getBalance() - source.getReal_balance()));
				flag = 1;
				break;
			}

		}
		// 添加非0限制
		if (flag == 0 && source.getBalance() > 0) {
			log.debug("source.getBalance()================>"
					+ source.getBalance());
			RemainResourceDto rrd = new RemainResourceDto();
			rrd.setResourceType(resourceType);
			rrd.setTotalResource(source.getBalance());
			rrd.setRemainResource(source.getReal_balance());
			rrd.setUsedResource(source.getBalance() - source.getReal_balance());
			rrd.setEffDate(effDate);
			rrd.setExpDate(expDate);
			destDtos.add(rrd);
		} else {
			log.debug("balanceid=" + source.getBalance_id() + "资源余量为0");
		}
	}

	private Set<Long> getValidBalanceIdOfMainPkg(
			List<LifeProductResourceRel> lprrs) {
		if ((lprrs == null) || (lprrs.isEmpty())) {
			return null;
		}
		Set<Long> balanceIds = new HashSet<Long>();
		for (LifeProductResourceRel lprr : lprrs) {
			if (lprr.getBalance_id() != 0) {
				if ((lprr.getProduct_flag() != null)
						&& (lprr.getProduct_flag().trim()
								.equals(MAIN_PRODUCT_FLAG))) {
					balanceIds.add(lprr.getBalance_id());
				}
			}
		}
		return balanceIds;
	}
}
