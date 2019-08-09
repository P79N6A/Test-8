package com.tydic.beijing.bvalue.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.HlpSmsSend;
import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.core.InfoUserDto;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LogTrade;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LogTradeShopping;
import com.tydic.beijing.bvalue.dao.LogTradeShoppingHis;
import com.tydic.beijing.bvalue.dao.ShoppingReject;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class ShoppingRejectProcess {
	
	private static Logger log=Logger.getLogger(ShoppingRejectProcess.class);
	
	@Autowired
	InfoUserDto infoUserDto;
	/**
	 * 
	 * 201706去除redis
	 */
//	@Autowired
//	InfoPayBalanceSync infoPayBalanceSync;
	
//	private String syncRedis;
	
	//private String yearMonth;

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
	
	
	public void dealOneUser(String strLine ) throws Exception {

		//yearMonth = thismonth;
		ShoppingReject shoppingReject = new ShoppingReject();
		String[] infos = strLine.split("\t");
		log.debug("infos.length==>"+infos.length);
		
		if(infos.length !=5){
			throw new BValueException(-80021,"退货记录"+strLine+"解析异常");
		}
		
		if(infos[0].equals("jdpin") && infos[1].equals("orderno") && infos[2].equals("amount")){
			return;
		}
		//顺序 jdpin|orderno|amount|completetime|orgorderno
		shoppingReject.setJdpin(infos[0]);
		shoppingReject.setOrderno(infos[1]);
		shoppingReject.setAmount(Long.parseLong(infos[2]));
		shoppingReject.setCompletetime(infos[3]);
		shoppingReject.setOrgorderno(infos[4]);
		 
		String jdPin = shoppingReject.getJdpin();
		long amount = shoppingReject.getAmount();
		if(amount >= 0){ //如果退货金额大于0，不做处理
			return;
		}
		
		amount = amount *-1;
		
		String yearMonth = shoppingReject.getCompletetime().substring(0,8);
		String userId = Common.md5(jdPin);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String createTime = sdf.format(new Date());
		log.debug("creatTime==>"+createTime+",and userid="+userId);
		long bValueForReject =0L;  //需要退减的总B值
		long bValueForReject_1 =0L;  //需要从溢出账本扣减的B值
		long bValueForReject_0 =0l; //需要从正常账本扣减的B值
		long oldTotalBValue =0L;
 
		//判断退货号是否已经存在 add zxm
		String orderNo = shoppingReject.getOrderno();
		boolean isOrderNoExits = isOrderNoExits(userId, orderNo);
		if(isOrderNoExits)
		{
			return;
		}
				
		List<InfoPayBalance> changedInfoPayBalance = new ArrayList<InfoPayBalance>();
		String tradeId = Common.getUUID();
		List<InfoPayBalance> listInfoPayBalance = new ArrayList<InfoPayBalance>();
		InfoPayBalance topFlowInfoPayBalance = new InfoPayBalance();
		
		long inittime =System.currentTimeMillis();
	    //获取关联信息
		InfoUserExternalAccount infoUserExternalAccount = getInfoUserExternalAccountByUserId(userId,createTime);
        //获取关联属性信息 目前关联属性信息没有什么用，暂取消
		//List<InfoUserExternalAccountAttr> listAttr = getAllExternalAccountAttrByUserId(userId,infoUserExternalAccount);
		
		//
		
		if(true){ //presentFlag(listAttr)
								
				//默认已经开过户，不在做开户判断
			infoUserDto.createInfoUser(jdPin, "110", createTime, new LogTrade());
			
			//购物赠明细是否存在该购物记录
			List<LogTradeShoppingHis> listLogTradeShoppingHis = getShoppingDetail(userId,shoppingReject.getOrgorderno());
			if(listLogTradeShoppingHis.size()==0){
				//没有找到购物记录，就记录到logtradeshopping表
				createLogTradeShopping(userId,jdPin,createTime,tradeId,shoppingReject);				
				return;
				//throw new BValueException(-80009,"原订单"+shoppingReject.getOrgorderno()+"没有找到购物记录");
			}
			
			//如果不是上个月的购物记录，则直接返回
			LogTradeShoppingHis logShoppingHis = listLogTradeShoppingHis.get(0);
			if(!isLastMonth(logShoppingHis.getOrder_completion_time(),yearMonth)){ //退货文件里的完成时间和购物的时间做比较
				log.debug("原订单完成时间"+logShoppingHis.getOrder_completion_time() +"不是"+createTime+"上个月");
				return;
			}
			
			if(listLogTradeShoppingHis.get(0).getOrder_amount() < amount){
//				log.debug("原订单金额"+listLogTradeShoppingHis.get(0).getOrder_amount() +"小于退货记录金额"+amount);
//				throw new BValueException(-80011,"原订单"+shoppingReject.getOrgorderno()+"的金额小于退货文件中的金额");
				amount = listLogTradeShoppingHis.get(0).getOrder_amount(); //如果原始订单金额小于退货金额，则退货金额以原始订单为准
			}

				//折算B值 2元折算1B，向上取整
			bValueForReject = getbValuea(amount);
				log.debug("get bValueForReject"+bValueForReject);
				
				//查到所有余额
				listInfoPayBalance =  getInfoPayBalanceByUserId(userId);
				log.debug("获得的账本"+listInfoPayBalance.size());
				
				for(InfoPayBalance tmpipb:listInfoPayBalance){
					if(tmpipb.getBalance_type_id()==0 && tmpipb.getExp_date().compareTo(sdf.format(new Date())) >= 0 ){ //类型是0并且时间在有效期内
						oldTotalBValue = oldTotalBValue + tmpipb.getBalance();
					}
				}
		 
				//查询是否有往期B值欠费, 不再单独做抵扣
				//bValueb = getOwe(bValuea,listInfoPayBalance,oweInfoPayBalance);
				//折算B值和溢出B值
				topFlowInfoPayBalance = getTopFlowInfoPayBalance(logShoppingHis.getTrade_id(),userId,1);  //1是购物赠的溢出账本类型
				
				bValueForReject_1 = getOverFlowBValue(bValueForReject,topFlowInfoPayBalance); //改为直接到数据库查询溢出账本
				bValueForReject_0 =  bValueForReject -bValueForReject_1;
				
				//log.debug("方法外获得溢出账本"+topFlowInfoPayBalance.getBalance_id());
				
				if(bValueForReject_1 <0 || bValueForReject_0 <0){
					throw new BValueException(-80002,"获得的赠送B值和溢出B值都不能小于0");
				}
 
				//更新账本表
				updateOverFlowInfoPayBalance(bValueForReject_1 ,1,yearMonth,listInfoPayBalance,userId,tradeId,topFlowInfoPayBalance); //溢出B值
				//modify zxm，不传帐本类型的参数了
			    updateNormalInfoPayBalance(bValueForReject_0,createTime,listInfoPayBalance,userId,tradeId,changedInfoPayBalance);  //正常B值
			
				//更新订单表
				updateLogTradeHis(userId,jdPin,createTime,tradeId,bValueForReject_0,bValueForReject_1,shoppingReject.getAmount());
				//更新购物赠订单历史表
				updateLogTradeShoppingHis(userId,jdPin,createTime,tradeId,shoppingReject);

				//发送提醒短信  暂 不发短信
//				String deviceNumber = infoUserExternalAccount.getExternal_account_code();
//				sendSms(deviceNumber,bValueForReject_0,oldTotalBValue-bValueForReject_0);
				
				//更新到redis
//				if(syncRedis.equals("Y")){
//					updateRedis(changedInfoPayBalance);
//				}
				
				 
		}

		long endtime =System.currentTimeMillis();
		log.debug("一个用户入库耗时"+(endtime-inittime));
		 
	}
	

	private InfoPayBalance getTopFlowInfoPayBalance(String trade_id,String user_id,
			int balanceTypeId) {
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("user_id", user_id);
		filter.put("trade_id", trade_id);
		filter.put("balanceTypeId", balanceTypeId);
		return S.get(InfoPayBalance.class).queryFirst(Condition.build("getTopFlowInfoPayBalance").filter(filter));
	}

	private boolean isLastMonth(String order_completion_time,String createTime) {
		int yyyy_org = Integer.parseInt(order_completion_time.substring(0, 4));
		int mm_org = Integer.parseInt(order_completion_time.substring(4, 6));
		int orgvalue = mm_org + yyyy_org*12;
		
		int yyyy_now =  Integer.parseInt(createTime.substring(0, 4));
		int mm_now = Integer.parseInt(createTime.substring(4, 6));
		int nowvalue = mm_now + yyyy_now*12;
		log.debug("this month =="+nowvalue +"orgmonth=="+orgvalue);
		if(nowvalue - orgvalue >1 ){
			return false;
		}else 
		{
			return true;
		}
	}

	private void createLogTradeShopping(String userId, String jdPin,
			String createTime, String tradeId, ShoppingReject shoppingReject) {
	    
		String yearMonth = shoppingReject.getCompletetime().substring(0,8);
		LogTradeShopping logTradeShopping = new LogTradeShopping();
		logTradeShopping.setUser_id(userId);
		logTradeShopping.setTrade_id(tradeId);
		logTradeShopping.setPartition_id(Integer.parseInt(yearMonth.substring(4,6)));
		logTradeShopping.setOrder_no(shoppingReject.getOrderno());
		logTradeShopping.setOrder_type("2");
		logTradeShopping.setReserve_1(shoppingReject.getOrgorderno()); //原始订单号记录到预留字段reserve_1
		logTradeShopping.setReserve_2(shoppingReject.getCompletetime()); //退货时间记录到预留字段2
		logTradeShopping.setOrder_amount(shoppingReject.getAmount());
		logTradeShopping.setProcess_tag(5); //未找到原始购物订单
		logTradeShopping.setProcess_time(createTime);
		 S.get(LogTradeShopping.class).create(logTradeShopping);
		
	}

	private void sendSms(String deviceNumber, long bValueForReject_0,long newbalance) {
		//发短信
		if(deviceNumber == null || deviceNumber.length()==0){
			return;
		}
		if(bValueForReject_0 >0){
			//String msg = "您好，由于您上月购物产品出现退货情况，根据您的退货记录，我们已扣除您"+bValueForReject_0+"B值，您目前剩余"+newbalance+"B值。【京东通信】";
			String msg = "|aoc.dic.bvaluecancel|"+bValueForReject_0+"|"+newbalance+"";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			//String sendTime = sdf.format(new Date());
//			TbSmsSendHis tbSmsSendHis = new TbSmsSendHis();
//			tbSmsSendHis.setMsisdn_send("10023");
//			tbSmsSendHis.setMsisdn_receive(deviceNumber);
//			tbSmsSendHis.setMessage_text(msg);
//			tbSmsSendHis.setSend_time(sendTime);
//			tbSmsSendHis.setProcess_tag("0");
//			tbSmsSendHis.setPara_key("aoc.dic.bvaluecancel");
			
			//S.get(TbSmsSendHis.class).create(tbSmsSendHis);

			log.info("SKU-refund 退货短信提醒:" + msg);
			HlpSmsSend sendHis = new HlpSmsSend();
			sendHis.setMsisdn_send("10023");
			sendHis.setMsisdn_receive(deviceNumber);
			sendHis.setMessage_text(msg);
			sendHis.setCreate_time(DateUtil.getSystemTime());
			
			S.get(HlpSmsSend.class).create(sendHis);
			
		}
		
	}
//
//	private String getSendTime() {
//		//获取短信发送时间
//		
//		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		if(cal.get(Calendar.HOUR_OF_DAY) >=21 || cal.get(Calendar.HOUR_OF_DAY) <= 9){
//			cal.add(Calendar.DAY_OF_MONTH, 1);
//			cal.set(Calendar.HOUR_OF_DAY, 9);
//			cal.set(Calendar.MINUTE, 0);
//		}
//		return sdf.format(cal.getTime());
//	}

	private List<LogTradeShoppingHis> getShoppingDetail(String userId,
			String orgorderno) {
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("user_id", userId);
		filter.put("order_no", orgorderno);
		return S.get(LogTradeShoppingHis.class).query(Condition.build("getShoppingDetail").filter(filter));
		 
	}
	
	//add zxm
	private  boolean isOrderNoExits(String userId,
			String orderno) {
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("user_id", userId);
		filter.put("order_no", orderno);
		List<LogTradeShoppingHis> tmp = S.get(LogTradeShoppingHis.class).query(Condition.build("isOrderNoExits").filter(filter));
		if(tmp.size()>0)
		{
			log.debug("the orderno "+orderno+" is exist!");
			return true;
		}
		return false;
		 
	}

	private long getOverFlowBValue(long bValueForReject, InfoPayBalance overFlowInfoPayBalance) {
		// 获得需要从溢出账本扣减的B值
		//InfoPayBalance infopaybalance = null;
		long bvalueforreject_1=0L;
		
		if(overFlowInfoPayBalance !=null && overFlowInfoPayBalance.getBalance_id() !=null && overFlowInfoPayBalance.getBalance_id().length()>0){
			long topflowvalue = overFlowInfoPayBalance.getBalance();
			if(topflowvalue >= bValueForReject){
				bvalueforreject_1 = bValueForReject;
			}else{
				bvalueforreject_1 = topflowvalue;
			}
 
		}
		
		return bvalueforreject_1;
	}

	private void updateOverFlowInfoPayBalance(long bValue, int balanceType,
			String yearMonth, List<InfoPayBalance> listInfoPayBalance,String userId,String tradeId,InfoPayBalance overFlowInfoPayBalance) { //,List<InfoPayBalance> changedInfoPayBalance
		
		if(bValue == 0){
			return;
		}

		long oldvalue =0L;
		long newvalue =0L;

		if(overFlowInfoPayBalance !=null){
			//找到了可用账本
			oldvalue = overFlowInfoPayBalance.getBalance();
			newvalue = oldvalue - bValue;

			Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("user_id", overFlowInfoPayBalance.getUser_id());
			filter.put("balance_id", overFlowInfoPayBalance.getBalance_id());
			filter.put("balance", bValue*-1);
			filter.put("eff_date", overFlowInfoPayBalance.getEff_date());
			filter.put("exp_date", overFlowInfoPayBalance.getExp_date());
			S.get(InfoPayBalance.class).batch(Condition.build("updateBalanceAndExpDate").filter(filter));
				
			BalanceAccessLog balanceAccessLog = new BalanceAccessLog();
			balanceAccessLog.setBalance_Id(overFlowInfoPayBalance.getBalance_id());
			balanceAccessLog.setAccess_Tag("1"); //0 增加  1 扣减
			balanceAccessLog.setBalance_Type_Id(balanceType);
			balanceAccessLog.setMoney(bValue*-1);
			balanceAccessLog.setNew_Balance(newvalue);
			balanceAccessLog.setOld_Balance(oldvalue);
			balanceAccessLog.setOperate_Time(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			balanceAccessLog.setPartition_Id(Integer.parseInt(yearMonth.substring(4,6)));
			balanceAccessLog.setTrade_Id(tradeId);
			balanceAccessLog.setTrade_Type_Code("203") ; //购物赠退货
			balanceAccessLog.setUser_Id(userId);
			
			S.get(BalanceAccessLog.class).create(balanceAccessLog);   
			
		}

	}
	
	//modify zxm
	private void updateNormalInfoPayBalance(long bValue,
			String createTime, List<InfoPayBalance> listInfoPayBalance,String userId,String tradeId,List<InfoPayBalance> changedInfoPayBalance ) {
		
		if(bValue == 0){
			return;
		}

		long allbalance = 0;//add zxm
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		InfoPayBalance oweInfoPayBalance = new InfoPayBalance();
		//判断是否有欠费账本，有的话直接更新到该账本，没有的话再去找正常账本
		if(isAlreadyExistsOweBalance(listInfoPayBalance,oweInfoPayBalance)){
			deductBalance(oweInfoPayBalance,bValue,tradeId);
    		changedInfoPayBalance.add(oweInfoPayBalance);
		}else{
			//先找到可退的账本
			List<InfoPayBalance> infoPayBalanceForDeduct = new ArrayList<InfoPayBalance>();
			List<InfoPayBalance> infoPayBalanceForSecondDeduct = new ArrayList<InfoPayBalance>();//add zxm
	        for(InfoPayBalance infoPayBalance :listInfoPayBalance){
				
	        	//modify zxm
				if((infoPayBalance.getBalance_type_id() == 0 || infoPayBalance.getBalance_type_id() == 3) && 
						 infoPayBalance.getExp_date().compareTo(createTime) >0){
					infoPayBalanceForDeduct.add(infoPayBalance);
					allbalance += infoPayBalance.getBalance();//add zxm
				}
			}
	        //add zxm
	        if(allbalance - bValue >= 0)//全部B值够本次购物退扣B
	        {
	        	doNormalInfoPayBalance(bValue, tradeId, infoPayBalanceForDeduct, changedInfoPayBalance);
	        }
	        else{
	        	//不够扣，需要在最近的0帐本中记录最后的负值
	        	long tmpValue = bValue - allbalance;
	        	//处理第一轮，把0和3帐本都扣成0
	        	doNormalInfoPayBalance(allbalance, tradeId, infoPayBalanceForDeduct, changedInfoPayBalance);
	        	for(InfoPayBalance infoPayBalance :listInfoPayBalance){
					if(infoPayBalance.getBalance_type_id() == 0 && 
							 infoPayBalance.getExp_date().compareTo(createTime) >0){
						infoPayBalanceForSecondDeduct.add(infoPayBalance);
					}
				}
	        	//重新处理0帐本，把最近的0帐本扣成负
	        	sort(infoPayBalanceForSecondDeduct, 2);
	        	int size = infoPayBalanceForSecondDeduct.size();
	        	deductBalance(infoPayBalanceForSecondDeduct.get(size-1),tmpValue,tradeId);
        		changedInfoPayBalance.add(infoPayBalanceForSecondDeduct.get(size-1));
	        }

		}
	 
	}
	
	//add zxm
	private void doNormalInfoPayBalance(long bValue, String tradeId, List<InfoPayBalance> infoPayBalanceForDeduct, List<InfoPayBalance> changedInfoPayBalance)
	{
		//排序
        sort(infoPayBalanceForDeduct, 1);
        //扣减
        int n =0;
        for(InfoPayBalance tmpipb:infoPayBalanceForDeduct){
        	n++;
        	if(bValue ==0){
        		break;
        	}
         
        	
        	if(n == infoPayBalanceForDeduct.size()){
        		//最后一个有效账本，如果有欠费则记录到该账本
        		deductBalance(tmpipb,bValue,tradeId);
        		changedInfoPayBalance.add(tmpipb);
        	}else{
        		//计算该账本可以扣除的B值量
        		long balance =  tmpipb.getBalance();
        		long tmpdeductValue = bValue >= balance ? balance:bValue;
        		bValue =  bValue - tmpdeductValue;
        		
        		deductBalance(tmpipb,tmpdeductValue,tradeId);
        		changedInfoPayBalance.add(tmpipb);
        	}
        	
        }
	}
	
	private void deductBalance(InfoPayBalance tmpipb, long bValue,String tradeId) {
		
		String expDate = tmpipb.getExp_date();
		if(bValue > tmpipb.getBalance()){
			expDate = "20501231235959";
		}
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("user_id", tmpipb.getUser_id());
		filter.put("balance_id", tmpipb.getBalance_id());
		filter.put("balance", bValue*-1);
		filter.put("eff_date", tmpipb.getEff_date());
		filter.put("exp_date", expDate);
		S.get(InfoPayBalance.class).batch(Condition.build("updateBalanceAndExpDate").filter(filter));
		
		
		long oldvalue =0L;
		long newvalue =0L;
 
			oldvalue = tmpipb.getBalance();
			newvalue = oldvalue - bValue;
			
			String operateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		BalanceAccessLog balanceAccessLog = new BalanceAccessLog();
		balanceAccessLog.setBalance_Id(tmpipb.getBalance_id());
		balanceAccessLog.setAccess_Tag("1"); //0 增加  1 扣减
		balanceAccessLog.setBalance_Type_Id(tmpipb.getBalance_type_id());
		balanceAccessLog.setMoney(bValue*-1);
		balanceAccessLog.setNew_Balance(newvalue);
		balanceAccessLog.setOld_Balance(oldvalue);
		balanceAccessLog.setOperate_Time(operateTime);
		balanceAccessLog.setPartition_Id(Integer.parseInt(operateTime.substring(4,6)));
		balanceAccessLog.setTrade_Id(tradeId);
		balanceAccessLog.setTrade_Type_Code("203") ; //购物赠退货
		balanceAccessLog.setUser_Id(tmpipb.getUser_id());
		
		S.get(BalanceAccessLog.class).create(balanceAccessLog);   
		
		tmpipb.setExp_date(expDate);
		tmpipb.setBalance(tmpipb.getBalance()-bValue);
		
	}

	private void sort(List<InfoPayBalance> infoPayBalanceForDeduct, int type) {
		  //1按类型和失效时间排序，2只按失效时间排序
		if(type == 1)
		{
			for(int i = 0;i<infoPayBalanceForDeduct.size();i++){
				for(int j = i+1;j<infoPayBalanceForDeduct.size();j++){
					
					//add zxm
					String iTimeAndId = infoPayBalanceForDeduct.get(i).getBalance_type_id() + infoPayBalanceForDeduct.get(i).getExp_date();
					String jTimeAndId = infoPayBalanceForDeduct.get(j).getBalance_type_id() + infoPayBalanceForDeduct.get(j).getExp_date();
					log.debug("zxm:iiiii" + iTimeAndId);
					log.debug("zxm:jjjjj" + iTimeAndId);
					if(iTimeAndId.compareTo(jTimeAndId)>0){
					//if(infoPayBalanceForDeduct.get(i).getExp_date().compareTo(infoPayBalanceForDeduct.get(j).getExp_date()) >0 ){
						log.debug("zxm:info_pay_balance:jjjjj" + infoPayBalanceForDeduct.get(j).getBalance_id());
						InfoPayBalance infoPayBalance = infoPayBalanceForDeduct.get(i);
						infoPayBalanceForDeduct.set(i, infoPayBalanceForDeduct.get(j));
						infoPayBalanceForDeduct.set(j, infoPayBalance);
					}
					
				}
			}
		}
		if(type == 2)
		{
			for(int i = 0;i<infoPayBalanceForDeduct.size();i++){
				for(int j = i+1;j<infoPayBalanceForDeduct.size();j++){
					
					//把失效期晚的放后面
					if(infoPayBalanceForDeduct.get(i).getExp_date().compareTo(infoPayBalanceForDeduct.get(j).getExp_date())  > 0 ){
						log.debug("zxm:info_pay_balance:jjjjj" + infoPayBalanceForDeduct.get(j).getBalance_id());
						InfoPayBalance infoPayBalance = infoPayBalanceForDeduct.get(i);
						infoPayBalanceForDeduct.set(i, infoPayBalanceForDeduct.get(j));
						infoPayBalanceForDeduct.set(j, infoPayBalance);
					}
					
				}
			}
		}
		
	}

	private boolean isAlreadyExistsOweBalance(
			List<InfoPayBalance> listInfoPayBalance,
			InfoPayBalance oweInfoPayBalance) {
		
		for(InfoPayBalance tmpipb:listInfoPayBalance){
			if(tmpipb.getBalance() <0 && tmpipb.getBalance_type_id()==0){
				//oweInfoPayBalance = tmpipb;
				oweInfoPayBalance.setBalance(tmpipb.getBalance());
				oweInfoPayBalance.setBalance_id(tmpipb.getBalance_id());
				oweInfoPayBalance.setBalance_type_id(tmpipb.getBalance_type_id());
				oweInfoPayBalance.setEff_date(tmpipb.getEff_date());
				oweInfoPayBalance.setExp_date(tmpipb.getExp_date());
				oweInfoPayBalance.setUser_id(tmpipb.getUser_id());
				return true;
			}
		}

		return false;
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
			if(tmpipb.getBalance_type_id() !=0 && tmpipb.getBalance_type_id() !=3){
				continue;
			}
			
//			int retvalue = infoPayBalanceSync.sync(tmpipb);
//			log.debug("同步中！"+retvalue);
//			if(retvalue != 0 ){
//				//同步失败，抛异常
//				throw new BValueException(-80005,"同步Redis失败");
//			}
		}
		
	}
 

	private void updateLogTradeShoppingHis(String userId, String jdPin,
			String createTime, String tradeId, ShoppingReject shoppingReject) {
    
			LogTradeShoppingHis logTradeShoppingHis = new LogTradeShoppingHis();
			logTradeShoppingHis.setUser_id(userId);
			logTradeShoppingHis.setTrade_id(tradeId);
			logTradeShoppingHis.setPartition_id(Integer.parseInt(createTime.substring(4,6)));
			logTradeShoppingHis.setOrder_no(shoppingReject.getOrderno());
			logTradeShoppingHis.setOrder_type("2");
			logTradeShoppingHis.setOrder_completion_time(shoppingReject.getCompletetime());  
			logTradeShoppingHis.setOrg_order_no(shoppingReject.getOrgorderno());
			logTradeShoppingHis.setOrder_amount(shoppingReject.getAmount());
			logTradeShoppingHis.setProcess_tag(2);
			logTradeShoppingHis.setProcess_time(createTime);
			 S.get(LogTradeShoppingHis.class).create(logTradeShoppingHis);
	
	}

	private void updateLogTradeHis(String userId, String jdPin,
			String createTime, String tradeId,long balance,long overtopvalue,long orderAmount) {

		LogTradeHis logTradeHis = new LogTradeHis();
		logTradeHis.setTrade_id(tradeId);
		logTradeHis.setTrade_type_code("203");  //购物赠送改为104 
		logTradeHis.setExternal_system_code("10000");
		logTradeHis.setUser_id(userId);
		logTradeHis.setChannel_type("110"); //后台BOSS渠道
		logTradeHis.setPartition_id(Integer.parseInt(createTime.substring(4,6)));
		logTradeHis.setProcess_tag(0L);
		logTradeHis.setTrade_time(createTime);
		logTradeHis.setProcess_time(createTime);
		logTradeHis.setBalance(balance*-1);
		logTradeHis.setRemark("购物赠退货B值扣减");
		logTradeHis.setOrder_amount(orderAmount);
		logTradeHis.setOvertop_value(overtopvalue*-1);
		logTradeHis.setOrder_completion_time(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        S.get(LogTradeHis.class).create(logTradeHis);
        
	}
 
 

	private long getbValuea(long sumAmount) {
		//2元1B值，向上取整
		long roundB = sumAmount /200;
		if( sumAmount > 200* roundB){
			roundB ++;
		}
		return roundB;
	}
/**
 * 
 * 201706去除redis
 */
//	public String getSyncRedis() {
//		return syncRedis;
//	}
//
//	public void setSyncRedis(String syncRedis) {
//		this.syncRedis = syncRedis;
//	}



	



	

}
