package com.tydic.beijing.billing.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.dao.BilActUserRealTimeBill;
import com.tydic.beijing.billing.dao.BilActUserRealTimeBillForMemcached;
import com.tydic.beijing.billing.dao.InfoPay;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.PayUserRelForMemCached;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.uda.service.S;

public class TestGetMem {

	private final static Logger LOGGER = Logger.getLogger(TestGetMem.class);

	public static void main(String[] args) {

		// vds.sql.pages(2, 20);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "uda.xml" });

		LOGGER.debug("test");
		String device_num = args[0];
		String user_id = args[1];
		System.out.println(device_num);
		System.out.println(user_id);

		InfoUser infouser = null;
		List<LifeUserProduct> l_LifeUserProdect = new ArrayList<LifeUserProduct>();
		List<PayUserRel> l_PayUserRel = new ArrayList<PayUserRel>();
		List<InfoPay> l_InfoPay = new ArrayList<InfoPay>();

		// S.get(UserInfoForMemCached.class).create(uifm);
		// S.get(AcctMonth.class).update(ac);
		// S.get(AcctMonth.class).remove("key1");

		UserInfoForMemCached uifm = S.get(UserInfoForMemCached.class).get(
				device_num);// 17090137049 17090135101
		System.out.println("===========================================");
		LOGGER.debug("---------begin UserInfoForMemCached---------");
		if (uifm != null) {
			infouser = uifm.getInfoUser();
			l_LifeUserProdect = uifm.getUserProducts();
			l_PayUserRel = uifm.getPayUserRels();
			l_InfoPay = uifm.getUserInfoPays();

			System.out.println("==infouser==");
			System.out.println(infouser);
			System.out.println("==LifeUserProduct==");
			for (LifeUserProduct lup : l_LifeUserProdect) {
				System.out.println(lup);
			}
			System.out.println("==PayUserRel==");
			for (PayUserRel lup : l_PayUserRel) {
				System.out.println(lup);
			}

			System.out.println("==InfoPay==");
			for (InfoPay ip : l_InfoPay) {
				System.out.println(ip);
			}
		}
		LOGGER.debug("---------end UserInfoForMemCached---------");

		PayUserRelForMemCached lupfm = S.get(PayUserRelForMemCached.class).get(
				BasicType.MEMCACHED_CLUSTER_TAG_1 + user_id);// 579037 405342

		LOGGER.debug("---------begin PayUserRelForMecCached---------");
		if (lupfm != null) {
			System.out.println("==user_id==");
			System.out.println(lupfm.getUser_id());
			System.out.println("==PayUserRel==");
			for (PayUserRel lup : lupfm.getUserPayUserRels()) {
				System.out.println(lup);
			}
		}

		LOGGER.debug("---------end PayUserRelForMecCached---------");

		BilActUserRealTimeBillForMemcached baurtbfm = S.get(
				BilActUserRealTimeBillForMemcached.class).get(
				BasicType.MEMCACHED_CLUSTER_TAG_1
						+ BasicType.MEMCACHED_BILL_PREFIX + user_id);

		LOGGER.debug("---------begin BilActUserRealTimeBillForMemcached---------");
		if (baurtbfm != null) {
			System.out.println("==user_id==");
			System.out.println(baurtbfm.getUser_id());
			System.out.println("==BilActUserRealTimeBill==");
			for (BilActUserRealTimeBill lup : baurtbfm.getL_userbill()) {
				System.out.println(lup);
			}
		}
		LOGGER.debug("---------end BilActUserRealTimeBillForMemcached---------");
		System.out.println("===========================================");
		context.close();
	}

}
