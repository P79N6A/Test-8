package com.tydic.beijing.bvalue.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.NIORandomAccessFile;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class BatchAdjustBValueThread extends Thread {
	
	private static Logger log=Logger.getLogger(BatchAdjustBValueThread.class);
	
	private String filePrefix;
	private String fileDir;
	private String threadId;
@Autowired
    private BatchAdjustBValueProcess batchAdjustBValueProcess;





	public String getThreadId() {
		return threadId;
	}






	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}






	/**
	 * @param filePrefix  文件前缀
	 * @param fileDir 文件路径
	 * @param threadId 线程ID
	 */
	//@Transactional(rollbackFor=Exception.class)
	public void run() {
 
         try {
        	
        	 File file = new File(fileDir);
             if (!file.isDirectory()) {
                      log.error("ERROR FILEPATH!!!!!");
                      return ;
             } else if (file.isDirectory()) {
                     String[] filelist = file.list();
                     for (int i = 0; i < filelist.length; i++) {
                    	 
                             File readfile = new File(fileDir + "//" + filelist[i]);
                             if (!readfile.isDirectory()) {
                            	 log.debug("开始判断文件==>"+filelist[i].toString());
                            	   if(!filelist[i].substring(0, filePrefix.length()).equals(filePrefix)  
                            			   || filelist[i].substring(filelist[i].length()-2).equals("_p")
                            			   ||filelist[i].substring(filelist[i].length()-2).equals("_s")
                            			   ||filelist[i].substring(filelist[i].length()-2).equals("_e")
                            			   ||filelist[i].substring(filelist[i].length()-2).equals("_m")
                            			   ){
                            		   continue;
                            	   }
                            	 File pFile = new File(fileDir + "//" + filelist[i]+"_p");
                            	 if(pFile.exists()){
                            		 //已经处理过了或正在处理
                            		 continue;
                            	 }
                            	   
                            	 dealFile(readfile);
                                     

                             } else if (readfile.isDirectory()) {
                                     continue;
                             }
                     }
                     
                
//            	 String suffix = threadId;
//     			
//     			//判断文件是否存在
//     			String fullFilePath = fileDir + filePrefix + "_" + suffix;
//     			log.debug("fulFilePath==>"+fullFilePath);
//     			File sourceFile  = new File(fullFilePath);
//     			if( !sourceFile.exists()){
//     				log.debug("file["+fullFilePath+"] is not exist!");
//     				return ;
//     			}
//     			dealFile(sourceFile);
//     			
     			

             }
        	 
       
		} catch (Exception e) {
			e.printStackTrace();
		
		} 
	} 
	
	
 



	private void dealFile(File sourceFile) {
		long startTime = System.currentTimeMillis();
		NIORandomAccessFile nioAccessFile ;
		 List<String> jdpinList = new ArrayList<String>();
//		String thismonth = sourceFile.getName().substring(12, 18);
//		log.debug("get month of file ==>"+thismonth);
	 
    try{
		 
		//每个源文件都有三个相关文件：处理进度进度_p,异常详情_e ,异常原始信息_s
		File sourceFile_e = new File(sourceFile.getPath()+"_e");
		File sourceFile_p = new File(sourceFile.getPath()+"_p");
		File sourceFile_s = new File(sourceFile.getPath()+"_s");
		File sourceFile_m = new File(sourceFile.getPath()+"_m");
		
		Path path = FileSystems.getDefault().getPath(sourceFile.getPath());
		nioAccessFile = new NIORandomAccessFile(path,"r");
		nioAccessFile.seek(0);

		long startposition =0; //byte position 
		if(sourceFile_p.exists()){ //如果有处理进度了，获取上次的处理进度
			String tmpReadLine = readFileByLines(sourceFile_p);
			if(tmpReadLine != null){
				startposition = Integer.parseInt(tmpReadLine);
			}
			return ;// 如果p已经存在，直接返回
		} else{
			sourceFile_p.createNewFile();
		}
	 
//		sourceFileReader = new BufferedReader(new FileReader(fullFilePath));
		String strLine ="";
		//while( (strLine = nioAccessFile.readLine()).length() >0){
		long sumtime =0L;
		int num =0;
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
				
				if(strLine ==null || strLine.length()==0){
					break;
				}
				
				num++;
				if(num <= startposition){
					continue;
				}
				
				if(strLine.length() ==0){
					continue;
				}
				
				String[] infos = strLine.split("\t");
				if(jdpinList.contains(infos[0])){
					appendfile(strLine+"\r\n",sourceFile_m);
				}else{
					generateBvalue(strLine,sourceFile_e,sourceFile_s);
					writefileP(num,sourceFile_p);
					jdpinList.add(infos[0]);					
				}

		}
		nioAccessFile.close();
		long endTimeb = System.currentTimeMillis() - startTime;
		jdpinList.clear();
		log.debug("thread"+"，总耗时"+ endTimeb/1000/60 +"分钟" +(endTimeb /1000)%60 +"秒" );
		
    } catch (Exception e){
    	log.debug("文件处理中异常");
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



	private void generateBvalue(String adjustMsg,File sourcefile_e,File sourcefile_s) throws Exception  {
		try {
		   
			long starttime = System.currentTimeMillis();
			adjustBvalue(adjustMsg);
            long endtime = System.currentTimeMillis();
            
            log.debug("单条记录耗时["+adjustMsg+"]==="+(endtime-starttime));
			
		} catch (Exception e) {
			e.printStackTrace();
			appendfile(adjustMsg,sourcefile_s);
			appendfile(e.toString(),sourcefile_e);
		}
		
	}
	
	
	 private void adjustBvalue (String adjustMsg) throws Exception {
		 
	    	//先判断是否已经调整过
			String[] infos = adjustMsg.split("\t");
			log.debug("["+adjustMsg+"]拆分数量="+infos.length);
			if(infos.length != 4){
				throw new Exception ("数据异常["+adjustMsg+"]");
			}
			
			String userId = Common.md5(infos[0]);
			if(!isExists(userId)){
				throw new Exception("用户["+infos[0]+"]不存在"); 
			}
			
			JSONObject inputJson = new JSONObject();
			inputJson.put("JDPin", infos[0]);
			inputJson.put("MobileNumber", "");
			inputJson.put("AdjustMode", infos[1]);
			inputJson.put("BValue", infos[2]);
			inputJson.put("AdjustType", infos[3]);
			inputJson.put("AdjustReason", "批量B值调整");
//			AdjustBValueImpl adjustBValueImpl = new AdjustBValueImpl();
//			adjustBValueImpl.setDb(new DbTool());
			batchAdjustBValueProcess.perform(inputJson);
			
		
			
	 }

	


	private boolean isExists(String userId) {
		
		List<InfoUser> listInfoUser= S.get(InfoUser.class).query(Condition.build("queryInfoUserByUserId").filter("userId", userId));
		log.debug("根据userid=["+userId+"]找到用户数量"+listInfoUser.size());
		if(listInfoUser.size()>0){
			return true;
		}
		return false;
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


 
 
 
 
}
