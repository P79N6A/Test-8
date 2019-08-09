package com.tydic.beijing.billing.resourceCharge;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.billing.interfacex.service.ResourceChargeService;
import com.tydic.beijing.billing.resourceCharge.ResourceCharge;
import com.tydic.beijing.billing.dto.ResourceChargeParaIn;
import com.tydic.beijing.billing.dto.ResourceChargeParaOut;
import com.tydic.beijing.billing.dto.ResourceChargeParaInList;


public class ResourceCharge {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(ResourceCharge.class);
	
	public static void main(String[] args){
	
	log.debug("---resourceCharge start ---");
	log.debug("---parsing resourceCharge.xml begin----");
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"resourceCharge.xml"});
	log.debug("---resourceCharge.xml parsing end---");
	log.debug("---get resourceChargeServiceImpl bean----");
	//context.start();
	ResourceChargeService resourceChargeService = (ResourceChargeService) context.getBean("ResourcCharge");
	//ResourceChargeDb ChargeDb = (ResourceChargeDb) context.getBean("ChargeDb");
	
	try{
		ResourceChargeParaOut resourceChargeParaOut = new ResourceChargeParaOut();
		ResourceChargeParaIn resourceChargeParaIn = new ResourceChargeParaIn();
		ResourceChargeParaInList paraList = new ResourceChargeParaInList();
		ResourceChargeParaInList paraList1 = new ResourceChargeParaInList();
		List<ResourceChargeParaInList> resourceChargeParaInList = new ArrayList<ResourceChargeParaInList>();
		
		paraList.setBalanceTypeId(10);
		paraList.setChargeValue(51200);
		paraList.setEffDate("20150116000000");
		paraList.setExpDate("20150310235959");
		resourceChargeParaInList.add(paraList);
		paraList1.setBalanceTypeId(11);
		paraList1.setChargeValue(51200);
		paraList1.setEffDate("20150116000000");
		paraList1.setExpDate("20150320235959");
		resourceChargeParaInList.add(paraList1);
		
		resourceChargeParaIn.setResourceChargeParaInList(resourceChargeParaInList);
		resourceChargeParaIn.setOperateType("9");
		resourceChargeParaIn.setPayId("21000000002015");
		resourceChargeParaIn.setSN("nhttest001");
		
		log.debug("---resourceChargeService.charge(resourceChargeParaIn)----");
		resourceChargeParaOut = resourceChargeService.charge(resourceChargeParaIn);

		
	}catch(InterruptedException e){
		e.printStackTrace();
	}catch (Exception e) {

		e.printStackTrace();
	}

	}
}
