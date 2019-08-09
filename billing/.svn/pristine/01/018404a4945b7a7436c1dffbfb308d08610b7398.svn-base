/**  
 * Project Name:Develop  
 * File Name:Test.java  
 * Package Name:com.tydic.beijing.billing.ua  
 * Date:2014年7月11日上午10:25:51  
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved.  
 *  
 */

package com.tydic.beijing.billing.ua;

import com.tydic.beijing.billing.ua.common.BasicException;
import com.tydic.beijing.billing.ua.common.MessageQueue;
import com.tydic.beijing.billing.ua.common.MessageQueueFactory;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:Test <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014年7月11日 上午10:25:51 <br/>
 * 
 * @author Bradish7Y
 * @version
 * @since JDK 1.7
 * @see
 */
public class UaApp {
	private static final Logger L = Logger.getLogger(UaApp.class);

	public static void main(String[] args) {
		welcome();

		ClassPathXmlApplicationContext ctx = SpringContextUtil.getContext();
		ctx.start();

		String mode = System.getProperty("UA.MODE");
		String confDir = System.getProperty("UA.CONFIG");
		String logDir = System.getProperty("UA.LOG.DIR");
		String logName = System.getProperty("UA.LOG.NAME");
		int counts = 1;
		try {
			counts = Integer.parseInt(System.getProperty("UA.Threads"));
			if (counts == 0) {
				counts = 1;
			}
		} catch (NumberFormatException e) {
			counts = 1;
		}

		if (mode == null) {
			mode = "BSS";
		}
		if (confDir == null) {
			// confDir = "./";
		}
		if (logDir == null) {
			// windows 不好使，需要在jvm上-D设置
			System.setProperty("UA.LOG.DIR", "./");
			logDir = "./";
		}

		if (logName == null) {
			// windows 不好使，需要在jvm上-D设置
			System.setProperty("UA.LOG.NAME", "ua");
			logName = "ua";
		}

		L.info("UA.MODE=" + mode);
		L.info("UA.CONFIG=" + confDir);
		L.info("UA.Threads=" + counts);
		L.info("UA.LOG.DIR=" + logDir);
		L.info("UA.LOG.NAME=" + logName);

		if (mode.toUpperCase().equals("BSS")) {
			Ua1Driver uDriver = null;
			L.info("Business Type[BSS]");
			try {
				uDriver = new Ua1Driver(confDir, counts);
				uDriver.run();
			} catch (BasicException e) {
				L.error("error", e);
				System.exit(-1);
			} catch (IOException e) {
				L.error("error", e);
				System.exit(-1);
			} catch (Exception e) {
				L.error("error", e);
				System.exit(-1);
			}

		} else if (mode.toUpperCase().equals("FORMAT")) {
			// for support only format, no call billing service
			Ua2Driver uDriver = null;
			L.info("Business Type[FORMAT]");

			try {
				uDriver = new Ua2Driver(confDir, counts);
				uDriver.run();
			} catch (BasicException e) {
				L.error("error", e);
				System.exit(-1);
			} catch (IOException e) {
				L.error("error", e);
				System.exit(-1);
			} catch (Exception e) {
				L.error("error", e);
				System.exit(-1);
			}

		} else if (mode != null && mode.toUpperCase().equals("OCS")) {
			throw new UnsupportedOperationException("No Support Business[OCS]") ;
		} else {
			L.warn("UA.MODE=null");
			printUsage();
		}

		// return file queue size every 30s
		MessageQueue mq = MessageQueueFactory.getInstance().getMessageQueue(
				MessageQueueFactory.queueType.FILE);
		while (true) {
			try {
				L.info("file queue size :" + mq.size()
						+ ", check next time after the 15 seconds, UaApp.main");
				TimeUnit.SECONDS.sleep(15);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * 
	 * usage:打印启动命令行例子. <br/>
	 * 
	 * @since JDK 1.6
	 */
	public static void printUsage() {
		StringBuffer sb = new StringBuffer();
		sb.append("Usage:\n");
		sb.append("java -cp ");
		sb.append("<class_path> ");
		sb.append("<-DUA.MODE=BSS|FORMAT> ");
		sb.append("[-Xms500m -Xmx500m] ");
		sb.append("<com.tydic.beijing.billing.ua.UaApp>");

		System.out.println(sb.toString());
	}

	public static void welcome() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nWelcome to").append('\n');
		sb.append("            ___   __").append('\n');
		sb.append("           /  /  /  |").append('\n');
		sb.append("          /  /  /  /___").append('\n');
		sb.append("         /  /__/  /  _  \\").append('\n');
		sb.append("         \\______,/\\__,__/\\   version 1.0.0").append('\n');

		L.info(sb.toString());
	}

}
