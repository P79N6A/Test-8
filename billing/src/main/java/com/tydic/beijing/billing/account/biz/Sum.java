package com.tydic.beijing.billing.account.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.datastore.DSAcctItemCode;
import com.tydic.beijing.billing.account.datastore.DSCodeAcctMonth;
import com.tydic.beijing.billing.account.type.BalanceInfo;
import com.tydic.beijing.billing.account.type.BilActAddUpRetType;
import com.tydic.beijing.billing.account.type.CodeDefine;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.account.type.ResourceInfo;
import com.tydic.beijing.billing.account.type.TariffInfo;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.BilActBalanceAddUp;
import com.tydic.beijing.billing.dao.BilActResourceAddUp;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.CodeActAcctItem;
import com.tydic.beijing.billing.dao.LogUsedBalanceLog;
import com.tydic.beijing.billing.dao.QAcctProcess;
import com.tydic.beijing.billing.dao.SystemTime;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class Sum extends MyApplicationContextUtil {

	private final static Logger LOGGER = Logger.getLogger(Sum.class);

	private DSAcctItemCode acctItemCodes;
	private DSCodeAcctMonth codeAcctMonth;

	public DSCodeAcctMonth getCodeAcctMonth() {
		return codeAcctMonth;
	}

	public void setCodeAcctMonth(DSCodeAcctMonth codeAcctMonth) {
		this.codeAcctMonth = codeAcctMonth;
	}

	public DSAcctItemCode getAcctItemCodes() {
		return acctItemCodes;
	}

	public void setAcctItemCodes(DSAcctItemCode acctItemCodes) {
		this.acctItemCodes = acctItemCodes;
	}

	public String query(String user_id, Integer acct_month, String time_stamp,
			String sys_date, Vector<TariffInfo> vec_t,
			Vector<BalanceInfo> vec_b, Vector<ResourceInfo> vec_r,
			int service_scenarious, Vector<LogUsedBalanceLog> vec_lubl, 
			Vector<BilActAddUp> vec_baau, Vector<BilActBalanceAddUp> vec_babau, 
			Vector<BilActResourceAddUp> vec_barau) throws Exception {

		// BalanceInfo balance_tmp = (BalanceInfo)
		// mycontext.getBean("BalanceInfo");

		TariffInfo tariff_tmp = (TariffInfo) mycontext.getBean("TariffInfo");

		SystemTime st = S.get(SystemTime.class).queryFirst(
				Condition.build("getTimestamp"));
		if (st == null) {
			throw new BasicException(ErrorCode.ERR_ITMESTAMP_NOT_FOUND,
					"getTimestamp error");
		}
		sys_date = st.getTime();
		time_stamp = st.getTimestamp();
		// 账期
		CodeAcctMonth cam = codeAcctMonth.get(acct_month);
		if(cam == null) {
			throw new BasicException(
					ErrorCode.ERR_ACCT_MONTH_NOT_FOUND,
					"the acct_month:" + acct_month + " is not define!");
		}
		String partition_no = cam.getPartition_no();
		// LogUsedBalanceLog
		/*
		 * for (Iterator<BalanceInfo> iter = vec_b.iterator(); iter.hasNext();)
		 * { balance_tmp = iter.next(); LogUsedBalanceLog log_tmp =
		 * (LogUsedBalanceLog) mycontext .getBean("LogUsedBalanceLog");
		 * log_tmp.setUser_id(user_id); log_tmp.setAcct_month(acct_month);
		 * log_tmp.setInsert_timestamp(time_stamp);
		 * log_tmp.setBalance_id(balance_tmp.getBalance_id());
		 * log_tmp.setBalance_type_id(balance_tmp.getBalance_type_id());
		 * log_tmp.setUsed_fee(balance_tmp.getBalance());
		 * log_tmp.setProcess_tag(CodeDefine.NON_TREATED);
		 * log_tmp.setInsert_date(sys_date); vec_lubl.addElement(log_tmp); }
		 */
		// BilActAddUp
		// CodeActAcctItem caai = new CodeActAcctItem();
		CodeActAcctItem caai = null;
		for (Iterator<TariffInfo> iter = vec_t.iterator(); iter.hasNext();) {
			tariff_tmp = iter.next();
			caai = acctItemCodes.get(tariff_tmp.getTariiff_id());
			if (caai == null) {
				throw new BasicException(
						ErrorCode.ERR_ACCT_ITEM_CODE_NOT_FOUND,
						"the acct_item_code:" + tariff_tmp.getTariiff_id()
								+ " is not define!");
			}
			BilActAddUp bil_tmp = (BilActAddUp) mycontext
					.getBean("BilActAddUp");
			bil_tmp.setUser_id(user_id);
			bil_tmp.setAcct_month(acct_month);
			bil_tmp.setPartition_no(partition_no);
			bil_tmp.setAcct_item_code(tariff_tmp.getTariiff_id());
			bil_tmp.setUnit_type_id(caai.getUnit_type_id());
			bil_tmp.setFee(tariff_tmp.getFee());
			bil_tmp.setUpdate_time(sys_date);
			bil_tmp.setInsert_timestamp(time_stamp);
			vec_baau.addElement(bil_tmp);
		}
		// BilActBalanceAddUp
		for (BalanceInfo bi : vec_b) {
			caai = acctItemCodes.get(tariff_tmp.getTariiff_id());
			if (caai == null) {
				throw new BasicException(
						ErrorCode.ERR_ACCT_ITEM_CODE_NOT_FOUND,
						"the acct_item_code:" + tariff_tmp.getTariiff_id()
								+ " is not define!");
			}
			BilActBalanceAddUp bal_tmp = new BilActBalanceAddUp();
			bal_tmp.setBalance_id(bi.getBalance_id());
			bal_tmp.setBalance_type_id(bi.getBalance_type_id());
			bal_tmp.setUnit_type_id(bi.getUnit_type_id());
			bal_tmp.setAcct_month(acct_month);
			bal_tmp.setPay_id(bi.getPay_id());
			bal_tmp.setUser_id(user_id);
			bal_tmp.setAcct_item_code(bi.getAcct_item_code());
			bal_tmp.setPartition_no(partition_no);
			bal_tmp.setDeduct_fee(bi.getFee());
			bal_tmp.setUpdate_time(sys_date);
			vec_babau.addElement(bal_tmp);
		}
		
		//BilActResourceAddUp
		for(ResourceInfo ri : vec_r) {
			BilActResourceAddUp res_tmp = new BilActResourceAddUp();
			
			res_tmp.setUser_id(user_id);
			res_tmp.setAcct_month(acct_month);
			res_tmp.setPartition_num(partition_no);
			res_tmp.setResource_id(ri.getResource_id());
			res_tmp.setResource_value(ri.getResource_value());
			res_tmp.setUpdate_time(sys_date);
			res_tmp.setInsert_timestamp(time_stamp);
			
			vec_barau.addElement(res_tmp);
		}
		return sys_date;
	}

	public boolean update(Vector<LogUsedBalanceLog> vec_lubl,
			Vector<BilActAddUp> vec_baau, Vector<BilActBalanceAddUp> vec_babau,
			Vector<BilActResourceAddUp> vec_barau,
			String session_id, String sys_date, List<BilActAddUp> list_baau) {
		/*
		 * LogUsedBalanceLog lubl; for (Iterator<LogUsedBalanceLog> iter =
		 * vec_lubl.iterator(); iter .hasNext();) { lubl = iter.next();
		 * S.get(LogUsedBalanceLog.class).create(lubl); }
		 */
		if (vec_baau.isEmpty() == false) {
			BilActAddUpRetType ret = new BilActAddUpRetType();
			update_BilActAddUp(vec_baau, ret);

			Map<String, Object> filter = new HashMap<String, Object>();
			filter.clear();

			filter.put("partition_no", ret.getPartition_no());
			filter.put("user_id", ret.getUser_id());
			filter.put("acct_month", ret.getAcct_month());

			List<BilActAddUp> list_baad = new ArrayList<BilActAddUp>();
			list_baad = S.get(BilActAddUp.class).query(
					Condition.build("getBilActAddUp").filter(filter));
			for (BilActAddUp addup : list_baad) {
				list_baau.add(addup);
			}
		}
		if(vec_babau.isEmpty() == false) {
			Map<String, Object> filter = new HashMap<String, Object>();
			BilActBalanceAddUp babau = null;
			for(BilActBalanceAddUp tmp : vec_babau) {
				filter.clear();
				filter.put("partition_no", tmp.getPartition_no());
				filter.put("user_id", tmp.getUser_id());
				filter.put("pay_id", tmp.getPay_id());
				filter.put("balance_id", tmp.getBalance_id());
				filter.put("acct_month", tmp.getAcct_month());
				filter.put("acct_item_code", tmp.getAcct_item_code());
				babau = S.get(BilActBalanceAddUp.class).queryFirst(
						Condition.build("query").filter(filter));
	
				if (babau == null) {
					// insert
					S.get(BilActBalanceAddUp.class).create(tmp);
				} else {
					// update
					S.get(BilActBalanceAddUp.class).update(tmp);
				}
			}
		}
		if(vec_barau.isEmpty() == false) {
			Map<String, Object> filter = new HashMap<String, Object>();
			BilActResourceAddUp barau = null;
			for(BilActResourceAddUp tmp : vec_barau) {
				filter.put("partition_num", tmp.getPartition_num());
				filter.put("user_id", tmp.getUser_id());
				filter.put("acct_month", tmp.getAcct_month());
				filter.put("resource_id", tmp.getResource_id());
				
				barau = S.get(BilActResourceAddUp.class).queryFirst(
						Condition.build("query").filter(filter));
				
				if (barau == null) {
					// insert
					S.get(BilActResourceAddUp.class).create(tmp);
				} else {
					// update
					S.get(BilActResourceAddUp.class).update(tmp);
				}
			}
		}

		QAcctProcess tmp = S.get(QAcctProcess.class).queryFirst(Condition.build("queryBySessionId").filter("session_id", session_id));
		tmp.setProcess_tag(CodeDefine.PROCESS_TAG_TREATED);
		tmp.setUpdate_time(sys_date);
		S.get(QAcctProcess.class).create(tmp);
		S.get(QAcctProcess.class).batch(Condition.build("deleteBySessionId").filter("session_id", session_id));
		
		return true;
	}

	public boolean update_QAcctProcessHis(QAcctProcess q) {
		S.get(QAcctProcess.class).update(q);
		return true;
	}

	public boolean update_BilActAddUp(Vector<BilActAddUp> vec_baau,
			BilActAddUpRetType ret) {
		// BilActAddUp iter_baau = null;
		BilActAddUp baau = null;

		Map<String, Object> filter = new HashMap<String, Object>();
		/*
		 * for (Iterator<BilActAddUp> iter = vec_baau.iterator();
		 * iter.hasNext();) { iter_baau = iter.next();
		 */
		for (BilActAddUp iter_baau : vec_baau) {

			filter.clear();

			filter.put("partition_no", iter_baau.getPartition_no());
			filter.put("user_id", iter_baau.getUser_id());
			filter.put("acct_month", iter_baau.getAcct_month());
			filter.put("acct_item_code", iter_baau.getAcct_item_code());
			baau = S.get(BilActAddUp.class).queryFirst(
					Condition.build("queryBilActAddUp").filter(filter));

			if (baau == null) {
				// insert
				S.get(BilActAddUp.class).create(iter_baau);
			} else {
				// update
				S.get(BilActAddUp.class).update(iter_baau);
			}
			ret.setUser_id(iter_baau.getUser_id());
			ret.setAcct_month(iter_baau.getAcct_month());
			ret.setPartition_no(iter_baau.getPartition_no());
		}
		return true;
	}

	public boolean refresh() {
		try {
			acctItemCodes.refresh();
			codeAcctMonth.refresh();
		} catch (BasicException e) {
			LOGGER.error(e.toString());
			return false;
		}
		return true;
	}
}
