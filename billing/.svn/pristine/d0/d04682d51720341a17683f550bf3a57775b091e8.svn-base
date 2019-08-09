package com.tydic.beijing.billing.interfacex.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.CDRCalling;
import com.tydic.beijing.billing.dao.GroupMemberRel;
import com.tydic.beijing.billing.dao.LifeServiceAttr;
import com.tydic.beijing.billing.dao.LogCdrExpertHis;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.util.SftpClient;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class ExpertCdrForCallTransferPartner {
	
	private long modValue;

	public long getModValue() {
		return modValue;
	}


	public void setModValue(long modValue) {
		this.modValue = modValue;
	}
	 private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private final static Logger log = Logger.getLogger(ExpertCdrForCallTransferPartner.class);
	
	/**
	 * @param startTime
	 * @param endTime
	 */
	@Transactional(rollbackFor=Exception.class)
	public void expert(RuleParameters tmpPartner) throws Exception{
		
	       int fileName_suf =1;
	    //   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String currTime = sdf.format(new Date());
			
			
			//该配置包含的上传IP和端口、用户名密码是否可用,
			String paraChar1 = tmpPartner.getPara_char1();
			String[] paraChar2 = tmpPartner.getPara_char2().split(":");
			String serverAddress = paraChar2[0];
			int serverPort =Integer.parseInt(paraChar2[1]);
			String user = paraChar2[2];
			String pwd = paraChar2[3];
			String filePrefix = paraChar2[4];
			
			String fileName_pre = filePrefix+currTime; //汽车之家"CRHM"
			
			log.debug("check config detail===>"+tmpPartner.toString());
			
			SftpClient sftpClient = new SftpClient();
			sftpClient.setHost(serverAddress);
			sftpClient.setPort(serverPort);
			sftpClient.setUsername(user);
			sftpClient.setPassword(pwd);
			
			//备份地址是否可用 fileDirBak  + fileName_pre
			String fileDirBak = tmpPartner.getPara_char4();
			String remoteFileDir = tmpPartner.getPara_char3();

			//获取该合作商最近一次上传记录
			String startTime ="";
			String endTime ="";
			List<LogCdrExpertHis> logCdrExpertHislist = getLastExpertInfo(tmpPartner);
			//是否能查到上传记录
			if (logCdrExpertHislist !=null && logCdrExpertHislist.size()==1){
				//确定本次要导出详单的起始时间和终止时间
				startTime = logCdrExpertHislist.get(0).getExpert_end_time(); //本次提取的开始时间是上次提取的结束时间
				endTime = getEndTime(startTime,tmpPartner.getPara_num1());
			}else{
				//TODO 没有找到上次的记录，或者找到的数量异常
				log.error("expert his not found!! or found multi records!!,please check log_cdr_expert_his by group_id ="+ tmpPartner.getPara_char1() );
				throw new Exception("查询导出历史异常"+tmpPartner.getPara_char1());
			}
			
			if(!isTime(logCdrExpertHislist.get(0).getNext_time())){
				//如果当前时间还未到下次导出时间，则返回
				return ;
			}
			
			log.debug("expert cdr since "+startTime +" to "+endTime);
						
			//获取该合作商下所有的在网用户
//			List<GroupMemberRel> listofAutoHomeUser = new ArrayList<GroupMemberRel>();
			long cnt =0l; 
//			listofAutoHomeUser = S.get(GroupMemberRel.class).query(Condition.build("queryForAutoHome").filter("group_id",paraChar1)); 
//		    log.debug("group_id "+tmpPartner.getPara_char1()+" have "+listofAutoHomeUser.size()+" members!");
		    List<String> listFile = new ArrayList<String>();
		
//		    for(GroupMemberRel tmpinfouser:listofAutoHomeUser){

				Map<String, Object> filter = new HashMap<String, Object>();
				filter.put("startTime", startTime);
				filter.put("endTime", changeEndTime(endTime));
				filter.put("table", "100");
//				filter.put("userId",tmpinfouser.getUser_id());
				filter.put("month", getMonth(startTime.substring(4,6)));
				filter.put("group_id",paraChar1);
				List<CDRCalling> listCdr = S.get(CDRCalling.class).query(Condition.build("queryCdrForCallForwardPartner").filter(filter)); 
				
				for(CDRCalling tmpcdr:listCdr){
					
					if(tmpcdr.getCall_type()==null || !tmpcdr.getCall_type().equals("2")){
						continue;//只提呼叫前转话单
					}
					
					//写入文件
				    cnt++;
					if(cnt %  modValue ==0){
						fileName_suf ++;
					}
					String suf =  getfullsuf(fileName_suf,3);
					
				    String fullFileName = fileName_pre+"_"+ suf +".txt";
				    //String fullFileName_p = fullFileName+"_p";
				    File outfile = new File( fileDirBak +  fullFileName);
				    
				    if(!listFile.contains(fullFileName)){
				    	listFile.add(fullFileName);
				    } 
				    
				    if(!outfile.exists()){
				       outfile.createNewFile();	
				    }
				    
//				    0-主叫呼出话单
//				    1-被叫呼入话单
//				    2-呼叫前转话单
//				    3-呼转拆分的话单
				    String callType = tmpcdr.getCall_type();
				    String sessionbegintime = sdf.format(tmpcdr.getStartTime());
				    String chargedParty ="";
				    String oppNumber ="";
				    if(callType.equals("0") || callType.equals("2")){
				    	chargedParty = tmpcdr.getCallingparty();
				    	oppNumber = tmpcdr.getCalledparty();
				    	//增加荣联呼转的号码转换
					   // oppNumber= getFinalCalledNumber(tmpinfouser.getUser_id(),oppNumber,sessionbegintime); C号不再转D号 20160628
				    }else{
				    	oppNumber = tmpcdr.getCallingparty();
				    	chargedParty = tmpcdr.getCalledparty();
				    }
				    
				    
				    
				    double fee = getfee(tmpcdr.getTariffinfo());
	               
				    
				    String appendTxt = chargedParty +"|"+sessionbegintime +"|" +
				              tmpcdr.getRoamingType()+"|" +callType +"|" + tmpcdr.getCallduration() +"|" 
				              +oppNumber +"|"+fee +"|"+tmpcdr.getFowardingnumber();
				    appendfile(appendTxt,outfile);
				   
				    
				}
				
				 
//			}
			
		    //update and insert log_cdr_expert_his
		    udpateAndInsertExpertHis(tmpPartner,startTime,endTime,logCdrExpertHislist.get(0),getFileName(listFile));
		    
		    log.debug("allfilename==>"+ listFile.toString());
		    
			//上传upload
		    try {
				sftpClient.connect();
				 for(String oldfile:listFile){
				
					 log.debug("filename==>"+(fileDirBak+oldfile));
					 sftpClient.upload(remoteFileDir, new File(fileDirBak+oldfile),oldfile+"_p");
					 sftpClient.rename(remoteFileDir+oldfile+"_p", remoteFileDir+oldfile);
				 }
				 sftpClient.disConnect();
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				log.error("error in uploading to sftp :"+e.getMessage()+",,,file:"+listFile.toString());
				sftpClient.disConnect();
			}
			
			
			
		}
		

//	private String getFinalCalledNumber(String user_id, String oppNumber,String calltime) {
//		
//		//121荣联呼转号码  120普通呼转号码
//		List<LifeServiceAttr> attrList = S.get(LifeServiceAttr.class).query(Condition.build("getUserAttrbyUserId").filter("user_id",user_id).filter("serv_attr", 121).filter("cdrtime",calltime));
//		
//		if(attrList !=null && attrList.size()>0){
//			return attrList.get(0).getServ_attr_value();
//		}else{
//			return oppNumber;
//		}
//	}
	
	//vds不能支持>=和<=，所以结束时间向前调1秒
	private String changeEndTime(String endTime) throws Exception {
		Calendar cal = Calendar.getInstance();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		cal.setTime(sdf.parse(endTime));
		cal.add(Calendar.SECOND, -1);
		
		return sdf.format(cal.getTime());
	}


	private String getFileName(List<String> listFile) {
		
		String fullFileName="";
		for(String tmpfile:listFile){
			int pos = tmpfile.lastIndexOf("/")+1;
			fullFileName= fullFileName+tmpfile.substring(pos)+"|";
		}
		
		return fullFileName;
	}


	private boolean isTime(String next_time) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currTime = sdf.format(new Date());
		if(currTime.compareTo(next_time) >=0){
			return true;
		}		
		
		return false;
	}


	/**
	 * @param tmpPartner group配置明细
	 * @param startTime 本次导出话单的起始时间
	 * @param endTime  本次导出话单的终止时间
	 * @param logCdrExpertHis2 上次的导出记录
	 */
	private void udpateAndInsertExpertHis(RuleParameters tmpPartner,
			String startTime, String endTime, LogCdrExpertHis logCdrExpertHis2,String fileName) throws Exception{
		
		logCdrExpertHis2.setIslastdeal("N");
		logCdrExpertHis2.setCdr_source("VOP");
		S.get(LogCdrExpertHis.class).update(logCdrExpertHis2);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		LogCdrExpertHis logCdrExpertHis = new LogCdrExpertHis();
		logCdrExpertHis.setGroup_id(tmpPartner.getPara_char1());
		logCdrExpertHis.setProcess_time(sdf.format(new Date()));
		logCdrExpertHis.setExpert_start_time(startTime);
		logCdrExpertHis.setExpert_end_time(endTime);
		logCdrExpertHis.setIslastdeal("Y");
		logCdrExpertHis.setNext_time(getEndTime(endTime, tmpPartner.getPara_num1()+10));//下次导出时间定为下个周期结束后推10分钟，避免系统时间差造成误差
		logCdrExpertHis.setFilename(fileName);
		logCdrExpertHis.setNote("");
		logCdrExpertHis.setCdr_source("VOP");
		S.get(LogCdrExpertHis.class).create(logCdrExpertHis);
		
	}


	private void appendfile(String strLine, File sourceFile) {
		 FileWriter writer = null;  
	        try {     
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
	            writer = new FileWriter(sourceFile.getPath(), true);     
	            writer.write(strLine+"\r\n");       
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
		/**
		 * @param startTime 开始时间
		 * @param para_num1 时间间隔(分钟)
		 * @return
		 */
		private String getEndTime(String startTime, int para_num1) throws Exception{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			cal.setTime(sdf.parse(startTime));
						
			cal.add(Calendar.MINUTE, para_num1);
						
			return sdf.format(cal.getTime());		
	}

	//获取最近一次导出上传记录
	private List<LogCdrExpertHis> getLastExpertInfo(RuleParameters tmpPartner) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("group_id", tmpPartner.getPara_char1());
		filter.put("islastdeal", "Y");
		filter.put("cdr_source", "VOP");
		return S.get(LogCdrExpertHis.class).query(
				Condition.build("getLastExpertInfo").filter(filter));
	}


    private static double getfee(String tariffinfo) {
		
		String[] infos = tariffinfo.split(";"); 
		int sumvalue =0;
	    for(int i=0;i<infos.length;i++){
	    	String info = infos[i];
	    	String[] kv =info.split(":");
	    	String value = kv[1];
	    	String key = kv[0];
//	    	if( !key.equals("10") && !key.equals("11") &&!key.equals("12")  ){
	    		sumvalue = sumvalue + Integer.parseInt(value);
//	    	}
	    	
	    }
	    
	    log.debug("cost money "+sumvalue);
	    
		return 1.0*sumvalue/100;
	} 
	
	/**
	 * @return 获取配置的需要导出呼转话单的合作商配置
	 */
	public List<RuleParameters> getAllCallTransferConfig() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("domain_code", 5040);
		filter.put("para_name", "EXPERTFORWARDCDR");
		return S.get(RuleParameters.class).query(
				Condition.build("queryByDomainCodeandParaName").filter(filter));
	}



 
	private Object getMonth(String substring) {
		int month = Integer.parseInt(substring) -1 ;
		
		if(month==0){
			month = 12;
		}
		return getfullsuf(month,2);
	}
	
	private static String getfullsuf(int fileName_suf,int length) {

        String orgsuf = "000000000"+fileName_suf;
		String fullsuf = orgsuf.substring(orgsuf.length()-length,orgsuf.length());
		return fullsuf;
	}
	
	


}
