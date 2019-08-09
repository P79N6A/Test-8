package com.tydic.beijing.billing.interfacex;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.dto.ResourceAdjustRequest;
import com.tydic.beijing.billing.dto.ResourceAdjustResponse;
import com.tydic.beijing.billing.dto.ResourceDto;
import com.tydic.beijing.billing.interfacex.service.impl.ResourceAdjustImpl;

public class ResourceAdjustMain {
	private final static Logger log = Logger.getLogger(ResourceAdjustMain.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "resourceAdjust.xml" });
		context.start();
		
		
		while (true) {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
			}
		}
// 
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//		String eff="2014-10-01 18-22-55";
//		
//		try {
//			System.out.println(sdf.parse(eff));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		
//		
//			ResourceAdjustRequest rar = new ResourceAdjustRequest();
//			rar.setChannelNo("123");
//			rar.setJdPin("jd");
//			rar.setMSISDN("17090141871"); //17000000073 17090700161  17090700193
//			rar.setSessionId("11577112");
//			rar.setRequestTime("2014-10-24 18-00-00");
////			rar.setActivityType("6");
////			rar.setReason("net-test");
//			List<ResourceDto> ResourceDtoList  = new ArrayList<ResourceDto>(); 
//			ResourceDto tmprd = new ResourceDto();
//			tmprd.setEffDate("2014-12-01  11-12-34");
//			tmprd.setExpDate("2014-12-31 23-59-59");
//			tmprd.setResourceType("1");
//			tmprd.setResourceNumber(445);
//			
//			ResourceDto tmprd2 = new ResourceDto();
//			tmprd2.setEffDate("2014-12-01 00-00-00");
//			tmprd2.setExpDate("2014-12-31 23-59-59");
//			tmprd2.setResourceType("3");
//			tmprd2.setResourceNumber(-1204);
//			
//			
//			ResourceDtoList.add(tmprd);
//			ResourceDtoList.add(tmprd2);
//			rar.setResourceDtoList(ResourceDtoList);
//			rar.setStaffId("xx");
// 
//			ResourceAdjustImpl tmprai = (ResourceAdjustImpl) context.getBean("resourceAdjust");
//			long starttime =System.currentTimeMillis();
//			ResourceAdjustResponse tmpret = tmprai.doProcess(rar);
//			long endtime =System.currentTimeMillis();
//			
//			log.debug("调用资源调整接口结果："+tmpret.getStatus()+"errmsg="+tmpret.getErrorMessage());
//			log.debug("调用资源调整接口耗时："+(endtime-starttime));
//		
//		Date tmpdate1 = new Date(1412158975000L);
//		Date tmpdate2 = new Date(1412158900000L);
//	 
//		System.out.println(tmpdate1.compareTo(tmpdate2));
		
		
//		1412158975000
//		1414684800000
//		1414771199000

		 
		
	}
}
