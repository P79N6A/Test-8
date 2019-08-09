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
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.NIORandomAccessFile;
import com.tydic.beijing.bvalue.dao.OriginalShoppingFile;

public class ImportShoppingFileThread extends Thread {
	
	private static Logger log=Logger.getLogger(ImportShoppingFileThread.class);
	
	@Autowired
	private ImportShoppingFileProcess importShoppingFileProcess ;
	private String filePrefix;
	private String fileDir;
	private String threadId;
	private int commitNum;

	private String splitChar;


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
			nioAccessFile.seek(startLine);
	
			 
		//	 sourceFileReader = new BufferedReader(new FileReader(fullFilePath));
			 List<OriginalShoppingFile> listOriginalShoppingFile = new ArrayList<OriginalShoppingFile>();
			
			String strLine ="";
			//while( (strLine = nioAccessFile.readLine()).length() >0){
			long num =0L;
			while( true){
				num++;
				long whilestarttime = System.currentTimeMillis();
			//	strLine = sourceFileReader.readLine();
				strLine = nioAccessFile.readLine();
				//long whilestarttime2 = System.currentTimeMillis();
				//log.debug("thread"+threadId+"读取文件-"+num+"耗时"+(whilestarttime2 - whilestarttime));
				//String tmpstr = new String(strLine.getBytes(),"UTF-8");
				
				
				//long whileendtime = System.currentTimeMillis();
				//log.debug("thread"+threadId+"readfile==>"+fullFilePath+"seekto "+startLine+",and get string==>["+strLine+"]");
				
				
				if(strLine == null )
				{
					importShoppingFileProcess.importFile(listOriginalShoppingFile,sourceFile_s);
					listOriginalShoppingFile.clear();
					//writefileP(startLine,sourceFile_p);
					
					break;
				}
				startLine = startLine +strLine.length();
				//判断该行是否标题行
//				if(isMenuLine(strLine) || strLine.length()==0){ //如果是标题行，则继续处理下一行
//					log.debug("menuline");
//					writefileP(startLine,sourceFile_p);
//					continue;
//				}
				//long splittime1 = System.currentTimeMillis();
				String[] shoppingInfos = strLine.split("\t");  //\t
				//long splittime = System.currentTimeMillis();
				//log.debug("thread"+threadId+"split-"+num+"耗时"+(splittime-whilestarttime));
//				log.debug("shoppingInfos.length==>"+shoppingInfos.length);
//				for(int i=0;i<shoppingInfos.length;i++){
//					log.debug("param"+i+" is "+shoppingInfos[i]);
//				}
				
				if(formatSuccess(shoppingInfos)){
					//格式校验成功的话，就开始做入库
					
					long trystarttime = System.currentTimeMillis();

						try {
							long savestarttime = System.currentTimeMillis();
							OriginalShoppingFile originalShoppingFile = new OriginalShoppingFile();
							originalShoppingFile.setAmount(shoppingInfos[3]);
							originalShoppingFile.setComplete_time(shoppingInfos[5]);
							originalShoppingFile.setImport_time(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
							originalShoppingFile.setJdpin(shoppingInfos[0]);
							originalShoppingFile.setOrder_no(shoppingInfos[1]);
							originalShoppingFile.setOrder_type(shoppingInfos[2]);
							originalShoppingFile.setOrg_order_no(shoppingInfos[4]);
							originalShoppingFile.setUser_id(Common.md5(shoppingInfos[0]));
							originalShoppingFile.setOriginalStr(strLine);
							originalShoppingFile.setProcess_tag("0");
							listOriginalShoppingFile.add(originalShoppingFile);
							if(listOriginalShoppingFile.size()>commitNum){
								importShoppingFileProcess.importFile(listOriginalShoppingFile,sourceFile_s);
								listOriginalShoppingFile.clear();
								
								//writefileP(startLine,sourceFile_p);
							} 
							
							long saveendtime = System.currentTimeMillis();
						//	log.debug("thread"+threadId+"生成对象-"+num+"耗时>"+(saveendtime -whilestarttime));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							appendfilee(strLine,sourceFile_s);
							appendfilee("format error "+strLine,sourceFile_e);
						}

				}else{
					//格式校验失败，记录e文件和s文件 line++
					//writefileP(startLine,sourceFile_p);
					appendfilee(strLine,sourceFile_s);
					appendfilee("format error "+strLine,sourceFile_e);
				}
			
				
				long onetime = System.currentTimeMillis();
				log.debug("thread"+threadId+"一次-"+num+"耗时===>"+(onetime-whilestarttime));
			}
			
			long endTime = System.currentTimeMillis();
			log.debug("thread"+threadId+"总耗时===>"+(endTime-startTime) /1000/60 +"分钟"+ ((endTime-startTime) /1000)%60 +"秒");
			writefileP(startLine,sourceFile_p);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("thread"+threadId+"exception"+e.toString());
			writefileP(startLine,sourceFile_p);
			
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
 


	public String getSplitChar() {
		return splitChar;
	}



	public void setSplitChar(String splitChar) {
		this.splitChar = splitChar;
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
