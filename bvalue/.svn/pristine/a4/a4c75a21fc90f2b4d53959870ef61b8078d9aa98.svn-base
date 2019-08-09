package com.tydic.beijing.bvalue.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.Constants;
import com.tydic.beijing.bvalue.common.NIORandomAccessFile;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.mem.InfoPayBalanceMem;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class SyncRedisByFileOfJDPin {
	
	private static Logger log=Logger.getLogger(SyncRedisByFileOfJDPin.class);
	
 
	private String commitRecord;
	private String filePrefix;
	private String fileDir;
	private String threadId;
	/**
	 * 
	 * 201706去除redis
	 */	
//	@Autowired
//	private InfoPayBalanceSync sync ;

	/**
	 * @param filePrefix  文件前缀
	 * @param fileDir 文件路径
	 * @param threadId 线程ID
	 */
	//@Transactional(rollbackFor=Exception.class)
	public void run() { //String filePrefix,String fileDir ,int threadId

	//	int threadId=1;
	//	BufferedReader sourceFileReader = null;
		long startTime = System.currentTimeMillis();
		
//		long startposition =1;
		NIORandomAccessFile nioAccessFile  ;
		
         try {
			//文本后缀
			String suffix = threadId;
			
			//判断文件是否存在
			String fullFilePath = fileDir + filePrefix + "_" + suffix;
			log.debug("fulFilePath==>"+fullFilePath);
			File sourceFile  = new File(fullFilePath);
			if( !sourceFile.exists()){
				log.debug("file["+fullFilePath+"] is not exist!");
				return ;
			}
			
			//每个源文件都有三个相关文件：处理进度进度_p,异常详情_e ,异常原始信息_s
			File sourceFile_e = new File(fullFilePath+"_e");
			File sourceFile_p = new File(fullFilePath+"_p");
			File sourceFile_s = new File(fullFilePath+"_s");

			long startposition =0; //byte position 
			if(sourceFile_p.exists()){ //如果有处理进度了，获取上次的处理进度
				String tmpReadLine = readFileByLines(sourceFile_p);
				if(tmpReadLine != null){
					startposition = Integer.parseInt(tmpReadLine);
				}
			} else{
				sourceFile_p.createNewFile();
			}
			 
			Path path = FileSystems.getDefault().getPath(fullFilePath);
			nioAccessFile = new NIORandomAccessFile(path,"r");
			nioAccessFile.seek(0);
			
//			sourceFileReader = new BufferedReader(new FileReader(fullFilePath));
			String strLine ="";
			//while( (strLine = nioAccessFile.readLine()).length() >0){
			long sumtime =0L;
			int num =0;
			List<String> listUserId = new ArrayList<String>();

			while( true){

					try {
						strLine = nioAccessFile.readLine();
					} catch (Exception e) {
						num ++;
						appendfile( num +"",sourceFile_s);
						appendfile(e.getMessage(),sourceFile_e);
						continue;
					}
					log.debug("从文件获得strLine==>["+strLine+"]");
					
				   
					if(strLine == null ){
						syncRedis(listUserId,sourceFile_e,sourceFile_s,suffix);
						listUserId.clear();
						num++;
						writefileP(num,sourceFile_p);
						break;
					}
					num++;
					if(num <= startposition){
						continue;
					}
					
					if(strLine.length() ==0){
						continue;
					}
					 
					
					
					listUserId.add(strLine);
					if(num % Integer.parseInt(commitRecord) ==0){
						long starttime =System.currentTimeMillis();
						syncRedis(listUserId,sourceFile_e,sourceFile_s,suffix);
						long endtime =System.currentTimeMillis();
						log.debug("一次提交耗时"+(endtime-starttime));
						listUserId.clear();
						if(listUserId.size()>0){
							log.debug("list里还有数据"+listUserId.size());
						}
						writefileP(num,sourceFile_p);
					}


			}
		
			long endTimeb = System.currentTimeMillis() - startTime;
			log.debug("thread"+threadId+"，总耗时"+ endTimeb/1000/60 +"分钟" +(endTimeb /1000)%60 +"秒,平均每"+commitRecord+"条耗时"+ ((endTimeb*1.0)/num)*Integer.parseInt(commitRecord)+"ms");
			
		} catch (Exception e) {
			e.printStackTrace();
		
		} 
	} 
	
	
 



	//进度写入p文件
	 private void writefileP(long startposition, File sourceFile_p) {
		
		 FileWriter writer = null;  
	        try {     
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
	            writer = new FileWriter(sourceFile_p.getPath(), false);     
	            writer.write(startposition+"\r\n");       
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



	private void syncRedis(List<String> listUserId,File sourcefile_e,File sourcefile_s,String suffix) {
		try {
			
			for(String tmpjdpin :listUserId){
				
				String userId = Common.md5(tmpjdpin);
//				log.debug("同步redis的user_id是："+userId);
				//List<InfoPayBalance> balanceList=db.getValidInfoPayBalanceByUserId(userId);	
				String currentTime=new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
				List<InfoPayBalance> balanceList =  S.get(InfoPayBalance.class).query(Condition.build("getValidInfoPayBalanceByUserId").filter("user_id", userId).filter("currentTime", currentTime));
				 
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
				String currTime = sdf.format(new Date());
				JSONArray balanceDtoList = new JSONArray();

					SimpleDateFormat sdfout=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
				HashMap<String, InfoPayBalance> infoMap = new HashMap<String, InfoPayBalance>();
				for(InfoPayBalance infoPayBalance : balanceList){
					
					if ((infoPayBalance.getBalance_type_id() == Constants.Balance_type_normal || 
							infoPayBalance.getBalance_type_id() == Constants.Balance_type_activity) &&
							infoPayBalance.getExp_date().compareTo(currTime)>0
							){
						JSONObject tmpJson = new JSONObject();
						tmpJson.put("Balance", infoPayBalance.getBalance());
						String outExpDate = sdfout.format(sdf.parse(infoPayBalance.getExp_date()));
						tmpJson.put("ExpDate", outExpDate);
						balanceDtoList.add(tmpJson);
						
						infoMap.put(infoPayBalance.getBalance_id(), infoPayBalance);
					}
				}
				
			 
				InfoPayBalanceMem  mem = new InfoPayBalanceMem();
				mem.setUser_id(userId);
				mem.setInfoMap(infoMap);
//				201706去除redis
//				log.debug("同步redis的mem是："+mem.toString());
//				sync.syncByObjNew(mem);
			}
			 
			
			
		} catch (Exception e) {
			e.printStackTrace();
			String outline = "";
			for(String tmpstr:listUserId){
				outline = outline + tmpstr +"\r\n";
			}
			appendfile(outline,sourcefile_s);
			appendfile(e.toString(),sourcefile_e);
		}
		
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
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public String readFileByLines(File file) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = reader.readLine();
            reader.close();
            
            return tempString;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


 


	public String getFilePrefix() {
		return filePrefix;
	}



	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}



	public String getFileDir() {
		return fileDir;
	}



	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}





 


	public String getCommitRecord() {
		return commitRecord;
	}






	public void setCommitRecord(String commitRecord) {
		this.commitRecord = commitRecord;
	}






	public String getThreadId() {
		return threadId;
	}






	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

 
 
}
