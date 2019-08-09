package com.tydic.beijing.billing.interfacex;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ibm.icu.util.Calendar;
import com.tydic.beijing.billing.interfacex.sendEdm.impl.SendEdmImpl;



/**
 * 
 * @author Dongxuanyi
 *
 */
public class SendEdmMain {
	private final static Logger log = Logger.getLogger(SendEdmMain.class);
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "sendEdm.xml" });
		context.start();
		int mod = Integer.parseInt(args[0]);
		log.debug("service SendEdm start successful.....");
		log.debug("mod....."+mod);
		SendEdmImpl SendEdms=(SendEdmImpl)context.getBean("SendEdm");
		if(mod == 1){
			try {
				SendEdms.sendmail();
			} catch (ClassNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else if(mod == 2){
			SendEdms.sendmailsState();
		}else if(mod == 3){
			SendEdms.sendmails();
		}else if(mod == 4){
			SendEdms.saveUserinfo();
		}
		
		log.debug(Calendar.getInstance().getTime()+":service SendEdm  stopped.........");
		
		context.stop();
		context.close();
		
		/*while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		*/

		
	}
	
}
