package com.tydic.beijing.billing.account.type;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.QAcctProcess;

public class UserBill {
	public final static int QUEUE_LENGTH = 100;
	private final static Logger LOGGER = Logger.getLogger(UserBill.class);
	private Queue<QAcctProcess> queue;
	//private ArrayBlockingQueue<QAcctProcess> queue;
	/*
	{
		queue.
	}
	*/
	public UserBill() {
		queue = new ArrayBlockingQueue<QAcctProcess>(QUEUE_LENGTH) ;
	}
		
	public synchronized Boolean push(QAcctProcess q) {
		try {
			if (queue.size() == QUEUE_LENGTH) {
				LOGGER.debug("the queue is full");
				this.wait();
			}
			queue.add(q);
			this.notify();
			return true;
		} catch(Exception e) {
			LOGGER.debug("push queue :" + q.toString() + " error!");
			return false;
		}
	}
	
	public synchronized QAcctProcess pop() {
		try {
			if(queue.isEmpty()) {
				this.wait();
				LOGGER.debug("the queue is empty");
			}
			QAcctProcess q = queue.remove();
			this.notify();
			return q;
		} catch(Exception e) {
			LOGGER.debug("pop queue QAcctProcess : error!");
			return null;
		}
	}
}
