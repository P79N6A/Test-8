package com.tydic.beijing.billing.credit;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import com.tydic.beijing.billing.credit.common.BasicException;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;

public class Test {

	public static void main(String[] args) throws BasicException, Exception {

		MemcachedClient mc = new MemcachedClient(AddrUtil.getAddresses("172.168.1.211:11211"));

		UserInfoForMemCached userinfoForMemcached = (UserInfoForMemCached) mc.get("17090131309");
		if (userinfoForMemcached == null) {
			System.out.println(123);
		} else {
			System.err.println(userinfoForMemcached.getInfoUser().getUser_id());
		}

		mc.shutdown();
		// ClassPathXmlApplicationContext ctx = SpringContextUtil.getContext();
		// ctx.start();
		// // get billing db info
		// DBInfo billing = (DBInfo) ctx.getBean("BillingDBInfo");
		// // get crm db info
		// DBInfo crm = (DBInfo) ctx.getBean("CrmDBInfo");
		//
		// CreditAction action = new CreditAction();
		//
		// action.setId(94732984L);
		// action.setUser_id("123");
		// action.setPay_id("456");
		// action.setReason("50");
		// action.setAction_time("20141118151400");
		// action.setLocal_net("777");
		// action.setMem_key(CreditAction.KEY_PREFIX);
		//
		// LogCreditAction logAction = new LogCreditAction();
		// logAction.setUser_id("123");
		// logAction.setPay_id("456");
		// logAction.setCredit_number(888);
		// logAction.setHistory(123);
		// logAction.setRealtime(123);
		// logAction.setReason("50");
		// logAction.setAction_time("20141118151400");
		// logAction.setSource("1");// source from fil
		//
		// TableOpers to = (TableOpers) ctx.getBean("TableOpers");
		// // to.insertAction(action, logAction);
		// List<QCreditInfoUserCredit> list = null;
		//
		// for (int i = 0; i < 10; i++) {
		// list = DBKit.getQCreditInfoUserCredit("10", i + "");
		// for (QCreditInfoUserCredit q : list) {
		// System.err.println(i);
		// System.err.println(q.toString());
		// }
		// }
		//
		// ctx.close();

	}

}
