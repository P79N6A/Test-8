package com.tydic.beijing.billing.account.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.jd.project.server.jsf.mobileserver.IMobileRefundService;
import com.jd.project.webbean.mobile.JdMobileRefundApply;
import com.tydic.beijing.billing.account.biz.RefundOps;
import com.tydic.beijing.billing.account.datastore.DSUserEventMapping;
import com.tydic.beijing.billing.account.service.Refund;
import com.tydic.beijing.billing.account.service.RefundQuery;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.QRefund;
import com.tydic.beijing.billing.dao.RuleUserEventMapping;
import com.tydic.beijing.billing.dto.RefundQueryResult;

/**
 * 
 * @author Tian
 *
 */
public class RefundImpl implements Refund {
	private final static Logger LOGGER = Logger.getLogger(RefundImpl.class);

	private DSUserEventMapping userEventMapping;
	private RefundOps ops;
	private RefundQuery rQuery;
	private IMobileRefundService iMobileRefundService;

	// private final static int Q_STATE_INIT = 0;
	private final static int Q_STATE_PROCESSING = 1;
	private final static int Q_STATE_OK = 2;
	private final static int Q_STATE_FAILED = 9;

	private final static String USER_EVENT_CODE_XIAOHU = "16";
	private final static String USER_EVENT_CODE_KAIHUFANXIAO = "101";
	private final static String USER_EVENT_CODE_CHEDAN = "19";

	@Override
	public void process() {
		Properties p = new Properties();
		try {
			p.load(RefundImpl.class.getClassLoader().getResourceAsStream(
					"refund.properties"));
			userEventMapping.load();
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			System.exit(0);
		}
		while (true) {
			List<QRefund> qrs = ops.scanQRefund();
			for (QRefund qr : qrs) {
				try {
					ops.updateQState(qr, 0, Q_STATE_PROCESSING);
					// 查找user_event_code映射
					RuleUserEventMapping ruem = userEventMapping.get(
							qr.getUser_event_code(), "Refund");
					if (ruem == null) {
						LOGGER.error("Refund DeviceNumber ["
								+ qr.getDevice_number()
								+ "] Query UserEventCode["
								+ qr.getUser_event_code() + "] Mapping Failed!");
						ops.updateQState(qr, 0, Q_STATE_FAILED);
						continue;
					}
					long refundAmount = 0L;
					long refundableAmount = 0L;
					// 查询可退余额
					RefundQueryResult rqResult = rQuery.doQuery(
							qr.getDevice_number(), qr.getUser_id());
					if (rqResult == null) {
						LOGGER.error("Refund DeviceNumber ["
								+ qr.getDevice_number()
								+ "] Query Refundable Money Failed!");
						ops.updateQState(qr, 0, Q_STATE_FAILED);
						continue;
					}
					if (rqResult.getErrorCode() == null) {
						refundableAmount = rqResult.getRefundable();
						LOGGER.debug("Refund DeviceNumber ["
								+ qr.getDevice_number()
								+ "] Query Refundable Money["
								+ refundableAmount + "]");
						/*
						 * if (refundableAmount <= 0L) {
						 * LOGGER.info("Refund DeviceNumber [" +
						 * qr.getDevice_number() +
						 * "] Query Refundable Money <= 0!"); // modified by
						 * tian@10150123 for: JIRA(JD-501) if
						 * (ruem.getUser_event_code().trim()
						 * .equals(USER_EVENT_CODE_KAIHUFANXIAO) ||
						 * ruem.getUser_event_code().trim()
						 * .equals(USER_EVENT_CODE_CHEDAN)) { if
						 * (rqResult.getNonRefundable() <= 0L) {
						 * LOGGER.info("Refund DeviceNumber [" +
						 * qr.getDevice_number() +
						 * "] Query NonRefundable Money <= 0!");
						 * ops.updateQState(qr, 0, Q_STATE_OK); continue; } }
						 * else { ops.updateQState(qr, 0, Q_STATE_OK); continue;
						 * } }
						 */
						LOGGER.debug("Refund DeviceNumber ["
								+ qr.getDevice_number()
								+ "]CRM Enter Refund Amount[" + qr.getRefund()
								+ "]!");
						// 开户返销，撤单需要退全部，且不需要调用POP
						if (ruem.getUser_event_code().trim()
								.equals(USER_EVENT_CODE_KAIHUFANXIAO)
								|| ruem.getUser_event_code().trim()
										.equals(USER_EVENT_CODE_CHEDAN)) {
							refundAmount = refundableAmount;
							long realRefund = 0L;
							if (refundableAmount > 0L) {
								realRefund = ops.updateBalance4RealRefund(qr,
										rqResult.getRefundableBalanceIds(),
										refundAmount, ruem.getBoss_type_code());
							}
							// modified by tian@10150123 for: JIRA(JD-501)
							// modifded by zhanghb JD-1129
							// if (rqResult.getNonRefundable() > 0L) {
							ops.updateBalance4NonRefund(qr,
									ruem.getBoss_type_code());
							// }
							ops.updateQState(qr, realRefund, Q_STATE_OK);
						} else if (ruem.getUser_event_code().trim()
								.equals(USER_EVENT_CODE_XIAOHU)) {
							if (qr.getRefund() <= 0L) {
								LOGGER.info("Refund DeviceNumber ["
										+ qr.getDevice_number()
										+ "]CRM Enter Refund Amount["
										+ qr.getRefund() + "] <= 0!");
								// added by tian@20150619 for: 哎呀，呆逼 ^_^
								// ops.updateBalance4NonRefund(qr,
								// ruem.getBoss_type_code());
								ops.updateQState(qr, 0, Q_STATE_OK);
								continue;
							}
//							long refundableAmountReal=getrefundableAmountReal(rqResult,refundableAmount);
							if (refundableAmount < qr.getRefund()) {
								refundAmount = refundableAmount;
							} else {
								refundAmount = qr.getRefund();
							}
							LOGGER.debug("最后应该扣费:"+refundAmount);
							
//							long realRefund = ops
//									.updateBalance4RealRefund(
//											qr,
//											rqResult.getRefundableBalanceIds(),
//											refundableAmount,
//											ruem.getBoss_type_code());
//							ops.updateQState(qr, refundAmount, Q_STATE_OK);
							
							JdMobileRefundApply jdMobileRefundApply=assembleRequestMessage(qr, p, refundAmount);
							String responseMsg =iMobileRefundService.jdMobileRefundApply(jdMobileRefundApply);
//									httpRestCallback(
//									p.getProperty("RequestUrl"),
//									assembleRequestMessage(qr, p, refundAmount));
							if (responseMsg == null) {
								LOGGER.error("Refund DeviceNumber ["
										+ qr.getDevice_number() + "] Request ["
										+ p.getProperty("RequestUrl")
										+ "] Failed!");
								ops.updateQState(qr, 0, Q_STATE_FAILED);
								continue;
							} else {
								if (parseResponseMessage(responseMsg) == 0) {
									long realRefund = ops
											.updateBalance4RealRefund(
													qr,
													rqResult.getRefundableBalanceIds(),
													refundableAmount,
													ruem.getBoss_type_code());
									// added by tian@20150619 for: 哎呀，呆逼 ^_^
									// ops.updateBalance4NonRefund(qr,
									// ruem.getBoss_type_code());
									ops.updateQState(qr, refundAmount, Q_STATE_OK);
								} else {
									ops.updateQState(qr, 0, Q_STATE_FAILED);
									continue;
								}
							}
						} else {
							ops.updateQState(qr, 0, Q_STATE_FAILED);
							continue;
						}
					} else {
						LOGGER.error("Refund DeviceNumber ["
								+ qr.getDevice_number()
								+ "] Query UserEventCode["
								+ qr.getUser_event_code() + "] ErrorMessage["
								+ rqResult.getErrorMessage() + "]");
						ops.updateQState(qr, 0, Q_STATE_FAILED);
						continue;
					}
				} catch (Exception ex) {
					LOGGER.error("Refund DeviceNumber ["
							+ qr.getDevice_number() + "] Process Failed!"
							+ ex.getMessage());
					ops.updateQState(qr, 0, Q_STATE_FAILED);
					continue;
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				LOGGER.warn(e.getMessage());
			}
		}
	}

	private int parseResponseMessage(String responseMsg) {
		try {
			JSONObject obj = JSONObject.fromObject(responseMsg);
			String responseStatus = (String) obj.get("Status");
			if (responseStatus == null) {
				LOGGER.error("Extract Response Message Failed![Return Status Not Found]");
			} else {
				if (responseStatus.trim().endsWith("1")) {
					return 0;
				} else {
					LOGGER.info("Extract Response Message Status["
							+ responseStatus + "]ErrorCode["
							+ obj.get("ErrorCode") + "]ErrorMessage["
							+ obj.get("ErrorMessage") + "]");
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Extract Response Message Failed![" + ex.getMessage()
					+ "]");
		}
		return -1;
	}

	private JdMobileRefundApply assembleRequestMessage(QRefund qr, Properties p,
			long refundAmount) {
		JdMobileRefundApply jdMobileRefundApply=new JdMobileRefundApply();
		
		jdMobileRefundApply.SessionId=qr.getSerial_no();
		jdMobileRefundApply.JDAcct=qr.getJd_acct();
		jdMobileRefundApply.MSISDN=qr.getDevice_number();
		jdMobileRefundApply.CustName=qr.getCust_name();
		jdMobileRefundApply.RefundReason=p.getProperty("RefundReason");
		jdMobileRefundApply.CustomerName=p.getProperty("CustomerName");
		jdMobileRefundApply.RefundType=p.getProperty("RefundType");
		jdMobileRefundApply.Amount=String.format("%.2f", refundAmount / 100.0f);
		jdMobileRefundApply.CustomerAccount=p.getProperty("CustomerAccount");
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("SessionId", qr.getSerial_no());
//		map.put("JDAcct", qr.getJd_acct());
//		map.put("MSISDN", qr.getDevice_number());
//		map.put("CustName", qr.getCust_name());
//		map.put("RefundReason", p.getProperty("RefundReason"));
//		map.put("CustomerAccount", p.getProperty("CustomerAccount"));
//		map.put("CustomerName", p.getProperty("CustomerName"));
//		map.put("RefundType", p.getProperty("RefundType"));
//		map.put("Amount", String.format("%.2f", refundAmount / 100.0f));

//		JSONObject obj = JSONObject.fromObject(map);
//		String str = obj.toString();
		LOGGER.debug("Request Message[SessionId:"+qr.getSerial_no()+",JDAcct:" + jdMobileRefundApply.JDAcct + ",MSISDN:"+jdMobileRefundApply.MSISDN+",CustName:"+jdMobileRefundApply.CustName+",RefundReason:"+jdMobileRefundApply.RefundReason+",RefundType:"+jdMobileRefundApply.RefundType+",Amount:"+String.format("%.2f", refundAmount / 100.0f)+",CustomerAccount:"+jdMobileRefundApply.CustomerAccount+",CustomerName:"+jdMobileRefundApply.CustomerName+"]");
		return jdMobileRefundApply;
	}

	private String httpRestCallback(String callbackURL, String message) {
		StringBuilder sb = new StringBuilder("");
		HttpURLConnection connection = null;
		try {
			URL url = new URL(callbackURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setReadTimeout(1000);
			connection.setRequestProperty("Content-Type", "text/json");
			connection.connect();
			PrintStream ps = new PrintStream(connection.getOutputStream());
			ps.print(message);
			ps.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				} else {
					sb.append(line);
				}
			}
			br.close();
			LOGGER.debug("Received Message[" + sb.toString() + "]");
		} catch (MalformedURLException e) {
			LOGGER.error(e.getMessage());
			return null;
		} catch (SocketTimeoutException e) {
			LOGGER.error(e.getMessage());
			return null;
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception ex) {
				LOGGER.error(ex.getMessage());
			}
		}
		return sb.toString();
	}

	//获得扣掉礼品卡17%的税之后的实际可退金额，by yanhongxia
//	private long getrefundableAmountReal(RefundQueryResult rQueryResult,long amount) {
//		List<InfoPayBalance> infoPayBalances=rQueryResult.getAllInfoPayBalance();
//		long giftAmount=0;
//		if (null!=infoPayBalances && infoPayBalances.size()>0) {
//			for(InfoPayBalance ipb:infoPayBalances){
//				if (ipb.getBalance_type_id()==28) {
//					giftAmount+=ipb.getReal_balance();
//				}
//			}
//		}
//		double realAmountDou=amount-giftAmount*0.17;
//		long realAmount=Math.round(realAmountDou);
//		return realAmount;
//	}
	public RefundOps getOps() {
		return ops;
	}

	public void setOps(RefundOps ops) {
		this.ops = ops;
	}

	public RefundQuery getrQuery() {
		return rQuery;
	}

	public void setrQuery(RefundQuery rQuery) {
		this.rQuery = rQuery;
	}

	public DSUserEventMapping getUserEventMapping() {
		return userEventMapping;
	}

	public void setUserEventMapping(DSUserEventMapping userEventMapping) {
		this.userEventMapping = userEventMapping;
	}

	public IMobileRefundService getiMobileRefundService() {
		return iMobileRefundService;
	}

	public void setiMobileRefundService(IMobileRefundService iMobileRefundService) {
		this.iMobileRefundService = iMobileRefundService;
	}
	
	
}
