package com.tydic.beijing.billing.account.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.biz.RechargeCallbackOps;
import com.tydic.beijing.billing.account.service.RechargeCallback;
import com.tydic.beijing.billing.dao.QBlock;
import com.tydic.beijing.billing.dao.QBlockUrge;
import com.tydic.beijing.billing.dao.QRechargeCallback;


public class RechargeCallbackImpl implements RechargeCallback {

	private final static Logger LOGGER = Logger
			.getLogger(RechargeCallbackImpl.class);

	private RechargeCallbackOps ops;

	private final static int Q_STATE_INIT = 11;
	private final static int Q_STATE_OK = 2;
	private final static int Q_STATE_CALLBACK_FAILED = 8;

	private final static int HTTP_REQUEST_RETRY_TIMES = 3;
	/*
	 * private final static String[] attrNames = { "agent_order_no",
	 * "fill_amount", "fill_mobile", "fill_time", "fill_type", "isp_id",
	 * "notify_url", "partner", "pay_no ", "province_id", "request_order_no",
	 * "sales_amount", "timestamp", "version", "key" };
	 */
	private final static String[] attrNames = { "agent_id", "agent_order_no",
			"fill_amount", "fill_time", "isp_id", "order_state", "partner",
			"province_id", "request_order_no", "sign", "sign_type",
			"timestamp", "version", "key" };

	@Override
	public void process(int mod,int partition) throws ParseException {
		Properties p = new Properties();
		
		try {
			p.load(RechargeCallbackImpl.class.getClassLoader()
					.getResourceAsStream("rechargecallback.properties"));
		} catch (Exception ex) {
			LOGGER.error("load rechargecallback.properties error!!!");
			LOGGER.error(ex.getMessage());
			System.exit(0);
		}
		while (true) {
			List<QRechargeCallback> qrcs = ops.scanQRechargeCallbackUpdate(mod, partition);
			for (QRechargeCallback qrc : qrcs) {
				String expMessage=qrc.getProcess_failed_desc();
				String responseMsg = null;
				String processTime = getCurrentTime();
				if (qrc.getState()==Q_STATE_INIT) {
					LOGGER.debug("Processing Q_Recharge_Callback Record.Serial_No["
							+ qrc.getSerial_no() + "]");
					if ((qrc.getCallbackurl() != null)
							&& !(qrc.getCallbackurl().trim().equals(""))) {
						for (int i = 0; i < HTTP_REQUEST_RETRY_TIMES; i++) {
							String signatureMD5 = makeMD5(produceSign(qrc, p,
									processTime),qrc);
							if (signatureMD5 == null) {
								break;
							}
							responseMsg = httpRestCallback(
									qrc.getCallbackurl(),
									assembleRequestMessage4Get(qrc, p, signatureMD5,
											processTime),qrc);
							if (responseMsg == null) {
								continue;
							} else {
								break;
							}
						}
						if (responseMsg == null) {
							LOGGER.debug("Recharge Callback for Record.Serial_No["
									+ qrc.getSerial_no() + "] Request ["
									+ qrc.getCallbackurl() + "] ["
									+ HTTP_REQUEST_RETRY_TIMES
									+ "]times and ALL FAILED!");
							ops.updateQState(qrc, Q_STATE_CALLBACK_FAILED,expMessage);
							continue;
						} else {
							if (parseResponseMessage(responseMsg) != 0) {
								ops.updateQState(qrc, Q_STATE_CALLBACK_FAILED,expMessage);
								continue;
							}
						}
					} else {
						LOGGER.warn("Processing Q_Recharge_Callback Record.Serial_No["
								+ qrc.getSerial_no() + "] NO CallbackURL!");
					}
					ops.updateQState(qrc, Q_STATE_OK,expMessage);
					LOGGER.debug("Processing Q_Recharge_Callback Record.Serial_No["
							+ qrc.getSerial_no() + "] Done!");
					
					//将处理成功的记录移到log表       
					//SimpleDateFormat sdfdate = new SimpleDateFormat("YYYY/MM/DD hh:mi:ss");
					//Date date=new Date();
					//String currentDate=sdfdate.format(date);
						LOGGER.debug("开始插log表！");
							ops.insertQRechargeCallbackLog(qrc);
							ops.deleteQRechargeCallback(qrc);// 删除处理成功的记录
				}
				
				
				if ((qrc.getCallbackurl() != null)
						&& !(qrc.getCallbackurl().trim().equals(""))) {
					for (int i = 0; i < HTTP_REQUEST_RETRY_TIMES; i++) {
						String signatureMD5 = makeMD5(produceSign(qrc, p,
								processTime),qrc);
						if (signatureMD5 == null) {
							break;
						}
						responseMsg = httpRestCallback(
								qrc.getCallbackurl(),
								assembleRequestMessage4Get(qrc, p, signatureMD5,
										processTime),qrc);
						if (responseMsg == null) {
							continue;
						} else {
							break;
						}
					}
					if (responseMsg == null) {
						LOGGER.debug("Recharge Callback for Record.Serial_No["
								+ qrc.getSerial_no() + "] Request ["
								+ qrc.getCallbackurl() + "] ["
								+ HTTP_REQUEST_RETRY_TIMES
								+ "]times and ALL FAILED!");
						ops.updateQState(qrc, Q_STATE_CALLBACK_FAILED,expMessage);
						continue;
					} else {
						if (parseResponseMessage(responseMsg) != 0) {
							ops.updateQState(qrc, Q_STATE_CALLBACK_FAILED,expMessage);
							continue;
						}
					}
				} else {
					LOGGER.warn("Processing Q_Recharge_Callback Record.Serial_No["
							+ qrc.getSerial_no() + "] NO CallbackURL!");
				}
				ops.updateQState(qrc, Q_STATE_OK,expMessage);
				LOGGER.debug("Processing Q_Recharge_Callback Record.Serial_No["
						+ qrc.getSerial_no() + "] Done!");
				
				//将处理成功的记录移到log表       
				//SimpleDateFormat sdfdate = new SimpleDateFormat("YYYY/MM/DD hh:mi:ss");
				//Date date=new Date();
				//String currentDate=sdfdate.format(date);
					LOGGER.debug("开始插log表！");
						ops.insertQRechargeCallbackLog(qrc);
						ops.deleteQRechargeCallback(qrc);// 删除处理成功的记录		
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.warn(e.getMessage());
			}
		}
	}

	/*
	 * private String httpRestCallback(String callbackURL, String message) {
	 * StringBuilder sb = new StringBuilder(""); HttpURLConnection connection =
	 * null; try { URL url = new URL(callbackURL); connection =
	 * (HttpURLConnection) url.openConnection();
	 * connection.setRequestMethod("POST"); connection.setDoOutput(true);
	 * connection.setDoInput(true); connection.setUseCaches(false);
	 * connection.setReadTimeout(1000); connection.connect(); PrintStream ps =
	 * new PrintStream(connection.getOutputStream()); ps.print(message);
	 * ps.close(); BufferedReader br = new BufferedReader(new InputStreamReader(
	 * connection.getInputStream())); while (true) { String line =
	 * br.readLine(); if (line == null) { break; } else { sb.append(line); } }
	 * br.close(); LOGGER.debug("Received Message[" + sb.toString() + "]"); }
	 * catch (MalformedURLException e) { LOGGER.error(e.getMessage()); return
	 * null; } catch (SocketTimeoutException e) { LOGGER.error(e.getMessage());
	 * return null; } catch (IOException e) { LOGGER.error(e.getMessage());
	 * return null; } finally { try { if (connection != null) {
	 * connection.disconnect(); } } catch (Exception ex) {
	 * LOGGER.error(ex.getMessage()); } } return sb.toString(); }
	 */
	// private String httpRestCallback(String callbackURL, NameValuePair[] vals) {
	private String httpRestCallback(String callbackURL, String vals,QRechargeCallback qrc) {
		HttpClient client = new HttpClient();
		String url = callbackURL;		
		int hostPos = url.indexOf("com/");		
		String hostUrl = url.substring(7, hostPos + 3);
		String postUrl = url.substring(hostPos + 3, url.length());
		// PostMethod post = null;
		GetMethod get = null;
		String expMessage=qrc.getProcess_failed_desc();
		
		try {
			
			client.getHostConfiguration().setHost(hostUrl);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
			client.getHttpConnectionManager().getParams().setSoTimeout(2000);
			/*
			post = new PostMethod(postUrl);
			post.setRequestHeader("Content-Type", "text/html;charset=utf-8");
			post.setRequestBody(vals);
			client.executeMethod(post);
			LOGGER.debug("Request URL[" + callbackURL + "] StatusLine["
					+ post.getStatusLine() + "]");
			String response = new String(post.getResponseBodyAsString()
					.getBytes("UTF-8"));
			LOGGER.debug(response);
			post.releaseConnection();
			*/
			get = new GetMethod(postUrl + "?" + vals);
			client.executeMethod(get);
			LOGGER.debug("Request URL[" + callbackURL + "] StatusLine["
					+ get.getStatusLine() + "]");
			String response = new String(get.getResponseBodyAsString()
					.getBytes("UTF-8"));
			LOGGER.debug(response);
			get.releaseConnection();
			return response;
		} catch (URIException e) {
			expMessage=e.getMessage();
			ops.updateQState(qrc,Q_STATE_CALLBACK_FAILED ,expMessage);
			LOGGER.error("httpRestCallback:URIException e");
			LOGGER.error(e.getMessage());
			return null;
		} catch (HttpException e) {
			expMessage=e.getMessage();
			ops.updateQState(qrc,Q_STATE_CALLBACK_FAILED ,expMessage);
			LOGGER.error("httpRestCallback:HttpException e");
			LOGGER.error(e.getMessage());
			return null;
		} catch (IOException e) {
			expMessage=e.getMessage();
			ops.updateQState(qrc, Q_STATE_CALLBACK_FAILED,expMessage);
			LOGGER.error("httpRestCallback:IOException e");
			LOGGER.error(e.getMessage(),e);
			return null;
		}
	}

	public String makeMD5(String s,QRechargeCallback qrc) {
		String result = null;
		MessageDigest md5 = null;
		String expMessage=qrc.getProcess_failed_desc();
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(s.getBytes("UTF-8"));
			// modified by tian@20150911 for: 修复原转换方法当高位为0时会省略的缺陷
			// result = new BigInteger(1, md5.digest()).toString(16);
			byte b[] = md5.digest(); // 128位2进制, 8*16
			// 128位二进制，转换16进制需32位
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256; // 高位2字节清0
				if (i < 16)
					buf.append("0"); // 低位1字节补0
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			LOGGER.debug("After Sign Encryption[" + result + "]");
		} catch (NoSuchAlgorithmException e) {
			expMessage=e.getMessage();
			ops.updateQState(qrc,Q_STATE_CALLBACK_FAILED , expMessage);
			LOGGER.error("makeMD5:NoSuchAlgorithmException e!!!");
			LOGGER.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			expMessage=e.getMessage();
			ops.updateQState(qrc, Q_STATE_CALLBACK_FAILED, expMessage);
			LOGGER.error("makeMD5:UnsupportedEncodingException e!!!");
			LOGGER.error(e.getMessage());
		}
		return result;
	}

	/*
	 * private String produceSign(QRechargeCallback qrc, Properties p, String
	 * processTime) { StringBuilder sb = new StringBuilder(""); String fillType
	 * = null; String fillAmountStr = null; if ((qrc.getSerial_no() != null) &&
	 * (!qrc.getSerial_no().trim().equals(""))) { sb.append(attrNames[0] +
	 * qrc.getSerial_no()); } if ((qrc.getRecharge_detail() != null) &&
	 * (!qrc.getRecharge_detail().trim().equals(""))) { String detail =
	 * qrc.getRecharge_detail(); String[] amountGroup = detail.split(";"); long
	 * fillAmount = 0L; for (String s : amountGroup) { String[] amountPiece =
	 * s.split(":"); fillAmount += Long.parseLong(amountPiece[1]); fillType =
	 * amountPiece[2]; } fillAmountStr = String.format("%.2f", fillAmount /
	 * 100.0f); sb.append(attrNames[1] + fillAmountStr); } if
	 * ((qrc.getDevice_number() != null) &&
	 * (!qrc.getDevice_number().trim().equals(""))) { sb.append(attrNames[2] +
	 * qrc.getDevice_number()); } if ((qrc.getPay_time() != null) &&
	 * (!qrc.getPay_time().equals(""))) { sb.append(attrNames[3] +
	 * qrc.getPay_time().replace("-", "").replace(" ", "") .replace(":", "")); }
	 * if (fillType != null) { sb.append(attrNames[4] + fillType); } String
	 * ispId = p.getProperty(attrNames[4]); if ((ispId != null) &&
	 * (!ispId.trim().equals(""))) { sb.append(attrNames[5] + ispId); } if
	 * ((qrc.getCallbackurl() != null) &&
	 * (!qrc.getCallbackurl().trim().equals(""))) { sb.append(attrNames[6] +
	 * qrc.getCallbackurl()); } String partner = p.getProperty(attrNames[6]); if
	 * ((partner != null) && (!partner.trim().equals(""))) {
	 * sb.append(attrNames[7] + partner); } if ((qrc.getPay_id() != null) &&
	 * (!qrc.getPay_id().trim().equals(""))) { sb.append(attrNames[8] +
	 * qrc.getPay_id()); } String provinceId = p.getProperty(attrNames[8]); if
	 * ((provinceId != null) && (!provinceId.trim().equals(""))) {
	 * sb.append(attrNames[9] + provinceId); } if ((qrc.getSerial_no() != null)
	 * && (!qrc.getSerial_no().trim().equals(""))) { sb.append(attrNames[10] +
	 * qrc.getSerial_no()); } if ((fillAmountStr != null) &&
	 * (!fillAmountStr.trim().equals(""))) { sb.append(attrNames[11] +
	 * fillAmountStr); } if ((processTime != null) && (!processTime.equals("")))
	 * { sb.append(attrNames[12] + processTime); } String version =
	 * p.getProperty(attrNames[13]); if ((version != null) &&
	 * (!version.trim().equals(""))) { sb.append(attrNames[13] + version); }
	 * String keys = p.getProperty(attrNames[14]); if ((keys != null) &&
	 * (!keys.trim().equals(""))) { sb.append(keys); } String sign =
	 * sb.toString(); LOGGER.debug("Ready for Sign Encryption[" + sign + "]");
	 * 
	 * return sign; }
	 */
	private String produceSign(QRechargeCallback qrc, Properties p,
			String timestatmp) {
		LOGGER.debug("produceSign:开始！！！");
		StringBuilder sb = new StringBuilder("");
		String fillAmountStr = null;
		String agentId = p.getProperty(attrNames[0]);
		if ((agentId != null) && (!agentId.trim().equals(""))) {
			sb.append(attrNames[0] + (agentId));
		}
		if ((qrc.getSerial_no() != null)
				&& (!qrc.getSerial_no().trim().equals(""))) {
			sb.append(attrNames[1] + (qrc.getSerial_no()));
		}
		if ((qrc.getRecharge_detail() != null)
				&& (!qrc.getRecharge_detail().trim().equals(""))) {
			String detail = qrc.getRecharge_detail();
			String[] amountGroup = detail.split(";");
			long fillAmount = 0L;
			for (String s : amountGroup) {
				String[] amountPiece = s.split(":");
				fillAmount += Long.parseLong(amountPiece[1]);
			}
			fillAmountStr = String.format("%.2f", fillAmount / 100.0f);
			sb.append(attrNames[2] + (fillAmountStr));
		}
		if ((qrc.getPay_time() != null) && (!qrc.getPay_time().equals(""))) {
			String payTime = qrc.getPay_time().replace("-", "")
					.replace(" ", "").replace(":", "");
			sb.append(attrNames[3] + (payTime));
		}
		String ispId = p.getProperty(attrNames[4]);
		if ((ispId != null) && (!ispId.trim().equals(""))) {
			sb.append(attrNames[4] + (ispId));
		}
		String orderState = "1";
		sb.append(attrNames[5] + (orderState));
		String partner = p.getProperty(attrNames[6]);
		if ((partner != null) && (!partner.trim().equals(""))) {
			sb.append(attrNames[6] + (partner));
		}
		String provinceId = p.getProperty(attrNames[7]);
		if ((provinceId != null) && (!provinceId.trim().equals(""))) {
			sb.append(attrNames[7] + (provinceId));
		}
		if ((qrc.getSerial_no() != null)
				&& (!qrc.getSerial_no().trim().equals(""))) {
			sb.append(attrNames[8] + (qrc.getSerial_no()));
		}
		// attrNames[9] sign
		// attrNames[10] sign_type
		sb.append(attrNames[11] + (timestatmp));
		String version = p.getProperty(attrNames[12]);
		if ((version != null) && (!version.trim().equals(""))) {
			sb.append(attrNames[12] + (version));
		}
		String keys = p.getProperty(attrNames[13]);
		if ((keys != null) && (!keys.trim().equals(""))) {
			sb.append(keys);
		}
		String sign = sb.toString();
		LOGGER.debug("Ready for Sign Encryption[" + sign + "]");

		return sign;
	}

	/*
	 * private String assembleRequestMessage(QRechargeCallback qrc, Properties
	 * p, String signatureMD5, String processTime) { Document document =
	 * DocumentHelper.createDocument(); Element root =
	 * DocumentHelper.createElement("recharge"); document.setRootElement(root);
	 * Element isSuccess = root.addElement("is_success");
	 * isSuccess.addText("Y"); Element response = root.addElement("response");
	 * Element protocol = response.addElement("protocol"); Element partner =
	 * protocol.addElement("param"); partner.addAttribute("name", "partner");
	 * partner.addText(p.getProperty("partner")); Element sign =
	 * protocol.addElement("param"); sign.addAttribute("name", "sign");
	 * sign.addText(signatureMD5); Element sign_type =
	 * protocol.addElement("param"); sign_type.addAttribute("name",
	 * "sign_type"); sign_type.addText(p.getProperty("sign_type")); Element
	 * timestamp = protocol.addElement("param"); timestamp.addAttribute("name",
	 * "timestamp"); timestamp.addText(processTime); Element version =
	 * protocol.addElement("param"); version.addAttribute("name", "version");
	 * version.addText(p.getProperty("version")); Element service =
	 * response.addElement("service"); Element request_order_no =
	 * service.addElement("param"); request_order_no.addAttribute("name",
	 * "request_order_no"); request_order_no.addText(qrc.getSerial_no());
	 * Element agent_order_no = service.addElement("param");
	 * agent_order_no.addAttribute("name", "agent_order_no");
	 * agent_order_no.addText(qrc.getSerial_no()); Element fill_mobile =
	 * service.addElement("param"); fill_mobile.addAttribute("name",
	 * "fill_mobile"); fill_mobile.addText(qrc.getDevice_number()); Element
	 * fill_amount = service.addElement("param");
	 * fill_amount.addAttribute("name", "fill_amount"); // split&addup amount
	 * String detail = qrc.getRecharge_detail(); String[] amountGroup =
	 * detail.split(";"); String fillType = null; long fillAmount = 0L; for
	 * (String s : amountGroup) { String[] amountPiece = s.split(":");
	 * fillAmount += Long.parseLong(amountPiece[1]); fillType = amountPiece[2];
	 * } fill_amount.addText(String.format("%.2f", fillAmount / 100.0f));
	 * Element fill_type = service.addElement("param");
	 * fill_type.addAttribute("name", "fill_type"); fill_type.addText(fillType);
	 * Element fill_time = protocol.addElement("param");
	 * fill_time.addAttribute("name", "fill_time");
	 * fill_time.addText(qrc.getPay_time().replace("-", "").replace(" ", "")
	 * .replace(":", "")); Element notify_url = service.addElement("param");
	 * notify_url.addAttribute("name", "notify_url");
	 * notify_url.addText(qrc.getCallbackurl()); Element pay_no =
	 * service.addElement("param"); pay_no.addAttribute("name", "pay_no");
	 * pay_no.addText(qrc.getPay_id()); Element isp_id =
	 * service.addElement("param"); isp_id.addAttribute("name", "isp_id");
	 * isp_id.addText(p.getProperty("isp_id")); Element province_id =
	 * service.addElement("param"); province_id.addAttribute("name",
	 * "province_id"); province_id.addText(p.getProperty("province_id"));
	 * Element sales_amount = service.addElement("param");
	 * sales_amount.addAttribute("name", "sales_amount");
	 * sales_amount.addText(String.format("%.2f", fillAmount / 100.0f)); String
	 * dXml = document.asXML(); LOGGER.debug("Request Message[" + dXml + "]");
	 * return document.asXML(); }
	 */
	private NameValuePair[] assembleRequestMessage(QRechargeCallback qrc,
			Properties p, String signatureMD5, String timestamp) {
		NameValuePair[] nvPairs = new NameValuePair[13];
		String fillAmountStr = null;

		String agentId = p.getProperty(attrNames[0]);
		if ((agentId != null) && (!agentId.trim().equals(""))) {
			NameValuePair val0 = new NameValuePair(attrNames[0], encoding2GBK(agentId));
			nvPairs[0] = val0;
		}
		if ((qrc.getSerial_no() != null)
				&& (!qrc.getSerial_no().trim().equals(""))) {
			NameValuePair val1 = new NameValuePair(attrNames[1],
					encoding2GBK(qrc.getSerial_no()));
			nvPairs[1] = val1;
		}
		if ((qrc.getRecharge_detail() != null)
				&& (!qrc.getRecharge_detail().trim().equals(""))) {
			String detail = qrc.getRecharge_detail();
			String[] amountGroup = detail.split(";");
			long fillAmount = 0L;
			for (String s : amountGroup) {
				String[] amountPiece = s.split(":");
				fillAmount += Long.parseLong(amountPiece[1]);
			}
			fillAmountStr = String.format("%.2f", fillAmount / 100.0f);
			NameValuePair val2 = new NameValuePair(attrNames[2], encoding2GBK(fillAmountStr));
			nvPairs[2] = val2;
		}
		if ((qrc.getPay_time() != null) && (!qrc.getPay_time().equals(""))) {
			String payTime = qrc.getPay_time().replace("-", "").replace(" ", "").replace(":", "");
			NameValuePair val3 = new NameValuePair(attrNames[3], encoding2GBK(payTime));
			nvPairs[3] = val3;
		}
		String ispId = p.getProperty(attrNames[4]);
		if ((ispId != null) && (!ispId.trim().equals(""))) {
			NameValuePair val4 = new NameValuePair(attrNames[4], encoding2GBK(ispId));
			nvPairs[4] = val4;
		}
		String orderState = "1";
		NameValuePair val5 = new NameValuePair(attrNames[5], encoding2GBK(orderState));
		nvPairs[5] = val5;
		String partner = p.getProperty(attrNames[6]);
		if ((partner != null) && (!partner.trim().equals(""))) {
			NameValuePair val6 = new NameValuePair(attrNames[6], encoding2GBK(partner));
			nvPairs[6] = val6;
		}
		String provinceId = p.getProperty(attrNames[7]);
		if ((provinceId != null) && (!provinceId.trim().equals(""))) {
			NameValuePair val7 = new NameValuePair(attrNames[7], encoding2GBK(provinceId));
			nvPairs[7] = val7;
		}
		if ((qrc.getSerial_no() != null)
				&& (!qrc.getSerial_no().trim().equals(""))) {
			NameValuePair val8 = new NameValuePair(attrNames[8],
					encoding2GBK(qrc.getSerial_no()));
			nvPairs[8] = val8;
		}
		// attrNames[9] sign
		NameValuePair val9 = new NameValuePair(attrNames[9], encoding2GBK(signatureMD5));
		nvPairs[9] = val9;
		// attrNames[10] sign_type
		String signType = p.getProperty(attrNames[10]);
		if ((signType != null) && (!signType.trim().equals(""))) {
			NameValuePair val10 = new NameValuePair(attrNames[10], encoding2GBK(signType));
			nvPairs[10] = val10;
		}
		NameValuePair val11 = new NameValuePair(attrNames[11], encoding2GBK(timestamp));
		nvPairs[11] = val11;
		String version = p.getProperty(attrNames[12]);
		if ((version != null) && (!version.trim().equals(""))) {
			NameValuePair val12 = new NameValuePair(attrNames[12], encoding2GBK(version));
			nvPairs[12] = val12;
		}
		return nvPairs;
	}
	
	private String assembleRequestMessage4Get(QRechargeCallback qrc,
			Properties p, String signatureMD5, String timestamp) {
		LOGGER.debug("assembleRequestMessage4Get:开始！！！");
		StringBuilder sb = new StringBuilder("");
		String fillAmountStr = null;

		String agentId = p.getProperty(attrNames[0]);
		if ((agentId != null) && (!agentId.trim().equals(""))) {
			sb.append(attrNames[0]);
			sb.append("=");
			sb.append(encoding2GBK(agentId));
			sb.append("&");
		}
		if ((qrc.getSerial_no() != null)
				&& (!qrc.getSerial_no().trim().equals(""))) {
			sb.append(attrNames[1]);
			sb.append("=");
			sb.append(encoding2GBK(qrc.getSerial_no()));
			sb.append("&");
		}
		if ((qrc.getRecharge_detail() != null)
				&& (!qrc.getRecharge_detail().trim().equals(""))) {
			String detail = qrc.getRecharge_detail();
			String[] amountGroup = detail.split(";");
			long fillAmount = 0L;
			for (String s : amountGroup) {
				String[] amountPiece = s.split(":");
				fillAmount += Long.parseLong(amountPiece[1]);
			}
			fillAmountStr = String.format("%.2f", fillAmount / 100.0f);
			sb.append(attrNames[2]);
			sb.append("=");
			sb.append(encoding2GBK(fillAmountStr));
			sb.append("&");
		}
		if ((qrc.getPay_time() != null) && (!qrc.getPay_time().equals(""))) {
			String payTime = qrc.getPay_time().replace("-", "").replace(" ", "").replace(":", "");	
			sb.append(attrNames[3]);
			sb.append("=");
			sb.append(encoding2GBK(payTime));
			sb.append("&");
		}
		String ispId = p.getProperty(attrNames[4]);
		if ((ispId != null) && (!ispId.trim().equals(""))) {
			sb.append(attrNames[4]);
			sb.append("=");
			sb.append(encoding2GBK(ispId));
			sb.append("&");
		}
		String orderState = "1";
		sb.append(attrNames[5]);
		sb.append("=");
		sb.append(encoding2GBK(orderState));
		sb.append("&");
		String partner = p.getProperty(attrNames[6]);
		if ((partner != null) && (!partner.trim().equals(""))) {
			sb.append(attrNames[6]);
			sb.append("=");
			sb.append(encoding2GBK(partner));
			sb.append("&");
		}
		String provinceId = p.getProperty(attrNames[7]);
		if ((provinceId != null) && (!provinceId.trim().equals(""))) {
			sb.append(attrNames[7]);
			sb.append("=");
			sb.append(encoding2GBK(provinceId));
			sb.append("&");
		}
		if ((qrc.getSerial_no() != null)
				&& (!qrc.getSerial_no().trim().equals(""))) {
			sb.append(attrNames[8]);
			sb.append("=");
			sb.append(encoding2GBK(qrc.getSerial_no()));
			sb.append("&");
		}
		// attrNames[9] sign
		sb.append(attrNames[9]);
		sb.append("=");
		sb.append(encoding2GBK(signatureMD5));
		sb.append("&");
		// attrNames[10] sign_type
		String signType = p.getProperty(attrNames[10]);
		if ((signType != null) && (!signType.trim().equals(""))) {
			sb.append(attrNames[10]);
			sb.append("=");
			sb.append(encoding2GBK(signType));
			sb.append("&");
		}
		sb.append(attrNames[11]);
		sb.append("=");
		sb.append(encoding2GBK(timestamp));
		sb.append("&");
		String version = p.getProperty(attrNames[12]);
		if ((version != null) && (!version.trim().equals(""))) {
			sb.append(attrNames[12]);
			sb.append("=");
			sb.append(encoding2GBK(version));
		}
		String retStr = sb.toString();
		LOGGER.debug("Assembled[" + retStr + "]");
		return retStr;
	}

	private int parseResponseMessage(String responseMessage) {
		LOGGER.debug("Received From JD[" + responseMessage + "]");
		if ((responseMessage != null)
				&& (responseMessage.trim().equalsIgnoreCase("Y"))) {
			return 0;
		}
		return -1;
	}

	private String getCurrentTime() {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date());
	}

	private String encoding2GBK(String value) {
		try {
			String afterEncode = java.net.URLEncoder.encode(value, "GBK");
			LOGGER.debug("Before Encode[" + value + "] After Encode["
					+ afterEncode + "]");
			return afterEncode;
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Charset [GBK] is NOT SUPPORTED!");
			return value;
		}
	}

	public RechargeCallbackOps getOps() {
		return ops;
	}

	public void setOps(RechargeCallbackOps ops) {
		this.ops = ops;
	}

//	public AccountProcess getAccountProcess() {
//		return accountProcess;
//	}
//
//	public void setAccountProcess(AccountProcess accountProcess) {
//		this.accountProcess = accountProcess;
//	}
	
	
}
