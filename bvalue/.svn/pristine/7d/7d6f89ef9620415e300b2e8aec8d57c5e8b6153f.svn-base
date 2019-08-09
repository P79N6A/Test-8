package com.tydic.beijing.bvalue.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.beijing.bvalue.dao.LogTradeCreateUserHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.service.QueryBAcctRemainBalance;

public class QueryBAcctRemainBalanceSyncImpl implements QueryBAcctRemainBalance{

	private static Logger log=Logger.getLogger(QueryBAcctRemainBalanceSyncImpl.class);
	
	private QueryBAcctRemainBalance  queryB;
	private DbTool db;
	
	public void setDb(DbTool db) {
		this.db = db;
	}
	
	public void setQueryB(QueryBAcctRemainBalance queryB) {
		this.queryB = queryB;
	}
	
	
	@Override
	public JSONObject perform(JSONObject request) {
		
		
		String syncTag="";
		String jdpin="";
		String req=request.toString();
		String userId="";
		log.debug("请求参数>>>>>>>:"+req);
		
		if(req.contains("SyncTag")){
			try{
				syncTag=request.getString("SyncTag");
				
				if(syncTag ==null ||syncTag.isEmpty() || !(syncTag.equals("1"))){
					JSONObject error=new JSONObject();
					error.put("Status", Constants.Result_Error);
					error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
					error.put("ErrorMessage", "请求参数错误[SyncTag]");
					log.debug("返回>>>>>:"+error.toString());
					return error;
				}
				
				jdpin=request.getString("JDPin");
				
				if(jdpin ==null || jdpin.isEmpty()){
					JSONObject error=new JSONObject();
					error.put("Status", Constants.Result_Error);
					error.put("ErrorCode",BValueErrorCode.ERR_REQUEST_PARAM );
					error.put("ErrorMessage", "请求参数错误[JDPin]");
					log.debug("返回>>>>>>>>:"+error.toString());
					return error;
					
				}
				userId=Common.md5(jdpin);
//				String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
				
				List<InfoPayBalance> payBalance =db.getInfoPayBalanceByUserId(userId);	//所有生效及失效的账本
//				InfoPayBalanceSync sync= new InfoPayBalanceSync();
				
				//开户
				
				if(payBalance == null){
					SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMddHHmmss");
					InfoUser  user=db.queryInfoUserByUserId(userId);
					if(user==null){
						InfoUser infouser=new InfoUser();
						infouser.setUser_id(userId);
						infouser.setJd_pin(jdpin);
						infouser.setCreate_channel("121");
						String createDate=sdf2.format(Calendar.getInstance().getTime());
						infouser.setCreate_date(createDate);
						InfoPayBalance payBalanceNew=new InfoPayBalance();
						String balanceId=Common.getUUID();
						payBalanceNew.setBalance_id(balanceId);
						payBalanceNew.setUser_id(userId);
						payBalanceNew.setBalance_type_id(0);
						payBalanceNew.setBalance(0);
						Calendar now1=Calendar.getInstance();
						String eff_date="";
						String exp_date="";
//						Calendar comeff=Calendar.getInstance();
//						comeff.set(Calendar.HOUR, 0);
//						comeff.set(Calendar.MINUTE, 0);
//						comeff.set(Calendar.SECOND, 0);
						now1.set(Calendar.MILLISECOND, 0);
//						comeff.set(Calendar.MONTH, 5);
						Date now=now1.getTime();
						
						String comeff=""+Calendar.getInstance().get(Calendar.YEAR)+"0630235959";
						String comeff2=""+Calendar.getInstance().get(Calendar.YEAR)+"0701000000";
						SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
						try {
							Date eff1=format.parse(comeff);
							Date eff2=format.parse(comeff2);
							if(now.before(eff1)){	//生效时间:6月30日（包含）前为当年1月1日 00:00:00   失效时间:6月30日（包含）前为当年12月31日 23:59:59
								eff_date=Calendar.getInstance().get(Calendar.YEAR)+"0101000000";
								exp_date=Calendar.getInstance().get(Calendar.YEAR)+"1231235959";
							}else if(now.after(eff2)){	////7月1日（包含）后为当年7月1日00:00:00  7月1日（包含）后为次年6月30日 23:59:59
								eff_date=""+(Calendar.getInstance().get(Calendar.YEAR))+"0701000000";
								exp_date=""+(Calendar.getInstance().get(Calendar.YEAR)+1)+"0630235959";
							}
						} catch (ParseException e) {
							JSONObject error =new JSONObject();
							error.put("Status", Constants.Result_Error);
							error.put("ErrorCode", Constants.SYSTEM_EXCEPTION);
							error.put("ErrorMessage", "系统异常");
							log.debug("ERR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+error.toString());
							
							return error;
						}
						payBalanceNew.setEff_date(eff_date);
						payBalanceNew.setExp_date(exp_date);
						LogTradeHis tradeHis=new LogTradeHis();
						tradeHis.setTrade_id(Common.getUUID());
						tradeHis.setTrade_type_code("501");
						tradeHis.setExternal_system_code("10000");
						tradeHis.setUser_id(userId);
						tradeHis.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
						tradeHis.setChannel_type("121");
						tradeHis.setProcess_tag(0);
						tradeHis.setTrade_time(createDate);
						
						LogTradeCreateUserHis createUserHis=new LogTradeCreateUserHis();
						createUserHis.setTrade_id(tradeHis.getTrade_id());
						createUserHis.setJd_pin(jdpin);
						createUserHis.setUser_id(userId);
						createUserHis.setPartition_id(tradeHis.getPartition_id());
						createUserHis.setProcess_tag(2);
						
						db.registUserOnCreateRelation(infouser, payBalanceNew,tradeHis,createUserHis);
//						sync.sync(payBalanceNew);
						
					}else{
						// 用户无账本  但存在资料info_user
						
						
					}
					
				}
				
				//同步有效账本
				String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
				
				String errmsg="";
				boolean syncSuc=true;
				for(InfoPayBalance iter : payBalance){
					if(iter.getExp_date().compareTo(currentTime)<=0)
						continue;
//					int ret=sync.sync(iter);
//					if(ret ==-1){
//						errmsg=errmsg+"USERID :"+iter.getUser_id() +" , Balance_id : "+iter.getBalance_id()+"|";
//						syncSuc=false;
//					}
					
				}
				
				
				if(syncSuc){
					JSONObject suc=new JSONObject();
					suc.put("Status", Constants.Result_Succ);
					suc.put("ErrorCode", "");
					suc.put("ErrorMessage", "");
					return suc;
				}else{
					log.debug("调用同步返回失败:>>>>>>"+errmsg);
					JSONObject error=new JSONObject();
					error.put("Status", Constants.Result_Error);
					error.put("ErrorCode", Constants.SYSTEM_EXCEPTION);
					error.put("ErrorMessage", "调用同步异常:"+errmsg);
					return error;
				}
				
				
			}catch(Exception e){
				log.error("获取请求参数异常,请求参数:"+req);
				JSONObject error=new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
				error.put("ErrorMessage", "获取请求参数异常");
				return error;
			}
			
			
		}else{
			return queryB.perform(request);
		}
		
		
	}

	
}
