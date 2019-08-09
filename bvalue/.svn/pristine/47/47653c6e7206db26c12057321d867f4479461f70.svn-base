package com.tydic.beijing.bvalue.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.tydic.beijing.bvalue.dao.ShoppingDetail;
import com.tydic.beijing.bvalue.dao.ShoppingSumInfo;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class UpdateBValueByShoppingFileProcess {
	
	private static Logger log=Logger.getLogger(UpdateBValueByShoppingFileProcess.class);
	
	@Autowired
	InfoUserDto infoUserDto;
//	201706去除redis
//	@Autowired
//	InfoPayBalanceSync infoPayBalanceSync;
	
	private String yearMonth;
	
	private final int max_unrela_all = 500;
	private final int max_rela_month = 500;
	

	
	@Transactional(rollbackFor=Exception.class)
	public void generateBvalue(List<String> listUserId) throws Exception {
		
		log.debug("----------------------------generateBvalue start---------------------");
		if(listUserId.size() ==0){
			return ;
		}
		
		for(String tmpuserid:listUserId){
			dealOneUser(tmpuserid);
		}
		
		
	}
	
	public void dealOneUser(String strLine) throws Exception {

		ShoppingSumInfo shoppingSumInfo = getJSONFromString(strLine);
		
		
		String jdPin = shoppingSumInfo.getJdpin();
		String userId = Common.md5(jdPin);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String createTime = sdf.format(new Date());
		log.debug("creatTime==>"+createTime+",and userid="+userId);
		long bValuea =0L;  //汇总金额后得到的初始B值
		//long bValueb =0L;   //扣减欠费后的B值
		long bValue_normal =0L;  //最终要归档的正常B值
		long bValue_overflow =0L; //最终要归档的溢出B值
		
		long sumAmount = Long.parseLong(shoppingSumInfo.getAmount()); //总消费金额  单位 分
		if(sumAmount <= 0){
			return;
		}
		
		List<InfoPayBalance> changedInfoPayBalance = new ArrayList<InfoPayBalance>();
		List<InfoPayBalance> oweInfoPayBalance = new ArrayList<InfoPayBalance>();
		String tradeId = Common.getUUID();
		List<InfoPayBalance> listInfoPayBalance = new ArrayList<InfoPayBalance>();
		
		long inittime =System.currentTimeMillis();
	    //获取关联信息
		InfoUserExternalAccount infoUserExternalAccount = getInfoUserExternalAccountByUserId(userId,createTime);
        //获取关联属性信息 目前关联属性信息没有什么用，暂取消
		//List<InfoUserExternalAccountAttr> listAttr = getAllExternalAccountAttrByUserId(userId,infoUserExternalAccount);
		
		if(true){ //presentFlag(listAttr)
								
				//开户
				infoUserDto.createInfoUser(jdPin, "110", createTime, new LogTrade());

				//折算B值 2元折算1B，向上取整
				bValuea = getbValuea(sumAmount,userId);
				log.debug("get bvaluea"+bValuea);
				
				//查到所有余额
				listInfoPayBalance =  getInfoPayBalanceByUserId(userId);
				log.debug("获得的账本"+listInfoPayBalance.size());
		 
				//查询是否有往期B值欠费, 不再单独做抵扣
				//bValueb = getOwe(bValuea,listInfoPayBalance,oweInfoPayBalance);
				//折算B值和溢出B值
				bValue_normal = getNormalBValue(bValuea,userId,infoUserExternalAccount,listInfoPayBalance,oweInfoPayBalance);
				bValue_overflow =  bValuea - bValue_normal;
				
				if(bValue_normal <0 || bValue_overflow <0){
					throw new BValueException(-80002,"获得的赠送B值和溢出B值都不能小于0");
				}

				if(oweInfoPayBalance.size() >1){
					throw new BValueException(-80003,"出现了多条欠费记录，异常");
				}
				//更新账本表
			    updateNormalInfoPayBalance(bValue_normal,0,createTime,listInfoPayBalance,userId,tradeId,changedInfoPayBalance,oweInfoPayBalance);  //正常B值
			    updateOverFlowInfoPayBalance(bValue_overflow ,1,yearMonth,listInfoPayBalance,userId,tradeId); //溢出B值

				
				//更新订单表
				updateLogTradeHis(userId,jdPin,createTime,tradeId,bValue_normal,bValue_overflow,Long.parseLong(shoppingSumInfo.getAmount()));
				//更新购物赠订单历史表
				updateLogTradeShoppingHis(userId,jdPin,createTime,tradeId,shoppingSumInfo);

				//更新到redis
				//updateRedis(changedInfoPayBalance);
				 
		}

		long endtime =System.currentTimeMillis();
		log.debug("一个用户入库耗时"+(endtime-inittime));
		 
	}
	
	
	private ShoppingSumInfo getJSONFromString(String strLine) throws Exception  {

			return   (ShoppingSumInfo) JSONObject.toBean(JSONObject.fromObject(strLine), ShoppingSumInfo.class);
		 
	}
 

	private void updateOverFlowInfoPayBalance(long bValue, int balanceType,
			String yearMonth, List<InfoPayBalance> listInfoPayBalance,String userId,String tradeId) { //,List<InfoPayBalance> changedInfoPayBalance
		
		if(bValue == 0){
			return;
		}

		String effDate;
		String expDate;
		long oldvalue =0L;
		long newvalue =0L;
 
		InfoPayBalance tmpinfoPayBalance = null;
		log.debug("yearMonth==>"+yearMonth);
	
		//溢出账本按月来计算
		effDate = yearMonth +"01000000";
		expDate = getLastDayofMonth(yearMonth)+"235959";
		log.debug("账本时间"+effDate+",exp="+expDate);
		
		
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
		balanceAccessLog.setOperate_Time(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		balanceAccessLog.setPartition_Id(Integer.parseInt(yearMonth.substring(4,6)));
		balanceAccessLog.setTrade_Id(tradeId);
		balanceAccessLog.setTrade_Type_Code("104");
		balanceAccessLog.setUser_Id(userId);
		
		S.get(BalanceAccessLog.class).create(balanceAccessLog);   
		
		tmpinfoPayBalance.setBalance(newvalue);
		//changedInfoPayBalance.add(tmpinfoPayBalance);
		
	}

	private String getLastDayofMonth(String yearMonth) {
		
		int year = Integer.parseInt(yearMonth.substring(0,4));
		int month = Integer.parseInt(yearMonth.substring(4,6));
			
		Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.YEAR, year);  
        cal.set(Calendar.MONTH, month -1);  
        // 某年某月的最后一天  
        return yearMonth+cal.getActualMaximum(Calendar.DATE); 
		
	}

	private void updateNormalInfoPayBalance(long bValue, int balanceType,
			String createTime, List<InfoPayBalance> listInfoPayBalance,String userId,String tradeId,List<InfoPayBalance> changedInfoPayBalance,List<InfoPayBalance> oweInfoPayBalance) {
		
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
		if(oweInfoPayBalance.size()>0){
			tmpinfoPayBalance = oweInfoPayBalance.get(0);
		}else{
			for(InfoPayBalance infoPayBalance :listInfoPayBalance){
				
				if(infoPayBalance.getBalance_type_id() == balanceType && 
						infoPayBalance.getEff_date().equals(effDate) && infoPayBalance.getExp_date().equals(expDate)){
					tmpinfoPayBalance = infoPayBalance;
					break;
				}
			}
		}
			
		
		if(tmpinfoPayBalance !=null){
			//找到了可用账本
			oldvalue = tmpinfoPayBalance.getBalance();
			newvalue = oldvalue + bValue;
			tmpinfoPayBalance.setBalance(bValue);
			if(newvalue < 0){
				effDate = tmpinfoPayBalance.getEff_date();//如果是销欠费，可能不能完全销清，这样的话就不能修改原生失效时间
				expDate = tmpinfoPayBalance.getExp_date();
			}
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);
			//S.get(InfoPayBalance.class).update(tmpinfoPayBalance);
			
			
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
		balanceAccessLog.setPartition_Id(Integer.parseInt(yearMonth.substring(4,6)));
		balanceAccessLog.setTrade_Id(tradeId);
		balanceAccessLog.setTrade_Type_Code("104");
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

	private InfoUserExternalAccount getInfoUserExternalAccountByUserId(
			String userId,String currTime) throws Exception {
		
		InfoUserExternalAccount infoUserExternalAccount = new InfoUserExternalAccount();
//		List<InfoUserExternalAccount> listExternalAccount = 
//		 S.get(InfoUserExternalAccount.class).query(Condition.build("byUserid").filter("user_id", userId).filter("currDate",currTime));
//		
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("user_id", userId);
		filter.put("currDate", currTime);
		List<InfoUserExternalAccount> listExternalAccount = 
		 S.get(InfoUserExternalAccount.class).query(Condition.build("byUserid").filter(filter));
		
		if(listExternalAccount.size()>1){
			throw new BValueException(-80004,"userId"+userId+"获得了多条关联信息，异常");
		}else if (listExternalAccount.size() ==1){
			infoUserExternalAccount = listExternalAccount.get(0);
		}
		
		return infoUserExternalAccount;
	}

	private boolean presentFlag(List<InfoUserExternalAccountAttr> listAttr) {
		//根据用户产品、订单范围、用户范围等判断是否赠送B值
		//联通自由行  联通合约产品 电信自由行 电信自由行合约 目前只有这四个产品才赠送B值
//		String param = "";
//		for(InfoUserExternalAccountAttr tmpiueaa:listAttr){
//			if(tmpiueaa.getAttr_code().equals("PRODUCT")){
//				param = tmpiueaa.getAttr_value();
//				break;
//			}
//		}
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

	private void updateRedis(List<InfoPayBalance>  listInfoPayBalance) throws Exception{
		
		for(InfoPayBalance tmpipb:listInfoPayBalance){
			
			if(tmpipb.getBalance_type_id() !=0){
				continue;
			}
			
//			int retvalue = infoPayBalanceSync.sync(tmpipb);
//			if(retvalue != 0 ){
//				//同步失败，抛异常
//				throw new BValueException(-80005,"同步Redis失败");
//			}
		}
		
	}
 

	private void updateLogTradeShoppingHis(String userId, String jdPin,
			String createTime, String tradeId, ShoppingSumInfo shoppingSumInfo) {
           //TODO 这个地方有待优化，vds不支持批量iNsert
			 JSONArray jsonarray = JSONArray.fromObject(shoppingSumInfo.getItems()); 

		for(int i = 0 ;i<jsonarray.size();i++){
			 ShoppingDetail tmpsd = (ShoppingDetail) JSONObject.toBean(JSONObject.fromObject(jsonarray.get(i)),ShoppingDetail.class);
			 
			LogTradeShoppingHis logTradeShoppingHis = new LogTradeShoppingHis();
			logTradeShoppingHis.setUser_id(userId);
			logTradeShoppingHis.setTrade_id(tradeId);
			logTradeShoppingHis.setPartition_id(Integer.parseInt(yearMonth.substring(4,6)));
			logTradeShoppingHis.setOrder_no(tmpsd.getOrderno());
			logTradeShoppingHis.setOrder_type("1");
			logTradeShoppingHis.setOrder_completion_time(tmpsd.getCompletetime());  
			logTradeShoppingHis.setOrg_order_no(tmpsd.getOrgorderno());
			logTradeShoppingHis.setOrder_amount(Long.parseLong(tmpsd.getAmount()));
			logTradeShoppingHis.setProcess_tag(2);
			logTradeShoppingHis.setProcess_time(createTime);
			 S.get(LogTradeShoppingHis.class).create(logTradeShoppingHis);
		}
		
	}

	private void updateLogTradeHis(String userId, String jdPin,
			String createTime, String tradeId,long balance,long overtopvalue,long orderAmount) {

		LogTradeHis logTradeHis = new LogTradeHis();
		logTradeHis.setTrade_id(tradeId);
		logTradeHis.setTrade_type_code("104");  //购物赠送改为104 
		logTradeHis.setExternal_system_code("10000");
		logTradeHis.setUser_id(userId);
		logTradeHis.setChannel_type("110"); //后台BOSS渠道
		logTradeHis.setPartition_id(Integer.parseInt(yearMonth.substring(4,6)));
		logTradeHis.setProcess_tag(0L);
		logTradeHis.setTrade_time(createTime);
		logTradeHis.setProcess_time(createTime);
		logTradeHis.setBalance(balance);
		logTradeHis.setRemark("购物赠");
		logTradeHis.setOrder_amount(orderAmount);
		logTradeHis.setOvertop_value(overtopvalue);
		logTradeHis.setOrder_completion_time(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        S.get(LogTradeHis.class).create(logTradeHis);
        
	}

 

	private long getNormalBValue(long bValueb, String userId, InfoUserExternalAccount infoUserExternalAccount, List<InfoPayBalance> listInfoPayBalance,List<InfoPayBalance> oweInfoPayBalance) {

		long retNormalValue =0L;
		if(infoUserExternalAccount.getExternal_account_id() == null || infoUserExternalAccount.getExternal_account_id().length() ==0){
			log.debug("没有关联京东通信账户");
			//如果没有关联,总共不能超过500B
			//先获得当前用户有多少B值
			long haveBValue =0L;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String createTime = sdf.format(new Date());
			
			for(InfoPayBalance infoPayBalance :listInfoPayBalance){
				//如果是0类型,且在有效期内
				if( infoPayBalance.getBalance_type_id() ==0 &&
						infoPayBalance.getEff_date().compareTo(createTime) <=0 && infoPayBalance.getExp_date().compareTo(createTime) >=0
						){
					haveBValue = haveBValue + infoPayBalance.getBalance();
				}
				
				//未关联的用户也是有可能B值小于0的
				if(infoPayBalance.getBalance_type_id() ==0 && infoPayBalance.getBalance() <0){
					oweInfoPayBalance.add(infoPayBalance);
				}
					
			}
			
			log.debug("user "+userId+" have bvalue==>"+haveBValue);
			if(haveBValue >= max_unrela_all){ //未绑定的用户，总封顶值改为500
				retNormalValue = 0L; 
			} else {
				retNormalValue = (bValueb+haveBValue) >=max_unrela_all? (max_unrela_all-haveBValue):bValueb;
				log.debug("get NormalValue==>"+retNormalValue);
			}

		}else{
			//如果已关联，每月不超过500B 
			retNormalValue = bValueb >max_rela_month ?max_rela_month:bValueb;		
			//TODO 还要判断用户是否欠费，如果有欠费需要抵扣欠费 默认500B包含要抵扣的欠费  如果不包含还需要修改
			for(InfoPayBalance infoPayBalance :listInfoPayBalance){
				if(infoPayBalance.getBalance_type_id() ==0 && infoPayBalance.getBalance() <0){
					oweInfoPayBalance.add(infoPayBalance);
				}
			}
			
			
		}
 
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


	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	
	



	

}
