package com.tydic.beijing.billing.interfacex.service;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.ServerSession;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.CDR100TransferJDN;
import com.tydic.beijing.billing.dao.LifeServiceAttrForMemcache;
import com.tydic.beijing.billing.dao.SynchronizeInfo;
import com.tydic.uda.core.Condition;
/*
 * 1、时间转换格式前，应该先校验是否符合规则或者为空，或者超出当前时间， 
 */
import com.tydic.uda.service.S;



public class DealJDNCdrProcess  {
	
	private String path;
	//private int cdrcnt;
	
	
	private static final String YYCDR = "1";
	private static final String DXCDR = "3";
	
	private static Logger log=Logger.getLogger(DealJDNCdrProcess.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Transactional(rollbackFor=Exception.class)
	public void dealJDN(List<CDR100TransferJDN> jdncdrs) throws Exception{
		
		log.debug("jdncdrs.count===>"+jdncdrs.size());
		String timenow = dateFormat.format(new Date());
		//语音话单文件
		String fileName_yy = getFileName(timenow,"YY");
		log.debug("get yy filename ==>"+fileName_yy);
		String fileNameWithPath_yy = path + fileName_yy;
		String fileNameWithPath_yy_p = fileNameWithPath_yy +"_p";
		
		File cdrFile_yy = new File(fileNameWithPath_yy_p);
		File cdrFileFinal_yy = new File(fileNameWithPath_yy);
		if(!cdrFile_yy.exists()){
			log.debug("创建话单文件！");
			cdrFile_yy.createNewFile();
		}
		//短信话单文件
		String fileName_dx = getFileName(timenow,"DX");
		log.debug("get dx filename ==>"+fileName_dx);
		String fileNameWithPath_dx = path + fileName_dx;
		String fileNameWithPath_dx_p = fileNameWithPath_dx +"_p";
		
		File cdrFile_dx = new File(fileNameWithPath_dx_p);
		File cdrFileFinal_dx = new File(fileNameWithPath_dx);
		if(!cdrFile_dx.exists()){
			cdrFile_dx.createNewFile();
		}	
		

		for(CDR100TransferJDN tmpcdr:jdncdrs){
			if (tmpcdr.getDuration()!=0) {
			String deviceNumber = getBossCallingNbr(tmpcdr);
			String cdrinfo = generateCdrInfo(tmpcdr,deviceNumber);
			log.debug("京牛号码是===="+deviceNumber+",得到的话单是===="+cdrinfo);
			if(deviceNumber != null && deviceNumber.length() >0){
				if(tmpcdr.getCdrType().equals(DXCDR)){
					appendfile(cdrinfo,cdrFile_dx);
					tmpcdr.setFilename(fileName_dx);
				}else if (tmpcdr.getCdrType().equals(YYCDR)){
					log.debug("开始写入话单文件==="+fileName_yy);
					appendfile(cdrinfo,cdrFile_yy);
					tmpcdr.setFilename(fileName_yy);
				}
				
				tmpcdr.setStatus("1");//创建话单成功
				
			}else{
				tmpcdr.setStatus("2"); //没有找到
			}
			
			tmpcdr.setPartitionId(tmpcdr.getReceiveTime().substring(4,6));
			updateDbInfo(tmpcdr);
			}else {
				tmpcdr.setStatus("3");//通话时间是0
				tmpcdr.setPartitionId(tmpcdr.getReceiveTime().substring(4,6));
				updateDbInfo(tmpcdr);
			}
		}
	
		renameFile(cdrFile_dx,cdrFileFinal_dx);
		renameFile(cdrFile_yy,cdrFileFinal_yy);
		
		
	}
	
	private void renameFile(File sourcefile, File finalfile) {
		if(sourcefile.length()>0){
			sourcefile.renameTo(finalfile);
		}else{
			log.debug("file "+ sourcefile.getName() +"is null,delete!");
			sourcefile.delete();
		}
		
	}

	//获取待处理的荣联话单
	public List<CDR100TransferJDN> getInitRLCdr(){
		
		String partitionId = dateFormat.format(new Date()).substring(4, 6);
		String lastPartitionId = getLastMonth(partitionId);
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("partitionId", partitionId);
		filter.put("lastPartitionId", lastPartitionId);
		return S.get(CDR100TransferJDN.class).query(Condition.build("getinitRecord").filter(filter));
	}
 	
	private String getLastMonth(String partitionId) {
		if(partitionId.equals("01")){
			return "12";
		}else{
			return  String.format("%02d", Integer.parseInt(partitionId)-1);
		}
		
	}

	//更新数据库荣联话单状态
	private void updateDbInfo(CDR100TransferJDN tmpcdr) {
		
		S.get(CDR100TransferJDN.class).update(tmpcdr);
	}

	//根据荣联话单生成ua识别的话单
	private String generateCdrInfo(CDR100TransferJDN rlcdr,String deviceNumber) throws Exception  {
		
		String cdrInfo = null;
		if(rlcdr.getCdrType().equals(YYCDR) && checkCdrSubTypeParam(rlcdr.getCdrSubType())){
			cdrInfo = "2001"+","+
		              rlcdr.getCallId()+","+
		              getUaCallingType(rlcdr.getCdrSubType())+","+
		              "010"+","+
		              deviceNumber+","+ //主叫号码
		              "010"+","+
		              rlcdr.getCalledNbr()+","+ //被叫号码
		              ""+","+
		              rlcdr.getSessionBeginTime().replace("-", "").replace(":", "").replace(" ", "")+","+
		              rlcdr.getDuration()+ ","+              //通话时长
		              ""+","+
		              ""+","+
		              "010"+","+
		              ""+","+
		              "0"+","+//漫游类型
		              ""+","+
		              "0"+","+
		              ""+","+//前转话单类型
		              ""+","+
		              ""+","+
		              ""+","+
		              ""+","+
		              " ";
		              
		}else if (rlcdr.getCdrType().equals(DXCDR)){
			cdrInfo = "2002"+","+
		              rlcdr.getCallId()+","+
		              getUaCallingType(rlcdr.getCdrSubType())+","+
		              deviceNumber+","+
		              rlcdr.getCalledNbr()+","+
		              rlcdr.getSessionBeginTime().replace("-", "").replace(":", "").replace(" ", "")+","+
		              ""+","+
		              ""+","+
		              ""+","+
		              "0"+","+
		              "1"+","+
		              "110"+","+  //网关代码
			          "110"+","+
		              "110"+","+
		              ""+","+
		              ""+","+
		              ""+","+
		              ""+","+
		              " ";
		}else if (rlcdr.getCdrType().equals(YYCDR) && checkCdrSubType1004(rlcdr.getCdrSubType())) {//双向回呼正向
			cdrInfo = "2001"+","+
		              rlcdr.getCallId()+","+
		              getUaCallingType(rlcdr.getCdrSubType())+","+
		              "010"+","+
		              deviceNumber+","+ //主叫号码,配送员号码
		              "010"+","+
		              rlcdr.getCalledNbr()+","+ //被叫号码
		              ""+","+
		              rlcdr.getCalledAnswerTime().replace("-", "").replace(":", "").replace(" ", "")+","+//被叫应答时间
		              rlcdr.getDuration()+ ","+              //通话时长
		              ""+","+
		              ""+","+
		              "010"+","+
		              ""+","+
		              "0"+","+//漫游类型
		              ""+","+
		              "0"+","+
		              ""+","+//前转话单类型
		              ""+","+
		              ""+","+
		              ""+","+
		              ""+","+
		              " ";
		}else if (rlcdr.getCdrType().equals(YYCDR) && checkCdrSubType1005(rlcdr.getCdrSubType())) {//双向回呼反向
			cdrInfo = "2001"+","+
		              rlcdr.getCallId()+","+
		              getUaCallingType(rlcdr.getCdrSubType())+","+
		              "010"+","+
		              rlcdr.getCallingNbr()+","+ //主叫号码,配送员号码
		              "010"+","+
		              deviceNumber+","+ //被叫号码
		              ""+","+
		              rlcdr.getCalledAnswerTime().replace("-", "").replace(":", "").replace(" ", "")+","+//被叫应答时间
		              rlcdr.getDuration()+ ","+              //通话时长
		              ""+","+
		              ""+","+
		              "010"+","+
		              ""+","+
		              "0"+","+//漫游类型
		              ""+","+
		              "0"+","+
		              ""+","+//前转话单类型
		              ""+","+
		              ""+","+
		              ""+","+
		              ""+","+
		              " ";
		}else{
			throw new Exception ("cdrtype error:"+rlcdr.getCdrType());
		}
		
		return cdrInfo;
	}

	private String getBossCallingNbr(CDR100TransferJDN cdr) {
		String sessionTime="";
		String callNbr="";
		if (cdr.getCdrSubType().equals("1004") || cdr.getCdrSubType().equals("1005")) {
			sessionTime=cdr.getCalledAnswerTime();
			if (cdr.getCdrSubType().equals("1004")) {
				callNbr=cdr.getCallingNbr();
			}
			if (cdr.getCdrSubType().equals("1005")) {
				callNbr=cdr.getCalledNbr();
			}
		}else {
			sessionTime=cdr.getSessionBeginTime();
			callNbr=cdr.getCallingNbr();
		}
		LifeServiceAttrForMemcache jdnMemcached = S.get(LifeServiceAttrForMemcache.class).get("JDNATTR001"+callNbr);
		sessionTime=sessionTime.replace("-", "").replace(":", "").replace(" ", "");
		log.debug("key is ==>"+("JDNATTR001"+callNbr));
		String retNumber =null;
		log.debug("计费号码是==="+callNbr+",时间是==="+sessionTime);
		if(jdnMemcached == null || jdnMemcached.getSynchronizeInfoList() ==null){
			return null;
		}
		
		for(SynchronizeInfo tmpinfo :jdnMemcached.getSynchronizeInfoList()){
			if(tmpinfo.getEff_date().compareTo(sessionTime) <=0 
					&& tmpinfo.getExp_date().compareTo(sessionTime) >=0){
				retNumber = tmpinfo.getDevice_number();
			}
		}
		
		return retNumber;
	}

	private String getUaCallingType(String cdrSubType) throws Exception  {
		
		//话单子类:1001语音短信通知、1002国内落地直拨、1003国内落地回拨、3001文本短信通知
		String bossCdrType = "";
		if(cdrSubType.equals("1001")){
			bossCdrType = "41"; 
		}else if (cdrSubType.equals("1002")){
			bossCdrType = "42"; 
		}else if (cdrSubType.equals("1003")){
			bossCdrType = "43"; 
		}else if (cdrSubType.equals("3001")){
			bossCdrType = "46"; 
		}else if (cdrSubType.equals("1004")){
			bossCdrType = "44"; 
		}else if (cdrSubType.equals("1005")){
			bossCdrType = "45"; 
		}else{
			throw new  Exception ("cdrtype error ");
		}
		return bossCdrType;
	}

	private void appendfile(String strLine, File sourceFile) {
		 FileWriter writer = null;  
	        try {     
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
	        	log.debug("话单写入目录："+sourceFile.getPath());
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

	//获取话单文件名
	private String getFileName(String timenow,String cdrType) {
		//格式 JIDO0100000YY00201607010000000000333500.dat
		return "JIDO"+
				"010"+
		        "0000"+
				cdrType+
				"00"+
				timenow+
				String.format("%010d", Integer.parseInt(timenow.substring(8,14)))
				+".dat";			
				
		
	}
	
	private boolean checkCdrSubTypeParam(String cdrSubType) {
		if (cdrSubType.equals("1001") || cdrSubType.equals("1002") || cdrSubType.equals("1003")
				|| cdrSubType.equals("3001")) {
			return true;
		}
		return false;
	}
	
	private boolean checkCdrSubType1004(String cdrSubType) {// 双向回呼，正向
		if (cdrSubType.equals("1004")) {
			return true;
		}
		return false;
	}

	private boolean checkCdrSubType1005(String cdrSubType) {// 双向回呼，反向
		if (cdrSubType.equals("1005")) {
			return true;
		}
		return false;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	
	
}
