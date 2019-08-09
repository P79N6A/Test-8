package com.tydic.beijing.billing.cyclerent.biz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.cyclerent.datastore.DSAcctMonth;
import com.tydic.beijing.billing.cyclerent.datastore.DSCodeList;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleGroupStateRelation;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleParameters;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleProductResource;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleProductResourceExt;
import com.tydic.beijing.billing.cyclerent.datastore.DSRuleSystemSwitch;
import com.tydic.beijing.billing.cyclerent.type.InfoUser;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.InfoProtocolDetail;
import com.tydic.beijing.billing.dao.LifeProductResourceRel;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.LifeUserStatus;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.RuleGroupStateRelation;
import com.tydic.beijing.billing.dao.RuleProductResource;
import com.tydic.beijing.billing.dao.RuleProductResourceExt;
import com.tydic.beijing.billing.dao.RuleSystemSwitch;
import com.tydic.beijing.billing.dto.BalanceChangeRequest;
import com.tydic.beijing.billing.dto.BalanceChangeResponse;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class ResourceToAccountOps {
	private static final Logger LOGGER = Logger
			.getLogger(ResourceToAccountOps.class);
	private DSRuleProductResource dsRuleProductResource = new DSRuleProductResource();
	private DSAcctMonth dsAcctMonth = new DSAcctMonth();
	private DSRuleGroupStateRelation dsRuleGroupStateRelation = new DSRuleGroupStateRelation();
	private BalanceChangeOps bcOps;// 余额变更公共类
	private DSRuleSystemSwitch dsRuleSystemSwitch = new DSRuleSystemSwitch();
	private DSCodeList dsCodeList = new DSCodeList();
	private DSRuleParameters dsRuleParameters = new DSRuleParameters();
	private DSRuleProductResourceExt dsRuleProductResourceExt = new DSRuleProductResourceExt();
	//
	// SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	// SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
	private static final int FULL_MONTH_EFF = 1;// 整月生效
	private static final int EFF_PASS = 2;// 只要生效过
	private static final int ONE_TEM_FIVE = 15;
	private static final int CREATE_DATE = 1;
	private static final int ACTIVE_DATE = 2;
	private static final int PRODUCT_EFF_DATE = 3;

	// public List<InfoUser> getInfoUser(int process_num, int channel_no) {
	// return S.get(InfoUser.class).query(
	// Condition.build("getByUserId")
	// .filter("process_num", process_num)
	// .filter("channel_no", channel_no));
	// }

	public List<InfoUser> getInfoUser(int process_num, int channel_no, int mod,
			int mod_i) {
		return S.get(InfoUser.class).query(
				Condition.build("getByUserId")
						.filter("process_num", process_num)
						.filter("channel_no", channel_no).filter("mod", mod)
						.filter("mod_id", mod_i));
	}

	public PayUserRel getPayUserRelByUserId(String user_id) {
		return S.get(PayUserRel.class).queryFirst(
				Condition.build("queryByUserIdForResTrade").filter("user_id",
						user_id));
	}

	public List<LifeUserProduct> getLifeUserProductByUserId(String user_id,
			String act_exp_date) {
		return S.get(LifeUserProduct.class).query(
				Condition.build("queryByUserIdAndRA")
						.filter("user_id", user_id)
						.filter("act_exp_date", act_exp_date));
	}

	public List<RuleProductResource> selectRuleProductResource(String product_id) {
		return dsRuleProductResource.getByProductid(product_id);
	}

	public LifeUserStatus getLifeUserStatusByUserId(String user_id) {
		return S.get(LifeUserStatus.class).queryFirst(
				Condition.build("queryByUserId").filter("user_id", user_id));
	}

	public CodeAcctMonth getCodeAcctMonth() {
		List<CodeAcctMonth> lcam = dsAcctMonth.getByUseTags("1");
		if (lcam != null && !lcam.isEmpty()) {
			return lcam.get(0);
		}
		return null;
	}

	public boolean effFalg(int mode, InfoUser lifeUserStatus,
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

	public LifeProductResourceRel selectLifeProductResourceRel(
			LifeProductResourceRel ppr) {
		return S.get(LifeProductResourceRel.class).queryFirst(
				Condition.build("query").filter("user_id", ppr.getUser_id())
						.filter("user_product_id", ppr.getUser_product_id())
						.filter("acct_month", ppr.getAcct_month())
						.filter("ofr_c_id", ppr.getOfr_c_id()));
	}

	public void load() throws Exception {
		dsAcctMonth.load();
		dsRuleProductResource.load();
		dsRuleGroupStateRelation.load();
		dsCodeList.load();
		dsRuleParameters.load();
		dsRuleSystemSwitch.load();
		// dsRuleProductResourceExt.load();
	}

	public void dealDb(List<BalanceChangeRequest> lrequest,
			List<LifeProductResourceRel> lLifeProductResourceRel)
			throws BasicException, Exception {
		for (BalanceChangeRequest request : lrequest) {
			LOGGER.debug("---balance change:" + request.toString() + "-----");
			if (request.getBalanceChangeInfo() == null
					|| request.getBalanceChangeInfo().isEmpty()) {
				continue;
			}
			BalanceChangeResponse response = bcOps.doProcess(request, false,
					false);
			if (response == null
					|| response.getStatus() == null
					|| response.getBalanceChangeSnapshot() == null
					|| response.getBalanceChangeSnapshot().isEmpty()
					|| response.getStatus().equalsIgnoreCase(
							BalanceChangeOps.STATUS_FAIL)) {
				throw new BasicException(
						ErrorCode.ERR_CODE_BALANCE_CHANGE_SNAP_SHOT_NULL,
						ErrorCode.ERR_MSG_BALANCE_CHANGE_SNAP_SHOT_NULL);
				// LOGGER.error("----respones:"+response.toString());
			}
			// checkBalanceChangeSnapShot(response.getBalanceChangeSnapshot());
			LOGGER.info("---" + response);
			// 余额变动请求正常处理
		}
		LOGGER.debug("---余额变动请求正常处理 ----");
		LOGGER.debug("---lLifeProductResourceRel:"
				+ lLifeProductResourceRel.toString());
		if (lLifeProductResourceRel != null
				&& !lLifeProductResourceRel.isEmpty()) {
			for (LifeProductResourceRel lprr : lLifeProductResourceRel) {
				S.get(LifeProductResourceRel.class).create(lprr);
			}
		}
	}

	public void setBalanceValue(RuleProductResource mrpr, Date product_eff) {
		double scale = getScale(mrpr.getOffset_mode(), product_eff);
		mrpr.setResource_value((long) Math.ceil(scale
				* mrpr.getResource_value()));
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

	public DSRuleProductResource getDsRuleProductResource() {
		return dsRuleProductResource;
	}

	public DSAcctMonth getDsAcctMonth() {
		return dsAcctMonth;
	}

	public DSRuleGroupStateRelation getDsRuleGroupStateRelation() {
		return dsRuleGroupStateRelation;
	}

	public BalanceChangeOps getBcOps() {
		return bcOps;
	}

	public void setDsRuleProductResource(
			DSRuleProductResource dsRuleProductResource) {
		this.dsRuleProductResource = dsRuleProductResource;
	}

	public void setDsAcctMonth(DSAcctMonth dsAcctMonth) {
		this.dsAcctMonth = dsAcctMonth;
	}

	public void setDsRuleGroupStateRelation(
			DSRuleGroupStateRelation dsRuleGroupStateRelation) {
		this.dsRuleGroupStateRelation = dsRuleGroupStateRelation;
	}

	public void setBcOps(BalanceChangeOps bcOps) {
		this.bcOps = bcOps;
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

	public boolean effSystemSwitch(String Switch_code) {
		List<RuleSystemSwitch> lrss = dsRuleSystemSwitch.get();
		if (lrss != null) {
			for (RuleSystemSwitch rss : lrss) {
				if (rss.getSwitch_code().equals(Switch_code)
						&& rss.getSwitch_value().trim().equals("0")) {
					return true;
				}
			}
		}

		return false;
	}

	public int getAgreementType(List<String> proto_type, InfoUser infoUser)
			throws BasicException {
		int agreement_type = BasicType.NOT_ARGEEMENT_TYPE;
		if (proto_type != null && !proto_type.isEmpty()
				&& infoUser.getUser_status().trim().equals("202")) {
			LOGGER.debug("-----");
			for (String is : proto_type) {
				String macro_code = dsCodeList.get(is);
				LOGGER.debug("-----macro:" + macro_code);
				if (macro_code != null && !macro_code.isEmpty()) {
					agreement_type = dsRuleParameters
							.getArgeementType(macro_code);
					if (agreement_type == 1) {
						if (infoUser.getEff_date().equals(
								getCodeAcctMonth().getAct_eff_date())) {
							LOGGER.debug("----合约用户，当月1号进入双停，改变状态为101收取租费----");
							infoUser.setUser_status("101");
						}
						break;
					}
				}
			}
		}
		return agreement_type;
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
				LOGGER.debug("---用户激活时间为空,默认取当前时间-----");
				infoUser.setActive_date(new java.sql.Date(new java.util.Date()
						.getTime()));
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
				+ df.format(getCodeAcctMonth().getAct_eff_date())
				+ ",end_time:" + df.format(date));
		if (getCodeAcctMonth().getAct_eff_date().after(date)) {
			return true;
		}
		return false;
	}

	public List<RuleProductResourceExt> selectRuleProductResourceExt(
			String product_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
