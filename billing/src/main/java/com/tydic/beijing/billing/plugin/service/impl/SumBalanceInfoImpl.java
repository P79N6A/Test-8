package com.tydic.beijing.billing.plugin.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.account.core.Dao2File;
import com.tydic.beijing.billing.account.type.MyApplicationContextUtil;
import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.plugin.biz.SumInfo;
import com.tydic.beijing.billing.plugin.service.SumBalanceInfo;

public class SumBalanceInfoImpl extends MyApplicationContextUtil implements SumBalanceInfo {

	private final static Logger LOGGER = Logger
			.getLogger(SumBalanceInfoImpl.class);

	private SumInfo sum;

	public SumInfo getSum() {
		return sum;
	}

	public void setSum(SumInfo sum) {
		this.sum = sum;
	}

	@Override
	public void sumBalanceInfo() {
		sum = (SumInfo) mycontext.getBean("Sum");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		/*
		String sql100 = "select a.servid, b.pay_id, a.balanceinfo, a.partition_no from cdr_cdr100_12 a, pay_user_rel b where a.servid = b.user_id and a.balanceinfo is not null";
		String sql200 = "select a.servid, b.pay_id, a.balanceinfo, a.partition_no from cdr_cdr200_12 a, pay_user_rel b where a.servid = b.user_id and a.balanceinfo is not null";
		String sql300 = "select a.servid, b.pay_id, a.balanceinfo, a.partition_no from cdr_cdr300_12 a, pay_user_rel b where a.servid = b.user_id and a.balanceinfo is not null";
		
		String url = "jdbc:oracle:thin:@172.168.1.246:1521:bssgx";
		String userName = "newbilling";
		String passWord = "newbilling";
		*/
		InputStream is = Dao2File.class.getResourceAsStream("/balanceInfoplugin.properties");
		Properties prop = new Properties();
		try {
			prop.load(is);
		} catch (IOException e) {
			LOGGER.error("Dao2File config error ", e);
		}
		String table_name = prop.getProperty("table_name");
		String url = prop.getProperty("url");
		String userName = prop.getProperty("userName");
		String passWord = prop.getProperty("passWord");
		String sql = "select a.servid, b.pay_id, a.balanceinfo, a.partition_no from " + table_name + " a, pay_user_rel b where a.servid = b.user_id and a.balanceinfo is not null";
		
		String user_id, pay_id, balance_info, partition_no;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, userName, passWord);
			stmt = conn.createStatement();
			stmt.setFetchSize(5000);
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				user_id = rs.getString(1);
				pay_id = rs.getString(2);
				balance_info = rs.getString(3);
				partition_no = "12";
				
				LOGGER.info(user_id + " " + pay_id + " " + balance_info + " " + partition_no);
				
				sum.doSum(user_id, pay_id, balance_info, partition_no);
			}

		} catch (ClassNotFoundException e) {
			LOGGER.error("ClassNotFoundException:" + e.toString());
			e.printStackTrace();
			System.exit(-1);
		} catch (SQLException e) {
			LOGGER.error("SQLException:" + e.toString());
			e.printStackTrace();
			System.exit(-1);
		} catch (BasicException e) {
			LOGGER.error("BasicException:" + e.toString());
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
