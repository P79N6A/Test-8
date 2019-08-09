package com.tydic.beijing.billing.credit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.credit.dao.InfoUserCredit;
import com.tydic.beijing.billing.credit.memcache.dao.BilActBill4CreditMemcache;
import com.tydic.beijing.billing.credit.memcache.dao.BilActRealTimeBill4CreditMemcache;
import com.tydic.beijing.billing.credit.memcache.dao.CreditActionMemcache;
import com.tydic.beijing.billing.credit.memcache.dao.InfoUser4CreditMemcache;
import com.tydic.beijing.billing.credit.memcache.dao.InfoUserCreditMemcache;
import com.tydic.uda.service.S;

public class Check {

	public static void main(String[] args) {

		if (args.length < 3) {
			System.out.println("args < 3");
			usage(args);
			System.exit(-1);
		}
		String tableName = args[0].toLowerCase();
		String userId = args[1];
		String payId = args[2];
		System.out.println("table_name:" + tableName);
		System.out.println("userId:" + userId);
		System.out.println("payId:" + payId);
		ClassPathXmlApplicationContext ctx = SpringContextUtil.getContext();
		ctx.start();
		if (tableName.compareTo("credit_action") == 0) {
			checkCreditAction(userId, payId);
		} else if (tableName.compareTo("info_user_credit") == 0) {
			checkInfoUserCredit(userId);
		} else if (tableName.compareTo("bil_act_real_time_bill") == 0) {
			checkRealtimeBill(userId, payId);
		} else if (tableName.compareTo("bil_act_bill") == 0) {
			checkHistoryBill(userId, payId);
		} else if (tableName.compareTo("info_user") == 0) {
			checkInfoUser(userId);
		} else {
			usage(args);
		}
		ctx.close();
	}

	public static void usage(String[] args) {
		System.out.println("usage: Check" + " table_name user_id pay_id");
		System.out
				.println("table_name:\ncredit_action\ninfo_user_credit\nbil_act_real_time_bill\nbil_act_bill\ninfo_user");
	}

	public static void checkCreditAction(String userId, String reason) {
		CreditActionMemcache action = new CreditActionMemcache();
		action.setUser_id(userId);
		action.setReason(reason);
		action.setMem_key(CreditActionMemcache.KEY_PREFIX);
		CreditActionMemcache ret = S.get(CreditActionMemcache.class).get(action.getMem_key());

		if (ret == null) {
			System.out.println("####################################################");
			System.out.println("not found........");
			System.out.println("####################################################");
		} else {
			System.out.println("####################################################");
			System.out.println(ret.toString());
			System.out.println("####################################################");
		}
	}

	public static void checkInfoUserCredit(String userId) {

		InfoUserCreditMemcache credit = new InfoUserCreditMemcache();
		credit.setMem_key(InfoUserCreditMemcache.KEY_PREFIX + userId);
		InfoUserCreditMemcache ret = S.get(InfoUserCreditMemcache.class).get(credit.getMem_key());

		if (ret == null) {
			System.out.println("####################################################");
			System.out.println("not found........");
			System.out.println("####################################################");
		} else {
			System.out.println("####################################################");
			for (InfoUserCredit e : ret.getInfoUserCreditList()) {
				System.out.println(e.toString());
			}
			System.out.println("####################################################");
		}
	}

	public static void checkRealtimeBill(String userId, String payId) {

		BilActRealTimeBill4CreditMemcache bilReal = new BilActRealTimeBill4CreditMemcache();
		bilReal.setUser_id(userId);
		bilReal.setPay_id(payId);
		bilReal.setMem_key(BilActRealTimeBill4CreditMemcache.KEY_PREFIX);
		BilActRealTimeBill4CreditMemcache ret = S.get(BilActRealTimeBill4CreditMemcache.class).get(
				bilReal.getMem_key());

		if (ret == null) {
			System.out.println("####################################################");
			System.out.println("not found........");
			System.out.println("####################################################");
		} else {
			System.out.println("####################################################");
			System.out.println(ret.toString());
			System.out.println("####################################################");
		}
	}

	public static void checkHistoryBill(String userId, String payId) {
		BilActBill4CreditMemcache bil = new BilActBill4CreditMemcache();
		bil.setUser_id(userId);
		bil.setPay_id(payId);
		bil.setMem_key(BilActBill4CreditMemcache.KEY_PREFIX);
		System.out.println(bil.getMem_key());
		BilActBill4CreditMemcache ret = S.get(BilActBill4CreditMemcache.class)
				.get(bil.getMem_key());

		if (ret == null) {
			System.out.println("####################################################");
			System.out.println("not found........");
			System.out.println("####################################################");
		} else {
			System.out.println("####################################################");
			System.out.println(ret.toString());
			System.out.println("####################################################");
		}
	}

	public static void checkInfoUser(String userId) {

		InfoUser4CreditMemcache infoUser = new InfoUser4CreditMemcache();
		infoUser.setUser_id(userId);
		infoUser.setMem_key(InfoUser4CreditMemcache.KEY_PREFIX);
		InfoUser4CreditMemcache ret = S.get(InfoUser4CreditMemcache.class).get(
				infoUser.getMem_key());

		if (ret == null) {
			System.out.println("####################################################");
			System.out.println("not found........");
			System.out.println("####################################################");
		} else {
			System.out.println("####################################################");
			System.out.println(ret.toString());
			System.out.println("####################################################");
		}
	}

}
