package com.tydic.beijing.bvalue.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.core.InfoUserDto;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.mem.InfoPayBalanceMem;
import com.tydic.beijing.bvalue.service.QueryBAcctRemainBalance;

public class QueryBAcctRemainBalanceImpl implements QueryBAcctRemainBalance{

	private static Logger log=Logger.getLogger(QueryBAcctRemainBalanceImpl.class);
	
	private DbTool db;
	private String dateFormat;
//	private String syncRedis;

	private InfoUserDto regist;
	public void setDb(DbTool db) {
		this.db = db;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public void setRegist(InfoUserDto regist) {
		this.regist = regist;
	}
	
//	
//	public String getSyncRedis() {
//		return syncRedis;
//	}
//
//	public void setSyncRedis(String syncRedis) {
//		this.syncRedis = syncRedis;
//	}

	@Override
	public JSONObject  perform(JSONObject  request) {
		
		String jdpin="";
		JSONObject jerror=new JSONObject();
		jerror.put("Status", Constants.Result_Error);
		jerror.put("ErrorCode", BValueErrorCode.ERR_SYSTEM_ERR);
		jerror.put("ErrorMessage", "服务异常");
		
		try{
		jdpin=request.getString("JDPin");
		
		if(jdpin==null || jdpin.isEmpty()){
			JSONObject json=new JSONObject();
			json.put("Status", Constants.Result_Error);
			json.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
			json.put("ErrorMessage", "查询参数错误");
			log.debug("请求参数错误[JDPin]");
			return json;
		}
		}catch(Exception e){
			JSONObject json=new JSONObject();
			json.put("Status", Constants.Result_Error);
			json.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
			json.put("ErrorMessage", "查询参数错误[JDPin]");
			return json;
		}
		
		String userId=Common.md5(jdpin);
		log.debug("查询JDPIN["+jdpin+"],USERID["+userId+"]");
		
		boolean newUser=false;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String currTime = sdf.format(new Date());

		List<InfoPayBalance> balance=new ArrayList<InfoPayBalance>();
		
//		InfoPayBalanceSync sync = new InfoPayBalanceSync();
		
//		InfoPayBalanceMem mem = sync.getInfoPayBalanceFromRedis(userId);
//		if(mem == null){
//			//newUser=true;
//			//如果redis没有该用户，直接返回0
//			JSONObject result=new JSONObject();
//			result.put("Status",Constants.Result_Succ );
//			result.put("ErrorCode", "");
//			result.put("ErrorMessage", "");
//			
//			JSONObject dtoJson = new JSONObject();
//			dtoJson.put("Balance", 0);
//			dtoJson.put("ExpDate", "2050-12-31 23-59-59");
//			
//			JSONArray jsonArray = new JSONArray();
//			jsonArray.add(dtoJson);
//			
//			result.put("BalanceDtoList", jsonArray);
//			
//			log.debug("返回的json对象："+result.toString());
//			return result;
//			
//		}else{
//			HashMap<String, InfoPayBalance> hm = mem.getInfoMap();
//			Date now=Calendar.getInstance().getTime();
//			for (Entry<String, InfoPayBalance> e : hm.entrySet()) {
//				InfoPayBalance vBalance=e.getValue();
//				try {
//					Date exp=sdf.parse(vBalance.getExp_date());
//					if(exp.before(now))					//去掉失效的
//						continue;
//				} catch (ParseException e1) {
//					log.error("date parse error !!");
//					return jerror;
//				}
//				
//				log.debug("infoPayBalance from redis >>>>>>>"+vBalance);
//				balance.add(vBalance);
//            
//			}	
//		}
		
		
		
//		if(newUser){						//开户
//			
//			SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMddHHmmss");
//			InfoUser  user=db.queryInfoUserByUserId(userId);
//			if(user==null){
//				InfoUser infouser=new InfoUser();
//				infouser.setUser_id(userId);
//				infouser.setJd_pin(jdpin);
//				infouser.setCreate_channel("121");
//				String createDate=sdf2.format(Calendar.getInstance().getTime());
//				infouser.setCreate_date(createDate);
//				InfoPayBalance payBalance=new InfoPayBalance();
//				String balanceId=Common.getUUID();
//				payBalance.setBalance_id(balanceId);
//				payBalance.setUser_id(userId);
//				payBalance.setBalance_type_id(0);
//				payBalance.setBalance(0);
//				Calendar now1=Calendar.getInstance();
//				String eff_date="";
//				String exp_date="";
////				Calendar comeff=Calendar.getInstance();
////				comeff.set(Calendar.HOUR, 0);
////				comeff.set(Calendar.MINUTE, 0);
////				comeff.set(Calendar.SECOND, 0);
//				now1.set(Calendar.MILLISECOND, 0);
////				comeff.set(Calendar.MONTH, 5);
//				Date now=now1.getTime();
//				
//				String comeff=""+Calendar.getInstance().get(Calendar.YEAR)+"0630235959";
//				String comeff2=""+Calendar.getInstance().get(Calendar.YEAR)+"0701000000";
//				SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
//				try {
//					Date eff1=format.parse(comeff);
//					Date eff2=format.parse(comeff2);
//					if(now.before(eff1)){	//生效时间:6月30日（包含）前为当年1月1日 00:00:00   失效时间:6月30日（包含）前为当年12月31日 23:59:59
//						eff_date=Calendar.getInstance().get(Calendar.YEAR)+"0101000000";
//						exp_date=Calendar.getInstance().get(Calendar.YEAR)+"1231235959";
//					}else if(now.after(eff2)){	////7月1日（包含）后为当年7月1日00:00:00  7月1日（包含）后为次年6月30日 23:59:59
//						eff_date=""+(Calendar.getInstance().get(Calendar.YEAR))+"0701000000";
//						exp_date=""+(Calendar.getInstance().get(Calendar.YEAR)+1)+"0630235959";
//					}
//				} catch (ParseException e) {
//					JSONObject error =new JSONObject();
//					error.put("Status", Constants.Result_Error);
//					error.put("ErrorCode", Constants.SYSTEM_EXCEPTION);
//					error.put("ErrorMessage", "系统异常");
//					log.debug("ERR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+error.toString());
//					
//					return error;
//				}
//				payBalance.setEff_date(eff_date);
//				payBalance.setExp_date(exp_date);
//				LogTradeHis tradeHis=new LogTradeHis();
//				tradeHis.setTrade_id(Common.getUUID());
//				tradeHis.setTrade_type_code("501");
//				tradeHis.setExternal_system_code("10000");
//				tradeHis.setUser_id(userId);
//				tradeHis.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
//				tradeHis.setChannel_type("121");
//				tradeHis.setProcess_tag(0);
//				tradeHis.setTrade_time(createDate);
//				
//				LogTradeCreateUserHis createUserHis=new LogTradeCreateUserHis();
//				createUserHis.setTrade_id(tradeHis.getTrade_id());
//				createUserHis.setJd_pin(jdpin);
//				createUserHis.setUser_id(userId);
//				createUserHis.setPartition_id(tradeHis.getPartition_id());
//				createUserHis.setProcess_tag(2);
//				
//				db.registUserOnCreateRelation(infouser, payBalance,tradeHis,createUserHis);
//				sync.sync(payBalance);
//			}else{	//radis无数据  存在用户资料
//				log.debug(">>>>>>>>>Query  DB.........");
//				balance=db.getValidInfoPayBalanceByUserId(userId);	//查询物理库
//				log.debug(">>>>>>>>return Query DB  ....  valid data  size:>>>:"+balance.size());
//			}
//			
////			log.debug("调用开户  >>>>>>>regist user:"+userId);
////			String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
////			
////			try {
////				regist.createInfoUser(jdpin, "121", currentTime, new LogTrade());
////			} catch (Exception e) {
////				log.error("调用开户失败>>>>>:"+e.getMessage());
////				
////			}
//		}
		
//		balance=db.getValidInfoPayBalanceByUserId(userId);	//查询物理库
//		
//		long value=0;
//		List<BalanceDto> dtos=new ArrayList<BalanceDto>();
//		
//		if( balance==null || balance.isEmpty()){      		//如果info_user查询不为null ,info_pay_balance也不该为null
//			JSONObject result=new JSONObject();
//			result.put("Status",Constants.Result_Succ );
//			result.put("ErrorCode", "");
//			result.put("ErrorMessage", "");
//			List<BalanceDto> dtosReturn=new ArrayList<BalanceDto>();
//			
//			Date now=Calendar.getInstance().getTime();
//			String expDate="";
//			try {
//				Date lastYearStart=new SimpleDateFormat("yyyyMMddHHmmss").parse(Calendar.getInstance().get(Calendar.YEAR)+"0701000000");
////				Date lastYearEnd=new SimpleDateFormat("yyyyMMddHHmmss").parse(Calendar.getInstance().get(Calendar.YEAR)+"1231235959");
//				if(now.before(lastYearStart)){
//					expDate=""+Calendar.getInstance().get(Calendar.YEAR)+"-12-31 23-59-59";
//				}else{
//					expDate=""+(Calendar.getInstance().get(Calendar.YEAR)+1)+"-06-30 23-59-59";
//				}
//				
//				BalanceDto dto=new BalanceDto(0,expDate);
//				dtosReturn.add(dto);
//			} catch (ParseException e) {
//				log.error("date parse error !");
//				JSONObject error=new JSONObject();
//				error.put("Status", Constants.Result_Error);
//				error.put("ErrorCode", BValueErrorCode.ERR_SYSTEM_ERR);
//				error.put("ErrorMessage", "服务异常");
//				return error;
//			}
//			ObjectMapper mapper = new ObjectMapper(); 
//			mapper.setPropertyNamingStrategy(new CapitalizedPropertyNamingStrategy());
//			try {
//				String json=mapper.writeValueAsString(dtosReturn);
//				result.put("BalanceDtoList", json);
//				return result;
//			} catch (Exception e) {
//				log.error("json parse error !");
//				JSONObject error=new JSONObject();
//				error.put("Status", Constants.Result_Error);
//				error.put("ErrorCode", BValueErrorCode.ERR_SYSTEM_ERR);
//				error.put("ErrorMessage", "服务异常");
//				return error;
//				
//			}
//			
//		}
//		SimpleDateFormat sdfout=new SimpleDateFormat(dateFormat);
//		for(InfoPayBalance iter : balance){
//			
//			if(iter.getBalance_type_id()==Constants.Balance_type_normal  
//					|| iter.getBalance_type_id()==Constants.Balance_type_activity
//					){	//只取自常B值   增加活动B值
//				value=iter.getBalance();
////				Date date=iter.getExp_date();
////				String exp=sdf.format(date);
//				String expt=iter.getExp_date();
//				Date expd=null;
//				try {
//					expd = sdf.parse(expt);
//				} catch (ParseException e) {
//					log.error("date parse error!");
//					JSONObject error=new JSONObject();
//					error.put("Status", Constants.Result_Error);
//					error.put("ErrorCode", BValueErrorCode.ERR_SYSTEM_ERR);
//					error.put("ErrorMessage", "服务异常");
//					return error;
//				}
//				String exp=sdfout.format(expd);
//				//TODO 暂时针对负值修改失效日期  
////				if(value < 0)
////					exp="2050-12-31 23-59-59";
//				
//				BalanceDto bd=new BalanceDto(value,exp);
//				dtos.add(bd);
//			}
//			
//		}
////		InfoPayBalanceSync sync = new InfoPayBalanceSync();
//        
//		for(InfoPayBalance iter : balance){
//			sync.sync(iter);
//		}
//		
//		
//		ObjectMapper mapper = new ObjectMapper(); 
//		mapper.setPropertyNamingStrategy(new CapitalizedPropertyNamingStrategy());
//		try {
//			String json=mapper.writeValueAsString(dtos);
//			JSONObject result=new JSONObject();
//			result.put("Status",Constants.Result_Succ );
//			result.put("ErrorCode", "");
//			result.put("ErrorMessage", "");
//			result.put("BalanceDtoList", json);
//			log.debug(" >>>[BalanceDtoList ]:>>>"+json);
//			return result;
//		} catch (JsonGenerationException e) {
//			log.debug("json parse  error!>>>>:"+e.getMessage());
//			return jerror;
//			
//		} catch (JsonMappingException e) {
//			log.debug("json parse  error!>>>>:"+e.getMessage());
//			return jerror;
//		} catch (IOException e) {
//			log.debug("json parse  error!>>>>:"+e.getMessage());
//			return jerror;
//		}  
		
	//	log.debug("是否同步Redis开关:"+this.syncRedis);
		JSONObject retJson = new JSONObject();
		SimpleDateFormat sdfout=new SimpleDateFormat(dateFormat);
		
		try {
/*			if(this.syncRedis !=null && this.syncRedis.equals("Y")){
				//如果是Y，则需要查询redis，没有则查mysql并同步redis
				InfoPayBalanceMem mem = sync.getInfoPayBalanceFromRedis(userId);
				log.debug("mem====["+mem+"]");
				if(mem ==null ){
					//如果Redis没有查到记录，还要到Mysql里查是否存在，如果存在就同步到redis并返回
					List<InfoPayBalance> balanceList=db.getValidInfoPayBalanceByUserId(userId);	
					
					retJson.put("Status", Constants.Result_Succ);
					retJson.put("ErrorCode", "");
					retJson.put("ErrorMessage", "");
					JSONArray balanceDtoList = new JSONArray();
					
					HashMap<String, InfoPayBalance> infoMap = new HashMap<String, InfoPayBalance>();
					for(InfoPayBalance infoPayBalance : balanceList){
						
						if ((infoPayBalance.getBalance_type_id() == Constants.Balance_type_normal || 
								infoPayBalance.getBalance_type_id() == Constants.Balance_type_activity) &&
								infoPayBalance.getExp_date().compareTo(currTime)>0
								){
							JSONObject tmpJson = new JSONObject();
							tmpJson.put("Balance", infoPayBalance.getBalance());
							String outExpDate = sdfout.format(sdf.parse(infoPayBalance.getExp_date()));
							tmpJson.put("ExpDate", outExpDate);
							balanceDtoList.add(tmpJson);
							
							infoMap.put(infoPayBalance.getBalance_id(), infoPayBalance);
						}
					}
					
					retJson.put("BalanceDtoList", balanceDtoList);
					
					if(balanceDtoList.size() >0 ){
						mem = new InfoPayBalanceMem();
						mem.setUser_id(userId);
						mem.setInfoMap(infoMap);
						
						sync.syncByObj(mem);
					}
 
				}else {
					//Redis有该用户，则直接返回
					retJson.put("Status", Constants.Result_Succ);
					retJson.put("ErrorCode", "");
					retJson.put("ErrorMessage", "");
					
					HashMap<String, InfoPayBalance> infoMap = mem.getInfoMap();
					JSONArray balanceDtoList = new JSONArray();
					
					for(String  tmpstr : infoMap.keySet()){
						InfoPayBalance infoPayBalance = infoMap.get(tmpstr);
						if ((infoPayBalance.getBalance_type_id() == Constants.Balance_type_normal || 
								infoPayBalance.getBalance_type_id() == Constants.Balance_type_activity) &&
								infoPayBalance.getExp_date().compareTo(currTime)>0
								){
							JSONObject tmpJson = new JSONObject();
							tmpJson.put("Balance", infoPayBalance.getBalance());
							String outExpDate = sdfout.format(sdf.parse(infoPayBalance.getExp_date()));
							tmpJson.put("ExpDate", outExpDate);
							balanceDtoList.add(tmpJson);
						}
								
					}
					
					retJson.put("BalanceDtoList", balanceDtoList);

				}
				
			}else {*/
				//如果是N，则不操作redis，直接查询mysql并返回
				List<InfoPayBalance> balanceList=db.getValidInfoPayBalanceByUserId(userId);	
				log.debug("------------------"+balanceList.toString());
				retJson.put("Status", Constants.Result_Succ);
				retJson.put("ErrorCode", "");
				retJson.put("ErrorMessage", "");
				
				JSONArray balanceDtoList = new JSONArray();
				
				for(InfoPayBalance infoPayBalance : balanceList){
					
					if ((infoPayBalance.getBalance_type_id() == Constants.Balance_type_normal || 
							infoPayBalance.getBalance_type_id() == Constants.Balance_type_activity) &&
							infoPayBalance.getExp_date().compareTo(currTime)>0
							){
						JSONObject tmpJson = new JSONObject();
						tmpJson.put("Balance", infoPayBalance.getBalance());
						String outExpDate = sdfout.format(sdf.parse(infoPayBalance.getExp_date()));
						tmpJson.put("ExpDate", outExpDate);
						balanceDtoList.add(tmpJson);
					}
				}
			     
				retJson.put("BalanceDtoList", balanceDtoList);
				
				
//			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("异常:"+e.getMessage());
			retJson.put("Status", Constants.Result_Error);
			retJson.put("ErrorCode",  BValueErrorCode.ERR_SYSTEM_ERR);
			retJson.put("ErrorMessage", "接口异常");
		}
//		InfoPayBalanceSync sync = new InfoPayBalanceSync();
        
//		for(InfoPayBalance iter : balance){
//			sync.sync(iter);
//		}
//		
//		
//		ObjectMapper mapper = new ObjectMapper(); 
//		mapper.setPropertyNamingStrategy(new CapitalizedPropertyNamingStrategy());
//		try {
//			String json=mapper.writeValueAsString(dtos);
//			JSONObject result=new JSONObject();
//			result.put("Status",Constants.Result_Succ );
//			result.put("ErrorCode", "");
//			result.put("ErrorMessage", "");
//			result.put("BalanceDtoList", json);
//			log.debug(" >>>[BalanceDtoList ]:>>>"+json);
//			return result;
//		} catch (JsonGenerationException e) {
//			log.debug("json parse  error!>>>>:"+e.getMessage());
//			return jerror;
//			
//		} catch (JsonMappingException e) {
//			log.debug("json parse  error!>>>>:"+e.getMessage());
//			return jerror;
//		} catch (IOException e) {
//			log.debug("json parse  error!>>>>:"+e.getMessage());
//			return jerror;
//		}  
		
		
		log.debug("返回消息"+retJson.toString());
		return retJson;
		
		
	}
	
	
//	public static void main(String args[]){
//		
//		
//		ApplicationContext ac=new ClassPathXmlApplicationContext(new String[]{"queryBAcctRemainBalance.xml"});
//		InfoPayBalance pay = new InfoPayBalance();
//        pay.setBalance_id("zzzzsssssssssssssssssstttttttee");
//        pay.setUser_id("01d7f40760960e7bd9443513f22ab9af");
//
//        InfoPayBalanceSync sync = new InfoPayBalanceSync();
//        sync.sync(pay);
//
//        InfoPayBalanceMem mem = sync.getInfoPayBalanceFromRedis("01d7f40760960e7bd9443513f22ab9af");
//        HashMap<String, InfoPayBalance> hm = mem.getInfoMap();
//
//        for (Entry<String, InfoPayBalance> e : hm.entrySet()) {
//             System.out.println("from redis>>>>>>>>"+e.getValue().toString());
//        }
//		
//	}

}
