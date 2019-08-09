/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.tap3;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.tap3.common.MessageQueue;
import com.tydic.beijing.billing.tap3.common.MessageQueueFactory;
import com.tydic.beijing.billing.tap3.common.StartUpParameters;
import com.tydic.beijing.billing.tap3.core.LoadConfiguration;
import com.tydic.beijing.billing.tap3.core.ScanFileHandler;
import com.tydic.beijing.billing.tap3.core.Tap3Handler;

/**
 * tap3 protocl parse app<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class Tap3ParserApp {

	private static final Logger log = Logger.getLogger(Tap3ParserApp.class);

	public static void main(String[] args) {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		// 加载配置
		LoadConfiguration load = new LoadConfiguration("tap3.xml", "output.xml");
		try {
			load.load();
		} catch (DocumentException e1) {
			log.error(e1);
			e1.printStackTrace();
			System.exit(-1);
		} catch (IOException e1) {
			log.error(e1);
			e1.printStackTrace();
			System.exit(-1);
		}

		// 启动参数
		StartUpParameters params = (StartUpParameters) ctx.getBean("parameters");

		// 扫描目录线程
		ScanFileHandler scanFile = (ScanFileHandler) ctx.getBean("scanFile");
		Thread scanFileThread = new Thread(scanFile, "scanFile");
		// 启动
		scanFileThread.start();

		int max = params.getTap3HandlerThreads();
		// 处理线程
		Thread[] startUp = new Thread[max];
		Tap3Handler[] tap3Handlers = new Tap3Handler[max];
		for (int i = 0; i < max; i++) {
			tap3Handlers[i] = (Tap3Handler) ctx.getBean("tap3Process");
			startUp[i] = new Thread(tap3Handlers[i], "tap3Handlers-" + i);
		}

		// 启动
		for (int i = 0; i < max; i++) {
			startUp[i].start();
		}

		MessageQueue queue = MessageQueueFactory.getInstance().getMessageQueue(
				MessageQueueFactory.queueType.FILE);
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(5);
				log.info("Queue size:" + queue.size());
			} catch (InterruptedException e) {
				// None
			}
		}
	}
}
