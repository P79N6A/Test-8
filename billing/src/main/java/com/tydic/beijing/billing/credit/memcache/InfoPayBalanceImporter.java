/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.credit.memcache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.credit.memcache.dao.InfoPayBalance4CreditMemcache;
import com.tydic.uda.service.S;

/**
 * importer<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class InfoPayBalanceImporter implements Runnable {
	private static final Logger Log = Logger.getLogger(InfoPayBalanceImporter.class);
	private DBInfo dBInfo = null;
	private int x;
	private int y;

	public InfoPayBalanceImporter(DBInfo dBInfo, int x, int y) {
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
			String sql = "select balance_id, pay_id, balance_type_id, balance, real_balance, used_balance, latn_id, eff_date, "
					+ "exp_date, local_net from info_pay_balance " + " where mod(pay_id, ?) = ?";
			Log.info("InfoPayBalance[" + sql + "]");
			stmt = conn.prepareStatement(sql);

			stmt.setFetchSize(1024);
			stmt.setInt(1, x);
			stmt.setInt(2, y);

			ResultSet rs = stmt.executeQuery();
			InfoPayBalance4CreditMemcache bab = new InfoPayBalance4CreditMemcache();
			long total = 0;
			while (rs.next()) {
				bab.setBalance_id(rs.getLong("balance_id"));
				bab.setPay_id(rs.getString("pay_id"));

				bab.setMem_key(InfoPayBalance4CreditMemcache.KEY_PREFIX);
				bab.setBalance_type_id(rs.getInt("balance_type_id"));
				bab.setBalance(rs.getLong("balance"));
				bab.setReal_balance(rs.getLong("real_balance"));
				bab.setUsed_balance(rs.getLong("used_balance"));
				bab.setLatn_id(rs.getInt("latn_id"));
				bab.setEff_date(rs.getDate("eff_date"));
				bab.setExp_date(rs.getDate("exp_date"));
				bab.setLocal_net(rs.getString("local_net"));

				S.get(InfoPayBalance4CreditMemcache.class).create(bab);

				total++;
				bab.clear();
			}

			if (rs != null) {
				rs.close();
				rs = null;
			}

			Log.info("InfoPayBalanceImporter-" + x + "-" + y + "-" + total);

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
