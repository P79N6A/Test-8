package com.tydic.beijing.bvalue.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.tydic.beijing.bvalue.dao.OriginalShoppingFile;
import com.tydic.uda.service.S;

public class ImportShoppingFileProcess {
	
	private static Logger log=Logger.getLogger(ImportShoppingFileProcess.class);
	
	@Transactional(rollbackFor=Exception.class)
	public void importFile(List<OriginalShoppingFile> listOriginalShoppingFile,File file) throws Exception {

		for(OriginalShoppingFile tmp:listOriginalShoppingFile){
			try {
				S.get(OriginalShoppingFile.class).create(tmp);
			} catch (Exception e) {
				e.printStackTrace();
				appendfilee(tmp.getOriginalStr(),file);
			}
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

}
