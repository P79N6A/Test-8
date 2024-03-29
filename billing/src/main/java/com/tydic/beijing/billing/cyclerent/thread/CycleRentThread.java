package com.tydic.beijing.billing.cyclerent.thread;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.cyclerent.biz.CycleRentBatchOps;
import com.tydic.beijing.billing.cyclerent.type.InfoUser;
import com.tydic.beijing.billing.cyclerent.type.ProductInfo;
import com.tydic.beijing.billing.dao.CDRCycle;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.HlpSmsSend;
import com.tydic.beijing.billing.dao.InfoProtocolDetail;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.LogCycleHistory;
import com.tydic.beijing.billing.dao.QAcctProcess;
import com.tydic.beijing.billing.dao.RuleOfrSplit;
import com.tydic.beijing.billing.dao.RuleTariffConfInfo;

public class CycleRentThread implements Runnable, ApplicationContextAware {
	private static final Logger LOGGER = Logger
			.getLogger(CycleRentThread.class);
	ApplicationContext mycontext;
	private int process_num;
	private int channel_no;// 控制进程（mod(user_id,?)）
	private int mod;// 结合mod_i,控制线程
	private int mod_i;//
	private int num;// 批量处理数量
	private String CycleAlarm;
	private String event_type;// 事件类型
	private CycleRentBatchOps ops;

	public void run() {
		try {
			DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			List<QAcctProcess> lQacctProcess = new ArrayList<QAcctProcess>();
			List<CDRCycle> lcdr = new ArrayList<CDRCycle>();
			List<LogCycleHistory> dLogCycleHistory = new ArrayList<LogCycleHistory>();
			List<LogCycleHistory> iLogCycleHistory = new ArrayList<LogCycleHistory>();
			List<HlpSmsSend> lHlpSmsSend = new ArrayList<HlpSmsSend>();
			ops = (CycleRentBatchOps) mycontext.getBean("CycleRentBatchOps");
//			cabOps = (CreateAccountBook) mycontext.getBean("createAccountBook");
			ops.load(event_type);// 加载规则表
			CodeAcctMonth codeAcctMonth = null;
			if (event_type.trim().equals("10131")) {
				codeAcctMonth = ops.getCodeAcctMonth();
			} else if (event_type.trim().equals("20131")) {
				codeAcctMonth = ops.getCodeAcctMonthByActTag();
			}
			if (codeAcctMonth == null) {
				LOGGER.error("------当前账期错误-----");
				return;
			}
			LOGGER.debug("----" + codeAcctMonth.toString() + "--------");

			List<InfoUser> lInfoUser = ops.getInfoUser(process_num, channel_no,
					mod, mod_i);// user_id,user_product_id取模
			if (lInfoUser == null) {
				LOGGER.debug("-----mod(user_id," + process_num + ")="
						+ channel_no + ",mod(device_number," + mod + "="
						+ mod_i + "),already deal ------");
				return;
			}
			for (InfoUser infoUser : lInfoUser) {
				long t1 = System.currentTimeMillis();
				LOGGER.debug("---begin time :" + t1);
				LOGGER.debug("----" + infoUser.toString()
						+ "-------deal begin -----");
//				PayUserRel payUserRel = null;
//				String smsTempl = "";
//				if (CycleAlarm.trim().equals("true")) {
//					payUserRel = ops.getPayUserRelByUserId(infoUser
//							.getUser_id());
//					if (payUserRel == null) {
//						LOGGER.error("---当前用户user_id[" + infoUser.getUser_id()
//								+ "],没有账户------");
//						continue;
//					}
//					LOGGER.debug(payUserRel.toString());
//				}
				String actExpDate = sdf.format(codeAcctMonth.getAct_exp_date());
				List<LifeUserProduct> lLifeUserProduct = ops
						.getLifeUserProductByUserId(infoUser.getUser_id(),Long.valueOf(event_type),actExpDate);
				if (lLifeUserProduct == null || lLifeUserProduct.isEmpty()) {
					LOGGER.debug("---当前用户user_id[" + infoUser.getUser_id()
							+ "]没有订购信息------");
					continue;
				}
				int agreement_type = 0;

				long nt1 = System.currentTimeMillis();
				// 获取用户合约类型
				if (ops.effSystemSwitch(BasicType.PROTOCOL_SWITCH)) {
					List<String> proto_type = new ArrayList<String>();
					// 获取用户合约类型
					List<InfoProtocolDetail> linfoProtocolDetail = ops.getUserProtocol(infoUser.getUser_id());
					if (linfoProtocolDetail == null || linfoProtocolDetail.isEmpty()) {
						LOGGER.debug("----user_id[" + infoUser.getUser_id()
								+ "],没有合约协议-----");
					} else {
						LOGGER.debug("---" + linfoProtocolDetail.toString() + "-----");
						for(InfoProtocolDetail ipd: linfoProtocolDetail){
							proto_type.add(ipd.getProto_type());
						}
					}
					
					agreement_type = ops.getAgreementType(proto_type,infoUser);
				}

				// 获取用户副卡信息
				int vicecard = 0;
//				if (ops.effSystemSwitch(BasicType.CARD_SWITCH)) {
//					List<InfoAuthMobile> liam = ops.getInfoAuthMobile(infoUser
//							.getDevice_number());
//					if (liam != null && !liam.isEmpty()) {
//						vicecard = liam.size();
//					} else {
//						LOGGER.debug("----该用户[" + infoUser.getUser_id()
//								+ "]没有副卡-----------");
//					}
//				}

				long nt2 = System.currentTimeMillis();
				LOGGER.debug("---infoprotocol time :"
						+ (System.currentTimeMillis() - nt1));
				LOGGER.debug("---lifeUserProduct time :"
						+ (System.currentTimeMillis() - nt2));
				LOGGER.debug("-#######################-user info begin----##############---");
				LOGGER.debug(infoUser.toString());
				LOGGER.debug(lLifeUserProduct.toString());
				LOGGER.debug("-####################---user info end--#########################------");
				LOGGER.debug("--get user info time :"
						+ (System.currentTimeMillis() - t1));
				Map<ProductInfo, List<RuleTariffConfInfo>> map = new HashMap<ProductInfo, List<RuleTariffConfInfo>>();
				// 月租
				for (LifeUserProduct product : lLifeUserProduct) {
					long mt1 = System.currentTimeMillis();
					ProductInfo productInfo = new ProductInfo();
					List<RuleTariffConfInfo> lrtci = new ArrayList<RuleTariffConfInfo>();
					LOGGER.debug("==product:" + product.toString());
					List<RuleOfrSplit> lOfrSplit = ops.getRuleOfrSplit(product
							.getProduct_id());
					if (lOfrSplit == null || lOfrSplit.isEmpty()) {
						continue;
					}
					for (RuleOfrSplit ruleOfrSplit : lOfrSplit) {
						LOGGER.debug("====ruleOfrSplit:"
								+ ruleOfrSplit.toString() + "----");
						ops.getTariffId(ruleOfrSplit.getOfr_c_id(), infoUser,
								event_type, vicecard, lrtci,
								product.getEff_date(), agreement_type);
						if (lrtci == null || lrtci.isEmpty()) {
							continue;
						}
					}
					if (lrtci == null || lrtci.isEmpty()) {
						LOGGER.debug("-用户[" + infoUser.getUser_id() + "],产品["
								+ product.getProduct_id() + "],用户状态["
								+ infoUser.getUser_status() + "],找不到月租资费信息");
						continue;
					}
					long mt2 = System.currentTimeMillis();
					LOGGER.debug("---ofr_c_id comput tariff time :"
							+ (mt2 - mt1));
					productInfo.setProduct_id(product.getProduct_id());
					productInfo
							.setUser_product_id(product.getUser_product_id());
					map.put(productInfo, lrtci);
					LOGGER.debug("--product_id:" + product.getProduct_id()
							+ ",list<TariffInfo>:" + lrtci.toString());
				}
				LOGGER.debug("---all product info:" + map.toString());
				long mt3 = System.currentTimeMillis();
				// 获取历史租费
				List<LogCycleHistory> lmLogCycleHistory = null;
				if (CycleAlarm.trim().equals("false")) {
					lmLogCycleHistory = ops.getLogCycleHistory(
							infoUser.getUser_id(),
							codeAcctMonth.getAcct_month(), event_type);
					LOGGER.debug("--history cycle fee info :"
							+ lmLogCycleHistory.toString());
				}
				LOGGER.debug("-----log time :"
						+ (System.currentTimeMillis() - mt3));

				if (lmLogCycleHistory == null || lmLogCycleHistory.isEmpty()) {
					LOGGER.debug("--log is null ----");
					assembleCycle(map, iLogCycleHistory, dLogCycleHistory,
							lQacctProcess, lcdr, codeAcctMonth, infoUser);
				} else {
					assembleCycle(map, lmLogCycleHistory, iLogCycleHistory,
							dLogCycleHistory, lQacctProcess, lcdr,
							codeAcctMonth, infoUser);
				}
				// assembleCycle(map, iLogCycleHistory, dLogCycleHistory,
				// lQacctProcess, lcdr, codeAcctMonth, infoUser);
				long mt4 = System.currentTimeMillis();
//
//				if (CycleAlarm.trim().equals("true")) {
//					// 查询用户余额
//					List<InfoPayBalance> lInfoPayBalance = ops
//							.getInfoPayBalance(payUserRel.getPay_id());
//					if (lInfoPayBalance == null) {
//						LOGGER.error("---user_id [" + infoUser.getUser_id()
//								+ "],找不到账本--------");
//						continue;
//					}
//					long user_balance = 0L;
//					long cycle_fee = 0L;
//					long credit_fee = 0L;
//					for (InfoPayBalance infoPayBalance : lInfoPayBalance) {
//						user_balance += infoPayBalance.getReal_balance();
//					}
//
//					InfoUserCredit infoUserCredit = ops
//							.getCreditNumberByUserId(infoUser.getUser_id());
//					if (infoUserCredit != null) {
//						credit_fee = infoUserCredit.getCredit_number();
//					}
//
//					for (LogCycleHistory lch : iLogCycleHistory) {
//						cycle_fee += lch.getFee();
//					}
//					lQacctProcess.clear();
//					lcdr.clear();
//					iLogCycleHistory.clear();
//					dLogCycleHistory.clear();
//					LOGGER.debug("-----user_id[" + infoUser.getUser_id()
//							+ "],余额:" + user_balance + ",信用值:" + credit_fee
//							+ ",租费:" + cycle_fee + "--------");
//					if ((user_balance + credit_fee) <= cycle_fee) {
//						if (smsTempl.equals("")) {
//							LOGGER.error("-----短信模板配置不全-----");
//						} else {
//							// 组装短信信息
//							HlpSmsSend hlpSmsSend = new HlpSmsSend();
//							hlpSmsSend
//									.setMsg_id(ops.getSeq("seq_hlp_sms_send"));
//							hlpSmsSend.setMsisdn_send("10023");
//							hlpSmsSend.setPriority(123);
//							hlpSmsSend.setMessage_text("|" + smsTempl + "|"
//									+ String.format("%.2f", cycle_fee / 100.0)
//									+ "|"
//									+ String.format("%.2f", cycle_fee / 100.0));
//							hlpSmsSend.setMsisdn_receive(infoUser
//									.getDevice_number());
//							hlpSmsSend.setCreate_time(sdf.format(new Date()));
//							hlpSmsSend.setRetry_times(0);
//							lHlpSmsSend.add(hlpSmsSend);
//						}
//					}
//				}
				LOGGER.debug("---assemble result info time:" + (mt4 - mt3));

				if (CycleAlarm.trim().equals("false")
						&& !lQacctProcess.isEmpty()
						&& lQacctProcess.size() % num == 0) {
					long mt5 = System.currentTimeMillis();
					ops.insertFileAndLog(lQacctProcess, lcdr, iLogCycleHistory,
							dLogCycleHistory);
					lQacctProcess.clear();
					lcdr.clear();
					iLogCycleHistory.clear();
					dLogCycleHistory.clear();
					long mt6 = System.currentTimeMillis();
					LOGGER.debug("--insert time :" + (mt6 - mt5));
				} else if (CycleAlarm.trim().equals("true")
						&& lHlpSmsSend.size() % num == 0) {
					// 插入hlp_sms_send
					ops.insertHlpSmsSend(lHlpSmsSend);
					lHlpSmsSend.clear();
				}

				long t2 = System.currentTimeMillis();
				LOGGER.debug("---end time :" + (t2 - t1));
			}

			if (CycleAlarm.trim().equals("false")) {
				ops.insertFileAndLog(lQacctProcess, lcdr, iLogCycleHistory,
						dLogCycleHistory);
				lQacctProcess.clear();
				lcdr.clear();
				iLogCycleHistory.clear();
				dLogCycleHistory.clear();
			} else if (CycleAlarm.trim().equals("true")) {
				ops.insertHlpSmsSend(lHlpSmsSend);
				lHlpSmsSend.clear();
			}
			LOGGER.debug("---curr mod(user_id," + process_num + ")="
					+ channel_no + "------deal success-------");
			return;
		} catch (BasicException e) {
			LOGGER.error("-----ERROR_INFO:" + e.toString());
			return;
		} catch (Exception e) {
			LOGGER.error("-----ERROR_INFO:" + e.toString(),e);
			return;
		}

	}

	private void assembleCycle(Map<ProductInfo, List<RuleTariffConfInfo>> map,
			List<LogCycleHistory> iLogCycleHistory,
			List<LogCycleHistory> dLogCycleHistory,
			List<QAcctProcess> lQacctProcess, List<CDRCycle> lcdr,
			CodeAcctMonth codeAcctMonth, InfoUser infoUser)
			throws NumberFormatException, BasicException, UnknownHostException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		InetAddress localhost = InetAddress.getLocalHost();
		for (ProductInfo pdi : map.keySet()) {
			List<RuleTariffConfInfo> lrtci = map.get(pdi);
			String q_value = "";
			// Iterator<RuleTariffConfInfo> iter1 = lrtci.iterator();
			
			// while (iter1.hasNext()) {
			// RuleTariffConfInfo rtci = iter1.next();
			for (RuleTariffConfInfo rtci : lrtci) {
				// Iterator<LogCycleHistory> iter2 =
				// lmLogCycleHistory.iterator();
				q_value += rtci.getAcct_item_code() + ":"
						+ rtci.getTariff_value() + ";";
				LogCycleHistory mlcy = new LogCycleHistory();
				mlcy.setAcct_month(codeAcctMonth.getAcct_month());
				mlcy.setUser_id(infoUser.getUser_id());
				mlcy.setProduct_id(pdi.getProduct_id());
				mlcy.setUser_product_id(pdi.getUser_product_id());
				mlcy.setTariff_id(rtci.getTariff_id());
				mlcy.setAcct_item_code(rtci.getAcct_item_code());
				mlcy.setEvent_type_id(rtci.getEvent_type_id() + "");
				mlcy.setFee(rtci.getTariff_value());
				mlcy.setUser_state(infoUser.getUser_status());
				mlcy.setOfr_c_id(pdi.getProduct_id());
				mlcy.setPresent_last_time(df.format(date));
				mlcy.setTariff_id(rtci.getTariff_id());
				iLogCycleHistory.add(mlcy);

			}
			if (!q_value.equals("")) {
				final String q_seq = "UA_CDR_SERIAL_NO";
				q_value = q_value.substring(0, q_value.length() - 1);
				QAcctProcess qap = new QAcctProcess();
				qap.setAcct_month(codeAcctMonth.getAcct_month());
				qap.setSession_id(ops.getSeq(q_seq) + "");
				qap.setChannel_no(Long.valueOf(infoUser.getUser_id())
						% ops.getchannel());
				qap.setCalled_party(infoUser.getDevice_number());
				qap.setCalling_party(infoUser.getDevice_number());
				qap.setUser_id(infoUser.getUser_id());
				qap.setTariff_info(q_value);
				qap.setProcess_tag(0);
				qap.setService_scenarious(500);
				lQacctProcess.add(qap);

				final String cdr_seq = "CYCLE_CDR_SERIAL_NO";
				CDRCycle cdr = new CDRCycle();
				cdr.setSerialno(ops.getSeq(cdr_seq));
				cdr.setVersion(1);
				cdr.setTickettype(0);
				cdr.setTimestamp(df.format(date));
				cdr.setHostid(localhost.getHostName());
				cdr.setServicescenarious(500);
				cdr.setChargedparty(infoUser.getDevice_number());
				cdr.setServid(infoUser.getUser_id());
				cdr.setTariffinfo(q_value);
				cdr.setMasterproductid(pdi.getProduct_id());
				cdr.setEventcause(1001 + "");
				cdr.setCycletype(2);
				cdr.setUserstate(infoUser.getUser_status());
				cdr.setPartition_no(codeAcctMonth.getPartition_no());
				lcdr.add(cdr);
			}
		}
	}

	private void assembleCycle(Map<ProductInfo, List<RuleTariffConfInfo>> map,
			List<LogCycleHistory> lmLogCycleHistory,
			List<LogCycleHistory> iLogCycleHistory,
			List<LogCycleHistory> dLogCycleHistory,
			List<QAcctProcess> lQacctProcess, List<CDRCycle> lcdr,
			CodeAcctMonth codeAcctMonth, InfoUser infoUser)
			throws NumberFormatException, BasicException, UnknownHostException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		InetAddress localhost = InetAddress.getLocalHost();
		for (ProductInfo pdi : map.keySet()) {
			List<RuleTariffConfInfo> lrtci = map.get(pdi);
			String q_value = "";
			Iterator<RuleTariffConfInfo> iter1 = lrtci.iterator();
			while (iter1.hasNext()) {
				RuleTariffConfInfo rtci = iter1.next();
				Iterator<LogCycleHistory> iter2 = lmLogCycleHistory.iterator();
				int flag2deal = 0;
				while (iter2.hasNext()) {
					LogCycleHistory lch = iter2.next();
					if (pdi.getProduct_id().equals(lch.getProduct_id())
							&& pdi.getUser_product_id().equals(
									lch.getUser_product_id())
							&& rtci.getTariff_id() == lch.getTariff_id()
							&& lch.getAcct_month() == codeAcctMonth
									.getAcct_month()) {
						flag2deal = 1;// 处理过
						if (rtci.getTariff_value() == lch.getFee()) {// fee相同，remove掉map.get(pdi)和lmLogCycleHistory中的记录
							iter1.remove();
						} else {// fee不同，抹掉历史租费，新增记录
							q_value += rtci.getAcct_item_code() + ":"
									+ rtci.getTariff_value() + ";"
									+ lch.getAcct_item_code() + ":-"
									+ lch.getFee() + ";";
							// rtci.setTariff_value(rtci.getTariff_value()
							// - lch.getFee());
							LogCycleHistory dlcy = new LogCycleHistory();
							dlcy.setAcct_month(codeAcctMonth.getAcct_month());
							dlcy.setUser_id(infoUser.getUser_id());
							dlcy.setProduct_id(pdi.getProduct_id());
							dlcy.setUser_product_id(pdi.getUser_product_id());
							dlcy.setTariff_id(lch.getTariff_id());
							dlcy.setAcct_item_code(rtci.getAcct_item_code());
							dlcy.setEvent_type_id(rtci.getEvent_type_id() + "");
							dlcy.setFee(rtci.getTariff_value());
							dlcy.setUser_state(infoUser.getUser_status());
							dlcy.setOfr_c_id(pdi.getProduct_id());
							dlcy.setPresent_last_time(df.format(date));
							dlcy.setTariff_id(rtci.getTariff_id());
							iLogCycleHistory.add(dlcy);
							dLogCycleHistory.add(lch);
						}
						iter2.remove();
					}
				}// 新增租费
				if (0 == flag2deal) {
					q_value += rtci.getAcct_item_code() + ":"
							+ rtci.getTariff_value() + ";";
					LogCycleHistory mlcy = new LogCycleHistory();
					mlcy.setAcct_month(codeAcctMonth.getAcct_month());
					mlcy.setUser_id(infoUser.getUser_id());
					mlcy.setProduct_id(pdi.getProduct_id());
					mlcy.setUser_product_id(pdi.getUser_product_id());
					mlcy.setTariff_id(rtci.getTariff_id());
					mlcy.setAcct_item_code(rtci.getAcct_item_code());
					mlcy.setEvent_type_id(rtci.getEvent_type_id() + "");
					mlcy.setFee(rtci.getTariff_value());
					mlcy.setUser_state(infoUser.getUser_status());
					mlcy.setOfr_c_id(pdi.getProduct_id());
					mlcy.setPresent_last_time(df.format(date));
					mlcy.setTariff_id(rtci.getTariff_id());
					iLogCycleHistory.add(mlcy);
				}
			}
			if (!q_value.equals("")) {
				q_value = q_value.substring(0, q_value.length() - 1);
				QAcctProcess qap = new QAcctProcess();
				qap.setAcct_month(codeAcctMonth.getAcct_month());
				qap.setSession_id(ops.getSeq("UA_CDR_SERIAL_NO") + "");
				qap.setChannel_no(Long.valueOf(infoUser.getUser_id())
						% ops.getchannel());
				qap.setCalled_party(infoUser.getDevice_number());
				qap.setCalling_party(infoUser.getDevice_number());
				qap.setUser_id(infoUser.getUser_id());
				qap.setTariff_info(q_value);
				qap.setProcess_tag(0);
				qap.setService_scenarious(500);
				lQacctProcess.add(qap);

				CDRCycle cdr = new CDRCycle();
				cdr.setSerialno(ops.getSeq("CYCLE_CDR_SERIAL_NO"));
				cdr.setVersion(1);
				cdr.setTickettype(0);
				cdr.setTimestamp(df.format(date));
				cdr.setHostid(localhost.getHostName());
				cdr.setServicescenarious(500);
				cdr.setChargedparty(infoUser.getDevice_number());
				cdr.setServid(infoUser.getUser_id());
				cdr.setTariffinfo(q_value);
				cdr.setMasterproductid(pdi.getProduct_id());
				cdr.setEventcause(1001 + "");
				cdr.setCycletype(2);
				cdr.setUserstate(infoUser.getUser_status());
				cdr.setPartition_no(codeAcctMonth.getPartition_no());
				lcdr.add(cdr);
			}
		}

		if (lmLogCycleHistory != null) {
			for (LogCycleHistory logCycleHistory : lmLogCycleHistory) {
				String tariff_info = logCycleHistory.getAcct_item_code() + ":-"
						+ logCycleHistory.getFee();
				dLogCycleHistory.add(logCycleHistory);
				QAcctProcess qap = new QAcctProcess();
				qap.setAcct_month(codeAcctMonth.getAcct_month());
				qap.setSession_id(ops.getSeq("UA_CDR_SERIAL_NO") + "");
				qap.setChannel_no(Long.valueOf(infoUser.getUser_id())
						% ops.getchannel());
				qap.setCalled_party(infoUser.getDevice_number());
				qap.setCalling_party(infoUser.getDevice_number());
				qap.setUser_id(infoUser.getUser_id());
				qap.setTariff_info(tariff_info);
				qap.setProcess_tag(0);
				qap.setService_scenarious(500);
				lQacctProcess.add(qap);

				CDRCycle cdr = new CDRCycle();
				cdr.setSerialno(ops.getSeq("CYCLE_CDR_SERIAL_NO"));
				cdr.setVersion(1);
				cdr.setTickettype(0);
				cdr.setTimestamp(df.format(date));
				cdr.setHostid(localhost.getHostName());
				cdr.setServicescenarious(500);
				cdr.setChargedparty(infoUser.getDevice_number());
				cdr.setServid(infoUser.getUser_id());
				cdr.setTariffinfo(tariff_info);
				cdr.setMasterproductid(logCycleHistory.getProduct_id());
				cdr.setEventcause(1001 + "");
				cdr.setCycletype(2);
				cdr.setUserstate(infoUser.getUser_status());
				cdr.setPartition_no(codeAcctMonth.getPartition_no());
				lcdr.add(cdr);
			}
		}

	}

	public int getChannel_no() {
		return channel_no;
	}

	public int getMod() {
		return mod;
	}

	public int getMod_i() {
		return mod_i;
	}

	public int getNum() {
		return num;
	}

	public void setChannel_no(int channel_no) {
		this.channel_no = channel_no;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	public void setMod_i(int mod_i) {
		this.mod_i = mod_i;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getProcess_num() {
		return process_num;
	}

	public void setProcess_num(int process_num) {
		this.process_num = process_num;
	}

	public ApplicationContext getMycontext() {
		return mycontext;
	}

	public void setMycontext(ApplicationContext mycontext) {
		this.mycontext = mycontext;
	}

	public CycleRentBatchOps getOps() {
		return ops;
	}

//	public CreateAccountBook getCabOps() {
//		return cabOps;
//	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public void setOps(CycleRentBatchOps ops) {
		this.ops = ops;
	}

//	public void setCabOps(CreateAccountBook cabOps) {
//		this.cabOps = cabOps;
//	}

	public String getCycleAlarm() {
		return CycleAlarm;
	}

	public void setCycleAlarm(String cycleAlarm) {
		CycleAlarm = cycleAlarm;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		mycontext = applicationContext;
	}

}
