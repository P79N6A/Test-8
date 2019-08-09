package com.tydic.beijing.billing.memcache;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dao.LifeServiceAttrForMemcache;
import com.tydic.beijing.billing.dao.SynchronizeInfo;
import com.tydic.uda.service.S;


public class GetMemcacheInfo {
	
	private final static Logger LOGGER = Logger.getLogger(GetMemcacheInfo.class);
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "uda.xml" });
		
		String jdnDeviceNumber = args[0];
		
		LOGGER.debug("jdnDeviceNumber:"+jdnDeviceNumber.toString());
		
		LifeServiceAttrForMemcache lifeServiceAttrForMemcache = S.get(LifeServiceAttrForMemcache.class).get(
				jdnDeviceNumber);
		
		LOGGER.debug("--- in GetMemcacheInfo ---");
		if(lifeServiceAttrForMemcache!=null && lifeServiceAttrForMemcache.getSynchronizeInfoList()!=null ){
			List<SynchronizeInfo> synchronizeinfoList = lifeServiceAttrForMemcache.getSynchronizeInfoList();
			for(SynchronizeInfo syn : synchronizeinfoList){
				LOGGER.debug("--- SynchronizeInfoList:"+syn.toString());
			}
			LOGGER.debug("------Query memcachedInfo by "+jdnDeviceNumber.toString()+" successful!-------");
		}else{
			LOGGER.debug("------Query memcachedInfo by "+jdnDeviceNumber.toString()+" failed!-------");
		}
		LOGGER.debug("--- out GetMemcacheInfo ---");
		context.close();
	}
}
