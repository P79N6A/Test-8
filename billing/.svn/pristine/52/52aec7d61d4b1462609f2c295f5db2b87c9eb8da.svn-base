package com.tydic.beijing.billing.rating;

 
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.rating.domain.RatingMsg;
import com.tydic.beijing.billing.rating.domain.RuleEventTypeTree;
import com.tydic.beijing.billing.rating.dto.DbConfigDetail;


public class CopyOfMain {


	private static final Logger log = Logger.getLogger(CopyOfMain.class);
     private static List<String> listforprint = new ArrayList<String>();
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		
		log.debug("suc");
		
	
		
		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"rating.xml"});
        
		//context.start();
		
		//RatingMsg ratingMsg =new RatingMsg();
		
		
		//ApplicationContextHelper applicationContextHelper = (ApplicationContextHelper) context.getBean("applicationContextHelper");
//		
		//DbConfigDetail dbConfigDetail = (DbConfigDetail) context.getBean("dbConfigDetail");
	
		//printeventbyRoot(dbConfigDetail.getAllRuleEventTypeTree());

//		long start = System.currentTimeMillis();
//        	while (true) {
//        		Thread.sleep(10000L);
//        	}

		//[100=80][101=20121020211803][000=c1-172-100-100-66-GGSNHI01.chinaunicom.com;1332783460;16788222;4262264831][001=2311263601924864364][R71=0][R85=1][R01=13006900012][R30=000,R301=220.206.147.178,R302=220.206.147.162,R305=cmnet,R306=0,R307=172.17.0.116,R309=3600,R3010=99-13921f739697eb74821040,R3012=460015862510310,R3013=1][R50=000,R504=0898,R505=0898,R5011=0,R5012=0898][R60=000,R601=0,R602=1,R603=0,R604=20121020211739,R605=0][B01=000,B010=4262264831,B016=3,B017=1024000][B06=1][B07=1][B08=1][B20=1][B21=1][vs=2]
		//Rating rating = (Rating) context.getBean("rating");
		//ChargeInitImpl rating = (ChargeInitImpl)  context.getBean("ChargeInitImpl");
		//String inputstr ="[100=60][101=201410201620380000][000=0][001=0011216279974223641][R01=17090830980][R02=17090830980][R03=17090830981][R71=20140916152427][R85=2][R65=0][R10=000,R103=1][R50=000,R504=010,R505=0477,R506=3,R507=010,R508=010,R509=0,R5010=3,R5011=4,R5012=010][B09=000,B090=1,B092=1,B096=1][B30=000,B302=1,B306=1,B307=121]";
		//="[100=80][101=20121020211803][000=c1-172-100-100-66-GGSNHI01.chinaunicom.com;1332783460;16788222;4262264831][001=2311263601924864364][R71=0][R85=1][R01=18614076724][R30=000,R301=220.206.147.178,R302=220.206.147.162,R305=cmnet,R306=0,R307=172.17.0.116,R309=3600,R3010=99-13921f739697eb74821040,R3012=460015862510310,R3013=1][R50=000,R504=0898,R505=0898,R5011=0,R5012=0898][R60=000,R601=0,R602=1,R603=0,R604=20141002211739,R605=0][B01=000,B010=4262264831,B016=3,B017=1024000][B06=1][B07=1][B08=1][B20=1][B21=1][vs=2]";
		
//		RatingImpl rating = (RatingImpl) context.getBean("rating");
//		
//				try {
//			String resultstr =rating.ratingInit(inputstr);
//					//String resultstr =rating.deal(inputstr);
//		} catch (Exception e) {
//			e.printStackTrace();
//		   // LOG.debug("main收到的异常======"+e.printStackTrace(););
//		}
//		long end = System.currentTimeMillis();
//    
//		CodeSpecialNbr num = new CodeSpecialNbr();
//		String xxx= num.getSpecial_nbr();
//		System.out.println(xxx);


	
		
		
	}
	

	
	
	private static void printeventbyRoot(List<RuleEventTypeTree>  allevent){
		
		for(RuleEventTypeTree tmproot:allevent){
			if(tmproot.getUp_event_type_rule_tree_id() ==-1){
				//log.debug(tmproot.getEvent_type_rule_tree_id()+tmproot.getEvent_type_rule_name()+"-->");
				
				if(tmproot.getEvent_type_rule_tree_id() !=10000 ){
					continue;
				}
				
				listforprint.clear();
				printevent(tmproot,allevent);
				
			}
		}
	}


	private static void printevent(RuleEventTypeTree tmproot,List<RuleEventTypeTree>  allevent) {
		
		if(tmproot.getEvent_type_id() ==-1){
			
			String tmpforprint = tmproot.getEvent_type_rule_tree_id()+tmproot.getEvent_type_rule_name()+"	";
					listforprint.add(tmpforprint);
			
			printlist();
					
			for(RuleEventTypeTree son:allevent){
				int i=0;
				if(son.getUp_event_type_rule_tree_id()==tmproot.getEvent_type_rule_tree_id()){
					printevent(son,allevent);
					i++;
				}
				
				if(i>0){
					listforprint.remove(listforprint.size()-1);
				}
			}
			
		}else
		{
			String tmpforprint = tmproot.getEvent_type_rule_tree_id()+"->"+tmproot.getEvent_type_id()+"->"+tmproot.getEvent_type_rule_name()+"";
			listforprint.add(tmpforprint);
			printlist();
		}
		
	}


	private static void printlist() {
		
		  String str="";
//	       for(String tmp:listforprint){
//	    	   str=str+tmp;
//	       }
		  for(int n =0;n<listforprint.size()-1;n++){
			  str=str+"$	";
		  }
		  str = str+listforprint.get(listforprint.size()-1);
	       str = str.trim();

			 FileWriter writer = null;  
		        try {     
		            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
		            writer = new FileWriter("e:\\event.txt", true);     
		            writer.write(str+"\r\n");       
		        } catch (IOException e) {     
		            e.printStackTrace();     
		        } finally {     
		            try {     
		                if(writer != null){  
		                    writer.close();     
		                }  
		            } catch (IOException e) {     
		                e.printStackTrace();     
		            }     
		        }   
		        
		
	}

 
	
	 
	
	
}
