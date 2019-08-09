package com.tydic.beijing.billing.account.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.type.BalanceWriteOff;
import com.tydic.beijing.billing.account.type.ItemWriteOff;
import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.BilActAccesslog;
import com.tydic.beijing.billing.dao.BilActBalanceAddUp;
import com.tydic.beijing.billing.dao.BilActBill;
import com.tydic.beijing.billing.dao.EndInfoUser;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.LogActWriteoff;
import com.tydic.beijing.billing.util.GroupBy;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class End {
	private final static Logger LOGGER = Logger.getLogger(End.class);
	private GroupBy gb;
	private List<BalanceWriteOff> list_bal;
	private List<ItemWriteOff> list_item;
	private Map<String, Object> filter;

	public End() {
		gb = new GroupBy();
		list_bal = new ArrayList<BalanceWriteOff>();
		list_item = new ArrayList<ItemWriteOff>();
		filter = new HashMap<String, Object>();
	}

	public void update(List<WriteOffDetail> writeOffDetail,
			List<BilActBalanceAddUp> balanceSum, String insert_date, String patitionNo) {
		// 账本 real_balance = balance 初始化
		if ((writeOffDetail == null || writeOffDetail.isEmpty())
				&& (balanceSum == null || balanceSum.isEmpty())) {
			LOGGER.info("write off detail and sum source balance is empty");
			return;
		}
		String payId = null;
		if((writeOffDetail != null) && (writeOffDetail.isEmpty() == false)) {
			payId = writeOffDetail.get(0).getPay_Id();
		} else if ((balanceSum != null) && (balanceSum.isEmpty() == false)) {
			payId = balanceSum.get(0).getPay_id();
		} else {
			LOGGER.info("write off detail and sum source balance is empty");
			return;
		}
		List<InfoPayBalance> ipbs = S.get(InfoPayBalance.class)
				.query(Condition.build("queryInit").filter("pay_id",
						payId));
		for (InfoPayBalance info : ipbs) {
			filter.clear();
			filter.put("balance_id", info.getBalance_id());
			S.get(InfoPayBalance.class).batch(
					Condition.build("initRealBalance").filter(filter));
		}
		
		//金钱账单  月结销账不返回资源类明细
		BilActBill bab = new BilActBill();
		long billing_id = 0;
		long balance = 0;
		String writeOffOperateId = "MONTH_END_" + EndQuery.GetOperatorId();
		billing_id = EndQuery.GetBillingId();
		gb.doGroupByItem(writeOffDetail, list_item);
		filter.clear();
		
		for(ItemWriteOff item : list_item) {
			bab.setBilling_id(billing_id);
			bab.setUser_id(item.getUser_Id());
			bab.setPay_id(item.getPay_Id());
			bab.setAcct_month(item.getAcct_month());
			bab.setAcct_item_code(item.getAcct_item_code());
			bab.setUnit_type_id(item.getUnit_type_id());
			bab.setFee(item.getFee());
			bab.setDiscount_fee(item.getDiscount_fee());
			bab.setAdjust_before(item.getAdjust_fee());
			bab.setAdjust_after(0);
			bab.setWrite_off_fee(item.getWriteoff_fee());
			long owe_fee = item.getFee() - item.getWriteoff_fee();
			if(item.getUnit_type_id() == BasicType.UNIT_TYPE_MONEY) {
				bab.setOwe_fee(owe_fee);
				if(owe_fee > 0) {
					bab.setOwe_tag(BasicType.END_OWE_TAG_Y);
				} else {
					bab.setOwe_tag(BasicType.END_OWE_TAG_N);
				}
				bab.setInvoice_tag(BasicType.END_INVOICE_TAG_N);
				bab.setInvoice_fee(item.getInvoice_fee());
				bab.setInvoice_time(null);
			} else {
				bab.setOwe_fee(0);
				bab.setOwe_tag(BasicType.END_OWE_TAG_N);
				bab.setInvoice_tag(BasicType.END_INVOICE_TAG_N);
				bab.setInvoice_fee(item.getInvoice_fee());
				bab.setInvoice_time(null);
			}
			bab.setPre_fee(0); //alter 赠款
			bab.setLate_fee(0); //alter 滞纳金
			bab.setInsert_date(insert_date);
			bab.setUpdate_time(insert_date);
			S.get(BilActBill.class).create(bab);
		}
		//资源账单
		gb.doGroupResourceByItem(balanceSum, list_item);
		filter.clear();
		for(ItemWriteOff item : list_item) {
			bab.setBilling_id(billing_id);
			bab.setUser_id(item.getUser_Id());
			bab.setPay_id(item.getPay_Id());
			bab.setAcct_month(item.getAcct_month());
			bab.setAcct_item_code(item.getAcct_item_code());
			bab.setUnit_type_id(item.getUnit_type_id());
			bab.setFee(item.getWriteoff_fee());//资源类账单费用==销账费用
			bab.setDiscount_fee(item.getDiscount_fee());
			bab.setAdjust_before(item.getAdjust_fee());
			bab.setAdjust_after(0);
			bab.setWrite_off_fee(item.getWriteoff_fee());
			long owe_fee = item.getFee() - item.getWriteoff_fee();
			if(item.getUnit_type_id() == BasicType.UNIT_TYPE_MONEY) {
				bab.setOwe_fee(owe_fee);
				if(owe_fee > 0) {
					bab.setOwe_tag(BasicType.END_OWE_TAG_Y);
				} else {
					bab.setOwe_tag(BasicType.END_OWE_TAG_N);
				}
				bab.setInvoice_tag(BasicType.END_INVOICE_TAG_N);
				bab.setInvoice_fee(item.getInvoice_fee());
				bab.setInvoice_time(null);
			} else {
				bab.setOwe_fee(0);
				bab.setOwe_tag(BasicType.END_OWE_TAG_N);
				bab.setInvoice_tag(BasicType.END_INVOICE_TAG_N);
				bab.setInvoice_fee(item.getInvoice_fee());
				bab.setInvoice_time(null);
			}
			bab.setPre_fee(0); //alter 赠款
			bab.setLate_fee(0); //alter 滞纳金
			bab.setInsert_date(insert_date);
			bab.setUpdate_time(insert_date);
			S.get(BilActBill.class).create(bab);
		}
		//金钱账本
		BilActAccesslog baal = new BilActAccesslog();
		gb.doGroupByBalance(writeOffDetail, list_bal);
		filter.clear();
		for(BalanceWriteOff bal : list_bal) {
			//存取款记录
			balance = EndQuery.GetBalance(bal.getBalance_id());
			baal.setOperate_id(writeOffOperateId);
			baal.setOperate_type(BasicType.OPERATE_TYPE_WRITEOFF);
			baal.setPartition_id(Integer.parseInt(patitionNo));
			baal.setPay_id(payId);
			baal.setBalance_id(bal.getBalance_id());
			baal.setBalance_type_id(bal.getBalance_type_id());
			baal.setAccess_tag(BasicType.ACCESS_TAG_DRAW);
			baal.setMoney(bal.getWriteoff_fee());
			baal.setOld_balance(balance);
			baal.setNew_balance(balance - bal.getWriteoff_fee());
			baal.setLocal_net(null);
			S.get(BilActAccesslog.class).create(baal);
			
			if(bal.getBalance_id() == BasicType.UNKNOWN_BALANCE_ID) {
				continue;
			}
			filter.put("writeoff_fee", bal.getWriteoff_fee());
			filter.put("balance_id", bal.getBalance_id());
			
			S.get(InfoPayBalance.class).batch(Condition.build("update4MonthEnd").filter(filter));
		}

		//销账日志 金钱
		LogActWriteoff lawo = new LogActWriteoff();
		for(WriteOffDetail wod : writeOffDetail){
			if(wod.getUnit_type_id() != BasicType.UNIT_TYPE_MONEY) {
				continue;
			}
			lawo.setBilling_id(billing_id);
			lawo.setUser_id(wod.getUser_Id());
			lawo.setPay_id(wod.getPay_Id());
			lawo.setAcct_month(wod.getAcct_month());
			lawo.setPartition_num(patitionNo);
			lawo.setAcct_item_code(wod.getAcct_item_code());
			lawo.setUnit_type_id(wod.getUnit_type_id());
			lawo.setFee(wod.getFee());
			lawo.setBalance_id(wod.getBalance_id());
			lawo.setBalance_type_id(wod.getBalance_type_id());
			lawo.setWriteoff_fee(wod.getWriteoff_fee());
			lawo.setOld_fee(wod.getBefore_fee());
			lawo.setNew_fee(wod.getAfter_fee());
			lawo.setOld_balance(wod.getBefore_balance());
			lawo.setNew_balance(wod.getAfter_balance());
			lawo.setUpdate_time(EndQuery.GetNow());
			S.get(LogActWriteoff.class).create(lawo);	
		}
		//销账日志 资源
		for(BilActBalanceAddUp babau : balanceSum) {
			balance = EndQuery.GetBalance(babau.getBalance_id());
			lawo.setBilling_id(billing_id);
			lawo.setUser_id(babau.getUser_id());
			lawo.setPay_id(babau.getPay_id());
			lawo.setAcct_month(babau.getAcct_month());
			lawo.setPartition_num(patitionNo);
			lawo.setAcct_item_code(babau.getAcct_item_code());
			lawo.setUnit_type_id(babau.getUnit_type_id());
			lawo.setFee(babau.getDeduct_fee());
			lawo.setBalance_id(babau.getBalance_id());
			lawo.setBalance_type_id(babau.getBalance_type_id());
			lawo.setWriteoff_fee(babau.getDeduct_fee());
			lawo.setOld_fee(babau.getDeduct_fee());
			lawo.setNew_fee(0);
			lawo.setOld_balance(balance);
			lawo.setNew_balance(balance - babau.getDeduct_fee());
			lawo.setUpdate_time(EndQuery.GetNow());
			S.get(LogActWriteoff.class).create(lawo);
		}
		
		//资源账本
		gb.doGroupByResourceBalance(balanceSum, list_bal);
		filter.clear();
		for(BalanceWriteOff bal : list_bal) {
			//存取款记录
			balance = EndQuery.GetBalance(bal.getBalance_id());
			baal.setOperate_id(writeOffOperateId);
			baal.setOperate_type(BasicType.OPERATE_TYPE_WRITEOFF);
			baal.setPartition_id(Integer.parseInt(patitionNo));
			baal.setPay_id(payId);
			baal.setBalance_id(bal.getBalance_id());
			baal.setBalance_type_id(bal.getBalance_type_id());
			baal.setAccess_tag(BasicType.ACCESS_TAG_DRAW);
			baal.setMoney(bal.getWriteoff_fee());
			baal.setOld_balance(balance);
			baal.setNew_balance(balance - bal.getWriteoff_fee());
			baal.setLocal_net(null);
			S.get(BilActAccesslog.class).create(baal);
			
			filter.put("writeoff_fee", bal.getWriteoff_fee());
			filter.put("balance_id", bal.getBalance_id());
			
			S.get(InfoPayBalance.class).batch(Condition.build("update4MonthEnd").filter(filter));
		}
	}
	
	public int modifyEndStatus(EndInfoUser user) {
		return S.get(EndInfoUser.class).update(user);
	}
}
