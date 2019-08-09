package com.tydic.beijing.billing.iop.service;

import java.util.List;

import org.apache.log4j.Logger;


import redis.clients.jedis.Jedis;



public class SyncDbInfo  extends Thread{
	

	private IOPInterface iop;

	public IOPInterface getIop() {
		return iop;
	}

	public void setIop(IOPInterface iop) {
		this.iop = iop;
	}


	private static Logger log=Logger.getLogger(SyncDbInfo.class);

	
	@Override
	public void run() {
		
	while(true){

		List<String> keyList = iop.getkeys();
		if(keyList.size()==0){
			try {
				Thread.sleep(1*60*1000);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //当前没有记录则休眠1分钟
		}
		
		for(String tmpkey:keyList){
			try {
				String redisValue = iop.dealTables(tmpkey);
				syncRedis(tmpkey,iop.getField(),redisValue);	
			} catch (Exception e) {
				log.error(iop.getClass().getName()+"syncerror"+tmpkey+","+e.getMessage(), e);
				e.printStackTrace();
			}
		}
		

	}
	
	

	}

	private void syncRedis(String tmpkey,String field, String redisValue) {

		Jedis jedis = JedisFactory.getJedis();
		//jedis.del(tmpkey);
		//jedis.set(tmpkey, redisValue);
		jedis.hdel(field,tmpkey);  //删除原来的
		jedis.hset( field,tmpkey, redisValue); //更新
		
		log.debug("jedis get test=============>"+jedis.hget(field,tmpkey));
	}


	
	

}
