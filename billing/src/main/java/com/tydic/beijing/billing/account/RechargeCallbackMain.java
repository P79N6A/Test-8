package com.tydic.beijing.billing.account;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.service.impl.CallBackLoadThread;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;

/**
 * 
 * @author Tian
 *
 */
public class RechargeCallbackMain extends MyApplicationContextUtil {
	private final static Logger LOGGER = Logger.getLogger(RechargeCallbackMain.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "rechargecallback.xml" });
//		context.start();
		mycontext=context;
		int mod = Integer.parseInt(args[0]);
		CallBackLoadThread[] array_lt = new CallBackLoadThread[mod];
		Thread[] array_thread = new Thread[mod];
		
		for(int i=0; i<mod; i++) {
			array_lt[i] = new CallBackLoadThread(mod, i);
			array_thread[i] = new Thread(array_lt[i]);
		}
		for (int i=0; i<mod; i++) {
			LOGGER.debug("===========================线程【"+i+"】！！！！");
			array_thread[i].start();
		}
		try {
			for (int i = 0; i < mod; i++) {
				LOGGER.debug("===========================执行！！！！");
				array_thread[i].join();
				LOGGER.debug("join " + array_thread[i].getId());
			}
		} catch (InterruptedException e) {
			LOGGER.debug(e.toString());
			
		}
		

//		((AbstractApplicationContext) context).close();
//		RechargeCallbackImpl rc = (RechargeCallbackImpl) context
//				.getBean("rechargeCallback");
//		rc.process();
	}
}
