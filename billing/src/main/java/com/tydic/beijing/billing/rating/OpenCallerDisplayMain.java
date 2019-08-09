package com.tydic.beijing.billing.rating;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dao.CDRCalling;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.beijing.billing.rating.domain.InfoUserReason;
import com.tydic.beijing.billing.rating.service.impl.OpenCallerDisplayProcess;
import com.tydic.dsi.log.logger.Logger;
import com.tydic.dsi.log.logger.LoggerFactory;

public class OpenCallerDisplayMain {
	public static final  Logger log = LoggerFactory.getLogger(OpenCallerDisplayMain.class);

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"OpenCallerDisplay.xml"});
		context.start();
		OpenCallerDisplayProcess opencdp=(OpenCallerDisplayProcess)context.getBean("openCallerDisplay");
		log.debug("开始开来显了！！！");
//		while (true) {
		//	opencdp.OpenCallDisplay();
//			Thread.sleep(60000L);
//		}
		
		// TODO Auto-generated method stub
				log.debug("===============实时调用，开来显服务=============");
				List<InfoUserReason> listOfInfoUserReason=new ArrayList<InfoUserReason>();
				List<CDRCalling> listOfCdrCalling=new ArrayList<CDRCalling>();
				InfoUser infoUser=new InfoUser();
				long begintime=System.currentTimeMillis();
				long beginNum=0;
				long endNum=100;
				while(true){
					try {
						listOfInfoUserReason=opencdp.getListOfInfoUserReason(beginNum, endNum);//查询停掉来显的用户
						int flag=opencdp.openCallerDisplay(listOfInfoUserReason,begintime,beginNum, endNum,listOfCdrCalling,infoUser);
						if (flag==1) {
							beginNum=0;
							endNum=100;
						}else {
							beginNum+=100;
							endNum+=100;
						}
					} catch (Exception e) {
						// TODO: handle exception
						log.error("报错了！！！");
						e.printStackTrace();
						System.exit(0);
					}
					
				}
	}
}
