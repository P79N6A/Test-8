package com.tydic.beijing.billing.memcache;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.memcache.service.impl.LoadThread;

public class MultiLoadDBMain extends MyApplicationContextUtil{
	private final static Logger LOGGER = Logger.getLogger(MultiLoadDBMain.class);
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"load.xml" });
		mycontext = context;
		
		int mod = Integer.parseInt(args[0]);
		LoadThread[] array_lt = new LoadThread[mod];
		Thread[] array_thread = new Thread[mod];
		for(int i=0; i<mod; i++) {
			array_lt[i] = new LoadThread(mod, i);
			array_thread[i] = new Thread(array_lt[i]);
		}
		//array_lt[0].run();
		for (int i=0; i<mod; i++) {
			array_thread[i].start();
		}
		try {
			for (int i = 0; i < mod; i++) {
				array_thread[i].join();
				LOGGER.debug("join " + array_thread[i].getId());
			}
		} catch (InterruptedException e) {
			LOGGER.debug(e.toString());
		}

		((AbstractApplicationContext) context).close();
	}

}
