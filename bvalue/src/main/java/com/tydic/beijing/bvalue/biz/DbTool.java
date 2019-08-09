package com.tydic.beijing.bvalue.biz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.HlpSmsSend;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.CodeBilBalanceType;
import com.tydic.beijing.bvalue.dao.ExternalAccountAttrInfo;
import com.tydic.beijing.bvalue.dao.InfoAvailableBalance;
import com.tydic.beijing.bvalue.dao.InfoBpool;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoPayBalanceDao;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LifeResourceList;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchange;
import com.tydic.beijing.bvalue.dao.LifeUserAutoExchangeDao;
import com.tydic.beijing.bvalue.dao.LogTrade;
import com.tydic.beijing.bvalue.dao.LogTradeAdjustHis;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeAutoExchangeSetHis;
import com.tydic.beijing.bvalue.dao.LogTradeBpoolHis;
import com.tydic.beijing.bvalue.dao.LogTradeBpoolHisDtl;
import com.tydic.beijing.bvalue.dao.LogTradeCreateUser;
import com.tydic.beijing.bvalue.dao.LogTradeCreateUserHis;
import com.tydic.beijing.bvalue.dao.LogTradeExchangeHis;
import com.tydic.beijing.bvalue.dao.LogTradeExternalAccountAttrHis;
import com.tydic.beijing.bvalue.dao.LogTradeExternalAccountHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LogTradeRelChg;
import com.tydic.beijing.bvalue.dao.LogTradeRewardHis;
import com.tydic.beijing.bvalue.dao.RegistBQueryUserDao;
import com.tydic.beijing.bvalue.dao.RuleParameters;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.DataService;
import com.tydic.uda.service.S;

public class DbTool {

	private static Logger log=Logger.getLogger(DbTool.class);
	
	
	public InfoUser queryUserByPin(String pin){
		
		return S.get(InfoUser.class).queryFirst(Condition.build("queryUserByPin").filter("jd_pin", pin));
		
	}
	
	public List<InfoAvailableBalance> queryAvailableBalance(String user_id){
		
		return S.get(InfoAvailableBalance.class).query(Condition.build("queryAvailableBalance").filter("user_id",user_id));
	}
	
	
	public List<LifeUserAutoExchange> queryAutoExchangeSet(String userId ,String currentTime){
		
		return S.get(LifeUserAutoExchange.class).query(Condition.build("queryAutoExchangeSet").filter("userId", userId).filter("currentTime", currentTime));
	}
	
	public List<LifeResourceList> queryExchangeResourceList(String userId ,String resource_list_id){
		
		return S.get(LifeResourceList.class).query(Condition.build("queryExchangeResourceList").filter("userId", userId).filter("resource_list_id", resource_list_id));
	}
	
	//京东查询B
	public List<InfoBpool> getqueryInfoBPool(String bPoolId){
		return S.get(InfoBpool.class).query(Condition.build("queryBPoolId").filter("bpool_id", bPoolId));
		
	}
	
	//Boss查询B 
	//按活动名字查询
	public List<InfoBpool> getqueryInfoBPoolCRM(StringBuffer condition){
		
	return S.get(InfoBpool.class).query(Condition.build("queryInfoBPoolCRM").filter("condition",condition));
	}
	//无参输入时全部输出
	public List<InfoBpool> getAllInfoBPoolCRM( ){
		
	return S.get(InfoBpool.class).query(Condition.build("queryInfoBPoolCRM"));
	}

	public List<LogTradeHis> getLogTradeHisByUserIdAndTime(String userId,String starttime,String endtime){
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("userId", userId);
		filter.put("startTime", starttime);
		filter.put("endTime", endtime);
		return S.get(LogTradeHis.class).query(Condition.build("getLogTradeHisByUserIdAndTime").filter(filter));
	}
	public LogTradeHis getLogTradeHisByTradeIdAndUserId(String userId,String tradeId){
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("userId", userId);
		filter.put("tradeId", tradeId);
		return  S.get(LogTradeHis.class).queryFirst(Condition.build("getLogTradeHisByTradeIdAndUserId").filter(filter));
	}
	
	public List<LogTradeHis> getLogTradeHisByUserIdAndOrderNo(String userId,String orderNo){
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("userId", userId);
		filter.put("orderNo", orderNo);
		return S.get(LogTradeHis.class).query(Condition.build("getLogTradeHisByUserIdAndOrderNo").filter(filter));
	}
	
	
	

	public List<LogTradeExchangeHis> getLogTradeExchangeHisbyTradeId(
			String trade_Id,String user_Id) {
	
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("trade_id",trade_Id);
		filter.put("user_id", user_Id);
		
		return  S.get(LogTradeExchangeHis.class).query(
				Condition.build("getLogTradeExchangeHisbyTradeId").filter(filter));
	}

	public int getPageCountOfBalanceAccessLog(String userId,String accesstag,String tradetypecode,String starttime,String endtime) {
		 
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("userId", userId);
		filter.put("accessTag", accesstag);
		filter.put("tradeTypeCode", tradetypecode );
		filter.put("startTime", starttime);
		filter.put("endTime", endtime);
		return S.get(BalanceAccessLog.class).query(
				Condition.build("getPageCountOfBalanceAccessLog").filter(filter)).size();
	}
	
	
	public InfoUserExternalAccount queryPinRelation(String userId ,String currentTime){
		
		return S.get(InfoUserExternalAccount.class).queryFirst(Condition.build("queryPinRelation").filter("user_id", userId).filter("currentTime", currentTime));
		
	}
	
public InfoUserExternalAccount isRelationExists(String userId ,String currentTime){
		
		return S.get(InfoUserExternalAccount.class).queryFirst(Condition.build("isRelationExists").filter("user_id", userId).filter("currentTime", currentTime));
	}
	
	public void addLogTradeRelChg(LogTradeRelChg  logTrade){
		S.get(LogTradeRelChg.class).create(logTrade);
	}
	
	public LogTradeRelChg queryProcess(String userId){
		
		return S.get(LogTradeRelChg.class).queryFirst(Condition.build("queryProcess").filter("user_id", userId));
	}
	
	
	public void addExternalInfoUser(InfoUserExternalAccount info){
		
		S.get(InfoUserExternalAccount.class).create(info);
				
	}
	
	public void addExternalUserAttr(List<InfoUserExternalAccountAttr> attrs){
		
		for(InfoUserExternalAccountAttr iter : attrs){
			S.get(InfoUserExternalAccountAttr.class).create(iter);
		}
		
	}
	
	public void addExternalUserHis(LogTradeExternalAccountHis his){
		S.get(LogTradeExternalAccountHis.class).create(his);
		
	}
	
	
	public void addExternalUserAttrHis(List<LogTradeExternalAccountAttrHis> his){
		
		for(LogTradeExternalAccountAttrHis  iter : his){
			S.get(LogTradeExternalAccountAttrHis.class).create(iter);
		}
		
	}

	
	/**
	 * update
	 * */
	public void updateInfoBpool(InfoBpool infoBpool){
		
		S.get(InfoBpool.class).update(infoBpool);
		
	}
	public void updateLogTradeBpoolHisDtl(String tradeId,String bpoolid, String bpoolname, String departMenttype, 
			String activitytype, String effdate, String expdate, long bpool, 
			float costvalue, long threshold, long activitystatus,  String contactphone, 
			String contactemail, String note){
		
		LogTradeBpoolHisDtl logTradeBpoolHisDtl = new LogTradeBpoolHisDtl();
		logTradeBpoolHisDtl.settrade_id(tradeId);
		logTradeBpoolHisDtl.setbpool_id(bpoolid);
		logTradeBpoolHisDtl.setbpool_name(bpoolname);
		logTradeBpoolHisDtl.setdepartMent_type(departMenttype);
		logTradeBpoolHisDtl.setactivity_type(activitytype);
		logTradeBpoolHisDtl.seteff_date(effdate);
		logTradeBpoolHisDtl.setexp_date(expdate);
		logTradeBpoolHisDtl.setbpool(bpool);
		logTradeBpoolHisDtl.setcost_value(costvalue);
		logTradeBpoolHisDtl.setthreshold(threshold);
		logTradeBpoolHisDtl.setactivity_status(activitystatus);
		logTradeBpoolHisDtl.setcontact_phone(contactphone);
		logTradeBpoolHisDtl.setcontact_email(contactemail);
		logTradeBpoolHisDtl.setnote(note);
		S.get(LogTradeBpoolHisDtl.class).update(logTradeBpoolHisDtl);
		
	}
	
//	public void updateLogTradeBpoolHis(String trade_Id){
//		
//		LogTradeBpoolHis logTradeBpoolHis = new LogTradeBpoolHis();
//		logTradeBpoolHis.settrade_id(trade_Id);
//		S.get(LogTradeBpoolHis.class).update(logTradeBpoolHis);	
//	}
	
	
	public void updateProcessTag(long tag,String tradeId ,String msg,String userId){
		LogTradeHis his=new LogTradeHis();
		his.setProcess_tag(tag);
		his.setTrade_id(tradeId);
		his.setRemark(msg);
		his.setUser_id(userId);
		S.get(LogTradeHis.class).update(his);
	}
	
	
	public void updatePinRelation(InfoUserExternalAccount external){
		S.get(InfoUserExternalAccount.class).update(external);
		
	}
	
	
	public void updateExternalAccountAttr(InfoUserExternalAccountAttr attr){
		S.get(InfoUserExternalAccountAttr.class).update(attr);
	}
	
	public void updateExternalUserHis(LogTradeExternalAccountHis his){
		S.get(LogTradeExternalAccountHis.class).update(his);
	}
	
	
	public void updateExternalUserAttrHis(LogTradeExternalAccountAttrHis attrHis){
		S.get(LogTradeExternalAccountAttrHis.class).update(attrHis);
		
	}
	
	
	public List<CodeBilBalanceType> getCodeBilBalanceType(){
		return S.get(CodeBilBalanceType.class).query(Condition.build("getCodeBilBalanceType"));
		
	}
	
	public void registUserOnQueryB(LogTrade lt ,LogTradeCreateUser createUser){
		S.get(LogTrade.class).create(lt);
		S.get(LogTradeCreateUser.class).create(createUser);
		
	}
	
	public InfoUser queryInfoUserByUserId(String userId){
		return S.get(InfoUser.class).queryFirst(Condition.build("queryInfoUserByUserId").filter("userId",userId));
	}
	
	public List<InfoPayBalance> getValidInfoPayBalanceByUserId(String userId){
		String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		return S.get(InfoPayBalance.class).query(Condition.build("getValidInfoPayBalanceByUserId").filter("user_id", userId).filter("currentTime", currentTime));
	}
	
	

	
	
	/**
	 * create
	 * */
	public void insertInfoBpool(InfoBpool b_act){
		S.get(InfoBpool.class).create(b_act);
		
	}
	
	public void insertLogTradeBpoolHisDtl(LogTradeBpoolHisDtl b_logdtl){
		S.get(LogTradeBpoolHisDtl.class).create(b_logdtl);
		
	}
	
	public void insertLogTradeBpoolHis(LogTradeBpoolHis b_loghis){
		S.get(LogTradeBpoolHis.class).create(b_loghis);
		
	}
	
	
	public void insertLogTradeHis(LogTradeHis trade){
		S.get(LogTradeHis.class).create(trade);
		
	}
	
	
	public void updateAttrDtos(List<InfoUserExternalAccountAttr> adds ,List<ExternalAccountAttrInfo> dels, List<LifeUserAutoExchange> addexchangelist, List<LifeUserAutoExchange> delexchangelist,
			List<LifeResourceList> addlifeResourcelist, LogTradeHis trade, List<LogTradeExternalAccountAttrHis> attrhislist){
		
		for(InfoUserExternalAccountAttr iter : adds){
			S.get(InfoUserExternalAccountAttr.class).create(iter);
			
		}
		
		for(ExternalAccountAttrInfo iter : dels){
			S.get(ExternalAccountAttrInfo.class).update(iter);
			
		}
		
		//属性同步时可能会修改自动兑换设置
		for(LifeUserAutoExchange tmpluae:addexchangelist){
			S.get(LifeUserAutoExchange.class).create(tmpluae);
		}
		
		for(LifeUserAutoExchange tmpluae:delexchangelist){
			S.get(LifeUserAutoExchange.class).update(tmpluae);
		}
		
		for(LifeResourceList tmplrl:addlifeResourcelist){
			S.get(LifeResourceList.class).create(tmplrl);
		}
		
		S.get(LogTradeHis.class).create(trade);
		
		for(LogTradeExternalAccountAttrHis tmpattrhis:attrhislist){
			S.get(LogTradeExternalAccountAttrHis.class).create(tmpattrhis);
		}
		
	}
	
	
	
	public List<RegistBQueryUserDao> LogTradeRegist(int server,int amount){
		switch (server) {
		case 1:
//			return S.get(RegistBQueryUserDao.class).query(Condition.build("getRegistUser1").filter("num", amount));
			return S.get(RegistBQueryUserDao.class).page(Condition.build("getRegistUser1"), 0, amount);
		case 2:
			return S.get(RegistBQueryUserDao.class).page(Condition.build("getRegistUser2"),0,amount);
		default:
			return null;
		}
		
	}
	
	public void registAll(List<InfoUser> infoUsers,List<LogTradeHis> tradeHis,List<LogTradeCreateUserHis> createUsers){
		
		for(InfoUser iter : infoUsers){	//新增
			InfoUser created=(InfoUser)S.get(InfoUser.class).create(iter);
			System.out.println("the user u just created is >>>>>>"+created.getUser_id());
			
		}
		for(LogTradeHis iter : tradeHis){	//新增
			S.get(LogTradeHis.class).create(iter);
		}
		for(LogTradeCreateUserHis iter : createUsers){
			S.get(LogTradeCreateUserHis.class).create(iter);
		}
		
		for(LogTradeHis iter : tradeHis){	//删除
			S.get(LogTradeHis.class).batch(Condition.build("deleteByTradeId").filter("user_id", iter.getUser_id()).filter("trade_id", iter.getTrade_id()), iter);
			
			S.get(LogTradeHis.class).batch(Condition.build("deleteCreateUserByTradeId").filter("user_id", iter.getUser_id()).filter("trade_id", iter.getTrade_id()), iter);
			
		}
		
		
	}
	
	//限单条处理
	public void registErr(List<LogTradeHis> tradeHis,List<LogTradeCreateUserHis> createUsers,String msg){
		for(LogTradeHis iter : tradeHis){
			iter.setProcess_tag(3);	//失败
			iter.setRemark(msg);
			S.get(LogTradeHis.class).create(iter);
		}
		
		for(LogTradeCreateUserHis iter : createUsers){
			iter.setProcess_tag(4);
			
			S.get(LogTradeCreateUserHis.class).create(iter);
		}
		
		for(LogTradeHis iter : tradeHis){
			S.get(LogTradeHis.class).batch(Condition.build("deleteByTradeId").filter("user_id", iter.getUser_id()).filter("trade_id", iter.getTrade_id()), iter);
		}
		
		
		
	}
	
	
	public RuleParameters getRunFlag(String domain_code,String trade_type_code,String para_name){
		String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("domain_code", domain_code);
		filter.put("trade_type_code", trade_type_code);
		filter.put("para_name", para_name);
		filter.put("currentTime",currentTime);
		return S.get(RuleParameters.class).queryFirst(Condition.build("getRunFlag").filter(filter));
	}
	
	public List<RuleParameters> getServers(String domain_code,String trade_type_code,String para_name){
		String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("domain_code", domain_code);
		filter.put("trade_type_code", trade_type_code);
		filter.put("para_name", para_name);
		filter.put("currentTime",currentTime);
		return S.get(RuleParameters.class).query(Condition.build("getRunFlag").filter(filter));
		
	}
	
	public List<RuleParameters> checkServerRule(){
		return S.get(RuleParameters.class).query(Condition.build("checkServerRule"));
		
	}
	
	public List<InfoPayBalance> getInfoPayBalanceByUserId(String userId){
		
		return S.get(InfoPayBalance.class).query(Condition.build("getInfoPayBalanceByUserId").filter("user_id", userId));
	}
	
	public void updateByBalanceId(InfoPayBalance balance){
		int ret=S.get(InfoPayBalance.class).update(balance);
		log.debug("update balance return value:"+ret);
	}
	
	public void addJdpinRelation(InfoUserExternalAccount external,List<InfoUserExternalAccountAttr> attrs,
			LogTradeExternalAccountHis his,List<LogTradeExternalAccountAttrHis> attrHis ,
			LifeUserAutoExchange exchange,List<LifeResourceList> resourceList,LogTradeHis logTradeHis){
		
		S.get(InfoUserExternalAccount.class).create(external);
		for(InfoUserExternalAccountAttr iter : attrs){
			S.get(InfoUserExternalAccountAttr.class).create(iter);
		}
		
		S.get(LogTradeExternalAccountHis.class).create(his);
		
		for(LogTradeExternalAccountAttrHis  iter : attrHis){
			S.get(LogTradeExternalAccountAttrHis.class).create(iter);
		}
		// 首关联之后取消自动兑换
		/*if(exchange !=null){     
			S.get(LifeUserAutoExchange.class).create(exchange);
		}*/  
		
		
		for(LifeResourceList iter: resourceList){
			S.get(LifeResourceList.class).create(iter);
		}
		S.get(LogTradeHis.class).create(logTradeHis);
	}
	
	public void delJdpinRelation(InfoUserExternalAccount external,InfoUserExternalAccountAttr attr,LogTradeExternalAccountHis accountHis,
			List<LogTradeExternalAccountAttrHis>  attrHisList,LifeUserAutoExchangeDao exchange,LogTradeHis logTradeHis){
		S.get(InfoUserExternalAccount.class).update(external);
		S.get(InfoUserExternalAccountAttr.class).update(attr);
		S.get(LogTradeExternalAccountHis.class).update(accountHis);
//		S.get(LogTradeExternalAccountAttrHis.class).update(attrHis);
		for(LogTradeExternalAccountAttrHis iter : attrHisList){
			S.get(LogTradeExternalAccountAttrHis.class).create(iter);
		}
		S.get(LifeUserAutoExchangeDao.class).update(exchange);
		S.get(LogTradeHis.class).create(logTradeHis);
	}
	
	
	public void insertInfoPayBalance(InfoPayBalance balance){
		
		S.get(InfoPayBalance.class).create(balance);
		
	}
	
	
	public void updateAdjustBValueForAdd(LogTradeHis trade,LogTradeAdjustHis adjustHis, List<InfoPayBalance> updatebalance,List<InfoPayBalance> insertbalance, List<BalanceAccessLog> accessLog){
		S.get(LogTradeHis.class).create(trade);
		
		S.get(LogTradeAdjustHis.class).create(adjustHis);
		//InfoPayBalanceDao balanceDao=new InfoPayBalanceDao(balance);
		
		//S.get(InfoPayBalanceDao.class).update(balanceDao);
		
		//S.get(BalanceAccessLog.class).create(accessLog);
		
		for(InfoPayBalance tmpipb:updatebalance){
			//S.get(InfoPayBalance.class).batch(arg0, arg1)  //updateBalanceAndExpDate
			Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("user_id", tmpipb.getUser_id());
			filter.put("balance_id", tmpipb.getBalance_id());
			filter.put("balance", tmpipb.getBalance());
			filter.put("eff_date", tmpipb.getEff_date());
			filter.put("exp_date", tmpipb.getExp_date());
			S.get(InfoPayBalance.class).batch(Condition.build("updateBalanceAndExpDate").filter(filter));
		}
		
		for(InfoPayBalance tmpipb:insertbalance){
			S.get(InfoPayBalance.class).create(tmpipb);
		}
		
		for(BalanceAccessLog tmpbal:accessLog){
			tmpbal.setTrade_Id(trade.getTrade_id());
			tmpbal.setTrade_Type_Code(trade.getTrade_type_code());
			S.get(BalanceAccessLog.class).create(tmpbal);
		}
		
		
	}
	
	
	public void updateAdjustDataForDecrease(LogTradeHis  trade,LogTradeAdjustHis adjustHis,List<BalanceAccessLog> accessLog,
			List<InfoPayBalance> adjustedBalance){
		
		S.get(LogTradeHis.class).create(trade);
		
		S.get(LogTradeAdjustHis.class).create(adjustHis);
		
		for(BalanceAccessLog iter : accessLog){
			S.get(BalanceAccessLog.class).create(iter);
		}
		
		for(InfoPayBalance iter : adjustedBalance){
			InfoPayBalanceDao balanceDao=new InfoPayBalanceDao(iter);
			log.debug(">>>>>>>>>>>>>expdate>>>>>>:"+iter.getExp_date());
			S.get(InfoPayBalanceDao.class).update(balanceDao);
		}
		
	}
	
	public void updateAdjustDataForDecrease(LogTradeHis  trade,LogTradeAdjustHis adjustHis,List<BalanceAccessLog> accessLog){
		
		S.get(LogTradeHis.class).create(trade);
		
		S.get(LogTradeAdjustHis.class).create(adjustHis);
		
		for(BalanceAccessLog iter : accessLog){
			S.get(BalanceAccessLog.class).create(iter);
		}
		
		
		
	}
	
	
	public void insertBalanceAccessLog(BalanceAccessLog balanceAccessLog){
		S.get(BalanceAccessLog.class).create(balanceAccessLog);
	}
	
	public List<InfoUserExternalAccountAttr> getExternalAccountAttrbyUserIdAndExternal(String userId,String externalId){
		
		return S.get(InfoUserExternalAccountAttr.class).query(Condition.build("getExternalAccountAttrbyUserIdAndExternal").filter("user_id", userId).filter("external_account_id", externalId));
	}

	public List<LogTradeAutoExchangeHis> getLogTradeAutoExchangeHisbyTradeId(
			String trade_id, String user_id) {
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("trade_id",trade_id);
		filter.put("user_id", user_id);
		
		return  S.get(LogTradeAutoExchangeHis.class).query(
				Condition.build("getLogTradeAutoExchangeHisbyTradeId").filter(filter));
		 

	}

	public List<LogTradeAutoExchangeSetHis> getLogTradeAutoExchangeSetHisByTradeId(
			String trade_id, String user_id) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("trade_id",trade_id);
		filter.put("user_id", user_id);
		return  S.get(LogTradeAutoExchangeSetHis.class).query(
				Condition.build("getLogTradeAutoExchangeSetHisByTradeId").filter(filter));
	}

	public List<LifeResourceList> getLifeResourceListByResourceId(
			String resource_list_id, String user_id) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("resource_list_id",resource_list_id);
		filter.put("userId", user_id);
		return  S.get(LifeResourceList.class).query(
				Condition.build("getLifeResourceListByResourceId").filter(filter));
	}

	public List<LogTradeExternalAccountHis> getLogTradeExternalAccountHisByTradeId(
			String trade_id, String user_id) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("trade_id",trade_id);
		filter.put("user_id", user_id);
		return  S.get(LogTradeExternalAccountHis.class).query(
				Condition.build("getLogTradeExternalAccountHisByTradeId").filter(filter));
	}

	public List<LogTradeExternalAccountAttrHis> getLogTradeExternalAccountAttrHis(
			String trade_id, String user_id) {
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("trade_id",trade_id);
		filter.put("user_id", user_id);
		return  S.get(LogTradeExternalAccountAttrHis.class).query(
				Condition.build("getLogTradeExternalAccountAttrHis").filter(filter));
		
	 
	}

	public List<LogTradeAdjustHis> getLogTradeAdjustHisByTradeId(
			String trade_id, String user_id) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("trade_id",trade_id);
		filter.put("user_id", user_id);
		return  S.get(LogTradeAdjustHis.class).query(
				Condition.build("getLogTradeAdjustHisByTradeId").filter(filter));
	}

	public List<LogTradeExternalAccountAttrHis> getAttrHisbyUserIdAndExternal(
			String user_id, String trade_id) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("trade_id",trade_id);
		filter.put("user_id", user_id);
		return  S.get(LogTradeExternalAccountAttrHis.class).query(
				Condition.build("getAttrHisbyUserIdAndExternal").filter(filter));
	}

	public InfoBpool queryInfoBpoolbyId(String bpoolid) {
		return S.get(InfoBpool.class).queryFirst(Condition.build("queryBPoolId").filter("bpool_id", bpoolid));
	}
	
	public void sendMsg(HlpSmsSend his) {

		S.get(HlpSmsSend.class).create(his);
	}

	public void insertLogTradeRewardHis(LogTradeRewardHis logTradeRewardHis) {
		S.get(LogTradeRewardHis.class).create(logTradeRewardHis);
		
	}

	public  List<LogTradeRewardHis> queryLogTradeRewardHis(
			String userId, String trade_id) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("trade_id",trade_id);
		filter.put("user_id", userId);
		return S.get(LogTradeRewardHis.class).query(Condition.build("queryLogTradeRewardHis").filter(filter));
	}

	public int queryLogTradeBpoolHisbypoolandOrder(String bpoolid, String orderNo) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("order_no",orderNo);
		filter.put("bpool_id", bpoolid);
		return S.get(LogTradeBpoolHis.class).query(Condition.build("queryLogTradeBpoolHisbypoolandOrder").filter(filter)).size();
	}
	
	
	public List<InfoPayBalance> getEffectiveInfoPayBalanceByUserId(String userId,final String current){
		
		return S.get(InfoPayBalance.class).query(Condition.build("getEffectiveInfoPayBalanceByUserId").filter("user_id", userId).filter("currDate",current));
	}
	
	
	public void registUserDisengagement(InfoUserExternalAccount external,InfoUserExternalAccountAttr attr,LogTradeExternalAccountHis accountHis,
			List<LogTradeExternalAccountAttrHis>  attrHisList,LifeUserAutoExchangeDao exchange,List<InfoPayBalance> infoPayBalance,
			List<BalanceAccessLog> insertBalanceAccess,LogTradeHis tradeHis){
		for(InfoPayBalance i:infoPayBalance){
			S.get(InfoPayBalance.class).update(i);
		}
		for(BalanceAccessLog b:insertBalanceAccess){
			S.get(BalanceAccessLog.class).create(b);
		}
		for(LogTradeExternalAccountAttrHis iter : attrHisList){
			S.get(LogTradeExternalAccountAttrHis.class).create(iter);
		}
		S.get(LogTradeHis.class).create(tradeHis);
		S.get(InfoUserExternalAccount.class).update(external);
		S.get(InfoUserExternalAccountAttr.class).update(attr);
		S.get(LogTradeExternalAccountHis.class).update(accountHis);
		S.get(LifeUserAutoExchangeDao.class).update(exchange);
	}
	
	public void CreatBalanceThree(InfoPayBalance balance,LogTradeHis tradeHis,LogTradeCreateUserHis  createUserHis,
			BalanceAccessLog insertBalanceAccessLog,LifeUserAutoExchange exchange,List<LifeResourceList> resourceList){
		
		
		S.get(InfoPayBalance.class).create(balance);
		S.get(LogTradeHis.class).create(tradeHis);
		S.get(LogTradeCreateUserHis.class).create(createUserHis);
		S.get(BalanceAccessLog.class).create(insertBalanceAccessLog);
		
		if(exchange !=null){
			S.get(LifeUserAutoExchange.class).create(exchange);
		}
		for(LifeResourceList iter: resourceList){
			S.get(LifeResourceList.class).create(iter);
		}
	}
	
	
	public void updateBalanceThree(InfoPayBalance giveInfoPayBalance,LogTradeHis tradeHis,BalanceAccessLog insertBalanceAccessLog
			,LifeUserAutoExchange exchange,List<LifeResourceList> resourceList){
		S.get(InfoPayBalance.class).update(giveInfoPayBalance);
		S.get(LogTradeHis.class).create(tradeHis);
		S.get(BalanceAccessLog.class).create(insertBalanceAccessLog);
		if(exchange !=null){
			S.get(LifeUserAutoExchange.class).create(exchange);
		}
		for(LifeResourceList iter: resourceList){
			S.get(LifeResourceList.class).create(iter);
		}
	}
	
	public void creatBalanceThreeAndZero(List<InfoPayBalance> balance,LogTradeHis tradeHis,List<LogTradeCreateUserHis>  createUserHis,
			BalanceAccessLog insertBalanceAccessLog,LifeUserAutoExchange exchange,List<LifeResourceList> resourceList){
		
		for(InfoPayBalance ipb:balance){
		S.get(InfoPayBalance.class).create(ipb);
		}
		S.get(LogTradeHis.class).create(tradeHis);
		for(LogTradeCreateUserHis ltcuh:createUserHis){
		S.get(LogTradeCreateUserHis.class).create(ltcuh);
		}
		S.get(BalanceAccessLog.class).create(insertBalanceAccessLog);
		
		if(exchange !=null){
			S.get(LifeUserAutoExchange.class).create(exchange);
		}
		for(LifeResourceList iter: resourceList){
			S.get(LifeResourceList.class).create(iter);
		}
	}
	
	
//	public void registUserOnCreateRelation(InfoUser user,InfoPayBalance balance ,LogTradeHis tradeHis,LogTradeCreateUserHis  createUserHis){
//		S.get(InfoUser.class).create(user);
//		S.get(InfoPayBalance.class).create(balance);
//		S.get(LogTradeHis.class).create(tradeHis);
//		S.get(LogTradeCreateUserHis.class).create(createUserHis);
//	}
	
	public void registUserOnCreateRelation(InfoUser user,InfoPayBalance balance ,LogTradeHis tradeHis,LogTradeCreateUserHis  createUserHis){
		S.get(InfoUser.class).create(user);
		S.get(InfoPayBalance.class).create(balance);
		S.get(LogTradeHis.class).create(tradeHis);
		S.get(LogTradeCreateUserHis.class).create(createUserHis);
	}

	public void insertNewInfoUser(InfoUserExternalAccount external,List<InfoUserExternalAccountAttr> attrs,
			LogTradeExternalAccountHis his,List<LogTradeExternalAccountAttrHis> attrHis ,
			LifeUserAutoExchange exchange,List<LifeResourceList> resourceList,
			InfoUser infouser,List<InfoPayBalance> balance, LogTradeHis tradeHis,
			LogTradeCreateUserHis createUserHis,
			BalanceAccessLog insertBalanceAccessLog) {
		S.get(InfoUserExternalAccount.class).create(external);
		for(InfoUserExternalAccountAttr iter : attrs){
			S.get(InfoUserExternalAccountAttr.class).create(iter);
		}
		
		S.get(LogTradeExternalAccountHis.class).create(his);
		
		for(LogTradeExternalAccountAttrHis  iter : attrHis){
			S.get(LogTradeExternalAccountAttrHis.class).create(iter);
		}
		
		if(exchange !=null){
			S.get(LifeUserAutoExchange.class).create(exchange);
		}
		
		
		for(LifeResourceList iter: resourceList){
			S.get(LifeResourceList.class).create(iter);
		}
		S.get(InfoUser.class).create(infouser);
		for(InfoPayBalance info:balance){
		S.get(InfoPayBalance.class).create(info);
		}
		S.get(LogTradeHis.class).create(tradeHis);
		S.get(LogTradeCreateUserHis.class).create(createUserHis);
		S.get(BalanceAccessLog.class).create(insertBalanceAccessLog);
	}
	
	public void insertNoFristCreateInfoUser(InfoUserExternalAccount external,List<InfoUserExternalAccountAttr> attrs,
			LogTradeExternalAccountHis his,List<LogTradeExternalAccountAttrHis> attrHis ,
			LifeUserAutoExchange exchange,List<LifeResourceList> resourceList,
			InfoUser infouser,List<InfoPayBalance> balance, LogTradeHis tradeHis,
			LogTradeCreateUserHis createUserHis) {
		S.get(InfoUserExternalAccount.class).create(external);
		for(InfoUserExternalAccountAttr iter : attrs){
			S.get(InfoUserExternalAccountAttr.class).create(iter);
		}
		
		S.get(LogTradeExternalAccountHis.class).create(his);
		
		for(LogTradeExternalAccountAttrHis  iter : attrHis){
			S.get(LogTradeExternalAccountAttrHis.class).create(iter);
		}
		
		if(exchange !=null){
			S.get(LifeUserAutoExchange.class).create(exchange);
		}
		
		
		for(LifeResourceList iter: resourceList){
			S.get(LifeResourceList.class).create(iter);
		}
		S.get(InfoUser.class).create(infouser);
		for(InfoPayBalance info:balance){
		S.get(InfoPayBalance.class).create(info);
		}
		S.get(LogTradeHis.class).create(tradeHis);
		S.get(LogTradeCreateUserHis.class).create(createUserHis);
	}
	
	
	public void insertInfoUser(InfoUserExternalAccount external,List<InfoUserExternalAccountAttr> attrs,
			LogTradeExternalAccountHis his,List<LogTradeExternalAccountAttrHis> attrHis ,
			LifeUserAutoExchange exchange,List<LifeResourceList> resourceList,
			LogTradeHis tradeHis, List<InfoPayBalance> updateInfoPayBalance,
			List<InfoPayBalance> insertInfoPayBalance,List<BalanceAccessLog> balanceAccessLog) {
		// TODO Auto-generated method stub
		S.get(InfoUserExternalAccount.class).create(external);
		for(InfoUserExternalAccountAttr iter : attrs){
			S.get(InfoUserExternalAccountAttr.class).create(iter);
		}
		S.get(LogTradeExternalAccountHis.class).create(his);
		
		for(LogTradeExternalAccountAttrHis  iter : attrHis){
			S.get(LogTradeExternalAccountAttrHis.class).create(iter);
		}
		
		if(exchange !=null){
			S.get(LifeUserAutoExchange.class).create(exchange);
		}
		for(LifeResourceList iter: resourceList){
			S.get(LifeResourceList.class).create(iter);
		}
		for(InfoPayBalance info:updateInfoPayBalance){
			S.get(InfoPayBalance.class).update(info);
			}
		S.get(LogTradeHis.class).create(tradeHis);
		for(InfoPayBalance info:insertInfoPayBalance){
			S.get(InfoPayBalance.class).create(info);
			}
		for(BalanceAccessLog log:balanceAccessLog){
			S.get(BalanceAccessLog.class).create(log);
		}
	
	}

	public void insertuserbyRelation(InfoUser infouser, LogTradeHis tradeHis,
			LogTradeCreateUserHis createUserHis) {
		
		S.get(InfoUser.class).create(infouser);
		S.get(LogTradeHis.class).create(tradeHis);
		S.get(LogTradeCreateUserHis.class).create(createUserHis);
		
	}
	
	//旧套餐变更新套餐，账本清0 by yanhongxia 
	public RuleParameters query504Attr(){
		return S.get(RuleParameters.class).queryFirst(Condition.build("query504attr"));
	}
	//通过user_id得到账本
	public List<InfoPayBalance> queryInfoPayBalancesByUserId(String userid){
		return S.get(InfoPayBalance.class).query(Condition.build("queryBalanceTo0ByUserId").filter("userId",userid));
	}
	
	public void updateInfoPayBalance(InfoPayBalance ipb){
		log.debug("更新账本开始!!!");
		S.get(InfoPayBalance.class).update(ipb);
		log.debug("更新账本完成！！！");
	}
	//2018/01/16 新增 查询log  判断在时间段内 是否二次做低销赠  gaobo
	public List<LogTradeHis> getLogTradeHisByUserIdAndDxztime(String userId, String dxztime1) {
		// TODO Auto-generated method stub
		return S.get(LogTradeHis.class).query(Condition.build("getLogTradeHisByUserIdAndDxztime").filter("user_id",userId).filter("trade_time",dxztime1));
	}
	//2018/01/16 新增 如果jdpin不同 则同步更新 
	public void updateInfoUserByUserId(InfoUser info_user) {
		// TODO Auto-generated method stub
		S.get(InfoUser.class).update(info_user);
	}
	//2018/01/16 新增 插入用户表 
	public void insertIntoInfoUser(InfoUser iu) {
		// TODO Auto-generated method stub
		S.get(InfoUser.class).create(iu);
	}
	//2018/01/16  插入 log 表 
	public void insertIntoLogTradeHis(LogTradeHis lth) {
		// TODO Auto-generated method stub
		S.get(LogTradeHis.class).create(lth);
	}
	//2018/01/16  插入 log 表
	public void insertIntoLogTradeCreateUserHis(LogTradeCreateUserHis ltcu) {
		// TODO Auto-generated method stub
		S.get(LogTradeCreateUserHis.class).create(ltcu);
	}
	//2018/01/16  更新 账本表 
	public void updateInfoPayBalanceByUserIdAndBalanceId(InfoPayBalance tmpinfoPayBalance) {
		// TODO Auto-generated method stub
		S.get(InfoPayBalance.class).update(tmpinfoPayBalance);
	}
	//2018/01/16  新增 账本表 
	public void insertIntoInfoPayBalance(InfoPayBalance tmpinfoPayBalance) {
		// TODO Auto-generated method stub
		S.get(InfoPayBalance.class).create(tmpinfoPayBalance);
	}
}
