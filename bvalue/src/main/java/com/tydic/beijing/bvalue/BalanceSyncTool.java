package com.tydic.beijing.bvalue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
/*
 * redis去掉后，就不需要同步。
 */
public class BalanceSyncTool {

	private static Logger log = Logger.getLogger(BalanceSyncTool.class);
	private DbTool db = new DbTool();
	
	public void doSync(){
		String fileName="zhengs.properties";
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String jdpin = br.readLine();
			while( jdpin != null){
				log.debug("\n同步余额 ，账号："+jdpin);
				String userId=Common.md5(jdpin);
				List<InfoPayBalance> balance=db.getValidInfoPayBalanceByUserId(userId);	
				if(balance == null){
					log.debug("账号:"+jdpin+",不存在账本");
					jdpin = br.readLine();
					continue;
				}
				for(InfoPayBalance iter : balance){
					
					InfoPayBalanceSync sync = new InfoPayBalanceSync();
					int ret = sync.sync(iter);
					if(ret == 0){
						log.debug("\n成功同步账本："+iter.getBalance_id());
					}else{
						log.debug(" \n!!!!同步账本失败:"+iter.getBalance_id());
					}
				}
				
				jdpin = br.readLine();
			}
			
			br.close();
			fr.close();
			
			log.debug("同步完毕");
		} catch (FileNotFoundException e) {
			log.debug(e.getMessage());
			return ;
		} catch(IOException e){
			log.debug(e.getMessage());
			return ;
		} 
		
		
		
		
		
	}
	
	public static void main(String args[]){
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("uda.xml");
		
		new BalanceSyncTool().doSync();
		
		
	}
}
