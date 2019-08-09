package com.tydic.beijing.billing.credit;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.credit.dao.CreditAction;
import com.tydic.beijing.billing.credit.dao.HlpSmsSend;
import com.tydic.beijing.billing.credit.dao.InfoAllList;
import com.tydic.beijing.billing.credit.dao.InfoAuthMobile;
import com.tydic.beijing.billing.credit.dao.LogCreditAction;
import com.tydic.beijing.billing.credit.dao.LogCreditFile;
import com.tydic.beijing.billing.credit.dao.LogCreditFlowOvertop;
import com.tydic.beijing.billing.credit.dao.QBlockCredit;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUser;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUserCredit;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUserCreditlog;
import com.tydic.beijing.billing.credit.dao.QCreditInfoUserlog;
import com.tydic.beijing.billing.credit.dao.QUserReasonSend;
import com.tydic.beijing.billing.credit.dao.QUserReasonSendPre;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class TableOpers {
	private static final Logger Log = Logger.getLogger(TableOpers.class);

	/**
	 * insertQUserReasonSend:reason.<br/>
	 *
	 * @param q
	 */
	public void insertQUserReasonSend(final QUserReasonSend q) {

		S.get(QUserReasonSend.class).create(q);
	}

	private static final int MAX_IN = 500;

	/**
	 * deleteQBlock:delete.<br/>
	 *
	 * @param delList
	 * @throws Exception
	 */
	public void deleteQBlock(Vector<String> delList) throws Exception {
		StringBuffer sb = new StringBuffer();
		int len = delList.size();

		Log.debug("###>step in deleteQBlock().delList.size=" + len);
		for (int i = 0; i < len; i++) {
			sb.append("'").append(delList.get(i)).append("'");
			if (i != 0 && i % MAX_IN == 0) {
				S.get(QBlockCredit.class).batch(
						Condition.build("delete4qblock").filter("row_id", sb.toString()),
						new QBlockCredit());
				sb.delete(0, sb.length());
			} else {
				if (i != len - 1) {
					sb.append(",");
				}
			}
		}

		Log.debug("###>step in deleteQBlock().sb.length=" + sb.length());
		if (sb.length() != 0) {
			S.get(QBlockCredit.class).batch(
					Condition.build("delete4qblock").filter("row_id", sb.toString()),
					new QBlockCredit());
		}
	}

	/**
	 * @param block
	 * @throws Exception
	 * @author cws
	 */
//	public void insertQBlockLogAndDeleteQBlock(final QBlockCredit block, final String rowId) throws Exception {
//		StringBuffer sb = new StringBuffer();
//		S.get(QBlockCredit.class).create(block);
//
//		sb.append("'").append(rowId).append("'");
//		S.get(QBlockCredit.class).batch(
//				Condition.build("delete4qblock").filter("row_id", sb.toString()),
//				new QBlockCredit());
//	}


	public void insertQCreditInfoUserlog(final QCreditInfoUserlog qciucl) throws Exception {
		S.get(QCreditInfoUserlog.class).create(qciucl);
	}

	/**
	 * deleteQCreditInfoUser:delete.<br/>
	 *
	 * @param delList
	 * @throws Exception
	 */
	public void deleteQCreditInfoUser(Vector<String> delList) throws Exception {
		StringBuffer sb = new StringBuffer();
		int len = delList.size();
		Log.debug("###>step in deleteQCreditInfoUser().delList.size=" + len);

		for (int i = 0; i < len; i++) {
			sb.append("'").append(delList.get(i)).append("'");
			if (i != 0 && i % MAX_IN == 0) {
				S.get(QCreditInfoUser.class).batch(
						Condition.build("delete4qcreditinfouser").filter("row_id", sb.toString()),
						new QCreditInfoUser());
				sb.delete(0, sb.length());
			} else {
				if (i != len - 1) {
					sb.append(",");
				}
			}
		}

		Log.debug("###>step in deleteQCreditInfoUser().sb.length=" + sb.length());
		if (sb.length() != 0) {
			S.get(QCreditInfoUser.class).batch(
					Condition.build("delete4qcreditinfouser").filter("row_id", sb.toString()),
					new QCreditInfoUser());
		}

	}

//	public void insertQCreditInfoUserlogAndDeleteQCreditInfoUser(final QCreditInfoUserlog qciucl, String rowId) throws Exception {
//		StringBuffer sb = new StringBuffer();
//		S.get(QCreditInfoUserlog.class).create(qciucl);
//
//		sb.append("'").append(rowId).append("'");
//		S.get(QCreditInfoUser.class).batch(
//				Condition.build("delete4qcreditinfouser").filter("row_id", sb.toString()),
//				new QCreditInfoUser());
//	}

	public void deleteQCreditInfoUserSingle(final String rowId) throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append("'").append(rowId).append("'");
		S.get(QCreditInfoUser.class).batch(
				Condition.build("delete4qcreditinfouser").filter("row_id", sb.toString()),
				new QCreditInfoUser());
	}

	public void insertQCreditInfoUserCreditlog(final QCreditInfoUserCreditlog qcreditinfousercreditlog) throws Exception {
		S.get(QCreditInfoUserCreditlog.class).create(qcreditinfousercreditlog);
	}

	public void deleteQCreditInfoUserCredit(Vector<String> delList) throws Exception {
		StringBuffer sb = new StringBuffer();
		int len = delList.size();
		Log.debug("###>step in deleteQCreditInfoUserCredit().delList.size=" + len);
		for (int i = 0; i < len; i++) {
			sb.append("'").append(delList.get(i)).append("'");
			if (i != 0 && i % MAX_IN == 0) {
				S.get(QCreditInfoUserCredit.class).batch(
						Condition.build("delete4qcreditinfousercredit").filter("row_id",
								sb.toString()), new QCreditInfoUserCredit());
				sb.delete(0, sb.length());
			} else {
				if (i != len - 1) {
					sb.append(",");
				}
			}
		}

		Log.debug("###>step in deleteQCreditInfoUserCredit().sb.length=" + sb.length());
		if (sb.length() != 0) {
			S.get(QCreditInfoUserCredit.class)
					.batch(Condition.build("delete4qcreditinfousercredit").filter("row_id",
							sb.toString()), new QCreditInfoUserCredit());
		}

	}

//	public void insertQCreditInfoUserCreditLogAndDeleteQCreditInfoUserCredit(final QCreditInfoUserCreditlog qcreditinfousercreditlog, final String rowId) throws Exception {
//		StringBuffer sb = new StringBuffer();
//		S.get(QCreditInfoUserCreditlog.class).create(qcreditinfousercreditlog);
//
//		sb.append("'").append(rowId).append("'");
//		S.get(QCreditInfoUserCredit.class)
//				.batch(Condition.build("delete4qcreditinfousercredit").filter("row_id",
//						sb.toString()), new QCreditInfoUserCredit());
//	}

	public void deleteQCreditInfoUserCreditSingle(final String rowId) {
		StringBuffer sb = new StringBuffer();

		sb.append("'").append(rowId).append("'");
		S.get(QCreditInfoUserCredit.class)
				.batch(Condition.build("delete4qcreditinfousercredit").filter("row_id",
						sb.toString()), new QCreditInfoUserCredit());
	}

	public void insertCreditAction(final CreditAction action) throws Exception {
		S.get(CreditAction.class).create(action);
	}

	public void deleteCreditAction(final String userId, final String reason) throws Exception {
		S.get(CreditAction.class).batch(
				Condition.build("delete4creditaction").filter("reason", reason)
						.filter("user_id", userId), new CreditAction());
	}

	public void insertLogCreditAction(final LogCreditAction logAction) throws Exception {
		S.get(LogCreditAction.class).create(logAction);
	}

	public void insertAction(final CreditAction action, final LogCreditAction logAction,
							 final HlpSmsSend sms, final String insertPreFlag) throws Exception {
		List<CreditAction> l = S.get(CreditAction.class).query(
				Condition.empty().filter("user_id", action.getUser_id())
						.filter("reason", action.getReason()));

		if (l.isEmpty()) {
			S.get(CreditAction.class).create(action);
			S.get(LogCreditAction.class).create(logAction);

			if (insertPreFlag.compareTo("0") == 0) {
				//	S.get(HlpSmsSend.class).create(sms);  改为多原因发短信
			} else {
			}

		} else {
			S.get(CreditAction.class).update(action);
			S.get(LogCreditAction.class).create(logAction);
			if (insertPreFlag.compareTo("0") == 0) {
				//	S.get(HlpSmsSend.class).create(sms); 改为多原因发短信
			} else {
			}

		}
	}

	public void deleteAndInsertAction(final String userId, final String reason,
									  final LogCreditAction logAction) throws Exception {

		S.get(CreditAction.class).batch(
				Condition.build("delete4creditaction").filter("reason", reason)
						.filter("user_id", userId), new CreditAction());
		S.get(LogCreditAction.class).create(logAction);

	}

//	public void deleteAndInsertActionAndQBlock(final String userId, final String reason,
//											   final LogCreditAction logAction, final QUserReasonSend q, final QBlockCredit block) throws Exception {
//		S.get(CreditAction.class).batch(
//				Condition.build("delete4creditaction").filter("reason", reason)
//						.filter("user_id", userId), new CreditAction());
//		S.get(LogCreditAction.class).create(logAction);
//		S.get(QUserReasonSend.class).create(q);
//		S.get(QBlockCredit.class).create(block);
//	}

	public void insertHlpSmsSend(final HlpSmsSend sms) throws Exception {
		S.get(HlpSmsSend.class).create(sms);
	}

	public void insertQBlock(final QBlockCredit block) throws Exception {
		S.get(QBlockCredit.class).create(block);
	}

	public void insertQUserReasonSendPre(final QUserReasonSend reason) throws Exception {

		QUserReasonSendPre pre = new QUserReasonSendPre();

		pre.setSerial_num(reason.getSerial_num());
		pre.setUser_no(reason.getUser_no());
		pre.setTele_type(reason.getTele_type());
		pre.setReason_code(reason.getReason_code());
		pre.setEnqueue_date(reason.getEnqueue_date());
		pre.setLocal_net(reason.getLocal_net());
		pre.setActive_type(reason.getActive_type());
		pre.setCharge_id(reason.getCharge_id());

		S.get(QUserReasonSendPre.class).create(pre);
	}

	public void insertLogCreditFile(final LogCreditFile logFile) throws Exception {
		S.get(LogCreditFile.class).create(logFile);
	}

	/**
	 * insertOverTop:封顶日志表，短信表.<br/>
	 *
	 * @param logCreditFlowOvertop
	 */
	public void insertLogCreditFlowOvertop(final LogCreditFlowOvertop logCreditFlowOvertop,
										   final HlpSmsSend sms) {
		S.get(LogCreditFlowOvertop.class).create(logCreditFlowOvertop);
		S.get(HlpSmsSend.class).create(sms);
	}


//	public void insertActionAndInsertQUserReasonSendAndDoRoofOfFlow(final CreditAction action, final LogCreditAction logAction, final HlpSmsSend sms, final String insertPreFlag, final boolean returnFlag, final QUserReasonSend qUserReasonSend, final LogCreditFlowOvertop logCreditFlowOvertop, final HlpSmsSend hlpSmsSend, final QUserReasonSend send) throws Exception {
//
//		if (returnFlag != true) {
//			S.get(QUserReasonSend.class).create(qUserReasonSend);
//			S.get(LogCreditFlowOvertop.class).create(logCreditFlowOvertop);
//			S.get(HlpSmsSend.class).create(hlpSmsSend);
//		} //执行流量封顶的语句
//
//
//		List<CreditAction> l = S.get(CreditAction.class).query(
//				Condition.empty().filter("user_id", action.getUser_id())
//						.filter("reason", action.getReason()));
//
//		if (l.isEmpty()) {
//			S.get(CreditAction.class).create(action);
//			S.get(LogCreditAction.class).create(logAction);
//
//			if (insertPreFlag.compareTo("0") == 0) {
//				//	S.get(HlpSmsSend.class).create(sms);  改为多原因发短信
//			} else {
//			}
//		} else {
//			S.get(CreditAction.class).update(action);
//			S.get(LogCreditAction.class).create(logAction);
//			if (insertPreFlag.compareTo("0") == 0) {
//				//	S.get(HlpSmsSend.class).create(sms); 改为多原因发短信
//			} else {
//			}
//		}
//
//		if (insertPreFlag.compareTo("0") == 0) {
//			S.get(QUserReasonSend.class).create(send);
//		} else {
//			// for temp
//			QUserReasonSendPre pre = new QUserReasonSendPre();
//
//			pre.setSerial_num(send.getSerial_num());
//			pre.setUser_no(send.getUser_no());
//			pre.setTele_type(send.getTele_type());
//			pre.setReason_code(send.getReason_code());
//			pre.setEnqueue_date(send.getEnqueue_date());
//			pre.setLocal_net(send.getLocal_net());
//			pre.setActive_type(send.getActive_type());
//			pre.setCharge_id(send.getCharge_id());
//
//			S.get(QUserReasonSendPre.class).create(pre);
//		}
//	}
	
	
	
	public void insertlimitTraffic( final LogCreditAction logAction,   final QUserReasonSend qUserReasonSend, final LogCreditFlowOvertop logCreditFlowOvertop) throws Exception {

		 
			S.get(QUserReasonSend.class).create(qUserReasonSend);
			S.get(LogCreditFlowOvertop.class).create(logCreditFlowOvertop);
			S.get(LogCreditAction.class).create(logAction);
		 
	}


	public void deleteAndInsertActionAndInsertQUserReasonSendAndDoRoofOfFlow(final String userId, final String reason, final LogCreditAction logAction, final boolean returnFlag, final QUserReasonSend qUserReasonSend, final LogCreditFlowOvertop logCreditFlowOvertop, final HlpSmsSend hlpSmsSend, final QUserReasonSend send) throws Exception {

		if (returnFlag != true) {
			S.get(QUserReasonSend.class).create(qUserReasonSend);
			S.get(LogCreditFlowOvertop.class).create(logCreditFlowOvertop);
			S.get(HlpSmsSend.class).create(hlpSmsSend);
		} //执行流量封顶的语句

		S.get(CreditAction.class).batch(Condition.build("delete4creditaction").filter("reason", reason)
				.filter("user_id", userId), new CreditAction());
		S.get(LogCreditAction.class).create(logAction);

		S.get(QUserReasonSend.class).create(send);
	}

	/**
	 * @param:查询红名单
	 * @author：biansen
	 */
	public List<InfoAuthMobile> getQueryInfoAuthMobile(String userId) {
		return S.get(InfoAuthMobile.class).query(Condition.build("getQueryInfoAuthMobile").filter("userId", userId));
	}

	public List<InfoAllList> queryAllListType(String customerId) {

		return S.get(InfoAllList.class).query(Condition.build("queryAllListType").filter("customerId", customerId));
	}
}
