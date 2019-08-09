package com.tydic.beijing.bvalue.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.beijing.bvalue.dao.LogTrade;
import com.tydic.beijing.bvalue.dao.LogTradeCreateUser;
import com.tydic.beijing.bvalue.dao.LogTradeCreateUserHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 开户
 * 
 * @author zhanghengbo
 * 
 */
public class InfoUserDto {

	private static Logger log = Logger.getLogger(InfoUserDto.class);

	/**
	 * @param jdPin
	 * @param channelType
	 * @param createTime
	 *            YYYYMMDDHH24MISS
	 * @param logTrade
	 *            后台处理需要传，其他实时调用不需要传
	 * @throws Exception
	 */
	public void createInfoUser(String jdPin, String channelType, String createTime,
			LogTrade logTrade) throws Exception {

		String userId = Common.md5(jdPin);
		String effDate = "";
		String expDate = "";
		// boolean needToUpdateLogTable = false;
		// log.debug("userId==>"+userId);
		int partitionId = Integer.parseInt(createTime.substring(4, 6));
		// long starttime =System.currentTimeMillis();

		// 先判断用户是否已存在，如果存在，直接返回
		if (ifAlreadyExisted(userId)) {
			return;
		}

		long chktime = System.currentTimeMillis();
		// log.debug(userId+"校验是否开户耗时"+(chktime-starttime));

		// 插用户表
		InfoUser infoUser = new InfoUser();
		infoUser.setCreate_channel(channelType);
		infoUser.setCreate_date(createTime);
		infoUser.setJd_pin(jdPin);
		infoUser.setUser_id(userId);
		// infoUser.setUser_level(0);
		insertIntoInfoUser(infoUser);

		// long infousertime =System.currentTimeMillis();
		// log.debug(userId+"开户耗时"+(infousertime-chktime));

		// 根据开户时间获取账本生失效时间
		// effdate 6月30日（包含）前为当年1月1日 00:00:00 7月1日（包含）后为次年7月1日00:00:00
		// expdate 6月30日（包含）前为当年12月31日 23:59:59 7月1日（包含）后为次年6月30日 23:59:59
		String createYear = createTime.substring(0, 4);
		// log.debug("createDate==>"+createYear);

		if (createTime.compareTo(createYear + "0701000000") >= 0) {
			effDate = createYear + "0701000000";
			expDate = (Integer.parseInt(createYear) + 1) + "0630235959";
		} else {
			effDate = createYear + "0101000000";
			expDate = createYear + "1231235959";
		}

		// log.debug("generated effDate ==>"+effDate+" and expDate==>"+expDate);
		// String balanceId = Common.getUUID();
		// InfoPayBalance infoPayBalance = new InfoPayBalance();
		// infoPayBalance.setBalance(0L);
		// infoPayBalance.setBalance_id(balanceId);
		// infoPayBalance.setBalance_type_id(0);
		// infoPayBalance.setEff_date(effDate);
		// infoPayBalance.setExp_date(expDate);
		// infoPayBalance.setUser_id(userId);
		// insertIntoInfoPayBalance(infoPayBalance);

		// long ipbtime =System.currentTimeMillis();
		// log.debug(userId+"账本耗时"+(infousertime-ipbtime));

		// 如果是后台处理，会传入tradeid，此时不需要再获得tradeId
		if (logTrade.getTrade_id() == null || logTrade.getTrade_id().length() == 0) { // 如果是购物赠等实时调用
			String tradeId = Common.getUUID();

			// log.debug("partitionId==>"+ partitionId);
			// needToUpdateLogTable = true;
			// log.debug("tradeId==>"+tradeId);
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
			logTradeHis.setOrder_completion_time(new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date()));
			insertIntoLogTradeHis(logTradeHis);

			// long tradehistime =System.currentTimeMillis();
			// log.debug(userId+"订单历史表耗时"+(infousertime-ipbtime));

			LogTradeCreateUserHis logTradeCreateUserHis = new LogTradeCreateUserHis();
			logTradeCreateUserHis.setJd_pin(jdPin);
			logTradeCreateUserHis.setPartition_id(partitionId);
			logTradeCreateUserHis.setProcess_tag(0);
			logTradeCreateUserHis.setTrade_id(tradeId);
			logTradeCreateUserHis.setUser_id(userId);
			insertIntoLogTradeCreateUserHis(logTradeCreateUserHis);

			// long createhistime =System.currentTimeMillis();
			// log.debug(userId+"开户历史表耗时"+(infousertime-createhistime));

		} else { // 如果是后台处理调用
		// log.debug("logTrade.tradeId==>"+logTrade.getTrade_id());
		// LogTradeHis logTradeHis = new LogTradeHis();
		// logTradeHis.setTrade_id(logTrade.getTrade_id());
		// logTradeHis.setTrade_type_code(logTrade.getTrade_type_code());
		// logTradeHis.setExternal_system_code(logTrade.getExternal_system_code());
		// logTradeHis.setUser_id(logTrade.getUser_id());
		// logTradeHis.setChannel_type(logTrade.getChannel_type());
		// logTradeHis.setPartition_id(logTrade.getPartition_id());
		// logTradeHis.setProcess_tag(logTrade.getProcess_tag());
		// logTradeHis.setTrade_time(logTrade.getTrade_time());
		// logTradeHis.setOrder_completion_time(new
		// SimpleDateFormat("YYYYMMDDHHmmss").format(new Date()));
		// insertIntoLogTradeHis(logTradeHis);
		//
		// LogTradeCreateUser logTradeCreateUser =
		// getLogTradeCreateUserByTradeId(logTrade.getTrade_id(),logTrade.getUser_id());
		// //log.debug("getcreateuser==>"+logTradeCreateUser.getTrade_id());
		//
		// LogTradeCreateUserHis logTradeCreateUserHis = new
		// LogTradeCreateUserHis();
		// logTradeCreateUserHis.setJd_pin(logTradeCreateUser.getJd_pin());
		// logTradeCreateUserHis.setPartition_id(logTradeCreateUser.getPartition_id());
		// logTradeCreateUserHis.setProcess_tag(logTradeCreateUser.getProcess_tag());
		// logTradeCreateUserHis.setTrade_id(logTradeCreateUser.getTrade_id());
		// logTradeCreateUserHis.setUser_id(logTradeCreateUser.getUser_id());
		// insertIntoLogTradeCreateUserHis(logTradeCreateUserHis);

		}

		// over
	}

	private boolean ifAlreadyExisted(String userId) {

		// log.debug("exist check userid==>"+userId);
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("userId", userId);

		List<InfoUser> listinfoUser = S.get(InfoUser.class).query(
				Condition.build("queryInfoUserByUserId").filter(filter));
		if (listinfoUser.size() > 0) {
			log.debug("userid" + userId + "is already existed!!");
			return true;
		}

		return false;
	}

	private LogTradeCreateUser getLogTradeCreateUserByTradeId(String trade_id, String user_id) {
		return S.get(LogTradeCreateUser.class).queryFirst(
				Condition.build("getLogTradeCreateUserByTradeId").filter("trade_id", trade_id)
						.filter("user_id", user_id));
	}

	// 实时调用时插createuserhis表
	private void insertIntoLogTradeCreateUserHis(LogTradeCreateUserHis logTradeCreateUserHis) {
		S.get(LogTradeCreateUserHis.class).create(logTradeCreateUserHis);

	}

	// 实时调用时插logtradehis表
	private void insertIntoLogTradeHis(LogTradeHis logTradeHis) {
		S.get(LogTradeHis.class).create(logTradeHis);
	}

	private void insertIntoInfoPayBalance(InfoPayBalance infoPayBalance) {
		S.get(InfoPayBalance.class).create(infoPayBalance);
	}

	private void insertIntoInfoUser(InfoUser infoUser) {
		S.get(InfoUser.class).create(infoUser);
	}

}
