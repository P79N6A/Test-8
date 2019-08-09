package com.tydic.beijing.billing.account.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.billing.account.biz.Sum;
import com.tydic.beijing.billing.account.type.BalanceInfo;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.account.type.ResourceInfo;
import com.tydic.beijing.billing.account.type.SumConf;
import com.tydic.beijing.billing.account.type.TariffInfo;
import com.tydic.beijing.billing.dao.BilActAddUp;
import com.tydic.beijing.billing.dao.BilActBalanceAddUp;
import com.tydic.beijing.billing.dao.BilActResourceAddUp;
import com.tydic.beijing.billing.dao.LogUsedBalanceLog;
import com.tydic.beijing.billing.dao.QAcctProcess;

public class SumCharge extends MyApplicationContextUtil {

	private final static Logger LOGGER = Logger.getLogger(SumCharge.class);
	private Vector<LogUsedBalanceLog> vec_lubl;
	private Vector<BilActAddUp> vec_baau;//增量数据
	private List<BilActAddUp> list_baau;//全量数据
	private Vector<BilActBalanceAddUp> vec_babau;
	private Vector<BilActResourceAddUp> vec_barau;

	@Autowired
	private SumConf conf_sum;
	@Autowired
	public Sum sum;

	public Sum getSum() {
		return sum;
	}

	public void setSum(Sum sum) {
		this.sum = sum;
	}

	public void getObject() {
		vec_lubl = new Vector<LogUsedBalanceLog>();
		vec_baau = new Vector<BilActAddUp>();
		list_baau = new ArrayList<BilActAddUp>();
		vec_babau = new Vector<BilActBalanceAddUp>();
		vec_barau = new Vector<BilActResourceAddUp>();
	}

	public int doSum(QAcctProcess qap) {
		String user_id = qap.getUser_id();
		int service_scenarious = qap.getService_scenarious();
		Vector<TariffInfo> vec_t = getTariffInfo(qap.getTariff_info());
		Vector<BalanceInfo> vec_b = getBalanceInfo(qap.getBalance_info());
		Vector<ResourceInfo> vec_r = getResourceInfo(qap.getResource_info());
		sum(user_id, qap.getAcct_month(), vec_t, vec_b, vec_r, service_scenarious, qap.getSession_id());
		if(vec_t.size() == 0) {
			return -1;
		}
		return 0;
	}

	private Vector<BalanceInfo> getBalanceInfo(String balance_info) {
		Vector<BalanceInfo> vec = new Vector<BalanceInfo>();
		if(balance_info == null) {
			return vec;
		}
		String balanceStr = balance_info;
		String[] balanceInfo = balanceStr.split(conf_sum.getBalance_split1());
		for(String b_info : balanceInfo) {
			BalanceInfo bi = (BalanceInfo) mycontext.getBean("BalanceInfo");
			String balance_semicolon[] = b_info.split(conf_sum.getBalance_split2());
			bi.setPay_id(balance_semicolon[0]);
			bi.setBalance_id(Long.parseLong(balance_semicolon[1]));
			String balance_comma[] = balance_semicolon[2].split(conf_sum.getBalance_split3());
			bi.setUnit_type_id(Integer.parseInt(balance_comma[0]));
			bi.setFee(Integer.parseInt(balance_comma[1]));
			bi.setBalance_type_id(Integer.parseInt(balance_comma[3]));
			bi.setAcct_item_code(Integer.parseInt(balance_comma[4]));
			vec.addElement(bi);
			LOGGER.debug(bi.toString());
		}
		return vec;
	}

	private Vector<TariffInfo> getTariffInfo(String tariff_info) {

		// TariffInfo ti =
		// (TariffInfo)MyApplicationContextUtil.getContext().getBean("TariffInfo");
		Vector<TariffInfo> vec = new Vector<TariffInfo>();

		String[] tariff = tariff_info.split(conf_sum.getTariff_split1());// 费用组分割
		for (String s : tariff) {
			TariffInfo ti = (TariffInfo) mycontext.getBean("TariffInfo");
			String[] info = s.split(conf_sum.getTariff_split2());// 费用分割
			ti.setTariiff_id(Integer.parseInt(info[0]));
			ti.setFee(Long.parseLong(info[1]));
			if(ti.getFee() == 0) {
				continue;
			}
			LOGGER.debug("tariff:" + ti.getTariiff_id() + " " + ti.getFee());
			vec.addElement(ti);
		}
		return vec;
	}

	private Vector<ResourceInfo> getResourceInfo(String resource_info) {
		Vector<ResourceInfo> vec = new Vector<ResourceInfo>();
		if (resource_info == null || resource_info.isEmpty()) {
			return vec;
		}
		
		String[] resource = resource_info.split(conf_sum.getResource_split1());// 资源组分割
		for (String s : resource) {
			ResourceInfo ri = (ResourceInfo) mycontext.getBean("ResourceInfo");
			String[] info = s.split(conf_sum.getResource_split2());// 资源分割
			ri.setResource_id(Long.parseLong(info[0]));
			ri.setResource_value(Long.parseLong(info[1]));
			if(ri.getResource_value() == 0) {
				continue;
			}
			LOGGER.debug("resource:" + ri.getResource_id() + " " + ri.getResource_value());
			vec.addElement(ri);
		}
		return vec;
		
	}
	
	private void sum(String user_id, int acct_month, Vector<TariffInfo> vec_t,
			Vector<BalanceInfo> vec_b, Vector<ResourceInfo> vec_r, 
			int service_scenarious, String session_id) {
		long runtime0 = System.currentTimeMillis();
		String time_stamp = new String();
		String sys_date = new String();
		// 清空vector
		clear_vec();
		// 获得时间戳
		// 系统时间
		// 查找帐务月
		// 查找资源类型
		long runtime1 = System.currentTimeMillis();
		try {
			sys_date = sum.query(user_id, acct_month, time_stamp, sys_date,
					vec_t, vec_b, vec_r, service_scenarious, vec_lubl, vec_baau, vec_babau, vec_barau);
		} catch (Exception e) {
			LOGGER.error("error : " + e.toString());
		}

		// 写预占记录表
		// 更新累账表
		// 更新用户使用量表
		// 跟新q触发表状态
		long runtime2 = System.currentTimeMillis();
		sum.update(vec_lubl, vec_baau, vec_babau, vec_barau, session_id, sys_date, list_baau);
		long runtime3 = System.currentTimeMillis();

		LOGGER.debug("Sum function time :["
				+ Long.toString(runtime3 - runtime0) + "]");
		LOGGER.debug("query DB time :[" + Long.toString(runtime2 - runtime1)
				+ "]");
		LOGGER.debug("update DB time :[" + Long.toString(runtime3 - runtime2)
				+ "]");
	}

	private void clear_vec() {
		vec_lubl.clear();
		vec_baau.clear();
		list_baau.clear();
		vec_babau.clear();
		vec_barau.clear();
	}

	public Vector<LogUsedBalanceLog> getVec_lubl() {
		return vec_lubl;
	}

	public void setVec_lubl(Vector<LogUsedBalanceLog> vec_lubl) {
		this.vec_lubl = vec_lubl;
	}

	public Vector<BilActAddUp> getVec_baau() {
		return vec_baau;
	}

	public void setVec_baau(Vector<BilActAddUp> vec_baau) {
		this.vec_baau = vec_baau;
	}

	public SumConf getConf_sum() {
		return conf_sum;
	}

	public void setConf_sum(SumConf conf_sum) {
		this.conf_sum = conf_sum;
	}
	
	public List<BilActAddUp> getList_baau() {
		return list_baau;
	}
}
