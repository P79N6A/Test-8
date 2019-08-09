package com.tydic.beijing.bvalue.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;


public class MySessionFactory   {
	
	private String user;
	private String pwd;
	private String host;
	private String port;
	private String dbname;
	private Connection conn;
	
	private static Logger log=Logger.getLogger(MySessionFactory.class);
	
	public Connection getConnection() {
		
		String url = "jdbc:mysql://"+host+":"+port+"/"+dbname+"?user="+user+"&password="+pwd+"&useUnicode=true&&characterEncoding=utf8&autoReconnect=true";
		log.debug("获得mysql连接url==>"+url);
		try {
			if(conn == null || conn.isClosed()){
				Class.forName("com.mysql.jdbc.Driver"); //加载mysq驱动
				conn= DriverManager.getConnection(url);
				conn.setAutoCommit(false);
			}
		} catch (SQLException e) {
			log.debug("获取connection异常");
			e.printStackTrace();			
		} catch (ClassNotFoundException e) {
			log.debug("加载mysql驱动异常");
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
