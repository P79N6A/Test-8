package com.tydic.beijing.billing.memcache;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.memcache.service.DB2Mem;

public class LoadDBMain {
	private final static Logger LOGGER = Logger.getLogger(LoadDBMain.class);
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"load.xml" });
		
		DB2Mem db2mem = (DB2Mem) context.getBean("DB2MemImpl");
		try {
			db2mem.load();
		} catch (Exception ex) {
			LOGGER.error(ex.toString());
		}
		((AbstractApplicationContext) context).close();
	}

}
