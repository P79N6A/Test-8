package com.tydic.beijing.bvalue.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * @author zhanghengbo
 *
 */
public class InfoPayBalanceManager {
	
	 

	/**
	 * @param userId 
	 * @param balanceType 需要修改的账本类型  0正常账本 3活动账本
	 * @param changeType b值修改类型 1增2减
	 * @param bValue 需要修改的B值  B值增减都为正
	 * @return 1成功0失败
	 * @throws Exceptionint
	 */
	public int manage(String userId ,int balanceType,int changeType,long bValue,List<InfoPayBalance> updateInfoPayBalance,List<InfoPayBalance> insertInfoPayBalance,
			List<BalanceAccessLog> insertBalanceAccessLog)  {
	
		 int retCode =0;
		 
		List<InfoPayBalance> oldInfoPayBalanceList = getInfoPayBalanceByManager(userId);
		//List<InfoPayBalance> newInfoPayBalanceList = new ArrayList<InfoPayBalance>();
		
		
		InfoPayBalance minusBalance = null;
		for(InfoPayBalance tmpipb:oldInfoPayBalanceList){
			if(tmpipb.getBalance() <0){
				minusBalance = new InfoPayBalance(tmpipb);
				break;
			}
		}
		
		long oweFee =0L; //欠费B值
		long writeOffFee =0L; //本次销欠B值
		long newBValue =0L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String createTime = sdf.format(new Date());
		int partitionId = Calendar.getInstance().get(Calendar.MONTH)+1;
		String effDate ="";
		String expDate ="";
		
		String createYear = createTime.substring(0, 4);
		if(createTime.compareTo(createYear+"0701000000") >=0 ){
			effDate = createYear+"0701000000";
			expDate = (Integer.parseInt(createYear)+1) +"0630235959";
		} else {
			effDate = createYear+"0101000000";
			expDate = createYear+"1231235959";
		}
		
		//
		if(changeType ==1){ //增
			//是否有欠费，如果有欠费，先销欠
			if(minusBalance != null){
			    oweFee = Math.abs(minusBalance.getBalance());
			    long oldBalance = minusBalance.getBalance();
			    
			    if(oweFee > bValue){
			    	//增加的B值不足销欠，直接追加到该账本，返回
			    	
			    	minusBalance.setBalance(bValue);
			    	BalanceAccessLog accessLog = new BalanceAccessLog();
//			    	accessLog.setTrade_Id(trade.getTrade_id()); 
//					accessLog.setTrade_Type_Code(trade.getTrade_type_code());
					accessLog.setUser_Id(userId);
					accessLog.setPartition_Id(partitionId);
					accessLog.setBalance_Id(minusBalance.getBalance_id());
					accessLog.setBalance_Type_Id(minusBalance.getBalance_type_id());
					accessLog.setAccess_Tag("0");		//存款
					accessLog.setMoney(bValue);
					accessLog.setOld_Balance(oldBalance);
					accessLog.setNew_Balance(oldBalance+ bValue);
					accessLog.setOperate_Time(createTime);
					
					insertBalanceAccessLog.add(accessLog);
					updateInfoPayBalance.add(minusBalance);
			    	return 1;
			    } else{
			    	//赠送B值足够销欠，同时包含正好能销欠的场景
			    	minusBalance.setBalance(oweFee);
			    	minusBalance.setEff_date(effDate);
			    	minusBalance.setExp_date(expDate);
			    	
			    	newBValue =  bValue - oweFee;
			    	
			    	if(newBValue ==0){
			    		//销欠后没有剩余，赠送值刚好能销欠
				    	BalanceAccessLog accessLog = new BalanceAccessLog();
//				    	accessLog.setTrade_Id(trade.getTrade_id()); 
//						accessLog.setTrade_Type_Code(trade.getTrade_type_code());
						accessLog.setUser_Id(userId);
						accessLog.setPartition_Id(partitionId);
						accessLog.setBalance_Id(minusBalance.getBalance_id());
						accessLog.setBalance_Type_Id(minusBalance.getBalance_type_id());
						accessLog.setAccess_Tag("0");		//存款
						accessLog.setMoney(oweFee);
						accessLog.setOld_Balance(oldBalance);
						accessLog.setNew_Balance(oldBalance+ oweFee);
						accessLog.setOperate_Time(createTime);
						
						insertBalanceAccessLog.add(accessLog);
						updateInfoPayBalance.add(minusBalance);
				    	return 1;
			    	}else{
			    		//销欠后还有余额，需要追加到现有账本或新增账本
			    		if(minusBalance.getBalance_type_id() == balanceType){
			    			//如果要调整的B值账本类型和欠费账本类型相同，则直接追加到该账本
			    			minusBalance.setBalance(bValue);
					    	BalanceAccessLog accessLog = new BalanceAccessLog();
//					    	accessLog.setTrade_Id(trade.getTrade_id()); 
//							accessLog.setTrade_Type_Code(trade.getTrade_type_code());
							accessLog.setUser_Id(userId);
							accessLog.setPartition_Id(partitionId);
							accessLog.setBalance_Id(minusBalance.getBalance_id());
							accessLog.setBalance_Type_Id(minusBalance.getBalance_type_id());
							accessLog.setAccess_Tag("0");		//存款
							accessLog.setMoney(bValue);
							accessLog.setOld_Balance(oldBalance);
							accessLog.setNew_Balance(oldBalance+ bValue);
							accessLog.setOperate_Time(createTime);
							
							insertBalanceAccessLog.add(accessLog);
							updateInfoPayBalance.add(minusBalance);
					    	return 1;
			    			
			    		}else{
			    			//否则，需要到现有账本里查找是否有合适的，有则追加，无则新增
			    			InfoPayBalance infoPayBalance = getRightInfoPayBalance(balanceType,effDate,expDate,oldInfoPayBalanceList) ;
			    			if (infoPayBalance == null ){
			    				//没有找到合适的账本，需要新增
			    				String tmpBalanceId = Common.getUUID();
			    			    infoPayBalance = new InfoPayBalance();
			    			    infoPayBalance.setBalance_id(tmpBalanceId);
			    			    infoPayBalance.setBalance_type_id(balanceType);
			    			    infoPayBalance.setBalance(newBValue);
			    			    infoPayBalance.setUser_id(userId);
			    			    infoPayBalance.setEff_date(effDate);
			    			    infoPayBalance.setExp_date(expDate);
			    			    
						    	BalanceAccessLog accessLog = new BalanceAccessLog();
//						    	accessLog.setTrade_Id(trade.getTrade_id()); 
//								accessLog.setTrade_Type_Code(trade.getTrade_type_code());
								accessLog.setUser_Id(userId);
								accessLog.setPartition_Id(partitionId);
								accessLog.setBalance_Id(tmpBalanceId);
								accessLog.setBalance_Type_Id(balanceType);
								accessLog.setAccess_Tag("0");		//存款
								accessLog.setMoney(newBValue);
								accessLog.setOld_Balance(0);
								accessLog.setNew_Balance(newBValue);
								accessLog.setOperate_Time(createTime);
								
								insertBalanceAccessLog.add(accessLog);
								insertInfoPayBalance.add(infoPayBalance);
								
								//原负账本需要更新到0
								BalanceAccessLog accessLog2 = new BalanceAccessLog();
//						    	accessLog.setTrade_Id(trade.getTrade_id()); 
//								accessLog.setTrade_Type_Code(trade.getTrade_type_code());
								accessLog2.setUser_Id(userId);
								accessLog2.setPartition_Id(partitionId);
								accessLog2.setBalance_Id(minusBalance.getBalance_id());
								accessLog2.setBalance_Type_Id(minusBalance.getBalance_type_id());
								accessLog2.setAccess_Tag("0");		//存款
								accessLog2.setMoney(minusBalance.getBalance());
								accessLog2.setOld_Balance(-minusBalance.getBalance());
								accessLog2.setNew_Balance(0);
								accessLog2.setOperate_Time(createTime);
								insertBalanceAccessLog.add(accessLog2);
								
								updateInfoPayBalance.add(minusBalance);
							 	
			    			}else {
			    				//找到了合适的账本，追加到该账本
			    			     
						    	BalanceAccessLog accessLog = new BalanceAccessLog();
//						    	accessLog.setTrade_Id(trade.getTrade_id()); 
//								accessLog.setTrade_Type_Code(trade.getTrade_type_code());
								accessLog.setUser_Id(userId);
								accessLog.setPartition_Id(partitionId);
								accessLog.setBalance_Id(infoPayBalance.getBalance_id());
								accessLog.setBalance_Type_Id(balanceType);
								accessLog.setAccess_Tag("0");		//存款
								accessLog.setMoney(newBValue);
								accessLog.setOld_Balance(infoPayBalance.getBalance());
								accessLog.setNew_Balance(newBValue + infoPayBalance.getBalance());
								accessLog.setOperate_Time(createTime);
								
								insertBalanceAccessLog.add(accessLog);
								
								infoPayBalance.setBalance(newBValue);
								updateInfoPayBalance.add(infoPayBalance);
			    				
								
								//扣负的账本也要加到accesslog表
								BalanceAccessLog accessLog2 = new BalanceAccessLog();
//						    	accessLog.setTrade_Id(trade.getTrade_id()); 
//								accessLog.setTrade_Type_Code(trade.getTrade_type_code());
								accessLog2.setUser_Id(userId);
								accessLog2.setPartition_Id(partitionId);
								accessLog2.setBalance_Id(minusBalance.getBalance_id());
								accessLog2.setBalance_Type_Id(minusBalance.getBalance_type_id());
								accessLog2.setAccess_Tag("0");		//存款
								accessLog2.setMoney(minusBalance.getBalance());
								accessLog2.setOld_Balance(-minusBalance.getBalance());
								accessLog2.setNew_Balance(0);
								accessLog2.setOperate_Time(createTime);
								insertBalanceAccessLog.add(accessLog2);
								
								updateInfoPayBalance.add(minusBalance);
								
			    				
			    			}
	
			    		}
			    		
			    	}
			    	
			    }
			    
			    

				
			} else{
				//没有欠费账本，直接追加到合适的账本里
				InfoPayBalance infoPayBalance = getRightInfoPayBalance(balanceType,effDate,expDate,oldInfoPayBalanceList) ;
    			if (infoPayBalance == null ){
    				//没有找到合适的账本，需要新增
    				String tmpBalanceId = Common.getUUID();
    			    infoPayBalance = new InfoPayBalance();
    			    infoPayBalance.setBalance_id(tmpBalanceId);
    			    infoPayBalance.setBalance_type_id(balanceType);
    			    infoPayBalance.setBalance(bValue);
    			    infoPayBalance.setUser_id(userId);
    			    infoPayBalance.setEff_date(effDate);
    			    infoPayBalance.setExp_date(expDate);
    			    
			    	BalanceAccessLog accessLog = new BalanceAccessLog();
//			    	accessLog.setTrade_Id(trade.getTrade_id()); 
//					accessLog.setTrade_Type_Code(trade.getTrade_type_code());
					accessLog.setUser_Id(userId);
					accessLog.setPartition_Id(partitionId);
					accessLog.setBalance_Id(tmpBalanceId);
					accessLog.setBalance_Type_Id(balanceType);
					accessLog.setAccess_Tag("0");		//存款
					accessLog.setMoney(bValue);
					accessLog.setOld_Balance(0);
					accessLog.setNew_Balance(bValue);
					accessLog.setOperate_Time(createTime);
					
					insertBalanceAccessLog.add(accessLog);
					insertInfoPayBalance.add(infoPayBalance);
    				
    			}else {
    				//找到了合适的账本，追加到该账本
    				
    				long oldBalance = infoPayBalance.getBalance() ;
    			    infoPayBalance.setBalance(bValue);
    			    
			    	BalanceAccessLog accessLog = new BalanceAccessLog();
//			    	accessLog.setTrade_Id(trade.getTrade_id()); 
//					accessLog.setTrade_Type_Code(trade.getTrade_type_code());
					accessLog.setUser_Id(userId);
					accessLog.setPartition_Id(partitionId);
					accessLog.setBalance_Id(infoPayBalance.getBalance_id());
					accessLog.setBalance_Type_Id(balanceType);
					accessLog.setAccess_Tag("0");		//存款
					accessLog.setMoney(bValue);
					accessLog.setOld_Balance(oldBalance);
					accessLog.setNew_Balance(bValue + oldBalance);
					accessLog.setOperate_Time(createTime);
					
					insertBalanceAccessLog.add(accessLog);
					updateInfoPayBalance.add(infoPayBalance);
    				
    				
    			}
			}
			
		}else if(changeType ==2) { //减
			 
			if(minusBalance !=null ){
				//有欠费，直接追加到该欠费账本
				
			    
		    	BalanceAccessLog accessLog = new BalanceAccessLog();
//		    	accessLog.setTrade_Id(trade.getTrade_id()); 
//				accessLog.setTrade_Type_Code(trade.getTrade_type_code());
				accessLog.setUser_Id(userId);
				accessLog.setPartition_Id(partitionId);
				accessLog.setBalance_Id(minusBalance.getBalance_id());
				accessLog.setBalance_Type_Id(balanceType);
				accessLog.setAccess_Tag("1");		//扣款
				accessLog.setMoney(bValue);
				accessLog.setOld_Balance(minusBalance.getBalance());
				accessLog.setNew_Balance(minusBalance.getBalance() - bValue);
				accessLog.setOperate_Time(createTime);
				
				minusBalance.setBalance(-1*bValue);
				
				insertBalanceAccessLog.add(accessLog);
				updateInfoPayBalance.add(minusBalance);
				
			}else {
				//无欠费，按账本类型、时间顺序扣b值账本，先扣0账本，再扣
				long lastBValue = bValue; //剩余要扣减的B值
			    for(InfoPayBalance tmpipb:oldInfoPayBalanceList){
			    	//依次做扣减
			    	if(tmpipb.getBalance()==0){
			    		
			    		updateInfoPayBalance.add(tmpipb);
			    		continue;
			    	}
			    	
			    	if(lastBValue ==0){
			    		break;
			    	}
			    	
			    	long deductBalance = Math.min(lastBValue, tmpipb.getBalance());
			    	lastBValue =  lastBValue - deductBalance;
			    	
			    	InfoPayBalance infoPayBalance = new InfoPayBalance(tmpipb);
			    	infoPayBalance.setBalance(-1*deductBalance);
			    	
			    	BalanceAccessLog accessLog = new BalanceAccessLog();
//			    	accessLog.setTrade_Id(trade.getTrade_id()); 
//					accessLog.setTrade_Type_Code(trade.getTrade_type_code());
					accessLog.setUser_Id(userId);
					accessLog.setPartition_Id(partitionId);
					accessLog.setBalance_Id(tmpipb.getBalance_id());
					accessLog.setBalance_Type_Id(tmpipb.getBalance_type_id());
					accessLog.setAccess_Tag("1");		//扣款
					accessLog.setMoney(deductBalance);
					accessLog.setOld_Balance(tmpipb.getBalance());
					accessLog.setNew_Balance(tmpipb.getBalance() - deductBalance);
					accessLog.setOperate_Time(createTime);
					
					insertBalanceAccessLog.add(accessLog);
			    	updateInfoPayBalance.add(infoPayBalance);
			    	
			    	
			    }
			    
			    if(lastBValue >0){
			    	///如果还有剩余待抵扣量，查找是否有合适的账本扣负，如果没有，新建一个0账本，扣负，并且结束时间2099年
			    	InfoPayBalance infoPayBalance = getRightInfoPayBalance(0, effDate, expDate, updateInfoPayBalance);
			    	if(infoPayBalance ==null){
			    		
			    		String tmpBalanceId = Common.getUUID();
			    		infoPayBalance = new InfoPayBalance();
	    			    infoPayBalance.setBalance_id(tmpBalanceId);
	    			    infoPayBalance.setBalance_type_id(0);
	    			    infoPayBalance.setBalance(lastBValue*-1);
	    			    infoPayBalance.setUser_id(userId);
	    			    infoPayBalance.setEff_date(effDate);
	    			    infoPayBalance.setExp_date("20991231235959");
	    			    
				    	BalanceAccessLog accessLog = new BalanceAccessLog();
//				    	accessLog.setTrade_Id(trade.getTrade_id()); 
//						accessLog.setTrade_Type_Code(trade.getTrade_type_code());
						accessLog.setUser_Id(userId);
						accessLog.setPartition_Id(partitionId);
						accessLog.setBalance_Id(tmpBalanceId);
						accessLog.setBalance_Type_Id(0);
						accessLog.setAccess_Tag("1");		//扣款
						accessLog.setMoney(lastBValue);
						accessLog.setOld_Balance(0);
						accessLog.setNew_Balance(lastBValue*-1);
						accessLog.setOperate_Time(createTime);
						
						insertBalanceAccessLog.add(accessLog);
						insertInfoPayBalance.add(infoPayBalance);
			    		
			    		
			    	}else{
			    
			    		for(int i=0;i<updateInfoPayBalance.size();i++){
			    			if(updateInfoPayBalance.get(i).getBalance_id().equals(infoPayBalance.getBalance_id())){
			    				long tmpbalance = updateInfoPayBalance.get(i).getBalance();
			    				updateInfoPayBalance.get(i).setBalance(tmpbalance-lastBValue);
			    				updateInfoPayBalance.get(i).setExp_date("20991231235959");
			    				break;
			    			}
			    		}
			    		
			    		for(int i=0;i<insertBalanceAccessLog.size();i++){
			    			if(insertBalanceAccessLog.get(i).getBalance_Id().equals(infoPayBalance.getBalance_id())){
			    				long accessbalance = insertBalanceAccessLog.get(i).getMoney();
			    				long newbalance = insertBalanceAccessLog.get(i).getNew_Balance();
			    				insertBalanceAccessLog.get(i).setMoney(accessbalance+lastBValue);
			    				insertBalanceAccessLog.get(i).setNew_Balance(newbalance-lastBValue);
			    			}
			    		}
			    		 
			    	}
			    	
			    	
			    }
				
				
			}
			
			
			
		}else{
			
		}

		retCode =1;
		return retCode;
	}

	private InfoPayBalance getOldInfoPayBalance(
			List<InfoPayBalance> oldInfoPayBalanceList, String balance_id) {

		for(InfoPayBalance tmpipb:oldInfoPayBalanceList){
			if(tmpipb.getBalance_id().equals(balance_id)){
				return tmpipb;
			}
		}
		return null;
	}

	//根据类型和生失效时间来查找合适的账本
	private InfoPayBalance getRightInfoPayBalance(int balanceType,
			String effDate, String expDate,List<InfoPayBalance> oldInfoPayBalanceList) {
		InfoPayBalance infoPayBalance = null;
		for(InfoPayBalance tmpipb:oldInfoPayBalanceList){
			if(tmpipb.getBalance_type_id() == balanceType && tmpipb.getEff_date().equals(effDate) && tmpipb.getExp_date().equals(expDate)) {
				infoPayBalance = new InfoPayBalance(tmpipb);
				break;
			}
		}
		return infoPayBalance;
	}

	//获取当前用户账本
	private List<InfoPayBalance> getInfoPayBalanceByManager(String userId) {
		String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		return S.get(InfoPayBalance.class).query(Condition.build("getInfoPayBalanceByManager").filter("user_id", userId).filter("currentTime", currentTime));
	}

}
