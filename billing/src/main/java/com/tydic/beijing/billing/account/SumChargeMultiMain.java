package com.tydic.beijing.billing.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.account.biz.RefreshTriggerOps;
import com.tydic.beijing.billing.account.thread.SumThread;
import com.tydic.beijing.billing.dao.LogRefreshTrigger;
import com.tydic.beijing.billing.util.RecordAssembler;


public class SumChargeMultiMain implements RefreshMainInterface{	
	private int mod;
	private SumThread[] array_thread;
	@Autowired
	private RefreshTriggerOps refresh;
	@Override
	public void refresh(int refreshBatchId, String datastoreName,
			String serviceName) {
		boolean res = true;
		String refreshStatus;
		String memo;
		for(int i=0; i<mod; i++) {
			res = res && array_thread[i].getSumcharge().getSum().refresh();
		}
		if(res == true) {
			refreshStatus = "OK";
			memo = null;
		} else {
			refreshStatus = "FAILURE";
			memo = "refresh SumChargeMultiMain error";
		}
		LogRefreshTrigger lrt = RecordAssembler.assembleLogRefreshTrigger(refreshBatchId,
				datastoreName, serviceName, refreshStatus, memo);
		refresh.insertLogRefreshTrigger(lrt);
	}
	
	public void process(int _channel_no, int _mod) {
		
		int channel_no = _channel_no;
		mod = _mod;
		
		array_thread = new SumThread[mod];
		for(int i=0; i<mod; i++) {
			array_thread[i] = new SumThread();
			array_thread[i].setChannel_no(channel_no);
			array_thread[i].setMod(mod);
			array_thread[i].setMod_i(i);
			
			new Thread(array_thread[i]).start();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "sum.xml"});
		String str_channel_no = args[0];
		String str_mod = args[1];
		SumChargeMultiMain main = (SumChargeMultiMain) context.getBean("SumChargeMultiMain");
		main.process(Integer.parseInt(str_channel_no), Integer.parseInt(str_mod));
		
		context.start();
		while (true) {
			Thread.sleep(10000L);
		}
		/*
		String str_channel_no = args[0];
		String str_mod = args[1];
		
		int channel_no = Integer.parseInt(str_channel_no);
		int mod = Integer.parseInt(str_mod);
		
		SumThread[] array_thread = new SumThread[mod];
		for(int i=0; i<mod; i++) {
			array_thread[i] = new SumThread();
			array_thread[i].setChannel_no(channel_no);
			array_thread[i].setMod(mod);
			array_thread[i].setMod_i(i);
			
			new Thread(array_thread[i]).start();
		}
		*/
	}
}
