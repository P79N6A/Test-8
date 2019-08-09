package com.tydic.beijing.billing.plugin.biz;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActBalanceAddUp;
import com.tydic.beijing.billing.dao.SystemTime;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class SumInfo {
	private final static Logger LOGGER = Logger
			.getLogger(SumInfo.class);
	
	private String split1 = ";";
	private String split2 = ":";
	private String split3 = ",";

	private String[] balanceInfo;
	private String balanceStr;
	private String[] balance_semicolon;//冒号
	private String[] balance_comma;//逗号
	
	private String balance_id;
	private String fee;
	private String unit_type;
	private String balance_type;
	private String item_code;
	
	private BilActBalanceAddUp babau;
	
	/*
	 * 【3138658:2,65536,429950,11,76;】为例： 账本ID:单位类型,数量,改变后余额,账本类型,帐目类型
	 * balance_id:unit_type,fee,BALANCE_end,balance_type,item_code
	 * sql
	 * select a.servid, b.pay_id, a.balanceinfo from cdr_cdr300_12 a, pay_user_rel b where a.servid = b.user_id and a.balanceinfo is not null
	 */
	public void doSum(String user_id, String pay_id, String balance_info, String partition_no) throws BasicException {
		BilActBalanceAddUp tmp = new BilActBalanceAddUp();
		balanceStr = balance_info;
		balanceInfo = balanceStr.split(split1);
		for(String b_info : balanceInfo) {
			balance_semicolon = b_info.split(split2);
			balance_id = balance_semicolon[0];
			balance_comma = balance_semicolon[1].split(split3);
			unit_type = balance_comma[0];
			fee = balance_comma[1];
			balance_type = balance_comma[3];
			item_code = balance_comma[4];

			SystemTime st = S.get(SystemTime.class).queryFirst(
					Condition.build("getTimestamp"));
			if(st == null) {
				throw new BasicException(ErrorCode.ERR_ITMESTAMP_NOT_FOUND,
						"getTimestamp error");
			}
			String sys_date = st.getTime();
			
			tmp.setBalance_id(Long.parseLong(balance_id));
			tmp.setBalance_type_id(Integer.parseInt(balance_type));
			tmp.setUnit_type_id(Integer.parseInt(unit_type));
			tmp.setAcct_month(224);
			tmp.setPay_id(pay_id);
			tmp.setUser_id(user_id);
			tmp.setAcct_item_code(Integer.parseInt(item_code));
			tmp.setPartition_no(partition_no);
			tmp.setDeduct_fee(Integer.parseInt(fee));
			tmp.setUpdate_time(sys_date);
			
			
			//入库
			LOGGER.info(balance_id + " " + unit_type + " " + fee + " " + balance_type + " " + item_code);
			Map<String, Object> filter = new HashMap<String, Object>();
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
}
