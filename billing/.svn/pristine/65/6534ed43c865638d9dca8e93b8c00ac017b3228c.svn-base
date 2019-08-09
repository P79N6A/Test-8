/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.credit;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.credit.common.BasicException;
import com.tydic.beijing.billing.credit.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeAcctMonth;

/**
 * credit application<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class CreditApp {

	private static final Logger Log = Logger.getLogger(CreditApp.class);
	private static Map<String, String> para = null;
	private static Map<String, String> proc = null;
	private static CodeAcctMonth codeAcctMonth = null;
	private static final int timeInterval = 30;// sleep x seconds

	private static void load(final String procId) throws BasicException, Exception {
		// load credit_para
		DBKit dbkit = (DBKit) SpringContextUtil.getContext().getBean("dbKit");
		para = dbkit.getCreditPara();
		proc = dbkit.getCreditProcPara(procId);

		codeAcctMonth = dbkit.getChargeDate();
	}

	private static void exitApp() {
		exitApp(-1);
	}

	private static void exitApp(final int status) {
		System.exit(status);
	}

	public static void main(String[] args) {
		SpringContextUtil.getContext();
		String procId = System.getProperty("PROC_ID");
		try {
			if (procId == null) {
				throw new BasicException(ErrorCode.PROC_ID_NOT_EXISTS, "process id not exists");
			}

			// load parameters
			load(procId);
			Log.info("###>para=" + para.toString());
			Log.info("###>codeAcctMonth=" + codeAcctMonth.toString());

			// q_block
			QBlockProcess qBlock = new QBlockProcess(para, proc, codeAcctMonth);
			Thread qBlockThread = new Thread(qBlock, "QBlockProcess");
			qBlockThread.start();

			// q_credit_info_user
			QCreditInfoUserProcess qUser = new QCreditInfoUserProcess(para, proc);
			Thread qUserThread = new Thread(qUser, "QCreditInfoUserProcess");
			qUserThread.start();

			// q_credit_info_user_credit
			QCreditInfoUserCreditProcess qCredit = new QCreditInfoUserCreditProcess(para, proc);
			Thread qCreditThread = new Thread(qCredit, "QCreditInfoUserCreditProcess");
			qCreditThread.start();

			// file credit startup
			FileProcess file = new FileProcess(para, proc, codeAcctMonth);
			Thread fileThread = new Thread(file, "FileProcess");
			fileThread.start();

			while (true) {
				try {
					TimeUnit.SECONDS.sleep(timeInterval);
					Log.info("CreditApp sleep");
				} catch (InterruptedException e) {

				}
			}
		} catch (BasicException e) {
			Log.error(e);
			exitApp();
		} catch (Exception e) {
			Log.error(e.getMessage(),e);
			exitApp();
		}

	}
}
