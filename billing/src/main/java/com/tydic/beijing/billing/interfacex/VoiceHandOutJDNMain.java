package com.tydic.beijing.billing.interfacex;


import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.impl.VoiceHandOutImpl;
import com.tydic.beijing.billing.interfacex.service.impl.VoiceHandOutJDNImpl;

public class VoiceHandOutJDNMain {
	private static final Logger log=Logger.getLogger(VoiceHandOutJDNMain.class);
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"voice_hand_out_jdn.xml"});
		context.start();
		VoiceHandOutJDNImpl vImpl=(VoiceHandOutJDNImpl)context.getBean("voiceHandout");
		try {
			vImpl.uploadVoice();
		} catch (Exception e) {
			log.error(e.toString(),e);
			e.printStackTrace();
			e.getMessage();
		}
		context.close();
	}

}
