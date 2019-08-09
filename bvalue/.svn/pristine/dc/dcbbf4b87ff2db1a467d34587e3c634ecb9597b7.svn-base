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

import com.tydic.beijing.bvalue.common.BValueException;
import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.common.DateUtil;
import com.tydic.beijing.bvalue.common.MyOraSessionFactory;
import com.tydic.beijing.bvalue.common.MySessionFactory;
import com.tydic.beijing.bvalue.core.InfoPayBalanceSync;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccount;
import com.tydic.beijing.bvalue.dao.InfoUserExternalAccountAttr;
import com.tydic.beijing.bvalue.dao.LogBSmsGwz;
import com.tydic.beijing.bvalue.dao.RuleParameters;
import com.tydic.beijing.bvalue.dao.ShoppingDetail;
import com.tydic.beijing.bvalue.dao.ShoppingSumInfo;
import com.tydic.beijing.bvalue.dao.mem.InfoPayBalanceMem;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.DataService;
import com.tydic.uda.service.S;

public class UpdateBValueByShoppingFileProcessJDBC {
	
	private static Logger log=Logger.getLogger(UpdateBValueByShoppingFileProcessJDBC.class);
//	201706去除redis
//	@Autowired
//	InfoPayBalanceSync infoPayBalanceSync;
	@Autowired
	MySessionFactory mySessionFactory ;
	@Autowired
	MyOraSessionFactory myOraSessionFactory;

	private String syncRedis;
	
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
	private List<RuleParameters> listrulePara=new ArrayList<RuleParameters>();
	
	public void generateBvalue(List<String> listUserId,String suffix,
			HashSet<String> userNum) throws Exception {
		
		log.debug("----------------------------generateBvalue start---------------------");
		if(listUserId.size() ==0){
			return ;
		}
		
		long starta = System.currentTimeMillis();
		
		Connection conn = null;
		Connection connOra = null;

		String localHost= getHost(suffix);
		mySessionFactory.setHost(localHost);
		
		log.debug("本次选择数据库==>"+localHost);
		
		
		long startb = System.currentTimeMillis();
		log.debug("获得数据库连接耗时:"+(startb-starta));
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
		
		List<InfoPayBalance> changedInfoPayBalance = new ArrayList<InfoPayBalance>();
		List<LogBSmsGwz> listgwz = new ArrayList<LogBSmsGwz>();
		
		try {
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

			long startc = System.currentTimeMillis();
			log.debug("preparestatement准备statement耗时:"+(startc-startb));
			
			//得到封顶值配置列表
			ResultSet resultSet1 = null;
			String domaincode="2000";
			String tradetypecode="104";
			Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("domain_code", domaincode);
			filter.put("trade_type_code", tradetypecode);
			listrulePara=S.get(RuleParameters.class).query(Condition.build("byattrValue").filter(filter));
			
			
			for(String tmpuserid:listUserId){
				try{
				dealOneUser(tmpuserid,stmt,infoUserStatement,infoPayBalanceStatement,logTradeHisStatement,logTradeCreateUserHisStatement,logTradeShoppingHisStatement,balanceAccessLogStatement,
						updateBalanceStatemenet,changedInfoPayBalance,listgwz,updateInfoUserStatement,userNum);
				}catch(BValueException e){
					log.debug("去你姥姥的:"+e.getErrMsg());
				}
			}
			
			long startd = System.currentTimeMillis();
			log.debug("数据处理for循环耗时:"+(startd-startc));
			
//			log.debug("shoppingstatement==>"+logTradeShoppingHisStatement.toString( ) );
//			log.debug("logtradehis==>"+logTradeHisStatement.toString( ) );
			
			//批量执行sql
			int[] infUserRet = infoUserStatement.executeBatch();
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
        }   
		
		
		log.debug("本次需要同步到Redis的账本数量"+changedInfoPayBalance.size());
		//更新到redis
//		try {
//			updateRedis(changedInfoPayBalance);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		//对于已关联的用户，需要把兑换记录插到oracle数据库
		
		try{
			
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
		
		
	}


	private String getHost(String suffix) {
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
		
		
	}

	public int getMod() {
		return mod;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	public void dealOneUser(String strLine,Statement stmt,PreparedStatement infoUserStatement,PreparedStatement infoPayBalanceStatement,
			PreparedStatement logTradeHisStatement,PreparedStatement logTradeCreateUserHisStatement,PreparedStatement logTradeShoppingHisStatement,
			PreparedStatement balanceAccessLogStatement, PreparedStatement updateBalanceStatemenet,List<InfoPayBalance> changedInfoPayBalance,
			List<LogBSmsGwz> listgwz, PreparedStatement updateInfoUserStatement,HashSet<String> userNum
			) throws Exception {

		long time1 = System.currentTimeMillis();
		ShoppingSumInfo shoppingSumInfo = getJSONFromString(strLine);
		
		
		String jdPin = shoppingSumInfo.getJdpin();
		String userId = Common.md5(jdPin);
		
		//获取购物赠时间本月最后一天
		int theyear=Integer.parseInt(yearMonth.substring(0, 4));
		int themonth=Integer.parseInt(yearMonth.substring(4,6));
		 Calendar cal = Calendar.getInstance();
	        //设置年份
	        cal.set(Calendar.YEAR,theyear);
	        //设置月份
	        cal.set(Calendar.MONTH, themonth-1);
	        //获取某月最大天数
	        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        //设置日历中月份的最大天数
	        cal.set(Calendar.DAY_OF_MONTH, lastDay);
	        //格式化日期
	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
	        String lastDayOfMonth = sdf1.format(cal.getTime());
	        String gwztime1 = lastDayOfMonth+"000000";
	        String gwztime = lastDayOfMonth+"235959";
		    
	      //判断用户是否已经做过购物赠，做过的话则不再处理
	      //TODO 暂屏蔽
		log.debug("购物赠gwztime是"+gwztime+"-----------------------");
		String sql = "select trade_id "
				+ " from log_trade_his where user_id=  '" +userId+
		    "' and trade_type_code ='104' and  date_format(trade_time , '%Y%m%d%H%i%s') >= '"+gwztime1+"'";
		ResultSet resultSet = stmt.executeQuery(sql);
		while(resultSet.next()){
			log.debug("赠送过B值了------------------------");
			return ;
		}
		
		
		
		
		//test
//		int n =0;
//		if(n==0){
//			log.debug("userId"+userId+"对应的hashcode取模值"+Math.abs(userId.hashCode()%1600));
//			if(Math.abs(userId.hashCode()%1600) >399 || Math.abs(userId.hashCode()%1600) <200){
//				log.debug("fuck!!!"+Math.abs(userId.hashCode()%1600) );
//			}
//			return;
//		}
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String createTime = sdf.format(new Date());
		log.debug("creatTime==>"+createTime+",and userid="+userId);
		long bValuea =0L;  //汇总金额后得到的初始B值
		//long bValueb =0L;   //扣减欠费后的B值
		long bValue_normal =0L;  //最终要归档的正常B值
		long bValue_overflow =0L; //最终要归档的溢出B值
		
		long sumAmount = Long.parseLong(shoppingSumInfo.getAmount()); //总消费金额  单位 分
		if(sumAmount <= 0){
			return;
		}
		
		long time2 = System.currentTimeMillis();
		//log.debug("逻辑处理前耗时="+(time2-time1));
		
		//TODO 增加判断该用户是否属于该数据库
	    if(!isRightDb(userId,this.mod,this.mySessionFactory.getHost())){
	    	log.debug("数据库选择错误,jdpin="+jdPin+",userid="+userId+",host=["+this.mySessionFactory.getHost()+"]");
	    	throw new BValueException(-80012,"数据库错误");
	    }
		
	    long time3 = System.currentTimeMillis();
		//log.debug("校验数据库是否正确耗时="+(time2-time3));
		
		List<InfoPayBalance> oweInfoPayBalance = new ArrayList<InfoPayBalance>();
		String tradeId = Common.getUUID();
		List<InfoPayBalance> listInfoPayBalance = new ArrayList<InfoPayBalance>();
		
		long inittime =System.currentTimeMillis();
	    //获取关联信息
		InfoUserExternalAccount infoUserExternalAccount = getInfoUserExternalAccountByUserId(userId,createTime,stmt);
		
		//增加手机号码的判断
		String ExternalAccountCode=infoUserExternalAccount.getExternal_account_code();
		String phoneNum=null;
		long checkcontainstarttime = System.currentTimeMillis();
		if(userNum.contains(ExternalAccountCode)){
			phoneNum=ExternalAccountCode;
		}
		if(phoneNum==null){
			log.debug("列表中没有对应的手机号"+ExternalAccountCode+"不予处理");
			return;
		}
		long checkcontainendtime = System.currentTimeMillis();
		log.debug("checkcontains cost time "+(checkcontainendtime-checkcontainstarttime)+"ms");
		
		
//		log.debug("关联信息===>"+infoUserExternalAccount.toString());
		//TODO 如果未关联，则不做购物赠   6月继续送
//		if(infoUserExternalAccount == null || infoUserExternalAccount.getUser_id() == null || infoUserExternalAccount.getUser_id().length()==0){
//			return ;
//		}
//		if(infoUserExternalAccount != null && infoUserExternalAccount.getUser_id() !=null && infoUserExternalAccount.getUser_id().length()>0){
//			//如果是关联用户，还需要判断是否合约乐购卡，如果是，则不做购物赠
//			log.debug("开始判断是否乐购卡，关联id="+infoUserExternalAccount.getExternal_account_id());
//			if(isLEGOUandHEYUE(stmt,userId,infoUserExternalAccount.getExternal_account_id())){
//				log.debug("............................乐购卡合约用户.........................");
//				return ;
//			}
//		}
				
		 long time4 = System.currentTimeMillis();
			//log.debug("查询关联表耗时="+(time4-time3));
			
		if(true){ //presentFlag(listAttr)
								
				//开户
				createInfoUser(jdPin, "110", createTime,stmt,infoUserStatement,logTradeHisStatement,logTradeCreateUserHisStatement,updateInfoUserStatement);

				 long time5 = System.currentTimeMillis();
					//log.debug("开户耗时="+(time5-time4));
					
				//折算B值 2元折算1B，向上取整
				bValuea = getbValuea(sumAmount,userId);
				//log.debug("get bvaluea"+bValuea);
				
				//查到所有余额
				listInfoPayBalance =  getInfoPayBalanceByUserId(userId,stmt);
				//log.debug("获得的账本"+listInfoPayBalance.size());
				
				long time6 = System.currentTimeMillis();
				//log.debug("查询账本耗时="+(time5-time6));
		 
				//查询是否有往期B值欠费, 不再单独做抵扣
				//bValueb = getOwe(bValuea,listInfoPayBalance,oweInfoPayBalance);
				//折算B值和溢出B值
				bValue_normal = getNormalBValue(stmt,bValuea,userId,infoUserExternalAccount,listInfoPayBalance,oweInfoPayBalance,gwztime);
				bValue_overflow =  bValuea - bValue_normal;
				
				
				//对于关联了的用户，赠送的B值还需要写入oracle作为发送短信依据
				if(infoUserExternalAccount !=null && infoUserExternalAccount.getUser_id()!= null && infoUserExternalAccount.getUser_id().length()>0)
				{
					LogBSmsGwz gwz = new LogBSmsGwz();
					gwz.setMsisdn(infoUserExternalAccount.getExternal_account_code());
					gwz.setBvalue(bValue_normal);
					gwz.setAcctmonth(yearMonth);
					gwz.setNote("");
					listgwz.add(gwz);
				}
				if(bValue_normal <0 || bValue_overflow <0){
					throw new BValueException(-80002,"获得的赠送B值和溢出B值都不能小于0");
				}

				if(oweInfoPayBalance.size() >1){
					log.error("多账本为负数用户user_id："+oweInfoPayBalance.get(0).getUser_id());
					
					throw new BValueException(-80003,"出现了多条欠费记录，异常");
				}
				//更新账本表
			    updateNormalInfoPayBalance(bValue_normal,0,createTime,listInfoPayBalance,userId,tradeId,changedInfoPayBalance,oweInfoPayBalance,infoPayBalanceStatement,balanceAccessLogStatement, stmt,updateBalanceStatemenet);  //正常B值
			    updateOverFlowInfoPayBalance(bValue_overflow ,1,yearMonth,listInfoPayBalance,userId,tradeId,infoPayBalanceStatement,balanceAccessLogStatement, stmt,updateBalanceStatemenet); //溢出B值

				
				//更新订单表
				updateLogTradeHis(userId,jdPin,createTime,tradeId,bValue_normal,bValue_overflow,Long.parseLong(shoppingSumInfo.getAmount()),logTradeHisStatement);
				//更新购物赠订单历史表
				updateLogTradeShoppingHis(userId,jdPin,createTime,tradeId,shoppingSumInfo,logTradeShoppingHisStatement);

				long time11 = System.currentTimeMillis();
				log.debug("获得一个用户的数据耗时="+(time11-time6));
		}

		long endtime =System.currentTimeMillis();
		//log.debug("一个用户入库耗时"+(endtime-inittime));
		 
	}
	
	//判断是否乐购卡和合约用户 是则返回true
	//合约属性编码  1001
    //产品属性编码  1002  取值范围（01自由行  02乐购卡）
	private boolean isLEGOUandHEYUE(Statement stmt, String userId,String externalAccountId) throws Exception {
 
				ResultSet resultSet  = null;
		try {
			List<InfoUserExternalAccountAttr> listattr = new ArrayList<InfoUserExternalAccountAttr>();
			
			String sql = "select external_account_id ,user_id,jd_pin ,external_system_code ,attr_code,attr_value, date_format(eff_date, '%Y%m%d%H%i%s') eff_date, date_format(exp_date, '%Y%m%d%H%i%s') exp_date "
					+ " from info_user_external_account_attr where user_id=  '" +userId+
			    "' and attr_code in ('1001','1002','1003') "
			    + " and external_account_id ='"+externalAccountId+"'";
			resultSet = stmt.executeQuery(sql);
			while(resultSet.next()){
				InfoUserExternalAccountAttr infoUserattr = new InfoUserExternalAccountAttr();
				infoUserattr.setExternal_account_id(resultSet.getString(1));
				infoUserattr.setUser_id(resultSet.getString(2));
				infoUserattr.setJd_pin(resultSet.getString(3));
				infoUserattr.setExternal_system_code(resultSet.getString(4));
				infoUserattr.setAttr_code(resultSet.getString(5));
				infoUserattr.setAttr_value(resultSet.getString(6));
				infoUserattr.setEff_date(resultSet.getString(7));
				infoUserattr.setExp_date(resultSet.getString(8));
				listattr.add(infoUserattr);
			}
			
			if(listattr.size()==0){
				return false;
			}
			
			if(isExistHEYUEAttr(listattr) && isLEGOU(listattr)){
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if (resultSet != null){
				resultSet.close();
			}
		}
		
		 
	}

	//产品属性编码  1002  取值范围（01自由行  02乐购卡）
	private boolean isLEGOU(List<InfoUserExternalAccountAttr> listattr) {
      String monthend = getLastDayofMonth(yearMonth)+"235959";
		
		for(InfoUserExternalAccountAttr tmpattr :listattr){
			if(tmpattr.getAttr_code().equals("1002") && tmpattr.getAttr_value()!= null && tmpattr.getAttr_value().equals("02") &&
					tmpattr.getExp_date().compareTo(monthend) >=0 
					&& tmpattr.getEff_date().compareTo(monthend) <=0){
				return true;
			}
		}
		return false;
	}


	private boolean isExistHEYUEAttr(List<InfoUserExternalAccountAttr> listattr) {
		//是否存在合约属性1001
		String monthend = getLastDayofMonth(yearMonth)+"235959";
		log.debug("用于判断是否合约的月底时间："+monthend);
		
		for(InfoUserExternalAccountAttr tmpattr :listattr){
			if(tmpattr.getAttr_code().equals("1001") && tmpattr.getAttr_value()!= null && tmpattr.getAttr_value().length()>0 &&
					tmpattr.getExp_date().compareTo(monthend) >=0
					&& tmpattr.getExp_date().compareTo(tmpattr.getEff_date()) >=0
					//&& tmpattr.getEff_date().compareTo(monthend) <=0
					){
				//只要存在合约，就算未生效也认为是合约用户,结束时间大于当前时间并且结束时间大于开始时间
				return true;
			}
		}
		
		return false;
	}


	private boolean isRightDb(String userId,int mod,String host) {
		
		int modValue = Math.abs(userId.hashCode()%mod);  
		log.debug("userid取哈希值后取模==["+modValue+"],and hosnt=["+host+"]");
		
		if(syncRedis.equals("TEST")){  //不同步redis，说明是测试库
			return true;
			
		}else if (syncRedis.equals("MJQREAL")){
			if(       (modValue >=0 && modValue <=199 && host.equals("bvalue1bak.mysql.jddb.com")) 
					||(modValue >=200 && modValue <=399 && host.equals("bvalue2bak.mysql.jddb.com")) 
					||(modValue >=400 && modValue <=599 && host.equals("bvalue3bak.mysql.jddb.com")) 
					||(modValue >=600 && modValue <=799 && host.equals("bvalue4bak.mysql.jddb.com")) 
					||(modValue >=800 && modValue <=999 && host.equals("bvalue5bak.mysql.jddb.com")) 
					||(modValue >=1000 && modValue <=1199 && host.equals("bvalue6bak.mysql.jddb.com")) 
					||(modValue >=1200 && modValue <=1399 && host.equals("bvalue7bak.mysql.jddb.com")) 
					||(modValue >=1400 && modValue <=1599 && host.equals("bvalue8bak.mysql.jddb.com")) ){
//				10.187.65.133		
//				10.187.179.230 		
//				10.187.179.229	
//				10.187.179.227		
//				10.187.179.220 		
//				10.187.179.215		
//				10.187.179.219 		
//				10.187.65.130
				return true;
			}else {
				return false;
			}
		}
		
		else if (syncRedis.equals("LFREAL")){
			if(       (modValue >=0 && modValue <=199 && host.equals("bvalue1.mysql.jddb.com")) 
					||(modValue >=200 && modValue <=399 && host.equals("bvalue2.mysql.jddb.com")) 
					||(modValue >=400 && modValue <=599 && host.equals("bvalue3.mysql.jddb.com")) 
					||(modValue >=600 && modValue <=799 && host.equals("bvalue4.mysql.jddb.com")) 
					||(modValue >=800 && modValue <=999 && host.equals("bvalue5.mysql.jddb.com")) 
					||(modValue >=1000 && modValue <=1199 && host.equals("bvalue6.mysql.jddb.com")) 
					||(modValue >=1200 && modValue <=1399 && host.equals("bvalue7.mysql.jddb.com")) 
					||(modValue >=1400 && modValue <=1599 && host.equals("bvalue8.mysql.jddb.com")) ){
				
//				10.191.31.89	1主数据库
//				10.190.68.243 	2主数据库
//				10.190.68.240	3主数据库
//				10.190.68.238	4主数据库
//				10.190.68.236	5主数据库
//				10.190.68.233	6主数据库
//				10.190.68.231	7主数据库
//				10.190.68.228	8主数据库
				
				return true;
			}else {
				return false;
			}
		}else if (syncRedis.equals("REAL")){
			int currHost = Integer.parseInt(host.substring(host.length()-3));
			//正式环境mod
			if(       (modValue >=0 && modValue <=199 && currHost ==231) 
					||(modValue >=200 && modValue <=399 && currHost ==232) 
					||(modValue >=400 && modValue <=599 && currHost ==233) 
					||(modValue >=600 && modValue <=799 && currHost ==234) 
					||(modValue >=800 && modValue <=999 && currHost ==235) 
					||(modValue >=1000 && modValue <=1199 && currHost ==236) 
					||(modValue >=1200 && modValue <=1399 && currHost ==237) 
					||(modValue >=1400 && modValue <=1599 && currHost ==238) ){
				return true;
			}else {
				return false;
			}
		}else if (syncRedis.equals("YFB")) {
			int currHost = Integer.parseInt(host.substring(host.length()-2));
			//正式环境mod
			if(       (modValue >=0 && modValue <=399 && currHost ==11) 
					||(modValue >=400 && modValue <=799 && currHost ==12) 
					||(modValue >=800 && modValue <=1199 && currHost ==13) 
					||(modValue >=1200 && modValue <=1599 && currHost ==14)  ){
				return true;
			}else {
				return false;
			}
		}else if (syncRedis.equals("DIC") || syncRedis.equals("DICTEST")){
			
			int currHost = Integer.parseInt(host.substring(host.length()-3));
			
			if(       (modValue >=0 && modValue <=799 && currHost ==217) 
					||(modValue >=800 && modValue <=1599 && currHost ==218)  ){
				return true;
			}else {
				return false;
			}
			
			
		}
		else{
			return false;
		}
		
	}

	private void createInfoUser(String jdPin, String channelType, String createTime,
			Statement stmt, PreparedStatement infoUserStatement,
			PreparedStatement logTradeHisStatement,
			PreparedStatement logTradeCreateUserHisStatement,
			PreparedStatement updateInfoUserStatement  
			 )  throws Exception {
		
		String userId = Common.md5(jdPin);
		String effDate = "";
		String expDate = "";
		// boolean needToUpdateLogTable = false;
		// log.debug("userId==>"+userId);
		int partitionId = Integer.parseInt(createTime.substring(4, 6));
		// long starttime =System.currentTimeMillis();

		// 先判断用户是否已存在，如果存在，直接返回
		if (ifAlreadyExisted(userId,stmt,updateInfoUserStatement,jdPin)) {
			return;
		}

		// 插用户表
		infoUserStatement.setString(1, userId);
		infoUserStatement.setString(2, jdPin);
		infoUserStatement.setString(3, createTime);
		infoUserStatement.setString(4, channelType);
		infoUserStatement.addBatch();
		
		infoUserStatement.clearParameters();

		String tradeId = Common.getUUID();
		logTradeHisStatement.setString(1, tradeId);
		logTradeHisStatement.setString(2, "501");
		logTradeHisStatement.setString(3, "10000");
		logTradeHisStatement.setString(4, channelType);
		logTradeHisStatement.setString(5, userId);
		logTradeHisStatement.setInt(6, partitionId);
		logTradeHisStatement.setString(7, "");
		logTradeHisStatement.setString(8, "");//第二行
		logTradeHisStatement.setInt(9, 0);
		logTradeHisStatement.setString(10, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		logTradeHisStatement.setString(11, "0");
		logTradeHisStatement.setString(12, "0");
		logTradeHisStatement.setInt(13, 0);
		logTradeHisStatement.setInt(14, 2);
		logTradeHisStatement.setString(15, createTime);//第三行
		logTradeHisStatement.setString(16, createTime);
		logTradeHisStatement.setString(17, "实时创建用户");
		logTradeHisStatement.setInt(18, 0);
		logTradeHisStatement.addBatch();
		
		logTradeHisStatement.clearParameters();

//		 insert into log_trade_his ("
//					+ "trade_id,trade_type_code,external_system_code,channel_type, user_id,partition_id,order_no,"
//					+ "order_type,order_amount,order_completion_time,balance_type_id,unit_type_id,balance,process_tag,"
//					+ "trade_time,process_time,remark,overtop_value) "
//					+ " values ("
//					+ "?,?,?,?,?,?,?,"
//					+ "?,?,str_to_date(?, '%Y%m%d%H%i%s'),?,?,?,?"
//					+ " ,str_to_date(?, '%Y%m%d%H%i%s'),str_to_date(?, '%Y%m%d%H%i%s'),?,?)"
					
	    
//		insert into log_trade_create_user_his(trade_id,jd_pin,user_id,partition_id,process_tag)
//		values(?,?,?,?,?)
		
		
		logTradeCreateUserHisStatement.setString(1, tradeId);
		logTradeCreateUserHisStatement.setString(2, jdPin);
		logTradeCreateUserHisStatement.setString(3, userId);
		logTradeCreateUserHisStatement.setInt(4, partitionId);
		logTradeCreateUserHisStatement.setInt(5, 2);
		logTradeCreateUserHisStatement.addBatch();
		
		logTradeCreateUserHisStatement.clearParameters();
	
	}

	//判断该用户是否已存在
	private boolean ifAlreadyExisted(String userId,Statement stmt,PreparedStatement updateInfoUserStatement,String jdpin) throws Exception  {
		
		ResultSet rs = null;
		try {
			
			long starttime = System.currentTimeMillis();
			String sql =" select user_id,jd_pin from info_user where user_id =  '" + userId +"'";
			String  jdpin_db ="";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				jdpin_db = rs.getString(2);
				if(jdpin_db!= null && !jdpin_db.equals(jdpin)){
					updateInfoUserStatement.setString(1, jdpin);
					updateInfoUserStatement.setString(2,userId);
					updateInfoUserStatement.addBatch();
				}
				
				return true;
			}
			
			long endtime = System.currentTimeMillis();
			log.debug("select耗时:查询用户是否存在-------->"+(endtime-starttime));
			
			rs.close();
			
		
		
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}  finally {
			if(rs != null){
				rs.close();
			}
		}
	}

	private ShoppingSumInfo getJSONFromString(String strLine) throws Exception  {

			return   (ShoppingSumInfo) JSONObject.toBean(JSONObject.fromObject(strLine), ShoppingSumInfo.class);
		 
	}
 

	private void updateOverFlowInfoPayBalance(long bValue, int balanceType,
			String yearMonth, List<InfoPayBalance> listInfoPayBalance,String userId,String tradeId,
			PreparedStatement infoPayBalanceStatement,PreparedStatement balanceAccessLogStatement ,Statement stmt,
			PreparedStatement updateBalanceStatemenet) throws Exception { //,List<InfoPayBalance> changedInfoPayBalance
		
		if(bValue == 0){
			return;
		}

		String effDate;
		String expDate;
		long oldvalue =0L;
		long newvalue =0L;
 
		InfoPayBalance tmpinfoPayBalance = null;
		log.debug("yearMonth==>"+yearMonth);
	
		//溢出账本按月来计算
		effDate = yearMonth +"01000000";
		expDate = getLastDayofMonth(yearMonth)+"235959";
		log.debug("账本时间"+effDate+",exp="+expDate);
		
		
		//看是否有符合条件的账本，有则更新到该账本，没有的话要新建账本
		for(InfoPayBalance infoPayBalance :listInfoPayBalance){
			
			if(infoPayBalance.getBalance_type_id() == balanceType && 
					infoPayBalance.getEff_date().equals(effDate) && infoPayBalance.getExp_date().equals(expDate)){
				tmpinfoPayBalance = infoPayBalance;
				break;
			}
		}
		
		if(tmpinfoPayBalance !=null){
			//找到了可用账本
			oldvalue = tmpinfoPayBalance.getBalance();
			newvalue = oldvalue + bValue;
			tmpinfoPayBalance.setBalance(bValue);
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);
			//S.get(InfoPayBalance.class).update(tmpinfoPayBalance);
	
//			String sql = "update info_pay_balance set balance = balance + "+bValue+", exp_date=str_to_date("+expDate+", '%Y%m%d%H%i%s') ,"
//					+ " eff_date=str_to_date("+effDate+", '%Y%m%d%H%i%s') "
//					+ " where user_id= "+tmpinfoPayBalance.getUser_id()+" and balance_id = "+ tmpinfoPayBalance.getBalance_id();
//			
//		    stmt.executeUpdate(sql);
		    
		    updateBalanceStatemenet.setLong(1, bValue);
		    updateBalanceStatemenet.setString(2, expDate);
		    updateBalanceStatemenet.setString(3, effDate);
		    updateBalanceStatemenet.setString(4, tmpinfoPayBalance.getUser_id());
		    updateBalanceStatemenet.setString(5, tmpinfoPayBalance.getBalance_id());
		    updateBalanceStatemenet.addBatch();
		    
		    updateBalanceStatemenet.clearParameters();
		    
				
		}else{
			//没有找到可用账本
            String tmpBalanceId = Common.getUUID();
			infoPayBalanceStatement.setString(1,  tmpBalanceId);
			infoPayBalanceStatement.setString(2, userId);
			infoPayBalanceStatement.setInt(3, balanceType);
			infoPayBalanceStatement.setInt(4, (int)bValue);
			infoPayBalanceStatement.setString(5, effDate);
			infoPayBalanceStatement.setString(6, expDate);
			infoPayBalanceStatement.addBatch();
			
			infoPayBalanceStatement.clearParameters();

			newvalue = bValue;
			tmpinfoPayBalance =  new InfoPayBalance();
			tmpinfoPayBalance.setBalance_id(tmpBalanceId);
		}
		
		balanceAccessLogStatement.setString(1, tradeId);
		balanceAccessLogStatement.setString(2, "104");
		balanceAccessLogStatement.setString(3, userId);
		balanceAccessLogStatement.setInt(4, Integer.parseInt(yearMonth.substring(4,6)));
		balanceAccessLogStatement.setString(5, tmpinfoPayBalance.getBalance_id());
		balanceAccessLogStatement.setInt(6, balanceType);
		balanceAccessLogStatement.setString(7, "0");
		balanceAccessLogStatement.setInt(8, (int)bValue);
		balanceAccessLogStatement.setInt(9, (int)oldvalue);
		balanceAccessLogStatement.setInt(10, (int)newvalue);
		balanceAccessLogStatement.setString(11, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		balanceAccessLogStatement.addBatch();
		
		balanceAccessLogStatement.clearParameters();
		
		tmpinfoPayBalance.setBalance(newvalue);
		//changedInfoPayBalance.add(tmpinfoPayBalance);
		
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

	private void updateNormalInfoPayBalance(long bValue, int balanceType,
			String createTime, List<InfoPayBalance> listInfoPayBalance,String userId,String tradeId,List<InfoPayBalance> changedInfoPayBalance,List<InfoPayBalance> oweInfoPayBalance,
			PreparedStatement infoPayBalanceStatement,PreparedStatement balanceAccessLogStatement ,Statement stmt ,PreparedStatement updateBalanceStatemenet ) throws Exception  {
		
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
		if(oweInfoPayBalance.size()>0){
			tmpinfoPayBalance = oweInfoPayBalance.get(0);
		}else{
			for(InfoPayBalance infoPayBalance :listInfoPayBalance){
				
				if(infoPayBalance.getBalance_type_id() == balanceType && 
						infoPayBalance.getEff_date().equals(effDate) && infoPayBalance.getExp_date().equals(expDate)){
					tmpinfoPayBalance = new InfoPayBalance(infoPayBalance);
					break;
				}
			}
		}
			
		
		if(tmpinfoPayBalance !=null){
			//找到了可用账本
			oldvalue = tmpinfoPayBalance.getBalance();
			newvalue = oldvalue + bValue;
			tmpinfoPayBalance.setBalance(bValue);
			if(newvalue < 0){
				effDate = tmpinfoPayBalance.getEff_date();//如果是销欠费，可能不能完全销清，这样的话就不能修改原生失效时间
				expDate = tmpinfoPayBalance.getExp_date();
			}
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);
			//S.get(InfoPayBalance.class).update(tmpinfoPayBalance);
			
//			String sql = "update info_pay_balance set balance = balance + "+bValue+", exp_date=str_to_date('"+expDate+"', '%Y%m%d%H%i%s') ,"
//					+ " eff_date=str_to_date('"+effDate+"', '%Y%m%d%H%i%s') "
//					+ " where user_id= '"+tmpinfoPayBalance.getUser_id()+"' and balance_id = '"+ tmpinfoPayBalance.getBalance_id() +"'";
//			
//		    stmt.executeUpdate(sql);
//		    
		    updateBalanceStatemenet.setLong(1, bValue);
		    updateBalanceStatemenet.setString(2, expDate);
		    updateBalanceStatemenet.setString(3, effDate);
		    updateBalanceStatemenet.setString(4, tmpinfoPayBalance.getUser_id());
		    updateBalanceStatemenet.setString(5, tmpinfoPayBalance.getBalance_id());
		    updateBalanceStatemenet.addBatch();
		    
		    updateBalanceStatemenet.clearParameters();
	
		}else{
			//没有找到可用账本
			String tmpbalanceId =  Common.getUUID();
			infoPayBalanceStatement.setString(1, tmpbalanceId);
			infoPayBalanceStatement.setString(2, userId);
			infoPayBalanceStatement.setInt(3, balanceType);
			infoPayBalanceStatement.setInt(4, (int)bValue);
			infoPayBalanceStatement.setString(5, effDate);
			infoPayBalanceStatement.setString(6, expDate);
			infoPayBalanceStatement.addBatch();
			
			infoPayBalanceStatement.clearParameters();
			
			tmpinfoPayBalance = new InfoPayBalance();
			tmpinfoPayBalance.setBalance_id(tmpbalanceId);
			tmpinfoPayBalance.setUser_id(userId);
			tmpinfoPayBalance.setBalance((int)bValue);
			tmpinfoPayBalance.setBalance_type_id(balanceType);
			tmpinfoPayBalance.setEff_date(effDate);
			tmpinfoPayBalance.setExp_date(expDate);

//			insert into info_pay_balance(balance_id, user_id , balance_type_id,balance,eff_date,exp_date)
//		    values
//		    (#{balance_id},#{user_id},#{balance_type_id},#{balance},STR_TO_DATE(#{eff_date},'%Y%m%d%H%i%s'),
//			STR_TO_DATE(#{exp_date},'%Y%m%d%H%i%s'))

			newvalue = bValue;
		}
		
		balanceAccessLogStatement.setString(1, tradeId);
		balanceAccessLogStatement.setString(2, "104");
		balanceAccessLogStatement.setString(3, userId);
		balanceAccessLogStatement.setInt(4, Integer.parseInt(yearMonth.substring(4,6)));
		balanceAccessLogStatement.setString(5, tmpinfoPayBalance.getBalance_id());
		balanceAccessLogStatement.setInt(6, balanceType);
		balanceAccessLogStatement.setString(7, "0");
		balanceAccessLogStatement.setInt(8, (int)bValue);
		balanceAccessLogStatement.setInt(9, (int)oldvalue);
		balanceAccessLogStatement.setInt(10, (int)newvalue);
		balanceAccessLogStatement.setString(11, createTime);
		balanceAccessLogStatement.addBatch();
		
		balanceAccessLogStatement.clearParameters();

		tmpinfoPayBalance.setBalance(newvalue);
		changedInfoPayBalance.add(tmpinfoPayBalance);
		
	}

	private List<InfoPayBalance> getInfoPayBalanceByUserId(String userId,Statement stmt) throws Exception  {
 
		ResultSet rs = null;
		try {
			List<InfoPayBalance> listInfoPayBalance = new ArrayList<InfoPayBalance>();
			
			long starttime = System.currentTimeMillis();
			String sql =" select balance_id,user_id,balance_type_id,balance, "
					+ " date_format(eff_date, '%Y%m%d%H%i%s') eff_date,date_format(exp_date, '%Y%m%d%H%i%s') exp_date "
					+ " from info_pay_balance where user_id= '"+ userId +"'";
				
			 rs = stmt.executeQuery(sql);
			 
			 long endtime = System.currentTimeMillis();
			 log.debug("select耗时:查询用户账户---------->"+(endtime-starttime));
			 
			while(rs.next()){
				InfoPayBalance infoPayBalance = new InfoPayBalance();
				infoPayBalance.setBalance_id(rs.getString(1));
				infoPayBalance.setUser_id(rs.getString(2));
				infoPayBalance.setBalance_type_id(rs.getInt(3));
				infoPayBalance.setBalance(rs.getLong(4));
				infoPayBalance.setEff_date(rs.getString(5));
				infoPayBalance.setExp_date(rs.getString(6));
				listInfoPayBalance.add(infoPayBalance);
			}
			
			rs.close();
			return listInfoPayBalance;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if(rs !=null ){
				rs.close();
			}
		}
	}

	private InfoUserExternalAccount getInfoUserExternalAccountByUserId(
			String userId,String currTime,Statement stmt) throws Exception {
		
		ResultSet resultSet  = null;
		try {
			InfoUserExternalAccount infoUserExternalAccount = new InfoUserExternalAccount();
			
			String sql = "select external_account_id ,user_id,jd_pin ,external_account_code "
					+ " from info_user_external_account where user_id=  '" +userId+
			    "' and date_format(eff_date, '%Y%m%d%H%i%s') <= '"+currTime+"' and '"+currTime+
			     "'   <= date_format(exp_date, '%Y%m%d%H%i%s')";
			resultSet = stmt.executeQuery(sql);
			while(resultSet.next()){
				infoUserExternalAccount.setExternal_account_id(resultSet.getString(1));
				infoUserExternalAccount.setUser_id(resultSet.getString(2));
				infoUserExternalAccount.setJd_pin(resultSet.getString(3));
				infoUserExternalAccount.setExternal_account_code(resultSet.getString(4));
			}
			
			resultSet.close();
			return infoUserExternalAccount;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if (resultSet != null){
				resultSet.close();
			}
		}
	}
 

//	private void updateRedis(List<InfoPayBalance>  listInfoPayBalance) throws Exception{
//		
//		for(InfoPayBalance tmpipb:listInfoPayBalance){
//			
//			if(tmpipb.getBalance_type_id() !=0 && tmpipb.getBalance_type_id() !=3){
//				continue;
//			}
//			
//			int retvalue = sync(tmpipb);
//			if(retvalue != 0 ){
//				//同步失败，抛异常
//				throw new BValueException(-80005,"同步Redis失败");
//			}
//		}
//		
//	}
	
//	public int sync(InfoPayBalance info) {
//		DataService<InfoPayBalanceMem> infoDataService = S.get(InfoPayBalanceMem.class);
//		
//		if(info.getBalance_type_id() !=0 && info.getBalance_type_id() !=3){
//			return 0;
//		}
//		
//		log.debug("本次要同步redis的账本="+info.toString());
//		
//		HashMap<String, InfoPayBalance> infoM = null;
//		if (info != null) {
//			// 查询数据库
//			String userId = info.getUser_id();
//			String balanceId = info.getBalance_id();
//
//			// 从redis取数据
//			InfoPayBalanceMem infoMem = infoDataService.get(userId);
//			if (infoMem != null) {
//				infoM = infoMem.getInfoMap();
//				
////TODO				if(){
////					
////				}
//				
//				
//				if (infoM != null && !infoM.isEmpty()) {
//					infoM.put(info.getBalance_id(), info);	
//					infoMem.setInfoMap(infoM);
//					int id=0;
//					try {
//						 id = infoDataService.update(infoMem);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						log.debug("更新到redis异常:");
//						e.printStackTrace();
//					}
//					
//					log.debug("redis存在该用户，更新redis返回值"+id);
//					
//					if (id !=0) {
//						log.error("更新同步失败，user_id[" + userId + "],balance_id[" + balanceId + "]");
//						return -1;
//					}
//				} else {
//					infoM = new HashMap<String, InfoPayBalance>();
//					infoM.put(balanceId, info);
//					infoMem = new InfoPayBalanceMem();
//					infoMem.setUser_id(userId);
//					infoMem.setInfoMap(infoM);
//
//					int  id = 0;
//					try {
//						id = infoDataService.update(infoMem);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						log.debug("更新到redis异常:");
//						e.printStackTrace();
//					}
//					
//					log.debug("redis存在该用户，更新redis返回值"+id);
//					if (id != 0) {
//						log.error("更新同步失败，user_id[" + userId + "],balance_id[" + balanceId + "]");
//						return -1;
//					}
//				}
//			} else {
//				infoM = new HashMap<String, InfoPayBalance>();
//				infoM.put(balanceId, info);
//				infoMem = new InfoPayBalanceMem();
//				infoMem.setUser_id(userId);
//				infoMem.setInfoMap(infoM);
//
//				String id = null ;
//				try {
//					id = (String ) infoDataService.create(infoMem);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					log.debug("创建到redis异常:");
//					e.printStackTrace();
//				}
//				
//				log.debug("redis没有该用户，更新redis返回值"+id);
//				
//				
//				if (id == null) {
//					log.error("创建同步失败，user_id[" + userId + "],balance_id[" + balanceId + "]");
//					return -1;
//				}
//			}
//
//			// 更新
////			if (infoDataService.update(infoMem) == 1) {
////				log.error("同步失败，user_id[" + userId + "],balance_id[" + balanceId + "]");
////				return -1;
////			}
//		} else {
//			log.error("同步失败，参数不能为空");
//			return -1;
//		}
//
//		return 0;
//	}
	
	


	private void updateLogTradeShoppingHis(String userId, String jdPin,
			String createTime, String tradeId, ShoppingSumInfo shoppingSumInfo,PreparedStatement logTradeShoppingHisStatement) throws Exception {

			 JSONArray jsonarray = JSONArray.fromObject(shoppingSumInfo.getItems()); 
			 log.debug("createTime =>"+createTime);

		for(int i = 0 ;i<jsonarray.size();i++){
			 ShoppingDetail tmpsd = (ShoppingDetail) JSONObject.toBean(JSONObject.fromObject(jsonarray.get(i)),ShoppingDetail.class);
			 
			 logTradeShoppingHisStatement.setString(1, tradeId); 
			 logTradeShoppingHisStatement.setString(2, userId); 
			 logTradeShoppingHisStatement.setInt(3, Integer.parseInt(yearMonth.substring(4,6)));
			 logTradeShoppingHisStatement.setString(4, tmpsd.getOrderno()); 
			 logTradeShoppingHisStatement.setString(5, "1"); 
			 logTradeShoppingHisStatement.setString(6, tmpsd.getCompletetime()); 
			 logTradeShoppingHisStatement.setString(7, tmpsd.getOrgorderno()); 
			 logTradeShoppingHisStatement.setInt(8, (int) Long.parseLong(tmpsd.getAmount()));
			 logTradeShoppingHisStatement.setInt(9, 2); 
			 logTradeShoppingHisStatement.setString(10, createTime); 
			 logTradeShoppingHisStatement.addBatch();
			 
			 logTradeShoppingHisStatement.clearParameters();
  
		}
		
	}

	private void updateLogTradeHis(String userId, String jdPin,
			String createTime, String tradeId,long balance,long overtopvalue,long orderAmount,PreparedStatement logTradeHisStatement) throws Exception {


		logTradeHisStatement.setString(1, tradeId);
		logTradeHisStatement.setString(2, "104");
		logTradeHisStatement.setString(3, "10000");
		logTradeHisStatement.setString(4, "110");
		logTradeHisStatement.setString(5, userId);
		logTradeHisStatement.setInt(6, Integer.parseInt(yearMonth.substring(4,6)));
		logTradeHisStatement.setString(7, "");
		logTradeHisStatement.setString(8, "");//第二行
		logTradeHisStatement.setInt(9, (int)orderAmount);
		logTradeHisStatement.setString(10, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		logTradeHisStatement.setString(11, "0"); //balancetypeid
		logTradeHisStatement.setString(12, "0");  //unittypeid
		logTradeHisStatement.setInt(13, (int)balance);
		logTradeHisStatement.setInt(14, 2);
		logTradeHisStatement.setString(15, createTime);//第三行
		logTradeHisStatement.setString(16, createTime);
		logTradeHisStatement.setString(17, "购物赠");
		logTradeHisStatement.setInt(18, (int)overtopvalue);
		logTradeHisStatement.addBatch();
		
		logTradeHisStatement.clearParameters();

//		 insert into log_trade_his ("
//					+ "trade_id,trade_type_code,external_system_code,channel_type, user_id,partition_id,order_no,"
//					+ "order_type,order_amount,order_completion_time,balance_type_id,unit_type_id,balance,process_tag,"
//					+ "trade_time,process_time,remark,overtop_value) "
//					+ " values ("
//					+ "?,?,?,?,?,?,?,"
//					+ "?,?,str_to_date(?, '%Y%m%d%H%i%s'),?,?,?,?"
//					+ " ,str_to_date(?, '%Y%m%d%H%i%s'),str_to_date(?, '%Y%m%d%H%i%s'),?,?)"
					
	    
//		insert into log_trade_create_user_his(trade_id,jd_pin,user_id,partition_id,process_tag)
//		values(?,?,?,?,?)
        
        
	}

 

	private long getNormalBValue(Statement stmt,long bValueb, String userId, InfoUserExternalAccount infoUserExternalAccount, List<InfoPayBalance> listInfoPayBalance,List<InfoPayBalance> oweInfoPayBalance,String gwztime) throws Exception {

		long retNormalValue =0L;
		if(infoUserExternalAccount.getExternal_account_id() == null || infoUserExternalAccount.getExternal_account_id().length() ==0){
			log.debug("没有关联京东通信账户");
			//如果没有关联,总共不能超过500B
			//先获得当前用户有多少B值
			long haveBValue =0L;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String createTime = sdf.format(new Date());
			
			for(InfoPayBalance infoPayBalance :listInfoPayBalance){
				//如果是0类型,且在有效期内
				if( infoPayBalance.getBalance_type_id() ==0 &&
						infoPayBalance.getEff_date().compareTo(createTime) <=0 && infoPayBalance.getExp_date().compareTo(createTime) >=0
						){
					haveBValue = haveBValue + infoPayBalance.getBalance();
				}
				
				//未关联的用户也是有可能B值小于0的
				if(infoPayBalance.getBalance_type_id() ==0 && infoPayBalance.getBalance() <0){
					oweInfoPayBalance.add(infoPayBalance);
				}
					
			}
			
			log.debug("user "+userId+" have bvalue==>"+haveBValue);
			if(haveBValue >= max_unrela_all){ //未绑定的用户，总封顶值改为500
				retNormalValue = 0L; 
			} else {
				retNormalValue = (bValueb+haveBValue) >=max_unrela_all? (max_unrela_all-haveBValue):bValueb;
				log.debug("get NormalValue==>"+retNormalValue);
			}

		}else{
			//如果已关联，每月不超过500B 
			String bparam=presentFlag(stmt, userId, infoUserExternalAccount,gwztime);
			if (!bparam.equals("0")) {
				max_rela_month=getBvalues(bparam);
			}
				retNormalValue = bValueb >max_rela_month ?max_rela_month:bValueb;	
			// 还要判断用户是否欠费，如果有欠费需要抵扣欠费 默认500B包含要抵扣的欠费  如果不包含还需要修改
			for(InfoPayBalance infoPayBalance :listInfoPayBalance){
				if(infoPayBalance.getBalance_type_id() ==0 && infoPayBalance.getBalance() <0){
					oweInfoPayBalance.add(infoPayBalance);
				}
			}
			
			
		}
 
		return retNormalValue;
	}

	
	//根据属性值得到封顶值
		private int getBvalues(String attrvalue){
			int bvalue=0;
			for (RuleParameters ruleParameters : listrulePara) {
				if(ruleParameters.getPara_name().equals(attrvalue)){
					bvalue=Integer.parseInt(ruleParameters.getPara_char1());
				}
			}
			log.debug("bvalue---------------------------"+bvalue);
			return bvalue;
		}
		
		
		private String presentFlag(Statement stmt, String userId,InfoUserExternalAccount infoUserExternalAccount,String gwztime)throws Exception {
			//根据用户产品、订单范围、用户范围等判断是否赠送B值
			//联通自由行  联通合约产品 电信自由行 电信自由行合约 目前只有这四个产品才赠送B值
			ResultSet resultSet  = null;
			ResultSet resultSet1  = null;
			String param="";
			String current=DateUtil.getSystemTime();
			try {
				List<InfoUserExternalAccountAttr> listattr = new ArrayList<InfoUserExternalAccountAttr>();
				String externalAccountId=infoUserExternalAccount.getExternal_account_id();
				
				String sql="select external_account_id ,user_id,jd_pin ,external_system_code ,attr_code,attr_value, "
				+"date_format(eff_date, '%Y%m%d%H%i%s') eff_date, date_format(exp_date, '%Y%m%d%H%i%s') exp_date "
                +"from info_user_external_account_attr where user_id='"+userId+"' and attr_code in ('1002','1003') "
				+"and external_account_id ='"+externalAccountId+"' and date_format(eff_date, '%Y%m%d%H%i%s')<='"+gwztime+"'"
                +"and date_format(exp_date, '%Y%m%d%H%i%s')>='"+gwztime+"'";
				
				
				String sql1="select external_account_id ,user_id,jd_pin ,external_system_code ,attr_code,attr_value, "
						+"date_format(eff_date, '%Y%m%d%H%i%s') eff_date, date_format(exp_date, '%Y%m%d%H%i%s') exp_date "
		                +"from info_user_external_account_attr where user_id='"+userId+"' and attr_code in ('1001') "
						+"and external_account_id ='"+externalAccountId+"' and date_format(eff_date, '%Y%m%d%H%i%s')<date_format(exp_date, '%Y%m%d%H%i%s')"
		                +"and date_format(exp_date, '%Y%m%d%H%i%s')>='"+current+"'";
				resultSet = stmt.executeQuery(sql);
				
				while(resultSet.next()){
					InfoUserExternalAccountAttr infoUserattr = new InfoUserExternalAccountAttr();
					infoUserattr.setExternal_account_id(resultSet.getString(1));
					infoUserattr.setUser_id(resultSet.getString(2));
					infoUserattr.setJd_pin(resultSet.getString(3));
					infoUserattr.setExternal_system_code(resultSet.getString(4));
					infoUserattr.setAttr_code(resultSet.getString(5));
					infoUserattr.setAttr_value(resultSet.getString(6));
					infoUserattr.setEff_date(resultSet.getString(7));
					infoUserattr.setExp_date(resultSet.getString(8));
					listattr.add(infoUserattr);
				}
				
				resultSet1 = stmt.executeQuery(sql1);
				while(resultSet1.next()){
					InfoUserExternalAccountAttr infoUserattr = new InfoUserExternalAccountAttr();
					infoUserattr.setExternal_account_id(resultSet1.getString(1));
					infoUserattr.setUser_id(resultSet1.getString(2));
					infoUserattr.setJd_pin(resultSet1.getString(3));
					infoUserattr.setExternal_system_code(resultSet1.getString(4));
					infoUserattr.setAttr_code(resultSet1.getString(5));
					infoUserattr.setAttr_value(resultSet1.getString(6));
					infoUserattr.setEff_date(resultSet1.getString(7));
					infoUserattr.setExp_date(resultSet1.getString(8));
					listattr.add(infoUserattr);
				}
				log.debug("listattr===="+listattr.toString());
				log.debug("属性表的有"+listattr.size()+"个--------------------");
				int isLegou=0;
				int isHeyue=0;
				int isMatch=0;
				int ownAttr=0;
				if(listattr.size()==0){
					log.debug("没有设置------");
					param="-1";//没有设置
				}else {
					for (InfoUserExternalAccountAttr tmpiueaa:listattr) {
						if (tmpiueaa.getAttr_code().equals("1001")) {
							log.debug("合约用户------------------------");
							isHeyue=1;
						}
						if (tmpiueaa.getAttr_code().equals("1002")&&tmpiueaa.getAttr_value().equals("02")) {
							isLegou=1;
							log.debug("乐购用户------------------------");
						}
					}
					if (isLegou==1&&isHeyue==1) {
						param="0";//乐购合约用户
					}
					if (isHeyue==0||isLegou==0) {
						for (InfoUserExternalAccountAttr tmpiueaa:listattr) {
							if(tmpiueaa.getAttr_code().equals("1003")){
								ownAttr=1;
								if(tmpiueaa.getAttr_value()==null){
									param="-1";
									log.debug("未设置属性用户----------------------------------");
									break;
								}else {
									for(RuleParameters ruleParameters:listrulePara){
										if (ruleParameters.getPara_name().equals(tmpiueaa.getAttr_value())) {
											isMatch=1;
											log.debug("有设置匹配封顶值-----------------------------------------"+tmpiueaa.getAttr_value());
											param=tmpiueaa.getAttr_value();//当属性设置的主产品在B封顶的规则表中存在时
										    break;
										}
									}
									if (isMatch==1) {
										break;
									}
									if (isMatch==0) {
										log.debug("没有匹配封顶值设置------------------------------");
										param="-1";//属性配置表的主产品在封顶值配置表中找不到，为默认
										break;
									}
									log.debug("主产品对应的id是"+param+"------------------------------");
								}
							}
						}
						//没有1003配置
						if (ownAttr==0) {
							log.debug("没有1003属性配置-------------------");
							param="-1";
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			} finally {
				if (resultSet != null){
					resultSet.close();
				}
			}
			//TODO 上线后需要细化，以及电信产品上线后需要补充电信产品等
			log.debug("param---------------------------"+param);
			return param;
}
	
	
	private long getOwe(long bValuea, List<InfoPayBalance> listInfoPayBalance,List<InfoPayBalance> oweInfoPayBalance) {
		// 检查是否有正常b值欠费，并把具体欠费记录到oweinfopaybalance
		long retValue = bValuea;
		for(int i=0;i<listInfoPayBalance.size();i++){
			if(listInfoPayBalance.get(i).getBalance() <0 && listInfoPayBalance.get(i).getBalance_type_id() ==0){
				//判断本次赠送的B值是否足够抵扣欠费，不足则扣光赠送值，够则把balance扣到0为止
				long deductvalue = 0L;
				if(retValue > listInfoPayBalance.get(i).getBalance()*-1){
					deductvalue = listInfoPayBalance.get(i).getBalance()*-1;
					retValue = retValue - deductvalue;
//					listInfoPayBalance.get(i).addBValue(deductvalue);
//					//更新info_pay_balance表
//				    updateBalanceOfInfoPayBalance(deductvalue,listInfoPayBalance.get(i));
				} else {
					deductvalue = retValue;
					retValue = 0;
					//更新info_pay_balance表 改为后面一起更新infopaybalance
//					listInfoPayBalance.get(i).addBValue(deductvalue);
//					updateBalanceOfInfoPayBalance(deductvalue,listInfoPayBalance.get(i));
				}
				
				oweInfoPayBalance.add(listInfoPayBalance.get(i));

			}
			
			if(retValue ==0){ //如果retValue ==0 退出循环，不再遍历用户欠费
				break;
			}
		}
		return retValue;
	}


	private long getbValuea(long sumAmount, String userId) {
		//2元1B值，向上取整
		long roundB = sumAmount /200;
		if( sumAmount > 200* roundB){
			roundB ++;
		}
		return roundB;
	}


	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	
	



	

}
