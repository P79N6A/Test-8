package com.tydic.beijing.bvalue.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tydic.beijing.bvalue.biz.DbTool;
import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.common.MyOraSessionFactory;
import com.tydic.beijing.bvalue.common.MySessionFactory;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.dao.BalanceAccessLog;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUser;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LogBSmsGwz;
import com.tydic.beijing.bvalue.dao.LogTradeCreateUserHis;
import com.tydic.beijing.bvalue.dao.LogTradeHis;
import com.tydic.beijing.bvalue.dao.LowShoppingSumInfo;
import com.tydic.beijing.bvalue.dao.RuleParameters;
import com.tydic.beijing.bvalue.dao.ShoppingDetail;
import com.tydic.beijing.bvalue.dao.ShoppingSumInfo;
import com.tydic.beijing.bvalue.dao.mem.InfoPayBalanceMem;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.DataService;
import com.tydic.uda.service.S;

public class LowUpdateBValueByShoppingFileProcessJDBC {
	
	private static Logger log=Logger.getLogger(LowUpdateBValueByShoppingFileProcessJDBC.class);
//	201706去除redis
//	@Autowired
//	InfoPayBalanceSync infoPayBalanceSync;
	/*@Autowired
	MySessionFactory mySessionFactory ;
	@Autowired
	MyOraSessionFactory myOraSessionFactory;*/
	@Autowired
	DbTool db;
	private String syncRedis;
	
	/*public void setDb(DbTool db) {
		this.db = db;
	}
	
	public DbTool getDb() {
		return this.db;
	}*/
	
	public String getSyncRedis() {
		return syncRedis;
	}


	public void setSyncRedis(String syncRedis) {
		this.syncRedis = syncRedis;
	}

	private String yearMonth;
	private int mod;
	
	private final int max_unrela_all = 200;
	private  int max_rela_month = 0;
	//private List<RuleParameters> listrulePara=new ArrayList<RuleParameters>();
	
	public void generateBvalue(List<String> listUserId) throws Exception {
		
		log.debug("----------------------------generateBvalue start"+listUserId.toString()+"---------------------");
		if(listUserId.size() ==0){
			return ;
		}
		
		/*long starta = System.currentTimeMillis();
		
		Connection conn = null;
		Connection connOra = null;

		String localHost= getHost(suffix);// 通过文件后缀 获取数据库
		mySessionFactory.setHost(localHost);
		
		log.debug("本次选择数据库==>"+localHost);
		
		
		long startb = System.currentTimeMillis();
		//log.debug("获得数据库连接耗时:"+(startb-starta));
        PreparedStatement   infoUserStatement= null;
		PreparedStatement	infoPayBalanceStatement= null;  
		PreparedStatement	logTradeHisStatement= null; 
		PreparedStatement	logTradeCreateUserHisStatement= null;
		PreparedStatement	logTradeShoppingHisStatement= null;  
		PreparedStatement	balanceAccessLogStatement= null;  
		PreparedStatement	updateBalanceStatemenet= null;
		PreparedStatement	InsertGWZTableStatement= null;
		
		PreparedStatement   updateInfoUserStatement = null; //个别用户乱码，需要修改jd_pin
		//TODO
		Statement stmt = null;
		*/
		
		//List<LogBSmsGwz> listgwz = new ArrayList<LogBSmsGwz>();
		
		/*	try {
			conn = mySessionFactory.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement(); //公共statemenet
			
			stmt.execute("set names utf8");
			
			infoUserStatement = conn.prepareStatement("insert into info_user (user_id,jd_pin,create_date,create_channel)"
					+ " values (?,?,STR_TO_DATE(?,'%Y%m%d%H%i%s'),?)");
			
			infoPayBalanceStatement = conn.prepareStatement(" insert into info_pay_balance("
					+ "balance_id, user_id , balance_type_id,balance,eff_date,exp_date) "
					+ " values ( ?,?,?,?,"
					+ " STR_TO_DATE(?,'%Y%m%d%H%i%s'), STR_TO_DATE(?,'%Y%m%d%H%i%s') )");	
			
			logTradeHisStatement = conn.prepareStatement(" insert into log_trade_his ("
					+ "trade_id,trade_type_code,external_system_code,channel_type, user_id,partition_id,order_no,"
					+ "order_type,order_amount,order_completion_time,balance_type_id,unit_type_id,balance,process_tag,"
					+ "trade_time,process_time,remark,overtop_value) "
					+ " values ("
					+ "?,?,?,?,?,?,?,"
					+ "?,?,str_to_date(?, '%Y%m%d%H%i%s'),?,?,?,?"
					+ " ,str_to_date(?, '%Y%m%d%H%i%s'),str_to_date(?, '%Y%m%d%H%i%s'),?,?)" );

			logTradeCreateUserHisStatement = conn.prepareStatement("insert into log_trade_create_user_his(trade_id,jd_pin,user_id,partition_id,process_tag) "
					+ "values(?,?,?,?,?)" );
			
			logTradeShoppingHisStatement = conn.prepareStatement(" insert into log_trade_shopping_his ( "
					+ "trade_id,user_id,partition_id,order_no,order_type,"
					+ " order_completion_time,org_order_no,order_amount,process_tag,process_time) "
					+ " values ("
					+ " ?,?,?,?,?,"
					+ " str_to_date(?, '%Y%m%d%H%i%s'),?,?,?,str_to_date(?, '%Y%m%d%H%i%s') )");
					
			balanceAccessLogStatement = conn.prepareStatement(" insert into balance_access_log( "
					+ "trade_Id, trade_Type_Code, user_Id, partition_Id, balance_Id, balance_Type_Id,"
					+ " access_Tag, money, old_Balance, new_Balance, operate_Time ) "
					+ " values ( ?,?,?,?,"
					+ "?,?,?,"
					+ "?,?,?,str_to_date(?, '%Y%m%d%H%i%s') )");
			
			updateBalanceStatemenet = conn.prepareStatement( "update info_pay_balance set balance = balance + ?,"
					+ " exp_date=str_to_date(?, '%Y%m%d%H%i%s') ,"
					+ " eff_date=str_to_date(?, '%Y%m%d%H%i%s') "
					+ " where user_id= ? and balance_id =? " );
			
			updateInfoUserStatement = conn.prepareStatement("update info_user set jd_pin = ? where user_id = ? ");

			
			log.debug("preparestatement准备statement耗时:"+(startc-startb));*/
			
			//得到封顶值配置列表
			/*ResultSet resultSet1 = null;
			String domaincode="2000";
			String tradetypecode="104";
			Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("domain_code", domaincode);
			filter.put("trade_type_code", tradetypecode);
			listrulePara=S.get(RuleParameters.class).query(Condition.build("byattrValue").filter(filter));*/
			List<InfoPayBalance> changedInfoPayBalance = new ArrayList<InfoPayBalance>();
			long startc = System.currentTimeMillis();
			
			for(String tmpuserid:listUserId){
				try{
					if ("jd_pin".equals(tmpuserid.substring(0,tmpuserid.indexOf(" ")))) {
						continue;
					}
				dealOneUser(tmpuserid,changedInfoPayBalance);
				}catch(BValueException e){
					log.debug("去你姥姥的:"+e.getErrMsg());
				}
			}
			
			long startd = System.currentTimeMillis();
			log.debug("数据处理for循环耗时:"+(startd-startc));
			log.debug("本次需要同步到Redis的账本数量"+changedInfoPayBalance.size());
//			log.debug("shoppingstatement==>"+logTradeShoppingHisStatement.toString( ) );
//			log.debug("logtradehis==>"+logTradeHisStatement.toString( ) );
			
			//批量执行sql
			/*int[] infUserRet = infoUserStatement.executeBatch();
			long start3 = System.currentTimeMillis();
			log.debug("infouser批量处理sql耗时:"+(start3-startd)+",数量"+infUserRet.length+",平均耗时+"+(start3-startd)*1.0/(infUserRet.length==0?1:infUserRet.length));
			
			int[] infPayBalanceRet = infoPayBalanceStatement.executeBatch();  
			long start4 = System.currentTimeMillis();
			log.debug("insert-infopaybalance批量处理sql耗时:"+(start4-start3)+",数量"+infPayBalanceRet.length+",平均耗时+"+(start4-start3)*1.0/(infPayBalanceRet.length==0?1:infPayBalanceRet.length));
			
			
			int[] logTradeHisRet = logTradeHisStatement.executeBatch(); 
			long start5 = System.currentTimeMillis();
			log.debug("logTradeHisStatement批量处理sql耗时:"+(start4-start5)+",数量"+logTradeHisRet.length+",平均耗时+"+(start4-start5)*1.0/(logTradeHisRet.length==0?1:logTradeHisRet.length));
			
			int[] createUserRet = logTradeCreateUserHisStatement.executeBatch();
			long start6 = System.currentTimeMillis();
			log.debug("createuserhis批量处理sql耗时:"+(start6-start5)+",数量"+createUserRet.length+",平均耗时+"+(start6-start5)*1.0/(createUserRet.length==0?1:createUserRet.length));
			
			int[] shoppingHisRet = logTradeShoppingHisStatement.executeBatch();  
			long start7 = System.currentTimeMillis();
			log.debug("shoppinghis批量处理sql耗时:"+(start7-start6)+",数量"+shoppingHisRet.length+",平均耗时+"+(start7-start6)*1.0/(shoppingHisRet.length==0?1:shoppingHisRet.length));
			
			int[] balanceAccessLogRet = balanceAccessLogStatement.executeBatch();  
			long start8 = System.currentTimeMillis();
			log.debug("balanceaccesslog批量处理sql耗时:"+(start8-start7)+",数量"+balanceAccessLogRet.length+",平均耗时+"+(start8-start7)*1.0/(balanceAccessLogRet.length==0?1:balanceAccessLogRet.length));
			
			int[] updateBalanceRet = updateBalanceStatemenet.executeBatch();
			long start9 = System.currentTimeMillis();
			log.debug("update-infopaybalance批量处理sql耗时:"+(start9-start8)+",数量"+updateBalanceRet.length+",平均耗时+"+(start9-start8)*1.0/(updateBalanceRet.length==0?1:updateBalanceRet.length));
			
//			updateInfoUserStatement.executeBatch();
//			long start10 = System.currentTimeMillis();
//			log.debug("infopaybalance批量处理sql耗时:"+(start10-start9));
			
			//checkresult(infUserRet,infPayBalanceRet,logTradeHisRet,createUserRet,shoppingHisRet,balanceAccessLogRet,updateBalanceRet);
			
			
			
			long starte = System.currentTimeMillis();
			log.debug("sql执行耗时:"+(starte-startd));
			
		    infoUserStatement.clearBatch();
			infoPayBalanceStatement.clearBatch();  
			logTradeHisStatement.clearBatch(); 
			logTradeCreateUserHisStatement.clearBatch();
			logTradeShoppingHisStatement.clearBatch();  
			balanceAccessLogStatement.clearBatch();  
			updateBalanceStatemenet.clearBatch(); 
			updateInfoUserStatement.clearBatch();
				
			stmt.close();
			infoUserStatement.close();
			infoPayBalanceStatement.close();  
			logTradeHisStatement.close(); 
			logTradeCreateUserHisStatement.close();
			logTradeShoppingHisStatement.close();  
			balanceAccessLogStatement.close();  
			updateBalanceStatemenet.close();
			updateInfoUserStatement.close();
 
			conn.commit(); //每次所有记录全处理后，commit
			conn.close();
			
		} catch (Exception e) {
			log.debug("异常，回滚"+e.getMessage());
			conn.rollback();
			
			e.printStackTrace();
			throw e;
		}  finally{     
            if(stmt!= null){     
                try {     
                    stmt.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                stmt= null;     
            }   
            if(infoUserStatement!= null){     
                try {     
                	infoUserStatement.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                infoUserStatement= null;     
            }   
            
            if(infoPayBalanceStatement!= null){     
                try {     
                	infoPayBalanceStatement.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                infoPayBalanceStatement= null;     
            }   
            
            if(logTradeHisStatement!= null){     
                try {     
                	logTradeHisStatement.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                logTradeHisStatement= null;     
            }   
            
            if(updateInfoUserStatement!= null){     
                try {     
                	updateInfoUserStatement.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                updateInfoUserStatement= null;     
            }   
            
            
            
            if(logTradeCreateUserHisStatement!= null){     
                try {     
                	logTradeCreateUserHisStatement.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                logTradeCreateUserHisStatement= null;     
            }   
            
            if(logTradeShoppingHisStatement!= null){     
                try {     
                	logTradeShoppingHisStatement.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                logTradeShoppingHisStatement= null;     
            }   
            
            if(balanceAccessLogStatement!= null){     
                try {     
                	balanceAccessLogStatement.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                balanceAccessLogStatement= null;     
            }   
            
            if(updateBalanceStatemenet!= null){     
                try {     
                	updateBalanceStatemenet.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                updateBalanceStatemenet= null;     
            }   
			
			
            if(conn!=null){     
                try {     
                    conn.close();     
                } catch (SQLException e) {     
                    // TODO Auto-generated catch block     
                    e.printStackTrace();     
                }     
                conn= null;     
            }     
        }   */
		
		
		
		//更新到redis
//		try {
//			updateRedis(changedInfoPayBalance);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		//对于已关联的用户，需要把兑换记录插到oracle数据库
		
		/*try{
			
			if(listgwz.size()>0){
				connOra = myOraSessionFactory.getConnection(syncRedis);
				connOra.setAutoCommit(false);
				InsertGWZTableStatement = connOra.prepareStatement("insert into log_B_sms_gwz (msisdn,acctmonth,bvalue,note)"
						+ " values (?,?,?,?)");
				
				for(LogBSmsGwz tmpgwz :listgwz){
					
					if(tmpgwz.getBvalue()<=0){
						continue;
					}
					
					InsertGWZTableStatement.setString(1, tmpgwz.getMsisdn());
					InsertGWZTableStatement.setString(2, tmpgwz.getAcctmonth());
					InsertGWZTableStatement.setLong(3, tmpgwz.getBvalue());
					InsertGWZTableStatement.setString(4, "");
					InsertGWZTableStatement.addBatch();
					
					InsertGWZTableStatement.clearParameters();
				}
                
			   //批量提交
				InsertGWZTableStatement.executeBatch();
				InsertGWZTableStatement.clearBatch();
				InsertGWZTableStatement.close();
				
				connOra.commit();
				connOra.close();
			}
					
		}catch(Exception e){
			log.debug("异常，回滚"+e.getMessage());
			connOra.rollback();
			e.printStackTrace();
			throw e ;
		}finally {
			   if(InsertGWZTableStatement!= null){     
	                try {     
	                	InsertGWZTableStatement.close();     
	                } catch (SQLException e) {     
	                    e.printStackTrace();     
	                }     
	                InsertGWZTableStatement= null;     
	            }   
		}
		*/
		
	}


	/*private String getHost(String suffix) {
		String localHost ="";
		int hostEnd = Integer.parseInt(suffix.substring(0, 5));
		if(syncRedis.equals("REAL")){
			
			localHost = "172.22.185.23"+hostEnd;  ///生产数据库
		}else if(syncRedis.equals("YFB")){
			localHost = "172.22.187.1"+hostEnd;  ///预发布环境
		}else if(syncRedis.equals("TEST")) {
			localHost="192.168.177.1"+hostEnd;//测试数据库
		}else if (syncRedis.equals("DIC")){
			localHost="172.168.1.21"+hostEnd;//测试数据库
		}else if (syncRedis.equals("DICTEST")){
			localHost="172.168.1.21"+hostEnd;//测试数据库
		}else if (syncRedis.equals("LFREAL")){
			if(hostEnd==1){
				localHost="bvalue1.mysql.jddb.com";
			}else if (hostEnd==2){
				localHost="bvalue2.mysql.jddb.com";
			}else if (hostEnd==3){
				localHost="bvalue3.mysql.jddb.com";
			}else if (hostEnd==4){
				localHost="bvalue4.mysql.jddb.com";
			}else if (hostEnd==5){
				localHost="bvalue5.mysql.jddb.com";
			}else if (hostEnd==6){
				localHost="bvalue6.mysql.jddb.com";
			}else if (hostEnd==7){
				localHost="bvalue7.mysql.jddb.com";
			}else if (hostEnd==8){
				localHost="bvalue8.mysql.jddb.com";
			}
			
//			10.191.31.89	1主数据库
//			10.190.68.243 	2主数据库
//			10.190.68.240	3主数据库
//			10.190.68.238	4主数据库
//			10.190.68.236	5主数据库
//			10.190.68.233	6主数据库
//			10.190.68.231	7主数据库
//			10.190.68.228	8主数据库
		}else if (syncRedis.equals("MJQREAL")){
			if(hostEnd==1){
				localHost="bvalue1bak.mysql.jddb.com";
			}else if (hostEnd==2){
				localHost="bvalue2bak.mysql.jddb.com";
			}else if (hostEnd==3){
				localHost="bvalue3bak.mysql.jddb.com";
			}else if (hostEnd==4){
				localHost="bvalue4bak.mysql.jddb.com";
			}else if (hostEnd==5){
				localHost="bvalue5bak.mysql.jddb.com";
			}else if (hostEnd==6){
				localHost="bvalue6bak.mysql.jddb.com";
			}else if (hostEnd==7){
				localHost="bvalue7bak.mysql.jddb.com";
			}else if (hostEnd==8){
				localHost="bvalue8bak.mysql.jddb.com";
			}
			
//			10.187.65.133		
//			10.187.179.230 		
//			10.187.179.229	
//			10.187.179.227		
//			10.187.179.220 		
//			10.187.179.215		
//			10.187.179.219 		
//			10.187.65.130
		}
		return localHost;
	}

	private void checkresult(int[] infUserRet, int[] infPayBalanceRet,
			int[] logTradeHisRet, int[] createUserRet, int[] shoppingHisRet,
			int[] balanceAccessLogRet, int[] updateBalanceRet) throws Exception{
		// 检查返回结果
		
		for(int n:infUserRet){
			if(n<1){
				log.debug("用户batch异常，返回值"+n+",");
				throw new BValueException(-80041,"用户batch异常，返回值"+n+",");
			}
		}
		
		for(int n:infPayBalanceRet){
			if(n<1){
				log.debug("账户新增batch异常，返回值"+n+",");
				throw new BValueException(-80042,"新增账户batch异常，返回值"+n+",");
			}
		}
		
		for(int n:logTradeHisRet){
			if(n<1){
				log.debug("logtradehis-batch异常，返回值"+n+",");
				throw new BValueException(-80043,"订单batch异常，返回值"+n+",");
			}
		}
		
		for(int n:shoppingHisRet){
			if(n<1){
				log.debug("logtradeshoppinghis-batch异常，返回值"+n+",");
				throw new BValueException(-80044,"logtradeshoppinghis-batch异常，返回值"+n+",");
			}
		}
		
		for(int n:createUserRet){
			if(n<1){
				log.debug("logtradecreateuserhis-batch异常，返回值"+n+",");
				throw new BValueException(-80045,"logtradecreateuserhis-batch异常，返回值"+n+",");
			}
		}
		
		for(int n:balanceAccessLogRet){
			if(n<1){
				log.debug("balanceAccessLogRet-batch异常，返回值"+n+",");
				throw new BValueException(-80046,"balanceAccessLogRet-batch异常，返回值"+n+",");
			}
		}
		
		for(int n:balanceAccessLogRet){
			if(n<1){
				log.debug("updateBalanceRet-batch异常，返回值"+n+",");
				throw new BValueException(-80047,"updateBalanceRet-batch异常，返回值"+n+",");
			}
		}
		
		
	}*/

	public int getMod() {
		return mod;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	public void dealOneUser(String strLine,List<InfoPayBalance> changedInfoPayBalance
			) throws Exception {
		log.debug("str00"+strLine.toString());
		long time1 = System.currentTimeMillis();
		//LowShoppingSumInfo lowShoppingSumInfo = getJSONFromString(strLine); 
		
		String jdPin = strLine.substring(0,strLine.indexOf(" "));
		String userId = Common.md5(jdPin);
		
		
		//获取低销赠时间本月最后一天  201801 
		int theyear=Integer.parseInt(yearMonth.substring(0, 4));
		int themonth=Integer.parseInt(yearMonth.substring(4,6));
		 Calendar cal = Calendar.getInstance();
	        //设置年份
	        cal.set(Calendar.YEAR,theyear);
	        //设置月份
	        cal.set(Calendar.MONTH, themonth);
	        //获取某月最大天数
	        //int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        //设置日历中月份的最大天数
	        cal.set(Calendar.DAY_OF_MONTH, 1);
	        //格式化日期
	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
	        String lastDayOfMonth = sdf1.format(cal.getTime());
	        String dxztime1 = lastDayOfMonth+"000000";
	        String dxztime = lastDayOfMonth+"235959";
		    
	      //判断用户是否已经做过低销赠，做过的话则不再处理     待改。。。。
		log.debug("低销赠dxztime是"+dxztime+"-----------------------");
		List<LogTradeHis> log_trade_his = db.getLogTradeHisByUserIdAndDxztime(userId,dxztime1);
		int size = log_trade_his.size();
		log.debug("低销赠log_trade_his是"+size+"-----------------------");
		if(size != 0){
			log.debug("低销 赠送过B值了------------------------");
			return ;
		}
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String createTime = sdf.format(new Date());
		log.debug("creatTime==>"+createTime+",and userid="+userId);
		long bValuea =0L;  //汇总金额后得到的初始B值
		
		long time2 = System.currentTimeMillis();
		log.debug("逻辑处理前耗时="+(time2-time1));
		
		
		String tradeId = Common.getUUID();
		List<InfoPayBalance> listInfoPayBalance = new ArrayList<InfoPayBalance>();
		
				
			
		if(true){
			
				//不进行开户 不进行判定 直接插入B值 更新账本表和log表 
				//createInfoUser(jdPin, "999", createTime);
					
				//读取文件 获得B值 
				bValuea = Long.parseLong(strLine.substring(strLine.indexOf(" ")+1).trim());
				log.debug("读取文件 获得B值:"+bValuea);
				//查到所有余额
				listInfoPayBalance =  getInfoPayBalanceByUserId(userId);
				long time3 = System.currentTimeMillis();
				//更新账本表 ---------------------正常B值
			    updateNormalInfoPayBalance(bValuea,0,createTime,listInfoPayBalance,userId,tradeId,changedInfoPayBalance);  
				//更新订单表 106 低销赠 
			    updateLogTradeHis(userId,jdPin,createTime,tradeId,bValuea);
			    
				long time4 = System.currentTimeMillis();
				log.debug("获得一个用户的数据耗时="+(time4-time3));
		}

	}
	
	private void createInfoUser(String jdPin, String channelType, String createTime
			 )  throws Exception {
		String userId = Common.md5(jdPin);
		String effDate = "";
		String expDate = "";
		int partitionId = Integer.parseInt(createTime.substring(4, 6));

		// 先判断用户是否已存在，如果存在，直接返回
		if (ifAlreadyExisted(userId,jdPin)) {
			return;
		}

		// 插用户表
		InfoUser iu = new InfoUser();
		iu.setUser_id(userId);
		iu.setJd_pin(jdPin);
		iu.setCreate_date(createTime);
		iu.setCreate_channel(channelType);
		iu.setUser_level(0);
		db.insertIntoInfoUser(iu);
		
		// 插入 log 记录
		LogTradeHis lth = new LogTradeHis();
		String tradeId = Common.getUUID();
		lth.setTrade_id(tradeId);
		lth.setTrade_type_code("501");
		lth.setExternal_system_code("10000");
		lth.setChannel_type(channelType);
		lth.setUser_id(userId);
		lth.setPartition_id(partitionId);
		lth.setOrder_no("");
		lth.setOrder_type("");
		lth.setOrder_amount(0);
		lth.setOrder_completion_time(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		lth.setBalance_type_id(0);
		lth.setUnit_type_id(0);
		lth.setBalance(0);
		lth.setProcess_tag(2);
		lth.setTrade_time(createTime);
		lth.setProcess_time(createTime);
		lth.setRemark("实时创建用户");
		lth.setOvertop_value(0);
		
		db.insertIntoLogTradeHis(lth);
		
		// 插入 log表
		LogTradeCreateUserHis ltcu = new LogTradeCreateUserHis();
		ltcu.setTrade_id(tradeId);
		ltcu.setJd_pin(jdPin);
		ltcu.setUser_id(userId);
		ltcu.setPartition_id(partitionId);
		ltcu.setProcess_tag(2);
		
		db.insertIntoLogTradeCreateUserHis(ltcu);
		
	
	}

	//判断该用户是否已存在
	private boolean ifAlreadyExisted(String userId,String jdpin) throws Exception  {
		
		
		
			InfoUser info_user = new InfoUser();
			long starttime = System.currentTimeMillis();
			String  jdpin_db ="";
			info_user = db.queryInfoUserByUserId(userId);
			
			while(info_user != null){
				jdpin_db = info_user.getJd_pin();
				if(jdpin_db!= null && !jdpin_db.equals(jdpin)){
					db.updateInfoUserByUserId(info_user);
				}
				
				return true;
			}
			
			long endtime = System.currentTimeMillis();
			log.debug("select耗时:查询用户是否存在-------->"+(endtime-starttime));
			
			return false;
		
	}

	private LowShoppingSumInfo getJSONFromString(String strLine) throws Exception  {

			return   (LowShoppingSumInfo) JSONObject.toBean(JSONObject.fromObject(strLine), LowShoppingSumInfo.class);
		 
	}
	private String getLastDayofMonth(String yearMonth) {
		
		int year = Integer.parseInt(yearMonth.substring(0,4));
		int month = Integer.parseInt(yearMonth.substring(4,6));
			
		Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.YEAR, year);  
        cal.set(Calendar.MONTH, month -1);  
        // 某年某月的最后一天  
        return yearMonth+cal.getActualMaximum(Calendar.DATE); 
		
	}

	private void updateNormalInfoPayBalance(long bValue, int balanceType,String createTime, List<InfoPayBalance> listInfoPayBalance,String userId,String tradeId,
			List<InfoPayBalance> changedInfoPayBalance ) throws Exception  {
		
		if(bValue == 0){
			return;
		}

		String effDate;
		String expDate;
		String createYear = createTime.substring(0, 4);
		long oldvalue =0L;
		long newvalue =0L;
 
		InfoPayBalance tmpinfoPayBalance = null;
		log.debug("createDate==>"+createYear);
	
		if(createTime.compareTo(createYear+"0701000000") >=0 ){
			effDate = createYear+"0701000000";
			expDate = (Integer.parseInt(createYear)+1) +"0630235959";
		} else {
			effDate = createYear+"0101000000";
			expDate = createYear+"1231235959";
		}
		
		//看是否有符合条件的账本，有则更新到该账本，没有的话要新建账本
		/*if(oweInfoPayBalance.size()>0){
			tmpinfoPayBalance = oweInfoPayBalance.get(0);
		}else{*/
			for(InfoPayBalance infoPayBalance :listInfoPayBalance){
				
				if(infoPayBalance.getBalance_type_id() == balanceType && 
						infoPayBalance.getEff_date().equals(effDate) && infoPayBalance.getExp_date().equals(expDate)){
					tmpinfoPayBalance = new InfoPayBalance(infoPayBalance);
					break;
				}
			}
		//}
			
		
		if(tmpinfoPayBalance !=null){
			//找到了可用账本
			oldvalue = tmpinfoPayBalance.getBalance();
			newvalue = oldvalue + bValue;
			tmpinfoPayBalance.setBalance(bValue);
			/*if(newvalue < 0){
				effDate = tmpinfoPayBalance.getEff_date();//如果是销欠费，可能不能完全销清，这样的话就不能修改原生失效时间
				expDate = tmpinfoPayBalance.getExp_date();
			}*/
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);
			//log.debug("读取文件: 4 " + tmpinfoPayBalance);		
			db.updateInfoPayBalanceByUserIdAndBalanceId(tmpinfoPayBalance);	
			
		    
	
		}else{
			//没有找到可用账本
			String tmpbalanceId =  Common.getUUID();
			
			tmpinfoPayBalance = new InfoPayBalance();
			
			tmpinfoPayBalance.setBalance_id(tmpbalanceId);
			tmpinfoPayBalance.setUser_id(userId);
			tmpinfoPayBalance.setBalance((int)bValue);
			tmpinfoPayBalance.setBalance_type_id(balanceType);
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);
			//log.debug("读取文件: 5 " + tmpinfoPayBalance);
			db.insertIntoInfoPayBalance(tmpinfoPayBalance);
			newvalue = bValue;
		}
		
		BalanceAccessLog bal = new BalanceAccessLog();
		bal.setTrade_Id(tradeId);		
		bal.setTrade_Type_Code("106");
		bal.setUser_Id(userId);
		bal.setPartition_Id(Integer.parseInt(yearMonth.substring(4,6)));
		bal.setBalance_Id(tmpinfoPayBalance.getBalance_id());
		bal.setBalance_Type_Id(balanceType);
		bal.setAccess_Tag("0");
		bal.setMoney((int)bValue);
		bal.setOld_Balance((int)oldvalue);
		bal.setNew_Balance((int)newvalue);
		bal.setOperate_Time(createTime);
		//log.debug("读取文件: 6 " + bal);
		db.insertBalanceAccessLog(bal);

		tmpinfoPayBalance.setBalance(newvalue);
		changedInfoPayBalance.add(tmpinfoPayBalance);
		
	}

	private List<InfoPayBalance> getInfoPayBalanceByUserId(String userId) throws Exception  {
 
			List<InfoPayBalance> listInfoPayBalance = new ArrayList<InfoPayBalance>();
			
			long starttime = System.currentTimeMillis();
				
			listInfoPayBalance = db.getInfoPayBalanceByUserId(userId);
			 
			 long endtime = System.currentTimeMillis();
			 log.debug("select耗时:查询用户账户---------->"+(endtime-starttime));
			 
			return listInfoPayBalance;
		
	}

	private void updateLogTradeHis(String userId, String jdPin,
			String createTime, String tradeId,long balance) throws Exception {
		
		LogTradeHis lth = new LogTradeHis();
		lth.setTrade_id(tradeId);
		lth.setTrade_type_code("106");
		lth.setExternal_system_code("10000");
		lth.setChannel_type("999");
		lth.setUser_id(userId);
		lth.setPartition_id(Integer.parseInt(yearMonth.substring(4,6)));
		lth.setOrder_no("");   
		lth.setOrder_type("");
		lth.setOrder_amount(0);
		lth.setOrder_completion_time(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		lth.setBalance_type_id(0);
		lth.setUnit_type_id(0);
		lth.setBalance((int)balance);
		lth.setProcess_tag(2);
		lth.setTrade_time(createTime);
		lth.setProcess_time(createTime);
		lth.setRemark("低销赠");
		lth.setOvertop_value(0);
		//log.debug("读取文件: 7 " + lth);
		
		db.insertIntoLogTradeHis(lth);
		
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	
	



	

}
