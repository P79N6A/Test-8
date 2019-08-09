/**
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.credit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

//import com.tydic.beijing.billing.credit.common.DbReconnect;
//import com.tydic.beijing.billing.credit.dao.QBlockCredit;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.credit.common.BasicException;
import com.tydic.beijing.billing.credit.dao.InfoUserCredit;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUserCredit;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUserCreditlog;
import com.tydic.beijing.billing.credit.memcache.dao.InfoUserCreditMemcache;
import com.tydic.uda.service.S;
//import  java.sql.SQLRecoverableException;

/**
 * q_credit_info_user_credit<br/>
 *
 * @author Bradish7Y
 * @since JDK 1.7
 *
 */
public class QCreditInfoUserCreditProcess extends Utils implements Runnable {

	private static final Logger Log = Logger.getLogger(QCreditInfoUserCreditProcess.class);
	public static volatile boolean stop = false;

	private Map<String, String> para = null;
	private Map<String, String> proc = null;

	public QCreditInfoUserCreditProcess(final Map<String, String> para,
										final Map<String, String> proc) {
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

		Log.info("###>parameters:");
		Log.info("###>mod=" + mod);
		Log.info("###>remainder=" + remainder);
		Log.info("###>perRecords=" + perRecords);
		Log.info("###>sleep=" + timeInterval);
		while (!stop) {

			try {
				List<QCreditInfoUserCredit> list = dbkit.getQCreditInfoUserCredit(mod, remainder);
				if (list != null) {
					InfoUserCredit info = new InfoUserCredit();
					for (QCreditInfoUserCredit oci : list) {

						Log.debug("###>QCreditInfoUserCredit[" + oci.toString() + "]");
						int action = oci.getAction();
						String userId = oci.getUser_id();

						Log.debug("###>get info_user_credit memcache, mem_key="
								+ InfoUserCreditMemcache.KEY_PREFIX + userId);
						InfoUserCreditMemcache infoHelp = S.get(InfoUserCreditMemcache.class).get(
								InfoUserCreditMemcache.KEY_PREFIX + userId);

						if (action == 0 || action == 1) {// insert, update
							Log.info("###>action=" + action + ", put or update it to memcache");

							boolean found = false;

							if (infoHelp != null) {
								for (InfoUserCredit c : infoHelp.getInfoUserCreditList()) {
									if (oci.getCredit_type().compareTo(c.getCredit_type()) == 0) {// update
										Log.debug("###>found, credit_type=" + c.getCredit_type());
										found = true;
										c.setEff_date(oci.getEff_date());
										c.setExp_date(oci.getExp_date());
										c.setEff_flag(oci.getEff_flag());
										c.setLocal_net(oci.getLocal_net());
										c.setCredit_type(oci.getCredit_type());
										c.setCredit_number(oci.getCredit_number());
										break;
									}
								}
								if (!found) {
									Log.debug("###>credit_type not exist, insert it to memcache");
									info.setUser_id(oci.getUser_id());
									info.setEff_date(oci.getEff_date());
									info.setExp_date(oci.getExp_date());
									info.setEff_flag(oci.getEff_flag());
									info.setLocal_net(oci.getLocal_net());
									info.setCredit_type(oci.getCredit_type());
									info.setCredit_number(oci.getCredit_number());

									infoHelp.getInfoUserCreditList().add(info);
								}
							} else {
								Log.debug("###>userId = " + userId + ", not found in memcache");
								info.setUser_id(oci.getUser_id());
								info.setEff_date(oci.getEff_date());
								info.setExp_date(oci.getExp_date());
								info.setEff_flag(oci.getEff_flag());
								info.setLocal_net(oci.getLocal_net());
								info.setCredit_type(oci.getCredit_type());
								info.setCredit_number(oci.getCredit_number());

								infoHelp = new InfoUserCreditMemcache();
								infoHelp.setMem_key(InfoUserCreditMemcache.KEY_PREFIX
										+ info.getUser_id());
								List<InfoUserCredit> infoCreditList = new ArrayList<InfoUserCredit>();
								infoCreditList.add(info);
								infoHelp.setInfoUserCreditList(infoCreditList);
								Log.info("###>userId = " + userId + ", put to memcache");
							}

							// insert
							Log.debug("###>InfoUserCreditMemcache mem_key=" + infoHelp.getMem_key());
							S.get(InfoUserCreditMemcache.class).create(infoHelp);
							QCreditInfoUserCreditlog qciucl = new QCreditInfoUserCreditlog();
							qciucl.setSerial_num(oci.getSerial_num());
							qciucl.setAction(oci.getAction());
							qciucl.setEnqueue_date(oci.getEnqueue_date());
							qciucl.setLocal_net(oci.getLocal_net());
							qciucl.setUser_id(oci.getUser_id());
							qciucl.setCredit_type(oci.getCredit_type());
							qciucl.setCredit_number(oci.getCredit_number());
							qciucl.setEff_date(oci.getEff_date());
							qciucl.setExp_date(oci.getExp_date());
							qciucl.setEff_flag(oci.getEff_flag());
							
							to.insertQCreditInfoUserCreditlog(qciucl);
							
							info.clear();
							// add rowid to list
							delList.addElement(oci.getRow_id());
						} else {
							Log.info("###>user_id[" + userId + "], None action!!!!");
						}


					}


					Log.debug("###>delete q_user_info_user_credit, delList.size=" + delList.size());
					// delete q_credit_info_user_credit(batch)
					to.deleteQCreditInfoUserCredit(delList);

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

//	public void mingleInsertQCreditInfoUserCreditLogAndDeleteQCreditInfoUserCreditg(TableOpers to,QCreditInfoUserCreditlog qcreditinfousercreditlog, String rowId) throws Exception {
//		int reconnectTimes = 0;
//		while (reconnectTimes < DbReconnect.DB_RECONNECT_LIMIT_TIMES) {
//			try {
//				reconnectTimes++;
//				to.insertQCreditInfoUserCreditLogAndDeleteQCreditInfoUserCredit(qcreditinfousercreditlog, rowId);
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
