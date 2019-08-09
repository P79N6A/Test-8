package com.tydic.beijing.billing.credit.memcache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.credit.memcache.dao.BilActBill4CreditMemcache;
import com.tydic.uda.service.S;

public class BilActBillImporter implements Runnable {

	private static final Logger Log = Logger.getLogger(BilActBillImporter.class);
	private DBInfo dBInfo = null;
	private int x;
	private int y;

	public BilActBillImporter(DBInfo dBInfo, int x, int y) {
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
			conn = DriverManager.getConnection(dBInfo.getDb_url(), dBInfo.getUsername(),
					dBInfo.getPassword());
			String sql = "select user_id, pay_id, sum(owe_fee) as owe_fee from bil_act_bill "
					+ " where mod(user_id, ?) = ? group by user_id, pay_id having sum(owe_fee) > 0";
			Log.info("BilActBill[" + sql + "]");
			stmt = conn.prepareStatement(sql);

			stmt.setFetchSize(1024);
			stmt.setInt(1, x);
			stmt.setInt(2, y);

			ResultSet rs = stmt.executeQuery();
			BilActBill4CreditMemcache bab = new BilActBill4CreditMemcache();
			long total = 0;
			while (rs.next()) {

				bab.setUser_id(rs.getString("user_id"));
				bab.setPay_id(rs.getString("pay_id"));
				bab.setMem_key(BilActBill4CreditMemcache.KEY_PREFIX);
				bab.setOwe_fee(rs.getLong("owe_fee"));
				S.get(BilActBill4CreditMemcache.class).create(bab);

				total++;
				bab.clear();
			}

			if (rs != null) {
				rs.close();
				rs = null;
			}

			Log.info("BilActBillImporter-" + x + "-" + y + "-" + total);

		} catch (Exception e) {
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
}
