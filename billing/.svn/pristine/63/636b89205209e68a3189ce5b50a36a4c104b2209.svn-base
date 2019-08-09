package com.tydic.beijing.billing.tools.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dao.RuleRefreshTrigger;
import com.tydic.beijing.billing.tools.biz.RefreshTriggerOps;
import com.tydic.beijing.billing.tools.service.RefreshTriggerConsumer;

public class RefreshTriggerConsumerImpl implements RefreshTriggerConsumer {
	private final static Logger LOGGER = Logger
			.getLogger(RefreshTriggerConsumerImpl.class);
	private static final String REFRESH_STATUS_OK = "OK";
	private static final String REFRESH_STATUS_FAIL = "FAIL";

	private RefreshTriggerOps ops;

	@Override
	public void process() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "refreshtrigger.xml" });
		context.start();
		ops = (RefreshTriggerOps) context.getBean("ops");
		List<RuleRefreshTrigger> rrts = ops.getRuleRefreshTriggerActive();
		if ((rrts != null) && (!rrts.isEmpty())) {
			for (RuleRefreshTrigger rrt : rrts) {
				LOGGER.debug("Begin to process service["
						+ rrt.getService_name() + "] datastore["
						+ rrt.getDatastore_name() + "] REFRESH Task...");
				Object obj = context.getBean(rrt.getService_name());
				if (obj == null) {
					LOGGER.error("Can't Find service[" + rrt.getService_name()
							+ "] in [refreshtrigger.xml]");
					continue;
				}
				Class[] cs = new Class[3];
				cs[0] = int.class;
				cs[1] = String.class;
				cs[2] = String.class;
				try {
					Method method = obj.getClass().getMethod("refresh", cs);
					method.invoke(obj, rrt.getRefresh_batch_id(),
							rrt.getDatastore_name(), rrt.getService_name());
					rrt.setRefresh_status(REFRESH_STATUS_OK);
					ops.updateRuleRefreshTrigger(rrt);
					continue;
				} catch (NoSuchMethodException e) {
					LOGGER.error("Process service[" + rrt.getService_name()
							+ "] datastore[" + rrt.getDatastore_name()
							+ "] REFRESH Task Exception[" + e.getMessage()
							+ "]");
					e.printStackTrace();
				} catch (SecurityException e) {
					LOGGER.error("Process service[" + rrt.getService_name()
							+ "] datastore[" + rrt.getDatastore_name()
							+ "] REFRESH Task Exception[" + e.getMessage()
							+ "]");
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					LOGGER.error("Process service[" + rrt.getService_name()
							+ "] datastore[" + rrt.getDatastore_name()
							+ "] REFRESH Task Exception[" + e.getMessage()
							+ "]");
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					LOGGER.error("Process service[" + rrt.getService_name()
							+ "] datastore[" + rrt.getDatastore_name()
							+ "] REFRESH Task Exception[" + e.getMessage()
							+ "]");
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					LOGGER.error("Process service[" + rrt.getService_name()
							+ "] datastore[" + rrt.getDatastore_name()
							+ "] REFRESH Task Exception[" + e.getMessage()
							+ "]");
					e.printStackTrace();
				}
				rrt.setRefresh_status(REFRESH_STATUS_FAIL);
				ops.updateRuleRefreshTrigger(rrt);
			}
		}
	}

	public static void main(String[] args) {
		RefreshTriggerConsumer rtc = new RefreshTriggerConsumerImpl();
		rtc.process();
	}
}
