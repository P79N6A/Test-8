/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.bvalue.core;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.mem.InfoPayBalanceMem;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.DataService;
import com.tydic.uda.service.S;

/**
 * info_pay_balance与redis同步程序<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class InfoPayBalanceSync {
	private static final Logger log = Logger.getLogger(InfoPayBalanceSync.class);

	/**
	 * `
	 * sync:提供InfoPayBalance，balance_id和user_id不能为空.<br/>
	 * 
	 * @param info
	 * @return 0 成功
	 *         -1 失败
	 */
	public int sync(InfoPayBalance info) {
		//log.debug("进入账本同步=====InfoPayBalanceSync===");
		DataService<InfoPayBalanceMem> infoDataService = S.get(InfoPayBalanceMem.class);
		log.debug("InfoPayBalanceMem"+infoDataService);
		HashMap<String, InfoPayBalance> infoM = null;
		if (info != null) {
			// 查询数据库
			String userId = info.getUser_id();
			String balanceId = info.getBalance_id();
			log.debug("同步用户信息，userId["+userId+"],账本id["+balanceId+"]");
			InfoPayBalance infoDb = S.get(InfoPayBalance.class).queryFirst(
					Condition.build("getByBalanceId").filter("userId", userId)
							.filter("balanceId", balanceId));
			if (infoDb == null) {
				log.error("同步失败，该条用户账本不存在，user_id[" + userId + "],balance_id[" + balanceId + "]");
				return -1;
			}
			// 从redis取数据
			log.debug("从infoDataService取出用户===============");
			InfoPayBalanceMem infoMem = infoDataService.get(userId);
			
			
			if (infoMem != null) {
				log.debug("从Redis获取的value=["+infoMem.toString()+"]");
				infoM = infoMem.getInfoMap();
				if (infoM != null && !infoM.isEmpty()) {
					log.debug("从Redis获取的账本不为空，数量"+infoM.size());
					InfoPayBalance infoPayBalance = infoM.get(info.getBalance_id());
					if (infoPayBalance == null) {
						infoM.put(info.getBalance_id(), infoDb);
						log.debug("加入账本"+infoDb.toString());
					} else {
						infoM.put(info.getBalance_id(), infoDb);
						log.debug("更新账本"+infoDb.toString());
					}
				} else {
					log.debug("从Redis获取到了value，但账本为空");
					infoM = new HashMap<String, InfoPayBalance>();
					infoM.put(balanceId, infoDb);
					infoMem = new InfoPayBalanceMem();
					infoMem.setUser_id(userId);
					infoMem.setInfoMap(infoM);

					String id = (String) infoDataService.create(infoMem);
					if (id == null) {
						log.error("同步失败，user_id[" + userId + "],balance_id[" + balanceId + "]");
						return -1;
					}
				}
			} else {
				log.debug("从Redis获取值为空！");
				
				infoM = new HashMap<String, InfoPayBalance>();
				infoM.put(balanceId, infoDb);
				infoMem = new InfoPayBalanceMem();
				infoMem.setUser_id(userId);
				infoMem.setInfoMap(infoM);

				String id = (String) infoDataService.create(infoMem);
				
				log.debug("在Redis创建用户返回["+id+"],本次同步使用的userid=["+userId+"]");
				
				if (id == null) {
					log.error("同步失败，user_id[" + userId + "],balance_id[" + balanceId + "]");
					return -1;
				}
			}
			log.debug("===========开始更新！================");
			// 更新
			if (infoDataService.update(infoMem) == 1) {
				log.error("同步失败，user_id[" + userId + "],balance_id[" + balanceId + "]");
				return -1;
			}
			log.debug("===========同步redis成功！================"+infoDataService.get(userId).getInfoMap().get(info.getBalance_id()));
		} else {
			log.error("同步失败，参数不能为空");
			return -1;
		}
		return 0;
	}

	/**
	 * 
	 * getInfoPayBalanceFromRedis:查询redis.<br/>
	 * 
	 * @param userId
	 * @return
	 *         null 返回为空<br/>
	 */
	public InfoPayBalanceMem getInfoPayBalanceFromRedis(final String userId) {
		DataService<InfoPayBalanceMem> infoDataService = S.get(InfoPayBalanceMem.class);
		InfoPayBalanceMem ret = infoDataService.get(userId);
		if (log.isDebugEnabled()) {
			if (ret != null)
				for (Entry<String, InfoPayBalance> e : ret.getInfoMap().entrySet()) {
					log.debug("SYN-" + e.toString());
				}
		}
		return ret;
	}
	
	public int removeInfoPayBalanceFromRedis(final String userId) {
		DataService<InfoPayBalanceMem> infoDataService = S.get(InfoPayBalanceMem.class);
		int removeNum= infoDataService.remove(userId);
		return removeNum;
	}

	public void syncByObj(InfoPayBalanceMem inputmem) {
		log.debug("开始同步redis===="+inputmem.toString());
		DataService<InfoPayBalanceMem> infoDataService = S.get(InfoPayBalanceMem.class);
		InfoPayBalanceMem infoMem = infoDataService.get(inputmem.getUser_id());
		if(infoMem ==null){
			String retStr = (String) infoDataService.create(inputmem);
			log.debug("在Redis创建用户返回信息:"+retStr);
			
		}else{
			int ret = infoDataService.update(inputmem);
			log.debug("在Redis更新用户返回信息:"+ret);
			
		}
		
		
		
	}
	
	public void syncByObjNew(InfoPayBalanceMem inputmem) {
		log.debug("开始同步redis===="+inputmem.toString());
		DataService<InfoPayBalanceMem> infoDataService = S.get(InfoPayBalanceMem.class);
		InfoPayBalanceMem infoMem = infoDataService.get(inputmem.getUser_id());
		if(infoMem ==null){
			String retStr = (String) infoDataService.create(inputmem);
			log.debug("在Redis创建用户返回信息:"+retStr);
		}else{
			String retStr = (String) infoDataService.create(inputmem);
			log.debug("在Redis更新用户返回信息:"+retStr);
			
		}
		
		
		
	}
	
}
