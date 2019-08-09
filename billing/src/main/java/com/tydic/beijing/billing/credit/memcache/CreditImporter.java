/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.credit.memcache;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.credit.SpringContextUtil;

/**
 * 信控--memcache导入<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class CreditImporter {

	private static final Logger Log = Logger.getLogger(CreditImporter.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = SpringContextUtil.getContext();

		// get billing db info
		DBInfo billing = (DBInfo) ctx.getBean("BillingDBInfo");
		// get crm db info
		DBInfo crm = (DBInfo) ctx.getBean("CrmDBInfo");

		String logDir = System.getProperty("CREDIT.LOG.DIR");
		String logName = System.getProperty("CREDIT.LOG.NAME");
		String strSplit = System.getProperty("SPLIT");

		if (logDir == null) {
			// windows 不好使，需要在jvm上-D设置
			System.setProperty("CREDIT.LOG.DIR", "./");
			logDir = "./";
		}

		if (logName == null) {
			// windows 不好使，需要在jvm上-D设置
			System.setProperty("CREDIT.LOG.NAME", "credit");
			logName = "ua";
		}

		int SPLIT = 0;
		try {
			SPLIT = Integer.parseInt(strSplit);
		} catch (NumberFormatException e) {
			SPLIT = 5;
		}

		Log.info("split size=" + SPLIT);
		Log.info("log dir=" + logDir);
		Log.info("log name=" + logName);
		BilActBillImporter[] bilActBill = new BilActBillImporter[SPLIT];
		BilActRealTimeBillImporter[] bilActRealTime = new BilActRealTimeBillImporter[SPLIT];
		CreditActionImporter[] cAction = new CreditActionImporter[SPLIT];
		// InfoPayBalanceImporter[] infoPayBalance = new
		// InfoPayBalanceImporter[SPLIT];
		InfoUserImpoter[] infoUser = new InfoUserImpoter[SPLIT];
		// PayUserRelImporter[] payUserRel = new PayUserRelImporter[SPLIT];
		InfoUserCreditHelpImporter[] infoUserCreditHelp = new InfoUserCreditHelpImporter[SPLIT];

		final int SERVICE = 5;
		Thread[][] thread = new Thread[SERVICE][SPLIT];

		long start = System.currentTimeMillis();
		int index = -1;

		// BilActBillImporter*****
		index++;
		for (int i = 0; i < SPLIT; i++) {
			bilActBill[i] = new BilActBillImporter(billing, SPLIT, i);
			thread[index][i] = new Thread(bilActBill[i], "BilActBillImporter-" + i);
		}

		// BilActRealTimeBillImporter*****
		index++;
		for (int i = 0; i < SPLIT; i++) {
			bilActRealTime[i] = new BilActRealTimeBillImporter(billing, SPLIT, i);
			thread[index][i] = new Thread(bilActRealTime[i], "BilActRealTimeBillImporter-" + i);
		}

		// CreditActionImporter*****
		index++;
		for (int i = 0; i < SPLIT; i++) {
			cAction[i] = new CreditActionImporter(billing, SPLIT, i);
			thread[index][i] = new Thread(cAction[i], "CreditActionImporter-" + i);
		}

		// InfoPayBalanceImporter
		// index++;
		// for (int i = 0; i < SPLIT; i++) {
		// infoPayBalance[i] = new InfoPayBalanceImporter(billing, SPLIT, i);
		// thread[index][i] = new Thread(infoPayBalance[i],
		// "InfoPayBalanceImporter-" + i);
		// }

		// InfoUserImpoter*****
		index++;
		for (int i = 0; i < SPLIT; i++) {
			infoUser[i] = new InfoUserImpoter(crm, SPLIT, i);
			thread[index][i] = new Thread(infoUser[i], "InfoUserImpoter-" + i);
		}

		// PayUserRelImporter
		// index++;
		// for (int i = 0; i < SPLIT; i++) {
		// payUserRel[i] = new PayUserRelImporter(billing, SPLIT, i);
		// thread[index][i] = new Thread(payUserRel[i], "PayUserRelImporter-" +
		// i);
		// }

		// InfoUserCreditImpoter*****
		index++;
		for (int i = 0; i < SPLIT; i++) {
			infoUserCreditHelp[i] = new InfoUserCreditHelpImporter(crm, SPLIT, i);
			thread[index][i] = new Thread(infoUserCreditHelp[i], "InfoUserCreditHelpImporter-" + i);
		}

		for (int i = 0; i < SERVICE; i++) {
			for (int j = 0; j < SPLIT; j++) {
				thread[i][j].start();
			}
		}

		for (int i = 0; i < SERVICE; i++) {
			for (int j = 0; j < SPLIT; j++) {
				try {
					thread[i][j].join();
				} catch (InterruptedException e) {
					Log.error(e.getMessage());
					System.exit(-1);
				}
			}
		}

		long elapsed = System.currentTimeMillis() - start;

		Log.info("import done..., elapsed time:" + elapsed + "ms");
		ctx.close();
	}
}
