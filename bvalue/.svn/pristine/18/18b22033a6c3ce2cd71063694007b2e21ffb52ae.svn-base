package com.tydic.beijing.bvalue.service.impl;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueErrorCode;
import com.tydic.beijing.bvalue.common.CapitalizedPropertyNamingStrategy;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.LogTradeAdjustHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dto.BalanceDto;
import com.tydic.beijing.bvalue.service.AdjustBValue;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class AdjustBValueImpl implements AdjustBValue{
	
	private static Logger log=Logger.getLogger(AdjustBValueImpl.class);
	private DbTool db;
	
//	private String syncRedis;
//
//	public String getSyncRedis() {
//		return syncRedis;
//	}
//
//
//	public void setSyncRedis(String syncRedis) {
//		this.syncRedis = syncRedis;
//	}


	public void setDb(DbTool db) {
		this.db = db;
	}
	
	
	@Override
	public JSONObject perform(JSONObject request) {
		
		log.debug("请求参数>>>>>>>:"+request.toString());
		
		JSONObject jerror = new JSONObject();
		jerror.put("Status", Constants.Result_Error);
		jerror.put("ErrorCode",BValueErrorCode.ERR_REQUEST_PARAM);
		jerror.put("ErrorMessage", "请求参数错误");
		String jdpin="";
		String mobileNumber="";
		String adjustMode="";
		long bvalue=0;
		String adjustReason="";
		String adjustType ="";  //1手动调整 2销户调整 3 b值活动调整
		int adjustBalanceType =0; //调整类型对应的账本类型，1 2 对应0账本，3对应3账本
		
		String userId="";
		try{
			jdpin=request.getString("JDPin");
			mobileNumber=request.getString("MobileNumber");
			adjustMode=request.getString("AdjustMode");
			bvalue=request.getLong("BValue");
			//adjustType = request.getString("AdjustType");
			if(request.containsKey("AdjustType")){
				adjustType = request.getString("AdjustType");
			}
			
			if(adjustType == null || adjustType.length()==0){
				adjustType ="1";
			}
			
			if(!adjustType.equals("1") && !adjustType.equals("2") && !adjustType.equals("3")){
				JSONObject error = new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode",BValueErrorCode.ERR_REQUEST_PARAM);
				error.put("ErrorMessage", "请求参数错误[AdjustType]");
				log.debug("请求参数错误[AdjustType]:"+adjustType);
				return error;
			}

			adjustBalanceType = getAdjustBalanceType(adjustType);
			
			if(jdpin.isEmpty() || adjustMode.isEmpty() || bvalue<=0){
				log.debug("请求参数错误");
				return jerror;
				
			}
			if(!adjustMode.equals("1") && !adjustMode.equals("2")){
				JSONObject error = new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode",BValueErrorCode.ERR_REQUEST_PARAM);
				error.put("ErrorMessage", "请求参数错误[AdjustMode]");
				log.debug("请求参数错误[AdjustMode]:"+adjustMode);
				return error;
			}
			userId=Common.md5(jdpin);
			// 新增必填 调整原因
			adjustReason=request.getString("AdjustReason").trim();
			if(adjustReason == null || adjustReason.isEmpty()){
				JSONObject error = new JSONObject();
				error.put("Status", Constants.Result_Error);
				error.put("ErrorCode",BValueErrorCode.ERR_REQUEST_PARAM);
				error.put("ErrorMessage", "请求参数错误[AdjustReason]");
				log.debug("请求参数错误[AdjustReason]:"+adjustMode);
				return error;
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			log.debug("输入参数错误"+e.toString());
			return jerror;
		}
		
		//不需要校验绑定关系
//		if(!checkPhoneNumber(userId,mobileNumber)){
//			JSONObject error = new JSONObject();
//			error.put("Status", Constants.Result_Error);
//			error.put("ErrorCode", BValueErrorCode.ERR_REQUEST_PARAM);
//			error.put("ErrorMessage", "查询参数错误,请核对手机号码及绑定关系");
//			log.debug("ERR>>>>>>>>"+jerror.toString());
//			return error ;
//		}
		
		boolean noBalance=false;
		
	//	InfoPayBalanceSync sync = new InfoPayBalanceSync();
		InfoPayBalance payBalance=new InfoPayBalance();
		
		if(!isExists(userId)){
			JSONObject error = new JSONObject();
			error.put("Status", Constants.Result_Error);
			error.put("ErrorCode",BValueErrorCode.ERR_REQUEST_PARAM);
			error.put("ErrorMessage", "用户不存在");
			log.debug("用户不存在"+error.toString());
			return error;
		}
		
		List<InfoPayBalance> balance=db.getValidInfoPayBalanceByUserId(userId);		//获取当前有效账本  得到 生失效日期为14位
		log.debug("balance==========="+balance.toString());
		if(balance ==null || balance.isEmpty()){	//创建账本  ,账本放入balance  同步redis
			log.debug(">>>>>>>>>>>>创建账本>>");
			noBalance=true;
//			InfoPayBalance payBalance=new InfoPayBalance();
			String balanceId=Common.getUUID();
			payBalance.setBalance_id(balanceId);
			payBalance.setUser_id(userId);
			payBalance.setBalance_type_id(adjustBalanceType); //0
			payBalance.setBalance(0);
			Calendar now1=Calendar.getInstance();
			String eff_date="";
			String exp_date="";
//			Calendar comeff=Calendar.getInstance();
//			comeff.set(Calendar.HOUR, 0);
//			comeff.set(Calendar.MINUTE, 0);
//			comeff.set(Calendar.SECOND, 0);
			now1.set(Calendar.MILLISECOND, 0);
//			comeff.set(Calendar.MONTH, 5);
			Date now=now1.getTime();
			
			String comeff=""+Calendar.getInstance().get(Calendar.YEAR)+"0630235959";
			String comeff2=""+Calendar.getInstance().get(Calendar.YEAR)+"0701000000";
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				Date eff1=format.parse(comeff);
				Date eff2=format.parse(comeff2);
				if(now.before(eff1)){	//6月30日（包含）前为当年1月1日 00:00:00  6月30日（包含）前为当年12月31日 23:59:59
					eff_date=Calendar.getInstance().get(Calendar.YEAR)+"0101000000";
					exp_date=Calendar.getInstance().get(Calendar.YEAR)+"1231235959";
				}else if(now.after(eff2)){	//7月1日（包含）后为当年7月1日00:00:00  7月1日（包含）后为次年6月30日 23:59:59
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
			payBalance.setEff_date(eff_date);
			payBalance.setExp_date(exp_date);
			if(balance == null){
				balance= new ArrayList<InfoPayBalance>();
			}
			
			balance.add(payBalance);
			
		}
		
		
		List<InfoPayBalance> balanceAdd=new ArrayList<InfoPayBalance>(); 		//保存正常B值账本  调增
		TreeSet<InfoPayBalance> balanceDel=new TreeSet<InfoPayBalance>(new Comparator<InfoPayBalance>(){	//保存正常B值账本  调减

			@Override
			public int compare(InfoPayBalance o1, InfoPayBalance o2) {
				return o1.getExp_date().compareTo(o2.getExp_date());
			}
			
		});
		
		InfoPayBalance  minusBalance=null;
		
		String now=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		for( InfoPayBalance iter : balance){
			if(iter.getBalance_type_id() !=0 && iter.getBalance_type_id() !=3){ //过滤溢出账本
				continue;
			}
			
			if(iter.getExp_date().compareTo(now)<=0){
				continue;
			}
			
			if(iter.getBalance()<0){
				minusBalance=iter;
			}
			
			if(adjustMode.equals("1")){
				balanceAdd.add(iter);
			}
			
			if(adjustMode.equals("2")){		//调减
				balanceDel.add(iter);
			}
			
			
		}
		
		
		
		String timeCen=""+Calendar.getInstance().get(Calendar.YEAR)+"0630235959";	//比较时间点
		String expDate1=""+Calendar.getInstance().get(Calendar.YEAR)+"1231235959";
		String expDate2=""+(Calendar.getInstance().get(Calendar.YEAR)+1)+"0630235959";   //次年
		long writeOff =0L;//核销的欠费
		long newBalance =0L; //赠送B值核销欠费后的余额
		long deficitBalance =0L; //欠费
		List<InfoPayBalance> listInfoPayBalanceForUpdate = new ArrayList<InfoPayBalance>(); //最终要更新到数据库的infopaybalance列表
		
		String balanceId="";	//记录调增账本
		InfoPayBalance balanceTo=new InfoPayBalance();
		if(adjustMode.equals("1")){		//调增
			log.debug(">>>>>>>调增");

//			if(minusBalance !=null){//有负账本
//				balanceId=minusBalance.getBalance_id();
////				minusBalance.setBalance(minusBalance.getBalance()+bvalue);
//				if(minusBalance.getBalance() + bvalue >=0){ //如果调整的B值足够抵扣欠费，则会修改该欠费账本的生失效时间
//					if(now.compareTo(timeCen)<=0){
//						String effDate=""+Calendar.getInstance().get(Calendar.YEAR)+"0101000000";
//						String expDate=expDate1;
//						minusBalance.setEff_date(effDate);
//						minusBalance.setExp_date(expDate);
//					}else{
//						String effDate=""+Calendar.getInstance().get(Calendar.YEAR)+"0701000000";
//						String expDate=expDate2;
//						minusBalance.setEff_date(effDate);
//						minusBalance.setExp_date(expDate);
//					
//					}
//				}
//				balanceTo=minusBalance;
//				
//			}else{//没有负账本
//				for(InfoPayBalance iter : balanceAdd){//如果没有负账本，就到当前账本列表里找一个合适的，加到这个账本
//					if(now.compareTo(timeCen)<=0){ 	//在6月30日（包含）前为当年1月1日 00:00:00，失效时间为当年12月31日 23:59:59
//						if(iter.getExp_date().equals(expDate1) && iter.getBalance_type_id() == adjustBalanceType){ //增加账本类型匹配
//							balanceId=iter.getBalance_id();
//							balanceTo=iter;
//							break;
//						}
//					
//					}else{		//在7月1日（包含）后为次年7月1日00:00:00，失效时间为次年6月30日 23:59:59
//						if(iter.getExp_date().equals(expDate2) && iter.getBalance_type_id() == adjustBalanceType){
//						
//							balanceId=iter.getBalance_id();
//							balanceTo=iter;
//							break;
//						}
//					
//					}
//				
//				}
//			
//			}
//			
//			if(balanceId.isEmpty()){	//未找到账本      包括新创建账本与原有账本
//				LogTradeHis trade =new LogTradeHis();
//				trade.setTrade_id(Common.getUUID());
//				trade.setTrade_type_code("506");
//				trade.setExternal_system_code(Constants.JDPinRelation_ExternalSysCode);
//				trade.setChannel_type("115");
//				trade.setUser_id(userId);
//				trade.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
//				if(adjustMode.equals("1")){
//					trade.setBalance(bvalue);
//				}else{
//					trade.setBalance(0-bvalue);
//				}
//				trade.setProcess_tag(3); 	//失败
//				trade.setTrade_time(now);
//				trade.setProcess_time(now);
//				trade.setRemark("无账本,失效日期:当年1231235959 | 次年0630235959");
//				db.insertLogTradeHis(trade);
//				
//				
//				log.debug("["+userId+"]无账本,失效日期:当年1231235959 | 次年0630235959");
//				log.error("["+userId+"]无账本,失效日期:当年1231235959 | 次年0630235959");
//				JSONObject error=new JSONObject();
//				error.put("Status", Constants.Result_Error);
//				error.put("ErrorCode", Constants.DataError);
//				error.put("ErrorMessage", "用户账本信息异常");
//				return error;
//			}
//			if(noBalance){
//				db.insertInfoPayBalance(balanceTo);
//			}
//			
//			//调增
//			
//			long oldBalance=balanceTo.getBalance();
//			balanceTo.setBalance(balanceTo.getBalance()+bvalue);
//			LogTradeHis  trade=initLogTradeHis(userId,bvalue,adjustMode);
//			LogTradeAdjustHis adjustHis=initLogTradeAdjustHis(trade,adjustMode,oldBalance,balanceTo.getBalance(),adjustReason);
//			BalanceAccessLog accessLog=new BalanceAccessLog();
//			accessLog.setTrade_Id(trade.getTrade_id());
//			accessLog.setTrade_Type_Code(trade.getTrade_type_code());
//			accessLog.setUser_Id(userId);
//			accessLog.setPartition_Id(trade.getPartition_id());
//			accessLog.setBalance_Id(balanceTo.getBalance_id());
//			accessLog.setBalance_Type_Id(balanceTo.getBalance_type_id());
//			accessLog.setAccess_Tag("0");		//存款
//			accessLog.setMoney(bvalue);
//			accessLog.setOld_Balance(oldBalance);
//			accessLog.setNew_Balance(balanceTo.getBalance());
//			accessLog.setOperate_Time(trade.getTrade_time());
//			db.updateAdjustBValueForAdd(trade,adjustHis,balanceTo,accessLog);
			
			
			//zhanghb add start
			
			LogTradeHis  trade=initLogTradeHis(userId,bvalue,adjustMode);
			
			
			InfoPayBalanceManager infoPayBalanceManager = new InfoPayBalanceManager();
			List<InfoPayBalance> updateInfoPayBalance = new ArrayList<InfoPayBalance>();
			List<InfoPayBalance> insertInfoPayBalance = new ArrayList<InfoPayBalance>();
			List<BalanceAccessLog> insertBalanceAccessLog = new ArrayList<BalanceAccessLog>();
			infoPayBalanceManager.manage(userId, adjustBalanceType, Integer.parseInt(adjustMode), bvalue, updateInfoPayBalance, insertInfoPayBalance, insertBalanceAccessLog);
			
			
			long totalOldBalance = getTotalBalance(balance);
			long totalChgBalance = getTotalBalance(updateInfoPayBalance) + getTotalBalance(insertInfoPayBalance);
			
			LogTradeAdjustHis adjustHis=initLogTradeAdjustHis(trade,adjustMode,totalOldBalance,totalOldBalance+totalChgBalance,adjustReason);
			
			
			db.updateAdjustBValueForAdd(trade,adjustHis,updateInfoPayBalance,insertInfoPayBalance,insertBalanceAccessLog);
			
			//zhanghb add end 
//			redis同步
//			if(this.syncRedis!=null && this.syncRedis.equals("Y")){
//				for(InfoPayBalance tmpipb:updateInfoPayBalance){
//					sync.sync(tmpipb);
//				}
//				for(InfoPayBalance tmpipb:insertInfoPayBalance){
//					sync.sync(tmpipb);
//				}
//			}
			
			
			//JSONObject result=getSuccJson(balanceAdd,balanceTo);
			JSONObject result = new JSONObject();
			result.put("Status", Constants.Result_Succ);
			result.put("ErrorCode", "");
			result.put("ErrorMessage", "成功");
			
			JSONArray balancedto = new JSONArray();
			List<InfoPayBalance> resultBalance=db.getValidInfoPayBalanceByUserId(userId);	
			for(InfoPayBalance tmpipb : resultBalance){
				if(tmpipb.getBalance_type_id() ==0 || tmpipb.getBalance_type_id()==3){
					JSONObject tmpjson = new JSONObject();
					String exp=tmpipb.getExp_date();
					String expDate=exp.substring(0,4)+"-"+exp.substring(4,6)+"-"+exp.substring(6,8)+" "+exp.substring(8,10)+"-"+exp.substring(10,12)+"-"+exp.substring(12,14);
					tmpjson.put("Balance", tmpipb.getBalance());
					tmpjson.put("ExpDate", expDate);
				}
			}
			
			result.put("BalanceDtoList", balancedto);
			
			return result;
			
		}
		
		

		else if(adjustMode.equals("2")){
			log.debug(">>>>>>>>>>>调减");
			long value=bvalue;
			List<InfoPayBalance> adjustedBalance=new ArrayList<InfoPayBalance>();	//调整过的
			List<BalanceAccessLog> accessLog=new ArrayList<BalanceAccessLog>();		
			
			
			int size=balanceDel.size();
			long oldTotalBalance=0;
			long newTotalBalance=0;
			
			
			//调减
//			for(InfoPayBalance iter : balanceDel){
//				long tmp=iter.getBalance();
//				oldTotalBalance=oldTotalBalance+tmp;
//				BalanceAccessLog balanceAccess=new BalanceAccessLog();
//				size--;
//				if(size==0){
//					if(value>0){
//						iter.setBalance(tmp-value);
//						if(iter.getBalance()<0){
//							iter.setExp_date("20501231235959");
//						}
//						newTotalBalance=newTotalBalance+iter.getBalance();
//						
//						balanceAccess.setBalance_Id(iter.getBalance_id());
//						balanceAccess.setBalance_Type_Id(iter.getBalance_type_id());
//						balanceAccess.setMoney(value);	//保存正值
//						balanceAccess.setOld_Balance(tmp);
//						balanceAccess.setNew_Balance(iter.getBalance());
//						
//					}
//				}else{
//					if(value==0){
//						continue;			// 需要统计原始总额
//					}
//					if( tmp >= value){
//						value=0;
//						iter.setBalance(tmp-bvalue);
//						newTotalBalance=newTotalBalance+iter.getBalance();
//						balanceAccess.setBalance_Id(iter.getBalance_id());
//						balanceAccess.setBalance_Type_Id(iter.getBalance_type_id());
//						balanceAccess.setMoney(value);
//						balanceAccess.setOld_Balance(tmp);
//						balanceAccess.setNew_Balance(iter.getBalance());
//						
//					}else{
//						value=value-tmp;
//						iter.setBalance(0);
//						newTotalBalance=newTotalBalance+iter.getBalance();
//						
//						balanceAccess.setBalance_Id(iter.getBalance_id());
//						balanceAccess.setBalance_Type_Id(iter.getBalance_type_id());
//						balanceAccess.setMoney(tmp);
//						balanceAccess.setOld_Balance(tmp);
//						balanceAccess.setNew_Balance(iter.getBalance());
//						
//					}
//					
//				}
//				adjustedBalance.add(iter);
//				accessLog.add(balanceAccess);
//			}
			
			LogTradeHis  trade=initLogTradeHis(userId,bvalue,adjustMode);
//			LogTradeAdjustHis adjustHis=initLogTradeAdjustHis(trade,adjustMode ,oldTotalBalance,newTotalBalance,adjustReason);
//			for(BalanceAccessLog iter : accessLog){
//				iter.setTrade_Id(trade.getTrade_id());
//				iter.setTrade_Type_Code(trade.getTrade_type_code());
//				iter.setUser_Id(trade.getUser_id());
//				iter.setPartition_Id(trade.getPartition_id());
//				iter.setAccess_Tag("1");	//取款
//				iter.setOperate_Time(now);
//			}
//			if(noBalance){
//				InfoPayBalance pay=adjustedBalance.get(0);
//				db.insertInfoPayBalance(pay);
//				db.updateAdjustDataForDecrease(trade, adjustHis, accessLog);
//				sync.sync(pay);
//			}else{
//			
//				db.updateAdjustDataForDecrease(trade,adjustHis,accessLog,adjustedBalance);
//				for(InfoPayBalance iter : adjustedBalance){
//					sync.sync(iter);
//				}
//				
//			}
			
			
			InfoPayBalanceManager infoPayBalanceManager = new InfoPayBalanceManager();
			List<InfoPayBalance> updateInfoPayBalance = new ArrayList<InfoPayBalance>();
			List<InfoPayBalance> insertInfoPayBalance = new ArrayList<InfoPayBalance>();
			List<BalanceAccessLog> insertBalanceAccessLog = new ArrayList<BalanceAccessLog>();
			infoPayBalanceManager.manage(userId, adjustBalanceType, Integer.parseInt(adjustMode), bvalue, updateInfoPayBalance, insertInfoPayBalance, insertBalanceAccessLog);
			
			
			long totalOldBalance = getTotalBalance(balance);
			long totalChgBalance = getTotalBalance(updateInfoPayBalance) + getTotalBalance(insertInfoPayBalance);
			
			LogTradeAdjustHis adjustHis=initLogTradeAdjustHis(trade,adjustMode,totalOldBalance,totalOldBalance+totalChgBalance,adjustReason);
			db.updateAdjustBValueForAdd(trade,adjustHis,updateInfoPayBalance,insertInfoPayBalance,insertBalanceAccessLog);
//			redis同步
//			if(this.syncRedis!=null && this.syncRedis.equals("Y")){
//				for(InfoPayBalance tmpipb:updateInfoPayBalance){
//					sync.sync(tmpipb);
//				}
//				for(InfoPayBalance tmpipb:insertInfoPayBalance){
//					sync.sync(tmpipb);
//				}
//			}
			
			
			//JSONObject result=getSuccJson(balanceDel,adjustedBalance);
			JSONObject result = new JSONObject();
			result.put("Status", Constants.Result_Succ);
			result.put("ErrorCode", "");
			result.put("ErrorMessage", "成功");
			
			JSONArray balancedto = new JSONArray();
			List<InfoPayBalance> resultBalance=db.getValidInfoPayBalanceByUserId(userId);	
			for(InfoPayBalance tmpipb : resultBalance){
				if(tmpipb.getBalance_type_id() ==0 || tmpipb.getBalance_type_id()==3){
					JSONObject tmpjson = new JSONObject();
					String exp=tmpipb.getExp_date();
					String expDate=exp.substring(0,4)+"-"+exp.substring(4,6)+"-"+exp.substring(6,8)+" "+exp.substring(8,10)+"-"+exp.substring(10,12)+"-"+exp.substring(12,14);
					tmpjson.put("Balance", tmpipb.getBalance());
					tmpjson.put("ExpDate", expDate);
				}
			}
			
			result.put("BalanceDtoList", balancedto);
			
			return result;
			//return result;
		}
		else{
			
			return jerror;
		}
		
	}

 


	private long getTotalBalance(List<InfoPayBalance> updateInfoPayBalance) {

        long totalbalance =0L;
        for(InfoPayBalance tmpipb:updateInfoPayBalance){
        	if(tmpipb.getBalance_type_id() !=0 && tmpipb.getBalance_type_id()!=3){
        		continue;
        	}
        	totalbalance = totalbalance + tmpipb.getBalance();        			
        }
		
		return totalbalance;
	}


	private int getAdjustBalanceType(String adjustType) {
		// 1和2 对应0账本，3对应3账本
		if(adjustType.equals("1") || adjustType.equals("2")){
			return 0;
		}else if (adjustType.equals("3")){
			return 3;
		}
		return 0;
	}


	public JSONObject getSuccJson(List<InfoPayBalance> balanceAdd ,InfoPayBalance balance){
		
		List<BalanceDto>  dtos=new ArrayList<BalanceDto>();
		for(InfoPayBalance iter : balanceAdd){
			BalanceDto dto=new BalanceDto();
			if(iter.getBalance_id().equals(balance.getBalance_id())){
				dto.setBalance(balance.getBalance());
				String exp=balance.getExp_date();
				String expDate=exp.substring(0,4)+"-"+exp.substring(4,6)+"-"+exp.substring(6,8)+" "+exp.substring(8,10)+"-"+exp.substring(10,12)+"-"+exp.substring(12,14);
				
				dto.setExpDate(expDate);
			}else{
				dto.setBalance(iter.getBalance());
				String exp=iter.getExp_date();
				String expDate=exp.substring(0,4)+"-"+exp.substring(4,6)+"-"+exp.substring(6,8)+" "+exp.substring(8,10)+"-"+exp.substring(10,12)+"-"+exp.substring(12,14);
				
				dto.setExpDate(expDate);
			}
			dtos.add(dto);
		}
		
		return getJsonUseList(dtos);
	}
	
	public JSONObject getJsonUseList(List<BalanceDto> dtos){
		ObjectMapper mapper =new ObjectMapper();
		mapper.setPropertyNamingStrategy(new CapitalizedPropertyNamingStrategy());
		JSONObject err=new JSONObject();
		err.put("Status", Constants.Result_Error);
		err.put("ErrorCode", BValueErrorCode.ERR_SYSTEM_ERR);
		err.put("ErrorMessage", "接口服务异常");
		String balanceList="";
		try {
			balanceList= mapper.writeValueAsString(dtos);
			
		} catch (JsonGenerationException e) {
			log.debug("json parse error !>>>>>:"+e.getMessage());
			return err;
		} catch (JsonMappingException e) {
			log.debug("json parse error !>>>>>:"+e.getMessage());
			return err;
		} catch (IOException e) {
			log.debug("json parse error !>>>>>:"+e.getMessage());
			return err;
		}
		
		JSONObject result=new JSONObject();
		result.put("Status", Constants.Result_Succ);
		result.put("ErrorCode", "");
		result.put("ErrorMessage", "");
		result.put("BalanceDtoList", balanceList);
		
		return result;
	}
	
	//调减
	public JSONObject getSuccJson(TreeSet<InfoPayBalance> all , List<InfoPayBalance> adjusted){
		List<BalanceDto>  dtos=new ArrayList<BalanceDto>();
		
		for(InfoPayBalance iter : all){
			boolean isAdjusted=false;
			for(InfoPayBalance it : adjusted){
				
				if(iter.getBalance_id().equals(it.getBalance_id())){
					BalanceDto dto=new BalanceDto();
					dto.setBalance(it.getBalance());
					String exp=it.getExp_date();
					String expDate=exp.substring(0,4)+"-"+exp.substring(4,6)+"-"+exp.substring(6,8)+" "+exp.substring(8,10)+"-"+exp.substring(10,12)+"-"+exp.substring(12,14);
					
					dto.setExpDate(expDate);
					dtos.add(dto);
					isAdjusted=true;
					break;
				}
			}
			if(!isAdjusted){
				BalanceDto dto=new BalanceDto();
				dto.setBalance(iter.getBalance());
				String exp=iter.getExp_date();
				String expDate=exp.substring(0,4)+"-"+exp.substring(4,6)+"-"+exp.substring(6,8)+" "+exp.substring(8,10)+"-"+exp.substring(10,12)+"-"+exp.substring(12,14);
				
				dto.setExpDate(expDate);
				dtos.add(dto);
			}
			
		}
		
		return getJsonUseList(dtos);
		
	}
	
	
	private boolean isExists(String userId) {
		
		List<InfoUser> listInfoUser= S.get(InfoUser.class).query(Condition.build("queryInfoUserByUserId").filter("userId", userId));
		log.debug("根据userid=["+userId+"]找到用户数量"+listInfoUser.size());
		if(listInfoUser.size()>0){
			return true;
		}
		return false;
	}
	
	
	public LogTradeHis initLogTradeHis(String userId ,long bvalue ,String mode){
		LogTradeHis trade=new LogTradeHis();
		String tradeId=Common.getUUID();
		trade.setTrade_id(tradeId);
		trade.setTrade_type_code("506");
		trade.setExternal_system_code(Constants.JDPinRelation_ExternalSysCode);
		trade.setChannel_type("115");
		trade.setUser_id(userId);
		trade.setPartition_id(Calendar.getInstance().get(Calendar.MONTH)+1);
		if(mode.equals("1")){	//调增
			trade.setBalance(bvalue);
		}else{			//调减
			trade.setBalance(0-bvalue);
		}
		trade.setProcess_tag(2);	//成功
		String now=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		trade.setTrade_time(now);
		trade.setProcess_time(now);
		trade.setRemark("succ");
		
		return trade;
		
	}
	
	
	public LogTradeAdjustHis  initLogTradeAdjustHis(LogTradeHis trade, String adjustMode ,long old ,long newTotal ,String reason){
		LogTradeAdjustHis  adjustHis=new LogTradeAdjustHis();
		adjustHis.setTrade_id(trade.getTrade_id());
		adjustHis.setUser_id(trade.getUser_id());
		adjustHis.setPartition_id(trade.getPartition_id());
		adjustHis.setAdjust_mode(adjustMode);
		adjustHis.setBalance_type_id(0);
		adjustHis.setUnit_type_id(0);
		adjustHis.setAdjust_fee(trade.getBalance());
		adjustHis.setAdjust_time(trade.getTrade_time());
		adjustHis.setOld_balance(old);
		adjustHis.setNew_balance(newTotal);
		adjustHis.setAdjust_reason(reason);
		return adjustHis;
	}
	
	
	boolean checkPhoneNumber(String userId , String phone){
		if(!Common.isMobileNumber(phone)){
			return false;
		}
		
		String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		InfoUserExternalAccount external=db.queryPinRelation(userId ,currentTime);
		if(external==null){
			return false;
		}
		
		String number=external.getExternal_account_code();
		if(!phone.equals(number)){
			return false;
		}
		
		return true;
	}
	
	
	
	
}
