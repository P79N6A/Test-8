package com.tydic.beijing.billing.account.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.core.Dao2File;
import com.tydic.beijing.billing.account.type.BalanceWriteOff;
import com.tydic.beijing.billing.account.type.ItemWriteOff;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.account.type.WriteOffDetail;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.BilActRealTimeBill;
import com.tydic.beijing.billing.dao.BilActRealTimeBillForFile;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.LogActPreWriteoff;
import com.tydic.beijing.billing.dao.LogActPreWriteoffForFile;
import com.tydic.beijing.billing.dao.LogUsedBalanceLog;
import com.tydic.beijing.billing.util.GroupBy;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class Simulate extends MyApplicationContextUtil {
	private final static Logger LOGGER = Logger.getLogger(Simulate.class);
	GroupBy gb;
	List<BalanceWriteOff> list_bal;
	List<ItemWriteOff> list_item;
	
	List<BilActRealTimeBill> l_item_tmp;
	List<LogActPreWriteoff> l_log_tmp;
	BilActRealTimeBillForFile itemForFile;
	LogActPreWriteoffForFile logForFile;
	Map<String, Object> filter;
	public Simulate() {
		gb = new GroupBy();
		//list在gb.doGroup中clean
		list_bal = new ArrayList<BalanceWriteOff>();
		list_item = new ArrayList<ItemWriteOff>();
		filter = new HashMap<String, Object>();
		
		l_item_tmp = new ArrayList<BilActRealTimeBill>();
		l_log_tmp = new ArrayList<LogActPreWriteoff>();
		itemForFile = new BilActRealTimeBillForFile();
		logForFile = new LogActPreWriteoffForFile();
	}

	//public void update(List<LogUsedBalanceLog> usedBalance, List<WriteOffDetail> detail, String update_time, String partition_num, List<List<BilActRealTimeBill>> ll_item, List<List<LogActPreWriteoff>> ll_log) throws BasicException {
	public void update(List<LogUsedBalanceLog> usedBalance, List<WriteOffDetail> detail, String update_time, String partition_num, Dao2File d2f_item, Dao2File d2f_log) throws BasicException, IOException {
		/*
		//JD余额模式不使用used_balance
		//更新LogUsedBalanceLog的process_tag和insert_date
		LogUsedBalanceLog treated = new LogUsedBalanceLog();
		treated.setProcess_tag(CodeDefine.TREATED);
		String timestamps = new String();
		for(Iterator<LogUsedBalanceLog> iter=usedBalance.iterator(); iter.hasNext(); ) {
			//此处用于uda解析sql的时候无法正确组装字符串的问题，迫不得已。
			timestamps += "'" + iter.next().getInsert_timestamp() + "'";
			if(iter.hasNext()) {
				timestamps += ", ";	
			}
		}
		if(usedBalance.size() > 0) {
			S.get(LogUsedBalanceLog.class).batch(Condition.build("updateByInsertTimestamp").filter("timestamps", timestamps), treated);
			//同时进行 used_balance 费用的释放，如此后面修改 real_balance 时就不需要关注used了
		}
		*/
		
		//账本恢复 real_balance = balance JD模型临时方案
		if(detail.size() == 0 || detail.isEmpty()) {
			LOGGER.info("write off detail is empty");
			return;
		}
		String payId = detail.get(0).getPay_Id();
		List<InfoPayBalance> ipbs = S.get(InfoPayBalance.class).query(
				Condition.build("queryInitRealBalance").filter("pay_id", payId));
		for(InfoPayBalance info : ipbs) {
			filter.clear();
			filter.put("balance_id", info.getBalance_id());
			S.get(InfoPayBalance.class).batch(Condition.build("initRealBalance").filter(filter));
		}
		
		//修改账本余额，以balance为基准，通过writeoff修改real_balance
		gb.doGroupByBalance(detail, list_bal);
		filter.clear();
		for(BalanceWriteOff bal : list_bal) {
			if(bal.getBalance_id() == BasicType.UNKNOWN_BALANCE_ID) {
				continue;
			}
			filter.put("writeoff_fee", bal.getWriteoff_fee());
			filter.put("balance_id", bal.getBalance_id());
			
			int res = S.get(InfoPayBalance.class).batch(Condition.build("updateByBalanceId").filter(filter));
			if (res == 0) {
				System.out.println("========== update RE_WRITEOFF");
				throw new BasicException(ErrorCode.RE_WRITEOFF, "the balance_id[" + bal.getBalance_id() + "] need re_simulate");
			}
		}
		
		//修改用户账户实时账单
		gb.doGroupByItem(detail, list_item);
		filter.clear();
		for(ItemWriteOff item : list_item) {
			
			BilActRealTimeBill rartb = new BilActRealTimeBill();
			rartb.setUser_id(item.getUser_Id());
            rartb.setPay_id(item.getPay_Id());
            rartb.setAcct_month(item.getAcct_month());
            rartb.setPartition_num(partition_num);
            rartb.setAcct_item_code(item.getAcct_item_code());
            rartb.setUnit_type_id(item.getUnit_type_id());
            rartb.setOrg_fee(item.getOrg_fee());
            rartb.setAdjust_before(item.getAdjust_fee());
            rartb.setDiscount_fee(item.getDiscount_fee());
            rartb.setFee(item.getFee());
            if(item.getUnit_type_id() == BasicType.UNIT_TYPE_MONEY) {
            	rartb.setNon_deduct_fee(item.getFee() - item.getWriteoff_fee());
            } else {
            	rartb.setNon_deduct_fee(0);
            }
            rartb.setInsert_date(update_time);
            l_item_tmp.add(rartb);
            //d2f_item.writeFile(rartb);
			
            //写文件同时插表，测试需要，以后注释掉
			/*
			filter.put("partition_num", partition_num);
			filter.put("user_id", item.getUser_Id());
			filter.put("pay_id", item.getPay_Id());
			filter.put("acct_month", item.getAcct_month());
			filter.put("acct_item_code", item.getAcct_item_code());
			//BilActRealTimeBill rartb = new BilActRealTimeBill();
			List<BilActRealTimeBill> l_result = 
					S.get(BilActRealTimeBill.class).query(Condition.build("queryBilActRealTimeBill").filter(filter));
			if (l_result.size() == 0) {
				//insert
                rartb.setUser_id(item.getUser_Id());
                rartb.setPay_id(item.getPay_Id());
                rartb.setAcct_month(item.getAcct_month());
                rartb.setPartition_num(partition_num);
                rartb.setAcct_item_code(item.getAcct_item_code());
                rartb.setUnit_type_id(item.getUnit_type_id());
                rartb.setFee(item.getFee());
                rartb.setNon_deduct_fee(item.getFee() - item.getWriteoff_fee());
                rartb.setInsert_date(update_time);
				S.get(BilActRealTimeBill.class).create(rartb);
			} else if (l_result.size() == 1) {
				//update
				rartb.setUser_id(item.getUser_Id());
                rartb.setPay_id(item.getPay_Id());
                rartb.setAcct_month(item.getAcct_month());
                rartb.setPartition_num(partition_num);
                rartb.setAcct_item_code(item.getAcct_item_code());
                rartb.setUnit_type_id(item.getUnit_type_id());
                rartb.setFee(item.getFee());
                rartb.setNon_deduct_fee(item.getFee() - item.getWriteoff_fee());
                rartb.setInsert_date(update_time);
				S.get(BilActRealTimeBill.class).update(rartb);
			} else {
				throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL, "BilActRealTimeBill has many line on the same \'partition_num user_id pay_id acct_month acct_item_code\'");
			}
			*/
		}
		itemForFile.setList(l_item_tmp);
		d2f_item.writeFile(itemForFile, l_item_tmp.size());

		
		//更新实时用户账户实时账单和插入模拟销账记录
		WriteOffDetail tmp = new WriteOffDetail();
		for(Iterator<WriteOffDetail> iter=detail.iterator(); iter.hasNext(); ) {
			tmp = iter.next();
			
			if(tmp.getUnit_type_id() != BasicType.UNIT_TYPE_MONEY) {
				continue;
			}
			//插入销账记录
			LogActPreWriteoff lapwf = new LogActPreWriteoff();
			lapwf.setUser_id(tmp.getUser_Id());
			lapwf.setPay_id(tmp.getPay_Id());
			lapwf.setAcct_month(tmp.getAcct_month());
			lapwf.setPartition_num(partition_num);
			lapwf.setAcct_item_code(tmp.getAcct_item_code());
			lapwf.setUnit_type_id(tmp.getUnit_type_id());
			lapwf.setFee(tmp.getFee());
			lapwf.setBalance_id(tmp.getBalance_id());
			lapwf.setBalance_type_id(tmp.getBalance_type_id());
			lapwf.setWriteoff_fee(tmp.getWriteoff_fee());
			lapwf.setOld_fee(tmp.getBefore_fee());
			lapwf.setNew_fee(tmp.getAfter_fee());
			lapwf.setOld_real_balance(tmp.getBefore_balance());
			lapwf.setNew_real_balance(tmp.getAfter_balance());
			lapwf.setUpdate_time(update_time);
			l_log_tmp.add(lapwf);
			//d2f_log.writeFile(lapwf);

			//写文件同时插表，测试需要，以后注释掉
			//S.get(LogActPreWriteoff.class).create(lapwf);
		}
		//d2f_log.writeFile(l_log_tmp);
		logForFile.setList(l_log_tmp);
		d2f_log.writeFile(logForFile, l_log_tmp.size());
	}
}
