package com.tydic.beijing.billing.credit.memcache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.credit.memcache.dao.CreditActionMemcache;
import com.tydic.uda.service.S;

public class CreditActionImporter implements Runnable {
	private static final Logger Log = Logger.getLogger(CreditActionImporter.class);
	private DBInfo dBInfo = null;
	private int x;
	private int y;

	public CreditActionImporter(DBInfo dBInfo, int x, int y) {
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
			String sql = "select id, user_id, pay_id, reason, to_char(action_time, 'yyyymmddhh24miss') as action_time, local_net from credit_action "
					+ "where mod(user_id, ?) = ?";
			Log.info("CreditAction[" + sql + "]");
			stmt = conn.prepareStatement(sql);

			stmt.setFetchSize(1024);
			stmt.setInt(1, x);
			stmt.setInt(2, y);

			ResultSet rs = stmt.executeQuery();
			CreditActionMemcache bab = new CreditActionMemcache();
			long total = 0;
			while (rs.next()) {
				bab.setId(rs.getLong("id"));
				bab.setUser_id(rs.getString("user_id"));
				bab.setPay_id(rs.getString("pay_id"));
				bab.setReason(rs.getString("reason"));
				bab.setMem_key(CreditActionMemcache.KEY_PREFIX);
				bab.setAction_time(rs.getString("action_time"));
				bab.setLocal_net(rs.getString("local_net"));

				S.get(CreditActionMemcache.class).create(bab);

				total++;
				bab.clear();
			}

			if (rs != null) {
				rs.close();
				rs = null;
			}

			Log.info("CreditActionImporter-" + x + "-" + y + "-" + total);

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
