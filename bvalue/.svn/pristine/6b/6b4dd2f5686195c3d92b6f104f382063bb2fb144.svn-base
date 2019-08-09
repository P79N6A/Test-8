package com.tydic.beijing.bvalue;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.service.QueryInfoBPoolCRM;
// import com.tydic.beijing.bvalue.service.impl.QueryBActivityCRMImpl;

public class QueryBActivityCRMMain {
	private static Logger log=Logger.getLogger(QueryBActivityCRMMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("queryInfoBPoolCRM.xml");
		context.start();
		log.debug("service QueryBActivityMain start ....");
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
		
		
 //       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
//		QueryInfoBPoolCRM queryinfobpoolcrm =  (QueryInfoBPoolCRM) context.getBean("queryInfoBPoolCRMImpl") ;
//		
//		
//			JSONObject inputJson = new JSONObject();
//		    //inputJson.put("ActivityType", "3");
//	    
//		    //inputJson.put("CreatTime", "2015-01-01 00-00-00");
//		    inputJson.put("PageIndex", "1");
//		    inputJson.put("RowPerPage", "10");
//		    //inputJson.put("DepartMentType", "1");
//		    JSONObject retJson =  queryinfobpoolcrm.queryInfoBPoolCRM(inputJson);
//		    log.debug("查询获得返回值=====>"+retJson.toString());
//			
		

	}

}
