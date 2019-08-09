package com.tydic.beijing.bvalue.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.HlpSmsSend;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.dao.AutoExchangeSmsSend;//add zxm
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoPayBalanceSku;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LifeResourceList;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchange;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchangeComposite;
import com.tydic.beijing.bvalue.dao.LogAutoExchangeLog;
import com.tydic.beijing.bvalue.dao.LogRatableHistory;
import com.tydic.beijing.bvalue.dao.LogTrade;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeSetHis;
import com.tydic.beijing.bvalue.dao.LogTradeCreateUserHis;
import com.tydic.beijing.bvalue.dao.LogTradeExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LogTradeShopping;
import com.tydic.beijing.bvalue.dao.LogTradeSku;
import com.tydic.beijing.bvalue.dao.LogTradeSkuHis;
import com.tydic.beijing.bvalue.dao.TbSmsSendHis;
import com.tydic.beijing.bvalue.dao.ExchangeRatableHistory;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

import oracle.net.aso.f;

public class TradeExchangeOper {

	private static final Logger log = Logger.getLogger(TradeExchangeOper.class);

	// 按balance_type_id查询有效余额，多个
	public List<InfoPayBalance> getInfoPayBalance(final String userId, final String typeId,
			final String currDate) {

		List<InfoPayBalance> ret = S.get(InfoPayBalance.class).query(
				Condition.build("getByUseridAndTypeId").filter("userId", userId)
						.filter("typeId", typeId).filter("currDate", currDate));

		return ret;
	}
	
	public List<InfoPayBalance> getInfoPayBalanceByManager(final String userId,  
			final String currDate) {
		return S.get(InfoPayBalance.class).query(Condition.build("getInfoPayBalanceByManager").filter("user_id", userId).filter("currentTime", currDate));
		 
	}
	
	

	// 查询有效余额，最多4条
	public List<InfoPayBalance> getAllInfoPayBalance(final String userId, final String currDate) {

		List<InfoPayBalance> ret = S.get(InfoPayBalance.class).query(
				Condition.build("getAll").filter("userId", userId).filter("currDate", currDate));

		return ret;
	}

	// 查询有效余额，最多4条
	public List<InfoPayBalance> getAllValidInfoPayBalance(final String userId, final String currDate) {

		List<InfoPayBalance> ret = S.get(InfoPayBalance.class).query(
				Condition.build("getValidInfoPayBalanceByUserId").filter("user_id", userId)
						.filter("currentTime", currDate));

		return ret;
	}

	// 更新余额
	public void uInfoPayBalanceByBalanceId(final InfoPayBalance ipb) {
		S.get(InfoPayBalance.class).update(ipb);
	}

	// 批量更新余额
	public void batchInfoPayBalanceByBalanceIdBatch(final List<InfoPayBalance> ib) {
		for (InfoPayBalance i : ib) {
			uInfoPayBalanceByBalanceId(i);
		}

	}

	// 更新，dataservice配置不同，更新expdate
	public void uInfoPayBalanceForExpDate(final InfoPayBalance pay) {
		S.get(InfoPayBalance.class).batch(
				Condition.build("updateBalanceAndExpDate").filter("balance", pay.getBalance())
						.filter("exp_date", pay.getExp_date())
						.filter("eff_date", pay.getEff_date()).filter("user_id", pay.getUser_id())
						.filter("balance_id", pay.getBalance_id()), pay);

	}

	// 新建账本表
	public void iInfoPayBalance(final InfoPayBalance t) {
		S.get(InfoPayBalance.class).create(t);
	}

	// 插入交易登记处理历史表
	public void iLogTradeHis(final LogTradeHis his) {
		S.get(LogTradeHis.class).create(his);
	}

	// 批量插入交易登记处理历史表
	public void batchLogTradeHis(final List<LogTradeHis> his) {
		for (LogTradeHis l : his) {
			iLogTradeHis(l);
		}
	}

	// 插入兑换处理历史表
	public void iLogTradeExchangeHis(final LogTradeExchangeHis log) {
		S.get(LogTradeExchangeHis.class).create(log);
	}

	// 批量插入兑换处理历史表
	public void batchLogTradeExchangeHis(final List<LogTradeExchangeHis> log) {
		for (LogTradeExchangeHis l : log) {
			iLogTradeExchangeHis(l);
		}
	}

	// 插入兑换资源表
	public void iLifeResourceList(final LifeResourceList t) {
		S.get(LifeResourceList.class).create(t);
	}

	// 查询life_resource_list
	public List<LifeResourceList> getLifeResourceLists(final String userId,
			final String resourceListId) {
		List<LifeResourceList> ret = S.get(LifeResourceList.class).query(
				Condition.build("xgetLifeSourceListByExchangeId").filter("userId", userId)
						.filter("resourceListId", resourceListId));
		return ret;
	}

	// 自动兑换订购关系
	public void iLifeUserAutoExchange(final LifeUserAutoExchange t) {
		S.get(LifeUserAutoExchange.class).create(t);
	}

	// 返回下月有效的记录
	public List<LifeUserAutoExchange> getLifeUserAutoExchanges(final String currDate) {
		List<LifeUserAutoExchange> ret = S.get(LifeUserAutoExchange.class).query(
				Condition.build("queryEffectiveRecord").filter("currDate", currDate));
		return ret;
	}

	// true 不存在， false 存在
	public boolean hasLifeUserAutoExchange(final String userId, final String exchangeId) {

		boolean ret = S
				.get(LifeUserAutoExchange.class)
				.query(Condition.build("xCheckExchangeId").filter("userId", userId)
						.filter("exchangeId", exchangeId)).isEmpty();
		return ret;
	}

	// 自动兑换订购关系
	public void uLifeUserAutoExchange(final LifeUserAutoExchange t) {
		int ret = S.get(LifeUserAutoExchange.class).update(t);
	}
	

	// 交易处理表
	public void iLogTrade(final LogTrade t) {
		S.get(LogTrade.class).create(t);
	}

	// 购物赠处理表
	public void iLogTradeShopping(final LogTradeShopping t) {
		S.get(LogTradeShopping.class).create(t);
	}

	// SKU 处理表
	public void iLogTradeSku(final LogTradeSku t) {
		S.get(LogTradeSku.class).create(t);
	}

	// 查询订单好是否重复处理LogTradeHis
	public List<LogTradeHis> getLogTradeHis(final String userId, final String orderNo,
			final String tradeTypeCodes) {
		List<LogTradeHis> ret = S.get(LogTradeHis.class).query(
				Condition.build("xByOrderNoAndTradeTypeCode").filter("user_id", userId)
						.filter("tradeTypeCodes", tradeTypeCodes).filter("order_no", orderNo));
		return ret;
	}

	// 查询订单好是否重复处理LogTrade
	public List<LogTrade> getLogTrade(final String userId, final String orderNO,
			final String tradeTypeCodes) {
		//long time1 = System.currentTimeMillis();
		List<LogTrade> ret = S.get(LogTrade.class).query(
				Condition.build("xByOrderNoAndTradeTypeCode").filter("user_id", userId)
						.filter("tradeTypeCodes", tradeTypeCodes).filter("order_no", orderNO));
		
		//long time2 = System.currentTimeMillis();
		//log.debug("查询trade表判断原订单是否存在耗时"+(time2-time1));
		return ret;
	}

	// 异动表
	public void iBalanceAccessLog(final BalanceAccessLog t) {
		S.get(BalanceAccessLog.class).create(t);
	}

	// 异动表，批量
	public void batchBalanceAccessLog(final List<BalanceAccessLog> t) {
		for (BalanceAccessLog b : t) {
			iBalanceAccessLog(b);
		}
	}

	// 自动兑换设置处理历史表
	public void iLogTradeAutoExchangeSetHis(final LogTradeAutoExchangeSetHis t) {
		S.get(LogTradeAutoExchangeSetHis.class).create(t);
	}

	// 自动兑换设置处理历史表，批量
	public void batchLogTradeAutoExchangeSetHis(final List<LogTradeAutoExchangeSetHis> t) {
		for (LogTradeAutoExchangeSetHis l : t) {
			iLogTradeAutoExchangeSetHis(l);
		}
	}

	// 查询累积量表
	public LogRatableHistory getLogRatableHistory(final String userId, final String currDate) {
		LogRatableHistory ret = S.get(LogRatableHistory.class).queryFirst(
				Condition.build("byUserId").filter("user_id", userId).filter("currDate", currDate));

		return ret;
	}

	//查询属性表中SKU配置，1003
	public List<InfoUserExternalAccountAttr> getExternalAccountAttr(String userId,String externalId){
		List<InfoUserExternalAccountAttr> attrslist=S.get(InfoUserExternalAccountAttr.class).query(
				Condition.build("byExternalAttr").filter("userId",userId).filter("externalId",externalId));
		return attrslist;
	}
	
	// update累积量表
	public void uLogRatableHistory(final LogRatableHistory t) {
		S.get(LogRatableHistory.class).update(t);
	}

	// insert累积量表
	public void iLogRatableHistory(final LogRatableHistory t) {
		S.get(LogRatableHistory.class).create(t);
	}

	// sku订单处理历史表
	public void iLogTradeSkuHis(final LogTradeSkuHis t) {
		S.get(LogTradeSkuHis.class).create(t);
	}

	// 删除
	public void dLogTradeSku(final LogTradeSku logTradeSku) {
		S.get(LogTradeSku.class).batch(
				Condition.build("deleteByTradeId").filter("user_id", logTradeSku.getUser_id())
						.filter("trade_id", logTradeSku.getTrade_id()), logTradeSku);
	}

	// 返回退货单价
	public LogTradeSku getBValueOfLogTradeSku(final String userId, final String tradeId,
			final String skuId) {
		LogTradeSku ret = S.get(LogTradeSku.class).queryFirst(
				Condition.build("xGetBValue").filter("user_id", userId).filter("trade_id", tradeId)
						.filter("sku_id", skuId));
		return ret;
	}

	// 返回退货单价
	public LogTradeSkuHis getBValueOfLogTradeSkuHis(final String userId, final String tradeId,
			final String skuId) {
		LogTradeSkuHis ret = S.get(LogTradeSkuHis.class).queryFirst(
				Condition.build("xGetBValue").filter("user_id", userId).filter("trade_id", tradeId)
						.filter("sku_id", skuId));
		return ret;
	}

	/**
	 * 
	 * getLogTradeByMod:分通道取表数据by user_id.<br/>
	 * 
	 * @param userId
	 * @param channel
	 * @param remainder
	 * @return
	 */
	public List<LogTrade> getLogTradeByMod(final int channel, final int remainder) {
		List<LogTrade> ret = S.get(LogTrade.class).query(
				Condition.build("byMod").filter("channel", channel).filter("remainder", remainder));

		return ret;
	}

	/**
	 * 
	 * isOrgNoExisted: 检查原始订单号是否存在.<br/>
	 * 
	 * @param userId
	 * @param tradeId
	 * @return
	 *         true notfound
	 *         false found
	 */
	public boolean isOrgNoNotFound(final String userId, final String orgOrderNo,
			final String tradeTypeCodes,LogTrade logTrade) {
		//long time1 = System.currentTimeMillis();
		if (getLogTradeHis(userId, orgOrderNo, tradeTypeCodes).isEmpty()) {
		//	long time2 = System.currentTimeMillis();		
		//	log.debug("查询his表原始订单是否存在耗时"+(time2-time1));
			List<LogTrade> logTradeA =  getLogTrade(userId, orgOrderNo, tradeTypeCodes);
			List<LogTrade> logTradeB = new ArrayList<LogTrade>();
			for(LogTrade tmplt :logTradeA){
				if(!tmplt.getTrade_id().equals(logTrade.getTrade_id())){
					logTradeB.add(tmplt);
				}
			}
			
			
			return logTradeB.isEmpty();
		} else {
			return false;
		}

	}

	/**
	 * 
	 * isDuplication:检查订单是否重复处理 .<br/>
	 * 
	 * @param orderNO
	 * @return
	 *         true 是
	 *         false 不是
	 */
	public boolean isDuplicated(final String userId, final String orderNo,
			final String tradeTypeCodes,LogTrade logTrade) {
		// 先查LogTradeHis
		//long time1= System.currentTimeMillis();
		List<LogTradeHis> logTradeHis = getLogTradeHis(userId, orderNo, tradeTypeCodes);
		
	//	long time2= System.currentTimeMillis();
		//log.debug("校验订单重复，查询his表耗时"+(time2-time1));
		
		if (logTradeHis.isEmpty()) {
			// 再查LogTrade
//			List<LogTrade> logTradeA = getLogTrade(userId, orderNo, tradeTypeCodes);
//			List<LogTrade> logTradeB = new ArrayList<LogTrade>();
//			for(LogTrade tmplt:logTradeA){
//				if(!tmplt.getTrade_id().equals(logTrade.getTrade_id())){
//					logTradeB.add(tmplt);
//				}
//			}
//
//			if (!logTradeB.isEmpty()) {
//				return true;
//			}
			return false;
		} else {
			return true;
		}
		//return false;
	}
	
	
	public boolean isDuplicated(final String userId, final String orderNo,
			final String tradeTypeCodes) {
		// 先查LogTradeHis
		//long time1= System.currentTimeMillis();
		List<LogTradeHis> logTradeHis = getLogTradeHis(userId, orderNo, tradeTypeCodes);
		if (logTradeHis.isEmpty()) {
			// 再查LogTrade
			List<LogTrade> logTradeA = getLogTrade(userId, orderNo, tradeTypeCodes);
			
			if (!logTradeA.isEmpty()) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * updateTradeSku:. <br/>
	 * 插入交易处理表log_trade.<br/>
	 * 插入SKU处理表log_trade_sku.<br/>
	 * 
	 * @param logTrades
	 * @param logTradeShoppings
	 *            @
	 */
	public void updateTradeSku(final LogTrade logTrade, final List<LogTradeSku> logTradeSkus) {

		iLogTrade(logTrade);

		for (LogTradeSku logTradeSku : logTradeSkus) {
			iLogTradeSku(logTradeSku);
		}
	}


	/**
	 * 更改当前自动兑换设置的失效时间，改为当月最后一天
	 */
	public void updateExpdate(LifeUserAutoExchange l){
		S.get(LifeUserAutoExchange.class).update(l);
	}


	/**
	 * 
	 * updateSetAutoExchange:<br/>
	 * 插入自动兑换订购关系Life_User_Auto_Exchange.<br/>
	 * 插入兑换资源表Life_Resource_List.<br/>
	 * 修改自动兑换订购关系Life_Resource_List.<br/>
	 * 插入自动兑换设置处理历史表Lift_Trade_Auto_Exchange_Set_his.<br/>
	 * 
	 * 
	 * @param lifeUserAutoExchangeCompositeList
	 *            @
	 */
	public void updateSetAutoExchange(final List<LifeUserAutoExchangeComposite> updateAutoExchange,
			final List<LifeUserAutoExchange> deleteAutoExchangel, final LogTradeHis logTradeHis,
			final List<LogTradeAutoExchangeSetHis> set) {
		// 优先删除
		for (LifeUserAutoExchange life : deleteAutoExchangel) {
			uLifeUserAutoExchange(life);
		}

		// 新增
		for (LifeUserAutoExchangeComposite composite : updateAutoExchange) {
			LifeUserAutoExchange autoExchange = composite.getLifeUserAutoExchange();
			iLifeUserAutoExchange(autoExchange);
			for (LifeResourceList life : composite.getLifeResourceList()) {
				iLifeResourceList(life);
			}
		}
		iLogTradeHis(logTradeHis);
		batchLogTradeAutoExchangeSetHis(set);

	}

	/**
	 * 
	 * serviceTradeExchange:<br/>
	 * 更新info_pay_balance.<br/>
	 * 插入兑换处理历史表log_trade_exchange_his.<br/>
	 * 插入交易登记处理历史表log_trade_his.<br/>
	 * 插入异动表balance_access_log
	 * 
	 * @param ib
	 * @param his
	 * @param log
	 *            @
	 */
	public void updateTradeExchange(final List<InfoPayBalance> ib, final LogTradeHis his,
			final List<LogTradeExchangeHis> log, final List<BalanceAccessLog> access,String currDate
			,ExchangeRatableHistory exchangeRatable,boolean isLEGOU) throws Exception {
		
		//增加判断余额
				String userId = his.getUser_id();
				List<InfoPayBalance> infoPayBalanceList_0 = getInfoPayBalance(userId,
						"0", currDate);
				List<InfoPayBalance> infoPayBalanceList_3 = getInfoPayBalance(userId,
						"3", currDate);
				for(InfoPayBalance tmpipb_0:infoPayBalanceList_0){
					for(InfoPayBalance tmpipb_3:infoPayBalanceList_3){
					if(tmpipb_0.getBalance()+tmpipb_3.getBalance() <0){
						throw new  Exception("B值余额不足");
						}
					}
				}
		
		batchInfoPayBalanceByBalanceIdBatch(ib);
		iLogTradeHis(his);
		batchLogTradeExchangeHis(log);
		batchBalanceAccessLog(access);
		if(isLEGOU){
		if(exchangeRatable.getUser_id()==null){
			ExchangeRatableHistory ratableHistory=new ExchangeRatableHistory();
			String accMonth=currDate.substring(0,6);
			ratableHistory.setAcct_month(accMonth);
			ratableHistory.setRatable_balance(-his.getBalance());
			ratableHistory.setUpdate_time(currDate);
			ratableHistory.setUser_id(userId);
			batchCreatExchangeRatableHistory(ratableHistory);	
		}else{
			batchExchangeRatableHistory(exchangeRatable);
		}
		}
		

	}
	public void batchCreatExchangeRatableHistory(final ExchangeRatableHistory history){
		S.get(ExchangeRatableHistory.class).create(history);
	}
	public void batchExchangeRatableHistory(final ExchangeRatableHistory history){
		S.get(ExchangeRatableHistory.class).update(history);
	}
	/**
	 * 
	 * getInfoUserExternalAccount:验证jdpin和手机号码关联性.<br/>
	 * 
	 * @param userId
	 * @return
	 *         @
	 */
	public InfoUserExternalAccount getInfoUserExternalAccount(final String userId,
			final String currDate) {
		InfoUserExternalAccount info = S.get(InfoUserExternalAccount.class).queryFirst(
				Condition.build("byUserid").filter("user_id", userId).filter("currDate", currDate));
		return info;
	}

	/**
	 * 
	 * hasExistedLifeUserAutoExchange:判断在用或预约是否已经存在.<br/>
	 * 
	 * @param userId
	 * @param effDate
	 * @return
	 */
	public boolean hasExistedLifeUserAutoExchange(final String userId, final String effDate) {
		List<LifeUserAutoExchange> ret = S.get(LifeUserAutoExchange.class).query(
				Condition.build("queryByTime").filter("userId", userId).filter("effDate", effDate));
		return ret.isEmpty() ? false : true;
	}

	public List<LogTradeSku> getLogTradeSkuByTradeId(final String userId, final String tradeId) {
		List<LogTradeSku> ret = S.get(LogTradeSku.class).query(
				Condition.build("byTradeId").filter("user_id", userId).filter("trade_id", tradeId));

		return ret;
	}

	/**
	 * 
	 * hasOldLifeUserAutoExchange查询原有的自动兑换设置.<br/>
	 * 
	 * @param userId
	 * @param currDate
	 * @return
	 */
	public LifeUserAutoExchange hasOldLifeUserAutoExchange(final String userId, final String currDate) {
		LifeUserAutoExchange ret = S.get(LifeUserAutoExchange.class).queryFirst(
				Condition.build("queryByTime").filter("userId", userId).filter("effDate", currDate));
		return ret;
	}
	
	/**
	 * 
	 * newasExistedLifeUserAutoExchange查询新增的自动兑换设置.<br/>
	 * 
	 * @param userId
	 * @param currDate
	 * @return
	 */
	public LifeUserAutoExchange newasExistedLifeUserAutoExchange(final String userId, final String currentDate) {
		LifeUserAutoExchange ret = S.get(LifeUserAutoExchange.class).queryFirst(
				Condition.build("queryByTime2").filter("userId", userId).filter("effDate", currentDate));
		return ret;
	}

	/**
	 * 
	 * queryByUserId查询所有的自动兑换设置.<br/>
	 * 
	 * @param userId
	 * @param currDate
	 * @return
	 */
	public List<LifeUserAutoExchange> queryByUserId(String userId,String currDate){
		List<LifeUserAutoExchange> ret=S.get(LifeUserAutoExchange.class).query(Condition.build("queryByUserId").filter("userId", userId).filter("currDate", currDate));
		return ret;
	}


	/**
	 * 
	 * appointmentHasExistedLifeUserAutoExchange:查询新增设置.<br/>
	 * 
	 * @param userId
	 * @param exchageId
	 * @return
	 */
	
	public LifeUserAutoExchange appointmentHasExistedLifeUserAutoExchange(final String userId, final String exchageId,String currDate) {
		LifeUserAutoExchange ret = S.get(LifeUserAutoExchange.class).queryFirst(
				Condition.build("queryByExchangeId").filter("userId", userId).filter("exchageId", exchageId).filter("currDate", currDate));
		return ret;
	}


	/**
	 * 
	 * updateSkuProcessDeposit:.<br/>
	 * 根据条件判断是否更新还是新增操作
	 * 
	 * @param infoPayBalance
	 * @param logRatableHistory
	 * @param logTradeSkuHis
	 * @param logTradeSku
	 * @param access
	 */
	public void updateSkuProcessDepositType_0(final int insertOrUpdate, final boolean hisNotFound,
			final InfoPayBalance infoPayBalance, final LogRatableHistory logRatableHistory,
			final BalanceAccessLog access) {
		if (insertOrUpdate == 0)
			iInfoPayBalance(infoPayBalance);
		else if (insertOrUpdate == 1)
			uInfoPayBalanceByBalanceId(infoPayBalance);
		else
			uInfoPayBalanceForExpDate(infoPayBalance);

		if (hisNotFound)
			iLogRatableHistory(logRatableHistory);
		else
			uLogRatableHistory(logRatableHistory);
//		iLogTradeSkuHis(logTradeSkuHis);
//		dLogTradeSku(logTradeSku);
		iBalanceAccessLog(access);
	}

	public void updateSkuProcessDepositType_2(final int payInsertOrUpdateType_0,
			final boolean payInsertOrUpdateType_2, final boolean hisNotFound,
			final InfoPayBalance payType_0, final InfoPayBalance payType_2,
			final LogRatableHistory logRatableHistory, final List<BalanceAccessLog> accesses) {
		if (payInsertOrUpdateType_0 == 0)
			iInfoPayBalance(payType_0);
		else if (payInsertOrUpdateType_0 == 1)
			uInfoPayBalanceByBalanceId(payType_0);
		else
			uInfoPayBalanceForExpDate(payType_0);

		if (payInsertOrUpdateType_2) {
			iInfoPayBalance(payType_2);
		} else {
			uInfoPayBalanceByBalanceId(payType_2);
		}
		if (hisNotFound)
			iLogRatableHistory(logRatableHistory);
		else
			uLogRatableHistory(logRatableHistory);
//		iLogTradeSkuHis(logTradeSkuHis);
//		dLogTradeSku(logTradeSku);
		batchBalanceAccessLog(accesses);
	}

	/**
	 * 
	 * updateSkuProcessRefund:.<br/>
	 * 根据条件判断是否更新还是新增操作
	 * 
	 * @param payNotFound
	 * @param ratableNotFound
	 * @param infoPayBalances
	 * @param balanceAccessLogs
	 * @param logTradeSkuHis
	 * @param logTradeSku
	 */
	public void updateSkuProcessRefund(final int balanceInsertOrUpdate,final boolean ratableNotFound,
			final List<InfoPayBalance> infoPayBalances,
			final LogRatableHistory logRatableHistory,
			final List<BalanceAccessLog> balanceAccessLogs, 
			List<InfoPayBalance> insertinfoPayBalances) {
		if (balanceInsertOrUpdate == 0)
			iInfoPayBalance(infoPayBalances.get(0));
		else {
			for (InfoPayBalance pay : infoPayBalances) {
				if (pay.getBalance() > 0) {
					uInfoPayBalanceByBalanceId(pay);
				} else {
					uInfoPayBalanceForExpDate(pay);
				}
			}
		}

		if (ratableNotFound)
			iLogRatableHistory(logRatableHistory);
		else
			uLogRatableHistory(logRatableHistory);

		batchBalanceAccessLog(balanceAccessLogs);
//		iLogTradeSkuHis(logTradeSkuHis);
//		dLogTradeSku(logTradeSku);
		
		for(InfoPayBalance ipb:insertinfoPayBalances){
			iIntoInfoPayBalance(ipb);
		}

	}

	/**
	 * 
	 * updateSkuProcessForLogTrade:删除log_trade，移动到his.<br/>
	 * 
	 * @param logTrade
	 */
	public void updateSkuProcessForLogTrade(final LogTrade logTrade, final LogTradeHis logHis) {
		S.get(LogTrade.class).batch(
				Condition.build("deleteByTradeId").filter("user_id", logTrade.getUser_id())
						.filter("trade_id", logTrade.getTrade_id()), logTrade);
		iLogTradeHis(logHis);
	}

	/**
	 * 
	 * updateSkuProcessForSkuToHis:sku to his.<br/>
	 * 
	 * @param logTradeSku
	 * @param logTradeSkuHis
	 */
	public void updateSkuProcessForSkuToHis(final LogTradeSku logTradeSku,
			final LogTradeSkuHis logTradeSkuHis) {
		this.dLogTradeSku(logTradeSku);
		this.iLogTradeSkuHis(logTradeSkuHis);
	}

	/**
	 * 
	 * updateSkuProcessForLogTradeUserNoRel:未关联用户不赠送，迁移trade/his, sku/his.<br/>
	 * 
	 * @param logTrade
	 * @param logHis
	 * @param logTradeSkus
	 * @param logTradeSkuHiss
	 */
	public void updateSkuProcessForLogTradeUserNoRel(final LogTrade logTrade,
			final LogTradeHis logHis, final List<LogTradeSku> logTradeSkus,
			final List<LogTradeSkuHis> logTradeSkuHiss) {
		S.get(LogTrade.class).batch(
				Condition.build("deleteByTradeId").filter("user_id", logTrade.getUser_id())
						.filter("trade_id", logTrade.getTrade_id()), logTrade);
		iLogTradeHis(logHis);

		for (LogTradeSku s : logTradeSkus) {
			this.dLogTradeSku(s);
		}

		for (LogTradeSkuHis s : logTradeSkuHiss) {
			this.iLogTradeSkuHis(s);
		}
	}

	/**
	 * 
	 * updateInfoPayBalance:新建账本表.<br/>
	 * 
	 * @param t
	 */
	public void updateInfoPayBalance(final InfoPayBalance t) {
		iInfoPayBalance(t);
	}

	/**
	 * 
	 * getOrgOrderCompleteTime:查询原始订单号完成时间.<br/>
	 * 
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	public String getOrgOrderCompleteTime(final String userId, final String orderNo,
			final String tradeTypeCodes) {
		// 先查LogTrade
		List<LogTradeHis> logTradeHis = getLogTradeHis(userId, orderNo, tradeTypeCodes);
		if (logTradeHis.isEmpty()) {
			// 先查LogTradeHis
			List<LogTrade> logTrade = getLogTrade(userId, orderNo, tradeTypeCodes);
			if (!logTrade.isEmpty()) {
				return logTrade.get(0).getOrder_completion_time();
			}
		} else {
			return logTradeHis.get(0).getOrder_completion_time();
		}

		return null;
	}

	/**
	 * **************开户***********************************
	 */

	// 实时调用时插createuserhis表
	private void iIntoLogTradeCreateUserHis(LogTradeCreateUserHis logTradeCreateUserHis) {
		S.get(LogTradeCreateUserHis.class).create(logTradeCreateUserHis);

	}

	// 实时调用时插logtradehis表
	private void iIntoLogTradeHis(LogTradeHis logTradeHis) {
		S.get(LogTradeHis.class).create(logTradeHis);
	}

	private void iIntoInfoPayBalance(InfoPayBalance infoPayBalance) {
		S.get(InfoPayBalance.class).create(infoPayBalance);
	}

	private void iIntoInfoUser(InfoUser infoUser) {
		S.get(InfoUser.class).create(infoUser);
	}

	/**
	 * @param jdPin
	 * @param channelType
	 * @param createTime
	 *            YYYYMMDDHH24MISS
	 * @param logTrade
	 *            后台处理需要传，其他实时调用不需要传
	 * @throws Exception
	 */
	public int updateOpenInfoUser(String jdPin, String channelType, String createTime) {

		String userId = Common.md5(jdPin);
		String effDate = "";
		String expDate = "";
		log.debug("Open--userId==>" + userId);
		int partitionId = Integer.parseInt(createTime.substring(4, 6));

		// 插用户表
		InfoUser infoUser = new InfoUser();
		infoUser.setCreate_channel(channelType);
		infoUser.setCreate_date(createTime);
		infoUser.setJd_pin(jdPin);
		infoUser.setUser_id(userId);
		// infoUser.setUser_level(0);
		iIntoInfoUser(infoUser);

		// 根据开户时间获取账本生失效时间
		// effdate 6月30日（包含）前为当年1月1日 00:00:00 7月1日（包含）后为次年7月1日00:00:00
		// expdate 6月30日（包含）前为当年12月31日 23:59:59 7月1日（包含）后为次年6月30日 23:59:59
		String createYear = createTime.substring(0, 4);
		log.debug("Open--createDate==>" + createYear);

		if (createTime.compareTo(createYear + "0701000000") >= 0) {
			effDate = createYear + "0701000000";
			expDate = (Integer.parseInt(createYear) + 1) + "0630235959";
		} else {
			effDate = createYear + "0101000000";
			expDate = createYear + "1231235959";
		}

		log.debug("Open--generated effDate ==>" + effDate + " and expDate==>" + expDate);
		String balanceId = Common.getUUID();
		InfoPayBalance infoPayBalance = new InfoPayBalance();
		infoPayBalance.setBalance(0L);
		infoPayBalance.setBalance_id(balanceId);
		infoPayBalance.setBalance_type_id(0);
		infoPayBalance.setEff_date(effDate);
		infoPayBalance.setExp_date(expDate);
		infoPayBalance.setUser_id(userId);
		iIntoInfoPayBalance(infoPayBalance);

		// 如果是后台处理，会传入tradeid，此时不需要再获得tradeId
		String tradeId = Common.getUUID();
		log.debug("Open--partitionId==>" + partitionId);
		// needToUpdateLogTable = true;
		log.debug("Open--tradeId==>" + tradeId);
		LogTradeHis logTradeHis = new LogTradeHis();
		logTradeHis.setTrade_id(tradeId);
		logTradeHis.setTrade_type_code("501");
		logTradeHis.setExternal_system_code("10000");
		logTradeHis.setUser_id(userId);
		logTradeHis.setChannel_type(channelType);
		logTradeHis.setPartition_id(partitionId);
		logTradeHis.setProcess_tag(0L);
		logTradeHis.setTrade_time(createTime);
		logTradeHis.setRemark("实时创建用户");
		logTradeHis.setOrder_completion_time(createTime);
		iIntoLogTradeHis(logTradeHis);

		LogTradeCreateUserHis logTradeCreateUserHis = new LogTradeCreateUserHis();
		logTradeCreateUserHis.setJd_pin(jdPin);
		logTradeCreateUserHis.setPartition_id(partitionId);
		logTradeCreateUserHis.setProcess_tag(0);
		logTradeCreateUserHis.setTrade_id(tradeId);
		logTradeCreateUserHis.setUser_id(userId);
		iIntoLogTradeCreateUserHis(logTradeCreateUserHis);

		return 0;
		// over
	}

	/**
	 * 
	 * ifAlreadyExisted:判断用户是否开户.<br/>
	 * 
	 * @param userId
	 * @return
	 */
	public boolean ifAlreadyExisted(String userId) {

		log.debug("Open--exist check userid==>" + userId);

		List<InfoUser> listinfoUser = S.get(InfoUser.class).query(
				Condition.build("queryInfoUserByUserId").filter("userId", userId));
		if (listinfoUser.size() > 0) {
			log.debug("Open--userid" + userId + "is already existed!!");
			return true;
		}

		return false;
	}

	// insert LogAutoExchangeLog
	public void iLogAutoExchangeLog(final LogAutoExchangeLog t) {
		S.get(LogAutoExchangeLog.class).create(t);
	}

	// 事务
	public void updateLogAutoExchangeLog(final LogAutoExchangeLog t) {
		iLogAutoExchangeLog(t);
	}

	/**
	 * 
	 * notfoundLogAutoExchangeLog:处理自动兑换，查重用.<br/>
	 * 
	 * @param userId
	 * @param exchangeId
	 * @param cycleId
	 * @return
	 */
	public boolean notfoundLogAutoExchangeLog(final String userId, final String exchangeId,
			final String cycleId) {

		return S.get(LogAutoExchangeLog.class)
				.query(Condition.build("xGetByExchangeId").filter("userId", userId)
						.filter("exchangeId", exchangeId).filter("cycleId", cycleId)).isEmpty() ? false
				: true;
	}

	// 自动兑换LogTradeAutoExchangeHis
	public void iLogTradeAutoExchangeHis(final LogTradeAutoExchangeHis t) {
		S.get(LogTradeAutoExchangeHis.class).create(t);
	}

	// 批量insert
	public void batchILogTradeAutoExchangeHis(final List<LogTradeAutoExchangeHis> t) {
		for (LogTradeAutoExchangeHis l : t) {
			iLogTradeAutoExchangeHis(l);
		}
	}

	/**
	 * 
	 * updateAutoProcess:自动兑换设置后台.<br/>
	 *
	 */
	public void updateAutoProcess(final LogTradeHis logTradeHis,
			List<LogTradeAutoExchangeHis> logTradeAutoExchangeHises,
			List<InfoPayBalance> infoPayBalances, List<BalanceAccessLog> balanceAccessLogs,
			LogAutoExchangeLog logAutoExchangeLog,String currDate,boolean isLEGOU,ExchangeRatableHistory exchangeRatable) {

		this.iLogTradeHis(logTradeHis);
		long ratableBalance=-logTradeHis.getBalance();
		String userId=logTradeHis.getUser_id();
		this.batchILogTradeAutoExchangeHis(logTradeAutoExchangeHises);
		this.batchInfoPayBalanceByBalanceIdBatch(infoPayBalances);
		this.batchBalanceAccessLog(balanceAccessLogs);
		
		iLogAutoExchangeLog(logAutoExchangeLog);
		if(isLEGOU){
			if(exchangeRatable.getUser_id()==null){
				ExchangeRatableHistory ratableHistory=new ExchangeRatableHistory();
				String accMonth=currDate.substring(0,6);
				ratableHistory.setAcct_month(accMonth);
				ratableHistory.setRatable_balance(ratableBalance);
				ratableHistory.setUpdate_time(currDate);
				ratableHistory.setUser_id(userId);
			batchCreatExchangeRatableHistory(ratableHistory);	
			}else{
				batchExchangeRatableHistory(exchangeRatable);
			}
			}
		
		//写oracle日志表，用于发短信
		/*
		for(LogTradeAutoExchangeHis tmphis: logTradeAutoExchangeHises){
			LogBSmsZddh logzddh = new LogBSmsZddh();
			
			long costBvalue = Long.parseLong(tmphis.getResource_value());
			
			logzddh.setMsisdn(msisdn);
			logzddh.setBvalue(costBvalue);
			logzddh.setAcctmonth("");
			logzddh.setRestype(tmphis.getResource_type_code());
			logzddh.setResvalue(Long.parseLong(tmphis.getResource_value()));
			
			S.get(LogBSmsZddh.class).create(logzddh);
		}*/
	}

	/*
	 * 短信发送
	 */

	public void insertExchangeMessage(TbSmsSendHis his) {

		S.get(TbSmsSendHis.class).create(his);
	}

	public void insertExchangeMessage(HlpSmsSend his) {

		S.get(HlpSmsSend.class).create(his);
	}
	
	//add zxm
	public void insertAutoExchangeMessage(AutoExchangeSmsSend his) {

		S.get(AutoExchangeSmsSend.class).create(his);
	}
	/**
	 * 
	 * updateSkuProcessDepositTypeNotRel_2:未关联. <br/>
	 * 
	 * @param payInsertOrUpdateType_0
	 * @param payInsertOrUpdateType_2
	 * @param hisNotFound
	 * @param payType_0
	 * @param payType_2
	 * @param logRatableHistory
	 * @param logTradeSkuHis
	 * @param logTradeSku
	 * @param accesses
	 */
	public void updateSkuProcessDepositTypeNotRel_2(final boolean payInsertOrUpdateType_2,
			final InfoPayBalance payType_2, final LogTradeSkuHis logTradeSkuHis,
			final LogTradeSku logTradeSku, final List<BalanceAccessLog> accesses) {

		if (payInsertOrUpdateType_2) {
			iInfoPayBalance(payType_2);
		} else {
			uInfoPayBalanceByBalanceId(payType_2);
		}
		iLogTradeSkuHis(logTradeSkuHis);
		dLogTradeSku(logTradeSku);
		batchBalanceAccessLog(accesses);
	}

	public void updateSkuProcessDepositTypeNotRel(final int payInsertOrUpdateType_0,
			final boolean payInsertOrUpdateType_2, final InfoPayBalance payType_0,
			final InfoPayBalance payType_2, final LogTradeSkuHis logTradeSkuHis,
			final LogTradeSku logTradeSku, final List<BalanceAccessLog> accesses) {
		if (payInsertOrUpdateType_0 == 0)
			iInfoPayBalance(payType_0);
		else if (payInsertOrUpdateType_0 == 1)
			uInfoPayBalanceByBalanceId(payType_0);
		else
			uInfoPayBalanceForExpDate(payType_0);

		if (payInsertOrUpdateType_2) {
			iInfoPayBalance(payType_2);
		} else {
			uInfoPayBalanceByBalanceId(payType_2);
		}
		iLogTradeSkuHis(logTradeSkuHis);
		dLogTradeSku(logTradeSku);
		batchBalanceAccessLog(accesses);
	}

	public void updateSkuProcessRefundNotRel(final int balanceInsertOrUpdate,
			final boolean ratableNotFound, final List<InfoPayBalance> infoPayBalances,
			final List<BalanceAccessLog> balanceAccessLogs, final LogTradeSkuHis logTradeSkuHis,
			final LogTradeSku logTradeSku,List<InfoPayBalance> insertInfoPayBalanceList) {
		if (balanceInsertOrUpdate == 0)
			iInfoPayBalance(infoPayBalances.get(0));
		else {
			for (InfoPayBalance pay : infoPayBalances) {
				
				if(pay.getBalance()==0){
					continue;
				}
				
				if (pay.getBalance() > 0) {
					uInfoPayBalanceByBalanceId(pay);
				} else {
					uInfoPayBalanceForExpDate(pay);
				}
			}
		}

		batchBalanceAccessLog(balanceAccessLogs);
		iLogTradeSkuHis(logTradeSkuHis);
		dLogTradeSku(logTradeSku);
		
		for(InfoPayBalance ipb:insertInfoPayBalanceList){
			iIntoInfoPayBalance(ipb);
		}

	}
	
	public List<InfoUserExternalAccountAttr> getExternalAccountAttrbyUserIdAndExternal(String userId,String externalId){
		
		return S.get(InfoUserExternalAccountAttr.class).query(Condition.build("getExternalAccountAttrbyUserIdAndExternal").filter("user_id", userId).filter("external_account_id", externalId));
	}
	
	public void updateLogTradeProcessTag(LogTrade logTrade) {
		
		
		S.get(LogTrade.class).update(logTrade);

	}
	
	
	public ExchangeRatableHistory getExchangeRatableHistory(final String userId,final String acctMonth){
		ExchangeRatableHistory exchangeRatable=S.get(ExchangeRatableHistory.class).queryFirst(
				Condition.build("getExchangeRatableHistory").filter("user_id", userId).filter("acct_month",acctMonth));
				return exchangeRatable;
	}
//	public void updateInfoPayBalance(final List<InfoPayBalance> ipb){
//		batchInfoPayBalanceByBalanceIdBatch(ipb);
//	 }
	
	
	//新sku内容
	public List<InfoPayBalanceSku> getInfoPayBalanceSkuByUserId_0(String userid,String orgorderid){
		return S.get(InfoPayBalanceSku.class).query(Condition.build("getInfoPayBalanceSkuByUserId0").filter("user_id",userid).filter("org_order_id",orgorderid));
	}
	
	public List<InfoPayBalanceSku> getInfoPayBalanceSkuByUserId_1(String userid,String orgorderid){
		return S.get(InfoPayBalanceSku.class).query(Condition.build("getInfoPayBalanceSkuByUserId2").filter("user_id",userid).filter("org_order_id",orgorderid));
	}
	
	//新sku赠
	public void updateSkuProcessDepositType(InfoPayBalanceSku infoPayBalanceSku,int infoPayBlanceSkuInsertFlag,LogTradeSkuHis logTradeSkuHis,LogTradeSku logTradeSku){
		if (infoPayBlanceSkuInsertFlag==0) {
			//更新
			uInfoPayBalanceSku(infoPayBalanceSku);
		}else if (infoPayBlanceSkuInsertFlag==1) {
			//插入
			iInfoPayBalanceSku(infoPayBalanceSku);
		} 
		iLogTradeSkuHis(logTradeSkuHis);
		dLogTradeSku(logTradeSku);
		
	}
	
	//新sku退
	public void updateSkuProcessRefundType( int infoPayBlanceSkuInsertFlag, 
			InfoPayBalanceSku infoPayBalanceSku,
			LogTradeSkuHis logTradeSkuHis,
			 LogTradeSku logTradeSku) {
		if (infoPayBlanceSkuInsertFlag == 0){
			uInfoPayBalanceSku(infoPayBalanceSku);
		}
		else if(infoPayBlanceSkuInsertFlag == 1) {
			iInfoPayBalanceSku(infoPayBalanceSku);
		}

		iLogTradeSkuHis(logTradeSkuHis);
		dLogTradeSku(logTradeSku);
	}
	
	//更新sku临时表
	public void uInfoPayBalanceSku(InfoPayBalanceSku infoPayBalanceSku){
		S.get(InfoPayBalanceSku.class).update(infoPayBalanceSku);
	}
	
	//插入sku临时表
	public void iInfoPayBalanceSku(InfoPayBalanceSku infoPayBalanceSku){
		S.get(InfoPayBalanceSku.class).create(infoPayBalanceSku);
	}
	
	
	
	
	//sku月底合并账本
	public List<InfoPayBalanceSku> getUserIdInInfoPayBalanceSkubyMode(int channel,int remainder){
		return S.get(InfoPayBalanceSku.class).query(Condition.build("getUserIdInInfoPayBalanceSkubyMode").filter("channel",channel).filter("remainder",remainder));
	}
	
	public List<InfoPayBalanceSku> getIfpSkuByUserId(String user_id){
		return S.get(InfoPayBalanceSku.class).query(Condition.build("getIfpSkuByUserId").filter("user_id",user_id));
	}
	
	public void updateInfoPayBalanceSkuTag(List<InfoPayBalanceSku> infoPayBalanceSkus,int processTag){
		for(InfoPayBalanceSku infoPayBalanceSku:infoPayBalanceSkus){
			infoPayBalanceSku.setProcess_tag(processTag);
			S.get(InfoPayBalanceSku.class).batch(
					Condition.build("updateProcessTag").filter("user_id", infoPayBalanceSku.getUser_id())
					.filter("processTag", processTag));
			break;
		}
	}
	
}