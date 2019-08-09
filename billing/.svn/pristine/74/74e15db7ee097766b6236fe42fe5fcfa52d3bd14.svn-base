package com.tydic.beijing.billing.memcache;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.memcache.service.DB2Mem;

public class UpdateDBMain {
	private final static Logger LOGGER = Logger.getLogger(UpdateDBMain.class);
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				//new String[] {"memcachedupdate.xml" });
				new String[] {"getmemtest.xml" });
		
		//DB2Mem db2mem = (DB2Mem) context.getBean("DB2MemUpdateDubbo");
		DB2Mem db2mem = (DB2Mem) context.getBean("memcachedupdate");
		
		try {
			db2mem.update(args[0], args[1]);
		} catch (Exception ex) {
			LOGGER.error(ex.toString());
		}
		((AbstractApplicationContext) context).close();
	}
}
