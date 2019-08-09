/**  
 * Project Name:Develop
 * File Name:MessageQueueFactory.java
 * Package Name:com.tydic.beijing.billing.ua
 * Date:2014年7月16日下午3:17:10
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 * @since JDK 1.7
 */
package com.tydic.beijing.billing.ua.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: MessageQueueFactory <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年7月16日 下午3:17:10 <br/>
 * 
 * @author Bradish7Y
 * @version
 */
public class MessageQueueFactory {
	public static enum queueType {
		FILE, // 文件队列
		OTHER // 其他
	}

	private volatile static MessageQueueFactory instance = null;
	private ConcurrentHashMap<queueType, MessageQueue> m = new ConcurrentHashMap<queueType, MessageQueue>();
	private static Object O_O = new Object();

	/**
	 * 
	 * getInstance:单例，通过工厂类取得一个队列. <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @return
	 */
	public static MessageQueueFactory getInstance() {
		if (instance == null) {
			synchronized (MessageQueueFactory.class) {
				if (instance == null) {
					instance = new MessageQueueFactory();
				}
			}
		}
		return instance;
	}

	/**
	 * 
	 * getMessageQueue:获取一个带有默认大小的队列ArrayBlockingQueue. <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @param type
	 *            队列类型
	 * @return
	 */
	public MessageQueue getMessageQueue(queueType type) {
		MessageQueue q = m.get(type);
		if (q == null) {
			synchronized (O_O) {
				q = m.get(type);
				if (q == null) {
					q = new MessageQueue();
					m.put(type, q);
				}
			}
		}
		return q;
	}
}
