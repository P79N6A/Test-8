package com.tydic.beijing.bvalue.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.beijing.bvalue.dao.LogTradeCreateUserHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.RegistBQueryUserDao;
import com.tydic.beijing.bvalue.dao.RuleParameters;
import com.tydic.beijing.bvalue.service.BUserRegist;

public class BUserRegistImpl implements BUserRegist{

	private static Logger log=Logger.getLogger(BUserRegistImpl.class);
	
	private DbTool db;
	private   List<Integer> servers;
	private int amount;	
	private boolean runflag=true;
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setDb(DbTool db) {
		this.db = db;
	}
	public   void setServers(Vector<Integer> servers) {
		this.servers = servers;
	}
	
	public void init(){		//加载配置
		List<RuleParameters> count=db.checkServerRule();
		if(count != null && count.size()>0){
			log.error("配置错误 !!");
			runflag=false;
		}
		List<RuleParameters> server=db.getServers("1000", "501", "server");
		if(server !=null){
			for(RuleParameters iter : server){
				log.debug(">>>>>>>:"+iter.getPara_char2());
				if(!Common.isNumber(iter.getPara_char2())){
					log.debug("配置错误");
					runflag=false;
				}
				servers.add(Integer.parseInt(iter.getPara_char2()));
			}
		}else{
			log.error("配置错误!");
			runflag=false;
		}
	}
	@Override
	public void registUser() {
		
		while(runflag){
			
			int server;
			synchronized (this) {
				if(servers.size()>0){
					server=servers.get(0);
					servers.remove(0);
				}else{
					server=0;
				}
			}
			
			List<RegistBQueryUserDao>  logTrade= db.LogTradeRegist(server,amount);
			List<InfoUser> infoUsers=new ArrayList<InfoUser>();
			List<LogTradeHis> tradeHis=new ArrayList<LogTradeHis>();
			List<LogTradeCreateUserHis>  createUsers=new ArrayList<LogTradeCreateUserHis>();
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
			
			if(logTrade !=null && !logTrade.isEmpty()){
				for(RegistBQueryUserDao  iter : logTrade){
					InfoUser user=new InfoUser();
					user.setCreate_channel(iter.getChannel_type());
					user.setUser_id(iter.getUser_id());
					user.setJd_pin(iter.getJd_pin());
					user.setCreate_date(sdf.format(Calendar.getInstance().getTime()));
					infoUsers.add(user);
					LogTradeHis trade=new LogTradeHis();
					trade.setTrade_id(iter.getTrade_id());
					trade.setTrade_type_code(iter.getTrade_type_code());
					trade.setExternal_system_code(iter.getExternal_system_code());
					trade.setUser_id(iter.getUser_id());
					trade.setPartition_id(iter.getPartition_id());
					trade.setChannel_type(iter.getChannel_type());
					trade.setProcess_tag(2);
					trade.setTrade_time(iter.getTrade_time());
					trade.setProcess_time(iter.getProcess_time());
					tradeHis.add(trade);
					LogTradeCreateUserHis createHis=new LogTradeCreateUserHis();
					createHis.setTrade_id(iter.getTrade_id());
					createHis.setJd_pin(iter.getJd_pin());
					createHis.setUser_id(iter.getUser_id());
					createHis.setPartition_id(iter.getPartition_id());
					createHis.setProcess_tag(2);
					createUsers.add(createHis);
				
				}
				
				
				
				
				
				try{
					db.registAll(infoUsers,tradeHis,createUsers);	//插入   删除  
					servers.add(server);
				}catch(Exception e){
					log.error(">>>>>>>>>>>开户异常 !!!");
					servers.add(server);
					if(amount==1)
						db.registErr(tradeHis,createUsers,"开户异常");	
					//删除log_trade,log_trade_create_user ,记log_trade_his,log_trade_create_user_his
					
					throw new RuntimeException("服务异常!!!!!");
				}
			
			}else{
				
				//判断启停标志
				RuleParameters rule=db.getRunFlag("1000","501","runflag");
				if(rule.getPara_char1().equals("0")){
					runflag=false;
					break;
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
				
		
		
		
	}

	
	
}
