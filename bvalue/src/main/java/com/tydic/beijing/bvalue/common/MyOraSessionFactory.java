package com.tydic.beijing.bvalue.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;


public class MyOraSessionFactory   {
	
	private String user;
	private String pwd;
	private String host;
	private String port;
	private String dbname;
	private Connection conn;
	private String url;
	
	private static Logger log=Logger.getLogger(MyOraSessionFactory.class);
	
	public Connection getConnection(String syncRedis) {
		
		//String url = "jdbc:mysql://"+host+":"+port+"/"+dbname+"?user="+user+"&password="+pwd+"&useUnicode=true&&characterEncoding=gb2312&autoReconnect=true";
		//url = "jdbc:Oracle:thin:@"+host+":"+port+":"+dbname;  
		
		if(syncRedis.equals("REAL")){
			url ="jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=172.17.27.141)(PORT=1523))(ADDRESS=(PROTOCOL=TCP)(HOST=172.17.27.142)(PORT=1523))(LOAD_BALANCE=no)(FAILOVER=on)(CONNECT_DATA=(SERVICE_NAME=rbsrv1)))";
		}else if (syncRedis.equals("YFB")){
			url =  "jdbc:oracle:thin:@172.22.145.110:1521:issu";  
		}else if (syncRedis.equals("TEST")){
			url =  "jdbc:Oracle:thin:@192.168.177.20:1521:bsstest";  
		}else if (syncRedis.equals("DIC")){
			url =  "jdbc:oracle:thin:@172.168.1.246:1521:bssgx";  
		}else if (syncRedis.equals("DICTEST")){
			url =  "jdbc:oracle:thin:@172.168.1.211:1621:jdtest";  
		}else if (syncRedis.equals("LFREAL")){
			url ="jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=172.20.151.103)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=172.20.151.104)(PORT=1521))(LOAD_BALANCE=no)(FAILOVER=on)(CONNECT_DATA=(SERVICE_NAME=rb)))";
		}else if (syncRedis.equals("MJQREAL")){
			url ="jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=172.22.145.110)(PORT=1521))(ADDRESS=(PROTOCOL=TCP))(LOAD_BALANCE=no)(FAILOVER=on)(CONNECT_DATA=(SERVICE_NAME=issu)))";
		}else{
			
		}
		 
 
		   
		log.debug("获得oracle连接url==>"+url);
		try {
			if(conn == null || conn.isClosed()){
				Class.forName("oracle.jdbc.driver.OracleDriver"); //加载oracle驱动
				conn = DriverManager.getConnection(url, user, pwd);  
				conn.setAutoCommit(false);
			}
		} catch (SQLException e) {
			log.debug("获取connection异常");
			e.printStackTrace();			
		} catch (ClassNotFoundException e) {
			log.debug("加载oracle驱动异常");
			e.printStackTrace();
		}
		
		return conn;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
}
