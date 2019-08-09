package com.tydic.beijing.billing.interfacex;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONStringer;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 * 
 * @author Dongxuanyi
 *
 */
public class SendPayableMain {
	private final static Logger log = Logger.getLogger(SendPayableMain.class);
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "SendPayable.xml" });
		context.start();
		log.debug("service SendPayable start successful.....");
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		

		
	}
	
}
