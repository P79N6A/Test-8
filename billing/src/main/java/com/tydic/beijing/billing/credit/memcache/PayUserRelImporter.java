package com.tydic.beijing.billing.credit.memcache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.credit.memcache.dao.PayUserRel4CreditMemcache;
import com.tydic.uda.service.S;

public class PayUserRelImporter implements Runnable {

	private static final Logger Log = Logger.getLogger(PayUserRelImporter.class);
	private DBInfo dBInfo = null;
	private int x;
	private int y;

	public PayUserRelImporter(DBInfo dBInfo, int x, int y) {
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
			String sql = "select pay_user_id, user_id, pay_id, latn_id, default_tag, payitem_code, paybalance_code, priority, eff_date, "
					+ "exp_date, eff_flag, limit_type, limit_valuea, limit_valueb from pay_user_rel "
					+ " where mod(user_id, ?) = ?";
			Log.info("PayUserRel[" + sql + "]");
			stmt = conn.prepareStatement(sql);

			stmt.setFetchSize(1024);
			stmt.setInt(1, x);
			stmt.setInt(2, y);

			ResultSet rs = stmt.executeQuery();
			PayUserRel4CreditMemcache bab = new PayUserRel4CreditMemcache();
			long total = 0;
			while (rs.next()) {

				bab.setPay_user_id(rs.getString("pay_user_id"));

				bab.setUser_id(rs.getString("user_id"));
				bab.setMem_key(PayUserRel4CreditMemcache.KEY_PREFIX);
				bab.setPay_id(rs.getString("pay_id"));
				bab.setLatn_id(rs.getLong("latn_id"));
				bab.setDefault_tag(rs.getString("default_tag"));
				bab.setPayitem_code(rs.getInt("payitem_code"));
				bab.setPaybalance_code(rs.getInt("paybalance_code"));
				bab.setPriority(rs.getInt("priority"));
				bab.setEff_date(rs.getDate("eff_date"));
				bab.setExp_date(rs.getDate("exp_date"));
				bab.setEff_flag(rs.getString("eff_flag"));
				bab.setLimit_type(rs.getString("limit_type"));
				bab.setLimit_valuea(rs.getLong("limit_valuea"));
				bab.setLimit_valueb(rs.getLong("limit_valueb"));

				S.get(PayUserRel4CreditMemcache.class).create(bab);

				total++;
				bab.clear();
			}

			if (rs != null) {
				rs.close();
				rs = null;
			}

			Log.info("PayUserRelImporter-" + x + "-" + y + "-" + total);

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
