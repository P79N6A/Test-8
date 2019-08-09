package com.tydic.beijing.billing.interfacex;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ibm.icu.util.Calendar;
import com.jiexun.sms.sv.SendMsg;
import com.tydic.beijing.billing.interfacex.service.impl.SendMessageServiceImpl;

public class SendMessageServiceMain {

	private static Logger  log=Logger.getLogger(SendMessageServiceMain.class);
	
	public static void main(String args[]){
		
		if (args.length == 0){
            log.error(">>>>>>>>>>>>>main args[] lost ..." );
            return ;
		}
		String instanceId =args[0];
     	if (!Character.isDigit( instanceId.charAt(0))){   //!F
            log.error(">>>>>>>>>>>illegal instanceId ....." );
            return ;
     	}

		
		String cmd= "jps -m|awk '/SendMessageServiceMain "+instanceId+"$/'|cat -n|sed -n '$p'|awk '{print $1}'";

        Process proc= null ;
         try {
                proc= Runtime. getRuntime().exec( new String[]{"/bin/sh" ,"-c" ,cmd }, null, null);
               
                proc.waitFor();
                BufferedReader out = new BufferedReader( new InputStreamReader(proc .getInputStream()));
            
               String process= out.readLine();
               log.debug("readline:" +process );
               
                if (process != null && Integer.parseInt(process)>1){
                      log .error(">>>>>>>>>>>instance :" +instanceId +" exists!!!!");
                      return ;
               }
               
                proc.destroy();
               
                InetAddress net = InetAddress.getLocalHost();
        		if(net !=null){
        			instanceId=net.getHostAddress().replace(".", "")+instanceId;
        		}else{
        			System.out.println("inetaddress is null ");
        		}
        } catch (IOException e ) {
                log .error("start error :" +e .getMessage());
                return ;
        } catch (InterruptedException e ){
                if (proc != null){
                	proc.destroy();
                }
                log .error("start error:" +e .getMessage());
                return ;
        } finally {
                proc= null ;
               
        }

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "smsSendService.xml" });
 		
 		context.start();
		SendMessageServiceImpl service=(SendMessageServiceImpl)context.getBean("SendMsgService");

		service.setInstanceId(instanceId);
		service.send();
		
		log.debug("\n++++++++++++++++++++++++\n"+Calendar.getInstance().getTime()+
				":service SendMsgService  stopped.........\n++++++++++++++++++++++\n");
		
		context.stop();
		context.close();
		
	}
	
}
