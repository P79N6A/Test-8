package com.tydic.beijing.billing.credit.memcache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.credit.memcache.dao.InfoUser4CreditMemcache;
import com.tydic.uda.service.S;

public class InfoUserImpoter implements Runnable {

	private static final Logger Log = Logger.getLogger(InfoUserImpoter.class);
	private DBInfo dBInfo = null;
	private int x;
	private int y;

	public InfoUserImpoter(DBInfo dBInfo, int x, int y) {
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
			String sql = "select user_id, tele_type, device_number, user_pwd, user_status, create_date, active_date, user_type, prepay_flag,"
					+ " local_net, develop_channel_id, product_id, proto_flag, sub_user_status, stop_date from info_user "
					+ " where mod(user_id, ?) = ?";
			Log.info("InfoUser[" + sql + "]");
			stmt = conn.prepareStatement(sql);

			stmt.setFetchSize(1024);
			stmt.setInt(1, x);
			stmt.setInt(2, y);

			ResultSet rs = stmt.executeQuery();
			InfoUser4CreditMemcache bab = new InfoUser4CreditMemcache();
			long total = 0;
			while (rs.next()) {

				bab.setUser_id(rs.getString("user_id"));

				bab.setMem_key(InfoUser4CreditMemcache.KEY_PREFIX);
				bab.setTele_type(rs.getString("tele_type"));
				bab.setDevice_number(rs.getString("device_number"));
				bab.setUser_pwd(rs.getString("user_pwd"));
				bab.setUser_status(rs.getString("user_status"));
				bab.setCreate_date(rs.getDate("create_date"));
				bab.setActive_date(rs.getDate("active_date"));
				bab.setUser_type(rs.getString("user_type"));
				bab.setPrepay_flag(rs.getString("prepay_flag"));
				bab.setLocal_net(rs.getString("local_net"));
				bab.setDevelop_channel_id(rs.getString("develop_channel_id"));
				bab.setProduct_id(rs.getString("product_id"));
				bab.setProto_flag(rs.getString("proto_flag"));
				bab.setSub_user_status(rs.getString("sub_user_status"));
				bab.setStop_date(rs.getDate("stop_date"));
				S.get(InfoUser4CreditMemcache.class).create(bab);

				total++;
				bab.clear();
			}

			if (rs != null) {
				rs.close();
				rs = null;
			}

			Log.info("InfoUserImpoter-" + x + "-" + y + "-" + total);

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
