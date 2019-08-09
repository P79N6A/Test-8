package com.tydic.beijing.billing.iop;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.iop.service.impl.OrderIsEffectFileServiceImpl;


public class OrderIsEffectFileMain {
	private final static Logger LOGGER = Logger.getLogger(OrderIsEffectFileMain.class);

	public static void main(String[] args) {
		LOGGER.debug("=========开始进入生成回执文件程序===========");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "order_is_effect_file.xml" });
		context.start();
		OrderIsEffectFileServiceImpl orderIsEffectFileServiceImpl=(OrderIsEffectFileServiceImpl)context.getBean("orderIsEffectFile");
		orderIsEffectFileServiceImpl.getIsEffectFile();
		context.close();
		// try {
		// RandomAccessFile randomAccessFile = new
		// RandomAccessFile("D:\\MyFile\\order\\aa.rsp", "rw");
		// String str = "6666";
		// byte[] b = str.getBytes();
		// int skip = 0;
		// randomAccessFile.setLength(randomAccessFile.length() + b.length);
		// for (long i = randomAccessFile.length() - 1; i > b.length + skip - 1;
		// i--) {
		// randomAccessFile.seek(i - b.length);
		// byte temp = randomAccessFile.readByte();
		// randomAccessFile.seek(i);
		// randomAccessFile.writeByte(temp);
		// }
		// randomAccessFile.seek(skip);
		// randomAccessFile.write(b);
		// randomAccessFile.close();
		//
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
