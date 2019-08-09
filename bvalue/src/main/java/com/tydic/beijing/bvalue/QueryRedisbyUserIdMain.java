package com.tydic.beijing.bvalue;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.mem.InfoPayBalanceMem;
import com.tydic.beijing.bvalue.service.impl.ShoppingRejectThread;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.DataService;
import com.tydic.uda.service.S;

public class QueryRedisbyUserIdMain {
	

	private static Logger log=Logger.getLogger(QueryRedisbyUserIdMain.class);

	public static void main(String args[]){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("ShoppingReject.xml");
		context.start();
		log.debug("service ShoppingRejectMain start ....");
		
		String userId = args[0];
 
		DataService<InfoPayBalanceMem> infoDataService = S.get(InfoPayBalanceMem.class);
		HashMap<String, InfoPayBalance> infoM = null;
			// 从redis取数据
		InfoPayBalanceMem infoMem = infoDataService.get(userId);
		
		log.debug("根据userid=["+userId+"]查询到["+infoMem.getInfoMap().size()+"]个账本,分别是：");
		long totalBalance =0L;
		HashMap<String,InfoPayBalance> infoMaps = infoMem.getInfoMap();
		for(String tmpo:infoMaps.keySet()){
			log.debug("根据balanceId=["+infoMaps.get(tmpo).getBalance_id() +"]的余额是["+infoMaps.get(tmpo).getBalance()+"],结束时间=["+infoMaps.get(tmpo).getExp_date()+"]");
			totalBalance = totalBalance + infoMaps.get(tmpo).getBalance();
		}
		
		log.debug("查询结束，总金额=["+totalBalance+"]^_^   ^_^   ^_^");
		
	}

}
