package com.tydic.beijing.billing.cyclerent.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.service.CreateAccountBook;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.common.ErrorCode;
import com.tydic.beijing.billing.cyclerent.biz.ControlTradeActTagOps;
import com.tydic.beijing.billing.cyclerent.biz.CycleRentOps;
import com.tydic.beijing.billing.cyclerent.type.ProductInfo;
import com.tydic.beijing.billing.dao.CDRCycle;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoProtocolDetail;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.dao.LifeProductResourceRel;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.LifeUserStatus;
import com.tydic.beijing.billing.dao.LogCycleHistory;
import com.tydic.beijing.billing.dao.LogRefreshTrigger;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.QAcctProcess;
import com.tydic.beijing.billing.dao.RuleOfrSplit;
import com.tydic.beijing.billing.dao.RuleProductResource;
import com.tydic.beijing.billing.dao.RuleTariffConfInfo;
import com.tydic.beijing.billing.dao.infolxproduct;
import com.tydic.beijing.billing.dto.BalanceChangeInfo;
import com.tydic.beijing.billing.dto.BalanceChangeRequest;
import com.tydic.beijing.billing.dto.BaseResponse;
import com.tydic.beijing.billing.dto.ControlActTagResult;
import com.tydic.beijing.billing.dto.ResourceChargeParaInList;
import com.tydic.beijing.billing.dto.RetryCycleRentInfo;
import com.tydic.beijing.billing.dto.RetryCycleRentResult;
import com.tydic.beijing.billing.interfacex.service.RetryCycleRentService;
import com.tydic.beijing.billing.util.RecordAssembler;
import com.tydic.uda.service.S;

public class CycleRentAndRes2AccountImpl implements RetryCycleRentService {
	private static Logger LOGGER = Logger
			.getLogger(CycleRentAndRes2AccountImpl.class);
//	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final String STATE_OK = "0";
	private static final String STATE_FAIL = "1";
	private static final String USER_STATE_401 = "401";// 预销户
	private static final String USER_STATE_402 = "402";// 销户
	public static final int SHUJU_BALANCE_TYPE = 11;
	
	public static final int RESOURCE_TYPE_SJ = 2;
	// private static final int CHANNEL_NO = 16;
	// 失效方式:0-立即，1-次日，2-次月
	public static final String EFF_FLAG_NOW = "0";
	public static final String EFF_FLAG_MORROW = "1";
	public static final String EFF_FLAG_CIYT = "2";
	public static final String lch_event_type_ID ="10131";
	// 周期类型
	public static final int CYCLE_TYPE_DAY = 1;
	public static final int CYCLE_TYPE_MONTH = 2;
	private CycleRentOps ops;
	private ControlTradeActTagOps ctatOps;// 账期控制公共类
	private CreateAccountBook cabOps;

	public RetryCycleRentResult doRetryCycleRent(RetryCycleRentInfo info) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		long t1 = System.currentTimeMillis();
		List<QAcctProcess> lQacctProcess = new ArrayList<QAcctProcess>();
		List<CDRCycle> lcdr = new ArrayList<CDRCycle>();
		List<LogCycleHistory> dLogCycleHistory = new ArrayList<LogCycleHistory>();
		List<LogCycleHistory> iLogCycleHistory = new ArrayList<LogCycleHistory>();
		List<LifeProductResourceRel> lLifeProductResourceRel = new ArrayList<LifeProductResourceRel>();
		String event_type = BasicType.EVENT_TYPE_ID_CYCLE;
		RetryCycleRentResult result = new RetryCycleRentResult();
		assembleResult(result, STATE_OK, null, null);
		String trade_id = UUID.randomUUID().toString();
		Date date = new Date();

		CycleRuleCalcuUtil Util = new CycleRuleCalcuUtil();
		if (info == null || info.getUserId() == null || info.getUserId().isEmpty()) {
			return assembleResult(result, STATE_FAIL,
					ErrorCode.ERR_CODE_PARAMETER_EMPTY,
					ErrorCode.ERR_MSG_PARAMETER_EMPTY);
		}
		String user_id = info.getUserId();
		LOGGER.debug("----user_id :" + user_id);
		try {
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
			LOGGER.debug("---------  账期校验ok -------");
			// 加载规则表、配置表和账期表
			ops.load();
			CodeAcctMonth codeAcctMonth = ops.getCodeAcctMonth();
//			CodeAcctMonth codeAcctMonth = ops.getCodeAcctMonthByRealTime(BasicType.USE_TAG,BasicType.ACT_TAG);
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
			if (lifeUserStatus.getUser_status().equals(USER_STATE_401)
					|| lifeUserStatus.getUser_status().equals(USER_STATE_402)) {
				event_type = BasicType.EVENT_TYPE_ID_MIN;
			}

			
			// 获取用户副卡信息
			int vicecard = 0;

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
			
			int agreement_type = ops.getAgreementType(proto_type,infoUser,lifeUserStatus);
			String act_exp_date = df.format(codeAcctMonth.getAct_exp_date());
			List<LifeUserProduct> lLifeProduct = ops.getLifeUserProduct(user_id,act_exp_date);
			if (lLifeProduct == null || lLifeProduct.isEmpty()) {
				return assembleResult(result, STATE_FAIL,
						ErrorCode.ERR_CODE_ORDER_INFO_NOT_NULL,
						ErrorCode.ERR_MSG_ORDER_INFO_NOT_NULL);
			}
			LOGGER.debug("----user info begin-------");
			LOGGER.debug(infoUser.toString());
			LOGGER.debug(lifeUserStatus.toString());
			LOGGER.debug(payUserRel.toString());
//			LOGGER.debug(liam.toString());
			LOGGER.debug(lLifeProduct.toString());
			LOGGER.debug("----user info end--------");
			Map<ProductInfo, List<RuleTariffConfInfo>> map = new HashMap<ProductInfo, List<RuleTariffConfInfo>>();
			// 月租
			for (LifeUserProduct product : lLifeProduct) {
				ProductInfo productInfo = new ProductInfo();
				List<RuleTariffConfInfo> lrtci = new ArrayList<RuleTariffConfInfo>();
				List<RuleOfrSplit> lOfrSplit = ops.getRuleOfrSplit(product
						.getProduct_id());
				if (lOfrSplit == null || lOfrSplit.isEmpty()) {
					continue;
				}
				long mt1 = System.currentTimeMillis();
				for (RuleOfrSplit ruleOfrSplit : lOfrSplit) {
					ops.getTariffId(ruleOfrSplit.getOfr_c_id(), infoUser,
							lifeUserStatus, event_type, vicecard, lrtci,product.getEff_date(),agreement_type);
					if (lrtci == null || lrtci.isEmpty()) {
						continue;
					}
				}
				if (lrtci == null || lrtci.isEmpty()) {
					LOGGER.debug("---产品[" + product.getProduct_id() + "],用户状态["+ lifeUserStatus.getUser_status() + "],找不到月租资费信息");
					continue;
				}
				long mt2 = System.currentTimeMillis();
				LOGGER.debug("---ofr_c_id comput tariff time :" + (mt2 - mt1));
				productInfo.setProduct_id(product.getProduct_id());
				productInfo.setUser_product_id(product.getUser_product_id());
				map.put(productInfo, lrtci);
				LOGGER.debug("--product_id:" + product.getProduct_id()
						+ ",list<TariffInfo>:" + lrtci.toString());
			}
			LOGGER.debug("---all product info:" + map.toString());
			// 获取历史租费
			List<LogCycleHistory> lmLogCycleHistory = ops.getLogCycleHistory(
					infoUser.getUser_id(), codeAcctMonth.getAcct_month(),
					event_type);
			LOGGER.debug("--history cycle fee info :"
					+ lmLogCycleHistory.toString());
			long mt3 = System.currentTimeMillis();
			if(map == null || map.isEmpty()){
				lmLogCycleHistory = null;
			}
			LOGGER.debug("------map----"+map);
		for (ProductInfo pdi : map.keySet()) {
				LOGGER.debug("----------------pdi"+pdi);
			    Iterator<LogCycleHistory> iter2 = lmLogCycleHistory.iterator();
			    while (iter2.hasNext()) {
					LOGGER.debug("*********进行历史租费判断********");
					LogCycleHistory lch = iter2.next();
					LOGGER.debug("*********读取历史租费********");
                    List<RuleTariffConfInfo> lrtci =ops.getCycFlag(lch.getUser_id()) ;
					LOGGER.debug("*********获取来显租费配置********");
                    if(lrtci.size()>0){
					LOGGER.debug("*****用户由来显租费配置***********");
					RuleTariffConfInfo rtci =lrtci.get(0);
					LOGGER.debug("------rtci----"+rtci);
					LOGGER.debug("------编码对比"+rtci.getCyc_flag().equals("1"));
					if(rtci.getCyc_flag().equals("1")){
					LOGGER.debug("----------lch"+lch.getProduct_id()+lch.getAcct_month()+lch.getEvent_type_id());
					String lch_acct_month=String.valueOf(lch.getAcct_month());
					LOGGER.debug("*********"+lch_acct_month);
					String zcacct_month=String.valueOf(codeAcctMonth.getAcct_month());
					LOGGER.debug("*********"+zcacct_month);
					String lch_event_type_id=String.valueOf(lch.getEvent_type_id());
					LOGGER.debug("*********"+lch_event_type_id);
					List<infolxproduct> lxproduct =ops.getLxProductid(lch.getUser_id());
					LOGGER.debug("***lxproduct******"+lxproduct);

					if(lxproduct.size()>0){
					Iterator<infolxproduct> ilp =lxproduct.iterator();
					LOGGER.debug("*********ilp********"+ilp.hasNext());
					while(ilp.hasNext()){
						infolxproduct lxp =ilp.next();
						LOGGER.debug("*********lxp********"+lxp);
						String lch_product_id=String.valueOf(lch.getProduct_id());
						LOGGER.debug("------来显表lx_product_ID"+lxp.getProduct_id());
					    LOGGER.debug("------历史表product_id"+lch_product_id);
						LOGGER.debug("------历史账期"+lch_acct_month);
					    LOGGER.debug("------现在账期"+zcacct_month);
					    LOGGER.debug("------事件标识"+rtci.getCyc_flag());
					    LOGGER.debug("------历史收租表事件标识"+lch_event_type_ID);
						LOGGER.debug("------编码对比"+rtci.getCyc_flag().equals("1")+"--------产品编码对比"+lch_product_id.equals(lxp.getProduct_id())+"------账期对比"+lch_acct_month.equals(zcacct_month)+"事件类型对比"+lch.getEvent_type_id().equals(lch_event_type_ID));
						//成立不收租
						if(lch_product_id.equals(lxp.getProduct_id())&&lch_acct_month.equals(zcacct_month) && lch_event_type_id.equals(lch_event_type_ID))
							{
								LOGGER.debug("--一元卡用户[ " +"--user_id--"+lch.getUser_id()+"--product_id--"+lch.getProduct_id()
								+ "]已收取过本月来电显示费---");
								return assembleResult(result, STATE_FAIL,
										ErrorCode.ERR_CODE_LogCycleHistory_NOT_NULL,
										ErrorCode.ERR_MSG_LogCycleHistory_NOT_NULL);	
							}	
						}
			        }
					}
					}
		}
					}	
			assembleCycle(map, lmLogCycleHistory, iLogCycleHistory,
					dLogCycleHistory, lQacctProcess, lcdr, codeAcctMonth,
					infoUser);
			long mt4 = System.currentTimeMillis();
			LOGGER.debug("---assemble result info time:" + (mt4 - mt3));
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
				Date start_date = Util.getNewDate(
						codeAcctMonth.getAct_eff_date(),
						product.getEff_date());
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
					if(!ops.offsetCycle(mrpr.getOffset_ref_type(),mrpr.getOffset_cycle(),infoUser,lifeUserStatus.getEff_date())){
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
					LOGGER.debug("-----rule_product_resources.agreement_type = "+mrpr.getAgreement_type()+",user_id agreement_type = " + agreement_type);
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
					ResourceChargeParaInList rcpCommon = Util
							.getEffAndExpDate(mrpr.getEff_flag(),
									mrpr.getCycle_type(),
									mrpr.getCycle_value(), start_date);
					String baseDate = CycleRuleCalcuUtil.getBaseDate(mrpr.getOffset_ref_type(), infoUser.getCreate_date(), infoUser.getActive_date(), product.getEff_date(), codeAcctMonth.getAct_eff_date());
//					setEffAndExpDate(mrpr, rcpCommon);
					ops.setBalanceValue(mrpr,baseDate);
					InfoPayBalance mipb = cabOps.createNew(
							infoUser.getLocal_net(), payUserRel.getPay_id(),
							mrpr.getBalance_type_id(),
							df.parse(rcpCommon.getEffDate()),
							df.parse((rcpCommon.getExpDate())));
					
					if(mipb == null){
						LOGGER.error("---创建账本失败----");
						throw new BasicException("10002","创建账本失败");
					}
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
			ops.insertFileAndLog(lQacctProcess, lcdr, iLogCycleHistory,
					dLogCycleHistory, lLifeProductResourceRel, request);
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

//	private void setEffAndExpDate(RuleProductResource mrpr,
//			ResourceChargeParaInList rcpCommon) throws ParseException {
//
//		Calendar cal = Calendar.getInstance(); // 获取时间参数
//		cal.setTime(new Date());
//		String eff_date = setEffDate(cal, mrpr);
//		String exp_date = setExpDate(cal, mrpr);
//		rcpCommon.setEffDate(eff_date);
//		rcpCommon.setExpDate(exp_date);
//
//		LOGGER.debug("--eff_date = " + eff_date + ",exp_date = " + exp_date);
//	}
//
//	private String setExpDate(Calendar cal, RuleProductResource mrpr) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
//		String exp_date = "";
//		if (mrpr.getCycle_type() == CYCLE_TYPE_DAY) {
//			cal.add(Calendar.DATE, mrpr.getCycle_value() - 1);
//		} else if (mrpr.getCycle_type() == CYCLE_TYPE_MONTH) {
//			cal.set(Calendar.DATE, 1);
//			cal.add(Calendar.MONTH, mrpr.getCycle_value());
//			cal.add(Calendar.DATE, -1);
//		}
//		exp_date = df.format(cal.getTime());
//		exp_date += "235959";
//		return exp_date;
//	}
//
//	private String setEffDate(Calendar cal, RuleProductResource mrpr) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
//		String eff_date = "";
//		if (mrpr.getEff_flag().equals(EFF_FLAG_NOW)) {// 立即生效
//
//		} else if (mrpr.getEff_flag().equals(EFF_FLAG_MORROW)) {// 次日生效
//			cal.add(Calendar.DATE, 1);
//		} else if (mrpr.getEff_flag().equals(EFF_FLAG_CIYT)) {// 次月生效
//			LOGGER.debug("---次月-------");
//			cal.set(Calendar.DATE, 1);// 设为当前月的1号
//			cal.add(Calendar.MONTH, 1);
//		}
//		eff_date = df.format(cal.getTime());
//		eff_date += "000000";
//		return eff_date;
//	}

	private void setChangeValue(RuleProductResource mrpr) {
		//if (mrpr.getBalance_type_id() == SHUJU_BALANCE_TYPE) {
		if(mrpr.getResource_type() == RESOURCE_TYPE_SJ) {
			mrpr.setResource_value(mrpr.getResource_value() * 1024);
		}
	}

	private void assembleCycle(Map<ProductInfo, List<RuleTariffConfInfo>> map,
			List<LogCycleHistory> lmLogCycleHistory,
			List<LogCycleHistory> iLogCycleHistory,
			List<LogCycleHistory> dLogCycleHistory,
			List<QAcctProcess> lQacctProcess, List<CDRCycle> lcdr,
			CodeAcctMonth acct_month, InfoUser infoUser)
			throws UnknownHostException, BasicException {
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
							&& lch.getAcct_month() == acct_month
									.getAcct_month()) {
						flag2deal = 1;// 处理过
						if (rtci.getTariff_value() == lch.getFee()) {// fee相同，remove掉map.get(pdi)和lmLogCycleHistory中的记录
							iter1.remove();
						} else {// fee不同，抹掉历史租费，新增记录
							q_value += rtci.getAcct_item_code() + ":"
									+ rtci.getTariff_value() + ";"
									+ lch.getAcct_item_code() + ":-"
									+ lch.getFee() + ";";
//							rtci.setTariff_value(rtci.getTariff_value()
//									- lch.getFee());
							LogCycleHistory dlcy = new LogCycleHistory();
							dlcy.setAcct_month(acct_month.getAcct_month());
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
					mlcy.setAcct_month(acct_month.getAcct_month());
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
				qap.setAcct_month(acct_month.getAcct_month());
				qap.setSession_id(ops.getSeq("UA_CDR_SERIAL_NO")+"");
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
				cdr.setPartition_no(acct_month.getPartition_no());
				lcdr.add(cdr);
			}
		}

		if (lmLogCycleHistory != null) {
			for (LogCycleHistory logCycleHistory : lmLogCycleHistory) {
				String tariff_info = logCycleHistory.getAcct_item_code() + ":-"
						+ logCycleHistory.getFee();
				dLogCycleHistory.add(logCycleHistory);
				QAcctProcess qap = new QAcctProcess();
				qap.setAcct_month(acct_month.getAcct_month());
				qap.setSession_id(ops.getSeq("UA_CDR_SERIAL_NO")+"");
				qap.setChannel_no(Long.valueOf(infoUser.getUser_id())
						% ops.getchannel());
				qap.setCalled_party(infoUser.getDevice_number());
				qap.setCalling_party(infoUser.getDevice_number());
				qap.setUser_id(infoUser.getUser_id());
				qap.setTariff_info(tariff_info);
				qap.setProcess_tag(0);
				qap.setService_scenarious(500);
				lQacctProcess.add(qap);
//				long SerialNo = ops.getFSn();

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
				cdr.setPartition_no(acct_month.getPartition_no());
				lcdr.add(cdr);
			}
		}

	}


	private void fillResourceChangeResult(BaseResponse result,
			String stateFail, String errCodeActTagInvilid,
			String errMsgActTagInvilid) {
		result.setStatus(stateFail);
		result.setErrorCode(errCodeActTagInvilid);
		result.setErrorMessage(errMsgActTagInvilid);
	}

	private RetryCycleRentResult assembleResult(RetryCycleRentResult result, String state,
			String code, String msg) {
		result.setStatus(state);
		result.setErrorCode(code);
		result.setErrorMessage(msg);
		LOGGER.info("--result:" + result.toString());
		return result;
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

	@Override
	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) {
		LogRefreshTrigger lrt = new LogRefreshTrigger();
		try {
			if (BasicType.DS_CODE_ACCT_MONTH.equals(datastoreName)
					|| BasicType.DS_CODE_TRADE_TYPE_CODE.equals(datastoreName)) {
				ctatOps.refresh(refreshBatchId, datastoreName, serviceName);
			} else {
				ops.refresh(refreshBatchId, datastoreName, serviceName);
			}
		} catch (BasicException ex) {
			LOGGER.error(ex.toString());
			lrt = RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
					datastoreName, serviceName, BasicType.REFRESH_STATUS_FAIL,
					ex.toString());
		} finally {
			try {
				LOGGER.debug("--- " + lrt.toString());
				S.get(LogRefreshTrigger.class).create(lrt);
			} catch (Exception ex) {
				LOGGER.error("Insert LogRefreshTrigger Failed!["
						+ ex.getMessage() + "]");
			}
		}
		
	}

}
