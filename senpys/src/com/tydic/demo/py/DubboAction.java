package com.tydic.demo.py;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jd.fsp.criterion.saf.service.PayableCriterionSAFService;
import com.tydic.jdSaf.registerTool.SafServiceRegisterTool;
public class DubboAction {
	
	private PayableCriterionSAFService payableSafService;

	// Spring 方法注入

	public String sendPayable(int rfSystemId,String feeTypeId,String payableJson, String payableExtJson,String configJson) {
		System.out.println("rfSystemId="+rfSystemId);
		System.out.println("feeTypeId="+feeTypeId);
		System.out.println("payableJson="+payableJson);
		System.out.println("payableExtJson="+payableExtJson);
		System.out.println("configJson="+configJson);
		String CriterionSafResult="";
		try {
			CriterionSafResult=this.payableSafService.sendCriterionPayable(rfSystemId,feeTypeId,payableJson,payableExtJson,configJson);
		} catch (Exception e) {
			System.out.println("调用失败=!!!!!!!!!!!!!!!!!!!!!!!");
			e.printStackTrace();
		}
		System.out.println("ok=!!!!!!!!!!!!!!!!!!!!!!!");
		return CriterionSafResult;
	}
	
	

	
	
	
@SuppressWarnings("unused")
public static void main(String[] args) {
	{
		
		SafServiceRegisterTool safServiceRegisterTool = new SafServiceRegisterTool(); 
		//safServiceRegisterTool.create("/saf_service/com.tydic.jdTest.safProvider.SafService/consumers","127.0.0.1:2181");
		//safServiceRegisterTool.create("/saf_service/com.tydic.jdTest.safProvider.SafService","127.0.0.1:2181");
		//safServiceRegisterTool.getChild("127.0.0.1:2181", "/saf_service/com.tydic.jdTest.safProvider.SafService");
		//safServiceRegisterTool.delete("127.0.0.1:2181", "/saf_service/com.tydic.jdTest.safProvider.SafService/routers");
		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]
			//{ "consumer.xml" });
		//DubboAction ddAction= (DubboAction) context.getBean("dubboAction");
		//ddAction.sendPayable(1, 1, "1", "1");
		//SafService ff=(SafService) context.getBean("payable4SafService");
		//ff.sendPayable(1, 1, 0, "1", "1");
		//System.out.println("DubboAction.main()");
	}
}






public void setPayableSafService(PayableCriterionSAFService payableSafService) {
	this.payableSafService = payableSafService;
}
	
}
