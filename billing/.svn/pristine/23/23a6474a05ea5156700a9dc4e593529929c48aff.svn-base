package com.tydic.beijing.billing.cyclerent.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.tydic.beijing.billing.account.service.CreateAccountBook;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.BasicType;
import com.tydic.beijing.billing.cyclerent.biz.ResourceToAccountOps;
import com.tydic.beijing.billing.cyclerent.type.InfoUser;
import com.tydic.beijing.billing.dao.CodeAcctMonth;
import com.tydic.beijing.billing.dao.InfoPayBalance;
import com.tydic.beijing.billing.dao.InfoProtocolDetail;
import com.tydic.beijing.billing.dao.LifeProductResourceExtRel;
import com.tydic.beijing.billing.dao.LifeProductResourceRel;
import com.tydic.beijing.billing.dao.LifeUserProduct;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.RuleProductResource;
import com.tydic.beijing.billing.dao.RuleProductResourceExt;
import com.tydic.beijing.billing.dto.BalanceChangeInfo;
import com.tydic.beijing.billing.dto.BalanceChangeRequest;
import com.tydic.beijing.billing.dto.ResourceChargeParaInList;

public class ResourceToAccountThread implements Runnable,ApplicationContextAware {
	private static final Logger LOGGER = Logger
			.getLogger(ResourceToAccountThread.class);
	ApplicationContext mycontext;
	// private static SimpleDateFormat DF = new SimpleDateFormat("yyyyMMdd");
	private static final int SHUJU_BALANCE_TYPE = 11;
	// 失效方式:0-立即，1-次日，2-次月
	private static final String EFF_FLAG_NOW = "0";
	
	private static final String EFF_FLAG_MORROW = "1";
	private static final String EFF_FLAG_CIYT = "2";
	// 周期类型
	private static final int CYCLE_TYPE_DAY = 1;
	private static final int CYCLE_TYPE_MONTH = 2;
	private static final int RESOURCE_TYPE_SJ = 2;

	private int process_num;
	private int channel_no;// 控制进程（mod(user_id,?)）
	private int mod;// 结合mod_id,控制线程
	private int mod_i;//
	private int num;// 批量处理数量
	private ResourceToAccountOps ops;
	private CreateAccountBook cabOps;// 创建账本

	public void run() {
		try {
			ops = (ResourceToAccountOps) mycontext.getBean("ResToAccountOps");
			cabOps = (CreateAccountBook) mycontext.getBean("createAccountBook");
			List<LifeProductResourceRel> lLifeProductResourceRel = new ArrayList<LifeProductResourceRel>();
			List<BalanceChangeRequest> lrequest = new ArrayList<BalanceChangeRequest>();
			ops.load();// 加载规则表
			CodeAcctMonth codeAcctMonth = ops.getCodeAcctMonth();
			if (codeAcctMonth == null) {
				LOGGER.error("------当前账期错误-----");
				return;
			}
			LOGGER.debug("----" + codeAcctMonth.toString() + "--------");
			LOGGER.debug("-----mod(user_id," + process_num + ")=" + channel_no
					+ ",mod(device_number," + mod + "=" + mod_i + "-----");
			List<InfoUser> lInfoUser = ops.getInfoUser(process_num, channel_no,
					mod, mod_i);// user_id,user_product_id取模
			if (lInfoUser == null) {
				LOGGER.debug("-----mod(user_id," + process_num + ")="
						+ channel_no + ",mod(device_number," + mod + "="
						+ mod_i + "),already deal ------");
				return;
			}

			for (InfoUser infoUser : lInfoUser) {
				SimpleDateFormat DF = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				LOGGER.debug("----" + infoUser.toString()
						+ "-------deal begin -----");
				long mt1 = System.currentTimeMillis();
				String trade_id = UUID.randomUUID().toString();
				PayUserRel payUserRel = ops.getPayUserRelByUserId(infoUser
						.getUser_id());
				if (payUserRel == null) {
					LOGGER.error("---当前用户user_id[" + infoUser.getUser_id()
							+ "],没有账户------");
					continue;
				}
				// 获取用户合约类型
				int agreement_type = 0;
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
				
				String actExpDate = df.format(codeAcctMonth.getAct_exp_date());
				List<LifeUserProduct> lLifeUserProduct = ops
						.getLifeUserProductByUserId(infoUser.getUser_id(),actExpDate);
				if (lLifeUserProduct == null) {
					LOGGER.debug("---当前用户user_id[" + infoUser.getUser_id()
							+ "]没有订购信息------");
					continue;
				}
				LOGGER.debug("----" + lLifeUserProduct.toString()
						+ "-------------");
				LOGGER.debug("----get user info time:"
						+ (System.currentTimeMillis() - mt1));
				BalanceChangeRequest request = new BalanceChangeRequest();
				request.setUserId(infoUser.getUser_id());
				request.setDeviceNumber(infoUser.getDevice_number());
				request.setPayId(payUserRel.getPay_id());
				request.setSerialNo(trade_id);// 交易流水号
				request.setOperType(BasicType.CYCLE_RENT);//
				// request.setOperChannel(resourcePara.getChannel_id());//
				// 接触渠道
				request.setLocalNet(infoUser.getLocal_net());
				// request.setOperStaff(resourcePara.getStaff_id());// 操作员
				request.setOperTime(df.format(new Date()));// 操作时间
				request.setOuterSerialNo(null);
				// request.setSystemId(resourcePara.getExternal_system_id());
				List<BalanceChangeInfo> lbalanceChangeInfo = new ArrayList<BalanceChangeInfo>();
				for (LifeUserProduct product : lLifeUserProduct) {
					LOGGER.debug("----" + product.toString() + "-------");
					long mt3 = System.currentTimeMillis();
					String product_flag = "";
					List<RuleProductResource> lmrpr = ops
							.selectRuleProductResource(product.getProduct_id());
					if (lmrpr == null || lmrpr.isEmpty()) {
						LOGGER.debug("---product:" + product.getProduct_id()
								+ ",没有在资源到账规则表中配置，不属于资源到账产品----");
						continue;
					}
					LifeProductResourceRel ppr = new LifeProductResourceRel();
					for (RuleProductResource mrpri : lmrpr) {
						LOGGER.debug("-----mrpr :" + mrpri.toString()
								+ "-----change befor ----");
						RuleProductResource mrpr = new RuleProductResource();
						mrpr = (RuleProductResource) mrpri.clone();
						if (!ops.effFalg(mrpr.getState_ref_mode(), infoUser,
								codeAcctMonth)) {
							LOGGER.debug("--product:"
									+ product.getProduct_id()
									+ ",产品由于用户状态生失效时间与当前账期时间根据规则比对，不需要扣租以及资源到账----");
							continue;
						}
						if (!ops.effFalgByGroup(infoUser.getUser_status(),
								mrpr.getState_group())) {
							LOGGER.debug("----product:"
									+ product.getProduct_id()
									+ ",当前用户状态下不用资源到账-----");
							continue;
						}
						if (!ops.offsetCycle(mrpr.getOffset_ref_type(),
								mrpr.getOffset_cycle(), infoUser,product.getEff_date()
								)) {
							LOGGER.debug("-----rule_ofr_tariff_relation.offser_ref_type---不用资源到账-----");
							continue;
						}
						LOGGER.debug("-----mrpr :" + mrpr.toString()
								+ "-----change after ----");
						if (!infoUser.getUser_status().trim().equals("202")) {
							agreement_type = 0;// 合约正常状态
						}
						if (mrpr.getAgreement_type() != agreement_type) {
							LOGGER.debug("-----rule_product_resources.agreement_type = "
									+ mrpr.getAgreement_type()
									+ ",user_id agreement_type = "
									+ agreement_type);
							continue;
						}
						setChangeValue(mrpr);
						product_flag = mrpr.getProduct_type() + "";
						ppr.setUser_id(infoUser.getUser_id());
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
						LOGGER.debug("--cacl rule time :"
								+ (System.currentTimeMillis() - mt3));
						long mt4 = System.currentTimeMillis();
						// 创建资源账本
						ResourceChargeParaInList rcpCommon = new ResourceChargeParaInList();
						setEffAndExpDate(mrpr, rcpCommon);
						ops.setBalanceValue(mrpr, product.getEff_date());
						InfoPayBalance mipb = cabOps.createNew(
								infoUser.getLocal_net(),
								payUserRel.getPay_id(),
								mrpr.getBalance_type_id(),
								DF.parse(rcpCommon.getEffDate()),
								DF.parse((rcpCommon.getExpDate())));
						if (mipb == null) {
							LOGGER.error("----创建账本失败----");
							continue;
						}

						LOGGER.debug("--new balance info:" + mipb.toString());
						LOGGER.debug("--create table time :"
								+ (System.currentTimeMillis() - mt4));
						long mt5 = System.currentTimeMillis();
						lbalanceChangeInfo
								.add(assembleBalanceChangeInfo(
										mipb.getBalance_id(),
										mipb.getBalance_type_id(),
										BasicType.ACCESS_TAG_DEPOSIT,
										mrpr.getResource_value(),
										rcpCommon.getEffDate(),
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
						LOGGER.debug("----mlppr:" + mlppr.toString());
						lLifeProductResourceRel.add(mlppr);
						LOGGER.debug("---resource account time:"
								+ (System.currentTimeMillis() - mt5));
					}
					
					/**
					 * 海航特权
					 
					List<RuleProductResourceExt> lmrpre = ops
							.selectRuleProductResourceExt(product.getProduct_id());
					if (lmrpre == null || lmrpre.isEmpty()) {
						LOGGER.debug("---product:" + product.getProduct_id()
								+ ",没有在特权资源到账规则表中配置，不属于特权资源到账产品----");
						continue;
					}
					LifeProductResourceExtRel mpper = new LifeProductResourceExtRel();
					*/

				}
				request.setBalanceChangeInfo(lbalanceChangeInfo);
				lrequest.add(request);
				if (!lrequest.isEmpty() && lrequest.size() % num == 0) {
					ops.dealDb(lrequest, lLifeProductResourceRel);
					lrequest.clear();
					lLifeProductResourceRel.clear();
				}
				LOGGER.debug("-----user deal time :"
						+ (System.currentTimeMillis() - mt1));
			}
			if (!lrequest.isEmpty()) {
				ops.dealDb(lrequest, lLifeProductResourceRel);
				lrequest.clear();
				lLifeProductResourceRel.clear();
			}
		} catch (BasicException ex) {
			LOGGER.error("------" + ex.toString());
			return;
		} catch (Exception ex) {
			LOGGER.error("-----" + ex.toString());
			return;
		}
		return;
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

	private void setEffAndExpDate(RuleProductResource mrpr,
			ResourceChargeParaInList rcpCommon) {
		Calendar cal = Calendar.getInstance(); // 获取时间参数
		cal.setTime(new Date());

		String eff_date = setEffDate(cal, mrpr);
		String exp_date = setExpDate(cal, mrpr);

		rcpCommon.setEffDate(eff_date);
		rcpCommon.setExpDate(exp_date);

		LOGGER.debug("--eff_date = " + eff_date + ",exp_date = " + exp_date);
	}

	private void setChangeValue(RuleProductResource mrpr) {
//		if (mrpr.getBalance_type_id() == SHUJU_BALANCE_TYPE) {
		if(mrpr.getResource_type() == RESOURCE_TYPE_SJ) {
			mrpr.setResource_value(mrpr.getResource_value() * 1024);
		}
	}

	private String setExpDate(Calendar cal, RuleProductResource mrpr) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		// java.sql.Date exp_date = (java.sql.Date) cal.getTime();
		String exp_date = "";
		if (mrpr.getCycle_type() == CYCLE_TYPE_DAY) {
			cal.add(Calendar.DATE, mrpr.getCycle_value() - 1);
		} else if (mrpr.getCycle_type() == CYCLE_TYPE_MONTH) {
			cal.set(Calendar.DATE, 1);
			cal.add(Calendar.MONTH, mrpr.getCycle_value());
			cal.add(Calendar.DATE, -1);
		}
		exp_date = df.format(cal.getTime());
		// exp_date += "235959";
		// exp_date = (java.sql.Date) cal.getTime();
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
		// eff_date += "000000";
		return eff_date;
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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		mycontext = applicationContext;
	}

}
