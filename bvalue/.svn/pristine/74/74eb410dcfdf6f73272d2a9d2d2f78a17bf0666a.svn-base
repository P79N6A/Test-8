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

public class ShoppingRejectThread extends Thread {
	
	private static Logger log=Logger.getLogger(ShoppingRejectThread.class);
	
	@Autowired
	private ShoppingRejectProcess shoppingRejectProcess ;
	
	private String commitRecord;
	private String filePrefix;
	private String fileDir;
	private String threadId;
	

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
//                     String[] filelist = file.list();
//                     for (int i = 0; i < filelist.length; i++) {
//                             File readfile = new File(fileDir + "//" + filelist[i]);
//                             if (!readfile.isDirectory()) {
//                            	 log.debug("开始判断文件==>"+filelist[i].toString());
//                            	   if(!filelist[i].substring(0, filePrefix.length()).equals(filePrefix)  
//                            			   || filelist[i].substring(filelist[i].length()-2).equals("_p")
//                            			   ||filelist[i].substring(filelist[i].length()-2).equals("_s")
//                            			   ||filelist[i].substring(filelist[i].length()-2).equals("_e")
//                            			   ){
//                            		   continue;
//                            	   }
//                            	 File pFile = new File(fileDir + "//" + filelist[i]+"_p");
//                            	 if(pFile.exists()){
//                            		 //已经处理过了或正在处理
//                            		 continue;
//                            	 }
//                            	   
//                            	 dealFile(readfile);
//                                     
//
//                             } else if (readfile.isDirectory()) {
//                                     continue;
//                             }
//                     }
                     
                
            	 String suffix = threadId;
     			
     			//判断文件是否存在
     			String fullFilePath = fileDir + filePrefix + "_" + suffix;
     			log.debug("fulFilePath==>"+fullFilePath);
     			File sourceFile  = new File(fullFilePath);
     			if( !sourceFile.exists()){
     				log.debug("file["+fullFilePath+"] is not exist!");
     				return ;
     			}
     			dealFile(sourceFile);
//     			
     			

             }
        	 
       
		} catch (Exception e) {
			e.printStackTrace();
		
		} 
	} 
	
	
 



	private void dealFile(File sourceFile) {
		long startTime = System.currentTimeMillis();
		NIORandomAccessFile nioAccessFile ;
//		String thismonth = sourceFile.getName().substring(12, 18);
//		log.debug("get month of file ==>"+thismonth);
	 
    try{
		 
		//每个源文件都有三个相关文件：处理进度进度_p,异常详情_e ,异常原始信息_s
		File sourceFile_e = new File(sourceFile.getPath()+"_e");
		File sourceFile_p = new File(sourceFile.getPath()+"_p");
		File sourceFile_s = new File(sourceFile.getPath()+"_s");
		
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
					generateBvalue(listUserId,sourceFile_e,sourceFile_s);
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
					generateBvalue(listUserId,sourceFile_e,sourceFile_s);
					listUserId.clear();
					writefileP(num,sourceFile_p);
				}


		}
		nioAccessFile.close();
		long endTimeb = System.currentTimeMillis() - startTime;
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



	private void generateBvalue(List<String> listUserId,File sourcefile_e,File sourcefile_s) {
		try {
			//long starttime =System.currentTimeMillis();
			//log.debug("本次要处理的用户数：["+listUserId.size()+"]");
			shoppingRejectProcess.generateBvalue(listUserId);
			//long endtime =System.currentTimeMillis();
		//	log.debug("一次提交耗时"+(endtime-starttime));
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

 
 
 
}
