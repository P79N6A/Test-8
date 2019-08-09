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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.common.NIORandomAccessFile;

public class BatchCreateUserThread extends Thread {
	
	private static Logger log=Logger.getLogger(BatchCreateUserThread.class);
	
	@Autowired
	private BatchCreateUserProcess batchCreateUserProcess ;
	private String filePrefix;
	private String fileDir;
	private String threadId;
	private int commitNum;


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
		File sourceFile_p = null;
		
		NIORandomAccessFile nioAccessFile  ;
		long startLine =0L;
		
         try {
			//文本后缀
			String suffix = threadId;		
			
			//判断文件是否存在
			String fullFilePath = fileDir + filePrefix + "_" + suffix;
			File sourceFile  = new File(fullFilePath);
			if( !sourceFile.exists()){
				log.debug("file["+fullFilePath+"] is not exist!");
				return ;
			}
			
			//每个源文件都有三个相关文件：处理进度进度_p,异常详情_e ,异常原始信息_s
			File sourceFile_e = new File(fullFilePath+"_e");
			sourceFile_p = new File(fullFilePath+"_p");
			File sourceFile_s = new File(fullFilePath+"_s");
			
			
			if(sourceFile_p.exists()){ //如果有处理进度了，获取上次的处理进度
				String tmpReadLine = readFileByLines(sourceFile_p);
				if(tmpReadLine != null){
					startLine = Long.parseLong(tmpReadLine);
				}
			} else{
				sourceFile_p.createNewFile();
			}
			
			//log.debug("start from position "+startLine);
			
			Path path = FileSystems.getDefault().getPath(fullFilePath);
			nioAccessFile = new NIORandomAccessFile(path,"r");
			nioAccessFile.seek(0);
	
			 
		//	 sourceFileReader = new BufferedReader(new FileReader(fullFilePath));
			 List<String> listJdpin = new ArrayList<String>();
			
			String strLine ="";
			//while( (strLine = nioAccessFile.readLine()).length() >0){
			long num =0L;
			while( true){
				long whilestarttime = System.currentTimeMillis();
			//	strLine = sourceFileReader.readLine();
				strLine = nioAccessFile.readLine();
				num++;
				
				if(num <= startLine ){
					continue;
				}
				
				if(strLine == null )
				{
					createuser(listJdpin,sourceFile_e,sourceFile_s);
					writefileP(num,sourceFile_p);
					break;
				}
				
				if(strLine.length() ==0){
					continue;
				}
				
				


				listJdpin.add(strLine);
				if(listJdpin.size()>commitNum){
				       createuser(listJdpin,sourceFile_e,sourceFile_s);
				       writefileP(num,sourceFile_p);
					   listJdpin.clear();
					   
					   long committime = System.currentTimeMillis();
						log.debug("thread"+threadId+"一次提交耗时===>"+(committime-whilestarttime));
					} 
			}
			
			long endTime = System.currentTimeMillis();
			log.debug("thread"+threadId+"总耗时===>"+(endTime-startTime) /1000/60 +"分钟"+ ((endTime-startTime) /1000)%60 +"秒");
			writefileP(num,sourceFile_p);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("thread"+threadId+"exception"+e.toString());
			//writefileP(startLine,sourceFile_p);
			
		}	
	} 
	
	
	
	private void createuser(List<String> listJdpin,File filee,File files) {
		
		String alljdpin = "";
		for(String tmpstr:listJdpin){
			alljdpin = alljdpin +"\r\n";
		}
		 
		try {
			batchCreateUserProcess.batchCreate(listJdpin);
			int i=0;
		} catch (Exception e) {
			appendfilee(alljdpin,files);
			appendfilee(e.getMessage(),filee);
			e.printStackTrace();
		}
		
	}



	private void appendfilee(String strLine, File sourceFile) {
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



	private boolean formatSuccess(String[] shoppingInfos) {
	    
		if(shoppingInfos.length != 6){
			return false;
		}
		
//		String orderTime = shoppingInfos[5];
//		if(orderTime == null || orderTime.length() != 14){
//			return false;
//		}

		return true;
	}


   //判断当前行是否标题行
	//jdpin|orderno|ordertype|amount|orgorderno|completetime
	private boolean isMenuLine(String strLine) {
        
		if(strLine.substring(0,6).equals("jdpin")  &&
				strLine.substring(strLine.length()-12).equals("completetime")){ //strLine.startsWith("jdpin") && strLine.endsWith("completetime")
			return true;
		}		
		return false;
	}



	//进度写入p文件
	 private void writefileP(long startLine, File sourceFile_p) {
		
		 FileWriter writer = null;  
	        try {     
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
	            writer = new FileWriter(sourceFile_p.getPath(), false);     
	            writer.write(startLine+"\r\n");       
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
 
 

	public int getCommitNum() {
		return commitNum;
	}



	public void setCommitNum(int commitNum) {
		this.commitNum = commitNum;
	}



	public String getThreadId() {
		return threadId;
	}



	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}


 
 

 
}
