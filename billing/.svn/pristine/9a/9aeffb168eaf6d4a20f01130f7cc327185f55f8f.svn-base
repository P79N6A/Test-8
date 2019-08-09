package com.tydic.beijing.billing.interfacex;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.impl.VoiceDonwloadImpl;

public class VoiceDonwloadMain {
	private static final Logger log = Logger.getLogger(VoiceDonwloadMain.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "voice_donwload.xml" });
		context.start();
		VoiceDonwloadImpl vImpl = (VoiceDonwloadImpl) context.getBean("voiceDonwload");
		try {
			vImpl.voiceDonwLoad();
		} catch (Exception e) {
			log.error(e.toString(), e);
			e.printStackTrace();
			e.getMessage();
		}
		context.close();
	}
			

}
