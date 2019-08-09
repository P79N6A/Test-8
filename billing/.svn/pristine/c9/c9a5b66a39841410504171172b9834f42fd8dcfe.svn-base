package com.tydic.beijing.billing.cyclerent.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.service.CreateAccountBook;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.cyclerent.biz.ControlTradeActTagOps;
import com.tydic.beijing.billing.cyclerent.biz.CycleRentOps;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoProtocolDetail;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeProductResourceRel;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.LifeUserStatus;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.RuleProductResource;
import com.tydic.beijing.billing.dto.BalanceChangeInfo;
import com.tydic.beijing.billing.dto.BalanceChangeRequest;
import com.tydic.beijing.billing.dto.BaseResponse;
import com.tydic.beijing.billing.dto.ControlActTagResult;
import com.tydic.beijing.billing.dto.Resource2AccountInfo;
import com.tydic.beijing.billing.dto.Resource2AccountResult;
import com.tydic.beijing.billing.dto.ResourceChargeParaInList;
import com.tydic.beijing.billing.interfacex.service.Resource2Account;

public class ResourceToAccountImpl implements Resource2Account {
	private static Logger LOGGER = Logger
			.getLogger(ResourceToAccountImpl.class);
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final String STATE_OK = "0";
	private static final String STATE_FAIL = "1";
	public static final int SHUJU_BALANCE_TYPE = 11;
	// private static final int CHANNEL_NO = 16;
	// 失效方式:0-立即，1-次日，2-次月
	public static final String EFF_FLAG_NOW = "0";
	public static final String EFF_FLAG_MORROW = "1";
	public static final String EFF_FLAG_CIYT = "2";
	// 周期类型
	public static final int CYCLE_TYPE_DAY = 1;
	public static final int CYCLE_TYPE_MONTH = 2;
	private CycleRentOps ops;
	private ControlTradeActTagOps ctatOps;// 账期控制公共类
	private CreateAccountBook cabOps;

	@Override
	public Resource2AccountResult doRes2Acct(Resource2AccountInfo info) {
		List<LifeProductResourceRel> lLifeProductResourceRel = new ArrayList<LifeProductResourceRel>();
		Resource2AccountResult result = new Resource2AccountResult();
		try {
			long t1 = System.currentTimeMillis();
			assembleResult(result, STATE_OK, null, null);
			String errorMsg = checkInputParameter(info);
			if (errorMsg != null) {
				result.setStatus(STATE_FAIL);
				result.setErrorCode(ErrorCode.ERR_PARAM_CONTENT + "");
				result.setErrorMessage(errorMsg);
				LOGGER.error("--" + info.toString());
				LOGGER.error("--" + result.toString());
				return result;
			}
			LOGGER.debug("----trade acct month control begin-----");
			// 判读当前act_tag下资源转换是否有效
			ControlActTagResult atresult = ctatOps
					.doProcess(BasicType.CYCLE_RENT);
			if (atresult.getResult() == 1) {
				fillResourceChangeResult(result, STATE_FAIL,
						ErrorCode.ERR_CODE_ACT_TAG_INVILID,
						ErrorCode.ERR_MSG_ACT_TAG_INVILID);
				return result;
			}
			LOGGER.debug("------账期校验OK------");
			String trade_id = UUID.randomUUID().toString();
			Date date = new Date();
			
			ops.load();// 加载规则表
			CodeAcctMonth codeAcctMonth = ops.getCodeAcctMonth();
			if (codeAcctMonth == null) {
				LOGGER.error("------当前账期错误-----");
				return assembleResult(result, STATE_FAIL,
						ErrorCode.ERR_CODE_OTHER_EXCEPTION, "--获取当前账期错误----");
			}

			LOGGER.debug("----" + codeAcctMonth.toString() + "--------");
			String user_id = info.getUserId();
			// 获取用户信息和订购的产品信息
			InfoUser infoUser = ops.getInfoUser(user_id);
			LifeUserStatus lifeUserStatus = ops.getlifeUserStatus(user_id);
			PayUserRel payUserRel = ops.getPayUserRel(user_id);
			if (infoUser == null || lifeUserStatus == null
					|| payUserRel == null) {
				return assembleResult(result, STATE_FAIL,
						ErrorCode.ERR_CODE_INFO_USER_EMPTY,
						ErrorCode.ERR_MSG_INFO_USER_EMPTY);
			}
			String actExpDate = df.format(codeAcctMonth.getAct_exp_date());
			List<LifeUserProduct> lLifeProduct = ops
					.getLifeUserProduct(user_id,actExpDate);
			if (lLifeProduct == null || lLifeProduct.isEmpty()) {
				return assembleResult(result, STATE_FAIL,
						ErrorCode.ERR_CODE_ORDER_INFO_NOT_NULL,
						ErrorCode.ERR_MSG_ORDER_INFO_NOT_NULL);
			}
			//获取用户合约类型
			List<String> proto_type = new ArrayList<String>();
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
			
			int agreement_type = ops.getAgreementType(proto_type,infoUser,lifeUserStatus);
			LOGGER.debug("----user info begin-------");
			LOGGER.debug(infoUser.toString());
			LOGGER.debug(lifeUserStatus.toString());
			LOGGER.debug(payUserRel.toString());
			LOGGER.debug(lLifeProduct.toString());
			LOGGER.debug("----user info end--------");

			// 资源到账
			LOGGER.debug("------resource to account begin----------------");
			BalanceChangeRequest request = new BalanceChangeRequest();
			request.setUserId(infoUser.getUser_id());
			request.setDeviceNumber(infoUser.getDevice_number());
			request.setPayId(payUserRel.getPay_id());
			request.setSerialNo(trade_id);// 交易流水号
			request.setOperType(BasicType.CYCLE_RENT);// 2.销账流水
			// request.setOperChannel(resourcePara.getChannel_id());// 接触渠道
			request.setLocalNet(infoUser.getLocal_net());
			// request.setOperStaff(resourcePara.getStaff_id());// 操作员
			request.setOperTime(df.format(date));// 操作时间
			request.setOuterSerialNo(null);
			// request.setSystemId(resourcePara.getExternal_system_id());
			List<BalanceChangeInfo> lbalanceChangeInfo = new ArrayList<BalanceChangeInfo>();
			for (LifeUserProduct product : lLifeProduct) {
				String product_flag = "";
				List<RuleProductResource> lmrpr = ops
						.selectRuleProductResource(product.getProduct_id());
				LOGGER.debug("----------rule product resource -----"
						+ product.toString());
				if (lmrpr == null) {
					LOGGER.debug("---product:" + product.getProduct_id()
							+ ",没有在资源到账规则表中配置，不属于资源到账产品----");
					continue;
				}
				LifeProductResourceRel ppr = new LifeProductResourceRel();
				for (RuleProductResource mrpri : lmrpr) {
					RuleProductResource mrpr = new RuleProductResource();
					mrpr = (RuleProductResource) mrpri.clone();
					if (!ops.effFalg(mrpr.getState_ref_mode(), lifeUserStatus,
							codeAcctMonth)) {
						LOGGER.debug("--product:" + product.getProduct_id()
								+ ",产品由于用户状态生失效时间与当前账期时间根据规则比对，不需要扣租以及资源到账----");
						continue;
					}
					if (!ops.effFalgByGroup(lifeUserStatus.getUser_status(),
							mrpr.getState_group())) {
						LOGGER.debug("----product:" + product.getProduct_id()
								+ ",当前用户状态下不用资源到账-----");
						continue;
					}
					if (!ops.offsetCycle(mrpr.getOffset_ref_type(),
							mrpr.getOffset_cycle(), infoUser,
							product.getEff_date())) {
						LOGGER.debug("-----rule_ofr_tariff_relation.offser_ref_type---不用资源到账-----");
						continue;
					}
					LOGGER.debug("-----mrpr :" + mrpr.toString());
					LOGGER.debug("---acctMonth = " + codeAcctMonth.toString());
//					if((lifeUserStatus.getUser_status().equals("202")) && (agreement_type == BasicType.ARGEEMENT_TYPE_NORMAL || agreement_type == BasicType.ARGEEMENT_TYPE_ABBORMAL)){
//						//合约
//						if(mrpr.getAgreement_type() != agreement_type){
//							LOGGER.debug("-----rule_product_resources.agreement_type = "+mrpr.getAgreement_type()+",user_id agreement_type = " + agreement_type);
//							continue;
//						}
//					}
					
					if(!lifeUserStatus.getUser_status().trim().equals("202")){
						agreement_type = 0;//合约正常状态
					}
					if(mrpr.getAgreement_type() != agreement_type){
						LOGGER.debug("-----rule_product_resources.agreement_type = "+mrpr.getAgreement_type()+",user_id agreement_type = " + agreement_type);
						continue;
					}
					
					setChangeValue(mrpr);
					product_flag = mrpr.getProduct_type() + "";
					ppr.setUser_id(user_id);
					ppr.setBalance_type_id(mrpr.getBalance_type_id());
					ppr.setProduct_id(product.getProduct_id());
					ppr.setUser_product_id(product.getUser_product_id());
					ppr.setAcct_month(codeAcctMonth.getAcct_month());
					ppr.setOfr_c_id(mrpr.getOfr_c_id());
					LOGGER.debug("----life product resource rel :"
							+ ppr.toString());
					LifeProductResourceRel mppr = ops
							.selectLifeProductResourceRel(ppr);
					if (mppr != null) {
						LOGGER.debug("--当前产品[ " + product.getProduct_id()
								+ "]已处理过---");
						continue;
					}
					LOGGER.debug("#$#$ " + mrpr.toString());
					// 创建资源账本
					ResourceChargeParaInList rcpCommon = new ResourceChargeParaInList();
					setEffAndExpDate(mrpr, rcpCommon);
					ops.setBalanceValue(mrpr, product.getEff_date());
					InfoPayBalance mipb = cabOps.createNew(
							infoUser.getLocal_net(), payUserRel.getPay_id(),
							mrpr.getBalance_type_id(),
							df.parse(rcpCommon.getEffDate()),
							df.parse((rcpCommon.getExpDate())));
					LOGGER.debug("--new balance info:" + mipb.toString());

					lbalanceChangeInfo.add(assembleBalanceChangeInfo(
							mipb.getBalance_id(), mipb.getBalance_type_id(),
							BasicType.ACCESS_TAG_DEPOSIT,
							mrpr.getResource_value(), rcpCommon.getEffDate(),
							rcpCommon.getExpDate()));
					LifeProductResourceRel mlppr = new LifeProductResourceRel();
					mlppr.setAcct_month(codeAcctMonth.getAcct_month());
					mlppr.setUser_id(infoUser.getUser_id());
					mlppr.setUser_product_id(product.getUser_product_id());
					mlppr.setProduct_id(product.getProduct_id());
					mlppr.setOfr_c_id(product.getProduct_id());
					mlppr.setProduct_flag(product_flag);
					mlppr.setSerial_num(trade_id);
					mlppr.setBalance_type_id(mipb.getBalance_type_id());
					mlppr.setBalance_id(mipb.getBalance_id());
					mlppr.setEff_date(df.format(mipb.getEff_date()));
					mlppr.setExp_date(df.format(mipb.getExp_date()));
					lLifeProductResourceRel.add(mlppr);
				}
			}
			LOGGER.debug("------resource to account begin----------------");
			request.setBalanceChangeInfo(lbalanceChangeInfo);
			ops.insertFileAndLog(null, null, null,
					null, lLifeProductResourceRel, request);
			long t2 = System.currentTimeMillis();

			LOGGER.debug("--user_id[" + user_id + "]---deal time :" + (t2 - t1));

		} catch (BasicException e) {
			LOGGER.error("-----ERROR_INFO:" + e.toString());
			return assembleResult(result, STATE_FAIL, e.getCodeStr(),
					e.getMessage());
		} catch (Exception e) {
			LOGGER.error("-----ERROR_INFO:" + e.toString());
			return assembleResult(result, STATE_FAIL,
					ErrorCode.ERR_CODE_PARAMETER_EMPTY, e.toString());
		}

		return result;
	}
	
	private BalanceChangeInfo assembleBalanceChangeInfo(long balanceId,
			int in_balance_type_id, String changeTypeDeposit, long inBalance,
			String effDate, String expDate) {
		BalanceChangeInfo bci = new BalanceChangeInfo();
		bci.setBalanceId(balanceId);
		bci.setBalanceTypeId(in_balance_type_id);
		bci.setChangeType(changeTypeDeposit);
		bci.setChangeValue(inBalance);
		bci.setEffDate(effDate);
		bci.setExpDate(expDate);
		return bci;
	}

	private void fillResourceChangeResult(BaseResponse result,
			String stateFail, String errCodeActTagInvilid,
			String errMsgActTagInvilid) {
		result.setStatus(stateFail);
		result.setErrorCode(errCodeActTagInvilid);
		result.setErrorMessage(errMsgActTagInvilid);
	}

	private String checkInputParameter(Resource2AccountInfo info) {
		if (info.getUserId() == null || info.getUserId().equals("")) {
			return "传入的参数：用户ID为空";
		} else if (info.getContactChannel() == null
				|| info.getContactChannel().equals("")) {
			return "传入的参数：接触渠道为空";
		} else if (info.getlProductInfo() == null
				|| info.getlProductInfo().isEmpty()) {
			return "传入的参数：产品信息为空";
		}
		return null;
	}
	
	private void setEffAndExpDate(RuleProductResource mrpr,
			ResourceChargeParaInList rcpCommon) throws ParseException {

		Calendar cal = Calendar.getInstance(); // 获取时间参数
		cal.setTime(new Date());
		//
		// cal.set(Calendar.HOUR_OF_DAY, 0);
		// cal.set(Calendar.MINUTE, 0);
		// cal.set(Calendar.SECOND, 0);

		String eff_date = setEffDate(cal, mrpr);
		String exp_date = setExpDate(cal, mrpr);

		rcpCommon.setEffDate(eff_date);
		rcpCommon.setExpDate(exp_date);

		LOGGER.debug("--eff_date = " + eff_date + ",exp_date = " + exp_date);
	}

	private Resource2AccountResult assembleResult(
			Resource2AccountResult result, String state, String code, String msg) {
		result.setStatus(state);
		result.setErrorCode(code);
		result.setErrorMessage(msg);
		LOGGER.info("--result:" + result.toString());
		return result;
	}
	
	private void setChangeValue(RuleProductResource mrpr) {
		if (mrpr.getBalance_type_id() == SHUJU_BALANCE_TYPE) {
			mrpr.setResource_value(mrpr.getResource_value() * 1024);
		}
	}
	
	private String setExpDate(Calendar cal, RuleProductResource mrpr) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String exp_date = "";
		if (mrpr.getCycle_type() == CYCLE_TYPE_DAY) {
			cal.add(Calendar.DATE, mrpr.getCycle_value() - 1);
		} else if (mrpr.getCycle_type() == CYCLE_TYPE_MONTH) {
			cal.set(Calendar.DATE, 1);
			cal.add(Calendar.MONTH, mrpr.getCycle_value());
			cal.add(Calendar.DATE, -1);
		}
		exp_date = df.format(cal.getTime());
		exp_date += "235959";
		return exp_date;
	}

	private String setEffDate(Calendar cal, RuleProductResource mrpr) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String eff_date = "";
		if (mrpr.getEff_flag().equals(EFF_FLAG_NOW)) {// 立即生效

		} else if (mrpr.getEff_flag().equals(EFF_FLAG_MORROW)) {// 次日生效
			cal.add(Calendar.DATE, 1);
		} else if (mrpr.getEff_flag().equals(EFF_FLAG_CIYT)) {// 次月生效
			LOGGER.debug("---次月-------");
			cal.set(Calendar.DATE, 1);// 设为当前月的1号
			cal.add(Calendar.MONTH, 1);
		}
		eff_date = df.format(cal.getTime());
		eff_date += "000000";
		return eff_date;
	}

	public CycleRentOps getOps() {
		return ops;
	}

	public void setOps(CycleRentOps ops) {
		this.ops = ops;
	}

	public ControlTradeActTagOps getCtatOps() {
		return ctatOps;
	}

	public void setCtatOps(ControlTradeActTagOps ctatOps) {
		this.ctatOps = ctatOps;
	}

	public CreateAccountBook getCabOps() {
		return cabOps;
	}

	public void setCabOps(CreateAccountBook cabOps) {
		this.cabOps = cabOps;
	}

}
