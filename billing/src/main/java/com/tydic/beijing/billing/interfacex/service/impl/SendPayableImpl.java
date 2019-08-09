/**
 * 
 */
package com.tydic.beijing.billing.interfacex.service.impl;

import java.util.Calendar;
import java.util.List;



import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONStringer;



import org.apache.log4j.Logger;

//import com.jd.fsp.application.saf.service.Payable4SafService;
import com.tydic.beijing.billing.dao.CurrentBillDto;
import com.tydic.beijing.billing.dao.PayUserRel;
import com.tydic.beijing.billing.dao.RemainBalance;
import com.tydic.beijing.billing.dao.UserInfoForMemCached;
import com.tydic.beijing.billing.dao.UserPayInfo;
import com.tydic.beijing.billing.dto.CurrentBill;
import com.tydic.beijing.billing.dto.QueryCurrentBillInfo;
import com.tydic.beijing.billing.dto.SendPayableDto;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.QueryCurrentBill;
import com.tydic.beijing.billing.interfacex.service.SendPayable;

/**
 * @author dongxuanyi
 *
 */
public class SendPayableImpl implements SendPayable{

	
	
	private static Logger log = Logger.getLogger(SendPayableImpl.class);

	@Override
	public boolean query(SendPayableDto info) {
		// TODO 自动生成的方法存根
		
		String jsops="[{\"rfBusinessId\":\"111\",\"uuid\":\"111\",\"supplierId\":\"bjlt001\",\"supplierName\":\"中国联通\",\"cooperationId\":\"110\",\"cooperationName\":\"北京京东叁佰陆拾度电子商务有限公司\",\"feeTypeId\":\"1\",\"amount\":\"100.2\",\"calAmount\":\"90\",\"difAmount\":\"10\",\"adjAmount\":\"0.2\",\"tax\":\"17\",\"bussinessTime\":\"2014-10-10 11:10:01\",\"deptName\":\"xxxx部门\",\"contractId\":\"xxxx\",\"remark\":\"xxxx\" },{\"rfBusinessId\":\"222\",\"uuid\":\"222\",\"supplierId\":\"bjlt001\",\"supplierName\":\"中国联通\",\"cooperationId\":\"110\",\"cooperationName\":\"北京京东叁佰陆拾度电子商务有限公司\",\"feeTypeId\":\"1\",\"amount\":\"100.2\",\"calAmount\":\"90\",\"difAmount\":\"10\",\"adjAmount\":\"0.2\",\"tax\":\"17\",\"bussinessTime\":\"2014-10-10 11:10:01\",\"deptName\":\"xxxx部门\",\"contractId\":\"xxxx\",\"remark\":\"xxxx\" }]";
		//JSONArray  jsonArray=(JSONArray)info.getPayable();
		JSONArray  jsonArray=(JSONArray)JSONSerializer.toJSON(jsops);
		List list=(List)JSONSerializer.toJava(jsonArray);
		 for (Object  obj: list) {
	         JSONObject jsonObject = JSONObject.fromObject(obj);
		        String rfBusinessId=jsonObject.getString("rfBusinessId");
				String uuid=jsonObject.getString("uuid");
				String supplierId=jsonObject.getString("supplierId");
				String supplierName=jsonObject.getString("supplierName");
				String cooperationId=jsonObject.getString("cooperationId");
				String cooperationName=jsonObject.getString("cooperationName");
				String feeTypeId=jsonObject.getString("feeTypeId");
				String amount=jsonObject.getString("amount");
				String calAmount=jsonObject.getString("calAmount");
				String difAmount=jsonObject.getString("difAmount");
				String adjAmount=jsonObject.getString("adjAmount");
				String tax=jsonObject.getString("tax");
				String bussinessTime=jsonObject.getString("bussinessTime");
				String deptName=jsonObject.getString("deptName");
				String contractId=jsonObject.getString("contractId");
				String remark=jsonObject.getString("remark");
                                      
				log.debug("服务[SendPayable],[rfBusinessId],["+rfBusinessId+"]"); 
				log.debug("服务[SendPayable],[uuid],["+uuid+"]");
     
	  }
	
          //Payable4SafService PayableService;  	
          //PayableService.sendPayable(arg0, arg1, arg2, arg3, arg4)
		
		return true;
	}


}
