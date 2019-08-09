package com.tydic.beijing.billing.interfacex;


import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.impl.VoiceHandOutImpl;

public class VoiceHandOutMain {
	private static final Logger log=Logger.getLogger(VoiceHandOutMain.class);
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"voice_hand_out.xml"});
		context.start();
		VoiceHandOutImpl vImpl=(VoiceHandOutImpl)context.getBean("voiceHandout");
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
