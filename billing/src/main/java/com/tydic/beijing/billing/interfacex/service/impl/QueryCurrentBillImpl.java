/**
 * 
 */
package com.tydic.beijing.billing.interfacex.service.impl;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.datastore.DSBalanceType;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.common.JDNToNewMsisdn;
import com.tydic.beijing.billing.dao.BilActRealTimeBillForOracle;
import com.tydic.beijing.billing.dao.CodeBilBalanceType;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dto.CurrentBill;
import com.tydic.beijing.billing.dto.QueryCurrentBillInfo;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.QueryCurrentBill;

/**
 * @author sung
 *
 */
public class QueryCurrentBillImpl implements QueryCurrentBill {
	private static Logger log = Logger.getLogger(QueryCurrentBillImpl.class);

	private final static String RESULT_CODE_OK = "1";
	private final static String RESULT_CODE_FAIL = "0";

	private DbTool tools;
	private DSBalanceType balanceTypes;
	private int flag = 0;
	/**
	 * @author sung
	 *
	 * @param msisdn
	 * @param contactChannle
	 */

	@Override
	public CurrentBill queryCurrentBill(QueryCurrentBillInfo info) {
		/*
		 * String msisdn=info.getMSISDN(); String
		 * channle=info.getContactChannle();
		 * log.debug("服务[QueryCurrentBill],号码["+msisdn+"],渠道["+channle+"]");
		 * CurrentBill cb=new CurrentBill(); DbTool db=new DbTool();
		 * 
		 * List<UserPayInfo> userInfos=db.queryUserInfoByNbr(msisdn);
		 * if(userInfos == null || userInfos.isEmpty()){
		 * log.error("错误码[ZSMART-CC-00003]:业务号码不存在"); cb.setStatus("0");
		 * cb.setErrorCode("ZSMART-CC-00003"); cb.setErrorMessage("业务号码不存在");
		 * return cb; }
		 * 
		 * 
		 * int findPay=0; String payId=""; for(UserPayInfo iter : userInfos){
		 * if(iter==null) continue; payId=iter.getPay_id(); if(payId !=null &&
		 * !payId.isEmpty()){ findPay=1; } String tag=iter.getDefault_tag();
		 * if(tag!=null && tag.equals("0")){ findPay=2; break; } }
		 * if(findPay==0){ log.debug("账户不存在"); cb.setStatus("0");
		 * cb.setErrorCode("ZSMART-CC-00016"); cb.setErrorMessage("账户不存在");
		 * return cb; }else if(findPay==1){ log.debug("默认账户不存在");
		 * cb.setStatus("0"); cb.setErrorCode("ZSMART-CC-00016");
		 * cb.setErrorMessage("默认账户不存在"); return cb; }
		 * 
		 * String userId=userInfos.get(0).getUser_id(); int
		 * month=Calendar.getInstance().get(Calendar.MONTH)+1;
		 * 
		 * String acctMonth=month>9?""+month:"0"+month;
		 * 
		 * List<CurrentBillDto> bill=db.queryCurrentBill(payId,
		 * userId,acctMonth); if(bill==null || bill.isEmpty() ){
		 * log.debug("没有找到记录"); cb.setStatus("0");
		 * cb.setErrorCode("ZSMART-CC-00032"); cb.setErrorMessage("没有查找到记录");
		 * return cb; } long fee=0; long realBalance=0; for(CurrentBillDto dto :
		 * bill){
		 * 
		 * realBalance+=dto.getReal_balance(); } fee=bill.get(0).getFee();
		 * cb.setCurFee(fee);
		 * 
		 * 
		 * cb.setBalance(realBalance);
		 * 
		 * 
		 * 
		 * //LOG log.debug(cb);
		 * 
		 * return cb;
		 */
		if (flag == 0) {
			try {
				balanceTypes.load();
			} catch (Exception e) {
				log.error("Load CODE_BIL_BALANCE_TYPE FAILED!");
				System.exit(0);
			}
			flag = 1;
		}
		CurrentBill cb = new CurrentBill();
		reset(cb);
		if (info != null) {
			log.debug("Request Message:[" + info.toString() + "]");
			String deviceNumber = info.getMSISDN();
			
			if ((deviceNumber != null) && (!deviceNumber.trim().equals(""))) {
				
				//add by wangtao begin
				deviceNumber = JDNToNewMsisdn.jdnToNewMsisdn(deviceNumber,BasicType.STARTSTR);
				//add by wangtao end
				
				InfoUser iu = tools.queryUserInfoByDeviceNumber(deviceNumber);
				if ((iu == null) || (iu.getUser_id() == null)) {
					log.error("DeviceNumber[" + deviceNumber + "] NOT FOUND!");
					cb.setStatus(RESULT_CODE_FAIL);
					cb.setErrorCode(ErrorCode.ERR_DEVICE_NUMBER_NONEXIST);
					cb.setErrorMessage("输入设备号码[" + deviceNumber + "]用户资料未找到！");
					return cb;
				}
				PayUserRel pur = tools.queryPayInfoByUserId(iu.getUser_id());
				if ((pur == null) || (pur.getPay_id() == null)) {
					log.error("DeviceNumber[" + deviceNumber
							+ "] Default PayId NOT FOUND!");
					cb.setStatus(RESULT_CODE_FAIL);
					cb.setErrorCode(ErrorCode.ERR_PAYID_NONEXSIT);
					cb.setErrorMessage("输入设备号码[" + deviceNumber + "]默认账户未找到！");
					return cb;
				}
				List<InfoPayBalance> ipbs = tools.queryBalanceInfoByPayId(pur
						.getPay_id());
				long balance = 0L;
				if ((ipbs != null) && (!ipbs.isEmpty())) {
					for (Iterator<InfoPayBalance> iter = ipbs.iterator(); iter
							.hasNext();) {
						InfoPayBalance ipb = iter.next();
						CodeBilBalanceType cbbt = balanceTypes.get(ipb
								.getBalance_type_id());
						if (cbbt == null) {
							log.warn("BalanceTypeId["
									+ ipb.getBalance_type_id()
									+ "] Not Found in table[CODE_BIL_BALANCE_TYPE]!");
							iter.remove();
						} else {
							if (cbbt.getUnit_type_id() != BasicType.UNIT_TYPE_MONEY) {
								iter.remove();
							} else {
								balance += ipb.getReal_balance();
							}
						}
					}
				}
				cb.setBalance(balance);
				List<BilActRealTimeBillForOracle> barts = tools
						.queryCurrentBillsByUserId(iu.getUser_id(),
								pur.getPay_id(), getCurrentMonth());
				long oweFee = 0L;
				if ((barts != null) && (!barts.isEmpty())) {
					for (BilActRealTimeBillForOracle bart : barts) {
						oweFee += bart.getFee();
					}
				}
				cb.setCurFee(oweFee);
			} else {
				log.error("DeviceNumber is NULL!");
				cb.setStatus(RESULT_CODE_FAIL);
				cb.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
				cb.setErrorMessage("输入设备号码为空！");
			}
		} else {
			log.error("Request Message is NULL!");
			cb.setStatus(RESULT_CODE_FAIL);
			cb.setErrorCode(ErrorCode.ERR_UNCLASSIFIED);
			cb.setErrorMessage("输入信息为空！");
		}
		return cb;
	}

	private void reset(CurrentBill cb) {
		cb.setErrorCode(null);
		cb.setErrorMessage(null);
		cb.setStatus(RESULT_CODE_OK);
		cb.setBalance(0L);
		cb.setCurFee(0L);
		cb.setPayForOthers(0L);
	}

	private String getCurrentMonth() {
		Date date = new Date(System.currentTimeMillis());
		return date.toString().substring(5, 7);
	}

	public DbTool getTools() {
		return tools;
	}

	public void setTools(DbTool tools) {
		this.tools = tools;
	}

	public DSBalanceType getBalanceTypes() {
		return balanceTypes;
	}

	public void setBalanceTypes(DSBalanceType balanceTypes) {
		this.balanceTypes = balanceTypes;
	}
}
