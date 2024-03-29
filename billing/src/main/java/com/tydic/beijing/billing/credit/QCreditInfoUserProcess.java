/**
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.credit;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

//import com.tydic.beijing.billing.credit.common.DbReconnect;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.credit.common.BasicException;
import com.tydic.beijing.billing.credit.dao.InfoUser4Credit;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUser;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUserCreditlog;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUserlog;
import com.tydic.beijing.billing.credit.memcache.dao.InfoUser4CreditMemcache;
import com.tydic.uda.service.S;

/**
 * q_credit_info_user process <br/>
 *
 * @author Bradish7Y
 * @since JDK 1.7
 *
 */
public class QCreditInfoUserProcess extends Utils implements Runnable {
	private static final Logger Log = Logger.getLogger(QCreditInfoUserProcess.class);
	public static volatile boolean stop = false;

	private Map<String, String> para = null;
	private Map<String, String> proc = null;

	public QCreditInfoUserProcess(Map<String, String> para, Map<String, String> proc) {
		this.para = para;
		this.proc = proc;
	}

	@Override
	public void run() {
		Log.debug("###>step in run()");
		ClassPathXmlApplicationContext ctx = SpringContextUtil.getContext();
		DBKit dbkit = (DBKit) SpringContextUtil.getContext().getBean("dbKit");
		// mod
		String mod = para.get("mod");
		// remainder
		String remainder = proc.get("remainder");
		Vector<String> delList = new Vector<String>();

		int timeInterval = 0;
		try {
			timeInterval = Integer.parseInt(para.get("sleep"));
		} catch (NumberFormatException e) {
			timeInterval = 3;
		}

		int perRecords = 0;
		try {
			perRecords = Integer.parseInt(para.get("per_records"));
		} catch (NumberFormatException e) {
			perRecords = 50;
		}
		TableOpers to = (TableOpers) ctx.getBean("TableOpers");
		Log.info("###>mod=" + mod);
		Log.info("###>remainder=" + remainder);
		Log.info("###>perRecords=" + perRecords);
		Log.info("###>sleep=" + timeInterval);

		while (!stop) {
			try {
				List<QCreditInfoUser> list = dbkit.getQCreditInfoUser(mod, remainder);

				if (list != null) {

					InfoUser4CreditMemcache infoUser = new InfoUser4CreditMemcache();
					for (QCreditInfoUser oci : list) {
						Log.debug("###>QCreditInfoUser[" + oci.toString() + "]");
//						String rowId = oci.getRow_id();
						InfoUser4Credit userDb = dbkit.getInfoUser(oci.getUser_id());
						if (userDb != null) {
							Log.debug("###>InfoUser4Credit[" + userDb.toString() + "] from db");
							infoUser = userDb;
							infoUser.setMem_key(InfoUser4CreditMemcache.KEY_PREFIX);
						} else {
							Log.error("###>user_id["
									+ oci.getUser_id()
									+ "] not in memcache, skip, ***Stop action***, q_credit_info_user:"
									+ oci.toString());

//							mingleDeleteQCreditInfoUserSingle(to,rowId);
							delList.addElement(oci.getRow_id());
							continue;
						}

						Log.debug("###>mem_key=" + infoUser.getMem_key());
						S.get(InfoUser4CreditMemcache.class).create(infoUser);
						QCreditInfoUserlog qciucl = new QCreditInfoUserlog();
						qciucl.setSerial_num(oci.getSerial_num());
						qciucl.setAction(oci.getAction());
						qciucl.setEnqueue_date(oci.getEnqueue_date());
						qciucl.setUser_id(oci.getUser_id());
						qciucl.setDevice_number(oci.getDevice_number());
						to.insertQCreditInfoUserlog(qciucl);
//						mingleInsertQCreditInfoUserlogAndDeleteQCreditInfoUser(to,qciucl,rowId);
						infoUser.clear();

						delList.addElement(oci.getRow_id());

					}
					Log.debug("###>delete q_credit_info_user, delList.size=" + delList.size());

					to.deleteQCreditInfoUser(delList);
					
					delList.clear();
				}// end list != null

				try {
					TimeUnit.SECONDS.sleep(timeInterval);
				} catch (InterruptedException e) {
				}
			} catch (BasicException e) {
				Log.error(e);
				exitApp();
			} catch (Exception e) {
				Log.error(e.getMessage());
				exitApp();
			}
		}

	}

//	public void mingleInsertQCreditInfoUserlogAndDeleteQCreditInfoUser(TableOpers to,QCreditInfoUserlog qciucl,String rowId){
//		int reconnectTimes = 0;
//		while (reconnectTimes < DbReconnect.DB_RECONNECT_LIMIT_TIMES) {
//			try {
//				reconnectTimes++;
//				to.insertQCreditInfoUserlogAndDeleteQCreditInfoUser(qciucl, rowId);
//			} catch (Exception ex) {
//				try {
//					TimeUnit.SECONDS.sleep(DbReconnect.DB_RECONNECT_SLEEP);
//				} catch (InterruptedException e) {
//				}
//				Log.error("数据库重连");
//				if (reconnectTimes == DbReconnect.DB_RECONNECT_LIMIT_TIMES - 1) {
//					Log.error("数据库重连" + DbReconnect.DB_RECONNECT_LIMIT_TIMES + "次失败，程序退出");
//					exitApp();
//				}
//			}
//			break;
//		}
//	}
//
//	public void mingleDeleteQCreditInfoUserSingle(TableOpers to,String rowId){
//		int reconnectTimes = 0;
//		while (reconnectTimes < DbReconnect.DB_RECONNECT_LIMIT_TIMES) {
//			try {
//				reconnectTimes++;
//				to.deleteQCreditInfoUserSingle(rowId);
//			} catch (Exception ex) {
//				try {
//					TimeUnit.SECONDS.sleep(DbReconnect.DB_RECONNECT_SLEEP);
//				} catch (InterruptedException e) {
//				}
//				Log.error("数据库重连");
//				if (reconnectTimes == DbReconnect.DB_RECONNECT_LIMIT_TIMES - 1) {
//					Log.error("数据库重连" + DbReconnect.DB_RECONNECT_LIMIT_TIMES + "次失败，程序退出");
//					exitApp();
//				}
//			}
//			break;
//		}
//	}

}
