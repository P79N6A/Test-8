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
import com.tydic.beijing.billing.credit.dao.InfoUserCredit;
import com.tydic.beijing.billing.credit.dao.LogCreditAction;
import com.tydic.beijing.billing.credit.dao.QBlockCredit;
import com.tydic.beijing.billing.credit.dao.QUserReasonSend;
import com.tydic.beijing.billing.credit.memcache.dao.BilActBill4CreditMemcache;
import com.tydic.beijing.billing.credit.memcache.dao.BilActRealTimeBill4CreditMemcache;
import com.tydic.beijing.billing.credit.memcache.dao.CreditActionMemcache;
import com.tydic.beijing.billing.credit.memcache.dao.InfoUser4CreditMemcache;
import com.tydic.beijing.billing.credit.memcache.dao.InfoUserCreditMemcache;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.uda.service.S;

/**
 * q_block<br/>
 *
 * @author Bradish7Y
 * @since JDK 1.7
 *
 */
public class QBlockProcess extends Utils implements Runnable {

	private static final Logger Log = Logger.getLogger(QBlockProcess.class);
	public static volatile boolean stop = false;
	private static final String OPEN = "71";// 欠费开机
	private static final String STOP = "50";// 欠费单停

	private Map<String, String> para = null;
	private Map<String, String> proc = null;
	private String partitionNo = null;

	public QBlockProcess(final Map<String, String> para, final Map<String, String> proc,
						 final CodeAcctMonth codeAcctMonth) {
		this.para = para;
		this.proc = proc;
		this.partitionNo = codeAcctMonth.getPartition_no();
	}

	@Override
	public void run() {
		Log.debug("step in run()");
		ClassPathXmlApplicationContext ctx = SpringContextUtil.getContext();
		QUserReasonSend send = new QUserReasonSend();
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

		long threshold = 0;
		try {
			threshold = Long.parseLong(para.get("credit_threshold"));
		} catch (NumberFormatException e) {
			Log.error("###>credit calculate threshold no set.......");
			exitApp();
		}
		Log.info("###>parameters:");
		Log.info("###>mod=" + mod);
		Log.info("###>remainder=" + remainder);
		Log.info("###>perRecords=" + perRecords);
		Log.info("###>sleep=" + timeInterval);
		Log.info("###>threshold=" + threshold);
		while (!stop) {
			try {
				List<QBlockCredit> list = dbkit.getQBlock(mod, remainder);

				int totalRecords = 0;
				if (list != null) {
					for (QBlockCredit qc : list) {
//						String rowId = qc.getRow_id();
						long history = 0;
						long realtime = 0;
						Log.debug("###>QBlock->[" + qc.getUser_id() + "]");
						String userId = qc.getUser_id();
						// get realtime bill
						// *********************************************************
						BilActRealTimeBill4CreditMemcache bilActRealTimeBill = dbkit.getBilActRealTimeBill(partitionNo,
								userId);

						if (bilActRealTimeBill == null) {
							realtime = 0;
							// remove existed realtime_bill in memcache
							Log.debug("###>remove existed realtime_bill bill in memcache");
							bilActRealTimeBill = new BilActRealTimeBill4CreditMemcache();
							bilActRealTimeBill.setUser_id(userId);
							bilActRealTimeBill.setPay_id(qc.getPay_id());
							bilActRealTimeBill.setMem_key(BilActRealTimeBill4CreditMemcache.KEY_PREFIX);

							Log.debug("###>remove, mem_key=" + bilActRealTimeBill.getMem_key());
							S.get(BilActRealTimeBill4CreditMemcache.class).remove(bilActRealTimeBill.getMem_key());

						} else {
							realtime = bilActRealTimeBill.getNon_deduct_fee();
							bilActRealTimeBill.setMem_key(BilActRealTimeBill4CreditMemcache.KEY_PREFIX);
							// update real bill to memcache
							Log.debug("###>update, mem_key=" + bilActRealTimeBill.getMem_key());
							S.get(BilActRealTimeBill4CreditMemcache.class).create(bilActRealTimeBill);
						}
						// get history bill
						// *********************************************************
						BilActBill4CreditMemcache bilActBill = dbkit.getBilActBill(userId);

						if (bilActBill == null) {
							history = 0;
							// remove existed history bill in memcache
							Log.debug("###>remove existed history bill in memcache");
							bilActBill = new BilActBill4CreditMemcache();
							bilActBill.setUser_id(userId);
							bilActBill.setPay_id(qc.getPay_id());
							bilActBill.setMem_key(BilActBill4CreditMemcache.KEY_PREFIX);
							Log.debug("###>remove, mem_key=" + bilActBill.getMem_key());
							S.get(BilActBill4CreditMemcache.class).remove(bilActBill.getMem_key());
							// 删除memcache的，如果有
						} else {
							history = bilActBill.getOwe_fee();
							// update history bill to memcache
							bilActBill.setMem_key(BilActBill4CreditMemcache.KEY_PREFIX);
							Log.debug("###>update, mem_key=" + bilActBill.getMem_key());
							S.get(BilActBill4CreditMemcache.class).create(bilActBill);
						}

						// get user credit from memcache
						// *********************************************************
						Log.debug("###>get info_user_credit from memcache, userId=" + userId + ", mem_key="
								+ InfoUserCreditMemcache.KEY_PREFIX + userId);
						InfoUserCreditMemcache infoUserCreditHelp = S.get(InfoUserCreditMemcache.class)
								.get(InfoUserCreditMemcache.KEY_PREFIX + userId);

						String currentDate = getSystemTime();
						long credit = 0;
						if (infoUserCreditHelp != null) {
							for (InfoUserCredit iuc : infoUserCreditHelp.getInfoUserCreditList()) {
								if (currentDate.compareTo(iuc.getEff_date()) >= 0
										&& currentDate.compareTo(iuc.getExp_date()) < 0) {
									credit += iuc.getCredit_number();
								}
							}
						}

						// calculate
						long calculRet = calculate(credit, history, realtime);

						if (calculRet >= threshold && history == 0 && realtime == 0) {
							Log.info("###>calculate(credit=" + credit + ", history=" + history + ", realtime="
									+ realtime + ") >= threshold=" + threshold + ", ret=true, user_id[" + userId
									+ "], open action");
							CreditActionMemcache tmpAction = new CreditActionMemcache();
							tmpAction.setUser_id(userId);
							tmpAction.setReason(STOP);
							tmpAction.setMem_key(CreditActionMemcache.KEY_PREFIX);
							// CreditActionMemcache cAction =
							// S.get(CreditActionMemcache.class).get(
							// tmpAction.getMem_key());
							// if (cAction != null) {
							TableOpers to = (TableOpers) ctx.getBean("TableOpers");
							long sequence = dbkit.getFileSn("seq_q_reason_sn");

							InfoUser4CreditMemcache infoUser = new InfoUser4CreditMemcache();
							infoUser.setUser_id(userId);
							infoUser.setMem_key(InfoUser4CreditMemcache.KEY_PREFIX);
							InfoUser4CreditMemcache infoUserMem = S.get(InfoUser4CreditMemcache.class)
									.get(infoUser.getMem_key());

							String teleType = null;
							String localNet = null;
							if (infoUserMem == null) {
								delList.add(qc.getRow_id());

								// insert log q_block
								TableOpers holy_shit = (TableOpers) ctx.getBean("TableOpers");
								String actionTime = this.getSystemTime();
								qc.setAction_time(actionTime);
								// user not found
								qc.setSource("9");
//								mingleDeleteQBlockAndLog(holy_shit,qc,rowId);
								holy_shit.insertQBlock(qc);
								Log.error(
										"###>user_id[" + userId + "] not in memcache, skip, q_block:" + qc.toString());
								// #####skip####################
								continue;

							} else {
								teleType = infoUserMem.getTele_type();
								localNet = infoUserMem.getLocal_net();
							}
							send.setSerial_num(sequence);
							send.setUser_no(userId);
							send.setTele_type(teleType);
							String actionTime = this.getSystemTime();
							send.setEnqueue_date(actionTime);
							send.setLocal_net(localNet);
//							qc.setAction_time(actionTime);
//							qc.setSource("0");

							Log.debug("###>ready to delete credit_action memcache, mem_key=" + tmpAction.getMem_key());
							S.get(CreditActionMemcache.class).remove(tmpAction.getMem_key());
							if (null != infoUserMem.getUser_status() && !infoUserMem.getUser_status().equals("")
									&& !infoUserMem.getUser_status().equals("501")) {
								send.setReason_code(OPEN);

								LogCreditAction logAction = new LogCreditAction();
								logAction.setSerial_no(dbkit.getFileSn("credit_log_sequence"));
								logAction.setUser_id(userId);
								logAction.setPay_id(qc.getPay_id());
								logAction.setCredit_number(credit);
								logAction.setHistory(history);
								logAction.setRealtime(realtime);
								logAction.setReason(send.getReason_code());
								logAction.setAction_time(actionTime);
								logAction.setSource("0");
								// insert log first
								Log.debug("###>ready to delete credit_action and insert log_credit_action");
								to.deleteAndInsertAction(userId, STOP, logAction);

								// insert reason
								Log.info("###>q_reason_send:" + send.toString());
								to.insertQUserReasonSend(send);
							}
							// insert log q_block
							qc.setAction_time(actionTime);
							qc.setSource("0");
							to.insertQBlock(qc);

							send.clear();
							Log.debug("###>success, insert q_user_reason_send[" + send.toString() + "]");
						} else {
							Log.info("###>calculate(credit=" + credit + ", history=" + history + ", realtime="
									+ realtime + ") >= threshold=" + threshold + ", ret=false, user_id[" + userId
									+ "], None action");

							// insert log q_block
							TableOpers to = (TableOpers) ctx.getBean("TableOpers");
							String actionTime = this.getSystemTime();
							qc.setAction_time(actionTime);
							qc.setSource("0");
//							mingleDeleteQBlockAndLog(to,qc,rowId);
							to.insertQBlock(qc);
						}
						totalRecords++;
						delList.add(qc.getRow_id());
					}

					Log.debug("###>delete q_block, delList.size=" + delList.size());
					// delete qblock(batch)
					TableOpers to = (TableOpers) ctx.getBean("TableOpers");
					to.deleteQBlock(delList);
					delList.clear();
				} // end list != null

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

//	public void mingleDeleteAndInsertActionAndQBlock(TableOpers to, String userId, String reason,
//													 LogCreditAction logAction, QUserReasonSend q, QBlockCredit block) {
//		int reconnectTimes = 0;
//		while (reconnectTimes < DbReconnect.DB_RECONNECT_LIMIT_TIMES) {
//			try {
//				reconnectTimes++;
//				to.deleteAndInsertActionAndQBlock(userId, reason, logAction, q, block);
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
//	public void mingleDeleteQBlockAndLog(TableOpers to, QBlockCredit block, String rowId) throws Exception {
//		int reconnectTimes = 0;
//		while (reconnectTimes < DbReconnect.DB_RECONNECT_LIMIT_TIMES) {
//			try {
//				reconnectTimes++;
//				to.insertQBlockLogAndDeleteQBlock(block, rowId);
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
