package com.tydic.beijing.billing.interfacex.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.billing.dao.CDRCalling;
import com.tydic.beijing.billing.dao.GroupMemberRel;
import com.tydic.beijing.billing.util.SftpClient;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class ExpertForAutoHome {
	
	private String fileDir ;
	private String fileDirBak;
	private String modValue;
	
	private String serverAddress;
	private int serverPort;
	private String user;
	private String pwd;
	private String remoteDir;
 
	
	public String getServerAddress() {
		return serverAddress;
	}


	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}


	public int getServerPort() {
		return serverPort;
	}


	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}


	public String getRemoteDir() {
		return remoteDir;
	}


	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPwd() {
		return pwd;
	}


	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	public String getModValue() {
		return modValue;
	}


	public void setModValue(String modValue) {
		this.modValue = modValue;
	}


	public String getFileDirBak() {
		return fileDirBak;
	}


	public void setFileDirBak(String fileDirBak) {
		this.fileDirBak = fileDirBak;
	}


	public String getFileDir() {
		return fileDir;
	}


	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}
	
	private final static Logger log = Logger.getLogger(ExpertForAutoHome.class);
	
	@Transactional(rollbackFor=Exception.class)
	public void expert(String startTime,String endTime){
		
		
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, -1);
//			
//			String timeString = sdf.format(cal.getTime());
			log.debug("获得导出时间段"+startTime+"到"+endTime);
			
			Date tmpstarttime = sdf.parse(startTime);
			Date tmpendtime = sdf.parse(endTime);
			
			if(modValue==null || modValue.length()==0){
				modValue="1000000";
			}
			
			//获取参数天
//			String startTime = expertTime+"000000";
//			String endTime =  expertTime+"235959";
			
			long time1 = System.currentTimeMillis();
			
			//String fileDir ="";
			String currTime = sdf.format(new Date());
			String fileName_pre = "CRHM"+currTime;
			int fileName_suf =1;
			
			//List<InfoUser> listInfoUser = new ArrayList<InfoUser>();
			List<GroupMemberRel> listofAutoHomeUser = new ArrayList<GroupMemberRel>();
			long cnt =0l; 
			
		   // listInfoUser = S.get(InfoUser.class).query(Condition.build("queryForAutoHome"));  
			listofAutoHomeUser = S.get(GroupMemberRel.class).query(Condition.build("queryForAutoHome")); 
		    log.debug("获得用户数："+listofAutoHomeUser.size());
		    List<String> listFile_p = new ArrayList<String>();
		    List<String> listFile = new ArrayList<String>();
			
			for(GroupMemberRel tmpinfouser:listofAutoHomeUser){

				Map<String, Object> filter = new HashMap<String, Object>();
				filter.put("startTime", startTime);
				filter.put("endTime", endTime);
				filter.put("table", "100");
				filter.put("userId",tmpinfouser.getUser_id());
				filter.put("month", getMonth(startTime.substring(4,6)));
				List<CDRCalling> listCdr = S.get(CDRCalling.class).query(Condition.build("queryCdrForAutoHome").filter(filter)); 
				
				for(CDRCalling tmpcdr:listCdr){
					
					if(tmpcdr.getCall_type()==null || !tmpcdr.getCall_type().equals("2")){
						continue;//只提呼叫前转话单
					}
					
					//写入文件
				    cnt++;
					if(cnt % Long.parseLong(modValue) ==0){
						fileName_suf ++;
					}
					String suf =  getfullsuf(fileName_suf,3);		
				    String fullFileName = fileDirBak  + fileName_pre+"_"+ suf +".txt";
				    String fullFileName_p = fullFileName+"_p";
				    File outfile = new File(fullFileName);
				    File outfile_p = new File(fullFileName_p);
				    
				    if(!listFile.contains(fullFileName)){
				    	listFile.add(fullFileName);
				    }
				    if(!listFile_p.contains(fullFileName_p)){
				    	listFile_p.add(fullFileName_p);
				    }
				    
				    if(!outfile.exists()){
				       outfile.createNewFile();	
				    }
				    if(!outfile_p.exists()){
				    	outfile_p.createNewFile();
				    }
				    
//				    0-主叫呼出话单
//				    1-被叫呼入话单
//				    2-呼叫前转话单
//				    3-呼转拆分的话单
				    String callType = tmpcdr.getCall_type();
				    String chargedParty ="";
				    String oppNumber ="";
				    if(callType.equals("0") || callType.equals("2")){
				    	chargedParty = tmpcdr.getCallingparty();
				    	oppNumber = tmpcdr.getCalledparty();
				    }else{
				    	oppNumber = tmpcdr.getCallingparty();
				    	chargedParty = tmpcdr.getCalledparty();
				    }
				    
				    double fee = getfee(tmpcdr.getTariffinfo());
	                String sessionbegintime = sdf.format(tmpcdr.getStartTime());
				    
				    String appendTxt = chargedParty +"|"+sessionbegintime +"|" +
				              tmpcdr.getRoamingType()+"|" +callType +"|" + tmpcdr.getCallduration() +"|" 
				              +oppNumber +"|"+fee +"|"+tmpcdr.getFowardingnumber();
				    appendfile(appendTxt,outfile);
				   
				    
				}
				
				 
			}
			
            for(String filep:listFile_p){
            	File tmpfilep = new File(filep);
            	if(tmpfilep.exists()){
            		tmpfilep.delete();
            	}
            }
            
            ///生成好的文件copy到autohome目录下
//            for(String oldfile:listFile){
//            	String filename = new File(oldfile).getName();
//            	String newfile = fileDir+ filename;
//
//            		copyFile(oldfile,newfile);
// 
//            }
            
            
    		
    		SftpClient sftpClient = new SftpClient();
    		sftpClient.setHost(this.serverAddress);
    		sftpClient.setPort(this.serverPort);
    		sftpClient.setUsername(this.user);
    		sftpClient.setPassword(this.pwd);
    		
    		try {
				sftpClient.connect();
				 for(String oldfile:listFile){
					 sftpClient.upload(this.remoteDir, new File(oldfile));
				 }
				 sftpClient.disConnect();
			} catch (Exception e) {
				
				log.error("上传sftp异常"+e.getMessage()+"本次上传文件列表:"+listFile.toString());
				sftpClient.disConnect();
			}
    		
            
            
			long time2 = System.currentTimeMillis();
			log.debug("提取汽车之家话单文件结束，耗时"+(time2-time1)+"ms,共导出话单量"+cnt+"行,用户数"+listofAutoHomeUser.size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("导出汽车之家话单异常");
		}
		
		
		
		
		
	} 
	
	
private Object getMonth(String substring) {
		int month = Integer.parseInt(substring) -1 ;
		
		if(month==0){
			month = 12;
		}
		return getfullsuf(month,2);
	}


private static double getfee(String tariffinfo) {
		
		String[] infos = tariffinfo.split(";"); 
		int sumvalue =0;
	    for(int i=0;i<infos.length;i++){
	    	String info = infos[i];
	    	String[] kv =info.split(":");
	    	String value = kv[1];
	    	String key = kv[0];
	    	if( !key.equals("10") && !key.equals("11") &&!key.equals("12")  ){
	    		sumvalue = sumvalue + Integer.parseInt(value);
	    	}
	    	
	    }
	    
	    log.debug("获得话单使用金钱"+sumvalue);
	    
		return 1.0*sumvalue/100;
	}
	private static String getfullsuf(int fileName_suf,int length) {

        String orgsuf = "000000000"+fileName_suf;
		String fullsuf = orgsuf.substring(orgsuf.length()-length,orgsuf.length());
		return fullsuf;
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
	
	  public void copyFile(String oldPath, String newPath) {     
		    try {     
		      int bytesum = 0;     
		      int byteread = 0;     
		      File oldfile = new File(oldPath);     
		      if (oldfile.exists()) { //文件存在时     
		        InputStream inStream = new FileInputStream(oldPath); //读入原文件     
		        FileOutputStream fs = new FileOutputStream(newPath);     
		        byte[] buffer = new byte[1000];     
		        while ( (byteread = inStream.read(buffer)) != -1) {     
		          bytesum += byteread; //字节数 文件大小     
		          log.debug(bytesum);     
		          fs.write(buffer, 0, byteread);     
		        }     
		        inStream.close();   
		        fs.close();
		      }     
		    }     
		    catch (Exception e) {     
		      log.debug("复制单个文件操作出错");
		      e.printStackTrace();     
		    
		    } 
		    
		  } 

}
