package com.tydic.beijing.bvalue.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.HlpSmsSend;
import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.core.InfoUserDto;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.InfoBpool;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.LogTrade;
import com.tydic.beijing.bvalue.dao.LogTradeBpoolHis;
import com.tydic.beijing.bvalue.dao.LogTradeBpoolHisDtl;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LogTradeRewardHis;
import com.tydic.beijing.bvalue.service.GiveOutBValue;

/**
 * @author zhanghengbo
 *
 */
public class GiveOutBValueImpl implements GiveOutBValue {

	@Autowired
	private DbTool dbTool;
	@Autowired
	InfoPayBalanceManager infoPayBalanceManager;
	@Autowired
	InfoUserDto infoUserDto;
	
	
//	@Autowired
//	InfoPayBalanceSync infoPayBalanceSync ;
//	
//	String syncRedis;
//	 
//	public String getSyncRedis() {
//		return syncRedis;
//	}
//
//	public void setSyncRedis(String syncRedis) {
//		this.syncRedis = syncRedis;
//	}

	private static Logger log=Logger.getLogger(GiveOutBValueImpl.class);
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public JSONObject giveOut(JSONObject inputJson) throws Exception  {
		// 台账记录查询
		JSONObject outputJson = new JSONObject();
		SimpleDateFormat mysqlSdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		
		String currTime = mysqlSdf.format(new Date());
		

		try {			
			
			/*
			 * ActivityId	String	1-50	活动ID	M
				Pwd	String	10	活动秘钥	M
				JdPin	String	1-100	JDPin	M
				BValue	Long	10	发放的通信B数量	M
				OrderNo	String	20	对端流水号	M
				PlatName	String	50	对端活动名称	O
				ChannelType	String	1-15	渠道编码（商城传110）	O
			 * */
			if(!inputJson.containsKey("ActivityId") 
					||!inputJson.containsKey("Pwd")
					|| !inputJson.containsKey("JdPin")
					|| !inputJson.containsKey("BValue")
					|| !inputJson.containsKey("OrderNo")){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", BValueErrorCode.ERR_INPUT_FORMAT);
				outputJson.put("ErrorMessage", "参数必填项为空");
				return outputJson;
			}
			
			String strbValue = inputJson.getString("BValue");
			if(strbValue.indexOf(".") >0){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10023);
				outputJson.put("ErrorMessage", "发放的B值必须是正整数");
				return outputJson;
			}
			
			Long bValue = inputJson.getLong("BValue");
			String platName = inputJson.containsKey("PlatName") ? inputJson.getString("PlatName") :"";
			if(bValue<0){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10023);
				outputJson.put("ErrorMessage", "发放的B值必须大于0");
				return outputJson;
			}else if (bValue==0){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10023);
				outputJson.put("ErrorMessage", "发放的B值必须是正整数");
				return outputJson;
			}else{
				
			}
			
			String bPoolId = inputJson.getString("ActivityId");
			String pwd = inputJson.getString("Pwd");
			String orderNo = inputJson.getString("OrderNo");
			String jdPin = inputJson.getString("JdPin");
			String channelId = inputJson.containsKey("ChannelType")?inputJson.getString("ChannelType"):"110";
			String userId = Common.md5(jdPin);
			//检查活动是否存在
			List<InfoBpool> infoBPoolMessage=dbTool.getqueryInfoBPool(bPoolId);

			if(infoBPoolMessage.size()==0){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10020);
				outputJson.put("ErrorMessage", "没有该活动ID");
				return outputJson;
			}
			
			InfoBpool infoBpool = infoBPoolMessage.get(0);
			if(infoBpool.getactivity_type()!= null && !infoBpool.getactivity_type().equals("1")){
				//不是发放类活动，是没有B池的，异常
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10022);
				outputJson.put("ErrorMessage", "该活动类型异常，不是发放类活动");
				return outputJson;
			}
			
			if(infoBpool.getactivity_status() !=1){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10022);
				outputJson.put("ErrorMessage", "活动已关闭");
				return outputJson;
			}
			
			String expDate = infoBpool.getexp_date();
			if(expDate.compareTo(currTime) <0)
			{
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10028);
				outputJson.put("ErrorMessage", "活动已结束");
				return outputJson;
			}
			
			String pwdinDB = infoBpool.getpwd();
			if(pwdinDB !=null && !pwdinDB.equals(pwd)){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10021);
				outputJson.put("ErrorMessage", "活动密码不匹配");
				return outputJson;
			}
			
			if(infoBpool.getbalance()<bValue){
				outputJson.put("Status", "0");
				outputJson.put("ErrorCode", -10024);
				outputJson.put("ErrorMessage", "活动剩余B值不足，发放失败");
				return outputJson;
			}
			
			//检查订单是否已发放过
			if(isExists(bPoolId,orderNo)){
				outputJson.put("Status", "1");
				outputJson.put("ErrorCode", "");
				outputJson.put("ErrorMessage", "该订单已经发放过");
				return outputJson;
			}
			
			//判断是否开户，没有开户则做开户
			infoUserDto.createInfoUser(jdPin, channelId, currTime, new LogTrade());
			
			//TODO 扣减活动B池，并记录台账表、B值活动日志表 并触发阀值告警
			Long oldBalance = infoBpool.getbalance();
			Long newBalance = oldBalance - bValue;
			
			infoBpool.setbalance(-1*bValue);
			infoBpool.setused_balance(bValue);
			
			
			String tradeId = Common.getUUID();
			log.debug("本次发放B值的TradeId="+tradeId);
			int partition_id =    (Calendar.getInstance().get(Calendar.MONTH) + 1);//按月分区
			
			LogTradeBpoolHis logTradeBpoolHis = new LogTradeBpoolHis();
			logTradeBpoolHis.settrade_id(tradeId);
			logTradeBpoolHis.setTRADE_TYPE_CODE("603");
			logTradeBpoolHis.setbpool_id(bPoolId);
			logTradeBpoolHis.setPARTITION_ID(partition_id);
			logTradeBpoolHis.setORDER_NO(orderNo);
			logTradeBpoolHis.setPROCESS_TAG(2L);
			logTradeBpoolHis.setPROCESS_TIME(sdf.format(new Date()));
			dbTool.insertLogTradeBpoolHis(logTradeBpoolHis);
			
		    LogTradeBpoolHisDtl logTradeBpoolHisDtl = new LogTradeBpoolHisDtl();
		    logTradeBpoolHisDtl.settrade_id(tradeId);
		    logTradeBpoolHisDtl.setbpool_id(bPoolId);
		    logTradeBpoolHisDtl.setjdpin(jdPin);
		    logTradeBpoolHisDtl.setbvalue(bValue);
		    logTradeBpoolHisDtl.setold_bpool(oldBalance);
		    logTradeBpoolHisDtl.setnew_bpool(newBalance);
		    logTradeBpoolHisDtl.setnote(platName);
		    dbTool.insertLogTradeBpoolHisDtl(logTradeBpoolHisDtl);
		    
		    sendChkMsg(infoBpool.getbpool(),oldBalance,newBalance,infoBpool.getthreshold(),infoBpool);
			
		    dbTool.updateInfoBpool(infoBpool);
		    
			//TODO 增加用户B值，并记录台账表、收支记录表、账本表
		    List<InfoPayBalance> updateInfoPayBalance = new ArrayList<InfoPayBalance>();
		    List<InfoPayBalance> insertInfoPayBalance = new ArrayList<InfoPayBalance>();
		    List<BalanceAccessLog> insertBalanceAccessLog = new ArrayList<BalanceAccessLog>();
		    int retCode = infoPayBalanceManager.manage(userId, 3, 1, bValue, updateInfoPayBalance, insertInfoPayBalance, insertBalanceAccessLog);
		    if(retCode!=1){
		    	throw new Exception("修改用户账本失败！");
		    }
		    
		    for(InfoPayBalance tmpipb:updateInfoPayBalance){
		    	dbTool.updateByBalanceId(tmpipb);
		    }
		    
		    for(InfoPayBalance tmpipb:insertInfoPayBalance){
		    	dbTool.insertInfoPayBalance(tmpipb);
		    }
		    
		    for(BalanceAccessLog tmpbal:insertBalanceAccessLog){
		    	tmpbal.setTrade_Id(tradeId);
		    	tmpbal.setTrade_Type_Code("604");
		    	dbTool.insertBalanceAccessLog(tmpbal);
		    }
		    
		    LogTradeHis logTradeHis = new LogTradeHis();
		    logTradeHis.setTrade_id(tradeId);
		    logTradeHis.setTrade_type_code("604");
		    logTradeHis.setExternal_system_code(Constants.JDPinRelation_ExternalSysCode);
		    logTradeHis.setChannel_type(channelId);
		    logTradeHis.setUser_id(userId);
		    logTradeHis.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
		    logTradeHis.setBalance(bValue);
		    logTradeHis.setProcess_tag(2);
		    logTradeHis.setOrder_no(orderNo);
		    logTradeHis.setTrade_time(currTime);
		    logTradeHis.setProcess_time(currTime);
		    logTradeHis.setRemark("B值发放成功");
		    dbTool.insertLogTradeHis(logTradeHis);
		    
		    LogTradeRewardHis logTradeRewardHis = new LogTradeRewardHis();
		    logTradeRewardHis.setTrade_id(tradeId);
		    logTradeRewardHis.setUser_id(userId);
		    logTradeRewardHis.setPartition_id(partition_id);
		    logTradeRewardHis.setBpool_id(bPoolId);
		    logTradeRewardHis.setPlat_name(platName);
		    logTradeRewardHis.setProcess_time(currTime);
		    logTradeRewardHis.setProcess_tag(2);
		    logTradeRewardHis.setBvalue(bValue);
		    dbTool.insertLogTradeRewardHis(logTradeRewardHis);
		 
		    
//		    if(syncRedis!=null && syncRedis.equals("Y")){
//		    	
//		    	for(InfoPayBalance tmpipb:insertInfoPayBalance){
//		    		infoPayBalanceSync.sync(tmpipb);
//		    	}
//		    	
//		    	for(InfoPayBalance tmpipb:updateInfoPayBalance){
//		    		infoPayBalanceSync.sync(tmpipb);
//		    	}
//		    }
		    

				outputJson.put("Status", "1");
				outputJson.put("ErrorCode", "");
				outputJson.put("ErrorMessage", "");
				
				log.debug("GiveOutBValueImpl返回消息："+outputJson.toString());
				return outputJson;
				
		} catch (Exception e) {
			e.printStackTrace();
			log.error("B值活动校验异常："+e.getMessage());
			throw e;
		}
 
		
	}

	//判断userid和orderno是否已经做过发放
	private boolean isExists(String bPoolId, String orderNo) {
	
		int size= dbTool.queryLogTradeBpoolHisbypoolandOrder(bPoolId,orderNo);
		
		if(size>0){
			return true;
		}
		return false;
	}

	private void sendChkMsg(Long bpool,long oldBalance,long newBalance,long threshold,InfoBpool infoBpool) {
				
		
		if((1.0*oldBalance/bpool) >= 1.0*threshold/100 && 
				(1.0*newBalance/bpool) < 1.0*threshold/100 ) {
			
			//【京东通信】{0}总计{1}B，已使用{2}%，活动结束时间{3}，请及时关注/追加通信B。
			String sendMsg ="|aoc.dic.bactivityalarm|"+infoBpool.getbpool_name()+"|"+infoBpool.getbpool()+"|"+((infoBpool.getbpool()-newBalance)*100/infoBpool.getbpool())+"|"+infoBpool.getexp_date().substring(0,8);
			
			String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
			
			HlpSmsSend sendHis=new HlpSmsSend();
			sendHis.setMsisdn_send("10023");
			sendHis.setMsisdn_receive(infoBpool.getcontact_phone());
			sendHis.setMessage_text(sendMsg);
//			sendHis.setPara_key(paraKey);
//			sendHis.setSend_time(currentTime);
			sendHis.setCreate_time(currentTime);
			
			//DbTool dbTool = new DbTool();
			dbTool.sendMsg(sendHis);
			
			infoBpool.setthre_status(2);
			
			
		}	else {
			
			long oldstatus = infoBpool.getthre_status();
			long newstatus =1;
			if(oldstatus ==1){
				newstatus =1;
			}else if (oldstatus==2){
				newstatus =3;
			}else if (oldstatus==3){
				newstatus =3;
			}
			
			infoBpool.setthre_status(newstatus);
			
		}
	}
	 

}
