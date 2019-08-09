package com.tydic.beijing.billing.cyclerent;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.cyclerent.thread.CycleRentThread;




public class CycleRentBatchMain {

	private static final Logger LOGGER = Logger.getLogger(CycleRentBatchMain.class);
	
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "cyclerentBatch.xml"});
		long t1 = System.currentTimeMillis();
		String process_num = args[0];//进程数量
		String str_channel_no = args[1];//mod(user,process_num)=channel_no
		String str_mod = args[2];//线程数
		String num = args[3];//批量处理数
		String event_id = args[4];//事件类型
		String cycle_alarm = args[5];//试算短信提醒
		process(Integer.parseInt(process_num),Integer.parseInt(str_channel_no), Integer.parseInt(str_mod),Integer.parseInt(num),context,event_id,cycle_alarm);
		System.out.println("-------CycleRent["+str_channel_no+"] deal success------");
		LOGGER.debug("-CycleRent["+str_channel_no+"] deal success-----deal time :"+((System.currentTimeMillis()-t1)/(1000*60))+" 分");
		
	}

	private static void process(int _process_num,int _channel_no, int _mod,int _num, ClassPathXmlApplicationContext context, String event_id, String cycle_alarm) {
		int channel_no = _channel_no;
		int mod;
		CycleRentThread[] array_thread;
		mod = _mod;
		int num = _num;
		array_thread = new CycleRentThread[mod];
		Thread[] thread = new Thread[mod];
		for(int i=0; i<mod; i++) {
//			array_thread[i] = new CycleRentThread();
			array_thread[i] = (CycleRentThread)context.getBean("CycleRentThread");
			array_thread[i].setProcess_num(_process_num);
			array_thread[i].setChannel_no(channel_no);
			array_thread[i].setMod(mod);
			array_thread[i].setMod_i(i);
			array_thread[i].setNum(num);
			array_thread[i].setEvent_type(event_id);
			array_thread[i].setCycleAlarm(cycle_alarm);
			thread[i] = new Thread(array_thread[i]);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
				return ;
			}
			thread[i].start();
		}
		for (Thread t : thread) {
			try {
				t.join();
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
				return ;
			}
		}
	}

}
