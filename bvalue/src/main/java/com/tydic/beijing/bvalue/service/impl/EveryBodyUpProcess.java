package com.tydic.beijing.bvalue.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.core.InfoUserDto;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LogTrade;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LogTradeShoppingHis;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class EveryBodyUpProcess {
	
	private static Logger log=Logger.getLogger(EveryBodyUpProcess.class);
	
	@Autowired
	InfoUserDto infoUserDto;
//	@Autowired
//	InfoPayBalanceSync infoPayBalanceSync;
	
	@Transactional(rollbackFor=Exception.class)
	public void generateBvalue(List<String> listUserId) throws Exception {
		
		if(listUserId.size() ==0){
			return ;
		}
		
		for(String tmpuserid:listUserId){
			dealOneUser(tmpuserid);
		}
		
		
	}
	
	public void dealOneUser(String strLine) throws Exception {

		String[] allinfos = strLine.split("\t");
		String jdPin = allinfos[0];
		 
		String sumamount = allinfos[1];
		if(jdPin.equals("jdpin") && sumamount.equals("amount")){
			return;
		}
		
		String userId = Common.md5(jdPin);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String createTime = sdf.format(new Date());
		log.debug("creatTime==>"+createTime +"and jdpin==["+jdPin+"],sumamount =["+sumamount+"],and userid=["+userId+"]");
		long bValuea =0L;  //汇总金额后得到的初始B值
		//long bValueb =0L;   //扣减欠费后的B值
		long bValue_normal =0L;  //最终要归档的正常B值
		long bValue_overflow =0L; //最终要归档的溢出B值
		
		//全民盛宴b值
		long happyValue =0L;
		long happyValue_normal =0L;
		long happyValue_overflow =0L;
		long happy_final =200L;
		
		long sumAmount = Long.parseLong(sumamount); //总消费金额  单位 分
		
		if(sumAmount<0){
			return;
		}
		
		List<InfoPayBalance> changedInfoPayBalance = new ArrayList<InfoPayBalance>();
		List<InfoPayBalance> oweInfoPayBalance = new ArrayList<InfoPayBalance>();
		String tradeId = Common.getUUID();
		List<InfoPayBalance> listInfoPayBalance = new ArrayList<InfoPayBalance>();
		
		long inittime =System.currentTimeMillis();
		//从购物赠表获取购物记录
//		List<OriginalShoppingFile> listOriginalShoppingFile = getAllShoppingInfos(userId);	
//		log.debug("listOriginalShoppingFile.size()==>"+listOriginalShoppingFile.size());
//		
//		long time2 =System.currentTimeMillis();
//		log.debug("用户"+strLine+"查询购物记录耗时"+(time2 - inittime));
//		
//		//先判断该用户ID是否已经处理过了
//		if( listOriginalShoppingFile.size() ==0 ||isDealt(listOriginalShoppingFile) ){
//			return;
//		}
//		
//		String jdPin = listOriginalShoppingFile.get(0).getJdpin();
//		
		List<InfoUserExternalAccount> listinfoUserExternalAccount = getInfoUserExternalAccountByUserId(userId,createTime);
	
//		long time3 =System.currentTimeMillis();
//		log.debug("用户"+strLine+"查询关联信息耗时"+(time3 - time2));
		
//		List<InfoUserExternalAccountAttr> listAttr = getAllExternalAccountAttrByUserId(userId,infoUserExternalAccount);
		
//		long time4 =System.currentTimeMillis();
//		log.debug("用户"+strLine+"查询属性耗时"+(time4 - time3));
		

								
				//开户
				infoUserDto.createInfoUser(jdPin, "110", createTime, new LogTrade());
				
				//log.debug("sumAmount==>"+sumAmount);	
				//折算B值 2元折算1B，向上取整
				bValuea = getbValuea(sumAmount,userId);
				log.debug("sumAmount==>"+sumAmount+"get bvaluea"+bValuea);
				
				//获得全民盛宴总B值
				happyValue = getHappyValue(sumAmount); 
				happyValue_normal =  happyValue > 2000? 2000:happyValue;
				happyValue_overflow = happyValue > happyValue_normal ? happyValue - happyValue_normal :0;

				//查到所有余额
				listInfoPayBalance =  getInfoPayBalanceByUserId(userId);
				log.debug("获得的账本"+listInfoPayBalance.size());
		
				//查询是否有往期B值欠费, 不再单独做抵扣
				//bValueb = getOwe(bValuea,listInfoPayBalance,oweInfoPayBalance);
				//折算B值和溢出B值
				bValue_normal = getNormalBValue(bValuea);
				bValue_overflow =  bValuea - bValue_normal;
				
				if(bValue_normal <0 || bValue_overflow <0){
					
					throw new BValueException(-80002,"获得的赠送B值和溢出B值都不能小于0");
				}

				//更新账本表
				bValue_normal = bValue_normal+happyValue_normal+ happy_final ;
				bValue_overflow = bValue_overflow + happyValue_overflow;
				
				long maxvalue =0L;
				long changeValue =0L;
				if(listinfoUserExternalAccount.size() > 0){
					maxvalue = 2700;
				} else {
					maxvalue =1000;
				}
				
				changeValue = bValue_normal >maxvalue ?bValue_normal-maxvalue :0;
				
				bValue_normal = bValue_normal - changeValue;
				
				bValue_overflow = bValue_overflow + changeValue;
				
				
			    updateNormalInfoPayBalance(bValue_normal,0,createTime,listInfoPayBalance,userId,tradeId,changedInfoPayBalance);  //正常B值
			    updateOverFlowInfoPayBalance(bValue_overflow ,1,createTime,listInfoPayBalance,userId,tradeId); //溢出B值

				//更新订单表
				updateLogTradeHis(userId,jdPin,createTime,tradeId,bValue_normal,bValue_overflow,sumAmount);
				//更新购物赠订单历史表
				//updateLogTradeShoppingHis(userId,jdPin,createTime,tradeId,listOriginalShoppingFile);
				//更新购物赠入库表状态
				//updateOriginalShoppingFile(userId,tradeId,createTime);

				//更新到redis
				//updateRedis(changedInfoPayBalance);
				


		
		
	}
	
	
	//全民盛宴B值
	private long getHappyValue(long sumAmount) {
		
		//long hpmoney = sumAmount /100;
		long hpvalue =  sumAmount  / (15*100);
		if(sumAmount > hpvalue*1500){
			hpvalue ++;
		}
		return hpvalue;
	}

	private void updateOverFlowInfoPayBalance(long bValue, int balanceType,
			String createTime, List<InfoPayBalance> listInfoPayBalance,String userId,String tradeId) {
		
		if(bValue == 0){
			return;
		}

		String effDate;
		String expDate;
		String createYear = createTime.substring(0, 4);
		long oldvalue =0L;
		long newvalue =0L;
 
		InfoPayBalance tmpinfoPayBalance = null;
		log.debug("createDate==>"+createYear);
	
		if(createTime.compareTo(createYear+"0701000000") >=0 ){
			effDate = createYear+"0701000000";
			expDate = (Integer.parseInt(createYear)+1) +"0630235959";
		} else {
			effDate = createYear+"0101000000";
			expDate = createYear+"1231235959";
		}
		
		//看是否有符合条件的账本，有则更新到该账本，没有的话要新建账本
		for(InfoPayBalance infoPayBalance :listInfoPayBalance){
			
			if(infoPayBalance.getBalance_type_id() == balanceType && 
					infoPayBalance.getEff_date().equals(effDate) && infoPayBalance.getExp_date().equals(expDate)){
				tmpinfoPayBalance = infoPayBalance;
				break;
			}
		}
		
		if(tmpinfoPayBalance !=null){
			//找到了可用账本
			oldvalue = tmpinfoPayBalance.getBalance();
			newvalue = oldvalue + bValue;
			tmpinfoPayBalance.setBalance(bValue);
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);
			//S.get(InfoPayBalance.class).update(tmpinfoPayBalance);
			
			//TODO 
			Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("user_id", tmpinfoPayBalance.getUser_id());
			filter.put("balance_id", tmpinfoPayBalance.getBalance_id());
			filter.put("balance", bValue);
			filter.put("eff_date", effDate);
			filter.put("exp_date", expDate);
			S.get(InfoPayBalance.class).batch(Condition.build("updateBalanceAndExpDate").filter(filter));
				
		}else{
			//没有找到可用账本
			tmpinfoPayBalance =  new InfoPayBalance();
			tmpinfoPayBalance.setBalance(bValue);
			tmpinfoPayBalance.setUser_id(userId);
			tmpinfoPayBalance.setBalance_id(Common.getUUID());
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);
			tmpinfoPayBalance.setBalance_type_id(balanceType);
			S.get(InfoPayBalance.class).create(tmpinfoPayBalance);
			
			newvalue = bValue;
		}
		
		BalanceAccessLog balanceAccessLog = new BalanceAccessLog();
		balanceAccessLog.setBalance_Id(tmpinfoPayBalance.getBalance_id());
		balanceAccessLog.setAccess_Tag("0");
		balanceAccessLog.setBalance_Type_Id(balanceType);
		balanceAccessLog.setMoney(bValue);
		balanceAccessLog.setNew_Balance(newvalue);
		balanceAccessLog.setOld_Balance(oldvalue);
		balanceAccessLog.setOperate_Time(createTime);
		balanceAccessLog.setPartition_Id(Integer.parseInt(createTime.substring(4,6)));
		balanceAccessLog.setTrade_Id(tradeId);
		balanceAccessLog.setTrade_Type_Code("102");
		balanceAccessLog.setUser_Id(userId);
		
		S.get(BalanceAccessLog.class).create(balanceAccessLog);   
		
		tmpinfoPayBalance.setBalance(newvalue); 
		
	}

	private void updateNormalInfoPayBalance(long bValue, int balanceType,
			String createTime, List<InfoPayBalance> listInfoPayBalance,String userId,String tradeId,List<InfoPayBalance> changedInfoPayBalance) {
		
		if(bValue == 0){
			return;
		}

		String effDate;
		String expDate;
		String createYear = createTime.substring(0, 4);
		long oldvalue =0L;
		long newvalue =0L;
 
		InfoPayBalance tmpinfoPayBalance = null;
		log.debug("createDate==>"+createYear);
	
		if(createTime.compareTo(createYear+"0701000000") >=0 ){
			effDate = createYear+"0701000000";
			expDate = (Integer.parseInt(createYear)+1) +"0630235959";
		} else {
			effDate = createYear+"0101000000";
			expDate = createYear+"1231235959";
		}
		
		//看是否有符合条件的账本，有则更新到该账本，没有的话要新建账本
		for(InfoPayBalance infoPayBalance :listInfoPayBalance){
			
			if(infoPayBalance.getBalance_type_id() == balanceType && 
					infoPayBalance.getEff_date().equals(effDate) && infoPayBalance.getExp_date().equals(expDate)){
				tmpinfoPayBalance = infoPayBalance;
				break;
			}
		}
		
		if(tmpinfoPayBalance !=null){
			//找到了可用账本
			oldvalue = tmpinfoPayBalance.getBalance();
			newvalue = oldvalue + bValue;
			tmpinfoPayBalance.setBalance(bValue);
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);
			//S.get(InfoPayBalance.class).update(tmpinfoPayBalance);
			
			//TODO 
			Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("user_id", tmpinfoPayBalance.getUser_id());
			filter.put("balance_id", tmpinfoPayBalance.getBalance_id());
			filter.put("balance", bValue);
			filter.put("eff_date", effDate);
			filter.put("exp_date", expDate);
			S.get(InfoPayBalance.class).batch(Condition.build("updateBalanceAndExpDate").filter(filter));
				
		}else{
			//没有找到可用账本
			tmpinfoPayBalance =  new InfoPayBalance();
			tmpinfoPayBalance.setBalance(bValue);
			tmpinfoPayBalance.setUser_id(userId);
			tmpinfoPayBalance.setBalance_id(Common.getUUID());
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);
			tmpinfoPayBalance.setBalance_type_id(balanceType);
			S.get(InfoPayBalance.class).create(tmpinfoPayBalance);
			
			newvalue = bValue;
		}
		
		BalanceAccessLog balanceAccessLog = new BalanceAccessLog();
		balanceAccessLog.setBalance_Id(tmpinfoPayBalance.getBalance_id());
		balanceAccessLog.setAccess_Tag("0");
		balanceAccessLog.setBalance_Type_Id(balanceType);
		balanceAccessLog.setMoney(bValue);
		balanceAccessLog.setNew_Balance(newvalue);
		balanceAccessLog.setOld_Balance(oldvalue);
		balanceAccessLog.setOperate_Time(createTime);
		balanceAccessLog.setPartition_Id(Integer.parseInt(createTime.substring(4,6)));
		balanceAccessLog.setTrade_Id(tradeId);
		balanceAccessLog.setTrade_Type_Code("102");
		balanceAccessLog.setUser_Id(userId);
		
		S.get(BalanceAccessLog.class).create(balanceAccessLog);   
		
		tmpinfoPayBalance.setBalance(newvalue);
		changedInfoPayBalance.add(tmpinfoPayBalance);
		
	}

	private List<InfoPayBalance> getInfoPayBalanceByUserId(String userId) {
 
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("user_id", userId);
		return S.get(InfoPayBalance.class).query(Condition.build("getInfoPayBalanceByUserId").filter(filter));
	}

	private List<InfoUserExternalAccount> getInfoUserExternalAccountByUserId(
			String userId,String currtime) throws Exception {
		
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("user_id", userId);
		filter.put("currDate", currtime);
		List<InfoUserExternalAccount> listExternalAccount = 
		 S.get(InfoUserExternalAccount.class).query(Condition.build("byUserid").filter(filter));
 
		
		return listExternalAccount;
	}

	private boolean presentFlag(List<InfoUserExternalAccountAttr> listAttr) {
		//根据用户产品、订单范围、用户范围等判断是否赠送B值
		//联通自由行  联通合约产品 电信自由行 电信自由行合约 目前只有这四个产品才赠送B值
		String param = "";
		for(InfoUserExternalAccountAttr tmpiueaa:listAttr){
			if(tmpiueaa.getAttr_code().equals("PRODUCT")){
				param = tmpiueaa.getAttr_value();
				break;
			}
		}
		//TODO 上线后需要细化，以及电信产品上线后需要补充电信产品等

		return true;
	}

	private List<InfoUserExternalAccountAttr> getAllExternalAccountAttrByUserId(
			String userId, InfoUserExternalAccount infoUserExternalAccount) {

		 List<InfoUserExternalAccountAttr> listInfoUserExternalAccountAttr = new ArrayList<InfoUserExternalAccountAttr>();
		 String externallAccountId =  infoUserExternalAccount.getExternal_account_id();
		 if(externallAccountId == null || externallAccountId.length() ==0){
			 return listInfoUserExternalAccountAttr;
		 }
		 
		 Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("user_id", userId);
			filter.put("external_account_id", externallAccountId);
		 listInfoUserExternalAccountAttr =  S.get(InfoUserExternalAccountAttr.class).query(Condition.build("getExternalAccountAttrbyUserIdAndExternal").filter(filter));
		 
		return listInfoUserExternalAccountAttr;
	}

//	private void updateRedis(List<InfoPayBalance>  listInfoPayBalance) throws Exception{
//		
//		for(InfoPayBalance tmpipb:listInfoPayBalance){
//			
//			if(tmpipb.getBalance_type_id() !=0){
//				continue;
//			}
//			
//			int retvalue = infoPayBalanceSync.sync(tmpipb);
//			if(retvalue != 0 ){
//				//同步失败，抛异常
//				throw new BValueException(-80005,"同步Redis失败");
//			}
//		}
//		
//	}




	private void updateLogTradeHis(String userId, String jdPin,
			String createTime, String tradeId,long balance,long overtopvalue,long sumAmount) {

		LogTradeHis logTradeHis = new LogTradeHis();
		logTradeHis.setTrade_id(tradeId);
		logTradeHis.setTrade_type_code("102");
		logTradeHis.setExternal_system_code("10000");
		logTradeHis.setUser_id(userId);
		logTradeHis.setChannel_type("110"); //后台BOSS渠道
		logTradeHis.setPartition_id(Integer.parseInt(createTime.substring(4,6)));
		logTradeHis.setProcess_tag(0L);
		logTradeHis.setTrade_time(createTime);
		logTradeHis.setBalance(balance);
		logTradeHis.setRemark("购物赠");
		logTradeHis.setOrder_amount(sumAmount);
		logTradeHis.setOvertop_value(overtopvalue);
		logTradeHis.setOrder_completion_time(new SimpleDateFormat("YYYYMMDDHHmmss").format(new Date()));
        S.get(LogTradeHis.class).create(logTradeHis);
        
	}

 

	private long getNormalBValue(long bValueb) {

		long retNormalValue =0L;
	 
			retNormalValue = bValueb >500 ?500:bValueb;			
	 
 
		return retNormalValue;
	}

	private long getOwe(long bValuea, List<InfoPayBalance> listInfoPayBalance,List<InfoPayBalance> oweInfoPayBalance) {
		// 检查是否有正常b值欠费，并把具体欠费记录到oweinfopaybalance
		long retValue = bValuea;
		for(int i=0;i<listInfoPayBalance.size();i++){
			if(listInfoPayBalance.get(i).getBalance() <0 && listInfoPayBalance.get(i).getBalance_type_id() ==0){
				//判断本次赠送的B值是否足够抵扣欠费，不足则扣光赠送值，够则把balance扣到0为止
				long deductvalue = 0L;
				if(retValue > listInfoPayBalance.get(i).getBalance()*-1){
					deductvalue = listInfoPayBalance.get(i).getBalance()*-1;
					retValue = retValue - deductvalue;
//					listInfoPayBalance.get(i).addBValue(deductvalue);
//					//更新info_pay_balance表
//				    updateBalanceOfInfoPayBalance(deductvalue,listInfoPayBalance.get(i));
				} else {
					deductvalue = retValue;
					retValue = 0;
					//更新info_pay_balance表 改为后面一起更新infopaybalance
//					listInfoPayBalance.get(i).addBValue(deductvalue);
//					updateBalanceOfInfoPayBalance(deductvalue,listInfoPayBalance.get(i));
				}
				
				oweInfoPayBalance.add(listInfoPayBalance.get(i));

			}
			
			if(retValue ==0){ //如果retValue ==0 退出循环，不再遍历用户欠费
				break;
			}
		}
		return retValue;
	}

	private void updateBalanceOfInfoPayBalance(long deductvalue,
			InfoPayBalance infoPayBalance) {
		 
		infoPayBalance.setBalance(deductvalue);
		S.get(InfoPayBalance.class).update(infoPayBalance);
	 
	}

	private long getbValuea(long sumAmount, String userId) {
		//2元1B值，向上取整
		long roundB = sumAmount /200;
		if( sumAmount > 200* roundB){
			roundB ++;
		}
		return roundB;
	}


	



	

}
