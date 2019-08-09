package com.tydic.beijing.billing.interfacex.service.impl;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.CDR100Transfer;
import com.tydic.beijing.billing.dao.QTransferDownload;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.ReceiveCDR;

import net.sf.json.JSONObject;
/*
 * 1、时间转换格式前，应该先校验是否符合规则或者为空，或者超出当前时间， 
 */


public class ReceiveCDRImpl implements ReceiveCDR {
	
	private static Logger log=Logger.getLogger(ReceiveCDRImpl.class);
	
	private DbTool dbTool ;
		@Override
	public JSONObject receiveCdr(JSONObject input) {
			

		long time =System.currentTimeMillis();
			log.debug("荣联报文===================》"+input);
			JSONObject output=new JSONObject();	


//		从JSON中取值
		String callId = input.getString("CallId");
		String caller = input.getString("Caller");
		String origCalled = input.getString("OrigCalled");
		String transferCall = input.getString("TransferCall");
		String calledResult = input.getString("CalledResult");
		String callerStartTime = input.getString("CallerStartTime");
		
		//非必传字段，加入传空值时的判断，若为空或者为0时则为null
		String called = input.containsKey("Called")?input.getString("Called"):null;
		String callerEndTime = input.containsKey("CallerEndTime")?input.getString("CallerEndTime"):null;
		String calledStartTime = input.containsKey("CalledStartTime")?input.getString("CalledStartTime"):null;
		String calledRingTime = input.containsKey("CalledRingTime")?input.getString("CalledRingTime"):null;
		String calledAnswerTime = input.containsKey("CalledAnswerTime")?input.getString("CalledAnswerTime"):null;
		String calledEndTime = input.containsKey("CalledEndTime")?input.getString("CalledEndTime"):null;
		String endPart = input.containsKey("EndPart")?input.getString("EndPart"):null;
		String recordUrl = input.containsKey("RecordUrl")?input.getString("RecordUrl"):null;
		
		//获取当前系统时间，存入receiveTime
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String receiveTime = dateFormat.format( now ); 
		
		String videoLoadTime = "";
		String videoFileName = "";
		
		//校验主键callid是否已经存在
		CDR100Transfer cdrt = new CDR100Transfer();	
		QTransferDownload qtd = new QTransferDownload();
			
		int count = dbTool.queryCallId(callId,receiveTime.substring(4,6));
		if(count > 0){
			log.error("callId已经存在，主键冲突");
			output.put("Status", "0");
			output.put("ErrorCode", "S-SIM-03000");
			output.put("ErrorMessage", "callId重复，主键冲突");
		}else{
		
		
		//检验报文信息是否完整		
		if(callId == null || callId.isEmpty()){
  			log.error("callId不存在");
  			output.put("Status", "0");
  			output.put("ErrorCode", "S-SIM-03001");
  			output.put("ErrorMessage", "callId不存在");
		}else if(caller == null || caller.isEmpty()){
			log.error("caller主叫号码不存在");
			output.put("Status", "0");
			output.put("ErrorCode", "S-SIM-03002");
  			output.put("ErrorMessage", "caller不存在");
		}else if(origCalled == null || origCalled.isEmpty()){
			log.error("origCalled被叫号码不存在");
			output.put("Status", "0");
			output.put("ErrorCode", "S-SIM-03003");
  			output.put("ErrorMessage", "origCalled不存在");
		}else if(transferCall == null || transferCall.isEmpty()){
			log.error("transferCall中继号不存在");
			output.put("Status", "0");
			output.put("ErrorCode", "S-SIM-03004");
  			output.put("ErrorMessage", "transferCall不存在");
		}else if(calledResult == null || calledResult.isEmpty()){
			log.error("calledResult呼叫结果编码不存在");
			output.put("Status", "0");
			output.put("ErrorCode", "S-SIM-03005");
  			output.put("ErrorMessage", "calledResult不存在");
		}else if(callerStartTime == null || callerStartTime.isEmpty()){
			log.error("callerStartTime接入荣联平台时间点不存在");
			output.put("Status", "0");
			output.put("ErrorCode", "S-SIM-03006");
  			output.put("ErrorMessage", "callerStartTime不存在");
		}else{
			
			//增加获取用户当前归属的集团编码20160628
			String groupid = dbTool.getGroupIdbyDeviceNumber(origCalled);
			
			//将获得的值插入两张表中
					
			cdrt.setCallId(callId);
			cdrt.setCaller(caller);             
			cdrt.setOrigCalled(origCalled);         
			cdrt.setTransferCall(transferCall);       
			cdrt.setCalled(called);             
			cdrt.setCalledResult(calledResult);       
			cdrt.setCallerStartTime(callerStartTime);    
			cdrt.setCallerEndTime(callerEndTime);      
			cdrt.setCalledStartTime(calledStartTime);    
			cdrt.setCalledRingTime(calledRingTime);     
			cdrt.setCalledAnswerTime(calledAnswerTime);   
			cdrt.setCalledEndTime(calledEndTime);	
			cdrt.setEndPart(endPart);
			cdrt.setRecordUrl(recordUrl);
			cdrt.setReceiveTime(receiveTime);
			cdrt.setVideoLoadTime(videoLoadTime);
			cdrt.setVideoFileName(videoFileName);
			cdrt.setGroupid(groupid);
			cdrt.setPartitionId(receiveTime.substring(4, 6));
			
			qtd.setCallId(callId);
			qtd.setCaller(caller);
			qtd.setOrigCalled(origCalled);
			qtd.setCalled(called); 
			qtd.setRecordUrl(recordUrl);
			qtd.setPartitionId(receiveTime.substring(4,6));

			//调用方法将值插入表中
			dbTool.insertCDR100Transfer(cdrt,qtd);

			log.debug("荣联接口表============>"+cdrt.toString());
			log.debug("荣联接口Q表============>"+qtd.toString());
			output.put("Status", "1");
			}
		}
			
			long time1 = System.currentTimeMillis();
			
			log.debug("输出结果output是："+output.toString());	
			log.debug("本次耗时====================》"+(time1-time)+"ms");
		
		return output;
		
	
	}


	public DbTool getDbTool() {
		return dbTool;
	}
	public void setDbTool(DbTool dbTool) {
		this.dbTool = dbTool;
	}
	
	
	
}
