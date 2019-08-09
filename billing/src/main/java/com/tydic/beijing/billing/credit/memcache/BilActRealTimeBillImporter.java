/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.credit.memcache;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.credit.common.BasicException;
import com.tydic.beijing.billing.credit.common.ErrorCode;
import com.tydic.beijing.billing.credit.memcache.dao.BilActRealTimeBill4CreditMemcache;
import com.tydic.uda.service.S;

/**
 * BilActRealTimeBill importer<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class BilActRealTimeBillImporter implements Runnable {
	private static final Logger Log = Logger.getLogger(BilActRealTimeBillImporter.class);
	private DBInfo dBInfo = null;
	private int x;
	private int y;

	public BilActRealTimeBillImporter(DBInfo dBInfo, int x, int y) {
		this.dBInfo = dBInfo;
		this.x = x;
		this.y = y;
	}

	@Override
	public void run() {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			ResultSet rs = null;
			conn = DriverManager.getConnection(dBInfo.getDb_url(), dBInfo.getUsername(),
					dBInfo.getPassword());
			// 账期，月份
			String partition_no = null;
			// 系统当前时间
			// Date curr_date = getSystemTime();

			// String charge_dql =
			// "select partition_no from code_acct_month where use_tag = 1 and ?"
			// + " between act_eff_date and act_exp_date";
			String charge_dql = "select partition_no from code_acct_month where use_tag = 1";

			stmt = conn.prepareStatement(charge_dql);
			// stmt.setDate(1, curr_date);
			rs = stmt.executeQuery();
			if (rs.next()) {
				partition_no = rs.getString("partition_no");
			}

			Log.info("partition_no=" + partition_no);

			if (partition_no == null) {
				throw new BasicException(ErrorCode.CHARGE_DATE_ERROR,
						"charge date error, partition_no is null");
			}

			rs.close();
			stmt.close();

			String sql = "select user_id, pay_id ,acct_month, sum(non_deduct_fee) as non_deduct_fee from bil_act_real_time_bill_"
					+ partition_no
					+ " where mod(user_id, ?) = ? and unit_type_id = 0 group by user_id, pay_id, acct_month having sum(non_deduct_fee) > 0";
			Log.info("BilActRealTimeBill[" + sql + "]");

			stmt = conn.prepareStatement(sql);

			stmt.setFetchSize(1024);
			stmt.setInt(1, x);
			stmt.setInt(2, y);

			rs = stmt.executeQuery();
			BilActRealTimeBill4CreditMemcache bab = new BilActRealTimeBill4CreditMemcache();
			long total = 0;
			while (rs.next()) {
				bab.setUser_id(rs.getString("user_id"));
				bab.setPay_id(rs.getString("pay_id"));

				bab.setMem_key(BilActRealTimeBill4CreditMemcache.KEY_PREFIX);
				bab.setAcct_month(rs.getInt("acct_month"));
				bab.setNon_deduct_fee(rs.getLong("non_deduct_fee"));
				S.get(BilActRealTimeBill4CreditMemcache.class).create(bab);

				total++;
				bab.clear();
			}

			if (rs != null) {
				rs.close();
				rs = null;
			}

			Log.info("BilActRealTimeBillImporter-" + x + "-" + y + "-" + total);

		} catch (SQLException e) {
			Log.error(e.getMessage());
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ee) {
				Log.error(ee.getMessage());
				System.exit(-1);
			}
			System.exit(-1);
		} catch (BasicException e) {
			Log.error(e);
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			Log.error(e.getMessage());
			System.exit(-1);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ee) {
				Log.error(ee.getMessage());
				System.exit(-1);
			}
		}
	}

	private Date getSystemTime() {
		// DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// for test
		return new Date(new java.util.Date().getTime());
		// return new Date();
	}
}
