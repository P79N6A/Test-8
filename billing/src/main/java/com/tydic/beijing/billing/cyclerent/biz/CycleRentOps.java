package com.tydic.beijing.billing.cyclerent.biz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.datastore.DSAcctMonth;
import com.tydic.beijing.billing.cyclerent.datastore.DSCodeList;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleGroupStateRelation;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleItemCodeRelation;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleOfrSplit;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleOfrTariffRelation;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleParameters;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleProductResource;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleSystemSwitch;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleTariffConfInfo;
import com.tydic.beijing.billing.cyclerent.type.BilActRealTimeBill;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.credit.dao.CreditSequenceUtils;
import com.tydic.beijing.billing.dao.CDRCycle;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.InfoAuthMobile;
import com.tydic.beijing.billing.dao.InfoProtocolDetail;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeProductResourceRel;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.LifeUserStatus;
import com.tydic.beijing.billing.dao.LogCycleHistory;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.QAcctProcess;
import com.tydic.beijing.billing.dao.RuleGroupStateRelation;
import com.tydic.beijing.billing.dao.RuleItemCodeRelation;
import com.tydic.beijing.billing.dao.RuleOfrSplit;
import com.tydic.beijing.billing.dao.RuleOfrTariffRelation;
import com.tydic.beijing.billing.dao.RuleProductResource;
import com.tydic.beijing.billing.dao.RuleSystemSwitch;
import com.tydic.beijing.billing.dao.RuleTariffConfInfo;
import com.tydic.beijing.billing.dao.infolxproduct;
import com.tydic.beijing.billing.dto.BalanceChangeRequest;
import com.tydic.beijing.billing.dto.BalanceChangeResponse;
import com.tydic.beijing.billing.dto.ResourcePara;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class CycleRentOps {
	private static Logger LOGGER = Logger.getLogger(CycleRentOps.class);

	private static final int CREATE_DATE = 1;
	private static final int ACTIVE_DATE = 2;
	private static final int PRODUCT_EFF_DATE = 3;
	private static final int ONE_TEM_FIVE = 15;
	private static final int FULL_MONTH_EFF = 1;// 整月生效
	private static final int EFF_PASS = 2;// 只要生效过
	private static final int GENERAL_CYCLE_FEE = 1;// 普通租费
	private static final int ASSISTANT_COUNT_TWO = 2;// 按成员数量计算，N*tariff_value
	private static final int ASSISTAN_COUNT_THREE = 3;// 按成员数量N，当N>=LOWER_VALUE
														// &&
														// N<=UPPER_VALUE时按TARIFF_VALUE收取租费，不满足条件不收取租费';
	private DSAcctMonth dsAcctMonth = new DSAcctMonth();
	private DSRuleParameters dsRuleParameters = new DSRuleParameters();
	private DSRuleOfrSplit dsRuleOfrSplit = new DSRuleOfrSplit();
	private DSRuleTariffConfInfo dsRuleTariffConfInfo = new DSRuleTariffConfInfo();
	private DSRuleOfrTariffRelation dsRuleOfrTariffRelation = new DSRuleOfrTariffRelation();
	private DSRuleGroupStateRelation dsRuleGroupStateRelation = new DSRuleGroupStateRelation();
	private DSRuleItemCodeRelation dsRuleItemCodeRelation = new DSRuleItemCodeRelation();
	private DSRuleProductResource dsRuleProductResource = new DSRuleProductResource();
	private DSRuleSystemSwitch dsRuleSystemSwitch = new DSRuleSystemSwitch();
	private DSCodeList dsCodeList = new DSCodeList();
	private CodeAcctMonth codeAcctMonth = null;
	private BalanceChangeOps bcOps;// 余额变更公共类

	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	public void load() throws Exception {
		LOGGER.debug("-----Load rule table begin -----");
		dsAcctMonth.load();
		dsRuleParameters.load();
		dsRuleOfrSplit.load();
		dsRuleOfrTariffRelation.load();
		dsRuleTariffConfInfo.load();
		dsRuleGroupStateRelation.load();
		dsRuleItemCodeRelation.load();
		dsRuleProductResource.load();
		dsCodeList.load();
		codeAcctMonth = getCodeAcctMonth();
//		codeAcctMonth = getCodeAcctMonthByRealTime(BasicType.USE_TAG,BasicType.ACT_TAG);
		LOGGER.debug("-----Load rule table end -----");
	}

	public InfoUser getInfoUser(String user_id) {
		return S.get(InfoUser.class).queryFirst(
				Condition.build("queryByUserId").filter("user_id", user_id));
	}
	
    public List<infolxproduct> getLxProductid(String user_id) {
		return S.get(infolxproduct.class).query(
				Condition.build("queryByLoguserId").filter("user_id", user_id));
	}
    public List<RuleTariffConfInfo> getCycFlag(String user_id) {
		return S.get(RuleTariffConfInfo.class).query(
				Condition.build("queryByforuserId").filter("user_id", user_id));
	}
	public List<LifeUserProduct> getLifeUserProduct(String user_id, String act_exp_date) {
		Map<String,Object> fiter = new HashMap<String,Object>();
		fiter.put("user_id", user_id);
		fiter.put("act_exp_date", act_exp_date);
		return S.get(LifeUserProduct.class).query(
				Condition.build("queryByUserId").filter(fiter));
	}

	public LifeUserStatus getlifeUserStatus(String user_id) {
		return S.get(LifeUserStatus.class).queryFirst(
				Condition.build("queryByUserId").filter("user_id", user_id));
	}

	public List<RuleOfrSplit> getRuleOfrSplit(String product_id) {
		// 排除掉ofr_c_id为空的
		List<RuleOfrSplit> lros = dsRuleOfrSplit.getByProductId(product_id);
		List<RuleOfrSplit> mlros = new ArrayList<RuleOfrSplit>();
		for (RuleOfrSplit ros : lros) {
			if (ros.getOfr_c_id() != null && !ros.getOfr_c_id().isEmpty()) {
				mlros.add(ros);
			}
		}
		return mlros;
	}

	public int getAgreementType(List<String> proto_type, InfoUser infoUser,
			LifeUserStatus lifeUserStatus) throws BasicException {
		int agreement_type = BasicType.NOT_ARGEEMENT_TYPE;
		if (proto_type != null && !proto_type.isEmpty()
				&& lifeUserStatus.getUser_status().trim().equals("202")) {
			LOGGER.debug("-----");
			for (String is : proto_type) {
				String macro_code = dsCodeList.get(is);
				LOGGER.debug("-----macro:" + macro_code);
				if (macro_code != null && !macro_code.isEmpty()) {
					agreement_type = dsRuleParameters
							.getArgeementType(macro_code);
					if (agreement_type == 1) {
						if (lifeUserStatus.getEff_date().equals(
								codeAcctMonth.getAct_eff_date())) {
							LOGGER.debug("----合约用户，当月1号进入双停，改变状态为101收取租费----");
							lifeUserStatus.setUser_status("101");
						}
						break;
					}
				}
			}
		}
		return agreement_type;
	}
	public void getTariffId(String ofr_c_id, InfoUser infoUser,
			LifeUserStatus lifeUserStatus, String event_type, int card_count,
			List<RuleTariffConfInfo> lrtci, Date product_eff, int agreement_type)
			throws BasicException, ParseException {

		// 通过user_status获取group_id
		List<RuleGroupStateRelation> lgsr = dsRuleGroupStateRelation
				.getByUserStatus(lifeUserStatus.getUser_status());

		if (lgsr != null) {
			// 获取资费信息
			for (RuleGroupStateRelation rgsr : lgsr) {
				LOGGER.debug("--state_group : " + rgsr.getState_group()
						+ ",ofr_c_id = " + ofr_c_id);
				List<RuleOfrTariffRelation> lrotr = dsRuleOfrTariffRelation
						.getByOfrAndGroup(ofr_c_id, rgsr.getState_group());
				if (lrotr != null) {
					for (RuleOfrTariffRelation rotr : lrotr) {
						// '1:整月生效才收取:status.eff_date <= acct_month.eff_date &&
						// status.exp_date >= acct_month.exp_date
						// 2:生效过就收取:status.eff_date <= acct_month.exp_date ||
						// status.exp_date >= acct_month.eff_date

						if (!effFalg(rotr.getState_ref_mode(), lifeUserStatus,
								codeAcctMonth)) {
							LOGGER.debug("----state_ref_mod ---不用收取租费-------");
							continue;
						}

						boolean flag2count = offsetCycle(
								rotr.getOffset_ref_type(),
								rotr.getOffset_cycle(), infoUser, product_eff);
						LOGGER.debug("--getOffset_ref_type result:"
								+ flag2count);
						if (!flag2count) {
							LOGGER.debug("-----rule_ofr_tariff_relation.offser_ref_type---不用收取租费-----");
							continue;
						}
						if (getStartDate(rotr, infoUser, product_eff)
								&& rotr.getOffset_mode() == 4) {
							LOGGER.debug("-------首月不收租费，不到账资源--------------------");
							continue;
						}

						if (!lifeUserStatus.getUser_status().trim()
								.equals("202")) {
							agreement_type = 0;// 合约正常状态
						}

						if (rotr.getAgreement_type() != agreement_type) {
							LOGGER.debug("-----rule_ofr_tariff_relation.agreement_type = "
									+ rotr.getAgreement_type()
									+ ",user_id agreement_type = "
									+ agreement_type);
							continue;
						}

						RuleTariffConfInfo rtci = dsRuleTariffConfInfo
								.getByTariffId(rotr.getTariff_id());
						if (rtci == null) {
							LOGGER.debug("--table rule_tarif_conf_info.tariff_id="
									+ rotr.getTariff_id() + " is null-----");
							continue;
						}

						long fee = 0L;
						if (rtci.getEvent_type_id() == Long.valueOf(event_type)) {
							if (event_type.equals(BasicType.EVENT_TYPE_ID_MIN)) {
								LOGGER.debug("#########开始计算用户本月冻款 ########################");
								// 计算保底
								List<BilActRealTimeBill> lbart = null;
								List<RuleItemCodeRelation> lRuleItemCodeRelation = dsRuleItemCodeRelation
										.getRuleItemCodeRelation();
								lbart = getAllBilActRealTimeBill(infoUser
										.getUser_id());
								if (lbart != null) {
									for (BilActRealTimeBill bart : lbart) {
										if (!effItemCode(
												bart.getAcct_item_code(),
												lRuleItemCodeRelation,
												rtci.getItem_group())) {
											continue;
										}
										fee += bart.getFee();
									}
								}
							}
							LOGGER.debug("---冻结费用:" + fee + ",保底费："
									+ rtci.getTariff_value());
							RuleTariffConfInfo mrtci = new RuleTariffConfInfo();
							mrtci = (RuleTariffConfInfo) rtci.clone();
							if (fee >= mrtci.getTariff_value()) {
								continue;
							} else {
								mrtci.setTariff_value(mrtci.getTariff_value()
										- fee);
							}
							switch (mrtci.getTariff_mode()) {
							case GENERAL_CYCLE_FEE:// 普通租费
								break;
							case ASSISTANT_COUNT_TWO:// 按成员数量计算(副卡，亲情成员，集团成员)
								if (mrtci.getRef_member_type().equals(
										BasicType.CONSULT_MEMBER_VICECARD)) {
									mrtci.setTariff_value(mrtci
											.getTariff_value() * card_count);
								} else {
									LOGGER.debug("------亲情人员和集团成员数据-没有实现---------");
									throw new BasicException(
											ErrorCode.ERR_CODE_PARA_SHUJU,
											ErrorCode.ERR_MSG_PARA_TARIFF_CONF);
								}
								break;
							case ASSISTAN_COUNT_THREE:
								break;
							}
							long tariff_value = (long) Math.ceil((mrtci
									.getTariff_value() * getScale(
									rotr.getOffset_mode(), product_eff)));
							if (tariff_value == 0) {
								continue;
							}
							mrtci.setTariff_value(tariff_value);
							lrtci.add(mrtci);
						}
					}
				}
			}
		}
		LOGGER.debug("--ofr_c_id:" + ofr_c_id + ",lrtci:" + lrtci.toString());

	}

	private boolean effItemCode(int acct_item_code,
			List<RuleItemCodeRelation> lRuleItemCodeRelation, long item_group) {
		for (RuleItemCodeRelation ricr : lRuleItemCodeRelation) {
			if (item_group == ricr.getItem_group()
					&& acct_item_code == ricr.getAcct_item_code()) {
				return true;
			}
		}
		return false;
	}

	private List<BilActRealTimeBill> getAllBilActRealTimeBill(String user_id) {
		BilActRealTimeBill bart = new BilActRealTimeBill();
		bart.setUser_id(user_id);
		bart.setAcct_month(codeAcctMonth.getAcct_month());
		LOGGER.debug("--BilActRealTimeBill---user_id:" + user_id
				+ ",acct_month:" + codeAcctMonth.getAcct_month());
		return S.get(BilActRealTimeBill.class).query(
				Condition
						.build("queryByUserId")
						.filter("partition_num",
								codeAcctMonth.getPartition_no())
						.filter("user_id", user_id)
						.filter("acct_month", codeAcctMonth.getAcct_month()));
	}

	public boolean effFalg(int mode, LifeUserStatus lifeUserStatus,
			CodeAcctMonth codeAcctMonth) throws BasicException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		LOGGER.debug("-----------------用户生失效时间与账期时间比较判断是否有效 begin--------");
		LOGGER.debug("---mode:" + mode + "," + ",satus.eff_date="
				+ df.format(lifeUserStatus.getEff_date()) + ",status.exp_date="
				+ df.format(lifeUserStatus.getExp_date())
				+ ",codeAcctMonth.eff_date="
				+ df.format(codeAcctMonth.getAct_eff_date())
				+ ",codeAcctMonth.exp_date="
				+ df.format(codeAcctMonth.getAct_exp_date()));
		switch (mode) {
		case FULL_MONTH_EFF:
			if ((lifeUserStatus.getEff_date().after(
					codeAcctMonth.getAct_eff_date()) || (lifeUserStatus
					.getEff_date().equals(codeAcctMonth.getAct_eff_date())))
					|| (lifeUserStatus.getExp_date().before(
							codeAcctMonth.getAct_exp_date()) || (lifeUserStatus
							.getExp_date().equals(codeAcctMonth
							.getAct_exp_date())))) {
				return false;
			}
			break;
		case EFF_PASS:
			if (!(lifeUserStatus.getEff_date().before(
					codeAcctMonth.getAct_exp_date()) || lifeUserStatus
					.getExp_date().after(codeAcctMonth.getAct_eff_date()))) {
				return false;
			}
			break;
		default:
			LOGGER.debug("------TBALE RULE_OFR_TARIFF_RELATION.OFFSET_REF_MODE value is 1 or 2");
			throw new BasicException(ErrorCode.ERR_CODE_SHUJU_CONFIG,
					"TBALE RULE_OFR_TARIFF_RELATION.STATE_REF_MODE value is 1 or 2");
		}

		return true;
	}

	public boolean offsetCycle(int offset_ref_type, int offset_cycle,
			InfoUser infoUser, Date product_eff_date) throws BasicException,
			ParseException {
		LOGGER.debug("offset_ref_type:" + offset_ref_type + ",offset_cycle="
				+ offset_cycle + ",product_eff_date=" + product_eff_date);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		LOGGER.debug("---infoUser:" + infoUser.toString());
		Date date = null;
		switch (offset_ref_type) {
		case CREATE_DATE:
			date = infoUser.getCreate_date();
			break;
		case ACTIVE_DATE:
			if (infoUser.getActive_date() == null
					|| infoUser.getActive_date().equals("")) {
				LOGGER.debug("---用户激活时间为空,默认取系统当前时间-----");
				infoUser.setActive_date(new java.sql.Date(new java.util.Date().getTime()));
			}
			date = infoUser.getActive_date();
			break;
		case PRODUCT_EFF_DATE:
			date = product_eff_date;
			break;
		default:
			LOGGER.debug("----------TBALE RULE_OFR_TARIFF_RELATION.OFFSET_REF_TYPE VALUE [1,2,3]-------------");
			throw new BasicException(ErrorCode.ERR_CODE_SHUJU_CONFIG,
					"TBALE RULE_OFR_TARIFF_RELATION.OFFSET_REF_TYPE VALUE [1,2,3]");
		}
		LOGGER.debug("--date:" + date);
		Calendar cal = Calendar.getInstance(); // 获取时间参数
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);// 1号
		cal.setTime(df2.parse(df2.format(cal.getTime())));
		cal.add(Calendar.MONTH, offset_cycle);
		cal.add(Calendar.SECOND, -1);
		date = cal.getTime();
		LOGGER.debug("--code_acct_month.act_eff_date:"
				+ df.format(codeAcctMonth.getAct_eff_date()) + ",end_time:"
				+ df.format(date));
		if (codeAcctMonth.getAct_eff_date().after(date)) {
			return true;
		}
		return false;
	}

	public boolean getStartDate(RuleOfrTariffRelation rotr, InfoUser infoUser,
			Date product_eff_date) throws BasicException {
		Date date = null;
		switch (rotr.getOffset_ref_type()) {
		case CREATE_DATE:
			date = infoUser.getCreate_date();
			break;
		case ACTIVE_DATE:
			if (infoUser.getActive_date() == null
					|| infoUser.getActive_date().equals("")) {
				LOGGER.debug("---用户激活时间为空-----");
				throw new BasicException(ErrorCode.ERR_CODE_SHUJU_CONFIG,
						"用户激活时间为空");
			}
			date = infoUser.getActive_date();
			break;
		case PRODUCT_EFF_DATE:
			date = product_eff_date;
			break;
		default:
			LOGGER.debug("----------TBALE RULE_OFR_TARIFF_RELATION.OFFSET_REF_TYPE VALUE [1,2,3]-------------");
			throw new BasicException(ErrorCode.ERR_CODE_SHUJU_CONFIG,
					"TBALE RULE_OFR_TARIFF_RELATION.OFFSET_REF_TYPE VALUE [1,2,3]");
		}
		if (date.before(codeAcctMonth.getAct_exp_date())
				&& date.after(codeAcctMonth.getAct_eff_date())) {
			return true;
		}
		return false;
	}

	public double getScale(int offset_mode, Date product_eff) {
		double scale = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(product_eff);
		int month = cal.get(Calendar.DAY_OF_MONTH);
		switch (offset_mode) {
		case 1:
			scale = 1;
			break;
		case 2:
			if (month <= ONE_TEM_FIVE) {
				scale = 1;
			} else {
				scale = 0.5;
			}
			break;
		case 3:
			cal.set(Calendar.DATE, 1);
			cal.roll(Calendar.DATE, -1);
			int maxDate = cal.get(Calendar.DATE);
			scale = (double) (maxDate - month + 1) / (double) maxDate;
			LOGGER.debug("-----day[" + month + "],maxDate[" + maxDate
					+ "]-----------");
			break;
		case 4:
			scale = 0;
			break;
		default:
			LOGGER.debug("TABLE RuleOfrTariffRelation.OFFSET_MODE VALEUE IN [1,2,3,4],now offset_mode = "
					+ offset_mode);
			break;
		}
		LOGGER.debug("---rule_ofr_tariff_relation.OFFSET_MODE[" + offset_mode
				+ "] count result :" + scale);
		return scale;
	}

	public CodeAcctMonth getCodeAcctMonth() {
		List<CodeAcctMonth> lcam = dsAcctMonth.getByUseTags("1");
		if (lcam != null && !lcam.isEmpty()) {
			return lcam.get(0);
		}
		return null;
	}

	public List<InfoAuthMobile> getInfoAuthMobile(String device_number) {
		return S.get(InfoAuthMobile.class).query(
				Condition.build("queryByDeviceNbr").filter("device_number",
						device_number));
	}

	public List<LogCycleHistory> getLogCycleHistory(String user_id,
			int acct_month, String event_type) {
		return S.get(LogCycleHistory.class).query(
				Condition.build("query").filter("user_id", user_id)
						.filter("acct_month", acct_month)
						.filter("event_type", event_type));
	}

	public void insertFileAndLog(List<QAcctProcess> lQacctProcess,
			List<CDRCycle> lcdr, List<LogCycleHistory> iLogCycleHistory,
			List<LogCycleHistory> dLogCycleHistory,
			List<LifeProductResourceRel> lLifeProductResourceRel,
			BalanceChangeRequest request) throws BasicException, Exception {
		if (request.getBalanceChangeInfo() != null
				&& !request.getBalanceChangeInfo().isEmpty()) {
			BalanceChangeResponse response = bcOps.doProcess(request, false,
					false);
			LOGGER.debug("---balance change:" + request.toString());
			if (response == null
					|| response.getStatus() == null
					|| response.getBalanceChangeSnapshot() == null
					|| response.getBalanceChangeSnapshot().isEmpty()
					|| response.getStatus().equalsIgnoreCase(
							BalanceChangeOps.STATUS_FAIL)) {
				throw new BasicException(
						ErrorCode.ERR_CODE_BALANCE_CHANGE_SNAP_SHOT_NULL,
						ErrorCode.ERR_MSG_BALANCE_CHANGE_SNAP_SHOT_NULL);
			}
			// checkBalanceChangeSnapShot(response.getBalanceChangeSnapshot());
			LOGGER.info(response);
			// 余额变动请求正常处理
			LOGGER.debug("---余额变动请求正常处理 ----");
		}

		if (lQacctProcess != null && !lQacctProcess.isEmpty()) {
			for (QAcctProcess qap : lQacctProcess) {
				S.get(QAcctProcess.class).create(qap);
				//
				// S.get(QAcctProcess.class).batch(
				// Condition.build("insertQAcctProcess"),qap);
			}
		}
		if (dLogCycleHistory != null && !dLogCycleHistory.isEmpty()) {
			for (LogCycleHistory dlcy : dLogCycleHistory) {
				LOGGER.debug("---" + dlcy.toString());
				S.get(LogCycleHistory.class)
						.batch(Condition
								.build("deleteLogCycleHistory")
								.filter("user_id", dlcy.getUser_id())
								.filter("user_product_id",
										dlcy.getUser_product_id())
								.filter("acct_month", dlcy.getAcct_month())
								.filter("tariff_id", dlcy.getTariff_id()), dlcy);
			}
		}
		if (iLogCycleHistory != null && !iLogCycleHistory.isEmpty()) {
			for (LogCycleHistory ilcy : iLogCycleHistory) {
				S.get(LogCycleHistory.class).create(ilcy);
			}
		}

		for (LifeProductResourceRel lprr : lLifeProductResourceRel) {
			S.get(LifeProductResourceRel.class).create(lprr);
		}
		// 写文件
		if (lcdr != null && !lcdr.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			for (CDRCycle cdr : lcdr) {
				sb.append(cdr.toFileString());
			}
			writeCdr(sb);
		}
	}

	public void writeCdr(StringBuffer sb) throws BasicException {
		ResourcePara rp = dsRuleParameters.get(BasicType.CYCLE_RENT);
		if (rp == null) {
			throw new BasicException(ErrorCode.ERR_CODE_PARAMETER_EMPTY,
					ErrorCode.ERR_MSG_PARA_CDR_PATH_NULL);
		}
		String str = sb.toString().substring(0, (sb.toString().length() - 1));
		String path = rp.getPath();

		String x = path.charAt(path.length() - 1) + "";
		if (!x.trim().equals("/")) {
			path = path + "/";
		}
		LOGGER.debug("-----path:" + path + ",str=" + str);
		BufferedWriter bw = null;
		try {
			String fileName = path + "BILLING_CDR500_"
					+ Thread.currentThread().getName()
					+ Thread.currentThread().getId() + (new Date()).getTime()
					+ ".tmp";
			LOGGER.debug("###fileName = " + fileName);
			File f = new File(fileName);
			if (!f.exists()) {
				LOGGER.debug("-----创建文件开始-----");
				f.createNewFile();
				LOGGER.debug("----文件[" + fileName + "]已创建-----");
			}
			FileWriter fw = new FileWriter(f, true);
			bw = new BufferedWriter(fw);
			bw.write(str);
			bw.newLine();
			System.out.println("文件内容写入完毕");
			bw.close();

			File oldf = new File(fileName);

			String nfn = fileName.substring(0, fileName.length() - 4);

			nfn += ".r";

			File newf = new File(nfn);
			if (oldf.exists() && !newf.exists()) {
				oldf.renameTo(newf);
			}

		} catch (IOException e) {
			LOGGER.error("---写文件失败-----errorMsg:-" + e.toString());
			throw new BasicException(ErrorCode.ERR_CODE_WRITE_FILE,
					ErrorCode.ERR_MSG_WRITE_FILE);
		}
	}

	public List<RuleProductResource> selectRuleProductResource(String product_id) {
		return dsRuleProductResource.getByProductid(product_id);
	}

	public PayUserRel getPayUserRel(String user_id) {
		return S.get(PayUserRel.class).queryFirst(
				Condition.build("queryByUserIdForResTrade").filter("user_id",
						user_id));
	}

	public LifeProductResourceRel selectLifeProductResourceRel(
			LifeProductResourceRel ppr) {
		return S.get(LifeProductResourceRel.class).queryFirst(
				Condition.build("query").filter("user_id", ppr.getUser_id())
						.filter("user_product_id", ppr.getUser_product_id())
						.filter("acct_month", ppr.getAcct_month())
						.filter("ofr_c_id", ppr.getOfr_c_id()));
	}

	public BalanceChangeOps getBcOps() {
		return bcOps;
	}

	public void setBcOps(BalanceChangeOps bcOps) {
		this.bcOps = bcOps;
	}

	public boolean effFalgByGroup(String user_status, String state_group) {
		List<RuleGroupStateRelation> lrgsr = dsRuleGroupStateRelation
				.getByGroup(state_group);
		if (lrgsr != null) {
			for (RuleGroupStateRelation rgsr : lrgsr) {
				if (rgsr.getUser_state().equals(user_status)) {
					return true;
				}
			}
		}

		return false;
	}

	public long getchannel() throws BasicException {
		ResourcePara rp = dsRuleParameters.get(BasicType.CYCLE_RENT);
		if (rp == null) {
			throw new BasicException(ErrorCode.ERR_CODE_PARAMETER_EMPTY,
					ErrorCode.ERR_MSG_PARA_CDR_PATH_NULL);
		}
		return rp.getChannel_no();
	}

	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) throws BasicException {
		if (BasicType.DS_RULE_PARAMETERS.equals(datastoreName)) {
			dsRuleParameters.refresh();
		} else if (BasicType.DS_RULE_OFR_SPLIT.equals(datastoreName)) {
			dsRuleOfrSplit.refresh();
		} else if (BasicType.DS_RULE_OFR_TARIFF_RELATION.equals(datastoreName)) {
			dsRuleOfrTariffRelation.refresh();
		} else if (BasicType.DS_RULE_GROUP_STATE_RELATION.equals(datastoreName)) {
			dsRuleGroupStateRelation.refresh();
		} else if (BasicType.DS_RULE_TARIFF_CONF_INFO.equals(datastoreName)) {
			dsRuleTariffConfInfo.refresh();
		} else if (BasicType.DS_RULE_PRODUCT_RESOURCE.equals(datastoreName)) {
			dsRuleProductResource.refresh();
		}

	}

	public long getSeq(String sequenceName) {
		CreditSequenceUtils s = S.get(CreditSequenceUtils.class).queryFirst(
				Condition.empty().filter("sequence_name", sequenceName));

		return s.getDuckduckgo();
	}

	public void setBalanceValue(RuleProductResource mrpr, Date product_eff) {
		double scale = getScale(mrpr.getOffset_mode(), product_eff);
		mrpr.setResource_value((long) Math.ceil(scale
				* mrpr.getResource_value()));
	}
	public void setBalanceValue(RuleProductResource mrpr, String baseDate) throws ParseException {
		double scale = 1;
		if(!("-1").equals(baseDate)){
			scale = getScale(mrpr.getOffset_mode(),df.parse(baseDate));
		}
		mrpr.setResource_value((long) Math.ceil(scale*mrpr.getResource_value()));
	}
	public List<InfoProtocolDetail> getUserProtocol(String user_id) {
		return S.get(InfoProtocolDetail.class).query(
				Condition.build("queryByUserId").filter("user_id", user_id));
	}

	public int getProtocolType(String protocol_id) throws BasicException {
		int agreement_type = BasicType.NOT_ARGEEMENT_TYPE;
		if (protocol_id != null && !protocol_id.isEmpty()) {
			String macro_code = dsCodeList.get(protocol_id);
			agreement_type = dsRuleParameters.getArgeementType(macro_code);
		}
		return agreement_type;
	}

	public void dealDb(List<LifeProductResourceRel> lLifeProductResourceRel,
			BalanceChangeRequest request) throws BasicException, Exception {
		if (request.getBalanceChangeInfo() != null
				&& !request.getBalanceChangeInfo().isEmpty()) {
			BalanceChangeResponse response = bcOps.doProcess(request, false,
					false);
			LOGGER.debug("---balance change:" + request.toString());
			if (response == null
					|| response.getStatus() == null
					|| response.getBalanceChangeSnapshot() == null
					|| response.getBalanceChangeSnapshot().isEmpty()
					|| response.getStatus().equalsIgnoreCase(
							BalanceChangeOps.STATUS_FAIL)) {
				throw new BasicException(
						ErrorCode.ERR_CODE_BALANCE_CHANGE_SNAP_SHOT_NULL,
						ErrorCode.ERR_MSG_BALANCE_CHANGE_SNAP_SHOT_NULL);
			}
			// checkBalanceChangeSnapShot(response.getBalanceChangeSnapshot());
			LOGGER.info(response);
			// 余额变动请求正常处理
			LOGGER.debug("---余额变动请求正常处理 ----");
		}

		for (LifeProductResourceRel lprr : lLifeProductResourceRel) {
			S.get(LifeProductResourceRel.class).create(lprr);
		}

	}

	public boolean effSystemSwitch(String Switch_code) {
		List<RuleSystemSwitch> lrss = dsRuleSystemSwitch.get();
		if(lrss != null){
			for(RuleSystemSwitch rss : lrss){
				if(rss.getSwitch_code().equals(Switch_code) && rss.getSwitch_value().trim().equals("0")){
					return true;
				}
			}
		}
		
		return false;
	}

	public CodeAcctMonth getCodeAcctMonthByRealTime(String useTag, String actTag)
			throws BasicException {
		CodeAcctMonth cam = S.get(CodeAcctMonth.class).queryFirst(
				Condition.build("queryByUseAndActTag")
						.filter("use_tag", useTag).filter("act_tag", actTag));
		if (cam == null) {
			throw new BasicException(ErrorCode.ERR_DATABASE_DATA_ABNORMAL,
					"TABLE[CODE_ACCT_MONTH] Shouldn't Be Empty!");
		}
		return cam;
	}

}
