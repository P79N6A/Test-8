package com.tydic.beijing.billing.account.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.BilActAdjust;
import com.tydic.beijing.billing.dao.BilActUserRealTimeBill;
import com.tydic.beijing.billing.dao.BilActUserRealTimeBillForMemcached;
import com.tydic.beijing.billing.account.biz.AccountProcessOps;
import com.tydic.beijing.billing.account.core.Dao2File;
import com.tydic.beijing.billing.account.type.BilActUserRealTimeBillForFile;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class AccountProcessOps {
	private final static Logger LOGGER = Logger.getLogger(AccountProcessOps.class);
	private String user_id;
	private List<BilActUserRealTimeBill> l_UserBill;
	private List<com.tydic.beijing.billing.account.type.BilActUserRealTimeBill> l_billD2F;
	private BilActUserRealTimeBillForFile userBillForFile;
	private BilActUserRealTimeBillForMemcached baurtbfm;
	
	AccountProcessOps() {
		user_id = new String();
		l_UserBill = new ArrayList<BilActUserRealTimeBill>();
		l_billD2F = new ArrayList<com.tydic.beijing.billing.account.type.BilActUserRealTimeBill>();
		userBillForFile = new BilActUserRealTimeBillForFile();
		baurtbfm = new BilActUserRealTimeBillForMemcached();
	}
	
	public void createBill(List<BilActAddUp> baaus) throws Exception {
		createBill(baaus, null);
	}
	
	public void createBill(List<BilActAddUp> baaus, Dao2File d2f_userBill) throws Exception {
		if ((baaus == null) || (baaus.isEmpty())) {
			return;
		}
		l_UserBill.clear();
		String user_id_tmp = null;
		int acct_month_tmp = 0;
		int acct_item_code_tmp = 0;
		long fee_tmp = 0;
//		long adjustFee = 0;
		List<BilActAdjust> list_baa=null;
		Map<String, Object> filter = new HashMap<String, Object>();
		for (BilActAddUp baau : baaus) {
			long adjustFee = 0;
			user_id_tmp = baau.getUser_id();
			acct_month_tmp = baau.getAcct_month();
			acct_item_code_tmp = baau.getAcct_item_code();
			fee_tmp = baau.getFee();
			LOGGER.debug("===BilActAddUp的fee_tmp："+fee_tmp);
			filter.clear();
			filter.put("user_id", user_id_tmp);
			filter.put("acct_month", acct_month_tmp);
			filter.put("acct_item_code", acct_item_code_tmp);
			list_baa = S.get(BilActAdjust.class).query(Condition.build("queryAdjust").filter(filter));
			if(list_baa != null && list_baa.size() > 0) {
				for(BilActAdjust baa : list_baa) {
					LOGGER.debug("BilActAdjust:"+baa.toString());
					if(baa.getAdjust_mode().equals(BasicType.ADJUST_MODE_PLUS)){
						adjustFee = adjustFee + baa.getAdjust_fee();
					}else if(baa.getAdjust_mode().equals(BasicType.ADJUST_MODE_MINUS)) {
						adjustFee = adjustFee - baa.getAdjust_fee();
					}else {
						throw new BasicException(ErrorCode.ERR_ADJUST_MODE, "ADJUST_MODE error");
					}
				}
				LOGGER.debug("[BilActAdjust的adjustFee]："+adjustFee);
			} else {
				adjustFee = 0;
			}
			
			//组装对象
			BilActUserRealTimeBill rtb = new BilActUserRealTimeBill();
			rtb.setUser_id(user_id_tmp);
			rtb.setAcct_month(acct_month_tmp);
			rtb.setAcct_item_code(acct_item_code_tmp);
			rtb.setUnit_type_id(baau.getUnit_type_id());
			rtb.setOrg_fee(fee_tmp);
			rtb.setDiscount_fee(0);
			rtb.setAdjust_before(adjustFee);
			rtb.setFee(fee_tmp + adjustFee);
			rtb.setInsert_date(QueryInfo.getDBSystemTimeIssue().getTime());
			rtb.setPartition_num(baau.getPartition_no());
			
			user_id = baau.getUser_id();
			LOGGER.debug("[BilActUserRealTimeBill的Fee是]："+(fee_tmp + adjustFee));
			l_UserBill.add(rtb);
			
			if(d2f_userBill != null) {
				com.tydic.beijing.billing.account.type.BilActUserRealTimeBill uBill_d2f = new com.tydic.beijing.billing.account.type.BilActUserRealTimeBill();
				uBill_d2f.setUser_id(user_id_tmp);
				uBill_d2f.setAcct_month(acct_month_tmp);
				uBill_d2f.setAcct_item_code(acct_item_code_tmp);
				uBill_d2f.setUnit_type_id(baau.getUnit_type_id());
				uBill_d2f.setOrg_fee(fee_tmp);
				uBill_d2f.setDiscount_fee(0);
				uBill_d2f.setAdjust_before(adjustFee);
				uBill_d2f.setFee(fee_tmp + adjustFee);
				uBill_d2f.setInsert_date(QueryInfo.getDBSystemTimeIssue().getTime());
				uBill_d2f.setPartition_num(baau.getPartition_no());
				
				l_billD2F.add(uBill_d2f);
			}
		}
		baurtbfm.setUser_id(BasicType.MEMCACHED_BILL_PREFIX + user_id);
		baurtbfm.setL_userbill(l_UserBill);
		S.get(BilActUserRealTimeBillForMemcached.class).create(baurtbfm);
		LOGGER.error("[BilActUserRealTimeBillForMemcached]:"+l_UserBill.toString());
		if(d2f_userBill != null) {
			userBillForFile.setList(l_billD2F);
			d2f_userBill.writeFile(userBillForFile, l_billD2F.size());
		}
	}
}
